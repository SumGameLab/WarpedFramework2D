/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.Color;
import java.awt.Point;

import warped.application.gui.GUILabel;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedAssembly;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.window.WarpedWindow;
import warped.user.mouse.WarpedMouse;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;

public class AssemblyToolTip extends WarpedAssembly {
	
	private static Point mousePoint;
	private static Point labelPoint = new Point();
	
	private static Color defLabelColor = new Color(60,60,60);
	private static Color defTextColor  = Color.YELLOW;
	private static Vec2i defSize       = new Vec2i(150, 30);       
	private static Vec2i defTextOffset = new Vec2i(14, 18);  
	private static int defTextSize     = 14;                   
	
	private GUILabel label   = new GUILabel();
	private Color labelColor = defLabelColor;
	private Color textColor  = defTextColor;
	private Vec2i size       =  defSize;
	private Vec2i textOffset = defTextOffset;
	private int textSize     = defTextSize;
	
	private int openingTick  = 0;
	private int openingDelay = 120;
	private boolean isTipOpeningQueued = false;
	
	private WarpedObject selectedObject;
	
	public AssemblyToolTip(WarpedManagerType type) {
		super(type);

	}

	public boolean isSelected(WarpedObject object) {
		if(selectedObject == null) return false;
		else if(selectedObject.getObjectID().isEqualTo(object)) return true;
		else return false;
	}
	
	public void select(WarpedObject selectedObject) {
		openingTick = 0;
		this.selectedObject = selectedObject;
		switch(selectedObject.getToolTipType()) {
		case DEFAULT:
		 	labelColor = defLabelColor;
		 	size       = defSize;
		 	textOffset = defTextOffset;
		 	textSize   = defTextSize;
		 	textColor  = defTextColor;
		 	break;
		default:
			break;
		}
		
		label.setText(selectedObject.getToolTipText());
		label.setLabelColor(labelColor);
		label.setBorderColor(Color.BLACK);
		label.setLabelSize(size.x, size.y);
		label.setTextOffset(textOffset.x, textOffset.y);
		label.setTextSize(textSize);
		label.setTextColor(textColor);
		label.updateGraphics();
	}
	
	public void queueTipOpening() {
		Console.ln("AssemblyToolTip -> queueTipOpening() -> queued opening tool tip");
		isTipOpeningQueued = true;
	}
	
	//public void resetTimeOut() {timeOutTick = 0;}
	
	@Override
	protected void offsetAssembly() {return;}
	@Override
	protected void defineAssembly() {return;}

	@Override
	protected void addAssembly() {
		addMember(label);
	}

	@Override
	public void updateAssembly() {
		if(WarpedMouse.isDragging()) close();
		
		mousePoint = WarpedMouse.getPoint();
		if(mousePoint.x > WarpedWindow.width / 2) labelPoint.x = mousePoint.x - size.x;
		else labelPoint.x = mousePoint.x;
		if(mousePoint.y > WarpedWindow.height/2) labelPoint.y = mousePoint.y - size.y;
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

