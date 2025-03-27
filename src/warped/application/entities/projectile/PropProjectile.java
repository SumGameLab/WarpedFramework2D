/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.projectile;

import java.awt.image.BufferedImage;

import warped.application.entities.WarpedEntitie;
import warped.application.object.WarpedObject;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.geometry.bezier.BezierCurveLinearD;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class PropProjectile extends WarpedEntitie {

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
	
	private VectorD velocity;
	private VectorD acceleration;
	private VectorD startPosition;
	private WarpedObject proximityTarget;
	private VectorD proximityLocation;
	private BezierCurveLinearD path;
	
	private boolean isHit = false;
	private boolean isVisibilityDelayed = false;
	
	private int tick = 0;
	private int timeOut = 0; 
	private double distanceOut = 0.0;
	private double proximity = 0.0;
	private double visibilityDelay = 0.0;
	
	public PropProjectile(BufferedImage raster, BezierCurveLinearD path) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.path = path;
		lifeType = LifeType.PATH;
	}
	
	public PropProjectile(BufferedImage raster, VectorD position, VectorD velocity) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		acceleration = new VectorD();
		lifeType = LifeType.NONE;
	}
	
	public PropProjectile(BufferedImage raster, VectorD position, VectorD velocity, int timeOut) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		this.timeOut = timeOut;
		acceleration = new VectorD();
		lifeType = LifeType.TIME;
	}
	
	public PropProjectile(BufferedImage raster, VectorD position, VectorD velocity, double distanceOut) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		this.distanceOut = distanceOut;
		acceleration = new VectorD();
		lifeType = LifeType.DISTANCE;
	}
	
	public PropProjectile(BufferedImage raster, VectorD position, VectorD velocity, double distanceOut, WarpedObject proximityTarget, double proximity) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		this.distanceOut = distanceOut;
		acceleration = new VectorD();
		this.proximity = proximity;
		this.proximityTarget = proximityTarget;
		lifeType = LifeType.PROXIMITY_TARGET;
	}
	
	public PropProjectile(BufferedImage raster, VectorD position, VectorD velocity, double distanceOut, WarpedObject proximityTarget, double proximity, double visibilityDelay) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		this.distanceOut = distanceOut;
		acceleration = new VectorD();
		this.proximity = proximity;
		this.proximityTarget = proximityTarget;
		this.visibilityDelay = visibilityDelay;
		this.isVisibilityDelayed = true;
		invisible();
		lifeType = LifeType.PROXIMITY_TARGET;
	}
	
	public PropProjectile(BufferedImage raster, VectorD position, VectorD velocity, double distanceOut, VectorD proximityLocation, double proximity) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		this.distanceOut = distanceOut;
		acceleration = new VectorD();
		this.proximity = proximity;
		this.proximityLocation = proximityLocation;
		lifeType = LifeType.PROXIMITY_LOCATION;
	}
	

	@Override
	public void updateObject() {
		
		if(lifeType == LifeType.PATH) return;
		velocity.add(acceleration);
		move(velocity);
		
		switch(lifeType) {
		case DISTANCE: if(startPosition.getDistanceBetweenVectors(getPosition()) > distanceOut) kill(); break;
		case NONE: 	break;
		case TIME:	if(tick++ > timeOut) kill();	break;
		case VELOCITY: 	break;
		case PROXIMITY_TARGET: 
			if(getCenter().getDistanceBetweenVectors(proximityTarget.getCenter()) < proximity) {
				isHit = true;
				kill(); 
			} else {
				double distanceTraveled = startPosition.getAngleBetweenVectors(getPosition()); 
				if(isVisibilityDelayed && !isVisible && distanceTraveled > visibilityDelay) visible();
				if(distanceTraveled > distanceOut) kill(); 
			}
			break;
		case PROXIMITY_LOCATION:
			//TODO implement proxy location
			break;
		case PATH:
			if(path.isComplete(getPosition())) {
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
	public void updateMid() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSlow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePassive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseEntered() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseExited() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

}
