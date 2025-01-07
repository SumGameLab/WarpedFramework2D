/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.FloatControl;

import warped.audio.WarpedAudioClip;
import warped.utilities.enums.WarpedLinkable;
import warped.utilities.utils.Console;

public class WarpedAudioFolder<T extends WarpedLinkable<? extends Enum<?>>> extends WarpedFolder {
	
	
	protected T folderType; 		  //The Type passed in by the creator of the folder, values will be linked with loaded files i.e. ImageType
	protected String folderPath; 	  //The path passed in by the creator of the folder i.e. res/graphics/images
	protected String folderExtension; //The extend part of the path that leads to this folder from the folder root i.e. /graphics/images
	protected String folderName; 	  //The name of the folder as it would appear in system explorer i.e. res
	
	private String linkDataFolderPath; //The path where linkData for all warpedFolders will be kept, is always exist as a sub directory of the folderName i.e. res/linkData/
	protected String linkDataFilePath; //The path to the folder where linkData will be written i.e. res/linkData/ImageType_wf2dat.txt
	protected String linkDataPath; //The path to the linkData resource folder, used when loading, always named by the folderType passed i.e. /linkData/ImageType_wf2dat.txt	
	
	protected File folder;				//A java File that is the folder containing the assets
	protected File[] contents;			//A java File Array this is the assets
	protected File linkDataFolder;		//A Java File that is the folder containing the linkData for all WarpedFolders	
	
	protected List<String> filePaths;
	private HashMap<T, WarpedAudioClip> clips = new HashMap<>();		

	private float volume = 20.0f;
	private FloatControl fc;
	private final float GAIN_MAX =   6.0f;
	private final float GAIN_MIN = -80.0f;
	
		
	public static <K extends WarpedLinkable<? extends Enum<?>>> WarpedAudioFolder<K> generateFolder(K folderType, String folderPath) {
		WarpedAudioFolder<K> folder = new WarpedAudioFolder<K>(folderType, folderPath);
		WarpedState.audioFolders.add(folder);
		return folder;
	}
	
	private WarpedAudioFolder(T folderType, String folderPath) {
		this.folderType = folderType;
		this.folderPath = folderPath;
				
		Console.ln("WarpedAudioFolder -> Folder Name : " + 1 + " -> Folder Type :  " + folderType.getClass() + " ->  Folder Path : " + folderPath);
		
		if(isFolderValid()) {
			if(isPathsSet()) {
				if(isLinkDataFolderValid()) {
					if(updateLinkData()) {
						loadDataFromFolder();
					} else {
						Console.err("WarpedAudioFolder -> failed to update linkData -> can't proceede");
						return;
					}
				} else {
					Console.err("WarpedAudioFolder -> couldn't validate linkData folder, can't proceede");
					return;
				}	
			} else {
				Console.err("WarpedAudioFolder -> folder and file paths were not set, can't proceede");
				return;
			}
		} else {
			if(isPathsSet()) {				
				if(readLinkData()) {
					loadLinkDataPaths();
				} else {
					Console.err("WarpedAudioFolder -> couldn't read linkData, can't proceede");
					return;
				}
			} else {
				Console.err("WarpedAudioFolder -> paths were not set to load linkData, can't proceede");
				return;
			}
		}			
		
		setVolume(0.5);
	}
	
	protected void loadDataFromFolder() {
		Console.condition("WarpedAudioFolder -> loadDataFromFolder() -> trying to load images from folder : " + folder.getPath());
		@SuppressWarnings("unchecked")
		ArrayList<T> imageTypes = (ArrayList<T>) folderType.getAll();
		for(int i = 0; i < contents.length; i++) {
			
			File file = contents[i];
			clips.put(imageTypes.get(i), new WarpedAudioClip(contents[i], folderPath + "/" + file.getName()));
		}
		if(clips.size() != contents.length) {
			Console.err("WarpedAudioFolder -> loadDataFromFolder() -> image size doesn't match number of types");
			return;
		} else Console.met("WarpedAudioFolder -> loadDataFromFolder() -> loaded " + clips.size() + " images");
		
	}
	
