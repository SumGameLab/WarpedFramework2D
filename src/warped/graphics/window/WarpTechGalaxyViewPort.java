/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import warped.application.state.WarpedManager;
import warped.application.state.WarpedObject;
import warped.utilities.math.vectors.VectorI;

public class WarpTechGalaxyViewPort extends WarpedViewport {

	public WarpTechGalaxyViewPort(String name, WarpedManager<? extends WarpedObject> target, WarpedCamera camera,
			VectorI frameSize, VectorI framePosition) {
		super(name, target, camera, frameSize, framePosition);
		// TODO Auto-generated constructor stub
	}

	/*
	protected void renderTransformedScaledActive() {
		eventObjects.clear();
		camera.update();
		Graphics2D g = getGraphics2D();
		getTarget().forEachActiveGroup((obj) -> {
			renderObject = obj;
			if(obj.isVisible()) {	
				if(camera.isTracking() && obj.isEqualTo(camera.getTarget()))	camera.updateTracking(obj);		
				camera.sizeTransformation(obj); //scaled size of object based on the zoom value
				obj.renderPosition.set(obj.getPosition()); //2D screen position for the objects 3D world position
				camera.positionTransformation(obj); //Offset the screen position for the view ports camera p
				obj.renderPosition.add(WarpedWindow.center); // offset the origin to the center of the screen
				if(!camera.isClipped(obj)) {							
					
					handleMouse();
					
					if(renderHints[OVERALL] == PRIMITIVE || obj.renderSize.x > 600.0 || obj.renderSize.y > 600) {
						g.drawImage(obj.raster(), (int)(obj.renderPosition.x),(int)(obj.renderPosition.y), (int)(obj.renderSize.x),(int)(obj.renderSize.y), null); // draw the object
					} 
					else if(obj.renderSize.x <= 1.0 || obj.renderSize.y <= 1.0) {
						g.drawImage(obj.raster(), (int)(obj.renderPosition.x),(int)(obj.renderPosition.y), 1, 1, null); // draw the object
					} else {
						AffineTransform at = new AffineTransform();
						at.translate(obj.renderPosition.x, obj.renderPosition.y);
						at.scale(camera.getZoom(), camera.getZoom());
						setRenderHints(g);
						g.drawRenderedImage(obj.raster(), at);
					}
				}
			}
		});
		g.dispose();
		if(eventObjects.size() > 0) eventObject = eventObjects.get(eventObjects.size() - 1);
	}
	*/
}
