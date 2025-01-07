/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class UtilsFiles {

	public static int getFilesCount(String folderPath) {
		int result;
		File folder = new File(folderPath);
		result = getCountFilesInFolder(folder);
		return result;
	}
	
	public static ArrayList<String> listFiles(String folderPath){
		ArrayList<String> result;
		File folder = new File(folderPath);
		result = listFiles(folder);		
		return result;
	}
	
	public static ArrayList<String> listFilePaths(String folderPath){
		ArrayList<String> result;
		File folder = new File(folderPath);
		result = listFilePaths(folder);		
		return result;
	}
	
	public static ArrayList<BufferedImage> getPNG(String folderPath){
		ArrayList<BufferedImage> result;
		File folder = new File(folderPath);
		result = getPNG(folder);
		return result;
	}
	
	public static BufferedImage[] getPNGArray(String folderPath){
		File folder = new File(folderPath);
		
		return getPNGArray(folder);
	}
	
	public static int getCountFilesInFolder(File folder) {
		int result = 0;
		result = folder.list().length;
		return result;
	}
	
	
	public static ArrayList<String> listFiles(File folder) {
		ArrayList<String> result = new ArrayList<>();
		if(folder == null) {
			Console.err("FileUtils -> listFilesForFoler() -> passed a null file as parameter");
			return result;
		}
		
		for(File file : folder.listFiles()) {
			result.add(file.getName());
		}
		return result;
	}
	
	public static ArrayList<String> listFilePaths(File folder) {
		ArrayList<String> result = new ArrayList<>();
		if(folder == null) {
			Console.err("FileUtils -> listFilesForFoler() -> passed a null file as parameter");
			return result;
		}
		
		for(File file : folder.listFiles()) {
			result.add(file.getPath());
		}
		return result;
	}
	
	
	
	public static ArrayList<BufferedImage> getPNG(File folder){
		ArrayList<BufferedImage> result = new ArrayList<>();
		if(folder == null) {
			Console.err("FileUtils -> listFilesForFoler() -> passed a null file as parameter");
			return result;
		}
		
		ArrayList<Integer> pngIndex = new ArrayList<>();
		ArrayList<String> filePaths = listFilePaths(folder);
		for(int i = 0; i < filePaths.size(); i++) {
			if(filePaths.get(i).endsWith(".png")) pngIndex.add(i);
		}
		
		for(int i = 0; i < pngIndex.size(); i++) {
			result.add(UtilsImage.loadBufferedImage(filePaths.get(pngIndex.get(i))));
		}
		
		return result;
	}
	
	public static BufferedImage[] getPNGArray(File folder){
		if(folder == null) {
			Console.err("FileUtils -> listFilesForFoler() -> passed a null file as parameter");
			return null;
		}
		ArrayList<String> filePaths = listFilePaths(folder);
		BufferedImage[] result = new BufferedImage[filePaths.size()];
		
		ArrayList<Integer> pngIndex = new ArrayList<>();
		for(int i = 0; i < filePaths.size(); i++) {
			if(filePaths.get(i).endsWith(".png")) pngIndex.add(i);
		}
		
		for(int i = 0; i < pngIndex.size(); i++) {
			result[i] = UtilsImage.loadBufferedImage(filePaths.get(pngIndex.get(i)));
		}
		
		return result;
	}

}
