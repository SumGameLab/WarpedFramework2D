/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.audio.FrameworkAudio;
import warped.audio.WarpedAudioClip;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.ButtonStateType;
import warped.utilities.enums.generalised.ButtonType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsImage;

public class GUIToggle extends WarpedGUI {
	
	/**AKA checkBox
	 * To use GUIToggle as a checkbox simply change the untoggled image to be the empty check box
	 * Then set the toggled image to be the checked version of the checkbox 
	 * */
	
	private ButtonType buttonType = ButtonType.COLOR;
	private ButtonStateType buttonState = ButtonStateType.RELEASED;
	
	private boolean toggle = false;
	private boolean enteredDragging = false;
	private boolean isLocked = false;
	
	//private ButtonSprite sprite;
	
	private WarpedAction toggleOnAction;
	private WarpedAction toggleOffAction;
	//private WarpedAction repeatingAction;
	private WarpedAction rightClickAction;
	
	private WarpedAudioClip enteredSFX = FrameworkAudio.defaultHover;
	private WarpedAudioClip exitedSFX  = FrameworkAudio.defaultUnhover;
	private WarpedAudioClip toggleOnSFX   = FrameworkAudio.defaultPress;
	private WarpedAudioClip toggleOffSFX  = FrameworkAudio.defaultRelease;
	
	private Vec2d defaultToggleSize = new Vec2d(120, 30);
	
	private int   borderThickness = 2;
	private Color buttonBorderColor = Color.BLACK;
	private Color buttonColor = Color.LIGHT_GRAY;
	private Color buttonHoverColor = new Color(110,60,60); 
	private Color buttonToggleColor = Color.RED;
	
	private Color labelColor = Color.YELLOW;
	private boolean isLabelVisible = false;
	private String label = "";
	private Vec2i  textOffset = new Vec2i();
	private Font font = UtilsFont.getPreferred();
	
	private BufferedImage toggleOnImage;
	private BufferedImage toggleOffImage;
	private BufferedImage hoveredOnImage;
	private BufferedImage hoveredOffImage;
	private BufferedImage lockedImage;
	
	
	public GUIToggle() {
		toggleOnImage = FrameworkSprites.standardIcons.getSprite(3, 26);
		toggleOffImage = FrameworkSprites.standardIcons.getSprite(3, 27);
		
		hoveredOnImage = UtilsImage.generateTintedClone(toggleOnImage, 30, Colour.RED);
		hoveredOffImage = UtilsImage.generateTintedClone(toggleOffImage, 30, Colour.RED);
		
		lockedImage = UtilsImage.generateTintedClone(toggleOffImage, 60, Colour.BLACK);
		
		isLabelVisible = false;
		buttonType = ButtonType.IMAGE;
		buttonState = ButtonStateType.PLAIN;
		updateGraphics();
	}
	
	
	public GUIToggle(BufferedImage rawSprite) {	
		toggleOffImage = rawSprite;
		toggleOnImage = UtilsImage.generateTintedClone(toggleOffImage, 60, Colour.RED);
		
		hoveredOffImage = UtilsImage.generateTintedClone(toggleOffImage, 30, Colour.RED);
		hoveredOnImage = UtilsImage.generateTintedClone(toggleOnImage, 30, Colour.RED);
		
		lockedImage = UtilsImage.generateTintedClone(toggleOffImage, 60, Colour.BLACK);
		
		isLabelVisible = false;
		buttonType  = ButtonType.IMAGE;
		buttonState = ButtonStateType.PLAIN;
		updateGraphics();
	}	
	
	
	public GUIToggle(String label) {
		this.label = label;
		isLabelVisible = true;
		buttonType  = ButtonType.COLOR;
		buttonState = ButtonStateType.PLAIN;
		setRaster(new BufferedImage((int)defaultToggleSize.x, (int)defaultToggleSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE));
		updateGraphics();
	}
	
	//public void setRepeatingAction(WarpedAction repeatingAction) {this.repeatingAction = repeatingAction;}
	public void setToggleOnAction(WarpedAction toggleOnAction) {this.toggleOnAction = toggleOnAction;}
	public void setToggleOffAction(WarpedAction toggleOffAction) {this.toggleOffAction = toggleOffAction;}
	public void setRightClickAction(WarpedAction rightClickAction) {this.rightClickAction = rightClickAction;}
	public void setToggleAction(WarpedAction toggleOnAction, WarpedAction toggleOffAction) {setToggleOnAction(toggleOnAction); setToggleOffAction(toggleOffAction);}
	
	public void setEnteredSFX(WarpedAudioClip enteredSFX)  {this.enteredSFX = FrameworkAudio.defaultHover;}
	public void setExitedSFX(WarpedAudioClip exitedSFX)    {this.exitedSFX  = FrameworkAudio.defaultUnhover;}
	public void setToggleOnSFX(WarpedAudioClip toggleOn)   {this.toggleOnSFX   = FrameworkAudio.defaultPress;}
	public void setToggleOffSFX(WarpedAudioClip toggleOff) {this.toggleOffSFX  = FrameworkAudio.defaultRelease;}
	
