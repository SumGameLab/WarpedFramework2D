/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.animation;

import java.awt.image.BufferedImage;

import warped.graphics.sprite.AnimatedSprite;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class WarpedAnimation extends AnimatedSprite {

	private static final int MIN_FRAMES = 4;
	private static final int MAX_FRAMES = 360;
	
	private static final double MIN_ROTATION_STEP = 0.0001;
	private static final double MAX_ROTATION_STEP = 0.25;
	
	private static final double MIN_SCALE_STEP 	  = 0.001;
	private static final double MAX_SCALE_STEP    = 10.0;

	private boolean isAlive = true;
	public VectorI position = new VectorI(0, 0);
	
	public WarpedAnimation(BufferedImage[] frames, int x, int y) {
		super(frames);
		position.set(x, y);
	}
	
	public WarpedAnimation(BufferedImage[] frames, double x, double y) {
		super(frames);
		position.set((int)x, (int)y);
	}
	
	public WarpedAnimation(BufferedImage image, int frameCount) {
		super(generateFrames(image, frameCount));		
	}
	
	/**The x position
	 * @return int - the x position in pixels.
	 * @author 5som3 */
	public int x() {return position.x();}
	
	/**The y position.
	 * @return int - the y position in pixels 
	 * @author 5som3 */
	public int y() {return position.y();}
	
	/**The animation will be removed from the viewport it was added to (if any).
	 * @author 5som3*/
	public void kill() {isAlive = false;}
	
	/**Is the animation scheduled to be removed.
	 * @return isAlive - if true the animation will be removed on the next cycle.
	 * @author 5som3*/
	public boolean isAlive() {return isAlive;}
	
	/**Generate an array of buffered images. Each entry in the array is a clone of the specified image.
	 * @param image - the image to clone into each frame.
	 * @param frameCount - the number of frames in the array.
	 * @author 5som3*/
	public static BufferedImage[] generateFrames(BufferedImage image, int frameCount) {
		if(frameCount < MIN_FRAMES) {
			Console.ln("WarpedAnimation -> generateFramesFromImage() -> frameCount must be at least " + MIN_FRAMES);
			frameCount = MIN_FRAMES;
		}
		
		if(frameCount > MAX_FRAMES) {
			Console.ln("WarpedAnimation -> generateFramesFromImage() -> frameCount must be less than " + MAX_FRAMES);
			frameCount = MAX_FRAMES;
		}
		BufferedImage[] frames = new BufferedImage[frameCount];
		
		for(int i = 0; i < frameCount; i++) frames[i] = UtilsImage.generateClone(image);
		return frames;
	}
	
	/**Add rotation to the animation.
	 * @param radianStep - the amount to rotate each image incrementally i.e. image 1 rotates radianStep * 1, image 2 rotates radianStep * 2 etc
	 * @param antiClockwise- the direction of rotation, if true the frames will be rotated anticlockwise, else they are rotated clockwise.
	 * @author 5som3*/
	public void addFrameRotation(double radianStep, boolean antiClockwise) {
		if(frames == null) {
			Console.err("WarpedAnimation -> addFrameScale() -> must generate frames first");
			return;
		}
		if(radianStep < MIN_ROTATION_STEP) {
			Console.err("WarpedAnimation -> addFrameRotation() -> step must be at leaset " + MIN_ROTATION_STEP);
			return;
		}
		if(radianStep > MAX_ROTATION_STEP) {
			Console.err("WarpedAnimation -> addFrameRotation() -> step must be no more than " + MAX_ROTATION_STEP);
			return;
		}
		
		if(antiClockwise) radianStep *= -1.0;
		
		for(int i = 0; i < frames.length; i++) {
			frames[i] = UtilsImage.generateRotatedCloneRads(frames[i], radianStep * i);
		}
	}
	
	/**Add scale to the animation.
	 *  @param scaleStep - the amount to scale each image incrementally i.e. image 1 scales by scaleStep * 1, image 2 scales by ScaleStep * 2 etc
	 *  @param scaleDown - if true the image will be scaledDown, else it will be scaled up.
	 *  @author 5som3;*/
	public void addFrameScale(int scaleStep, boolean scaleDown) {
		if(frames == null) {
			Console.err("WarpedAnimation -> addFrameScale() -> must generate frames first");
			return;
		}
		if(scaleStep < MIN_SCALE_STEP) {
			Console.err("WarpedAnimation -> addFrameScale() -> scaleStep must be at least " + MIN_SCALE_STEP);
			return;
		}
		
		if(scaleStep > MAX_SCALE_STEP) {
			Console.err("WarpedAnimation -> addFrameScale() -> scaleStep must be no more than " + MAX_SCALE_STEP);
			return;
		}
		
		if(scaleDown) scaleStep *= -1.0;
		
		for(int i = 0; i < frames.length; i++) {
			frames[i] = UtilsImage.generateScaledClone(frames[i], scaleStep * i);
		}
	}
	
	/**Add translation to the animation.
	 * @param translationStepX - the amount to translate each image incrementally in the x axis. i.e. image 1 translates by translationStepX * 1, image 2 translates by translationStepX * 2 etc
	 * @param translationStepY - the amount to translate each image incrementally in the y axis. i.e. image 1 translates by translationStepY * 1, image 2 translates by translationStepY * 2 etc
	 * @author 5som3*/
	public void addFrameTranslation(double translationStepX, double translationStepY) {
		if(frames == null) {
			Console.err("WarpedAnimation -> addFrameScale() -> must generate frames first");
			return;
		}
		
		int frameWidth  = frames[0].getWidth();
		int frameHeight = frames[0].getHeight();
		
		if(translationStepX > frameWidth || translationStepX < -frameWidth || translationStepY > frameHeight || translationStepY < -frameHeight) {
			Console.err("WarpedAnimation -> addFrameTranslation() -> out of bounds : " + translationStepX + ", " + translationStepY);
			return;
		}
		
		for(int i = 0; i < frames.length; i++) {
			frames[i] = UtilsImage.generateTranslatedClone(frames[i], (int)(i * translationStepX), (int)(i * translationStepY));
		}
	}
	
	
}
