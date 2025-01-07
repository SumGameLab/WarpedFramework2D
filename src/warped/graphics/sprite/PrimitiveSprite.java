/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.image.BufferedImage;

public class PrimitiveSprite extends WarpedSprite {

	
	public PrimitiveSprite() {
		raster = new BufferedImage(2,2,1);
	}
	
	public PrimitiveSprite(BufferedImage image) {
		raster = image;
		//size.x  = image.getWidth();
		//size.y = image.getHeight();
	}
	public void update() {}
	
}
