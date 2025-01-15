/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import warped.application.state.WarpedApplication;
import warped.application.state.WarpedState;
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
	
	
	
	public static void main(String[] args) {		
		startFramework("WarpedFramework2D - SomeKid", 1920, 1080, null);
		startApplication(new TestApplication());
	}
	
	
	
	public static final void startFramework(String windowName, int gameWidth, int gameHeight, String iconPath) {
		Console.ln("WarpedFramework2D -> startFramework() -> starting : ");
		Console.startLogging();
		FrameworkSprites.loadMediaPlayerSprites();
		mediaPlayer = new WarpedMediaPlayer(false);
		mediaPlayer.setFixedSize(1920, 1080);
		mediaPlayer.setAutoFullscreen(true);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setSkippable(true);
		mediaPlayer.setEndOfMediaAction(() -> {
			window.setVisible(true);
			mediaPlayer.setVisible(false);
			mediaPlayer.unload();
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
		
		robot.mouseMove((int)WarpedWindow.center.x, (int)WarpedWindow.center.y);
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
	
	public static final void startApplication(WarpedApplication application) {		
		app = application;
		app.start();
		
		state.initialize();
		UtilsName.initialize();
				
		WarpedState.play();
		finishedLoading();
	}
	
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
	
	public final static boolean isRunning() {return isRunning;}
	public final static boolean isLoading() {return isLoading;}
	
	public static final void finishedLoading() {
		if(isLoading == false) {
			Console.err("WarpedFramework -> finishedLoading() -> already finished loading");
			return;
		}
		isLoading = false;
		loadProgress = 0.0;
		app.startApplication();
	}
	public static final double getLoadProgress() {return loadProgress;}
	public static final void setLoadProgress(double progress) {loadProgress = UtilsMath.clamp(progress, 0.0, 1.0);}
	
	public final static Robot getRobot() {return robot;}
	public final static WarpedState getState() {return state;}
	public final static WarpedUserInput getUserInput() {return userInput;}
	public final static WarpedWindow getWindow() {return window;}
	public final static WarpedApplication getApp() {return app;}
	public final static WarpedMediaPlayer getMediaPlayer() {return mediaPlayer;}
	
}
