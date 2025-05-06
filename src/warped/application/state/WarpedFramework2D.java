/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import warped.audio.FrameworkAudio;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.window.WarpedMediaPlayer;
import warped.graphics.window.WarpedWindow;
import warped.user.WarpedUserInput;
import warped.user.mouse.WarpedMouse;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsName;

public abstract class WarpedFramework2D {	
		
	private static WarpedState state;
	private static WarpedUserInput userInput;
	private static WarpedWindow window;
	private static WarpedMediaPlayer mediaPlayer;
	private static WarpedApplication app; 
	
	private static Robot robot;
	private static TrayIcon trayIcon;
	
	private static boolean isRunning = true;
	private static boolean isLoading = true;
	private static double  loadProgress = 0.0;
	
	private static boolean isDebugging = true;
	
	public static boolean isDebuging() {return isDebugging;}
	
	/**Call This function on the first line of your main function.
	 * It will prepare and start the framework to run your application.
	 *@param windowName - The name you want to be displayed at the top of the window (when application is in window)
	 *@param gameWidth - the native resolution width in pixels.
	 *@param gameHeight - the native resolution height in pixels.
	 *@param iconPath - the path relative to src folder for the icon you want to be displayed in the top left corner of the window. If path is null or invalid the window will have default java application icon
	 *@apiNote (gameWidth / gameHeight) - The native resolution for your game (note: the game width/height can be scaled which is the windowWidth/windowHeight. gameWidth/height is the native resolution)
	 *@author 5som3*/
	public static final void startFramework(String windowName, int gameWidth, int gameHeight, String iconPath) {
		Console.ln("WarpedFramework2D -> startFramework() -> starting : ");
		Console.setLogging(true);
		FrameworkSprites.loadMediaPlayerSprites();
		
		mediaPlayer = new WarpedMediaPlayer(false);
		mediaPlayer.setFixedSize(1920, 1080);
		mediaPlayer.setVolume(0.75);
		mediaPlayer.setAutoFullscreen(true);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setSkippable(true);
		mediaPlayer.setEndOfMediaAction(() -> {
			window.setVisible(true);
			mediaPlayer.setVisible(false);
			mediaPlayer.unload();
			mediaPlayer.close();
		});
		mediaPlayer.loadMP4("/framework/graphics/warped_framework.mp4");
		
		UtilsName.initialize();
		
		FrameworkSprites.loadFrameworkSprites();
		FrameworkAudio.loadFrameworkAudio();
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		userInput = new WarpedUserInput();		
		window    = new WarpedWindow(windowName, gameWidth, gameHeight, iconPath); 
		state 	  = new WarpedState();
		
		robot.mouseMove((int)WarpedWindow.getCenterX(), (int)WarpedWindow.getCenterY());
		robot.mousePress(0);
		
		WarpedMouse.isTrapMouse = false;
		
		if(SystemTray.isSupported()) {
			SystemTray systemTray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(FrameworkSprites.trayIcon);
			PopupMenu popMenu = new PopupMenu();
			MenuItem show = new MenuItem("Show");
			show.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WarpedWindow.getFrame().setVisible(true);
				}
				
			});
			MenuItem hide = new MenuItem("Hide");
			hide.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WarpedWindow.getFrame().setVisible(false);
				}
				
			});
			MenuItem exit = new MenuItem("Exit");
			exit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WarpedFramework2D.stop();
				}
				
			});
			
			popMenu.add(show);
			popMenu.add(hide);
			popMenu.add(exit);
			trayIcon.setPopupMenu(popMenu);
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**Call this function on the second line of your main function.
	 * It will prepare and start your application.
	 * @param application -  input a new instance of your application. This can be any class that extends WarpedApplication.
	 * @author 5som3*/
	public static final void startApplication(WarpedApplication application) {		
		app = application;
		app.load();
		
		state.initializeAssemblys();
		
		isLoading = false;
		setLoadProgress(1.0);
		Console.ln("WarpedFramework -> startApplication()");
		app.startApplication();
		WarpedState.play();
		
	}
	
	/**Stop the application and close the framework.
	 * @author 5som3*/
	public static final void stop() {
		Console.ln("WarpedFramework2D -> stop() -> stopping...");
		isRunning = false;
		WarpedState.pause();

		if(trayIcon != null) {
			SystemTray.getSystemTray().remove(trayIcon);
			Console.ln("WarpedFramework2D -> stop() -> removed trayIcon");
		}
		state.stop();
		window.stop();
		
		Console.ln("WarpedFramework2D -> stop() -> stopped");
		Console.stop();
		System.exit(0);
	}
	//--------
	//---------------- Access --------
	//--------
	
	/**Is the framework running.
	 * @return boolean - true if the framework is running else false.
	 * @author 5som3*/
	public final static boolean isRunning() {return isRunning;}
	
	/**Is the framework loading.
	 * @return boolean - true if the initial framework loading is still loading else false.
	 * @author 5som3*/
	public final static boolean isLoading() {return isLoading;}
	
	/*
	public static final void finishedLoading() {
		if(isLoading == false) {
			Console.err("WarpedFramework -> finishedLoading() -> already finished loading");
			return;
		}
		isLoading = false;
		loadProgress = 0.0;
		app.startApplication();
	}
	*/
	public static final double getLoadProgress() {return loadProgress;}
	public static final void setLoadProgress(double progress) {loadProgress = UtilsMath.clamp(progress, 0.0, 1.0);}
	
	public final static Robot getRobot() {return robot;}
	
	/**Get the application state
	 * @return WarpedState - the state containing the different managers.
	 * @author 5som3*/
	public final static WarpedState getState() {return state;}
	
	/**Get the user input.
	 * @return WarpedUserInput - the user input contains the WarpedMouse and WarpedKeyboard.
	 * @author 5som3*/
	public final static WarpedUserInput getUserInput() {return userInput;}
	
	/**Get the application window.
	 * @return WarpedWindow - the window contains all the viewports that render the objects in the WarpedState.
	 * @author 5som3*/
	public final static WarpedWindow getWindow() {return window;}
	
	/**Get the application.
	 * @return WarpedApplication - the application that the framework is running.
	 * @author 5som3*/
	public final static WarpedApplication getApp() {return app;}
	
	/**Get the media player.
	 * @return WarpedMediaPlayer - can be used to display mp4's.
	 * @author 5som3*/
	public final static WarpedMediaPlayer getMediaPlayer() {return mediaPlayer;}
	
}
