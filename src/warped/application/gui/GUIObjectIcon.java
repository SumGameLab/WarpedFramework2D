/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.object.WarpedObject;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;

public class GUIObjectIcon extends WarpedGUI {


	protected WarpedObject target;
	private BufferedImage nullIcon;
	private BufferedImage icon;
	private Vec2i iconSize   = new Vec2i(100);
	private Vec2i iconOffset = new Vec2i(100);
	private int preferredIconSize = 100;
	
	
	
	public GUIObjectIcon() {
		nullIcon = new BufferedImage(preferredIconSize, preferredIconSize, WarpedProperties.BUFFERED_IMAGE_TYPE);
		icon     = new BufferedImage(preferredIconSize, preferredIconSize, WarpedProperties.BUFFERED_IMAGE_TYPE);
	}
	
	public GUIObjectIcon(Vec2d position, int maxIconSize) {
		this.position = position;
		this.preferredIconSize = maxIconSize;
		nullIcon = new BufferedImage(maxIconSize, maxIconSize, WarpedProperties.BUFFERED_IMAGE_TYPE);
		updateGraphics();
	}
	
	public GUIObjectIcon(Vec2d position, int iconSize, WarpedObject target) {
		this.position.set(position);
		this.target = target;
		this.preferredIconSize = iconSize;
		nullIcon = new BufferedImage(iconSize, iconSize, WarpedProperties.BUFFERED_IMAGE_TYPE);
		updateGraphics();
	}
		
	public void setTarget(WarpedObject target) {
		this.target = target;
		updateGraphics();	
	}

	public void setPreferredIconSize(int maxIconSize) {
		this.preferredIconSize = maxIconSize;
		updateGraphics();
	}
	
	
	private void setIconSizeAndPosition() {
		Vec2d targetSize = target.getSize();
		if(targetSize.x <= preferredIconSize && targetSize.y <= preferredIconSize) {
			iconSize.x = (int)targetSize.x;
			iconSize.y = (int)targetSize.y;
			
			iconOffset.x = (int)((preferredIconSize - targetSize.x) / 2);
			iconOffset.y = (int)((preferredIconSize - targetSize.y) / 2);
			
		} else {
			
			double aspectRatio = 1.0;
			if(targetSize.x > targetSize.y) {
				aspectRatio = targetSize.y / targetSize.x;
				iconSize.x = preferredIconSize;
				iconSize.y = (int)(preferredIconSize * aspectRatio);
				
				iconOffset.x = 0;
				iconOffset.y = (int)((preferredIconSize - targetSize.y) / 2);
				
			} else if (targetSize.y > targetSize.x) {
				aspectRatio = targetSize.x / targetSize.y;
				iconSize.y = preferredIconSize;
				iconSize.x = (int)(preferredIconSize * aspectRatio);
				
				iconOffset.x = (int)((preferredIconSize - targetSize.x) / 2);
				iconOffset.y = 0;
				
			} else {
				iconSize.x = preferredIconSize;
				iconSize.y = preferredIconSize;
				
				iconOffset.x = 0; 
				iconOffset.y = 0;
			}	
		}
		
	}
	
	private void drawGraphics() {
		icon = new BufferedImage(preferredIconSize, preferredIconSize, WarpedProperties.BUFFERED_IMAGE_TYPE);
		icon.getGraphics().drawImage(target.raster(), iconOffset.x, iconOffset.y, iconSize.x, iconSize.y, null);
	}
	
	protected void updateGraphics() {		
		if(target == null) {
			setRaster(nullIcon);
		}
		else {
			setIconSizeAndPosition();
			drawGraphics();
			setRaster(icon);	
		}
		
	}
	
	protected void mouseEntered() {return;}
	protected void mouseExited() {return;}
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	protected void updateObject() {return;}
	protected void updatePosition() {return;}
	protected void updateRaster() {return;}

	

}
