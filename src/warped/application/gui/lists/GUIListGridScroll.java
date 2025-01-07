/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.lists;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIListGridScroll<T> extends WarpedGUI {

	private Vec2i gridSize  	   = new Vec2i(8, 4);
	private Vec2i entrySize 	   = new Vec2i(100, 100);
	private Vec2i hoverCoordinate  = new Vec2i();
	private Vec2i selectCoordinate = new Vec2i();
	private int   selectIndex 	   = 0;

	protected T selectEntry;
	
	protected ArrayList<T> list = new ArrayList<>();
	
	protected ArrayList<BufferedImage> entryIcons = new ArrayList<>();
	protected ArrayList<String> entryNames 		= new ArrayList<>();
	
	boolean drawNames = true;
	
	private int scrollOffset 		= 0; //number of pixels items in grid are shifted down
	private int scrollMax 			= 0; //pixels - maximum size of scroll offset - the difference between the height of the grid and the theoretical max height of current entrys 
	private double scrollProgress 	= 0; //scroll as percentage
	
	private int gridPadding = 1;
	
	boolean isDragging = false;
	
	private Color gridColor 		= Color.BLACK;
	private Color backgroundColor 	= Colour.GREY_DARK.getColor();
	private Color scrollBarColor	= Colour.GREY_DARK_DARK.getColor();
	private Color scrollButtonColor = Colour.GREY_LIGHT.getColor();
	private Color hoverColor  		= Colour.HOVER.getColor();
	private Color nameColor			= Color.YELLOW;
	
	private Vec2i textOffset = new Vec2i();
	
	private Font font = UtilsFont.getPreferred();
	
	private int scrollButtonHeight;
	private int scrollBarThickness;
	private int scrollButtonY;
	
	private WarpedAction selectAction = () -> {Console.ln("GUIListGridScroll -> default select action");};
	private WarpedAction dragAction = () -> {Console.ln("GUIListGridScroll -> default select action");};
	private WarpedAction enteredAction = () -> {Console.ln("GUIListGridScroll -> default select action");};
	
	
	
	public GUIListGridScroll(int gridWidth, int gridHeight) {
		gridSize.set(gridWidth, gridHeight);	
	}
	public GUIListGridScroll(int gridWidth, int gridHeight, boolean drawNames) {
		gridSize.set(gridWidth, gridHeight);	
		this.drawNames = drawNames;
	}
	//--------
	//------------- Access ------------
	//--------
	public void clear() {
		isDragging = false;
		selectEntry = null;
		list = null;
		entryIcons.clear();
		entryNames.clear(); 
		scrollOffset = 0;
	}
	public void setSelectAction(WarpedAction selectAction) {this.selectAction = selectAction;}
	public void setDragAction(WarpedAction dragAction) {this.dragAction = dragAction;}
	public void setEnteredAction(WarpedAction enteredAction) {this.enteredAction = enteredAction;}
	public int listWidth()  {return (gridSize.x * entrySize.x) + scrollBarThickness;}
	public int listHeight() {return  gridSize.y * entrySize.y;}
	public ArrayList<T> getList(){return list;}
	public int size() {return entryIcons.size();}
	public T getSelectEntry() {return selectEntry;}
	public int getSelectIndex() {return selectIndex;}
	public Vec2i getSelectEntryPosition() {return new Vec2i(selectCoordinate.x * entrySize.x, selectCoordinate.y * entrySize.y);}
	public Vec2i getSelectEntryCoordinate() {return selectCoordinate;}
	public void removeSelectEntry(boolean modifyOriginal) {
		if(selectIndex < 0) {
			Console.err("GUIListGridScroll -> removeSelectEntry() -> index is out of bounds : " + selectIndex);
			return;
		}
		if(modifyOriginal && selectIndex < list.size()) list.remove(selectIndex);
		if(selectIndex < entryIcons.size()) entryIcons.remove(selectIndex);
		if(selectIndex < entryNames.size()) entryNames.remove(selectIndex);		
	}
	
	public void setEntrySize(int x, int y) {entrySize.set(x, y);}
	
	
	public void setScroll(int scrollOffset) {
		if(scrollMax <= 0) return;
		if(scrollOffset > 0 && scrollOffset < scrollMax) {
			this.scrollOffset = scrollOffset;
			scrollProgress = scrollOffset / scrollMax;
			scrollProgress = UtilsMath.dividePrecise(scrollOffset, scrollMax);	
			updateGraphics();
		} else Console.err("GUIScrollList -> setScroll() -> scroll is outside of domain : (min, scroll, max) : (" + 0 + ", " + scrollOffset + ", " + scrollMax + ")");
	}
	
	protected void setScrollParameters() {
		scrollMax = (Math.floorDiv(size(), gridSize.y) * entrySize.y) - listHeight(); 
		Console.ln("GUIListGridScroll -> setScrollParemeters() -> scrollMax : " + scrollMax);
		if(scrollMax < 0) scrollMax = 0;
		scrollButtonHeight = listHeight() - scrollMax;
		if(scrollButtonHeight < 20) scrollButtonHeight = 20;
		if(scrollOffset > scrollMax) {
			scrollOffset = scrollMax;
			updateGraphics();
		}
	}
	//--------
	//---------------- Graphics --------
	//--------
	public void updateGraphics() {
		
		BufferedImage img = new BufferedImage(listWidth(), listHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		g.setColor(scrollBarColor);
		g.fillRect(listWidth() - scrollBarThickness, 0, scrollBarThickness, listHeight());
		
		g.setColor(scrollButtonColor);
		g.fillRect(listWidth() - scrollBarThickness, scrollButtonY, scrollBarThickness, scrollButtonHeight);
		
		for(int i = 0; i < entryIcons.size(); i++) {
			int xCoord = i % gridSize.x;
			int yCoord = Math.floorDiv(i, gridSize.x);
			
			int drawX =  xCoord * entrySize.x;
			int drawY = (yCoord * entrySize.y) - scrollOffset;
			
			g.setColor(gridColor);
			g.fillRect(drawX, drawY, entrySize.x, entrySize.y);
			
			g.setColor(backgroundColor);
			g.fillRect(drawX + gridPadding, drawY + gridPadding, entrySize.x - gridPadding * 2, entrySize.y - gridPadding * 2);
			
			g.drawImage(entryIcons.get(i), drawX, drawY, entrySize.x, entrySize.y, null);
			
			if(drawNames) {				
				g.setColor(nameColor);
				g.setFont(font);
				g.drawString(entryNames.get(i), drawX + textOffset.x, drawY + textOffset.y + font.getSize());		
			}
			
			if(hoverCoordinate.isEqual(xCoord, yCoord)) {
				g.setColor(hoverColor);
				g.fillRect(drawX, drawY,  entrySize.x, entrySize.y);
			}
		}
		
		g.dispose();
		setRaster(img);
	}
	
	//--------
	//---------------- List Types --------
	//--------
	public void setList(ArrayList<T> list) {Console.ln("GUIListGridScroll -> setList () -> default set list method should be overridden with an anonymous inner method defined and object creation");}	
	
	//--------
	//---------------- Update --------
	//--------
	@Override
	protected void updateRaster() {return;}
	
	@Override
	protected void updateObject() {return;}
	
	@Override
	protected void updatePosition() {return;}

	
	//--------
	//---------------- Interaction --------
	//--------
	@Override
	protected void mouseExited() {hoverCoordinate.set(-1);}
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int hx = Math.floorDiv( mouseEvent.getPointRelativeToObject().x, entrySize.x);
		int hy = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), entrySize.y);
		
		if(hx < 0 || hy < 0 || hx >= gridSize.x || hy >= gridSize.y) return;
		
		if(!hoverCoordinate.isEqual(hx, hy)) {
			hoverCoordinate.set(hx, hy);		
			updateGraphics();
		}		
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		//TODO make dragging in a designated region, dragging at the moment will drag action and drag entrys at the same time
		
		int sx = Math.floorDiv( mouseEvent.getPointRelativeToObject().x, entrySize.x);
		int sy = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), entrySize.y);
		
		selectIndex = sx + sy * gridSize.x;
		if(sx < 0 || sy < 0 || sx >= gridSize.x || sy >= gridSize.y || selectIndex >= size()) return;
		//if(sx < 0 || sy < 0 || sx >= gridSize.x || sy > size() / gridSize.x || sx + (sy * gridSize.x) >= size()) return;		
		selectCoordinate.set(sx, sy);	
		selectEntry = list.get(selectIndex);
		
		dragAction.action();
		
		if(scrollMax <= 0) return;
		double selectY = mouseEvent.getPointRelativeToObject().y;		
		isDragging = true;
		setScroll((int)((selectY / listHeight()) * scrollMax));
		Console.ln("GUIListGridScroll -> mouseDragged -> selectY : " + selectY);
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(isDragging) {
			isDragging = false;
			return;
		}
		int sx = Math.floorDiv( mouseEvent.getPointRelativeToObject().x, entrySize.x);
		int sy = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), entrySize.y);
		
		if(sx < 0 || sy < 0 || sx >= gridSize.x || sy > size() / gridSize.x || sx + (sy * gridSize.x) >= size()) return;
		
		if(!selectCoordinate.isEqual(sx, sy)) {
			selectCoordinate.set(sx, sy);		
			selectEntry = list.get(selectCoordinate.x + selectCoordinate.y * gridSize.x);
		}		
		
		selectAction.action();		
	}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
	
	}


	@Override
	protected void mouseEntered() {
		enteredAction.action();
		
	}
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	
	
	
	
	
	
	
	
}
