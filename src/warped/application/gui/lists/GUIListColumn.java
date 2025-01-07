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
import warped.application.object.WarpedObject;
import warped.user.actions.WarpedAction;
import warped.user.keyboard.WarpedKeyboardController;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIListColumn extends WarpedGUI{

	private Color backgroundColor = Colour.getColor(Colour.GREY_DARK);
	private Color borderColor = Color.BLACK;
	
	private boolean isAutoResize 	  = false;
	protected boolean isEntryRemovable  = false;
	private boolean isDragging 	  	  = false;
	
	private Color hoverColor = Colour.RED.getColor(80);
	private Color selectColor = Color.RED;
	protected Color entryTitleColor 	= Color.YELLOW;
	private Color entryTextColor 	= Color.WHITE;
	private int borderThickness 	= 2;
	private int entryThickness 		= 38;
	private int scrollBarThickness 	= 10;
	private int scrollButtonSize 	= 400;

	private int columnWidthMin = 50;
	private int netWidth  = 200;
	private int netHeight = 300;
	
	private int entryWidth 	= netWidth - scrollBarThickness;
	private int entryHeight = borderThickness + entryThickness;
 
	protected int hoveredIndex = -1;
	protected int selectedIndex = -1;
	
	private Font font 		= UtilsFont.getPreferred();
	
	protected ArrayList<List<String>> listEntrys  = new ArrayList<>();
	protected ArrayList<WarpedAction> listActions = new ArrayList<>(); 
	
	private BufferedImage taskImage;
	
	private int scroll 			  	= 0;
	private double scrollProgress 	= 0.0;
	private int scrollMax 		  	= 0;
	private int scrollMin 		  	= 0;
	private int scrollStep 		  	= 8;
	private Color scrollBarColor 	= Color.getColor(getToolTipText());
	private Color scrollButtonColor = Colour.getColor(Colour.GREY_LIGHT);
	
	private int columns = 2;
	private ArrayList<Integer> columnWidths = new ArrayList<>();
	private ArrayList<Integer> columnOffsets = new ArrayList<>();
	protected ArrayList<Vec2i> textOffsets = new ArrayList<>();
	
	public GUIListColumn() {
		initColumns();
		taskImage = new BufferedImage(netWidth, netHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(taskImage);
	}
	
	public GUIListColumn(int listWidth, int listHeight) {
		if(listWidth < columnWidthMin) {
			Console.err("GUIColumnScroll -> netWidth is too small, must be larger than " + columnWidthMin + " : " + listWidth);
			listWidth = columnWidthMin;
		}
		this.netWidth = listWidth;
		this.netHeight = listHeight;
		initColumns();
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(taskImage);
	}
	
	public GUIListColumn(int listWidth, int listHeight, int columns) {
		if(listWidth < columnWidthMin) {
			Console.err("GUIColumnScroll -> netWidth is too small, must be larger than " + columnWidthMin + " : " + listWidth);
			listWidth = columnWidthMin;
		}
		this.netWidth = listWidth;
		this.netHeight = listHeight;
		initColumns(columns);		
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(taskImage);
	}
	
	public GUIListColumn(int listWidth, int listHeight, int columns, boolean isAutoResize) {
		if(listWidth < columnWidthMin) {
			Console.err("GUIColumnScroll -> netWidth is too small, must be larger than " + columnWidthMin + " : " + listWidth);
			listWidth = columnWidthMin;
		}
		this.netWidth = listWidth;
		this.netHeight = listHeight;
		this.isAutoResize = isAutoResize;
		initColumns(columns);
		taskImage = new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(taskImage);
	}
	
	private void initColumns() {initColumns(columns);}
	private void initColumns(int columns) {
		if(columns < 2) {
			Console.err("GUIColumnScroll -> must have at least 2 columns, use a GUIList if you only need 1 column");
			columns = 2;
		}
		this.columns = columns;
		initColumnWidths();
		initColumnOffsets();
		initTextOffsets();
	}
	
	private void initColumnWidths() {
		int columnWidth = netWidth / columns;
		if(columnWidth < columnWidthMin) {
			Console.err("GUIColumnScroll -> netWidth is too small to accomodate number of columns : " + columns);
			columnWidth = columnWidthMin;
		}
	
		for(int i = 0; i < columns; i++) {
			columnWidths.add(columnWidth);
			columnOffsets.add(columnWidth * i);
		}
	}
	
	private void initColumnOffsets() {
		columnOffsets.clear();
		columnOffsets.add(0);
		int subTotal = 0;
		for(int i = 0; i < columnWidths.size(); i++) {
			subTotal += columnWidths.get(i);
			columnOffsets.add(subTotal);
		}
	}
	
	private void initTextOffsets() {
		for(int i = 0; i < columnWidths.size(); i++) {
			textOffsets.add(new Vec2i());
		}
	}
	
	//--------
	//------------- Access ------------
	//--------
	public int getLength() {return listEntrys.size();}
	public void setListSize(int listWidth, int listHeight) {
		this.netWidth = listWidth;
		this.netHeight = listHeight;
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

	
	public void setTextSize(int textSize) {font = UtilsFont.getPreferred(textSize);}
	
	
	public void setTextOffset(int column, Vec2i offset) {
		if(column < 0 || column > columnWidths.size()) {
			Console.err("GUIColumnScroll -> setTextOffset() -> column does not exist : " + column);
			return;
		}
		textOffsets.set(column, offset);
	}
	
	public void setColumnWidth(int column, int width) {
		if(column < 0 || column > columnWidths.size()) {
			Console.err("GUIColumnScroll -> setColumnWidth() -> column outside of domain 0 <= " + column + " <= " + columnWidths.size());
			return;
		}
		if(width < columnWidthMin) {
			Console.err("GUIColumnScroll -> setColumnWidth() -> column can't be smaller than : " + columnWidthMin);
			width = columnWidthMin;
		}
		columnWidths.set(column, width); 
		initColumnOffsets();
		updateGraphics();
	}
	
	public void setColumnWidths(ArrayList<Integer> columnWidths) {
		if(columnWidths.size() > columns) {
			Console.err("GUIColumnScroll -> setColumnWidth() -> number of columns is less than number of input Widths");
		}
		for(int i = 0; i < this.columnWidths.size(); i++) {
			this.columnWidths.set(i, columnWidths.get(i));
		}
		initColumnOffsets();
		updateGraphics();
	}
	
	public void resetSelected() {
		selectedIndex = -1;
		updateGraphics();
	}
	
	//--------
	//------------- List Types ------------
	//--------
	public void setList(WarpedObject obj) {Console.err("GUIListColumn -> setList() -> this method must be overwritten with a annonymous inner class");};
	public void setList(WarpedKeyboardController controller) {Console.err("GUIListColumn -> setList() -> this method must be overwritten with a annonymous inner class");};
	
	
	//--------
	//------------- Entries ------------
	//--------
	protected void clearList() {
		listEntrys.clear();
		listActions.clear();
		updateGraphics();
	}
	
	protected void addEntry(List<String> entryText) {
		listEntrys.add(entryText);
		updateGraphics();
	}
	
	protected void addEntry(List<String> entryText, WarpedAction entryAction) {
		listEntrys.add(entryText);
		listActions.add(entryAction);
		updateGraphics();
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
	
	private void setScrollParameters() {
		scrollMax = ((listEntrys.size() * entryHeight) - netHeight);
		if(scrollMax < 0) scrollMax = 0;
		scrollButtonSize = netHeight - scrollMax;
		if(scrollButtonSize < 20) scrollButtonSize = 20;
		if(scroll > scrollMax) {
			scroll = scrollMax;
			updateGraphics();
		}
	}
	
	
	
	
	//--------
	//------------- Graphics ------------
	//--------
	private void drawBackground(Graphics g) {
		g.setColor(borderColor);
		g.fillRect(0, 0, netWidth, netHeight);
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, netWidth - borderThickness * 2, netHeight - borderThickness * 2);
		
	}
	
	private void drawColumns(Graphics g) {
		for(int i = 0; i < columns - 1; i++) {
			g.fillRect((columnOffsets.get(i) + columnWidths.get(i)) - borderThickness, 0, borderThickness, netHeight);
		}		
	}
	
	private void drawScrollBar(Graphics g) {
		g.setColor(borderColor);
		g.fillRect(netWidth - scrollBarThickness, 0, scrollBarThickness, netHeight);
		g.setColor(scrollBarColor);
		g.fillRect((netWidth - scrollBarThickness) + borderThickness, borderThickness, scrollBarThickness - borderThickness * 2, netHeight - borderThickness * 2);
		g.setColor(scrollButtonColor);
		int buttonY = (int)((netHeight * scrollProgress) - (scrollButtonSize / 2));
		if(buttonY < 0) buttonY = 0;
		if(buttonY + scrollButtonSize > netHeight) buttonY = netHeight - scrollButtonSize;
		g.fillRect((netWidth - scrollBarThickness) + borderThickness, buttonY, scrollBarThickness - borderThickness * 2, scrollButtonSize);
	}

	private void drawEntry(Graphics g, List<String> entry, int x, int y, int index) {
		if(entry.size() == 0) {
			Console.err("GUIList -> drawEntry() -> entry has no strings");
			return;
		}
		if(x > netWidth || y > netHeight) {
			Console.err("GUIList -> drawEntry -> entry is outside of raster");
			return;
		}
		if(selectedIndex == index) {
			g.setColor(selectColor);
			g.fillRect(x, y, netWidth - borderThickness - scrollBarThickness, entryThickness);
		}
		else if(hoveredIndex == index) {
			g.setColor(hoverColor);
			g.fillRect(x, y, netWidth - borderThickness - scrollBarThickness, entryThickness);
		}
		g.setFont(font);
		g.setColor(entryTitleColor);
		int textY = y + font.getSize() + textOffsets.get(0).y;
		int textX = x + textOffsets.get(0).x;
		g.drawString(entry.get(0), textX, textY);
		g.setColor(entryTextColor);
		if(entry.size() > 1) {
			for(int i = 1; i < entry.size(); i++) {
				if(i >= columns) {
					Console.err("GUIColumnScroll -> drawEntry() -> entry has more columns than exist : " + i);
					continue;
				}
				textY = y + font.getSize() + textOffsets.get(i).y;
				textX = columnOffsets.get(i) + textOffsets.get(i).x;
				if(textX > netWidth) {
					Console.err("GUIColumnScroll -> drawEntry() -> entrys are too wide");
					return;
				}
				if(textY > netHeight) return;
				g.drawString(entry.get(i), textX, textY);								
			}
		}
		g.setColor(borderColor);
		g.fillRect(0, y + entryThickness, netWidth - scrollBarThickness, borderThickness);
	}
	
	private void resize() {
		this.netHeight = netHeight * (entryThickness + borderThickness) + (borderThickness * 2);
		netWidth = 0;
		columnWidths.forEach(w -> {netWidth += w;});
		taskImage = new BufferedImage(netWidth, netHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
	}
	
	
	//--------
	//------------- Update ------------
	//--------
	public void updateGraphics() {
		if(isAutoResize) resize();
		else taskImage = new BufferedImage(netWidth, netHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = taskImage.getGraphics();
		drawBackground(g);
		drawScrollBar(g);
		for(int i = 0; i < listEntrys.size(); i++) {
			List<String> entry = listEntrys.get(i);
			int entryY = borderThickness + (borderThickness * i) + (entryThickness * i) - scroll;
			if(entryY > netHeight) break;
			drawEntry(g, entry, borderThickness, entryY, i);
		}
		drawColumns(g);
		g.dispose();
		setRaster(taskImage);	
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
		selectedIndex = index;
		listActions.get(index).action();
		updateGraphics();
	}
	
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
	 hoveredIndex = -1;
	 updateGraphics();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		isDragging = false;
		int hoveredIndex = (int) (Math.floor((mouseEvent.getPointRelativeToObject().y + scroll)/entryHeight));
		if(this.hoveredIndex != hoveredIndex) {
			Console.ln("GUIColumnScroll -> mouseMoved() -> hovered index : " + hoveredIndex);
			this.hoveredIndex = hoveredIndex;
			updateGraphics();
		}
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		isDragging = true;
		///double selectX = mouseEvent.getPointRelativeToObject().x;
		double selectY = mouseEvent.getPointRelativeToObject().y;
		if(selectY < entryHeight) {
			
		} else {			
			if(scrollMax <= 0) return;		
			setScroll((int)((selectY / netHeight) * scrollMax));
		}
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(scrollMax <= 0) return;
		int selectX = mouseEvent.getPointRelativeToObject().x;
		if(selectX > entryWidth) {
			double selectY = mouseEvent.getPointRelativeToObject().y;
			setScroll((int)((selectY / netHeight) * scrollMax));
		}
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(listEntrys.size() == 0) return;
		int selectY = mouseEvent.getPointRelativeToObject().y + scroll;
		int selectX = mouseEvent.getPointRelativeToObject().x;
		
		if(scrollMax > 0  && isDragging) return;
		if(selectX < netWidth - scrollBarThickness) {
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
