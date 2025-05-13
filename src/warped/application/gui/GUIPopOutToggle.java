/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import warped.application.actionWrappers.ActionToggle;
import warped.application.gui.GUIButton.ButtonStateType;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class GUIPopOutToggle extends WarpedGUI {

	
	/*GUIPopOutToggle has these functionalities:
	 * 
	* 		- A permanently visible button that controls the visibility of a menu.
	 * 		- Has a menu of toggles that can be shown / hidden by the main button. 
	 * 		- Each toggle can have unique toggleOn / toggleOff actions assigned.
	 * 		- Toggles have persistent state that is visible when the menu is popped out.
	 * 		- Toggles can have the colour set for their background, text and hover effect.
	 * 
	 * */
	
	private boolean isPoppedOut = false;

	private DirectionType popDirection = DirectionType.DOWN;
	private ButtonStateType buttonState = ButtonStateType.HOVERED;

	private int hoveredIndex = -1;
	private VectorI buttonSize = new VectorI(90, 30);
	
	private String menuLabel = "default";
	private ArrayList<ActionToggle> toggles = new ArrayList<>(); 
		
	private int borderThickness 	= 2;
	
	private VectorI labelTextOffset = new VectorI(20, 16);
	private VectorI toggleTextOffset= new VectorI(10, 16);
	
	private Color borderColor 		= Colour.BLACK.getColor();
	private Color toggleOffColor	= Colour.GREY_DARK.getColor();
	private Color toggleOnColor		= Colour.GREEN.getColor();
	private Color labelTextColor  	= Color.YELLOW;
	private Color toggleTextColor	= Color.WHITE;
	private Color labelColor      	= Colour.GREY_LIGHT.getColor();
	private Color hoverColor 		= Colour.RED_DARK.getColor(100); 
	private Color pressColor 		= Colour.RED.getColor(100);
	
	private Font toggleFont 		= new Font("ButtonFont", Font.PLAIN, 12);
	private Font labelFont 			= new Font("DropDownToggleFont", Font.PLAIN, 12);
		
	/**A menu with the specified label and no options.
	 * @param menuLabel - the label for the button that will show/hide the options.
	 * @author 5som3*/
	public GUIPopOutToggle(String menuLabel) {
		this.menuLabel = menuLabel;
		initializeRasters();
		updateGraphics();
	}
	
	/**A menu with the specified label and options
	 * @param menuLabel - the label for the button that will show/hide the options. 
	 * @param toggles - the options to show when the menu open.
	 * @author 5som3*/
	public GUIPopOutToggle(String menuLabel, ActionToggle... toggles) {
		this.menuLabel = menuLabel;
		this.toggles = new ArrayList<>(Arrays.asList(toggles));
		initializeRasters();
		updateGraphics();
	}
	
	/**A menu that pops in the specified direction with the specified label and options.
	 * @param menuLabel - the label for the button that will show/hide the options.
	 * @param popDirection - the direction that the menu will pop open. 
	 * @param toggles - the options to show when the menu open.
	 * @author 5som3*/
	public GUIPopOutToggle(String menuLabel, DirectionType popDirection, ActionToggle... toggles) {
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN && popDirection != DirectionType.LEFT && popDirection != DirectionType.RIGHT) {
			Console.err("GUIPopOutToggle -> constructor () -> pop out direction is not a valid direction : " + popDirection);
			popDirection = DirectionType.DOWN;
		}
		this.menuLabel = menuLabel;
		this.toggles = new ArrayList<>(Arrays.asList(toggles));
		initializeRasters();
		updateGraphics();
	}
	
	/**Set the options for this menu to display when popped out.
	 * @param toggles - a list of the options to display.
	 * @author 5som3*/
	public void setToggles(ActionToggle... toggles) {
		this.toggles = new ArrayList<>(Arrays.asList(toggles));
		initializeRasters();
		updateGraphics();
	}
	
	/**Set the options for this menu to display when popped out.
	 * @param toggles - an ArrayList of the options to display.
	 * @author 5som3*/
	public void setToggles(ArrayList<ActionToggle> toggles) {
		this.toggles = toggles;
		initializeRasters();
		updateGraphics();
	}
	
	/**Set the offset for this GUI.
	 * Use in conjunction with offset() to position elements within an assembly.
	 * @param x - the x offset in pixels.
	 * @param y - the y offset in pixels.
	 * @implNote - will adjust for the LEFT and UP popDirection cases.
	 * @author 5som3*/
	public void setOffset(double x, double y) {
		if(popDirection == DirectionType.LEFT) offset.set(x - buttonSize.x() * toggles.size(), y);
		else if(popDirection == DirectionType.UP) offset.set(x, y - buttonSize.y() * toggles.size());
		else offset.set(x, y);
	}
	
	/**Set the size of the raster to be the maximum needed size.
	 * @author 5som3*/
	private void initializeRasters() {
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN) setSize(buttonSize.x(), (buttonSize.y() * (this.toggles.size() + 1)));
		else if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) setSize((buttonSize.x() * (this.toggles.size() + 1)), buttonSize.y());
		else {Console.err("GUIPopOutMenu -> initializeRasters() -> invalid case : " + popDirection); return;}
	}
	
	/**Set the font for the options.
	 * @param optionFont - the font to use.
	 * @author 5som3*/
	public void setToggleFont(Font optionFont) {
		this.toggleFont = optionFont;
		updateGraphics();
	}
	
	/**Set the font for the label. 
	 * (The button that hides / shows the menu options).
	 * @param labelFont - the font to use.
	 * @author 5som3*/
	public void setLabelFont(Font labelFont) {
		this.labelFont = labelFont;
		updateGraphics();
	}
	
	/**Set the text size for the options.
	 * @param textSize - the size of the text.
	 * @author 5som3*/
	public void setToggleTextSize(int textSize) {
		this.toggleFont = new Font(toggleFont.getFontName(), toggleFont.getStyle(), textSize);
		updateGraphics();
	}

	/**Set the text size for the label.
	 * (The button that hides / shows the menu options).
	 * @param textSize - the size of the text.
	 * @author 5som3*/
	public void setLabelTextSize(int textSize) {
		this.labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(), textSize);
		updateGraphics();
	}
	
	/**Set the background color for the label.
	 * (The button that hides / shows the menu options).
	 * @param labelColor - the color behind the label text.
	 * @author 5som3*/
	public void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
		updateGraphics();
	}
	
	/**Set the background color for the toggles when they are off.
	 * @param toggleOffColor - the color behind the option text.
	 * @author 5som3*/
	public void setToggleOffColor(Color toggleOffColor) {
		this.toggleOffColor = toggleOffColor;
		updateGraphics();
	}
	
	/**Set the background color for the toggles when they are off.
	 * @param toggleOffColor - the color behind the option text.
	 * @author 5som3*/
	public void setToggleOnColor(Color toggleOnColor) {
		this.toggleOffColor = toggleOnColor;
		updateGraphics();
	}
	
	/**Set the text color of the options.
	 * @param optionTextColor - the text color.
	 * @author 5som3*/
	public void setOptionTextColor(Color optionTextColor) {
		this.toggleTextColor = optionTextColor;
		updateGraphics();
	}
	
	/**Set the color of the label text.
	 * (The button that hides / shows the menu options).
	 * @param labelTextColor - the color for the label text.
	 * @author 5som3*/
	public void setLabelTextColor(Color labelTextColor) {
		this.labelTextColor = labelTextColor;
		updateGraphics();
	}
	
	/**Set the colour for the borders.
	 * @param borderColor - the color for borders.
	 * @apiNote For no border color setBorderThickness(0);
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/**Set the color of buttons when they are pressed.
	 * @param pressColor - the color to overlay buttons when they are pressed.
 	 * @apiNote You should use a hover color with an alpha of about 100, using a full color will obscure the text.
	 * @author 5som3*/
	public void setPressColor(Color pressColor) {
		this.pressColor = pressColor;
		updateGraphics();
	}
	
	/**The color of buttons when they are hovered by the mouse
	 * @param hoverColor - the color to draw over the button when hovered
	 * @apiNote You should use a hover color with an alpha of about 100, using a full color will obscure the text
	 * @author 5som3*/
	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
		updateGraphics();
	}
	
	/**Set the thickness of the borders.
	 * @param borderThickness - the thickness of borders in pixels.
	 * @apiNote set 0 borderThickness for no borders.
	 * @author 5som3*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0 || borderThickness > getHeight() / 4 || borderThickness > getWidth() / 4) {
			Console.err("GUIPopOutMenu -> setBorderThickness() -> borderThickness out of bounds : " + borderThickness);
			borderThickness = 2;
		}
		updateGraphics();
	}
	
	/**Set the offset for the label text
	 * (The button that hides / shows the menu options).
	 * @param xOffset - the xOffset in pixels.
	 * @param yOffset - the yOffset in pixels.
	 * @author 5som3*/
	public void setLabelTextOffset(int xOffset, int yOffset) {
		this.labelTextOffset.set(xOffset, yOffset);
		updateGraphics();
	}
	
	/**Set the offset for the option text
	 * @param xOffset - the xOffset in pixels.
	 * @param yOffset - the yOffset in pixels.
	 * @author 5som3*/
	public void setToggleTextOffset(int xOffset, int yOffset) {
		this.toggleTextOffset.set(xOffset, yOffset);
		updateGraphics();
	}
	
	/**Set the size for each of the buttons in the menu. (including the label).
	 * @param width - the width of a button in pixels.
	 * @param height - the height of a button in pixels.
	 * @author 5som3*/
	public void setButtonSize(int width, int height) {
		buttonSize.set(width, height);
		updateGraphics();
	}
	
	/**Set the direction for the button to pop out.
	 * @param popDirection - the direction to pop out, possible directions : up, down, left, right.
	 * @apiNote If using offset with the PopoutMenu you will need to call offset() after changing the popDirection.
	 * @author 5som3*/
	public void setPopDirection(DirectionType popDirection) {
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN && popDirection != DirectionType.LEFT && popDirection != DirectionType.RIGHT) {
			Console.err("GUIPopOutMenu -> SetPopDirection() -> invalid pop Direction : " + popDirection); 
			return;
		}		
		
		if(this.popDirection != popDirection) {
			if(this.popDirection == DirectionType.UP)   super.setOffset(getOffsetX(), getOffsetY() + toggles.size() * buttonSize.y());
			if(this.popDirection == DirectionType.LEFT) super.setOffset(getOffsetX() + toggles.size() * buttonSize.x(), getOffsetY());
			
			if(popDirection == DirectionType.UP)   super.setOffset(getOffsetX(), getOffsetY() - toggles.size() * buttonSize.y());
			if(popDirection == DirectionType.LEFT) super.setOffset(getOffsetX() - toggles.size() * buttonSize.x(), getOffsetY());
		}
		
		this.popDirection = popDirection;
		initializeRasters();
		updateGraphics();
	}
	
	/**Shows the options for this menu
	 * @author 5som3*/
	public void popOut() {
		if(!isPoppedOut) {			
			isPoppedOut = true;
			updateGraphics();
			Console.ln("GUIPopOutMenu -> popOut()");
		}
	}
	
	/**Hides the options for this menu
	 * @author 5som3*/
	public void pullIn() {
		if(isPoppedOut) {		
			isPoppedOut = false;
			updateGraphics();
			Console.ln("GUIPopOutMenu -> pullIn()");
		}
	}
	
	/**Toggle the visibility of the menu options. 
	 * If hidden the menu will be visible. 
	 * If visible the menu will be hidden.
	 * @author 5som3*/
	public void togglePop() {if(isPoppedOut) pullIn(); else popOut();}
	
	/**Set the states for the toggles in this menu.
	 * @param states - a list of the states for this menu.
	 * @implNote will not trigger any of the toggleOn / toggleOff actions to occur.
	 * @author 5som3*/
	public void setToggleStates(boolean... states) {
		for(int i = 0; i < states.length; i++) {
			if(i >= toggles.size()) {
				Console.err("GUIPopOutToggle -> setToggleStates() -> size of states exceeds number of toggles in the menu");
				break;
			}
			toggles.get(i).setState(states[i]);
		}
		updateGraphics();
	}
	
	private void updateGraphics() {		
		Graphics g = getGraphics();
		
		switch(popDirection) {
		case DOWN:
			for(int i = 0; i < toggles.size() + 1; i++) {
				if(i == 0) {
					g.setColor(borderColor);
					g.fillRect(0, 0, buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setFont(labelFont);
					g.setColor(labelTextColor);
					g.drawString(menuLabel, labelTextOffset.x(), labelTextOffset.y());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
					if(toggles.get(i - 1).isToggled()) g.setColor(toggleOnColor);
					else g.setColor(toggleOffColor);
					g.fillRect(borderThickness, i * buttonSize.y() + borderThickness,  buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(toggleTextColor);
					g.setFont(toggleFont);
					g.drawString(toggles.get(i - 1).getName(), toggleTextOffset.x(), i * buttonSize.y() + toggleTextOffset.y());
				}
				
				if(hoveredIndex >= 0 && i == hoveredIndex) {
					if(buttonState == ButtonStateType.PRESSED) g.setColor(pressColor);
					else g.setColor(hoverColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
				}
			}			
			break;
		case RIGHT:
			for(int i = 0; i < toggles.size() + 1; i++) {
				if(i == 0) {
					g.setColor(borderColor);
					g.fillRect(0, 0, buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setFont(labelFont);
					g.setColor(labelTextColor);
					g.drawString(menuLabel, labelTextOffset.x(), labelTextOffset.y());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(i * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
					if(toggles.get(i - 1).isToggled()) g.setColor(toggleOnColor);
					else g.setColor(toggleOffColor);
					g.fillRect(i * buttonSize.x() + borderThickness, borderThickness,  buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(toggleTextColor);
					g.setFont(toggleFont);
					g.drawString(toggles.get(i - 1).getName(),  i * buttonSize.x() + toggleTextOffset.x(), toggleTextOffset.y());
				}
				if(hoveredIndex >= 0 && i == hoveredIndex) {
					if(buttonState == ButtonStateType.PRESSED) g.setColor(pressColor);
					else g.setColor(hoverColor);
					g.fillRect(i * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
				}
			}
			break;
		case UP:
			for(int i = 0; i < toggles.size() + 1; i++) {
				if(i == toggles.size()) {
					g.setColor(borderColor);
					g.fillRect(0, toggles.size() * buttonSize.y(), buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(borderThickness, toggles.size() * buttonSize.y() + borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(labelTextColor);
					g.setFont(labelFont);
					g.drawString(menuLabel, labelTextOffset.x(), toggles.size() * buttonSize.y() + labelTextOffset.y());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
					if(toggles.get(i).isToggled()) g.setColor(toggleOnColor);
					else g.setColor(toggleOffColor);
					g.fillRect(borderThickness, i * buttonSize.y() + borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(toggleTextColor);
					g.setFont(toggleFont);
					g.drawString(toggles.get(i).getName(), toggleTextOffset.x(), i * buttonSize.y() + toggleTextOffset.y());
				}
				if(hoveredIndex >= 0 && i == hoveredIndex) {
					if(buttonState == ButtonStateType.PRESSED) g.setColor(pressColor);
					else g.setColor(hoverColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
				} 
			}
			break;
		case LEFT:
			for(int i = 0; i < toggles.size() + 1; i++) {
				if(i == toggles.size()) {
					g.setColor(borderColor);
					g.fillRect(toggles.size() * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(toggles.size() * buttonSize.x() + borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(labelTextColor);
					g.setFont(labelFont);
					g.drawString(menuLabel, toggles.size() * buttonSize.x() + labelTextOffset.x(), labelTextOffset.y());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(i * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
					if(toggles.get(i).isToggled()) g.setColor(toggleOnColor);
					else g.setColor(toggleOffColor);
					g.fillRect(i * buttonSize.x() + borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(toggleTextColor);
					g.setFont(toggleFont);
					g.drawString(toggles.get(i).getName(), i * buttonSize.x() + toggleTextOffset.x(), toggleTextOffset.y());
				}
				if(hoveredIndex >= 0 && i == hoveredIndex) {
					if(buttonState == ButtonStateType.PRESSED) g.setColor(pressColor);
					else g.setColor(hoverColor);
					g.fillRect(i * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
				}
			}
			break;
		default:
			Console.ln("GUIPopOutMenu -> updateGraphics() -> invalid popDirection : " + popDirection);
			break;
		}
		
		g.dispose();
		pushGraphics();
	}

	
	
	
	@Override
	protected void mouseEntered() {
		buttonState = ButtonStateType.HOVERED;	
	}

	@Override
	protected void mouseExited() {
		Console.ln("GUIPopOutMenu -> mouseExited()");
		buttonState = ButtonStateType.PLAIN;
		hoveredIndex = -1;
		if(isPoppedOut)	pullIn();
		else updateGraphics();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.HOVERED;
		
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN)    index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y());		
		if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x());

		if(index != hoveredIndex) {
			hoveredIndex = index;
			updateGraphics();
		}
		
	}


	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.PRESSED;
		
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN)    index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y());		
		if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x());
		
		if(index != hoveredIndex) {
			hoveredIndex = index;
			updateGraphics();
		}
		
	
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.PLAIN;
		
		if(popDirection == DirectionType.UP)    {
			index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y());
			if(index == toggles.size()) togglePop();	
			else {
				toggles.get(index).toggle();
				hoveredIndex = -1;
				pullIn();
			}
		}
		
		if(popDirection == DirectionType.DOWN)    {
			index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y());
			if(index == 0) togglePop();
			else {
				toggles.get(index - 1).toggle();
				hoveredIndex = -1;
				pullIn();
			}
		}
		
		if(popDirection == DirectionType.LEFT) {
			index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x());
			if(index == toggles.size())togglePop();
			else {
				toggles.get(index).toggle();
				hoveredIndex = -1;
				pullIn();
			}
		}
		
		if(popDirection == DirectionType.RIGHT) {
			index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x());
			if(index == 0) togglePop();
			else {
				toggles.get(index - 1).toggle();
				hoveredIndex = -1;
				pullIn();
			}
		}		
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		int index = -1;
		buttonState = ButtonStateType.HOVERED;
		
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN)    index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y());		
		if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x());
		
		if(index != hoveredIndex) {
			hoveredIndex = index;
			updateGraphics();
		}
	}
	
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
}
