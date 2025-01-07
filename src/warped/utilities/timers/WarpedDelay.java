/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.timers;

public class WarpedDelay {

	private int tick = 0;
	private int delay = 120;
	
	private boolean isComplete = false;
	private boolean isStarted = false;

	public boolean isComplete() {return isComplete;}
	public boolean isStarted() {return isStarted;}
	
	
	public WarpedDelay() {}
	public WarpedDelay(int delay) {
		this.delay = delay;
	}
	
	public void start() {isStarted = true;}
	public void resetDelay() {tick = 0;}
	public void reset() {
		tick = 0; 
		isStarted = false;
		isComplete = false;
	}
	
	public void update() {
		if(isStarted) {			
			tick++;
			if(tick > delay) {
				tick = 0;
				isComplete = true;
			}
		}
	}
	
}
