/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.mouse;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import warped.WarpedFramework2D;
import warped.application.gui.GUIInventoryItems;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedState;
import warped.audio.FrameworkAudio;
import warped.graphics.window.WarpedWindow;
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
	
	public static final int ALPHA_THRESHHOLD = 10;
	
	public static final int PLAIN = 0;
	public static final int PRESS = 1;
	public static final int LOAD  = 2;
	
	
	private static WarpedMouseController mouseController = new DefaultMouseController();
	
	private static Point lastPoint = new Point(1,1);
	
	
	// 18/1/24 -> Implement different mouse graphics for press / release etc
	private static boolean inWindow   = true;
	private static boolean isDragging = false;
	private static boolean isDraggingItem = false;
	public static boolean isTrapMouse = true;
	
	private static boolean isFocused  = false;
	private static boolean isPressed  = false;
	private static boolean isLocked   = false;
	private static GUIInventoryItems itemParent;
	private static int dragItemIndex;
	
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
	
	public static GUIInventoryItems getDragItemInventory() {return itemParent;}
	public static int getDragItemIndex() { return dragItemIndex;}
	
	public static Point getPoint() {return mouseController.getMousePoint();}
	public static boolean isInWindow() {return inWindow;}
	public static boolean isPressed()  {return isPressed;}
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
	
	//--------
	//---------------- Dragging -------
	//--------	
	public static void focus() {isFocused = true;}
	public static void unfocus() {isFocused = false;}
	
	public static void dragItem(WarpedObject obj, GUIInventoryItems parent, int dragIndex) {
		itemParent = parent;
		dragItemIndex = dragIndex;
		mouseController.setTemporaryCursorImage(obj.raster());
		resetCursor();
		isDraggingItem = true;
	}
	
	public static void dropItem() {
		if(isDraggingItem) {			
			itemParent = null;
			dragItemIndex = -1;
			resetCursor();
			isDraggingItem = false;
		}
	}
	

	//--------
	//---------------- Update --------
	//--------
	public void update() {
		if(mouseController.isLoadState() && mouseController.setLoadCursor()) WarpedWindow.getFrame().setCursor(mouseController.getCursor());
		if(!inWindow && isTrapMouse && isOutOfBounds(lastPoint)) {
			WarpedFramework2D.getRobot().mouseMove(lastPoint.x, lastPoint.y);
		}
	}
	
	
	//--------
	//---------------- Overrides --------
	//--------
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isLocked) return;
		else WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.WHEEL_ROTATION));
	}
	
	public void mouseDragged(MouseEvent e) {
		if(isLocked) return;
		else {			
			if(inWindow)isDragging = true;
			mouseController.setPoint(new Point((int)(e.getPoint().x / WarpedWindow.getWindowScale().x), (int)(e.getPoint().y / WarpedWindow.getWindowScale().y)));
			WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.DRAG));
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		if(isLocked) return;
		else {			
			mouseController.setPoint(e.getPoint());
			WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.MOVE));
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if(isLocked) return;
		dropItem();		
		if(!isPressed) {
			isPressed = true;
			mouseController.setCursorState(PRESS);
		}
		Console.ln("WarpedMouse -> mousePressed()");
		WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.BUTTON_PRESS));

	}
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
			resetCursor();
		}
		if(isLocked) return;
		if(isPressed) {
			isPressed = false;
			mouseController.setCursorState(PLAIN);
		}
		Console.ln("WarpedMouse -> mouseRelase()");
		isDragging = false;
		if(WarpedState.isPaused()) FrameworkAudio.error.play();
		WarpedWindow.MouseEvent(new WarpedMouseEvent(e, MouseEventType.BUTTON_RELEASE));
			
	}
	public void mouseEntered(MouseEvent e) {
		inWindow = true;
	}
	public void mouseExited(MouseEvent e) {
		inWindow = false;
		isDragging = false;
		mouseController.setPoint(e.getPoint());

		if(isTrapMouse) {			
			int x, y;
			int ex = e.getX();
			int ey = e.getY();
			if(ex <= 0) x = 2;
			else if(ex >= WarpedWindow.width) x = WarpedWindow.width - 2;
			else x = ex;
			
			if(ey <= 0) y = 2;
			else if (ey >= WarpedWindow.height) y = WarpedWindow.height - 2;
			else y = ey;
			lastPoint.setLocation(x, y);
		}
	}
	
	private boolean isOutOfBounds(Point point) {
		if(point.x == 2 || point.y == WarpedWindow.width - 2 || point.y == 2 || point.y == WarpedWindow.height - 2) return true; else return false;
	}
	
	/** 6/6/23 -> Don't use -> is invalidated by mouse moving while clicking*/
	public synchronized void mouseClicked(MouseEvent e) {}	
	
}
