/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import warped.WarpedProperties;
import warped.audio.FrameworkAudio;
import warped.audio.WarpedAudioClip;
import warped.graphics.sprite.ButtonSprite;
import warped.graphics.window.WarpedWindow;
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

public class GUIButton extends WarpedGUI {
	
	public static final Colour DEFAULT_BUTTON_COLOR = Colour.GREY_DARK;
	public static final int DEFAULT_BUTTON_WIDTH = 150;
	public static final int DEFAULT_BUTTON_HEIGHT = 30;

	private ButtonType type = ButtonType.COLOR;
	
	private Color backgroundColor = Color.BLACK;
	private Byte backgroundAlpha  = (byte) 256;
	private Color buttonColor 	  = DEFAULT_BUTTON_COLOR.getColor();
	private int borderThickness   = 3;
	
	
	private boolean repeatPressAction = false;
	private boolean isPressing = false;
	
	private Vec2d buttonSize = new Vec2d(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
	
	private Boolean hasText  = false;
	private List<String> text;
	private Color textColor  = Color.YELLOW;
	private Font textFont 	 = UtilsFont.getPreferred();
	private Vec2i textOffset = new Vec2i(6, 6);
	
	private boolean isDragging = false;
	private boolean isEnteredDragging = false;
	
	private Vec2d dragOffset = new Vec2d();
	
	private Vec2d c2 = new Vec2d(1920, 1080); //Max x / y position of the button
	
	private ButtonSprite sprite = new ButtonSprite((int)buttonSize.x, (int)buttonSize.y, Colour.GREY_DARK);
		
	private WarpedAudioClip enteredSFX  = FrameworkAudio.defaultHover;
	private WarpedAudioClip exitedSFX   = FrameworkAudio.defaultUnhover;
	private WarpedAudioClip pressedSFX  = FrameworkAudio.defaultPress;
	private WarpedAudioClip releasedSFX = FrameworkAudio.defaultRelease;
	
	private WarpedAction pressedAction;                                                                 
	private WarpedAction releasedAction;
	private WarpedAction enteredAction;
	private WarpedAction exitedAction;
	private WarpedAction draggedAction;
	private WarpedAction movedAction = () -> {return;};
	
	public GUIButton(BufferedImage rawSprite) {
		this.sprite = new ButtonSprite(rawSprite);
		setRaster(this.sprite.raster());
	}
	
	public GUIButton() {
		setGameObjectSize(buttonSize);
		setText(Arrays.asList("default"));
		setType(ButtonType.COLOR);
	}
	
	public GUIButton(int x, int y) {
		if(x < 0 || y < 0 || x > WarpedWindow.width || y > WarpedWindow.height) {
			Console.err("GUIButton -> constructor() -> button proportions are invalid (x, y) : ( " + x + ", " + y + ")");
			setGameObjectSize(buttonSize);
		} else setGameObjectSize(new Vec2d(x, y));
		setText(Arrays.asList("default"));
		setType(ButtonType.COLOR);
	}
	
	public GUIButton(String text) {
		setGameObjectSize(buttonSize);
		setText(Arrays.asList(text));
		setType(ButtonType.COLOR);
	}
	
	public GUIButton(String text, ButtonType type) {
		setGameObjectSize(buttonSize);
		setText(Arrays.asList(text));
		setType(type);
	}
	
	public GUIButton(Color color, Vec2d size) {
		setGameObjectSize(size);
		setBackgroundColor(color);
		setType(ButtonType.COLOR);
	}
	
	public GUIButton(Color color, Vec2d size, String text) {
		setGameObjectSize(size);
		setBackgroundColor(color);
		setText(Arrays.asList(text));
		setType(ButtonType.COLOR);
	}
	
	public GUIButton(ButtonSprite sprite, Vec2d position) {
		this.position = position;
		this.sprite = sprite;
		setRaster(this.sprite.raster());
	}

	//--------
	//---------------------- Access ---------------------- 
	//--------
	public void setButtonState(ButtonStateType type) {
		switch(type) {
		case HOVERED: sprite.hover(); 	 break;
		case PLAIN: sprite.plain(); 	 break;
		case PRESSED: sprite.press();	 break;
		case RELEASED: sprite.plain(); break;
		default: Console.err("GUIButton -> setButtonState() -> button type is not supported : " + type); break;
		}
		setRaster(sprite.raster());
	}
	
	public void setRepeatPress(boolean repeatPress) {this.repeatPressAction = repeatPress;}
	public void repeatPress() {repeatPressAction = true;}
	public void noRepeatPress() {repeatPressAction = false;};
	
	public void lockUnseen() {isLocked = true;}
	public void unlockUnseen() {isLocked = false;}
	public void mseExited() {mouseExited();}
	
	public void setMouseMovedAction(WarpedAction moveAction) {this.movedAction = moveAction;}
	
	public void lock() {
		isLocked = true;
		sprite.lock();
		setRaster(sprite.raster());
	}
	
	public void setLock(boolean lock) {
		if(lock)lock();
		else unlock();
	}
	
	public void unlock() {
		isLocked = false;
		sprite.plain();
		setRaster(sprite.raster());
	}
	public void toggleLock() {
		if(isLocked) unlock();
		else lock();
	}
	
	public void setButtonSize(int width, int height) {
		setGameObjectSize(width, height);
		updateGraphics();
	}
	
	public void setType(ButtonType type) {
		this.type = type;
		updateGraphics();
	}
	
	public ButtonSprite getSprite() {return sprite;}
	public void setBackgroundClear() {
		type = ButtonType.CLEAR;
		updateGraphics();
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	public void setButtonColor(Color buttonColor){
		this.buttonColor = buttonColor;
		updateGraphics();
	}
	
	public void setEnteredSFX(WarpedAudioClip enteredSFX) {this.enteredSFX = enteredSFX;}
	public void setExitedSFX(WarpedAudioClip exitedSFX) {this.exitedSFX = exitedSFX;}
	public void setPressedSFX(WarpedAudioClip pressedSFX) {this.pressedSFX = pressedSFX;}
	public void setReleasedSFX(WarpedAudioClip releasedSFX) {this.releasedSFX = releasedSFX;}

	
	public void setPressedAction(WarpedAction pressedAction) {this.pressedAction = pressedAction;}
	public void setReleasedAction(WarpedAction releasedAction) {this.releasedAction = releasedAction;}
	public void setEnteredAction(WarpedAction enteredAction) {this.enteredAction = enteredAction;}
	public void setExitedAction(WarpedAction exitedAction) {this.exitedAction = exitedAction;}
	public void setDraggedAction(WarpedAction draggedAction) {this.draggedAction = draggedAction;}

	
	//--------
	//---------------------- Graphics ----------------------
	//--------
	public void updateGraphics() {
		switch(type) {
		case CLEAR: updateClearButtonGraphics(); break;
		case COLOR:	updateColorButtonGraphics(); break;
		case IMAGE:	setRaster(sprite.raster());	break;
		default: Console.err("GUIButton -> updateGraphics() -> inavlid buton type : " + type); break;
		}
	}
	
	private void updateClearButtonGraphics() {
		BufferedImage img = new BufferedImage((int)size.x,(int)size.y,WarpedProperties.BUFFERED_IMAGE_TYPE);
		sprite.setRaster(img);
		if(hasText) setText(text);
		setRaster(sprite.raster());		
	}
	
	private void updateColorButtonGraphics() {
		BufferedImage img = new BufferedImage((int)size.x,(int)size.y,WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, (int)size.x, (int)size.y);
		
		g.setColor(buttonColor);
		g.fillRect(borderThickness, borderThickness,  (int)(size.x - borderThickness * 2), (int)(size.y - borderThickness * 2));
		sprite.setRaster(img);
		if(hasText) setText(text);
		setRaster(sprite.raster());
	}
	
	public void setBackgroundAlpha(byte alpha) {
		this.backgroundAlpha = alpha;
		switch(type) {
		case CLEAR:
		case COLOR:
			Color col = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), (int)backgroundAlpha);
			setBackgroundColor(col);
			break;
		case IMAGE:
			sprite.setRaster(UtilsImage.generateAlphaClone(sprite.raster(), alpha));
			break;
		default: Console.err("GUIButton -> setButtonAlpha() -> invalid case : " + type); return;		
		}
	}
	
	
	//--------
	//---------------------- Button Text ----------------------
	//--------
	public void setText(String text) {setText(Arrays.asList(text));}
	
