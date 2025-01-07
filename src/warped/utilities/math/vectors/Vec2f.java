/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.vectors;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class Vec2f { //NOTE -> when used in combination with MatrixD will be considered a ROW VECTOR

	public float x, y;
		
	//Constructors
	public Vec2f() {x = y = 0.0f;}
	public Vec2f(float v) {x = y = v;}
	public Vec2f(float x, float y) {this.x = x; this.y = y;}
	public Vec2f(Vec2f vec) {this.x = vec.x; this.y = vec.y;}
	
	//Getters and Setters
	public static int getM() {return 1;}
	public static int getN() {return 2;}
	public void set(float value) {x = y = value;}
	public void set(Vec2f vec) {x = vec.x;y = vec.y;}
	public void set(float x, float y) {this.x = x;this.y = y;}
	public void zero() {x = y = 0.0f;}
	public double getValue(int index) {
		switch(index) {
		case 0 : return x;		
		case 1 : return y;
		default: Console.err("Vec2d -> getValue() -> invalid index : " + index); return -0.1;
		}
	}
	
	/////Basic Vector Operations
	public void add(double x, double y) {this.x += x;	this.y += y;}
	public void add(Vec2d vec) {x += vec.x;y += vec.y;}
	public void minus(double x, double y) {this.x -= x; this.y -= y;}
	public void minus(Vec2d vec) {x -= vec.x;y -= vec.y;}
	public void scale(double scale) {x *= scale; y *= scale;}
	public void scale(Vec2d scale) {this.x *= scale.x; this.y *= scale.y;}
	public String getPrintln() {return (" (" + x + ", " + y + ") ");}
	public void println() {Console.ln(" (" + x + ", " + y + ") ");}
	public void copy(Vec2f vec) {x = vec.x; y = vec.y;}
	
	public static Vec2f addVectors(Vec2f vector1, Vec2f vector2) {
		Vec2f result = new Vec2f();
		result.x = vector1.x;
		result.y = vector1.y;
		result.x += vector2.x;
		result.y += vector2.y;
		return result;
	}
	public static Vec2f scale(float scalar, Vec2f vec) {
		Vec2f result = new Vec2f();
		result.x = vec.x * scalar;
		result.y = vec.y * scalar;
		return result;
	}
	
	public static Vec2f clone(Vec2f vec) {
		Vec2f result = new Vec2f();
		result.x = vec.x;
		result.y = vec.y;
		return result;
	}
	
	public static double difference(Vec2f vector1, Vec2f vector2) {
		double rx = (vector1.x - vector2.x) * (vector1.x - vector2.x);
		double ry = (vector1.y - vector2.y) * (vector1.y - vector2.y);  
		return Math.sqrt(rx  +  ry);		
	}
	
	public double difference(Vec2f vector) {
		double rx = (x - vector.x) * (x - vector.x);
		double ry = (y - vector.y) * (y - vector.y);  
		return Math.sqrt(rx  +  ry);
	}
	
	public Vec2f unitVector() {
		Vec2f result = new Vec2f();
		float mod = modulus();
		result.x = x / mod;
		result.y = y / mod;
		return result;
	}
	
	
	public float modulus() {return (float) Math.sqrt((x * x) + (y * y));}
	
	public Vec2f vecDifference(Vec2f vector) {
		float rx = (this.x - vector.x) * (this.x - vector.x);
		float ry = (this.y - vector.y) * (this.y - vector.y);
		return new Vec2f(rx, ry);
	}
	
	/////////Vector Generation
	public static Vec2f generateRandomVector(float xBound, float yBound) {
		Vec2f result = new Vec2f();
		result.x = UtilsMath.random.nextFloat(xBound);
		result.y = UtilsMath.random.nextFloat(yBound);
		return result;		
	}
	public static Vec2f generateRandomVector(float xMin,float xMax, float yMin, float yMax) {
		Vec2f result = new Vec2f();
		result.x = UtilsMath.random.nextFloat(xMin,xMax);
		result.y = UtilsMath.random.nextFloat(yMin,yMax);
		return result;		
	}
	public static Vec2f generateRandomBaseVector() {
		Vec2f result = new Vec2f();
		Vec2f r = generateRandomVector(3,3);
		
		if(r.x == 0) 	  result.x = -1;
		else if(r.x == 1) result.x =  1;
		else 			  result.x =  0;
		
		if(r.y == 0)	  result.y = -1;
		else if(r.y == 1) result.y =  1;
		else 			  result.y =  0;
	
		return result;
	}
	
	/////////Vector Conversion
	public static Vec2f convertVec3ToVec2(Vec3f original) {
		Vec2f result = new Vec2f();
		result.x = original.x;
		result.y = original.y;
		return result;
	}

	/////////Logical Checks
	public boolean equals(Vec2f vec) {
		if(vec.x == this.x && vec.y == this.y) return true;
		else return false;
	}
	public boolean lessThan(Vec2f vec) {
		if(x < vec.x && y < vec.y) return true;
		return false;
	}
	public boolean greatThan(Vec2f vec) {
		if(x > vec.x && y > vec.y) return true;
		return false;
	}
	public boolean equals(float value) {
		if(x == value && y == value) return true;
		else return false;
	}
	public boolean lessThan(float value) {
		if(x < value && y < value) return true;
		return false;
	}
	public boolean greatThan(float value) {
		if(x > value && y > value) return true;
		return false;
	}
	public boolean anyLessThan(float value) {
		if(x < value || y < value) return true;
		return false;
	}
	public boolean anyGreatThan(float value) {
		if(x > value || y > value) return true;
		return false;
	}
	
}
