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

	/*------Handling of Mouse Events in Warped Framework
	 * When a mouse event occurs a new warped mouse event is created and passed to the WarpedWindow.
	 * The WarpedWindow will then check if that mouse event occurred in any of the view ports. Where this is true that viewport will be passed a clone of the mouse event.
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
	
	
	public static final short PLAIN = 0;
	public static final short PRESS = 1;
	public static final short LOAD  = 2;
	
	private static WarpedMouseController mouseController = new DefaultMouseController();
	private static Point lastPoint = new Point(1,1);
	
	private static boolean inWindow   = true;
	private static boolean isDragging = false;
	public static boolean isTrapMouse = true;
	
	private static boolean isFocused  	  = false;
	private static boolean isPressed  	  = false;
	private static boolean isLeftPressed  = false;
	private static boolean isRightPressed = false;
	private static boolean isLocked 	  = false;
	
		
	/**Set the mouse controller for the application.
	 * @param mouseController - the controller for the mouse.
	 * @author 5som3*/
	public static void setMouseController(WarpedMouseController mouseController) {WarpedMouse.mouseController = mouseController;}
	
	/**Set the cursors image temporarily.
	 * @param image - the image for the cursor to have temporarily. 
	 * @implNote The cursor select point will be the top left corner pixel of the image
	 * @implNote Image will be scaled to fit the the cursor size.
	 * @author 5som3*/
	public static void setTemporaryCursor(BufferedImage image) {
		mouseController.setTemporaryCursorImage(image);
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	} 
	
	/**Set the intermediate cursor.
	 * @param image - the image for the cursor to have intermittently.
	 * @implNote The cursor select point will be the top left corner pixel of the image
	 * @implNote Image will be scaled to fit the the cursor size.
	 * @author 5som3*/
	public static void setIntermediateCursor(BufferedImage image) {
		mouseController.setIntermediateCursor(image);
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	}
	
	/**Restore the cursor to the mouseController default.
	 * @implNote The cursor select point will be the top left corner pixel of the image
	 * @implNote Image will be scaled to fit the the cursor size.
	 * @author 5som3*/
	public static void resetIntermediateCursor() {
		mouseController.resetIntermediateCursor();
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	}
	
	/**Get the current point of the mouse in the window.
	 * @return Point - the point of the cursor.
	 * @apiNote point is measured in pixels relative to the top left corner of the window.
	 * @apiNote point is unscaled 
	 * @author 5som3*/
	public static Point getPoint() {return mouseController.getMousePoint();}
	
	/**Check if the mouse is contained within the application window.
	 * @return boolean - true if the mouse is over the application window else false.
	 * @author 5som3*/
	public static boolean isInWindow() {return inWindow;}
	
	/**Check if the mouse is currently pressed. 
	 * @return boolean - true if any of the buttons on the mouse is pressed else false.
	 * @author 5som3*/
	public static boolean isPressed()  {return isPressed;}
	
	/**Is the left mouse button currently pressed.
	 * @return boolean - true if the left mouse button is currently pressed, else false.
	 * @author 5som3*/
	public static boolean isLeftPressed()  {return isLeftPressed;}
	
	/**Is the right mouse button currently pressed.
	 * @return boolean - true if the right mouse button is currently pressed, else false.
	 * @author 5som3*/
	public static boolean isRightPressed()  {return isRightPressed;}
	
	/**Is the mouse currently dragging.
	 * @return boolean - true if the mouse is currently dragging, else false.
	 * @apiNote The mouse can be 'lock dragged' with the right mouse in some instances.
	 * @apiNote In these cases the isDragging state will stay true until the left mouse is next pressed.
	 * @author 5som3*/
	public static boolean isDragging() {return isDragging;}
	
	/**Is the mouse currently focused.
	 * @return boolean - true if the mouse is dragging a GUIButton
	 * @implNote Is focused allows for the mouse to leave the button and continue dragging it.
	 * @implNote Without is focused the mouse can escape when dragging GUI very quickly over the window.
	 * @author 5som3*/
	public static boolean isFocused()  {return isFocused;}

	/**Is the mouse currently dragging an item.
	 * @return boolean - true if the mouse is dragging an item, else false.
	 * @author 5som3*/
	public static boolean isDraggingItem() {return  mouseController.isDraggingItem();}
	
	/**Set the lock state of the mouse
	 * @param isLocked - if true mouse events will be locked (mouse events will be blocked).
	 * @author 5som3*/
	public static void setLock(boolean isLocked) {WarpedMouse.isLocked = isLocked;}
	
	/**Toggle the lock state of the mouse.
	 * @author 5som3*/
	public static void toggleLock() {if(isLocked) isLocked = false; else isLocked = true;}
		
	/**Set the state of the cursor.
	 * @param cursorState - the cursor state.
	 * @apiNote WarpedMouse.PLAIN - the default cursor state.
	 * @apiNote WapredMouse.PRESS - the press cursor state.
	 * @apiNote WarpedMouse.LOAD - the load cursor state-.
	 * @author 5som3*/
	public static void setCursorState(short cursorState) {mouseController.setCursorState(cursorState);}
	
	/**Restore cursor to the mouseController default.
	 * @author 5som3*/
	public static void resetCursor() {
		mouseController.setCursorState(PLAIN);
		mouseController.resetCursor();
		WarpedWindow.getFrame().setCursor(mouseController.getCursor());
	}	
	
	/**Set the focus state.
	 * @param isFocused - the focus state for the mouse
	 * @apiNote You shouldn't need to use mouse focus.
	 * @apiNote It exist to allow continual dragging of GUIButtons and by extension all GUI.
	 * @author 5som3*/
	public static void setFocus(boolean isFocused) {WarpedMouse.isFocused = isFocused;}

	/**Get the item currently being dragged by the mouse.
	 * @return WarpedItem<?> - the item being dragged.
	 * @apiNote Will return the last dragged item if not currently dragging anything.
	 * @apiNote Will return null if no item has been dragged since the application began.
	 * @author 5som3*/
	public static WarpedItem<?> getDraggedItem(){return mouseController.getDraggedItem();}
	
	/**Drag the specified item from the specified inventory.
	 * @param dragGUI - the GUIInventory that the item is being dragged from.
	 * @param dragItem - the item to drag.
	 * @apiNote Will temporarily set the cursor to the dragged items raster.
	 * @implNote The dragGUI must be specified in case the drag operation fails.
	 * @implNote In these instances the item will be restored to dragGUI
	 * @implNote To drag and drop items in the world you must make an extension to the WarpedMouseController and override the update() method
	 * @author 5som3*/
	public static void dragItem(GUIInventory<?> dragGUI, WarpedItem<?> dragItem) {
		mouseController.dragItem(dragGUI, dragItem);
		setTemporaryCursor(dragItem.raster());
	}
	
	/**Drop the item being dragged (if any)
	 * @implNote will also reset the cursor to the mouseController default
	 * @author 5som3*/
	public static void dropItem() {
		mouseController.dropItem();
		resetCursor();
		isDragging = false;
	}
	
	/**DO NOT CALL!
	 * @implNote This method is called automatically by the WarpedUserInput
	 * @implNote Will update the mouseController 
	 * @author 5som3*/
	public void update() {
		mouseController.update();
		if(mouseController.isLoadState() && mouseController.setLoadCursor()) WarpedWindow.getFrame().setCursor(mouseController.getCursor());
		if(!inWindow && isTrapMouse && isOutOfBounds(lastPoint)) {
			WarpedFramework2D.getRobot().mouseMove(lastPoint.x, lastPoint.y);
		}
	}
	
	/**Pass mouse event to the WarpedWindow for processing*/
	@Override
	public final void mouseWheelMoved(MouseWheelEvent e) {
		if(isLocked) return;
		else WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.WHEEL_ROTATION));
	}
	
	/**Pass mouse event to the WarpedWindow for processing*/
	@Override
	public final void mouseDragged(MouseEvent e) {
		if(isLocked) return;
		else {			
			if(inWindow)isDragging = true;
			mouseController.setPoint(new Point((int)(e.getPoint().x / WarpedWindow.getWindowScale().x()), (int)(e.getPoint().y / WarpedWindow.getWindowScale().y())));
			WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.DRAG));
		}
	}
	
	/**Pass mouse event to the WarpedWindow for processing*/
	@Override
	public final void mouseMoved(MouseEvent e) {
		if(isLocked) return;
		else {			
			mouseController.setPoint(e.getPoint());
			WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.MOVE));
		}
		
	}
	
	/**Pass mouse event to the WarpedWindow for processing*/
	@Override
	public final void mousePressed(MouseEvent e) {
		if(isLocked) return;
		if(!isPressed) {
			isPressed = true;
			if(!mouseController.isDraggingItem()) mouseController.setCursorState(PRESS);
			if(e.getButton() == MouseEvent.BUTTON1) isLeftPressed = true;
			else isRightPressed = true;
		}
		Console.ln("WarpedMouse -> mousePressed()");
		WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.BUTTON_PRESS));

	}

	/**Pass mouse event to the WarpedWindow for processing*/
	@Override
	public final void mouseReleased(MouseEvent e) {
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
		if(e.getButton() == MouseEvent.BUTTON3) {
			mouseController.resetCursor();
		}
	
		if(WarpedState.isPaused()) FrameworkAudio.error.play();
		WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.BUTTON_RELEASE));
	}
	
	/**Pass mouse event to the WarpedWindow for processing*/
	@Override
	public final void mouseEntered(MouseEvent e) {
		inWindow = true;
	}
	
	/**Pass mouse event to the WarpedWindow for processing*/
	@Override
	public final void mouseExited(MouseEvent e) {
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
	
	/**Pass mouse event to the WarpedWindow for processing*/
	private final boolean isOutOfBounds(Point point) {
		if(point.x == 2 || point.y == WarpedWindow.getWindowWidth() - 2 || point.y == 2 || point.y == WarpedWindow.getWindowHeight() - 2) return true; else return false;
	}
	
	/** 6/6/23 -> Don't use -> is invalidated by mouse moving while clicking*/
	@Override
	public final void mouseClicked(MouseEvent e) {}
	
}
