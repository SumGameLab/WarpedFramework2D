/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.depthFields;

import java.awt.image.BufferedImage;

import warped.graphics.sprite.PrimitiveSprite;

public class DepthFieldPrimitive extends WarpedDepthField{

	PrimitiveSprite sprite;
	
	public DepthFieldPrimitive(BufferedImage image) {
		this.sprite = new PrimitiveSprite(image);
		this.ateractive();
		setRaster(sprite.raster());
	}
	
	public DepthFieldPrimitive(PrimitiveSprite sprite) {
		this.sprite = sprite;
		this.ateractive();
		setRaster(sprite.raster());
	}
	
	protected void updateRaster() {return;}
	protected void updateObject() {return;}
	protected void updatePosition() {return;}


				
}

	

	

