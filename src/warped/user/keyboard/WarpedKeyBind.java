/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import warped.application.state.WarpedState;
import warped.graphics.sprite.PrimitiveSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.actions.WarpedAction;
import warped.utilities.utils.Console;

public class WarpedKeyBind<T> {

	protected T bound;
	protected PrimitiveSprite sprite;
	//protected BufferedImage raster;
	protected int key = -1;
	protected int defaultKey = -1;
	protected String name = "defaultKeyBind";
	protected boolean isListening = false; //this is for getting user input when rebinding 
	protected WarpedAction pressAction;
	protected WarpedAction releaseAction;
	
	public WarpedKeyBind(String name, int key, WarpedAction pressAction, WarpedAction releaseAction) {
		this.name = name;
		this.key = key;
		defaultKey = key;
		
		
		this.pressAction = pressAction;
		this.releaseAction = releaseAction;
	}
	
	public WarpedKeyBind(String name, WarpedAction pressAction, WarpedAction releaseAction) {
		this.name = name;		
		listen();
		
		this.pressAction = pressAction;
		this.releaseAction = releaseAction;
	}
	
	public WarpedKeyBind(String name, T bound) {
		this.name = name;
		this.bound = bound;
		listen();
		
	}
	
	public T getBound() {return bound;}
	//public void setBound(T bound) {this.bound = bound;}
	public String getName() {return name;}
	public int getKey() {return key;}
	public String getKeyName() {return KeyEvent.getKeyText(key);}
	
	public void setSprite(PrimitiveSprite sprite) {this.sprite = sprite;}
	public void setSprite(BufferedImage sprite) {this.sprite = new PrimitiveSprite(sprite);}
	
	//public void setRaster(BufferedImage raster) {this.raster = raster;}
	public void setKey(int key) {
		stopListening();
		this.key = key;
		if(bound != null && WarpedState.hotBar.isOpen()) WarpedState.hotBar.updateGraphics();
	}
	public void restoreDefaultKey() {this.key = defaultKey;}
	public void setName(String name) {this.name = name;}
	public void listen() {isListening = true;}
	public boolean isListening() {return isListening;}
	public void stopListening() {isListening = false;}
	public void setActions(WarpedAction pressAction, WarpedAction releaseAction) {this.pressAction = pressAction; this.releaseAction = releaseAction;}
	public void setPressAction(WarpedAction pressAction) {this.pressAction = pressAction;}
	public void setReleaseAction(WarpedAction releaseAction) {this.releaseAction = releaseAction;}
	
	public void press() {if(pressAction != null) pressAction.action();}
	public void release() {if(releaseAction != null) releaseAction.action();}
		
	public BufferedImage raster() {return sprite.raster();}
	
