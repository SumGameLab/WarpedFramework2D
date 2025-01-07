/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.object;

import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.user.mouse.WarpedMouseEvent;

public class WarpedVoidObject extends WarpedObject {

	public WarpedVoidObject() {
		raster = new BufferedImage(1,1, WarpedProperties.BUFFERED_IMAGE_TYPE);
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
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}
	
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

protected void updateMid() {return;};
	
	protected void updateSlow() {return;}; 

	protected void updatePassive() {return;};
}
