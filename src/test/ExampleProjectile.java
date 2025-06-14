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
		move(velocity);
		if (x() > 1920 - getWidth() || x() < 0) velocity.setX(-velocity.x());
		if (y() > 1080 - getHeight() || y() < 0) velocity.setY(-velocity.y());		
	}

	@Override
	protected void updateMid() {return;}

	@Override
	protected void updateSlow() {return;}

	@Override
	protected void updatePassive() {return;}

}
