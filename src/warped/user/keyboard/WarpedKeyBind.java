/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import warped.application.state.WarpedState;
import warped.graphics.sprite.PrimitiveSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.KeyboardIcons;
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
		case KeyEvent.VK_1:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ONE);
		case KeyEvent.VK_2:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.TWO);
		case KeyEvent.VK_3:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.THREE);
		case KeyEvent.VK_4:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.FOUR);
		case KeyEvent.VK_5:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.FIVE);
		case KeyEvent.VK_6:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.SIX);
		case KeyEvent.VK_7:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.SEVEN);
		case KeyEvent.VK_8:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.EIGHT);
		case KeyEvent.VK_9:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.NINE);
		case KeyEvent.VK_0:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ZERO);
			
		case KeyEvent.VK_F1:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F1);
		case KeyEvent.VK_F2:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F2);
		case KeyEvent.VK_F3:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F3);	
		case KeyEvent.VK_F4:                return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F4);
		case KeyEvent.VK_F5:                return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F5);
		case KeyEvent.VK_F6:                return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F6);
		case KeyEvent.VK_F7:                return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F7);
		case KeyEvent.VK_F8:                return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F8);
		case KeyEvent.VK_F9:                return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F9);
		case KeyEvent.VK_F10:               return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F10);
		case KeyEvent.VK_F11:               return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F11);
		case KeyEvent.VK_F12:               return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F12);
			
		case KeyEvent.VK_ESCAPE:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ESC);
		case KeyEvent.VK_ENTER:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ENTER);
		case KeyEvent.VK_PRINTSCREEN:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.PRINT);
		case KeyEvent.VK_PAUSE:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.PAUSE);
			
		case KeyEvent.VK_DEAD_TILDE:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.TILDE);
		case KeyEvent.VK_MINUS:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.MINUS);
		case KeyEvent.VK_PLUS:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.PLUS);
		case KeyEvent.VK_BACK_SPACE:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.BACK_SPACE);
		case KeyEvent.VK_EQUALS:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.EQUALS);
		
		case KeyEvent.VK_INSERT:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.INS);  
		case KeyEvent.VK_DELETE:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.DEL);  
		case KeyEvent.VK_HOME:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.HOME);  
		case KeyEvent.VK_END:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.END);  
		case KeyEvent.VK_PAGE_UP:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.PAGE_UP);  
		case KeyEvent.VK_PAGE_DOWN:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.PAGE_DOWN);  
			
		case KeyEvent.VK_CAPS_LOCK:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.CAPS_LOCK);  
		case KeyEvent.VK_SCROLL_LOCK:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.SCRL_LOCK);  
		case KeyEvent.VK_NUM_LOCK:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.NUM_LOCK);  
			
		case KeyEvent.VK_TAB:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.TAB);
		case KeyEvent.VK_SHIFT:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.SHIFT);
		case KeyEvent.VK_CONTROL:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.CTRL);
		case KeyEvent.VK_ALT:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ALT);
		case KeyEvent.VK_WINDOWS:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.WIN);
		case KeyEvent.VK_SPACE:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.SPACE);
			
		case KeyEvent.VK_ASTERISK:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ASTERISK);

		case KeyEvent.VK_SLASH:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.FORWARD_SLASH);
		case KeyEvent.VK_BACK_SLASH:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.BACK_SLASH);
		case KeyEvent.VK_BRACELEFT:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.BRACE_LEFT);
		case KeyEvent.VK_BRACERIGHT:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.BRACE_RIGTH);
		case KeyEvent.VK_OPEN_BRACKET:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.BRACKET_LEFT);
		case KeyEvent.VK_CLOSE_BRACKET:		return FrameworkSprites.getKeyboardIcon(KeyboardIcons.BRACKET_RIGHT);
		case KeyEvent.VK_LEFT_PARENTHESIS:	return FrameworkSprites.getKeyboardIcon(KeyboardIcons.PARENTHESIS_LEFT);
		case KeyEvent.VK_RIGHT_PARENTHESIS:	return FrameworkSprites.getKeyboardIcon(KeyboardIcons.PARENTHESIS_RIGHT);
		
		case KeyEvent.VK_COLON:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.COLON);
		case KeyEvent.VK_SEMICOLON:			return FrameworkSprites.getKeyboardIcon(KeyboardIcons.SEMI_COLON);
		case KeyEvent.VK_COMMA:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.COMMA);
			
		case KeyEvent.VK_A:					return FrameworkSprites.getKeyboardIcon(KeyboardIcons.A);
		case KeyEvent.VK_B:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.B);
		case KeyEvent.VK_C:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.C);
		case KeyEvent.VK_D:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.D);
		case KeyEvent.VK_E:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.E);
		case KeyEvent.VK_F:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.F);
		case KeyEvent.VK_G:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.G);
		case KeyEvent.VK_H:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.H);
		case KeyEvent.VK_I:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.I);
		case KeyEvent.VK_J:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.J);
		case KeyEvent.VK_K:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.K);
		case KeyEvent.VK_L:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.L);
		case KeyEvent.VK_M:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.M);
		case KeyEvent.VK_N:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.N);
		case KeyEvent.VK_O:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.O);
		case KeyEvent.VK_P:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.P);
		case KeyEvent.VK_Q:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.Q);
		case KeyEvent.VK_R:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.R);
		case KeyEvent.VK_S:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.S);
		case KeyEvent.VK_T:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.T);
		case KeyEvent.VK_U:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.U);
		case KeyEvent.VK_V:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.V);
		case KeyEvent.VK_W:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.W);
		case KeyEvent.VK_X:	                return FrameworkSprites.getKeyboardIcon(KeyboardIcons.X);
		case KeyEvent.VK_Y:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.Y);
		case KeyEvent.VK_Z:                 return FrameworkSprites.getKeyboardIcon(KeyboardIcons.Z);
			
		case KeyEvent.VK_UP:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_UP);
		case KeyEvent.VK_RIGHT:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_RIGHT);
		case KeyEvent.VK_DOWN:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_DOWN);
		case KeyEvent.VK_LEFT:				return FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_LEFT);
	
		default:
			Console.err("KeyBind -> getKeyImage() -> invalid key : " + key);
			return FrameworkSprites.error;
		}
	}
	
}
