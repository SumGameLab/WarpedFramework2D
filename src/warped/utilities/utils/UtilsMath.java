/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Point;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import warped.application.tile.RiverSegment;
import warped.utilities.enums.generalised.RotationType;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.math.vectors.Vec3d;
import warped.utilities.math.vectors.Vec3i;

public class UtilsMath {
	
	//
	//---------------------Definitions--------------------------
	//

	public static final Composite clearComposite = AlphaComposite.getInstance(AlphaComposite.CLEAR);
	public static final Composite drawComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
	
	public static Random random = new Random();
	/**Sixteenth Rotation*/
	public static final double PI_ON_EIGHTH	   = Math.PI * 0.125;
	/**Eighth Rotation*/
	public static final double PI_ON_FOUR 	   = Math.PI * 0.25;
	/**Quarter Rotation*/
	public static final double PI_ON_TWO 	   = Math.PI * 0.5;		
	/**Half Rotation*/
	public static final double PI 			   = Math.PI * 1.0;		
	/**Three Quarter Rotation*/
	public static final double THREE_PI_ON_TWO = Math.PI * 1.5;
	/**Full Rotation*/
	public static final double TWO_PI  		   = Math.PI * 2.0; 
	
	
	public static final double convertCelsiusToJoules(double celsius) {return celsius * 1899.1005;}
	public static final double convertYardToMeters(double yards) {return yards * 0.9144;}
	public static final double convertFeetToMeters(double feet) {return feet * 0.3048;}
	public static final double convertInchesToMeters(double inches) {return inches * 0.0254;}
	
