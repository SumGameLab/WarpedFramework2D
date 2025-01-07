/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.equipment;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public enum EquipmentType {
	
	APPAREL,
	TOOL;
	
	private static Map<Integer, EquipmentType> map = new HashMap<>();
	static {
		for (EquipmentType type : EquipmentType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static EquipmentType get(int index) {
	    return (EquipmentType) map.get(index);
	}
	public static EquipmentType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	
	public String getString() {return getString(this);}
	public static String getString(EquipmentType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	

	public ItemEquipment generateEquipment() {return generateEquipment(this, 1);}
	public ItemEquipment generateEquipment(int quantity) {return generateEquipment(this, quantity);}
	public static ItemEquipment generateEquipment(EquipmentType type, int quantity) {
		if(quantity <= 0) {
			System.err.println("AmmunitionType -> generateAmmunition() -> quantity must be greater than 0 : " + quantity + ", it will be set to 1.");
			quantity = 1;
		}
		
		return new ItemEquipment(type, quantity);
	}
	
}
