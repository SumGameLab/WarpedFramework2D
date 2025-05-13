package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import warped.application.actionWrappers.ActionOption;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.window.WarpedMouse;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIListVertical extends WarpedGUI {
	
	/*GUIListVertical provides these functionalities: 
	 * 
	 * 		- Display a list of selectable options.
	 * 		- Each option can have unique functionality.
	 * 		- Set the border, background and text color for each option.
	 * 		- Set the font for the options.
	 * 		- Set the size of the list.
	 * 		- If options are too big to fit in the list a scroll bar will be added automatically. 	
	 * 
	 * */
	
	private int hoverIndex = 0;

	private int buttonHeight = 30;
	
	private ArrayList<ActionOption> options; 
	private ArrayList<Boolean> optionLocks;
		
	private boolean isAutoHidden = false;
	private boolean isScrollBarVisible = false;
	
	private WarpedAction enterAction = null;
	private WarpedAction exitAction = null;
	
	private Color scrollBarColor 	= Colour.GREY_DARK_DARK.getColor();
	private Color scrollButtonColor = Colour.GREY_LIGHT.getColor();
	
	private int scrollBarThickness = 5;
	private int scrollButtonHeight = getHeight();
	private int scrollButtonPosition = 0;
	
	private double scrollProgress = 0.0;
	private int scrollMax = 0;
	private int scrollOffset = 0;
	
	private int buttonPadding = 2;
	private VectorI textOffset = new VectorI(1,1);
	
	private Color paddingColor = Color.BLACK;
	private Color buttonColor = Color.DARK_GRAY;
	private Color buttonHoverColor = new Color(110, 60, 60, 60);
	private Color lockColor = new Color(30, 30, 30, 180);
	
	private Color textColor = Color.YELLOW;
	private Font font 		= UtilsFont.getPreferred();
	
	
	/**A vertical list with the default parameters.
	 * @author 5som3*/
	public GUIListVertical() {
		updateGraphics();
	}
	
	/**A vertical list with the specified parameters.
	 * @param width - The width of the list in pixels. (also the width of each button). 
	 * @param height - The maximum height of the list in pixels. (list may appear smaller if the number of buttons don't fill the list). 
	 * @author 5som3*/
	public GUIListVertical(int width, int height) {
		setListSize(width, height);
	}
	
	
	/**A vertical list with the specified parameters.
	 * @param width - The width of the list in pixels. (also the width of each button). 
	 * @param height - The maximum height of the list in pixels. (list may appear smaller if the number of buttons don't fill the list). 
	 * @author 5som3*/
	public GUIListVertical(int width, int height, int buttonHeight) {
		this.buttonHeight = buttonHeight;
		setListSize(width, height);
		
	}
	
	/**Set the size of the list
	 * @param width - The width of the list in pixels. (also the width of each button). 
	 * @param height - The maximum height of the list in pixels. (list may appear smaller if the number of buttons don't fill the list). 
	 * @author 5som3*/
	public void setListSize(int width, int height) {
		setSize(width, height);
		updateScrollParameters();
		updateGraphics();
	}
	
	/**Will make the list invisible after an option has been selected.
	 * @author 5som3*/
	public void autoHide() {isAutoHidden = true;}
	
	/**Set if the list should be hidden after an entry is selected.
	 * @param autoHide - if true the list will be hidden once an option is selected. 
	 * @author 5som3*/
	public void setAutoHide(boolean autoHide) {isAutoHidden = autoHide;}
	
	/**Set the color of the button padding.
	 * @param b
	 * */
	public void setPaddingColor(Color borderColor) {
		this.paddingColor = borderColor;
		updateGraphics();
	}
	
	/**Set the color behind the option text.
	 * @param buttonColor - the color of each button.
	 * @author 5som3*/
	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
		updateGraphics();
	}
	
	/**Set the color of the option text in each button.l
	 * @param textColor - the color of each option in the list
	 * @author 5som3*/
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	/**Will set the thickness of the padding that surrounds each button.
	 * @param buttonPadding - the padding in pixels, must be positive or 0.
	 * @apiNote setButtonPadding(0) for no button padding.
	 * @author 5som3*/
	public void setButtonPadding(int buttonPadding) {
		if(buttonPadding < 0) {
			buttonPadding = 0;
			Console.err("GUIListVertical -> setButtonPadding() -> button padding must be positive or 0");
		}
		this.buttonPadding = buttonPadding;
		updateGraphics();
	}
	
	/**Set an action to trigger when the mouse enters the list.
	 * @param enterAction - the action to trigger when the mouse enters the list
	 * @author 5som3*/
	public void setEnterAction(WarpedAction enterAction) {this.enterAction = enterAction;}
	
	/**Set an action to trigger when the mouse leaves the list.
	 * @param enterAction - the action to trigger when the mouse enters the list
	 * @author 5som3*/
	public void setExitAction(WarpedAction exitAction) {this.exitAction = exitAction;}

	/**Get then number of options in the list
	 * @return int - the number of options.
	 * @author 5som3*/
	public int optionCount() {return options.size();}
	
	/**Set the offset of the option text inside of each button on the list.
	 * @param x - the x offset of the text in pixels.
	 * @param y - the y offset of the text in pixels.
	 * @apiNote The offset is measured from the top left corner of the button to the bottom left corner of the buttons respective text. 
	 * @author 5som3*/
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
		updateGraphics();
	}
	
	/**Set the size of the text for each option in the list.
	 * @param size - the size of the font.
	 * @author 5som3*/
	public void setTextSize(int size) {
		font = new Font(font.getFontName(), font.getStyle(), size);
		updateGraphics();
	}
	
	/**Set the size of the option buttons in the list.
	 * @param width - the width of each option button in pixels.
	 * @param height - the height of each option button in pixels.
	 * @apiNote The width of the button also defines the width of the list.
	 * @author 5som3*/	
	public void setButtonSize(int width, int height) {
		setSize(width, getHeight());
		buttonHeight = height;
		updateScrollParameters();
		updateGraphics();
	}
		
	/**Set the options that this list will contain.
	 * @param options - an array list of options that the list will be display.
	 * @implNote Will update the scroll parameters and graphics.
	 * @author 5som3*/
	public void setOptions(ArrayList<ActionOption> options) {
		this.options = options;
		updateScrollParameters();
		updateGraphics();
	}
	
	/**Set the options that this list will contain.
	 * @param options - a list of options that the list will be display.
	 * @implNote Will update the scroll parameters and graphics.
	 * @author 5som3*/
	public void setOptions(ActionOption... options) {setOptions(new ArrayList<>(Arrays.asList(options)));}
	
	/**Clear all options for the list.
	 * @implNote will update the scroll parameters and graphics.
	 * @author 5som3*/
	public void clear() {
		if(options == null) options = new ArrayList<ActionOption>();
		else options.clear();
		updateScrollParameters();
		updateGraphics();
	}
	
	/**The height of each button in the list.
	 * @return int - the height of the button in pixels
	 * @author 5som3*/
	public int getButtonHeight() {return buttonHeight;}
	
	/**Set the options for this list as well as an array that determines which options are interactive in the list.
	 * @param options - a list of options to display in this gui.
	 * @param optionLocks - a list of booleans that will control which of the options is interactive.
	 * @apiNote OptionLocks are used when you want to show a menu contains an option but it is currently unavailable.
	 * @author 5som3*/
	public void setOptions(ArrayList<ActionOption> options, ArrayList<Boolean> optionLocks) {
		if(options.size() != optionLocks.size()) {
			Console.err("GUIListBUtton -> setOptions() -> options and optionLocks are not equal size");
			return;
		}
		this.options = options;
		this.optionLocks = optionLocks;
		updateScrollParameters();
		updateGraphics();
	}
	
	private void updateScrollButtonPosition() {
		scrollButtonPosition = (int)(getHeight() * scrollProgress) - scrollButtonHeight / 2;
		if(scrollButtonPosition < 0) scrollButtonPosition = 0;
		if(scrollButtonPosition > getHeight() - scrollButtonHeight) scrollButtonPosition = getHeight() - scrollButtonHeight;
	}
	
	private void updateScrollParameters() {
		if(options == null) {
			scrollMax = 0;
			return;
		}
		scrollOffset = 0;
		scrollMax = options.size() * (buttonHeight + buttonPadding) - getHeight();
		if(scrollMax < 0) {
			scrollMax = 0;
			isScrollBarVisible = false;
		} else isScrollBarVisible = true;
		
	}
	
	private void updateGraphics() {		
		if(options == null) return;

		Graphics g = getGraphics();		
		
		for(int i = 0; i < optionCount(); i++) {	// draw each button
			ActionOption option = options.get(i);
			
			int drawY = (i * (buttonPadding + buttonHeight)) - scrollOffset;
			int drawWidth = getWidth() - scrollBarThickness;
			
			if(drawY + buttonHeight < 0 || drawY > getHeight()) continue;
			
			g.setColor(paddingColor);
			g.fillRect(0, drawY, drawWidth, buttonHeight);
			
			g.setColor(buttonColor);
			g.fillRect(buttonPadding, buttonPadding, drawWidth - buttonPadding * 2, buttonHeight - buttonPadding * 2);
			
			g.setFont(font);
			g.setColor(textColor);
			g.drawString(option.getName(), textOffset.x(), drawY + textOffset.y() + font.getSize());
			
			if(i == hoverIndex) {
				if(optionLocks != null && optionLocks.get(i));					
				else {
					g.setColor(buttonHoverColor);
					g.fillRect(0, drawY, drawWidth, buttonHeight);
				}
			}			
			
			if(optionLocks != null && optionLocks.get(i)) {
				g.setColor(lockColor);
				g.fillRect(0, drawY, drawWidth, buttonHeight);
			}
		}
		
		if(isScrollBarVisible) {
			g.setColor(scrollBarColor);
			g.fillRect(getWidth() - scrollBarThickness, 0, scrollBarThickness, getHeight()); // draw scroll bar
			
			g.setColor(scrollButtonColor);	
			g.fillRect(getWidth() - scrollBarThickness, scrollButtonPosition, scrollBarThickness, scrollButtonHeight);			
		}
		
		g.dispose();
		pushGraphics();
	}


	
	//--------
	//---------------- Interaction --------
	//--------
	@Override
	protected void mouseEntered() {if(enterAction!= null) enterAction.action();}
	@Override
	protected void mouseExited() {
		hoverIndex = -1;
		if(exitAction != null) exitAction.action();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int index = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), buttonHeight);

		if(index != hoverIndex) {
			hoverIndex = index;
			updateGraphics();
		}		
	}


	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int index = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), buttonHeight + buttonPadding);
		Console.ln("GUIScrollButtonMenu -> mouseReleaseed -> select index : " + index);
		if(index < 0 || index >= options.size()) return;
		else if(optionLocks != null && optionLocks.get(index)) return;
		options.get(index).action();
		if(isAutoHidden) setVisible(false);
	}
	
	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(WarpedMouse.isFocused()) return;
		if(scrollMax <= 0) return;
		Point p = mouseEvent.getPointRelativeToObject();
		scrollProgress = p.y / (double)getHeight();
		scrollProgress = UtilsMath.clamp(scrollProgress, 0.0, 1.0);
		scrollOffset = (int)(scrollMax * scrollProgress);
		
		int index = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), buttonHeight);
		if(index >= 0 && index < options.size()) hoverIndex = index;
			
		updateScrollButtonPosition();
		updateGraphics();	
		
	}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

}
