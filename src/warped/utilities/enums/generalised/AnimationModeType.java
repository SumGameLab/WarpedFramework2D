package warped.utilities.enums.generalised;

public enum AnimationModeType {

	/**The animation will play once. 
	 * The first frame will be frame[0].
	 * The last frame will be frame[length].
	 * When frame[length] is reached the completeAction will be triggered once
	 * @author SomeKid*/
	PLAY,			
	
	/**The animation will repeat indefinitely. 
	 * The first frame will be frame[0].
	 * The last frame will be frame[length].
	 * When frame[length] is reached the completeAction will be triggered once
	 * If repeatAction() function is called the completeAction will trigger every time the animation completes
	 * @author SomeKid*/
	REPEAT,			
	
	/**The animation will play once. 
	 * The first frame will be frame[length].
	 * The last frame will be frame[0].
	 * When frame[length] is reached the completeAction will be triggered once
	 * @author SomeKid*/
	PLAY_REVERSE,
	
	/**The animation will repeat indefinitely. 
	 * The first frame shown will be frame[length].
	 * The last frame shown will be frame[0].
	 * When frame[length] is reached the completeAction will be triggered once
	 * If repeatAction() function is called the completeAction will trigger every time the animation completes
	 * @author SomeKid*/
	REPEAT_REVERSE,
	
	/**The animation will play once.
	 * The animation will play forward and then play in reverse
	 * The first frame shown will be frame[0].
	 * The last frame shown will be frame[0].
	 * When Frame[0] is reached the completeAction will be triggered once.
	 * @author SomeKid */
	PLAY_MIRROR,
	
	/**The animation will repeat indefinitely.
	 * The animation will play forward and then play in reverse
	 * The first frame shown will be frame[0].
	 * The last frame shown will be frame[0].
	 * When frame[0] is reached the completeAction will be triggered once
	 * If repeatAction() function is called the completeAction will trigger every time the animation completes
	 * @author SomeKid*/
	REPEAT_MIRROR,
	
	
	/**The animation will play indefinitely.
	 * The animation will play for a random number of frames less than the length and more than length / 4.
	 * When the random frame is reached the direction of the animation will be reversed.
	 * If the animation reaches the end frame it will play reversed and then continue to count frames until the random number is reached.
	 * */
	RANDOMISE;
	
	
}
