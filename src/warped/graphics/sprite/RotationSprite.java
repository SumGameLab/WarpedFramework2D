/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.graphics.window.WarpedWindow;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;

public class RotationSprite extends WarpedSprite {

	private double rotation = 0.0;
	private BufferedImage unrotatedImage;
	//private int width, height;
	
	Graphics2D g2d;
		
	private static RenderingHints aliasHint  = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private static RenderingHints alphaHint  = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
	private static RenderingHints interpHint = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	private static RenderingHints renderHint = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	private static RenderingHints colorHint  = new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
	private static RenderingHints ditherHint = new RenderingHints(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
	
	public void update() {return;}

	public RotationSprite(int width, int height) {
		if(width < 0 || height < 0 || width > WarpedWindow.width || height > WarpedWindow.height) {
			Console.err("RotationSprite -> generateRotationSprite() -> invalid dimensions (width, height) :  ( " + width + ", " + height + ")");
		}
		unrotatedImage =  new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(unrotatedImage);
	}
	
	public RotationSprite(BufferedImage raster) {
		unrotatedImage = UtilsImage.generateClone(raster);
		setRaster(unrotatedImage);
	}
	
	public void setRotation(double rotation) {
		if(Double.isNaN(rotation)) {
			Console.err("RotationSprite -> setRotation() -> rotation is not a number : " + rotation);
			return;
		}

		this.rotation = UtilsMath.clampRadianRotation(rotation);
		
		if(rotation == 0) {
			setRaster(unrotatedImage);
		} else {
			int width  = unrotatedImage.getWidth();
			int height = unrotatedImage.getHeight();
		
			int rotationOriginX = (width  / 2);    //The point around which the image will be rotated, in this case the center point
			int rotationOriginY = (height / 2);
			
			AffineTransform at = new AffineTransform();
			at.rotate(rotation, rotationOriginX, rotationOriginY);
						
			BufferedImage rotated = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
			g2d = rotated.createGraphics();
			g2d.setComposite(UtilsMath.clearComposite);
			g2d.fillRect(0, 0, width, height);
			g2d.setComposite(UtilsMath.drawComposite);
			g2d.addRenderingHints(renderHint);
			g2d.addRenderingHints(colorHint);
			g2d.addRenderingHints(aliasHint);
			g2d.addRenderingHints(interpHint);
			g2d.addRenderingHints(alphaHint);
			g2d.addRenderingHints(ditherHint);
			g2d.drawRenderedImage(unrotatedImage, at);
			g2d.dispose();
			setRaster(rotated);
		}
	}
	public double getRotation() {return rotation;}
	
	public BufferedImage getUnrotatedImage() {return unrotatedImage;}
	public void setUnrotatedImge(BufferedImage unrotatedImage) {
		this.unrotatedImage = unrotatedImage;
		setRotation(rotation);
	}
}
