/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import warped.WarpedProperties;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.KeyboardIcons;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.ButtonStateType;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsString;

public class GUIPopOutToggle extends WarpedGUI {

	private boolean isPoppedOut = false;
	private boolean isExtended = false;
	private DirectionType popDirection = DirectionType.DOWN;

	private int selectIndex = 0;
	//private int previousSelectIndex = 0;
	private ButtonStateType buttonState = ButtonStateType.PLAIN;
	private int hoveredIndex = 0;
	
	private Vec2i buttonSize = new Vec2i(120, 30);
	
	private ArrayList<String> actionLabels = new ArrayList<>();
	private HashMap<String, WarpedAction> selectActions   = new HashMap<>(); 
	private HashMap<String, WarpedAction> deselectActions = new HashMap<>();
	
	private int borderThickness = 1;
	private Vec2i textOffset = new Vec2i(1,1);
	
	private Color borderColor = Color.BLACK;
	private Color buttonColor = Color.DARK_GRAY;
	private Color buttonHoverColor = new Color(110,60,60); 
	private Color buttonPressColor = Color.RED;
	
	private Color textColor = Color.YELLOW;
	private Font font 		= UtilsFont.getPreferred();
	
	private BufferedImage pulledInRaster;   //The raster that displays only the first button
	private BufferedImage popedOutRaster; //The raster that displays all the buttons
	
