/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import warped.functionalInterfaces.WarpedAction;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;


public class WarpedMediaPlayer  {	
	
	private JFrame frame;
	private JFXPanel fxPanel;
	private WarpedMediaPane mediaPane;
	private Scene scene;
	
	private VectorI fixedSize = new VectorI();
	private VectorI frameSize = new VectorI();
	
	private static final int MIN_FRAME_WIDTH  =  400;
	private static final int MIN_FRAME_HEIGHT =  300;
	private static final int MAX_FRAME_WIDTH  = 3860;
	private static final int MAX_FRAME_HEIGHT = 2140;
	
	private boolean isFixedSize = false;
	private boolean isFullscreen = false;
	private boolean isUserControlled = true;
	
	private  ComponentAdapter resizeListener = new ComponentAdapter() {
    	public void componentResized(ComponentEvent evt) {
    		//mediaPane.setFrameSize(frame.getSize().width - 16, frame.getSize().height - 38);
    		frameSize.set(frame.getWidth(), frame.getHeight());
    		fxPanel.setSize(frame.getSize().width - 16, frame.getSize().height - 38);
    	}
    };
        
	public WarpedMediaPlayer() {
		Console.ln("WarpedMediaPlayer -> construct()");
		initializePanel();
	    initializeFrame();
	    //mediaPane.setFrameSize(frame.getWidth(), frame.getHeight());
	}
	
	public WarpedMediaPlayer(boolean isUserControlled) {
		Console.ln("WarpedMediaPlayer -> construct()");
		this.isUserControlled = isUserControlled;
		initializePanel();
	    initializeFrame();
	   // mediaPane.setFrameSize(frame.getWidth(), frame.getHeight());
	}

	private void initializePanel() {		
		Console.ln("WarpedMediaPlayer -> initalizePanel()");
		fxPanel = new JFXPanel();
		mediaPane = new WarpedMediaPane(this);
		mediaPane.setBackground(Background.fill(javafx.scene.paint.Color.BLACK));
		StackPane root = new StackPane();
		root.setBackground(Background.fill(javafx.scene.paint.Color.BLACK));
		scene = new Scene(root);
		scene.setFill(javafx.scene.paint.Color.BLACK);
		scene.setRoot(mediaPane);
		fxPanel.setScene(scene);	    
		fxPanel.setBackground(Color.BLACK);
		
	}
	
	private void initializeFrame() {
		Console.ln("WarpedMediaPlayer -> initalizeFrame()");
	    JFrame nFrame = new JFrame();
	    nFrame.setBackground(Color.BLACK);
	    nFrame.setTitle("WarpedMediaPlayer");
	    nFrame.setIconImage(WarpedWindow.frameIcon);
	  
	    if(isFixedSize) nFrame.setSize(fixedSize.x(), fixedSize.y());
	    else nFrame.setSize(1280, 640);
	    
	    //mediaPane.setFrameSize(nFrame.getSize().width - 16, nFrame.getSize().height - 38);
	    fxPanel.setSize(nFrame.getSize().width - 16, nFrame.getSize().height - 38);
	    
	    nFrame.setLocation(0, 0);
	    if(isFullscreen) nFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);		
		nFrame.setUndecorated(isFullscreen);
	    nFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	    nFrame.addComponentListener(resizeListener);	    
	    nFrame.getContentPane().add(fxPanel);	    
	    
	    JFrame oFrame = frame;
	    frame = nFrame;
	    
	    if(oFrame != null) {
	    	oFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			oFrame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	    }
	    
	    frame.setVisible(true);
	}
	
	protected boolean isUserControlled() {return isUserControlled;}
	protected boolean isFullscreen() {return isFullscreen;}
	protected void toggleFullscreen() {
		if(isFullscreen)isFullscreen = false;
		else isFullscreen = true;
		initializeFrame();
	}
	public VectorI getFrameSize() {return new VectorI(frame.getWidth(), frame.getHeight());}
	
	public void loadMP4(String path) {mediaPane.loadFrameworkMedia(path);}
	public void play() {mediaPane.play();}
	public void stop() {mediaPane.stop();}
	public void pause() {mediaPane.stop();}
	public void unload() {mediaPane.unload();}
	public void setVolume(double volume) {mediaPane.setVolume(volume);}
	public void offsetMedia() {mediaPane.offsetMedia();}
	public void seek(double time) {mediaPane.seek(time);}
	public void setAutoPlay(boolean autoPlay) {mediaPane.setAutoPlay(autoPlay);}
	public void setAutoFullscreen(boolean autoFullcreen) {mediaPane.setAutoFullscreen(autoFullcreen);}
	public void setEndOfMediaAction(WarpedAction action) {mediaPane.setEndOfMediaAction(action);}
	public void setVisible(boolean isVisible) {frame.setVisible(isVisible);}
	public void setSkippable(boolean isSkippable) {mediaPane.setSkippable(isSkippable);}
	
	
	public void setFixedSize(int width, int height) {
		if(width < MIN_FRAME_WIDTH) {
			Console.err("WarpedMediaPlayer -> setFixedSize() -> width is too small, it will be set to the minimum : " + MIN_FRAME_WIDTH);
			width = MIN_FRAME_WIDTH;
		} else if(width > MAX_FRAME_WIDTH) {
			Console.err("WarpedMediaPlayer -> setFixedSize() -> width is too large, it will be set to the maximum : "  + MAX_FRAME_WIDTH);
			width = MAX_FRAME_WIDTH;
		}
			
		if(height < MIN_FRAME_HEIGHT) {
			Console.err("WarpedMediaplayer -> setFixedSize() -> height is too small, it will be set to the minimum : " + MIN_FRAME_HEIGHT);
			height = MIN_FRAME_HEIGHT;
		} else if(height > MAX_FRAME_HEIGHT) {
			Console.err("WarpedMediaPlayer -> setFrameSize() -> height is too big, it will be set to the maximum : " + MAX_FRAME_HEIGHT);
			height = MAX_FRAME_HEIGHT;
		}
		
		Console.ln("WarpedMediaPlayer -> setFixedSize() -> setting fixed size too ( " + width + ", " + height + ")");
		isFixedSize = true;
		fixedSize.set(width, height);
		frame.setSize(fixedSize.x(), fixedSize.y());
	}
	
	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		initializeFrame();
	}
	
	public int getFrameWidth() {return frame.getWidth();}
	public int getFrameHeight() {return frame.getHeight();}
	
	
}
