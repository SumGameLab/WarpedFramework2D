/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.util.ArrayList;

import warped.utilities.utils.Console;

public abstract class WarpedKeyboardController  {
	
	protected String name = "Default Controller";
	
	//protected HashMap<Integer, WarpedKeyBind> keyBinds = new HashMap<>(); //Keybinds have exclusive keys, each key can be bound to only one of these actions
	protected ArrayList<WarpedKeyBind> keyBinds = new ArrayList<>(); //Hotbinds are NOT exclusive, a key may have multiple hot binds that will all trigger when pressed

	
	
	/**The name of this controller, only used for debug in the framework inspector.
	 * @param name - the name to display in the inspector for this controller.
	 * @author 5som3*/
	public void setName(String name) {this.name = name;}
	
	/**Get the name for this controller.
	 * @return String - the name.
	 * @author 5som3*/
	public String getName() {return name;}
	
	
	
	/**Get the hotbinds for this controller.
	 * @return ArrayList<WarpedKeyBind> - the hotbinds.
	 * @author 5som3*/
	public ArrayList<WarpedKeyBind> getHotBinds(){return keyBinds;}
	
	/**The number of hotbinds on the controller.
	 * @return int - the hotbind count.
	 * @author 5som3*/
	public int getBindingCount() {return keyBinds.size();}
	

	
	/**Restore all bindings on the controller to their default keys.
	 * @author 5som3*/
	public void restoreDefaultKeys() {
		for(WarpedKeyBind bind : keyBinds) {
			bind.restoreDefaultKey();
		}
	}

	/**Activate the press action for any bindings with the specified key.
	 * @param key - press action for bindings with this key.
	 * @author 5som3*/
	public void pressKey(int key) {
		keyBinds.forEach(b -> {if(b.key == key) b.press();});
	}
	
	/**Activate the release action for any bindings with the specified key.
	 * @param key - release action for bindings with this key.
	 * @author 5som3*/
	public void releaseKey(int key) {
		keyBinds.forEach(b -> {if(b.key == key) b.release();});
	}
	
	/**Clear all keybinds from this controller.
	 * @author 5som3*/
	public void clearBindings() {keyBinds.clear();}
	
	/**Add a keybinding to the controller.
	 * @param bind - the keybind to add to this controller.
	 * @apiNote Multiple keybinds can be bound to the same key. In these cases both bind actions will trigger when the key is interacted with.  
	 * @author 5som3*/
	public void addBinding(WarpedKeyBind bind) {
		Console.ln("WarpedKeyboardController -> addHotBind() -> adding binding " + bind.name + " , on key : " + bind.key);
		keyBinds.add(bind);
	}
	
	/**Remove a binding from the controller.
	 * @param index - the index of the keybinding to remove. 
	 * @author 5som3*/
	public void removeBinding(int index) {
		if(index < 0 || index >= keyBinds.size()) {
			Console.err("WarpedKeyboardController -> removeHotBind() -> index out of bounds : " + index);
			return;
		}
		keyBinds.remove(index);
	}
	

	

	
	
}
