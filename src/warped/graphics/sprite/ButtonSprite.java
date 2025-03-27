/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import warped.application.gui.GUIButton.ButtonStateType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;

public class ButtonSprite extends WarpedSprite {

	public static final Colour DEFAULT_BUTTON_COLOR = Colour.GREY_DARK;
	public static final int DEFAULT_BUTTON_WIDTH 	= 150;
	public static final int DEFAULT_BUTTON_HEIGHT 	= 30;
	
	private boolean isColourButton = true;
	private Colour borderColour 	  = Colour.BLACK;
	private Colour buttonColour 	  = Colour.GREY_DARK;
	private int borderThickness   	  = 3;
	
	private List<String> text;
	private Color textColor  = Color.YELLOW;
	private Font textFont 	 = UtilsFont.getPreferred();
	private VectorI textOffset = new VectorI(8, textFont.getSize() + 5);
	
	private BufferedImage originalRaster;
	
	private BufferedImage plainRaster;	
	private BufferedImage hoveredRaster;
	private BufferedImage pressedRaster;
	private BufferedImage lockedRaster;
	
	private ButtonStateType state = ButtonStateType.PLAIN;

	/**The default button sprite.
	 * @author 5som3*/
	public ButtonSprite() {
		super(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
		setButtonColour(DEFAULT_BUTTON_COLOR);
		updateGraphics();
		setRasterFast(plainRaster);
		
	}
	
	/**A button sprite of the specified width and height.
	 * @param width - the width of the sprite in pixels.
	 * @param height - the height of the sprite in pixels. 
	 * @author 5som3*/
	public ButtonSprite(int width, int height) {
		super(width, height);
		setButtonColour(DEFAULT_BUTTON_COLOR);
		updateGraphics();
		setRasterFast(plainRaster);
	}
	
	/**A button sprite with the specified parameters
	 * @param width - the width of the sprite in pixels.
	 * @param height - the height of the sprite in pixels.
	 * @param colour - the base colour of the button. (will change when interacted with the mouse).
	 * @author 5som3*/
	public ButtonSprite(int width, int height, Colour colour) {
		super(width, height);
		setButtonColour(colour);
		updateGraphics();
		setRasterFast(plainRaster);
	}
	
	/**A button sprite based on an image. The width and height will be set to the image width and height.
	 * @author 5som3*/
	public ButtonSprite(BufferedImage image) {
		super(image.getWidth(), image.getHeight());
		originalRaster = image;
		generateRasterClones();
		setRasterFast(plainRaster);	
	}
	
	/**Generates the coloured variants for the button sprite (plain, hovered, pressed, locked).
	 * @author 5som3*/
	private void generateRasterClones() {
		plainRaster    = UtilsImage.generateClone(originalRaster);
		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
	}
	
	
	
	private void updateGraphics() {
		Graphics g = getGraphics();
		
		if(isColourButton) {			
			g.setColor(borderColour.getColor());
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(buttonColour.getColor());
			g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
			
		} else g.drawImage(originalRaster, 0, 0, getWidth(), getHeight(), null);
		
		if(text != null) {			
			g.setColor(textColor);
			g.setFont(textFont);
			for(int i = 0; i < text.size(); i++) {
				g.drawString(text.get(i), textOffset.x(), textOffset.y() + (textFont.getSize() + 2) * i);
			}
		}
		
		g.dispose();
		
		plainRaster = getBackBuffer();
		swapBuffers();
		
		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
		
		ButtonStateType oldState = state;
		state = null;
		setState(oldState);
	}
	
	private void setState(ButtonStateType state) {
		switch(state) {
		case HOVERED: hover();	 break;
		case LOCKED: lock();	 break;
		case PLAIN: plain();	 break;
		case PRESSED: press();   break;
		default: 
			Console.err("ButtonSprite -> setState() -> inavlid case : " + state);
			break;
		};
	}
	
	/**Set the colour of the border
	 * @param borderColour - the color to paint the border
	 * @apiNote If you want no border set the borderThickness to 0;
	 * @author SomeKid*/
	public void setBorderColour(Colour borderColour) {
		this.borderColour = borderColour;
		updateGraphics();
	}
	
	/**Set the thickness of the border
	 * @param borderThickness - Must be greater than -1, set to 0 for no border
	 * @author SomeKid*/
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = UtilsMath.clampMin(borderThickness, 0);
		updateGraphics();
	}
	
