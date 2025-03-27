/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;

public class GUIImage extends WarpedGUI {
	
	/*GUIImage provides these functions
	 * 		- set an image to display.
	 * 		- outline the image with a colored border.
	 * 		- can preserve the aspect ratio for the image or stretch it to fill the desired size
	 */
	
	private BufferedImage originalImage = new BufferedImage(1, 1, 2);
	private Colour borderColour = Colour.BLACK;
	private int borderThickness = 0;

	private boolean isStretched = false;
	
	public GUIImage() {
		ateractive();
	}

	public GUIImage(BufferedImage image) {
		ateractive();
		sprite.setSize(image.getWidth(), image.getHeight());
		originalImage = image;
		updateGraphics();
	}
	
	public GUIImage(BufferedImage image, int width, int height) {
		ateractive();
		sprite.setSize(width, height);
		originalImage = image;
		updateGraphics();
	}
	
	public GUIImage(BufferedImage image, int width, int height, boolean isStretched) {
		ateractive();
		this.isStretched = isStretched;
		sprite.setSize(width, height);
		originalImage = image;
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
	public void setBorderColour(Colour borderColour) {
		this.borderColour = borderColour;
		updateGraphics();
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
		updateGraphics();
	}
		
	/**The aspect ratio of the set image will be preserved
	 * @author SomeKid*/
	public void preserveAspectRatio() {
		isStretched = false;
		updateGraphics();
	}
	
	/**The set image will be stretched to fill the GUIImage
	 * @apiNote if the GUI does not have a fixed size*/
	public void stretchImage() {
		isStretched = true;
		updateGraphics();
	}
	
	/**Set the image 
	 * @param image - this image will be used to generate the graphics for the GUI but will not be modified
	 * @author 5some*/
	public void setImage(BufferedImage image) {
		originalImage = image;
		updateGraphics();
	}
	
	
	private void updateGraphics() {
		Graphics g = getGraphics();
		int c1x = 0;
		int c1y = 0;
		int c2x = getWidth();
		int c2y = getHeight();
		
		if(borderThickness > 0) {
			g.setColor(borderColour.getColor());
			g.fillRect(0, 0, getWidth(), getHeight());
			c1x = borderThickness;
			c1y = borderThickness;
			c2x = getWidth() - borderThickness * 2;
			c2y = getHeight() - borderThickness * 2;
		}
		
		if(isStretched)	g.drawImage(originalImage, c1x, c1y, c2x, c2y, null);
		else {
			double aspectRatio = originalImage.getWidth() / originalImage.getHeight();
			if(aspectRatio >= 1.0) { //landscape
				int width = c2x - c1x;
				int height = (int)(width / aspectRatio);
				int x = c1x;
				int y = c1y + height / 3;
				g.drawImage(originalImage, x, y, width, height, null);
			} else { // portrait
				int height = c2y - c1y;
				int width = (int)(height * aspectRatio);
				int y = c1y;
				int x = c1x + width / 3;
				g.drawImage(originalImage, x, y, width, height, null);
			}
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
