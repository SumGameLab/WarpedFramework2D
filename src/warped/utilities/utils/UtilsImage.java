/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import warped.WarpedProperties;
import warped.application.state.WarpedFramework2D;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.enums.generalised.ColorComponentType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.VectorI;

public class UtilsImage {

	public static final Composite clearComposite = AlphaComposite.getInstance(AlphaComposite.CLEAR);
	public static final Composite drawComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
	
	private static final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	
	public static final VolatileImage generateVolatileImage(int width, int height) {
		VolatileImage image = gc.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
		clearImage(image);
		return image;
	}
	
	
	public static void clearImage(VolatileImage image) {
		Graphics2D g2d = image.createGraphics();
		g2d.setComposite(UtilsImage.clearComposite);
		g2d.fillRect(0, 0, image.getSnapshot().getWidth(), image.getSnapshot().getHeight());
		g2d.setComposite(UtilsImage.drawComposite);
		g2d.dispose();
	}
	
	//--------
	//---------------- Loading ---------------
	//--------
	public static BufferedImage loadBufferedImage(Class<?> clazz, String path) {
		BufferedImage image = null;
		URL url = null;
	
		if(clazz.getModule().getClassLoader().getResource(path.substring(3)) == null){
			Console.err("UtilsImage -> loadBufferedImage -> couldn't find resource at: " + path.substring(3));
		} else {
			url = clazz.getModule().getClassLoader().getResource(path.substring(3));
			Console.ln("UtilsImage -> loadBufferedImage() -> url Framework Class : " + url.toString());
		}
		
		if(url == null) {
			Console.err("UtilsImage -> loadBufferedImage -> couldn't find resource :( ");
			return FrameworkSprites.error;
		} else try {
			image = ImageIO.read(url);
			Console.ln("UtilsImage -> loadBufferedImage() -> loaded image from : " + url.getPath());
			return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Console.err("UtilsImage -> loadBufferedImage() -> failed to loaded bufferedImage from path : " + url);
		return FrameworkSprites.error;
		
	}
	
	public static BufferedImage basicLoadBufferedImage(String path) {
		BufferedImage image = null;
        try {
            File file = new File(path);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error loading image from file: " + e.getMessage());
            e.printStackTrace();
        }
        return image;
	}
	
	public static BufferedImage loadBufferedImage(String path) {
		BufferedImage image = null;
		URL url = null;
	
		if(WarpedFramework2D.class.getResource(path.substring(3)) == null){
			Console.err("UtilsImage -> loadBufferedImage -> couldn't find resource at: " + path.substring(3));
		} else {
			url = WarpedFramework2D.class.getResource(path.substring(3));
			Console.blueln("UtilsImage -> loadBufferedImage() -> url Framework Class : " + url.toString());
		}
		
		if(url == null) {
			Console.err("UtilsImage -> loadBufferedImage -> couldn't find resource :( ");
			return FrameworkSprites.error;
		} else try {
			image = ImageIO.read(url);
			Console.ln("UtilsImage -> loadBufferedImage() -> loaded image from : " + url.getPath());
			return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Console.err("UtilsImage -> loadBufferedImage() -> failed to loaded bufferedImage from path : " + url);
		return FrameworkSprites.error;
		
	}
		
	/**Generate a sprite sheet from a folder of animation frames.
	 * @param path - the path of the folder containing the animation frames.
	 * @return BufferedImage - returns a buffered image containing the sprite strip.
	 * @apiNote frames are loaded by file name alphabetically. 
	 * @author 5som3*/
	public static BufferedImage generateSpriteSheet(String path) {
		BufferedImage result = null;
		
		File folder = new File(path);
		if(!folder.exists()) {
			Console.err("UtilsImage -> constructSpriteSheet() -> no folder exists at path : " + path);
			return result;
		}
		
		File[] imageFiles = folder.listFiles();
		if(imageFiles.length == 0) {
			Console.err("UtilsImage -> constructSpriteSheet() -> folder exists but contains no files");
			return result;
		}
		
		int frameWidth  = 0;
		int frameHeight = 0;
		ArrayList<BufferedImage> frames = new ArrayList<>();
		for(int i = 0; i < imageFiles.length; i++) {
			try {
				frames.add(ImageIO.read(imageFiles[i]));
			} catch (IOException e) {
				Console.err("UtilsImage -> constructSpriteSheet() -> failed to load frame from file : " + i);
				Console.stackTrace(e);
			}
			if(i == 0) {
				frameWidth  = frames.get(0).getWidth();
				frameHeight = frames.get(0).getHeight();
			}
			if(frames.get(i).getWidth() != frameWidth) {
				Console.err("UtilsImage -> constructSpriteSheet() -> animation inconsistent, width of image " + i + " does not match the frameWidth : " + frameWidth);
				return result;
			}
			if(frames.get(i).getHeight() != frameHeight) {
				Console.err("UtilsImage -> constructSpriteSheet() -> animation inconsistent, height of image " + i + " does not match the frameHeight : " + frameHeight);
				return result;
			}
		}
		
		result = new BufferedImage(frameWidth * frames.size(), frameHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = result.getGraphics();
		for(int i = 0; i < frames.size(); i++) g.drawImage(frames.get(i), i * frameWidth, 0, frameWidth, frameHeight, null);
		g.dispose();
		return result;
	}
	
	/**Remove all opacity from an image
	 * @param image - the image to remove opacity from
	 * @return BufferedImage - a new image based on the input but all pixels have 255 alpha (solid colour)
	 * @apiNote Doesn't modify the original image, returns a new buffered image based on the input image.
	 * @author 5som3*/
	public static BufferedImage removeOpacity(BufferedImage image) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				int ogColour = image.getRGB(x, y);
				
				int alpha = (ogColour >> 24) & 0xFF;
				int red   = (ogColour >> 16) & 0xFF;
			    int green = (ogColour >> 8) & 0xFF;
			    int blue  = ogColour & 0xFF;
				
			    int paintColour = 0;
			    if(alpha < 10) paintColour = (0 << 24) | (0 << 16) | (0 << 8) | 0;
			    else paintColour = (255 << 24) | (red << 16) | (green << 8) | blue;
				
				result.setRGB(x, y, paintColour);
			}
		}
		return result;		
	}
	
