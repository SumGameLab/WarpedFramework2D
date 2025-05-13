/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.entities.projectile;

import java.awt.image.BufferedImage;

import warped.application.state.WarpedObject;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class Projectile extends WarpedObject {

	public enum LifeType {
		TIME,
		DISTANCE,
		VELOCITY
	}
	
	private LifeType lifeType = LifeType.TIME;
	
	private VectorD velocity;
	private VectorD acceleration;
	private VectorD startPosition;
		
	private int tick = 0;
	private int timeOut = 1000; 
	private double distanceOut = 0.0;
	
	protected void hit(WarpedObject collider) {
		Console.blueln("Proejctile -> hit() -> projectile hit : " + collider.getClass().getSimpleName());
		kill();
	}
	
	public Projectile(BufferedImage raster, VectorD position, VectorD velocity) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		this.setPosition(position);
		startPosition = new VectorD(position);
		acceleration = new VectorD();
	}
	
	public Projectile(BufferedImage raster, VectorD position, VectorD velocity, int timeOut) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		this.timeOut = timeOut;
		acceleration = new VectorD();
		lifeType = LifeType.TIME;
	}
	
	public Projectile(BufferedImage raster, VectorD position, VectorD velocity, double distanceOut) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		setPositionPointer(position);
		startPosition = new VectorD(position);
		this.distanceOut = distanceOut;
		acceleration = new VectorD();
		lifeType = LifeType.DISTANCE;
	}
	


	@Override
	public void updateObject() {
		velocity.add(acceleration);
		move(velocity);
		
		switch(lifeType) {
		case DISTANCE: if(startPosition.getDistanceBetweenVectors(getPosition()) > distanceOut) kill(); break;
		case TIME:	if(tick++ > timeOut) kill();	break;
		case VELOCITY: 	break;
		default:
			Console.err("PropProjectile -> updateObject -> invalid life type : " + lifeType);
			break;
		}
	}
	
	@Override
	public void updateMid() {return;}

	@Override
	public void updateSlow() {return;}

	@Override
	public void updatePassive() {return;}

	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

}
