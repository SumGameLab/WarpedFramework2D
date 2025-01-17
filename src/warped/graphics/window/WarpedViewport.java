/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import warped.WarpedProperties;
import warped.application.object.WarpedObject;
import warped.application.object.WarpedVoidObject;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManager;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.camera.WarpedCamera;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;

public class WarpedViewport {

	/**Notes : 
	 * 
	 * 
	 * ---ViewPort---  
	 * Each ViewPort has its own thread that is responsible for drawing
	 * 
	 * --Mouse Events
	 * ViewPort has two main functions related to mouseEvents.
	 * 
	 * The MouseEvent() function is called indirectly from the WarpedMouse thread.
	 * As mouse events are generated they are passed from the WarpedMouse to the screen and then to the relevant ViewPorts.
	 * 
	 * The other function is dispatchMouseEvent().
	 * This function is called periodically from the WarpedScreen thread. 
	 * */
	private String name;	
	private long updateDuration;
	
	private WarpedManager<? extends WarpedObject> target; // graphic input	
	public  WarpedCamera camera = WarpedState.cameraManager.getDefaultEntitieCamera();
	
	private ArrayList<Integer> targetGroups = new ArrayList<>();
	
	public long getUpdateDuration() {return updateDuration;}
	
	private boolean visible = true;
	private boolean interactive = true;
	
	private Vec2i size = new Vec2i(); 
	private Vec2i position = new Vec2i();
	
	private BufferedImage raster;    //graphic output
	private BufferedImage[] rasterBuffer;
	private static int bufferSize;
	private int rasterIndex = 0;
	
	
	   

	public WarpedMouseEvent mouseEvent;

	public ArrayList<WarpedObject> eventObjects = new ArrayList<>();
	public WarpedObject eventObject  = new WarpedVoidObject(); 
	public WarpedObject renderObject = new WarpedVoidObject(); //The object currently being rendered	
	
	protected Object[] renderHints = new Object[9];
	private RenderType renderType = RenderType.ACTIVE;
	
	AffineTransform at = new AffineTransform();
	
	public static final int RENDERING 			   = 0;
	public static final int COLOR                  = 1;
	public static final int TEXT_ANTIALIASING      = 2;
	public static final int ANTIALIASING		   = 3;
	public static final int INTERPOLATION          = 4;
	public static final int ALPHA_INTERPOLATION    = 5;
	public static final int STROKE                 = 6;
	public static final int DITHERING              = 7;
	public static final int OVERALL				   = 8;
	
	public static final Integer PRIMITIVE = 0;
	public static final Integer ADVANCE   = 1;
	
	private static final Composite clearComposite = AlphaComposite.getInstance(AlphaComposite.CLEAR);
	private static final Composite drawComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
	//--------
	//---------------- Constructors -------
	//--------
	public WarpedViewport(String name, WarpedManager<? extends WarpedObject> target, WarpedCamera camera, Vec2i frameSize, Vec2i framePosition) { /*GameContext must be defined at this point*/
		this.name = name;
		WarpedViewport.bufferSize = WarpedWindow.getBufferSize();
		//viewportTimer = new Timer("WarpedViewport : " + name);
		this.size = frameSize;
		this.position = framePosition; 
		this.target = target;
		this.camera = camera;
		camera.setCornerPins(0, 0, framePosition.x + frameSize.x, framePosition.y + frameSize.y);
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[bufferSize];
		for(int i = 0; i < bufferSize; i++) rasterBuffer[i] = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		raster = rasterBuffer[0];
		setDefaultRenderHints();
		
		//Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::update, 0, 16666666, TimeUnit.NANOSECONDS);
	}
	
	@SuppressWarnings("unchecked")
	public WarpedViewport(String name, WarpedManagerType target, int x, int y, int width, int height) {
		this.name = name;
		WarpedViewport.bufferSize = WarpedWindow.getBufferSize();
		//viewportTimer = new Timer("WarpedViewport : " + name);
		this.size = new Vec2i(width, height);
		this.position = new Vec2i(x, y); 
		this.target = WarpedState.getManager(target);;
		this.camera = WarpedState.cameraManager.getDefaultCamera(target);
		camera.setCornerPins(0, 0, x + width, y + height);
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[bufferSize];
		for(int i = 0; i < bufferSize; i++) rasterBuffer[i] = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		raster = rasterBuffer[0];
		setDefaultRenderHints();
		
	}
	
	@SuppressWarnings("unchecked")
	public WarpedViewport(String name, WarpedManagerType target) {
		this.name = name;
		WarpedViewport.bufferSize = WarpedWindow.getBufferSize();
		//viewportTimer = new Timer("WarpedViewport : " + name);
		this.size = new Vec2i(WarpedWindow.width, WarpedWindow.height);
		this.position = new Vec2i(); 
		this.target = WarpedState.getManager(target);;
		this.camera = WarpedState.cameraManager.getDefaultCamera(target);
		camera.setCornerPins(0, 0, size.x, size.y);
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[bufferSize];
		for(int i = 0; i < bufferSize; i++) rasterBuffer[i] = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		raster = rasterBuffer[0];
		setDefaultRenderHints();
		
	}

