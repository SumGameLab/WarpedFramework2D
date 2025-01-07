/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.ammunition;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public enum AmmunitionType {

	BULLET,
	MISSILE,
	TORPEDO;
	
	
	private static Map<Integer, AmmunitionType> map = new HashMap<>();
	static {
		for (AmmunitionType type : AmmunitionType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static AmmunitionType get(int index) {
	    return (AmmunitionType) map.get(index);
	}
	public static AmmunitionType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public double getMass() {return getMass(this);}
	public static double getMass(AmmunitionType type) {
		switch(type) {
		case BULLET:
		case MISSILE:
		case TORPEDO:
		default:
			System.err.println("AmmunitionType -> getMass() -> invalid case : " + type);
			return 0.0;
		}
	}
	
	public double getValue() {return getValue(this);}
	public static double getValue(AmmunitionType type) {
		switch(type) {
		case BULLET:
		case MISSILE:
		case TORPEDO:
		default:
			System.err.println("AmmunitionType -> getValue() -> invalid case : " + type);
			return 0.0;
		
		}
	}
	
	public String getString() {return getString(this);}
	public static String getString(AmmunitionType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
	public ItemAmmunition generateAmmunition() {return generateAmmunition(this, 1);}
	public ItemAmmunition generateAmmunition(int quantity) {return generateAmmunition(this, quantity);}
	public static ItemAmmunition generateAmmunition(AmmunitionType type, int quantity) {
		if(quantity <= 0) {
			System.err.println("AmmunitionType -> generateAmmunition() -> quantity must be greater than 0 : " + quantity + ", it will be set to 1.");
			quantity = 1;
		}
		
		return new ItemAmmunition(type, quantity);
	}
	
	
}


