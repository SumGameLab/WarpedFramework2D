/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import warped.utilities.utils.Console;

public class WarpedKeyboard implements KeyListener {

	//private static ArrayList<Character> pressedKeys = new ArrayList<>();
	//private static ArrayList<Character> releasedKeys = new ArrayList<>();
		
	private static WarpedKeyboardController activeController;
	private static ArrayList<WarpedTypeable> typeables = new ArrayList<>();

	private static boolean isLocked = false;
	
	private static HashMap<Integer, Boolean> pressedKeys = new HashMap<>();

	public WarpedKeyboard() {
		setController(new DefaultController());
	}
	
	/**Set a controller to receive events from the keyboard.
	 * @param controller - the controller for the keyboard.
	 * @author 5som3*/
	public static void setController(WarpedKeyboardController controller) {
		boolean isLockedState = isLocked;
		isLocked = true;
		activeController = controller;
		isLocked = isLockedState;	
	}
	
	/**WarpedTypeables will receive a KeyEvent through the keyTyped method.
	 * @param typeable - the object to stop receiving KeyEvents when ever a key is released. 
	 * @author 5som3*/
	protected static void removeTypeable(WarpedTypeable typeable) {
		for(int i = 0; i < typeables.size(); i++) {
			if(typeables.get(i).equals(typeable)) typeables.remove(i);
		}
	}	
	
	/**WarpedTypeables will receive a KeyEvent through the keyTyped method.
	 * @param typeable - the object to start receiving KeyEvents when ever a key is released. 
	 * @author 5som3*/
	protected static void addTypeable(WarpedTypeable typeable) {
		if(!contains(typeable))	typeables.add(typeable);
	}
	
	/**Get the active controller.
	 * @return WarpedKeyboardController - the current keyboard controller.
	 * @author 5som3*/
	public static WarpedKeyboardController getActiveController() {return activeController;}	
	
	/**Lock or unlock the keyboard
	 * @param isLocked - if true the controller and typeables will not receive any keyboard events.
	 * @author 5som3*/
	public static void setLock(boolean isLocked) {WarpedKeyboard.isLocked = isLocked;}
	
	/**Toggle the locked state of the keyboard.
	 * @author 5som3*/
	public static void toggleLock() {if(isLocked) isLocked = false;	else isLocked = true;}
	
	
	/**Get a string of the keys pressed since the last time getPresses() was called.
	 * @return String - a string of the keys that are pressed 
	 * @author 5som3*/
	public static String getPresses() {
		String result = "";
		ArrayList<Integer> keys = new ArrayList<>(pressedKeys.keySet());
		for(int i = 0; i < keys.size(); i++) {
			if(pressedKeys.get(keys.get(i)) == true) result += KeyEvent.getKeyText(keys.get(i));
		}
		return result;
		
	}
	
	/**Is the specified key currently pressed.
	 * @param keyCode - the keyCode of the key to check
	 * @return boolean - true if the key is pressed.
	 * @apiNote Will return false with error if the key has not been pressed at least once
	 * @author 5som3 */
	public static boolean isKeyPressed(int keyCode) {
		if(pressedKeys.containsKey(keyCode)) return pressedKeys.get(keyCode);
		else {
			Console.err("WarpeeKeyboard -> isKeyPressed() -> key doesn't exist : " + keyCode);
			return false;
		}
	}
	
	/**Does the keyboard contain the specified typeable
	 * @param typeable - the typeable to check for.
	 * @apiNote all typeables contained will received keyPressed() and keyRecieved() events from the keyboard.
	 * @author 5som3*/
	public static boolean contains(WarpedTypeable typeable) {if(typeables.contains(typeable)) return true; else return false;}
	
	/**The number of bindings on the active controller.
	 * @return int - the number of bindings.
	 * @author 5som3*/
	public static int getKeyBindingsCount(){return activeController.getBindingCount();}
		
	/**This method is triggered when ever a key is pressed on the keyboard.
	 * @param e - the key event from the keyboard. This method can trigger multiple times during a single press.
	 * @author 5som3*/
	public void keyPressed(KeyEvent e) {
		pressedKeys.put(e.getKeyCode(), true);
		if(isLocked) return;
		activeController.pressKey(e.getKeyCode());
		for(int i = 0; i < typeables.size(); i++) typeables.get(i).keyPressed(e);
	}
	
	/**This method is triggered when ever a key is released on the keyboard.
	 * @param e - the key event from the keyboard. This method can trigger multiple times during a single press.
	 * @author 5som3*/
	public void keyReleased(KeyEvent e) {
		pressedKeys.put(e.getKeyCode(), false);
		if(isLocked) return;
		activeController.releaseKey(e.getKeyCode());
		for(int i = 0; i < typeables.size(); i++) typeables.get(i).keyReleased(e);	
	}
	
	
	public void keyTyped(KeyEvent e) {return;}

	
}
