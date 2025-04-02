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
	
	/*
	 * Serves as a wrapper for the JFX panel.
	 * The media player has a separate panel to the WarpedWindow.
	 * This makes it possible to play media in a separate window while the application is running.
	 *  
	 * */
	
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
        
    /**A WarpedMediaPlayer with the default parameters.
     * @author 5som3*/
	public WarpedMediaPlayer() {
		Console.ln("WarpedMediaPlayer -> construct()");
		initializePanel();
	    initializeFrame();
	    //mediaPane.setFrameSize(frame.getWidth(), frame.getHeight());
	}
	
	/**A WarpedMediaPlayer with the default parameters.
	 * @param isUserControlled - if true the media player controls will be visible in the player else the player will not have user controls. 
     * @author 5som3*/
	public WarpedMediaPlayer(boolean isUserControlled) {
		Console.ln("WarpedMediaPlayer -> construct()");
		this.isUserControlled = isUserControlled;
		initializePanel();
	    initializeFrame();
	   // mediaPane.setFrameSize(frame.getWidth(), frame.getHeight());
	}	
	
	/**Load an mp4 so that it can be played in the media player.
	 * @param path - the path of the mp4 relative to the root of wf2d root directory.
	 * @author 5som3*/
	public void loadMP4(String path) {mediaPane.loadFrameworkMedia(path);}
	
	/**Unload any loaded media
	 * @author 5som3*/
	public void unload() {mediaPane.unload();}
	
	/**Start playing a loaded mp4.
	 * @author 5som3*/
	public void play() {mediaPane.play();}
		
	/**Stop playing the loaded media and jump to the initial frame.
	 * @author 5som3*/
	public void stop() {mediaPane.stop();}
	
	/**Stop the media at the current frame.
	 * @author 5som3*/
	public void pause() {mediaPane.pause();}
	
	/**Set the volume of the media.
	 * @param volume - the volume to play the media. Domain 0.0 <= volume <= 1.0
	 * @author 5som3*/
	public void setVolume(double volume) {mediaPane.setVolume(volume);}
	
	/**Jump to a specific time in the media.
	 * @param time - the time as percentage to jump to. Domain 0.0 <= time <= 1.0.
	 * @author 5som3*/
	public void seek(double time) {mediaPane.seek(time);}
	
	/**Set if the media should being playing as soon as it is loaded.
	 * @param isAutoPlay - if true the media will begin playing once it is loaded else it will wait untill play() is called.
	 * @author 5som3*/
	public void setAutoPlay(boolean autoPlay) {mediaPane.setAutoPlay(autoPlay);}
	
	/**Set if the media should be full screened as soon as it is loaded.
	 * @param isAutoFullscreen - If true the media will be set to fullscreen borderless once it finishes loading.
	 * @author 5som3*/
	public void setAutoFullscreen(boolean autoFullcreen) {mediaPane.setAutoFullscreen(autoFullcreen);}
	
	/**Set an action to trigger when the media finishes playing.
	 * @param action - the action to execute when the media is finished.
	 * @author 5som3*/
	public void setEndOfMediaAction(WarpedAction action) {mediaPane.setEndOfMediaAction(action);}
	
	/**Set if the media can be skipped by user.
	 * @param isSkippable - if true the user can jump to the end of the media by clicking it.
	 * @author 5som3*/
	public void setSkippable(boolean isSkippable) {mediaPane.setSkippable(isSkippable);}
	
	/**Set the visibility of the media player.
	 * @param isVisible - if true the media player will be visible.
	 * @author 5som3*/
	public void setVisible(boolean isVisible) {frame.setVisible(isVisible);}
	
	/**Set the size of the media player to a fixed size.
	 * @param width - the width of the media player in pixels.
	 * @param height - the height of the media player in pixels.
	 * @implNote the window will no longer be re-sizeable by dragging the window border.
	 * @author 5som3 */
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
	
	/**Set if the window will be fullscreen.
	 * @param isFullscreen - if true the media player will be fullscreen borderless else it will be windowed and bordered.
	 * @author 5som3*/
	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		initializeFrame();
	}
	
	/**Set the width of the media player.
	 * @return int - the width of the window frame in pixels.
	 * @author 5som3*/
	public int getWidth() {return frame.getWidth();}
	
	/**Set the height of the media player.
	 * @return int - the height of the window frame in pixels.
	 * @author 5som3*/
	public int getHeight() {return frame.getHeight();}
	
	public void close() {
		mediaPane.close();
		mediaPane = null;
	
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	
		fxPanel = null;
		frame = null;
		scene = null;
	}
	
	
	/**Creates all the subclasses*/
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
	
	/**Sets up the jFrame*/
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
	
	/**Is the media player controls accessible to the user.
	 * @return boolean - true if the media player controls are visible.
	 * @author 5som3*/
	protected boolean isUserControlled() {return isUserControlled;}
	
	/**Is the media player fullscreen
	 * @return boolean - true if the media player is fullscreen.
	 * @author 5som3*/
	protected boolean isFullscreen() {return isFullscreen;}
	
	/**Swap between fullscreen and windowed.
	 * @author 5som3*/
	protected void toggleFullscreen() {
		if(isFullscreen)isFullscreen = false;
		else isFullscreen = true;
		initializeFrame();
	}
	
}
