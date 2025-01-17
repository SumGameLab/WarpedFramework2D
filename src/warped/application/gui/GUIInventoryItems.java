/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.entities.item.WarpedItem;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUIInventoryItems extends WarpedGUI {
	
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
	
	private boolean isDragging = false;
	private WarpedGroup<WarpedItem<?>> invent;
	private WarpedGroupIdentity inventoryID;
	
	public GUIInventoryItems(int columns, int rows) {
		this.columns = columns; 
		this.rows = rows;
		setRaster(new BufferedImage(1,1,WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public void selectInventory(WarpedGroupIdentity inventoryID) {
		if(inventoryID.getManagerType() != WarpedManagerType.ITEM) {
			Console.err("GUIInventory -> selectInventory() -> inventoryID is not an item Context Group");
			return;
		}
		this.inventoryID  = inventoryID;
		invent = WarpedState.itemManager.getGroup(inventoryID);
		iconWidth = invent.getMemberSize().x;
		iconHeight = invent.getMemberSize().y;
		itemSpacingX = iconWidth + columnMargin;
		itemSpacingY = iconHeight + rowMargin;
		iconOffsetX = columnMargin / 2;
		textOffsetY = iconHeight + font.getSize() + 2;
		textOffsetX = iconWidth / 6;
		updateGraphics();		
	}

	public void updateGraphics() {
		
		BufferedImage img = new BufferedImage(itemSpacingX * columns, itemSpacingY * rows, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		g.setColor(backgroundColor);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
				
		int x = 0;
		int y = 0;
		for(int i = 0; i < invent.getMemberCount(); i++) {
			if(x >= columns) {
				x = 0;
				y++;
			}
			if(y >= rows) {
				return;
			}
			
			int rx = x * itemSpacingX;
			int ry = y * itemSpacingY;
			
			g.drawImage(invent.getMember(i).raster(), rx + iconOffsetX , ry, iconWidth, iconHeight, null);
			g.setFont(font);
			g.setColor(textColor);
			g.drawString(invent.getMember(i).getQuantityString(), rx + textOffsetX, ry + textOffsetY);
			
			if(x == hoverX && y == hoverY) {
				g.setColor(hoverColor);
				g.fillRect(rx, ry, rx + iconWidth + columnMargin, ry + iconHeight + rowMargin);
			}
			
			x++;
		}
		
		g.dispose();
		setRaster(img);
	}
	
	public WarpedGroup<WarpedItem<?>> getInventory(){return invent;}
	public WarpedGroupIdentity getInventoryID() {return inventoryID;}
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
		hoverX = hoverY = -1;
		updateGraphics();
		
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int hx = Math.floorDiv(mouseEvent.getPointRelativeToObject().x, itemSpacingX);
		int hy = Math.floorDiv(mouseEvent.getPointRelativeToObject().y, itemSpacingY);
		
		if(hx < 0 || hy < 0 || hx >= columns || hy >= rows) return;
		
		int index = hx + hy * columns;
		if(index >= invent.getMemberCount()) return;
		
		//invent.getMember(index).toolTip();
		//invent.getMember(index).toolTip();
		//invent.getMember(index).toolTip();
		
		if(hoverX != hx || hoverY != hy) {
			//invent.getMember(index).unhovered();
			hoverX = hx;
			hoverY = hy;
			updateGraphics();
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
		if(index >= invent.getMemberCount()) return;
		
		WarpedMouse.dragItem(invent.getMember(index), this, index);
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int hx = Math.floorDiv(mouseEvent.getPointRelativeToObject().x, itemSpacingX);
		int hy = Math.floorDiv(mouseEvent.getPointRelativeToObject().y, itemSpacingY);
		
		if(hx < 0 || hy < 0 || hx >= columns || hy >= rows) return;

		if(WarpedMouse.isDraggingItem()) {
			int dropIndex = hx + hy * columns;
			Console.ln("GUIInventory -> mouseReleased() -> dropping item at index : " + dropIndex);
			if(dropIndex > invent.getMemberCount()) {
				Console.ln("GUIInventory -> dropIndex() -> drop index too high -> chaned to : " + dropIndex);
				dropIndex = invent.getMemberCount();
			}

			if(WarpedMouse.getDragItemInventory().getInventoryID().isEqual(inventoryID)) swapItems(WarpedMouse.getDragItemIndex(), dropIndex); //move item within the same invent
			else dropItem(WarpedMouse.getDragItemInventory(), WarpedMouse.getDragItemIndex(), dropIndex);	//move item from one invent to another
			
			
			return;							
		}
		
		int index = hx + hy * columns;
		if(index >= invent.getMemberCount()) return;
		
		invent.getMember(index).mouseEvent(mouseEvent);
	}

	private void swapItems(int dragIndex, int swapIndex) {
		
	}
	
	public void dropItem(GUIInventoryItems previousInventory, int dragIndex, int dropIndex) {
		Console.ln("GUIInventory -> dropItem() -> drag index : " + dragIndex + " -> drop index : " + dropIndex);
		WarpedItem<?> item = previousInventory.getItem(dragIndex);
		previousInventory.removeItem(dragIndex);
		addItem(item, dropIndex);
	}
	
	private WarpedItem<?> getItem(int itemIndex) {
		if(itemIndex < 0 || itemIndex >= invent.getMemberCount()) {
			Console.err("GUIInventory -> getItem() -> itemIndex out of bounds : " + itemIndex);
			return null;
		}
		return invent.getMember(itemIndex);
	}
	
	/**Note : this will also remove the item from the context group that contains it, not just the GUI*/
	private void removeItem(int dragItemIndex) {
		WarpedState.itemManager.getGroup(inventoryID).removeMember(dragItemIndex);
		updateGraphics();
	}
	/**Note : this will also add the item to this inventory's context group, not just add it to the GUI*/
	private void addItem(WarpedItem<?> item, int index) {
		WarpedState.itemManager.getGroup(inventoryID).addMember(item, index);
		updateGraphics();
	}
	
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

}
