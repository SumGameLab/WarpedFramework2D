/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.WarpedProperties;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class GUICanvas<T extends GUIDrawable> extends WarpedGUI {
	
	protected ArrayList<T> drawObjects = new ArrayList<>();
	
	protected BufferedImage backgroundImage;
	protected Color backgroundColor = Colour.GREY_DARK.getColor();
	protected Color borderColor = Color.BLACK;
	
	protected int borderThickness = 2;
	
	protected boolean fixedIconSize = true;
	
	protected VectorI mapSize = new VectorI(1);
	protected VectorI iconSize = new VectorI(32);
	protected double scale = 1.0;			//Scales size and position of drawObjects in the canvas 
	
	protected VectorI offset = new VectorI(); // Offset in pixel precision
	
	protected VectorI c1 = new VectorI(); //Clip bounds
	protected VectorI c2 = new VectorI(1);
	
	protected T selectObject;
	protected VectorI selectPosition;
	
	protected WarpedAction altClickAction = () -> {Console.ln("GUICanvas -> devault right click action");};
	
	public GUICanvas(int mapWidth, int mapHeight) {
		mapSize.set(mapWidth, mapHeight);		
		updateGraphics();
	}
	
	public GUICanvas(int mapWidth, int mapHeight, int iconWidth, int iconHeight) {
		mapSize.set(mapWidth, mapHeight);
		iconSize.set(iconWidth, iconHeight);		
		updateGraphics();
	}
	
	//--------
	//---------------- Access --------
	//--------
	public void flexibleIconSize() {fixedIconSize = false;}
	public void fixedIconSize() {fixedIconSize = true;}
	public void setIconSize(VectorI iconSize) {this.iconSize = iconSize;}
	public VectorI getSelectPosition() {return selectPosition;}
	public void setScale(double scale) {this.scale = scale;}
	/***/
	public void setOffset(VectorI vec) {setOffset(vec.x(), vec.y());}
	public void setOffset(int xPixels, int yPixels) {offset.set(xPixels, yPixels);}
	public void offset(int xPixels, int yPixels) {offset.add(xPixels, yPixels);}
	public T getSelectObject() {return selectObject;}
	public void clearSelectObject() {selectObject = null;}
	
	public void setAltClickAction(WarpedAction altClickAction) {this.altClickAction = altClickAction;}
	
	public void setDrawable(ArrayList<T> drawables) {
		Console.ln("GUICanvas -> setDrawable() -> drawObjects size : " + drawObjects.size());
		drawObjects = drawables;
	}
	public void addDrawable(ArrayList<T> drawables) {
		for(int i = 0; i < drawables.size(); i++) {
			T drawable = drawables.get(i);
			if(drawable == null) {
				Console.err("GUICanavas -> addDrawable() -> drawables contains null value at index : " + i);
				continue;
			}
			
			drawObjects.add(drawable);
		}
	}
	
	public void addDrawable(T drawable) {
		if(drawObjects.contains(drawable)) {
			Console.err("GUIMap -> addMapable() -> map already contains object");
			return;
		}
		drawObjects.add(drawable);
	}
	
	public void clear() {
		drawObjects.clear();
		BufferedImage img = new BufferedImage(mapSize.x(), mapSize.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(img);					
	}
	
	public void updateGraphics() {
		BufferedImage img = new BufferedImage(mapSize.x(), mapSize.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		
		if(backgroundImage == null) {	//set draw bounds, draw background
			c1.set(0, 0);
			c2.set(mapSize.x(), mapSize.y());
			g.drawImage(backgroundImage, c1.x(), c1.y(), c2.x(), c2.y(), null);
		} else {
			c1.set(borderThickness, borderThickness);
			c2.set(mapSize.x() - borderThickness * 2, mapSize.y() - borderThickness * 2);

			g.setColor(borderColor);
			g.fillRect(0, 0, mapSize.x(), mapSize.y());
			g.setColor(backgroundColor);
			g.fillRect(c1.x(), c1.y(), c2.x(), c2.y());
		}

		for(int i = 0; i < drawObjects.size(); i++) { // draw mapped objects
			T mapObject = drawObjects.get(i);
			if(!mapObject.isAlive()) {
				drawObjects.remove(i);
				i--;
				continue;
			}			
			
			int x, y, sx, sy;
			
			if(fixedIconSize) {				
				x = (int)((mapObject.getMapPosition().x() + offset.x() - (iconSize.x() / 2)) * scale);
				y = (int)((mapObject.getMapPosition().y() + offset.y() - (iconSize.y() / 2)) * scale);

				sx = (int)(iconSize.x() * scale);
				sy = (int)(iconSize.x() * scale);
			} else {
				sx = (int)(mapObject.getMapIcon().getWidth() * scale);
				sy = (int)(mapObject.getMapIcon().getHeight() * scale);
	
				x = (int)((mapObject.getMapPosition().x() + offset.x() - (mapObject.getMapIcon().getWidth() / 2)) * scale);
				y = (int)((mapObject.getMapPosition().y() + offset.y() - (mapObject.getMapIcon().getHeight() / 2)) * scale);
				
			}
			
			if(x + sx < c1.x() || y + sy < c1.y() || x > c2.x() || y > c2.y()) continue;
			if(selectObject != null && selectObject.equals(mapObject)) g.drawImage(mapObject.getMapIconSelected(), x, y, sx, sy, null);
			else g.drawImage(mapObject.getMapIcon(), x, y, sx, sy, null);
		}			
		
		g.dispose();
		setRaster(img);					
	}

	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
	
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
 
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int x = (int)((mouseEvent.getPointRelativeToObject().x) / scale - offset.x() + (iconSize.x() / 2)); 
		int y = (int)((mouseEvent.getPointRelativeToObject().y) / scale - offset.y() + (iconSize.y() / 2));
		Console.ln("GUICanvas -> mouseReleased() -> mouse coords : " + x + ", " + y);
		
		
		MouseEvent mouseE = mouseEvent.getMouseEvent();
		if(mouseE.getButton() == MouseEvent.BUTTON3) {
			if(selectPosition == null) selectPosition = new VectorI();
			selectPosition.set(x, y);
			altClickAction.action();
		}else {			
			for(int i = 0; i < drawObjects.size(); i++) {
				T mapObject = drawObjects.get(i);
				if(mapObject.selectIfHit(x, y, iconSize.x(), iconSize.y())) {
					selectObject = mapObject;
					return;
				}
			}
		}
		
		
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	

	@Override
	public void updateObject() {
		updateGraphics();
	}


	
	
}
