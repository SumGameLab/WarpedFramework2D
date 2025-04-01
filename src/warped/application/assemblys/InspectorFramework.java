/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import warped.WarpedFramework2D;
import warped.application.actionWrappers.ActionOption;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIPopOutMenu;
import warped.application.gui.GUIRadioButtons;
import warped.application.gui.GUIShape;
import warped.application.gui.GUITextBoxLined;
import warped.application.gui.GUIToggle;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.CameraManager;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.audio.FrameworkAudio;
import warped.graphics.camera.WarpedCameraType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.graphics.window.WarpedViewport;
import warped.graphics.window.WarpedWindow;
import warped.user.WarpedUserInput;
import warped.user.keyboard.WarpedKeyboard;
import warped.user.mouse.WarpedMouse;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class InspectorFramework extends WarpedAssembly {		
		
	public enum FrameworkInspectorPage {
		
		FRAMEWORK_BUTTONS,
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
	
	private FrameworkInspectorPage page = FrameworkInspectorPage.FRAMEWORK_BUTTONS;

	private int borderThickness = 3;
	private GUIShape background = new GUIShape(410, 620);
	private Rectangle borderRect = new Rectangle(0, 0, 410, 620);
	private Rectangle backgroundRect = new Rectangle(borderThickness, borderThickness, borderRect.width - borderThickness * 2, borderRect.height - borderThickness * 2);
	
	private GUIButton title     = new GUIButton(borderRect.width - borderThickness - (30 * 4) - 90, 30, "  WarpedFramework2D  ");
	private GUIButton next      = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_RIGHT));
	private GUIButton previous  = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_LEFT));
	private GUIToggle refresh   = new GUIToggle(FrameworkSprites.getStandardIcon(StandardIcons.REFRESH));
	private GUIButton close     = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIPopOutMenu pages = new GUIPopOutMenu("page");
	
	private GUIRadioButtons resolution = new GUIRadioButtons(Arrays.asList("720p", "1080p", "1440p"), 
			Arrays.asList(() -> {WarpedFramework2D.getWindow().setWindowResolution(1280, 720);},
					() -> {WarpedFramework2D.getWindow().setWindowResolution(1920, 1080);},
					() -> {WarpedFramework2D.getWindow().setWindowResolution(2560, 1440);}));
	private GUIButton exit		= new GUIButton("exit");

	
	
	private GUITextBoxLined managerText  = new GUITextBoxLined(400, 580);	
	
	private GUIShape screenParameters = new GUIShape(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight());	
	private Rectangle v_crossHair = new Rectangle((int)WarpedWindow.getCenterX() - 5, (int)WarpedWindow.getCenterY() - 1, 10, 2);
	private Rectangle h_crossHair = new Rectangle((int)WarpedWindow.getCenterX() - 1, (int)WarpedWindow.getCenterY() - 5, 2, 10);
	private Rectangle top 	 = new Rectangle(0,0, WarpedWindow.getWindowWidth(), 2);
	private Rectangle bottom = new Rectangle(0, WarpedWindow.getWindowHeight() - 2, WarpedWindow.getWindowWidth(), 2);
	private Rectangle left 	 = new Rectangle(0,0, 2, WarpedWindow.getWindowHeight());
	private Rectangle right  = new Rectangle(WarpedWindow.getWindowWidth() - 2, 0, 2, WarpedWindow.getWindowHeight());

	public InspectorFramework(WarpedManagerType type) {
		super(type);		
	}
	
	@Override
	protected void defineAssembly() {
		close.setOffset(borderRect.width- 30 - borderThickness, 0);
		refresh.setOffset(close.getOffsetX() - 30, 0);
		next.setOffset(refresh.getOffsetX() - 30, 0);
		previous.setOffset(next.getOffsetX() - 30, 0);
		pages.setOffset(previous.getOffsetX() - 90, 0);
		
		managerText.setOffset(5, 35);
		
		resolution.setOffset(50, 150);
		exit.setOffset(50, 270);
		
		
		close.offset(title);
		next.offset(title);
		previous.offset(title);
		managerText.offset(title);
		refresh.offset(title);
		pages.offset(title);
		
		resolution.offset(title);
		exit.offset(title);

		
		screenParameters.addRectangle(v_crossHair, Color.RED);
		screenParameters.addRectangle(h_crossHair, Color.RED);
		screenParameters.addRectangle(top,    Color.YELLOW);
		screenParameters.addRectangle(bottom, Color.YELLOW);
		screenParameters.addRectangle(left,   Color.YELLOW);
		screenParameters.addRectangle(right,  Color.YELLOW);
		
		close.setToolTip("Close");
		refresh.setToolTip("Refresh");
		next.setToolTip("Next");
		previous.setToolTip("Previous");
		managerText.setToolTip("Refresh to update");
		
		
		background.addRectangle(borderRect, Color.BLACK);
		background.addRectangle(backgroundRect, Colour.GREY_DARK);
		
		exit.setReleasedAction(mouseE -> {WarpedFramework2D.stop();});
		resolution.setAxis(AxisType.HORIZONTAL);
		
		title.draggable();
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				if(!i.isEqualTo(screenParameters)) {
					i.offset(title);
				}				
			});			
		});
		
		ArrayList<ActionOption> pageOptions = new ArrayList<>();
		pageOptions.add(new ActionOption(FrameworkInspectorPage.AUDIO.toString(), () -> {page = FrameworkInspectorPage.AUDIO; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.CAMERA.toString(), () -> {page = FrameworkInspectorPage.CAMERA; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.DEPTH_FIELD.toString(), () -> {page = FrameworkInspectorPage.DEPTH_FIELD; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.ENTITIE.toString(), () -> {page = FrameworkInspectorPage.ENTITIE; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.FRAMEWORK_BUTTONS.toString(), () -> {page = FrameworkInspectorPage.FRAMEWORK_BUTTONS; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.GAME_STATE.toString(), () -> {page = FrameworkInspectorPage.GAME_STATE; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.GUI.toString(), () -> {page = FrameworkInspectorPage.GUI; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.ITEM.toString(), () -> {page = FrameworkInspectorPage.ITEM; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.KEYBOARD.toString(), () -> {page = FrameworkInspectorPage.KEYBOARD; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.MOUSE.toString(), () -> {page = FrameworkInspectorPage.MOUSE; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.OBJECT.toString(), () -> {page = FrameworkInspectorPage.OBJECT; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.SCREEN.toString(), () -> {page = FrameworkInspectorPage.SCREEN; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.TILE.toString(), () -> {page = FrameworkInspectorPage.TILE; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.WARPED_FRAMEWORK.toString(), () -> {page = FrameworkInspectorPage.WARPED_FRAMEWORK; updatePage();}));
		pages.setOptions(pageOptions);
		
		close.setReleasedAction(
				mouseE -> {
					close();
					WarpedState.toolTip.close();
				});
		close.setReleasedSFX(FrameworkAudio.defaultClose);
		next.setReleasedAction(mouseE -> {
			page = FrameworkInspectorPage.getNextPage(page);
			updatePage();
			});
		previous.setReleasedAction(mouseE -> {
			page = FrameworkInspectorPage.getPreviousPage(page);
			updatePage();
			});
		  
		
		managerText.ateractive();
		updatePage();	
		
		
		refresh.setRepeatingAction(() -> {updatePage();});		
	}

	public void inspectTimers() {
		page = FrameworkInspectorPage.WARPED_FRAMEWORK;
		updatePage();
		refresh.setToggle(true);
		open();
	}
	
	private void showButtons() {
		resolution.setVisible(true);
		exit.setVisible(true);
	}
	
	private void hideButtons() {
		resolution.setVisible(false);
		exit.setVisible(false);
	}
	
	private void updatePage() {
		if(WarpedState.isPaused()) {
			Console.err("AssemblyFrameworkInspector -> updateTextbox() -> state is paused");
			return;
		}
		
		managerText.clearTextLines();
		int line = 0;
		if(page != FrameworkInspectorPage.FRAMEWORK_BUTTONS) hideButtons();
		
		switch(page) {
		case FRAMEWORK_BUTTONS:
			showButtons();
			break;
		case WARPED_FRAMEWORK:
			managerText.setTextLine(line, "Threads - Update Duration : (target frequency, ms per cycle)", Color.YELLOW); line += 2;
			managerText.setTextLine(line, "---WarpedState---", Color.YELLOW);	line ++;
			managerText.setTextLine(line, "Active  : (58 per second, " + UtilsMath.round(WarpedState.getActiveCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.setTextLine(line, "Mid     : (1 per second,  " + UtilsMath.round(WarpedState.getMidCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.setTextLine(line, "Slow    : (1 per minute,  " + UtilsMath.round(WarpedState.getSlowCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.setTextLine(line, "Passive : (1 per hour,    " + UtilsMath.round(WarpedState.getPassiveCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line += 2;
			            
			managerText.setTextLine(line, "---Window---", Color.YELLOW); line ++;
			managerText.setTextLine(line, "Update : (60 per second, " + UtilsMath.round(WarpedWindow.getUpdateDuration() * 0.000001, 3) + ")"); line++;
                        
			managerText.setTextLine(line, "---User Input : (10 per second, " + UtilsMath.round(WarpedUserInput.getUpdateDuration() * 0.000001, 3) + ")", Color.YELLOW); line += 2;
			            
			managerText.setTextLine(line, "---Warped Viewports---", Color.YELLOW); line++;
			for(int i = 0; i < WarpedWindow.getViewPorts().length; i++) {
				WarpedViewport viewport = WarpedWindow.getViewPort(i);
				managerText.setTextLine(line, viewport.getName() +  " viewPort : (60 per second, " + UtilsMath.round(viewport.getUpdateDuration() * 0.000001, 3) + ")", Color.WHITE); line++;
			}
			
			break;
			
		case GAME_STATE:
			managerText.setTextLine(line, "Game State", Color.YELLOW); 												line += 2;
			managerText.setTextLine(line, "Game Object Count :  " + WarpedState.getGameObjectCount()); 				line ++;
			managerText.setTextLine(line, "Active Game Object Count :  " + WarpedState.getActiveGameObjectCount());	line += 2;
			break;
			
		case SCREEN:
			managerText.setTextLine(line, "Screen", Color.YELLOW); 														line += 2;
			managerText.setTextLine(line, "Width : " +  WarpedWindow.getWindowWidth()); 								line ++;
			managerText.setTextLine(line, "Height : " + WarpedWindow.getWindowHeight()); 								line ++;
			managerText.setTextLine(line, "Center : (" + WarpedWindow.getCenterX() + ", " + WarpedWindow.getCenterY()); line ++;
			managerText.setTextLine(line, "View Ports   :  " + WarpedWindow.getViewPortCount()); 						line ++;
			break;
			
		case AUDIO:
			managerText.setTextLine(line, "Audio", Color.YELLOW); 											line += 2;
			//managerText.putTextLine(line, "Active Sound Effects : " + WarpedAudio.getSoundEffectCount()); 	line += 2; 
			/*
			ArrayList<SoundEffectType> fx = WarpedAudio.getActiveSoundEffects();
			for(int i = 0; i < fx.size(); i++) {
				managerText.putTextLine(line, fx.get(i).toString()); 										line++;
			}
			*/
			break;
			
		case CAMERA:
			managerText.setTextLine(line, "Cameras", Color.YELLOW); 										line += 2;
			ArrayList<WarpedCameraType> activeCameras = CameraManager.getActiveCameras();
			for(int i = 0; i < activeCameras.size(); i++) {
				WarpedCameraType type = activeCameras.get(i);
				managerText.setTextLine(line, type.toString()); line++;
				managerText.setTextLine(line, "Position    :  " + CameraManager.getCamera(type).getPosition().getString()); line++;
				managerText.setTextLine(line, "(C1), (C2)  :  " + CameraManager.getCamera(type).getC1().getString() + ", " + CameraManager.getCamera(type).getC2().getString()); line++;
				managerText.setTextLine(line, "Zoom        :  " + CameraManager.getCamera(type).getZoom()); 	 line++;
				managerText.setTextLine(line, "Zoom Speed  :  " + CameraManager.getCamera(type).getZoomSpeed()); line += 2;				
			}			
			break;
			
		case KEYBOARD:
			managerText.setTextLine(line, "Keyboard", Color.YELLOW); 													line += 2;
			managerText.setTextLine(line, "Keyboard Controller :  " + WarpedKeyboard.getActiveController().getName());  line++;
			managerText.setTextLine(line, "Key Presses  :  " + WarpedKeyboard.getPressesString());                      line++;
			managerText.setTextLine(line, "Key Releases :  " + WarpedKeyboard.getReleaseString());                      line++;
			managerText.setTextLine(line, "Key Bindings   :  " + WarpedKeyboard.getKeyBindingsCount());					line++;
			break;                  
			                        
		case MOUSE:     
			managerText.setTextLine(line, "Mouse", Color.YELLOW); 																			 line += 2;
			managerText.setTextLine(line, "Mouse in Window  :  " + WarpedMouse.isInWindow());												 line++;
			managerText.setTextLine(line, "Mouse dragging   :  " + WarpedMouse.isDragging());                                                line++;
			managerText.setTextLine(line, "Mouse is focused :  " + WarpedMouse.isFocused());                                                 line++;
			managerText.setTextLine(line, "Mouse position (x, y)  :  (" + WarpedMouse.getPoint().x + ", " + WarpedMouse.getPoint().y + ")"); line++; 
			break;                  
			
		case OBJECT:   
			managerText.setTextLine(line,  "Manager : Object", Color.YELLOW); 																	     line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getGroupCount()); 			 line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getActiveGroupCount());	     line++;	
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getGameObjectCount());       line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.OBJECT).getActiveGameObjectCount()); line++;
			break;  	
			
		case DEPTH_FIELD:  
			managerText.setTextLine(line,  "Manager : Depth Field", Color.YELLOW); 																		  line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getGroupCount()); 			  line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getActiveGroupCount());	  line++;	
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getGameObjectCount());       line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.DEPTH_FIELD).getActiveGameObjectCount()); line++;
			break;  
			
		case ENTITIE:
			managerText.setTextLine(line,  "Manager : Entitie", Color.YELLOW); 																			  line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getGroupCount());                line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getActiveGroupCount());          line++;
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getGameObjectCount());           line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.ENTITIE).getActiveGameObjectCount());     line++;
			break;
			
		case GUI:    
			managerText.setTextLine(line,  "Manager : GUI", Color.YELLOW); 																				  line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.GUI).getGroupCount());                    line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.GUI).getActiveGroupCount());              line++;
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.GUI).getGameObjectCount());               line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.GUI).getActiveGameObjectCount());         line++;
			break; 
			
		case TILE: 
			managerText.setTextLine(line,  "Manager : Tile", Color.YELLOW); 																			  line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.TILE).getGroupCount());                   line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.TILE).getActiveGroupCount());             line++;
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.TILE).getGameObjectCount());              line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.TILE).getActiveGameObjectCount());        line++;
			break;   
			
		case ITEM: 
			managerText.setTextLine(line,  "Manager : Item", Color.YELLOW); 																			  line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.getManager(WarpedManagerType.ITEM).getGroupCount());                   line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.getManager(WarpedManagerType.ITEM).getActiveGroupCount());             line++;
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.getManager(WarpedManagerType.ITEM).getGameObjectCount());              line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.getManager(WarpedManagerType.ITEM).getActiveGameObjectCount());        line++;
			break;
			
		default:
			Console.err("InspectorManager -> initializeTextBox() -> invalid case : " + page);
			return;		
		}
		
		managerText.updateGraphics();
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
		addMember(resolution);
		addMember(exit);

		addMember(pages);
		
	}
	
	

	
}
