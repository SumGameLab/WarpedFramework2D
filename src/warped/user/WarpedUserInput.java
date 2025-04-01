/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user;

import java.util.Timer;
import java.util.TimerTask;

import warped.user.keyboard.WarpedKeyboard;
import warped.user.mouse.WarpedMouse;

public class WarpedUserInput {
	
	private static long updateDuration;
	
	private static Timer userTimer = new Timer("Timer Thread : UserInput");
	private static TimerTask updateInput = new TimerTask() {
		public void run() {
			long cycleStartTime = System.nanoTime();
			mouse.update();
			keyboard.update();
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
