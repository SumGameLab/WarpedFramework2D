/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUILabel  extends WarpedGUI {
	
	/*GUILabel provides these functions
	 * 		- display a line of text to the user
	 * 		- set the border, background and text colour
	 * 		- set the background alpha to to have semi or completely transparent labels
	 */
	
	private String text = "default text";

	private Color borderColor = Color.BLACK;
	private int borderThickness = 2;
	
	private Color backgroundColour = Colour.GREY_DARK.getColor();
	private Color textColor = Color.WHITE;
	private Font textFont = UtilsFont.getPreferred();
	
	private BufferedImage backgroundImage;  
	private boolean isBackgroundVisible = false;
	
	private VectorI textOffset = new VectorI(8, textFont.getSize() + 6);
	
	/**A label with the default parameters.
	 * @author 5som3*/
	public GUILabel() {
		setSize(120, 40);
		updateGraphics();
	}
	
	/**A label with the specified parameters.
	 * @param width - the width of the label in pixels.
	 * @param height - the height of the label in pixels.
	 * @author 5som3*/
	public GUILabel(int width, int height) {
		setSize(width, height);
		updateGraphics();
	}
	
	/**A label with the specified parameters.
	 * @param text - the text to display in the label.
	 * @author 5som3*/
	public GUILabel(String text) {
		this.text = text;
		sprite.setSize(200, 30);
		updateGraphics();
	}
	
	/**Set the visibility of the background and border color.
	 * @param isBackgroundVisible - if true the background will be visible else it will not be rendered.
	 * @apiNote Will also make the background visible. 
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackgroundVisible) {
		if(this.isBackgroundVisible != isBackgroundVisible) {
			this.isBackgroundVisible = isBackgroundVisible;
			updateGraphics();
		}
	}
	
	/**Set the background to be an image.
	 * @param backgroundImage - a buffered image to render behind the label text.
	 * @author 5som3 */
	public void setBackgroundImage(BufferedImage backgroundImage) {
		isBackgroundVisible = true;
		this.backgroundImage = backgroundImage;
		updateGraphics();
	}
	
	/**Clear the background image.
	 * @author 5som3 */
	public void clearBackgroundImage() {
		this.backgroundImage = null;
		updateGraphics();
	}
	
	/**Useful if you have many labels that you want to have the same format
	 * @param label- the label to copy the format from
	 * @author SomeKid*/
	public void pointToFormat(GUILabel label) {
		this.borderColor     = label.borderColor;
		this.borderThickness = label.borderThickness;
		this.textColor 		 = label.textColor;
		this.textFont 		 = label.textFont;
		this.textOffset 	 = label.textOffset;
		this.setSize(label.getSize().x(), label.getSize().x());
		updateGraphics();
	}
	
	
	/**The thickness of the border
	 * @param - the thickness in pixels, 0 thickness is no border
	 * @apiNote Will also make the background visible.
	 * @author SomeKid*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0 || borderThickness > getWidth() / 4 || borderThickness > getHeight() / 4) {
			Console.err("GUILabel -> setBorderThickness() -> thickness is invalid : " + borderThickness);
			return;
		} 
		isBackgroundVisible = true;
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	/**The colour of the border
	 *@param borderColor - the color to use
	 *@apiNote Will also make the background visible.
	 *@author SomeKid */
	public void setBorderColor(Color borderColor) {
		isBackgroundVisible = true;
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/**The text on the label
	 * @param text - the text to display
	 * @author SomeKid*/
	public void setText(String text) {
		this.text = text;
		updateGraphics();
	}

	/**The colour of the text
	 * @param textColor - the color to use
	 * @author SomeKid*/
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	/**Set the style of the text
	 * @param textStyle - 0 plain
	 * 					- 1 bold
	 * 					- 2 italic
	 * @author SomeKid*/
	public void setTextStyle(int textStyle) {
		textStyle = UtilsMath.clamp(0, textStyle, 2);
		textFont = new Font(textFont.getFontName(), textStyle, textFont.getSize());
		updateGraphics();
	}
	
	/**Set the size of the text
	 * @param textSize - the size of the text, must be at least 1
	 * @author SomeKid*/
	public void setTextSize(int textSize) {
		textSize = UtilsMath.clampMin(textSize, 1);
		textFont = new Font(textFont.getFontName(), textFont.getStyle(), textSize);
		updateGraphics();
	}
	
	/**The offset of the text on the label
	 * @param x, y, - the offset measured in pixels from the top left corner.
	 * @author SomeKid*/
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
		updateGraphics();
	}

	/**Set the colour of the label behind the text
	 * @param backgroundColor - the color to draw behind the text
	 * @apiNote Will also make the background visible. 
	 * @author SomeKid*/
	public void setBackgroundColor(Color backgroundColor) {
		isBackgroundVisible = true;
		this.backgroundColour = backgroundColor;
		updateGraphics();
	}
	
	/**Set the alpha of the background colour and border colour.
	 * @param alpha - a value in the range 0 -> 255 with 0 being transparent and 255 being opaque.
	 * @author SomeKid*/
	public void setLabelAlpha(int alpha) {
		if(alpha < 0 || alpha > 255) {
			Console.err("GUILable -> setLabelAlpha() -> invalid alpha : " + alpha);
			return;
		}
		backgroundColour = new Color(backgroundColour.getRed(), backgroundColour.getGreen(), backgroundColour.getBlue(), alpha);
		borderColor = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), alpha);
		updateGraphics();
	}
	
	/**Set the size of the label
	 * @param width - the width of the label in pixels
	 * @param height - the height of the label in pixels
	 * @apiNote Graphics are updated automatically after changing the image size.
	 * @author SomeKid*/
	public void setLabelSize(int width, int height) {
		if(width < 1 || height < 1) {
			Console.err("GUILabel -> setLabelSize() -> label size invalid : " + width + ", " + height);
			if(width < 1) width = 100;
			else height = 30;
		}
		sprite.setSize(width, height);
		updateGraphics();
	}
	
	public void updateGraphics() {
		Graphics g = getGraphics();
		
		if(isBackgroundVisible) {
			if(borderThickness > 0) {				
				g.setColor(borderColor);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			if(backgroundImage == null)	{
				g.setColor(backgroundColour);
				g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
			}
			else g.drawImage(backgroundImage, borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2, null);
			
		}
		
		g.setColor(textColor);
		g.setFont(textFont);
		g.drawString(text, textOffset.x(), textOffset.y());
		
		g.dispose();
		pushGraphics();		
	}
	
	protected void mouseEntered() {return;}
	protected void mouseExited() {return;}
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	public void updateObject() {return;}
	
	
	
}
