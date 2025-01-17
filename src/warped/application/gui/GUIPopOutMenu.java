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
import warped.application.object.WarpedOption;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.KeyboardIcons;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.ButtonStateType;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsString;

public class GUIPopOutMenu extends WarpedGUI {

	private boolean isPoppedOut = false;
	private boolean isExtended = false;
	private DirectionType popDirection = DirectionType.DOWN;

	private ButtonStateType buttonState = ButtonStateType.HOVERED;
	private int hoverIndex = 0;
	
	private Vec2i buttonSize = new Vec2i(120, 30);
	
	private String menuLabel = "default";
	private ArrayList<String> actionLabels = new ArrayList<>();
	private HashMap<String, WarpedAction> selectActions   = new HashMap<>(); 
	
	private WarpedAction enterAction = null;
	private WarpedAction exitAction = null;
	
	private int borderThickness = 2;
	private Vec2i textOffset = new Vec2i(1,1);
	
	private Color borderColor = Color.BLACK;
	private Color buttonColor = Color.DARK_GRAY;
	private Color buttonHoverColor = new Color(110,60,60); 
	private Color buttonPressColor = Color.RED;
	
	private int textSize 	= 12;
	private Color textColor = Color.YELLOW;
	private int textStyle 	= Font.PLAIN;
	private Font font 		= new Font("DropDownToggleFont", textStyle, textSize);
	
	private BufferedImage pulledInRaster;   //The raster that displays only the first button
	private BufferedImage popedOutRaster; //The raster that displays all the buttons

