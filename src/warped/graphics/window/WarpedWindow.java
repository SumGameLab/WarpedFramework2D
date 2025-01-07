/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import javafx.embed.swing.JFXPanel;
import warped.WarpedFramework2D;
import warped.WarpedProperties;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.WarpedUserInput;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsString;

public class WarpedWindow extends Canvas {
	private static final long serialVersionUID = 1L;
	/** Notes : 
	 *  Never use (WarpedScreen.WIDTH/HEIGHT) that is Canvas width / height, instead use WarpedScreen.width/height  
	 *
	 	-------- WarpedScreen
	 *	WarpedScreen has two primary responsibilities
	 *	The foremost is to draw the ViewPorts onto the canvas
	 *	The second is to dispatch mouse events to the ViewPorts
	 *
	 *	--- ViewPorts
	 *	Each ViewPort is responsible for preparing its own raster.
	 *	WarpedScreen only needs to get the raster from each viewport and draw them according to the stack.	 	 
	 *	Each ViewPort is drawn on top of the previous according to the order of the ViewPortStack.
	 *
	 *	---  MouseEvents
	 *	When a mouse event occurs the mouse thread will call WarpedScreen's MouseEvent() function.
	 *	The MouseEvent() will send a copy of the WarpedMouseEvent to any of the ViewPorts that contain the event, are visible and interactive.
	 *	
	 *  When a ViewPort receives a mouse event it will set the MouseEvents trace so that the mouse position relative to the ViewPort can be accessed.
	 *  Each ViewPort must have its own copy of the mouse event so difference in locations of the ViewPorts can be accounted for.
	 *  The ViewPort then repeats a similar process for each of its layers;
	 *  creating a copy of the mouse event and passing it to any ViewPortLayer that contains the event, is visible and interactive.
	 *
	 *	Each ViewPortLayer (VPL) Contains a pointer to the most recent MouseEvent.
	 *  Any new mouse events that occur in the VPL will override the old one, even if the old event was never handled.
	 *  i.e. if there are 10 mouse events generated in the less than 1 frame only the most recent mouse event will be processed.
	 *  
	 * */ 
	
	private static final Vec2i MIN_WINDOW_RESOLUTION = new Vec2i(400, 300);
	private static final Vec2i MAX_WINDOW_RESOLUTION = new Vec2i(3860, 2140);
	
	private static final Vec2i MIN_APPLICATION_RESOLUTION = new Vec2i(320, 200);
	private static final Vec2i MAX_APPLICATION_RESOLUTION = new Vec2i(1920, 1080);
	
	private static Vec2i applicationResolution = new Vec2i(MIN_APPLICATION_RESOLUTION.x, MIN_APPLICATION_RESOLUTION.y); //Resolution of the application, not the displayed resolution
	private static Vec2i windowResolution = new Vec2i(MIN_APPLICATION_RESOLUTION.x, MIN_APPLICATION_RESOLUTION.y);//Resolution of the window, the displayed resolution
	private static Vec2d windowScale = new Vec2d(1.0, 1.0);
	private static Vec2d mouseScale = new Vec2d(1.0, 1.0);
	
	private static String windowName = "WarpedFramework2D";
	private static long updateDuration;
	private static double loadProgress = 0.0;
	private static boolean isFullScreen = false;
	
	private static ScheduledExecutorService executor;
	private static Timer loadTimer = new Timer();
	private static TimerTask updateLoadGraphics = new TimerTask(){public void run() {renderLoadscreen();}};
	private static WindowListener stopListener = new WindowAdapter() {@Override public void windowClosing(WindowEvent e) {WarpedFramework2D.stop();}};	
	
	public static  BufferedImage frameIcon = UtilsImage.loadBufferedImage("res/framework/graphics/frame_icon.png");
	private static JFrame frame;
	
	private static JFXPanel fxPanel = new JFXPanel();
	
	private static Graphics2D bufferGraphics;
	private static Graphics bsGraphics;
	private static final int BUFFER_SIZE = 3;

	/**The Window width and height*/
	public static int width  = 1920; 
	public static int height = 1080; 
	public static Vec2i center = new Vec2i(width / 2, height / 2);
			
	private static WarpedViewport[] viewPorts = new WarpedViewport[1];
	private static BufferedImage[] rasterBuffer = new BufferedImage[1];
	private static int rasterIndex = 0;

	private static int   loadBarBorderThickness = 5;
	private static Vec2i loadBarSize = new Vec2i((int)(WarpedWindow.width - 200), 50);
	private static Vec2i loadBarPosition = new Vec2i(100, WarpedWindow.height - 100);
	private static Font  font = new Font("FunnyFlont", Font.PLAIN, 20);
	private static BufferStrategy bs;
	
