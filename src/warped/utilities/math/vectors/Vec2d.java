/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.vectors;

import java.awt.Point;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class Vec2d { //NOTE -> when used in combination with MatrixD will be considered a ROW VECTOR

	public double x, y;
		
	//Constructors
	public Vec2d() {x = y = 0;}
	public Vec2d(double v) {x = y = v;}
	public Vec2d(double x, double y) {this.x = x; this.y = y;}
	public Vec2d(Vec2d vec) {this.x = vec.x; this.y = vec.y;}
	public Vec2d(Vec3d vec) {this.x = vec.x; this.y = vec.y;}
	public Vec2d(Vec2i vec) {this.x = (double)vec.x; this.y = (double)vec.y;}
	
	//Getters and Setters
	public static int getM() {return 1;}
	public static int getN() {return 2;}
	public void set(double value) {x = y = value;}
	public void set(Vec3d vec) {x = vec.x;y = vec.y;}
	public void set(Vec2d vec) {x = vec.x;y = vec.y;}
	public void set(Vec2i vec) {x = vec.x;y = vec.y;}
	public void set(double x, double y) {this.x = x;this.y = y;}
	public void set(Point point) {x = point.x; y = point.y;}
	public void zero() {x = y = 0;}
	public double getValue(int index) {
		switch(index) {
		case 0 : return x;		
		case 1 : return y;
		default: Console.err("Vec2d -> getValue() -> invalid index : " + index); return -0.1;
		}
	}
	
	// --------Instance Operations
	public void add(double v) {this.x += v; this.y += v;}
	public void add(double x, double y) {this.x += x; this.y += y;}
	public void addX(double x) {this.x += x;}
	public void addY(double y) {this.y += y;}
	public void add(Vec2d vec) {x += vec.x; y += vec.y;}
	public void add(Vec2i vec) {x += vec.x;y += vec.y;}
	public void add(Vec3d vec) {x += vec.x; y += vec.y;}
	
	public void add(double magnitude, Vec2d target) {
		Vec2d dir = this.unitVector(target);
		dir.scale(magnitude);
		add(dir);
	}
	
	public boolean reduced(double value, double cap) {
		if(value <= 0.0) {
			Console.err("Vec2d -> reduce() -> percent is outside bounds : " + value);
			value = 0.001;
		}
		
		Vec2d vec = unitVector();
		vec.scale(-value);
		add(vec);
		
		//Console.ln("Vec2d -> reduce() -> : " + getString());
		if(x <= cap && x >= -cap && y <= cap && y >= -cap) return true;
		return false;
	}
	public void minus(double v) {this.x -= v; this.y -= v;}
	public void minus(double x, double y) {this.x -= x; this.y -= y;}
	public void minus(Vec2d vec) {x -= vec.x;y -= vec.y;}
	public void minus(Vec3d vec) {x -= vec.x;y -= vec.y;}
	public void scale(double scale) {x *= scale; y *= scale;}
	public void scale(Vec2d scale) {this.x *= scale.x; this.y *= scale.y;}
	public double dotProduct(Vec2d vec) {return UtilsMath.dotProduct(this, vec);}
	
	public String getString() {return (" (" + (float)x + ", " + (float)y + ") ");}
	public void println() {Console.ln("Vec2d : (" + x + ", " + y + ") ");}
	public Vec2d clone() {return new Vec2d(x,y);}
	public void round() {x = Math.round(x); y = Math.round(y);}
	
	public void variation(Vec2d variation) {
		double rx = UtilsMath.random.nextDouble(variation.x);
		double ry = UtilsMath.random.nextDouble(variation.y);
		
		if(UtilsMath.random.nextBoolean()) x += rx;
		else x -= rx;
		
		if(UtilsMath.random.nextBoolean()) y += ry;
		 else y -= ry;
	}
	
	public void setAnyLessThan(double minimumValue, double setValue) {
		if(x < minimumValue) x = setValue;
		if(y < minimumValue) y = setValue;
	}
	public void setAnyGreaterThan(double maxValue, double setValue) {
		if(x > maxValue) x = setValue;
		if(y > maxValue) y = setValue;
	}
	
	// ----------Static Operations
	public static Vec2d addVectors(Vec2d vec1, Vec2d vec2) {
		Vec2d result = new Vec2d();
		result.x = vec1.x + vec2.x;
		result.y = vec1.y + vec2.y;
		return result;
	}
	
	public static Vec2d scale(Vec2d vec, double scale) {
		Vec2d result = new Vec2d();
		result.set(vec);
		result.scale(scale);
		return result;
	}
	
	public static Vec2d addition(Vec2d vector1, Vec2d vector2) {
		Vec2d result = new Vec2d();
		result.x = vector1.x;
		result.y = vector1.y;
		result.x += vector2.x;
		result.y += vector2.y;
		return result;
	}
	public static Vec2d addition(Vec2d vector1, Vec3d vector2) {
		Vec2d result = new Vec2d();
		result.x = vector1.x;
		result.y = vector1.y;
		result.x += vector2.x;
		result.y += vector2.y;
		return result;
	}
	public static Vec2d subtraction(Vec2d vector1, Vec2d vector2) {
		Vec2d result = new Vec2d();
		result.x = vector1.x;
		result.y = vector1.y;
		result.x -= vector2.x;
		result.y -= vector2.y;
		return result;
	}
	public static Vec2d subtraction(Vec2d vector1, Vec3d vector2) {
		Vec2d result = new Vec2d();
		result.x = vector1.x;
		result.y = vector1.y;
		result.x -= vector2.x;
		result.y -= vector2.y;
		return result;
	}
	public static Vec2d scale(double scalar, Vec2d vec) {
		Vec2d result = new Vec2d();
		result.x = vec.x * scalar;
		result.y = vec.y * scalar;
		return result;
	}
	
	public static Vec2d clone(Vec2d vec) {
		Vec2d result = new Vec2d();
		result.x = vec.x;
		result.y = vec.y;
		return result;
	}
	
	public static double difference(Vec2d vector1, Vec2d vector2) {
		double rx = (vector1.x - vector2.x) * (vector1.x - vector2.x);
		double ry = (vector1.y - vector2.y) * (vector1.y - vector2.y);  
		return Math.sqrt(rx  +  ry);		
	}
	
	public double difference(Vec2d vector) {
		double rx = (x - vector.x) * (x - vector.x);
		double ry = (y - vector.y) * (y - vector.y);  
		return Math.sqrt(rx  +  ry);
	}
	
	
	public Vec2d unitVector() {
		Vec2d result = new Vec2d();
		double mod = modulus();
		result.x = x / mod;
		result.y = y / mod;
		return result;
	}
	
	
	public Vec2d vecDifference(Vec2d vector) {
		Vec2d result = new Vec2d();
		result.x = (this.x - vector.x);
		result.y = (this.y - vector.y);
		return result;
	}
	
	public Vec2d unitVector(Vec2d b) {return unitVector(this, b);}
	public static Vec2d unitVector(Vec2d a, Vec2d b) {
		Vec2d dif = b.vecDifference(a);
		double mod = dif.modulus();
		return new Vec2d(dif.x / mod, dif.y / mod);
	}
	
	
	public double modulus() {
		double result = Math.sqrt((x * x) + (y * y));
		return result;
		
	}
	public static double modulus(Vec2d vec) {return Math.sqrt((vec.x * vec.x) + (vec.y * vec.y));}
	
	
	public static Vec2d vectorDifference(Vec2d vec1, Vec2d vec2) {
		return new Vec2d((vec2.x - vec1.x), (vec2.y - vec1.y));
	}
		
	
	
	/////////Vector Generation
	public Vec2d generateScaledClone(double scale) {return new Vec2d(this.x * scale, this.y * scale);}
	public static Vec2d generateScaledClone(Vec2d vec, double scale) {return new Vec2d(vec.x * scale, vec.y * scale);}
	
	public static Vec2d generateRandomVector(double xBound, double yBound) {
		Vec2d result = new Vec2d();
		result.x = UtilsMath.random.nextDouble(xBound);
		result.y = UtilsMath.random.nextDouble(yBound);
		return result;		
	}
	
	public static Vec2d generateRandomVector(double xMin,double xMax, double yMin, double yMax) {
		Vec2d result = new Vec2d();
		result.x = UtilsMath.random.nextDouble(xMin,xMax);
		result.y = UtilsMath.random.nextDouble(yMin,yMax);
		return result;		
	}

	public static Vec2d generateRandomVariedVector(Vec2d vec, double variance) {
		Vec2d result = new Vec2d(vec.x, vec.y);
		if(variance <= 0 ) {
			Console.err("Vec2d -> generateRandomVector() -> margin must be greater than 0");
			return result;
		}
		
		double rx = UtilsMath.random.nextDouble(variance);
		double ry = UtilsMath.random.nextDouble(variance);
		
		if( UtilsMath.random.nextBoolean()) result.x += rx;
		else result.x -= rx;			
		
		if(UtilsMath.random.nextBoolean()) result.y += ry;
		else result.y -= ry;
		
		return result;
	}
	
	public static Vec2d generateRandomVariedVector(Vec2d vec, Vec2d variance) {
		Vec2d result = new Vec2d(vec.x, vec.y);
		if(variance.x <= 0 || variance.y <= 0) {
			Console.err("Vec2d -> generateRandomVector() -> margin must be greater than 0");
			return result;
		}
		
		double rx = UtilsMath.random.nextDouble(variance.x);
		double ry = UtilsMath.random.nextDouble(variance.y);
		
		if( UtilsMath.random.nextBoolean()) result.x += rx;
		else result.x -= rx;			
		
		if(UtilsMath.random.nextBoolean()) result.y += ry;
		else result.y -= ry;
		
		return result;
	}
	
	public static Vec2d generateUnitVector(Vec2d vec1, Vec2d vec2) {
		Vec2d result = vectorDifference(vec1, vec2);
		double mod = result.modulus();
		result.x = result.x / mod;
		result.y = result.y / mod;
		return result;
	}
	
	public static Vec2d generateRandomBaseVector() {
		Vec2d result = new Vec2d();
		Vec2d r = generateRandomVector(3,3);
		
		if(r.x == 0) 	  result.x = -1;
		else if(r.x == 1) result.x =  1;
		else 			  result.x =  0;
		
		if(r.y == 0)	  result.y = -1;
		else if(r.y == 1) result.y =  1;
		else 			  result.y =  0;
	
		return result;
	}
	
	/////////Vector Conversion
	public static Vec2d convertVec3ToVec2(Vec2d original) {
		Vec2d result = new Vec2d();
		result.x = original.x;
		result.y = original.y;
		return result;
	}

	/////////Logical Checks
	public boolean isEqualTo(Vec2d vec) {if(vec.x == this.x && vec.y == this.y) return true; else return false;}
	public boolean isLessThan(Vec2d vec) {if(x < vec.x && y < vec.y) return true; else return false;}
	public boolean isGreaterThan(Vec2d vec) {if(x > vec.x && y > vec.y) return true; else return false;}
	public boolean isEqualTo(double value) {if(x == value && y == value) return true; else return false;}
	public boolean isLessThan(double value) {if(x < value && y < value) return true; else return false;}
	public boolean isGreaterThan(double value) {if(x > value && y > value) return true; else return false;}
	public boolean isAnyLessThan(double value) {if(x < value || y < value) return true; else return false;}
	public boolean isAnyGreaterThan(double value) {if(x > value || y > value) return true; else return false;}
	public boolean isZeroed() {if(x == 0.0 && y == 0.0) return true; else return false;}
	
}
