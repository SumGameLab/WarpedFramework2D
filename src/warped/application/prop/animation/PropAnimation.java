/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.prop.animation;

import warped.application.prop.WarpedProp;
import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;

public class PropAnimation extends WarpedProp {


	WarpedSpriteSheet sheet;
	
	private int frame = 0;
	private int tick  = 0;
	private int delay = 2;
	
	private boolean isPersistant = false;
	private boolean isAutoRestart = false;
	private boolean isComplete = false;
	private int restartDelay = 10;
	private int restartTick = 0;
	
	private boolean isRestartQueued = false;
	
	public PropAnimation(WarpedSpriteSheet sheet, double x, double y) {
		super();
		this.sheet = sheet;
		position.set(x, y);
		setRaster(sheet.getSprite(0));
	}
	
	public PropAnimation(WarpedSpriteSheet sheet, double x, double y, boolean autoRestart) {
		super();
		frame = sheet.size();
		this.sheet = sheet;
		isPersistant = true;
		this.isAutoRestart = autoRestart;
		position.set(x, y);
		setRaster(sheet.getSprite(0));
	}

	

	@Override
	protected void updateRaster() {
		if(isComplete && !isRestartQueued) return;
		if(tick++ > delay) {
			tick = 0;
			if(frame >= sheet.size()) {
				if(isRestartQueued) {
					frame = 0;
					isRestartQueued = false;
				} else if(!isPersistant) {
					kill();
					return;					
				} else if(isAutoRestart) {
					if(restartTick++ > restartDelay) {
						frame = 0;
						isComplete = false;
					} 					
				} else isComplete = true; 
			} else {
				setRaster(sheet.getSprite(frame));
				frame++;
			}
		}
	}
	
	public int getFrame() {return frame;}
	public void restart() {isRestartQueued = true;}
	public boolean isRestartQueued() {return isRestartQueued;}
	public boolean isComplete() {return isComplete;}
	public void setComplete() {isComplete = true;}
	@Override
	protected void updateObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateMid() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateSlow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updatePassive() {
		// TODO Auto-generated method stub
		
	}

}
