/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Point;

import warped.graphics.sprite.RotationSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.StyleType;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class GUIRotationPicker extends WarpedGUI {

	private double rotation = 0.0;
	private RotationSprite sprite;
	private Point lastPoint = new Point();
	private Point currentPoint = new Point();
	
	private WarpedAction pressAction = () -> {Console.ln("GUIAnglePicker -> default press action should be overwritten");}; 
	private WarpedAction releaseAction = () -> {Console.ln("GUIAnglePicker -> default release action should be overwritten");};
	
	public GUIRotationPicker(StyleType type) {
		switch(type) {
		case WARP_TECH:
			sprite = FrameworkSprites.anglePicker.generateRotationSprite(0, 0);
			break;
		default:
			Console.err("GUIAnglePicker -> Consturctor() -> unssuported style case : + type"); 
 			break;
		
		}
		setRaster(sprite.raster());
	}
	
	public double getRotation() {return rotation;}
	public void setRotation(double rotaiton) {
		this.rotation = UtilsMath.clampRadianRotation(rotaiton);
		updateGraphics();
	}
	
	public void updateGraphics() {
		sprite.setRotation(rotation);
		setRaster(sprite.raster());
	}
	
	public void setPressAction(WarpedAction pressAction) {
		this.pressAction = pressAction;
	}
	public void setReleasedAction(WarpedAction releaseAction) {
		this.releaseAction = releaseAction;
	}
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
		invisible();
		lastPoint.x = 0;
		lastPoint.y = 0;
		currentPoint.x = 0;
		currentPoint.y = 0;
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		double rotation = 0.0;
		currentPoint.setLocation(mouseEvent.getPointRelativeToObject());
		
		currentPoint.x -= size.x / 2;
		currentPoint.y -= size.y / 2;
		
		rotation = UtilsMath.angleBetweenVectorsSigned(lastPoint, currentPoint);		
		this.rotation += rotation;
		UtilsMath.clampRadianRotation(this.rotation);
		lastPoint.setLocation(currentPoint);
		updateGraphics();
	
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		lastPoint.x = 0;
		lastPoint.y = 0;
		currentPoint.x = 0;
		currentPoint.y = 0;
		pressAction.action();
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {releaseAction.action();}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		
	}

	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

	
	
}
