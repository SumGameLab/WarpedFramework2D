/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.audio.FrameworkAudio;
import warped.functionalInterfaces.StringAction;
import warped.graphics.window.WarpedMouseEvent;
import warped.user.keyboard.WarpedTypeable;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class GUITextInputLine extends WarpedGUI implements WarpedTypeable {
	
	/*GUITextInputLine provides these functions : 
	 * 		
	 * 		- Allows the user to input a string.
	 * 		- Has a button to trigger an action.
	 * 		- Programmable action that occurs when the return key or the button is pressed.
	 * 		- Set the color of text, border, background, hover and input.
	 * 		- Customize the size and position of the button.
	 * */
	
	private ArrayList<Character> keyLog = new ArrayList<>();
	
	private String inputString = "";
	private String prompt = "|";
	private String blankText = "Click here...";
	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

	private int borderThickness   	= 2;
	private int maxCharacters 		= 25;
	
	private boolean isPrompt = true;
	private int tick = 0;
	private static int delay = 40;
	
	private int promptIndex = 0;
	
	private boolean isPressed 		= false;
	private boolean isInput 		= false;
	private boolean isLineHovered   = false;
	private boolean isButtonHovered = false;
	private boolean isButtonVisible = true;
	
	private BufferedImage buttonImage;
	private BufferedImage backgroundImage;
	
	private Color borderColor	  = Color.BLACK;
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color buttonColor 	  = Colour.GREY.getColor();
	private Color hoverColor 	  = Colour.RED.getColor(60);
	private Color pressColor      = Colour.RED_DARK.getColor(60);
	private Color inputTextColor  = Color.WHITE;
	private Color blankTextColor  = Color.YELLOW;
		
	private VectorI buttonSize	 	  = new VectorI( 40, 40);
	private VectorI buttonOffset      = new VectorI(207,  3);
	private VectorI textOffset 		  = new VectorI( 24, 28);

	private StringAction buttonAction = str -> {Console.ln("GUITextInputLine -> inputString :" + inputString);}; 	
	
	/**A text input line with the default parameters.
	 * @author 5som3*/
	public GUITextInputLine() {setSize(250, 46);}
	
	/**A text input line with the specified parameters.
	 * @param width - the width of the text line in pixels.
	 * @param height - the height of the text line in pixels.
	 * @author 5som3*/
	public GUITextInputLine(int width, int height) {setSize(width, height);}
	
	/**A text input line with the specified parameters.
	 * @param width - the width of the text line in pixels.
	 * @param height - the height of the text line in pixels.
	 * @param blankText - the text to display in the line when their is no input text.
	 * @author 5som3*/
	public GUITextInputLine(int width, int height, String blankText) {
		setSize(width, height);
		setBlankText(blankText);
	}
	
	/**Get the inputString.
	 * @return String - the string that the user can edit.
	 * @author 5som3*/
	public String getLine() {return inputString;}
	
	/**Set the text to display when their is no input.
	 * @param text - the text to display.
	 * @author 5som3*/
	public void setBlankText(String blankText) {
		this.blankText = blankText;
		updateGraphics();
	}
	
	/**Set the size of the button that triggers the set buttonAction (if any).
	 * @param width - the width of the button in pixels.
	 * @param height - the height of the button in pixels.
	 * @author 5som3*/
	public void setButtonSize(int width, int height) {
		if(width <= 0 || height <= 0) {
			Console.err("GUITextInputLine -> setButtonSize() -> width and height must be postive");
			if(width <= 0) width = 40; 
			else height = 40;
		}
		buttonSize.set(width, height);
		updateGraphics();
	}
	
	/**Set the offset of the button. 
	 * @param xOffset - the x Offset in pixels.
	 * @param yOffset - the y offset in pixels.
	 * @apiNote offset measured from top left corner of the text line to the top left corner of the button.
	 * @author 5som3*/
	public void setButtonOffset(int xOffset, int yOffset) {
		buttonOffset.set(xOffset, yOffset);
		updateGraphics();
	}
	
	/**Set the offset of the text.
	 * @param xOffset - the x offset in pixels.
	 * @param yOffset - the y offset in pixels.
	 * @apiNote offset measured from top left corner of the text line to the bottom left corner of the text.
	 * @author 5som3*/
	public void setTextOffset(int xOffset, int yOffset) {
		textOffset.set(xOffset, yOffset);
		updateGraphics();
	}
	
	/**Set the border color
	 * @param borderColor - the colour of the border
	 * @apiNote set borderThickness(0) for no border color.
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/**Set the thickness of the border.
	 * @param borderThickness - the thickness of the border color in pixels.
	 * @apiNote set borderThickness(0) for no border color.
	 * @author 5som3*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0) {
			Console.err("GUITextInputLine -> setBorderThickness() -> border thickness must be positive");
			borderThickness = 0;
		}
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	/**Set the color of the background.
	 * @param backgroundColor - the colour behind the text.
	 * @param will clear the background image (if any).
	 * @author 5som3*/
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.backgroundImage = null;
		updateGraphics();
	}
	
	/**Set the image behind the text and button.
	 * @param backgroundImage - the image to draw behind the text and buttons.
	 * @author 5som3*/
	public void setBackgroundImage(BufferedImage backgroundImage) {
		this.backgroundImage = backgroundImage;
		updateGraphics();
	}
	
	/**Set the image for the button.
	 * @param buttonImage - the image to draw as the button.
	 * @author 5som3*/
	public void setButtonImage(BufferedImage buttonImage) {
		this.buttonImage = buttonImage;
		updateGraphics();
	}
	
	/**Set the color of the button.
	 * @param buttonColor - the colour of the button.
	 * @apiNote will remove the button image (if any).
	 * @author 5som3*/
	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
		this.buttonImage = null;
		updateGraphics();
	}
	
	/**Set the color to overlay the text line when it is hovered.
	 * @param hoverColor - the color to overlay the text or button (depending which is hovered).
	 * @author 5som3*/
	public void setHoverColor(Color hoverColor) {
		this.hoverColor = hoverColor;
		updateGraphics();
	}
	
	/**Set the action that will trigger when the button or return key is pressed.
	 * @param buttonAction - the action.
	 * @author 5som3*/
	public void setButtonAction(StringAction buttonAction) {
		this.buttonAction = buttonAction;
		updateGraphics();		
	}
	
	/**Set if the action button will be visible
	 * @param isButtonVisible - if true the button will be visible and interactive else it will not be rendered and not interactive.
	 * @apiNote If the button is not visible the only way to execute the buttonAction will be with the return key (enter key).
	 * @author 5som3 */
	public void setButtonVisible(boolean isButtonVisible) {
		if(this.isButtonVisible != isButtonVisible) {
			this.isButtonVisible = isButtonVisible;
			updateGraphics();
		}
	}
	
	/**Set the maximum number of characters that the user can input.
	 * @param maxCharacters - the maximum number of characters.
	 * @author 5som3*/
	public void setMaxCharacters(int maxCharacters) {this.maxCharacters = maxCharacters;}
	
	/**Set the font of the text.
	 * @param font - the font.
	 * @author 5som3*/
	public void setFont(Font font) {
		this.font = font;
		updateGraphics();
	}
	
	/**Set the size of the text.
	 * @param textSize - the text size
	 * @author 5som3*/
	public void setTextSize(int textSize) {
		if(textSize < 6) {
			Console.err("GUITextInputLine -> setTextSize() -> text size too small ");
			textSize = 6;
		}
		font = new Font(font.getFontName(), font.getStyle(), textSize);
		updateGraphics();
	}


	/**Set if the input line will receive input from the keyboard.
	 * @param isInput - if true the keyboard input will be forwarded to the input line.
	 * @author 5som3*/
	public void setInputState(boolean isInput) {		
		if(this.isInput != isInput) {
			this.isInput = isInput;			
			if(isInput) {
				setReceiving(true);
				updateGraphics();				
			} else setReceiving(false);
		}

	}
	
	/**Set the user input string.
	 * @param inputString - the string that the user will write into.
	 * @author 5som3*/
	public void setText(String inputString) {
		this.inputString = inputString;
		updateGraphics();
	}
	
	
	public void updateGraphics() {
		Graphics g = getGraphics();
		
		g.setColor(borderColor); //Draw background
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
		if(backgroundImage != null) g.drawImage(backgroundImage, borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2, null);
		
		if(isLineHovered) { //Draw hover colours
			if(isPressed || isInput) g.setColor(pressColor);
			else g.setColor(hoverColor);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		
		g.setFont(font);//Draw text
		if(isInput) {
			g.setColor(inputTextColor);
			g.drawString(inputString, textOffset.x(), textOffset.y());
			if(promptIndex > inputString.length()) promptIndex = inputString.length();
			if(isPrompt) g.drawString(prompt, font.getSize() + g.getFontMetrics().stringWidth(inputString.substring(0, promptIndex)) - 2, font.getSize()); // draw prompt
		} else {
			g.setColor(blankTextColor);
			g.drawString(blankText, textOffset.x(), textOffset.y());
		}
				
		if(isButtonVisible) { //Draw button
			g.setColor(borderColor);
			g.fillRect(buttonOffset.x(), buttonOffset.y(), buttonSize.x(), buttonSize.y());
			g.setColor(buttonColor);
			g.fillRect(buttonOffset.x() + borderThickness, buttonOffset.y() + borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
			if(buttonImage != null) g.drawImage(buttonImage,buttonOffset.x() + borderThickness, buttonOffset.y() + borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2, null);
			
			if(isButtonHovered) { // Draw button hover
				if(isPressed) g.setColor(pressColor);
				else g.setColor(hoverColor);
				g.fillRect(buttonOffset.x(), buttonOffset.y(), buttonSize.x(), buttonSize.y());
			}
		}
		
		
		g.dispose();
		pushGraphics();
	}
	
	@Override
	public void updateObject() {
		if(isInput) { // update prompt
			tick++;
			if(tick > delay) {
				tick = 0;
				if(isPrompt) isPrompt = false;
				else isPrompt = true;
				updateGraphics();
			}
		}
	}
	
	
	//--------
	//---------------- Interaction --------
	//--------	
	@Override
	protected void mouseEntered() {
		Console.ln("GUITextInputLine -> mouseEntered()");
	}

	@Override
	protected void mouseExited() {
		Console.ln("GUITextInputLine -> mouseExited()");
		isButtonHovered = false;
		isLineHovered = false;
		updateGraphics();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		if(!isButtonVisible) {
			isLineHovered = true;
			return;
		}
		int mx = mouseEvent.getPointRelativeToObject().x;
		int my = mouseEvent.getPointRelativeToObject().y;
		if(mx > buttonOffset.x() && mx < buttonOffset.x() + buttonSize.x() && my > buttonOffset.y() && my < buttonOffset.y() + buttonSize.y()) {
			if(!isButtonHovered) {
				isButtonHovered = true;
				isLineHovered = false;
				updateGraphics();
				FrameworkAudio.defaultHover.play();
			}
		} else {
			if(!isLineHovered) {
				isButtonHovered = false;
				isLineHovered = true;
				updateGraphics();
				FrameworkAudio.defaultUnhover.play();
			}
		}
			
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(!isPressed) {
			isPressed = true;
			updateGraphics();
		}
	}

	private void enterInput() {
		buttonAction.action(inputString);
		keyLog.clear();
		inputString = "";
		tick = 0;
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) { 
		isPressed = false;
		if(isButtonHovered) {
			if(isButtonVisible) {
				enterInput();
				updateGraphics();
			}
		} else setInputState(true);
	}
	
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	@Override
	public void keyPressed(KeyEvent e) {return;}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_ESCAPE){
			isPrompt = false;
			setInputState(false);
			updateGraphics();
			return;
		}
		
		if(key == KeyEvent.VK_ENTER){
			enterInput();
			updateGraphics();
		}
		
		if(key == KeyEvent.VK_END) {
			promptIndex = keyLog.size();
			return;
		}
		if(key == KeyEvent.VK_HOME) {
			promptIndex = 0;
			return;
		}
		
		if(key == KeyEvent.VK_DELETE && promptIndex < keyLog.size()) {
			keyLog.remove(promptIndex);
		}
		if(key == KeyEvent.VK_BACK_SPACE && promptIndex > 0) {
			keyLog.remove(promptIndex - 1);
			promptIndex--;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			tick = delay;
			promptIndex--;
			if(promptIndex < 0) promptIndex = 0;
			Console.ln("WarpedKeyboard -> keyReleased() -> Left Arrow -> prompt Index : " + promptIndex);
			return;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			tick = delay;
			promptIndex++;
			if(promptIndex >= keyLog.size())	keyLog.add(' ');
			Console.ln("WarpedKeyboard -> keyReleased() -> Right Arrow -> prompt Index : " + promptIndex);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyChar() == KeyEvent.VK_SLASH || isLetterEvent(e) || isNumberEvent(e)) {				
			keyLog.add(promptIndex, e.getKeyChar());
			Console.ln("WarpedKeyboard -> keyReleased() -> logging key : " + e.getKeyChar());
			promptIndex++;
		}
		inputString = "";
		for(int i = 0; i < keyLog.size(); i++) {
			inputString += keyLog.get(i);
		}
		updateGraphics();
	}

	private boolean isLetterEvent(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A || key == KeyEvent.VK_B || key == KeyEvent.VK_C || key == KeyEvent.VK_D || key == KeyEvent.VK_E || key == KeyEvent.VK_F || 
		   key == KeyEvent.VK_G || key == KeyEvent.VK_H || key == KeyEvent.VK_I || key == KeyEvent.VK_J || key == KeyEvent.VK_K || key == KeyEvent.VK_L || 
		   key == KeyEvent.VK_M || key == KeyEvent.VK_N || key == KeyEvent.VK_O || key == KeyEvent.VK_P || key == KeyEvent.VK_Q || key == KeyEvent.VK_R ||
		   key == KeyEvent.VK_S || key == KeyEvent.VK_T || key == KeyEvent.VK_U || key == KeyEvent.VK_V || key == KeyEvent.VK_W || key == KeyEvent.VK_X || 
		   key == KeyEvent.VK_Y || key == KeyEvent.VK_Z) return true; else return false;
	}
	
	private boolean isNumberEvent(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_0 || key == KeyEvent.VK_1 || key == KeyEvent.VK_2 || key == KeyEvent.VK_3 || key == KeyEvent.VK_4 || key == KeyEvent.VK_5 ||
		   key == KeyEvent.VK_6 || key == KeyEvent.VK_7 || key == KeyEvent.VK_8 || key == KeyEvent.VK_9) return true; else return false;
	}

	

}
