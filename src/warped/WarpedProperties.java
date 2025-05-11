/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped;

import java.awt.image.BufferedImage;

public class WarpedProperties {
	/* ---------------------- Window ---------------------- */
	public static final int BUFFERED_IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;

	/* ---------------------- Camera ---------------------- */
	
	public static boolean CLAMP_MIN_RENDER_SIZE 		  = true;
	public static void toggleCapMinSize() {if(CLAMP_MIN_RENDER_SIZE) CLAMP_MIN_RENDER_SIZE = false; else CLAMP_MIN_RENDER_SIZE = true;}
	
	/* ------------------ Thread Clocks ------------------ */
	public static final boolean CAP_FRAMES 				  = true;					
	public static final int WARPED_GAME_REFRESH_RATE 	  = 60; 	//Controls game speed, higher values game runs faster; lower slower
	public static final int SCREEN_REFRESH_RATE 		  = 60;	//Controls frame rate of screen + all screen composers
	public static final int AUDIO_CONTROLLER_REFRESH_RATE = 60;		
	public static final int USER_INPUT_REFRESH_RATE 	  = 60; 
	public static final int ALPHA_THRESHOLD = 10; // the alpha threshold that will be used when checking for collisions
	
	/* ------------------ Saving ------------------ */
	public static final String SAVE_DIR = "saves/";
	public static final String SAVE_FILE_TYPE = ".wfd";
	public static String SAVE_PATH = "default/"; //Change this path and add a save folder per game
	
	
}
