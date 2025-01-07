/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.generalised;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum DayType {

	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY,
	SUNDAY;
	
	private static Map<Integer, DayType> map = new HashMap<>();
	static {
		for (DayType type : DayType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static DayType get(int index) {
	    return (DayType) map.get(index);
	}
	public static int size() {return map.size();}
	
	public static DayType getNextDay(DayType day) {
		if(day == SUNDAY) return MONDAY;
		else return DayType.get(day.ordinal() + 1);
	}
	public static DayType getPreviousDay(DayType day) {
		if(day == MONDAY) return SUNDAY;
		else return DayType.get(day.ordinal() - 1);
	}
	public static DayType getRandomDay() {return DayType.get(UtilsMath.random(SUNDAY.ordinal()));}
	
	public static String getString(DayType day) {
		switch(day) {
		case FRIDAY: 	return "Friday";
		case MONDAY: 	return "Monday";
		case SATURDAY:	return "Saturday";
		case SUNDAY:	return "Sunday";
		case THURSDAY:	return "Thursday";
		case TUESDAY:	return "Tuesday";
		case WEDNESDAY:	return "Wednesday";
		default: System.err.println("DayType -> getString() -> invalid case : " + day); return "ERRROR!";
		
		}
	}
	
	
}
