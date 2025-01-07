/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.WarpedProperties;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.GameObjectAction;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.math.vectors.Vec3d;
import warped.utilities.utils.Console;

public abstract class WarpedObject {
	
	/**GameObject
	 * This class is the progenitor of all objects in WarpedFramework2D
	 * Any object that you want to be compatible Warped must be an extension of this class
	 * 	 * 
	 * */
	protected WarpedObjectIdentity objectID;
	protected boolean hasID = false;
	private boolean isExpressEvents = false;
	
	protected GameObjectAction updateActiveAction  	= (g) -> {return;};
	protected GameObjectAction updateMidAction 	 	= (g) -> {return;};
	protected GameObjectAction updateSlowAction 	= (g) -> {return;};
	protected GameObjectAction updatePassiveAction 	= (g) -> {return;};
	
	protected String name = "default Name";
	protected BufferedImage raster;
	protected boolean isVisible 		= true; //Visible 	   - objects that are invisible will not trigger mouse events and any mouse event in an invisible object will pass through it to hit an object behind
	private boolean isInteractive 	= true; // Interactive - objects that are interactive will not have mouse events triggers, however they will still 'absorb' mouse events that hit them, just no effect
	private boolean isHovered 		= false;
	protected boolean isAlive 		= true;
	protected boolean isDraggable 	= false;
	
	//These are the real values before any coordinate transformation / camera transformation is applied 
	protected Vec2d position 	 = new Vec2d();
	protected Vec2d size 		 = new Vec2d();		
	protected Vec2d centerOffset = new Vec2d(); 

	protected String toolTipText 	= "Game Object";
	private ToolTipType toolTipType = ToolTipType.DEFAULT;

	private WarpedMouseEvent mouseEvent;
	
	private boolean isToolTipOpen   = false;
	private boolean hasToolTip 		= false;
	private int toolTipTick    		= 0;
	private static int toolTipDelay = 120;
	
	public Vec2d renderPosition = new Vec2d();
	public Vec2d renderSize  	= new Vec2d();
	
	private ArrayList<WarpedOption> selectOptions = new ArrayList<>();	

	
	//--------
	//------------------- Object Init ---------------------
	//--------
	public final void initObjectIdentity(WarpedObjectIdentity objectIdentity) {
		if(hasID) {
			Console.err("WarpedGameObject -> initObjectIdentity() -> this object is already identified as (manager, group, index) : ( " + objectID.getManagerType() + ", " + objectID.getGroupIndex() + ", " + objectID.getGroupIndex() + " ) -> this ID will be overwritten, use override id if this is your intention");
			overrideObjectIdentity(objectIdentity);
			return;
		}
		this.objectID = objectIdentity;
		hasID = true;
	}
	
	public final void overrideObjectIdentity(WarpedObjectIdentity objectIdentity) {
		if(!hasID) {
			Console.err("WarpedGameObject -> overrideObjectIdentity() -> this object has no identity to override, it will be initialized instead");
			initObjectIdentity(objectIdentity);
			return;
		}
		this.objectID = objectIdentity;
	}
	
	//--------
	//------------------- Access ---------------------
	//--------
	public boolean collision(WarpedObject b) {return collision(this, b);}
	public static boolean collision(WarpedObject a, WarpedObject b) {
		Vec2d aPos = a.getCenter();
		Vec2d bPos = b.getPosition();
		if(aPos.x > bPos.x && aPos.y > bPos.y && aPos.x < bPos.x + b.getSize().x && aPos.y < bPos.y + b.getSize().y) return true; else return false;
	}
	
	/**@return an ID pointing to the manager, array and index of the object*/
	public final WarpedObjectIdentity getObjectID() {return objectID;}
	public final boolean isEqual(WarpedObject obj) {
		if(obj == null) return false;
		if(obj.getObjectID().getUniqueIdentity() == objectID.getUniqueIdentity()) return true; else return false;
	}
	
	/**@return boolean true if alive*/
	public final boolean isAlive() {return isAlive;} 
	
