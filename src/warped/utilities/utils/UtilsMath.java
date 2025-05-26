/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import warped.utilities.enums.generalised.RotationType;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;

public class UtilsMath {
	
	//--------
	//---------------- Definitions ----------------
	//--------

	public static final Random random = new Random();
	
	/**One sixteenth of a full rotation.
	 * @return double - rotation in radians.
	 * @author 5som3*/
	public static final double PI_ON_EIGHT	   = Math.PI * 0.125;
	
	/**One eighth of a full rotation.
	 * @return double - rotation in radians.
	 * @author 5som3*/
	public static final double PI_ON_FOUR 	   = Math.PI * 0.25;
	
	/**One quarter of a full rotation.
	 * @return double - rotation in radians.
	 * @author 5som3*/
	public static final double PI_ON_TWO 	   = Math.PI * 0.5;
	
	/**One half of a full rotation.
	 * @return double - rotation in radians.
	 * @author 5som3*/
	public static final double PI 			   = Math.PI * 1.0;
	
	/**Three quarters of a full rotation.
	 * @return double - rotation in radians.
	 * @author 5som3*/
	public static final double THREE_PI_ON_TWO = Math.PI * 1.5;
	
	/**A full rotation.
	 * @return double - rotation in radians.
	 * @author 5som3*/
	public static final double TWO_PI  		   = Math.PI * 2.0; 
	
	//--------
	//---------------- Convert rotation --------
	//--------
	
	/**Convert degrees to radian equivalent.
	 * @param degrees - the angle in to convert.
	 * @return double - the radian equivalent.
	 * @author 5som3*/
	public static final double convertDegreesToRadians(double degrees) {return degrees * 0.0174533;}
	
	/**Convert radians to degree equivalent.
	 * @param radians - the radians in to convert.
	 * @return double - the degree equivalent.
	 * @author 5som3*/
	public static final double convertRadiansToDegrees(double radians) {return radians * 57.2958;}
	
	//--------
	//---------------- Convert Temperature --------
	//--------
	
	/**Convert celsius to equivalent fahrenheit.
	 * @param celsius - the value to convert.
	 * @return double - fahrenheit equivalent.
	 * @author 5som3*/
	public static final double convertCelsiusToFahrenheit(double celsius) {return celsius * 33.8;}
	
	/**Convert celsius to the equivalent kelvin.
	 * @param celsius - the value to convert.
	 * @return double - the kelvin equivalent.
	 * @author 5som3*/
	public static final double covertCelsiusToKelvin(double celsius) {return celsius + 273.15;}
	
	/**Convert fahrenheit to celsius.
	 * @param fahrenheit - the value to convert.
	 * @return double - celsius equivalent.
	 * @author 5som3*/
	public static final double convertFahrenheitToCelsius(double fahrenheit) {return fahrenheit / 33.8;}
	
	//--------
	//---------------- Convert Length --------
	//--------
	
	
	/**Convert yards to meters.
	 * @param yards - the value to convert.
	 * @return double - meter equivalent.
	 * @author 5som3*/
	public static final double convertYardToMeters(double yards) {return yards * 0.9144;}
	
	/**Convert feet to meters.
	 * @param feet - the value to convert.
	 * @return double - meter equivalent.
	 * @author 5som3*/
	public static final double convertFeetToMeters(double feet) {return feet * 0.3048;}
	
	/**Convert inches to meters.
	 * @param yards - the value to convert.
	 * @return double - meter equivalent.
	 * @author 5som3*/
	public static final double convertInchesToMeters(double inches) {return inches * 0.0254;}
	
	
	//--------
	//---------------- Convert Mass --------
	//--------
	
	/**Convert ounces to kilograms.
	 * @param ounces - the value to convert.
	 * @return double - the kilogram equivalent. 
	 * @author 5som3*/
	public static final double convertOuncesToKilo(double ounces) {return ounces * 0.0283495;}
	
	/**Convert pounds to kilograms.
	 * @param pounds - the value to convert.
	 * @return double - the kilogram equivalent. 
	 * @author 5som3*/
	public static final double convertPoundsToKilo(double pounds) {return pounds * 0.453592;}
	
	/**Convert tons to kilograms.
	 * @param ounces - the value to convert.
	 * @return double - the kilogram equivalent. 
	 * @author 5som3*/
	public static final double convertTonToKilo(double tons) {return tons * 1016.05;}
	
	//--------
	//---------------- Convert time --------
	//--------
	
	/**The duration of a single cycle given a specific frequency.
	 * @param frequency - the framerate measured in frames per second.
	 * @return long - the duration of a single cycle in milliseconds. 
	 * @author 5som3*/
	public static final long convertHzToMillis(int frequency) {return (long) (1000 / frequency);}

	
	//--------
	//---------------- Basic Maths --------
	//--------	
	
	/**The absolute square of a number.
	 * @param value - the value to square. 
	 * @return int - the absolute square. 
	 * @author 5som3*/
	public static int square(int value) {return (value * value);}
	
	/**The absolute square of a number.
	 * @param value - the value to square. 
	 * @return double - the absolute square. 
	 * @author 5som3*/
	public static double square(double value) {return (value * value);}
	
