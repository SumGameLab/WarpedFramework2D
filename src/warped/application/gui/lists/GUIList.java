/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.lists;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUIList<T> extends WarpedGUI {
	
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color borderColor = Color.BLACK;
	
	private boolean isAutoResize = false;
	private boolean isEntryRemovable = true;
	
	private Color entryTitleColor = Color.YELLOW;
	private Color entryTextColor = Color.WHITE;
	private int borderThickness = 2;
	private int entryThickness = 38;
	private int indexThickness = borderThickness + entryThickness;
	
	private int width = 150;
	private int height = 400;

	private Font font = UtilsFont.getPreferred();
	
	private ArrayList<List<String>> listEntrys  = new ArrayList<>();
	private ArrayList<WarpedAction> listActions = new ArrayList<>(); 
	
	private BufferedImage taskImage;
	

	public GUIList() {
		taskImage = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE); 
		setRaster(taskImage);
	}
	
	//--------
	//---------------- Access --------
	//--------
	public int getLength() {return listEntrys.size();}
	
	public void clearList() {
		listEntrys.clear();
		listActions.clear();
		updateGraphics();
	}
	
	public void addEntry(List<String> entryText) {
		listEntrys.add(entryText);
		updateGraphics();
	}
	
	public void addEntry(List<String> entryText, WarpedAction entryAction) {
		listEntrys.add(entryText);
		listActions.add(entryAction);
		updateGraphics();
	}
	
	public void removeEntry(int index) {
		if(index < 0 || index >= listEntrys.size()) {
			Console.err("GUIList -> removeEntry() -> index is outside of bound : " + index);
			return;
		}
		listEntrys.remove(index);
		listActions.remove(index);
		updateGraphics();
	}
	
	
	//--------
	//---------------- List Types --------
	//--------
	public void setList(ArrayList<T> list) {Console.err("GUList -> setList() -> Annonymous inner function must be defined at ceration of list");};
	
	
	//--------
	//---------------- Update --------
	//--------
	private void updateGraphics() {
		if(isAutoResize) resize();
		else taskImage = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = taskImage.getGraphics();
		drawBackground(g);
		for(int i = 0; i < listEntrys.size(); i++) {
			List<String> entry = listEntrys.get(i);
			drawEntry(g, entry, borderThickness, borderThickness + (borderThickness * i) + (entryThickness * i));
		}
		g.dispose();
		setRaster(taskImage);	
	}
	
	private void resize() {
		this.height = height * (entryThickness + borderThickness) + (borderThickness * 2);
		taskImage = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
	}
	
	private void drawBackground(Graphics g) {
		g.setColor(borderColor);
		g.fillRect(0, 0, width, height);
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, width - borderThickness * 2, height - borderThickness * 2);
		
	}
	
	private void drawEntry(Graphics g, List<String> entry, int x, int y) {
		if(entry.size() == 0) {
			Console.err("GUIList -> drawEntry() -> entry has no strings");
			return;
		}
		if(x > width || y > height) {
			Console.err("GUIList -> drawEntry -> entry is outside of raster");
			return;
		}
		g.setFont(font);
		g.setColor(entryTitleColor);
		int textY = y + font.getSize();
		int textX = x + font.getSize();
		g.drawString(entry.get(0), textX, textY);
		g.setColor(entryTextColor);
		if(entry.size() > 1) {
			for(int i = 1; i < entry.size(); i++) {
				textY = (y + font.getSize()) + (font.getSize() * i);
				if(textY < 0 || textY > height) {
					Console.err("GUIList -> drawEntry() -> entry text is outside of raster");
					return;
				}
				g.drawString(entry.get(i), textX, textY);								
			}
		}
		g.setColor(borderColor);
		g.fillRect(0, y + entryThickness, width, borderThickness);
	}
	
	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}
	
	//--------
	//---------------- Interaction --------
	//--------
	public void select(int index) {listActions.get(index).action();}
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
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(listEntrys.size() == 0) return;
		int selectIndex = (int) (Math.floor(mouseEvent.getPointRelativeToObject().y / indexThickness));
		Console.ln("GUIList -> mouseReleased() -> selectIndex : " + selectIndex);
		if(selectIndex < 0) selectIndex = 0;
		if(selectIndex >= listEntrys.size()) selectIndex = listEntrys.size() - 1;
		select(selectIndex);
		if(isEntryRemovable) removeEntry(selectIndex);
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

}
