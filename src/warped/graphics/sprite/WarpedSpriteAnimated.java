/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class WarpedSpriteAnimated {

	public static final int NORMAL = 0;
	public static final int REVERSE = 1;
	public static final int MIRROR = 2;
	
	private boolean mirror = false;
	private boolean repeat = true;
	private boolean isComplete = false;
	
	private int animationStyle = 0;
	
	private int frame = 0;
	private int animationLength;
	
	private int tick = 0;
	private int delay = 1;

	private WarpedSpriteSheet animationSheet;

	public WarpedSpriteAnimated(WarpedSpriteSheet animationSheet) {
		this.animationSheet = animationSheet;
		animationLength = animationSheet.size();
	}
	
	public WarpedSpriteAnimated(WarpedSpriteSheet animationSheet, boolean repeat) {
		this.animationSheet = animationSheet;
		this.repeat = repeat;
		animationLength = animationSheet.size();
	}
	
	public WarpedSpriteAnimated(WarpedSpriteSheet animationSheet, boolean repeat, int delay) {
		this.animationSheet = animationSheet;
		this.repeat = repeat;
		this.delay = delay;
		animationLength = animationSheet.size();
	}
	
	public WarpedSpriteAnimated(WarpedSpriteSheet animationSheet, int animationStyle) {
		this.animationStyle = animationStyle;
		this.animationSheet = animationSheet;
		animationLength = animationSheet.size();
	}
	
	public WarpedSpriteAnimated(WarpedSpriteSheet animationSheet, int animationStyle, boolean repeat) {
		this.animationStyle = animationStyle;
		this.animationSheet = animationSheet;
		this.repeat = repeat;
		animationLength = animationSheet.size();
		
	}
	
	public void updateFrame() {
		if(delay > 1) {			
			tick++;
			if(tick > delay) {
				tick = 0;
			} else return;
		}
		
		if(repeat) {			
			switch(animationStyle) {
			case NORMAL:
				frame++;
				if(frame >= animationLength) frame = 0;
				break;
			case REVERSE:
				frame--;
				if(frame < 0) frame = animationLength - 1;
				break;
			case MIRROR:
				if(mirror) frame--;
				else frame++;
				if(frame >= animationLength) {mirror = true; frame = animationLength - 1;}
				if(frame < 0) {mirror = false; frame = 0;}
				break;
			default:
				Console.err("AnimatedSPrite -> getFrame() -> invalid style : " + animationStyle);
				break;
			}
		} else if(!isComplete){
			switch(animationStyle) {
			case NORMAL:
				frame++;
				if(frame >= animationLength) {isComplete = true; frame = animationLength - 1;}
				break;
			case REVERSE:
				frame--;
				if(frame < 0) {isComplete = true; frame = 0;}
				break;
			case MIRROR:
				if(mirror) frame--;
				else frame++;
				if(frame >= animationLength) {mirror = true; frame = animationLength - 1;}
				if(frame < 0) {mirror = false; isComplete = true; frame = 0;}
				break;
			default:
				Console.err("AnimatedSPrite -> getFrame() -> invalid style : " + animationStyle);
				break;
			}
		} else return;
	}
	
	public void setFrameDelay(int frameDelay) {this.delay = frameDelay;}
	public boolean isComplete() {return isComplete;}
	public BufferedImage getFrame() {
		updateFrame();
		return animationSheet.getSprite(frame);
	}
	
	public BufferedImage getFrame(int frameOffset) {
		if(frameOffset > animationLength) frameOffset = frameOffset % animationLength;
		int offsetFrame = frame + frameOffset;
		if(offsetFrame >= animationLength) offsetFrame -= (animationLength - 1);
		return animationSheet.getSprite(offsetFrame); 
	}

	public BufferedImage getFrame(double rotation) {
		updateFrame();
		
		double angle = UtilsMath.clampRadianRotation(rotation);
		if(angle == 0) {
			return animationSheet.getSprite(frame);
		} else {	
			int rotationOriginX = (animationSheet.spriteWidth  / 2);    //The point around which the image will be rotated, in this case the center point
			int rotationOriginY = (animationSheet.spriteHeight / 2);
			
			AffineTransform at = new AffineTransform();
			at.rotate(angle, rotationOriginX, rotationOriginY);
			
			BufferedImage rotated = new BufferedImage(animationSheet.spriteWidth, animationSheet.spriteHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
			Graphics2D g2d = rotated.createGraphics();
			g2d.setComposite(UtilsMath.clearComposite);
			g2d.fillRect(0, 0, animationSheet.spriteWidth, animationSheet.spriteHeight);
			g2d.setComposite(UtilsMath.drawComposite);
			g2d.drawRenderedImage(animationSheet.getSprite(frame), at);
			g2d.dispose();
			return rotated;
		}
	}
	
	
}
