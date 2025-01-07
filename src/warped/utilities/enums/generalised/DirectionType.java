/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.generalised;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum DirectionType {
	
	UP,
	DOWN,
	LEFT,
	RIGHT,
	
	UP_LEFT,
	UP_RIGHT,
	
	DOWN_LEFT,
	DOWN_RIGHT;
	
	private static Map<Integer, DirectionType> map = new HashMap<>();
	static {
		for (DirectionType type : DirectionType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static DirectionType get(int index) {
	    return (DirectionType) map.get(index);
	}
	public static DirectionType getRandomDirectionType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public static DirectionType getRandomAxialDirection() {
		int rnd = UtilsMath.getD4();
		switch(rnd) {
		case 1: return UP;
		case 2: return DOWN;
		case 3: return LEFT;
		case 4: return RIGHT;
		default:
			System.err.println("DirectionType -> getRandomCartesianDirection() -> invalid case : " + rnd);
			return UP;
		}
	}
	
}