	public void setText(List<String> text) {
		if(text == null) {
			Console.err("GUIButton -> setText() -> invalid text is null or no text");
			hasText = false;
			return;
		}
		this.text = text;
		hasText = true;
		sprite.setButtonText(textOffset.x, textOffset.y, text, textFont, textColor);
		setRaster(sprite.raster());
		
	}
	
	public void setTextOffset(Vec2i textOffset) {
		this.textOffset = textOffset;
		setText(text);
	}
	
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
		setText(text);
	}
	
	public void setFont(Font textFont) {
		this.textFont = textFont;
		setText(text);
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		setText(text);
	}
	
	/**The maximum x,y position of the button - Typically you would want to set this to the size of the viewport you intend to render it in
	 * By default c2 = (1920, 1080)*/
	public void setC2(Vec2d c2) {this.c2 = c2;}
	
	
	//--------
	//---------------------- Update ----------------------
	//--------
	protected void updateRaster()  {return;}
	protected void updatePosition(){
		if(isLocked) return;
		if(!WarpedMouse.isDragging()) {				
			isDragging = false;
			WarpedMouse.unfocus();
		}
		if(isDragging) {
			if(dragOffset.isEqualTo(0.0)){
				Console.err("GUIButton -> dragOffset not set");
			} else {
				position.x = WarpedMouse.getPoint().x - dragOffset.x;
				position.y = WarpedMouse.getPoint().y - dragOffset.y;
				
				if(position.x < 0) position.x = 0;
				if(position.y < 0) position.y = 0;
				if(position.x + size.x > c2.x) position.x = c2.x - size.x;
				if(position.y + size.y > c2.y) position.x = c2.y - size.y;				
			}
			
			if(draggedAction != null) draggedAction.action();
		}
	}
	protected void updateObject()  {
		if(repeatPressAction && isPressing) {
			if(WarpedMouse.isPressed()) {
				pressedAction.action();				
			} else isPressing = false;
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
			setRaster(sprite.raster());
			enteredSFX.play();
			
			if(enteredAction != null) enteredAction.action();
		}
	}
	
	protected void mouseExited() {
		Console.ln("GUIButton -> mouseExited()");
		isPressing = false;
		if(isLocked) return;
		if(!isDragging && !isEnteredDragging) {
			sprite.plain();
			setRaster(sprite.raster());
			exitedSFX.play();
			
			if(exitedAction != null) exitedAction.action();
		}
		isEnteredDragging = false;
	}
	
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(isLocked) return;
		if(!isDragging && !isEnteredDragging) {			
			sprite.press();
			setRaster(sprite.raster());
			pressedSFX.play();		
			
			if(pressedAction!= null) {
				isPressing = true;
				pressedAction.action();
			}
		}
	} 
	
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		Console.ln("GUIButton -> mouseReleased()");
		if(isLocked) return;
		if(!isDragging && !isEnteredDragging) {		
			sprite.hover();
			setRaster(sprite.raster());
			releasedSFX.play();
			
			if(releasedAction != null) releasedAction.action();
		}
		if(isEnteredDragging) {
			sprite.hover();
			setRaster(sprite.raster());
			isEnteredDragging = false;
		}
	}	
	
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(isLocked) return;
		if(isDraggable && !isEnteredDragging) {
			if(!isDragging && !WarpedMouse.isFocused()) {
				sprite.press();
				setRaster(sprite.raster());
				WarpedMouse.focus();
				
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
			dragOffset.x = mouseEvent.getPointRelativeToObject().x;
			dragOffset.y = mouseEvent.getPointRelativeToObject().y;
			movedAction.action();
		}
	}
	
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


}
