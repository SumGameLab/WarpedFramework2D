/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user;

import java.util.Timer;
import java.util.TimerTask;

import warped.graphics.window.WarpedMouse;
import warped.user.keyboard.WarpedKeyboard;

public class WarpedUserInput {
	
	private static long updateDuration;
	
	private static Timer userTimer = new Timer("Timer Thread : User Input");
	private static TimerTask updateInput = new TimerTask() {
		public void run() {
			long cycleStartTime = System.nanoTime();
			mouse.update();
			updateDuration = System.nanoTime() - cycleStartTime;
		}
	};
	
	
	public static long getUpdateDuration() {return updateDuration;}
	public static WarpedKeyboard keyboard = new WarpedKeyboard();
	public static WarpedMouse mouse = new WarpedMouse();
	
	public WarpedUserInput() {
		userTimer.schedule(updateInput, 0, 100);
	}
	
}
