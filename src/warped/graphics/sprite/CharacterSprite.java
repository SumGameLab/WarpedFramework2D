package warped.graphics.sprite;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class CharacterSprite extends WarpedSprite {

	public enum AnimationType {
		MOVE_RIGHT,
		MOVE_DOWN_RIGHT,
		MOVE_DOWN,
		MOVE_DOWN_LEFT,
		MOVE_LEFT,
		MOVE_UP_LEFT,
		MOVE_UP,
		MOVE_UP_RIGHT;
	}
	
	private AnimationType animationType = AnimationType.MOVE_DOWN; 
	
	private int frame = 0;
	private int frameRate = 24; //frames per second
	private HashMap<AnimationType, BufferedImage[]> moveAnimations = new HashMap<>(); 
	
	private BufferedImage[] frames;
	
	private static Timer animationTimer = new Timer();
	private TimerTask updateTask;
	
	
	/**An 8 directional animated sprite.
	 * @param spriteSheet - the sprite sheet containing the frames for each of the 8 movement animations.
	 * @param axis - the direction of the animation strips, horizontal or vertical.
	 * @author 5som3*/
	public CharacterSprite(WarpedSpriteSheet spriteSheet, AxisType axis) {
		super(spriteSheet.getSpriteWidth(), spriteSheet.getSpriteHeight());
		moveAnimations.put(AnimationType.MOVE_RIGHT, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_RIGHT.ordinal()));
		moveAnimations.put(AnimationType.MOVE_DOWN_RIGHT, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_DOWN_RIGHT.ordinal()));
		moveAnimations.put(AnimationType.MOVE_DOWN, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_DOWN.ordinal()));
		moveAnimations.put(AnimationType.MOVE_DOWN_LEFT, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_DOWN_LEFT.ordinal()));
		moveAnimations.put(AnimationType.MOVE_LEFT, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_LEFT.ordinal()));
		moveAnimations.put(AnimationType.MOVE_UP_LEFT, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_UP_LEFT.ordinal()));
		moveAnimations.put(AnimationType.MOVE_UP, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_UP.ordinal()));
		moveAnimations.put(AnimationType.MOVE_UP_RIGHT, spriteSheet.getSpriteAxis(axis, AnimationType.MOVE_UP_RIGHT.ordinal()));
		
		setAnimation(animationType);
		play();
	}
	
	/**An 8 directional animated sprite.
	 * @param spriteSheet - the sprite sheet containing the frames for each of the 8 movement animations.
	 * @param axis - the direction of the animation strips, horizontal or vertical.
	 * @param moveRight - the index of the animation to move right.
	 * @param moveDownRight - the index of the animation to move diagonally down right.
	 * @param moveDown  - the index of the animation to move down.
	 * @param moveDownLeft - the index of the animation to move diagonally down left.
	 * @param moveLeft - the index of the animation to move left.
	 * @param moveUpLeft - the index of the animation to move diagonally up left.
	 * @param moveUp - the index of the animation to move up.
	 * @param moveUpRight - the index of the animation to move diagonally up right.
	 * @author 5som3*/
	public CharacterSprite(WarpedSpriteSheet spriteSheet, AxisType axis, int moveRight, int moveDownRight, int moveDown, int moveDownLeft,
			int moveLeft, int moveUpLeft, int moveUp, int moveUpRight) {
		super(spriteSheet.getSpriteWidth(), spriteSheet.getSpriteHeight());
		moveAnimations.put(AnimationType.MOVE_RIGHT, spriteSheet.getSpriteAxis(axis, moveRight));
		moveAnimations.put(AnimationType.MOVE_DOWN_RIGHT, spriteSheet.getSpriteAxis(axis, moveDownRight));
		moveAnimations.put(AnimationType.MOVE_DOWN, spriteSheet.getSpriteAxis(axis, moveDown));
		moveAnimations.put(AnimationType.MOVE_DOWN_LEFT, spriteSheet.getSpriteAxis(axis, moveDownLeft));
		moveAnimations.put(AnimationType.MOVE_LEFT, spriteSheet.getSpriteAxis(axis, moveLeft));
		moveAnimations.put(AnimationType.MOVE_UP_LEFT, spriteSheet.getSpriteAxis(axis, moveUpLeft));
		moveAnimations.put(AnimationType.MOVE_UP, spriteSheet.getSpriteAxis(axis, moveUp));
		moveAnimations.put(AnimationType.MOVE_UP_RIGHT, spriteSheet.getSpriteAxis(axis, moveUpRight));
		
		setAnimation(animationType);
		play();
	}
	
	/**Get the current type of animation.
	 * @return AnimationType - the type of animation playing.
	 * @author 5som3*/
	public AnimationType getAnimation() {return animationType;}
	
	/**Set the characters animation.
	 * @param AnimationType - the type of animation to play.
	 * @author 5som3*/
	public void setAnimation(AnimationType animationType) {
		this.animationType = animationType;
		frames = moveAnimations.get(animationType);
	}
	
	/**Set the frame rate of the animation.
	 * @param fps - the desired frame rate in frames per second.
	 * @apiNote default fps is 24.
	 * @apiNote fps must be in the domain : (0 < fps <= 60).
	 * @author SomeKid*/
	public void setFrameRate(int fps) {
		if(fps < 1 || fps > 60) {
			Console.err("AnimatedSprite -> setFrameRate() -> framerate is out of bounds : " + fps + ", it will be set to 24fps");
			fps = 24;
		}
		this.frameRate = fps;
		pause();
		play();
	}
	
	/**Jump to a specific frame
	 * @param frame - the frame to jump to
	 * @author SomeKid*/
	public void setFrame(int frame) {
		if(frame < 0 || frame >= frames.length) {
			Console.err("AnimatedSprite -> setFrame() -> invalid frame (min, frame, max) : ( 0, " + frame + ", " + (frames.length - 1) + ")");
			return;
		} 
		this.frame = frame;
		setRasterFast(frames[frame]);
	}

	/**Pause the animation.
	 * @author 5som3*/
	public void pause() {
		if(updateTask == null) return;
		updateTask.cancel();
		updateTask = null;
	}
	
	/**Play the animation.
	 * @apiNote call setAnimation(AnimationType) to change the characters animation.
	 * @author 5som3*/
	public void play() {
		updateTask = new TimerTask() {public void run() {updateAnimation();}};
		animationTimer.scheduleAtFixedRate(updateTask, 0, UtilsMath.convertHzToMillis(frameRate));
	}
	
	private void updateAnimation() {
		frame++;
		if(frame >= frames.length) frame = 0;
		setRasterFast(frames[frame]);
	}
	
}
