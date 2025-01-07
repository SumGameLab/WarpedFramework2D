/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import warped.WarpedFramework2D;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIShape;
import warped.application.gui.GUIToggle;
import warped.application.gui.textBox.GUITextBoxLined;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.CameraManager;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.audio.FrameworkAudio;
import warped.graphics.camera.WarpedCameraType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.window.WarpedViewport;
import warped.graphics.window.WarpedWindow;
import warped.user.WarpedUserInput;
import warped.user.keyboard.WarpedKeyboard;
import warped.user.mouse.WarpedMouse;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class AssemblyFrameworkInspector extends WarpedAssembly {		
		
	public enum FrameworkInspectorPage {
		
		WARPED_FRAMEWORK,
		GAME_STATE,
		
		SCREEN,
		AUDIO,
		
		CAMERA,
		KEYBOARD,
		MOUSE,
		
		OBJECT,
		ENTITIE,
		DEPTH_FIELD,
		TILE,
		ITEM,
		GUI;
		
		private static Map<Integer, FrameworkInspectorPage> map = new HashMap<>();
		static {
	        for (FrameworkInspectorPage type : FrameworkInspectorPage.values()) {
	            map.put(type.ordinal(), type);
	        }
	    }
		public static FrameworkInspectorPage get(int index) {
			if(index < 0 ) {
				Console.err("FrameworkInspectorPage -> get() -> index is less than 0 : " + index);
				return null;
			}
			if(index >= map.size()) {
				Console.err("FrameworkInspectorPage -> get() -> index is larger than map size : " + index);
				return null;
			}
			if(map.get(index) == null) {
				Console.err("FrameworkInspectorPage -> get() -> map contains a null value at index : " + index);
				return null;
			}
		    return (FrameworkInspectorPage) map.get(index);
		}
		
		public static FrameworkInspectorPage getPreviousPage(FrameworkInspectorPage managerType) {
			if(managerType == null) {
				Console.err("FrameworkInspectorPage -> getPreviousPage() -> passed null manager Type");
				return get(1);
			}
			int index = managerType.ordinal() - 1;
			if(index < 0) index = size() - 1;
			return get(index);
		}
		
		public static FrameworkInspectorPage getNextPage(FrameworkInspectorPage managerType) {
			if(managerType == null) {
				Console.err("FrameworkInspectorPage -> getNextPage() -> passed null manager Type");
				return get(1);
			}
			int index = managerType.ordinal() + 1; 		
			if( index >= map.size()) index = 0;
			return get(index);
		}
		
		
		public static int size() {return map.size();}
		
		
	}

	private boolean update = false;
	
	private FrameworkInspectorPage page = FrameworkInspectorPage.WARPED_FRAMEWORK;

	
	private GUIButton title    = new GUIButton("  WarpedFramework2D  ");
	private GUIButton next     = new GUIButton(FrameworkSprites.standardIcons.getSprite(6, 4));
	private GUIButton previous = new GUIButton(FrameworkSprites.standardIcons.getSprite(8, 4));
	private GUIToggle refresh  = new GUIToggle(FrameworkSprites.standardIcons.getSprite(4, 4));
	private GUIButton close    = new GUIButton(FrameworkSprites.standardIcons.getSprite(2, 4)); 
	
	private GUIButton smallRes = new GUIButton("720p");
	private GUIButton normalRes = new GUIButton("1080p");
	private GUIButton largeRes = new GUIButton("4K");
	
	private GUITextBoxLined managerText  = new GUITextBoxLined();
	
	private int borderThickness = 3;
	
	private GUIShape background = new GUIShape();
	private Rectangle borderRect = new Rectangle(0, 0, 400, 600);
	private Rectangle backgroundRect = new Rectangle(borderThickness, borderThickness, borderRect.width - borderThickness * 2, borderRect.height - borderThickness * 2);
	
	private Vec2d closeButtonOffset    = new Vec2d(borderRect.width	     - 30 - borderThickness, 0);
	private Vec2d refreshButtonOffset  = new Vec2d(closeButtonOffset.x   - 30, 0);
	private Vec2d nextButtonOffset     = new Vec2d(refreshButtonOffset.x - 30, 0);
	private Vec2d previousButtonOffset = new Vec2d(nextButtonOffset.x    - 30, 0);
	private Vec2d managerTextOffset    = new Vec2d(5, 35);

	private GUIShape screenParameters = new GUIShape();	
	private Rectangle v_crossHair = new Rectangle((int)WarpedWindow.center.x - 5, (int)WarpedWindow.center.y - 1, 10, 2);
	private Rectangle h_crossHair = new Rectangle((int)WarpedWindow.center.x - 1, (int)WarpedWindow.center.y - 5, 2, 10);
	private Rectangle top 	 = new Rectangle(0,0, WarpedWindow.width, 2);
	private Rectangle bottom = new Rectangle(0, WarpedWindow.height - 2, WarpedWindow.width, 2);
	private Rectangle left 	 = new Rectangle(0,0, 2, WarpedWindow.height);
	private Rectangle right  = new Rectangle(WarpedWindow.width - 2, 0, 2, WarpedWindow.height);

	public AssemblyFrameworkInspector(WarpedManagerType type) {
		super(type);		
		screenParameters.addRectangle(v_crossHair, Color.RED);
		screenParameters.addRectangle(h_crossHair, Color.RED);
		screenParameters.addRectangle(top,    Color.YELLOW);
		screenParameters.addRectangle(bottom, Color.YELLOW);
		screenParameters.addRectangle(left,   Color.YELLOW);
		screenParameters.addRectangle(right,  Color.YELLOW);
		screenParameters.updateGraphics();
		
		close.setToolTip("Close");
		refresh.setToolTip("Refresh");
		next.setToolTip("Next");
		previous.setToolTip("Previous");
	}
	
	@Override
	protected void offsetAssembly() {
		close.setPositionOffset(closeButtonOffset);
		next.setPositionOffset(nextButtonOffset);
		previous.setPositionOffset(previousButtonOffset);
		
		managerText.setPositionOffset(managerTextOffset);
		refresh.setPositionOffset(refreshButtonOffset);
		
		smallRes.setPositionOffset(50, 150);
		normalRes.setPositionOffset(50, 190);
		largeRes.setPositionOffset(50, 230);
		
		close.offsetPosition();
		next.offsetPosition();
		previous.offsetPosition();
		managerText.offsetPosition();
		refresh.offsetPosition();
		
		smallRes.offsetPosition();
		normalRes.offsetPosition();
		largeRes.offsetPosition();
	}

	
	@Override
	protected void defineAssembly() {
		background.addRectangle(borderRect, Color.BLACK);
		background.addRectangle(backgroundRect, Colour.GREY_DARK);
		background.updateGraphics();
		
		title.setButtonSize(borderRect.width - borderThickness - (30 * 4), 30);
		title.draggable();
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				if(!i.isEqualTo(screenParameters)) {
					i.setPosition(title.getPosition().x, title.getPosition().y);
					i.offsetPosition();
				}				
			});			
		});
		
		close.setReleasedAction(() -> {close();});
		close.setReleasedSFX(FrameworkAudio.defaultClose);
		next.setReleasedAction(() -> {
			page = FrameworkInspectorPage.getNextPage(page);
			updateTextBox();
			});
		previous.setReleasedAction(() -> {
			page = FrameworkInspectorPage.getPreviousPage(page);
			updateTextBox();
			});
		  
		
		managerText.ateractive();
		managerText.resizeable();
		updateTextBox();	
		
		smallRes.setReleasedAction(() -> {WarpedFramework2D.getWindow().setWindowResolution(1280, 720);});
		normalRes.setReleasedAction(() -> {WarpedFramework2D.getWindow().setWindowResolution(1920, 1080);});
		largeRes.setReleasedAction(() -> {WarpedFramework2D.getWindow().setWindowResolution(2560, 1440);});

		refresh.setUpdateMidAction(g -> {updateTextBox();});		
	}

	private void updateTextBox() {
		if(WarpedState.isPaused()) {
			Console.err("AssemblyFrameworkInspector -> updateTextbox() -> state is paused");
			return;
		}
		managerText.clearText();
		int line = 0;
		if(page != FrameworkInspectorPage.SCREEN) {
			smallRes.invisible();
			normalRes.invisible();
			largeRes.invisible();
		}
		switch(page) {
		case WARPED_FRAMEWORK:
			managerText.putTextLine(line, "Threads - Update Duration : (target frequency, ms per cycle)", Color.YELLOW); line += 2;
			managerText.putTextLine(line, "---WarpedState---", Color.YELLOW);	line ++;
			managerText.putTextLine(line, "Active  : (58 per second, " + UtilsMath.round(WarpedState.getActiveCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.putTextLine(line, "Mid     : (1 per second,  " + UtilsMath.round(WarpedState.getMidCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.putTextLine(line, "Slow    : (1 per minute,  " + UtilsMath.round(WarpedState.getSlowCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.putTextLine(line, "Passive : (1 per hour,    " + UtilsMath.round(WarpedState.getPassiveCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line += 2;
			
			managerText.putTextLine(line, "---Window---", Color.YELLOW); line ++;
			managerText.putTextLine(line, "Update : (60 per second, " + UtilsMath.round(WarpedWindow.getUpdateDuration() * 0.000001, 3) + ")"); line++;

			managerText.putTextLine(line, "---User Input : (10 per second, " + UtilsMath.round(WarpedUserInput.getUpdateDuration() * 0.000001, 3) + ")", Color.YELLOW); line += 2;
			
			managerText.putTextLine(line, "---Warped Viewports---", Color.YELLOW); line++;
			for(int i = 0; i < WarpedWindow.getViewPorts().length; i++) {
				WarpedViewport viewport = WarpedWindow.getViewPort(i);
				managerText.putTextLine(line, viewport.getName() +  " viewPort : (60 per second, " + UtilsMath.round(viewport.getUpdateDuration() * 0.000001, 3) + ")", Color.WHITE); line++;
			}
			
			break;
			
		case GAME_STATE:
			managerText.putTextLine(line, "Game State", Color.YELLOW); 												line += 2;
			managerText.putTextLine(line, "Game Object Count :  " + WarpedState.getGameObjectCount()); 				line ++;
			managerText.putTextLine(line, "Active Game Object Count :  " + WarpedState.getActiveGameObjectCount());	line += 2;
			
			//managerText.putTextLine(line, "Context Coordinate Data  :  " + WarpedCoordinateSystem.getCoordinateTypeString()); 	line++;
			//managerText.putTextLine(line, "Coordinate Scalar : " + WarpedCoordinateSystem.getCoordinateScalarString()); 		line++;
			break;
			
		case SCREEN:
			managerText.putTextLine(line, "Screen", Color.YELLOW); 										line += 2;
			managerText.putTextLine(line, "Width : " +  WarpedWindow.width); 							line ++;
			managerText.putTextLine(line, "Height : " + WarpedWindow.height); 							line ++;
			managerText.putTextLine(line, "Center : " + WarpedWindow.center.getString()); 				line ++;
			managerText.putTextLine(line, "View Ports   :  " + WarpedWindow.getViewPortCount()); 		line ++;
		
			smallRes.visible();
			normalRes.visible();
			largeRes.visible();
			break;
			
		case AUDIO:
			managerText.putTextLine(line, "Audio", Color.YELLOW); 											line += 2;
			//managerText.putTextLine(line, "Active Sound Effects : " + WarpedAudio.getSoundEffectCount()); 	line += 2; 
			/*
			ArrayList<SoundEffectType> fx = WarpedAudio.getActiveSoundEffects();
			for(int i = 0; i < fx.size(); i++) {
				managerText.putTextLine(line, fx.get(i).toString()); 										line++;
			}
			*/
			break;
			
		case CAMERA:
			managerText.putTextLine(line, "Cameras", Color.YELLOW); 										line += 2;
			ArrayList<WarpedCameraType> activeCameras = CameraManager.getActiveCameras();
			for(int i = 0; i < activeCameras.size(); i++) {
				WarpedCameraType type = activeCameras.get(i);
				managerText.putTextLine(line, type.toString()); line++;
				managerText.putTextLine(line, "Position    :  " + CameraManager.getCamera(type).getPosition().getString()); line++;
				managerText.putTextLine(line, "(C1), (C2)  :  " + CameraManager.getCamera(type).getC1().getString() + ", " + CameraManager.getCamera(type).getC2().getString()); line++;
				managerText.putTextLine(line, "Zoom        :  " + CameraManager.getCamera(type).getZoom()); 	 line++;
				managerText.putTextLine(line, "Zoom Speed  :  " + CameraManager.getCamera(type).getZoomSpeed()); line += 2;				
			}			
			break;
			
		case KEYBOARD:
			managerText.putTextLine(line, "Keyboard", Color.YELLOW); 													line += 2;
			managerText.putTextLine(line, "Keyboard Controller :  " + WarpedKeyboard.getActiveController().getName());  line++;
			managerText.putTextLine(line, "Key Presses  :  " + WarpedKeyboard.getPressesString());                      line++;
			managerText.putTextLine(line, "Key Releases :  " + WarpedKeyboard.getReleaseString());                      line++;
			managerText.putTextLine(line, "Key Bindings   :  " + WarpedKeyboard.getKeyBindingsCount());					line++;
			break;                  
			                        
		case MOUSE:                 
			managerText.putTextLine(line, "Mouse", Color.YELLOW); 																			 line += 2;
			managerText.putTextLine(line, "Mouse in Window  :  " + WarpedMouse.isInWindow());												 line++;
			managerText.putTextLine(line, "Mouse dragging   :  " + WarpedMouse.isDragging());                                                line++;
			managerText.putTextLine(line, "Mouse is focused :  " + WarpedMouse.isFocused());                                                 line++;
			managerText.putTextLine(line, "Mouse position (x, y)  :  (" + WarpedMouse.getPoint().x + ", " + WarpedMouse.getPoint().y + ")"); line++; 
			break;                  
			
		case OBJECT:           
			managerText.putTextLine(line,  "Manager : Object", Color.YELLOW); 																			  line += 2;
			managerText.putTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getGroupCount()); 			  line++;
			managerText.putTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getActiveGroupCount());	  line++;	
			managerText.putTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getGameObjectCount());       line++;
			managerText.putTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getActiveGameObjectCount()); line++;
			break;  	
			
		case DEPTH_FIELD:           
			managerText.putTextLine(line,  "Manager : Depth Field", Color.YELLOW); 																			  line += 2;
			managerText.putTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getGroupCount()); 			  line++;
			managerText.putTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getActiveGroupCount());	  line++;	
			managerText.putTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getGameObjectCount());       line++;
			managerText.putTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getActiveGameObjectCount()); line++;
			break;  
			
		case ENTITIE:
			managerText.putTextLine(line,  "Manager : Entitie", Color.YELLOW); 																				  line += 2;
			managerText.putTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getGroupCount());                line++;
			managerText.putTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getActiveGroupCount());          line++;
			managerText.putTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getGameObjectCount());           line++;
			managerText.putTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getActiveGameObjectCount());     line++;
			break;
			
		case GUI:                 
			managerText.putTextLine(line,  "Manager : GUI", Color.YELLOW); 																					  line += 2;
			managerText.putTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.GUI).getGroupCount());                    line++;
			managerText.putTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.GUI).getActiveGroupCount());              line++;
			managerText.putTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.GUI).getGameObjectCount());               line++;
			managerText.putTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.GUI).getActiveGameObjectCount());         line++;
			break; 
			
		case TILE:         
			managerText.putTextLine(line,  "Manager : Tile", Color.YELLOW); 																				  line += 2;
			managerText.putTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.TILE).getGroupCount());                   line++;
			managerText.putTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.TILE).getActiveGroupCount());             line++;
			managerText.putTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.TILE).getGameObjectCount());              line++;
			managerText.putTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.TILE).getActiveGameObjectCount());        line++;
			break;   
			
		case ITEM:                                            
			managerText.putTextLine(line,  "Manager : Item", Color.YELLOW); 																				  line += 2;
			managerText.putTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.ITEM).getGroupCount());                   line++;
			managerText.putTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.ITEM).getActiveGroupCount());             line++;
			managerText.putTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.ITEM).getGameObjectCount());              line++;
			managerText.putTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.ITEM).getActiveGameObjectCount());        line++;
			break;
			
		default:
			Console.err("InspectorManager -> initializeTextBox() -> invalid case : " + page);
			return;		
		}
		managerText.updateTextBox();		
	}
	
	@Override
	protected void addAssembly() {
		addMember(background);
		addMember(title);
		addMember(close);
		addMember(next);
		addMember(previous);
		addMember(managerText);
		addMember(refresh);
		addMember(screenParameters);
		addMember(smallRes);
		addMember(normalRes);
		addMember(largeRes);
	}

	@Override
	protected void updateAssembly() {return;}
	
}
