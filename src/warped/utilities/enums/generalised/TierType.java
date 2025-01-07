/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.generalised;

import java.util.HashMap;
import java.util.Map;

public enum TierType {

	TIER_1,
	TIER_2,
	TIER_3,
	TIER_4,
	TIER_5,
	TIER_6,
	TIER_7,
	TIER_8,
	TIER_9,
	TIER_10;
	
	
	private static Map<Integer, TierType> map = new HashMap<>();
	static {
		for (TierType type : TierType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static TierType get(int index) {
	    return (TierType) map.get(index);
	}
	
	public static int size() {return map.size();}
	
}
