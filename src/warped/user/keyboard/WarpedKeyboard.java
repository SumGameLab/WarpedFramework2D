/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import warped.utilities.utils.Console;

public class WarpedKeyboard implements KeyListener {

	private static final int KEY_SIZE = 1028;
	private static String pressString;
	private static String releaseString;
	
	private static ArrayList<KeyEvent> presses 	= new ArrayList<>();
	private static ArrayList<KeyEvent> releases = new ArrayList<>();
	
	private static KeyEvent[] pressedKeys 	= new KeyEvent[KEY_SIZE];
	private static KeyEvent[] releasedKeys 	= new KeyEvent[KEY_SIZE];
	
	//private static HashMap<Integer, WarpedAction> pressBindings   = new HashMap<>();
	//private static HashMap<Integer, WarpedAction> releaseBindings = new HashMap<>();
	
	private static WarpedKeyboardController activeController;

	private static ArrayList<Character> keyLog = new ArrayList<>();
	
	private static boolean isLocked = false;
	private static boolean isSpeaking = false;
	private static boolean isKeyLogging = false;
	private static int key = -1;
	
	private static int delayTick 	 = 0;
	private static int delayDuration = 240;
	
	private static HashMap<WarpedControllerType, WarpedKeyboardController> controllers = new HashMap<>();
	
	
	public WarpedKeyboard() {
		addController(new DefaultController());
		//addController(new WarpTechController());
		setController(WarpedControllerType.DEFAULT);
	}
	
	
	
	
	//--------
	//---------------- Access ------------------
	//--------
	
	private static synchronized void addKeyToLog(char key) {keyLog.add(key);}
	public static synchronized void clearKeyLog() {keyLog.clear();}
	public static synchronized String getKeyLog() {
		String result = "";
		if(keyLog == null || keyLog.size() == 0) return result;
		for(int i = 0; i < keyLog.size(); i++) {
			result += keyLog.get(i).toString();
		}
		return result;
	}
	public static boolean isKeyLogging() {return isKeyLogging;}
	public static void startKeyLogging() {isKeyLogging = true;}
	public static void stopKeyLogging() {isKeyLogging = false;}
	
	public static void setController(WarpedControllerType type) {
		Console.ln("WarpedKeyboard -> setController() -> Controller : " + type.toString());
		if(!controllers.containsKey(type)) {
			Console.err("Keyboard -> setController() -> keyboard controllers does not contain a keyboard of the type : " + type);
			return;
		}
		setController(controllers.get(type));		
	}
	
	public static synchronized WarpedKeyboardController getActiveController() {return activeController;}
	public static synchronized WarpedKeyboardController getController(WarpedControllerType controllerType) {return controllers.get(controllerType);}
	
	public static void speak() {isSpeaking = true;}	
	public static void lock() {isLocked = true;}
	public static void unlock() {isLocked = false;}
	public static void toggleLock() {if(isLocked) isLocked = false;	else isLocked = true;}
	public static void stopSpeaking(){
		isSpeaking = false;
		key = -1;
		activeController.stopListening();
	}
	
