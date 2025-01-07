/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.enums.WarpedLinkable;
import warped.utilities.utils.Console;

public class WarpedImageFolder<T extends WarpedLinkable<? extends Enum<?>>> extends WarpedFolder {

	protected T folderType; 		  //The Type passed in by the creator of the folder, values will be linked with loaded files i.e. ImageType
	protected String folderPath; 	  //The path passed in by the creator of the folder i.e. res/graphics/images
	protected String folderExtension; //The extend part of the path that leads to this folder from the folder root i.e. /graphics/images
	protected String folderName; 	  //The name of the folder as it would appear in system explorer i.e. res
	
	private   String linkDataFolderPath; //The path where linkData for all warpedFolders will be kept, is always exist as a sub directory of the folderName i.e. res/linkData/
	protected String linkDataFilePath; //The path to the folder where linkData will be written i.e. res/linkData/ImageType_wf2dat.txt
	protected String linkDataPath;	   //The path to the linkData resource folder, used when loading, always named by the folderType passed i.e. /linkData/ImageType_wf2dat.txt	
	
	protected File folder;				//A java File that is the folder containing the assets
	protected File[] contents;			//A java File Array this is the assets
	protected File linkDataFolder;		//A Java File that is the folder containing the linkData for all WarpedFolders
	
	protected List<String> filePaths;
	private HashMap<T, BufferedImage> images = new HashMap<>();
	
	public static <K extends WarpedLinkable<? extends Enum<?>>> WarpedImageFolder<K> generateFolder(K type, String folderPath){
		return new WarpedImageFolder<K>(type, folderPath);
	}
	
	protected WarpedImageFolder() {};
	protected WarpedImageFolder(T folderType, String folderPath) {
		this.folderType = folderType;
		this.folderPath = folderPath;
				
		Console.ln("WarpedImageFolder -> Folder Name : " + 1 + " -> Folder Type :  " + folderType.getClass() + " ->  Folder Path : " + folderPath);
		
		if(isFolderValid()) {
			if(isPathsSet()) {
				if(isLinkDataFolderValid()) {
					if(updateLinkData()) {
						loadDataFromFolder();
					} else {
						Console.err("WarpedImageFolder -> failed to update linkData -> can't proceede");
						return;
					}
				} else {
					Console.err("WarpedImageFolder -> couldn't validate linkData folder, can't proceede");
					return;
				}	
			} else {
				Console.err("WarpedImageFolder -> folder and file paths were not set, can't proceede");
				return;
			}
		} else {
			if(isPathsSet()) {				
				if(readLinkData()) {
					loadLinkDataPaths();
				} else {
					Console.err("WarpedImageFolder -> couldn't read linkData, can't proceede");
					return;
				}
			} else {
				Console.err("WarpedImageFolder -> paths were not set to load linkData, can't proceede");
				return;
			}
		}			
	}
	
	protected void loadDataFromFolder() {
		Console.condition("WarpedImageFolder -> loadDataFromFolder() -> trying to load images from folder : " + folder.getPath());
		@SuppressWarnings("unchecked")
		ArrayList<T> imageTypes = (ArrayList<T>) folderType.getAll();
		for(int i = 0; i < contents.length; i++) {
			
			File file = contents[i];
			Console.condition("WarpedImageFolder -> loadDataFromFolder() -> trying to load image from the file : " + file.getPath());
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(image == null) {
				Console.err("WarpedImageFolder -> loadDataFromFolder() -> failed to read image from file");
				continue;
			}
			images.put(imageTypes.get(i), image);
		}
		if(images.size() != contents.length) {
			Console.err("WarpedImageFolder -> loadDataFromFolder() -> image size doesn't match number of types");
			return;
		} else Console.met("WarpedImageFolder -> loadDataFromFolder() -> loaded " + images.size() + " images");
		
	}
	
