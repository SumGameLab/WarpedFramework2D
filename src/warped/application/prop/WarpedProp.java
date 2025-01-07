/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.prop;

import java.awt.image.BufferedImage;

import warped.application.object.WarpedObject;
import warped.user.mouse.WarpedMouseEvent;

public abstract class WarpedProp extends WarpedObject {	
		
	/**Props that have a duration of 0 or less will not expire*/
	public WarpedProp() {
		ateractive();
		setRaster(new BufferedImage(1, 1, 1));
	}
	public WarpedProp(BufferedImage raster) {
		ateractive();
		setRaster(raster);
	}
	
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	
}
