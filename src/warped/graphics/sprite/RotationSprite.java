/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class RotationSprite extends WarpedSprite {

	private double rotation = 0.0;
	private BufferedImage unrotatedImage;
			
	private static RenderingHints aliasHint  = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private static RenderingHints alphaHint  = new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
	private static RenderingHints interpHint = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	private static RenderingHints renderHint = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	private static RenderingHints colorHint  = new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
	private static RenderingHints ditherHint = new RenderingHints(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
	
	public RotationSprite(int width, int height) {
		super(width, height);
		unrotatedImage =  new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);		
		updateGraphics();
	}
	
	public RotationSprite(BufferedImage image) {
		super(image.getWidth(), image.getHeight());
		unrotatedImage = image;
		updateGraphics();
	
	}

	/**Set the image to be rotated by this sprite
	 * @param unrotateImage - the new image for the sprite
	 * @author SomeKid*/
	public void setRotationImage(BufferedImage unrotatedImage) {
		this.unrotatedImage = unrotatedImage;
		setSize(unrotatedImage.getWidth(), unrotatedImage.getHeight());
		setRotation(rotation);
	}
	
	/**Get the current rotation for the sprite
	 * @return rotation - measured in radians as clockwise rotation from the positive horizontal axis
	 * @author SomeKid*/
	public double getRotation() {return rotation;}
	
	/**Rotate the sprite relative to its current rotation
	 * @param rotation - Must be in radians
	 * @param 		   - Measured in clockwise rotation from the current rotation
	 * @param          - can be positive or negative rotation
	 * @param		   - can be multiples of rotations i.e. 26PI, a suitable equivalent will be calculated
	 * @author SomeKid*/
	public void rotate(double rotation) {setRotation(this.rotation + rotation);}
	
	/**Set the rotation for this sprite
	 * @param rotation - Must be in radians
	 * @param 		   - measured as clockwise rotation from the positive horizontal axis
	 * @param          - can be positive or negative rotation
	 * @param		   - can be multiples of rotations i.e. 26PI, a suitable equivalent will be calculated
	 * @author SomeKid*/
	public void setRotation(double rotation) {
		if(Double.isNaN(rotation)) {
			Console.err("RotationSprite -> setRotation() -> rotation is not a number : " + rotation);
			rotation = 0.0;
		}
		this.rotation = UtilsMath.clampRadianRotation(rotation);
		updateGraphics();
	}
	

	private void updateGraphics() { 
		Graphics2D g2d = getGraphics();
		
		int rotationOriginX = (getWidth()  / 2);    //The point around which the image will be rotated, in this case the center point
		int rotationOriginY = (getHeight() / 2);
			
		AffineTransform at = new AffineTransform();
		at.rotate(rotation, rotationOriginX, rotationOriginY);
			
		g2d.addRenderingHints(renderHint);
		g2d.addRenderingHints(colorHint);
		g2d.addRenderingHints(aliasHint);
		g2d.addRenderingHints(interpHint);
		g2d.addRenderingHints(alphaHint);
		g2d.addRenderingHints(ditherHint);
		g2d.drawRenderedImage(unrotatedImage, at);
		g2d.dispose();
		
		pushGraphics();		
	}
		
}