	public static String getPressesString() {return pressString;}
	public static String getReleaseString() {return releaseString;}
	public static int getKeyBindingsCount(){return activeController.getKeyBindCount();}
	//public static int getReleaseBindingsCount(){return releaseBindings.size();}
	
	
	//--------
	//---------------- Update ------------------
	//--------
	public void update() {
		if(delayTick++ > delayDuration) {
			delayTick = 0;
			releaseString = "";
		}		
		pressString = "";
		presses.forEach(e -> {pressString += e.getKeyChar() + " ,  ";});
		presses.clear();
		
		releases.forEach(e -> {releaseString += e.getKeyChar() + " ,  ";});
		releases.clear();
		
		if(isSpeaking) { // rebinding keys
			if(key == -1) return;
			else {				
				isSpeaking = false;
				Console.ln("WarpedKeyboard -> update() -> rebinding to key : " + key);
				
				/*
				if(WarpedState.keyboardOptions.isOpen()) {
					if(activeController.isKeyBound(key)){						
						WarpedState.notify.addNotification(KeyEvent.getKeyText(key)  + "  :  Key is already bound");
						WarpedState.keyboardOptions.keyBindings.resetSelected();
						stopSpeaking();
						return;
					}
					
					int listeningKey = -1;
					for(Map.Entry<Integer, WarpedKeyBind<?>> set : activeController.keyBinds.entrySet()) {
						if(set.getValue().isListening) {
							listeningKey = set.getKey();
							Console.ln("WarpedKeyboard -> update() -> listenKey : " + listeningKey);
							break;
						}
					}
					
					activeController.rebindKeyBind(listeningKey, key);
					activeController.stopListening();
				}
				*/
				/*
				if(WarpedState.hotBar.isOpen()) {
					for(int i = 0; i < activeController.hotBinds.size(); i++) {
						if(activeController.hotBinds.get(i).isListening) {
							activeController.hotBinds.get(i).setKey(key);
						}
					}
				}
				*/
				
				
				//if(WarpedState.keyboardOptions.isOpen()) WarpedState.keyboardOptions.keyBindings.setList(activeController);
				key = -1;
			}
			
			return;
		}
		
		for(int i = 0; i < KEY_SIZE; i++) {
			if(pressedKeys[i] != null) {				
				presses.add(pressedKeys[i]);
			}
		}
		if(!isLocked) presses.forEach(k ->{activeController.pressKey(k.getKeyCode());});
		
		for(int i = 0; i < KEY_SIZE; i++) {
			if(releasedKeys[i] != null) {
				releases.add(releasedKeys[i]);
				releasedKeys[i] = null;
			}
		}
		if(!isLocked) releases.forEach(kr -> {activeController.releaseKey(kr.getKeyCode());});
		
	}

	
	//--------
	//------------ KeyEvents ----------------------
	//--------
	public void keyPressed(KeyEvent e) {
		if(isSpeaking) key = e.getKeyCode();
		else pressedKeys[e.getKeyCode()] = e;
	}
	
	public void keyReleased(KeyEvent e) {
		pressedKeys[e.getKeyCode()] = null;
		releasedKeys[e.getKeyCode()] = e;
		if(isKeyLogging) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_ENTER) {isKeyLogging = false; return;}
			if(key == KeyEvent.VK_BACK_SPACE && keyLog.size() > 0) {keyLog.remove(keyLog.size() - 1); return;}
			if(e.getKeyCode() == KeyEvent.VK_SPACE || isLetterEvent(e) || isNumberEvent(e)) addKeyToLog(e.getKeyChar());
		}
	}
	
	public void keyTyped(KeyEvent e) {return;}
	
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
	//--------
	//------------ Controllers  ----------------------
	//--------
	private static void addController(WarpedKeyboardController controller) {
		if(controllers.containsKey(controller.getType())) {
			Console.ln("Keyboard -> addController() -> overwriting controller : " + controller.getType());
		}
		controllers.put(controller.getType(), controller);
	}
	
	private static void setController(WarpedKeyboardController controller) {
		boolean isLockedState = isLocked;
		isLocked = true;
		activeController = controller;
		/*
		pressBindings    = new HashMap<>();
		releaseBindings  = new HashMap<>();
		activeController = controller;
		for(KeyBind bind : activeController.keyBinds) {
			if(bind.pressAction != null) pressBindings.put(bind.key, bind.pressAction);
			if(bind.releaseAction != null) releaseBindings.put(bind.key, bind.releaseAction);
			
		}		
		 */
		isLocked = isLockedState;	
	
	}
	
	/*
	private static void setController() {
		boolean isLockedState = isLocked;
		isLocked = true;
		//activeController
		/*
		pressBindings    = new HashMap<>();
		releaseBindings  = new HashMap<>();
		for(KeyBind bind : activeController.keyBinds) {
			if(bind.pressAction != null) pressBindings.put(bind.key, bind.pressAction);
			if(bind.releaseAction != null) releaseBindings.put(bind.key, bind.releaseAction);
			
		}		
	isLocked = isLockedState;	
	}
	 */

	
}