	protected void loadLinkDataPaths() {
		@SuppressWarnings("unchecked")
		ArrayList<T> imageTypes = (ArrayList<T>) folderType.getAll();
		
		Console.condition("WarpedImageFolder -> loadDataPaths() -> trying to load images");
		for(int i = 0; i < filePaths.size(); i++) {
			String filePath = filePaths.get(i);
			
			URL url = getClass().getResource(filePath);
			if(url == null) {
				Console.err("WarpedImageFolder -> loadDataPaths() -> couldn't find resource at path : " + filePath);
				continue;
			}
			
			BufferedImage image = null;
			try {
				image = ImageIO.read(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(image == null) {
				Console.err("WarpedImageFolder -> loadDataPaths() -> failed to read image from url : " + url.toString());
				continue;
			}
			images.put(imageTypes.get(i), image);
		}
		
		if(images.size() == imageTypes.size()) Console.met("WarpedImageFolder -> loadDataPaths() -> loaded images from dataPath");
		else Console.err("WarpedImageFolder -> loadDataPaths() -> failed to load data");
		
	}
	
	protected final boolean readLinkData() {
		Console.ln("WarpedImageFolder -> readLinkData() -> looking for linkData..");
		InputStream stream = null;
	
		Console.condition("WarpedImageFolder -> readLinkData() -> looking for linkData at : " + linkDataPath); 
		stream = getClass().getResourceAsStream(linkDataPath);
		
		if(stream == null) {
			Console.err("WarpedImageFOlder -> readLinkData() -> can't find link data");
		} else Console.met("WarpedImageFolder -> readLinKData() -> found linkData");
		
		BufferedReader txtReader = new BufferedReader(new InputStreamReader(stream));
		filePaths = txtReader.lines().toList();
		Console.met("WarpedImageFolder -> readLinkData() -> read " + filePaths.size() + " filePaths from linkData");
		return true;
	}
	
	protected final boolean isLinkDataFolderValid() {
		linkDataFolder = new File(linkDataFolderPath);
		if(linkDataFolder.exists()) {
			Console.ln("WarpedImageFolder -> isLinkDataFolderValid() -> folder is valid");
			return true;
		} else {
			Console.condition("WarpedImageFolder -> isLinkDataFolderValid() -> folder doesn't exist, trying to create..");
			if(linkDataFolder.mkdirs()) {
				Console.met("WarpedImageFolder -> isLinkDataFolderValid() -> created new data folder");
				return true;
			} else {
				Console.err("WarpedImageFolder -> isLinkDataFolderValid() -> failed to create data link folder");
				return false;
			}
		}
	}
	
	protected final boolean updateLinkData() {
		File file = new File(linkDataFilePath);
		if(file.exists()) {
			Console.ln("WarpedImageFolder -> updateLinkData() -> overwritting linkData for : " + folderType.getClass().getSimpleName());
		} else Console.ln("WarpedImageFolder -> updateLinkData() -> creating linkData for : " + folderType.getClass().getSimpleName());
		
		
		Console.condition("WarpedImageFolder -> updateLinkData() -> trying to write link data to : " + linkDataFilePath);
		try {
			PrintWriter writer = new PrintWriter(linkDataFilePath, "UTF-8");
		//	ArrayList<String> sp = new ArrayList<>();
			for(int i = 0; i < contents.length; i++) {
				String ln = folderExtension + "/" + contents[i].getName();
			//	sp.add(ln);
				writer.println(ln);
			}
			writer.close();
			Console.met("WarpedImageFolder -> updateLinkData() -> updated linkData");
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.err.println("WarpedImageFolder -> updateLinkData() -> failed to update linkData");
		return false;
	}
	
	protected final boolean isPathsSet() {		
		Console.condition("WarpedImageFolder -> isFolderValid() -> setting folder path and extension");
		for(int i = 0; i < folderPath.length(); i++) {
			if(folderPath.substring(i, i + 1).equals("/")) {
				folderName = folderPath.substring(0, i);
				Console.met("WarpedImageFolder -> isFolderValid() -> set folder path name to : " + folderName);
				folderExtension = folderPath.substring(i);
				Console.met("WarpedImageFolder -> isFolderValid() -> set folder extension to : " + folderExtension);
				break;
			}
		}
		
		if(folderName == null || folderName.equals("") || folderExtension == null || folderExtension.equals("")) {				
			Console.err("WarpedImageFolder -> isFolderValid() -> failed to set folder name or extension");
			return false;
		} else {
			linkDataFolderPath = folderName + LINK_EXTENSION;
			Console.met("WarpedImageFolder -> isFolderValid() -> linkDataFolderPath set to : " + linkDataFolderPath);
			linkDataFilePath = linkDataFolderPath + folderType.getClass().getSimpleName() + DATA_SUFFIX;
			Console.met("WarpedImageFolder -> isFolderValid() -> linkDataFilePath set to : " + linkDataFilePath);
			linkDataPath = linkDataFilePath.substring(folderName.length());
			Console.met("WarpedImageFolder -> isFolderValid() -> linkDataPath set to : " + linkDataPath);
			return true;
		}
	}
	
	protected final boolean isFolderValid() {
		folder = new File(folderPath);
		if(folder.exists()) {
			Console.condition("WarpedImageFolder -> isFolderValid() -> found folder, checking contents..");
			contents = folder.listFiles();
			if(contents.length == 0) {
				Console.err("WarpedImageFolder -> isFolderValid() -> folder has no files");
				return false;
				
			} else if(contents.length != folderType.size()) {
				Console.err("WarpedImageFolder -> isFolderValid() -> folder size does types size -> (type size, folder size) : ( " + folderType.size() + ", " + contents.length + ")");
				return false;
				
			} else for(int i = 0; i < contents.length; i++) {
				File file = contents[i];
				if(!file.getName().endsWith(".png")) {
					Console.err("WarpedImageFolder -> isFolderValid() -> folder contains files that are not .png ->  this file is not png : " + file.getName());
					return false;
				}	
			}	
			Console.met("WarpedImageFolder -> isFolderValid() -> folder is valid");
			return true;
		} else {
			Console.err("WarpedImageFolder -> isFolderValid() -> no folder exists at : " + folderPath);
			return false;
		}		
	}
	
	public BufferedImage getImage(T imageName) {
		if(images.containsKey(imageName)) return images.get(imageName);
		else {
			Console.err("WarpedImageFolder -> " + 1 + " ->  getImage() -> the image : " + imageName + " does not exist in the folder");
			return FrameworkSprites.error;
		}
	}
	
	public ArrayList<BufferedImage> getImages(List<T> imageNames){
		ArrayList<BufferedImage> result = new ArrayList<>();
		for(int i = 0; i < imageNames.size(); i++) {
			result.add(getImage(imageNames.get(i)));
		}
		return result;		
	}
	
	public void saveImages(String path) {
		if(images == null) {
			Console.err("WarpedImageFolder -> saveImages() -> images is null");
			return;
		}
		if(images.size() == 0) {
			Console.err("WarpedImageFodler -> saveImages() -> the folder : " + 1 + " -> does not contain any images");
			return;			
		}
		
		boolean isFolderValid = false;
		File folder = new File(path);
		if(!folder.exists()) {
			Console.err("WarpedImageFolder -> saveImags() -> the save directory does not exist : " + path);
			Console.err("WarpedImageFolder -> saveImags() -> trying to create save directory..");
			try {
				folder.createNewFile();
				Console.ln("WarpedImageFolder -> saveImages() -> created directory at : " + path);
				isFolderValid = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else isFolderValid = true;
		
		if(!isFolderValid) {
			Console.err("WarpedImageFolder -> saveImages() -> folder is not valid : " + path + " -> could not save");
			return;
		}
		
		Console.ln("WarpedImageFolder -> saveImages() -> saving folder : " + 1 + " -> to the location : " + path);
		images.forEach((t, img) -> {
			String imgPath = "" + path + "/" + t.toString() +".png";
			
			File file = new File(imgPath);
			try {
				ImageIO.write(img, "png", file);
				Console.ln("WarpedImageFolder -> saveImages() -> saved image : " + t.toString() + " -> at location : " + imgPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		
	
	}
	
}