	public static final double convertOuncesToKilo(double ounces) {return ounces * 0.0283495;}
	public static final double convertPoundsToKilo(double pounds) {return pounds * 0.453592;}
	public static final double convertTonToKilo(double tons) {return tons * 1016.05;}
	
	
	
	
	//
	//-------------------Basic Maths-------------------------------
	//	
	public static double difference(double a, double b) {
		double result = a - b;
		if(result < 0) result *= -1.0;
		return result;
	}
	public static double pythag(double opposite, double hypotenuse) {return Math.asin(opposite / hypotenuse);}
	public static int square(int value) {return (value * value);}
	public static int cubed(int value) {return(value * value * value);}
	public static int floor(double value) { return (int)Math.floor(value);}
	public static double square(double value) {return (value * value);}
	public static double cubed(double value) {return(value * value * value);}
	public static double round(double value, int decimalPlaces) {
		if (decimalPlaces < 0) {
			Console.err("Maths -> nextDouble -> invalid number of decimal places : " + decimalPlaces);
			decimalPlaces = 1;			
		}
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(decimalPlaces + 1, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public static Float round(float value, int decimalPlaces) {
		if (decimalPlaces < 0) {
			Console.err("Maths -> nextDouble -> invalid number of decimal places : " + decimalPlaces);
			decimalPlaces = 1;			
		}
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
		return bd.floatValue();
	}
	
	public static double dividePrecise(int a, int b) {return ((double)a)/ ((double)b);}
	
	//--------
	//---------------- inversion --------
	//--------
	public static boolean[] invert(boolean[] array) { 
		boolean[] result = new boolean[array.length];
		for(int i = 0; i < array.length; i++) {
			if(array[i]) result[i] = false;
			else result[i] = true;
		}
		return result;
	}
	
	public static double[] invert(double[] array) {
		double[] result = new double[array.length];
		for(int i = 0; i < array.length; i++) result[i] = 1 / array[i];
		return result;		
	}
	
	
	
	//--------
	//---------------- Angular Math -------- 
	//--------	
	
	public static Vec2d getUnitVector(double rotation) {return new Vec2d(Math.cos(rotation), Math.sin(rotation));}
	
	public static Vec2d getUnitVector(double rotation, double scale) {return new Vec2d(Math.cos(rotation) * scale, Math.sin(rotation) * scale);}
	 
	/**Angle of unit Vector pointing from Vec1 to Vec2
	 * Angle is defined as clockwise rotation from the positive x axis (java standard positive x axis is horizontal right translation)
	 * Angle is radians
	 * */
	public static double getDirectionAngle(Vec2d vec1, Vec2d vec2) {
		Vec2d dst = positiveDifference(vec2, vec1);
		if(vec2.y > vec1.y && vec2.x > vec1.x) {
			return(Math.atan(dst.y/dst.x));			
		} 
		if(vec2.y > vec1.y && vec2.x < vec1.x) {
			return(UtilsMath.PI_ON_TWO + Math.atan(dst.x/dst.y));
		} 
		if(vec2.y < vec1.y && vec2.x < vec1.x) {
			return (UtilsMath.PI + Math.atan(dst.y/dst.x));
		} 
		if(vec2.y < vec1.y && vec2.x > vec1.x) {
			return(UtilsMath.THREE_PI_ON_TWO + Math.atan(dst.x/dst.y));
		}
		
		Console.err("Maths -> getDirectionAngle() -> math error, this case should not be reached");
		return -1.0;
	}
	public static double getDirectionAngle(Vec3d vec1, Vec3d vec2) {
		Vec3d dst = positiveDifference(vec2, vec1);
		if(vec2.y > vec1.y && vec2.x > vec1.x) {
			return(Math.atan(dst.y/dst.x));			
		} 
		if(vec2.y > vec1.y && vec2.x < vec1.x) {
			return(UtilsMath.PI_ON_TWO + Math.atan(dst.x/dst.y));
		} 
		if(vec2.y < vec1.y && vec2.x < vec1.x) {
			return (UtilsMath.PI + Math.atan(dst.y/dst.x));
		} 
		if(vec2.y < vec1.y && vec2.x > vec1.x) {
			return(UtilsMath.THREE_PI_ON_TWO + Math.atan(dst.x/dst.y));
		}
		Console.err("Maths -> getDirectionAngle() -> math error, this case should not be reached");
		return -1.0;
	}
	public static double getDirectionAngle(Vec3d vec) {return getDirectionAngle(new Vec3d(), vec);}
	public static double getDirectionAngle(Vec2d vec) {return getDirectionAngle(new Vec2d(), vec);}
	
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
	
	public static double angleBetweenVectors(Vec2i vec1, Vec2i vec2){return angleBetweenVectors(vec1.x, vec1.y, vec2.x, vec2.y);}
	public static double angleBetweenVectors(Vec3i vec1, Vec3i vec2) {return angleBetweenVectors(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);}
	
	public static double angleBetweenVectors(Vec2d vec1, Vec2d vec2){return angleBetweenVectors(vec1.x, vec1.y, vec2.x, vec2.y);}
	
	public static double angleBetweenVectors(double vec1X, double vec1Y, double vec2X, double vec2Y) {	
		return Math.acos( 		  (dotProduct(vec1X, vec1Y, vec2X, vec2Y)) 
						/ (magnitude(vec1X, vec1Y) * magnitude(vec2X, vec2Y)) );
	}
	public static double angleBetweenVectorsSigned(double vec1X, double vec1Y, double vec2X, double vec2Y) {	
		return Math.atan2( determinant(vec1X, vec1Y, vec2X, vec2Y), dotProduct(vec1X, vec1Y, vec2X, vec2Y));
		
		
	}
	public static double angleBetweenVectors(Vec3d vec1, Vec3d vec2) {return angleBetweenVectors(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);}
	public static double angleBetweenVectors(double vec1X, double vec1Y, double vec1Z, double vec2X, double vec2Y, double vec2Z) {	
		return Math.acos(		 (dotProduct(vec1X, vec1Y, vec1Z, vec2X, vec2Y, vec2Z)) 
						/ (magnitude(vec1X, vec1Y, vec1Z) * magnitude(vec2X, vec2Y, vec2Z)) );
	}
	
	//--------
	//---------------- Dot Product --------
	//--------
	public static int dotProduct(Point p1, Point p2) 	 {return dotProduct(p1.x, p1.y, p2.x, p2.y);}
	public static int dotProduct(Vec2i vec1, Vec2i vec2) {return dotProduct(vec1.x, vec1.y, vec2.x, vec2.y);}
	public static int dotProduct(Vec3i vec1, Vec3i vec2) {return dotProduct(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.x);}
	public static int dotProduct(int x1, int y1, int x2, int y2) {return ( (x1 * x2) 
																		 + (y1 * y2) );}
	public static int dotProduct(int x1, int y1, int z1, int x2, int y2, int z2) {return ( (x1 * x2) 
																						 + (y1 * y2)
																						 + (z1 * z2));}
	
	public static double dotProduct(Vec2d vec1, Vec2d vec2) {return dotProduct(vec1.x, vec1.y, vec2.x, vec2.y);}
	public static double dotProduct(Vec3d vec1, Vec3d vec2) {return dotProduct(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);}
	public static double dotProduct(double x1, double y1, double x2, double y2) {return ( (x1 * x2) 
																						+ (y1 * y2) );}
	public static double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2) {return ( (x1 * x2)
																											  + (y1 * y2) 
																											  + (z1 * z2) );}
	
	//--------
	//---------------- Determinant --------
	//--------
	public static double determinant(Point p1, Point p2) {return determinant(p1.x, p1.y, p2.x, p2.y);}
	public static double determinant(double x1, double y1, double x2, double y2) {return ( (x1 * y2)
																						 - (y1 * x2));}
	
	//
	//------------------ Math to String ----------------------------
	//
	
	public static void printHex(int value) {
		System.out.printf("^x\n", value);}
	public static void printBin(int value) {Console.ln(Integer.toBinaryString(value));}
	

	//--------
	//---------------- Magnitude --------
	//--------
	
	public static double magnitude(int x, int y) {return Math.sqrt( square(x) + square(y) );}
	public static double magnitude(int x, int y, int z) {return UtilsMath.square( square(x) + square(y) + square(z) );}
	public static double magnitude(double x, double y) {return Math.sqrt( square(x) + square(y) );}
	public static double magnitude(double x, double y, double z) {return UtilsMath.square( square(x) + square(y) + square(z) );}
	public static double magnitude(Vec2i vec) {return Math.sqrt( square(vec.x) + square(vec.y) );}
	public static double magnitude(Vec2d vec) {return Math.sqrt( square(vec.x) + square(vec.y) );}
	public static double magnitude(Vec3i vec) {return Math.sqrt( square(vec.x) + square(vec.y) + square(vec.z)); }
	public static double magnitude(Vec3d vec) {return Math.sqrt( square(vec.x) + square(vec.y) + square(vec.z)); }
	
	//--------
	//---------------- Positive --------
	//--------
	
	public static double positive(double value) {if(value < 0.0) return value *= -1.0; else return value;}
	public static int positive(int value) {if(value < 0) return value *= -1; else return value;}
	public static float positive(float value) {if(value < 0f) return value *= -1f; else return value;}
	public static long positive(long value) {if(value < 0) return value *= -1; else return value;}
	public static void positive(Vec2i vec) {
		if(vec.x < 0) vec.x *= -1; 
		if(vec.y < 0) vec.y *= -1;
	}
	public static void positive(Vec3i vec) {
		if(vec.x < 0) vec.x *= -1; 
		if(vec.y < 0) vec.y *= -1;
		if(vec.z < 0) vec.z *= -1;
	}
	public static void positive(Vec2d vec) {
		if(vec.x < 0.0) vec.x *= -1.0;
		if(vec.y < 0.0) vec.y *= -1.0;
	}
	public static void positive(Vec3d vec) {
		if(vec.x < 0.0) vec.x *= -1.0; 
		if(vec.y < 0.0) vec.y *= -1.0;
		if(vec.z < 0.0) vec.z *= -1.0;
	}

	
	public static Vec2d positiveDifference(Vec2d vec1, Vec2d vec2) {
		Vec2d result = new Vec2d(vec1);
		result.minus(vec2);
		positive(result);
		return result;
	}
	public static Vec3d positiveDifference(Vec3d vec1, Vec3d vec2) {
		Vec3d result = new Vec3d(vec1);
		result.minus(vec2);
		positive(result);	
		return result;
	}

	
	
	//--------
	//----------------------Comparators -----------------------
	//--------
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
	public static boolean isEqual(Vec2i vec1, Vec2i vec2) {if(vec1.x == vec2.x && vec1.y == vec2.y) return true; else return false;}
	public static boolean isEqual(Vec3i vec1, Vec3i vec2) {if(vec1.x == vec2.x && vec1.y == vec2.y && vec1.z == vec2.z) return true; else return false;}
	public static boolean isEqual(Vec2d vec1, Vec2d vec2) {if(vec1.x == vec2.x && vec1.y == vec2.y) return true; else return false;}
	public static boolean isEqual(Vec3d vec1, Vec3d vec2) {if(vec1.x == vec2.x && vec1.y == vec2.y && vec1.z == vec2.z) return true; else return false;}
	
	public static boolean isDifGreaterThan(Point p1, Point p2, double x) {
		int xDif = p1.x - p2.x;
		int yDif = p1.y - p2.y;
		if(xDif > x || xDif < -x || yDif > x || yDif < -x) return true; else return false;
	}
	
	
	//--------
	//---------------------- Bezier -----------------------
	//--------
	
	public static Vec2d quadraticBezier(Vec2d p0, Vec2d p1,Vec2d p2, double t) {
		Vec2d result = new Vec2d();
		result.x = Math.pow(1 - t, 2) * p0.x + 
				   (1 - t) * 2 * t * p1.x + 
				   t * t * p2.x;
		result.y = Math.pow(1 - t, 2) * p0.y + 
				   (1 - t) * 2 * t * p1.y + 
				   t * t * p2.y;
		return result;
	}

	public static void quadraticBezier(Vec2d p0, Vec2d p1,Vec2d p2, double t, Vec2d pf) {
		pf.x = Math.pow(1 - t, 2) * p0.x + 
				   (1 - t) * 2 * t * p1.x + 
				   t * t * p2.x;
		pf.y = Math.pow(1 - t, 2) * p0.y + 
				   (1 - t) * 2 * t * p1.y + 
				   t * t * p2.y;
		
	}
	
	public static Vec2d cubicBezier(Vec2d p0, Vec2d p1, Vec2d p2, Vec2d p3, double t) {
		Vec2d result = new Vec2d();
		result.x = Math.pow(1 - t, 3) * p0.x + 
				   Math.pow(1 - t, 2) * 3 * t * p1.x + 
				   (1 - t) * 3 * t * t * p2.x + 
				   t * t * t * p3.x;
		result.y = Math.pow(1 - t, 3) * p0.y + 
				   Math.pow(1 - t, 2) * 3 * t * p1.y + 
				   (1 - t) * 3 * t * t * p2.y + 
				   t * t * t * p3.y;
		return result;
	}
	
	public static void cubicBezier(Vec2d p0, Vec2d p1, Vec2d p2, Vec2d p3, double t, Vec2d pf) {
		pf.x = Math.pow(1 - t, 3) * p0.x + 
				   Math.pow(1 - t, 2) * 3 * t * p1.x + 
				   (1 - t) * 3 * t * t * p2.x + 
				   t * t * t * p3.x;
		pf.y = Math.pow(1 - t, 3) * p0.y + 
				   Math.pow(1 - t, 2) * 3 * t * p1.y + 
				   (1 - t) * 3 * t * t * p2.y + 
				   t * t * t * p3.y;
	}
	
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
	/**98.4% odds*/
	public static boolean c6() {return cn(6);}
	/**96.5% odds*/
	public static boolean c5() {return cn(5);}
	/**93.7% odds*/
	public static boolean c4() {return cn(4);}
	/**87.5% odds*/
	public static boolean c3() {if(random.nextBoolean() || random.nextBoolean() || random.nextBoolean()) return true; else return false;}
	/**75.0% odds*/
	public static boolean c2() {if(random.nextBoolean() || random.nextBoolean()) return true; else return false;}
	/**50.0% odds*/
	public static boolean coinFlip() {return random.nextBoolean();}
	/**33.3% odds*/
	public static boolean d3() {if(random.nextInt(3) == 0) return true; else return false;}
	public static int getD3() {return random.nextInt(1, 4);}
	/**25.0% odds*/
	public static boolean d4() {if(random.nextInt(4) == 0) return true; else return false;}
	public static int getD4() {return random.nextInt(1, 5);}
	/**20.0% odds*/
	public static boolean d5() {if(random.nextInt(5) == 0) return true; else return false;}
	public static int getD5() {return random.nextInt(1, 5);}
	/**16.7% odds*/
	public static boolean d6() {return diceRoll();}
	public static boolean diceRoll() {if(random.nextInt(6) == 0) return true; else return false;}
	public static int getDiceRoll() {return random.nextInt(1, 7);}
	/**12.5% odds*/
	public static boolean d8() {if(random.nextInt(8) == 0) return true; else return false;}
	/**10.0% odds*/
	public static boolean d10() {if(random.nextInt(10) == 0) return true; else return false;}
	/**08.3% odds*/
	public static boolean d12() {if(random.nextInt(12) == 0) return true; else return false;}
	public static int getD12() {return random.nextInt(1, 13);}
	/**06.2% odds*/
	public static boolean d16() {if(random.nextInt(16) == 0) return true; else return false;}
	public static int getD16() {return random.nextInt(1, 17);}
	/**05.0% odds*/
	public static boolean d20() {if(random.nextInt(20) == 0) return true; else return false;}
	public static int getD20() {return random.nextInt(1, 21);}
	/**01.0% odds*/
	public static boolean d100() {if(random.nextInt(100) == 0) return true; else return false;}
	public static int getD100() {return random.nextInt(1, 101);}
	/**01.0% odds*/
	public static boolean d1000() {if(random.nextInt(1000) == 0) return true; else return false;}
	public static int getD1000() {return random.nextInt(1, 1001);}
	public static boolean dn(int n) {
		if(n <= 2) {
			Console.err("Maths -> dn() -> dice must have at least two sides, n will be set to 2");
			n = 2;
		}
		if(random.nextInt(n) == 0) return true; else return false;
	}
	public static int getDn(int n) {
		if(n <= 2) {
			Console.err("Maths -> dn() -> dice must have at least two sides, n will be set to 2");
			n = 2;
		}
		return random.nextInt(1, n + 1);
	}
	public static boolean cn(int n) {
		if(n <= 1) {
			Console.err("Maths -> cn() -> coin must be flipped at least once");
			n = 1;
		}
		for(int i = 0; i < n; i ++) {
			if(random.nextBoolean()) return true;
		} return false;
	}
	
	public static int random(int lowerBound, int upperBound) {return random.nextInt(lowerBound, upperBound);}
	public static int random(int upperBound) {return random.nextInt(upperBound);}
	public static int randomInt() {return random.nextInt();}
	
	public static Long random(Long lowerBound, Long upperBound) {return random.nextLong(lowerBound, upperBound);}
	public static Long random(Long upperBound) {return random.nextLong(upperBound);}
	public static Long randomLong() {return random.nextLong();}
	
	public static double random(double lowerBound, double upperBound) {return random.nextDouble(lowerBound, upperBound);}
	public static double random(double upperBound) {return random.nextDouble(upperBound);}
	public static double randomDouble() {return random.nextDouble();}
	public static double random(double lowerBound, double upperBound, int decimalPlaces) {
		return round(random.nextDouble(lowerBound, upperBound), decimalPlaces);
	}
	public static double random(double upperBound, int decimalPlaces) {
		return round(random.nextDouble(upperBound), decimalPlaces);
	}
	public static double randomDouble(int decimalPlaces) {
		return round(random.nextDouble(), decimalPlaces);
	}

	public static Float random(Float lowerBound, Float upperBound) {return random.nextFloat(lowerBound, upperBound);}
	public static Float random(Float upperBound) {return random.nextFloat(upperBound);}
	public static Float random() {return random.nextFloat();}
	public static Float random(Float lowerBound, Float upperBound, int decimalPlaces) {
		return round(random.nextFloat(lowerBound, upperBound), decimalPlaces);
	}
	public static Float randomFloat(Float upperBound, int decimalPlaces) {
		return round(random.nextFloat(upperBound), decimalPlaces);
	}
	public static Float randomFloat(int decimalPlaces) {
		return round(random.nextFloat(), decimalPlaces);
	}	
	
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
	/**This function will take any radian rotation value and return the equivalent radian rotation within the range of 0 <= result <= TWO_PI 
	 * @param double -> rotation in radians -> domain  :  any double value
	 * @return double -> rotation in radians -> range  :  0 <= result < TWO_PI
	 * */
	public static double clampRadianRotation(double rotation) {
		if(rotation > 0 && rotation < UtilsMath.TWO_PI) return rotation;
		else {		
			if(rotation == -TWO_PI || rotation == TWO_PI || rotation == 0) return 0;
			if(rotation < -TWO_PI) {
				double divisions = Math.floor(rotation / UtilsMath.TWO_PI);
				return TWO_PI - (rotation - (divisions * UtilsMath.TWO_PI));
			}
			if(rotation > -UtilsMath.TWO_PI && rotation < 0) {
				return UtilsMath.TWO_PI - rotation;
			} 
			if(rotation > UtilsMath.TWO_PI) {
				double divisions = Math.floor(rotation / UtilsMath.TWO_PI);
				return rotation - (divisions * UtilsMath.TWO_PI);
			}
			
			Console.err("Maths -> clampRadian -> there is a critical math error, rotation : " + rotation);
			return 0.0;
		}
	}
	
	public static double clampDegreeRotation(double rotation) {
		if(rotation > 0 && rotation < 360) return rotation;
		else {
			if(rotation < -360.0) {
				double divisions = Math.floor(rotation / 360.0);
				return 360 - (rotation - (divisions * 360.0));
			}
			if(rotation > -360.0 && rotation < 0) {
				return 360 - rotation;
			} 
			if(rotation > 360) {
				double divisions = Math.floor(rotation / 360);
				return rotation - (divisions * 360);
			}
			
			Console.err("Maths -> clampRotation -> there is a critical math error! :(");
			return -1.0;
		}
	}
	
	public static Double clamp(double value, double min, double max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}
	
	public static int clamp(int value, int min, int max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}
	
	public static float clamp(float value, float min, float max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}

	public static Integer clamp(Integer value, Integer min, Integer max) {
		if(value > max) return max;
		if(value < min) return min;
		return value;
	}
	
	public static Double clampMax(double value, double max) {
		if(value > max) return max;
		return value;
	}
		
	public static int clampMax(int value, int max) {
		if(value > max) return max;
		return value;
	}
	
	public static float clampMax(float value, float max) {
		if(value > max) return max;
		return value;
	}
	
	public static Integer clampMax(Integer value, Integer max) {
		if(value > max) return max;
		return value;
	}

	public static Double clampMin(double value, double min) {
		if(value < min) return min;
		return value;
	}
	
	public static int clampMin(int value, int min) {
		if(value < min) return min;
		return value;
	}
	
	public static float clampMin(float value, float min) {
		if(value < min) return min;
		return value;
	}
	
	public static Integer clampMin(Integer value, Integer min) {
		if(value < min) return min;
		return value;
	}
	
	public static void clampMin(Vec2i vec, int min) {
		if(vec.x < min) vec.x = min;
		if(vec.y < min) vec.y = min;
	}
	
	public static void clampMax(Vec2i vec, int max) {
		if(vec.x > max) vec.x = max;
		if(vec.y > max) vec.y = max;
	}

	public static void clamp(Vec2i vec, int min, int max) {
		clampMin(vec, min);
		clampMax(vec, max);
	}
	
	public static void clampMin(Vec3i vec, int min) {
		if(vec.x < min) vec.x = min;
		if(vec.y < min) vec.y = min;
		if(vec.z < min) vec.z = min;		
	}
	
	public static void clampMax(Vec3i vec, int max) {
		if(vec.x > max) vec.x = max;
		if(vec.y > max) vec.y = max;
		if(vec.z > max) vec.z = max;
	}
	
	public static void clamp(Vec3i vec, int min, int max) {
		clampMin(vec, min);
		clampMax(vec, max);
	}	
	
	public static void clampMin(Vec2d vec, double min) {
		if(vec.x < min) vec.x = min;
		if(vec.y < min) vec.y = min;
	}
	
	public static void clampMax(Vec2d vec, double max) {
		if(vec.x > max) vec.x = max;
		if(vec.y > max) vec.y = max;
	}
	
	public static void clamp(Vec2d vec, int min, int max) {
		clampMin(vec, min);
		clampMax(vec, max);
	}
	
	public static void clampMin(Vec3d vec, double min) {
		if(vec.x < min) vec.x = min;
		if(vec.y < min) vec.y = min;
		if(vec.z < min) vec.z = min;
	}
	
	public static void clampMax(Vec3d vec, double max) {
		if(vec.x > max) vec.x = max;
		if(vec.y > max) vec.y = max;
		if(vec.z > max) vec.z = max;
	}
	
	public static void clamp(Vec3d vec, int min, int max) {
		clampMin(vec, min);
		clampMax(vec, max);
	}
	
	
	//
	//------------------Vector Maths-----------------------------------------
	//
	
	public static double vectorDifference(int x1, int y1, int x2, int y2) {
		double result = Math.sqrt(square(x2 - x1) + square(y2 - y1) );
		return result;
	}
	
	public static double vectorDifference(double x1, double y1, double x2, double y2) {
		double result = Math.sqrt( square(x2 - x1) + square(y2 - y1) );
		return result;
	}
	
	public static double vectorDifference(Vec2i vec1, Vec2i vec2)   {return vectorDifference(vec1.x, vec1.y, vec2.x, vec2.y);}
	public static double vectorDifference(Vec2d vec1, Vec2d vec2)   {return vectorDifference(vec1.x, vec1.y, vec2.x, vec2.y);}
	//public static double vectorDifference2D(Vec3d vec1, Vec3d vec2) {return vectorDifference(vec1.x, vec1.y, vec2.x, vec2.y);}
	
	//
	//-------------------Searching Algorithms------------------------
	//
	
	public static boolean contains(ArrayList<Vec2i> vectors, int x, int y) {return contains(vectors, new Vec2i(x, y));}
	public static boolean contains(ArrayList<Vec2i> vectors, Vec2i vec) {
		for(int i = 0; i < vectors.size(); i++) {
			if(vectors.get(i).isEqual(vec)) return true;
		}
		return false;
	}
	
	public static boolean containsCoordinate(ArrayList<RiverSegment> segments, int x, int y) {return containsCoordinate(segments, new Vec2i(x, y));}
	public static boolean containsCoordinate(ArrayList<RiverSegment> segments, Vec2i vec) {
		for(int i = 0; i < segments.size(); i++) {
			if(segments.get(i).getCoords().isEqual(vec)) return true;
		}
		return false;
	}
	
	
	public static int findMin(int[] values) 				 {return values[findMinIndex(values)];}
	public static int findMin(int[][] values) 				 {Vec2i coords = findMinIndex(values); return values[coords.x][coords.y];}
	public static double findMin(double[] values) 		  	 {return values[findMinIndex(values)];}
	public static double findMin(double[][] values) 		 {Vec2i coords = findMinIndex(values); return values[coords.x][coords.y];}
	public static double findMin(ArrayList<Integer> values)  {return values.get(findMinIndex(values));}
	public static int findMax(int[] values) 				 {return values[findMaxIndex(values)];}
	public static int findMax(int[][] values) 				 {Vec2i coords = findMaxIndex(values); return values[coords.x][coords.y];}
	public static double findMax(double[] values) 			 {return values[findMaxIndex(values)];}
	public static double findMax(double[][] values) 	     {Vec2i coords = findMaxIndex(values); return values[coords.x][coords.y];}
	public static double findMax(ArrayList<Integer> values)  {return values.get(findMinIndex(values));}
	
	public static int findMinX(ArrayList<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMinXIndex(rectangles)).x;}
	public static int findMaxX(ArrayList<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMaxXIndex(rectangles)).x;}
	public static int findMinY(ArrayList<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMinYIndex(rectangles)).y;}
	public static int findMaxY(ArrayList<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMaxYIndex(rectangles)).y;}
	public static int findMinWidth(ArrayList<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMinWidthIndex(rectangles)).width;}
	public static int findMaxWidth(ArrayList<Rectangle> rectangles) {return rectangles.get(UtilsMath.findMaxWidthIndex(rectangles)).height;}
	
	
	public static int findMinIndex(int[] values) {
		int result = -1;
		
		int currentMin = 0;
		for(int i = 0; i < values.length; i++) {
			if(i == 0) continue;
			if(values[i] < values[currentMin]) currentMin = i;
		}
		result = currentMin;
		
		return result;
	}

	public static Vec2i findMinIndex(int[][] values) {
		Vec2i result = new Vec2i();
		
		Vec2i currentMin = new Vec2i();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] < values[currentMin.x][currentMin.y]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		
		return result;
	}
	
	public static int findMinIndex(double[] values) {
		int result = -1;
		
		int currentMin = 0;
		for(int i = 0; i < values.length; i++) {
			if(i == 0) continue;
			if(values[i] < values[currentMin]) currentMin = i;
		}
		result = currentMin;
		
		return result;
	}
	
	public static Vec2i findMinIndex(double[][] values) {
		Vec2i result = new Vec2i();
		
		Vec2i currentMin = new Vec2i();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] < values[currentMin.x][currentMin.y]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		
		return result;
	}
	
	public static int findMinIndex(ArrayList<Integer> values) {
		int result = -1;
		
		int currentMin = 0;
		for(int i = 0; i < values.size(); i++) {
			if(i == 0) continue;
			if(values.get(i) < values.get(currentMin)) currentMin = i;
		}
		result = currentMin;
		
		return result;
	}
	
	
	
	public static int findMaxIndex(int[] values) {
		int result = -1;
		
		int currentMin = 0;
		for(int i = 0; i < values.length; i++) {
			if(i == 0) continue;
			if(values[i] > values[currentMin]) currentMin = i;
		}
		result = currentMin;
		
		return result;
	}
	
	public static Vec2i findMaxIndex(int[][] values) {
		Vec2i result = new Vec2i();
		
		Vec2i currentMin = new Vec2i();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] > values[currentMin.x][currentMin.y]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		
		return result;
	}
		
	public static int findMaxIndex(double[] values) {
		int result = -1;
		
		int currentMin = 0;
		for(int i = 0; i < values.length; i++) {
			if(i == 0) continue;
			if(values[i] > values[currentMin]) currentMin = i;
		}
		result = currentMin;
		
		return result;
	}
	
	public static Vec2i findMaxIndex(double[][] values) {
		Vec2i result = new Vec2i();
		
		Vec2i currentMin = new Vec2i();
		for(int y = 0; y < values.length; y++) {
			for(int x = 0; x < values[y].length; x++) {
				if(x == 0 && y == 0) continue;
				if(values[x][y] > values[currentMin.x][currentMin.y]) currentMin.set(x, y);
			}
		}
		result = currentMin;
		
		return result;
	}
	
	public static int findMaxIndex(ArrayList<Integer> values) {
		int result = -1;
		
		int currentMin = 0;
		for(int i = 0; i < values.size(); i++) {
			if(i == 0) continue;
			if(values.get(i) > values.get(currentMin)) currentMin = i;
		}
		result = currentMin;
		
		return result;
	}
	
	private static boolean validRectangles(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMinXIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMaxXIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMinYIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMaxYIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMinWidthIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMaxWidthIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMaxHeightIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static int findMinHeightIndex(ArrayList<Rectangle> rectangles) {
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
	
	public static Vec2d findMinBounds(ArrayList<Rectangle> rectangles) {
		Vec2d result = new Vec2d(findMinX(rectangles), findMinY(rectangles));		
		return result;
	}
	
	public static Vec2d findMaxBounds(ArrayList<Rectangle> rectangles) {
		Vec2d result = new Vec2d();
		double xMax = 0;
		double yMax = 0;
		
		for(int i = 0; i < rectangles.size(); i++) {
			Rectangle rect = rectangles.get(i);
			double cx = rect.getX() + rect.getWidth();
			double cy = rect.getY() + rect.getHeight();
			
			if(cx > xMax) xMax = cx;
			if(cy > yMax) yMax = cy;
		
		}
		
		result.set(xMax, yMax);
		return result;
	}
	
	
}
