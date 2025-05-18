/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import warped.application.entities.item.ItemBindable;
import warped.application.entities.item.WarpedItem;
import warped.application.state.WarpedInventory;
import warped.application.state.WarpedInventory.SortType;
import warped.graphics.window.WarpedMouse;
import warped.graphics.window.WarpedMouseEvent;
import warped.user.keyboard.WarpedKeyboard;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUIInventory<T extends ItemBindable<?>> extends WarpedGUI {
	
	private Color backgroundColor = new Color(50,50,50);
	
	private Color textColor = Color.YELLOW;
	private Font font = UtilsFont.getPreferred();

	private int columns = 8; 
	private int rows = 8;
	
	private int iconWidth = 64;
	private int iconHeight = 64;
	
	private int rowMargin = 20;
	private int columnMargin = 10;
	
	private int itemSpacingX = iconWidth + columnMargin;
	private int itemSpacingY = iconHeight + rowMargin;
	
	private int iconOffsetX = columnMargin / 2;
	private int textOffsetY = iconHeight + font.getSize() + 2;
	private int textOffsetX = iconWidth / 6;
	
	private int hoverX = -1;
	private int hoverY = -1;
	private Color hoverColor = new Color(60,60,60,60);
	
	private WarpedInventory<T> selectInvent;

	
	private static int dropIndex;
	private static int dragIndex;
	
	/**An interactive inventory with the specified number of rows and columns
	 * @param columns - the number of columns in the inventory.
	 * @param rows - the number of rows in the inventory. 
	 * @author 5som3*/
	public GUIInventory(int columns, int rows) {
		this.columns = columns; 
		this.rows = rows;
		setSize(itemSpacingX * columns, itemSpacingY * rows);
	}
	
	/**Select an inventory to inspect
	 * @param invent - the inventory to inspect
	 * @author 5som3*/
	public void selectInventory(WarpedInventory<T> selectInvent) {
		this.selectInvent = selectInvent;
		updateGraphics();		
	}

	/**Get the inventory currently displayed in the GUI.
	 * @return WarpedInventory<T> - the inventory currently selected.
	 * @author 5som3*/
	public WarpedInventory<T> getInventory(){return selectInvent;}

	/**Drop an item from the previous inventory into this inventory.
	 * @author 5som3*/
	public void dropItem() {
		WarpedItem<?> dropItem = WarpedMouse.getDraggedItem();
		if(dropItem.getObjectID() == null) {
			Console.err("GUIInventory -> dropItem() -> the item has no ID -> ignore this error if moving half stack");
		} else if(dropItem.getObjectID().getGroupID().getManagerID() != selectInvent.getGroupID().getManagerID()) {
			Console.err("GUIInventory -> dropItem() -> dropItem is not from the same item set : " + dropItem.getItemType());
			return;
		}		
		@SuppressWarnings("unchecked")
		WarpedItem<T> di = (WarpedItem<T>) dropItem;
		
		selectInvent.addMember(di, dropIndex);
		updateGraphics();
		
		dragIndex  = -1;
		dropIndex  = -1;
		WarpedMouse.dropItem();
	}
	
	/**Drop an item from the previous inventory into this inventory.
	 * @param previousInventory - the inventory that the item is being taken from.
	 * @param dragIndex - the index of the item in the previous inventory.
	 * @param dropIndex - the index where the item is to be inserted into this inventory.
	 * @author 5som3*/
	public void restoreItem(WarpedItem<?> dropItem) {
		if(dropItem.getObjectID() == null) {
			Console.err("GUIInventory -> dropItem() -> the item has no ID -> ignore this error if moving half stack");
		} else if(dropItem.getObjectID().getGroupID().getManagerID() != selectInvent.getGroupID().getManagerID()) {
			Console.err("GUIInventory -> dropItem() -> dropItem is not from the same item set : " + dropItem.getItemType());
			return;
		}		
		@SuppressWarnings("unchecked")
		WarpedItem<T> di = (WarpedItem<T>) dropItem;
		
		selectInvent.addMember(di, dragIndex);
		updateGraphics();
		
		dragIndex  = -1;
		dropIndex  = -1;
	}
			
	/**Sort the inventory with the specified sort type
	 * @param sort - the type of sorting to apply to the inventory
	 * @apiNote Graphics will be updated after the sorting has completed.
	 * @implNote The sorting will be applied to the inventory that this GUI is inspecting, (not just a visual change)
	 * @author 5som3*/
	public void sort(SortType sort) {
		selectInvent.sort(sort);
		updateGraphics();
	}
	

		
	/***/
	private void updateGraphics() {	
		Graphics g = getGraphics();
		
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
				
		int x = 0;
		int y = 0;
		for(int i = 0; i < selectInvent.size(); i++) {
			if(x >= columns) {
				x = 0;
				y++;
			}
			if(y >= rows) {
				return;
			}
			
			int rx = x * itemSpacingX;
			int ry = y * itemSpacingY;
			
			g.drawImage(selectInvent.getMember(i).raster(), rx + iconOffsetX , ry, iconWidth, iconHeight, null);
			g.setFont(font);
			g.setColor(textColor);
			g.drawString(selectInvent.getMember(i).getQuantityString(), rx + textOffsetX, ry + textOffsetY);
			
			if(x == hoverX && y == hoverY) {
				g.setColor(hoverColor);
				g.fillRect(rx, ry, rx + iconWidth + columnMargin, ry + iconHeight + rowMargin);
			}
			
			x++;
		}
		g.dispose();
		pushGraphics();
	}
	
	boolean leftDragging = false;
	@Override
	protected void mouseEntered() {
		if(WarpedMouse.isLeftPressed() && WarpedMouse.isDraggingItem()) {
			leftDragging = true;
		}
		
	}

	@Override
	protected void mouseExited() {
		leftDragging = false;
		hoverX = hoverY = -1;
		updateGraphics();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int hx = Math.floorDiv(mouseEvent.getPointRelativeToObject().x, itemSpacingX);
		int hy = Math.floorDiv(mouseEvent.getPointRelativeToObject().y, itemSpacingY);
		
		if(hx < 0 || hy < 0 || hx >= columns || hy >= rows) return;
		
		int index = hx + hy * columns;
		if(index >= selectInvent.size()) index = selectInvent.size() - 1;
		
		if(hoverX != hx || hoverY != hy) {
			//invent.getMember(index).unhovered();
			hoverX = hx;
			hoverY = hy;
			updateGraphics();
		}	
		if(WarpedMouse.isDraggingItem()) {			
			if(WarpedMouse.isRightPressed()) {
				if(WarpedMouse.getDraggedItem().getQuantity() > 1) {
					selectInvent.addMember(new WarpedItem<T>((T)WarpedMouse.getDraggedItem().getItemType()));
					WarpedMouse.getDraggedItem().consume();
					updateGraphics();
				} else dropItem();
			}
			if(leftDragging && !WarpedMouse.isPressed() && WarpedMouse.isDraggingItem()) {
				dropIndex = index;
				leftDragging = false;
				dropItem();
			}
		}
		
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		//if(mouseEvent.getMouseEvent().getButton() == MouseEvent.BUTTON3) return;
		if(WarpedMouse.isFocused() || WarpedMouse.isDraggingItem()) return;
		int hx = Math.floorDiv(mouseEvent.getPointRelativeToObject().x, itemSpacingX);
		int hy = Math.floorDiv(mouseEvent.getPointRelativeToObject().y, itemSpacingY);
		if(hx < 0 || hy < 0 || hx >= columns || hy >= rows) return;
		int index = hx + hy * columns;
		if(index >= selectInvent.size()) return;
		
		WarpedItem<T> selectItem = selectInvent.getMember(index);
		dragIndex = index;
		if(WarpedKeyboard.isKeyPressed(KeyEvent.VK_SHIFT) && selectItem.getQuantity() > 1) {
			WarpedMouse.dragItem(this, new WarpedItem<T>(selectItem.getItemType(), selectItem.getQuantity() / 2));
			selectItem.setQuantity(selectItem.getQuantity() - selectItem.getQuantity() / 2);
		} else {			
			WarpedMouse.dragItem(this, selectItem);		
			selectInvent.removeMember(index);
		}
		updateGraphics();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(WarpedMouse.isDraggingItem() && mouseEvent.getButton() == MouseEvent.BUTTON3) {
			if(WarpedMouse.getDraggedItem().getObjectID() == null) {
				Console.err("GUIInventory -> mousePressed() -> item has no ID -> ignore if this is a half stack");
			} else if(WarpedMouse.getDraggedItem().getObjectID().getGroupID().getManagerID() != selectInvent.getGroupID().getManagerID()) {
				Console.err("GUIInventory -> dropItem() -> dropItem is not from the same item set : " + WarpedMouse.getDraggedItem().getItemType());
				return;
			}	
			
			int hx = Math.floorDiv(mouseEvent.getPointRelativeToObject().x, itemSpacingX);
			int hy = Math.floorDiv(mouseEvent.getPointRelativeToObject().y, itemSpacingY);
			
			if(hx < 0 || hy < 0 || hx >= columns || hy >= rows) return;
			int index = hx + hy * columns;	
			dropIndex = index;
			if(WarpedMouse.getDraggedItem().getQuantity() > 1) {
				selectInvent.addMember(new WarpedItem<T>((T)WarpedMouse.getDraggedItem().getItemType()));
				WarpedMouse.getDraggedItem().consume();
				updateGraphics();
			} else dropItem();
		}
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(mouseEvent.getButton() == MouseEvent.BUTTON3) return;
		leftDragging = false;
		int hx = Math.floorDiv(mouseEvent.getPointRelativeToObject().x, itemSpacingX);
		int hy = Math.floorDiv(mouseEvent.getPointRelativeToObject().y, itemSpacingY);
		
		if(hx < 0 || hy < 0 || hx >= columns || hy >= rows) return;
		int index = hx + hy * columns;

		if(WarpedMouse.isDraggingItem() && mouseEvent.getButton() != MouseEvent.BUTTON3) {
			Console.ln("GUIInventory -> mouseReleased() -> dropping item at index : " + dropIndex);
			if(index > selectInvent.size()) {
				Console.ln("GUIInventory -> dropIndex() -> drop index too high -> chaned to : " + dropIndex);
				index = selectInvent.size();
			}

			dropIndex = index;
			dropItem();			
			return;							
		} 
			
		if(index >= selectInvent.size()) return;
		Console.ln("GUIInventory -> mouseReleased() -> passing event to member at : " + index);
		selectInvent.getMember(index).executeMouseEvent(mouseEvent);
	}


	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


}