	private static int stdIconSize = 20;
	private static BufferedImage popUpArrowGraphic   = FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_UP);
	private static BufferedImage popDownArrowGraphic = FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_DOWN);

	public int getToggleIndex() {return selectIndex;}
	
	public GUIPopOutToggle(List<String> actionLabels, List<WarpedAction> selectActions) {
		if(actionLabels.size() != selectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + actionLabels.size() + ", " + selectActions.size());
			return;
		}
		
		for(int i = 0; i < actionLabels.size(); i++) {
			this.actionLabels.add(actionLabels.get(i));
			this.selectActions.put(actionLabels.get(i), selectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	public GUIPopOutToggle(List<String> actionLabels, List<WarpedAction> selectActions, DirectionType popDirection) {
		if(actionLabels.size() != selectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + actionLabels.size() + ", " + selectActions.size());
			return;
		}
		
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN) {
			Console.err("GUIPopOutToggle -> constructor () -> pop out direction is not a valid direction : " + popDirection);
			return;
		}
		
		this.popDirection = popDirection;
		
		for(int i = 0; i < actionLabels.size(); i++) {
			this.actionLabels.add(actionLabels.get(i));
			this.selectActions.put(actionLabels.get(i), selectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}

	public GUIPopOutToggle(List<String> actionLabels, List<WarpedAction> selectActions, DirectionType popDirection, int width, int height) {
		if(actionLabels.size() != selectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + actionLabels.size() + ", " + selectActions.size());
			return;
		}
		
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN) {
			Console.err("GUIPopOutToggle -> constructor () -> pop out direction is not a valid direction : " + popDirection);
			return;
		}
		
		buttonSize.x = width;
		buttonSize.y = height;
		this.popDirection = popDirection;
		
		for(int i = 0; i < actionLabels.size(); i++) {
			this.actionLabels.add(actionLabels.get(i));
			this.selectActions.put(actionLabels.get(i), selectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	public GUIPopOutToggle(List<String> actionLabels, List<WarpedAction> selectActions, List<WarpedAction> deselectActions) {
		if(actionLabels.size() != selectActions.size() || selectActions.size() != deselectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + actionLabels.size() + ", " + selectActions.size());
			return;
		}
		
		for(int i = 0; i < actionLabels.size(); i++) {
			actionLabels.add(actionLabels.get(i));
			this.selectActions.put(actionLabels.get(i), selectActions.get(i));
			this.deselectActions.put(actionLabels.get(i), deselectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	public GUIPopOutToggle(List<String> actionLabels, List<WarpedAction> selectActions, List<WarpedAction> deselectActions, DirectionType popDirection) {
		if(actionLabels.size() != selectActions.size() || selectActions.size() != deselectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + actionLabels.size() + ", " + selectActions.size());
			return;
		}
		
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN) {
			Console.err("GUIPopOutToggle -> constructor () -> pop out direction is not a valid direction : " + popDirection);
			return;
		}
		
		this.popDirection = popDirection;
		
		for(int i = 0; i < actionLabels.size(); i++) {
			this.actionLabels.add(actionLabels.get(i));
			this.selectActions.put(actionLabels.get(i), selectActions.get(i));
			this.deselectActions.put(actionLabels.get(i), deselectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	public GUIPopOutToggle(List<String> actionLabels, List<WarpedAction> selectActions, List<WarpedAction> deselectActions, DirectionType popDirection, int width, int height) {
		if(actionLabels.size() != selectActions.size() || selectActions.size() != deselectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + actionLabels.size() + ", " + selectActions.size());
			return;
		}
		
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN) {
			Console.err("GUIPopOutToggle -> constructor () -> pop out direction is not a valid direction : " + popDirection);
			return;
		}
		
		buttonSize.x = width;
		buttonSize.y = height;
		this.popDirection = popDirection;
		
		for(int i = 0; i < actionLabels.size(); i++) {
			this.actionLabels.add(actionLabels.get(i));
			this.selectActions.put(actionLabels.get(i), selectActions.get(i));
			this.deselectActions.put(actionLabels.get(i), deselectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	public <T extends Enum<T>> GUIPopOutToggle(T[] actionLabels, List<WarpedAction> selectActions) {
		if(actionLabels.length != selectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + this.actionLabels.size() + ", " + selectActions.size());
			return;
		}
		this.actionLabels = UtilsString.convertEnumToStringArray(actionLabels);
		if(this.actionLabels == null || this.actionLabels.size() <= 0) {
			Console.err("GUIDropDownToggle() -> constructor() -> actionLabels is invalid");
			return;
		}
		
		for(int i = 0; i < this.actionLabels.size(); i++) {
			this.selectActions.put(this.actionLabels.get(i), selectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	private void initializeRasters() {
		pulledInRaster   = new BufferedImage(buttonSize.x, buttonSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		popedOutRaster = new BufferedImage(buttonSize.x, (buttonSize.y * this.actionLabels.size()), WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(pulledInRaster);
	}
	
	private void popOut() {
		isPoppedOut = true;
		extend();
		updateGraphics();
	}
	
	private void pullIn() {
		isPoppedOut = false;
		retract();
		updateGraphics();
	}
	
	
	private void extend() {
		if(popDirection == DirectionType.UP) {			
			if(!isExtended) {
				isExtended = true;
				position.y -= (buttonSize.y * (actionLabels.size() - 1));
			}
		}
		
	}
	
	private void retract() {
		if(popDirection == DirectionType.UP) {			
			if(isExtended) {
				isExtended = false;
				position.y += (buttonSize.y * (actionLabels.size() - 1));
			}
		}
	}
	
	
	private void select(int selectIndex) {
		if(selectIndex < 0 || selectIndex > actionLabels.size()) {
			Console.err("GUIDropDownToggle -> selectAction() -> actionIndex is out of function domain 0 < actionIndex < " + actionLabels.size() + "  -> action index : " + selectIndex);
			return;
		}
		
		if(!deselectActions.containsKey(actionLabels.get(this.selectIndex))) {	//The deselect action - the index from the button that is to be overwritten with a new selection
			Console.ln("GUIDropDownToggle -> selectAction() -> drop down toggle has no deselect action for : " + actionLabels.get(selectIndex));
		} else {			
			WarpedAction action = deselectActions.get(actionLabels.get(this.selectIndex));
			if(action == null) {
				Console.err("GUIDropDownToggle -> deselectAction() -> action is null : " + this.selectIndex);
			} else action.action();
		}

		if(!selectActions.containsKey(actionLabels.get(selectIndex))) {   //The selected action - the new index passed in from the users selection 
			Console.ln("GUIDropDownToggle -> selectAction() -> drop down toggle has no select action for : " + actionLabels.get(selectIndex));
		} else {			
			WarpedAction action = selectActions.get(actionLabels.get(selectIndex));
			if(action == null) {
				Console.err("GUIDropDownToggle -> selectAction() -> action is null : " + selectIndex);
			} else action.action();
		}
		
		
		if(selectIndex == 0 && !isPoppedOut) {
			popOut();
			return;
		} else {
			this.selectIndex = selectIndex;
			pullIn();
		
		}
	}
	
	private void updateGraphics() {
		if(!isPoppedOut) {
			setRaster(pulledInRaster);
			Graphics2D g = raster.createGraphics();
			switch(buttonState) {
			case PLAIN: drawButton(g, selectIndex, 0, 0, buttonColor); break;
			case HOVERED: drawButton(g, selectIndex, 0, 0, buttonHoverColor); break;
			case PRESSED : drawButton(g, selectIndex, 0, 0, buttonPressColor); break;
			default: Console.err("GUIDropDownToggle -> updateGraphics -> invalid switch case : " + buttonState); break;
			}
		} else {
			setRaster(popedOutRaster);
			Graphics2D g = raster.createGraphics();
			switch(buttonState) {
			case HOVERED:
				if(selectIndex == hoveredIndex)	drawButton(g, hoveredIndex, 0, hoveredIndex * buttonSize.y, buttonPressColor);
				else drawButton(g, hoveredIndex, 0, hoveredIndex * buttonSize.y, buttonHoverColor);
				for(int i = 0; i < actionLabels.size(); i++) {
					if(i == selectIndex)  continue;
					if(i == hoveredIndex) continue;
					drawButton(g, i, 0, i * buttonSize.y, buttonColor);
				}
				break;
			case PRESSED:
				if(selectIndex != hoveredIndex) drawButton(g, selectIndex, 0, 0, buttonColor);
				drawButton(g, hoveredIndex, 0, hoveredIndex * buttonSize.y, buttonPressColor);
				for(int i = 0; i < actionLabels.size(); i++) {
					if(i == selectIndex)  continue;
					if(i == hoveredIndex) continue;
					drawButton(g, i, 0, i * buttonSize.y, buttonColor);
				}
				break;
			default:
				Console.err("GUIPopOutToggle -> updateGraphics() -> invalid button state : " + buttonState);
				break;
			}
		}
	}

	private void drawButton(Graphics2D g, int labelIndex, int x, int y, Color buttonColor) {
		g.setColor(borderColor);
		g.fillRect(x, y, buttonSize.x, buttonSize.y);
		
		g.setColor(buttonColor);	
		g.fillRect(borderThickness + x, borderThickness + y, buttonSize.x - borderThickness * 2, buttonSize.y - borderThickness * 2);
		
		g.setColor(textColor);
		g.setFont(font);
		g.drawString(actionLabels.get(labelIndex), x + borderThickness * 2 + textOffset.x, y + (borderThickness * 2) + font.getSize() + textOffset.y);
		
		
		if(!isPoppedOut && popDirection == DirectionType.UP)   g.drawImage(popUpArrowGraphic, buttonSize.x - (stdIconSize + borderThickness * 2), (buttonSize.y / 2) - (stdIconSize / 2), stdIconSize, stdIconSize, null);
		if(!isPoppedOut && popDirection == DirectionType.DOWN) g.drawImage(popDownArrowGraphic, buttonSize.x - (stdIconSize + borderThickness * 2), (buttonSize.y / 2) - (stdIconSize / 2), stdIconSize, stdIconSize, null);
	}
	
	@Override
	protected void mouseEntered() {
		buttonState = ButtonStateType.HOVERED;
		hoveredIndex = 0;
		pullIn();
	}

	@Override
	protected void mouseExited() {
		buttonState = ButtonStateType.PLAIN;
		hoveredIndex = 0;
		pullIn();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		buttonState = ButtonStateType.HOVERED;
		int index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y);
		if(index >= actionLabels.size()) index = actionLabels.size() - 1;
		if(index != hoveredIndex) {
			hoveredIndex = index;
			updateGraphics();
		}
	}


	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		buttonState = ButtonStateType.PRESSED;
		updateGraphics();
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int selectIndex = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y); 
		if(selectIndex == 0) buttonState = ButtonStateType.HOVERED;
		else buttonState = ButtonStateType.PLAIN;
		hoveredIndex = 0;
		
		if(!isPoppedOut && selectIndex == 0) {
			popOut();
			return;
		}
		select(selectIndex);
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void updateRaster() {return;}
	@Override
	protected void updateObject() {return;}
	@Override
	protected void updatePosition() {return;}	
}
