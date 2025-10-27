/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite.spriteSheets;

import java.awt.image.BufferedImage;

import warped.graphics.sprite.ButtonSprite;
import warped.graphics.sprite.RotationSprite;
import warped.graphics.sprite.ToggleSprite;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public class WarpedSpriteSheet {
	
	private int sheetWidth;  //pixel width of the sheet 
	private int sheetHeight; //pixel height of the sheet 
	
	protected BufferedImage[] rawSpriteImages; //array of sub images derived from the sheet 
	private int spriteWidth;  //pixel width of a single sprite 
	private int spriteHeight; //pixel height of a single sprite  	
		
	private int spriteXCount; 
	private int spriteYCount; 
	private int spriteCount;  

	
	/**Create a sprite sheet from a buffered image.
	 * @param sheet - a buffered image containing the sprites.
	 * @param spriteWidth - the width of an individual sprite on the sheet.
	 * @param spriteHeight - the height of an individual sprite on the sheet.
	 * @author 5som3 */
	public WarpedSpriteSheet(BufferedImage sheet, int spriteWidth,int spriteHeight) {	    
		if(sheet == null) {
			Console.err("WarpedSpriteSheet -> sheet could not be read from jar");
			return;
		} 
		
		Console.ln("WarpedSpriteSheet -> creating new sprite sheet..");		
		sheetWidth = sheet.getWidth();
		sheetHeight = sheet.getHeight();
		
		if(spriteWidth > sheetWidth || spriteHeight > sheetHeight) {
			Console.err("WarpedSpriteSheet -> spriteWidth or height is larger than sheet size -> sprite/sheet size : " + "( " + spriteWidth + ", " + spriteHeight + ") (" + sheetWidth + ", " + sheetHeight + ")");
			spriteWidth = sheetWidth;
			spriteHeight = sheetHeight;
		}
		if(sheetWidth % spriteWidth != 0 || sheetHeight % spriteHeight != 0) {
			Console.err("WarpedSpriteSheet -> sheet width or height is not a multiple of the spriteWidth or height, this means their is either wasted space or their is a mistake on the sprite sheet");
		}
		
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		spriteXCount = sheetWidth / spriteWidth;
		spriteYCount = sheetHeight / spriteHeight;
		spriteCount = spriteXCount * spriteYCount;
		rawSpriteImages = new BufferedImage[spriteCount];
		int i = 0;
		for(int y = 0; y < spriteYCount; y++) {
			for(int x = 0; x < spriteXCount; x++) {
				rawSpriteImages[i] = sheet.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
				i++;
			}
		}
		Console.ln("WarpedSpriteSheet -> Number of sprites on sheet : " + spriteCount);
		Console.ln("WarpedSpriteSheet -> Sprite (Width, Height) : ("+spriteWidth+", "+spriteHeight+")");
	}

	/**Load a sheet from a file path.
	 * @param sheetPath - the path relative to the root folder.
	 * @author 5som3*/
	public WarpedSpriteSheet(String sheetPath) {
		Console.ln("WarpedSpriteSheet -> path : " + sheetPath);
		if(!sheetPath.endsWith("_wf.png")) {
			Console.err("WarpedSpriteSheet -> sprite sheet path dosen't end with _wf.png, must be in the format  : (name)_wf2d_(width)_(height)_wf.png -> Omitting brackets");
			return;
		};
		
		if(!sheetPath.contains("_wf2d_")) {
			Console.err("WarpedSpriteSheet -> sprite sheet path doesn't contain _wf2d_, must be in the format  : (name)_wf2d_(width)_(height)_wf.png -> Omitting brackets");
			return;
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
		for(int i = 0; i < sheetPath.length(); i++) { // Find sprite size by file naming convention - spritesheets must adhere to this naming convention to load
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
					Console.ln("WarpedSpriteSheet -> checking..");
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
			Console.err("WarpedSpriteSheet -> did not find start key word, the file must be named in the format : (name)_wf2d_(spriteWidth)_(spriteHeight)_wf.png -> Omitting brackets"); 
			return;
		}  
		
		spriteWidth =  UtilsString.convertStringToInt(width);
	    spriteHeight = UtilsString.convertStringToInt(height);
	    Console.ln("WarpedSpriteSheet -> (spriteWidth, spriteHeight)  :  (" + spriteWidth + ", " + spriteHeight + ")");
	    		
		BufferedImage sheet = UtilsImage.loadBufferedImage(sheetPath);	
		if(sheet == null) {
			Console.err("WarpedSpriteSheet -> sheet could not be read from jar");
			return;
		}
		
		sheetWidth = sheet.getWidth();
		sheetHeight = sheet.getHeight();

		if(spriteWidth > sheetWidth || spriteHeight > sheetHeight) {
			Console.err("WarpedSpriteSheet -> spriteWidth or height is larger than sheet size -> sprite/sheet size : " + "( " + spriteWidth + ", " + spriteHeight + ") (" + sheetWidth + ", " + sheetHeight + ")");
			spriteWidth = sheetWidth;
			spriteHeight = sheetHeight;
		}
		if(sheetWidth % spriteWidth != 0 || sheetHeight % spriteHeight != 0) {
			Console.err("WarpedSpriteSheet -> sheet width or height is not a multiple of the spriteWidth or height, this means their is either wasted space or their is a mistake on the sprite sheet");
		}
		
		spriteXCount = sheetWidth / spriteWidth;
		spriteYCount = sheetHeight / spriteHeight;
		spriteCount = spriteXCount * spriteYCount;
		rawSpriteImages = new BufferedImage[spriteCount];
		int i = 0;
		for(int y = 0; y < spriteYCount; y++) {
			for(int x = 0; x < spriteXCount; x++) {
				rawSpriteImages[i] = sheet.getSubimage(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
				i++;
			}
		}
	
		Console.ln("WarpedSpriteSheet -> Number of sprites on sheet : " + spriteCount);
		Console.ln("WarpedSpriteSheet -> Sprite (Width, Height) : ("+spriteWidth+", "+spriteHeight+")");
	}
	
	/**The width of the sprites on this sheet.
	 * @return int - the width of the sprite in pixels.
	 * @author 5som3*/
	public int getSpriteWidth() {return spriteWidth;}
	
	/**The height of the sprites on this sheet.
	 * @return int - the height of the sprite in pixels.
	 * @author 5som3*/
	public int getSpriteHeight() {return spriteHeight;}
	
	/**The width of the spriteSheet.
	 * @return int - the width of the sheet in pixels.
	 * @author 5som3*/
	public int getSheetWidth() {return sheetWidth;}
	
	/**The height of the spriteSheet.
	 * @return int - the height of the sheet in pixels.
	 * @author 5som3*/
	public int getSheetHeight() {return sheetWidth;}
	
	/**The number of sprites across the sheet.
	 * @return int - the width of the sheet in sprites. 
	 * @author 5som3*/
	public int getSpriteCountX() {return spriteXCount;}
	
	/**The number of sprites down the sheet.
	 * @return int - the height of the sheet in sprites.
	 * @author 5som3*/
	public int getSpriteCountY() {return spriteYCount;}
	
	/**The total number of sprite images on this sprite sheet.
	 * @return int - the number of sprites on the sheet.
	 * @author 5som3*/
	public int getSpriteCount() {return rawSpriteImages.length;}

	/**A new ToggleSprite based on a sprite in this sheet.
	 * @param x - the x coordinate of the sprite.
	 * @param y - the y coordinate of the sprite.
	 * @return ToggleSprite - a new ToggleSprite based on the specified sprite
	 * @apiNote (x, y) are measured in sprite count i.e. (3, 2) = 3 sprites across, 2 sprites down. 
	 * @author 5som3*/
	public ToggleSprite    generateToggleSprite(int x, int y) {return new ToggleSprite(getSprite(x, y));}
	
	
	/**A new ButtonSprite based on a sprite in this sheet.
	 * @param x - the x coordinate of the sprite.
	 * @param y - the y coordinate of the sprite.
	 * @return ButtonSprite - a new ButtonSprite based on the specified sprite
	 * @apiNote (x, y) are measured in sprite count i.e. (3, 2) = 3 sprites across, 2 sprites down. 
	 * @author 5som3
	public ButtonSprite    generateButtonSprite(int x, int y) {return new ButtonSprite(getSprite(x,y));}
	 * */
	
	/**A new ButtonSprite based on a sprite in this sheet.
	 * @param index - the index of the sprite in the rawSprites array.
	 * @return ButtonSprite - a new ButtonSprite based on the specified sprite
	 * @author 5som3
	public ButtonSprite    generateButtonSprite(int index) {return new ButtonSprite(getSprite(index));}
	 * */
	
	/**A new RotationSprite based on a sprite in this sheet.
	 * @param x - the x coordinate of the sprite.
	 * @param y - the y coordinate of the sprite.
	 * @return RotationSprite - a new RotationSprite based on the specified sprite
	 * @apiNote (x, y) are measured in sprite count i.e. (3, 2) = 3 sprites across, 2 sprites down.
	 * @author 5som3*/
	public RotationSprite  generateRotationSprite(int x, int y) {return new RotationSprite(getSprite(x,y));}
	
	
	/**Get a sprite from this sheet.
	 * @param x - the x coordinate of the sprite.
	 * @param y - the y coordinate of the sprite. 
	 * @return BufferedImage - the specified sprite on this sheet.
	 * @apiNote (x, y) are measured in sprite count i.e. (3, 2) = 3 sprites across, 2 sprites down.
	 * @implNote Changes to the returned image will persist for the duration of runtime.
	 * @implNote If you don't want to affect other objects using the same image then you should make a copy of the returned image and edit the copy.
	 * @author 5som3*/
	public BufferedImage getSprite(int x, int y) {
		if(x >= spriteXCount || y >= spriteYCount) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> invalid sheet coordinates : (" + x + ", " + y +")" + ", sheet size : (" + spriteXCount + ", " + spriteYCount + " )");
			return null;
		}
		return rawSpriteImages[x + y * spriteXCount];
	}
	
	/**Get a sprite from this sheet.
	 * @param coord - the (x, y) coordinate for the sprite. 
	 * @return BufferedImage - the specified sprite on this sheet.
	 * @apiNote (x, y) are measured in sprite count i.e. (3, 2) = 3 sprites across, 2 sprites down.
	 * @implNote Changes to the returned image will persist for the duration of runtime.
	 * @implNote If you don't want to affect other objects using the same image then you should make a copy of the returned image and edit the copy.
	 * @author 5som3*/
	public BufferedImage getSprite(VectorI coord) {
		if(coord.x() >= spriteXCount || coord.y() >= spriteYCount) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> invalid sheet coordinates : " + coord.getString());
			return null;
		}
		if(rawSpriteImages[coord.x() + coord.y() * spriteXCount] == null) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> sheet contains null sprite at : " + coord.getString());
		}
		return rawSpriteImages[coord.x() + coord.y() * spriteXCount];
	}
	
	/**Get a sprite from this sheet.
	 * @param index - the index of the sprite in the rawSprite array.
	 * @return BufferedImage - the specified sprite on this sheet.
	 * @implNote Changes to the returned image will persist for the duration of runtime.
	 * @implNote If you don't want to affect other objects using the same image then you should make a copy of the returned image and edit the copy.
	 * @author 5som3*/
	public BufferedImage getSprite(int index) {
		if(index < 0 || index > rawSpriteImages.length) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> invalid sprite index : " + index);
			return null;
		}
		if(rawSpriteImages[index] == null) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> sheet contains null sprite at : " + index);
		}
		return rawSpriteImages[index];
	}
	
	/**Get a random sprite from this sheet.
	 * @return BufferedImage - an random sprite from this sheet.
	 * @implNote Changes to the returned image will persist for the duration of runtime.
	 * @implNote If you don't want to affect other objects using the same image then you should make a copy of the returned image and edit the copy.
	 * @author 5som3*/
	public BufferedImage getRandomSprite() {return rawSpriteImages[UtilsMath.random(rawSpriteImages.length)];}
	
	/**Get the array containing all the sprites.
	 * @return BufferedImage[] - the array of sprites. 
	 * @apiNote NOT a copy, any changes made to these images will effect any other object that refers to them.
	 * @apiNote Typically used for spriteSheets containing a single animation.
	 * @author 5som3*/
	public BufferedImage[] getSprites() {return rawSpriteImages;}
	
	/**Get a row of sprites.
	 * @param row - the row to get from this sheet.
	 * @return BufferedImage[] - an array of all the sprites in the row.
	 * @apiNote Number of sprites will be specific to this sheet.
	 * @apiNote Sprites are loaded from left to right.
	 * @implNote Changes to the returned images will persist for the duration of runtime.
	 * @implNote If you don't want to affect other objects using the same images then you should make copys of the returned images and edit the copys.
	 * @author 5som3*/
	public BufferedImage[] getSpriteRow(int row) {
		if(row < 0 || row >= spriteYCount) {
			Console.err("WarpedSpriteSheet -> getSpriteRow() -> no row " + row + " exists.");
			return null;
		}
		BufferedImage[] result = new BufferedImage[spriteXCount];
		for(int i = 0; i < spriteXCount; i++) result[i] = getSprite(i, row);
		return result;
	}
	
	/**Get a column of sprites.
	 * @param column - the column to get from this sheet.
	 * @return BufferedImage[] - an array of all the sprites in the row.
	 * @apiNote Number of sprites will be specific to this sheet.
	 * @apiNote Sprites are loaded from top to bottom.
	 * @implNote Changes to the returned images will persist for the duration of runtime.
	 * @implNote If you don't want to affect other objects using the same images then you should make copys of the returned images and edit the copys.
	 * @author 5som3*/
	public BufferedImage[] getSpriteColumn(int column) {
		if(column < 0 || column >= spriteXCount) {
			Console.err("WarpedSpriteSheet -> getSpriteRow() -> no column " + column + " exists.");
			return null;
		}
		BufferedImage[] result = new BufferedImage[spriteYCount];
		for(int i = 0; i < spriteYCount; i++) result[i] = getSprite(column, i);
		return result;
	}

	/**Get a row or column of sprites depending on the axis specified.
	 * @param axis - If horizontal, will return the specified row.
	 * @param axis - If Vertical, will return the specified column.
	 * @param index - The index of the row / column to get.
	 * @return BufferedImage[] - an array of all the sprites in the row or column.
	 * @apiNote Number of sprites will be specific to this sheet.
	 * @apiNote Sprites are loaded from top to bottom for columns and left to right for rows.
	 * @implNote Changes to the returned images will persist for the duration of runtime.
	 * @implNote If you don't want to affect other objects using the same images then you should make copys of the returned images and edit the copys.
	 
	 * */
	public BufferedImage[] getSpriteAxis(AxisType axis, int index) {
		BufferedImage[] result = new BufferedImage[1];
		result[0] = FrameworkSprites.error;
		switch(axis) {
		case HORIZONTAL: 
			if(hasRow(index)) result = getSpriteRow(index);
			else Console.err("WarpedSpriteSheet -> getSpriteAxis() -> spite sheet doesn't have row : " + index);
			break;
		case VERTICAL:
			if(hasColumn(index)) result = getSpriteColumn(index);
			else Console.err("WarpedSpriteSheet -> getSpriteAxis() -> spite sheet doesn't have column : " + index);
			break;
		default:
			Console.err("WarpedSpriteSheet -> getSpriteAxis() -> SpriteSheet has no axis : " + axis);
			break;
		}
		return result;
	}
	
	/**Does the SpriteSheet have the specified row.
	 * @param boolean - true if the row exists else false.
	 * @author 5som3*/
	public boolean hasRow(int row) {if(row < 0 || row >= spriteYCount) return false; else return true;}
	
	/**Does the SpriteSheet have the specified column.
	 * @param boolean - true if the column exists else false.
	 * @author 5som3*/
	public boolean hasColumn(int column) {if(column < 0 || column >= spriteXCount) return false; else return true;}
	
	
}