	/**Round a decimal value to a specific number of decimal points.
	 * @param value - the value to round.
	 * @param decimalPlaces - the number of decimals the result will have.
	 * @return double - the rounded number. i.e. round(3.1415, 3) = 3.142.
	 * @author 5som3*/
	public static double round(double value, int decimalPlaces) {
		if (decimalPlaces < 1 || decimalPlaces > 9) {
			Console.err("Maths -> nextDouble -> invalid number of decimal places : " + decimalPlaces);
			if(decimalPlaces < 1) decimalPlaces = 1;	
			else decimalPlaces = 9;
		}
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(decimalPlaces + 1, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	
	/**Round a decimal value to a specific number of decimal points.
	 * @param value - the value to round.
	 * @param decimalPlaces - the number of decimals the result will have.
	 * @return double - the rounded number. i.e. round(3.1415, 3) = 3.142.
	 * @author 5som3*/
	public static float round(float value, int decimalPlaces) {
		if (decimalPlaces < 0) {
			Console.err("Maths -> nextDouble -> invalid number of decimal places : " + decimalPlaces);
			decimalPlaces = 1;			
		}
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
		return bd.floatValue();
	}
	
	/**Divide two integers with double precision.
	 * @param a - the numerator.
	 * @param b - the denominator.
	 * @return double - the result in double precision.
	 * @author 5som3*/
	public static double divide(int a, int b) {return ((double)a)/ ((double)b);}
	
	/**Generate an inverted array of booleans. 
	 * @param array - the array to invert.
	 * @return boolean[] - a new array inverse to the original one.
	 * @apiNote All trues will become false and all false will become true.
	 * @apiNote The original array will be unmodified.
	 * @author 5som3*/
	public static boolean[] inverted(boolean[] array) { 
		boolean[] result = new boolean[array.length];
		for(int i = 0; i < array.length; i++) {
			if(array[i]) result[i] = false;
			else result[i] = true;
		}
		return result;
	}
	
	/**Generate an inverted array of doubles.
	 * @param array - the array to invert.
	 * @return double[] - a new array inverse to the original one.
	 * @apiNote All positives will become negative and all negatives will become positive.
	 * @apiNote The original array will be unmodified. 
	 * @author 5som3*/
	public static double[] inverted(double[] array) {
		double[] result = new double[array.length];
		for(int i = 0; i < array.length; i++) result[i] = 1 / array[i];
		return result;		
	}
	
	/**Generate an inverted array of integers.
	 * @param array - the array to invert.
	 * @return int[] - a new array inverse to the original one.
	 * @apiNote All positives will become negative and all negatives will become positive.
	 * @apiNote The original array will be unmodified. 
	 * @author 5som3*/
	public static int[] inverted(int[] array) {
		int[] result = new int[array.length];
		for(int i = 0; i < array.length; i++) result[i] = 1 / array[i];
		return result;		
	}
	
	/**Invert the specified array of booleans
	 * @param array - the array to invert.
	 * @apiNote All trues will become false and all false will become true.
	 * @author 5som3*/
	public static void invert(boolean[] array) {
		for(int i = 0; i < array.length; i++) {
			array[i] = array[i] ? false : true;
		}
	}
	
	/**Invert the specified array of integers.
	 * @param array - the array to invert.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static void invert(int[] array) {
		for(int i = 0; i < array.length; i++) {
			array[i] = -array[i];
		}
	}
	
	/**Invert the specified array of doubles.
	 * @param array - the array to invert.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static void invert(double[] array) {
		for(int i = 0; i < array.length; i++) {
			array[i] = -array[i];
		}
	}
	
	/**Invert the specified array of integers.
	 * @param array - the array to invert.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static void invert(int[][] array) {
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[i].length; j++) {				
				array[i][j] = -array[i][j];
			}
		}
	}
	
	/**Invert the specified array of doubles.
	 * @param array - the array to invert.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static void invert(double[][] array) {
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[i].length; j++) {				
				array[i][j] = -array[i][j];
			}
		}
	}
	
	/**Invert the specified list of integers.
	 * @param list - the list to invert.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static void invertIntegers(List<Integer> list) {
		for(int i = 0; i < list.size(); i++) {
			list.set(i, -list.get(i));
		}
	}
	
	/**Invert the specified list of doubles
	 * @param list - the list to invert.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static void invertDoubles(List<Double> list) {
		for(int i = 0; i < list.size(); i++) {
			list.set(i, -list.get(i));
		}
	}
	
	/**Invert the specified list of booleans
	 * @param list - the list to invert
	 * @apiNote All trues will become false and all false will become true.
	 * @author 5som3*/
	public static void invertBooleans(List<Boolean> list) {
		for(int i = 0; i < list.size(); i++) {
			list.set(i, list.get(i) ? false : true);
		}
	}
	
	/**Generate an inverted list based on the specified list. 
	 * @param list - the list of integers to invert.
	 * @return List<Integer> - a new list of integers inverse to the specified list.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static List<Integer> invertedIntegers(List<Integer> list){
		ArrayList<Integer> result = new ArrayList<>();
		for(int i = 0; i < list.size(); i++) {
			result.add(-list.get(i));
		}
		return result;
	}

	/**Generate an inverted list based on the specified list. 
	 * @param list - the list of integers to invert.
	 * @return List<Double> - a new list of integers inverse to the specified list.
	 * @apiNote All positives will become negative and all negatives will become positive. 
	 * @author 5som3*/
	public static List<Double> invertedDoubles(List<Double> list){
		ArrayList<Double> result = new ArrayList<>();
		for(int i = 0; i < list.size(); i++) {
			result.add(-list.get(i));
		}
		return result;
	}
	
	/**Generate an inverted list based on the specified list. 
	 * @param list - the list of booleans to invert.
	 * @return List<Boolean> - a new list of booleans inverse to the specified list.
	 * @apiNote All trues will become false and all false will become true.
	 * @author 5som3*/
	public static List<Boolean> invertedBooleans(List<Boolean> list){
		ArrayList<Boolean> result = new ArrayList<>();
		for(int i = 0; i < list.size(); i++) {
			result.add(list.get(i) ? false : true);
		}
		return result;
	}
	
	//--------
	//---------------- Angular Math -------- 
	//--------	
	public static VectorD getUnitVector(double rotation) {return new VectorD(Math.cos(rotation), Math.sin(rotation));}
	
	public static VectorD getUnitVector(double rotation, double scale) {return new VectorD(Math.cos(rotation) * scale, Math.sin(rotation) * scale);}
	/**The shortest angular distance from r1 to r2
	 * The r1 and r2 are defined as positive rotations from the positive x axis*/
	public static RotationType findShortestRotation(double r1, double r2) {
		if(r1 < 0 || r2 < 0 || r1 > UtilsMath.TWO_PI || r2 > UtilsMath.TWO_PI) {
			Console.err("Maths -> findShortestRotation() -> angles are out side of range 0 <= angle <= 2Pi : (r1, r2) : (" + r1 + ", " + r2 + ") they will be clamped");
			r1 = clampRadianRotation(r1);
			r2 = clampRadianRotation(r2);
		}
		double clockwiseDistance 	 = 0.0;
		double antiClockwiseDistance = 0.0;
		if(r2 > r1) {
			clockwiseDistance = r2 - r1;
			antiClockwiseDistance = (UtilsMath.TWO_PI - r2) + r1;
		} else {
			clockwiseDistance = (UtilsMath.TWO_PI - r1) + r2;
			antiClockwiseDistance = r1 - r2;
		}
		if(clockwiseDistance < antiClockwiseDistance) return RotationType.CLOCKWISE;
		else return RotationType.ANTICLOCKWISE;
	}

	
	public static double angleBetweenVectors(Point p1, Point p2){
		double result =	angleBetweenVectors(p1.x, p1.y, p2.x, p2.y);
		if(Double.isNaN(result)) return 0.0;
		else return result;
	}
	
	public static double angleBetweenVectorsSigned(Point p1, Point p2){
		double result =	angleBetweenVectorsSigned(p1.x, p1.y, p2.x, p2.y);
		if(Double.isNaN(result)) return 0.0;
		else return result;
	}
	
	public static double angleBetweenVectorsSigned(double vec1X, double vec1Y, double vec2X, double vec2Y) {	
		return Math.atan2( determinant(vec1X, vec1Y, vec2X, vec2Y), dotProduct(vec1X, vec1Y, vec2X, vec2Y));
		
	}
	
	public static double angleBetweenVectors(double vec1X, double vec1Y, double vec2X, double vec2Y) {	
		return Math.acos( 		  (dotProduct(vec1X, vec1Y, vec2X, vec2Y)) 
						/ (magnitude(vec1X, vec1Y) * magnitude(vec2X, vec2Y)) );
	}
	
	//--------
	//---------------- Vector Math --------
	//--------
	public static double dotProduct(double x1, double y1, double x2, double y2) {return ( (x1 * x2) + (y1 * y2) );}
	public static double determinant(double x1, double y1, double x2, double y2) {return ( (x1 * y2) - (y1 * x2));}
	public static double magnitude(double x, double y) {return Math.sqrt( square(x) + square(y));}
	public static double distanceBetweenVectors(int x1, int y1, int x2, int y2) {
		double result = Math.sqrt(square(x2 - x1) + square(y2 - y1) );
		return result;
	}
	
	public static double distanceBetweenVectors(double x1, double y1, double x2, double y2) {
		double result = Math.sqrt( square(x2 - x1) + square(y2 - y1) );
		return result;
	}
	
	//
	//------------------ Math to String ----------------------------
	//
	
	public static void printHex(int value) {
		System.out.printf("^x\n", value);}
	public static void printBin(int value) {Console.ln(Integer.toBinaryString(value));}
	
	
	/**Get a decimal value as a string with the specified number of decimal points.
	 * @param value - the value to round.
	 * @param decimalPlaces - the number of decimals the result will have.
	 * @return string - the rounded number. i.e. round(3.1415, 3) = 3.142.
	 * @author 5som3*/
	public static String getString(double value, int decimalPlaces) {
		String result = "";
		if (decimalPlaces < 1 || decimalPlaces > 9) {
			Console.err("Maths -> nextDouble -> invalid number of decimal places : " + decimalPlaces);
			decimalPlaces = 1;			
		}
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(decimalPlaces , RoundingMode.CEILING);
		int decimalDiff = decimalPlaces - getNumberOfDecimals(bd);
		
		result += bd.doubleValue();
		if(isIntegerValue(bd)) for(int i = 0; i < decimalPlaces - 1; i++) result += "0";
		else for(int i = 0; i < decimalDiff; i++) {result += "0";}
		return result;
	}
	
	//--------
	//----------------------Comparators -----------------------
	//--------
	
	/**Determine if the value has significant decimals.
	 * @param bd - the value to check.
	 * @return true if the decimals are all 0 else false;
	 * @author 5som3*/
	public static boolean isIntegerValue(BigDecimal bd) {return bd.stripTrailingZeros().scale() <= 0;}
	
	/**Determine the number of decimal places.
	 * @param bigDecimal - the value to check
	 * @return int - the number of decimal places
	 * @author 5som3*/
	public static int getNumberOfDecimals(BigDecimal bigDecimal) {return Math.max(0, bigDecimal.stripTrailingZeros().scale());}
	
	public static boolean isEqual(boolean[] b1, boolean[] b2) {
		if(b1.length != b2.length) return false;
		boolean result = true;
		for(int i = 0; i < b1.length; i++) {
			if(b1[i] != b2[i]) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	public static boolean isEqualFrom(boolean[] b1, boolean[] b2, int startIndex) {
		if(startIndex < 0) {
			Console.err("UtilsMath -> isEqualFrom() -> start index is less than 0");
			return false;
		}
		
		boolean result = true;
		
		int endIndex = 0;
		if(b1.length < b2.length) {
			endIndex = startIndex + b1.length;
			
			int i = -1;
			for(int j = startIndex; j < endIndex; j++) {
				i++;
				
				if(b1[i] != b2[j]) {
					result = false;
					break;
				}
			}
		} else {
			endIndex = startIndex + b2.length;
			
			int i = -1;
			for(int j = startIndex; j < endIndex; j++) {
				i++;
				
				if(b1[i] != b2[j]) {
					result = false;
					break;
				}
			}
		}
		
		return result;
	}
	
	public static boolean isEqualAt(boolean[] b1, boolean[] b2, List<Integer> indices) {		
		if(b1.length == indices.size()) {
			for(int i = 0; i < indices.size(); i++) {
				if(!isEqualAt(b2, b1[i], indices.get(i))) return false;
			}
			return true;
		} else if(b2.length == indices.size()) {
			for(int i = 0; i < indices.size(); i++) {
				if(!isEqualAt(b1, b2[i], indices.get(i))) return false;
			}
			return true;
		} else {			
			Console.err("MathUtils -> isEqualAt() -> indices don't match either boolean arrays");
			return false;
		}
		
	}
	
	public static boolean isEqualAt(boolean[] b1, boolean b, int index) {
		if(index < 0 || index >= b1.length) {
			Console.err("UtilsMath -> isEqualAt() -> index is out of bounds : " + index);
			return false;			
		}
		
		if(b1[index] == b) return true; else return false;
	}
	
	public static boolean isEqualBeginning(boolean[] b1, boolean[] b2) { //Fore - the first part. If lists are not equal length; the lists will be considered equal (i.e. return true) if the values up to the end of the shorter list are equal to values in the longer list, the extra values of the longer list are then ignored as we are only considering the 'fore'
		boolean result = true;
		
		int startIndex = 0;
		int endIndex = 0;
		if(b1.length < b2.length) endIndex = b1.length; 		
		else  endIndex = b2.length;
		
		for(int i = startIndex; i < endIndex; i++) {
			if(b1[i] != b2[i]) {
				result = false;
				break;
			}
		} 
		
		return result;		
	}
	
	public static boolean isEqualEnding(boolean[] b1, boolean[] b2) { //Aft - the end part. Opposite of isEqualFore, checks only the end
		boolean result = true;
		
		int endIndex   = 0;
		int startIndex = 0;
		
		if(b1.length < b2.length) {
			endIndex = b2.length;
			startIndex = endIndex - b1.length;

			int i = -1;
			for(int j = startIndex; j < endIndex; j++) {
				i++;
				if(b1[i] != b2[j]) {
					result = false;
					break;
				}
			}
		} else {
			endIndex = b1.length;
			startIndex = endIndex - b2.length;

			int i = -1;
			for(int j = startIndex; j < endIndex; j++) {
				i++;
				if(b1[j] != b2[i]) {
					result = false;
					break;
				}
			}
		}
		
		return result;		
	}
	
	
	
	public static boolean isEqual(Point p1, Point p2) {if(p1.x == p2.x && p1.y == p2.y) return true; else return false;}
	
	
	
	
	//--------
	//---------------------- Bezier -----------------------
	//--------
	/*
	public static VectorD quadraticBezier(VectorD p0, VectorD p1,VectorD p2, double t) {
		VectorD result = new VectorD();
		result.x() = Math.pow(1 - t, 2) * p0.x() + 
				   (1 - t) * 2 * t * p1.x() + 
				   t * t * p2.x();
		result.y() = Math.pow(1 - t, 2) * p0.y() + 
				   (1 - t) * 2 * t * p1.y() + 
				   t * t * p2.y();
		return result;
	}

	public static void quadraticBezier(VectorD p0, VectorD p1,VectorD p2, double t, VectorD pf) {
		pf.x() = Math.pow(1 - t, 2) * p0.x() + 
				   (1 - t) * 2 * t * p1.x() + 
				   t * t * p2.x();
		pf.y() = Math.pow(1 - t, 2) * p0.y() + 
				   (1 - t) * 2 * t * p1.y() + 
				   t * t * p2.y();
		
	}
	
	public static VectorD cubicBezier(VectorD p0, VectorD p1, VectorD p2, VectorD p3, double t) {
		VectorD result = new VectorD();
		result.x() = Math.pow(1 - t, 3) * p0.x() + 
				   Math.pow(1 - t, 2) * 3 * t * p1.x() + 
				   (1 - t) * 3 * t * t * p2.x() + 
				   t * t * t * p3.x();
		result.y() = Math.pow(1 - t, 3) * p0.y() + 
				   Math.pow(1 - t, 2) * 3 * t * p1.y() + 
				   (1 - t) * 3 * t * t * p2.y() + 
				   t * t * t * p3.y();
		return result;
	}
	
	public static void cubicBezier(VectorD p0, VectorD p1, VectorD p2, VectorD p3, double t, VectorD pf) {
		pf.x() = Math.pow(1 - t, 3) * p0.x() + 
				   Math.pow(1 - t, 2) * 3 * t * p1.x() + 
				   (1 - t) * 3 * t * t * p2.x() + 
				   t * t * t * p3.x();
		pf.y() = Math.pow(1 - t, 3) * p0.y() + 
				   Math.pow(1 - t, 2) * 3 * t * p1.y() + 
				   (1 - t) * 3 * t * t * p2.y() + 
				   t * t * t * p3.y();
	}
	*/
	
	//--------
	//--------------------- Number List Generation ----------------------
	//--------
	public static ArrayList<Integer> getRandomUniqueOrderFrom(int a, int b){
		ArrayList<Integer> result = new ArrayList<>();
		if(a == b) {
			result.add(a);
			return result;
		}
		if(a < 0) {
			Console.err("Maths -> getRandomUniqueOrderFrom() -> a is negative, it will be made positve");
			a *= -1;
		}
		if(b < 0) {
			Console.err("Maths -> getRandomUniqueOrderFrom() -> b is negative, it will be made positve");
			b *= -1;
		}
		if(a > b) {
			Console.err("Maths -> getRandomUniqueOrderFrom() -> a is greater than b -> parameter order will be swapped");
			int a1 = a;
			a = b;
			b = a1;
		}
		
		int tick = 0;
		int TIME_OUT = 10000;
		int dif = b - a;
		while(result.size() < dif) {
			tick++;
			if(tick > TIME_OUT) {
				Console.err("Maths -> getRandomUniqueOrderFrom() -> whileLoop timed out, could be an error, or your list length exceeds : " + TIME_OUT);
				return result;
			}
			int rnd = random(a, b);
			if(!result.contains(rnd)) result.add(rnd);			
		}	
		return result;
	}
	
	
	//--------
	//---------------------- Random Number Generation -----------------------
	//--------
	/**Chance on 1 out of 6 coin flips.
	 * @return boolean - true if one of the six coin flips is heads. (98.4% chance for true result)
	 * @author 5som3*/
	public static boolean c6() {return cn(6);}
	
	/**Chance on 1 out of 5 coin flips.
	 * @return boolean - true if one of the six coin flips is heads. (96.5% chance for true result)
	 * @author 5som3*/
	public static boolean c5() {return cn(5);}
	
	/**Chance on 1 out of 4 coin flips.
	 * @return boolean - true if one of the six coin flips is heads. (93.7% chance for true result)
	 * @author 5som3*/
	public static boolean c4() {return cn(4);}
	
	/**Chance on 1 out of 3 coin flips.
	 * @return boolean - true if one of the six coin flips is heads. (87.5% chance for true result)
	 * @author 5som3*/
	/***/
	public static boolean c3() {if(random.nextBoolean() || random.nextBoolean() || random.nextBoolean()) return true; else return false;}
	
	/**Chance on 1 out of 2 coin flips.
	 * @return boolean - true if one of the six coin flips is heads. (75.0% chance for true result)
	 * @author 5som3*/
	/***/
	public static boolean c2() {if(random.nextBoolean() || random.nextBoolean()) return true; else return false;}
	
	/**Chance of heads in a coin flip.
	 * @return boolean - true if flips heads. (50.0% chance for true result)
	 * @author 5som3*/
	public static boolean coinFlip() {return random.nextBoolean();}
	
	/**Chance on 1 out of (n) coin flips.
	 * @param n - the number of coins to flip.
	 * @return boolean - true if one of the (n) coin flips is heads.
	 * @author 5som3*/
	public static boolean cn(int n) {
		if(n <= 1) {
			Console.err("Maths -> cn() -> coin must be flipped at least once");
			n = 1;
		}
		for(int i = 0; i < n; i ++) {
			if(random.nextBoolean()) return true;
		} return false;
	}
	
	
	/**Chance of calling a number on a 3 side dice.
	 * @return boolean - true 1/3rd of the time. (33.3% chance for true result)
	 * @author 5som3 */
	public static boolean d3() {if(random.nextInt(3) == 0) return true; else return false;}
	
	/**Chance of calling a number on a 4 side dice.
	 * @return boolean - true 1/4th of the time. (25.0% chance for true result)
	 * @author 5som3 */
	public static boolean d4() {if(random.nextInt(4) == 0) return true; else return false;}
	
	/**Chance of calling a number on a 5 side dice.
	 * @return boolean - true 1/5th of the time. (20.0% chance for true result)
	 * @author 5som3 */
	public static boolean d5() {if(random.nextInt(5) == 0) return true; else return false;}

	/**Chance of calling a number on a 6 side dice.
	 * @return boolean - true 1/6th of the time. (16.7% chance for true result)
	 * @author 5som3 */
	public static boolean d6() {if(random.nextInt(6) == 0) return true; else return false;}

	/**Chance of calling a number on a 6 side dice.
	 * @return boolean - true 1/8th of the time. (12.5% chance for true result)
	 * @author 5som3 */
	public static boolean d8() {if(random.nextInt(8) == 0) return true; else return false;}

	/**Chance of calling a number on a 10 side dice.
	 * @return boolean - true 1/10th of the time. (10.0% chance for true result)
	 * @author 5som3 */
	public static boolean d10() {if(random.nextInt(10) == 0) return true; else return false;}
	
	/**Chance of calling a number on a 6 side dice.
	 * @return boolean - true 1/12th of the time. (8.3% chance for true result)
	 * @author 5som3 */
	public static boolean d12() {if(random.nextInt(12) == 0) return true; else return false;}
	
	/**Chance of calling a number on a 6 side dice.
	 * @return boolean - true 1/16th of the time. (6.2% chance for true result)
	 * @author 5som3 */
	public static boolean d16() {if(random.nextInt(16) == 0) return true; else return false;}
	
	/**Chance of calling a number on a 6 side dice.
	 * @return boolean - true 1/20th of the time. (5.0% chance for true result)
	 * @author 5som3 */
	public static boolean d20() {if(random.nextInt(20) == 0) return true; else return false;}
	
	/**Chance of calling a number on an (n) sided dice.
	 * @param n - the number of faces on the dice to roll.
	 * @return boolean - true 1/(n)th of the time.
	 * @author 5som3*/
	public static boolean dn(int n) {
		if(n <= 2) {
			Console.err("Maths -> dn() -> dice must have at least two sides, n will be set to 2");
			n = 2;
		}
		if(random.nextInt(n) == 0) return true; else return false;
	}
	
	/**The outcome of rolling a 3 sided dice.
	 * @return int - a number in the range 1 - 3 (inclusive). 
	 * @author 5som3*/
	public static int getD3() {return random.nextInt(1, 4);}
	
	/**The outcome of rolling a 4 sided dice.
	 * @return int - a number in the range 1 - 4 (inclusive). 
	 * @author 5som3*/
	public static int getD4() {return random.nextInt(1, 5);}
	
	/**The outcome of rolling a 5 sided dice.
	 * @return int - a number in the range 1 - 5 (inclusive). 
	 * @author 5som3*/
	public static int getD5() {return random.nextInt(1, 6);}
	
	/**The outcome of rolling a 6 sided dice.
	 * @return int - a number in the range 1 - 4 (inclusive). 
	 * @author 5som3*/
	public static int getD6() {return random.nextInt(1, 7);}
	
	/**The outcome of rolling a 12 sided dice.
	 * @return int - a number in the range 1 - 12 (inclusive). 
	 * @author 5som3*/
	public static int getD12() {return random.nextInt(1, 13);}
	
	/**The outcome of rolling a 16 sided dice.
	 * @return int - a number in the range 1 - 16 (inclusive). 
	 * @author 5som3*/
	public static int getD16() {return random.nextInt(1, 17);}
	
	/**The outcome of rolling a 20 sided dice.
	 * @return int - a number in the range 1 - 20 (inclusive). 
	 * @author 5som3*/
	public static int getD20() {return random.nextInt(1, 21);}	

	/**The outcome of rolling an (n) sided dice.
	 * @return int - a number in the range 1 - (n) (inclusive).
	 * @param n - the number of faces on the dice. 
	 * @author 5som3*/
	public static int getDn(int n) {
		if(n <= 2) {
			Console.err("Maths -> dn() -> dice must have at least two sides, n will be set to 2");
			n = 2;
		}
		return random.nextInt(1, n + 1);
	}
	
	/**Get a random integer.
	 * @return int - a random integer, could be negative or positive.
	 * @author 5som3*/
	public static int randomInt() {return random.nextInt();}
	
	/**Get a random positive integer.
	 * @param upperBound - the maximum value (exclusive) that can be returned.
	 * @return int - a random number (r) in the range : 0 <= upperBound.
	 * @author 5som3*/
	public static int random(int upperBound) {return random.nextInt(upperBound);}
	
	/**Get a random integer in the specified range.
	 * @param lowerBound - the minimum value (inclusive) that can be returned.
	 * @param upperBound - the maximum value (exclusive) that can be returned.
	 * @return int - a random number (r) in the range : lowerBound <= r <= upperBound
	 * @author 5som3*/
	public static int random(int lowerBound, int upperBound) {return random.nextInt(lowerBound, upperBound);}
	
	/**Get a random Long
	 * @return Long - a random Long, could be negative or positive.
	 * @author 5som3*/
	public static Long randomLong() {return random.nextLong();}
	
	/**Get a random positive Long.
	 * @param upperBound - the maximum value (exclusive) that can be returned.
	 * @return Long - a random number (r) in the range : 0 <= upperBound.
	 * @author 5som3*/
	public static Long random(Long upperBound) {return random.nextLong(upperBound);}
	
	/**Get a random Long in the specified range.
	 * @param lowerBound - the minimum value (inclusive) that can be returned.
	 * @param upperBound - the maximum value (exclusive) that can be returned.
	 * @return Long - a random number (r) in the range : lowerBound <= r <= upperBound
	 * @author 5som3*/
	public static Long random(Long lowerBound, Long upperBound) {return random.nextLong(lowerBound, upperBound);}
	
	/**Get a random double.
	 * @return double - a random double, could be negative or positive.
	 * @author 5som3*/
	public static double randomDouble() {return random.nextDouble();}
	
	/**Get a random double.
	 * @param int - the number of decimal places
	 * @return double - a random double, could be negative or positive.
	 * @author 5som3*/
	public static double randomDouble(int decimalPlaces) {return round(random.nextDouble(), decimalPlaces);}
	
	/**Get a random double
	 * @param upperBound - the maximum value (exclusive) that can be returned. 
	 * @return double - a random Long, could be negative or positive.
	 * @author 5som3*/
	public static double random(double upperBound) {return random.nextDouble(upperBound);}
	
	/**Get a random double in the specified range.
	 * @param lowerBound - the minimum value (inclusive) that can be returned.
	 * @param upperBound - the maximum value (exclusive) that can be returned.
	 * @return double - a random number (r) in the range : lowerBound <= r <= upperBound
	 * @author 5som3*/
	public static double random(double lowerBound, double upperBound) {return random.nextDouble(lowerBound, upperBound);}
	
	/**Get a random double in the specified range.
	 * @param lowerBound - the minimum value (inclusive) that can be returned.
	 * @param upperBound - the maximum value (exclusive) that can be returned.
	 * @param decimalPlaces - returned value is rounded to the number of decimal places
	 * @return double - a random number (r) in the range : lowerBound <= r <= upperBound
	 * @author 5som3*/
	public static double random(double lowerBound, double upperBound, int decimalPlaces) {
		return round(random.nextDouble(lowerBound, upperBound), decimalPlaces);
	}
	
	/**Get a random double
	 * @param upperBound - the maximum value (exclusive) that can be returned.
	 * @param decimalPlaces - returned value is rounded to the number of decimal places   
	 * @return double - a random Long, could be negative or positive.
	 * @author 5som3*/
	public static double random(double upperBound, int decimalPlaces) {
		return round(random.nextDouble(upperBound), decimalPlaces);
	}
	

	public static Float random() {return random.nextFloat();}
	public static Float random(Float upperBound) {return random.nextFloat(upperBound);}
	public static Float random(Float lowerBound, Float upperBound) {return random.nextFloat(lowerBound, upperBound);}
	public static Float random(Float lowerBound, Float upperBound, int decimalPlaces) {return round(random.nextFloat(lowerBound, upperBound), decimalPlaces);}
	public static Float random(Float upperBound, int decimalPlaces) {return round(random.nextFloat(upperBound), decimalPlaces);}
	public static Float randomFloat(int decimalPlaces) {return round(random.nextFloat(), decimalPlaces);}	
	
	//--------
	//---------------------- Generation -----------------------
	//--------
	public static double[] clone(double[] array) {
		double[] result = new double[array.length];
		for(int i = 0; i < array.length; i++) result[i] = array[i];
		return result;
	}
	
	
	//--------
	//---------------------- Variance -----------------------
	//--------
	public static double variance(int value, int variance) {
		if(variance <= 0) {
			Console.err("Maths -> variance -> variance must be greater than 0 : " + variance);
			return value;
		}
		int result = value;
		if(random.nextBoolean()) result += random.nextInt(variance);
		else result -= random.nextDouble(variance);
		return result;
	}
	
	
	public static double variance(double value, double variance) {
		if(variance <= 0) {
			Console.err("Maths -> variance -> variance must be greater than 0 : " + variance);
			return value;
		}
		double result = value;
		if(random.nextBoolean()) result += random.nextDouble(variance);
		else result -= random.nextDouble(variance);
		return result;
	}
	
	//--------
	//---------------------Clamp functions----------------------
	//--------
	/**This function will convert radian rotation to an equivalent radian rotation within the range of 0 <= result < TWO_PI 
	 * @param double -> rotation in radians -> domain  :  any double value
	 * @return double -> rotation in radians -> range  :  0 <= result < TWO_PI
	 * */
	public static double clampRadianRotation(double rotation) {
		if(rotation >= 0.0 && rotation < UtilsMath.TWO_PI) return rotation;
		if(rotation == -TWO_PI || rotation == TWO_PI) return 0.0;
		
		if(rotation < 0.0) {
			double divisions = (Math.ceil((-rotation) / UtilsMath.TWO_PI));
			return rotation + (divisions * UtilsMath.TWO_PI);
		} else {
			double divisions = Math.floor(rotation / UtilsMath.TWO_PI);
			return rotation - (divisions * UtilsMath.TWO_PI);
		}
	}
	
	/**This function will convert degree rotation to an equivalent degree rotation within the range of 0 <= result < TWO_PI 
	 * @param double -> rotation in degrees -> domain  :  any double value
	 * @return double -> rotation in degrees -> range  :  0 <= result < TWO_PI
	 * */
	public static double clampDegreeRotation(double rotation) {return convertRadiansToDegrees(clampRadianRotation(convertDegreesToRadians(rotation)));}
	
	/** Confine a value to a specific range.
	 * @param value - the value to be confined.
	 * @param min   - the minimum of the confine range (inclusive).
	 * @param max   - the maximum of the confine range (inclusive).
	 * @return value if within the range; min if value is less than min; max if greater than max;
	 * @author SomeKid*/
	public static Double clamp(double value, double min, double max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}
	
	/** Confine a value to a specific range.
	 * @param value - the value to be confined.
	 * @param min   - the minimum of the confine range (inclusive).
	 * @param max   - the maximum of the confine range (inclusive).
	 * @return value if within the range; min if value is less than min; max if greater than max;
	 * @author SomeKid*/
	public static int clamp(int value, int min, int max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}
	
	/** Confine a value to a specific range.
	 * @param value - the value to be confined.
	 * @param min   - the minimum of the confine range (inclusive).
	 * @param max   - the maximum of the confine range (inclusive).
	 * @return value if within the range; min if value is less than min; max if greater than max;
	 * @author SomeKid*/
	public static float clamp(float value, float min, float max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}

	/** Confine a value to a specific range.
	 * @param value - the value to be confined.
	 * @param min   - the minimum of the confine range (inclusive).
	 * @param max   - the maximum of the confine range (inclusive).
	 * @return value if within the range; min if value is less than min; max if greater than max;
	 * @author SomeKid*/
	public static Integer clamp(Integer value, Integer min, Integer max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}
	
	/** Stop a value from exceeding the specified maximum limit.
	 * @param value - the value to be confined.
	 * @param max   - the maximum limit (inclusive).
	 * @return value if less than or equal to max; max if greater than max;
	 * @author SomeKid*/
	public static Double clampMax(double value, double max) {
		if(value > max) return max;
		return value;
	}

	/** Stop a value from exceeding the specified maximum limit.
	 * @param value - the value to be confined.
	 * @param max   - the maximum limit (inclusive).
	 * @return value if less than or equal to max; max if greater than max;
	 * @author SomeKid*/
	public static int clampMax(int value, int max) {
		if(value > max) return max;
		return value;
	}
	
	/** Stop a value from exceeding the specified maximum limit.
	 * @param value - the value to be confined.
	 * @param max   - the maximum limit (inclusive).
	 * @return value if less than or equal to max; max if greater than max;
	 * @author SomeKid*/
	public static float clampMax(float value, float max) {
		if(value > max) return max;
		return value;
	}
	
	/** Stop a value from exceeding the specified maximum limit.
	 * @param value - the value to be confined.
	 * @param max   - the maximum limit (inclusive).
	 * @return value if less than or equal to max; max if greater than max;
	 * @author SomeKid*/
	public static Integer clampMax(Integer value, Integer max) {
		if(value > max) return max;
		return value;
	}

	/** Stop a value from falling below the specified minimum limit.
	 * @param value - the value to be confined.
	 * @param min   - the maximum limit (inclusive).
	 * @return value if greater than or equal to min; min if less than min;
	 * @author SomeKid*/
	public static Double clampMin(double value, double min) {
		if(value < min) return min;
		return value;
	}
	
	/** Stop a value from falling below the specified minimum limit.
	 * @param value - the value to be confined.
	 * @param min   - the maximum limit (inclusive). 
	 * @return value if greater than or equal to min; min if less than min;
	 * @author SomeKid*/
	public static int clampMin(int value, int min) {
		if(value < min) return min;
		return value;
	}
	
	/** Stop a value from falling below the specified minimum limit.
	 * @param value - the value to be confined.
	 * @param min   - the maximum limit (inclusive).
	 * @return value if greater than or equal to min; min if less than min;
	 * @author SomeKid*/
	public static float clampMin(float value, float min) {
		if(value < min) return min;
		return value;
	}
	
	/** Stop a value from falling below the specified minimum limit.
	 * @param value - the value to be confined.
	 * @param min   - the maximum limit (inclusive).
	 * @return value if greater than or equal to min; min if less than min;
	 * @author SomeKid*/
	public static Integer clampMin(Integer value, Integer min) {
		if(value < min) return min;
		return value;
	}
	

	
	/**Does the list of vectors contain a vector equal to the specified components.
	 * @param vectors - the list of vectors to check.
	 * @param components - the vector components to compare to.
	 * @return boolean - true if the list contains a vector with the specified vector components else false.
	 * @author 5som3*/
	public static boolean containsVectorI(List<VectorI> vectors, int...components) {if(findVectorIIndex(vectors, components) == -1) return false; else return true;}
	
	/**Does the list of vectors contain a vector equal to the specified vector.
	 * @param vectors - the list of vectors to check.
	 * @param vec - the vector to compare to.
	 * @return boolean - true if the list contains a vector equal to the specified vector else false.
	 * @author 5som3*/
	public static boolean containsVectorI(List<VectorI> vectors, VectorI vec) {if(findVectorIIndex(vectors, vec) == -1) return false; else return true;}
	
	/**Find the index of the vector equal to the specified coordinates.
	 * @param vectors - the list of vectors to check.
	 * @param components - the vector components to look for. 
	 * @return int - the index of the first vector with components equal to the specified components.
	 * @apiNote Will return -1 if no vector in the list is equal to the specified components. 
	 * @author 5som3*/
	public static int findVectorIIndex(List<VectorI> vectors, int... components) {
		for(int i = 0; i < vectors.size(); i++) {
			if(vectors.get(i).isEqual(components)) return i;
		}
		return -1;
	}
	
	/**Find the index of the vector equal to the specified vec.
	 * @param vectors - the list of vectors to check.
	 * @param vec - the vector components to look for. 
	 * @return int - the index of the first vector with components equal to the specified components.
	 * @apiNote Will return -1 if no vector in the list is equal to the specified components. 
	 * @author 5som3*/
	public static int findVectorIIndex(List<VectorI> vectors, VectorI vec) {
		for(int i = 0; i < vectors.size(); i++) {
			if(vectors.get(i).isEqual(vec)) return i;
		}
		return -1;
	}
	
	/**Does the list of vectors contain a vector equal to the specified components.
	 * @param vectors - the list of vectors to check.
	 * @param components - the vector components to compare to.
	 * @return boolean - true if the list contains a vector with the specified vector components else false.
	 * @author 5som3*/
	public static boolean containsVectorD(List<VectorD> vectors, double...components) {if(findVectorDIndex(vectors, components) == -1) return false; else return true;}
	
	/**Does the list of vectors contain a vector equal to the specified vector.
	 * @param vectors - the list of vectors to check.
	 * @param vec - the vector to compare to.
	 * @return boolean - true if the list contains a vector equal to the specified vector else false.
	 * @author 5som3*/
	public static boolean containsVectorD(List<VectorD> vectors, VectorD vec) {if(findVectorDIndex(vectors, vec) == -1) return false; else return true;}
	
	/**Find the index of the vector equal to the specified coordinates.
	 * @param vectors - the list of vectors to check.
	 * @param components - the vector components to look for. 
	 * @return int - the index of the first vector with components equal to the specified components.
	 * @apiNote Will return -1 if no vector in the list is equal to the specified components. 
	 * @author 5som3*/
	public static int findVectorDIndex(List<VectorD> vectors, double... components) {
		for(int i = 0; i < vectors.size(); i++) {
			if(vectors.get(i).isEqual(components)) return i;
		}
		return -1;
	}
	
	/**Find the index of the vector equal to the specified vec.
	 * @param vectors - the list of vectors to check.
	 * @param vec - the vector components to look for. 
	 * @return int - the index of the first vector with components equal to the specified components.
	 * @apiNote Will return -1 if no vector in the list is equal to the specified components. 
	 * @author 5som3*/
	public static int findVectorDIndex(List<VectorD> vectors, VectorD vec) {
		for(int i = 0; i < vectors.size(); i++) {
			if(vectors.get(i).isEqual(vec)) return i;
		}
		return -1;
	}
	
	
	
	/**Find the minimum value in the specified array.
	 * @param values - the array of values to check.
	 * @return Integer - the minimum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static int findMin(List<Integer> values)  {return values.get(findMinIndex(values));}
		
	/**Find the minimum value in the specified array.
	 * @param values - the array of values to check.
	 * @return int - the minimum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static int findMin(int[] values) 				 {return values[findMinIndex(values)];}
	
	/**Find the minimum value in the specified array.
	 * @param values - the array of values to check.
	 * @return int - the minimum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static int findMin(int[][] values) 				 {VectorI coords = findMinIndex(values); return values[coords.x()][coords.y()];}
	
	/**Find the minimum value in the specified array.
	 * @param values - the array of values to check.
	 * @return double - the minimum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static double findMin(double[] values) 		  	 {return values[findMinIndex(values)];}
	
	/**Find the minimum value in the specified array.
	 * @param values - the array of values to check.
	 * @return double - the minimum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static double findMin(double[][] values) 		 {VectorI coords = findMinIndex(values); return values[coords.x()][coords.y()];}
	
	/**Find the maximum value in the specified array.
	 * @param values - the array of values to check.
	 * @return int - the maximum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static int findMax(Integer[] values)              {return values[findMaxIndex(values)];}
	
	/**Find the maximum value in the specified array.
	 * @param values - the array of values to check.
	 * @return int - the maximum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static int findMax(int[] values) 				 {return values[findMaxIndex(values)];}
	
	/**Find the maximum value in the specified array.
	 * @param values - the array of values to check.
	 * @return int - the maximum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static int findMax(int[][] values) 				 {VectorI coords = findMaxIndex(values); return values[coords.x()][coords.y()];}
	
	/**Find the maximum value in the specified array.
	 * @param values - the array of values to check.
	 * @return int - the maximum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static int findMax(List<Integer> values)     {return values.get(findMaxIndex(values));}
	
	/**Find the maximum value in the specified array.
	 * @param values - the array of values to check.
	 * @return double - the maximum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static double findMax(double[] values) 			 {return values[findMaxIndex(values)];}
	
	/**Find the maximum value in the specified array.
	 * @param values - the array of values to check.
	 * @return double - the maximum value in the array.
	 * @implNote will cause array index out of bounds exception if values is null or empty.  
	 * @author 5som3*/
	public static double findMax(double[][] values) 	     {VectorI coords = findMaxIndex(values); return values[coords.x()][coords.y()];}
	
	/**Find the minimum X position in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the minimum X position from the list.
	 * @author 5som3 */
	public static int findMinX(List<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMinXIndex(rectangles)).x;}
	
	/**Find the maximum X position in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the maximum X position from the list.
	 * @author 5som3 */
	public static int findMaxX(List<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMaxXIndex(rectangles)).x;}
	
	/**Find the minimum Y position in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the minimum Y position from the list.
	 * @author 5som3 */
	public static int findMinY(List<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMinYIndex(rectangles)).y;}
	
	/**Find the maximum Y position in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the maximum Y position from the list.
	 * @author 5som3 */
	public static int findMaxY(List<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMaxYIndex(rectangles)).y;}
	
