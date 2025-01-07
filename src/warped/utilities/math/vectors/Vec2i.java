/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.vectors
;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class Vec2i { //NOTE -> when used in combination with MatrixI will be considered a ROW VECTOR

	public int x, y;
	
	//Constructors
	public Vec2i() {x = y = 0;}
	public Vec2i(int v) {x = y = v;}
	public Vec2i(int x, int y) {this.x = x;	this.y = y;}
	public Vec2i(Vec2i vec) {this.x = vec.x; this.y = vec.y;}
	public Vec2i(Vec3d vec) {this.x = (int)vec.x; this.y = (int)vec.y;}
	public Vec2i(Vec2d vec) {this.x = (int)vec.x; this.y = (int)vec.y;}
	public Vec2i(double x, double y) {this.x = (int)x; this.y = (int)y;}
	
	
	//Getters and setters
	public static int getM() {return 1;}
	public static int getN() {return 2;}
	public void set(Vec2i vec, int delta) {this.x = vec.x + delta; this.y = vec.y + delta;}
	public void set(Vec2i vec, double delta) {this.x = vec.x + (int)delta; this.y = vec.y + (int)delta;}
	public void set(Vec2i vec) {x = vec.x;y = vec.y;}
	public void set(Vec2d vec) {this.x = (int)vec.x; this.y = (int)vec.y;}
	public void set(int x, int y) {this.x = x; this.y = y;}
	public void set(double x, double y) {this.x = (int)x; this.y = (int)y;}
	public void zero() {x = y = 0;}
	public void set(int value) {x = y = value;}
	public int getValue(int index) {
		switch(index) {
		case 0: return x;
		case 1: return y;
		default: Console.err("Vec2i -> getValue() -> tried to access non existant index : " + index); return -1;
		}		
	}
	
	//Instance specific -> will change the value of the object that calls it
	//Basic Vector Operations
	public void println() {Console.ln(" ( " + x + ", " + y + " )");}
	public String getString() {return (" ( " + x + ", " + y + " )");}
	public void add(int v) {this.x += v; this.y += v;}
	public void add(Vec2i vec) {x += vec.x; y += vec.y;}
	public void add(Vec2d vec) {x += (int)vec.x; y += (int)vec.y;}
	public void add(int x, int y) {this.x += x; this.y += y;}
	public void add(double x, double y) {this.x += x; this.y += y;}
	public void minus(int v) {this.x += v; this.y += v;}
	public void minus(Vec2i vec) {x -= vec.x; y -= vec.y;}
	public void minus(int x, int y) {this.x -= x; this.y -= y;}
	public void scale(double scale) {x *= scale; y *= scale;}
	public void scale(Vec2i scale) {x *= scale.x; y *= scale.y;}
	public void copy(Vec2i vec) {this.x = vec.x; this.y = vec.y;}
	public double dotProduct(Vec2i vec) {return UtilsMath.dotProduct(this, vec);}
	public void Vary(int v) {this.x += (UtilsMath.random(v * 2) - v); this.y += (UtilsMath.random(v * 2) - v);}
	
	public Vec2i vecDifference(Vec2i vector) {
		int rx = (this.x - vector.x) * (this.x - vector.x);
		int ry = (this.y - vector.y) * (this.y - vector.y);
		return new Vec2i(rx, ry);
	}
	
	public double intDifference(Vec2i vector) {
		int rx = (this.x - vector.x) * (this.x - vector.x);
		int ry = (this.y - vector.y) * (this.y - vector.y); 
		return (double) Math.sqrt(rx + ry);
	}
	
	//Vector Generation
	public Vec2i generateScalecInstance(int scale) {return generateScaledInstance(this, scale);}
	public static Vec2i generateScaledInstance(Vec2i vec, int scale) {return new Vec2i(vec.x * scale,vec.y * scale);}
	public static Vec2i generateRandomVectorVariance(Vec2i vec, int margin) {
		Vec2i result = new Vec2i(vec.x, vec.y);
		
		if(margin <= 0 ) {
			Console.err("Vec2d -> generateRandomVector() -> margin must be greater than 0");
			return result;
		}
		int rx = UtilsMath.random.nextInt(margin);
		int ry = UtilsMath.random.nextInt(margin);
		boolean xp = UtilsMath.random.nextBoolean();
		boolean yp = UtilsMath.random.nextBoolean();
		
		if(xp) vec.x += rx;
		else vec.x -= rx;			
		
		if(yp) vec.y += ry;
		else vec.y -= ry;
		
		
		return result;
	}
	
	public static Vec2i generateRandomVector(int xBound, int yBound) {
		Vec2i result = new Vec2i();
		result.x = UtilsMath.random.nextInt(xBound);
		result.y = UtilsMath.random.nextInt(yBound);
		return result;		
	}
	
	public static Vec2i generateRandomVector(int xMin,int xMax, int yMin, int yMax) {
		Vec2i result = new Vec2i();
		result.x = UtilsMath.random.nextInt(xMin, xMax);
		result.y = UtilsMath.random.nextInt(yMin, yMax);
		return result;		
	}
	
	public static Vec2i generateRandomBaseVector() {
		Vec2i result = new Vec2i();
		Vec2i r = generateRandomVector(3,3);
		
		if(r.x == 0) 	  result.x = -1;
		else if(r.x == 1) result.x =  1;
		else 			  result.x =  0;
		
		if(r.y == 0)	  result.y = -1;
		else if(r.y == 1) result.y =  1;
		else 			  result.y =  0;
	
		return result;
	}
	
	public Vec2i generateClone() {
		return new Vec2i(x, y);
	}
	
	//Vector conversion
	public static Vec2i convertVec3ToVec2(Vec3i original) {
		Vec2i result = new Vec2i();
		result.x = original.x;
		result.y = original.y;
		return result;
	}
	
	public Vec2d convert2iTo2d() {return convert2iTo2d(this);}
	public static Vec2d convert2iTo2d(Vec2i original) {
		return new Vec2d((double)original.x, (double)original.y);
	}

	//Static vector operations
	//Returns a new result vector without changing the input vectors

	public static Vec2i addVectors(Vec2i vector1, Vec2i vector2) {
		Vec2i result = new Vec2i();
		result.x = vector1.x + vector2.x;
		result.y = vector1.y + vector2.y;
		return result;
	}
	
	
	public static Vec2i subtractVectors(Vec2i vector1, Vec2i vector2) {
		Vec2i result = new Vec2i();
		result.x = vector1.x - vector2.x;
		result.y = vector1.y - vector2.y;
		return result;
	}
	

	public double difference(Vec2i vector) {
		int rx = (this.x - vector.x) * (this.x - vector.x);
		int ry = (this.y - vector.y) * (this.y - vector.y);  
		return Math.sqrt(rx  +  ry);
	}
	
	public static double difference(Vec2i vector1, Vec2i vector2) {
		int rx = (vector1.x - vector2.x) * (vector1.x - vector2.x);
		int ry = (vector1.y - vector2.y) * (vector1.y - vector2.y);  
		return Math.sqrt(rx  +  ry);		
	}
	
	public boolean isEqual(Vec2i vec) {return isEqual(vec.x, vec.y);}
	public boolean isEqual(int x, int y) {if(this.x == x && this.y == y) return true; else return false;}
	
	
	
		
	
}
