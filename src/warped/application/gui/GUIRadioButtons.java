/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import warped.audio.FrameworkAudio;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.sprite.ToggleSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.FontMatrix.FontStyleType;

public class GUIRadioButtons extends WarpedGUI {
	
	/*GUIRadioButtons provides these functions
	 * 		- a visual display of multiple options where only one can be true at a time.
	 * 		- programmable action for each of the options added.
	 * 		- automatically resizes the sprite to fit the set number of options.
	 * 		- optional text displayed next to buttons.
	 * 		- Extremely useful when adding options to menus where only one can be true i.e. character select, resolution select, brush select 
	 */
	private AxisType axis = AxisType.VERTICAL;

	private ArrayList<String> buttonNames = new ArrayList<>();
	private ArrayList<WarpedAction> buttonActions = new ArrayList<>();
	private ToggleSprite toggleSprite;
	
	private VectorI textOffset = new VectorI(); //relative to each button
	
	private int horizontalPadding = 10; // padding between buttons horizontally
	private int verticalPadding = 10; //padding between buttons vertically
	
	private int borderThickness = 2;
	private int trueIndex = 0;
	private int hoveredIndex = -1;
	
	private boolean isBackgroundVisible = true;
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color borderColor = Color.BLACK;
	
	private VectorI buttonSize = new VectorI();
	
	private Color textColor = Color.WHITE;
	private Font textFont = fontMatrix.getDynamic();
	private FontStyleType fontStyle = FontStyleType.REGULAR;

	
	public GUIRadioButtons(WarpedAction... buttonActions) {		
		if(buttonActions.length == 0) {
			Console.err("GUIRadioButtons -> GUIRadioButtons() -> buttonActions must contain at least one action");
			return;
		}
		for(int i = 0 ; i < buttonActions.length; i++) {
			this.buttonNames.add("");
			this.buttonActions.add(buttonActions[i]);
			toggleSprite = new ToggleSprite(FrameworkSprites.getStandardIcon(StandardIcons.RADIO_CHECKED), FrameworkSprites.getStandardIcon(StandardIcons.RADIO_UNCHECKED));
		}
		buttonSize.set(30, 30);
		updateRadioSize();
		updateGraphics();
	}
	
	public GUIRadioButtons(List<String> buttonNames, List<WarpedAction> buttonActions) {
		if(buttonNames.size() != buttonActions.size()) Console.err("GUIRadioButtons -> GUIRadioButtons() -> size of buttonNames and buttonActions do not match");
		
		for(int i = 0 ; i < buttonActions.size(); i++) {
			this.buttonNames.add(buttonNames.get(i));
			this.buttonActions.add(buttonActions.get(i));
			toggleSprite = new ToggleSprite(FrameworkSprites.getStandardIcon(StandardIcons.RADIO_CHECKED), FrameworkSprites.getStandardIcon(StandardIcons.RADIO_UNCHECKED));
		}
		buttonSize.set(30, 30);
		updateRadioSize();
		updateGraphics();
	}
	