	private int stdIconSize = 20;
	private static BufferedImage popUpArrowGraphic    = FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_UP);
	private static BufferedImage popLeftArrowGraphic  = FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_LEFT);
	private static BufferedImage popDownArrowGraphic  = FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_DOWN);
	private static BufferedImage popRightArrowGraphic = FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_RIGHT);
	
	public GUIPopOutMenu() {
		initializeRasters();
		updateGraphics();
	}
	
	public GUIPopOutMenu(String menuLabel, List<String> actionLabels, List<WarpedAction> selectActions) {setButtons(menuLabel, actionLabels, selectActions);}
	public <T extends Enum<T>> GUIPopOutMenu(String menuLabel, T[] actionLabels, List<WarpedAction> selectActions) {setButtons(menuLabel, UtilsString.convertEnumToStringArray(actionLabels), selectActions);}
	
	public GUIPopOutMenu(String menuLabel, List<String> actionLabels, List<WarpedAction> selectActions, DirectionType popDirection) {
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN && popDirection != DirectionType.LEFT && popDirection != DirectionType.RIGHT) {
			Console.err("GUIPopOutToggle -> constructor () -> pop out direction is not a valid direction : " + popDirection);
			return;
		}
		this.popDirection = popDirection;
		setButtons(menuLabel, actionLabels, selectActions);
	}
	
	public void setButtons(String menuLabel, List<String> actionLabels, List<WarpedAction> selectActions) {
		this.actionLabels.clear();
		this.selectActions.clear();
		if(menuLabel == null || menuLabel.equals("")) {
			Console.err("GUIPopOutMen -> constructor() -> your pop out menu must have a label");
			return;
		}
		
		if(actionLabels.size() != selectActions.size()) {
			Console.err("GUIDropDownToggle -> constructor() -> number of actions does not match number of options : " + actionLabels.size() + ", " + selectActions.size());
			return;
		}
		
		this.menuLabel = menuLabel;
		for(int i = 0; i < actionLabels.size(); i++) {
			this.actionLabels.add(actionLabels.get(i));
			this.selectActions.put(actionLabels.get(i), selectActions.get(i));
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	public synchronized void setEnterAction(WarpedAction enterAction) {this.enterAction = enterAction;}
	public synchronized void setExitAction(WarpedAction exitAction) {this.exitAction = exitAction;}
	
	public void setButtons(String menuLabel, ArrayList<WarpedOption> options) {
		actionLabels.clear();
		selectActions.clear();
	
		if(options.size() <= 0 || options == null) {
			Console.err("GUIDPopOutMen -> setButtons() -> number of options is <= 0 or null");
			return;
		}
		
		
		
		this.menuLabel = menuLabel;
		for(int i = 0; i < options.size(); i++) {
			this.actionLabels.add(options.get(i).getName());
			this.selectActions.put(options.get(i).getName(), options.get(i).getAction());
		}
		
		initializeRasters();
		updateGraphics();
	}
	
	private void initializeRasters() {
		pulledInRaster = new BufferedImage(buttonSize.x, buttonSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN) 			
			popedOutRaster = new BufferedImage(buttonSize.x, (buttonSize.y * this.actionLabels.size()) + buttonSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		else if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) 
			popedOutRaster = new BufferedImage((buttonSize.x * this.actionLabels.size()) + buttonSize.x, buttonSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		else {Console.err("GUIPopOutMenu -> initializeRasters() -> invalid case : " + popDirection); return;}
		setRaster(pulledInRaster);
	}
	
	public boolean containsOption(String option) {if(selectActions.containsKey(option)) return true; else return false;}
	public Vec2i getButtonSize() {return buttonSize;}
	public void setPopDirection(DirectionType popDirection) {
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN || popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) this.popDirection = popDirection;
		else Console.err("GUIPopOutMenu -> SetPopDirection() -> invalid pop Direction : " + popDirection); return;
	}
	
	public void popOut() {
		if(!isPoppedOut) {			
			isPoppedOut = true;
			extend();
			updateGraphics();
			Console.ln("GUIPopOutMenu -> popOut()");
		}
	}
	
	public void pullIn() {
		if(isPoppedOut) {			
			isPoppedOut = false;
			retract();
			updateGraphics();
			Console.ln("GUIPopOutMenu -> pullIn()");
		}
	}
	
	
	private void extend() {
		if(popDirection == DirectionType.UP) {			
			if(!isExtended) {
				isExtended = true;
				position.y -= (buttonSize.y * (actionLabels.size()));
				hoverIndex = actionLabels.size() - 1;
				hoverIndex = actionLabels.size();
			}
		}
		if(popDirection == DirectionType.LEFT) {
			if(!isExtended) {
				isExtended = true;
				position.x -= (buttonSize.x * (actionLabels.size()));
				hoverIndex = actionLabels.size();
			}
		}
		buttonState = ButtonStateType.HOVERED;
	}
	
	private void retract() {
		if(popDirection == DirectionType.UP) {			
			if(isExtended) {
				isExtended = false;
				position.y += (buttonSize.y * (actionLabels.size()));
			}
		}
		if(popDirection == DirectionType.LEFT) {
			if(isExtended) {
				isExtended = false;
				position.x += (buttonSize.x * (actionLabels.size()));
			}
		}
	}
	
	private void select() {		  
		int actionIndex = -1;
		actionIndex = hoverIndex - 1;
		
		if(hoverIndex == 0) {
			if(isPoppedOut) pullIn();
			else popOut();
		} else {
			if(!selectActions.containsKey(actionLabels.get(actionIndex))) {   //The selected action - the new index passed in from the users selection 
				Console.ln("GUIDropDownToggle -> selectAction() -> drop down toggle has no action for : " + actionIndex);
			} else {			
				WarpedAction action = selectActions.get(actionLabels.get(actionIndex));
				if(action == null) {
					Console.err("GUIDropDownToggle -> selectAction() -> action is null");
				} else action.action();
			}
			pullIn();
		}		
	}
	
	private void updateGraphics() {		
		int buttonIndex = hoverIndex - 1;
		if(!isPoppedOut) {
			setRaster(pulledInRaster);
			Graphics2D g = raster.createGraphics();
			drawMenuLabelButton(g);
			g.dispose();
			
		} else {
			setRaster(popedOutRaster);
			Graphics2D g = raster.createGraphics();
			drawMenuLabelButton(g);
			if(hoverIndex > 0 && hoverIndex < actionLabels.size() + 1) {				
				
				if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN) {
					switch(buttonState) {
					case HOVERED: drawButton(g, buttonIndex, 0, hoverIndex * buttonSize.y, buttonHoverColor); break;
					case PRESSED: drawButton(g, buttonIndex, 0, hoverIndex * buttonSize.y, buttonPressColor); break;
					case PLAIN  : drawButton(g, buttonIndex, 0, hoverIndex * buttonSize.y, buttonColor); break;
					default:
						Console.err("GUIPopOutMenu -> updateGraphics() -> invalid button state : " + buttonState); 
						break;
					}					
				} else {
					switch(buttonState) {
					case HOVERED: drawButton(g, buttonIndex, hoverIndex * buttonSize.x, 0, buttonHoverColor); break;
					case PRESSED: drawButton(g, buttonIndex, hoverIndex * buttonSize.x, 0, buttonPressColor); break;
					case PLAIN  : drawButton(g, buttonIndex, hoverIndex * buttonSize.x, 0, buttonColor); break;
					default:
						Console.err("GUIPopOutMenu -> updateGraphics() -> invalid button state : " + buttonState); 
						break;
					}			
				}
				
			}
			
			if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN) {				
				for(int i = 0; i < actionLabels.size(); i++) {
					if( i == hoverIndex - 1) continue;
					drawButton(g, i, 0, buttonSize.y + buttonSize.y * i, buttonColor);
				}
			} else {
				for(int i = 0; i < actionLabels.size(); i++) {
					if( i == hoverIndex - 1) continue;
					drawButton(g, i, buttonSize.x + buttonSize.x * i, 0, buttonColor);
				}
			}
			
			g.dispose();
		}
	}


	
	private void drawMenuLabelButton(Graphics2D g) {
		Color col = null;
		if(buttonState == ButtonStateType.HOVERED && hoverIndex == 0) col = buttonHoverColor;
		else col = buttonColor;
		
		g.setColor(borderColor);
		g.fillRect(0, 0, buttonSize.x, buttonSize.y);
		
		g.setColor(col);	
		g.fillRect(borderThickness, borderThickness, buttonSize.x - borderThickness * 2, buttonSize.y - borderThickness * 2);
		
		g.setColor(textColor);
		g.setFont(font);
		g.drawString(menuLabel, borderThickness * 2 + textOffset.x, (borderThickness * 2) + textSize + textOffset.y);	
			
		switch(popDirection) {
		case DOWN:  g.drawImage(popDownArrowGraphic,  buttonSize.x - (stdIconSize + borderThickness * 2), (buttonSize.y / 2) - (stdIconSize / 2), stdIconSize, stdIconSize, null); break;
		case UP:    g.drawImage(popUpArrowGraphic,  buttonSize.x - (stdIconSize + borderThickness * 2), (buttonSize.y / 2) - (stdIconSize / 2), stdIconSize, stdIconSize, null); break;
		case RIGHT: g.drawImage(popRightArrowGraphic,  buttonSize.x - (stdIconSize + borderThickness * 2), (buttonSize.y / 2) - (stdIconSize / 2), stdIconSize, stdIconSize, null); break;
		case LEFT:  g.drawImage(popLeftArrowGraphic,  buttonSize.x - (stdIconSize + borderThickness * 2), (buttonSize.y / 2) - (stdIconSize / 2), stdIconSize, stdIconSize, null); break;
		default:
			break;
		}
	}
	
	private void drawButton(Graphics2D g, int labelIndex, int x, int y, Color buttonColor) {
		g.setColor(borderColor);
		g.fillRect(x, y, buttonSize.x, buttonSize.y);
		
		g.setColor(buttonColor);	
		g.fillRect(borderThickness + x, borderThickness + y, buttonSize.x - borderThickness * 2, buttonSize.y - borderThickness * 2);
		
		g.setColor(textColor);
		g.setFont(font);
		g.drawString(actionLabels.get(labelIndex), x + borderThickness * 2 + textOffset.x, y + (borderThickness * 2) + textSize + textOffset.y);
	}
	
	@Override
	protected void mouseEntered() {
		if(enterAction!= null) enterAction.action();
		buttonState = ButtonStateType.HOVERED;
		hoverIndex = 0;
	}

	@Override
	protected void mouseExited() {
		if(exitAction != null) exitAction.action();
		buttonState = ButtonStateType.PLAIN;
		hoverIndex = 0;
		pullIn();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.HOVERED;
		
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN)    index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y);		
		if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x);

		if(index != hoverIndex) {
			hoverIndex = index;
			updateGraphics();
		}
		
	}


	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.PRESSED;
		
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN)    index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y);		
		if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x);
		
		if(index != hoverIndex) {
			hoverIndex = index;
			updateGraphics();
		}
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.PLAIN;
		
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN)    index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y);		
		if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x);
		
		if(index != hoverIndex) {
			hoverIndex = index;
			updateGraphics();
		}

		select();
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.HOVERED;
		
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN)    index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y);		
		if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x);
		
		if(index != hoverIndex) {
			hoverIndex = index;
			updateGraphics();
		}}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void updateRaster() {return;}
	@Override
	protected void updateObject() {return;}
	@Override
	protected void updatePosition() {return;}
}
