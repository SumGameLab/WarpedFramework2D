/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import java.awt.Point;

import warped.application.gui.GUILabel;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedAssembly;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.window.WarpedWindow;
import warped.user.mouse.WarpedMouse;
import warped.utilities.utils.Console;

public class ToolTip extends WarpedAssembly {
	
	private static Point mousePoint;
	private static Point labelPoint = new Point();
	
	private GUILabel label   = new GUILabel(200, 100);	
	
	private int openingTick  = 0;
	private int openingDelay = 120;
	private boolean isTipOpeningQueued = false;
	
	private WarpedObject selectedObject;
	
	
	public ToolTip(WarpedManagerType type) {
		super(type);
		label.setBackgroundVisible(true);

	}

	/**Check if the object is the current selected.
	 * @param object - An object to compare against this one.
	 * @return boolean - true if the objects are the same, else false.
	 * @author 5som3*/
	public boolean isSelected(WarpedObject object) {
		if(selectedObject == null) return false;
		else if(selectedObject.getObjectID().isEqualTo(object)) return true;
		else return false;
	}
	
	/**Select an object to display the tool tip for.
	 * @param selectedObject - the object to select.
	 * @author 5som3*/
	public void select(WarpedObject selectedObject) {
		openingTick = 0;
		this.selectedObject = selectedObject;
		label.setText(selectedObject.getToolTip());
	}
	
	/**Queues opening of the tool tip.
	 * @author 5som3*/
	public void queueTipOpening() {
		Console.ln("AssemblyToolTip -> queueTipOpening() -> queued opening tool tip");
		isTipOpeningQueued = true;
	}	
	
	/**Set the time between hovering an object and the tool tip appearing.
	 * @param delay - the delay in ticks (typically 58 ticks is approximately 1 second).
	 * @author 5som3 */
	public void setDelay(int delay) {
		if(delay < 30 || delay > 300) {
			Console.err("AssemblyToolTip -> setDelay() -> delay out of bounds : 30 < " + delay + " < 300");
			return;
		}
		this.openingDelay = delay;
	}
	
	/**Get the label that the tool tip assembly uses.
	 * @apiNote This can be used to change the appearance of the tool tip i.e. set background colour, font, text size, label size...
	 * @author 5som3*/
	public GUILabel getLabel() {return label;} 
	
	@Override
	protected void defineAssembly() {	
		label.setLabelSize(120, 30);
	}

	@Override
	protected void addAssembly() {
		addMember(label);
	}

	@Override
	public void updateAssembly() {
		if(WarpedMouse.isDragging()) close();
		
		mousePoint = WarpedMouse.getPoint();
		if(mousePoint.x > WarpedWindow.getWindowWidth() / 2) labelPoint.x = mousePoint.x - label.getWidth();
		else labelPoint.x = mousePoint.x;
		if(mousePoint.y > WarpedWindow.getWindowHeight()/2) labelPoint.y = mousePoint.y - label.getHeight();
		else labelPoint.y = mousePoint.y + 30;
		label.setPosition(labelPoint.x, labelPoint.y);
		
		
		if(isTipOpeningQueued) {
			openingTick++;
			if(openingTick > openingDelay) {
				isTipOpeningQueued = false;
				openingTick = 0;
				Console.ln("AssemblyToolTip -> opening tool tip");
				open();
			}
		}
		
	}
	

}

