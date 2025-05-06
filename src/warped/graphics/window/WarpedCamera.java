/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import warped.application.state.WarpedObject;
import warped.utilities.math.geometry.bezier.BezierCurveCamera;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class WarpedCamera {
	
	public static final double DEFAULT_CAMERA_MOVE_SPEED  = 1.0;
	public static final double DEFAULT_CAMERA_ZOOM_D  	  = 100.0;
	
	public static final double DEFAULT_ZOOM_MAX = 3.0;
	public static final double DEFAULT_ZOOM_MIN = 0.0001;

	protected WarpedObject target;
	
	protected VectorD position = new VectorD();
	protected double zoom = 1.0;

	//protected double rotation = 0.0;
	//protected VectorD cp1 = new VectorD(0, 0);
	//protected VectorD cp2 = new VectorD(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight());
	
	protected double moveSpeed = DEFAULT_CAMERA_MOVE_SPEED;
	protected double zoomSpeed = zoom / DEFAULT_CAMERA_ZOOM_D;
	protected double rotationSpeed = UtilsMath.TWO_PI / 1024;
	
	protected double zoomMax = DEFAULT_ZOOM_MAX;
	protected double zoomMin = DEFAULT_ZOOM_MIN;
	
	protected BezierCurveCamera path;
	protected boolean isTracking = false;
	private boolean isTrackingQued = false;
	
	
	protected WarpedCamera() {
		
	}
	
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
	
	/**Set the target of the camera.
	 * @param target - the object to lock the camera to
	 * @implNote The camera can actively track an object that is targeted.
	 * @implNote Will not track target until startTracking is called.
	 * @author 5som3*/
	public void setTarget(WarpedObject target) {this.target = target;}
	
	/**Is the specified object the target of this camera
	 * @param target - the object to check.
	 * @return boolean - true if the object is the target of this camera
	 * @implNote The camera can actively track an object that is targeted.
	 * @implNote Will not track target until startTracking is called.
	 * @author 5som3*/
	public boolean isTarget(WarpedObject target) {
		if(target == null && this.target == null) return true;
		else if(this.target == null || target == null) return false;
		
		if(this.target.isEqualTo(target)) return true; else return false;
	}

	/**Is the camera tracking an object
	 * @return boolean - true if the camera is tracking an object
	 * @author 5som3*/
	public boolean isTracking() {return isTracking;}
	
	/**Start tracking the specified target 
	 * @param target - the object to track
	 * @implNote The camera can actively track an object that is targeted.
	 * @implNote Will not track target until startTracking is called.
	 * @author 5som3*/	
	public void trackTarget(WarpedObject target) {
		this.target = target; 
		startTracking();
	}
	
	
	/*
	public VectorD getC1() {return cp1;}
	
	/**
	public VectorD getC2() {return cp2;}
	
	
	public void setCornerPins(int c1x, int c1y, int c2x, int c2y) {
		cp1.set(c1x, c1y);
		cp2.set(c2x, c2y);
	}
	*/
	
	
	
	/**Start or stop tracking the object depending on if tracking it already.
	 * @param target - the object to toggle track.
	 * @implNote If tracking another object or no object the camera will track the new target.
	 * @implNote If already tracking the input object the camera will stop tracking.
	 * @author 5som3*/	
	public void toggleTrackTarget(WarpedObject target) {
		if(this.target.isEqual(target)) {
			toggleTracking(); 
			return;
		}
		this.target = target; 
		startTracking();
	}
	
	/**Pan to toggleTrack depending on if tracking it already.
	 * @param target - the object to toggle track.
	 * @implNote If tracking another object or no object the camera will pan to the object and then being tracking the new target.
	 * @implNote If already tracking the input object the camera will stop tracking.
	 * @author 5som3*/	
	public void togglePanTrackTarget(WarpedObject target) {
		if(isTracking) {
			
			if(this.target == target) {
				stopTracking();
				return;
			} else stopTracking();
			
		}
		this.target = target;
		isTrackingQued = true;
		path = new BezierCurveCamera(position, target.getRenderCentre(), 0.001);
	}
	
	/**Pan and zoom into toggleTrack depending on if tracking it already.
	 * @param target - the object to toggle track.
	 * @implNote If tracking another object or no object the camera will pan and zoom to the object and then being tracking the new target.
	 * @implNote If already tracking the input object the camera will stop tracking.
	 * @author 5som3*/	
	public void togglePanZoomTrackTarget(WarpedObject target) {
		if(isTracking) {
			if(this.target.isEqual(target)) {
				stopTracking();
				return;
			} else stopTracking();
			
		}
		this.target = target;
		isTrackingQued = true;
		path = new BezierCurveCamera(position, target.getRenderCentre(), 0.001, true);
	}
	

	/*
	public void focusOnTarget(WarpedObject target) {
		this.target = target;
		if(isTracking) stopTracking();
		isTrackingQued = true;
		path = new BezierCurveCamera(position, target.getRenderCentre(), 0.0004, 0.3, 1.0, 3); 
		
	}
	public void toggleTrackSelection() {
		if(this.target == WarpedState.selectionManager.getSelectedIdentity()) {
			toggleTracking();
			return;
		}
		this.target = WarpedState.selectionManager.getSelectedIdentity();
		startTracking();
	}
	*/
	
	/**Start or stop tracking the target depending on if tracking already
	 * @implNote Will start tracking if not tracking else will stop tracking.
	 * @author 5som3*/
	public void toggleTracking() {
		if(target == null) {
			Console.err("WarpedCamera -> toggleTracking -> target is null");
			isTracking = false;
			return;
		}
		if(isTracking) stopTracking();
		else startTracking();
	}
	
	/**Start tracking the target (if any).
	 * @implNote Will start tracking if has target and is not tracking else will stop tracking.
	 * @author 5som3*/
	public void startTracking() {
		if(target == null) {
			Console.err("WarpedCamera -> startTracking -> targetID is null ");
			return;
		}
		isTracking = true;
	}
	
	/**Stop tracking target (if any)
	 * @author 5som3*/
	public void stopTracking() {isTracking = false;}
	
	
	//public void setRotation(double rotation) {this.rotation = rotation;}
	//public double getRotation() {return rotation;}
	
	/**Get the position of this camera.
	 * @return VectorD - the position vector for this camera
	 * @author 5som3*/
	public VectorD getPosition(){return position;}
	
	/**Get the zoom of this camera.
	 * @return double - the zoom multiplier.
	 * @implNote Example : 1.0 = object size unchanged in view. 2.0 = double size of objects in view. 0.5 = half size of objects in view.
	 * @author 5som3 */
	public double getZoom() {return zoom;}
	
	/**Get the move speed of the camera.
	 * @return double - the move speed.
	 * @implNote This is the delta that will be applied when panning
	 * @author 5som3*/
	public double getMoveSpeed() {return moveSpeed;}
	
	/**Get the zoom speed of the camera
	 * @return double - the zoom speed
	 * @implNote This is the delta applied when calling zoomIn() or zoomOut() 
	 * @author 5som3*/
	public double getZoomSpeed() {return zoomSpeed;}
	
	/**Get the target of this camera (if any)
	 * @return WarpedObject - the target of this camera.
	 * @author 5som3*/
	public WarpedObject getTarget() {return target;}
	
	/**Set the position of the camera
	 * @param position - the vector position of the camera
	 * @author 5som3*/	
	public void setPosition(VectorD position) {this.position.set(position);}
	
	/**Set the position of the camera
	 * @param position - the vector position of the camera
	 * @author 5som3*/	
	public void setPosition(VectorI position) {this.position.set(position);}
	
	/**Set the position of the camera
	 * @param x - the x component of the cameras vector position 
	 * @param y - the y component of the cameras vector position 
	 * @author 5som3*/	
	public void setPosition(double x, double y) {position.set(x,y);}
	
	/**Set the zoom of the camera 
	 *@param zoom - the zoom for the camera. Domain : zoomMin <= zoom <= zoomMax 
	 *@author 5som3*/
	public void setZoom(double zoom) {
		if(zoom < zoomMin || zoom > zoomMax) {
			Console.err("WarpedCamera -> setZoom() -> zoom out of bounds : " + zoom + ", will be corrected");
			if(zoom < zoomMin) zoom = zoomMin;
			else zoom = zoomMax;
		} else this.zoom = zoom;
	}
	
	/**Move the camera based on its current position
	 * @param vec - the distance to move from the current position 
	 * @author 5som3*/
	public void move(VectorD vec) {this.position.add(vec);}
	
	/**Move the camera based on its current position
	 * @param vec - the distance to move from the current position 
	 * @author 5som3*/
	public void move(double x, double y) {this.position.add(x, y);}
	
	/**Move the camera up at the set move speed.
	 * @author 5som3*/
	public void panUp()    {position.add(0.0, moveSpeed);}
	
	/**Move the camera down at the set move speed.
	 * @author 5som3*/
	public void panDown()  {position.add(0.0, -moveSpeed);}
	
	/**Move the camera left at the set move speed.
	 * @author 5som3*/
	public void panLeft()  {position.add(moveSpeed);}
	
	/**Move the camera right at the set move speed.
	 * @author 5som3*/
	public void panRight() {position.add(-moveSpeed);}
	
	/**Zoom the camera in at the set zoom speed.
	 * @author 5som3*/
	public void zoomIn()   {
		zoom += zoomSpeed;
		if(zoom > zoomMax) zoom = zoomMax;
		
		zoomSpeed = zoom / DEFAULT_CAMERA_ZOOM_D;
		moveSpeed = DEFAULT_CAMERA_MOVE_SPEED / zoom;
	}
	
	/**Zoom the camera out at the set zoom speed.
	 * @author 5som3*/
	public void zoomOut()  {
		zoom -= zoomSpeed;
		if(zoom < zoomMin) zoom = zoomMin;
		
		zoomSpeed = zoom / DEFAULT_CAMERA_ZOOM_D;
		moveSpeed = DEFAULT_CAMERA_MOVE_SPEED / zoom;
	}
	
	
	
	/*		
	public boolean isClipped(WarpedObject obj) {
		if(obj.getRenderPosition().x() + obj.getRenderSize().x() < cp1.x()) return true; // outside left bound
		if(obj.getRenderPosition().y() + obj.getRenderSize().y() < cp1.y()) return true; // outside top bound
		if(obj.getRenderPosition().x() > cp2.x()) return true; // outside right bound
		if(obj.getRenderPosition().y() > cp2.y()) return true; // outside bottom bound
		return false;
	}
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
	protected void update() {
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
	
	/**Update camera position if target position changes*/
	protected void updateTracking(WarpedObject obj) {position.set(obj.getRenderCentre());}
	
	

}
