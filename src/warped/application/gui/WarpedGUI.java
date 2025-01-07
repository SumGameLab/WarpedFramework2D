/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;


import warped.application.object.WarpedObject;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;

public abstract class WarpedGUI extends WarpedObject {

	/**to position objects within a group relative to some point; typically the title bar 
	 * If used, intended to be set when the element is initialized
	 * Used in combination with 'offsetPosition()' function*/
	protected Vec2d positionOffset = new Vec2d();
	protected boolean isLocked = false;

	
	public void setPositionOffset(Vec2d positionOffset) {this.positionOffset = positionOffset;}
	public void setPositionOffset(Vec2i positionOffset) {this.positionOffset.set(positionOffset);}
	public void setPositionOffset(double x, double y) {positionOffset.x = x; positionOffset.y = y;}
	public void offsetPosition() {position.x += positionOffset.x; position.y += positionOffset.y;}
	

	protected void updateMid() {return;};
	
	protected void updateSlow() {return;}; 

	protected void updatePassive() {return;};
	
}
