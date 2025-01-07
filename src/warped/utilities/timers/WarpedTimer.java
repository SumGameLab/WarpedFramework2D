/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.timers;

public class WarpedTimer {

	private int tick = 0;
	private int delay = 60;
	
	
	public WarpedTimer(int delay) {
		this.delay = delay;
	}
	
	public boolean toUpdate() {
		tick++;
		if(tick > delay) {
			tick = 0;
			return true;
		}
		return false;
	}
	
	
}
