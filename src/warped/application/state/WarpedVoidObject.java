/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import warped.graphics.sprite.WarpedSprite;
import warped.user.mouse.WarpedMouseEvent;

public class WarpedVoidObject extends WarpedObject {

	public WarpedVoidObject() {
		sprite = new WarpedSprite(1,1);
	}
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


	
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	@Override
	public void updateObject() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateMid() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateSlow() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updatePassive() {
		// TODO Auto-generated method stub
		
	}

	
}