	private static Object[] renderHints = new Object[6];
	
	public static final int RENDERING 			   = 0;
	public static final int COLOR                  = 1;
	public static final int ANTIALIASING		   = 2;
	public static final int INTERPOLATION          = 3;
	public static final int ALPHA_INTERPOLATION    = 4;
	public static final int DITHERING              = 5;
		
	public WarpedWindow(String windowName, int applicationWidth, int applicationHeight, String iconPath) {
		Console.ln("WarpedWindow -> Creating window with Settings : ");
		Console.ln("WarpedWindow -> Name  : " + windowName);
		Console.ln("WarpedWindow -> Width  : " + applicationWidth);
		Console.ln("WarpedWindow -> Height : " + applicationHeight);
		System.setProperty("sun.java2d.opengl", "True");

		renderHints[RENDERING] 			 = RenderingHints.VALUE_RENDER_SPEED;
		renderHints[ANTIALIASING] 		 = RenderingHints.VALUE_ANTIALIAS_OFF;
		renderHints[COLOR] 				 = RenderingHints.VALUE_COLOR_RENDER_SPEED;
		renderHints[INTERPOLATION] 		 = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
		renderHints[ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
		renderHints[DITHERING]			 = RenderingHints.VALUE_DITHER_DISABLE;
		
		WarpedWindow.windowName = windowName;
		WarpedWindow.width  = applicationWidth;
		WarpedWindow.height = applicationHeight;
		WarpedWindow.applicationResolution.set(width, height);
		WarpedWindow.windowResolution.set(width, height);
		Dimension size = new Dimension(WarpedWindow.width, WarpedWindow.height);
		setPreferredSize(size);
		center.set(applicationWidth / 2, applicationHeight / 2);
		
		if(iconPath != null) frameIcon = UtilsImage.loadBufferedImage(iconPath);
		
		rasterBuffer = new BufferedImage[BUFFER_SIZE];
		for(int i = 0; i < BUFFER_SIZE; i++) rasterBuffer[i] = new BufferedImage(applicationWidth, applicationHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		
		
		addKeyListener(WarpedUserInput.keyboard);
		addMouseListener(WarpedUserInput.mouse);
		addMouseMotionListener(WarpedUserInput.mouse);
		addMouseWheelListener(WarpedUserInput.mouse);
		initializeFrame(true);
		
		createBufferStrategy(2);
		bs = getBufferStrategy();
        
        loadTimer.scheduleAtFixedRate(updateLoadGraphics, 0, 16);

	}
	

	
	public final void setWindowResolution(int x, int y) {
		if(x < MIN_WINDOW_RESOLUTION.x || y < MIN_WINDOW_RESOLUTION.y || x > MAX_WINDOW_RESOLUTION.x || y > MAX_WINDOW_RESOLUTION.y) {
			Console.err("WarpedWindow -> setWindowResolution() -> window resolution is out of bounds -> min : " + MIN_WINDOW_RESOLUTION.getString());
			Console.err("WarpedWindow -> setWindowResolution() -> window resolution is out of bounds -> max : " + MAX_WINDOW_RESOLUTION.getString());
			Console.err("WarpedWindow -> setWindowResolution() -> window resolution is out of bounds -> value : ( " + x + ", " + y + ")");
			return;
		}
		if(x == width && y == height) {
			Console.err("WarpedWindow -> setWindowResolution() -> window resolution is already ( " + x + ", " + y + ")");
			return;
		}
		
		stopExecutor();

		WarpedWindow.width = x;
		WarpedWindow.height = y;
		WarpedWindow.windowResolution.set(x, y);
		windowScale.set((double)windowResolution.x / (double)applicationResolution.x, (double)windowResolution.y / (double)applicationResolution.y);
		Console.ln("WarpedWindow -> setWindowResolution() -> setting resolution to : " + x + ", " + y);
		Console.ln("WarpedWindow -> setWindowResolution() -> setting resolution scale to : " + windowScale.getString());
		if(windowScale.x >= 1.0 || windowScale.y >= 1.0) renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
		else renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
		
		Dimension size = new Dimension(WarpedWindow.width, WarpedWindow.height);
		setPreferredSize(size);
		for(int i = 0; i < BUFFER_SIZE; i++) rasterBuffer[i] = new BufferedImage(windowResolution.x, windowResolution.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		
		if(windowScale.x != 1.0 || windowScale.y != 1.0) isFullScreen = false;
		else isFullScreen = true;
		initializeFrame(isFullScreen);
		setVisible(true);
				
	}
	
	private final void initializeFrame(boolean fullscreen) {
		stopExecutor();
		WarpedWindow.isFullScreen = fullscreen;
		JFrame nFrame = new JFrame();
		nFrame.setResizable(false);
		nFrame.setTitle(windowName);
		nFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);		
		nFrame.setUndecorated(fullscreen);
		nFrame.add(this);
		nFrame.pack();
		nFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		nFrame.addWindowListener(stopListener);
		nFrame.setLocationRelativeTo(null);
		nFrame.setIconImage(frameIcon);
		nFrame.setLocationByPlatform(false);
		nFrame.setLocation(0, 0);

		JFrame oFrame = frame;
		frame = nFrame;
		
		if(oFrame != null) {			
			oFrame.removeWindowListener(stopListener);
			oFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			oFrame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}		
		
		startExecutor();
	}
	
	public final void setVisible(boolean isVisible) {frame.setVisible(isVisible);}
	
	private static final void update() {
		long cycleStartTime = System.nanoTime();
		renderViewports();
		dispatchMouseEvent();
		updateDuration = System.nanoTime() - cycleStartTime;
	}
	
	//--------
	//---------------- Access --------
	//--------
	public void fullScreen() {
		if(isFullScreen) return;
		Console.ln("WarpedWindow -> fullscreen()");
		initializeFrame(true);
		setVisible(true);

	}
	public void setFullscreen(boolean isFullScreen) {
		if(WarpedWindow.isFullScreen == isFullScreen) {
			Console.err("WarpedWindow -> setFullscreen() -> window fullscreen is already : " + isFullScreen);
			return;
		}
		WarpedWindow.isFullScreen = isFullScreen;
		if(isFullScreen) fullScreen();
		else windowed();
	}
	
	public void windowed() {
		if(!isFullScreen) return;
		Console.ln("WarpedWindow -> windowed()");
		initializeFrame(false);
		setVisible(true);
	}
	
	public void toggleFullscreen() {
		Console.ln("WarpedWindow -> toggleFullscreen()");
		if(isFullScreen) windowed();
		else fullScreen();
	}
	
	public static long getUpdateDuration() {return updateDuration;}
	public static WarpedViewport getViewPort(int index) {
		if(index < 0 || index >= viewPorts.length) {
			Console.err("WarpedWindow -> getViewport() -> index is out of bounds : (index, length)" + index + ", " + viewPorts.length);
			return viewPorts[0];
		}
		return viewPorts[index];
	}
	public static WarpedViewport[] getViewPorts(){return viewPorts;}
	public static int getViewPortCount() { return viewPorts.length;}
	public static JFrame getFrame() {return frame;}
	public static int getBufferSize() {return BUFFER_SIZE;}
	public static Vec2d getWindowScale() {return windowScale;}
	
	//--------
	//---------------- Global Graphic Options --------
	//-------- Note : set graphics per viewport for greater level of graphics control
	public void hintRenderingQuality() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintRenderingQuality();}
	public void hintRenderingSpeed()   {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintRenderingSpeed();}
                
	public void hintColorQuality() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintColorQuality();}
	public void hintColorSpeed() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintColorSpeed();}
	            
	public void hintAntialiasingOn() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAntialiasingOn();}
	public void hintAntialiasingOff() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAntialiasingOff();}
	            
	public void hintTextAntialiasingOn() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAntialiasingOn();}
	public void hintTextAntialiasingOff() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintRenderingQuality();}
	            
	public void hintInterpolationNearestNeighbour() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintInterpolationNearestNeighbour();}
	public void hintInterpolationBilinear() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintInterpolationBilinear();}
	public void hintInterpolationBicubic() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintInterpolationBicubic();}
	            
	public void hintAlphaInterpolationQuality() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAlphaInterpolationQuality();}
	public void hintAlphaInterpolationSpeed() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAlphaInterpolationSpeed();}
	            
	public void hintStrokeNormalise() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintStrokeNormalise();}
	public void hintStrokePure() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintStrokePure();}
	            
	public void hintDitheringOn() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintDitheringOn();}
	public void hintDitheringOff() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintDitheringOff();}
	
	
	public static Graphics2D getBufferGraphics() {
		rasterIndex++;
		if(rasterIndex >= BUFFER_SIZE) rasterIndex = 0;
		return rasterBuffer[rasterIndex].createGraphics();
	}

	//--------
	//---------------- Graphics Render --------
	//--------
	private static AffineTransform at = new AffineTransform();
	public static void renderViewports() {
		bufferGraphics = getBufferGraphics();			
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.fillRect(0, 0, windowResolution.x, windowResolution.y);	

		setRenderHints(bufferGraphics);
	
		for(int i = 0; i < viewPorts.length; i++) {
			WarpedViewport port = viewPorts[i];
			if(port.isVisible()) {
				at.setTransform(windowScale.x, 0.0, 0.0, windowScale.y, port.getPosition().x * windowScale.x, port.getPosition().y * windowScale.y);
				bufferGraphics.drawRenderedImage(port.raster(), at);
			}
		}
		bufferGraphics.dispose();
		
		bsGraphics = bs.getDrawGraphics();
		bsGraphics.drawImage(rasterBuffer[rasterIndex], 0, 0, windowResolution.x, windowResolution.y, null);
		bsGraphics.dispose();
		bs.show();
	}

	private static void setRenderHints(Graphics2D g) {
		bufferGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,	   		renderHints[RENDERING]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 		renderHints[ANTIALIASING]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, 	renderHints[COLOR]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 		renderHints[INTERPOLATION]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, 			renderHints[DITHERING]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, renderHints[ALPHA_INTERPOLATION]);
	}
	

	private static void renderLoadscreen() {
		bufferGraphics = getBufferGraphics();
		if(WarpedFramework2D.getLoadProgress() > loadProgress) loadProgress += 0.004;
		if(loadProgress >= 1.0) {
			WarpedFramework2D.finishedLoading(); 
			loadProgress = 0.0; 
		}
		bufferGraphics.drawImage(FrameworkSprites.loadBackground, 0, 0, width, height, null);
		bufferGraphics.setColor(Color.GRAY);
		bufferGraphics.fillRect(loadBarPosition.x, loadBarPosition.y, loadBarSize.x, loadBarSize.y);
		bufferGraphics.setColor(Color.GREEN);
		bufferGraphics.fillRect(loadBarPosition.x + loadBarBorderThickness, loadBarPosition.y + loadBarBorderThickness, (int)((loadBarSize.x - (loadBarBorderThickness * 2)) * loadProgress), loadBarSize.y - (loadBarBorderThickness * 2));
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.setFont(font);
		bufferGraphics.drawString(UtilsString.getFunnyString(), loadBarPosition.x * 2,  loadBarPosition.y + 20 + loadBarBorderThickness * 2);
		bufferGraphics.dispose();
		
		bsGraphics = bs.getDrawGraphics();
		bsGraphics.drawImage(rasterBuffer[rasterIndex], 0, 0, width, height, null);
		bsGraphics.dispose();
		bs.show();
		
	}
	
	//--------
	//---------------- View Ports --------
	//--------
	public static void setViewPorts(List<WarpedViewport> ports) { 	 	
		Console.ln("WarpedWindow -> setViewPorts() -> seting " + ports.size() + " viewports");
		viewPorts = new WarpedViewport[ports.size()];
		updateLoadGraphics.cancel();
		loadTimer.cancel();
		updateLoadGraphics = null;
		loadTimer = null;
		for(int i = 0; i < ports.size(); i++) {
			viewPorts[i] = ports.get(i);
			Console.ln("WarpedWindow -> added viewport : " + ports.get(i).getName() + " at index " + i);
		}
		startExecutor();
	}
	
	private static void stopExecutor() {
		if(executor!= null) executor.close();
	}
	private static void startExecutor() {
		
		if(loadTimer != null) return;
		executor = Executors.newScheduledThreadPool(viewPorts.length + 2);
		executor.scheduleAtFixedRate(WarpedWindow::update, 0, 16666666, TimeUnit.NANOSECONDS);
		for(int i = 0; i < viewPorts.length; i++) {			
			executor.scheduleAtFixedRate(viewPorts[i]::update, 0, 16666666, TimeUnit.NANOSECONDS);
		}
		
		
	}
	
	public synchronized void stop() {
		Console.ln("WarpedWindow -> stop()");
		if(executor != null) executor.close();		
		else Console.err("WarpedWindow -> stop() -> executor is already null");
		closeFrame();
	}
	
	private static void closeFrame() {		
		frame.removeWindowListener(stopListener);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	
	//--------
	//---------------- Mouse --------
	//--------
	public static void MouseEvent(WarpedMouseEvent mouseEvent) {
//		mouseEvent.applywindowScale(windowScale);
		if(WarpedFramework2D.isLoading()) return;
		for(int i = viewPorts.length - 1; i >= 0; i--) {
			WarpedViewport port = viewPorts[i];
			if(port.isInteractive() && port.isVisible() && port.isHit(mouseEvent.getPointTrace())) {
				port.MouseEvent(WarpedMouseEvent.generateClone(mouseEvent));
			}
		} 
	}
	
	private static void dispatchMouseEvent() {
		if(WarpedFramework2D.isLoading()) return;
		for(int i = viewPorts.length - 1; i >= 0; i--) {
			viewPorts[i].dispatchMouseEvents();
		}
	}	
	
	
}
