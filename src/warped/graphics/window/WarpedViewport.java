/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.window;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import warped.WarpedProperties;
import warped.application.state.WarpedGroup;
import warped.application.state.WarpedManager;
import warped.application.state.WarpedObject;
import warped.functionalInterfaces.WarpedAction;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

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
	
	public  WarpedCamera camera = new WarpedCamera();

	private WarpedManager<?> target; 	
	private ArrayList<WarpedGroup<?>> targetGroups = new ArrayList<>();
		
	private boolean visible = true;
	private boolean interactive = true;
	
	private VectorI size 	  	 = new VectorI(); 
	private VectorI position 	= new VectorI();
	private VectorI cornerPoint = new VectorI();
	
	private BufferedImage buffer; //graphic input
	private BufferedImage raster;    //graphic output
	private BufferedImage[] rasterBuffer;
	private int bufferIndex = 0;
	
	private short fps = 0;

	public WarpedMouseEvent mouseEvent;
	private WarpedObject eventObject;
	private ArrayList<WarpedMouseEvent> mouseEvents = new ArrayList<>();;
	private ArrayList<WarpedObject> eventObjects = new ArrayList<>();
	
	//private ArrayList<WarpedAnimation> animations = new ArrayList<>();

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
	
	public enum RenderType {
		//ANIMATION,
		PRIMITIVE,
		PRIMITIVE_TRANSFORMED_SCALED,
		ACTIVE,
		ACTIVE_TRANSFORMED_SCALED,
		BAKED, 
		BAKED_TRANSFORMED_SCALED,
		TARGET_GROUPS,
		TARGET_GROUPS_TRANSFORMED_SCALED,
	}
	
	/**A new viewport with the specified parameters.
	 * @param name - the name of the viewport (not significant, mainly for debug purposes)
	 * @param target - The manager that the viewport should target.
	 * @param camera - The camera for the viewport to use
	 * @param size - the size of the viewPort
	 * @param position - the position of the viewPort in the window.
	 * @author 5som3*/
	public WarpedViewport(String name, WarpedManager<? extends WarpedObject> target, WarpedCamera camera, VectorI size, VectorI position) { /*GameContext must be defined at this point*/
		this.name = name;
		this.size = size;
		this.position = position;
		this.target = target;
		this.camera = camera;
		cornerPoint.set(position.x() + size.x(), position.y() + size.y());		

		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[WarpedWindow.BUFFER_SIZE];
		for(int i = 0; i < WarpedWindow.BUFFER_SIZE; i++) rasterBuffer[i] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		pushGraphics();
		setDefaultRenderHints();
		setRenderMethod(RenderType.ACTIVE);	
	}
	
	/**A new viewport with the specified parameters.
	 * @param name - the name of the viewport (not significant, mainly for debug purposes)
	 * @param target - The manager that the viewport should target.
	 * @param x - the x component of the viewport position in the window.
	 * @param y - the y component of the viewport position in the window.
	 * @param width - the width of the viewport.
	 * @param height - the height of the viewport.
	 * @author 5som3*/
	public WarpedViewport(String name, WarpedManager<?> target, int x, int y, int width, int height) {
		this.name = name;
		this.size = new VectorI(width, height);
		this.position = new VectorI(x, y); 
		this.target = target;
		this.camera = new WarpedCamera();
		cornerPoint.set(position.x() + size.x(), position.y() + size.y());		
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[WarpedWindow.BUFFER_SIZE];
		for(int i = 0; i < WarpedWindow.BUFFER_SIZE; i++) rasterBuffer[i] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		pushGraphics();
		setDefaultRenderHints();
		setRenderMethod(RenderType.ACTIVE);
	}
	
	/**A new viewport with the specified parameters.
	 * @param name - the name of the viewport (not significant, mainly for debug purposes)
	 * @param target - The manager that the viewport should target.
	 * @author 5som3*/
	public WarpedViewport(String name, WarpedManager<?> target) {
		this.name = name;
		this.size = new VectorI(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight());
		this.position = new VectorI(); 
		this.target = target;
		this.camera = new WarpedCamera();
		cornerPoint.set(position.x() + size.x(), position.y() + size.y());		
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[WarpedWindow.BUFFER_SIZE];
		for(int i = 0; i < WarpedWindow.BUFFER_SIZE; i++) rasterBuffer[i] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		pushGraphics();
		setDefaultRenderHints();
		setRenderMethod(RenderType.ACTIVE);
	}
	
	/**A new viewport with the specified parameters.
	 * @param name - the name of the viewport (not significant, mainly for debug purposes)
	 * @param target - the manager that the viewport should target.
	 * @param renderType - the type of render method for the viewport to use.  
	 * @author 5som3*/
	public WarpedViewport(String name, WarpedManager<?> target, RenderType renderType) {
		this.name = name;
		this.size = new VectorI(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight());
		this.position = new VectorI(); 
		this.target = target;
		this.camera = new WarpedCamera();
		cornerPoint.set(position.x() + size.x(), position.y() + size.y());		
		
		Console.ln("WarpedViewPort -> " + name + " Constructing..");
		
		rasterBuffer = new BufferedImage[WarpedWindow.BUFFER_SIZE];
		for(int i = 0; i < WarpedWindow.BUFFER_SIZE; i++) rasterBuffer[i] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		pushGraphics();
		setDefaultRenderHints();
		setRenderMethod(renderType);
	}

	/**The duration of the last update cycle.
	 * @author 5som3*/
	public long getUpdateDuration() {return updateDuration;}
	
	/**The number of frames generated since the last time getFPS() was called.
	 * @return short - the frame count
	 * @author 5som3*/
	public final short getFPS() {
		short val = fps;
		fps = 0;
		return val;
	}

	public final void clearMouseEvents() {
		mouseEvent.handle();
		for(int i = 0; i < mouseEvents.size(); i++) {
			mouseEvents.get(i).handle();
		}
		mouseEvents.clear();
		mouseEvent = null;
		eventObject = null;
	}
	
	/**For the currently targeted manager, set which groups should be viewed by this viewport.
	 * @param groups - a list of the groups within the current targeted manager to draw.
	 * @author SomeKid*/
	public final void setTargetGroups(WarpedGroup<?>... groups) {setTargetGroups(Arrays.asList(groups));}
	
	/**For the currently targeted manager, set which groups should be viewed by this viewport.
	 * @param groups - a list of the groups within the current targeted manager to draw.
	 * @author SomeKid*/
	public final void setTargetGroups(List<WarpedGroup<?>> groups) {
		targetGroups.clear();
		targetGroups = new ArrayList<>(groups);		
	}
	
	/**If the viewport has been baked with graphics they will be cleared.
	 * @implNote the viewport raster will be made completely transparent.
	 * @author 5som3*/
	public void clearBake() {
		if(renderType != RenderType.BAKED) {
			Console.err("WarpedViewport -> " + name + " -> viewport isn't baked, clearing bake is pointless");
			return;
		} else {
			Graphics g = getGraphics();
			g.dispose();
			pushGraphics();
		}
	}
	
	/**Sets the render method to Baked and renders the the currently targeted groups (if any).
	 * @param isScaledAndTransformed - if true the render method will scale and transform the image according to the current camera 2.
	 * @param groups - a list of the groups within the current targeted manager to bake.
	 * @apiNote baked viewports will only draw groups that have been targeted, it is independent if which groups are open / closed.
	 * @author SomeKid*/
	public void bakeGroups(boolean isScaledAndTransformed, WarpedGroup<?>... groups) {
		setTargetGroups(groups);
		bakeGroups(isScaledAndTransformed);
	}
	
	/**Sets the render method to Baked and renders the the currently targeted groups (if any).
	 * @param isScaledAndTransformed - if true the render method will scale and transform the image according to the current camera 2.
	 * @apiNote baked viewports will only draw groups that have been targeted, it is independent if which groups are open / closed.
	 * @author SomeKid*/
	public void bakeGroups(boolean isScaledAndTransformed) {		
		renderType = RenderType.BAKED;
		BufferedImage baked = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics2D g = baked.createGraphics();
		setRenderHints(g);
		
		if(isScaledAndTransformed) {	
			for(int i = 0; i < targetGroups.size(); i++) {
				targetGroups.get(i).forEach((obj) -> {
					obj.setRenderTransformations(camera);
					if(obj.isVisible() && !isClipped(obj)) {			
						at.setTransform(1.0, 0.0, 0.0, 1.0, obj.getPosition().x(), obj.getPosition().y());
						g.drawRenderedImage(obj.raster(), at);	
					}
				});
			}
		} else for(int i = 0; i < targetGroups.size(); i++) {
			targetGroups.get(i).forEach((obj) -> {
				if(obj.isVisible() && !isClipped(obj)) {			
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
	 * @returns boolean - If true the viewport will be rendered in the window.
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
	 * @apiNote    - Has mouse I/O
	 * @apiNote PRIMITIVE_TRANSFOREMD_SCALED
	 * @apiNote    - Only groups which are 'open' will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will not be applied. 
	 * @apiNote    - Has mouse I/O
	 * @apiNote ACTIVE 
	 * @apiNote    - Only groups which are 'open' will be rendered.
	 * @apiNote    - Camera scale and translation will be ignored.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote    - Has mouse I/O
	 * @apiNote ACTIVE_TRANSFORMED_SCALED
	 * @apiNote    - Only groups which are 'open' will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote    - Has mouse I/O
	 * @apiNote BAKED
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation will be ignored.
	 * @apiNote    - Produces one frame at the time this function is called, does not update actively.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote    - Has mouse I/O
	 * @apiNote BAKED_TRANSFORMED_SCALED
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Produces one frame at the time this function is called, does not update actively.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote    - Has mouse I/O
	 * @apiNote TARGET_GROUPS
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation will be ignored.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote    - Has mouse I/O
	 * @apiNote TARGET_GROUPS_TRANSFORMED_SCALED
	 * @apiNote    - Only groups which are targeted by this viewport will be rendered.
	 * @apiNote    - Camera scale and translation is applied.
	 * @apiNote    - Actively produces new frames at target 60fps.
	 * @apiNote    - Render hints will be applied.
	 * @apiNote    - Has mouse I/O
	 * @apiNote ANIMATION                                                                             
   	 * @apiNote    - This render method will not draw warpedObjects
   	 * @apiNote    - Renders from a separate arrayList of WarpedAnimation.
   	 * @apiNote    - Use this mode for a VFX viewport that will just display animations. 
   	 * @apiNote    - Warped Animations are removed when complete.  
   	 * @apiNote    - Does not have mouse I/O                                                                                
	 * @author 5som3*/
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
	
	/*Add an animation to be rendered in the viewport.
	 * @param animation - the animation to render.
	 * @apiNote The Viewport must be in animation mode. use setRenderMode(RenderType.ANIMATION);
	 * @apiNote Animations will be removed automatically from the viewport as they complete.
	 * @apiNote Animations will begin playing when they are added (if not playing already).
	 * @author 5som3
	public void addAnimation(WarpedAnimation animation) {
		if(renderType != RenderType.ANIMATION) {
			WarpedConsole.err("WarpedViewport -> " + name + " -> addAnimation() -> this viewport is not set to render animations, change render method before adding animations");
			return;
		} else animations.add(animation);
	}
	
	
	/**Remove all the animations (if any).
	 * @author 5som3
	public void clearAnimations() {
		if(renderType != RenderType.ANIMATION) {
			WarpedConsole.err("WarpedViewport -> " + name + " -> clearAnimations() -> this viewport is not set to render animations, change render method before clearing animations");
			return;
		} else animations.clear();
	}
	 * */
	
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

	/**Set the position of this viewport in the window.
	 * @param x - the x position in pixels.
	 * @param y - the y position in pixels.
	 * @apiNote The position is measured from the top left corner of the window to the top left corner of the viewport.
	 * @author 5som3*/
	public void setPosition(int x, int y) {
		this.position.set(x, y);
		cornerPoint.set(position.x() + size.x(), position.y() + size.y());		
	}
	
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
	
	/**The graphic output for this viewport
	 * @return BufferedImage - this image is the current frame of viewport at the time this function is called.
	 * @implNote You should not try to edit the viewport raster manually. 
	 * @implNote Editing this image will possibly cause momentary graphical artifacts.
	 * @author SomeKid*/
	protected BufferedImage raster() {return raster;}
		
	
	/**DO NOT CALL - this method is scheduled for automatic execution by the WarpedWindow.
	 * @implNote redraws the viewport based on the current target / targets.
	 * @author SomeKid*/
	protected final void update() {
		long cycleStartTime = System.nanoTime();
		camera.update();
		if(mouseEvents.size() > 0) {
			if(mouseEvent== null) mouseEvent = mouseEvents.getLast();
			mouseEvents.clear();
		}
		render();
		if(eventObjects.size() > 0) {
			if(eventObject == null) eventObject = eventObjects.getLast();
			for(int i = 0; i < eventObjects.size() - 1; i++) eventObjects.get(i).unhovered();
			eventObjects.clear();
		}
		fps++;
		updateDuration = System.nanoTime() - cycleStartTime;		
	}
	
	
	/**@implNote This function is called from the WarpedWindow class automatically when a mouse event occurs over it.
	 * @implNote This function may be called multiple times per frame, only the last mouse event of each frame is considered; all other events will be trashed.
	 * @author SomeKid */
	protected void MouseEvent(WarpedMouseEvent mouseEvent) {
		mouseEvent.updateTrace(this);
		mouseEvents.add(mouseEvent);
	}
	
	/**@implNote This function is called automatically from the WarpedWindow once at the end of each render cycle.
	 * @author SomeKid*/
	protected void dispatchMouseEvents() {		
		if(mouseEvent == null || eventObject == null || mouseEvent.isHandled()) {
			mouseEvent  = null;
			eventObject = null;
			return;
		} else if(eventObject.isInteractive()){			
			eventObject.hovered();		
			eventObject.mouseEvent(mouseEvent);
			mouseEvent.handle();
		} else {
			mouseEvent.handle();
			eventObject.unhovered();
		} 
		mouseEvent  = null;
		eventObject = null;
	}
	
	
	/**Checks if the renderObject interacts with the mouseEvent, if the renderObject is added to the eventObjects shortList.
	 * The last object in eventObjects will be the object that actually receives a mouse event when dispactMouseEvent() triggers. 
	 * 	@author 5som3*/
	protected void handleMouse(WarpedObject renderObject) {
		if(mouseEvent == null) return;
		else if(mouseEvent.isHandled()) return;
		if(renderObject.isInteractive() && renderObject.isVisible() && isOverObject(renderObject)) eventObjects.add(renderObject);
		else renderObject.unhovered();
	}
	
	/**Check if the specified object is in the bounds of the viewport*/
	private boolean isClipped(WarpedObject object) {
		if(object.getRenderPosition().x() + object.getRenderSize().x() < position.x()) return true; // outside left bound
		if(object.getRenderPosition().y() + object.getRenderSize().y() < position.y()) return true; // outside top bound
		if(object.getRenderPosition().x() > cornerPoint.x()) return true; // outside right bound
		if(object.getRenderPosition().y() > cornerPoint.y()) return true; // outside bottom bound
		return false;
	}
	
	/**Check if the mouse event is over the specified object*/
	protected boolean isOverObject(WarpedObject renderObject){
		Point point = mouseEvent.getPointRelativeToViewPort();
		
		int traceX = (int)(point.x - renderObject.getRenderPosition().x());
		int traceY = (int)(point.y - renderObject.getRenderPosition().y());
		
		if(traceX < 0 || traceY < 0 || traceX >= renderObject.getRenderSize().x() || traceY >= renderObject.getRenderSize().y()) return false;
		
		int pixelX = (int)(traceX / camera.getZoom());
		int pixelY = (int)(traceY / camera.getZoom());
		
		if(pixelX < 0 || pixelY < 0 || pixelX >= renderObject.raster().getWidth() || pixelY >= renderObject.raster().getHeight()) return false;

		int col = renderObject.raster().getRGB(pixelX, pixelY);
		int alpha = 0xFF & (col >> 24);
		if(alpha > WarpedProperties.ALPHA_THRESHOLD) {
			return true;
		} else return false;	
	}
	

	/**Get the graphics for this viewport.
	 * @return Graphics2D - a graphics interface with next buffer in the viewport.
	 * @implNote any changes made to the graphics will not not be visible raster is set to the new buffer.
	 * @author SomeKid*/
	private final Graphics2D getGraphics() {
		Graphics2D g2d = buffer.createGraphics();
		g2d.setComposite(UtilsImage.clearComposite);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setComposite(UtilsImage.drawComposite);
		return g2d;
	}
	
	private final void pushGraphics() {
		raster = rasterBuffer[bufferIndex];
		bufferIndex++;
		if(bufferIndex == WarpedWindow.BUFFER_SIZE) bufferIndex = 0;
		buffer = rasterBuffer[bufferIndex];
	}
	
	/** Renders the viewport based on the set method and set's the output raster to the new buffer
	 * @author SomeKid*/
	private void render() {renderMethod.action();}
	
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

	
	/**ANIMATION                                                                             
   	 * - This render method will not draw warpedObjects
   	 * - Renders from a separate arrayList of WarpedAnimation.
   	 * - Use this mode for a VFX viewport that will just display animations. 
   	 * - Warped Animations are removed when complete.  
   	 * - Does not have mouse I/O                                                                                
	 * @author 5som3
	private final WarpedAction animation = () -> {
		Graphics2D g = getGraphics();
		for(int i = 0; i < animations.size(); i++) {
			WarpedAnimation a = animations.get(i);
			if(a.isComplete() || !a.isAlive()) {
				animations.remove(i);
				i--;
			}
			else g.drawImage(a.raster(), a.x(), a.y(), a.getWidth(), a.getHeight(), null);			
		}
		g.dispose();
		pushGraphics();
	};
	 * */
	
	
	/**PRIMITIVE                                                                             
   	 * - This is the simplest and fastest render method.                         
   	 * - Only groups which are 'open' will be rendered.                                   
   	 * - Camera scale and translation will be ignored.                                    
   	 * - Actively produces new frames at target 60fps.                                    
   	 * - Render Hints will not be applied.     
   	 * - Has mouse I/O                                                                                    
	 * @author 5som3*/
	private final WarpedAction primitive = () -> {
		Graphics2D g = getGraphics();
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations();
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !isClipped(obj)) {			
				g.drawImage(obj.raster(), (int)(obj.getRenderPosition().x()),(int)(obj.getRenderPosition().y()), (int)(obj.getRenderSize().x()),(int)(obj.getRenderSize().y()), null); 
			}
			handleMouse(obj);
		});		
		g.dispose();
		pushGraphics();
	};
	
	/**PRIMITIVE_TRANSFOREMD_SCALED                                                          
	 *   - Only groups which are 'open' will be rendered.                                   
	 *   - Camera scale and translation is applied.                                         
     *   - Actively produces new frames at target 60fps.                                    
     *   - Render hints will not be applied.      
     *   - Has mouse I/O           
     * @author 5som3*/
	private final WarpedAction primitiveTransformedScaled = () -> {
		Graphics2D g = getGraphics();
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations(camera);
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !isClipped(obj)) {			
				g.drawImage(obj.raster(), (int)(obj.getRenderPosition().x()),(int)(obj.getRenderPosition().y()), (int)(obj.getRenderSize().x()),(int)(obj.getRenderSize().y()), null); 
			}
			handleMouse(obj);
		});		
		g.dispose();
		pushGraphics();
	};
	
	/**ACTIVE                                                                                
     *  - Only groups which are 'open' will be rendered.                                   
     *  - Camera scale and translation will be ignored.                                    
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.      
     *  - Has mouse I/O         
	 * @author 5som3 */
	private final WarpedAction render = () -> {
		Graphics2D g = getGraphics();
				
		setRenderHints(g);
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations();
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !isClipped(obj)) {			
				at.setTransform(1.0, 0.0, 0.0, 1.0, obj.x(), obj.y());
				g.drawRenderedImage(obj.raster(), at);	
			}
			handleMouse(obj);
		});		
		g.dispose();	
		pushGraphics();
	};
	
	/**ACTIVE_TRANSFORMED_SCALED                                                             
     *  - Only groups which are 'open' will be rendered.                                   
     *  - Camera scale and translation is applied.                                         
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.     
     *  - Has mouse I/O    
	 * @author 5som3*/
	private final WarpedAction renderTransformedScaled = () -> {
		Graphics2D g = getGraphics();
		setRenderHints(g);
		target.forEachActiveGroup(obj -> {
			obj.setRenderTransformations(camera);
			if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
			if(obj.isVisible() && !isClipped(obj)) {			
				at.setTransform(camera.getZoom() * obj.getRenderScale(), 0.0, 0.0, camera.getZoom() * obj.getRenderScale(), obj.getRenderPosition().x(), obj.getRenderPosition().y());
				g.drawRenderedImage(obj.raster(), at);	
			}
			handleMouse(obj);
		});		
		g.dispose();
		pushGraphics();
	};
	
	/**TARGET_GROUPS                                                                         
     *  - Only groups which are targeted by this viewport will be rendered.                
     *  - Camera scale and translation will be ignored.                                    
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.        
     *  - Has mouse I/O        
	 * @author 5som3*/
	private final WarpedAction renderTargets = () -> {
		Graphics2D g = getGraphics();
		setRenderHints(g);
		for(int i = 0; i < targetGroups.size(); i++) {
			targetGroups.get(i).forEach(obj -> {
				obj.setRenderTransformations();
				if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
				if(obj.isVisible() && !isClipped(obj)) {			
					at.setTransform(1.0, 0.0, 0.0, 1.0, obj.getPosition().x(), obj.getPosition().y());
					g.drawRenderedImage(obj.raster(), at);	
				}
				handleMouse(obj);
			});
		}
		g.dispose();
		pushGraphics();
	};
	
	/**TARGET_GROUPS_TRANSFORMED_SCALED                                                      
     *  - Only groups which are targeted by this viewport will be rendered.                
     *  - Camera scale and translation is applied.                                         
     *  - Actively produces new frames at target 60fps.                                    
     *  - Render hints will be applied.     
     *  - Has mouse I/O       
	 * @author 5som3*/
	private final WarpedAction renderTargetsTransformedScaled = () -> {
		Graphics2D g = getGraphics();
		setRenderHints(g);
		for(int i = 0; i < targetGroups.size(); i++) {
			targetGroups.get(i).forEach(obj -> {
				obj.setRenderTransformations(camera);
				if(camera.isTracking() && obj.isEqualTo(camera.getTarget())) camera.updateTracking(obj);		
				if(obj.isVisible() && !isClipped(obj)) {			
					at.setTransform(camera.getZoom() * obj.getRenderScale(), 0.0, 0.0, camera.getZoom() * obj.getRenderScale(), obj.getRenderPosition().x(), obj.getRenderPosition().y());
					g.drawRenderedImage(obj.raster(), at);	
				}
				handleMouse(obj);
			});
		}
		g.dispose();	
		pushGraphics();
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
