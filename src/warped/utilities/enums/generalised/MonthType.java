/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.generalised;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum MonthType {
	
	JANUARY,
	FEBUARY,
	MARCH,
	APRIL,
	MAY,
	JUNE,
	JULY,
	AUGUST,
	SEPTEMBER,
	OCTOBER,
	NOVEMBER,
	DECEMBER;
	
	private static Map<Integer, MonthType> map = new HashMap<>();
	static {
		for (MonthType type : MonthType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static MonthType getNextMonth(MonthType month){
		if(month == MonthType.DECEMBER) return JANUARY;
		else return MonthType.get(month.ordinal() + 1);
	}
	
	public static MonthType getPreviousMonth(MonthType month) {
		if(month == MonthType.JANUARY) return DECEMBER;
		else return MonthType.get(month.ordinal() - 1);
	}
	
	public static MonthType getRandomMonth() {return MonthType.get(UtilsMath.random(DECEMBER.ordinal()));}
	
	public static MonthType get(int index) {
	    return (MonthType) map.get(index);
	}
	public static int size() {return map.size();}
	
	
	public static String getString(MonthType month) {
		switch(month) {
		case APRIL: 	return "April";
		case AUGUST:	return "August";
		case DECEMBER:	return "December";
		case FEBUARY:	return "Febuary";
		case JANUARY:	return "January";
		case JULY:		return "July";
		case JUNE: 		return "June";
		case MARCH:		return "March";
		case MAY:		return "May";
		case NOVEMBER:	return "November";
		case OCTOBER: 	return "October";
		case SEPTEMBER: return "September";
		default: System.out.println("MonthType -> getString() -> invalid case : " + month); return "ERROR!";
		}
	}
	
	
}
