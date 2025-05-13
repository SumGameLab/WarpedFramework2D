/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import warped.application.state.WarpedObject;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUIIcon extends WarpedGUI {

	/*GUIIcon provides these functions
	 * 		
	 * 		-display any game object as an icon.	
	 * 		-set the game object during runtime
	 * 		- 'set and forget', redraws the raster every time the targets raster changes.
	 * 		   because GUIIcon redraws every frame it is advisable to use them sparingly.
	 * 		- set a specific size for the icon, the target will be stretched or shrunk to fit the icon accordingly.
	 * 		- option to preserve aspect ratio or fill icon
	 * 		- display on a clear background or have a colored background.
	 * 		- option to set text to be drawn over the icon
	 * 		- set font size, colour, offset for optional text
	 * 
	 * */

	protected WarpedObject target;
	
	private VectorI framedSize = new VectorI();
	private VectorI framedOffset = new VectorI();
	private VectorI textOffset = new VectorI();

	private boolean isAspectRatio = true;
	private boolean isBackground = false;
	private boolean isText 	  = false;
	
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color borderColor = Colour.BLACK.getColor();
	private Color textColor;

	private String textOverlay; 
	
	private int borderThickness = 2;
	
	private Font textFont = UtilsFont.getPreferred();
		
	
	public GUIIcon(int width, int height) {
		setIconSize(width, height);	
		maxFrameSize();
	}
	
	public GUIIcon(int width, int height, WarpedObject target) {
		setIconSize(width, height);
		setTarget(target);
	}

	/**Set the colour of the background.
	 * */
	public void setBackgroundColor(Color backgroundColor) {
		this.isBackground = true;
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	/**Set the color of the border. 
	 * @param borderColor - the new color for the border.
	 * @apiNote The Color is only visible if borderThickness > 0.
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/**Set the thickness of the border - the icon will be drawn inside the border area.
	 * @param borderThickness - the thickness of the border measured in pixels.
	 * 			 			  - set to 0 for no border.
	 * @author 5som3*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0 || borderThickness > getWidth() / 4 || borderThickness > getHeight()/ 4) {
			Console.err("GUIIcon -> setBorderThickness() -> invalid border size : " + borderThickness);
			borderThickness = 2;
		}
		this.borderThickness = borderThickness;
		updateFramingParameters();
		updateGraphics();
	}
	
	/**Set the font of the text overlay ( if any).
	 * @param font - the new font for the text overlay.
	 * @author 5som3*/
	public void setFont(Font font) {
		this.textFont = font;
		updateGraphics();
	}
	
	/**Set the size of the text overlay (if any).
	 * @param textSize - the new size of the text, must be greater than 2.
	 * @author 5som3*/
	public void setTextSize(int textSize) {
		if(textSize < 2) {
			Console.err("GUIIcon -> setTextSize() -> textSize is too small : " + textSize);
			 textSize = 12;
		}
		this.textFont = new Font(textFont.getFontName(), textFont.getStyle(), textSize);
		updateGraphics();
	}
	
	/**Set the offset of the text (if any)
	 * @param xOffset - the xOffset measured in pixels
	 * @param yOffset - the yOffset measured in pixels 
	 * @apiNote the offset is the distance between the bottomLeft corner of the text line and the top left corner of the icon
	 * @author 5som3*/
	public void setTextOffset(int xOffset, int yOffset) {
		textOffset.set(xOffset, yOffset);
		updateGraphics();
	}
	
	/**Set the colour of the text (if any);
	 * @param textColor - the color of the text.
	 * @author 5som3*/
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	/**Set the text to display over the icon.
	 * @param String textOverlay - the text to overlay the icon.
	 * @apiNote Also sets the text to be visible, this can be hidden again later by calling isTextVisible(false).
	 * @author 5som3.*/
	public void setTextOverlay(String textOverlay) {
		this.isText = true;
		this.textOverlay = textOverlay;
		updateGraphics();
	}
	
	/**Set the visibility of the text (if any)
	 * @param isText - if true any text set will be rendered over the icon
	 * @author 5som3*/
	public void setTextVisible(boolean isText) {
		if(this.isText != isText) {			
			this.isText = isText;
			updateGraphics();
		}
	}
	
	/**Set if the background will be visible behind the icon.
	 * @param isBackground - if true the border and background will be visible, else they will no be rendered.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackground) {
		if(this.isBackground != isBackground) {			
			this.isBackground = isBackground;
			updateGraphics();
		}
	}
	
	/**Set if the icon should be drawn with the original aspect ratio or fill.
	 * @param isAspectRatio - if true the aspectRatio of the image will match the target objects aspectRatio
	 * 						- if false the target objects raster will fill the icon area
	 * @author 5som3*/
	public void setPreserveAspectRatio(boolean isAspectRatio) {
		if(this.isAspectRatio != isAspectRatio) {
			this.isAspectRatio = isAspectRatio;
			updateFramingParameters();
			updateGraphics();
		}
	}
	
	/**Set the object that this icon will get its graphics from
	 * @param WarpedObject - the object this icon will target.
	 * @apiNote setTarget(null) will clear the graphics and target from the icon.
	 * @author 5som3*/
	public void setTarget(WarpedObject target) {
		if(this.isEqual(target)) {
			Console.err("GUIIcon -> setTarget() -> icon can not target itself");
			return;
		}
		if(this.target != null) this.target.getSprite().clearDeltaAction();
		if(target == null) this.target = null;
		else {			
			this.target = target;
			target.getSprite().setDeltaAction(() -> {updateGraphics();});
			updateFramingParameters();
		}
		updateGraphics();
	}

	/**Set the size of the icon.
	 * Updates the graphics to match the new size.
	 * @param width  - the width of the icon in pixels.
	 * @param height - the height of the icon in pixels.
	 * @apiNote updates the graphics to fill the new size after changing it.
	 * @author 5som3*/
	public void setIconSize(int width, int height) {
		setSize(width,height);
		updateFramingParameters();
		updateGraphics();
	}
	
	/**Update how the icon is framed within the total drawable area;
	 * sets the size of the image inside the icon and the the offset required to center an image of that size.
	 * @apiNote If isAspectRatio is false the framed size will be the GUIIcon size less the borderSize
	 * @apiNote If isAspectRatio is true the framed size will be the largest possible size within the icon bounds that preserves the aspectRatio of the target.
	 * @author 5som3 */
	private void updateFramingParameters() {
		if(target == null) {
			Console.err("GUIIcon -> updateFramedSize() -> there is no target to base the frame size on");
			 maxFrameSize();
			return;
		}
		
		if(!isAspectRatio) maxFrameSize();
		else {
			double targetAspectRatio = (double)target.getWidth() / (double)target.getHeight();
			if(targetAspectRatio <= 0.0) {
				Console.err("GUIIcon -> updateFramedSize() -> image is invalid proportions (width, height) : ( " + target.getWidth() + ", " + target.getHeight() + " )");
				 maxFrameSize();
				return;
			} 
						
			int fWidth  = 0; 
			int fHeight = 0;
			
			int xOffset = 0;
			int yOffset = 0;
			
			
			
			if(targetAspectRatio < 1.0) { //portrait
				fHeight = getHeight() - borderThickness * 2; // try to fill height
				fWidth = (int) (getHeight() * targetAspectRatio);
				if(fWidth > getWidth() - borderThickness * 2) { // image width clipped, find max portrait that fills width
					fWidth = getWidth() - borderThickness * 2;
					fHeight = (int)(getWidth() / targetAspectRatio);
					xOffset = borderThickness;
					yOffset = (getHeight() - fHeight - (borderThickness * 2)) / 2; 
				} else {					
					xOffset = (int)(getWidth() - fWidth - (borderThickness * 2)) / 2;
					yOffset = borderThickness;		
				}
			} else { // landscape
				fWidth = getWidth() - borderThickness * 2; // try to fill width
				fHeight = (int)(getWidth() / targetAspectRatio);
				if(fHeight > getHeight() - borderThickness * 2) { // image height clipped, find max landscape that fills height.
					fHeight = getHeight() - borderThickness * 2;
					fWidth  = (int)(getHeight() * targetAspectRatio);
					xOffset = (int)(getWidth() - fWidth - (borderThickness * 2)) / 2;
					yOffset = borderThickness;
				} else {					
					xOffset = borderThickness;
					yOffset = (getHeight() - fHeight - (borderThickness * 2)) / 2;
				}
			}
			
			framedSize.set(fWidth, fHeight);
			framedOffset.set(xOffset, yOffset);
			Console.ln("GUIIcon -> updateFramingParameters() -> framedSize : " + framedSize.getString() + ", framedOffset : " + framedOffset.getString());
		}
	}
	
	/**Set the frameParameters to stretch the target object raster across the drawable area in the icon 
	 * @author 5som3 */
	private void maxFrameSize() {
		framedSize.set(getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
		framedOffset.set(borderThickness, borderThickness);
		return;
	}
	
	
	private void updateGraphics() {		
		Graphics g = getGraphics();
		
		if(isBackground) {			
			g.setColor(borderColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(backgroundColor);
			g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
		}
		
		if(target != null) g.drawImage(target.raster(), framedOffset.x(), framedOffset.y(), framedSize.x(), framedSize.y(), null);
		
		
		if(isText) {			
			g.setColor(textColor);
			g.setFont(textFont);
			g.drawString(textOverlay, textOffset.x(), textOffset.y());			
		}
		
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
	


	

}
