/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.vectors;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class Vec3i { // NOTE -> when used in combination with MatrixD will be considered a ROW VECTOR
	
	public int x, y, z;
	
	//Constructors
	public Vec3i() {x = y = z = 0;}
	public Vec3i(int v) {x = y = z = v;}
	public Vec3i(int x, int y, int z) {this.x = x; this.y = y; this.z = z;}
	public Vec3i(Vec3i vec) {this.x = vec.x; this.y = vec.y; this.z = vec.z;}

	//Getters and setters
	public static int getM() {return 1;}
	public static int getN() {return 3;}
	public void set(Vec3i vec) {x = vec.x; y = vec.y; z = vec.z;}
	public void setValue(int value) { x = y = z = value;}
	public void zero() {x = y = 0;}
	public int getValue(int index) {
		switch(index) {
		case 0: return x;
		case 1: return y;
		case 2: return z;
		default: Console.err("Vec3i -> getValue() -> invalid index : " + index);	return -1;
		}	
	}
	
	//Basic Vector Functions
	//Instance specific -> will change the vector that calls the function
	public void println() {Console.ln(" (" + x + ", " + y + ", "+ z + ") ");}
	public String getPrintln() {return(" (" + x + ", " + y + ", "+ z + ") ");}
	public void add(Vec3i vec) {x += vec.x;	y += vec.y;	z += vec.z;}
	public void minus(Vec3i vec) {x -= vec.x; y -= vec.y; z -= vec.z;}
	public void scaleVector(int scale) {x = x * scale;	y = y * scale;	z = z * scale;}
	public double dotProduct(Vec3i vec) {return UtilsMath.dotProduct(this, vec);}
	
	public Vec3i vecDifference(Vec3i vector) {
		int rx = (this.x - vector.x) * (this.x - vector.x);
		int ry = (this.y - vector.y) * (this.y - vector.y);
		int rz = (this.z - vector.z) * (this.z - vector.z);
		return new Vec3i(rx, ry, rz);
	}
	
	public double doubleDifference(Vec3i vector) {
		int rx = (this.x - vector.x) * (this.x - vector.x);
		int ry = (this.y - vector.y) * (this.y - vector.y); 
		int rz = (this.z - vector.z) * (this.z - vector.z); 
		return Math.sqrt(rx  +  ry + rz);
	}
	
	//Static vector methods
	//Returns a new result vector without modifying the input vectors
	public static Vec3i addVectors(Vec3i vector1, Vec3i vector2) {
		Vec3i result = new Vec3i();
		result.x = vector1.x + vector2.x;
		result.y = vector1.y + vector2.x;
		result.z = vector1.z + vector2.x;
		return result;
	}
	public static Vec3i vecDifference(Vec3i vector, Vec3i vector2) {
		int rx = (vector.x - vector2.x) * (vector.x - vector2.x);
		int ry = (vector.y - vector2.y) * (vector.y - vector2.y);
		int rz = (vector.z - vector2.z) * (vector.z - vector2.z);
		return new Vec3i(rx, ry, rz);		
	}
	
	public static double doubleDifference(Vec3i vector, Vec3i vector2) {
		int rx = (vector.x - vector2.x) * (vector.x - vector2.x);
		int ry = (vector.y - vector2.y) * (vector.y - vector2.y); 
		int rz = (vector.z - vector2.z) * (vector.z - vector2.z); 
		return Math.sqrt(rx  +  ry + rz);
	}
	
	//Vector Generation
	public static Vec3i generateRandomVector(Vec2i xBounds, Vec2i yBounds, Vec2i zBounds) {
		Vec3i result = new Vec3i();
		result.x = UtilsMath.random.nextInt(xBounds.x, xBounds.y);
		result.y = UtilsMath.random.nextInt(yBounds.x, yBounds.y);
		result.z = UtilsMath.random.nextInt(zBounds.x, zBounds.y);
		return result;		
	}
	public static Vec3i generateRandomVector(Vec2i bounds) {
		Vec3i result = new Vec3i();
		result.x = UtilsMath.random.nextInt(bounds.x, bounds.y);
		result.y = UtilsMath.random.nextInt(bounds.x, bounds.y);
		result.z = UtilsMath.random.nextInt(bounds.x, bounds.y);
		return result;		
	}
	public static Vec3i generateRandomVector(int xBound, int yBound, int zBound) {
		Vec3i result = new Vec3i();
		result.x = UtilsMath.random.nextInt(xBound);
		result.y = UtilsMath.random.nextInt(yBound);
		result.z = UtilsMath.random.nextInt(zBound);
		return result;		
	}
	public static Vec3i generateRandomBaseVector() {
		Vec3i result = new Vec3i();
		Vec3i r = generateRandomVector(3,3,3);
		
		if(r.x == 0) 	  result.x = -1;
		else if(r.x == 1) result.x =  1;
		else 			  result.x =  0;
		
		if(r.y == 0)	  result.y = -1;
		else if(r.y == 1) result.y =  1;
		else 			  result.y =  0;
		
		if(r.z == 0) 	  result.z = -1;
		else if(r.z == 1) result.z =  1;
		else 			  result.z =  0;
		
		return result;
	}
	
	//Vector conversion	
	public static Vec3i convertVec2ToVec3(Vec2i original) {
		Vec3i result = new Vec3i();
		result.x = original.x;
		result.y = original.y;
		return result;
	}
	
	//Logical Checks
	public boolean equals(Vec3i vec) {if(vec.x == this.x && vec.y == this.y && vec.z == this.z) return true; return false;}
	public boolean lessThan(Vec3i vec) {if(x < vec.x && y < vec.y && z < vec.z)return true;	return false;}
	public boolean greaterThan(Vec3i vec) { if(x > vec.x && y > vec.y && z > vec.z)return true;	return false;}
	
	public static boolean equals(Vec3i vec1, Vec3i vec2) {if(vec1.x == vec2.x && vec1.y == vec2.y && vec1.z == vec2.z)return true;return false;}
	public static boolean lessThan(Vec3i vec1, Vec3i vec2) {if(vec1.x < vec2.x && vec1.y < vec2.y && vec1.z < vec2.z) return true; return false;}
	public static boolean greaterThan(Vec3i vec1, Vec3i vec2) { if(vec1.x > vec2.x && vec1.y > vec2.y && vec1.z > vec2.z) return true; return false;}
	

	
	
	
}
