/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.prop;

import java.awt.image.BufferedImage;

import warped.application.object.WarpedObject;
import warped.utilities.math.geometry.bezier.BezierCurveLinearD;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class PropProjectile extends WarpedProp {

	public enum LifeType {
		TIME,
		DISTANCE,
		VELOCITY,
		PATH,
		PROXIMITY_TARGET,
		PROXIMITY_LOCATION,
		NONE,
	}
	
	private LifeType lifeType;
	
	private Vec2d velocity;
	private Vec2d acceleration;
	private Vec2d startPosition;
	private WarpedObject proximityTarget;
	private Vec2d proximityLocation;
	private BezierCurveLinearD path;
	
	private boolean isHit = false;
	private boolean isVisibilityDelayed = false;
	
	private int tick = 0;
	private int timeOut = 0; 
	private double distanceOut = 0.0;
	private double proximity = 0.0;
	private double visibilityDelay = 0.0;
	
	public PropProjectile(BufferedImage raster, BezierCurveLinearD path) {
		setRaster(raster);
		this.path = path;
		lifeType = LifeType.PATH;
	}
	
	public PropProjectile(BufferedImage raster, Vec2d position, Vec2d velocity) {
		setRaster(raster);
		this.velocity = velocity;
		this.position = position;
		startPosition = position.clone();
		acceleration = new Vec2d();
		lifeType = LifeType.NONE;
	}
	
	public PropProjectile(BufferedImage raster, Vec2d position, Vec2d velocity, int timeOut) {
		setRaster(raster);
		this.velocity = velocity;
		this.position = position;
		startPosition = position.clone();
		this.timeOut = timeOut;
		acceleration = new Vec2d();
		lifeType = LifeType.TIME;
	}
	
	public PropProjectile(BufferedImage raster, Vec2d position, Vec2d velocity, double distanceOut) {
		setRaster(raster);
		this.velocity = velocity;
		this.position = position;
		startPosition = position.clone();
		this.distanceOut = distanceOut;
		acceleration = new Vec2d();
		lifeType = LifeType.DISTANCE;
	}
	
	public PropProjectile(BufferedImage raster, Vec2d position, Vec2d velocity, double distanceOut, WarpedObject proximityTarget, double proximity) {
		setRaster(raster);
		this.velocity = velocity;
		this.position = position;
		startPosition = position.clone();
		this.distanceOut = distanceOut;
		acceleration = new Vec2d();
		this.proximity = proximity;
		this.proximityTarget = proximityTarget;
		lifeType = LifeType.PROXIMITY_TARGET;
	}
	
	public PropProjectile(BufferedImage raster, Vec2d position, Vec2d velocity, double distanceOut, WarpedObject proximityTarget, double proximity, double visibilityDelay) {
		setRaster(raster);
		this.velocity = velocity;
		this.position = position;
		startPosition = position.clone();
		this.distanceOut = distanceOut;
		acceleration = new Vec2d();
		this.proximity = proximity;
		this.proximityTarget = proximityTarget;
		this.visibilityDelay = visibilityDelay;
		this.isVisibilityDelayed = true;
		invisible();
		lifeType = LifeType.PROXIMITY_TARGET;
	}
	
	public PropProjectile(BufferedImage raster, Vec2d position, Vec2d velocity, double distanceOut, Vec2d proximityLocation, double proximity) {
		setRaster(raster);
		this.velocity = velocity;
		this.position = position;
		startPosition = position.clone();
		this.distanceOut = distanceOut;
		acceleration = new Vec2d();
		this.proximity = proximity;
		this.proximityLocation = proximityLocation;
		lifeType = LifeType.PROXIMITY_LOCATION;
	}
	
	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {
		switch(lifeType) {
		case DISTANCE: if(UtilsMath.vectorDifference(startPosition, position) > distanceOut) kill(); break;
		case NONE: 	break;
		case TIME:	if(tick++ > timeOut) kill();	break;
		case VELOCITY: 	break;
		case PROXIMITY_TARGET: 
			if(UtilsMath.vectorDifference(getCenter(), proximityTarget.getCenter()) < proximity) {
				isHit = true;
				kill(); 
			} else {
				double distanceTraveled = UtilsMath.vectorDifference(startPosition, position); 
				if(isVisibilityDelayed && !isVisible && distanceTraveled > visibilityDelay) visible();
				if(distanceTraveled > distanceOut) kill(); 
			}
			break;
		case PROXIMITY_LOCATION:
			//TODO implement proxy location
			break;
		case PATH:
			if(path.isComplete(position)) {
				isHit = true;
				kill();
			}
			break;
		default:
			Console.err("PropProjectile -> updateObject -> invalid life type : " + lifeType);
			break;
		}
	}
	
	public boolean isHit() {return isHit;}

	@Override
	protected void updatePosition() {
		if(lifeType == LifeType.PATH) return;
		velocity.add(acceleration);
		position.add(velocity);
	}

	@Override
	protected void updateMid() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateSlow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updatePassive() {
		// TODO Auto-generated method stub
		
	}

}