	public GUIRadioButtons(List<String> buttonNames, List<WarpedAction> buttonActions, BufferedImage radioOn, BufferedImage radioOff) {
		if(buttonNames.size() != buttonActions.size()) Console.err("GUIRadioButtons -> GUIRadioButtons() -> size of buttonNames and buttonActions do not match");
		if(radioOn.getWidth() != radioOff.getWidth() || radioOn.getHeight() != radioOff.getHeight()) Console.err("GUIRadioButtons -> GUIRadioButtons() -> size of toggle images do not match");
		
		for(int i = 0 ; i < buttonActions.size(); i++) {
			this.buttonNames.add(buttonNames.get(i));
			this.buttonActions.add(buttonActions.get(i));
			toggleSprite = new ToggleSprite(FrameworkSprites.getStandardIcon(StandardIcons.RADIO_CHECKED), FrameworkSprites.getStandardIcon(StandardIcons.RADIO_UNCHECKED));
		}
		buttonSize.set(radioOn.getWidth(), radioOn.getHeight());
		updateRadioSize();
		updateGraphics();
	}

	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		textFont = fontMatrix.getDynamic(fontStyle, textFont.getSize());
	}

	/**Set the axis that the buttons will spread across.
	 * @param axis - the axis that will be applied.
	 * @author SomeKid*/
	public void setAxis(AxisType axis) {
		this.axis = axis;
		updateRadioSize();
		updateGraphics();
	}
	
	/**The padding applied around the radio buttons
	 * @param verticalPadding - will be applied beneath each button, if setAxis() is vertical this will be the button spacing
	 * @param horizontalPadding - will be applied to the right of each button, if setAxis() is horizontal this will be the button spacing
	 *@author SomeKid*/
	public void setButtonPadding(int horizontalPadding, int verticalPadding) {
		this.verticalPadding = verticalPadding;
		this.horizontalPadding = horizontalPadding;
		updateRadioSize();
		updateGraphics();
	}
		
	/**Set the currently selected radio button.
	 * @param index - the index of the button to set.
	 * @apiNote This will not cause the button action to trigger, you should use this function if you want to set the default value for the radio buttons
	 * @author SomeKid*/
	public void setSelectButton(int index) {
		if(buttonActions.size() == 0) {
			Console.err("GUIRadioButtons -> setSelectButton() -> radio buttons have not been initialised");
			return;
		}
		if(index < 0 || index >= buttonActions.size()) {
			Console.err("GUIRadioButtons -> setSelectButton() -> index is out of bounds : " + index);
			return;
		}
		this.trueIndex = index;
		updateGraphics();
	}
	
	/**Get the index of the currently selected button.
	 * @return int - the index that is true.
	 * @author SomeKid*/
	public int getSelectedButtonIndex() {return trueIndex;}
	
	/**Set the color of the border
	 * @param color - the color to use, for no border color set borderThickness to 0
	 * @implNote Do not use colors with alpha less than 255.
	 * @implNote The background and border are used to clear graphic data and this could be broken with semi transparent colors
	 * @author SomeKid*/
	public void setBorderColor(Color color) {
		this.borderColor = color;
		updateGraphics();
	}
	
	/**Set the names for each radio button.
	 * @param names - the names to set the radio buttons.
	 * @author 5som3*/
	public void setButtonNames(String... names) {
		if(names.length > buttonActions.size()) Console.err("GUIRadioButtons -> setButtonNames() -> setting more names than there are buttons, extra names will be ignored");
		for(int i = 0; i < names.length; i++) {
			buttonNames.set(i, names[i]);
		}
		
	}
	
	/**Set the color of the background
	 * @param color - the color to use
	 * @implNote Do not use colors with alpha less than 255.
	 * @implNote The background and border are used to clear graphic data and this could be broken with semi transparent colors
	 * @author SomeKid*/
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
		updateGraphics();
	}
	
	/**Set if the background and background border will be rendered.
	 * @param isBackgroundVisible - if true the background and background border will be rendered.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackgroundVisible) {
		this.isBackgroundVisible = isBackgroundVisible;
		updateGraphics();
	}
	
	/**Set the offset of the text displayed next to the radio buttons.
	 * @param x, y - the coordinates measured in pixels from the top left corner for each of the corresponding buttons
	 * @author SomeKid*/
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
		updateGraphics();
	}
	
	/**Set the size of the text displayed next to the radio buttons. 
	 * @param size - the size of the text, must be at least 1
	 * @author SomeKid*/
	public void setTextSize(int size) {
		if(size < 1) { 
			Console.err("GUIRadioButtons -> setTextSize() -> invalid size : " + size);
			size = 12;
		}
		textFont = new Font(textFont.getFontName(), textFont.getStyle(), size);
		updateGraphics();
	}
		
	/**Set the font of the text displayed next to the radio buttons.
	 * @param font - the font to use
	 * @author SomeKid*/
	public void setTextFont(Font font) {
		this.textFont = font;
		updateGraphics();
	}
	
	/**Set the color of the text displayed next to the radio buttons.
	 * @param color - the color to use
	 * @author SomeKid*/
	public void setTextColor(Color color) {
		this.textColor = color;
		updateGraphics();
	}
	
	private void updateRadioSize() {
		if(axis == AxisType.HORIZONTAL) setSize((buttonSize.x() + horizontalPadding) * buttonActions.size(), buttonSize.y() + verticalPadding + textFont.getSize());
		else setSize(buttonSize.x() + horizontalPadding, (buttonSize.y() + verticalPadding) * buttonActions.size() + textFont.getSize());
	}


	public void updateGraphics() {
		Graphics g = getGraphics();
		
		if(isBackgroundVisible) {			
			if(borderThickness > 0) {
				g.setColor(borderColor);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			
			g.setColor(backgroundColor);
			g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
		}
		
		g.setColor(textColor);
		for(int i = 0; i < buttonActions.size(); i++) {
			int buttonX = 0;
			int buttonY = g.getFontMetrics().getMaxAscent();
			
			if(axis == AxisType.HORIZONTAL) buttonX = i * (buttonSize.x() + horizontalPadding);    				
			else buttonY =  i * (buttonSize.y() + verticalPadding) + g.getFontMetrics().getMaxAscent();
			
			if(i == trueIndex) {
				if(i == hoveredIndex) g.drawImage(toggleSprite.getOnHoveredRaster(), buttonX, buttonY, buttonSize.x(), buttonSize.y(), null);
				else g.drawImage(toggleSprite.getOnRaster(), buttonX, buttonY, buttonSize.x(), buttonSize.y(), null);
			} else {
				if(i == hoveredIndex) g.drawImage(toggleSprite.getOffHoveredRaster(), buttonX, buttonY, buttonSize.x(), buttonSize.y(), null);
				else g.drawImage(toggleSprite.getOffRaster(), buttonX, buttonY, buttonSize.x(), buttonSize.y(), null);
			}
						
			g.setFont(textFont);
			g.drawString(buttonNames.get(i), buttonX + textOffset.x(), buttonY + textOffset.y());	
		}
		
		g.dispose();
		pushGraphics();
	}
	
	//--------
	//---------------- Interaction --------
	//--------
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int index = -1;
		
		if(axis == AxisType.HORIZONTAL) index = (int) Math.floor(mouseEvent.getPointRelativeToObject().getX() / (buttonSize.x() + horizontalPadding));
		else index = (int) Math.floor(mouseEvent.getPointRelativeToObject().getY() / (buttonSize.y() + verticalPadding));
		
		if(index < 0 || index >= buttonActions.size()) return;
		if(hoveredIndex != index) {			
			hoveredIndex = index;
			updateGraphics();
			FrameworkAudio.defaultHover.play();
		}
	}
	
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int index = -1;
		
		if(axis == AxisType.HORIZONTAL) index = (int) Math.floor(mouseEvent.getPointRelativeToObject().getX() / (buttonSize.x() + horizontalPadding));
		else index = (int) Math.floor(mouseEvent.getPointRelativeToObject().getY() / (buttonSize.y() + verticalPadding));
		
		if(index < 0 || index >= buttonActions.size()) return;
		trueIndex = index;
		buttonActions.get(index).action();
		updateGraphics();
		Console.ln("GUIRadioButtons -> mouseReleased() -> radio buttons set : " + index);
	}
	
	@Override
	protected void mouseEntered() {return;}
	@Override
	protected void mouseExited() {FrameworkAudio.defaultUnhover.play();}
	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	
	
	
}