	protected void loadLinkDataPaths() {
		@SuppressWarnings("unchecked")
		ArrayList<T> imageTypes = (ArrayList<T>) folderType.getAll();
		
		Console.condition("WarpedAudioFolder -> loadDataPaths() -> trying to load images");
		for(int i = 0; i < filePaths.size(); i++) {
			String filePath = filePaths.get(i);
			clips.put(imageTypes.get(i), new WarpedAudioClip(filePath));
		}
		
		if(clips.size() == imageTypes.size()) Console.met("WarpedAudioFolder -> loadDataPaths() -> loaded images from dataPath");
		else Console.err("WarpedAudioFolder -> loadDataPaths() -> failed to load data");
		
	}
	
	protected final boolean readLinkData() {
		Console.ln("WarpedAudioFolder -> readLinkData() -> looking for linkData..");
		InputStream stream = null;
	
		Console.condition("WarpedAudioFolder -> readLinkData() -> looking for linkData at : " + linkDataPath);
		if(getClass().getResourceAsStream(linkDataPath) == null) {
			Console.condition("WarpedAudioFolder -> readLinkData() -> couln't find data");
		} else stream = getClass().getResourceAsStream(linkDataPath);
		
		
		if(stream == null) {
			Console.err("WarpedAudioFolder -> readLinkData() -> can't find link data");
		} else Console.met("WarpedAudioFolder -> readLinKData() -> found linkData");
		
		BufferedReader txtReader = new BufferedReader(new InputStreamReader(stream));
		filePaths = txtReader.lines().toList();
		Console.met("WarpedAudioFolder -> readLinkData() -> read " + filePaths.size() + " filePaths from linkData");
		return true;
	}
	
	protected final boolean isLinkDataFolderValid() {
		linkDataFolder = new File(linkDataFolderPath);
		if(linkDataFolder.exists()) {
			Console.ln("WarpedAudioFolder -> isLinkDataFolderValid() -> folder is valid");
			return true;
		} else {
			Console.condition("WarpedAudioFolder -> isLinkDataFolderValid() -> folder doesn't exist, trying to create..");
			if(linkDataFolder.mkdirs()) {
				Console.met("WarpedAudioFolder -> isLinkDataFolderValid() -> created new data folder");
				return true;
			} else {
				Console.err("WarpedAudioFolder -> isLinkDataFolderValid() -> failed to create data link folder");
				return false;
			}
		}
	}
	
