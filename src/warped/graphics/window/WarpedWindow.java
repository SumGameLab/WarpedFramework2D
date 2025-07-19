/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import warped.WarpedProperties;
import warped.application.state.WarpedFramework2D;
import warped.application.state.WarpedManager;
import warped.application.state.WarpedState;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.WarpedUserInput;
import warped.utilities.WarpedThreadFactory;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;
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
	
	private static final VectorI MIN_WINDOW_RESOLUTION = new VectorI(400, 300);
	private static final VectorI MAX_WINDOW_RESOLUTION = new VectorI(3840, 2160);
	
	private static final VectorI MIN_APPLICATION_RESOLUTION = new VectorI(320, 200);
	private static final VectorI MAX_APPLICATION_RESOLUTION = new VectorI(1920, 1080);
	
	private static VectorI applicationResolution = new VectorI(MIN_APPLICATION_RESOLUTION.x(), MIN_APPLICATION_RESOLUTION.y()); //Resolution of the application, not the displayed resolution
	private static VectorI windowResolution = new VectorI(MIN_APPLICATION_RESOLUTION.x(), MIN_APPLICATION_RESOLUTION.y());//Resolution of the window, the displayed resolution
	private static VectorD windowScale = new VectorD(1.0, 1.0);

	
	private static String windowName = "WarpedFramework2D";
	private static long updateDuration;
	private static long renderDuration;
	private static double loadProgress = 0.0;
	private static boolean isFullScreen = false;
	
	private static ScheduledExecutorService executor;
	private static Timer timer = new Timer("Timer Thread : Load Timer");
	private static TimerTask updateLoadGraphics = new TimerTask(){public void run() {renderLoadscreen();}};
	//private static TimerTask renderLoadGraphics = new TimerTask(){public void run() {renderCanvas();}};
	private static WindowListener stopListener = new WindowAdapter() {@Override public void windowClosing(WindowEvent e) {WarpedFramework2D.stop();}};	
	
	public static  BufferedImage frameIcon = UtilsImage.loadBufferedImage("res/framework/graphics/frame_icon.png");
	private static JFrame frame;

	private static AffineTransform at = new AffineTransform();
	
	private static Graphics2D bufferGraphics;
	private static Graphics bsGraphics;
	protected static final int BUFFER_SIZE = 3;

	protected static File outputFolder;
	protected static String outputPath;
	protected static boolean isLoggingFrames = false;
	protected static int frameLogCount = 0;
	protected static ArrayList<BufferedImage> frameLog = new ArrayList<>(); 
	
	private static int width  = 1920; 
	private static int height = 1080; 
	private static VectorI center = new VectorI(width / 2, height / 2);
			
	private static WarpedViewport[] viewPorts = new WarpedViewport[2];
	private static BufferedImage[] rasterBuffer = new BufferedImage[1];
	
	private static BufferedImage raster = new BufferedImage(1, 1, 1);
	private static BufferedImage buffer = new BufferedImage(1, 1, 1);
	private static int bufferIndex = 0;

	private static int   loadBarBorderThickness = 5;
	private static VectorI loadBarSize = new VectorI((int)(WarpedWindow.width - 200), 50);
	private static VectorI loadBarPosition = new VectorI(100, WarpedWindow.height - 100);
	private static Font  font = new Font("FunnyFlont", Font.PLAIN, 20);
	private static BufferStrategy bs;
	
	private static Object[] renderHints = new Object[6];
		
	private static short ups = 0;
	private static short fps = 0;
	
	public enum RenderHints {
		RENDERING, 			
		COLOR,               
		ANTIALIASING,		
		INTERPOLATION,
		ALPHA_INTERPOLATION, 
		DITHERING           
	}
	
	public WarpedWindow(String windowName, int applicationWidth, int applicationHeight, String iconPath) {
		Console.ln("WarpedWindow -> Creating window with Settings : ");
		Console.ln("WarpedWindow -> Name  : " + windowName);
		Console.ln("WarpedWindow -> Width  : " + applicationWidth);
		Console.ln("WarpedWindow -> Height : " + applicationHeight);
		
		System.setProperty("sun.java2d.opengl", "True");
		
		renderHints[RenderHints.RENDERING.ordinal()] 			 = RenderingHints.VALUE_RENDER_QUALITY;
		renderHints[RenderHints.ANTIALIASING.ordinal()] 		 = RenderingHints.VALUE_ANTIALIAS_ON;
		renderHints[RenderHints.COLOR.ordinal()] 				 = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
		renderHints[RenderHints.INTERPOLATION.ordinal()] 		 = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		renderHints[RenderHints.ALPHA_INTERPOLATION.ordinal()]   = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
		renderHints[RenderHints.DITHERING.ordinal()]			 = RenderingHints.VALUE_DITHER_ENABLE;
		
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

		setBackground(Color.BLACK);
				
		try {createBufferStrategy(3, new BufferCapabilities(new ImageCapabilities(true),  new ImageCapabilities(true), BufferCapabilities.FlipContents.PRIOR));}
		catch (AWTException e) {Console.stackTrace(e);}
		bs = getBufferStrategy();

		viewPorts[0] = new WarpedViewport("Object", WarpedState.objectManager, 0, 0, width, height); 
		viewPorts[1] = new WarpedViewport("GUI", WarpedState.guiManager, 0, 0, width, height); 
				
        timer.scheduleAtFixedRate(updateLoadGraphics, 0, 16);
        //timer.scheduleAtFixedRate(renderLoadGraphics, 0, 16);
	}
	

	/**Sets the size of the WarpedWindow in pixels.
	 * @param x - The new width of the window measured in pixels.
	 * @param y - The new height of the window measured in pixels.
	 * @implNote This will destroy the old window and replace it with a new one of the specified size.
	 * @implNote Updating of the window and any viewports will be paused until the new window is ready  (usually less than 100ms)
	 * @implNote NOTE : 4k applications not supported. Rendering a window larger than 1440p is not advised. 
	 * @author SomeKid*/
	public final void setWindowResolution(int x, int y) {
		if(x < MIN_WINDOW_RESOLUTION.x() || y < MIN_WINDOW_RESOLUTION.y() || x > MAX_WINDOW_RESOLUTION.x() || y > MAX_WINDOW_RESOLUTION.y()) {
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
		windowScale.set((double)windowResolution.x() / (double)applicationResolution.x(), (double)windowResolution.y() / (double)applicationResolution.y());
		
		Console.ln("WarpedWindow -> setWindowResolution() -> setting resolution to : " + x + ", " + y);
		Console.ln("WarpedWindow -> setWindowResolution() -> setting resolution scale to : " + windowScale.getString());
		
		if(windowScale.x() >= 1.0 || windowScale.y() >= 1.0) renderHints[RenderHints.INTERPOLATION.ordinal()] = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
		else renderHints[RenderHints.INTERPOLATION.ordinal()] = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
		
		Dimension size = new Dimension(WarpedWindow.width, WarpedWindow.height);
		setPreferredSize(size);
		for(int i = 0; i < BUFFER_SIZE; i++) rasterBuffer[i] = new BufferedImage(windowResolution.x(), windowResolution.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		
	
		initializeFrame(isFullScreen);
		setVisible(true);
				
	}
	
	/**Get the scale of the window. This is the window ratio in comparison to the application resolution.
	 * @author 5som3*/
	public static VectorD getWindowScale() {return windowScale;}
	
	/**DO NOT USE - Get the number of updates since the last time this method was called.
	 * @implNote This method is used for debugging purposes. The value is displayed in the FrameworkInspector.
	 * @implNote Using this value anywhere else will invalidate the UPS count. 
	 * @author 5som3 */
	public final static short getUPS() {
		short val = ups;
		ups = 0;
		return val;		
	}
	
	/**DO NOT USE - Get the number of frames drawn since the last time this method was called.
	 * @implNote This method is used for debugging purposes. The value is displayed in the FrameworkInspector.
	 * @implNote Using this value anywhere else will invalidate the FPS count. 
	 * @author 5som3 */
	public final static short getFPS() {
		short val = fps;
		fps = 0;
		return val;		
	}
	
	/**Set the visibility of the JFrame.
	 * @apiNote You shouldn't need to call this function manually.
	 * @implNote Called automatically when constructing or resizing the window.
	 * @author SomeKid*/
	public final void setVisible(boolean isVisible) {frame.setVisible(isVisible);}
	
	/**Make the window fullscreen borderless.
	 * @apiNote If the resolution of the monitor (native res) is different from the application then the application will be stretched to fill the monitor.
	 * @apilNote This could distort the appearance of the application but it will still function predictably. 
	 * @apilNote If this is a problem you should setWindowResolution() to the native res when the application starts.
	 * @implNote NOTE : 4k applications NOT supported. Rendering a window larger than 1440p is not advised.
	 * @implNote      : For 3840 x 2160 setWindowResolution(1920, 1080) and then fullscreen(). This will stretch without distortion and still be playable.
	 * @author SomeKid*/
	public void fullScreen() {
		if(isFullScreen) return;
		Console.ln("WarpedWindow -> fullscreen()");
		initializeFrame(true);
		setVisible(true);

	}
	
	/**Make windowed with border.
	 * @apiNote If the resolution of the monitor (native res) is different from the application then the application will be stretched to fill the monitor.
	 * @apilNote This could distort the appearance of the application but it will still function predictably. 
	 * @apilNote If this is a problem you should setWindowResolution() to the native res when the application starts.
	 * @implNote NOTE : 4k applications NOT supported. Rendering a window larger than 1440p is not advised.
	 * @implNote      : For 3840 x 2160 setWindowResolution(1920, 1080) and then fullscreen(). This will stretch without distortion and still be playable.
	 * @author SomeKid*/
	public void windowed() {
		if(!isFullScreen) return;
		Console.ln("WarpedWindow -> windowed()");
		initializeFrame(false);
		setVisible(true);
	}
	
	/**Set the window mode.
	 * @param isFullscreen - if true will be 'fullscreen borderless' else 'windowed bordered'.
	 * @apiNote If the resolution of the monitor (native res) is different from the application then the application will be stretched to fill the monitor.
	 * @apilNote This could distort the appearance of the application but it will still function predictably. 
	 * @apilNote If this is a problem you should setWindowResolution() to the native res when the application starts.
	 * @implNote NOTE : 4k applications NOT supported. Rendering a window larger than 1440p is not advised.
	 * @implNote      : For 3840 x 2160 setWindowResolution(1920, 1080) and then fullscreen(). This will stretch without distortion and still be playable.
	 * @author SomeKid*/
	public void setFullscreen(boolean isFullScreen) {
		if(WarpedWindow.isFullScreen == isFullScreen) {
			Console.err("WarpedWindow -> setFullscreen() -> window fullscreen is already : " + isFullScreen);
			return;
		}
		WarpedWindow.isFullScreen = isFullScreen;
		if(isFullScreen) fullScreen();
		else windowed();
	}
	
	/**Toggle window mode between 'fullscreen borderless' and 'windowed bordered'.
	 * @apiNote If the resolution of the monitor (native res) is different from the application then the application will be stretched to fill the monitor.
	 * @apilNote This could distort the appearance of the application but it will still function predictably. 
	 * @apilNote If this is a problem you should setWindowResolution() to the native res when the application starts.
	 * @implNote NOTE : 4k applications NOT supported. Rendering a window larger than 1440p is not advised.
	 * @implNote      : For 3840 x 2160 setWindowResolution(1920, 1080) and then fullscreen(). This will stretch without distortion and still be playable.
	 * @author SomeKid*/
	public void toggleFullscreen() {
		Console.ln("WarpedWindow -> toggleFullscreen()");
		if(isFullScreen) windowed();
		else fullScreen();
	}
	
	/**Set then number of frames to log to hard drive.
	 * @param frameCount - the number of frames to log.
	 * @apiNote Frames will be logged to a new subfolder in the directory dat -> log -> windowDump
	 * @author 5som3*/
	public static void setLogFrames(int frameCount) {
		if(frameCount < 1) {
			Console.err("WarpedWindow -> setLogFrames() -> must log at least 1 frame, frame count will be set to 1.");
			frameCount = 1;
		}
		if(frameCount > 1800) {
			Console.err("WarpedWindow -> setLogFrames() -> must log at less than 1800 frames, frame count will be set to 1800.");
			frameCount = 1800;
		}
		
		File folder = new File("dat/log/graphics/window");
		if(!folder.exists()) {
			if(!folder.mkdirs()) {
				Console.err("WarpedWindow -> setLogFrames() -> failed to create graphics log folder, unable to proceede");
				return;
			} else Console.ln("WarpedWindow -> setLogFrames() -> created new graphics log folder, proceeding..");
		} else Console.ln("WarpedWindow -> setLogFrames() -> graphics log folder exist, proceeding...");
		File[] subFolders = folder.listFiles();
		File outputFolder = new File("dat/log/graphics/window/frameDump_" + subFolders.length);
		outputPath = "dat/log/graphics/window/frameDump_" + subFolders.length +"/"; 
		if(!outputFolder.mkdir()) {
			Console.err("WarpedWindow -> setLogFrames() -> failed to create graphics log subfolder " + subFolders.length);
			return;
		} else Console.ln("WarpedWindow -> setLogFrames() -> created graphics log subfolder " + subFolders.length);
		
		WarpedWindow.outputFolder = outputFolder;
		frameLogCount = frameCount;
		isLoggingFrames = true;
	}
	
	/**Get the time taken to update the window.
	 * @return long - the time taken to do the most recent update cycle measured in nanoseconds.
	 * @author SomeKid */
	public static long getUpdateDuration() {return updateDuration;}
	
	/**Get the time taken to update the window.
	 * @return long - the time taken to do the most recent update cycle measured in nanoseconds.
	 * @author SomeKid */
	public static long getRenderDuration() {return renderDuration;}
	
	/**Get one of the viewports in the window.
	 * @param index - the index of the viewport, will return viewPorts[0] if index is invalid.
	 * @author SomeKid*/
	public static WarpedViewport getViewPort(int index) {
		if(index < 0 || index >= viewPorts.length) {
			Console.err("WarpedWindow -> getViewport() -> index is out of bounds : (index, length)" + index + ", " + viewPorts.length);
			return viewPorts[0];
		}
		return viewPorts[index];
	}
	
	/**Get the array containing the viewports.
	 * @return WarpedViewport[] - the viewports
	 * @author SomeKid*/
	public static WarpedViewport[] getViewPorts(){return viewPorts;}
	
	/**Get the number of viewports in the window.
	 * @return int - the viewport count.
	 * @author SomeKid*/
	public static int getViewPortCount() { return viewPorts.length;}
	
	/**Get the JFrame that the WarpedWindow is based on.
	 * @return JFrame - The windows JFrame
	 * @apiNote You should not need to access this during application development, it is here for debug purposes.
	 * @author SomeKid*/
	public static JFrame getFrame() {return frame;}
	
	/**Get the number of buffers
	 * @return int - the buffer count.
	 * @author SomeKid*/
	public static int getBufferSize() {return BUFFER_SIZE;}
	
	/**Get the scale of the window
	 * @return VectorD - the x and y scale.
	 * @author SomeKid
	 * 
	public static VectorD getWindowScale() {return windowScale;}
	 * */
	
	/**The width of the WarpedWindow in pixels.
	 * @author 5som3*/
	public static int getWindowWidth() {return width;}
	
	/**The height of the WarpedWindow in pixels
	 * @author 5som3*/
	public static int getWindowHeight() {return height;}
	
	public static int getApplicationWidth() {return applicationResolution.x();}
	
	public static int getApplicationHeight () {return applicationResolution.y();}
	
	
	/**The center of the window.
	 * @return int - the center of the window x coordinate measured in pixels.
	 * @author 5som3*/
	public static int getCenterX() {return center.x();}
	
	/**The center of the window.
	 * @return int - the center of the window y coordinate measured in pixels.
	 * @author 5som3*/
	public static int getCenterY() {return center.y();}
	
	/**Add a viewport at the specified index.
	 * @param target - the manager to draw in the viewport.
	 * @param index - the index of the viewport. (the draw stack position).
	 * @apiNote index is effectively the viewports priority in the draw stack.
	 * @apiNote example viewports[0 -> n] where n is the number of viewports:
	 * @apiNote 0 - the first viewport drawn and the lowest priority to receive mouse events.
	 * @apiNote 1 - drawn on top of viewport[0], mouse events will check against object in viewport[1] before they are check against objects in viewport[2]. 
	 * @apiNote ...
	 * @apiNote n - the last viewport drawn on top of all others for the viewports and the highest priority to receive mouse events.
	 * @implNote WarpedFramework2D has two viewports by default, the object viewport (index 0) and the GUI viewport (index 1). 
	 * @implNote These viewports 
	 * @author 5som3*/
	public static WarpedViewport addViewport(WarpedManager<?> target, int index) {
		if(index < 0) {
			Console.err("WarpedWindow -> addViewport() -> index out of bounds : " + index);
			index = 0;
		} else if(index > viewPorts.length) {
			Console.err("WarpedWindow -> addViewport() -> index out of bounds : " + index);
			index = 0;
		}
		
		WarpedViewport[] viewports = new WarpedViewport[viewPorts.length + 1];
		viewports[index] = new WarpedViewport("Manager" + target.getClass().getSimpleName(), target); 
		for(int i = 0; i < viewPorts.length; i++) {
			if(i < index) viewports[i] = viewPorts[i];
			else viewports[i + 1] = viewPorts[i]; 
		}
		
		setViewPorts(viewports);
		return viewports[index];
	}
	
	/**Set the viewports to be displayed in the window.
	 * @param ports - the viewports to add to this window. Ports will be drawn in the order they are added from bottom to top
	 * @param       - i.e. setViewPorts(a, b, c) will be drawn : a behind b behind c
	 * @author SomeKid*/
	public static void setViewPorts(WarpedViewport... ports) { 	 	
		Console.ln("WarpedWindow -> setViewPorts() -> seting " + ports.length + " viewports");
		viewPorts = new WarpedViewport[ports.length];
		updateLoadGraphics.cancel();
		timer.cancel();
		//fxPanel = null;
		updateLoadGraphics = null;
		timer = null;
		for(int i = 0; i < ports.length; i++) {
			viewPorts[i] = ports[i];
			Console.ln("WarpedWindow -> added viewport : " + ports[i].getName() + " at index " + i);
		}
		startExecutor();
	}
	
	/**DO NOT CALL - This function is called automatically when the application closes.
	 * @implNote stops the window from updating
	 * @author SomeKid */
	public synchronized void stop() {
		Console.ln("WarpedWindow -> stop()");
		stopExecutor();
		/*
		if(executor != null) {
			executor.shutdown();
			executor.close();
		}
		else WarpedConsole.err("WarpedWindow -> stop() -> executor is already null");
		 */
		closeFrame();
	}
	
	//--------
	//---------------- Global Graphic Options --------
	//-------- Note : set graphics per viewport for greater level of graphics control
	/**Better render quality but slower.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsRenderingQuality() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintRenderingQuality();}
	public static void hintRenderingQuality() {renderHints[RenderHints.RENDERING.ordinal()] = RenderingHints.VALUE_RENDER_QUALITY;}
	/**Reduced render quality but faster.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsRenderingSpeed()   {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintRenderingSpeed();}
	public static void hintRenderingSpeed() {renderHints[RenderHints.RENDERING.ordinal()] = RenderingHints.VALUE_RENDER_SPEED;}
	/**Better colour quality but slower.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored. 
	 * @author SomeKid*/
	public static void hintViewportsColorQuality() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintColorQuality();}
	public static void hintColorQuality() {renderHints[RenderHints.COLOR.ordinal()] = RenderingHints.VALUE_COLOR_RENDER_QUALITY;}
	/**Reduced colour quality but faster.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsColorSpeed() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintColorSpeed();}
	public static void hintColorSpeed() {renderHints[RenderHints.COLOR.ordinal()] = RenderingHints.VALUE_COLOR_RENDER_SPEED;}
	/**Turn on antialiasing for images.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/            
	public static void hintViewportsAntialiasingOn() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAntialiasingOn();}
	public static void hintAntialiasingOn() {renderHints[RenderHints.ANTIALIASING.ordinal()] = RenderingHints.VALUE_ANTIALIAS_ON;}
	/**Turn off antialiasing for images.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/      
	public static void hintViewportsAntialiasingOff() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAntialiasingOff();}
	public static void hintAntialiasingOff() {renderHints[RenderHints.ANTIALIASING.ordinal()] = RenderingHints.VALUE_ANTIALIAS_OFF;}
	/**Turn on antialiasing for text.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/      
	public static void hintViewportsTextAntialiasingOn() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAntialiasingOn();}
	//public static void hintTextAntialiasingOn() {renderHints[RenderHints..ordinal()] = RenderingHints.VALUE_ANTIALIAS_ON;}
	/**Turn off antialiasing for text.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/      
	public static void hintViewportsTextAntialiasingOff() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintRenderingQuality();}
	            
	/**Scale images using NearestNeighbour algorithm - fast but low quality.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/      
	public static void hintViewportsInterpolationNearestNeighbour() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintInterpolationNearestNeighbour();}
	public static void hintInterpolationNearestNeighbour() {renderHints[RenderHints.INTERPOLATION.ordinal()] = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;}
	/**Scale images using Bilinear algorithm - better quality than NearestNeighbour but slower.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/      
	public static void hintViewportsInterpolationBilinear() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintInterpolationBilinear();}
	public static void hintInterpolationBilinear() {renderHints[RenderHints.INTERPOLATION.ordinal()] = RenderingHints.VALUE_INTERPOLATION_BILINEAR;}
	/**Scale images using Bicubic algorithm - slow but accurate scaling.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/      
	public static void hintViewportsInterpolationBicubic() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintInterpolationBicubic();}
	public static void hintInterpolationBicubic() {renderHints[RenderHints.INTERPOLATION.ordinal()] = RenderingHints.VALUE_INTERPOLATION_BICUBIC;}
	/**Best quality alpha blending, slowest
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsAlphaInterpolationQuality() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAlphaInterpolationQuality();}
	public static void hintAlphaInterpolationQuality() {renderHints[RenderHints.ALPHA_INTERPOLATION.ordinal()] = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;}
	/**Fast, reduced fidelity 
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsAlphaInterpolationSpeed() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintAlphaInterpolationSpeed();}
	public static void hintAlphaInterpolationSpeed() {renderHints[RenderHints.ALPHA_INTERPOLATION.ordinal()] = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;}
	            
	/**Not sure what this does, lol.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsStrokeNormalise() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintStrokeNormalise();}
	/**Not sure what this does, lol.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsStrokePure() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintStrokePure();}
	            
	/**Improves the quality of edges of rotated images, increases render time.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsDitheringOn() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintDitheringOn();}
	public static void hintDitheringOn() {renderHints[RenderHints.DITHERING.ordinal()] = RenderingHints.VALUE_DITHER_ENABLE;}
	/**Skip dithering, increase render time.
	 * @apiNote Applied to all viewports in the window. Each viewport can also be set individually.
	 * @apiNote If hintOverallPrimitive() this hint will be ignored.
	 * @author SomeKid*/
	public static void hintViewportsDitheringOff() {for(int i = 0; i < viewPorts.length; i++) viewPorts[i].hintDitheringOff();}
	public static void hintDitheringOff() {renderHints[RenderHints.DITHERING.ordinal()] = RenderingHints.VALUE_DITHER_DISABLE;}
	
	
	//--------
	//--------
	//--------
	
	
	/**This method is not called it is scheduled for execution when the JFrame is initialized.
	 * @author SomeKid*/
	private static final void update() {
		long cycleStartTime = System.nanoTime();
		renderViewports();
		ups++;
		updateDuration = System.nanoTime() - cycleStartTime;
	}
	
	private static void pushGraphics() {
		raster = rasterBuffer[bufferIndex];
		bufferIndex++;
		if(bufferIndex == BUFFER_SIZE) bufferIndex = 0;
		buffer = rasterBuffer[bufferIndex];
	}
	

	/**Gets the next image in the buffer and fills it with black, then draws the viewports onto the image, finally the composite of viewports is drawn into this canvas
	 * @author SomeKid*/
	private static void renderViewports() {
		bufferGraphics = getBufferGraphics();			
		setRenderHints(bufferGraphics);
			
		for(int i = 0; i < viewPorts.length; i++) {
			WarpedViewport port = viewPorts[i];
			if(port.isVisible()) {
				at.setTransform(windowScale.x(), 0.0, 0.0, windowScale.y(), port.getX(), port.getY());
				bufferGraphics.drawRenderedImage(port.raster(), at);
			}
		}
		bufferGraphics.dispose();
		
		if(isLoggingFrames) {
			frameLog.add(UtilsImage.generateClone(buffer));
			frameLogCount--;
			Console.ln("WarpedWindow -> renderViewports() -> logging frame : " + frameLog.size());
			
			if(frameLogCount < 1) {
				isLoggingFrames = false;
				Console.ln("WarpedWindow -> renderViewports() -> writing frame dump...");
				for(int i = 0; i < frameLog.size(); i++) {
					File logFrame = new File(outputPath + "frame_" + i + ".png");
					try {
						ImageIO.write(frameLog.get(i), "png", logFrame);
					} catch (IOException e) {
						Console.stackTrace(e);
					}
				}
				frameLog.clear();
			}
		}
		pushGraphics();
		
		long cycleStartTime = System.nanoTime();
		bsGraphics = bs.getDrawGraphics();
		bsGraphics.drawImage(raster, 0, 0, width, height, null);
		bsGraphics.dispose();
		bs.show();
		fps++;
		renderDuration = System.nanoTime() - cycleStartTime;
	}
	
	/**This method is not called it is scheduled when the WarpedWindow is constructed. Draws the load screen into the canvas while WarpedFramework2D is initialized.
	 * @author SomeKid*/
	private static void renderLoadscreen() {
		bufferGraphics = getBufferGraphics();
		if(WarpedFramework2D.getLoadProgress() > loadProgress) loadProgress += 0.004;
		
		if(loadProgress >= 1.0) {
			updateLoadGraphics.cancel();
			timer.cancel();
			updateLoadGraphics = null;
			timer = null;
			startExecutor();
		}
		
		bufferGraphics.drawImage(FrameworkSprites.loadBackground, 0, 0, width, height, null);
		bufferGraphics.setColor(Color.GRAY);
		bufferGraphics.fillRect(loadBarPosition.x(), loadBarPosition.y(), loadBarSize.x(), loadBarSize.y());
		bufferGraphics.setColor(Color.GREEN);
		bufferGraphics.fillRect(loadBarPosition.x() + loadBarBorderThickness, loadBarPosition.y() + loadBarBorderThickness, (int)((loadBarSize.x() - (loadBarBorderThickness * 2)) * loadProgress), loadBarSize.y() - (loadBarBorderThickness * 2));
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.setFont(font);
		bufferGraphics.drawString(UtilsString.getFunnyString(), loadBarPosition.x() * 2,  loadBarPosition.y() + 20 + loadBarBorderThickness * 2);
		bufferGraphics.dispose();
		
		pushGraphics();
		
		long cycleStartTime = System.nanoTime();
		bsGraphics = bs.getDrawGraphics();
		bsGraphics.drawImage(raster, 0, 0, width, height, null);
		bsGraphics.dispose();
		bs.show();
		fps++;
		renderDuration = System.nanoTime() - cycleStartTime;
	}
	
	/** Get the graphics for the next image in the buffer.
	 * @author SomeKid*/
	private static Graphics2D getBufferGraphics() {
		Graphics2D g2d = buffer.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		return g2d;
	}
	
	/** Sets the rendering hints for a graphics context using the hint parameters set in the WarpedWindow.
	 * @author SomeKid*/
	private static void setRenderHints(Graphics2D g) {
		bufferGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,	   		renderHints[RenderHints.RENDERING.ordinal()]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 		renderHints[RenderHints.ANTIALIASING.ordinal()]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, 	renderHints[RenderHints.COLOR.ordinal()]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 		renderHints[RenderHints.INTERPOLATION.ordinal()]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, 			renderHints[RenderHints.DITHERING.ordinal()]);
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, renderHints[RenderHints.ALPHA_INTERPOLATION.ordinal()]);
	}
		

	/** Stops the executor that updates WarpedWindow and all of its viewports.
	 * @author SomeKid*/
	private static void stopExecutor() {
		if(executor!= null) {
			executor.shutdown();
			if(executor.isTerminated()) {	//FIXME Executor doesn't shutdown properly when executed from a button, works fine when exectued by console command			
				executor.close();
			} else {
				Console.err("WarpedWindow -> stopExecutor() -> executor failed to shutdown in time!");
			}
		}
	}
	
	/** Starts the executor and schedules update tasks for the WarpedWindow and all of its viewports.
	 * @author SomeKid*/
	private static void startExecutor() {
		if(timer != null) {
			Console.err("WarpedWindow -> startExecutor() -> the load screen is still being rendered, ");
			return;
		}
		
		executor = Executors.newScheduledThreadPool(viewPorts.length + 2, new WarpedThreadFactory("Window Thread"));
		executor.scheduleAtFixedRate(WarpedWindow::update, 0, 16666666, TimeUnit.NANOSECONDS);
		executor.scheduleAtFixedRate(WarpedWindow::dispatchMouseEvent, 0, 16666666, TimeUnit.NANOSECONDS);
		
		for(int i = 0; i < viewPorts.length; i++) {			
			executor.scheduleAtFixedRate(viewPorts[i]::update, 0, 16666666, TimeUnit.NANOSECONDS);
		}		
	}
	
	/** Close the JFrame
	 * @author SomeKid*/
	private static void closeFrame() {		
		frame.removeWindowListener(stopListener);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	/**Creates a new JFrame and closes the old frame if one exists.
	 * @author SomeKid*/
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

	
	//--------
	//---------------- Mouse --------
	//--------
	/**DO NOT CALL - This function is called automatically by WarpedMouse when a mouse event occurs.
	 * @implNote Checks if the mouse event occurred in the bounds of any of the viewports and forwards the event to any relevant port
	 * @author SomeKid*/
	protected static void MouseEvent(WarpedMouseEvent mouseEvent) {
		if(WarpedFramework2D.isLoading()) return;
		
		for(int i = viewPorts.length - 1; i >= 0; i--) {
			WarpedViewport port = viewPorts[i];
			if(port.isInteractive() && port.isVisible() && isHit(port, mouseEvent.getPointTrace())) {
				port.MouseEvent(new WarpedMouseEvent(mouseEvent));
			}
		} 
	}
	
	/** Checks if a point occurred within the bounds of a viewport.
	 * @author SomeKid*/
	private static final boolean isHit(WarpedViewport port, Point trace) { // check if click is contained within the bounds of the view port
		if(trace.x < port.getX() || trace.y < port.getY() || trace.x >= port.getX() + port.getWidth() || trace.y >= port.getY() + port.getHeight()) return false;
		else return true;
	}
	
	/** Dispatches mouse events for each viewport if any exist. 
	 * @author SomeKid
	 * */
	private static void dispatchMouseEvent() {
		if(WarpedFramework2D.isLoading()) return;
		for(int i = viewPorts.length - 1; i >= 0; i--) {
			viewPorts[i].dispatchMouseEvents();
		}
	}	
	
	
	
	
	
}
