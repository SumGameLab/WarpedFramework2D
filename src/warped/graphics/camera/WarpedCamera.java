/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.camera;

import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.state.WarpedState;
import warped.graphics.window.WarpedWindow;
import warped.utilities.math.geometry.bezier.BezierCurveCamera;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class WarpedCamera {
	
	protected WarpedObjectIdentity target;
	protected WarpedCameraType id;
	
	protected VectorD position = new VectorD();
	protected double zoom = 1.0;
	protected double rotation = 0.0;
	
	protected VectorD cp1 = new VectorD(0, 0);
	protected VectorD cp2 = new VectorD(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight());
	
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
	/*
	public void positionTransformation(WarpedObject obj) {
		obj.getRenderPosition().set(obj.getPosition().x + position.x, obj.getPosition().y + obj.getPosition().y);
		obj.getRenderPosition().scale(zoom);
		//obj.renderPosition.add(position);
		//obj.renderPosition.scale(zoom);
	}
	
	public void sizeTransformation(WarpedObject obj) {obj.getRenderSize().set(obj.getWidth() * obj.getRenderScale(), obj.getHeight() * obj.getRenderScale());}
	*/
	
//	public VectorD getCornerPins() {return cornerPins;}
	
	public WarpedCameraType getID() {return id;}
	public void setID(WarpedCameraType id) {this.id = id;}
	public void setTarget(WarpedObjectIdentity target) {this.target = target;}
	
	public boolean isTarget(WarpedObjectIdentity target) {
		if(target == null && this.target == null) return true;
		else if(this.target == null || target == null) return false;
		
		if(this.target.isEqualTo(target)) return true; else return false;
	}
	
	public VectorD getC1() {return cp1;}
	public VectorD getC2() {return cp2;}
	
	
	public void setCornerPins(int c1x, int c1y, int c2x, int c2y) {
		cp1.set(c1x, c1y);
		cp2.set(c2x, c2y);
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
		path = new BezierCurveCamera(position, WarpedState.getGameObject(target).getRenderCentre(), 0.001);
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
		path = new BezierCurveCamera(position, WarpedState.getGameObject(target).getRenderCentre(), 0.001, true);
	}
	
	public void focusOnTarget(WarpedObjectIdentity target) {
		this.target = target;
		if(isTracking) stopTracking();
		isTrackingQued = true;
		path = new BezierCurveCamera(position, WarpedState.getGameObject(target).getRenderCentre(), 0.0004, 0.3, 1.0, 3); 
		
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
	public void updateTracking(WarpedObject obj) {position.set(obj.getRenderCentre());}
	
	
	//-------------------- Camera Controls

	public void setRotation(double rotation) {this.rotation = rotation;}
	public VectorD getPosition(){return position;}
	public double getZoom() {return zoom;}
	public double getMoveSpeed() {return moveSpeed;}
	public double getZoomSpeed() {return zoomSpeed;}
	public double getRotation() {return rotation;}
	public WarpedObjectIdentity getTarget() {return target;}
	
	public void setPosition(VectorD position) {this.position.set(position);}
	public void setPosition(VectorI position) {this.position.set(position);}
	public void setPosition(double x, double y) {position.set(x,y);}
	public void setZoom(double zoom) {this.zoom = zoom;}
	public void move(VectorD vec) {this.position.add(vec);}
	public void move(double x, double y) {this.position.add(x, y);}
	public void panUp()    {position.add(0.0, moveSpeed);}
	public void panDown()  {position.add(0.0, -moveSpeed);}
	public void panLeft()  {position.add(moveSpeed);}	
	public void panRight() {position.add(-moveSpeed);}
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
		if(obj.getRenderPosition().x() + obj.getRenderSize().x() < cp1.x()) return true; // outside left bound
		if(obj.getRenderPosition().y() + obj.getRenderSize().y() < cp1.y()) return true; // outside top bound
		if(obj.getRenderPosition().x() > cp2.x()) return true; // outside right bound
		if(obj.getRenderPosition().y() > cp2.y()) return true; // outside bottom bound
		return false;
	}
	
	protected void setZoomSpeed() {zoomSpeed = zoom / DEFAULT_CAMERA_ZOOM_D;}
	protected void setMoveSpeed() {moveSpeed = DEFAULT_CAMERA_MOVE_SPEED / zoom;}
}
