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
import java.util.Arrays;
import java.util.List;

import warped.WarpedProperties;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManager;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.camera.WarpedCamera;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class WarpedViewport {

	/*Notes : 
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
	 * 
	 * It would be ideal to separate mouse events from the drawing the viewport objects but it is a convenient time to check them as all the data should be in the cache at that time 
	 * */
	
	private String name;	
	private long updateDuration;
	
	private WarpedManager<?> target; // graphic input	
	public  WarpedCamera camera = WarpedState.cameraManager.getDefaultEntitieCamera();
	
	private ArrayList<Integer> targetGroups = new ArrayList<>();
	
	public long getUpdateDuration() {return updateDuration;}
	
	private boolean visible = true;
	private boolean interactive = true;
	
	private VectorI size = new VectorI(); 
	private VectorI position = new VectorI();
	
	private BufferedImage raster;    //graphic output
	private BufferedImage[] rasterBuffer;
	private static int bufferSize;
	private int rasterIndex = 0;

	public WarpedMouseEvent mouseEvent;

	public ArrayList<WarpedObject> eventObjects = new ArrayList<>();
	
	protected Object[] renderHints = new Object[8];
	
	private RenderType renderType = RenderType.ACTIVE;
	private WarpedAction renderMethod;
	
	AffineTransform at = new AffineTransform();
	
	public static final int RENDERING 			   = 0;
	public static final int COLOR                  = 1;
	public static final int TEXT_ANTIALIASING      = 2;
	public static final int ANTIALIASING		   = 3;
	public static final int INTERPOLATION          = 4;
	public static final int ALPHA_INTERPOLATION    = 5;
	public static final int STROKE                 = 6;
	public static final int DITHERING              = 7;
		
	private static final Composite clearComposite = AlphaComposite.getInstance(AlphaComposite.CLEAR);
	private static final Composite drawComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
	
	public enum RenderType {
		PRIMITIVE,
		PRIMITIVE_TRANSFORMED_SCALED,
		ACTIVE,
		ACTIVE_TRANSFORMED_SCALED,
		BAKED, 
		BAKED_TRANSFORMED_SCALED,
		TARGET_GROUPS,
		TARGET_GROUPS_TRANSFORMED_SCALED,
	}
	
	//--------
	//---------------- Constructors -------
	//--------
	public WarpedViewport(String name, WarpedManager<? extends WarpedObject> target, WarpedCamera camera, VectorI frameSize, VectorI framePosition) { /*GameContext must be defined at this point*/
		this.name = name;
		WarpedViewport.bufferSize = WarpedWindow.getBufferSize();
		//viewportTimer = new Timer("WarpedViewport : " + name);
		this.size = frameSize;
		this.position = framePosition; 
		this.target = target;
		this.camera = camera;
		camera.setCornerPins(0, 0, framePosition.x() + frameSize.x(), framePosition.y() + frameSize.y());
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[bufferSize];
		for(int i = 0; i < bufferSize; i++) rasterBuffer[i] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		raster = rasterBuffer[0];
		setDefaultRenderHints();
		setRenderMethod(RenderType.ACTIVE);
		
		
	}
	
	
	public WarpedViewport(String name, WarpedManagerType target, int x, int y, int width, int height) {
		this.name = name;
		WarpedViewport.bufferSize = WarpedWindow.getBufferSize();
		//viewportTimer = new Timer("WarpedViewport : " + name);
		this.size = new VectorI(width, height);
		this.position = new VectorI(x, y); 
		this.target = WarpedState.getManager(target);;
		this.camera = WarpedState.cameraManager.getDefaultCamera(target);
		camera.setCornerPins(0, 0, x + width, y + height);
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[bufferSize];
		for(int i = 0; i < bufferSize; i++) rasterBuffer[i] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		raster = rasterBuffer[0];
		setDefaultRenderHints();
		setRenderMethod(RenderType.ACTIVE);
		
	}
	
	
	public WarpedViewport(String name, WarpedManagerType target) {
		this.name = name;
		WarpedViewport.bufferSize = WarpedWindow.getBufferSize();
		//viewportTimer = new Timer("WarpedViewport : " + name);
		this.size = new VectorI(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight());
		this.position = new VectorI(); 
		this.target = WarpedState.getManager(target);;
		this.camera = WarpedState.cameraManager.getDefaultCamera(target);
		camera.setCornerPins(0, 0, size.x(), size.y());
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[bufferSize];
		for(int i = 0; i < bufferSize; i++) rasterBuffer[i] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		raster = rasterBuffer[0];
		setDefaultRenderHints();
		setRenderMethod(RenderType.ACTIVE);
		
	}

	/**DO NOT CALL - this method is scheduled for automatic execution by the WarpedWindow.
	 * @implNote redraws the viewport based on the current target / targets.
	 * @author SomeKid*/
	public final void update() {
		long cycleStartTime = System.nanoTime();
		camera.update();
		render();
		updateDuration = System.nanoTime() - cycleStartTime;		
	}
	
	/**For the currently targeted manager, set which groups should be viewed by this viewport.
	 * @param groups - a list of the groups within the current targeted manager to draw.
	 * @author SomeKid*/
	public final void setTargetGroups(Integer... groups) {setTargetGroups(Arrays.asList(groups));}
	
	/**For the currently targeted manager, set which groups should be viewed by this viewport.
	 * @param groups - a list of the groups within the current targeted manager to draw.
	 * @author SomeKid*/
	public final void setTargetGroups(List<Integer> groups) {
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
	
	/**Sets the render method to Baked and renders the the currently targeted groups (if any).
	 * @param isScaledAndTransformed - if true the render method will scale and transform the image according to the current camera 2.
	 * @param groups - a list of the groups within the current targeted manager to bake.
	 * @apiNote baked viewports will only draw groups that have been targeted, it is independent if which groups are open / closed.
	 * @author SomeKid*/
	public void bakeGroups(boolean isBakedAndTransformed, Integer... groups) {
		setTargetGroups(groups);
		bakeGroups(isBakedAndTransformed);
	}
	
	/**Sets the render method to Baked and renders the the currently targeted groups (if any).
	 * @param isScaledAndTransformed - if true the render method will scale and transform the image according to the current camera 2.
	 * @apiNote baked viewports will only draw groups that have been targeted, it is independent if which groups are open / closed.
	 * @author SomeKid*/
	public void bakeGroups(boolean isScaledAndTransformed) {		
		setRenderMethod(RenderType.BAKED);
		BufferedImage baked = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics2D g = baked.createGraphics();
		setRenderHints(g);
		
		if(isScaledAndTransformed) {	
			for(int i = 0; i < targetGroups.size(); i++) {
				getTarget().getGroup(targetGroups.get(i)).forEach((obj) -> {
					obj.setRenderTransformations(camera);
					if(obj.isVisible() && !camera.isClipped(obj)) {			
						at.setTransform(1.0, 0.0, 0.0, 1.0, obj.getPosition().x(), obj.getPosition().y());
						g.drawRenderedImage(obj.raster(), at);	
					}
				});
			}
		} else for(int i = 0; i < targetGroups.size(); i++) {
			getTarget().getGroup(targetGroups.get(i)).forEach((obj) -> {
				if(obj.isVisible() && !camera.isClipped(obj)) {			
					at.setTransform(1.0, 0.0, 0.0, 1.0, obj.getPosition().x(), obj.getPosition().y());
					g.drawRenderedImage(obj.raster(), at);	
				}
			});
		}
		
		g.dispose();
		raster = baked;		
	}
	
	/**The manager that this viewport is targeting.
	 * @return WarpedManager<?> - the current manager.
	 * @author SomeKid*/
	public WarpedManager<? extends WarpedObject> getTarget() {return target;}
	
	/**The camera this viewport is using to calculate render translation and scale. 
	 * @return WarpedCamera - the current camera.
	 * @author SomeKid*/
	public WarpedCamera getCamera() {return camera;}

	
	/**Is the viewport visible in the window
	 * @returns boolean - If true the viewport will be drawn in the window.
	 * @author SomeKid */
	public boolean isVisible() {return visible;}
	
	/**Can the mouse interact with the viewport.
	 * @return boolean - If true the mouse will be able to interact with objects displayed in the viewport
	 * @author SomeKid */
	public boolean isInteractive() {return interactive;}
	
	/**Set type of rendering that this viewport should use.
	 * All viewports will draw groups from their respective targeted managers.
	 * @param renderType - the type of rendering to use.
	 * @apiNote PRIMITIVE 
	 * @apiNote    - This is the simplest and fastest render method. 
	 * @apiNote    - Only groups which are 'open' will be rendered.
	 * @apiNote    - Camera scale and translation will be ignored.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render Hints will not be applied. 
	 * @apiNote PRIMITIVE_TRANSFOREMD_SCALED
	 * @apiNote    - Only groups which are 'open' will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will not be applied. 
	 * @apiNote ACTIVE 
	 * @apiNote    - Only groups which are 'open' will be rendered.
	 * @apiNote    - Camera scale and translation will be ignored.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote ACTIVE_TRANSFORMED_SCALED
	 * @apiNote    - Only groups which are 'open' will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote BAKED
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation will be ignored.
	 * @apiNote    - Produces one frame at the time this function is called, does not update actively.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote BAKED_TRANSFORMED_SCALED
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Produces one frame at the time this function is called, does not update actively.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote TARGET_GROUPS
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation will be ignored.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote TARGET_GROUPS_TRANSFORMED_SCALED
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * */
	public void setRenderMethod(RenderType renderType) {
		this.renderType = renderType;
		switch(renderType) {
		case PRIMITIVE:
			renderMethod = primitive;
			break;
		case PRIMITIVE_TRANSFORMED_SCALED:
			renderMethod = primitiveTransformedScaled;
			break;
		case ACTIVE: 
			renderMethod = render;				
			break;
		case ACTIVE_TRANSFORMED_SCALED:
			renderMethod = renderTransformedScaled;	
			break;
		case BAKED:
			bakeGroups(false);
			renderMethod = renderBaked;				
			break;
		case BAKED_TRANSFORMED_SCALED:
			bakeGroups(true);
			renderMethod = renderBaked;
			break;
		case TARGET_GROUPS:			
			renderMethod = renderTargets; 
			break;
		case TARGET_GROUPS_TRANSFORMED_SCALED:
			renderMethod = renderTargetsTransformedScaled;
			break;
		default:
			Console.err("WarpedViewport -> setRenderMethod() -> invalid case :" + renderType);
			return;		
		}
	}
	
	/**Set if the mouse can interactive with objects drawn by this viewport
	 * @param isInteractive  - If true the mouse will be able to interact with objects displayed in the viewport
	 * @apiNote If the viewport doesn't need mouse I/O you should set this false.
	 * @impNote A viewport that is not visible will not be able to interact with the mouse even if isInteractve is true
	 * @author SomeKid */
	public void setInteractive(boolean isInteractive) {this.interactive = isInteractive;}
		
	/**Set if the viewport is visible in window
	 * @param isVisible - If true the viewport will be drawn in the window
	 * @impNote A viewport that is not visible will not be able to interact with the mouse even if isInteractve is true
	 * @author SomeKid*/
	public void setVisible(boolean isVisible) {this.visible = isVisible;}

	/**Get the name of the viewport
	 * @return String - the name
	 * @apiNote The name of the viewport is mainly for debug purposes. It is not important for the operation of the viewport.
	 * @author SomeKid*/
	public String getName() {return name;}
	
	/**Get the X position of the viewport relative to the window
	 * @return int - the top left corner of this viewport X coordinate measured in pixels.
	 * @implNote The origin of the window is the top left corner of the window.
	 * @author SomeKid*/
	public int getX() {return position.x();}
	
	/**Get the Y position of the viewport relative to the window
	 * @return int - the top left corner of this viewport Y coordinate measured in pixels.
	 * @implNote The origin of the window is the top left corner of the window.
	 * @author SomeKid*/
	public int getY() {return position.y();}
	
	/**Get the width of this viewport
	 * @return int - the width of the viewport measured in pixels 
	 * @author SomeKid*/
	public int getWidth() {return size.x();}
	
	/**Get the height of this viewport
	 * @return int - the height of the viewport measured in pixels
	 * @author SomeKid*/
	public int getHeight() {return size.y();}
	
	/**The graphic output for this viewport
	 * @return BufferedImage - this image is the current frame of viewport at the time this function is called.
	 * @implNote You should not try to edit the viewport raster manually. 
	 * @implNote Editing this image will possibly cause momentary graphical artifacts.
	 * @author SomeKid*/
	public BufferedImage raster() {return raster;}
	
	/**Sets the rendering parameters for this viewport to the default.
	 * @apiNote Call this function if you have changed the render settings and want to reset them.
	 * @author SomeKid*/
	public void setDefaultRenderHints() {
		renderHints[RENDERING] 			 = RenderingHints.VALUE_RENDER_QUALITY;
		renderHints[COLOR] 				 = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
		renderHints[ANTIALIASING] 		 = RenderingHints.VALUE_ANTIALIAS_ON;
		renderHints[TEXT_ANTIALIASING]   = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
		renderHints[INTERPOLATION]    	 = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		renderHints[ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
		renderHints[STROKE] 			 = RenderingHints.VALUE_STROKE_PURE;
		renderHints[DITHERING] 		     = RenderingHints.VALUE_DITHER_ENABLE;
	}
	
	/**Better render quality but slower.
	 * @apiNote primitive render methods will ignore these hints. 
	 * @author SomeKid*/
	public void hintRenderingQuality() {renderHints[RENDERING] = RenderingHints.VALUE_RENDER_QUALITY;}
	/**Reduced render quality but faster.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintRenderingSpeed()   {renderHints[RENDERING] = RenderingHints.VALUE_RENDER_SPEED;}
	/**Better colour quality but slower.
	 * @apiNote primitive render methods will ignore these hints. 
	 * @author SomeKid*/
	public void hintColorQuality() {renderHints[COLOR] = RenderingHints.VALUE_COLOR_RENDER_QUALITY;}
	/**Reduced colour quality but faster.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintColorSpeed() {renderHints[COLOR] = RenderingHints.VALUE_COLOR_RENDER_SPEED;}
	/**Turn on antialiasing for images.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/            
	public void hintAntialiasingOn() {renderHints[ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON;}
	/**Turn off antialiasing for images.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/      
	public void hintAntialiasingOff() {renderHints[ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_OFF;}
	/**Turn on antialiasing for text.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/      
	public void hintTextAntialiasingOn() {renderHints[TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;}
	/**Turn off antialiasing for text.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/      
	public void hintTextAntialiasingOff() {renderHints[TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;}
	
	/**Scale images using NearestNeighbour algorithm - fast but low quality.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/      
	public void hintInterpolationNearestNeighbour() {renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;}
	/**Scale images using Bilinear algorithm - better quality than NearestNeighbour but slower.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/      
	public void hintInterpolationBilinear() {renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BILINEAR;}
	/**Scale images using Bicubic algorithm - slow but accurate scaling.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/      
	public void hintInterpolationBicubic() {renderHints[INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BICUBIC;}
	            
	/**Best quality alpha blending, slowest
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintAlphaInterpolationQuality() {renderHints[ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;}
	/**Fast, reduced fidelity 
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintAlphaInterpolationSpeed() {renderHints[ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;}
	            
	/**Not sure what this does, lol.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintStrokeNormalise() {renderHints[STROKE] = RenderingHints.VALUE_STROKE_NORMALIZE;}
	/**Not sure what this does, lol.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintStrokePure() {renderHints[STROKE] = RenderingHints.VALUE_STROKE_PURE;}
	            
	/**Improves the quality of edges of rotated images, increases render time.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintDitheringOn() {renderHints[DITHERING] = RenderingHints.VALUE_DITHER_ENABLE;}
	/**Skip dithering, increase render time.
	 * @apiNote primitive render methods will ignore these hints.
	 * @author SomeKid*/
	public void hintDitheringOff() {renderHints[DITHERING] = RenderingHints.VALUE_DITHER_DISABLE;}
	
		
	//--------
	//-------------- Mouse Event ----------------------
	//--------
	/** DO NOT CALL.
	 * @implNote This function is called from the WarpedWindow class automatically when a mouse event occurs over it.
	 * @implNote This function may be called multiple times per frame, only the last mouse event of each frame is considered; all other events will be trashed.
	 * @author SomeKid */
	public void MouseEvent(WarpedMouseEvent mouseEvent) {
		mouseEvent.updateTrace(this);
		this.mouseEvent = mouseEvent;
	}
	
	/** DO NOT CALL. 
	 * @implNote This function is called automatically from the WarpedWindow once at the end of each render cycle.
	 * @author SomeKid*/
	public void dispatchMouseEvents() {
		if(eventObjects.size() == 0 || !isEvent()) return;
		WarpedObject eventObject = eventObjects.get(eventObjects.size() - 1);
		eventObjects.clear();
		
		if(mouseEvent.isHandled()) {
			mouseEvent = null;
			return;		
		}
		
		eventObject.hovered();		
		eventObject.mouseEvent(mouseEvent);
		mouseEvent.handle();
		mouseEvent  = null;
		eventObject = null;
		
	}
	
	/**Checks if the renderObject interacts with the mouseEvent, if the renderObject is added to the eventObjects shortList.
	 * The last object in eventObjects will be the object that actually receives a mouse event when dispactMouseEvent() triggers. 
	 * 	@author 5som3*/
	protected void handleMouse(WarpedObject renderObject) {
		if(!isEvent()  || !renderObject.isVisible() || !renderObject.isInteractive()) return;		
		if(isOverObject(renderObject)) eventObjects.add(renderObject);
		else renderObject.unhovered();
	}
		
	private boolean isEvent() {if(mouseEvent == null) return false; else return true;}

	
	private boolean isOverObject(WarpedObject renderObject){
		WarpedMouseEvent mEvent = mouseEvent;
		if(mEvent == null) return false; 
		Point point = mEvent.getPointRelativeToViewPort();
		
		int traceX = (int)(point.x - renderObject.getRenderPosition().x());
		int traceY = (int)(point.y - renderObject.getRenderPosition().y());
		
		if(traceX > 0 && traceX < renderObject.getRenderSize().x()
		&& traceY > 0 && traceY < renderObject.getRenderSize().y()) {
			
			int pixelX = (int)(traceX / camera.getZoom());
			int pixelY = (int)(traceY / camera.getZoom());
			
			if(pixelX > 0 && pixelX < renderObject.raster().getWidth() 
			&& pixelY > 0 && pixelY < renderObject.raster().getHeight()) {
				int col = renderObject.raster().getRGB(pixelX, pixelY);
				int alpha = 0xFF & (col >> 24);
				if(alpha > WarpedMouse.ALPHA_THRESHHOLD) {
					return true;
				}
				else return false;				
			} else return false;
		}else return false;
	}
	

	//--------
	//---------------- Graphics --------
	//--------	
	
	/**Get the graphics for this viewport.
	 * @return Graphics2D - a graphics interface with next buffer in the viewport.
	 * @implNote any changes made to the graphics will not not be visible raster is set to the new buffer.
	 * @author SomeKid*/
	private Graphics2D getGraphics() {
		rasterIndex++;
		if(rasterIndex >= bufferSize) rasterIndex = 0;
		Graphics2D g = rasterBuffer[rasterIndex].createGraphics();
		clear(g);
		return g; 		
	}
	
	/** Renders the viewport based on the set method and set's the output raster to the new buffer
	 * @author SomeKid*/
	private void render() {	
		renderMethod.action();
		raster = rasterBuffer[rasterIndex];
	}
	
	/**Sets the render hints for a graphical context to the render hint parameters of this Viewport.
	 * @author SomeKid*/
	private void setRenderHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_RENDERING, 		   renderHints[RENDERING]);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 	   renderHints[ANTIALIASING]); 
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   renderHints[TEXT_ANTIALIASING]); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, 	   renderHints[COLOR]); 
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 	   renderHints[INTERPOLATION]); 
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, renderHints[ALPHA_INTERPOLATION]); 
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, 	   renderHints[STROKE]); 
		g.setRenderingHint(RenderingHints.KEY_DITHERING, 		   renderHints[DITHERING]); 
	}
	
	/*
	protected void renderTargetGroup() {
		eventObjects.clear();
		camera.update();
		Graphics2D g = getGraphics2D();
		setRenderHints(g);
		for(int i = 0; i < targetGroups.size(); i++) {
			getTarget().getGroup(targetGroups.get(i)).forEach((obj) -> {
	 			renderObject = obj;
	 			if(obj.isVisible()) {			
	 				if(camera.isTracking() && obj.isEqualTo(camera.getTarget()))	camera.updateTracking(obj);		
	 				if(!camera.isClipped(obj)) {							
	 					
	 					if(renderHints[OVERALL] == PRIMITIVE) {
							g.drawImage(obj.raster(), (int)(obj.getRenderPosition().x),(int)(obj.getRenderPosition().y), (int)(obj.getRenderSize().x),(int)(obj.getRenderSize().y), null); // draw the object
						} else {						
							at.setTransform(1.0, 0.0, 0.0, 1.0, obj.getRenderPosition().x, obj.getRenderPosition().y);
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

	protected void renderActive() {
		eventObjects.clear();
		camera.update();
		Graphics2D g = getGraphics2D();
		clear(g);	
		
		//<WarpedGroup<WarpedObject>> activeGroups = target.getActiveGroups();
		
		//getTarget().getActiveGroups().forEach(group -> {
		
		//});
		
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
	*/

	/**Fills a graphical context with 100% transparent pixels. 
	 * @author SomeKid */
	private void clear(Graphics2D g) {
		g = rasterBuffer[rasterIndex].createGraphics();
		g.setComposite(clearComposite);
		g.fillRect(0,0, size.x(), size.y());
		g.setComposite(drawComposite);		
	}
	
	/**PRIMITIVE                                                                             
   	 * - This is the simplest and fastest render method.                                  
   	 * - Only groups which are 'open' will be rendered.                                   
   	 * - Camera scale and translation will be ignored.                                    
   	 * - Actively produces new frames at target 60fps.                                    
   	 * - Render Hints will not be applied.                                                                                        
	 * @author 5som3*/
	private final WarpedAction primitive = () -> {
		Graphics2D g = getGraphics();
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations();
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !camera.isClipped(obj)) {			
				g.drawImage(obj.raster(), (int)(obj.getRenderPosition().x()),(int)(obj.getRenderPosition().y()), (int)(obj.getRenderSize().x()),(int)(obj.getRenderSize().y()), null); 
			}
			handleMouse(obj);
		});		
		g.dispose();
	};
	
	/**PRIMITIVE_TRANSFOREMD_SCALED                                                          
	 *   - Only groups which are 'open' will be rendered.                                   
	 *   - Camera scale and translation is applied.                                         
     *   - Actively produces new frames at target 60fps.                                    
     *   - Render hints will not be applied.                 
     * @author 5som3*/
	private final WarpedAction primitiveTransformedScaled = () -> {
		Graphics2D g = getGraphics();
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations(camera);
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !camera.isClipped(obj)) {			
				g.drawImage(obj.raster(), (int)(obj.getRenderPosition().x()),(int)(obj.getRenderPosition().y()), (int)(obj.getRenderSize().x()),(int)(obj.getRenderSize().y()), null); 
			}
			handleMouse(obj);
		});		
		g.dispose();	
	};
	
	/**ACTIVE                                                                                
     *  - Only groups which are 'open' will be rendered.                                   
     *  - Camera scale and translation will be ignored.                                    
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.               
	 * @author 5som3 */
	private final WarpedAction render = () -> {
		Graphics2D g = getGraphics();
		setRenderHints(g);
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations();
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !camera.isClipped(obj)) {			
				at.setTransform(1.0, 0.0, 0.0, 1.0, obj.getPosition().x(), obj.getPosition().y());
				g.drawRenderedImage(obj.raster(), at);	
			}
			handleMouse(obj);
		});		
		g.dispose();	
	};
	
	/**ACTIVE_TRANSFORMED_SCALED                                                             
     *  - Only groups which are 'open' will be rendered.                                   
     *  - Camera scale and translation is applied.                                         
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.         
	 * @author 5som3*/
	private final WarpedAction renderTransformedScaled = () -> {
		Graphics2D g = getGraphics();
		setRenderHints(g);
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations(camera);
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !camera.isClipped(obj)) {			
				at.setTransform(camera.getZoom() * obj.getRenderScale(), 0.0, 0.0, camera.getZoom() * obj.getRenderScale(), obj.getRenderPosition().x(), obj.getRenderPosition().y());
				g.drawRenderedImage(obj.raster(), at);	
			}
			handleMouse(obj);
		});		
		g.dispose();		
	};
	
	/**TARGET_GROUPS                                                                         
     *  - Only groups which are targeted by this viewport will be rendered.                
     *  - Camera scale and translation will be ignored.                                    
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.                
	 * @author 5som3*/
	private final WarpedAction renderTargets = () -> {
		Graphics2D g = getGraphics();
		setRenderHints(g);
		for(int i = 0; i < targetGroups.size(); i++) {
			target.getGroup(targetGroups.get(i)).forEach(obj -> {
				obj.setRenderTransformations();
				if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
				if(obj.isVisible() && !camera.isClipped(obj)) {			
					at.setTransform(1.0, 0.0, 0.0, 1.0, obj.getPosition().x(), obj.getPosition().y());
					g.drawRenderedImage(obj.raster(), at);	
				}
				handleMouse(obj);
			});
		}
		g.dispose();	
	};
	
	/**TARGET_GROUPS_TRANSFORMED_SCALED                                                      
     *  - Only groups which are targeted by this viewport will be rendered.                
     *  - Camera scale and translation is applied.                                         
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.            
	 * @author 5som3*/
	private final WarpedAction renderTargetsTransformedScaled = () -> {
		Graphics2D g = getGraphics();
		setRenderHints(g);
		for(int i = 0; i < targetGroups.size(); i++) {
			target.getGroup(targetGroups.get(i)).forEach(obj -> {
				obj.setRenderTransformations(camera);
				if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
				if(obj.isVisible() && !camera.isClipped(obj)) {			
					at.setTransform(camera.getZoom() * obj.getRenderScale(), 0.0, 0.0, camera.getZoom() * obj.getRenderScale(), obj.getRenderPosition().x(), obj.getRenderPosition().y());
					g.drawRenderedImage(obj.raster(), at);	
				}
				handleMouse(obj);
			});
		}
		g.dispose();	
		
	};

	/**BAKED                                                                                 
     *  - Only groups which are targeted by this viewport will be rendered.                
     *  - Camera scale and translation will be ignored.                                    
     *  - Produces one frame at the time this function is called, does not update actively.
     *  - Render hints will be applied.   
     *   
     *BAKED_TRANSFORMED_SCALED                                                              
     *  - Only groups which are targeted by this viewport will be rendered.                
     *  - Camera scale and translation is applied.                                         
     *  - Produces one frame at the time this function is called, does not update actively.
     *  - Render hints will be applied.                
     * @author 5som3*/
	private final WarpedAction renderBaked = () -> {return;};

	
	

}
