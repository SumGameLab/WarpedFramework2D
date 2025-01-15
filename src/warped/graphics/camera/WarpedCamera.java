/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.camera;

import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.state.WarpedState;
import warped.graphics.window.WarpedWindow;
import warped.utilities.math.geometry.bezier.BezierCurveCamera;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class WarpedCamera {
	
	protected WarpedObjectIdentity target;
	protected WarpedCameraType id;
	
	protected Vec2d position = new Vec2d();
	protected double zoom = 1.0;
	protected double rotation = 0.0;
	
	protected Vec2d c1 = new Vec2d(0, 0);
	protected Vec2d c2 = new Vec2d(WarpedWindow.width, WarpedWindow.height);
	
	public final double DEFAULT_CAMERA_MOVE_SPEED  = 1.0;
	public final double DEFAULT_CAMERA_ZOOM_D  	   = 100.0;
	
	protected double moveSpeed = DEFAULT_CAMERA_MOVE_SPEED;
	protected double zoomSpeed = zoom / DEFAULT_CAMERA_ZOOM_D;
	protected double rotationSpeed = UtilsMath.TWO_PI / 1024;
	
	public static final double DEFAULT_ZOOM_MAX = 3.0;
	public static final double DEFAULT_ZOOM_MIN = 0.0001;
	
	protected double zoomMax = DEFAULT_ZOOM_MAX;
	protected double zoomMin = DEFAULT_ZOOM_MIN;
	
	protected BezierCurveCamera path;
	protected boolean isTracking = false;
	private boolean isTrackingQued = false;
	
	public WarpedCamera(WarpedCameraType id) {setID(id);}
	
	//--------------Coordinate Transformations
	//To be used by view port when rendering
	public void positionTransformation(WarpedObject obj) {
		obj.renderPosition.add(position);
		obj.renderPosition.scale(zoom);
	}
	
	public void sizeTransformation(WarpedObject obj) {obj.renderSize.set(obj.getSize().x * zoom, obj.getSize().y * zoom);}
	
	public Vec2d getC1() {return c1;}
	public Vec2d getC2() {return c2;}
	public WarpedCameraType getID() {return id;}
	public void setID(WarpedCameraType id) {this.id = id;}
	public void setTarget(WarpedObjectIdentity target) {this.target = target;}
	
	public boolean isTarget(WarpedObjectIdentity target) {
		if(target == null && this.target == null) return true;
		else if(this.target == null || target == null) return false;
		
		if(this.target.isEqualTo(target)) return true; else return false;
	}
	
	public void setCornerPins(int c1x, int c1y, int c2x, int c2y) {
		c1.x = c1x;
		c1.y = c1y;
		c2.x = c2x;
		c2.y = c2y;
	}
	
	//--------------------------Tracking
	public boolean isTracking() {return isTracking;}
	public void trackTarget(WarpedObjectIdentity targetID) {
		this.target = targetID; 
		startTracking();
	}

	
	public void toggleTrackTarget(WarpedObjectIdentity target) {
		if(this.target == target) {
			toggleTracking(); 
			return;
		}
		this.target = target; 
		startTracking();
	}
	
	public void togglePanTrackTarget(WarpedObjectIdentity target) {
		if(isTracking) {
			
			if(this.target == target) {
				stopTracking();
				return;
			} else stopTracking();
			
		}
		this.target = target;
		isTrackingQued = true;
		path = new BezierCurveCamera(position, WarpedState.getGameObject(target).getRenderCenter(), 0.001);
	}
	
	public void togglePanZoomTrackTarget(WarpedObjectIdentity target) {
		if(isTracking) {
			
			if(this.target == target) {
				stopTracking();
				return;
			} else stopTracking();
			
		}
		this.target = target;
		isTrackingQued = true;
		path = new BezierCurveCamera(position, WarpedState.getGameObject(target).getRenderCenter(), 0.001, true);
	}
	
	public void focusOnTarget(WarpedObjectIdentity target) {
		this.target = target;
		if(isTracking) stopTracking();
		isTrackingQued = true;
		path = new BezierCurveCamera(position, WarpedState.getGameObject(target).getRenderCenter(), 0.0004, 0.3, 1.0, 3); 
		
	}
	/*
	public void toggleTrackSelection() {
		if(this.target == WarpedState.selectionManager.getSelectedIdentity()) {
			toggleTracking();
			return;
		}
		this.target = WarpedState.selectionManager.getSelectedIdentity();
		startTracking();
	}
	*/
	
	
	public void toggleTracking() {
		if(target == null) {
			Console.err("WarpedCamera -> toggleTracking -> target is null");
			isTracking = false;
			return;
		}
		if(isTracking) stopTracking();
		else startTracking();
	}
	
	public void startTracking() {
		if(target == null) {
			Console.err("WarpedCamera -> startTracking -> targetID is null ");
			return;
		}
		isTracking = true;
	}
	public void stopTracking() {isTracking = false;}

	
	public void updateTracking(WarpedObject obj) {		
		position.x = -obj.getPosition().x - (obj.renderSize.x / 2);
		position.y = -obj.getPosition().y - (obj.renderSize.x / 2);	
	}
	
	
	//-------------------- Camera Controls

	public void setRotation(double rotation) {this.rotation = rotation;}
	public Vec2d getPosition(){return position;}
	public double getZoom() {return zoom;}
	public double getMoveSpeed() {return moveSpeed;}
	public double getZoomSpeed() {return zoomSpeed;}
	public double getRotation() {return rotation;}
	public WarpedObjectIdentity getTarget() {return target;}
	
	public void setPosition(Vec2d position) {this.position = position;}
	public void setPosition(Vec2i position) {this.position.x = position.x; this.position.y = position.y;}
	public void setPosition(double x, double y) {position.set(x,y);}
	public void setZoom(double zoom) {this.zoom = zoom;}
	public void move(Vec2d vec) {position.x += vec.x; position.y += vec.y;}
	public void move(double x, double y) {position.x += x; position.y += y;}
	public void panUp()    {position.y += moveSpeed;}
	public void panDown()  {position.y -= moveSpeed;}
	public void panLeft()  {position.x += moveSpeed;}	
	public void panRight() {position.x -= moveSpeed;}
	public void zoomIn()   {
		zoom += zoomSpeed;
		if(zoom > zoomMax) zoom = zoomMax;
		setZoomSpeed();
		setMoveSpeed();
	}
	public void zoomOut()  {
		zoom -= zoomSpeed;
		if(zoom < zoomMin) zoom = zoomMin; 
		setZoomSpeed();
		setMoveSpeed();
	}
	/*
	public void rotateClockwise() {
		rotation += rotationSpeed;
		if(rotation > ROT_RANGE_MAX) rotation = ROT_RANGE_MIN; // reset range to min at 360 degree rotation
		//Screen.setScreenRotation(rotation);
	}
	public void rotateCounterClockwise() {
		rotation -= rotationSpeed;
		if(rotation < ROT_RANGE_MIN) rotation = ROT_RANGE_MAX; // reset range to max at 360 degree rotation
		//Screen.setScreenRotation(rotation);
	}
	*/
	
	/**This update method should be called by its parent view port
	 * when that viewport renders it should update the camera once*/
	public void update() {
		if(path != null) {
			
			if(path.isComplete(this)) {
				path = null;
				
				if(isTrackingQued) {
					isTrackingQued = false;
					startTracking();
				}
			}
			
		}
	}
	
	public boolean isClipped(WarpedObject obj) {
		if(obj.renderPosition.x + obj.renderSize.x < c1.x) return true; // outside left bound
		if(obj.renderPosition.y + obj.renderSize.y < c1.y) return true; // outside top bound
		if(obj.renderPosition.x > c2.x) return true; // outside right bound
		if(obj.renderPosition.y > c2.y) return true; // outside bottom bound
		return false;
	}
	
	protected void setZoomSpeed() {zoomSpeed = zoom / DEFAULT_CAMERA_ZOOM_D;}
	protected void setMoveSpeed() {moveSpeed = DEFAULT_CAMERA_MOVE_SPEED / zoom;}
}