	/**Write a buffered image to file.
	 * @param image - the image to write to file.
	 * @param outputFileNamePath - the file path and file name as a string i.e. /folder/file_name
	 * @return boolean - true if the file was written to file, else false.  
	 * @apiNote Function assumes that the folder already exists, will fail if location doesn't exist.  
	 * @author 5som3*/
	public static boolean writeImageToFile(BufferedImage image, String outputFileNamePath) {		
		File output = new File(outputFileNamePath + ".png");
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			Console.err("UtilsImage -> writeImageToFile() -> failed to write file");
			Console.stackTrace(e);
			return false;
		}
		return true;
	}
	
	//--------
	//---------------- Folder Macros ---------------
	//--------
	public static void paintBackground(String folderPath, Color color) {
		File folder = new File(folderPath);
		if(!folder.exists()) {
			Console.err("UtilsImage -> paintBackground() -> folder does not exist");
			return;
		}
		
		File[] contents = folder.listFiles();
 		if(contents.length == 0) {
 			Console.err("UtilsImage -> paintBackground() -> the folder does not contain any files");
 			return;
 		}
 		
 		for(int i = 0; i < contents.length; i++) {
 			File file = contents[i];
 			if(!file.getName().endsWith(".png")) {
 				Console.err("UtilsImage -> paintBackground() -> the file : " + file.getName() + " -> is not a png and will be skipped");
 				continue;
 			}
 			
 			String imgPath = "" + folderPath + "/" + file.getName();
 			BufferedImage img = loadBufferedImage(imgPath);
 			
 			BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 			Graphics g = result.getGraphics();
 			g.setColor(color);
 			g.fillRect(0, 0, result.getWidth(), result.getHeight());
 			g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
 			g.dispose();
 			
 			try {
				ImageIO.write(result, "png", file);
				Console.ln("UtilsImage -> paintBackground() -> painted image background : " + file.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
 			
 		}
 		
 		Console.ln("UtilsImage -> paintBackground() -> paint job complete, have a nice day :)");
		
	}
	
	public static void makeCopys(String outputFolder, BufferedImage img, int quantity, String name, int indexOffset) {
		if(img == null) {
			Console.err("UtilsImage -> makeCopys() -> the image passed was null");
			return;
		}
		
		if(quantity < 1 || quantity > 1000) {
			Console.err("UtilsImage -> makeCopys() -> quantity is out of bounds : " + quantity);
			return;
		}
		
		File folder = new File(outputFolder);
		if(!folder.exists()) {
			folder.mkdirs();
			Console.ln("UtilsImage -> makeCopys() -> created output folder at : " + outputFolder);
		}
		
		Console.ln("UtilsImage -> makeCopys() -> making " + quantity + " copys at location : " + outputFolder);
		for(int i = 0; i < quantity; i++) {
			BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
			Graphics g = result.getGraphics();
			g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
			g.dispose();
			
			int index = i + indexOffset;
			String fileName = name + index;
			String filePath = "" + outputFolder + "/" + fileName + ".png";
			
			File file = new File(filePath);
			try {
				ImageIO.write(result, "png", file);
				Console.ln("UtilsImage -> makeCopys() -> made a copy called : " + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		Console.ln("UtilsImage -> finished image copys() -> have a nice day :)");
	}
	
	
	
	
	//--------
	//---------------- Cloning ---------------
	//--------	
	public static BufferedImage generateFadedClone(BufferedImage image, double fade) {
		if(fade >= 1.0) {
			Console.err("UtilsImage -> generateFadedClone() -> Fade must be less than 1.0 : " + fade);
			fade = 0.99;
		} else if (fade <= 0.0) {
			Console.err("UtilsImage -> generateFadedClone() -> Fade must be greater than 0.0 : " + fade);
			fade = 0.01;
		}
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = result.getGraphics();
		
		g.dispose();
		return result;
	}
	public static BufferedImage generateClone(BufferedImage image) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = result.createGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		g.dispose();
		return result;
	}
	
	public static BufferedImage generateGradientClone(BufferedImage image, DirectionType fadeDirection, double fadeOffset) {
		if(fadeOffset < 0.1 || fadeOffset > 0.8) {
			Console.err("UtilsImage -> generateGradientClone() -> fadeOffset out of bounds : " + fadeOffset);
			fadeOffset = 0.25;
		}
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		BufferedImage result = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		
		switch(fadeDirection) {
		case LEFT:		
			for(int x = 0; x < width; x++) {
				double progress = 0.0;
				if(x != 0) progress = (double)x/(double)width;
				progress -= progress * fadeOffset;
				
				if(progress > 1.0) progress = 1.0;
				if(progress < 0.0) progress = 0.0;
				
				for(int y = 0; y < height; y++) {
					int color = image.getRGB(x, y);
		            color = (color & 0xffffff) | ((int)(255 * progress) << 24);
		            result.setRGB(x, y, color);
				}
			}
			break;
		case RIGHT:
			for(int x = 0; x < width; x++) {
				double progress = 0.0;
				if(x != 0) progress = 1.0 - ((double)x/(double)width);
				progress -= progress * fadeOffset;
				
				if(progress > 1.0) progress = 1.0;
				if(progress < 0.0) progress = 0.0;
				
				for(int y = 0; y < height; y++) {
					int color = image.getRGB(x, y);
					color = (color & 0xffffff) | ((int)(255 * progress) << 24);
					result.setRGB(x, y, color);
				}
			}
			break;
		case DOWN:
			for(int y = 0; y < height; y++) {
				double progress = 0.0;
				if(y != 0) progress = (double)y/(double)width;
				progress -= progress * fadeOffset;
				
				if(progress > 1.0) progress = 1.0;
				if(progress < 0.0) progress = 0.0;
				
				for(int x = 0; x < height; x++) {
					int color = image.getRGB(x, y);
		            color = (color & 0xffffff) | ((int)(255 * progress) << 24);
		            result.setRGB(x, y, color);
				}
			}
			break;
		case UP:
			for(int x = 0; x < width; x++) {
				double progress = 0.0;
				if(x != 0) progress = (double)x/(double)width;
				progress -= progress * fadeOffset;
				
				if(progress > 1.0) progress = 1.0;
				if(progress < 0.0) progress = 0.0;
				
				for(int y = 0; y < height; y++) {
					int color = image.getRGB(x, y);
		            color = (color & 0xffffff) | ((int)(255 * progress) << 24);
		            result.setRGB(x, y, color);
				}
			}
			break;
		default:
			Console.err("UtilsImage -> generateGradientClone() -> invalid case : " + fadeDirection);		
		}
		return result;	
	}
	
	/**The returned image will fill the new width and height, aspect ratio will be preserved*/
	public static BufferedImage generateSizedClone(BufferedImage image, int size) {
		BufferedImage result = new BufferedImage(size, size, WarpedProperties.BUFFERED_IMAGE_TYPE);
		
		VectorI drawSize = new VectorI();
		VectorI drawOffset = new VectorI();
		
		int width = image.getWidth();
		int height = image.getHeight();

		double aspectRatio = width / height;
		
		if(width == height) {
			drawSize.set(size, size);				
			drawOffset.set(0,0);
		} else if(width > height) {
			drawSize.scale(size, (int)(width / aspectRatio)); 
			drawOffset.set(0, (height - drawSize.y()) / 2);
		} else {
			drawSize.set(size, (int)(height * aspectRatio));
			drawOffset.set(0, (width - drawSize.y()) / 2);  
		}

		Graphics g = result.getGraphics();
		g.drawImage(image, drawOffset.x(), drawOffset.y(), drawSize.x(), drawSize.y(), null);
		g.dispose();

		return result;
	}
	
	/**The returned image will be scaled to the new width and height, aspect ratio will not be preserved*/
	public static BufferedImage generateFixedSizeClone(BufferedImage image, int width, int height) {
		BufferedImage result = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = result.getGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return result;
	}
	
	public static BufferedImage generateScaledClone(BufferedImage image, double scale) {
		if(scale < 0.0) {
			Console.err("UtilsImage -> generateScaledClone() -> scale is invalid : " + scale);
			return null;
		}
		int sx = (int)(image.getWidth() * scale);
		int sy = (int)(image.getHeight() * scale);
		BufferedImage result = new BufferedImage(sx, sy, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = result.getGraphics();
		g.drawImage(image, 0, 0, sx, sy, null);
		g.dispose();
		return result;
	}
	
	public static BufferedImage generateTintedClone(BufferedImage original, int alpha, Colour colour) {
		BufferedImage result = generateClone(original);
		Graphics g = result.getGraphics();
		g.setColor(Colour.getColor(colour, alpha));
		g.fillRect(0, 0, result.getWidth(), result.getHeight());
		g.dispose();
		return result;
	}
	
	public static BufferedImage generateAlphaClone(BufferedImage original, byte alpha) {
		BufferedImage result = generateClone(original);
		UtilsImage.setAlpha(result, alpha);
		return result;
	}	

	public static BufferedImage generateBrightnessClone(BufferedImage original, int amount) {
		BufferedImage result = generateClone(original);
        changeBrightness(result, amount);
        return result;
	}
	
	//--------
	//---------------- Outline ---------------
	//--------
	public static BufferedImage outlineClone(BufferedImage original, Colour outlineColour) {return outlineClone(original, outlineColour.getColor());}
	public static BufferedImage outlineClone(BufferedImage original, Color outlineColor) {return outlineClone(original, outlineColor, false);}
	public static BufferedImage outlineClone(BufferedImage original, Color outlineColor, boolean fillCorners) {return outlineClone(original, outlineColor, fillCorners, Colour.EMPTY.getColor());}
	public static BufferedImage outlineClone(BufferedImage original, Color outlineColor, boolean fillCorners, Color emptyColor) {
		BufferedImage result = generateClone(original);
		for(int y = 0; y < result.getHeight(); y++) {
			for(int x = 0; x < result.getWidth(); x++) {
				if(Colour.isEqual(result.getRGB(x, y), emptyColor)){
					
					if(fillCorners) {
						for(int i = 0; i < 9; i++) { //Check surrounding tiles
							if(i == 4) continue;
							
							int xi = (i % 3) - 1;
							int yi = (i / 3) - 1;
							
							int xx = x + xi;
							int yy = y + yi;
							
							if(!Colour.isEqual(result.getRGB(xx, yy), emptyColor)) {//has neighbour 
								result.setRGB(x, y, outlineColor.getRGB());//set outline
								break;
							}
						}
					} else {
						for(int i = 1; i < 8; i++) { //Check surrounding tiles excluding corners
							if(i % 2 == 0) continue;
							
							int xi = (i % 3) - 1;
							int yi = (i / 3) - 1;
							
							int xx = x + xi;
							int yy = y + yi;
							
							if(!Colour.isEqual(result.getRGB(xx, yy), emptyColor)) { //has neighbour
								result.setRGB(x, y, outlineColor.getRGB());	//set outline
								break;
							}
							
						}
					}
					
				} 
			}
		}
		
		return result;
	}
	
	//--------
	//---------------- Tint ---------------
	//--------
	
	public static void tintRegion(BufferedImage raster, Colour colour, int x, int y, int w, int h) {
		if(x < 0 || y < 0 || x > raster.getWidth() || y > raster.getHeight()) {
			Console.err("UtilsImage -> tintRegion() -> invalid region");
			return;
		}
		Graphics g = raster.getGraphics();
		g.setColor(colour.getColor());
		g.fillRect(x, y, w, h);
		g.dispose();
		
	}
	
	public static void tintRegion(BufferedImage raster, Colour colour, int alpha, int x, int y, int w, int h) {
		if(x < 0 || y < 0 || x > raster.getWidth() || y > raster.getHeight()) {
			Console.err("UtilsImage -> tintRegion() -> invalid region");
			return;
		}
		Graphics g = raster.getGraphics();
		g.setColor(colour.getColor(alpha));
		g.fillRect(x, y, w, h);
		g.dispose();
		
	}
	
	//--------
	//---------------- Alpha ---------------
	//--------
	public static void setAlpha(BufferedImage image, byte alpha) {       
	    alpha %= 0xff; 
	    for (int y = 0; y <image.getHeight(); y++) {          
	        for (int x = 0; x < image.getWidth(); x++) {
	            int color = image.getRGB(x, y);

	            int mc = (alpha << 24) | 0x00ffffff;
	            int newcolor = color & mc;
	            image.setRGB(x, y, newcolor);            
	            
	            
	        }

	    }
	}
	

	//--------
	//---------------- Masking ---------------
	//--------
	public static int getColorComponent(BufferedImage img, int x, int y, ColorComponentType colType) {
		switch(colType) {
		case ALPH: 	return getAlpha(img.getRGB(x, y));
		case BLUE:	return getBlue(img.getRGB(x, y));
		case GREEN: return getGreen(img.getRGB(x, y));
		case RED: 	return getRed(img.getRGB(x,y));
		default: Console.err("UtilsImage -> getColorComponent() -> invalid case : " + colType); return -1;	
		}
	}
	public static Color getColor(BufferedImage img, int x, int y) {
		int col = img.getRGB(x, y);
		return new Color(getRed(col), getGreen(col), getBlue(col), getAlpha(col));
	}
	public static boolean isMatchingColor(BufferedImage img, int x, int y, Color col1) {
		boolean result = true;
		Color col2 = getColor(img,x,y);
		if(col1.getRed()   != col2.getRed())   result = false;
		if(col1.getGreen() != col2.getGreen()) result = false;
		if(col1.getBlue()  != col2.getBlue())  result = false;
		if(col1.getAlpha() != col2.getAlpha()) result = false;
		return result;}
	public static int getBlue(int argb)  {return (0xFF & (argb >>  0));}
	public static int getGreen(int argb) {return (0xFF & (argb >>  8));}
	public static int getRed(int argb)   {return (0xFF & (argb >> 16));}
	public static int getAlpha(int argb) {return (0xFF & (argb >> 24));}
	
	
	//--------
	//---------------- Brightness ---------------
	//--------
	public static void changeBrightness(BufferedImage original, int amount) {
		int[] resultPixels = ((DataBufferInt)original.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {

                int argb  = resultPixels[x + y * original.getWidth()]; 
                int alpha = 0xFF & (argb >> 24);
                int red   = 0xFF & (argb >> 16);
                int green = 0xFF & (argb >>  8);
                int blue  = 0xFF & (argb >>  0);

                
                red   = UtilsMath.clamp(red   + amount, 0, 255); // Can be called like this thanks to import
                green = UtilsMath.clamp(green + amount, 0, 255);
                blue  = UtilsMath.clamp(blue  + amount, 0, 255);

                resultPixels[x + y * original.getWidth()] = (alpha << 24) | (red << 16 ) | (green<<8) | blue;
            }
        }
	}
	
	
	
	
	//--------
	//---------------- Rotation ---------------
	//--------
	public static BufferedImage generateRotatedSizedCloneDegrees(BufferedImage img, double degrees) {
	    double rads = Math.toRadians(degrees);
	    return generateRotatedSizedCloneRads(img, rads);
	}
	
	public static BufferedImage generateRotatedSizedCloneRads(BufferedImage img, double radians) {
		//WarpedConsole.ln("ImageUtils -> generateRotatedCloneRads() -> angle of rotation : " + angle);
		
		double angle = UtilsMath.clampRadianRotation(radians);
		if(angle == 0) return generateClone(img);
			   
		double sin = Math.abs(Math.sin(angle));
		double cos = Math.abs(Math.cos(angle));
		
		int originalWidth  = img.getWidth();
		int originalHeight = img.getHeight();
		
		double newWidth  = originalWidth  * cos + originalHeight * sin;
		double newHeight = originalHeight * cos + originalWidth  * sin;
				
		double translationX = Math.floor((newWidth  - originalWidth)  / 2);
		double translationY = Math.floor((newHeight - originalHeight) / 2);
		
		int rotationOriginX = (originalWidth  / 2);    //The point around which the image will be rotated, in this case the center point
		int rotationOriginY = (originalHeight / 2);
		
		AffineTransform at = new AffineTransform();
		at.translate(translationX , translationY);
		at.rotate(angle, rotationOriginX, rotationOriginY);
		
		BufferedImage result = new BufferedImage((int)newWidth, (int)newHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics2D g2d = result.createGraphics();
		g2d.drawRenderedImage(img, at);
		return result;
	
	}	
	
	public static BufferedImage generateRotatedCloneRads(BufferedImage img, double radians) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage rotated = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics2D g2d = rotated.createGraphics();
		
		int rotationOriginX = (width  / 2);
		int rotationOriginY = (height / 2);
		
		AffineTransform at = new AffineTransform();
		at.rotate(radians, rotationOriginX, rotationOriginY);
		
		g2d.drawRenderedImage(img, at);
		g2d.dispose();
		
		return rotated;
	}
	
	//--------
	//---------------- Scale ---------------
	//--------
	/**@Return BufferedImage -  the result is scaled internally. The width and height of the result are the same as the input image but the content will be scaled*/
	public static BufferedImage generateScaledClone(BufferedImage img, int delta) {
		if(delta <= - img.getWidth() || delta <= - img.getHeight()) {
			Console.err("UtilsImage -> generateScaledClone() -> scale change is too small, can not be less than -imgSize");
			if(img.getWidth() < img.getHeight()) delta = -img.getWidth() + 1;
			else delta = -img.getHeight() + 1;
		}
		
		int oWidth  = img.getWidth();
		int oHeight = img.getHeight();
		
		int nWidth  = img.getWidth()  + delta;
		int nHeight = img.getHeight() + delta;
				
		int x = (oWidth  / 2) - (nWidth  / 2);
		int y = (oHeight / 2) - (nHeight / 2);
		
		BufferedImage scaled = new BufferedImage(oWidth, oHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = scaled.getGraphics();
		g.drawImage(img, x, y, nWidth, nHeight, null);
		g.dispose();
	
		return scaled;		
	}
	
	
	//--------
	//---------------- Translation ---------------
	//--------
	public static BufferedImage generateTranslatedClone(BufferedImage img, int translationX, int translationY) {
		if(img == null) {
			Console.err("UtilsImage -> generateTranslatedClone() -> img parameter is null");
			return null;
		}
		
		int oWidth = img.getWidth();
		int oHeight = img.getHeight();
		
		if(translationX > oWidth || translationX < -oWidth || translationY > oHeight || translationY < -oHeight) {
			Console.err("UtilsImage -> generateTranslatedClone() -> out of bounds : " + translationX + ", " + translationY);
			return null;
		}
		
		
		BufferedImage translated = new BufferedImage(oWidth, oHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = translated.getGraphics();
		g.drawImage(img, translationX, translationY, oWidth, oHeight, null);
		g.dispose();
	
		return translated;
	}
	
	
	//--------
	//---------------- Alpha Blending ---------------
	//--------
	/**
	 * DO NOT USE -> HAS BEEN TESTED AND HAS A BUG -> NEEDS TO BE DEBUGGED 
	 * @param uli = underlay image
	 * @param oli = overlay image
	 * @return a buffered image resulting from alpha blending each pixel*/
	public static BufferedImage alphaBlendImages(BufferedImage uli, BufferedImage oli) {
		if(uli.getWidth() != oli.getWidth() || uli.getHeight() != oli.getHeight()) {
			Console.err("UtilsImage -> alphaBlendImages -> images are of incompatible sizes");
			return null;
		}
		int width  = uli.getWidth();
		int height = uli.getHeight();
		BufferedImage result = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int resultPixel = alphaBlendColors(uli.getRGB(x, y), oli.getRGB(x, y));
				result.setRGB(x, y, resultPixel);
			}
		}
		
		return result;	
	}
	
	/**
	 * DO NOT USE -> HAS BEEN TESTED AND HAS A BUG -> NEEDS TO BE DEBUGGED 
	 * @param ulc = underlay colour
	 * @param olc = overlay colour*/
	public static Color alphaBlendColors(Color ulc, Color olc) { 
		Color result = new Color(0,0,0,0);
		int alpha = 255 - (255 - olc.getAlpha()) * (255 - ulc.getAlpha());
		if(alpha <= 0) return result; //result is fully transparent
		
		int red   = olc.getRed()   * olc.getAlpha() / alpha + ulc.getRed()   * ulc.getAlpha() * (255 - olc.getAlpha()) / alpha;
		int green = olc.getGreen() * olc.getAlpha() / alpha + ulc.getGreen() * ulc.getAlpha() * (255 - olc.getAlpha()) / alpha;
		int blue  = olc.getBlue()  * olc.getAlpha() / alpha + ulc.getBlue()  * ulc.getAlpha() * (255 - olc.getAlpha()) / alpha;
		
		result = new Color(red, green, blue, alpha); 
		return result;
	}
	
	
	/**
	 *@param ulc = underlay colour
	 *@param olc = overlay colour */ 
	public static int alphaBlendColors(int ulc, int olc) {
	 	 int result = 0;
		 
		 int uAlpha = 0xFF & (ulc >> 24);
         int uRed   = 0xFF & (ulc >> 16);
         int uGreen = 0xFF & (ulc >>  8);
         int uBlue  = 0xFF & (ulc >>  0);
         
         int oAlpha = 0xFF & (olc >> 24);
         int oRed   = 0xFF & (olc >> 16);
         int oGreen = 0xFF & (olc >>  8);
         int oBlue  = 0xFF & (olc >>  0);

         int alpha = 255 - (255 - oAlpha) * (255 - uAlpha);
         if(alpha <= 0) return result; //result is fully transparent
         
         int red   = oRed   * oAlpha / alpha + uRed   * uAlpha * (255 - oAlpha) / alpha;
 		 int green = oGreen * oAlpha / alpha + uGreen * uAlpha * (255 - oAlpha) / alpha;
 		 int blue  = oBlue  * oAlpha / alpha + uBlue  * uAlpha * (255 - oAlpha) / alpha;
 		
 		result = ((alpha & 0xFF) << 24) |
				 ((red   & 0xFF) << 16) |
				 ((green & 0xFF) << 8)  |
				 ((blue  & 0xFF) << 0);
         
		return result;
	}
	
	

	
	
	/* ---------------------- Noise ---------------------- */
	/**
	 * Generates a 2D int array containing the alpha noise to generate fades / transitions */
	/*
	public static int[][] generateTransitionNoise(BufferedImage tile, TileTransitionType transitionType) {
		int width = tile.getWidth();
		int height = tile.getHeight();
		
		int[][] result = new int[width][height];  // an array of alpha values that will determine how the two tiles are blended together
		
		int transitionSteps = width / 2; // number of pixels the transition is spread over
		int stepIncrement  = 255 / transitionSteps; //alpha change per pixel
		
		switch(transitionType) {
		case INNER_CURVE_90:
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					if(x <= width / 4)		{result[x][y] = 0; continue;} //first quarter of the transition tile will be the underlay tile
					if(y <= height / 4)     {result[x][y] = 0; continue;}
				
					
					int x2 = (int) ((int) width * 1.2);
					int y2 = (int) ((int) height * 1.2);
					int distance = (int)Maths.vectorDifference(x, y, x2, y2);
					int maxDistance = width;
					int d = maxDistance - distance;
					result[x][y] = d * stepIncrement;
					
					if(Maths.random.nextBoolean()) {
						if(Maths.random.nextBoolean()) {
							result[x][y] += stepIncrement / 4;
						} else result[x][y] -= stepIncrement / 4;
					}

					if(result[x][y] > 255) result[x][y] = 255;
					if(result[x][y] < 0) result[x][y] = 0;	
				}
			}	
			return result;
			
			
		case OUTER_CURVE_90:
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					if(x <= width / 4)		{result[x][y] = 0; continue;} //first quarter of the transition tile will be the underlay tile
					if(y <= height / 4)     {result[x][y] = 0; continue;}
				
					
					int x2 = (int) ((int) width * 1.25);
					int y2 = (int) ((int) height * 1.25);
					int distance = (int)Maths.vectorDifference(x, y, x2, y2);
					int maxDistance = width;
					int d = maxDistance - distance;
					result[x][y] = d * stepIncrement;
					if(result[x][y] > 255) result[x][y] = 255;
					if(result[x][y] < 0) result[x][y] = 0;
				}
			}
			
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					result[x][y] = 255 - result[x][y];
					
					if(Maths.random.nextBoolean()) {
						if(Maths.random.nextBoolean()) {
							result[x][y] += stepIncrement / 4;
						} else result[x][y] -= stepIncrement / 4;
					}
					
					if(result[x][y] > 255) result[x][y] = 255;
					if(result[x][y] < 0) result[x][y] = 0;
					
				}
			}
			return result;

			
		case STRAIGHT:
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					if(x <= width / 5)		{result[x][y] = 0;   continue;} //first quarter of the transition tile will be the underlay tile
					if(x > (3 * width / 4)) {result[x][y] = 255; continue;} //second quarter of the transition tile will be the overlay tile 
					
					result[x][y] = (x - (width / 5)) * stepIncrement; // the middle half of  the tile will fade between the two tile layers  
					
					if(Maths.random.nextBoolean()) {
						if(Maths.random.nextBoolean()) {
							result[x][y] += stepIncrement / 4;
						} else result[x][y] -= stepIncrement / 4;
					}
				}
			}
			return result;
			
			
		default:
			WarpedConsole.err("TileGenerator -> generateTransitionNoise() -> invalid transition type case : " + transitionType);
			return null;
		
		}
	}
	*/
		
}
