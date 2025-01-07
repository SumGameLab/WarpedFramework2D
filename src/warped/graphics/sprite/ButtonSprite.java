/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import warped.WarpedProperties;
import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.enums.generalised.ButtonStateType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.UtilsImage;

public class ButtonSprite extends WarpedSprite {

	//FIXME -> 24/1/24 -> This class uses too much memory and it is excessive. 
	//Instead of copying the image 5 times and having 5 rasters that get toggled.
	//it would be much better to have a single raster and the original image, then update the raster with a color filter.
	//I.e. draw into the raster with the original graphic, then use fill rect to draw the appropriate color for the corosponding opperation
	//
	
	private BufferedImage buttonOriginal;
	
	/**Button sprite must use copys of the input
	 * this is because text will be drawn over these images 
	 * and may need to be reset to the original at some point*/
	
	private BufferedImage plainRaster;	
	private BufferedImage hoveredRaster;
	private BufferedImage pressedRaster;
	private BufferedImage lockedRaster;
	
	private ButtonStateType state = ButtonStateType.PLAIN;
	

	private Color lockedColor = new Color(60, 60, 60, 200);
	//private static BufferedImage lock = UtilsImage.loadBufferedImage("assets/framework/buttonlock.png");
	
	public ButtonSprite(int width, int height, Colour colour) {
		Graphics g;
		buttonOriginal = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		plainRaster    = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		g = plainRaster.getGraphics();
		g.setColor(Colour.getColor(colour));
		g.fillRect(0, 0, width, height);
		g.dispose();

		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
		initRaster();
	}
	
	public ButtonSprite(BufferedImage image) {
		buttonOriginal = image;
		plainRaster    = UtilsImage.generateClone(buttonOriginal);
		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
		initRaster();
	}
	
	public ButtonSprite(WarpedSpriteSheet sheet, int x, int y) {
		buttonOriginal = sheet.getSprite(x, y);
		plainRaster    = UtilsImage.generateClone(buttonOriginal);
		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
		initRaster();
	}
	
	public ButtonSprite(WarpedSpriteSheet sheet, int index) {
		buttonOriginal = sheet.getSprite(index);
		plainRaster    = UtilsImage.generateClone(buttonOriginal);
		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
		initRaster();
	}
	
	public ButtonSprite(BufferedImage toggleOffImg, Colour hoverTint, BufferedImage toggleOnImg) {
		buttonOriginal = toggleOffImg;
		plainRaster    = UtilsImage.generateClone(toggleOffImg);
		hoveredRaster  = UtilsImage.generateTintedClone(toggleOffImg, 30, hoverTint);
		pressedRaster  = toggleOnImg;
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
		initRaster();
	}
	

	/**InitRaster
	 * the reason we have to do this is because the button text may change in the future 
	 * so we will need a copy of the original background to paint behind the new text*/
	private void initRaster() {
		Graphics g = lockedRaster.getGraphics();
		g.setColor(lockedColor);
		g.fillRect(0, 0, lockedRaster.getWidth(), lockedRaster.getHeight());
		//int cx = lockedRaster.getWidth() / 2;
		//int cy = lockedRaster.getHeight() / 2;
		//g.drawImage(lock, cx - lock.getWidth() / 2, cy - lock.getHeight() / 2, lock.getWidth(), lock.getHeight(),null);
		raster = plainRaster;
	}
	
	public ButtonStateType getButtonState() {return state;}
	public void plain()   {
		if(state == ButtonStateType.PLAIN) return;
		else { 			
			state = ButtonStateType.PLAIN;
			raster = plainRaster;
		}
	}
	
	public void hover()   {
		if(state == ButtonStateType.HOVERED) return;
		else {			
			state = ButtonStateType.HOVERED;
			raster = hoveredRaster;
		}
	}
	
	public void press()   {
		if(state == ButtonStateType.PRESSED) return;
		else {
			state = ButtonStateType.PRESSED;
			raster = pressedRaster;			
		}
	}
	
	public void lock() 	  {
		raster = lockedRaster;
		state = ButtonStateType.LOCKED;
	}
	
	
	public void setRaster(BufferedImage raster) {
		buttonOriginal = raster;
		plainRaster    = UtilsImage.generateClone(buttonOriginal);
		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
		initRaster();
	}
	
	
	public void setButtonText(int x, int y, List<String> text, Font font, Color color) {	
		setplainText(x, y, text, font, color);
		setHoveredText(x, y, text, font, color);
		setPressedText(x, y, text, font, color);
		setLockedText(x, y, text, font, color);
		raster = plainRaster;
	}
	
	public void setplainText(int x, int y, List<String> text, Font font, Color color) {
		plainRaster = UtilsImage.generateClone(buttonOriginal);
		Graphics2D g2d;
		g2d = plainRaster.createGraphics();
		g2d.setColor(color);
		g2d.setFont(font);
		for(int i = 0; i < text.size(); i++) {			
			g2d.drawString(text.get(i), x, (y + font.getSize()) + (i * font.getSize()));
		}
		g2d.dispose();
	}
	public void setHoveredText(int x, int y, List<String> text, Font font, Color color) {
		hoveredRaster = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		Graphics2D g2d;
		g2d = hoveredRaster.createGraphics();
		g2d.setColor(color);
		g2d.setFont(font);
		for(int i = 0; i < text.size(); i++) {			
			g2d.drawString(text.get(i), x, (y + font.getSize()) + (i * font.getSize()));
		}
		g2d.dispose();
	}
	public void setPressedText(int x, int y, List<String> text, Font font, Color color) {
		pressedRaster = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		Graphics2D g2d;
		g2d = pressedRaster.createGraphics();
		g2d.setColor(color);
		g2d.setFont(font);
		for(int i = 0; i < text.size(); i++) {			
			g2d.drawString(text.get(i), x, (y + font.getSize()) + (i * font.getSize()));
		}
		g2d.dispose();
	}
	
	public void setLockedText(int x, int y, List<String> text, Font font, Color color){
		lockedRaster = UtilsImage.generateClone(buttonOriginal);
		Graphics2D g2d;
		g2d = lockedRaster.createGraphics();
		g2d.setColor(color);
		g2d.setFont(font);
		for(int i = 0; i < text.size(); i++) {			
			g2d.drawString(text.get(i), x, (y + font.getSize()) + (i * font.getSize()));
		}
		g2d.setColor(lockedColor);
		g2d.fillRect(0, 0, lockedRaster.getWidth(), lockedRaster.getHeight());
		int cx = lockedRaster.getWidth() / 2;
		int cy = lockedRaster.getHeight() / 2;
		//g2d.drawImage(lock, cx - lock.getWidth() / 2, cy - lock.getHeight() / 2, lock.getWidth(), lock.getHeight(),null);
		g2d.dispose();
	}
	
	@Override
	public void update() {
				
	}

	
	
	
}
