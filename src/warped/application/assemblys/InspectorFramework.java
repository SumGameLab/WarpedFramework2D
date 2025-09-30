/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import warped.application.actionWrappers.ActionOption;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIClock;
import warped.application.gui.GUIListVertical;
import warped.application.gui.GUIPopOutMenu;
import warped.application.gui.GUIRadioButtons;
import warped.application.gui.GUIShape;
import warped.application.gui.GUITextBoxLined;
import warped.application.gui.GUIToggle;
import warped.application.gui.GUIClock.ClockModeType;
import warped.application.state.GUIAssembly;
import warped.application.state.WarpedFramework2D;
import warped.application.state.WarpedState;
import warped.audio.FrameworkAudio;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.graphics.window.WarpedMouse;
import warped.graphics.window.WarpedViewport;
import warped.graphics.window.WarpedWindow;
import warped.user.WarpedUserInput;
import warped.user.keyboard.WarpedKeyBind;
import warped.user.keyboard.WarpedKeyboard;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class InspectorFramework extends GUIAssembly {		
		
	public enum FrameworkInspectorPage {
		
		FRAMEWORK_BUTTONS,
		WARPED_FRAMEWORK,
		GAME_STATE,
		
		SCREEN,
		AUDIO,
		
		KEYBOARD,
		MOUSE,
		
		OBJECT,
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
	
	private GUIButton title     = new GUIButton(borderRect.width - borderThickness - (30 * 4) - 160, 30, "  WarpedFramework2D  ");
	private GUIButton next      = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_RIGHT));
	private GUIButton previous  = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_LEFT));
	private GUIToggle refresh   = new GUIToggle(FrameworkSprites.getStandardIcon(StandardIcons.REFRESH));
	private GUIButton close     = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIPopOutMenu pages = new GUIPopOutMenu("page");
	
	private GUIClock clock = new GUIClock();
	
	private GUIRadioButtons resolution = new GUIRadioButtons(Arrays.asList("720p", "1080p", "  4K "), 
			Arrays.asList(() -> {WarpedFramework2D.getWindow().setWindowResolution(1280, 720);},
					() -> {WarpedFramework2D.getWindow().setWindowResolution(1920, 1080);},
					() -> {WarpedFramework2D.getWindow().setWindowResolution(3840, 2160);}));
	private GUIButton exit		= new GUIButton("exit");
	
	private GUITextBoxLined managerText  = new GUITextBoxLined(400, 580);	
	
	private GUIShape screenParameters = new GUIShape(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight());	
	private Rectangle v_crossHair = new Rectangle((int)WarpedWindow.getCenterX() - 5, (int)WarpedWindow.getCenterY() - 1, 10, 2);
	private Rectangle h_crossHair = new Rectangle((int)WarpedWindow.getCenterX() - 1, (int)WarpedWindow.getCenterY() - 5, 2, 10);
	private Rectangle top 	 = new Rectangle(0,0, WarpedWindow.getWindowWidth(), 2);
	private Rectangle bottom = new Rectangle(0, WarpedWindow.getWindowHeight() - 2, WarpedWindow.getWindowWidth(), 2);
	private Rectangle left 	 = new Rectangle(0,0, 2, WarpedWindow.getWindowHeight());
	private Rectangle right  = new Rectangle(WarpedWindow.getWindowWidth() - 2, 0, 2, WarpedWindow.getWindowHeight());

	private GUIListVertical keybinds = new GUIListVertical();
	
	public InspectorFramework() {
		super();		
	}
	
	@Override
	protected void defineAssembly() {
		close.setOffset(borderRect.width- 30 - borderThickness, 0);
		refresh.setOffset(close.getOffsetX() - 30, 0);
		next.setOffset(refresh.getOffsetX() - 30, 0);
		previous.setOffset(next.getOffsetX() - 30, 0);
		pages.setOffset(previous.getOffsetX() - 90, 0);
		clock.setOffset(pages.getOffsetX() - 70, 0);
		keybinds.setOffset(10, 40);
		
		managerText.setOffset(5, 35);
		managerText.setInteractive(false);
		
		resolution.setOffset(50, 150);
		exit.setOffset(50, 270);
		
		
		close.offset(title);
		next.offset(title);
		previous.offset(title);
		managerText.offset(title);
		refresh.offset(title);
		pages.offset(title);
		keybinds.offset(title);
		clock.offset(title);
		
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
		
		clock.setClockMode(ClockModeType.HOUR_12_WITH_SECONDS);
		
		background.addRectangle(borderRect, Color.BLACK);
		background.addRectangle(backgroundRect, Colour.GREY_DARK);
		
		exit.setReleasedAction(mouseE -> {WarpedFramework2D.stop();});
		resolution.setAxis(AxisType.HORIZONTAL);
		
		title.setDragable(true);
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				if(!i.isEqualTo(screenParameters)) {
					i.offset(title);
				}				
			});			
		});
		
		
		keybinds.setVisible(false);
		keybinds.setSize(400, 400);
		updateKeybindOptions();
		
		ArrayList<ActionOption> pageOptions = new ArrayList<>();
		pageOptions.add(new ActionOption(FrameworkInspectorPage.AUDIO.toString(), () -> {page = FrameworkInspectorPage.AUDIO; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.FRAMEWORK_BUTTONS.toString(), () -> {page = FrameworkInspectorPage.FRAMEWORK_BUTTONS; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.GAME_STATE.toString(), () -> {page = FrameworkInspectorPage.GAME_STATE; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.GUI.toString(), () -> {page = FrameworkInspectorPage.GUI; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.KEYBOARD.toString(), () -> {page = FrameworkInspectorPage.KEYBOARD; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.MOUSE.toString(), () -> {page = FrameworkInspectorPage.MOUSE; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.OBJECT.toString(), () -> {page = FrameworkInspectorPage.OBJECT; updatePage();}));
		pageOptions.add(new ActionOption(FrameworkInspectorPage.SCREEN.toString(), () -> {page = FrameworkInspectorPage.SCREEN; updatePage();}));
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
		  
		
		managerText.setInteractive(true);
		updatePage();	
		
		
		refresh.setRepeatingAction(() -> {updatePage();});		
	}

	public void updateKeybindOptions() {
		ArrayList<ActionOption> result = new ArrayList<>();
		for(WarpedKeyBind bind : WarpedKeyboard.getActiveController().getHotBinds()) {
			result.add(new ActionOption(bind.getName() + "  :  " + KeyEvent.getKeyText(bind.getKey()), () -> {bind.setReceiving(true);} ));
		}
		keybinds.setOptions(result);
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
		keybinds.setVisible(false);
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
			managerText.setTextLine(line, "Threads : (Target Hz, Measured Hz, Millis per cycle)", Color.YELLOW); line += 2;
			managerText.setTextLine(line, "---WarpedState---", Color.YELLOW);	line ++;
			managerText.setTextLine(line, "Active  : (58 Hz, " + WarpedState.getCycleCount() + ", " + UtilsMath.round(WarpedState.getActiveCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.setTextLine(line, "Mid     : (1 per second, n.a, " + UtilsMath.round(WarpedState.getMidCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.setTextLine(line, "Slow    : (1 per minute, n.a, " + UtilsMath.round(WarpedState.getSlowCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line ++;
			managerText.setTextLine(line, "Passive : (1 per hour,  n.a, " + UtilsMath.round(WarpedState.getPassiveCycleDuration() * 0.000001, 3) + ")", Color.WHITE); line += 2;
			            
			managerText.setTextLine(line, "---Window---", Color.YELLOW); line ++;
			managerText.setTextLine(line, "Update : (60 Hz, " + WarpedWindow.getUPS() + ", " + UtilsMath.round(WarpedWindow.getUpdateDuration() * 0.000001, 3) + ")"); line++;
			managerText.setTextLine(line, "Render : (60 Hz, " + WarpedWindow.getFPS() + ", " + UtilsMath.round(WarpedWindow.getRenderDuration() * 0.000001, 3) + ")"); line++;
                        
			managerText.setTextLine(line, "---User Input : (10 Hz, n.a, " + UtilsMath.round(WarpedUserInput.getUpdateDuration() * 0.000001, 3) + ")", Color.YELLOW); line += 2;
			            
			managerText.setTextLine(line, "---Warped Viewports---", Color.YELLOW); line++;
			for(int i = 0; i < WarpedWindow.getViewPorts().length; i++) {
				WarpedViewport viewport = WarpedWindow.getViewPort(i);
				managerText.setTextLine(line, viewport.getName() +  " viewPort : (60 Hz, " + viewport.getFPS() + ", " + UtilsMath.round(viewport.getUpdateDuration() * 0.000001, 3) + ")", Color.WHITE); line++;
			}
			
			break;
			
		case GAME_STATE:
			managerText.setTextLine(line, "Game State", Color.YELLOW); 												line += 2;
			managerText.setTextLine(line, "Game Object Count :  " + WarpedState.getObjectCount()); 				line ++;
			managerText.setTextLine(line, "Active Game Object Count :  " + WarpedState.getActiveObjectCount());	line += 2;
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
			

		case KEYBOARD:
			//keybinds.setOptions(null)
			keybinds.setVisible(true);
			managerText.setTextLine(line, "Keyboard", Color.YELLOW); 													line += 2;
			managerText.setTextLine(line, "Keyboard Controller :  " + WarpedKeyboard.getActiveController().getName());  line++;
			managerText.setTextLine(line, "Key Presses  :  " + WarpedKeyboard.getPresses());                      		line++;
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
			managerText.setTextLine(line,  "Manager : Object", Color.YELLOW); 											line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.guiManager.size()); 		line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.guiManager.getOpenGroupCount());	 	line++;	
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.guiManager.getObjectCount());     	line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.guiManager.getOpenObjectCount()); 	line++;
			break;  	
			
	
		case GUI:    
			managerText.setTextLine(line,  "Manager : GUI", Color.YELLOW); 												line += 2;
			managerText.setTextLine(line,  "Groups              :  " + WarpedState.guiManager.size());         line++;
			managerText.setTextLine(line,  "Active Groups       :  " + WarpedState.guiManager.getOpenGroupCount());     line++;
			managerText.setTextLine(line,  "Game Objects        :  " + WarpedState.guiManager.getObjectCount());        line++;
			managerText.setTextLine(line,  "Active Game Objects :  " + WarpedState.guiManager.getOpenObjectCount());    line++;
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
		addMember(clock);

		addMember(keybinds);
		addMember(pages);
		
	}
	
	

	
}