	public void update() {
		long cycleStartTime = System.nanoTime();
		render();
		updateDuration = System.nanoTime() - cycleStartTime;		
	}
	
	
	//--------
	//---------------------- Access ----------------------
	//--------	
	public void setTargetGroups(List<Integer> groups) {
		targetGroups.clear();
		for(int i = 0; i < groups.size(); i++) {
			int index = groups.get(i);
			if(index < 0 || index > target.getGroups().size()) {
				Console.err("WarpedViewport -> setTargetGroups() -> index out of bounds : " + index);
				return;
			}
			targetGroups.add(index);
		}
	}
	public void bakeTargetGroups(List<Integer> groups) {
		setTargetGroups(groups);
		
		BufferedImage baked = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics2D g = baked.createGraphics();

		setRenderHints(g);
		for(int i = 0; i < targetGroups.size(); i++) {
			getTarget().getGroup(targetGroups.get(i)).forEach((obj) -> {
	 			if(obj.isVisible()) {			
	 				obj.renderSize.set(obj.getSize());
	 				obj.renderPosition.set(obj.getPosition());	
	 				if(!camera.isClipped(obj)) {							
	 					if(renderHints[OVERALL] == PRIMITIVE) {
							g.drawImage(obj.raster(), (int)(obj.renderPosition.x),(int)(obj.renderPosition.y), (int)(obj.renderSize.x),(int)(obj.renderSize.y), null); // draw the object
						} else {						
							at.setTransform(1.0, 0.0, 0.0, 1.0, obj.renderPosition.x, obj.renderPosition.y);
							g.drawRenderedImage(obj.raster(), at);	
						}
	 				}
	 			}
	 		});
			
		}
		g.dispose();
		if(eventObjects.size() > 0 && eventObject == null) eventObject = eventObjects.get(eventObjects.size() - 1);
		raster = baked;		
	}
	public WarpedManager<? extends WarpedObject> getTarget() {return target;}
	public WarpedCamera getCamera() {return camera;}
	
	public Graphics2D getGraphics2D() {
		rasterIndex++;
		if(rasterIndex >= bufferSize) rasterIndex = 0;
		return rasterBuffer[rasterIndex].createGraphics();		
	}
	
	public void clearBake() {
		if(renderType != RenderType.BAKED) {
			Console.err("WarpedViewport -> clearBake() -> renderType is not baked");
			return;
		}
		
		raster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
	}
	
