/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import warped.application.entities.item.WarpedItem;
import warped.application.gui.GUIInventory;
import warped.application.state.WarpedFramework2D;
import warped.application.state.WarpedState;
import warped.audio.FrameworkAudio;
import warped.user.mouse.DefaultMouseController;
import warped.user.mouse.MouseEventType;
import warped.user.mouse.WarpedMouseController;
import warped.utilities.utils.Console;

public class WarpedMouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	/**------Handling of Mouse Events in Warped Framework
	 * When a mouse event occurs a new warped mouse event is created and passed to the Screen class.
	 * The Screen will then check if that mouse event occurred in any of the view ports. Where this is true that viewport will be passed the mouse event.
	 * The view port will then check to see if the mouse event occurred within any of its layers. Where this is true that layer will be passed the mouse event.
	 * 
	 * Each View Port Layer has a single variable that holds only the most recent mouse event.
	 * When that Layer is rendered by its parent View Port all objects will have their screen space coordinates calculated at this time.
	 * When the screen space coordinate is calculated for any object the view port layer will check if the mouse event (if one exists) occurred within the bounds.
	 * When an event does occur within the bounds of an object the layer will flag that object as its 'event object'.
	 * The layer will continue to render flagging any subsequent objects as the new 'event object'.
	 * 
	 * Once the layer has finished rendering the last object to have been flagged will be the top most object.
	 * The dispatch event function is then called and passes the event onto the 'event object.
	 * 
	 * The reason this system is necessary is because in warped framework there are instance where many game objects are drawn overlapping one another.
	 * This System allows for the mouse event to be passed to its correct object without adding much extra computation
	 * */
	
	
	public static final int PLAIN = 0;
	public static final int PRESS = 1;
	public static final int LOAD  = 2;
	
	private static WarpedMouseController mouseController = new DefaultMouseController();
	private static Point lastPoint = new Point(1,1);
	
	private static boolean inWindow   = true;
	private static boolean isDragging = false;
	public static boolean isTrapMouse = true;
	
	private static boolean isFocused  = false;
	private static boolean isPressed  = false;
	private static boolean isLeftPressed  = false;
	private static boolean isRightPressed  = false;
	private static boolean isLocked   = false;
	
	
	private static GUIInventory<?> dragGUI;
	private static WarpedItem<?> dragItem;
	private static boolean isDraggingItem = false;

	//private static boolean isDraggingItem = false;
	//private static GUIInventory<?> itemParent;
	//private static int dragItemIndex;
	private static int tick = 0;
	private static final int delay = 1;
	
	//--------
	//---------------- Access --------
	//--------
	public static void setMouseController(WarpedMouseController mouseController) {WarpedMouse.mouseController = mouseController;}
	public static void setTemporaryCursor(BufferedImage image) {
		mouseController.setTemporaryCursorImage(image);
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	} 
	public static void setIntermediateCursor(BufferedImage image) {
		mouseController.setIntermediateCursor(image);
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	}
	public static void resetIntermediateCursor() {
		mouseController.resetIntermediateCursor();
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	}
	
	//public static GUIInventory<?> getDragItemInventory() {return itemParent;}
	//public static int getDragItemIndex() { return dragItemIndex;}
	
	public static Point getPoint() {return mouseController.getMousePoint();}
	public static boolean isInWindow() {return inWindow;}
	public static boolean isPressed()  {return isPressed;}
	
	public static boolean isLeftPressed()  {return isLeftPressed;}
	public static boolean isRightPressed()  {return isRightPressed;}
	public static boolean isDragging() {return isDragging;}
	public static boolean isFocused()  {return isFocused;}


	public static boolean isDraggingItem() {return isDraggingItem;}
	
	
	public static void lockMouse() {isLocked = true;}
	public static void unlockMouse() {isLocked = false;}
	public static void toggleLock() {if(isLocked) isLocked = false; else isLocked = true;}
		
	
	public static void setCursorState(int cursorState) {mouseController.setCursorState(cursorState);}
	
	public static void resetCursor() {
		mouseController.setCursorState(PLAIN);
		mouseController.resetCursor();
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	}	
	
	public static void focus() {isFocused = true;}
	public static void unfocus() {isFocused = false;}

	//--------
	//---------------- Dragging -------
	//--------	
	
	/**
	 * */
	public static WarpedItem<?> getDraggedItem(){return dragItem;}
		
	/**Drag an item with the mouse
	 * @param dragGUI - the GUI that the item was dragged from.
	 * @param dragItem - the item that is being dragged.War
	 * @implNote Will set the cursor to match the dragged item.
	 * @author 5som3 
	 * */
	public static void dragItem(GUIInventory<?> dragGUI, WarpedItem<?> dragItem) {
		WarpedMouse.dragGUI = dragGUI;
		WarpedMouse.dragItem = dragItem;
		setTemporaryCursor(dragItem.raster());
		isDraggingItem = true;
	}
	
	/**Drop the item and reset the cursor.
	 * @implNote Will reset the cursor to the controller default.
	 * @author 5som3 */
	public static void dropItem() {
		if(isDraggingItem) {			
			isDraggingItem = false;
			isDragging = false;
			resetCursor();
		}
	}
	/*
	*/
	

	//--------
	//---------------- Update --------
	//--------
	public void update() {
		if(isDraggingItem && !isDragging) {
			tick++;
			if(tick > delay) {
				tick = 0;
				isDraggingItem = false;
				resetCursor();
				dragGUI.restoreItem(dragItem);
			}
		}
		if(mouseController.isLoadState() && mouseController.setLoadCursor()) WarpedWindow.getFrame().setCursor(mouseController.getCursor());
		if(!inWindow && isTrapMouse && isOutOfBounds(lastPoint)) {
			WarpedFramework2D.getRobot().mouseMove(lastPoint.x, lastPoint.y);
		}
	}
	
	
	//--------
	//---------------- Overrides --------
	//--------
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isLocked) return;
		else WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.WHEEL_ROTATION));
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(isLocked) return;
		else {			
			if(inWindow)isDragging = true;
			mouseController.setPoint(new Point((int)(e.getPoint().x / WarpedWindow.getWindowScale().x()), (int)(e.getPoint().y / WarpedWindow.getWindowScale().y())));
			WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.DRAG));
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(isLocked) return;
		else {			
			mouseController.setPoint(e.getPoint());
			WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.MOVE));
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(isLocked) return;
		if(!isPressed) {
			isPressed = true;
			if(!isDraggingItem) mouseController.setCursorState(PRESS);
			if(e.getButton() == MouseEvent.BUTTON1) isLeftPressed = true;
			else isRightPressed = true;
		}
		Console.ln("WarpedMouse -> mousePressed()");
		WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.BUTTON_PRESS));

	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(isLocked) return;
		Console.ln("WarpedMouse -> mouseRelase()");
		if(isPressed) {
			isPressed = false;
			isLeftPressed = false;
			isRightPressed = false;
		}
		if(e.getButton() == MouseEvent.BUTTON1) {			
			isDragging = false;
			mouseController.setCursorState(PLAIN);
		}
	
		if(WarpedState.isPaused()) FrameworkAudio.error.play();
		WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.BUTTON_RELEASE));
			
		
		/*
		if(WarpedKeyboard.isKeyLogging()) {
			WarpedKeyboard.clearKeyLog();
			WarpedKeyboard.stopKeyLogging();
		}
		*/
			
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		inWindow = true;
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		inWindow = false;
		isDragging = false;
		mouseController.setPoint(e.getPoint());

		if(isTrapMouse) {			
			int x, y;
			int ex = e.getX();
			int ey = e.getY();
			if(ex <= 0) x = 2;
			else if(ex >= WarpedWindow.getWindowWidth()) x = WarpedWindow.getWindowWidth() - 2;
			else x = ex;
			
			if(ey <= 0) y = 2;
			else if (ey >= WarpedWindow.getWindowHeight()) y = WarpedWindow.getWindowHeight() - 2;
			else y = ey;
			lastPoint.setLocation(x, y);
		}
	}
	
	private boolean isOutOfBounds(Point point) {
		if(point.x == 2 || point.y == WarpedWindow.getWindowWidth() - 2 || point.y == 2 || point.y == WarpedWindow.getWindowHeight() - 2) return true; else return false;
	}
	
	/** 6/6/23 -> Don't use -> is invalidated by mouse moving while clicking*/
	@Override
	public synchronized void mouseClicked(MouseEvent e) {}
	
}
