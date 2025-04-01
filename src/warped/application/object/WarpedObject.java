/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import warped.application.state.WarpedState;
import warped.graphics.camera.WarpedCamera;
import warped.graphics.sprite.WarpedSprite;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public abstract class WarpedObject {
	
	/*GameObject
	 * This class is the progenitor of all objects in WarpedFramework2D
	 * Any object that you want to be compatible with WarpedFramework2D must be an extension of this class.
	 * @author SomeKid*/
	
	protected WarpedObjectIdentity objectID;
	private boolean isExpressEvents = false;
	
	protected boolean isVisible 	= true; //Visible 	   - objects that are invisible will not trigger mouse events and any mouse event in an invisible object will pass through it to hit an object behind
	private boolean   isInteractive = true; // Interactive - objects that are interactive will not have mouse events triggers, however they will still 'absorb' mouse events that hit them, just no effect
	private boolean   isHovered 	= false;
	protected boolean isAlive 		= true;
	 
	protected WarpedSprite sprite 	= new WarpedSprite(1, 1);
	private WarpedMouseEvent mouseEvent;
	//protected WarpedAction pushGraphicsEvent = () -> {return;};
	private String toolTip = null;

	//In application space, prior to camera transformation
	private VectorD position 	 	= new VectorD(); 

	//In Screen space, subsequent to camera transformation
	private VectorD renderPosition	= new VectorD();
	private VectorD renderSize 	 	= new VectorD();
	private VectorD renderCentre 	= new VectorD();
	
	//The sprite size will be scaled by this value when the sprite is drawn
	protected double renderScale 	= 1.0;
	
	//--------
	//------------------- Object Init ---------------------
	//--------
	private final boolean hasID() {if(objectID == null) return false; else return true;}
	
	public final void initObjectIdentity(WarpedObjectIdentity objectIdentity) {
		if(hasID()) {
			Console.err("WarpedGameObject -> initObjectIdentity() -> this object is already identified as (manager, group, index) : ( " + objectID.getManagerType() + ", " + objectID.getGroupIndex() + ", " + objectID.getGroupIndex() + " ) -> this ID will be overwritten, use override id if this is your intention");
			overrideObjectIdentity(objectIdentity);
			return;
		}
		this.objectID = objectIdentity;
	}
	
	public final void overrideObjectIdentity(WarpedObjectIdentity objectIdentity) {
		if(!hasID()) {
			Console.err("WarpedGameObject -> overrideObjectIdentity() -> this object has no identity to override, it will be initialized instead");
			initObjectIdentity(objectIdentity);
			return;
		}
		this.objectID = objectIdentity;
	}
	
	
	//--------
	//------------------- Access ---------------------
	//--------
	//TODO - implement a better collision system, checking one entitie at a time is bad*/
	public boolean collision(WarpedObject b) {return collision(this, b);}
	//TODO - implement a better collision system, checking one entitie at a time is bad*/
	public static boolean collision(WarpedObject a, WarpedObject b) {if(a.getCenterX() > b.getPosition().x() && a.getCenterY() > b.getPosition().y() && a.getCenterX() < b.getPosition().x() + b.getSize().x() && a.getCenterY() < b.getPosition().y() + b.getSize().y()) return true; else return false;}
	
	/**@return ID pointing to the manager, array and index of the object*/
	public final WarpedObjectIdentity getObjectID() {return objectID;}
	
	/**Check if another Object is the same as this object.
	 * @return boolean - true if the objects have the same object identity else false.
	 * @implNote will return false if the parameter is null.
	 * @author 5som3*/
	public final boolean isEqual(WarpedObject obj) {
		if(obj == null) return false;
		if(obj.getObjectID().getUniqueIdentity() == objectID.getUniqueIdentity()) return true; else return false;
	}
	
	/**Check if another object is the same as this object.
	 * @return boolean - true if the object identitys are matching.
	 * @author 5som3*/
	public final boolean isEqual(WarpedObjectIdentity objID) {if(this.objectID.isEqualTo(objID)) return true; else return false;}
	
	/**
	 * */
	public final boolean isAlive() {return isAlive;} 
	public final boolean isVisible() {return isVisible;}
	public final boolean isInteractive() {return isInteractive;}
	public final boolean isExpress() {return isExpressEvents;}
	
	
	/**Edit the Graphics2D for this objects sprite with more advance features (slower).
	 * @return Graphics2D - editable graphics2D object for the back buffer in the rasterBuffer.
	 * @apiNote - remember to call dispose() on the graphics object when fished editing.
	 * @apiNote - remember to call pushGraphics() after disposing to make the changes visible.	  
	 * @author SomeKid*/
	public final Graphics2D getGraphics() {return sprite.getGraphics();}
	
	/**This will make any changes made to getGraphics() / getGraphics2D() visible.
	 * @apiNote This should only be called after dispose() has been called on the graphics context returned from getGraphics() / getGraphics2D().
	 * @author SomeKid*/
	public final void pushGraphics() {sprite.pushGraphics();}

	/**Will remove the object from the game
	 * An object that is killed
	 * 		-will not receive update events
	 * 		-will not receive mouse events
	 * 		-will not be rendered
	 * 		-will be removed from the group at the end of the next cycle
	 * @author SomeKid*/
	public void kill() {isAlive = false;}
	
	/**Will receive mouse events even when the game state is paused.
	 * 		Typically Warped Objects will not receive mouse events when the GameState is paused
	 * 		There are times when an object needs to receive mouse events even when the game is paused, 
	 * 		i.e. a button that pauses the GameState needs to receive mouse events once it has paused the state.
	 * @deprecated - use setExpress(), expressMouseEvents() and standardMouseEvents() will be removed. 
	 * @author SomeKid*/
	public final void expressMouseEvents()  {isExpressEvents = true;}
	
	/**When the game state is paused will not receive mouse events.
	 * 		Typically Warped Objects will not receive mouse events when the GameState is paused
	 * 		There are times when an object needs to receive mouse events even when the game is paused, 
	 * 		i.e. a button that pauses the GameState needs to receive mouse events once it has paused the state.
	 * @deprecated - use setExpress(), expressMouseEvents() and standardMouseEvents() will be removed. 
	 * @author SomeKid*/
	public final void standardMouseEvents() {isExpressEvents = false;}
	
	/**Express objects will be able to interact with the mouse event when the WarpedState is paused.
	 * @param isExpressEvents - if true will interact with mouse when updates are paused else will not receive mouse events when updates are paused.
	 * @author 5som3*/
	public final void setExpress(boolean isExpressEvents) {this.isExpressEvents = isExpressEvents;}
	
	/**Make the object visible
	 * Invisible Objects
	 * 		- will receive update events
	 * 		- will not receive mouse events
	 * 		- will not be drawn
	 * @deprecated - use setVisible(), visible() and invisible() are to be removed.
	 * @author SomeKid*/
	public final void visible()   {isVisible = true;}
	
	/**Make the object invisible
	 * Invisible Objects
	 * 		- will receive update events
	 * 		- will not receive mouse events
	 * 		- will not be drawn
	 * @deprecated - use setVisible(), visible() and invisible() are to be removed.
	 * @author SomeKid*/
	public final void invisible() {isVisible = false;}
	
	/**Set the visibility of this object
	 * @param isVisible - if true the object will be visible else it will be invisible;
	 * @implNote It's only possible for the mouse to interact with objects that are rendered.
	 * @implNote Any invisible object will also by definition not be able to be interact with the mouse.
	 * @author 5som3*/
	public final void setVisible(boolean isVisible) {this.isVisible = isVisible;}
	
	/**Swap the state of invisibility - if visible will become invisible, if invisible will become visible
	 * Invisible Objects
	 * 		- will receive update events
	 * 		- will not receive mouse events
	 * 		- will not be drawn
	 * @author SomeKid*/
	public final void toggleVisibility() {if(isVisible) isVisible = false; else isVisible = true;}
	
	/**Makes the object Interactive
	 * Interactivity specifically only refers to mouse interactivity i.e. hoverable, clickable, draggable
	 * Objects that aren't Interactive
	 * 		- will receive update events
	 * 		- will not receive mouse events
	 * 		- will be drawn
	 * @deprecated - use setInteractive(). ateractive() and interactive() are to be removed. 
	 * @author SomeKid*/
	public final void interactive() {isInteractive = true;}
	
	/**Makes the object non-interactive (ateractive)
	 * Interactivity specifically only refers to mouse interactivity i.e. hoverable, clickable, draggable
	 * Objects that aren't Interactive
	 * 		- will receive update events
	 * 		- will not receive mouse events
	 * 		- will be drawn 
	 * @deprecated - use setInteractive(). ateractive() and interactive() are to be removed.
	 * @author SomeKid*/
	public final void ateractive() {isInteractive = false;}
	
	/**Set whether the object can interact with the mouse.
	 * @param isInteractive - If true the object will interact with the mouse else it will not recieve mouse events.
	 * @author 5som3*/
	public final void setInteractive(boolean isInteractive) {this.isInteractive = isInteractive;}
	
	/**Check if two WarpedObjects are the same
	 * @param obj - the object to compare with this one
	 * @author SomeKid */
	
	public final boolean isEqualTo(WarpedObject obj) {if(objectID.isEqualTo(obj.getObjectID())) return true; else return false;}
	
	/**Check if this object matches the object identity
	 * @param objectID - the ID to compare with this objects ID against
	 * @author SomeKid */
	public final boolean isEqualTo(WarpedObjectIdentity objectID) {if(this.objectID.isEqualTo(objectID)) return true; else return false;}
		
	/**The size of the sprite in pixels
	 * @return VectorI - vector.x = width
	 * 				 - vector.y = height
	 * @author SomeKid*/
	public final VectorI getSize() {return sprite.getSize();}
	
	/**The width of sprite in pixels
	 * @return int - width
	 * @author SomeKid*/
	public final int getWidth() {return sprite.getWidth();}
	
	/**The height of sprite in pixels
	 * @return int - height
	 * @author SomeKid*/
	public final int getHeight() {return sprite.getHeight();}
	
	/**The top left corner of the game object int application space 
	 * @return Vec2d - the top left corner of the raster measured in pixels from the origin in game space.
	 * @author SomeKid*/
	public final VectorD getPosition() {return position;}
	
	/**The x coordinate of this game object.
	 * @return double - the position of this game object. On screen this point appears as the top-left corner of this objects sprite
	 * @author 5som3*/
	public final double x() {return position.x();}
	
	/**The y coordinate of this game object.
	 * @return double - The position of this game object. On screen this point appears as the top-left corner of this objects sprite
	 * @author 5som3*/
	public final double y() {return position.y();}
	
	/**The center point of the object - x coordinate
	 * @return double - the center of the raster measured in pixels from the origin in game space
	 * @author SomeKid*/
	public final double getCenterX() {return position.x() + sprite.getWidth()  / 2;}
	
	/**The center point of the object - y coordinate
	 * @return double - the center of the raster measured in pixels from the origin in game space
	 * @author SomeKid*/
	public final double getCenterY() {return position.y() + sprite.getHeight() / 2;}
	
	/**Get the center point of the object in game space
	 * @return VectorD - a new vector object containing the current center x, y coordinates.
	 * @author 5som3*/
	public final VectorD getCenter() {return new VectorD(getCenterX(), getCenterY());}
	
	/**The transformed position to be used when rendering
	 * @return Vec2d - the top left corner of the raster measured in pixels in screen space.
	 * @apiNote Do not manually set render position with this vector, instead set the position of the object and position of the relevant camera.
	 * @author SomeKid*/
	public final VectorD getRenderPosition() {return renderPosition;}
	
	/**The transformed size of the object to be used when rendering the objects sprite.
	 * @return Vec2d - the render size measured in pixels in screen space.
	 * @apiNote Do not manually set render size with this vector, instead set the size of the object and zoom of the relevant camera.
	 * @author SomeKid*/
	public final VectorD getRenderSize() {return renderSize;}
	
	/**The transformed center of the object to be used for camera tracking.
	 * @return Vec2d - the center of the objects raster measured in pixels in screen space.
	 * @apiNote Do not manually set render center with this vector, instead set the size of the object and zoom of the relevant camera.
	 * @author SomeKid*/
	public final VectorD getRenderCentre() {return renderCentre;}
	
	/**Apply camera size/position transformation to the object.
	 * You should not manually setRenderTransformations ( this is an automated process )
	 * This method should only be called by WarpedViewports when rendering.
	 * @author SomeKid*/ 
	public final void setRenderTransformations(WarpedCamera camera) {
		renderPosition.set((position.x() + camera.getPosition().x()), (position.y() + camera.getPosition().y()));
		renderPosition.scale(camera.getZoom());
		renderSize.set(getWidth() * camera.getZoom() * renderScale, getHeight() * camera.getZoom() * renderScale);
		renderCentre.set(renderPosition.x() + renderSize.x() / 2, renderPosition.y() + renderSize.y() / 2);
	}
	
	/**Apply size/position transformation to the object.
	 * You should not manually setRenderTransformations ( this is an automated process )
	 * This method should only be called by WarpedViewports when rendering.
	 * @author SomeKid*/ 
	public final void setRenderTransformations() {
		renderPosition.set(position);
		renderSize.set(sprite.getSize());
	}
	
	/**The scale will be applied to the sprite size when rendering
	 * @return double - render scale for this object
	 * @author SomeKid*/
	public final double getRenderScale() {return renderScale;}
	
	/**Set the size of the sprite
	 * Note -  This will also clear the raster so any graphics will need to be redrawn
	 *@author SomeKid*/
	public void setSize(int width, int height) {sprite.setSize(width, height);}
	
	/**Set the position of this object.
	 * @param position - the new position for this object. 
	 * @author SomeKid*/
	public final void setPosition(VectorD position) {this.position.set(position);}
	
	/**Set the position of this object.
	 * @param position - the new position for this object. 
	 * @author SomeKid*/
	public final void setPosition(VectorI position) {this.position.set(position);}
	
	/**Set the position of this object.
	 * @param x - the x coordinate for this object.
	 * @param y - the y coordinate for this object. 
	 * @author SomeKid*/
	public final void setPosition(double x, double y) {this.position.set(x, y);}
	
	/**Set the position vector to the input vector
	 *  Note - this game objects position will refer to the input vector so any subsequent changes made to the vector will carry to this object
	 *  @author SomeKid*/
	public final void setPositionPointer(VectorD position) {this.position = position;}
	
	/**Move the object a distance from its current position.
	 * @param x - movement in the x axis. (- left / + right).
	 * @param y - movement in the y axis. (- up   / + down).
	 * @author SomeKid*/
	public final void move(VectorI vec) {this.position.add(vec);}
	
	/**Move the object a distance from its current position.
	 * @param x - movement in the x axis. (- left / + right).
	 * @param y - movement in the y axis. (- up   / + down).
	 * @author SomeKid*/
	public final void move(VectorD vec) {this.position.add(vec);}
	
	/**Move the object a distance from its current position.
	 * @param values - a list of the vector components to move. 
	 * @apiNote If the list of values is longer than the number of vector components the extra values will be skipped.
	 * @author SomeKid*/
	public final void move(double... values) {this.position.add(values);}
	
			
	/**Get the tooltip for this object.
	 * @return String - the tooltip text.
	 * @apiNote tooltips exist within the GUIManager and will appear when the mouse hovers over this object
	 * @author SomeKid*/
	public final String getToolTip() {return toolTip;}
	
	/**Set the tooltip text for this object.
	 * @param toolTip - the text to use for this objects tooltip.
	 * @apiNote tooltips exist within the GUIManager and will appear when the mouse hovers over this object
	 * @author SomeKid*/
	public final void setToolTip(String toolTip) {this.toolTip = toolTip;}
	
	/**Does this object have tooltip text.
	 * @result boolean - true if the object has tooltip text else false.
	 * @author SomeKid*/
	public final boolean hasToolTip() {if(toolTip == null) return false; else return true;}
	
	/**Clears any tool tip text
	 * @author SomeKid*/
	protected final void clearToolTip() {toolTip = null;}
	
	

	//--------
	//------------------- Update ---------------------
	//--------
	/**DO NOT CALL THIS METHOD
	  *Updating is controlled by the group that this member belongs to
	  *@author SomeKid*/
	public final void updateActively() {
		if(!isAlive) return;
		if(mouseEvent != null) {
			executeMouseEvent(mouseEvent);
			mouseEvent = null;
		}
		updateObject();	
	};

	
	/**The graphics object for this warped object
	 * @author SomeKid*/
	public final WarpedSprite getSprite() {return sprite;}
	/**The graphics object for this warped object
	 * @author SomeKid*/
	public final void setSprite(WarpedSprite sprite) {this.sprite = sprite;}
	/**Get the raster for this warped object
	 * @author SomeKid*/
	public final BufferedImage raster() {return sprite.raster();}
	
	//--------
	//------------------- Mouse ---------------------
	//--------
	/**DO NOT CALL! outside of ViewPortLayer
	 * @author SomeKid*/
	public final void mouseEvent(WarpedMouseEvent mouseEvent) {
		//Console.ln("WarpedObject -> mouseEvent() " + getClass().getSimpleName() + " -> " + name);
		if(WarpedMouse.isFocused()) return;
		if(!isAlive) return;
		if(!isVisible) return;
		if(!isInteractive) {mouseEvent.handle(); return;}
		if(mouseEvent.isHandled()) return;
		
		mouseEvent.updateTrace(renderPosition);
		if(isExpressEvents) {
			//Console.ln("WarpedObject -> mouseEvent() -> express event");
			executeMouseEvent(mouseEvent);
		}
		else {
			//Console.ln("WarpedObject -> mouseEvent() -> regular event");
			this.mouseEvent = mouseEvent;
		}
	}
	
	private final void executeMouseEvent(WarpedMouseEvent mouseEvent) {
		if(!isHovered) return;
		switch(mouseEvent.getMouseEventType()) {
		case BUTTON_PRESS: 
			mousePressed(mouseEvent); 	
			break;
		case BUTTON_RELEASE:
			Console.ln("WarpedObject -> mouseEvent() -> button released on :  " + this.getClass().getSimpleName());
			mouseReleased(mouseEvent);	
			break;
		case MOVE: mouseMoved(mouseEvent); 			
			break;
		case DRAG: 
			mouseDragged(mouseEvent);
			//wasDragging = true;
			break;
		case WHEEL_ROTATION: mouseRotation(mouseEvent);
			break;
		default: 	
			Console.err("WarpedGameObject -> MouseEvent() -> invalid switch case : " + mouseEvent.getMouseEventType()); 
			break;
		}
	}
	
	
	public final boolean isHovered() {return isHovered;}
	
	/**DO NOT CALL! outside of ViewPortLayer
	 * @author SomeKid*/
	public final void hovered() {
		if(WarpedMouse.isDragging() && WarpedMouse.isFocused()) return;
		if(!isHovered) {	
			mouseEntered();
			isHovered = true;

			if(hasToolTip()) {
				WarpedState.toolTip.select(this);
				WarpedState.toolTip.queueTipOpening();
			} else WarpedState.toolTip.close();
			
		}
		
	}	
	
	/**DO NOT CALL! outside of ViewPortLayer
	 * @author SomeKid*/
	public final void unhovered() {
		//if(isDraggable && WarpedMouse.isDragging()) return;
	
		if(isHovered) {		
			mouseExited();
			isHovered = false;
			if(hasToolTip() && WarpedState.toolTip.isSelected(this)) WarpedState.toolTip.close();	
		}		
	}
	 
	
	
	//--------
	//------------------- Interaction ---------------------
	//--------
	
	
	//Custom mouse functionality for all game objects -> everything is a button
	protected abstract void mouseEntered();
	protected abstract void mouseExited();
	protected abstract void mouseMoved(WarpedMouseEvent mouseEvent);
	protected abstract void mouseDragged(WarpedMouseEvent mouseEvent);
	protected abstract void mousePressed(WarpedMouseEvent mouseEvent);
	protected abstract void mouseReleased(WarpedMouseEvent mouseEvent);
	protected abstract void mouseRotation(WarpedMouseEvent mouseEvent);
	
	//--------
	//------------------- Abstract Update ---------------------
	//--------	

	/**DO NOT CALL! - This method is called automatically by the group that it belongs to.
	 * @apiNote Is called once per tick. 
	 * @apiNote Game logic should be event based not updated per tick but it is possible.
	 * @apiNote Avoid using this method unless absolutely necessary. 
	 * @implNote Objects will only be updated if the group they are in is 'Open'.
	 * @author SomeKid*/
	public abstract void updateObject();
	
	/**DO NOT CALL! - This method is called automatically by the group that it belongs to.
	 * @apiNote Is called once per second. 
	 * @apiNote Useful for simple recurring entitie logic i.e. pathing checks, spawn checks.
	 * @implNote Objects will only be updated if the group they are in is 'Open'.  
	 * @author SomeKid*/
	public abstract void updateMid();
	
	/**DO NOT CALL! - This method is called automatically by the group that it belongs to.
	 * @apiNote Is called once per minute.
	 * @apiNote Useful for moderately complex regular recurring updates i.e. some global/world update that requires each game object perform various logical checks.
	 * @implNote Objects will only be updated if the group they are in is 'Open'. 
	 * @author SomeKid*/
	public abstract void updateSlow(); 
	
	/**DO NOT CALL! - This method is called automatically by the group that it belongs to.
	 * @apiNote Is called once per hour.
	 * @apiNote For very complex regular recurring updates.
	 * @implNote Objects will only be updated if the group they are in is 'Open'. 
	 * @author SomeKid*/
	public abstract void updatePassive();
	
	//--------
	//------------------- Save ---------------------
	//-------- 
	
	
	
	
}
