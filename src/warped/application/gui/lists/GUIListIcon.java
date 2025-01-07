/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.lists;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIListIcon<T> extends WarpedGUI {

	private Color backgroundColor = Colour.getColor(Colour.GREY_DARK);
	private Color borderColor = Color.BLACK;
	
	private boolean isAutoResize 	  = false;
	private boolean isEntryRemovable  = false;
	private boolean isDragging 	  	  = false;
	
	private Color entryTitleColor 	= Color.YELLOW;
	private Color entryTextColor 	= Color.WHITE;
	private int borderThickness 	= 2;
	private int entryThickness 		= 50;
	private int scrollBarThickness 	= 10;
	private int scrollButtonSize 	= 400;
	
	private int listWidth 	= 300;
	private int listHeight 	= 360;
	
	private int entryWidth 	= listWidth - scrollBarThickness;
	private int entryHeight = borderThickness + entryThickness;
 
	
	protected Vec2i textOffset = new Vec2i();
	private int textSize 	= 12;
	private int textStyle 	= Font.PLAIN;
	private Font font 		= UtilsFont.getPreferred();
	
	protected ArrayList<BufferedImage> listIcons   = new ArrayList<>(); 
	protected ArrayList<List<String>>  listEntrys  = new ArrayList<>();
	protected ArrayList<WarpedAction>  listActions = new ArrayList<>(); 
	
	private BufferedImage taskImage;
	
	private int scroll 			  	= 0;
	private double scrollProgress 	= 0.0;
	private int scrollMax 		  	= 0;
	private int scrollMin 		  	= 0;
	private int scrollStep 		  	= 8;
	private Color scrollBarColor 	= Color.getColor(getToolTipText());
	private Color scrollButtonColor = Colour.getColor(Colour.GREY_LIGHT);
	
	
	public GUIListIcon() {
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE); 
		setRaster(taskImage);
	}
	
	public GUIListIcon(int listWidth, int listHeight) {
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		entryWidth 	= listWidth - scrollBarThickness;   
		entryHeight = borderThickness + entryThickness; 
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(taskImage);
	}
	
	public GUIListIcon(int listWidth, int listHeight, boolean isAutoResize) {
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		this.isAutoResize = isAutoResize;
		entryWidth 	= listWidth - scrollBarThickness;   
		entryHeight = borderThickness + entryThickness; 
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(taskImage);
	}
	
	
	//--------
	//------------- Access ------------
	//--------
	public int getLength() {return listEntrys.size();}
	public void setListSize(int listWidth, int listHeight) {
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		entryWidth 	= listWidth - scrollBarThickness;   
		entryHeight = borderThickness + entryThickness; 
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		updateGraphics();
		setRaster(taskImage);
	}
	public void setListSize(int listWidth, int listHeight, int entryThickness) {
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		this.entryThickness = entryThickness;
		entryWidth 	= listWidth - scrollBarThickness;   
		entryHeight = borderThickness + entryThickness; 
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		updateGraphics();
		setRaster(taskImage);
	}
	
	public void scrollUp() {
		if(scrollMax <= 0) return;
		Console.ln("GUIListScroll -> scrollUp() -> scrollProgress : " + scrollProgress);
		scroll += scrollStep;
		if(scroll > scrollMax) scroll = scrollMax;
		scrollProgress = UtilsMath.dividePrecise(scroll, scrollMax);	
		updateGraphics();	
	}
	
	public void scrollDown() {
		if(scrollMax <= 0) return;
		Console.ln("GUIListScroll -> scrollDown() -> scrollProgress : " + scrollProgress);
		scroll -= scrollStep;
		if(scroll < scrollMin) scroll = scrollMin;
		scrollProgress = UtilsMath.dividePrecise(scroll, scrollMax);
		updateGraphics();		
	}
	
	public void setScroll(int scroll) {
		if(scrollMax <= 0) return;
		if(scroll > scrollMin && scroll < scrollMax) {
			this.scroll = scroll;
			scrollProgress = scroll / scrollMax;
			scrollProgress = UtilsMath.dividePrecise(scroll, scrollMax);	
			updateGraphics();
		} else Console.err("GUIScrollList -> setScroll() -> scroll is outside of domain : (min, scroll, max) : (" + scrollMin + ", " + scroll + ", " + scrollMax + ")");
	}

	public void setTextSize(int textSize) {
		if(textSize <= 0) {
			Console.err("GUIListScroll -> setTextSize() -> text size must be positive : " + textSize);
			return;
		}
		this.textSize = textSize;
		font = new Font("ListFont", textStyle, textSize);
	}
	
	public void sortAlphabet() {
		ArrayList<BufferedImage> sortedIcons   = new ArrayList<>();
		ArrayList<List<String>> sortedEntrys   = new ArrayList<>();
		ArrayList<WarpedAction> sortedActions = new ArrayList<>();
		
		List<String> strings = new ArrayList<>();
		
		listEntrys.forEach(l -> {strings.add(l.get(0));});
		Collections.sort(strings);
		
		for(int i = 0; i < strings.size(); i++) {
			for(int j = 0; j < listEntrys.size(); j++) {
				
				if(strings.get(i).equals(listEntrys.get(j).get(0))){
					sortedEntrys.add(listEntrys.get(j));
					sortedIcons.add(listIcons.get(j));
					sortedActions.add(listActions.get(j));
					break;
				}
				
			}
		}
		
		listEntrys  = sortedEntrys;
		listIcons   = sortedIcons;
		listActions = sortedActions;
		updateGraphics();
	}
	
	public void reverseOrder() {
		Collections.reverse(listEntrys);
		Collections.reverse(listIcons);
		Collections.reverse(listActions);
		updateGraphics();
	}
	
	public void clearList() {
		listEntrys.clear();
		listActions.clear();
		listIcons.clear();
	}
	
	
	//--------
	//------------- List Types - > Anonymous Inner Classes -> define functions add creation of list  ------------
	//--------
	public void setList(ArrayList<T> list){Console.err("GUIListIcon -> setList(ArrayList) -> this method needs to be created as an annonymous inner method when this object is created");}
	public void setList(T[] list){Console.err("GUIListIcon -> setList(Array) -> this method needs to be created as an annonymous inner method when this object is created");}
	
	//--------
	//------------- Graphics ------------
	//--------
	
	
	protected void addEntry(List<String> entryText) {
		listEntrys.add(entryText);
		updateGraphics();
		setScrollParameters();
	}
	
	protected void addEntry(List<String> entryText, WarpedAction entryAction) {
		listEntrys.add(entryText);
		listActions.add(entryAction);
		updateGraphics();
		setScrollParameters();
	}
	
	protected void removeEntry(int index) {
		if(index < 0 || index >= listEntrys.size()) {
			Console.err("GUIList -> removeEntry() -> index is outside of bound : " + index);
			return;
		}
		listEntrys.remove(index);
		listActions.remove(index);
		setScrollParameters();
		updateGraphics();
	}
	
	protected void setScrollParameters() {
		scrollMax = ((listEntrys.size() * entryHeight) - listHeight);
		if(scrollMax < 0) scrollMax = 0;
		scrollButtonSize = listHeight - scrollMax;
		if(scrollButtonSize < 20) scrollButtonSize = 20;
		if(scroll > scrollMax) {
			scroll = scrollMax;
			updateGraphics();
		}
	}
	
	private void drawBackground(Graphics g) {
		g.setColor(borderColor);
		g.fillRect(0, 0, listWidth, listHeight);
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, listWidth - borderThickness * 2, listHeight - borderThickness * 2);
	}
	
	private void drawScrollBar(Graphics g) {
		g.setColor(borderColor);
		g.fillRect(listWidth - scrollBarThickness, 0, scrollBarThickness, listHeight);
		g.setColor(scrollBarColor);
		g.fillRect((listWidth - scrollBarThickness) + borderThickness, borderThickness, scrollBarThickness - borderThickness * 2, listHeight - borderThickness * 2);
		g.setColor(scrollButtonColor);
		int buttonY = (int)((listHeight * scrollProgress) - (scrollButtonSize / 2));
		if(buttonY < 0) buttonY = 0;
		if(buttonY + scrollButtonSize > listHeight) buttonY = listHeight - scrollButtonSize;
		g.fillRect((listWidth - scrollBarThickness) + borderThickness, buttonY, scrollBarThickness - borderThickness * 2, scrollButtonSize);
	}

	private void drawEntry(Graphics g, int j, int x, int y) {
		List<String> entry = listEntrys.get(j);
		if(entry.size() == 0) {
			Console.err("GUIList -> drawEntry() -> entry has no strings");
			return;
		}
		if(x > listWidth || y > listHeight) {
			Console.err("GUIList -> drawEntry -> entry is outside of raster");
			return;
		}
		g.setFont(font);
		g.setColor(entryTitleColor);
		int textY = y + textSize + textOffset.y;
		int textX = x + textSize + textOffset.x;
		g.drawString(entry.get(0), textX, textY);
		g.setColor(entryTextColor);
		if(entry.size() > 1) {
			for(int i = 1; i < entry.size(); i++) {
				textY = (y + textSize) + (textSize * i) + textOffset.y;
				if(textY > listHeight) return;
				g.drawString(entry.get(i), textX, textY);								
			}
		}
		
		g.drawImage(listIcons.get(j), entryWidth - entryHeight, y, entryHeight, entryHeight, null);
		
		g.setColor(borderColor);
		g.fillRect(0, y + entryThickness, listWidth - scrollBarThickness, borderThickness);
	}
	
	
	//--------
	//------------- Update ------------
	//--------

	public void updateGraphics() {
		if(isAutoResize) resize();
		else taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = taskImage.getGraphics();
		drawBackground(g);
		drawScrollBar(g);
		for(int i = 0; i < listEntrys.size(); i++) {
			int entryY = borderThickness + (borderThickness * i) + (entryThickness * i) - scroll;
			if(entryY > listHeight) break;
			drawEntry(g, i, borderThickness, entryY);
		}
		g.dispose();
		setRaster(taskImage);	
	}
	
	private void resize() {
		this.listHeight = listHeight * (entryThickness + borderThickness) + (borderThickness * 2);
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
	}
	
	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

	
	//--------
	//------------- Interaction ------------
	//--------

	private void selectEntry(int index) {
		listActions.get(index).action();
	}
	
	@Override
	protected void mouseEntered() {
		Console.ln("GUIListIcon -> mouseEntered()");
	}

	@Override
	protected void mouseExited() {
		Console.ln("GUIListIcon -> mouseExited()");
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		isDragging = false;
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(scrollMax <= 0) return;
		double selectY = mouseEvent.getPointRelativeToObject().y;		
		isDragging = true;
		setScroll((int)((selectY / listHeight) * scrollMax));
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(scrollMax <= 0) return;
		int selectX = mouseEvent.getPointRelativeToObject().x;
		if(selectX > entryWidth) {
			double selectY = mouseEvent.getPointRelativeToObject().y;
			setScroll((int)((selectY / listHeight) * scrollMax));
		}
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(listEntrys.size() == 0) return;
		int selectY = mouseEvent.getPointRelativeToObject().y + scroll;
		int selectX = mouseEvent.getPointRelativeToObject().x;
		
		if(scrollMax > 0  && isDragging) return;
		if(selectX < listWidth - scrollBarThickness) {
			int entryIndex = (int) (Math.floor(selectY/entryHeight));
			if(entryIndex < 0) entryIndex = 0;
			if(entryIndex >= listEntrys.size()) entryIndex = listEntrys.size() - 1;
			selectEntry(entryIndex);
			if(isEntryRemovable) removeEntry(entryIndex);
		}
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		if(0 + mouseEvent.getWheelEvent().getWheelRotation() > 0) scrollUp();
		else scrollDown();
	}
}
