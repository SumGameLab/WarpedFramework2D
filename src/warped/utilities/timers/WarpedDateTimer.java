/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.timers;

import warped.utilities.enums.generalised.DayType;
import warped.utilities.enums.generalised.MonthType;
import warped.utilities.enums.generalised.SeasonType;
import warped.utilities.utils.UtilsMath;

public class WarpedDateTimer {

	
	private DayType day = DayType.MONDAY;
	private MonthType month = MonthType.JANUARY;
	private SeasonType season = SeasonType.SPRING;
	private int year = 2000;
	private int week = 1;
	
	public WarpedDateTimer() {
		day = DayType.getRandomDay();
		month = MonthType.getRandomMonth();
		year = UtilsMath.random.nextInt(2120, 2160);
	}
	
	
	public DayType getDay() {return day;}
	public int getWeek() {return week;}
	public MonthType getMonth() {return month;}
	public int getYear() {return year;}
	public SeasonType getSeason() {return season;}
	
	public void increment() {
		nextDay();
		if(day == DayType.MONDAY) nextWeek();
		if(day == DayType.MONDAY && week == 1) nextMonth();
		if(month == MonthType.JANUARY && week == 1 && day == DayType.MONDAY) year++;
		nextSeason();
	}
	
	public void nextDay() {day = DayType.getNextDay(day);}
	public void nextWeek() {
		week++;
		if(week > 4) week = 1;
	}
	public void nextMonth() {month = MonthType.getNextMonth(month);}
	public void nextYear() {year++;}
	public void nextSeason() {
		switch(month) {
		case JANUARY: 	case FEBUARY:	case MARCH:
			season = SeasonType.SUMMER;
			break;
		case APRIL:		case MAY: 		case JUNE:
			season = SeasonType.AUTUMN;
			break;
		case JULY:		case AUGUST: 	case SEPTEMBER:
			season = SeasonType.WINTER;
			break;
		case OCTOBER: 	case NOVEMBER: 	case DECEMBER:
			season = SeasonType.SPRING;
			break;
		default: System.err.println("WarpedDateTimer -> nextSeason() -> invalid case : " + month); break;
		
		}
	}
	
}