	/**Will remove the object from the game*/
	public void kill() {isAlive = false;}
	
	/** OBJECT VISIBLITY
	 * if invisible the object will not have its raster rendered
	 */
	public final void expressMouseEvents()  {isExpressEvents = true;}
	public final void standardMouseEvents() {isExpressEvents = false;}
	public final boolean isVisible() {return isVisible;}
	public final void visible()   {isVisible = true;}
	public final void invisible() {isVisible = false;}
	public final void toggleVisibility() {if(isVisible) isVisible = false; else isVisible = true;}
	
	
	/** OBJECT INTERACTIVITY
	 * specifically only refers to mouse interactivity
	 * i.e. hoverable, clickable, draggable
	 * if ateractive, object will not be passed mouse events, however it will nullify mouse events 
	 * */
	public final boolean isInteractive() {return isInteractive;}
	public final void interactive() {isInteractive = true;}
	public final void ateractive() {isInteractive = false;}
	
	public final boolean isEqualTo(WarpedObject obj) {if(objectID.isEqualTo(obj.getObjectID())) return true; else return false;}
	public final boolean isEqualTo(WarpedObjectIdentity objectID) {if(this.objectID.isEqualTo(objectID)) return true; else return false;}
	
	public String getToolTipText() {return toolTipText;}
	public final ToolTipType getToolTipType() {return toolTipType;}
	
	/**the original (untransformed) size / position / offset of the raster in pixels*/
	public final Vec2d getSize() {return size;}
	public final Vec2d getPosition() {return position;}
	public final Vec2d getCenter() {return Vec2d.addVectors(position, centerOffset);}
	public final Vec2d getCenterOffset() {return centerOffset;}
	public String getName() {return name;}
	
	/**the transformed position and size to be used when rendering*/
	public final Vec2d getRenderPosition() {return renderPosition;}
	public final Vec3d getRenderCenter() {return new Vec3d(position.x + renderSize.x / 2, position.y + renderSize.y / 2, 0);}
	public final Vec2d getRenderSize() {return renderSize;}
	

	public final synchronized BufferedImage raster()  {return raster;}	
	public final void setGameObjectSize(Vec2d size) {this.size = size;};	
	public final void setGameObjectSize(int x, int y) {size.x = x; size.y = y;}
	public final void setPosition(Vec2i position) {this.position.x = position.x; this.position.y = position.y;}
	public final void setPosition(Vec2d position) {this.position.x = position.x; this.position.y = position.y;}
	public final void setPositionPointer(Vec2d position) {this.position = position;}
	public final void setPosition(int x, int y) {position.x = (double)x; position.y = (double)y;}
	public final void setPosition(double x, double y) {position.x = x; position.y = y;}
	
	public final void setUpdateActiveAction(GameObjectAction updateAction) {this.updateActiveAction = updateAction;}
	public final void setUpdateMidAction(GameObjectAction midAction) {this.updateMidAction = midAction;}
	public final void setUpdateSlowAction(GameObjectAction slowAction) {this.updateSlowAction = slowAction;}
	public final void setUpdatePassiveAction(GameObjectAction passiveAction) {this.updatePassiveAction = passiveAction;}
	
	private final void setRenderPriority() {/*TODO  18/1/24 ->  implement set render priority function*/}
	
	
	public final WarpedOption getSelectOption(int index) {return selectOptions.get(index);}
	public final ArrayList<WarpedOption> getSelectOptions() {return selectOptions;}

