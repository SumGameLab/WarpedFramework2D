/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.container;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public enum ContainerType {

	CRATE_SMALL,
	CRATE_MEDIUM,
	CRATE_LARGE,
	
	CHEST_SMALL,
	CHEST_MEDIUM,
	CHEST_LARGE;
	
	private static Map<Integer, ContainerType> map = new HashMap<>();
	static {
		for (ContainerType type : ContainerType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static ContainerType get(int index) {
	    return (ContainerType) map.get(index);
	}
	public static ContainerType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public String getString() {return getString(this);}
	public static String getString(ContainerType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
	public double getMass() {return getMass(this);}
	public static double getMass(ContainerType type) {
		switch(type) {
		case CHEST_LARGE:
		case CHEST_MEDIUM:
		case CHEST_SMALL:
		case CRATE_LARGE:
		case CRATE_MEDIUM:
		case CRATE_SMALL:
		default:
			System.err.println("ContainerType -> getMass() -> invalid case : " + type);
			return 0.0;
		}
	}
	
	public double getValue() {return getValue(this);}
	public static double getValue(ContainerType type) {
		switch(type) {
		case CHEST_LARGE:
		case CHEST_MEDIUM:
		case CHEST_SMALL:
		case CRATE_LARGE:
		case CRATE_MEDIUM:
		case CRATE_SMALL:
		default:
			System.err.println("ContainerType -> getValue() -> invalid case : " + type);
			return 0.0;	
		}
	}
	
	public ItemContainer generateContainer() {return generateContainer(this, 1);}
	public ItemContainer generateContainer(int quantity) {return generateContainer(this, quantity);}
	public static ItemContainer generateContainer(ContainerType type, int quantity) {
		if(quantity <= 0) {
			System.err.println("ContainerType -> generateContainer() -> quantity must be greater than 0 : " + quantity + ", it will be set to 1.");
			quantity = 1;
		}
		
		return new ItemContainer(type, quantity);
	}
	
	
}
