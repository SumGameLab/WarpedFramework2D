/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.drink;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public enum DrinkType {

	MINOR_HEALTH_POTION;
	
	private static Map<Integer, DrinkType> map = new HashMap<>();
	static {
		for (DrinkType type : DrinkType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static DrinkType get(int index) {
	    return (DrinkType) map.get(index);
	}
	public static DrinkType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public String getString() {return getString(this);}
	public static String getString(DrinkType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
	
	public ItemDrink generateDrink() {return generateDrink(this, 1);}
	public ItemDrink generateDrink(int quantity) {return generateDrink(this, quantity);}
	public static ItemDrink generateDrink(DrinkType type, int quantity) {
		if(quantity <= 0) {
			System.err.println("AmmunitionType -> generateAmmunition() -> quantity must be greater than 0 : " + quantity + ", it will be set to 1.");
			quantity = 1;
		}
		
		return new ItemDrink(type, quantity);
	}
	
}
