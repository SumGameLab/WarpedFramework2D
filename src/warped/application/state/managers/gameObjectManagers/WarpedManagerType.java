/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.Console;

public enum WarpedManagerType {

	VFX,
	OBJECT,
	ENTITIE,
	DEPTH_FIELD,
	TILE,
	ITEM,
	GUI;
	
	
	private static Map<Integer, WarpedManagerType> map = new HashMap<>();
	static {
        for (WarpedManagerType type : WarpedManagerType.values()) {
            map.put(type.ordinal(), type);
        }
    }
	public static WarpedManagerType get(int index) {
		if(index < 0 ) {
			Console.err("ContextManagerType -> get() -> index is less than 0 : " + index);
			return null;
		}
		if(index >= map.size()) {
			Console.err("ContextManagerType -> get() -> index is larger than map size : " + index);
			return null;
		}
		if(map.get(index) == null) {
			Console.err("ContextManagerType -> get() -> map contains a null value at index : " + index);
			return null;
		}
	    return (WarpedManagerType) map.get(index);
	}
	
	public static WarpedManagerType getPreviousManager(WarpedManagerType managerType) {
		if(managerType == null) {
			Console.err("ContextManagerType -> getNextManager() -> passed null manager Type");
			return get(1);
		}
		int index = managerType.ordinal() - 1;
		if(index < 0) index = size() - 1;
		return get(index);
	}
	
	public static WarpedManagerType getNextManager(WarpedManagerType managerType) {
		if(managerType == null) {
			Console.err("ContextManagerType -> getNextManager() -> passed null manager Type");
			return get(1);
		}
		int index = managerType.ordinal() + 1; 		
		if( index >= map.size()) index = 0;
		return get(index);
	}
	
	
	public static int size() {return map.size();}
	
}