	public void setRenderMethod(RenderType renderType) {this.renderType = renderType;}
	public boolean isVisible() {if(visible)return true; return false;}
	public boolean isInteractive() {if(interactive)return true; return false;}
	public void interactive() {interactive = true;}
	public void ateractive() {interactive = false;}
	public void visible()   {visible = true;}
	public void invisible() {visible = false;}
	public String getName() {return name;}
	public Vec2i getPosition() {return position;}
	public Vec2i getSize() {return size;}
	public BufferedImage raster() {return raster;}
	
	
	//--------
	//-------- Graphics Options --------
	//--------
	public void setDefaultRenderHints() {
		renderHints[RENDERING] 			 = RenderingHints.VALUE_RENDER_QUALITY;
		renderHints[COLOR] 				 = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
		renderHints[ANTIALIASING] 		 = RenderingHints.VALUE_ANTIALIAS_ON;
		renderHints[TEXT_ANTIALIASING]   = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
		renderHints[INTERPOLATION]    	 = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		renderHints[ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
		renderHints[STROKE] 			 = RenderingHints.VALUE_STROKE_PURE;
		renderHints[DITHERING] 		     = RenderingHints.VALUE_DITHER_ENABLE;
		renderHints[OVERALL] 		     = ADVANCE;
	}
	
	public void hintRenderingQuality() {renderHints[RENDERING] = RenderingHints.VALUE_RENDER_QUALITY;}
	public void hintRenderingSpeed()   {renderHints[RENDERING] = RenderingHints.VALUE_RENDER_SPEED;}
                
	public void hintColorQuality() {renderHints[COLOR] = RenderingHints.VALUE_COLOR_RENDER_QUALITY;}
	public void hintColorSpeed() {renderHints[COLOR] = RenderingHints.VALUE_COLOR_RENDER_SPEED;}
	            
	public void hintAntialiasingOn() {renderHints[ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON;}
	public void hintAntialiasingOff() {renderHints[ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_OFF;}
	            
	public void hintTextAntialiasingOn() {renderHints[TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;}
	public void hintTextAntialiasingOff() {renderHints[TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;}
	            
	public void hintInterpolationNearestNeighbour() {renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;}
	public void hintInterpolationBilinear() {renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BILINEAR;}
	public void hintInterpolationBicubic() {renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BICUBIC;}
	            
	public void hintAlphaInterpolationQuality() {renderHints[ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;}
	public void hintAlphaInterpolationSpeed() {renderHints[ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;}
	            
	public void hintStrokeNormalise() {renderHints[STROKE] = RenderingHints.VALUE_STROKE_NORMALIZE;}
	public void hintStrokePure() {renderHints[STROKE] = RenderingHints.VALUE_STROKE_PURE;}
	            
	public void hintDitheringOn() {renderHints[DITHERING] = RenderingHints.VALUE_DITHER_ENABLE;}
	public void hintDitheringOff() {renderHints[DITHERING] = RenderingHints.VALUE_DITHER_DISABLE;}
	
	public void hintOverallAdvance() {renderHints[OVERALL] = ADVANCE;}
	public void hintOverallPrimitive() {renderHints[OVERALL] = PRIMITIVE;}
		
	//--------
	//-------------- Mouse Event ----------------------
	//--------
	public void MouseEvent(WarpedMouseEvent mouseEvent) {
		mouseEvent.updateTrace(this);
		this.mouseEvent = mouseEvent;				
	}
	
	public boolean isHit(Point trace) { // check if click is contained within the bounds of the view port
		if(trace.x > position.x && trace.x < (position.x + size.x)
		&& trace.y > position.y && trace.y < (position.y + size.y)) return true;
		else return false;
	}
	
	
	public void dispatchMouseEvents() {
		if(!isEventObject()) return;
		
		if(!isEvent()) return;
		else if(mouseEvent.isHandled()) {
			mouseEvent = null;
			return;		
		}
		
		eventObject.hovered();		
		eventObject.mouseEvent(mouseEvent);
		mouseEvent.handle();
		mouseEvent = null;
		eventObject = null;
	}
	
	protected void handleMouse() {
		if(!isEvent()  || !renderObject.isVisible() || !renderObject.isInteractive() || mouseEvent.isHandled()) return;		
		
		if(!isOverObject()) renderObject.unhovered();
		else {
			eventObjects.forEach(obj -> {obj.unhovered();});
			eventObjects.add(renderObject);		
		}
	}
		
	private boolean isEvent() {if(mouseEvent == null) return false; else return true;}
	private boolean isEventObject() {if(eventObject == null) return false; else return true;}
	
	private boolean isOverObject(){
		WarpedMouseEvent mEvent = mouseEvent;
		if(mEvent == null) return false; 
		Point point = mEvent.getPointRelativeToViewPort();
		
		int traceX = (int)(point.x - renderObject.renderPosition.x);
		int traceY = (int)(point.y - renderObject.renderPosition.y);
		
		if(traceX > 0 && traceX < renderObject.renderSize.x
		&& traceY > 0 && traceY < renderObject.renderSize.y) {
			
			int pixelX = (int)(traceX / camera.getZoom());
			int pixelY = (int)(traceY / camera.getZoom());
			
			if(pixelX > 0 && pixelX < renderObject.raster().getWidth() 
			&& pixelY > 0 && pixelY < renderObject.raster().getHeight()) {
				int col = renderObject.raster().getRGB(pixelX, pixelY);
				int alpha = 0xFF & (col >> 24);
				if(alpha > WarpedMouse.ALPHA_THRESHHOLD) return true;
				else return false;				
			} else return false;
		}else return false;
	}
	

	//--------
	//---------------- Graphics --------
	//--------	
	public enum RenderType {
		ACTIVE,
		ACTIVE_TRANSFORMED_SCALED,
		TARGET_GROUPS,
		BAKED,
		
	}
	
	
	public void render() {	
		switch(renderType) {
		case ACTIVE: renderActive();									 break;
		case ACTIVE_TRANSFORMED_SCALED: renderTransformedScaledActive(); break;
		case TARGET_GROUPS: renderTargetGroup(); 						 break;
		case BAKED: return;
		default:
			Console.err("WarpedViewPort -> render() -> invalid case : + renderType");
			break;
		}
		raster = rasterBuffer[rasterIndex];
	}
	
	protected void setRenderHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_RENDERING, 		   renderHints[RENDERING]);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 	   renderHints[ANTIALIASING]); 
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   renderHints[TEXT_ANTIALIASING]); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, 	   renderHints[COLOR]); 
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 	   renderHints[INTERPOLATION]); 
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, renderHints[ALPHA_INTERPOLATION]); 
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, 	   renderHints[STROKE]); 
		g.setRenderingHint(RenderingHints.KEY_DITHERING, 		   renderHints[DITHERING]); 
	}
	
	protected void renderTargetGroup() {
		eventObjects.clear();
		camera.update();
		Graphics2D g = getGraphics2D();
		setRenderHints(g);
		for(int i = 0; i < targetGroups.size(); i++) {
			getTarget().getGroup(targetGroups.get(i)).forEach((obj) -> {
	 			renderObject = obj;
	 			if(obj.isVisible()) {			
	 				obj.renderSize.set(obj.getSize());
	 				obj.renderPosition.set(obj.getPosition());
	 				if(camera.isTracking() && obj.isEqualTo(camera.getTarget()))	camera.updateTracking(obj);		
	 				if(!camera.isClipped(obj)) {							
	 					
	 					if(renderHints[OVERALL] == PRIMITIVE) {
							g.drawImage(obj.raster(), (int)(obj.renderPosition.x),(int)(obj.renderPosition.y), (int)(obj.renderSize.x),(int)(obj.renderSize.y), null); // draw the object
						} else {						
							at.setTransform(1.0, 0.0, 0.0, 1.0, obj.renderPosition.x, obj.renderPosition.y);
							g.drawRenderedImage(obj.raster(), at);	
						}
						handleMouse();
	 				}
	 			}
			});
		}

		g.dispose();
		if(eventObjects.size() > 0 && eventObject == null) eventObject = eventObjects.get(eventObjects.size() - 1);
	}
	
	protected void renderTransformedScaledActive() {
		eventObjects.clear();
		camera.update();
		Graphics2D g = getGraphics2D();
		setRenderHints(g);
		getTarget().forEachActiveGroup((obj) -> {
			renderObject = obj;
			if(obj.isVisible()) {	
				if(camera.isTracking() && obj.isEqualTo(camera.getTarget()))	camera.updateTracking(obj);		
				camera.sizeTransformation(obj); //scaled size of object based on the zoom value
				obj.renderPosition.set(obj.getPosition());
				camera.positionTransformation(obj); //Offset the screen position for the view ports camera p
				obj.renderPosition.add(WarpedWindow.center); // offset the origin to the center of the screen
				if(!camera.isClipped(obj)) {							
					
					handleMouse();
					
					
					if(renderHints[OVERALL] == PRIMITIVE) {
						g.drawImage(obj.raster(), (int)(obj.renderPosition.x),(int)(obj.renderPosition.y), (int)(obj.renderSize.x),(int)(obj.renderSize.y), null); // draw the object
					} 
					else if(obj.renderSize.x <= 1.0 || obj.renderSize.y <= 1.0) {
						g.drawImage(obj.raster(), (int)(obj.renderPosition.x),(int)(obj.renderPosition.y), 1, 1, null); // draw the object
					} else {
						at.setTransform(camera.getZoom(), 0.0, 0.0, camera.getZoom(), obj.renderPosition.x, obj.renderPosition.y);
						g.drawRenderedImage(obj.raster(), at);
					}
				}
			}
		});
		g.dispose();
		if(eventObjects.size() > 0 && eventObject == null) eventObject = eventObjects.get(eventObjects.size() - 1);
	}

	public void clear(Graphics2D g) {
		g = rasterBuffer[rasterIndex].createGraphics();
		g.setComposite(clearComposite);
		g.fillRect(0,0, size.x, size.y);
		g.setComposite(drawComposite);		
	}
	
	protected void renderActive() {
		eventObjects.clear();
		camera.update();
		Graphics2D g = getGraphics2D();
		clear(g);	
		
		//<WarpedGroup<WarpedObject>> activeGroups = target.getActiveGroups();
		
		//getTarget().getActiveGroups().forEach(group -> {
			
		//});
		/*
		 */
 		getTarget().forEachActiveGroup((obj) -> {
 			renderObject = obj;
 			if(obj.isVisible()) {			
 				obj.renderSize.set(obj.getSize());
 				obj.renderPosition.set(obj.getPosition());
 				if(camera.isTracking() && obj.isEqualTo(camera.getTarget()))	camera.updateTracking(obj);		
 				if(!camera.isClipped(obj)) {							
 					
 					if(renderHints[OVERALL] == PRIMITIVE) {
						g.drawImage(obj.raster(), (int)(obj.renderPosition.x),(int)(obj.renderPosition.y), (int)(obj.renderSize.x),(int)(obj.renderSize.y), null); // draw the object
					} else {						
						at.setTransform(1.0, 0.0, 0.0, 1.0, obj.renderPosition.x, obj.renderPosition.y);
						g.drawRenderedImage(obj.raster(), at);	
					}
					handleMouse();
 				}
 			}
 		});
 		
		g.dispose();
		if(eventObjects.size() > 0) eventObject = eventObjects.get(eventObjects.size() - 1);
	}
	
		
	
	
	

}
