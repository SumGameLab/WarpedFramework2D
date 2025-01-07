/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.animation;

import java.awt.image.BufferedImage;

import warped.user.actions.WarpedAction;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class WarpedAnimation {

	
	private static final int MIN_FRAMES = 4;
	private static final int MAX_FRAMES = 360;
	
	private static final double MIN_ROTATION_STEP = 0.0001;
	private static final double MAX_ROTATION_STEP = 0.25;
		
	private static final double MIN_SCALE_STEP 	  = 0.001;
	private static final double MAX_SCALE_STEP    = 10.0;
	
	private BufferedImage raster;
	private static BufferedImage[] frames;

	private int frame;
	
	private int tick = 0;
	private int frameDuration = 10; //duration in ticks
	
	private boolean loop = true;
	private boolean reverse = false;
	private boolean isComplete = false;
	
	private Vec2i size = new Vec2i();
	
	private WarpedAction completeAction = () -> {Console.ln("WarpedAnimation -> default completionAction()");};
	private WarpedAction completeReverseAction = () -> {Console.ln("WarpedAnimation -> default completionReverseAction()");};
	
	public WarpedAnimation(boolean loop) {
		this.loop = loop;
	}
	
	/*
	public WarpedAnimation(String folderPath) {
		if(frames == null) frames = UtilsFiles.getPNGArray(folderPath);	
		raster = frames[UtilsMath.randomInt(frames.length)];
	}
	*/
	
	
	public WarpedAnimation(BufferedImage image, int frameCount) {
		generateFrames(image, frameCount);
	}
	
	public void setReverse(boolean reverse) {this.reverse = reverse;}
	public void reverseOff() {reverse = false;}
	public void reverseOn() {reverse = true;}
	public Vec2i getSize() {return size;}
	public void setCompletionAction(WarpedAction completionAction) {this.completeAction = completionAction;}
	
	public void setFrameDuration(int frameDuration) {
		if(frameDuration < 0) {
			Console.err("WarpedAnimation -> setFrameDuration() -> frameDuration can't be less than 0");
			frameDuration = 0;
		}
		this.frameDuration = frameDuration;
	}
	
	/**@return if true raster has changed and should set new graphics*/
	public boolean update() {
		if(!loop && isComplete) return false;
		
		tick++;
		if(tick > frameDuration) {
			tick = 0;
			
			if(reverse) frame--; else frame++;

			if(frame >= frames.length) {
				if(loop) frame = 0;
				else {
					frame = frames.length - 1;
					isComplete = true;
					completeAction.action();
				}
			}
			
			if(frame < 0) {
				if(loop) frame = frames.length - 1;
				else {
					frame = 0;
					isComplete = true;
					completeReverseAction.action();
				}
			}

			raster = frames[frame];
			return true;
		}
		return false;
	}
	
	public boolean isComplete() {return isComplete;}
	
	public BufferedImage raster() {return raster;}
	
	public void reset() {
		tick = 0;
		isComplete = false;
		if(reverse) frame = frames.length - 1;
		else frame = 0;
		if(frames == null || raster == null) return;
		raster = frames[frame];
	}
	
	
	
	public void generateFrames(BufferedImage image, int frameCount) {
		if(frames != null) {
			Console.err("WarpedAnimation -> generateFramesFromImage() -> frames already exist, they will be cleared for the new animation");
			frames = null;
		}
		if(frameCount < MIN_FRAMES) {
			Console.ln("WarpedAnimation -> generateFramesFromImage() -> frameCount must be at least " + MIN_FRAMES);
			return;
		}
		
		if(frameCount > MAX_FRAMES) {
			Console.ln("WarpedAnimation -> generateFramesFromImage() -> frameCount must be less than " + MAX_FRAMES);
			return;
		}

		frames = new BufferedImage[frameCount];
		size.set(image.getWidth(), image.getHeight());
		
		for(int i = 0; i < frameCount; i++) {frames[i] = UtilsImage.generateClone(image);}
		
	}
	
	
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
