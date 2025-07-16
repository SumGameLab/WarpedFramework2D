/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;


import warped.application.state.WarpedObject;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;

public abstract class WarpedGUI extends WarpedObject {

	protected VectorD offset = new VectorD(0, 0);

	protected boolean isLocked 			= false;
	protected boolean isDraggable 		= false;
		
	/**Set the offset for this GUI.
	 * Use in conjunction with offset() to position elements within an assembly.
	 * @param positionOffset - the offset to apply when calling offset()
	 * @author 5som3*/
	public void setOffset(VectorD positionOffset) {setOffset(positionOffset.x(), positionOffset.y());}
	
	/**Set the offset for this GUI.
	 * Use in conjunction with offset() to position elements within an assembly.
	 * @param positionOffset - the offset to apply when calling offset()
	 * @author 5som3*/
	public void setOffset(VectorI positionOffset) {setOffset(positionOffset.x(), positionOffset.y());}
	
	/**Set the offset for this GUI.
	 * Use in conjunction with offset() to position elements within an assembly.
	 * @param x - the x offset in pixels.
	 * @param y - the y offset in pixels.
	 * @author 5som3*/
	public void setOffset(double x, double y) {offset.set(x, y);}
	
	/**Offset this objects position from the specified gui's position.
	 * @param gui - the GUI to use as the origin point for the offset.
	 * @apiNote The offset will be the amount specified by the setOffset() function. 
	 * @author 5som3*/
	public void offset(WarpedGUI gui) {setPosition(gui.x() + offset.x(), gui.y() + offset.y());}
	
	/**Offset this objects position from the specified vector position.
	 * @param origin - the origin point that the offset will be measured from.
	 * @apiNote The offset will be the amount specified by the setOffset() function. 
	 * @author 5som3*/
	public void offset(VectorI origin) {setPosition(origin.x() + offset.x(), origin.y() + offset.y());}
	
	/**Offset this objects position from the specified vector position.
	 * @param origin - the origin point that the offset will be measured from.
	 * @apiNote The offset will be the amount specified by the setOffset() function. 
	 * @author 5som3*/
	public void offset(VectorD origin) {setPosition(origin.x() + offset.x(), origin.y() + offset.y());}

	/**Get the x coordinate of this GUI's offset.
	 * @return double - the offset x coordinate in pixels 
	 * @author 5som3*/
	public double getOffsetX() {return offset.x();}
	
	/**Get the y coordinate of this GUI's offset.
	 * @return double - the offset x coordinate in pixels 
	 * @author 5som3*/
	public double getOffsetY() {return offset.y();}

	/**Set if the GUI can be dragged with the mouse.
	 * @param isDraggable - if true the GUI will be able to be dragged with the mouse.
	 * @author 5som3*/
	public void setDragable(boolean isDraggable) {this.isDraggable = isDraggable;}
	
	@Override
	public void updateObject() {return;}
	
	@Override
	public void updatePosition(double deltaTime) {return;}
	
	@Override
	public void updateMid() {return;};
	
	@Override
	public void updateSlow() {return;}; 

	/**This will generate new buffered images for the sprite of the specified size.
	 * @apiNote the updateGraphics() method will execute after the sprite has been resized. 
	 *@author SomeKid*/
	public void setSize(int width, int height) {
		sprite.setSize(width, height);
		updateGraphics();
	}
	
	protected abstract void updateGraphics();

	

	
}

