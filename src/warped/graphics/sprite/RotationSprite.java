/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class RotationSprite extends WarpedSprite {

	private double rotation = 0.0;
	private BufferedImage unrotatedImage;
			
	private AffineTransform at = new AffineTransform();
	
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
		
		at.setToRotation(rotation, rotationOriginX, rotationOriginY);
		AffineTransformOp op = new AffineTransformOp(at, rh);
		
		g2d.drawImage(op.filter(unrotatedImage, null), 0, 0, null);
		g2d.dispose();
		
		pushGraphics();		
	}
		
}

