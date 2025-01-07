/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite.spriteSheets;

import java.awt.image.BufferedImage;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public abstract class FrameworkSprites {
	//If these objects are not created by the launcher then you may have some difficult to find bugs
	//Always remember to initialize sprites sheets in the constructor of this class
	
	public static WarpedSpriteSheet dialIcons;	    		
	public static WarpedSpriteSheet standardIcons;		
	public static WarpedSpriteSheet tileTransitions;
	public static WarpedSpriteSheet anglePicker;
	public static WarpedSpriteSheet mouse;
	public static WarpedSpriteSheet mouseLoad;
	public static WarpedSpriteSheet mediaIcons;
	
	public static BufferedImage trayIcon;
	public static BufferedImage loadBackground;
	public static BufferedImage error;
	
	
	public static void loadFrameworkSprites() {
		Console.ln("FrameworkSprites -> loadFrameworkSprites() -> loading..");
		standardIcons		= new WarpedSpriteSheet("res/framework/graphics/standard_icons_wf2d_30_30_wf.png");                                                   
		dialIcons	        = new WarpedSpriteSheet("res/framework/graphics/small_compass_wf2d_100_100_wf.png");
		anglePicker 		= new WarpedSpriteSheet("res/framework/graphics/angle_picker_wf2d_200_200_wf.png");
		tileTransitions 	= new WarpedSpriteSheet("res/framework/graphics/transitions_wf2d_64_64_wf.png");		
		mouseLoad		 	= new WarpedSpriteSheet("res/framework/graphics/mouse_load_wf2d_192_192_wf.png");
		mouse				= new WarpedSpriteSheet("res/framework/graphics/mouse_wf2d_100_130_wf.png");
		
		trayIcon 			= UtilsImage.loadBufferedImage("res/framework/graphics/tray_icon.png");
		loadBackground	    = UtilsImage.loadBufferedImage("res/framework/graphics/load_background.png");
		error 				= UtilsImage.loadBufferedImage("res/framework/graphics/error.png");
		
	}
	
	public static void loadMediaPlayerSprites() {
		Console.ln("FrameworkSprites -> loadMediaPlayerSprites() -> loading..");
		mediaIcons			= new WarpedSpriteSheet("res/framework/graphics/media_icons_wf2d_38_38_wf.png"); 		
	}
	
	
	
		
		
	 	
		
		
		
		
		
	
	
}
