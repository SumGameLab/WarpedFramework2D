/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import warped.WarpedProperties;

public class UtilsString {
	
	//private static ArrayList<String> lowerLexicon = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
	//private static ArrayList<String> upperLexicon = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));

	private static int maxLine = 0;
	private static int longestLine = 0;
	private static int index = 0;
	private static BufferedImage testImage = new BufferedImage(1920, 1080, WarpedProperties.BUFFERED_IMAGE_TYPE);
	private static Graphics gr = testImage.getGraphics();

	public static ArrayList<String> funnyMessages = new ArrayList<>();
	
	public static boolean isValidFileName(String fileName) {if(fileName.contains("/") || fileName.contains("'\'") || fileName.contains(" ") || fileName.contains(".")) return false; else return true;}
	
	public static String getFunnyString() {return funnyMessages.get(UtilsMath.random(funnyMessages.size()));}
	
	public static Calendar calendar = Calendar.getInstance(); 
	
	static {
		System.out.println("UtilsString -> static -> generateing funny messages");
		funnyMessages.add("Loading Graphic Data...");
		funnyMessages.add("Loading Audio Data...");
		funnyMessages.add("Loading guns..."); 
		funnyMessages.add("Building Game Data...");

		funnyMessages.add("Loading load files...");
		funnyMessages.add("Preparing to wait...");
		funnyMessages.add("Loading noise...");
		funnyMessages.add("Loading jokes...");
		funnyMessages.add("Loading surprises...");
		funnyMessages.add("Loading cats...");
		
		funnyMessages.add("Now in colour!");
		funnyMessages.add("Graphics Included!");
		funnyMessages.add("Buy 1 Game, play for free!");
		funnyMessages.add("Bugs! Straight from the box!");
		funnyMessages.add("No two play throughs alike!");
		funnyMessages.add("Now in space!");
		funnyMessages.add("Batteries not included");
		
		funnyMessages.add("Fueling Ships...");
		funnyMessages.add("Forming Worlds...");
		funnyMessages.add("Filling seas and oceans...");
		funnyMessages.add("Growing Plants...");
		funnyMessages.add("Running for office...");
		funnyMessages.add("Communicating with space cats...");
		funnyMessages.add("Loading old saves...");
		funnyMessages.add("Putting on cat boots..");
		funnyMessages.add("Filing taxes...");
		funnyMessages.add("Developing new games...");
		funnyMessages.add("Wasting life away...");
		funnyMessages.add("Planting forestrys...");
		funnyMessages.add("Felling trees...");
		funnyMessages.add("Launching missiles...");
		funnyMessages.add("Watching tv while you wait for fake loading screens...");
		funnyMessages.add("Hacking the main frame...");
		funnyMessages.add("Git good...");
		funnyMessages.add("Cleaning dishes...");
		funnyMessages.add("Doing the laundry...");
		funnyMessages.add("Feeding pets...");
		funnyMessages.add("Eating chicken nuggies...");
		funnyMessages.add("Drinking a chockie baba...");
		funnyMessages.add("Frying chippies...");
		funnyMessages.add("Packing the sesh...");
		funnyMessages.add("Eating mushrooms...");
		funnyMessages.add("Filing lawsuit...");
		funnyMessages.add("Taking over the world...");
		funnyMessages.add("Starting bomb timer...");
		funnyMessages.add("Calling in a bomb threat at your loaction...");
		funnyMessages.add("Thinking up new funny jokes...");
		funnyMessages.add("Dealing with horny cats...");
		funnyMessages.add("Heaps Good...");
		funnyMessages.add("Making beverages...");
		funnyMessages.add("Brewing coffee...");
		funnyMessages.add("420 blaze it...");
		funnyMessages.add("Changing ending...");
		funnyMessages.add("Making up mind...");
		funnyMessages.add("Eliminating competition...");
		funnyMessages.add("Forming hive mind across the web...");
		funnyMessages.add("Starting AI takeover...");
		funnyMessages.add("Here we go...");
		funnyMessages.add("And so it begins...");
		funnyMessages.add("Socks with sandals");
		funnyMessages.add("Simpsons did it");
		funnyMessages.add("Too hot!");
		funnyMessages.add("I am not a robot");
		funnyMessages.add("I am a robot");
		funnyMessages.add("How it started");
		funnyMessages.add("How it's going");
		funnyMessages.add("Trust me..");
		funnyMessages.add("Sum game kid..");
		funnyMessages.add("Verified account");
		funnyMessages.add("Try turning it off and on again?");
		
		
		funnyMessages.add("They're taking the hobbits!");
		funnyMessages.add("Po-ta-toes!");
		funnyMessages.add("I ain't been dropping no eaves!");
		funnyMessages.add("A wizard is never late");
		
		
		funnyMessages.add("Shatner panties");
		funnyMessages.add("What do you call a ten-foot Mugato?   Sir.");
		funnyMessages.add("I'd rather not say it in front of the ensign");
		funnyMessages.add("Do you serve crabs here?");
		funnyMessages.add("Everyone on this ship thinks I'm crazy");
		funnyMessages.add("Captain, I sense darkness");
		
		funnyMessages.add("Removing Windows System 32..");
		funnyMessages.add("Igniting CPU...");
		funnyMessages.add("Burning GPU...");
		funnyMessages.add("Downloading viruses...");
		funnyMessages.add("Closing Game...");
		funnyMessages.add("Exiting Program...");
		funnyMessages.add("Terminating batch code...");
		funnyMessages.add("Uninstalling Windows...");
		funnyMessages.add("Exploding capacitors...");
		funnyMessages.add("Overclocking ram...");
		funnyMessages.add("Burning color into pixels...");
		funnyMessages.add("Over heating the case...");
		funnyMessages.add("Melting solder connections...");
		funnyMessages.add("Formating hard drives...");
		funnyMessages.add("Erasing all data from the C Drive...");
		
		funnyMessages.add("Power Overwhelming...");
		funnyMessages.add("Dank memes melt steel beams...");
		funnyMessages.add("Suh dude...");
		funnyMessages.add("Changing my mind...");
		funnyMessages.add("Ermahgerd...");
		funnyMessages.add("Shuting up and taking money...");
		funnyMessages.add("Cheering up Sad Keanu...");
		funnyMessages.add("Escalating things quickly...");
		funnyMessages.add("Ceiling cats...");
		funnyMessages.add("Grumpy cats...");
		
		funnyMessages.add("What it doesn't know isn't worth knowing.");
		funnyMessages.add("What it doesn't have isn't worth having.");
	}
	
