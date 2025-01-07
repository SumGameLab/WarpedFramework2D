/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.WarpedProperties;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.utils.UtilsMath;

public class GUIShape extends WarpedGUI {

	private ArrayList<Color> colors = new ArrayList<>();
	private ArrayList<Rectangle> rectangles = new ArrayList<>();
	
	private Vec2d shapeSize = new Vec2d(1, 1);
	
	public GUIShape() {
		this.ateractive();
	}
	
	public void addRectangle(Rectangle rectangle, Colour colour) {
		rectangles.add(rectangle);
		colors.add(colour.getColor());
	}
	
	public void addRectangle(Rectangle rectangle, Color color) {
		rectangles.add(rectangle);
		colors.add(color);
	}

	public void clear() {
		rectangles.clear();
		colors.clear();
	}

	public void updateGraphics() {
		updateGraphicsSize();
		BufferedImage img = new BufferedImage((int)shapeSize.x, (int)shapeSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		drawRectangles(img);
		setRaster(img);
	}
	
	private void updateGraphicsSize() {shapeSize.set(UtilsMath.findMaxBounds(rectangles));}
	
	private void drawRectangles(BufferedImage img) {
		Graphics g = img.getGraphics();
		for(int i = 0; i < rectangles.size(); i++) {
			Rectangle rect = rectangles.get(i);
			g.setColor(colors.get(i));
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
		g.dispose();
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
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

	
	
	
}
