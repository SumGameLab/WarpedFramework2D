/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import warped.application.state.WarpedState;
import warped.user.actions.WarpedAction;
import warped.utilities.utils.Console;

public abstract class WarpedKeyboardController implements WarpedKeyboardActions {
	
	/**
	 * HotBinds - 15/5/24 
	 * */
	protected String name = "Default Controller";
	protected WarpedControllerType type;
	
	protected WarpedKeyboardController() {};
	
	protected HashMap<Integer, WarpedKeyBind<?>> keyBinds = new HashMap<>(); //Keybinds have exclusive keys, each key can be bound to only one of these actions
	protected ArrayList<WarpedKeyBind<?>> hotBinds = new ArrayList<>(); //Hotbinds are NOT exclusive, a key may have multiple hot binds that will all trigger when pressed

	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	public HashMap<Integer, WarpedKeyBind<?>> getKeybinds(){return keyBinds;}
	public ArrayList<WarpedKeyBind<?>> getHotBinds(){return hotBinds;}
	public int getHotBindCount() {return hotBinds.size();}
	public int getKeyBindCount() {return keyBinds.size();}
	public WarpedControllerType getType() {return type;}
	
	public boolean isKeyListening() {
		 for(Map.Entry<Integer, WarpedKeyBind<?>> set : keyBinds.entrySet()) {
			 if(set.getValue().isListening) return true;
         }
		 return false;
	}	
	public boolean isKeyBound(int key) {if(keyBinds.containsKey(key)) return true; else return false;}
	public void stopListening() {
		for(Map.Entry<Integer, WarpedKeyBind<?>> set : keyBinds.entrySet()) {
			 set.getValue().stopListening();
        }		
	}
	public void restoreDefaultKeys() {
		HashMap<Integer, WarpedKeyBind<?>> binds = new HashMap<>();
		
		for(Map.Entry<Integer, WarpedKeyBind<?>> set : keyBinds.entrySet()){
			WarpedKeyBind<?> bind = set.getValue();
			bind.restoreDefaultKey();
			binds.put(bind.getKey(), bind);
		}	
		
		keyBinds = binds;	
	}

	//--------
	//---------------- Button --------
	//-------
	public void pressKey(int key) {
		if(keyBinds.containsKey(key)) keyBinds.get(key).press();
		hotBinds.forEach(b -> {if(b.key == key) b.press();});
	}
	public void releaseKey(int key) {
		if(keyBinds.containsKey(key)) keyBinds.get(key).release();
		hotBinds.forEach(b -> {if(b.key == key) b.release();});
	}
	
	//--------
	//---------------- Clear Bindings --------
	//-------
	public void clearBindings() {keyBinds.clear();}
	public void clearKeyBindPress(Integer key) {keyBinds.get(key).pressAction = null;}
	public void clearKeyBindRelease(Integer key){keyBinds.get(key).releaseAction = null;}
	public void clearBinding(Integer key) {
		clearKeyBindPress(key);
		clearKeyBindRelease(key);
	}
	
	//--------
	//---------------- KeyBinds --------
	//-------
	public void addKeyBind(WarpedKeyBind<?> keyBind) {
		if(keyBinds.containsKey(keyBind.getKey())) {
			Console.err("WarpedController -> addKeyBind() -> controller already contains binding for key : " + keyBind.key);
			return;
		}
		keyBinds.put(keyBind.getKey(), keyBind);
	}
	
	public void removeKeyBind(Integer key) {
		for(int i = 0; i < keyBinds.size(); i++){
			if(keyBinds.get(i).key == key) {
				keyBinds.remove(i);
				return;
			}
		}
		Console.err("WarpedController -> removeKeyBind() -> keyBind doesn't exist for key : " + key);
	}
	
	public void rebindKeyBind(Integer oldKey, Integer newKey) {
		if(!keyBinds.containsKey(oldKey)) {
			Console.err("WarpedController -> rebind() -> no keybind exists for key : " + oldKey);
			return;
		}
		if(keyBinds.containsKey(newKey)) {
			Console.err("WarpedController -> rebind() -> new key already has a binding, it will be overwritten");
		}
		keyBinds.get(oldKey).setKey(newKey);
		keyBinds.put(newKey, keyBinds.get(oldKey));
		keyBinds.remove(oldKey);
	}
	
	public void setKeyBindPress(Integer key, WarpedAction pressAction) {
		if(!keyBinds.containsKey(key)) {
			Console.err("WarpedController -> setPressAction() -> no keybind exits for key : " + key);
			return;
		}
		keyBinds.get(key).setPressAction(pressAction);
	}
	
	/**Replaces the bindings action with a new action if one is passed
	 * Passing a null action will not replace the action bound with null, it will be ignored
	 * If you want to clear an action use the clearPressBinding() / clearReleaseBinding() functions*/
	public void setKeyBindActions(Integer key, WarpedAction pressAction, WarpedAction releaseAction) {
		if(!keyBinds.containsKey(key)) {
			Console.err("WarpedController -> rewbind() -> no keybind exits for key : " + key);
			return;
		}
		keyBinds.get(key).setPressAction(pressAction);
		keyBinds.get(key).setReleaseAction(releaseAction);
	}	
	
	//--------
	//---------------- HotBinds --------
	//-------
	
	public void addHotBind(WarpedKeyBind<?> bind) {
		for(int i = 0; i < hotBinds.size(); i++) {
			WarpedKeyBind<?> b = hotBinds.get(i);
			if(bind.getBound() == null || b.getBound() == null) break;
			if(bind.getBound().equals(b.getBound())){
				WarpedState.notify.addNotification("Already bound to HotBar");
				return;
			}
		}
		Console.ln("WarpedKeyboardController -> addHotBind() -> adding binding " + bind.name + " , on key : " + bind.key);
		hotBinds.add(bind);
	}
	
	public void removeHotBind(int index) {
		if(index < 0 || index >= hotBinds.size()) {
			Console.err("WarpedKeyboardController -> removeHotBind() -> index out of bounds : " + index);
			return;
		}
		hotBinds.remove(index);
	}
	
	public void clearHotBinds() {
		hotBinds.clear();
	}

	

	
	
}