	public BufferedImage getKeyImage() {return getKeyImage(key);}
	public static BufferedImage getKeyImage(int key) {
		switch(key) {
		case KeyEvent.VK_1:					return FrameworkSprites.standardIcons.getSprite(0, 0);
		case KeyEvent.VK_2:					return FrameworkSprites.standardIcons.getSprite(1, 0);
		case KeyEvent.VK_3:					return FrameworkSprites.standardIcons.getSprite(2, 0);
		case KeyEvent.VK_4:					return FrameworkSprites.standardIcons.getSprite(3, 0);
		case KeyEvent.VK_5:					return FrameworkSprites.standardIcons.getSprite(4, 0);
		case KeyEvent.VK_6:					return FrameworkSprites.standardIcons.getSprite(5, 0);
		case KeyEvent.VK_7:					return FrameworkSprites.standardIcons.getSprite(6, 0);
		case KeyEvent.VK_8:					return FrameworkSprites.standardIcons.getSprite(7, 0);
		case KeyEvent.VK_9:					return FrameworkSprites.standardIcons.getSprite(8, 0);
		case KeyEvent.VK_0:					return FrameworkSprites.standardIcons.getSprite(9, 0);
			
		case KeyEvent.VK_F1:				return FrameworkSprites.standardIcons.getSprite( 0, 11);
		case KeyEvent.VK_F2:				return FrameworkSprites.standardIcons.getSprite( 1, 11);
		case KeyEvent.VK_F3:				return FrameworkSprites.standardIcons.getSprite( 2, 11);	
		case KeyEvent.VK_F4:                return FrameworkSprites.standardIcons.getSprite( 3, 11);
		case KeyEvent.VK_F5:                return FrameworkSprites.standardIcons.getSprite( 4, 11);
		case KeyEvent.VK_F6:                return FrameworkSprites.standardIcons.getSprite( 5, 11);
		case KeyEvent.VK_F7:                return FrameworkSprites.standardIcons.getSprite( 6, 11);
		case KeyEvent.VK_F8:                return FrameworkSprites.standardIcons.getSprite( 7, 11);
		case KeyEvent.VK_F9:                return FrameworkSprites.standardIcons.getSprite( 8, 11);
		case KeyEvent.VK_F10:               return FrameworkSprites.standardIcons.getSprite( 9, 11);
		case KeyEvent.VK_F11:               return FrameworkSprites.standardIcons.getSprite(10, 11);
		case KeyEvent.VK_F12:               return FrameworkSprites.standardIcons.getSprite(11, 11);
			
		case KeyEvent.VK_ESCAPE:			return FrameworkSprites.standardIcons.getSprite( 6, 10);
		case KeyEvent.VK_ENTER:				return FrameworkSprites.standardIcons.getSprite( 7, 10);
		case KeyEvent.VK_PRINTSCREEN:		return FrameworkSprites.standardIcons.getSprite( 8,  7);
		case KeyEvent.VK_PAUSE:				return FrameworkSprites.standardIcons.getSprite(10,  7);
			
		case KeyEvent.VK_DEAD_TILDE:		return FrameworkSprites.standardIcons.getSprite( 1, 10);
		case KeyEvent.VK_MINUS:				return FrameworkSprites.standardIcons.getSprite(10,  0);
		case KeyEvent.VK_PLUS:				return FrameworkSprites.standardIcons.getSprite(11,  1);
		case KeyEvent.VK_BACK_SPACE:		return FrameworkSprites.standardIcons.getSprite(10, 10);
		case KeyEvent.VK_EQUALS:			return FrameworkSprites.standardIcons.getSprite(11,  0);
		
		case KeyEvent.VK_INSERT:			return FrameworkSprites.standardIcons.getSprite(2, 7);
		case KeyEvent.VK_DELETE:			return FrameworkSprites.standardIcons.getSprite(3, 7);
		case KeyEvent.VK_HOME:				return FrameworkSprites.standardIcons.getSprite(4, 7);
		case KeyEvent.VK_END:				return FrameworkSprites.standardIcons.getSprite(5, 7);
		case KeyEvent.VK_PAGE_UP:			return FrameworkSprites.standardIcons.getSprite(6, 7);
		case KeyEvent.VK_PAGE_DOWN:			return FrameworkSprites.standardIcons.getSprite(7, 7);
			
		case KeyEvent.VK_CAPS_LOCK:			return FrameworkSprites.standardIcons.getSprite( 8, 9);
		case KeyEvent.VK_SCROLL_LOCK:		return FrameworkSprites.standardIcons.getSprite( 9, 7);
		case KeyEvent.VK_NUM_LOCK:			return FrameworkSprites.standardIcons.getSprite(11, 7);
			
		case KeyEvent.VK_TAB:				return FrameworkSprites.standardIcons.getSprite( 8,  8);
		case KeyEvent.VK_SHIFT:				return FrameworkSprites.standardIcons.getSprite( 3, 10);
		case KeyEvent.VK_CONTROL:			return FrameworkSprites.standardIcons.getSprite( 4, 10);
		case KeyEvent.VK_ALT:				return FrameworkSprites.standardIcons.getSprite( 5, 10);
		case KeyEvent.VK_WINDOWS:			return FrameworkSprites.standardIcons.getSprite( 8, 10);
		case KeyEvent.VK_SPACE:				return FrameworkSprites.standardIcons.getSprite(11, 10);
			
		case KeyEvent.VK_ASTERISK:			return FrameworkSprites.standardIcons.getSprite(7, 1);

		case KeyEvent.VK_SLASH:				return FrameworkSprites.standardIcons.getSprite(6, 8);
		case KeyEvent.VK_BACK_SLASH:		return FrameworkSprites.standardIcons.getSprite(7, 8);
		case KeyEvent.VK_BRACELEFT:			return FrameworkSprites.standardIcons.getSprite(0, 9);
		case KeyEvent.VK_BRACERIGHT:		return FrameworkSprites.standardIcons.getSprite(1, 9);	
		case KeyEvent.VK_OPEN_BRACKET:		return FrameworkSprites.standardIcons.getSprite(0, 8);
		case KeyEvent.VK_CLOSE_BRACKET:		return FrameworkSprites.standardIcons.getSprite(1, 8);
		case KeyEvent.VK_LEFT_PARENTHESIS:	return FrameworkSprites.standardIcons.getSprite(8, 1);
		case KeyEvent.VK_RIGHT_PARENTHESIS:	return FrameworkSprites.standardIcons.getSprite(9, 1);
		
		case KeyEvent.VK_COLON:				return FrameworkSprites.standardIcons.getSprite(2, 9);
		case KeyEvent.VK_SEMICOLON:			return FrameworkSprites.standardIcons.getSprite(2, 8);
		case KeyEvent.VK_COMMA:				return FrameworkSprites.standardIcons.getSprite(4, 8);
			
		case KeyEvent.VK_A:					return FrameworkSprites.standardIcons.getSprite( 0, 5);
		case KeyEvent.VK_B:                 return FrameworkSprites.standardIcons.getSprite( 1, 5);
		case KeyEvent.VK_C:                 return FrameworkSprites.standardIcons.getSprite( 2, 5);
		case KeyEvent.VK_D:                 return FrameworkSprites.standardIcons.getSprite( 3, 5);
		case KeyEvent.VK_E:                 return FrameworkSprites.standardIcons.getSprite( 4, 5);
		case KeyEvent.VK_F:                 return FrameworkSprites.standardIcons.getSprite( 5, 5);
		case KeyEvent.VK_G:                 return FrameworkSprites.standardIcons.getSprite( 6, 5);
		case KeyEvent.VK_H:                 return FrameworkSprites.standardIcons.getSprite( 7, 5);
		case KeyEvent.VK_I:                 return FrameworkSprites.standardIcons.getSprite( 8, 5);
		case KeyEvent.VK_J:                 return FrameworkSprites.standardIcons.getSprite( 9, 5);
		case KeyEvent.VK_K:                 return FrameworkSprites.standardIcons.getSprite(10, 5);
		case KeyEvent.VK_L:                 return FrameworkSprites.standardIcons.getSprite(11, 5);
		case KeyEvent.VK_M:                 return FrameworkSprites.standardIcons.getSprite( 0, 6);
		case KeyEvent.VK_N:                 return FrameworkSprites.standardIcons.getSprite( 1, 6);
		case KeyEvent.VK_O:                 return FrameworkSprites.standardIcons.getSprite( 2, 6);
		case KeyEvent.VK_P:                 return FrameworkSprites.standardIcons.getSprite( 3, 6);
		case KeyEvent.VK_Q:                 return FrameworkSprites.standardIcons.getSprite( 4, 6);
		case KeyEvent.VK_R:                 return FrameworkSprites.standardIcons.getSprite( 5, 6);
		case KeyEvent.VK_S:                 return FrameworkSprites.standardIcons.getSprite( 6, 6);
		case KeyEvent.VK_T:                 return FrameworkSprites.standardIcons.getSprite( 7, 6);
		case KeyEvent.VK_U:                 return FrameworkSprites.standardIcons.getSprite( 8, 6);
		case KeyEvent.VK_V:                 return FrameworkSprites.standardIcons.getSprite( 9, 6);
		case KeyEvent.VK_W:                 return FrameworkSprites.standardIcons.getSprite(10, 6);
		case KeyEvent.VK_X:	                return FrameworkSprites.standardIcons.getSprite(11, 6);
		case KeyEvent.VK_Y:                 return FrameworkSprites.standardIcons.getSprite( 0, 7);
		case KeyEvent.VK_Z:                 return FrameworkSprites.standardIcons.getSprite( 1, 7);
			
		case KeyEvent.VK_UP:				return FrameworkSprites.standardIcons.getSprite(5, 4);
		case KeyEvent.VK_RIGHT:				return FrameworkSprites.standardIcons.getSprite(6, 4);
		case KeyEvent.VK_DOWN:				return FrameworkSprites.standardIcons.getSprite(7, 4);
		case KeyEvent.VK_LEFT:				return FrameworkSprites.standardIcons.getSprite(8, 4);
		
		case -1:							return FrameworkSprites.standardIcons.getSprite(2, 10);
			
		default:
			Console.err("KeyBind -> getKeyImage() -> invalid key : " + key);
			return FrameworkSprites.standardIcons.getSprite(2, 10);
		}
	}
	
}