	public void setToggleState(boolean toggle) {
		this.toggle = toggle;
		updateGraphics();
	}
	
	
	private void updateGraphics() {
		switch(buttonType) {
		case COLOR:
			Graphics2D g = raster.createGraphics();
			g.setColor(buttonBorderColor);
			g.fillRect(0, 0, (int)size.x, (int)size.y);
			
			switch(buttonState) {
			case HOVERED: 	g.setColor(buttonHoverColor);	break;
			case EXITED: if(toggle) g.setColor(buttonToggleColor);
						 else g.setColor(buttonColor); break;
			case PLAIN: 	g.setColor(buttonColor); break;
			case PRESSED: 	g.setColor(buttonToggleColor); break;
			case RELEASED: 	g.setColor(buttonColor); break;
			case TOGGLED: 	g.setColor(buttonToggleColor); break;
			default: Console.err("GUIToggle -> updateGraphics() -> invalid switch Case : " + buttonState); break;			
			}
			g.fillRect(borderThickness, borderThickness, (int)size.x - borderThickness, (int)size.y - borderThickness);
			
			if(isLabelVisible) {
				g.setColor(labelColor);
				g.setFont(font);
				g.drawString(label, (borderThickness * 2) + textOffset.x, (borderThickness * 2) + font.getSize() + textOffset.x);
			}	
			g.dispose();
			setRaster(raster);
			break;
			
		case IMAGE:
			if(isLocked) {
				setRaster(lockedImage);
				break;
			}
			switch(buttonState) {
			case HOVERED:
				if(toggle) setRaster(hoveredOnImage);
				else setRaster(hoveredOffImage); break;
			case RELEASED:
				if(toggle) {
					toggleOffSFX.play();
					if(toggleOffAction != null) toggleOffAction.action();
					toggle = false;
					setRaster(hoveredOnImage);
				} else {
					toggleOnSFX.play();
					if(toggleOnAction != null) toggleOnAction.action();
					toggle = true;
					setRaster(hoveredOffImage);
				}
				break;
			case PLAIN:
				if(toggle) setRaster(toggleOnImage);
				else setRaster(toggleOffImage);
				break;
			default: Console.err("GUIToggle -> updateGraphics() -> invalid switch case : " + buttonState); break;
			}
		break;
		default: Console.err("GUIToggle -> updateGraphics() -> invalild switch case : " + buttonType); return;		
		}
	}
	
	public void toggleLock() {if(isLocked) unlock(); else lock();}
	
	public void lock() {
		if(!isLocked) {			
			isLocked = true;
			updateGraphics();
		}
	}
	
	public void unlock() {
		if(isLocked) {			
			isLocked = false;
			updateGraphics();
		}
	}
	
	public void setToggle(boolean toggle) {
		if(toggle) toggleOn(); 
		else toggleOff();
	}
	public void toggle() {
		if(toggle)toggleOff();
		else toggleOn();
		
	}
	
	
	public void updateMidly() {
		if(!toggle) return;
		updateMid();
		updateMidAction.action(this); 	
	}
	
	public void toggleOff() {
		if(toggle) {
			toggle = false;
			updateGraphics();
			if(toggleOffAction == null) Console.err("GUIToggle -> toggleOff -> toggleOffAction is null");
			else toggleOffAction.action();
			toggleOffSFX.play();
		}
	}
	
	public void toggleOn() {
		if(!toggle) {
			toggle = true;
			updateGraphics();
			if(toggleOnAction == null) Console.err("GUIToggle -> toggleOn -> toggleOnAction is null");
			else toggleOnAction.action();
			toggleOnSFX.play();
		}
	}

	
	@Override
	protected void mouseEntered() {
		if(WarpedMouse.isDragging()) {
			enteredDragging = true;
			return;
		}
		enteredSFX.play();
		buttonState = ButtonStateType.HOVERED;
		updateGraphics();
		
	}

	@Override
	protected void mouseExited() {
		if(!enteredDragging) {			
			exitedSFX.play();
			buttonState = ButtonStateType.PLAIN;
			updateGraphics();
		}
		enteredDragging = false;
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
		
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(isLocked) return;
		if(!enteredDragging) {
			if(mouseEvent.getMouseEvent().getButton() == MouseEvent.BUTTON1) toggle();
			if(mouseEvent.getMouseEvent().getButton() == MouseEvent.BUTTON3) {
				if(rightClickAction == null) Console.err("GUIToggle -> mouseReleased() -> toggle has no right click action");
				else rightClickAction.action();
				if(toggle) buttonState = ButtonStateType.RELEASED;
				else buttonState = ButtonStateType.PLAIN;
				updateGraphics();
			}
		}
		enteredDragging = false;
		
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) { return;}
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
