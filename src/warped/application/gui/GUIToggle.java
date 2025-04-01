/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.image.BufferedImage;

import warped.audio.FrameworkAudio;
import warped.audio.WarpedAudioClip;
import warped.functionalInterfaces.WarpedAction;
import warped.functionalInterfaces.WarpedButtonAction;
import warped.graphics.sprite.ToggleSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class GUIToggle extends WarpedGUI {
	
	/*GUIToggle provides these functions
	 * 		- set graphics that automatically respond to mouse interaction
	 * 		- set sound effects that trigger automatically with mouse interaction
	 * 		- set programmable actions for when the toggle is turned on or off
	 * 		- optional repeating action that occurs while the toggle is turned on
	 */
	
	private boolean toggle = false;
	private boolean isLocked = false;
	private boolean enteredDragging = false;
	
	private WarpedButtonAction toggleOnAction;
	private WarpedButtonAction toggleOffAction;
	private WarpedAction repeatingAction;	
	
	private WarpedAudioClip enteredSFX    = FrameworkAudio.defaultHover;
	private WarpedAudioClip exitedSFX     = FrameworkAudio.defaultUnhover;
	private WarpedAudioClip toggleOnSFX   = FrameworkAudio.defaultPress;
	private WarpedAudioClip toggleOffSFX  = FrameworkAudio.defaultRelease;		
	
	private ToggleSprite sprite;
	
	public GUIToggle() {
		sprite = new ToggleSprite(FrameworkSprites.getStandardIcon(StandardIcons.CHECKBOX_CHECKED), FrameworkSprites.getStandardIcon(StandardIcons.CHECKBOX_UNCHECKED)); 
		setSprite(sprite);
		updateToggleState();
	}
	
	public GUIToggle(BufferedImage toggleOffImage) {
		sprite = new ToggleSprite(UtilsImage.generateTintedClone(toggleOffImage, 60, Colour.RED), toggleOffImage);  
		setSprite(sprite);
		updateToggleState();
	}
	
	
	public GUIToggle(BufferedImage toggleOnImage, BufferedImage toggleOffImage) {
		sprite = new ToggleSprite(toggleOnImage, toggleOffImage); 
		setSprite(sprite);
		updateToggleState();
	}	
	
	/**Set the toggle on action
	 * @param toggleOnAction - The action will be triggered (once) each time the toggle is toggled on.
	 * @author SomeKid*/
	public void setToggleOnAction(WarpedButtonAction toggleOnAction) {this.toggleOnAction = toggleOnAction;}
	
	/**Set the toggle off action
	 * @param toggleOffAction - The action will be triggered (once) each time the toggle is toggled off.
	 * @author SomeKid*/
	public void setToggleOffAction(WarpedButtonAction toggleOffAction) {this.toggleOffAction = toggleOffAction;}
	
	/**Set repeating action
	 * @param repeatingAction - this action will be triggered once every second for as long as the toggle is in the on state
	 * @author SomeKid*/
	public void setRepeatingAction(WarpedAction repeatingAction) {this.repeatingAction = repeatingAction;}
	
	/**Set toggle sound effect
	 * @param enteredSFX - the clip will play (once) each time the mouse enters the toggle
	 * @author SomeKid*/
	public void setEnteredSFX(WarpedAudioClip enteredSFX)  {this.enteredSFX = FrameworkAudio.defaultHover;}
	
	/**Set toggle sound effect
	 * @param exitedSFX - the clip will play (once) each time the mouse exits the toggle
	 * @author SomeKid*/
	public void setExitedSFX(WarpedAudioClip exitedSFX)    {this.exitedSFX  = FrameworkAudio.defaultUnhover;}
	
	/**Set toggle sound effect
	 * @param toggleOn - the clip will play (once) each time the toggle is toggled on
	 * @author SomeKid*/
	public void setToggleOnSFX(WarpedAudioClip toggleOnSFX)   {this.toggleOnSFX   = toggleOnSFX;}
	
	/**Set toggle sound effect
	 * @param toggleOff - the clip will play (once) each time the toggle is toggled off
	 * @author SomeKid*/
	public void setToggleOffSFX(WarpedAudioClip toggleOffSFX) {this.toggleOffSFX  = toggleOffSFX;}
	
	/**Set the toggle state
	 * @param toggle - the new state for the toggle
	 * @author SomeKid*/
	public void setToggleState(boolean toggle) {
		this.toggle = toggle;
		updateToggleState();
	}
	
	
	/**Toggle the locked state of the button; if the button is locked it will be unlocked and visa versa
	 * @apiNote a locked toggle will not interact with the mouse
	 * @author SomeKid*/
	public void toggleLock() {
		if(isLocked) unlock(); 
		else lock();
	}
	
	/**Lock the toggle
	 * @apiNote a locked toggle will not interact with the mouse
	 * @author SomeKid*/
	public void lock() {
		if(!isLocked) {			
			isLocked = true;
			updateToggleState();
		}
	}
	
	/**Unlock the toggle
	 * @apiNote a locked toggle will not interact with the mouse
	 * @author SomeKid*/
	public void unlock() {
		if(isLocked) {			
			isLocked = false;
			updateToggleState();
		}
	}
	
	/**Set the locked state for the toggle
	 * @param toggle - the new state for the toggle
	 * @author SomeKid*/
	public void setToggle(boolean toggle) {
		this.toggle = toggle;
		updateToggleState();
	}
	
	
	private void toggle(WarpedMouseEvent buttonEvent) {
		if(toggle) toggleOff(buttonEvent);
		else toggleOn(buttonEvent);
		
	}
	
	private void toggleOff(WarpedMouseEvent buttonEvent) {
		if(toggle) {
			toggle = false;
			if(toggleOffAction == null ) Console.ln("GUIToggle -> toggleOff -> toggleOffAction is null");
			else toggleOffAction.action(buttonEvent);
			toggleOffSFX.play();
			updateToggleState();
		}
	}
	
	private void toggleOn(WarpedMouseEvent buttonEvent) {
		if(!toggle) {
			toggle = true;
			if(toggleOnAction == null) Console.ln("GUIToggle -> toggleOn -> toggleOnAction is null");
			else toggleOnAction.action(buttonEvent);
			toggleOnSFX.play();
			updateToggleState();
		}
	}
	
	
	private void updateToggleState() {
		if(isLocked) {
			sprite.locked();
			return;
		}
		
		if(toggle) { 
			if(isHovered()) sprite.hoveredOn(); else sprite.toggleOn();
		} else {
			if(isHovered()) sprite.hoveredOff(); else sprite.toggleOff();
		}		
	}
	
	@Override
	protected void mouseEntered() {
		if(WarpedMouse.isDragging()) {
			enteredDragging = true;
			return;
		}
		enteredSFX.play();
		updateToggleState();
		
	}

	@Override
	protected void mouseExited() {
		if(!enteredDragging) {			
			exitedSFX.play();
			updateToggleState();
		}
		enteredDragging = false;
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(isLocked) return;
		if(!enteredDragging) toggle(mouseEvent);
		enteredDragging = false;
	}

	@Override
	public void updateMid() {if(toggle && repeatingAction != null) repeatingAction.action();};

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) { return;}
	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	

	

}
