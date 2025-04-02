/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import warped.WarpedProperties;
import warped.functionalInterfaces.WarpedAction;
import warped.utilities.enums.generalised.AnimationModeType;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class AnimatedSprite extends WarpedSprite {

	private static Timer animationTimer = new Timer("Timer Thread : Animated Sprite");
	private TimerTask updateTask;

	private AnimationModeType mode = AnimationModeType.REPEAT;
	private boolean isComplete = false;
	private boolean isRepeatAction = false;
	private WarpedAction completeAction = () -> {Console.ln("AnimatedSprite -> default Completion action");};
		
	private int frame = 0;
	private int frameRate = 24; //frames per second
	protected BufferedImage[] frames;
	private boolean mirror = false;

	
	/**An animation from a single series of frames.
	 * The animation is scheduled as a task for the animation timer to execute.
	 * The animation timer is shared across all AnimatedSprites.
	 * @param frames - The frames for the animation. Frames must be in their sequential order.
	 * @apiNote Animation can be set to different AnimationModeTypes described in this class. 
	 * */
	public AnimatedSprite(BufferedImage[] frames) {		
		super(frames[0].getWidth(), frames[0].getHeight());
		int checkWidth  = 1;
		int checkHeight = 1;
		for(int i = 0; i < frames.length; i++) {
			if(frames[i] == null) {
				Console.err("AnimatedSprite -> AnimatedSprite() -> frames contains a null image, a dummy image will be added");	
				frames[i] = new BufferedImage(checkWidth, checkHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
			}
			if(i == 0) {
				checkWidth  = frames[0].getWidth();
				checkHeight = frames[0].getHeight();
			}
			if(frames[i].getWidth() != checkWidth || frames[i].getHeight() != checkHeight) {
				Console.err("AnimatedSprite -> AnimatedSprite() -> frames contains images that are inconsistent sizes, the animation will not be drawn correctly");
			}
		}
		this.frames = frames;
		setRasterFast(frames[0]);
		
		setAnimationMode(mode);
	}
	
	/**Control repeating of the completeAction
	 * @param isRepeatAction - If true the set completeAction will trigger every time the animation restarts
	 * @author SomeKid*/	
	public void setRepeatAction(boolean isRepeatAction) {this.isRepeatAction = isRepeatAction;}
	
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
	
	/**Get the index of the frame that is currently the raster.
	 * @return int - the index of the raster frame.
	 * @author 5som3*/
	public int getFrame() {return frame;}
	
	/**Pause the animation
	 * @author SomeKid*/
	public void pause() {cancelUpdate();}
	
	/**Resume the animation
	 * @author SomeKid*/
	public void play() {setAnimationMode(mode);}
	
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
		setAnimationMode(mode);		
	}
	
	/**Set an action to occur when the animation completes.
	 * @param completeAction - The action to execute at completion.
	 * @apiNote See AnimationModeType (above) for documentation on when the completeAction will be executed.
	 * @apiNote Call the setRepeatAction(true) function to have the action repeat multiple times on a repeating animation.
	 * @author SomeKid*/
	public void setCompleteAction(WarpedAction completeAction) {
		if(completeAction == null) {
			Console.err("AnimatedSprite -> setCompleteAction() -> the action is null");
			return;
		} else this.completeAction = completeAction;
	}
	
	/**Set how the animation will be played
	 * @param mode - the animation mode type to use
	 * 			   - see the AnimationModeType (above) for documentation on the what the different modes are
	 * @author SomeKid*/
	public void setAnimationMode(AnimationModeType mode) {
		cancelUpdate();
		
		this.mode = mode;
		switch(mode) {
		case PLAY:			 updateTask = new TimerTask() {public void run() {updatePlay();}};			break;
		case PLAY_MIRROR:	 updateTask = new TimerTask() {public void run() {updateMirror();}};		break;
		case PLAY_REVERSE:	 updateTask = new TimerTask() {public void run() {updatePlayReverse();}};	break;
		case REPEAT:		 updateTask = new TimerTask() {public void run() {updateRepeat();}};		break;
		case REPEAT_MIRROR:	 updateTask = new TimerTask() {public void run() {updateRepeatMirror();}};	break;
		case REPEAT_REVERSE: updateTask = new TimerTask() {public void run() {updateRepeatReverse();}};	break;
		default:
			Console.err("AnimatedSprite -> setAnimationMode() -> invalid case : " + mode);
			break;
		}
		animationTimer.scheduleAtFixedRate(updateTask, 0, UtilsMath.convertHzToMillis(frameRate));
	}

	/**Is the animation currently playing in any mode.
	 * @return boolean - true if the animation is being updated currently else false.
	 * @author 5som3*/
	public boolean isPlaying() {if(updateTask == null) return false; else return true;}
	
	private final void complete() {
		isComplete = true;
		if(isRepeatAction || !isComplete) completeAction.action();
	}
	
	private final void cancelUpdate() {
		if(updateTask == null) return;
		updateTask.cancel();
		updateTask = null;
	}
	
	private final void updatePlay() {
		frame++;
		if(frame >= frames.length) {
			complete();
			cancelUpdate();
		} else setRasterFast(frames[frame]);
	}
	
	private final void updateMirror() {
		if(mirror) {
			frame--;
			if(frame < 0) {
				complete();
				cancelUpdate();
				return;
			}
		} else {
			frame++;
			if(frame >= frames.length) frame = frames.length - 1;
			mirror = true;
		}
		setRasterFast(frames[frame]);
	}
	
	private final void updatePlayReverse() {
		frame--;
		if(frame < 0) {
			complete();
			cancelUpdate();
		} else setRasterFast(frames[frame]);
	}
	
	private final void updateRepeat() {
		frame++;
		if(frame >= frames.length) {
			frame = 0;
			complete();
		}
		setRasterFast(frames[frame]);
	}
	
	private final void updateRepeatMirror() {
		if(mirror) {
			frame--;
			if(frame < 0) {
				mirror = false;
				frame = 1;
				complete();
			}
		} else {
			frame++;
			if(frame >= frames.length) {
				mirror = true;
				frame = frames.length - 2;
			}
		}
		setRasterFast(frames[frame]);
	}
	
	private final void updateRepeatReverse() {
		frame--;
		if(frame < 0) {
			frame = frames.length - 1;
			complete();
		}
		setRasterFast(frames[frame]);
	}
	
	/**Has the animation completed.
	 * @return boolean - true if the animation has complete. 
	 * */
	public final boolean isComplete() {return isComplete;}
	
	
	
}
