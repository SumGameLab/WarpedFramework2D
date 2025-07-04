/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.mouse;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import warped.application.entities.item.WarpedItem;
import warped.application.gui.GUIInventory;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.window.WarpedMouse;
import warped.graphics.window.WarpedWindow;
import warped.utilities.utils.Console;

public abstract class WarpedMouseController {

	private short cursorState = 0;
	
	protected short loadTick   = 0;
	protected static final short LOAD_DELAY  = 5; 
	protected short dropTick = 0;
	protected static final short DROP_DELAY = 1;
	
	private int loadCursorIndex = 0;	

	protected boolean isIntermediateCursor = false;
	
	protected Cursor plainCursor;
	protected Cursor pressCursor;
	protected Cursor temporaryCursor;
	protected Cursor intermediateCursor;
	
	protected Cursor cursor;
	
	protected Cursor loadCursor0;
	protected Cursor loadCursor1;
	protected Cursor loadCursor2;
	protected Cursor loadCursor3;
	protected Cursor loadCursor4;
	protected Cursor loadCursor5;
	protected Cursor loadCursor6;
	protected Cursor loadCursor7;	
	
	
	protected GUIInventory<?> dragGUI;
	protected WarpedItem<?> dragItem;
	protected boolean isDraggingItem = false;
	
	protected static Point zeroPoint = new Point();
	public Point mousePoint = new Point();
	
	private WarpedAction resetCursorAction = () -> {Console.err("WapredMouseController -> default reset cursor action, this should be overwritten by your game");};
	
	/**
	 * */
	public WarpedMouseController() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		plainCursor = toolkit.createCustomCursor(FrameworkSprites.mouse.getSprite(0), zeroPoint, "plainCursor");
		pressCursor = toolkit.createCustomCursor(FrameworkSprites.mouse.getSprite(1), zeroPoint, "pressCursor");
		
		loadCursor0 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(0), zeroPoint, "loadCursor_0");
		loadCursor1 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(1), zeroPoint, "loadCursor_1");
		loadCursor2 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(2), zeroPoint, "loadCursor_2");
		loadCursor3 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(3), zeroPoint, "loadCursor_3");
		loadCursor4 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(4), zeroPoint, "loadCursor_4");
		loadCursor5 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(5), zeroPoint, "loadCursor_5");
		loadCursor6 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(6), zeroPoint, "loadCursor_6");
		loadCursor7 = toolkit.createCustomCursor(FrameworkSprites.mouseLoad.getSprite(7), zeroPoint, "loadCursor_7");
	}
	
		
	public WarpedMouseController(BufferedImage plainCursor, BufferedImage pressCursor) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		this.plainCursor = toolkit.createCustomCursor(plainCursor , zeroPoint, "plainCursor");
		this.pressCursor = toolkit.createCustomCursor(pressCursor , zeroPoint, "plainCursor");
	}
	
	public void resetCursor() {resetCursorAction.action();}
	public int getCursorState() {return cursorState;}
	public boolean isLoadState() {if(cursorState == WarpedMouse.LOAD) return true; else return false;}
	
	public void setResetCursorAction(WarpedAction action) {this.resetCursorAction = action;}

	
	public void setCursorState(short cursorState) {
		this.cursorState = cursorState;
		if(isIntermediateCursor) cursor = intermediateCursor;
		else switch(cursorState) {
		case WarpedMouse.PLAIN: cursor = plainCursor; break;
		case WarpedMouse.PRESS: cursor = pressCursor; break;
		case WarpedMouse.LOAD: cursor = loadCursor0; break;
		default: Console.err("WarpedMouse -> setCursorState() -> invalid case : " + cursorState); return;
		} 
		WarpedWindow.getFrame().setCursor(cursor);
	}
	
	public Point getMousePoint() {return mousePoint;}
	public void setPoint(Point point) {mousePoint.setLocation(point);}
	public Cursor getCursor() {return cursor;}
	
	public boolean setLoadCursor() {
		loadTick++;
		if(loadTick > LOAD_DELAY) {
			loadTick = 0;
			loadCursorIndex++;
			if(loadCursorIndex > 7) loadCursorIndex = 0;
			switch(loadCursorIndex) {
			case 0: cursor = loadCursor0;	break;
			case 1: cursor = loadCursor1;   break;
			case 2: cursor = loadCursor2;   break;
			case 3: cursor = loadCursor3;   break;
			case 4: cursor = loadCursor4;   break;
			case 5: cursor = loadCursor5;   break;
			case 6: cursor = loadCursor6;   break;
			case 7: cursor = loadCursor7;   break;
			default:
				Console.err("WarpedMouseController -> getLoadCursor() -> invalid case : " + cursor);
				cursor = loadCursor0;
				break;
			}
			return true;
		} else return false;
	}
	
	public void setTemporaryCursorImage(BufferedImage img) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		temporaryCursor = toolkit.createCustomCursor(img, zeroPoint, "plainCursor");	
		cursor = temporaryCursor;
	}	
	
	public void setIntermediateCursor(BufferedImage img) {
		isIntermediateCursor = true;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		intermediateCursor = toolkit.createCustomCursor(img, zeroPoint, "plainCursor");	
		cursor = intermediateCursor;
	}
	
	public void setIntermediateCursor(BufferedImage img, short offsetX, short offsetY) {
		if(offsetX >= 32 || offsetY >= 32) {
			Console.err("WarpedMouseControler -> setIntermediateCursor() -> offset is out of bounds, must be less than curour bounds (32 on windows)");
			if(offsetX >= 32) offsetX = 31;
			if(offsetY >= 32) offsetY = 31;
		}
		isIntermediateCursor = true;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		intermediateCursor = toolkit.createCustomCursor(img, new Point(offsetX, offsetY), "plainCursor");	
		cursor = intermediateCursor;
	}
	
	public void resetIntermediateCursor() {
		isIntermediateCursor = false;
		setCursorState(cursorState);
	}
	
	/**
	 * */
	public boolean isDraggingItem() {return  isDraggingItem;}
	
	public WarpedItem<?> getDraggedItem(){return dragItem;}
		
	/**Drag an item with the mouse
	 * @param dragGUI - the GUI that the item was dragged from.
	 * @param dragItem - the item that is being dragged.War
	 * @implNote Will set the cursor to match the dragged item.
	 * @author 5som3 
	 * */
	public void dragItem(GUIInventory<?> dragGUI, WarpedItem<?> dragItem) {
		this.dragGUI = dragGUI;
		this.dragItem = dragItem;
		isDraggingItem = true;
	}
	
	/**Drop the item and reset the cursor.
	 * @implNote Will reset the cursor to the controller default.
	 * @author 5som3 */
	public void dropItem() {
		if(isDraggingItem) {			
			isDraggingItem = false;
		}
	}
	
	public void update() {
		if(isDraggingItem && !WarpedMouse.isDragging()) {
			dropTick++;
			if(dropTick > DROP_DELAY) {
				dropTick = 0;
				isDraggingItem = false;
				resetCursor();
				dragGUI.restoreItem(dragItem);
			}
		}
		
	}
	
}