	/**Set the colour of the button
	 * @apiNote Will change the button type to be a colour button. If an image was set prior it will not be drawn after setting buttonColour
	 * @author SomeKid*/
	public void setButtonColour(Colour buttonColour) {
		this.isColourButton = true;
		this.buttonColour = buttonColour;
		updateGraphics();
	}
	
	/**Set the image for the button
	 * @apiNote No border will be drawn for an image button. 
	 * @apiNote If you need a border put add one to the image your using for the button.
	 * @author SomeKid*/
	public void setButtonImage(BufferedImage raster) {
		this.isColourButton = false;
		setSize(raster.getWidth(), raster.getHeight());
		isColourButton = false;
		originalRaster = raster;
		plainRaster    = UtilsImage.generateClone(originalRaster);
		hoveredRaster  = UtilsImage.generateTintedClone(plainRaster, 30, Colour.RED);
		pressedRaster  = UtilsImage.generateTintedClone(plainRaster, 60, Colour.RED);
		lockedRaster   = UtilsImage.generateTintedClone(plainRaster, 90, Colour.BLACK);
	}
	
	/**Set the text to draw over the button.
	 * @param text - If you pass multiple text lines they will be drawn with an offset of font size 
	 * @author SomeKid*/
	public void setText(String... text) {
		if(text == null) {
			Console.err("GUIButton -> setText() -> invalid text is null or no text");
			return;
		}
		this.text = Arrays.asList(text);
		updateGraphics();		
	}
	
	/**Set the text to draw over the button.
	 * @param text - If you pass multiple text lines they will be drawn with an offset of font size 
	 * @author SomeKid*/
	public void setText(int x, int y, Font font, Color color, String... text) {	
		this.text = Arrays.asList(text);
		textOffset.set(x, y);
		this.textFont = font;
		this.textColor = color;
		updateGraphics();
	}
	
	/**Set the text offset
	 * @param textOffset - Measured in pixels from the top left corner
	 * @author SomeKid*/
	public void setTextOffset(VectorI textOffset) {
		this.textOffset = textOffset;
		updateGraphics();
	}
	
	/**Set the text offset
	 * @param textOffset - Measured in pixels from the top left corner
	 * @author SomeKid*/
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
		updateGraphics();
	}
	
	/**Remove the button text if any exists
	 * @author SomeKid*/
	public void clearText() {
		text = null;
		updateGraphics();
	}
	
	/**The font for the button text if any
	 * @author SomeKid*/
	public void setFont(Font textFont) {
		this.textFont = textFont;
		updateGraphics();
	}
	
	/**The colour of the button text if any
	 * @author SomeKid*/
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}	

	/**Set the button state to plain
	 * @author SomeKid*/
	public void plain()   {
		if(state == ButtonStateType.PLAIN) return;
		else { 			
			state = ButtonStateType.PLAIN;
			setRasterFast(plainRaster);
		}
	}
	
	/**Set the button state to hover
	 * @author SomeKid*/
	public void hover()   {
		if(state == ButtonStateType.HOVERED) return;
		else {			
			state = ButtonStateType.HOVERED;
			setRasterFast(hoveredRaster);
		}
	}
	
	/**Set the button state to press
	 * @author SomeKid*/
	public void press()   {
		if(state == ButtonStateType.PRESSED) return;
		else {
			state = ButtonStateType.PRESSED;
			setRasterFast(pressedRaster);			
		}
	}
	
	/**Set the button state to locked
	 * @author SomeKid*/
	public void lock() 	  {
		setRasterFast(lockedRaster);
		state = ButtonStateType.LOCKED;
	}
	
	
	
	
	
	
}
