/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import warped.audio.FrameworkAudio;
import warped.audio.WarpedAudioClip;
import warped.functionalInterfaces.WarpedAction;
import warped.functionalInterfaces.WarpedButtonAction;
import warped.graphics.sprite.ButtonSprite;
import warped.graphics.window.WarpedMouse;
import warped.graphics.window.WarpedMouseEvent;
import warped.graphics.window.WarpedWindow;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class GUIButton extends WarpedGUI {
	
	/*GUIButton provides these functions
	 * 		- set graphics and text inside the button that automatically respond to mouse interaction
	 * 		- set sound effects that trigger automatically with mouse interactions
	 * 		- set programmable actions that trigger automatically with mouse interaction
	 */
	
	private boolean isDragging = false;
	private boolean isEnteredDragging = false;
	
	private VectorD dragOffset = new VectorD();
	
	//private VectorD c2 = new VectorD(1920, 1080); //Max x / y position of the button
	
	public enum ButtonStateType {
		/**The default state of the button*/
		PLAIN,
		/**When the mouse position is contained within the bounds of the button in screen space*/
		HOVERED,
		/**When a mouse button is pressed while the button state is HOVERED*/
		PRESSED,
		/**When the button is not usable*/
		LOCKED;		
	}
	
	private WarpedAudioClip enteredSFX  = FrameworkAudio.defaultHover;
	private WarpedAudioClip exitedSFX   = FrameworkAudio.defaultUnhover;
	private WarpedAudioClip pressedSFX  = FrameworkAudio.defaultPress;
	private WarpedAudioClip releasedSFX = FrameworkAudio.defaultRelease;
	
	private WarpedButtonAction pressedAction;                                                                 
	private WarpedButtonAction releasedAction;
	              
	private WarpedAction enteredAction;
	private WarpedAction exitedAction;
	              
	private WarpedAction draggedAction;
	private WarpedAction movedAction;
	
	private ButtonSprite sprite;
	
	/**A button painted with the input image. Will be the same size as the input image.
	 * @param image - the image to base this button on.
	 * @author 5som3*/
	public GUIButton(BufferedImage image) {
		sprite = new ButtonSprite(image);
		setSprite(sprite);
		setButtonState(ButtonStateType.PLAIN);
	}
	
	/**A button of the size specified. Will have default button graphics which can be changed later.
	 * @param width - the width of the button in pixels.
	 * @param height - the height of the button in pixels.
	 * @author 5som3*/
	public GUIButton(int width, int height) {
		if(width < 0 || height < 0 || width > WarpedWindow.getWindowWidth() || height > WarpedWindow.getWindowHeight()) {
			Console.err("GUIButton -> constructor() -> button proportions are invalid (x, y) : ( " + width + ", " + height + ")");
			width = 120;
			height = 30;
		} 
		
		sprite = new ButtonSprite(width, height);
		sprite.setText("default");
		setSprite(sprite);
		setButtonState(ButtonStateType.PLAIN);
	}
	
	/**A button with the specified text displayed.
	 * @param text - the text to draw over the button
	 * @author 5som3*/
	public GUIButton(String text) {
		sprite = new ButtonSprite();
		sprite.paint(Colour.GREY_DARK.getColor());
		sprite.setText(text);
		setSprite(sprite);
		setButtonState(ButtonStateType.PLAIN);
	}
	
	/**A button of the specified parameters.
	 * @param width - the width of the button in pixels.
	 * @param height - the height of the button in pixels.
	 * @param color - the background color of the button.
	 * @author 5som3 */
	public GUIButton(int width, int height, Colour color) {
		sprite = new ButtonSprite(width, height, color);
		sprite.setButtonColour(color);
		setSprite(sprite);
		setButtonState(ButtonStateType.PLAIN);
	}
	
	/**A button of the specified parameters.
	 * @param width - the width of the button in pixels.
	 * @param height - the height of the button in pixels.
	 * @param text - the text to draw over the button
	 * @author 5som3 */
	public GUIButton(int width, int height, String text) {
		sprite = new ButtonSprite(width, height);
		sprite.setText(text);
		setSprite(sprite);
		setButtonState(ButtonStateType.PLAIN);
	}
	
	/**A button of the specified parameters.
	 * @param width - the width of the button in pixels.
	 * @param height - the height of the button in pixels.
	 * @param color - the background color of the button.
	 * @param text - the text to draw over the button.
	 * @author 5som3 */
	public GUIButton(int width, int height, Colour color, String text) {
		sprite = new ButtonSprite();
		setSprite(sprite);
		sprite.setBorderColour(color);
		sprite.setText(text);
		setButtonState(ButtonStateType.PLAIN);
	}
	
	/**A button of the specified parameters.
	 * @Param sprite - a ButtonSprite object to use for this GUIButton.
	 * @param position - a VectorD to set as PositionPointer
	 * */
	public GUIButton(ButtonSprite sprite, VectorD position) {
		setPositionPointer(position);
		this.sprite = sprite;
		setSprite(sprite);
		setButtonState(ButtonStateType.PLAIN);
	}

	/**Set the size of the button.
	 * @param width -the width of the button in pixels
	 * @param height - the height of the button in pixels.
	 * @author 5som3*/
	public void setButtonSize(int width, int height) {
		sprite.setButtonSize(width, height);
	}
	
	/**Set button sound effect
	 * @param enteredSFX - The sound effect will trigger (once) when the mouse enters the button
	 * @author SomeKid*/
	public void setEnteredSFX(WarpedAudioClip enteredSFX) {this.enteredSFX = enteredSFX;}
	
	/**Set button sound effect
	 * The sound effect will trigger (once) when the mouse leaves the button
	 * @author SomeKid*/
	public void setExitedSFX(WarpedAudioClip exitedSFX) {this.exitedSFX = exitedSFX;}
	
	/**Set button sound effect
	 * The sound effect will trigger (once) when a mouse button is pressed on the button
	 * @author SomeKid*/
	public void setPressedSFX(WarpedAudioClip pressedSFX) {this.pressedSFX = pressedSFX;}
	
	/**Set button sound effect
	 * The sound effect will trigger (once) when a mouse button is released on the button
	 * @author SomeKid*/
	public void setReleasedSFX(WarpedAudioClip releasedSFX) {this.releasedSFX = releasedSFX;}
	
	
	/**Set button action
	 * @param pressedAction - this action will trigger (once) when a mouse button is pressed in the button
	 * @author SomeKid*/
	public void setPressedAction(WarpedButtonAction pressedAction) {this.pressedAction = pressedAction;}
	
	/**Set button action
	 * @param releasedAction - this action will trigger (once) when a mouse button is pressed in the button
	 * @author SomeKid*/
	public void setReleasedAction(WarpedButtonAction releasedAction) {this.releasedAction = releasedAction;}
	
	/**Set button action
	 * @param enteredAction - this action will trigger (once) when the mouse enters the button
	 * @author SomeKid*/
	public void setEnteredAction(WarpedAction enteredAction) {this.enteredAction = enteredAction;}
	
	/**Set button action
	 * @param exitedAction - this action will trigger (once) when the mouse exits the button
	 * @author SomeKid*/
	public void setExitedAction(WarpedAction exitedAction) {this.exitedAction = exitedAction;}
	
	/**Set button action
	 * @param exitedAction - this action will trigger (every tick) when the mouse is dragging the button
	 * @author SomeKid*/
	public void setDraggedAction(WarpedAction draggedAction) {this.draggedAction = draggedAction;}
	
	
	/**Set button action
	 * @param exitedAction - this action will trigger (multiple times) when ever the mouse moves over the button
	 * @author SomeKid*/
	public void setMouseMovedAction(WarpedAction moveAction) {this.movedAction = moveAction;}
	
	/**Set the color of the button text.
	 * @param textColor - the color to render the text.
	 * @author 5som3*/
	public void setTextColor(Color textColor) {sprite.setTextColor(textColor);}
	
	/**Set the text to draw over the button.
	 * @param text - If you pass multiple text lines they will be drawn with an offset of font size 
	 * @author SomeKid*/
	public void setText(String... text) {sprite.setText(text);}
	
	/**Set the text offset
	 * @param textOffset - Measured in pixels from the top left corner
	 * @author SomeKid*/
	public void setTextOffset(int x, int y) {sprite.setTextOffset(x, y);}
	
	/**Set the current state of the button.
	 * @param type - the state to apply.
	 * 			   - see ButtonStateType (above) for documentation on the different states.
	 * @author SomeKid*/
	public void setButtonState(ButtonStateType type) {
		switch(type) {
		case HOVERED: sprite.hover(); 	 break;
		case PLAIN: sprite.plain(); 	 break;
		case PRESSED: sprite.press();	 break;
		case LOCKED: sprite.lock();	 	 break;
		default: Console.err("GUIButton -> setButtonState() -> button type is not supported : " + type); break;
		}
	}
	
	/**Lock the button.
	 * @apiNote While locked the button will not interact with the users mouse.
	 * @author SomeKid*/
	public void lock() {
		isLocked = true;
		sprite.lock();
	}
	
	/**Unlock the button(){
	 * @apiNote While locked the button will not interact with the users mouse. 
	 * @author SomeKid*/
	public void unlock() {
		isLocked = false;
		sprite.plain();
	}
	
	/**Set the lock state of the button.
	 * @param lock - the locked state.
	 * @apiNote While locked the button will not interact with the users mouse.
	 * @author SomeKid*/
	public void setLock(boolean lock) {
		if(lock)lock();
		else unlock();
	}
	
	/**Swap the lock state to the opposite of what it just was
	 * @author SomeKid*/
	public void toggleLock() {
		if(isLocked) unlock();
		else lock();
	}
	
	

	//--------
	//---------------------- Update ----------------------
	//--------
	@Override
	public void updateObject()  {		
		if(isLocked) return;
		if(!WarpedMouse.isDragging()) {				
			isDragging = false;
			WarpedMouse.setFocus(false);
		}
		if(isDragging) {
			if(dragOffset.isEqual(0.0, 0.0)){
				Console.err("GUIButton -> dragOffset not set");
			} else {
				
				double px = WarpedMouse.getPoint().x - dragOffset.x();
				double py = WarpedMouse.getPoint().y - dragOffset.y();
				
				if(px < 0) px = 0;
				if(py < 0) py = 0;
				if(px + sprite.getSize().x() > WarpedWindow.getApplicationWidth()) px = WarpedWindow.getApplicationWidth() - sprite.getSize().x();
				if(py + sprite.getSize().y() > WarpedWindow.getApplicationHeight()) px = WarpedWindow.getApplicationHeight() - sprite.getSize().y();	
				
				setPosition(px, py);
			}
			
			if(draggedAction != null) draggedAction.action();
		}
	}

	
	//--------
	//---------------------- Interaction ----------------------
	//--------
	protected void mouseEntered() {
		Console.ln("GUIButton -> mouseEntered()");
		if(isLocked) return;
		if(WarpedMouse.isDragging()) isEnteredDragging = true;
		if(!isDragging && !isEnteredDragging) {			
			sprite.hover();
			enteredSFX.play();
			
			if(enteredAction != null) enteredAction.action();
		}
	}
	
	protected void mouseExited() {
		Console.ln("GUIButton -> mouseExited()");
		if(isLocked) return;
		if(!isDragging && !isEnteredDragging) {
			sprite.plain();
			exitedSFX.play();
			
			if(exitedAction != null) exitedAction.action();
		}
		isEnteredDragging = false;
	}
	
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(isLocked) return;
		if(!isDragging && !isEnteredDragging) {			
			sprite.press();
			pressedSFX.play();		
			if(pressedAction!= null) pressedAction.action(mouseEvent);
			
		}
	} 
	
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		Console.ln("GUIButton -> mouseReleased()");
		if(isLocked) return;
		if(!isDragging && !isEnteredDragging) {		
			sprite.hover();
			releasedSFX.play();
			if(releasedAction != null) releasedAction.action(mouseEvent);
		}
		if(isEnteredDragging) {
			sprite.hover();
			isEnteredDragging = false;
		}
	}	
	
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(isLocked) return;
		if(isDraggable && !isEnteredDragging) {
			if(!isDragging && !WarpedMouse.isFocused()) {
				sprite.press();
				WarpedMouse.setFocus(true);;
				
				isDragging = true;
			} 								
		}		
	}
	
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		if(isLocked) return;
		if(!isDragging) {	
			if(mouseEvent.getPointRelativeToObject() == null) {
				Console.err("GUIButton -> mouseMoved() -> warpedmouseevent.getPointRelativeToObject() returned null");
				return; 
			}
			dragOffset.set(mouseEvent.getPointRelativeToObject().x, mouseEvent.getPointRelativeToObject().y);
			if(movedAction != null) movedAction.action();
		}
	}
	
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}



}
