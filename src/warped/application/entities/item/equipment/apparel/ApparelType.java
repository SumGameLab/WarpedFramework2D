/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.equipment.apparel;

import warped.utilities.utils.UtilsString;

public enum ApparelType {

	HAT;
	public String getString() {return getString(this);}
	public static String getString(ApparelType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
}
