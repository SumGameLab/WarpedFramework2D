/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.food;

import warped.utilities.utils.UtilsString;

public enum FoodType {

	DOUGHNUT;
	public String getString() {return getString(this);}
	public static String getString(FoodType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
	public ItemFood generateFood() {return generateFood(this, 1);}
	public ItemFood generateFood(int quantity) {return generateFood(this, quantity);}
	public static ItemFood generateFood(FoodType type, int quantity) {
		if(quantity <= 0) {
			System.err.println("AmmunitionType -> generateAmmunition() -> quantity must be greater than 0 : " + quantity + ", it will be set to 1.");
			quantity = 1;
		}
		
		return new ItemFood(type, quantity);
	}
	
	
}
