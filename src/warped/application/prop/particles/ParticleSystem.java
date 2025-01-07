/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.prop.particles;

import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.prop.WarpedProp;
import warped.user.mouse.WarpedMouseEvent;

public class ParticleSystem extends WarpedProp {

	int systemWidth;
	int systemHeight;
	
	public ParticleSystem(int systemWidth, int systemHeight) {
		super(new BufferedImage(systemWidth, systemHeight, WarpedProperties.BUFFERED_IMAGE_TYPE));
		this.systemWidth  = systemWidth;
		this.systemHeight = systemHeight;
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
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateRaster() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub
		
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