	protected final boolean updateLinkData() {
		File file = new File(linkDataFilePath);
		if(file.exists()) {
			Console.ln("WarpedAudioFolder -> updateLinkData() -> overwritting linkData for : " + folderType.getClass().getSimpleName());
		} else Console.ln("WarpedAudioFolder -> updateLinkData() -> creating linkData for : " + folderType.getClass().getSimpleName());
		
		
		Console.condition("WarpedAudioFolder -> updateLinkData() -> trying to write link data to : " + linkDataFilePath);
		try {
			PrintWriter writer = new PrintWriter(linkDataFilePath, "UTF-8");
			ArrayList<String> sp = new ArrayList<>();
			for(int i = 0; i < contents.length; i++) {
				String ln = folderExtension + "/" + contents[i].getName();
				sp.add(ln);
				writer.println(ln);
			}
			writer.close();
			Console.met("WarpedAudioFolder -> updateLinkData() -> updated linkData");
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.err.println("WarpedAudioFolder -> updateLinkData() -> failed to update linkData");
		return false;
	}
	
	protected final boolean isPathsSet() {		
		Console.condition("WarpedAudioFolder -> isFolderValid() -> setting folder path and extension");
		for(int i = 0; i < folderPath.length(); i++) {
			if(folderPath.substring(i, i + 1).equals("/")) {
				folderName = folderPath.substring(0, i);
				Console.met("WarpedAudioFolder -> isFolderValid() -> set folder path name to : " + folderName);
				folderExtension = folderPath.substring(i);
				Console.met("WarpedAudioFolder -> isFolderValid() -> set folder extension to : " + folderExtension);
				break;
			}
		}
		
		if(folderName == null || folderName.equals("") || folderExtension == null || folderExtension.equals("")) {				
			Console.err("WarpedAudioFolder -> isFolderValid() -> failed to set folder name or extension");
			return false;
		} else {
			linkDataFolderPath = folderName + LINK_EXTENSION;
			Console.met("WarpedAudioFolder -> isFolderValid() -> linkDataFolderPath set to : " + linkDataFolderPath);
			linkDataFilePath = linkDataFolderPath + folderType.getClass().getSimpleName() + DATA_SUFFIX;
			Console.met("WarpedAudioFolder -> isFolderValid() -> linkDataFilePath set to : " + linkDataFilePath);
			linkDataPath = linkDataFilePath.substring(folderName.length());
			Console.met("WarpedAudioFolder -> isFolderValid() -> linkDataPath set to : " + linkDataPath);
			return true;
		}
	}
	
	protected final boolean isFolderValid() {
		folder = new File(folderPath);
		if(folder.exists()) {
			Console.condition("WarpedAudioFolder -> isFolderValid() -> found folder, checking contents..");
			contents = folder.listFiles();
			if(contents.length == 0) {
				Console.err("WarpedAudioFolder -> isFolderValid() -> folder has no files");
				return false;
				
			} else if(contents.length != folderType.size()) {
				Console.err("WarpedAudioFolder -> isFolderValid() -> folder size does types size -> (type size, folder size) : ( " + folderType.size() + ", " + contents.length + ")");
				return false;
				
			} else for(int i = 0; i < contents.length; i++) {
				File file = contents[i];
				if(!file.getName().endsWith(".wav")) {
					Console.err("WarpedAudioFolder -> isFolderValid() -> folder contains files that are not .png ->  this file is not png : " + file.getName());
					return false;
				}	
			}	
			Console.met("WarpedAudioFolder -> isFolderValid() -> folder is valid");
			return true;
		} else {
			Console.err("WarpedAudioFolder -> isFolderValid() -> no folder exists at : " + folderPath);
			return false;
		}		
	}
	
		

	//---------
	//---------------------- Play Controls ---------------------- 
	//-------
	public String getName() {return folderName;}

	
	public void play(T clip) {
		if(clips.containsKey(clip)) clips.get(clip).play();
		else Console.err("WarpedAudioFolder -> " + folderName + " -> play() -> there is no audio clip called : " + clip);
	}
	public void stop(T clip) {
		if(clips.containsKey(clip))clips.get(clip).stop();
		else Console.err("WarpedAudioFolder -> " + folderName + " -> stop() -> there is no audio clip called : " + clip);
	}
	public WarpedAudioClip get(T clip) {
		if(clips.containsKey(clip)) return clips.get(clip);
		else {Console.err("WarpedAudioFolder -> " + folderName + 
				" -> get() -> there is not audio clip called : " + clip); return null;}
	}
	
	public void stop() {clips.forEach((k, c) -> {c.stop();});}
	public void close () {clips.forEach((k, c) -> {c.close();});
	}
	
	//-----
	//----------------- Volume Controls ----------------------
	//-----
	
	public void volumeUp() {
		volume += 1.0f; 
		if(volume > GAIN_MAX) volume = GAIN_MAX; 
	}
	public void volumeDown() {
		volume -= 1.0f; 
		if(volume < GAIN_MIN) volume = GAIN_MIN;
	}



	public void setVolume(double volume) {
		if(volume < 0.0) {
			Console.err("WarpedAudio -> " + folderName + " -> volume out of bounds : " + volume);
			volume = 0.0;
		}
		if(volume > 1.0) {
			Console.err("WarpedAudio ->  " + folderName + " -> volume out of bounds : " + volume);
			volume = 1.0;
		}
		double v = volume;
		clips.forEach((n, c) -> {c.setVolume(v);});	
	}

	
	//-------
	//-------------Update----------------
	//-------
	/**Do not manually call this method, it is called Automatically by WarpedState*/
	public void update() {
		clips.forEach((n, c) -> {c.update();});
	}

}
