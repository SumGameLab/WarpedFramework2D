/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.vectors;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class Vec3f { //NOTE -> when used in combination with MatrixD will be considered a ROW VECTOR

	public float x, y, z;
	
	//Constructors
	public Vec3f() {x = y = z = 0.0f;}
	public Vec3f(float v) {x = y = z = v;}
	public Vec3f(float x, float y, float z) {this.x = x;this.y = y;this.z = z;}
	public Vec3f(Vec3f vec) {this.x = vec.x; this.y = vec.y; this.z = vec.z;}
	
	//Getters and Setters
	public static int getM() {return 1;}
	public static int getN() {return 3;}
	public void set(int v) {x = y = z = v;}
	public void set(float x, float y, float z) {this.x = x;this.y = y;this.z = z;}
	public void zero() {x = y = z = 0.0f;}
	public double getValue(int index) {
		switch(index) {
		case 0: return x;
		case 1: return y;
		case 2: return z;
		default: Console.err("Vec3d -> getValue() -> invalid index : " + index); return -0.1;
		}
	}
	
	//Instanced operations
	//WILL change the instance that calls these functions
	////////Basic Vector Operations
	public void scale(int scalar) {x *= scalar; y*= scalar; z *= scalar;}
	public void scale(Vec3f scalar) { x *= scalar.x; y *= scalar.y; z *= scalar.z;}
	public void add(float x, float y, float z) {this.x += x; this.y += y; this.z += z;}
	public void add(Vec3f vec) {x += vec.x;y += vec.y; z += vec.z;};
	public void minus(Vec3f vec) {x -= vec.x;y -= vec.y;z -= vec.z;}
	public void minus(float x, float y, float z) {this.x -= x; this.y -= y; this.z -= z;}
	public void println() {Console.ln(" (" + x + ", " + y + ", "+ z + ") ");}
	public void copy(Vec3f vec) {x = vec.x; y = vec.y; z = vec.z;}
	
	//Instance operations 
	//Will not change the instance that calls these functions but will use them for calculation
	public double dotProduct(Vec3f vec) {return ((x * vec.x) + (y * vec.y) + (z * vec.z));}
	
	public Vec3f crossProduct(Vec3f vec) {
		Vec3f result = new Vec3f();
		//TODO  18/1/24 -> implement crossProduct function
		return result;
	}
	
	public float difference(Vec3f vector) { //FIXME 18/1/24 -> is this correct? -> LOL I'm not going to fix this today either 
		float rx = (this.x - vector.x) * (this.x - vector.x);
		float ry = (this.y - vector.y) * (this.y - vector.y);  
		float rz = (this.z - vector.z) * (this.z - vector.z); 
		return (float) Math.sqrt(rx  +  ry + rz);
	}	
	
	public Vec3f vecDifference(Vec3f vector) {
		Vec3f result = new Vec3f();
		result.x = (this.x - vector.x) * (this.x - vector.x);
		result.y = (this.y - vector.y) * (this.y - vector.y);
		result.z = (this.z - vector.z) * (this.z - vector.z);
		return result;
	}
	
	public Vec3f unitVector() {
		Vec3f result = new Vec3f();
		float mod = modulus();
		result.x = x / mod;
		result.y = y / mod;
		result.z = z / mod;
		return result;
	}
	
	public float modulus() {return (float)Math.sqrt((x * x) + (y * y) + (z * z));}
	
	//Static operations,
	//Return a new result -> Does NOT change the inputs
	public static Vec3f clone(Vec3f vec) {
		Vec3f result = new Vec3f();
		result.x = vec.x;
		result.y = vec.y;
		result.z = vec.z;
		return result; 
	}
	
	public static double dotProduct(Vec3f vec1, Vec3f vec2) {
		return ((vec1.x * vec2.x) + (vec1.y * vec2.y) + (vec1.z * vec2.z));
	}
	
	public static Vec3f scale(float scalar, Vec3f vec) {
		Vec3f result = new Vec3f();
		result.x = vec.x * scalar;
		result.y = vec.y * scalar;
		result.z = vec.z * scalar;
		return result;
	}
	
	public static Vec3f scaleVector(Vec3f vec1, Vec3f vec2) {
		Vec3f result = new Vec3f();
		result.x = vec1.x * vec2.x;
		result.y = vec1.y * vec2.y;
		result.z = vec1.z * vec2.z;
		return result;
	}
	public static Vec3f crossProduct(Vec3f vec1, Vec3f vec2) {
		Vec3f result = new Vec3f();
		result.x =   (vec1.y * vec2.z) - (vec1.z * vec2.y); 
		result.y = -((vec1.x * vec2.z) - (vec1.z * vec2.x));
		result.z =   (vec1.x * vec2.y) - (vec1.y * vec2.x);
		return result;
	}
	
	public static Vec3f addVectors(Vec3f vector1, Vec3f vector2) {
		Vec3f result = new Vec3f();
		result.x = vector1.x;
		result.y = vector1.y;
		result.z = vector1.z;
		result.x += vector2.x;
		result.y += vector2.y;
		result.z += vector2.z;
		return result;
	}
	public static Vec3f subtractVectors(Vec3f vec1, Vec3f vec2) {
		Vec3f result = new Vec3f();
		result.x = vec1.x;
		result.y = vec1.y;
		result.z = vec1.z;
		result.x -= vec2.x;
		result.y -= vec2.y;
		result.z -= vec2.z;
		return result;
	}
	
	public static float difference(Vec3f vector1, Vec3f vector2) {
		double rx = (vector1.x - vector2.x) * (vector1.x - vector2.x);
		double ry = (vector1.y - vector2.y) * (vector1.y - vector2.y);  
		double rz = (vector1.z - vector2.z) * (vector1.z - vector2.z);  
		return (float) Math.sqrt(rx  +  ry + rz);		
	}
	
	
	////////////////VECTOR GENERATION
	public static Vec3f generateRandomVector(float xBound, float yBound, float zBound) {
		Vec3f result = new Vec3f();
		result.x = UtilsMath.random.nextFloat(xBound);
		result.y = UtilsMath.random.nextFloat(yBound);
		result.z = UtilsMath.random.nextFloat(zBound);
		return result;		
	}
	public static Vec3f generateRandomVector(float xMin,float xMax, float yMin, float yMax, float zMin, float zMax) {
		Vec3f result = new Vec3f();
		result.x = UtilsMath.random.nextFloat(xMin,xMax);
		result.y = UtilsMath.random.nextFloat(yMin,yMax);
		result.z = UtilsMath.random.nextFloat(zMin,zMax);
		return result;		
	}
	public static Vec3f generateRandomBaseVector() {
		Vec3f result = new Vec3f();
		Vec3f r = generateRandomVector(3,3,3);
		
		if(r.x == 0) 	  result.x = -1;
		else if(r.x == 1) result.x =  1;
		else 			  result.x =  0;
		
		if(r.y == 0)	  result.y = -1;
		else if(r.y == 1) result.y =  1;
		else 			  result.y =  0;
		
		if(r.z == 0)	  result.z = -1;
		else if(r.z == 1) result.z =  1;
		else 			  result.z =  0;
	
		return result;
	}
	
	///////////////////////CONVERSION
	public static Vec2f convertVec3ToVec2(Vec3f original) {
		Vec2f result = new Vec2f();
		result.x = original.x;
		result.y = original.y;
		return result;
	}
		
	
	//LOGICAL CHECKS
	public boolean equals(Vec3f vec) {if(vec.x == this.x && vec.y == this.y && vec.z == this.z) return true; return false;}
	public boolean lessThan(Vec3f vec) {if(x < vec.x && y < vec.y && z < vec.z)return true;	return false;}
	public boolean greaterThan(Vec3f vec) { if(x > vec.x && y > vec.y && z > vec.z)return true;	return false;}
	
	public static boolean equals(Vec3f vec1, Vec3f vec2) {if(vec1.x == vec2.x && vec1.y == vec2.y && vec1.z == vec2.z)return true;return false;}
	public static boolean lessThan(Vec3f vec1, Vec3f vec2) {if(vec1.x < vec2.x && vec1.y < vec2.y && vec1.z < vec2.z) return true; return false;}
	public static boolean greaterThan(Vec3f vec1, Vec3f vec2) { if(vec1.x > vec2.x && vec1.y > vec2.y && vec1.z > vec2.z) return true; return false;}
}
