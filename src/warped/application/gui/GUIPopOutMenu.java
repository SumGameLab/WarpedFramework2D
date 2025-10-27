/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import warped.application.actionWrappers.ActionOption;
import warped.application.gui.GUIButton.ButtonStateType;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.FontMatrix.FontStyleType;

public class GUIPopOutMenu extends WarpedGUI {
	
	/*GUIPopOutMenu provides these functionalities
	 * 			
	 * 		- A permanently visible button that controls the visibility of a menu.
	 * 		- Has a menu of buttons that can be shown / hidden by the main button. 
	 * 		- Menu options can be set during runtime.
	 * 		- Options have a single action that will trigger once each time the option is selected.
	 * 		- Buttons can have the colour set for their background, text and hover effect.
	 * 		
	 */

	private boolean isPoppedOut = false;

	private DirectionType popDirection = DirectionType.DOWN;
	private ButtonStateType buttonState = ButtonStateType.HOVERED;

	private int hoveredIndex = -1;
	private VectorI buttonSize = new VectorI(90, 30);
	
	private String menuLabel = "default";
	private ArrayList<ActionOption> options = new ArrayList<>(); 
		
	private int borderThickness 	= 2;
	
	private VectorI labelTextOffset = new VectorI(20, 4);
	private VectorI optionTextOffset= new VectorI(10, 4);
	
	private Color borderColor 		= Colour.BLACK.getColor();
	private Color optionColor 		= Colour.GREY_DARK.getColor();
	private Color optionTextColor	= Color.WHITE;
	private Color labelTextColor  	= Color.YELLOW;
	private Color labelColor      	= Colour.GREY_LIGHT.getColor();
	private Color hoverColor 	= Colour.RED_DARK.getColor(100); 
	private Color pressColor 	= Colour.RED.getColor(100);
	
	private Font optionFont 		= fontMatrix.getDynamic();
	private Font labelFont 			= fontMatrix.getDynamic();
	private FontStyleType fontStyle = FontStyleType.REGULAR;
		
	/**A menu with the specified label and no options.
	 * @param menuLabel - the label for the button that will show/hide the options.
	 * @author 5som3*/
	public GUIPopOutMenu(String menuLabel) {
		this.menuLabel = menuLabel;
		initializeRasters();
		updateGraphics();
	}
	
	/**A menu with the specified label and options
	 * @param menuLabel - the label for the button that will show/hide the options. 
	 * @param options - the options to show when the menu open.
	 * @author 5som3*/
	public GUIPopOutMenu(String menuLabel, ActionOption... options) {
		this.menuLabel = menuLabel;
		this.options = new ArrayList<>(Arrays.asList(options));
		initializeRasters();
		updateGraphics();
	}
	
	/**A menu that pops in the specified direction with the specified label and options.
	 * @param menuLabel - the label for the button that will show/hide the options.
	 * @param popDirection - the direction that the menu will pop open. 
	 * @param options - the options to show when the menu open.
	 * @author 5som3*/
	public GUIPopOutMenu(String menuLabel, DirectionType popDirection, ActionOption... options) {
		if(popDirection != DirectionType.UP && popDirection != DirectionType.DOWN && popDirection != DirectionType.LEFT && popDirection != DirectionType.RIGHT) {
			Console.err("GUIPopOutToggle -> constructor () -> pop out direction is not a valid direction : " + popDirection);
			popDirection = DirectionType.DOWN;
		}
		this.menuLabel = menuLabel;
		this.options = new ArrayList<>(Arrays.asList(options));
		initializeRasters();
		updateGraphics();
	}
	
