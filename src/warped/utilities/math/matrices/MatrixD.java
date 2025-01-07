/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.matrices;

import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.math.vectors.Vec3d;
import warped.utilities.utils.Console;

public class MatrixD {

	private double[][] matrix;
	private Vec2i matSize = new Vec2i();
	
	public MatrixD(int size) {
		matSize = new Vec2i(size,size);
		matrix = new double[size][size];
	}
	public MatrixD(int m, int n) {
		matSize = new Vec2i(m,n);
		matrix = new double[m][n];		
	}
	public MatrixD(Vec2i size) {
		matSize = new Vec2i(size.x,size.y);
		matrix = new double[size.x][size.y];
	}
	
	//Basic Matrix Operation
	// Getters and Setters
	public Vec2i getSize() {Vec2i result = new Vec2i(); result.x = matSize.x; result.y = matSize.y; return result;}
	public int getM() {return matSize.x;}	// get number of rows
	public int getN() {return matSize.y;}	// get number of columns
	public void setValue(int m, int n, double value) {matrix[m][n] = value;} // set specific value in matrix
	public double getValue(int m, int n) {return matrix[m][n];} // get specific value from matrix
	public void zero() {	// sets entire matrix to zero
		for(int m = 0; m < getM(); m++) {
			for(int n = 0; n < getN(); n++) {
				matrix[m][n] = 0.0;
			}
		}
	}
	public void setMatrix(double [][] matrix) { // set int[][] to a new int [][] and adjusts size accordingly;
		matSize.x = matrix.length;
		matSize.y = matrix[0].length;
		this.matrix = matrix;
	}
	public void set(double value) { // sets entire matrix to value
		for(int m = 0; m < getM(); m++) {
			for(int n = 0; n < getN(); n++) {
				matrix[m][n] = value;
				
			}
		}
	}
	public void println() {
		Console.ln("");
		for(int m = 0; m < getM(); m++) {
			System.out.print(" [");
			for(int n = 0; n < getN(); n++) {
				if(n == 0) System.out.print(getValue(m,n));
				else System.out.print(", " + getValue(m,n));
			}
			System.out.print("] ");
			Console.ln("");
		}
	}
	
	///// Logical Checks
	public static boolean canMultiply(MatrixD mat1, MatrixD mat2) {if(mat1.getN() == mat2.getM()) return true;	return false;}

	// Instance methods, 
	//Modifies this instance when called 
	public void add(MatrixD mat) {
		if(!getSize().isEqual(mat.getSize())) {
			Console.err("MatrixDouble -> tried to add matricies of inconsistent size :"); 
			mat.getSize().println();
			mat.getSize().println();
			return;
		}
		for(int m = 0; m < mat.getM(); m++) {
			for(int n =0; n< mat.getN(); n++) {
				matrix[m][n] += mat.getValue(m, n);
			}
		}
	}
	
	public void subtract(MatrixD mat) {
		if(!getSize().isEqual(mat.getSize())) {
			Console.err("MatrixDouble -> tried to add matricies of inconsistent size :"); 
			mat.getSize().println();
			mat.getSize().println();
			return;
		}
		for(int m = 0; m < mat.getM(); m++) {
			for(int n =0; n< mat.getN(); n++) {
				matrix[m][n] -= mat.getValue(m, n);
			}
		}
	}
	
	public void multiply(MatrixD mat) {
		if(!canMultiply(this,mat)) {
			Console.err("MatrixDouble -> can't multiply matricies because they are incompatible -> Matrix Size : ");
			this.getSize().println();
			mat.getSize().println();
			return;
		}
		matrix = MatrixD.multiplicationRaw(this, mat);		
	}
	
	// Static Functions
	// General use Matrix Operations for WarpedFramework 
	// Returns a new matrix leaving old data unchanged
	public static MatrixD addition(MatrixD mat1, MatrixD mat2) { // Matrix 1 + matrix 2
		if(!mat1.getSize().isEqual(mat2.getSize())) {
			Console.err("MatrixDouble -> tried to add matricies of inconsistent size :"); 
			mat1.getSize().println();
			mat2.getSize().println();
			return null;
		}
		MatrixD result = new MatrixD(mat1.getSize());
		for(int m = 0; m < mat1.getSize().x; m++) {
			for(int n = 0; n < mat1.getSize().y; n++) {
				result.setValue(m, n, mat1.getValue(m, n) + mat2.getValue(m, n)) ;
			}
		}
		return result;	 
	}
	