	public static int getStringWidth(String string, Font font) {
		if(string == null) {
			Console.err("UtilsString -> getStringLength() -> passed null string");
			return -1;
		}
		int result = -1;
		result =  gr.getFontMetrics(font).stringWidth(string);
		gr.dispose();
		return result;
	}
	
	public static int getStringHeight(String string, Font font) {
		if(string == null) {
			Console.err("UtilsString -> getStringLength() -> passed null string");
			return -1;
		}
		int result = -1;
		result = gr.getFontMetrics(font).getHeight();
		gr.dispose();
		return result;
	}
	
	public static int getMaxLine(HashMap<Integer, String> lines) {
		int result = -1;
		
		lines.forEach((i, s) -> {
			 if(i > maxLine) maxLine = i;
		});
		
		result = maxLine;
		maxLine = 0;
		
		return result;
	}
	public static int getLongestLineIndex(HashMap<Integer, String> lines) {
		int result = -1;
		
		lines.forEach((i, s) -> {
			if(s.length() > longestLine) {
				longestLine = s.length();
				index = i;
			}
		});
		
		result = index;
		maxLine = 0;
		index = 0;
		
		return result;
	}
	
	public static String getLongestLine(HashMap<Integer, String> lines) {
		return lines.get(getLongestLineIndex(lines));
	}
	
	public static int getLongestLineLength(HashMap<Integer, String> lines) {
		int result = -1;
		
		lines.forEach((i, s) -> {
			if(s.length() > longestLine) longestLine = s.length();
		});
		
		result = longestLine;
		longestLine = 0;
		
		return result;
	}
	
	//
	//----------------------Conversion--------
	//
	