	/**Find the minimum width in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the minimum width among the specified rectangles.
	 * @author 5som3*/
	public static int findMinWidth(List<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMinWidthIndex(rectangles)).width;}
	
	/**Find the minimum height in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the minimum height among the specified rectangles.
	 * @author 5som3*/
	public static int findMinHeight(List<Rectangle> rectangles){return rectangles.get(UtilsMath.findMinHeightIndex(rectangles)).height;}
	
	/**Find the maximum width in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the maximum width among the specified rectangles.
	 * @author 5som3*/
	public static int findMaxWidth(List<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMaxWidthIndex(rectangles)).width;}
	
	/**Find the maximum height in the specified list.
	 * @param rectangles - the list of rectangles to check.
	 * @return int - the maximum height among the specified rectangles.
	 * @author 5som3*/
	public static int findMaxHeight(List<Rectangle> rectangles){return rectangles.get(UtilsMath.findMaxHeightIndex(rectangles)).height;}
	
	/**Find the index of the minimum value in the array.
	 * @param values - the array of values to check.
	 * @return int - the index of the minimum value in the array.
	 * @apiNote returns -1 with error if array is null or empty
	 * @author 5som3*/
	public static int findMinIndex(int[] values) {
		if(values == null || values.length == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return -1;
		}
		
		int result = 0;
		for(int i = 0; i < values.length; i++) {
			result = (values[i] < values[result]) ? i : result;
		}		
		return result;
	}

	/**Find the coordinates of the minimum value in the array.
	 * @param values - the array of values to check.
	 * @return VectorI - the coordinate of the minimum value in the array.
	 * @apiNote returns vector (-1, -1) with error if array is null or empty
	 * @author 5som3*/
	public static VectorI findMinIndex(int[][] values) {
		VectorI result = new VectorI(-1);
		
		if(values == null || values.length == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return result;
		}
		
		VectorI currentMin = new VectorI();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] < values[currentMin.x()][currentMin.y()]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		return result;
	}
	
	/**Find the index of the minimum value in the array.
	 * @param values - the array of values to check.
	 * @return int - the index of the minimum value in the array.
	 * @apiNote returns -1 with error if array is null or empty
	 * @author 5som3*/
	public static int findMinIndex(double[] values) {
		if(values == null || values.length == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return -1;
		}
		
		int result = 0;
		for(int i = 0; i < values.length; i++) {
			result = (values[i] < values[result]) ? i : result;
		}		
		return result;
	}
	
	/**Find the coordinates of the minimum value in the array.
	 * @param values - the array of values to check.
	 * @return VectorI - the coordinate of the minimum value in the array.
	 * @apiNote returns vector (-1, -1) with error if array is null or empty
	 * @author 5som3*/
	public static VectorI findMinIndex(double[][] values) {
		VectorI result = new VectorI();
		
		VectorI currentMin = new VectorI();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] < values[currentMin.x()][currentMin.y()]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		
		return result;
	}
	
	/**Find the index of the minimum value in the array.
	 * @param values - the array of values to check.
	 * @return int - the index of the minimum value in the array.
	 * @apiNote returns -1 with error if array is null or empty
	 * @author 5som3*/
	public static int findMinIndex(List<Integer> values) {
		if(values == null || values.size() == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return -1;
		}
		
		int result = 0;
		for(int i = 0; i < values.size(); i++) {
			result = (values.get(i) < values.get(result)) ? i : result;
		}		
		return result;
	}
	
	
	
	/**Find the index of the maximum value in the array.
	 * @param values - the array of values to check.
	 * @return int - the index of the maximum value in the array.
	 * @apiNote returns -1 with error if array is null or empty
	 * @author 5som3*/
	public static int findMaxIndex(Integer[] values) {
		if(values == null || values.length == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return -1;
		}
		
		int result = 0;
		for(int i = 0; i < values.length; i++) {
			result = (values[i] > values[result]) ? i : result;
		}		
		return result;
	}
	
	/**Find the index of the maximum value in the array.
	 * @param values - the array of values to check.
	 * @return int - the index of the maximum value in the array.
	 * @apiNote returns -1 with error if array is null or empty
	 * @author 5som3 */
	public static int findMaxIndex(int[] values) {
		if(values == null || values.length == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return -1;
		}
		
		int result = 0;
		for(int i = 0; i < values.length; i++) {
			result = (values[i] > values[result]) ? i : result;
		}		
		return result;
	}
	
	/**Find the coordinates of the maximum value in the array.
	 * @param values - the array of values to check.
	 * @return VectorI - the coordinate of the maximum value in the array.
	 * @apiNote returns vector (-1, -1) with error if array is null or empty
	 * @author 5som3*/
	public static VectorI findMaxIndex(int[][] values) {
		VectorI result = new VectorI();
		
		VectorI currentMin = new VectorI();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] > values[currentMin.x()][currentMin.y()]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		
		return result;
	}
		
	/**Find the index of the maximum value in the array.
	 * @param values - the array of values to check.
	 * @return int - the index of the maximum value in the array.
	 * @apiNote returns -1 with error if array is null or empty
	 * @author 5som3 */
	public static int findMaxIndex(double[] values) {
		if(values == null || values.length == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return -1;
		}
		
		int result = 0;
		for(int i = 0; i < values.length; i++) {
			result = (values[i] > values[result]) ? i : result;
		}		
		return result;
	}
	
	/**Find the coordinates of the maximum value in the array.
	 * @param values - the array of values to check.
	 * @return VectorI - the coordinate of the maximum value in the array.
	 * @apiNote returns vector (-1, -1) with error if array is null or empty
	 * @author 5som3*/
	public static VectorI findMaxIndex(double[][] values) {
		VectorI result = new VectorI();
		
		VectorI currentMin = new VectorI();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] > values[currentMin.x()][currentMin.y()]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		
		return result;
	}
	
	/**Find the index of the maximum value in the array.
	 * @param values - the array of values to check.
	 * @return int - the index of the maximum value in the array.
	 * @apiNote returns -1 with error if array is null or empty
	 * @author 5som3 */
	public static int findMaxIndex(List<Integer> values) {
		if(values == null || values.size() == 0) {
			Console.err("UtilsMath -> findMinIndex() -> values array is null or empty");
			return -1;
		}
		
		int result = 0;
		for(int i = 0; i < values.size(); i++) {
			result = (values.get(i) > values.get(result)) ? i : result;
		}		
		return result;
	}
	
	/**Is the list null or contain null.
	 * @param rectangles - an arrayList of the rectangles to check.
	 * @return boolean - false if the array is null or contains null, else true.
	 * @author 5som3*/
	public static boolean validRectangles(List<Rectangle> rectangles) {
		if(rectangles == null) {
			Console.err("Maths -> findMinPosIndex(ArrayList<Rectangle>) -> passed null array as parameter ");
			return false;
		}
		if(rectangles.contains(null)) {
			Console.err("Maths-> findMinPosIndex(ArrayList<Rectangle>) -> rectangles contains a null variable ");
			return false;
		}
		return true;
	}
	
	/**Find the index of the rectangle with the minimum X value.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the minimum X component.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMinXIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).x < minRect.x) {
				minRect = rectangles.get(i);
				result = i;
			}
		}
		return result;
	}
	
	/**Find the index of the rectangle with the maximum X value.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the maximum X component.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMaxXIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).x > minRect.x) {
				minRect = rectangles.get(i);
				result = i;
			}
		}
		return result;
	}
	
	/**Find the index of the rectangle with the minimum Y value.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the minimum Y component.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMinYIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).y < minRect.y) {
				minRect = rectangles.get(i);
				result = i;
			}
		}
		return result;
	}
	
	/**Find the index of the rectangle with the maximum Y value.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the maximum Y component.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMaxYIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).y > minRect.y) {
				minRect = rectangles.get(i);
				result = i;
			}
		}
		return result;
	}
	
	/**Find the index of the rectangle with the minimum width.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the minimum width.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMinWidthIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).width < minRect.width) {
				minRect = rectangles.get(i);
				result = i;
			}
		}
		return result;
	}
	
	/**Find the index of the rectangle with the maximum width.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the maximum width.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMaxWidthIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).width > minRect.width) {
				minRect = rectangles.get(i);
				result = i;
			}
		}
		return result;
	}
	
	/**Find the index of the rectangle with the maximum height.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the maximum height.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMaxHeightIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).height < minRect.height) {
				minRect = rectangles.get(i);
				result = i;
			}
		}
		return result;
	}
	
	/**Find the index of the rectangle with the minimum height.
	 * @param rectangles - an array of rectangles to check.
	 * @return int - the index of the rectangle with the minimum height.
	 * @apiNote Will return -1 if rectangles is null or contains null.
	 * @author 5som3*/
	public static int findMinHeightIndex(List<Rectangle> rectangles) {
		if(!validRectangles(rectangles)) return -1;
		int result = 0; 
		Rectangle minRect = rectangles.get(0);
		for(int i = 0; i < rectangles.size(); i++) {
			if(rectangles.get(i).height < minRect.getHeight()) {
				minRect = rectangles.get(i);
				result = i;
			}
		} 
		return result;
	}
	
	/**Get the minimum x/y components from the specified list of rectangles.
	 * @param rectangles - an array of rectangles to check.
	 * @return VectorD - a vector containing the minimum x and y components from the array. 
	 * @apiNote x/y components may be from different rectangles. 
	 * @apiNote Example : will return (rectangles[1].x, rectangles[2].y) if rectangles[1] is minimum X, and rectangles[2] is minimum Y.
	 * @author 5som3*/
	public static VectorI findMinBounds(List<Rectangle> rectangles) {
		VectorI result = new VectorI(findMinX(rectangles), findMinY(rectangles));		
		return result;
	}
	
	/**Get the maximum x/y components from the specified list of rectangles.
	 * @param rectangles - an array of rectangles to check.
	 * @return VectorD - a vector containing the maximum x and y components from the array. 
	 * @apiNote NOTE: NOT the maximum width/height of the rectangles, calculates relative point based on x + width, y + height.
	 * @apiNote x/y components may be from different rectangles. 
	 * @apiNote Example : will return (rectangles[1].x + width, rectangles[2].y + height) if rectangles[1].x + width is maximum, and rectangles[2].y + height is maximum.
	 * @author 5som3*/
	public static VectorI findMaxBounds(List<Rectangle> rectangles) {
		VectorI result = new VectorI();
		int xMax = 0;
		int yMax = 0;
		
		for(int i = 0; i < rectangles.size(); i++) {
			Rectangle rect = rectangles.get(i);
			int cx = rect.x + rect.width;
			int cy = rect.y + rect.height;
			
			if(cx > xMax) xMax = cx;
			if(cy > yMax) yMax = cy;
		
		}
		
		result.set(xMax, yMax);
		return result;
	}
	

	
	
}
