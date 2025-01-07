/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.enums.WarpedLinkable;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsString;

public class WarpedSpriteFolder<T extends WarpedLinkable<? extends Enum<?>>> extends WarpedImageFolder<T> {

	private int spriteWidth, spriteHeight;
	
	private HashMap<T, WarpedSpriteSheet> spriteSheets = new HashMap<>();
	
	public static <K extends WarpedLinkable<? extends Enum<?>>> WarpedSpriteFolder<K> generateFolder(K folderType, String folderPath){
		return new WarpedSpriteFolder<K>(folderType, folderPath);
	}
	
	protected WarpedSpriteFolder(T folderType, String folderPath) {
		this.folderType = folderType;
		this.folderPath = folderPath;
				
		Console.ln("WarpedSpriteFolder -> Folder Name : " + 1 + " -> Folder Type :  " + folderType.getClass() + " ->  Folder Path : " + folderPath);
		
		if(isFolderValid()) {
			if(isPathsSet()) {
				if(isLinkDataFolderValid()) {
					if(updateLinkData()) {
						loadDataFromFolder();
					} else {
						Console.err("WarpedSpriteFolder -> failed to update linkData -> can't proceede");
						return;
					}
				} else {
					Console.err("WarpedSpriteFolder -> couldn't validate linkData folder, can't proceede");
					return;
				}	
			} else {
				Console.err("WarpedSpriteFolder -> folder and file paths were not set, can't proceede");
				return;
			}
		} else {
			if(isPathsSet()) {				
				if(readLinkData()) {
					loadLinkDataPaths();
				} else {
					Console.err("WarpedSpriteFolder -> couldn't read linkData, can't proceede");
					return;
				}
			} else {
				Console.err("WarpedSpriteFolder -> paths were not set to load linkData, can't proceede");
				return;
			}
		}			
		
	}
	
	protected final void loadDataFromFolder() {
		Console.condition("WarpedSpriteFolder -> loadDataFromFolder() -> trying to load images from folder : " + folder.getPath());
		@SuppressWarnings("unchecked")
		ArrayList<T> imageTypes = (ArrayList<T>) folderType.getAll();
		for(int i = 0; i < contents.length; i++) {
			
			File file = contents[i];
			Console.condition("WarpedSpriteFolder -> loadDataFromFolder() -> trying to load image from the file : " + file.getPath());
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
				getSpriteSize(file.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(image == null) {
				Console.err("WarpedSpriteFolder -> loadDataFromFolder() -> failed to read image from file");
				continue;
			}
			spriteSheets.put(imageTypes.get(i), new WarpedSpriteSheet(image, spriteWidth, spriteHeight));
		}
		if(spriteSheets.size() != contents.length) {
			Console.err("WarpedSpriteFolder -> loadDataFromFolder() -> image size doesn't match number of types");
			return;
		} else Console.met("WarpedSpriteFolder -> loadDataFromFolder() -> loaded " + spriteSheets.size() + " images");
		
	}
	
	protected final void loadLinkDataPaths() {
		@SuppressWarnings("unchecked")
		ArrayList<T> imageTypes = (ArrayList<T>) folderType.getAll();
		
		Console.condition("WarpedSpriteFolder -> loadDataPaths() -> trying to load images");
		for(int i = 0; i < filePaths.size(); i++) {
			String filePath = filePaths.get(i);
			
			URL url = getClass().getResource(filePath);
			if(url == null) {
				Console.err("WarpedSpriteFolder -> loadDataPaths() -> couldn't find resource at path : " + filePath);
				continue;
			}
			
			BufferedImage image = null;
			try {
				image = ImageIO.read(url);
				getSpriteSize(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(image == null) {
				Console.err("WarpedSpriteFolder -> loadDataPaths() -> failed to read image from url : " + url.toString());
				continue;
			}
			spriteSheets.put(imageTypes.get(i), new WarpedSpriteSheet(image, spriteWidth, spriteHeight));
		}
		
		if(spriteSheets.size() == imageTypes.size()) Console.met("WarpedImageFolder -> loadDataPaths() -> loaded images from dataPath");
		else Console.err("WarpedSpriteFolder -> loadDataPaths() -> failed to load data");
		
	}
	
	private final void getSpriteSize(String sheetPath) {
		Console.ln("WarpedSpriteFolder -> getSpiteSize() -> path : " + sheetPath);
		if(!sheetPath.endsWith("_wf.png")) {
			Console.err("WarpedSpriteFolder -> getSpiteSize() -> sprite sheet path dosen't end with _wf.png, must be in the format  : (name)_wf2d_(width)_(height)_wf.png -> Omitting brackets");
			return;
		};
		if(!sheetPath.contains("_wf2d_")) {
			Console.err("WarpedSpriteFolder -> getSpiteSize() ->  sprite sheet path doesn't contain _wf2d_, must be in the format  : (name)_wf2d_(width)_(height)_wf.png -> Omitting brackets");
		}
		
		String testString 	= "";
		boolean testing   	= false;
		String start  	  	= "wf2d_";
		int testCount 	  	= 0;
		
		boolean readWidth 	= false;
		String width  	  	= "";
		
		boolean readHeight 	= false;
		String height 		= "";

		boolean checkWidthAndHeight = false;

		String character = "";
		for(int i = 0; i < sheetPath.length(); i++) {
			character = sheetPath.substring(i, i + 1);
			
			if(testing) {
				testString += character;
				testCount++;
				if(testString.equals(start)) {
					readWidth  = true;
					testing    = false;
					i++;
					character = sheetPath.substring(i, i + 1);
				}
				if(testCount > start.length()) {
					Console.ln("WarpedSpriteFolder -> checking..");
					testing = false;
					testCount = 0;
					testString = "";
				}
			}
			
			if(readWidth) {
				if(character.equals("_")) {
					readWidth  = false;
					readHeight = true;
					i++;
					character = sheetPath.substring(i, i + 1);
				} else {
					width += character;
				}
			}
			
			
			if(readHeight) {
				if(character.equals("_")) {
					readHeight = false;
					checkWidthAndHeight = true;
				} else {
					height += character;
				}
			}
			
			if(character.equals("_") && testing) {
				testCount = 0;
				testString = "";
			}
			
			if(character.equals("_") && !readWidth && !readHeight && !checkWidthAndHeight) {
				testing = true;
			}
		}
		
		if(!checkWidthAndHeight) {
			Console.err("WarpedSpriteFolder -> did not find start key word, the file must be named in the format : (name)_wf2d_(spriteWidth)_(spriteHeight)_wf.png -> Omitting brackets"); 
			return;
		}  
		
		spriteWidth =  UtilsString.convertStringToInt(width);
	    spriteHeight = UtilsString.convertStringToInt(height);
	    Console.ln("WarpedSpriteFolder -> (spriteWidth, spriteHeight)  :  (" + spriteWidth + ", " + spriteHeight + ")");
	}
	
	
	public String getName() {return folderName;}	
	public WarpedSpriteSheet getSheet(T sheetName) {
		if(spriteSheets.containsKey(sheetName)) return spriteSheets.get(sheetName);
		else {
			Console.err("WarpedSpriteFolder -> " + folderName + " -> getSheet() -> doesn't contain a sheet called : " + sheetName);
			return null;
		}
	}
	public BufferedImage getSprite(T sheetName, int x, int y) {return getSheet(sheetName).getSprite(x, y);}
	public BufferedImage getSprite(T sheetName, int index) {return getSheet(sheetName).getSprite(index);}		
	
}
