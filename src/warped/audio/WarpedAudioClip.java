/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import warped.application.state.WarpedFramework2D;
import warped.functionalInterfaces.WarpedAction;
import warped.utilities.utils.Console;

public class WarpedAudioClip {

//	private File audioFile;
	private Clip audioClip;
	private String path;
	private File file;
	
	private boolean isFrameworkAudio = false;
	private boolean isFileLoaded = false;
	private boolean isLooping = false;
	private boolean isFading = false;
	private boolean isFadeOut;
	private boolean isFadeIn;
	
	public boolean isPlaying() {
		if(audioClip == null) return false;
		if(!audioClip.isOpen()) return false;
		if(!audioClip.isActive()) return false;
		return true;
	}
	
	private WarpedAction endClipAction = () -> {Console.ln("Finished clip : " + path);};
	
	private float volume = 1.0f;
	
	private final static int TIME_OUT = 300;
	private int timeOutTick = 0;
	
	public WarpedAudioClip(String path) {
		Console.ln("WarpedAudioClip -> constructing new clip from stream at : " + path);
		this.path = path;
		isFileLoaded = false;
		file = null;
		initializeClip();
	}
	
	public WarpedAudioClip(File file, String path) {
		Console.ln("WarpedAudioClip -> constructing new clip from file at : " + path);
		this.path = path;
		this.file = file;
		isFileLoaded = true;
		initializeClip();
	}
	
	public WarpedAudioClip(String path, boolean isFrameworkAudio) {
		Console.ln("WarpedAudioClip -> constructing new clip from stream at : " + path);
		this.path = path;
		this.isFrameworkAudio = true;
		file = null;
		initializeClip();
	}
	
	private void initializeClip() {
		try {
			audioClip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setEndClipAction(WarpedAction action) {
		this.endClipAction = action;
	}
	
	
	private void openAudioFile() {
		Console.condition("WarpedAudioClip -> openAudioFile() -> trying to open audio file : " + file.getName());
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			audioClip.open(ais);
			
			ais.close();
			Console.met("WarpedAudioClip -> openAudioFile() -> opened audio file");
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			Console.met("WarpedAudioClip -> openAudioFile() -> failed to open audio file");
			Console.stackTrace(e);
		}	
	}
	
	private void openAudioStream() {
		Console.condition("WarpedAudioClip -> openAudioStream() -> trying to open audio stream at : " + path);
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(WarpedFramework2D.getApp().getClass().getResource(path));
			audioClip.open(ais);
			ais.close();
			Console.met("WarpedAudioClip -> openAudioStream() -> stream was opened");
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			Console.err("WarpedAudioClip -> openAudioStream() -> failed to open stream");
			Console.stackTrace(e);
		}		
	}
	
	private void openFrameworkAudio() {
		Console.condition("WarpedAudioClip -> openFrameworkAudio() -> trying to open audio stream at : " + path);
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(WarpedFramework2D.class.getResource(path)) ;
			audioClip.open(ais);
			ais.close();
			Console.met("WarpedAudioClip -> openFrameworkAudio() -> stream was opened");
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			Console.err("WarpedAudioClip -> openFrameworkAudio() -> failed to open stream");
			Console.stackTrace(e);
		}	
	}
	
	
	public void update() {
		if(audioClip == null) return;
		else if(!audioClip.isOpen()) return;
		else if(audioClip.isActive()) return;
		else {
			if(audioClip.getFramePosition() == audioClip.getFrameLength() && isLooping) {
				audioClip.setFramePosition(0);
				audioClip.start();
				return;
			}
			timeOutTick++;
			if(timeOutTick > TIME_OUT) {
				timeOutTick = 0;
				if(audioClip.getFramePosition() == audioClip.getFrameLength()) endClipAction.action();
				audioClip.close();
			}
		}
	}
	
	public boolean isLooping() {return isLooping;}
	public boolean isFading() {return isFading;}
	public void setLoop(boolean isLooping) {this.isLooping = isLooping;}
	public void loop() {isLooping = true;}
	public void noLoop() {isLooping = false;}
	
	private boolean isValid() {if(audioClip == null || !audioClip.isOpen()) return false; else return true;}
	
	public float getVolume() {
		if(!isValid()) {
			Console.err("WarpedAudioClip -> getVolume() -> audioClip is not valid");
			return -1.0f;
		}
	    FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
	    return (float) Math.pow(10f, gainControl.getValue() / 20f);
	}

	public void setVolume(double volume) {
		if(volume < 0.0) {
			Console.err("WarpedAudioClip -> setVolume() -> volume must be in the domain 0.0 - 1.0 inclusive -> input volume : " + volume);
			volume = 0.0;
		} else if(volume > 1.0) {
			Console.err("WarpedAudioClip -> setVolume() -> volume must be in the domain 0.0 - 1.0 inclusive -> input volume : " + volume);
			volume = 1.0;
		} else this.volume = (float) volume;
		setClipVolume();
	}
	
	private void setClipVolume() {
		if(isValid()) {			
			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);        
			gainControl.setValue(20f * (float) Math.log10(volume));
		}		
	}
	
	public void play() {
		if(audioClip == null || !audioClip.isOpen()) {
			if(isFileLoaded) openAudioFile();
			else if(isFrameworkAudio) openFrameworkAudio();
			else openAudioStream();
			setClipVolume();
		}	
		
		if(audioClip.isActive()) return;
		timeOutTick = 0;
		audioClip.setFramePosition(0);
		audioClip.start();
		Console.ln("WarpedAudioClip -> play() -> playing :" + path);
	}
	
	public void stop() {
		if(audioClip == null) return;
		else if(audioClip.isActive()) audioClip.stop();
	}
	
	public void close() {
		if(audioClip == null) return;
		else if(!audioClip.isOpen()) return;
		else {
			audioClip.stop();
			audioClip.close();
		}
	}
	
	
	/*
	
	public void fadeIn() {
		isFading = true;
		isFadeIn = true;
	}
	
	public void fadeOut() {
		isFading = true;
		isFadeOut = true;
	}
	
	public boolean updateFade(float volumeCap) {
		if(!isFading) return true;
		if(volumeCap > 6.0f) volumeCap = 6.0f;
		if(volumeCap < -80.0f) volumeCap = -80.0f;
		if(isFadeIn && isFadeOut) {
			WarpedConsole.err("AudioData -> fade() -> logically inconsisted -> can't fadeIn and fadeOut at the same time");
			isFadeIn = false;
			isFadeOut = false;
			return false;
		}
		if(isFadeIn)  return updateFadeIn();
		if(isFadeOut) return updateFadeOut();
		return false;
	}
	
	private boolean updateFadeOut() {
		volume -= 1.0f;
		if(volume < -80.0f) volume = -80.0f;
		if(volume == -80.0f) {
			isFading = false;
			return true;
		}
		return false;
	}
	
	private boolean updateFadeIn() {
		volume += 1.0f;
		if(volume > 6.0f) volume = 6.0f;
		if(volume >= volumeCap) {
			volume = volumeCap;
			isFading = false;
			return true;
		}
		return false;
	}
	*/
	
	
}