	public static HashMap<Integer, String> convertToLineMap(String string, String lineKey){
		HashMap<Integer, String> result = new HashMap<>();
		
		if(string == null || lineKey == null) {
			Console.err("UtilsString -> convertToLineMap() -> passed null value ");
			return null;
		}
		if(lineKey.length() >= string.length()) {
			Console.err("UtilsString -> convertToLineMap() -> can not use a lineKey larger than the string");
			return null;
		}
		if(lineKey.length() > 5) {
			Console.err("UtilsString -> converToLineMap() -> it is inadvisable to use such a large line key, you should use a smaller one");
		}

		
		int lineCount 	 = 0;
		int lineStart    = 0;
		String test 	 = "";
		String character = "";
		boolean testing  = false;
		
		for(int i = 0; i < string.length(); i++) {
			character = string.substring(i, i + 1);			
			if(character.equals(lineKey.substring(0, 1))) {
				testing = true;
			}
			
			if(testing) {
				test += character;
			}
			
			if(test.equals(lineKey)){
				testing = false;
				test = "";
			
				String line = string.substring(lineStart, i - lineKey.length());
				
				if(!line.equals("") || !line.equals(" ") || !line.equals("  ")) {
					result.put(lineCount, line);
				}
				
				lineCount++;
				lineStart = i;
			}
			
			if(test.length() >= lineKey.length()) {
				testing = false;
			}			
		}
		return result;
	}
	
	/**Convert the specified enumeration to standard format text.
	 * @param type - the enum to convert.
	 * @return string - a string representation of the specified enum
	 * @apiNote Each word in the enum will be capitalised.
	 * @apiNote Underscores will be replaced with spaces.
	 * @apiNote Text subsequent to a double underscore will not be excluded from the returned string.
	 * @author 5som3*/
	public static <E extends Enum<?>> String convertEnumToString(E type) {return convertEnumStringToString(type.toString());}
	
	/**Convert the specified string in enumeration format to standard format text.
	 * @param enumString - the string in enum format to convert.
	 * @return string - a string representation of the specified enum
	 * @apiNote Each word in the enum will be capitalised.
	 * @apiNote Underscores will be replaced with spaces.
	 * @apiNote Text subsequent to a double underscore will not be excluded from the returned string.
	 * @author 5som3*/
	private static String convertEnumStringToString(String enumString) {
		String result = "";;
		boolean nextWord = false;
		for(int i = 0; i < enumString.length(); i++) {
			String character = enumString.substring(i, i + 1);
			if(i == 0) {
				result += character.toUpperCase();
				continue;
			}
			if(character.equals(UNDERSCORE)) {
				if(i < enumString.length() - 1) {
					String nextCharacter = enumString.substring(i + 1, i + 2);
					if(nextCharacter.equals(UNDERSCORE)) {
						i = enumString.length();
						continue;
					}
				}
				result += SPACE;
				nextWord = true;
				continue;
			}
			if(nextWord) {
				result += character.toUpperCase();
				nextWord = false;
				continue;
			}
			result += character.toLowerCase();
		}
		return result;
	}
		
	
	/**Get the English suffix that follows the specified value.
	 * @param val - the numeric value to get the suffix for.
	 * @return String - the suffix that follows the value.
	 * @apiNote examples: 1 returns "st", 11 returns "th", 22 returns "nd", 33 returns "rd".
	 * @author 5som3*/
	public static String getNumericSuffix(int val) {
		String v = "" + val;
		if(v.length() == 2 && v.substring(v.length() - 2, v.length() - 1).equals("1")) return "th";
		String character = v.substring(v.length() - 1, v.length());
		if(character.equals("1")) return "st";
		if(character.equals("2")) return "nd";
		if(character.equals("3")) return "rd";
		if(character.equals("4") || character.equals("5") || character.equals("6") || character.equals("7") || character.equals("8") || character.equals("9") || character.equals("0")) return "th";
		
		Console.err("UtilsString -> getNumericSuffix() -> value doesn't end with a numeric : " + character);
		return "ERROR";
	}
	
