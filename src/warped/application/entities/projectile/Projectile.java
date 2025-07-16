/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.entities.projectile;

import java.awt.image.BufferedImage;

import warped.application.state.WarpedObject;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class Projectile extends WarpedObject {

	public enum LifeType {
		TIME,
		DISTANCE;
	}
	
	protected LifeType lifeType = LifeType.TIME;
	
	protected VectorD velocity;
	protected VectorD acceleration;
	protected VectorD startPosition;
		
	protected int tick = 0;
	protected int timeOut = 1000; 
	protected double distanceOut = 0.0;
	
	protected WarpedAction expireAction = () -> {Console.ln("Projectile -> default expire action");};
		
	
	/**This method will trigger when the projectile collides with another object.
	 * @implNote DO NOT CALL, this method is called automatically by the WarpedCollisionManager.
	 * @param collider - the object that the projectile collided with.
	 * @apiNote Collisions are only check for groups that have been added to the WarpedCollisionManager. 
	 * @author 5som3 */
	protected void hit(WarpedObject collider) {
		Console.ln("Proejctile -> hit() -> projectile hit : " + collider.getClass().getSimpleName());
		kill();
	}
	
	/**A projectile with the specified parameters.
	 * @param raster - the image that the projectile will appear as.
	 * @param position - the start position for the projectile.
	 * @param velocity - the speed and direction of the projectile.
	 * @apiNote Projectiles will eventually expire if no collision occurs. 
	 * @apiNote By default projectiles will expire after 1000 ticks.
	 * @apiNote When a projectile expires the expireAction will trigger. 
	 * @author 5som3*/
	public Projectile(BufferedImage raster, VectorD position, VectorD velocity) {
		sprite.setSize(raster.getWidth(), raster.getHeight());
		sprite.paint(raster);
		this.velocity = velocity;
		this.setPosition(position);
		startPosition = new VectorD(position);
		acceleration = new VectorD();
	}
	
	/**A projectile with the specified parameters.
	 * @param raster - the image that the projectile will appear as.
	 * @param position - the start position for the projectile.
	 * @param velocity - the speed and direction of the projectile.
	 * @param timeOut - the number of ticks that will occur before the projectile expires. 
	 * @apiNote Projectiles will eventually expire if no collision occurs. 
	 * @apiNote When a projectile expires the expireAction will trigger. 
	 * @author 5som3*/
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
	
	/**A projectile with the specified parameters.
	 * @param raster - the image that the projectile will appear as.
	 * @param position - the start position for the projectile.
	 * @param velocity - the speed and direction of the projectile.
	 * @param distanceOut - the distance that the projectile can travel before it expires. 
	 * @apiNote Projectiles will eventually expire if no collision occurs. 
	 * @apiNote When a projectile expires the expireAction will trigger. 
	 * @author 5som3*/
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
	
	/**Set an action to trigger when the projectile expires.
	 * @param expireAction - the action to trigger at time of expiry.
	 * @apiNote The action will trigger when the projectile expires by any expire type.
	 * @author 5som3*/
	public void setExpireAction(WarpedAction expireAction) {this.expireAction = expireAction;}

	@Override
	public void updateObject() {		
		switch(lifeType) {
		case DISTANCE:
			if(startPosition.getDistanceBetweenVectors(getCenter()) > distanceOut) {
				expireAction.action();
				kill();
			}
			break;
		case TIME:
			tick++;
			if(tick > timeOut) {
				expireAction.action();
				kill();	
			}
			break;
		default:
			Console.err("PropProjectile -> updateObject -> invalid life type : " + lifeType);
			break;
		}
	}
	
	@Override
	protected void updatePosition(double deltaTime) {
		velocity.add(acceleration);
		move(velocity.x() * deltaTime, velocity.y() * deltaTime);
		
	}
	
	@Override
	public void updateMid() {return;}

	@Override
	public void updateSlow() {return;}

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
