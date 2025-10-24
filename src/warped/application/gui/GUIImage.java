/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUIImage extends WarpedGUI {
	
	/*GUIImage provides these functions
	 * 		- set an image to display.
	 * 		- outline the image with a colored border.
	 * 		- can preserve the aspect ratio for the image or stretch it to fill the desired size
	 */
	
	private BufferedImage originalImage = new BufferedImage(1, 1, 2);
	private Color borderColour = Colour.BLACK.getColor();
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	
	private int borderThickness = 0;
		
	private boolean isBackgroundVisible = false;
	private boolean isAspectRatio = false;
	
	private VectorI framedSize = new VectorI();
	private VectorI framedOffset = new VectorI();
	
	
	/**A new GUIImage with the default parameters.
	 * @author 5som3 */
	public GUIImage() {
		setSize(50, 50);
		getSprite().paint(Color.RED);
		setInteractive(false);
	}

	/**A new GUIImage the size of the image param
	 * @param image - the image to render.
	 * @apiNote GUIImage will be resized to the image size.
	 * @author 5som3*/
	public GUIImage(BufferedImage image) {
		setInteractive(false);
		setSize(image.getWidth(), image.getHeight());
		originalImage = image;
		updateGraphics();
	}
	
	/**A new GUIImage of the specified size.
	 * @param image - the image to render in the GUIImage.
	 * @param width - the width of the image in pixels
	 * @param height - the height of the image in pixels.
	 * @author 5som3*/
	public GUIImage(BufferedImage image, int width, int height) {
		setInteractive(false);
		setSize(width, height);
		originalImage = image;
		updateGraphics();
	}
	
	/**A new GUIImage of the specified size.
	 * @param image - the image to render in the GUIImage.
	 * @param width - the width of the image in pixels
	 * @param height - the height of the image in pixels.
	 * @param isStretched - if true the image will be stretched to fill the specified width and height, else it will fit the size and maintain its aspectRatio.
	 * @author 5som3*/
	public GUIImage(BufferedImage image, int width, int height, boolean isStretched) {
		setInteractive(false);
		setImage(image, width, height, isStretched);
		updateGraphics();
	}
	
	/**Set the thickness of the border colour
	 * @param borderThickness - the thickness for the border, 0 thickness will have no border 
	 * @author SomeKid*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0 || borderThickness > sprite.getWidth() / 4 || borderThickness > sprite.getHeight() / 4) {
			Console.err("GUIImage -> setBorderThickness() -> invalid thickness : " + borderThickness);
			return;
		}
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	/**Set the colour of the border
	 * @param borderColour - the colour of the border, for no colour set the border thickness to 0
	 * @author SomeKid*/
	public void setBorderColour(Color borderColour) {
		this.borderColour = borderColour;
		updateGraphics();
	}

	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		return;
	}
	
	/**The set image will be drawn at the specified size.
	 * @param width, height - the width and height the GUI will be fixed to
	 * @author SomeKid*/
	public void setSize(int width, int height) {
		if(width < 0 || height < 0) {
			Console.err("GUIImage -> setFixedSize() -> invalid size : ( " + width + ", " + height + ") ");
			return;
		}
		sprite.setSize(width, height);
		updateFramingParameters();
		updateGraphics();
	}
		
	/**Set if the aspect ratio will be preserved when resizing the image.
	 * @param isAspectRatio - if true the aspectRatio of the original will be preserved.
	 * @author 5som3*/	
	public void setAspectPreserved(boolean isAspectRatio) {
		this.isAspectRatio = isAspectRatio;
		updateFramingParameters();
		updateGraphics();
	}
	
	/**Set the image 
	 * @param image - this image will be used to generate the graphics for the GUI but will not be modified.
	 * @apiNote Image will be rendered to fit the current GUIImage size.
	 * @apiNote Image will fill the GUI if isStretched(true) else it will fit the current size while maintaining its original aspect-ratio.
	 * @author 5some*/
	public void setImage(BufferedImage image) {
		originalImage = image;
		updateFramingParameters();
		updateGraphics();
	}
	
	/**Set the image 
	 * @param image - this image will be used to generate the graphics for the GUI but will not be modified.
	 * @param isResized - if true the GUIImage will have its size set to match the size of the image param. 
	 * @author 5some*/
	public void setImage(BufferedImage image, boolean isResized) {
		originalImage = image;		
		if(isResized) setSize(image.getWidth(), image.getHeight());
		else {
			updateFramingParameters();
			updateGraphics();
		}
	}
	
	/**Set the image 
	 * @param image - this image will be used to generate the graphics for the GUI but will not be modified.
	 * @param width - the width for the GUIImage in pixels
	 * @param height - the height for the GUIImage in pixels.
	 * @param isStretched - If true the image will be stretched to fill the GUI else it will fit and maintain aspect-ratio.
	 * @author 5some*/
	public void setImage(BufferedImage image, int width, int height, boolean isStretched) {
		if(width <= 0 || height <= 0) {
			Console.err("GUIImage -> setImage() -> width and height must be greater than 0");
			if(width <= 0) width = 100;
			else height = 100;
		}
		setSize(width, height);
		this.isAspectRatio = isStretched;
		originalImage = image;
		updateFramingParameters();
		updateGraphics();
	}
	
	/**Set if the background will be visible or not.
	 * @param isBackgroundVisible - if true the background will be visible else it will not be rendered.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackgroundVisible) {
		if(this.isBackgroundVisible != isBackgroundVisible) {
			this.isBackgroundVisible = isBackgroundVisible;
			updateGraphics();
		}
	}
	
	/**Update how the icon is framed within the total drawable area;
	 * sets the size of the image inside the icon and the the offset required to center an image of that size.
	 * @apiNote If isAspectRatio is false the framed size will be the GUIIcon size less the borderSize
	 * @apiNote If isAspectRatio is true the framed size will be the largest possible size within the icon bounds that preserves the aspectRatio of the target.
	 * @author 5som3 */
	private void updateFramingParameters() {
		if(originalImage == null) {
			Console.err("GUIImage -> updateFramedSize() -> there is no target to base the frame size on");
			 maxFrameSize();
			return;
		}
		
		if(!isAspectRatio) maxFrameSize();
		else {
			double targetAspectRatio = (double)originalImage.getWidth() / (double)originalImage.getHeight();
			if(targetAspectRatio <= 0.0) {
				Console.err("GUIIcon -> updateFramedSize() -> image is invalid proportions (width, height) : ( " + originalImage.getWidth() + ", " + originalImage.getHeight() + " )");
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
	
	public void updateGraphics() {
		Graphics g = getGraphics();

		if(isBackgroundVisible) {			
			g.setColor(borderColour);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(backgroundColor);
			g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
		}
		
		g.drawImage(originalImage, framedOffset.x(), framedOffset.y(), framedSize.x(), framedSize.y(), null);
		
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
