/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.audio;

import warped.utilities.utils.Console;

public class FrameworkAudio {
	
	public static WarpedAudioClip defaultPress;
	public static WarpedAudioClip defaultRelease;
	public static WarpedAudioClip defaultHover;
	public static WarpedAudioClip defaultUnhover;
	public static WarpedAudioClip defaultClose;
	public static WarpedAudioClip error;
	public static WarpedAudioClip alert;
	
	private static double volume;
	
	private FrameworkAudio() {};
	
	public static void loadFrameworkAudio() {
		defaultPress  	= new WarpedAudioClip("/framework/audio/button_press.wav", true);
		defaultRelease	= new WarpedAudioClip("/framework/audio/button_release.wav", true);
		defaultHover    = new WarpedAudioClip("/framework/audio/button_hover.wav", true);
		defaultUnhover  = new WarpedAudioClip("/framework/audio/button_unhover.wav", true);
		defaultClose    = new WarpedAudioClip("/framework/audio/window_close.wav", true);
		error    		= new WarpedAudioClip("/framework/audio/error.wav", true);
		alert 		   	= new WarpedAudioClip("/framework/audio/alert.wav", true);
		
	}
	
	/**Do not manually call this method, it is called automatically be WarpedState*/
	public static void update() {
		defaultPress.update();
		defaultRelease.update();
		defaultHover.update();
		defaultUnhover.update();
		defaultClose.update();
		error.update();
		alert.update();
	}
	
	
	public static void setVolume(double volume) {
		if(volume < 0.0) {
			Console.err("FrameworkAudio -> setVolume -> volume out of bounds : " + volume);
			volume = 0.0;
		}
		if(volume > 1.0) {
			Console.err("FrameworkAudio -> setVolume -> volume out of bounds : " + volume);
			volume = 1.0;
		}
		FrameworkAudio.volume = volume;
		defaultPress.setVolume(volume);
		defaultRelease.setVolume(volume);
		defaultHover.setVolume(volume);
		defaultUnhover.setVolume(volume);
		defaultClose.setVolume(volume);
	}
	
	public static double getVolume() {return volume;}

	
	
	
	
}
