/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite.spriteSheets;

import java.awt.image.BufferedImage;

import warped.graphics.sprite.ButtonSprite;
import warped.graphics.sprite.PrimitiveSprite;
import warped.graphics.sprite.RotationSprite;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public class WarpedSpriteSheet {
	
	private int sheetWidth; /** pixel width of the sheet */
	private int sheetHeight; /** pixel height of the sheet */
	private boolean isSheetSquare = false;
	
	private String name;
	
	protected BufferedImage[] rawSpriteImages; /**array of sub images derived from the sheet */
	public int spriteWidth;	/**pixel width of a single sprite */
	public int spriteHeight;/** pixel height of a single sprite */ 
	private boolean isSpriteSquare = false;
		
	protected int spriteXCount; 
	protected int spriteYCount; 
	protected int spriteCount;  
	
	public boolean isSpriteSquare() {if(isSpriteSquare) return true; return false;}
	public boolean isSheetSquare() {if(isSheetSquare) return true; return false;}
	
	/** total number of sprites on the sheet */
	public int size() {int result = spriteCount; return result;}
	/** number of sprites across the sheet (sheet width in sprite precision)*/
	public int getXcount() {int result = spriteXCount; return result;}
	/** number of sprites from top to bottom (sheet height in sprite precision)*/
	public int getYcount() {int result = spriteYCount; return result;}
	
	/**SpriteSheet contains sprites of ONLY ONE size
	 * to create sprites of a different size create a new sprite sheet with that size
	 * Create all sprite sheets as static objects, no need to have multiples of the same image data
	 * @param string - directory location of the sprite sheet file
	 * 		  int    - width of each sprite on the sheet	
	 * 		  int    - height of each sprite on the sheet
	 * 		  */

	
	public String getName() {return name;}
	
	public WarpedSpriteSheet(BufferedImage sheet, int spriteWidth,int spriteHeight) {	    
		if(spriteWidth == spriteHeight) isSpriteSquare = true;
	
		if(sheet == null) {
			Console.err("WarpedSpriteSheet -> sheet could not be read from jar");
			return;
		} else Console.ln("WarpedSpriteSheet -> creating new sprite sheet..");
			
		
		sheetWidth = sheet.getWidth();
		sheetHeight = sheet.getHeight();
		if(sheetWidth == sheetHeight) isSheetSquare = true;
		if(spriteWidth > sheetWidth || spriteHeight > sheetHeight) {
			Console.err("WarpedSpriteSheet -> spriteWidth or height is larger than sheet size -> sprite/sheet size : " + "( " + spriteWidth + ", " + spriteHeight + ") (" + sheetWidth + ", " + sheetHeight + ")");
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

	public WarpedSpriteSheet(String sheetPath) {
		Console.ln("WarpedSpriteSheet -> path : " + sheetPath);
		if(!sheetPath.endsWith("_wf.png")) {
			Console.err("WarpedSpriteSheet -> sprite sheet path dosen't end with _wf.png, must be in the format  : (name)_wf2d_(width)_(height)_wf.png -> Omitting brackets");
			return;
		};
		if(!sheetPath.contains("_wf2d_")) {
			Console.err("WarpedSpriteSheet -> sprite sheet path doesn't contain _wf2d_, must be in the format  : (name)_wf2d_(width)_(height)_wf.png -> Omitting brackets");
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
	    
		if(spriteWidth == spriteHeight) isSpriteSquare = true;
		
		BufferedImage sheet = UtilsImage.loadBufferedImage(sheetPath);
		
		if(sheet == null) {
			Console.err("WarpedSpriteSheet -> sheet could not be read from jar");
			return;
		}
		
		sheetWidth = sheet.getWidth();
		sheetHeight = sheet.getHeight();
		if(sheetWidth == sheetHeight) isSheetSquare = true;
		if(spriteWidth > sheetWidth || spriteHeight > sheetHeight) {
			Console.err("WarpedSpriteSheet -> spriteWidth or height is larger than sheet size -> sprite/sheet size : " + "( " + spriteWidth + ", " + spriteHeight + ") (" + sheetWidth + ", " + sheetHeight + ")");
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
	
	
	/* ---------------------- Sprite Generation ---------------------- */
	public PrimitiveSprite generatePrimitiveSprite(int i) {return new PrimitiveSprite(getSprite(i));}
	public PrimitiveSprite generatePrimitiveSprite(int x, int y) {return new PrimitiveSprite(getSprite(x,y));}
	public PrimitiveSprite generateRandomPrimitiveSprite() {return new PrimitiveSprite(getRandomBufferedImage());}
	public ButtonSprite    generateButtonSprite(int x, int y) {return new ButtonSprite(this, x, y);}
	public ButtonSprite    generateButtonSprite(int index) {return new ButtonSprite(this, index);}
	//public ButtonSprite    generateButtonSprite(int i, int j, int k) {return new ButtonSprite(this, i, j, k);}
	
	public RotationSprite  generateRotationSprite(int x, int y) {return new RotationSprite(getSprite(x,y));}
	
	/* ---------------------- Raw Sprite data Generation ---------------------- */
	/**@return returns the raw data of a primitive sprite*/
	public BufferedImage getRandomSpriteFromColumn(int x) {
		if(x < 0 || x >= spriteXCount) {
			Console.err("WarpedSpriteSheet -> getRandomSpriteFroMColumn(int x) -> invalid column : " + x);
			return null;
		}
		return rawSpriteImages[x + (UtilsMath.random(spriteYCount)) * spriteXCount];
	}
	
	
	public BufferedImage getRandomSpriteFromRow(int y) {
		if(y < 0 || y >= spriteYCount) { 
			Console.err("WarpedSpriteSheet -> getRandomSpriteFromRow(int y) -> invalid row " + y);
			return null;
		}
		return rawSpriteImages[UtilsMath.random(spriteXCount) + y * spriteXCount];
	}
	
	public BufferedImage getSprite(int x, int y) {
		if(x >= spriteXCount || y >= spriteYCount) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> invalid sheet coordinates : (" + x + ", " + y +")" + ", sheet size : (" + spriteXCount + ", " + spriteYCount + " )");
			return null;
		}
		return rawSpriteImages[x + y * spriteXCount];
	}
	
	public BufferedImage getSprite(Vec2i coord) {
		if(coord.x >= spriteXCount || coord.y >= spriteYCount) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> invalid sheet coordinates : " + coord.getString());
			return null;
		}
		if(rawSpriteImages[coord.x + coord.y * spriteXCount] == null) {
			Console.err("WarpedSpriteSheet -> getRawSprite() -> sheet contains null sprite at : " + coord.getString());
		}
		return rawSpriteImages[coord.x + coord.y * spriteXCount];
	}
	
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
	
	public BufferedImage getRandomRawSprite() {
		return rawSpriteImages[UtilsMath.random(rawSpriteImages.length)];
	}
	
	private BufferedImage getRandomBufferedImage() {
		int rand = UtilsMath.random.nextInt(spriteCount);
		int i = 0;
		for(int y = 0; y < spriteYCount; y++) {
			for(int x = 0; x < spriteXCount; x++) {
				if(rand == i) return getSprite(x,y);
				i++;
			}
		}
		Console.err("WarpedSpriteSheet -> getRandomSprite() -> failed to find random sprite");
		return null;
	}

	

	
}