	protected final void addSelectOption(WarpedOption option) {selectOptions.add(option);}
	protected final void clearSelectOptions() {selectOptions.clear();}
	public final void setToolTip(String toolTipText) {
		hasToolTip = true;
		this.toolTipText = toolTipText;
		toolTipType = ToolTipType.DEFAULT;
	}
	protected final void setToolTip(String toolTipText, ToolTipType toolTipType) {
		hasToolTip = true;
		this.toolTipText = toolTipText;
		this.toolTipType = toolTipType;
	}
	protected final void clearToolTip() {hasToolTip = false;}
	
	
	//--------
	//------------------- Update ---------------------
	//--------
	/**This method shouldn't need to be called by the user or for Game Development
	  *This update is specifically to be called by by the ContextGroup that the object is contained within*/
	public final void updateActively() {
		if(!isAlive) return;
		if(mouseEvent != null) {
			executeMouseEvent(mouseEvent);
			mouseEvent = null;
		}
		updateObject();
		updatePosition();
		setRenderPriority(); // probably want to do this after changing position instead of every update cycle
		updateRaster();	
		updateActiveAction.action(this);
	};
	public void updateMidly() {
		updateMid();
		updateMidAction.action(this); 	
	}
	public final void updateSlowly() {
		updateSlow();
		updateSlowAction.action(this);
	}
	public final void updatePassively() {
		updatePassive();
		updatePassiveAction.action(this);
	}
	
	//--------
	//------------------- Graphics ---------------------
	//--------
	public final void setRaster(BufferedImage raster) {
		this.raster = raster;
		size.x = raster.getWidth(); 
		size.y = raster.getHeight();
		centerOffset.x = size.x / 2;
		centerOffset.y = size.y / 2;
	}
	
	public final void paintRaster(BufferedImage raster) {
		setRaster(new BufferedImage((int)raster.getWidth(), (int)raster.getHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE));
		this.raster.getGraphics().drawImage(raster, 0, 0, (int)size.x, (int)size.y, null);
	}
	
	public final void paintRaster(Color color) {
		Console.ln("WarpedObject -> paintRaster() -> painted raster : " + color);
		Graphics g = raster.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, raster.getWidth(), raster.getHeight());
		g.dispose();
	}
	
	//--------
	//------------------- Mouse ---------------------
	//--------
	//private boolean wasDragging = false;
	/**DO NOT CALL! outside of ViewPortLayer*/
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
		switch(mouseEvent.getMouseEventType()) {
		case BUTTON_PRESS: 
			mousePressed(mouseEvent); 	
			break;
		case BUTTON_RELEASE:
			Console.ln("WarpedObject -> mouseEvent() -> button released on : " + this.name +", " + this.getClass().getSimpleName());
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
	
	/**DO NOT CALL! outside of ViewPortLayer*/
	public final boolean isHovered() {if(isHovered)return true; else return false;}
	
	/**DO NOT CALL! outside of ViewPortLayer*/
	
	public final void hovered() {
		if(WarpedMouse.isDragging() && WarpedMouse.isFocused()) return;
		if(!isHovered) {	
			mouseEntered();
			isHovered = true;

			//Console.ln("WarpedGameObject -> hovered() -> " + this.getClass().getSimpleName());
			if(hasToolTip) {
				WarpedState.toolTip.select(this);
				WarpedState.toolTip.queueTipOpening();
			} else WarpedState.toolTip.close();
			
		}
		
	}	
	
	public final void unhovered() {
		if(isDraggable && WarpedMouse.isDragging()) return;
		if(isHovered) {		
			mouseExited();
			isHovered = false;
			if(hasToolTip && WarpedState.toolTip.isSelected(this)) WarpedState.toolTip.close();	
		}		
	}
	 
	
	
	//--------
	//------------------- Interaction ---------------------
	//--------
	public final void draggable()   {isDraggable = true;}
	public final void undraggable() {isDraggable = false;}
	
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
	/**Actively Updated*/
	protected abstract void updateRaster();
	/**Actively Updated*/
	protected abstract void updateObject();
	/**Actively Updated*/
	protected abstract void updatePosition();
	/**Updated Once per second*/
	protected abstract void updateMid();
	/**Updated Once per second*/
	protected abstract void updateSlow(); 
	/**Updated Once per hour*/
	protected abstract void updatePassive();
	
	//--------
	//------------------- Save ---------------------
	//-------- 
	
	
	
	
}
