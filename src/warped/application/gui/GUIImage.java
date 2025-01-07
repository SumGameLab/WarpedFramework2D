/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.graphics.sprite.WarpedSprite;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;

public class GUIImage extends WarpedGUI {
	
	private boolean isFixedSize = false;
	int fixedSize = 100;
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	
	public GUIImage() {
		setRaster(new BufferedImage(1,1,WarpedProperties.BUFFERED_IMAGE_TYPE));
		ateractive();
	}

	public GUIImage(int size) {
		fixedSize = size;
		isFixedSize = true;
		ateractive();
	}
	public GUIImage(BufferedImage image) {
		setRaster(image);
		ateractive();
	}
	
	public GUIImage(WarpedSprite sprite) {
		setRaster(sprite.raster());
		ateractive();
	}
	public GUIImage(WarpedSprite sprite, Vec2d position) {
		setRaster(sprite.raster());
		this.position = position;
		ateractive();
	}
		
	public void setImage(BufferedImage img) {
		if(isFixedSize) {
			BufferedImage result = new BufferedImage(fixedSize, fixedSize, WarpedProperties.BUFFERED_IMAGE_TYPE);
			
			Vec2i drawSize = new Vec2i();
			Vec2i drawOffset = new Vec2i();
			
			int width = img.getWidth();
			int height = img.getHeight();

			double aspectRatio = width / height;
			
			if(width == height) {
				drawSize.y = fixedSize;
				drawSize.x = fixedSize;				
				
				drawOffset.x = 0;
				drawOffset.y = 0;
			} else if(width > height) {
				drawSize.x = fixedSize;
				drawSize.y = (int)(width / aspectRatio);
				
				drawOffset.x = 0;
				drawOffset.y = (height - drawSize.y) / 2;				
			} else {
				drawSize.y = fixedSize;
				drawSize.x = (int)(height * aspectRatio);
				
				drawOffset.y = 0;
				drawOffset.x = (width - drawSize.y) / 2; 
			}

			Graphics g = result.getGraphics();
			g.setColor(backgroundColor);
			g.fillRect(0, 0, fixedSize, fixedSize);
			g.drawImage(img, drawOffset.x, drawOffset.y, drawSize.x, drawSize.y, null);
			g.dispose();
			setRaster(result);
			
		} else setRaster(img);
		
	}
	
	public void setFixedSize(int size) {
		fixedSize = size;
		isFixedSize = true;
	}
	
	public void setBackgroundColor(Colour backgroundColor) {this.backgroundColor = backgroundColor.getColor();}
	public void setBackgroundColor(Color backgroundColor) {this.backgroundColor = backgroundColor;}
	
	public void fixedSize() {isFixedSize = true;}
	
	protected void mouseEntered() {return;}
	protected void mouseExited() {return;}
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	protected void updateRaster() {return;}
	protected void updateObject() {return;}
	protected void updatePosition() {return;}
	
}