	public static MatrixD subtraction(MatrixD mat1, MatrixD mat2) { //Matrix 1 - Matrix 2 NOTE Matrix 1 - Matrix 2 != Matrix 2 - Matrix 1
		if(!mat1.getSize().isEqual(mat2.getSize())) {
			Console.err("MatrixDouble -> tried to add matricies of inconsistent size :"); 
			mat1.getSize().println();
			mat2.getSize().println();
			return null;
		}
		MatrixD result = new MatrixD(mat1.getSize());
		result = MatrixD.copy(mat1);
		result.add(MatrixD.negative(mat2));
		return result;
	}
	
	public static MatrixD multiplication(Vec2d vec, MatrixD mat) {
		if(Vec2d.getN() != mat.getM()) {
			Console.err("MatrixDouble -> can't multiply Vec2d with matrix because they are incompatible -> Matrix Size : ");
			mat.getSize().println();
			return null;
		}
		MatrixD result = new MatrixD(Vec2d.getM(), mat.getN());
		for(int m = 0; m < result.getM(); m++) {
			for(int n = 0; n < result.getN(); n++) {
				result.setValue(m, n, productVec2dN(vec, mat, n));
			}
		}
		return result;
	}
	
	public static MatrixD multiplication(Vec3d vec, MatrixD mat) {
		if(Vec3d.getN() != mat.getM()) {
			Console.err("MatrixDouble -> can't multiply Vec3d with matrix because they are incompatible -> Matrix Size : ");
			mat.getSize().println();
			return null;
		}
		MatrixD result = new MatrixD(Vec3d.getM(), mat.getN());
		for(int m = 0; m < result.getM(); m++) {
			for(int n = 0; n < result.getN(); n++) {
				result.setValue(m, n, productVec3dN(vec,mat,n));
			}
		}
		return result;
	}
		
	public static MatrixD multiplication(MatrixD mat1, MatrixD mat2) {
		if(!canMultiply(mat1,mat2)) {
			Console.err("MatrixDouble -> can't multiply matricies because they are incompatible");
			mat1.getSize().println();
			mat2.getSize().println();
			return null;
		}
		MatrixD result = new MatrixD(mat1.getM(), mat2.getN()); // create matrix of resulting matrix size to return
		for(int m = 0; m < result.getM(); m++) { 								
			for(int n = 0; n < result.getN(); n++) {					// go through each value in result matrix and set it		
				result.setValue(m, n, productMN(mat1, m, mat2, n));				
			}
		}
		return result;
	}
	
	public static MatrixD negative(MatrixD mat) {
		MatrixD result = new MatrixD(mat.getM(), mat.getN());
		for(int m = 0; m < mat.getM(); m++) {
			for(int n = 0; n < mat.getN(); n++) {
				result.setValue(m, n, -mat.getValue(m, n));
			}
		}
		return result;
	}
	
	public static MatrixD copy(MatrixD mat) {
		MatrixD result = new MatrixD(mat.getM(), mat.getN());
		for(int m = 0; m < mat.getM(); m++) {
			for(int n = 0; n < mat.getN(); n++) {
				result.setValue(m, n, mat.getValue(m, n));
			}
		}
		return result;
	}

	
	
	//// PRIAVTE METHODS - Returns primitive data type not class -> to be used internally only!
	//returns a new value containing the result doesn't modify input values	
	private static double[][] multiplicationRaw(MatrixD mat1, MatrixD mat2){ // returns actual matrix rather than matrix class object
		if(!canMultiply(mat1,mat2)) {
			Console.err("MatrixDouble -> can't multiply matricies because they are incompatible");
			mat1.getSize().println();
			mat2.getSize().println();
			return null;
		}
		double[][] result = new double[mat1.getM()][mat2.getN()];
		for(int m = 0; m < mat1.getM(); m++) { 								
			for(int n = 0; n < mat2.getN(); n++) {							
				result[m][n] = productMN(mat1, m, mat2, n);				
			}
		}
		return result;
	}
	
	private static double productMN(MatrixD mat1, int m, MatrixD mat2, int n) {	//CHECK -> canMultiply() before using productMN
		double result = 0;
		for(int p = 0; p < mat1.getN(); p++) {
			result += mat1.getValue(m, p) * mat2.getValue(p, n); // multiply a select row of matrix 1 with a select column of matrix 2
		}
		return result;
	}
	
	private static double productVec2dN(Vec2d vec, MatrixD mat, int n) {
		double result = 0;
		for(int p = 0; p < Vec2d.getN(); p++) {
			result += vec.getValue(p) * mat.getValue(p, n);
		}
		return result;
	}
	private static double productVec3dN(Vec3d vec, MatrixD mat, int n) {
		double result = 0;
		for(int p = 0; p < Vec3d.getN(); p++) {
			result += vec.getValue(p) * mat.getValue(p, n);
		}
		return result;
	}
	
	
}