	/**Set the options for this menu to display when popped out.
	 * @param options - a list of the options to display.
	 * @author 5som3*/
	public void setOptions(ActionOption... options) {
		this.options = new ArrayList<>(Arrays.asList(options));
		initializeRasters();
		updateGraphics();
	}
	
	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		optionFont = fontMatrix.getDynamic(fontStyle, optionFont.getSize());
		labelFont = fontMatrix.getDynamic(fontStyle, labelFont.getSize());
	}
	
	/**Set the options for this menu to display when popped out.
	 * @param options - an ArrayList of the options to display.
	 * @author 5som3*/
	public void setOptions(ArrayList<ActionOption> options) {
		this.options = options;
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
		if(popDirection == DirectionType.LEFT) offset.set(x - buttonSize.x() * options.size(), y);
		else if(popDirection == DirectionType.UP) offset.set(x, y - buttonSize.y() * options.size());
		else offset.set(x, y);
	}
	
	/**Set the size of the raster to be the maximum needed size.
	 * @author 5som3*/
	private void initializeRasters() {
		if(popDirection == DirectionType.UP || popDirection == DirectionType.DOWN) setSize(buttonSize.x(), (buttonSize.y() * (this.options.size() + 1)));
		else if(popDirection == DirectionType.LEFT || popDirection == DirectionType.RIGHT) setSize((buttonSize.x() * (this.options.size() + 1)), buttonSize.y());
		else {Console.err("GUIPopOutMenu -> initializeRasters() -> invalid case : " + popDirection); return;}
	}
	
	/**Set the font for the options.
	 * @param optionFont - the font to use.
	 * @author 5som3*/
	public void setOptionFont(Font optionFont) {
		this.optionFont = optionFont;
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
	public void setOptionTextSize(int textSize) {
		optionFont = optionFont.deriveFont(Font.PLAIN, textSize);
		updateGraphics();
	}

	/**Set the text size for the label.
	 * (The button that hides / shows the menu options).
	 * @param textSize - the size of the text.
	 * @author 5som3*/
	public void setLabelTextSize(int textSize) {
		labelFont = labelFont.deriveFont(Font.PLAIN, textSize);
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
	
	/**Set the background color for the options.
	 * (The button that hides / shows the menu options).
	 * @param optionColor - the color behind the option text.
	 * @author 5som3*/
	public void setOptionColor(Color optionColor) {
		this.optionColor = optionColor;
		updateGraphics();
	}
	
	/**Set the text color of the options.
	 * @param optionTextColor - the text color.
	 * @author 5som3*/
	public void setOptionTextColor(Color optionTextColor) {
		this.optionTextColor = optionTextColor;
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
	public void setOptionTextOffset(int xOffset, int yOffset) {
		this.optionTextOffset.set(xOffset, yOffset);
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
			if(this.popDirection == DirectionType.UP)   super.setOffset(getOffsetX(), getOffsetY() + options.size() * buttonSize.y());
			if(this.popDirection == DirectionType.LEFT) super.setOffset(getOffsetX() + options.size() * buttonSize.x(), getOffsetY());
			
			if(popDirection == DirectionType.UP)   super.setOffset(getOffsetX(), getOffsetY() - options.size() * buttonSize.y());
			if(popDirection == DirectionType.LEFT) super.setOffset(getOffsetX() - options.size() * buttonSize.x(), getOffsetY());
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
	
	
	
	public void updateGraphics() {		
		Graphics g = getGraphics();
		
		switch(popDirection) {
		case DOWN:
			for(int i = 0; i < options.size() + 1; i++) {
				if(i == 0) {
					g.setColor(borderColor);
					g.fillRect(0, 0, buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setFont(labelFont);
					g.setColor(labelTextColor);
					g.drawString(menuLabel, labelTextOffset.x(), labelTextOffset.y() + g.getFontMetrics().getMaxAscent());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
					g.setColor(optionColor);
					g.fillRect(borderThickness, i * buttonSize.y() + borderThickness,  buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(optionTextColor);
					g.setFont(optionFont);
					g.drawString(options.get(i - 1).getName(), optionTextOffset.x(), i * buttonSize.y() + optionTextOffset.y() + g.getFontMetrics().getMaxAscent());
				}
				
				if(hoveredIndex >= 0 && i == hoveredIndex) {
					if(buttonState == ButtonStateType.PRESSED) g.setColor(pressColor);
					else g.setColor(hoverColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
				}
			}			
			break;
		case RIGHT:
			for(int i = 0; i < options.size() + 1; i++) {
				if(i == 0) {
					g.setColor(borderColor);
					g.fillRect(0, 0, buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setFont(labelFont);
					g.setColor(labelTextColor);
					g.drawString(menuLabel, labelTextOffset.x(), labelTextOffset.y() + g.getFontMetrics().getMaxAscent());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(i * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
					g.setColor(optionColor);
					g.fillRect(i * buttonSize.x() + borderThickness, borderThickness,  buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(optionTextColor);
					g.setFont(optionFont);
					g.drawString(options.get(i - 1).getName(),  i * buttonSize.x() + optionTextOffset.x(), optionTextOffset.y() + g.getFontMetrics().getMaxAscent());
				}
				if(hoveredIndex >= 0 && i == hoveredIndex) {
					if(buttonState == ButtonStateType.PRESSED) g.setColor(pressColor);
					else g.setColor(hoverColor);
					g.fillRect(i * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
				}
			}
			break;
		case UP:
			for(int i = 0; i < options.size() + 1; i++) {
				if(i == options.size()) {
					g.setColor(borderColor);
					g.fillRect(0, options.size() * buttonSize.y(), buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(borderThickness, options.size() * buttonSize.y() + borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(labelTextColor);
					g.setFont(labelFont);
					g.drawString(menuLabel, labelTextOffset.x(), options.size() * buttonSize.y() + labelTextOffset.y() + g.getFontMetrics().getMaxAscent());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
					g.setColor(optionColor);
					g.fillRect(borderThickness, i * buttonSize.y() + borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(optionTextColor);
					g.setFont(optionFont);
					g.drawString(options.get(i).getName(), optionTextOffset.x(), i * buttonSize.y() + optionTextOffset.y() + g.getFontMetrics().getMaxAscent());
				}
				if(hoveredIndex >= 0 && i == hoveredIndex) {
					if(buttonState == ButtonStateType.PRESSED) g.setColor(pressColor);
					else g.setColor(hoverColor);
					g.fillRect(0, i * buttonSize.y(), buttonSize.x(), buttonSize.y());
				} 
			}
			break;
		case LEFT:
			for(int i = 0; i < options.size() + 1; i++) {
				if(i == options.size()) {
					g.setColor(borderColor);
					g.fillRect(options.size() * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
					g.setColor(labelColor);
					g.fillRect(options.size() * buttonSize.x() + borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(labelTextColor);
					g.setFont(labelFont);
					g.drawString(menuLabel, options.size() * buttonSize.x() + labelTextOffset.x(), labelTextOffset.y() + g.getFontMetrics().getMaxAscent());
				} else if(isPoppedOut) {
					g.setColor(borderColor);
					g.fillRect(i * buttonSize.x(), 0, buttonSize.x(), buttonSize.y());
					g.setColor(optionColor);
					g.fillRect(i * buttonSize.x() + borderThickness, borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
					g.setColor(optionTextColor);
					g.setFont(optionFont);
					g.drawString(options.get(i).getName(), i * buttonSize.x() + optionTextOffset.x(), optionTextOffset.y() + g.getFontMetrics().getMaxAscent());
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
			if(index == options.size()) togglePop();	
			else {
				options.get(index).action();
				hoveredIndex = -1;
				pullIn();
			}
		}
		
		if(popDirection == DirectionType.DOWN)    {
			index = (int)Math.floor(mouseEvent.getPointRelativeToObject().y / buttonSize.y());
			if(index == 0) togglePop();
			else {
				options.get(index - 1).action();
				hoveredIndex = -1;
				pullIn();
			}
		}
		
		if(popDirection == DirectionType.LEFT) {
			index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x());
			if(index == options.size())togglePop();
			else {
				options.get(index).action();
				hoveredIndex = -1;
				pullIn();
			}
		}
		
		if(popDirection == DirectionType.RIGHT) {
			index = (int)Math.floor(mouseEvent.getPointRelativeToObject().x / buttonSize.x());
			if(index == 0) togglePop();
			else {
				options.get(index - 1).action();
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
