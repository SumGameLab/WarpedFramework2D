/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.camera;

import java.util.HashMap;
import java.util.Map;

public enum WarpedCameraType {
	
	DEFAULT_OBJECT,
	DEFAULT_ENTITIE,
	DEFAULT_GUI,
	DEFAULT_ITEM,
	DEFAULT_DEPTH_FIELD,
	DEFAULT_TILE,
	DEFAULT_VFX;
	
	
	private static Map<Integer, WarpedCameraType> map = new HashMap<>();
	static {
        for (WarpedCameraType type : WarpedCameraType.values()) {
            map.put(type.ordinal(), type);
        }
    }
	public static WarpedCameraType get(int index) {
	    return (WarpedCameraType) map.get(index);
	}
	public static int size() {return map.size();}

	
}
