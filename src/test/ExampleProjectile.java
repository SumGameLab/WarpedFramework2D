package test;

import java.awt.Color;
import java.awt.Graphics2D;

import warped.application.state.WarpedObject;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.UtilsMath;

public class ExampleProjectile extends WarpedObject {

	VectorD velocity = new VectorD(UtilsMath.random(-1.0, 1.0) * 6, UtilsMath.random(-1.0, 1.0) * 6);
	
	public ExampleProjectile(double x, double y) {
		setPosition(x, y);
		setSize(100, 100);
		Graphics2D g = getGraphics();
		g.setColor(Color.WHITE);
		g.fillOval(0, 0, getHeight(), getHeight());
		g.dispose();
		pushGraphics();
	}
	
	public ExampleProjectile(double x, double y, double velocityX, double velocityY) {
		setPosition(x, y);
		setSize(100, 100);
		velocity.set(velocityX, velocityY);
		Graphics2D g = getGraphics();
		g.setColor(Color.WHITE);
		g.fillOval(0, 0, getHeight(), getHeight());
		g.dispose();
		pushGraphics();
	}
	
	protected void hit(WarpedObject collider) {
		//kill();
	}
	
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
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void updateObject() {
		if (x() > 1920 - getWidth()) {
			velocity.setX(-velocity.x());
			getPosition().setX(1920 - getWidth());
		}
		if(x() < 0) {
			velocity.setX(-velocity.x());
			getPosition().setX(0);
		}
	}

	@Override
	protected void updatePosition(double deltaTime) {
		move(velocity.x() * deltaTime, velocity.y() * deltaTime);	
	}
	
	@Override
	protected void updateMid() {return;}

	@Override
	protected void updateSlow() {return;}



}
