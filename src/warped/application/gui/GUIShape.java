/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;

public class GUIShape extends WarpedGUI {

	/*GUIShape provides these functions
	 * 		- add multiple colored rectangles to build up an image
	 * 		- useful to add a background or block out areas on a HUD
	 * 
	 *GUIShape does not provide an user interaction
	 *		- to create a shape with interaction make an extension to this class and override the mouse functions
	 */
	
	private ArrayList<Color> rectangleColors = new ArrayList<>();
	private ArrayList<Rectangle> rectangles = new ArrayList<>();
	
	public GUIShape(int width, int height) {
		setInteractive(false);
		sprite.setSize(width, height);
	}
		
	/**Add a rectangle of a specific color to the shape
	 * @param rectangle - the rectangle to add
	 * @param colour    - the colour to draw the rectangle
	 * @author SomeKid*/
	public void addRectangle(Rectangle rectangle, Colour colour) {addRectangle(rectangle, colour.getColor());}
	
	/**Add a rectangle of a specific color to the shape
	 * @param rectangle - the rectangle to add
	 * @param colour    - the colour to draw the rectangle
	 * @author SomeKid*/
	public void addRectangle(Rectangle rectangle, Color color) {
		rectangles.add(rectangle);
		rectangleColors.add(color);
		updateGraphics();
	}
	
	/**Clears all rectangles from the shape
	 * @author SomeKid*/
	public void clear() {
		rectangles.clear();
		rectangleColors.clear();
		sprite.clearBuffers();
	}

	private void updateGraphics() {
		Graphics g = getGraphics();
		
		for(int i = 0; i < rectangles.size(); i++) {
			Rectangle rect = rectangles.get(i);
			g.setColor(rectangleColors.get(i));
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
		
		g.dispose();
		pushGraphics();
	}
	
	
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	public void updateObject() {return;}


	
	
	
}
