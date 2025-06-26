/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.functionalInterfaces.WarpedAction;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIListGrid<T> extends WarpedGUI {

	private VectorI gridSize  	     = new VectorI(8, 4);
	private VectorI entrySize 	     = new VectorI(100, 100);
	private VectorI hoverCoordinate  = new VectorI(0, 0);
	private VectorI selectCoordinate = new VectorI(0, 0);
	private int   selectIndex 	     = 0;

	protected T selectEntry;
	
	protected ArrayList<T> list = new ArrayList<>();
	
	protected ArrayList<String> entryNames 		= new ArrayList<>();
	protected ArrayList<BufferedImage> entryIcons = new ArrayList<>();
	
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
	
	private VectorI textOffset = new VectorI();
	
	private Font font = UtilsFont.getPreferred();
	
	private int scrollButtonHeight;
	private int scrollBarThickness;
	private int scrollButtonY;
	
	private WarpedAction selectAction = () -> {Console.ln("GUIListGridScroll -> default select action");};
	private WarpedAction dragAction = () -> {Console.ln("GUIListGridScroll -> default select action");};
	private WarpedAction enteredAction = () -> {Console.ln("GUIListGridScroll -> default select action");};
	
	
	/**A list with the specified number of rows and columns.
	 * @param columns - the number of columns in the grid. (the width of the grid in grid cells).
	 * @param rows - the number of rows in the grid. (the height of the grid in grid cells).
	 * @author 5som3 */
	public GUIListGrid(int columns, int rows) {
		gridSize.set(columns, rows);	
		setSize(gridSize.x() * entrySize.x() + scrollBarThickness, gridSize.y() * entrySize.y());
		updateGraphics();
	}
	
	/**A list with the specified number of rows and columns.
	 * @param columns - the number of columns in the grid. (the width of the grid in grid cells).
	 * @param rows - the number of rows in the grid. (the height of the grid in grid cells).
	 * @param drawNames - if true the names of the entrys will be visible else they will not be rendered
	 * @author 5som3 */
	public GUIListGrid(int columns, int rows, boolean drawNames) {
		gridSize.set(columns, rows);
		setSize(gridSize.x() * entrySize.x() + scrollBarThickness, gridSize.y() * entrySize.y());
		this.drawNames = drawNames;
		updateGraphics();
	}


	
	/**Clear all entrys in the grid.
	 * @author 5som3*/
	public void clear() {
		isDragging = false;
		selectEntry = null;
		list = null;
		entryNames.clear(); 
		scrollOffset = 0;
	}
	
	/**Set an action to be executed when the grid is selected.
	 * @param selectAction - the action to execute when the grid is selected.
	 * @author 5som3*/
	public void setSelectAction(WarpedAction selectAction) {this.selectAction = selectAction;}
	
	/**Set the action to execute when the grid is dragged.
	 * @param dragAction - the action will execute multiple times while dragged.
	 * @author 5som3*/
	public void setDragAction(WarpedAction dragAction) {this.dragAction = dragAction;}
	
	/**Set an action to occur when the mouse enters the grid
	 * @param enteredAction - the action will execute once when the mouse enters the grid.
	 * @author 5som3*/
	public void setEnteredAction(WarpedAction enteredAction) {this.enteredAction = enteredAction;}
	
	/**Get the list of entrys in this grid.
	 * @return ArrayList<T> - a list of all the entrys.
	 * @author 5som3 */
	public ArrayList<T> getList(){return list;}
	
	/**Get the size of the list.
	 * @return int - the number of entrys in the list.
	 * @author 5som3*/
	public int size() {return list.size();}
	
	/**Set the size of the list's sprite.
	 * @param width - the width of the list in pixels.
	 * @param height - the height of the list in pixels.
	 * @apiNote will also update the scroll parameters and graphics to match the new size.
	 * @author 5som3*/
	public void setSize(int width, int height) {
		sprite.setSize(width, height);
		updateScrollParameters();
		updateGraphics();
	}
	
	/**Get the selected entry.
	 * @return T - returns the selected instance of the same type as this list.
	 * @author 5som3*/
	public T getSelectEntry() {return selectEntry;}
	
	/**Get the index of the selected entry.
	 * @return int - the index of the selectEntry.
	 * @author 5som3*/
	public int getSelectIndex() {return selectIndex;}
	
	/**Get the position of the select entry in pixels.
	 * @return VectorI - a new vector containing the position in pixels.
	 * @apiNote The position is measured from the top left corner of the ListGrid to the top left corner of the entry.
	 * @author 5som3*/
	public VectorI getSelectEntryPosition() {return new VectorI(selectCoordinate.x() * entrySize.x(), selectCoordinate.y() * entrySize.y());}
	
	/**Get the coordinate of the select entry.
	 * @return VectorI - the coordinate in grid cells (column, row).
	 * @author 5som3*/
	public VectorI getSelectEntryCoordinate() {return selectCoordinate;}
	
	/**Remove an entry from the grid.
	 * @param modifyOriginal - if true the entry will also be removed from the original list as well as this gui.
	 * @author 5som3*/
	public void removeSelectEntry(boolean modifyOriginal) {
		if(selectIndex < 0) {
			Console.err("GUIListGridScroll -> removeSelectEntry() -> index is out of bounds : " + selectIndex);
			return;
		}
		if(modifyOriginal && selectIndex < list.size()) list.remove(selectIndex);
		if(selectIndex < entryNames.size()) entryNames.remove(selectIndex);		
	}
	
	/**Set the size of each cell in the grid.
	 *@param width - the width of each cell in pixels.
	 *@param height - the height of each cell in pixels.
	 *@author 5som3*/
	public void setEntrySize(int width, int height) {
		entrySize.set(width, height);
		setSize(gridSize.x() * entrySize.x() + scrollBarThickness, gridSize.y() * entrySize.y());
		updateGraphics();
	}
	
	/**Set the scroll for the grid.
	 * @param scrollOffset - the amount that the grid is scrolled (if possible).
	 * @author 5som3 */
	public void setScroll(int scrollOffset) {
		if(scrollMax <= 0) return;
		if(scrollOffset > 0 && scrollOffset < scrollMax) {
			this.scrollOffset = scrollOffset;
			scrollProgress = scrollOffset / scrollMax;
			scrollProgress = UtilsMath.divide(scrollOffset, scrollMax);	
			updateGraphics();
		} else Console.err("GUIScrollList -> setScroll() -> scroll is outside of domain : (min, scroll, max) : (" + 0 + ", " + scrollOffset + ", " + scrollMax + ")");
	}
	
	
	private void updateScrollParameters() {
		scrollMax = (Math.floorDiv(size(), gridSize.y()) * entrySize.y()) - getHeight(); 
		Console.ln("GUIListGridScroll -> setScrollParemeters() -> scrollMax : " + scrollMax);
		if(scrollMax < 0) scrollMax = 0;
		scrollButtonHeight = getHeight() - scrollMax;
		if(scrollButtonHeight < 20) scrollButtonHeight = 20;
		if(scrollOffset > scrollMax) {
			scrollOffset = scrollMax;
			updateGraphics();
		}
	}

	protected void updateGraphics() {
		Graphics g = getGraphics();
		
		g.setColor(scrollBarColor);
		g.fillRect(getWidth() - scrollBarThickness, 0, scrollBarThickness, getHeight());
		
		g.setColor(scrollButtonColor);
		g.fillRect(getWidth() - scrollBarThickness, scrollButtonY, scrollBarThickness, scrollButtonHeight);
		
		for(int i = 0; i < list.size(); i++) {
			int xCoord = i % gridSize.x();
			int yCoord = Math.floorDiv(i, gridSize.x());
			
			int drawX =  xCoord * entrySize.x();
			int drawY = (yCoord * entrySize.y()) - scrollOffset;
			
			g.setColor(gridColor);
			g.fillRect(drawX, drawY, entrySize.x(), entrySize.y());
			
			g.setColor(backgroundColor);
			g.fillRect(drawX + gridPadding, drawY + gridPadding, entrySize.x() - gridPadding * 2, entrySize.y() - gridPadding * 2);
			
			g.drawImage(entryIcons.get(i), drawX, drawY, entrySize.x(), entrySize.y(), null);
			
			if(drawNames) {				
				g.setColor(nameColor);
				g.setFont(font);
				g.drawString(entryNames.get(i), drawX + textOffset.x(), drawY + textOffset.y() + font.getSize());		
			}
			
			if(hoverCoordinate.isEqual(xCoord, yCoord)) {
				g.setColor(hoverColor);
				g.fillRect(drawX, drawY,  entrySize.x(), entrySize.y());
			}
		}
		
		g.dispose();
		pushGraphics();
	}
	
	/**Override this method when creating a new GUIListGrid.
	 * @param list - set the entry Icons and names with the data you want to list from the type T.
	 * @author 5som3*/
	public void setList(ArrayList<T> list) {Console.ln("GUIListGridScroll -> setList () -> default set list method should be overridden with an anonymous inner method defined and object creation");}	
	

	@Override
	protected void mouseExited() {hoverCoordinate.set(-1);}
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int hx = Math.floorDiv( mouseEvent.getPointRelativeToObject().x, entrySize.x());
		int hy = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), entrySize.y());
		
		if(hx < 0 || hy < 0 || hx >= gridSize.x() || hy >= gridSize.y()) return;
		
		if(!hoverCoordinate.isEqual(hx, hy)) {
			hoverCoordinate.set(hx, hy);		
			updateGraphics();
		}		
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		//TODO make dragging in a designated region, dragging at the moment will drag action and drag entrys at the same time
		
		int sx = Math.floorDiv( mouseEvent.getPointRelativeToObject().x, entrySize.x());
		int sy = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), entrySize.y());
		
		selectIndex = sx + sy * gridSize.x();
		if(sx < 0 || sy < 0 || sx >= gridSize.x() || sy >= gridSize.y() || selectIndex >= size()) return;
		//if(sx < 0 || sy < 0 || sx >= gridSize.x() || sy > size() / gridSize.x() || sx + (sy * gridSize.x()) >= size()) return;		
		selectCoordinate.set(sx, sy);	
		selectEntry = list.get(selectIndex);
		
		dragAction.action();
		
		if(scrollMax <= 0) return;
		double selectY = mouseEvent.getPointRelativeToObject().y;		
		isDragging = true;
		setScroll((int)((selectY / getHeight()) * scrollMax));
		Console.ln("GUIListGridScroll -> mouseDragged -> selectY : " + selectY);
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(isDragging) {
			isDragging = false;
			return;
		}
		int sx = Math.floorDiv( mouseEvent.getPointRelativeToObject().x, entrySize.x());
		int sy = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), entrySize.y());
		
		if(sx < 0 || sy < 0 || sx >= gridSize.x() || sy > size() / gridSize.x() || sx + (sy * gridSize.x()) >= size()) return;
		
		if(!selectCoordinate.isEqual(sx, sy)) {
			selectCoordinate.set(sx, sy);		
			selectEntry = list.get(selectCoordinate.x() + selectCoordinate.y() * gridSize.x());
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