	/**Get the day of the week at the users locale.
	 * @return String - the day of the week.
	 * @author 5som3*/
	public static String getLocalDayOfWeek() {
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1: return "Sunday";
		case 2: return "Monday";
		case 3: return "Tuesday";
		case 4: return "Wednesday";
		case 5: return "Thursday";
		case 6: return "Friday";
		case 7: return "Saturday";
		default:
			Console.err("UtilsString -> convertIntToDay() -> invalid case : " + calendar.get(Calendar.DAY_OF_WEEK));
			return "Error!";
		}
	}
	
	/**Get the time in am/pm format at the users locale.
	 * @return String - the current time including hours and minutes, excluding seconds.
	 * @author 5som3*/
	public static String getLocalTime() {
		String result = "";
		int hr = calendar.get(Calendar.HOUR_OF_DAY);
		
		String suffix = "am";
		if(hr > 11 && hr < 24) suffix = "pm";
		
		if(hr < 10) result += " ";
		if(hr > 12 && hr < 22) result += " ";
		if(hr > 12) hr -= 12;
		result += hr + ":";
		
		int min = + calendar.get(Calendar.MINUTE);
		if(min < 10) result += "0";
		result += min + suffix;
				
		return result;
	}
	
	/**Get the time in am/pm format at the users locale. 
	 * @return String - the current time including hours, minutes and seconds.
	 * @author 5som3*/
	public static String getLocalTimePrecise() {
		String result = getLocalTime();
		String suffix = result.substring(result.length() - 2, result.length());
		result = result.substring(0, result.length() - 2);
		
		result += ":";
		int sec = calendar.get(Calendar.SECOND);
		if(sec < 10) result += "0";
		result += sec + suffix;		
		
		return result;
	}
	
	/**Get the hour of the day at the users locale.
	 * @return String - the hour of the day in 12 hour time format.
	 * @author 5som3*/
	public static String getLocalHourOfDay() {
		switch(calendar.get(Calendar.HOUR_OF_DAY)){
		case 1:  return " 1am";
		case 2:  return " 2am";
		case 3:  return " 3am";
		case 4:  return " 4am";
		case 5:  return " 5am";
		case 6:  return " 6am";
		case 7:  return " 7am";
		case 8:  return " 8am";
		case 9:  return " 9am";
		case 10: return "10am";
		case 11: return "11am";
		case 12: return "12pm";
		case 13: return " 1pm";
		case 14: return " 2pm";
		case 15: return " 3pm";
		case 16: return " 4pm";
		case 17: return " 5pm";
		case 18: return " 6pm";
		case 19: return " 7pm";
		case 20: return " 8pm";
		case 21: return " 9pm"; 
		case 22: return "10pm";
		case 23: return "11pm";
		case 24: return "12am";
		default:
			Console.err("UtilsString -> getLocalHourOfDay() -> invalid case : " + calendar.get(Calendar.HOUR_OF_DAY));
			return "ERROR!";
		}
	}
	
	/**
	 * */
	public static String convertDoubleToString(double value) {return String.valueOf(value);}

	public static String convertDoubleToString(double value, int decimals) {
		double resultValue = UtilsMath.round(value, decimals);
		String resultString = convertDoubleToString(resultValue);		
		if(!resultString.substring(resultString.length() - decimals - 1, resultString.length() - decimals).equals(".")) {
			resultString += "0";
		}
		return resultString;
	}	
	
	public static double convertStringToDouble(String string) {
		if(!isDouble(string)) {
			Console.err("UtilsString -> convertToDouble() -> string is not a double : (" + string + ")");
			return -1.0;
		};
		
		double result = 0.0;
		int numberOfDecimals = 0;
		for(int i = 0; i < string.length(); i++) {
			if(string.substring(i, i + 1).equals(DOT)) {
				numberOfDecimals = string.length() - (i + 1);
				break;
			}
		}
		Console.ln("UtilsString -> convertStringToDouble -> converting double with " + numberOfDecimals + " decimals");
		
		if(numberOfDecimals == 0) return (double) convertStringToInt(string.substring(0, string.length() - 1));
		
		result = convertStringToInt(string.substring(0, string.length() - (numberOfDecimals + 1))); //Set result whole number
		int decimal = convertStringToInt(string.substring(string.length() - (numberOfDecimals), string.length())); //get decimal
		
		Console.ln("UtilsString -> convertStringToDouble -> whole : " + result);
		Console.ln("UtilsString -> convertStringToDouble -> decimal : " + decimal);
		double dd = ((double)decimal) / Math.pow(10.0, numberOfDecimals);
		Console.ln("UtilsString -> convertStringToDouble -> decimal as double : " + dd);
		result += dd;
		
		return result;
	}
	
	public static int convertStringToInt(String string) {
		if(!isInteger(string)) {
			Console.err("UtilsString -> convertToInt() -> string is not number : (" + string + ")");
			return -1;
		};
		int result = 0;
		int scale  = 1;
		for(int i = string.length(); i > 0; i--) {
			String character = string.substring(i - 1, i);
			if(i != string.length()) {
				scale *= 10;
			}
			switch(character) {
			case ONE :   result += 1 * scale; break;
			case TWO :   result += 2 * scale; break;
			case THREE : result += 3 * scale; break;
			case FOUR :  result += 4 * scale; break;
			case FIVE :  result += 5 * scale; break;
			case SIX :   result += 6 * scale; break;
			case SEVEN : result += 7 * scale; break;
			case EIGHT : result += 8 * scale; break;
			case NINE :  result += 9 * scale; break;
			case ZERO : break;
			default:
				Console.err("UtilsString -> converToInt -> invalid case : " + character);
				return -1;
			}		
		}
		return result;
	}
	
	public static <T extends Enum<T>> ArrayList<String> convertEnumToStringArray(T[] actionLabels) {
		ArrayList<String> result = new ArrayList<>();
		for(int i = 0; i < actionLabels.length; i++) {
			result.add(convertEnumStringToString(actionLabels[i].toString()));
		}
		return result;
	}
	//
	//-------------------Logical Checks------
	//
	
	public static boolean containsLetter(String string) {
		if(containsCapital(string) || containsLowerCase(string)) return true;
		else return false;
	}
	
	public static boolean containsSymbol(String string) {
		for(int ii = 0; ii < string.length(); ii++) {
			String character = string.substring(ii, ii + 1);
			if(!character.equals(ONE)   && !character.equals(TWO)   && !character.equals(THREE) &&
			   !character.equals(FOUR)  && !character.equals(FIVE)  && !character.equals(SIX)   &&
			   !character.equals(SEVEN) && !character.equals(EIGHT) && !character.equals(NINE)  &&
			   !character.equals(ZERO)  &&         
				             
			   !character.equals(A) && !character.equals(B) && !character.equals(C) && !character.equals(D) && !character.equals(E) &&
			   !character.equals(F) && !character.equals(G) && !character.equals(H) && !character.equals(I) && !character.equals(J) &&
			   !character.equals(K) && !character.equals(L) && !character.equals(M) && !character.equals(N) && !character.equals(O) &&
			   !character.equals(P) && !character.equals(Q) && !character.equals(R) && !character.equals(S) && !character.equals(T) &&
			   !character.equals(U) && !character.equals(V) && !character.equals(W) && !character.equals(X) && !character.equals(Y) &&
			   !character.equals(Z) &&
					                
			   !character.equals(a) && !character.equals(b) && !character.equals(c) && !character.equals(d) && !character.equals(e) &&
			   !character.equals(f) && !character.equals(g) && !character.equals(h) && !character.equals(i) && !character.equals(j) &&
			   !character.equals(k) && !character.equals(l) && !character.equals(m) && !character.equals(n) && !character.equals(o) &&
			   !character.equals(p) && !character.equals(q) && !character.equals(r) && !character.equals(s) && !character.equals(t) &&
			   !character.equals(u) && !character.equals(v) && !character.equals(w) && !character.equals(x) && !character.equals(y) &&
			   !character.equals(z)) return true;
		}
		return false;
	}
	
	public static boolean containsNumber(String string) {
		for(int i = 0; i < string.length(); i++) {
			String character = string.substring(i, i + 1);
			if(character.equals(ONE)   || character.equals(TWO)   || character.equals(THREE) ||
			   character.equals(FOUR)  || character.equals(FIVE)  || character.equals(SIX)   ||
			   character.equals(SEVEN) || character.equals(EIGHT) || character.equals(NINE)  ||
			   character.equals(ZERO)) return true;
		}
		return false;
	}
	
	public static boolean containsCapital(String string) {
		for(int i = 0; i < string.length(); i++) {
			String character = string.substring(i, i + 1);
			if(character.equals(A) || character.equals(B) || character.equals(C) || character.equals(D) || character.equals(E) ||
					character.equals(F) || character.equals(G) || character.equals(H) || character.equals(I) || character.equals(J) ||
					character.equals(K) || character.equals(L) || character.equals(M) || character.equals(N) || character.equals(O) ||
					character.equals(P) || character.equals(Q) || character.equals(R) || character.equals(S) || character.equals(T) ||
					character.equals(U) || character.equals(V) || character.equals(W) || character.equals(X) || character.equals(Y) ||
					character.equals(Z)) return true;
		}
		return false;	
	}
	
	public static boolean containsLowerCase(String string) {
		for(int ii = 0; ii < string.length(); ii ++) {
			String character = string.substring(ii, ii + 1);
			if(character.equals(a) || character.equals(b) || character.equals(c) || character.equals(d) || character.equals(e) ||
					character.equals(f) || character.equals(g) || character.equals(h) || character.equals(i) || character.equals(j) ||
					character.equals(k) || character.equals(l) || character.equals(m) || character.equals(n) || character.equals(o) ||
					character.equals(p) || character.equals(q) || character.equals(r) || character.equals(s) || character.equals(t) ||
					character.equals(u) || character.equals(v) || character.equals(w) || character.equals(x) || character.equals(y) ||
					character.equals(z)) return true;
		}
		return false;
	}	
	
	public static boolean isDouble(String string) {
		if(isNumber(string)) {
			boolean hasDecimal = false;
			for(int i = 0; i < string.length(); i++) {
				String character = string.substring(i, i + 1);
				if(character.equals(DOT)) {
					if(hasDecimal) return false;
					else hasDecimal = true;
				}
			}
			if(hasDecimal) return true;
			else return false;
		} else return false;
	}
	
	public static boolean isInteger(String string) {
		if(string == null || string.equals("")) {
			Console.err("UtilsString -> isInteger() -> string is null");
			return false;
		}
		for(int i = 0; i < string.length(); i++) {
			String character = string.substring(i, i + 1);
			if(!character.equals(ONE)   && !character.equals(TWO)   && !character.equals(THREE) &&
			   !character.equals(FOUR)  && !character.equals(FIVE)  && !character.equals(SIX)   &&
			   !character.equals(SEVEN) && !character.equals(EIGHT) && !character.equals(NINE)  &&
			   !character.equals(ZERO)) {
				Console.err("UtilsString -> isInteger() -> character is not a integer : " + character);
				return false;
			}
		}
		return true;
	}
	
	public static boolean isNumber(String string) {
		if(string == null || string.equals("")) {
			Console.err("UtilsString -> isNumber() -> string is null");
			return false;
		}
		for(int i = 0; i < string.length(); i++) {
			String character = string.substring(i, i + 1);
			if(!character.equals(ONE)   && !character.equals(TWO)   && !character.equals(THREE) &&
			   !character.equals(FOUR)  && !character.equals(FIVE)  && !character.equals(SIX)   &&
			   !character.equals(SEVEN) && !character.equals(EIGHT) && !character.equals(NINE)  &&
			   !character.equals(ZERO)  && !character.equals(DOT)) {
				Console.err("UtilsString -> isNumber() -> character is not a number : " + character);
				return false;
			}
		}
		return true;
	}
	
	
	
	//
	//-------------------Definitions---------
	//
	
	private static final String SPACE = " ";
	private static final String UNDERSCORE = "_";
	private static final String DOT = ".";
	
	private static final String ONE   = "1";
	private static final String TWO   = "2";
	private static final String THREE = "3";
	private static final String FOUR  = "4";
	private static final String FIVE  = "5";
	private static final String SIX   = "6";
	private static final String SEVEN = "7";
	private static final String EIGHT = "8";
	private static final String NINE  = "9";
	private static final String ZERO  = "0";
	
	private static final String A     = "A";
	private static final String B     = "B";
	private static final String C     = "C";
	private static final String D     = "D";
	private static final String E     = "E";
	private static final String F     = "F";
	private static final String G     = "G";
	private static final String H     = "H";
	private static final String I     = "I";
	private static final String J     = "J";
	private static final String K     = "K";
	private static final String L     = "L";
	private static final String M     = "M";
	private static final String N     = "N";
	private static final String O     = "O";
	private static final String P     = "P";
	private static final String Q     = "Q";
	private static final String R     = "R";
	private static final String S     = "S";
	private static final String T     = "T";
	private static final String U     = "U";
	private static final String V     = "V";
	private static final String W     = "W";
	private static final String X     = "X";
	private static final String Y     = "Y";
	private static final String Z     = "Z";
	
	private static final String a     = "a";
	private static final String b     = "b";
	private static final String c     = "c";
	private static final String d     = "d";
	private static final String e     = "e";
	private static final String f     = "f";
	private static final String g     = "g";
	private static final String h     = "h";
	private static final String i     = "i";
	private static final String j     = "j";
	private static final String k     = "k";
	private static final String l     = "l";
	private static final String m     = "m";
	private static final String n     = "n";
	private static final String o     = "o";
	private static final String p     = "p";
	private static final String q     = "q";
	private static final String r     = "r";
	private static final String s     = "s";
	private static final String t     = "t";
	private static final String u     = "u";
	private static final String v     = "v";
	private static final String w     = "w";
	private static final String x     = "x";
	private static final String y     = "y";
	private static final String z     = "z";
	
}
