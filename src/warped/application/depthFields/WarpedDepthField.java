/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.depthFields;

import java.awt.image.BufferedImage;

import warped.application.object.WarpedObject;
import warped.application.state.managers.CameraManager;
import warped.graphics.camera.WarpedCameraType;
import warped.user.mouse.WarpedMouseEvent;

public class WarpedDepthField extends WarpedObject {
	
	private double horizontalPanSpeed  = 1.0;
	private double verticalPanSpeed    = 1.0;
	
	public WarpedDepthField(BufferedImage image) {
		setSize(image.getWidth(), image.getHeight());
		sprite.paint(image);
	}
	
	public void panUp()    {move(0.0, verticalPanSpeed   * (1 / CameraManager.getCamera(WarpedCameraType.DEFAULT_ENTITIE).getZoom()));};
	public void panDown()  {move(0.0, -verticalPanSpeed   * (1 / CameraManager.getCamera(WarpedCameraType.DEFAULT_ENTITIE).getZoom()));};
	public void panLeft()  {move(horizontalPanSpeed * (1 / CameraManager.getCamera(WarpedCameraType.DEFAULT_ENTITIE).getZoom()));};
	public void panRight() {move(-horizontalPanSpeed * (1 / CameraManager.getCamera(WarpedCameraType.DEFAULT_ENTITIE).getZoom()));};
	
	public void setPanSpeed(double speed) {horizontalPanSpeed = verticalPanSpeed = speed;}
	public void setHorizontalPanSpeed(double horizontalPanSpeed) { this.horizontalPanSpeed = horizontalPanSpeed;}
	public void setVerticalPanSpeed(double verticalPanSpeed) { this.verticalPanSpeed = verticalPanSpeed;}
	
	protected void mouseEntered() {return;}
	protected void mouseExited() {return;}
	protected void mouseOver() {return;}
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	@Override
	public void updateMid() {return;};
	
	@Override
	public void updateSlow() {return;}; 

	@Override
	public void updatePassive() {return;}
	
	@Override
	public void updateObject() {return;}
}

