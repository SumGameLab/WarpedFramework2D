/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.matrices;

import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class MatrixI {

	private int[][] matrix;
	private VectorI matSize = new VectorI();
	
	public MatrixI(int size) {
		matSize = new VectorI(size,size);
		matrix = new int[size][size];
	}
	public MatrixI(int m, int n) {
		matSize = new VectorI(m,n);
		matrix = new int[m][n];		
	}
	public MatrixI(VectorI size) {
		matSize = new VectorI(size.x(),size.y());
		matrix = new int[size.x()][size.y()];
	}
	
	// Getters and Setters
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
		matSize.set(matrix.length, matrix[0].length);
	}
	public VectorI getSize() {return matSize;}
	public int getM() {return matSize.x();}	// get number of rows
	public int getN() {return matSize.y();}	// get number of columns
	public void setValue(int m, int n, int value) {matrix[m][n] = value;} // set specific value in matrix
	public int getValue(int m, int n) {return matrix[m][n];} // get specific value from matrix
	public void zero() {	// sets entire matrix to zero
		for(int m = 0; m < getM(); m++) {
			for(int n = 0; n < getN(); n++) {
				matrix[m][n] = 0;
			}
		}
	}
	public void set(int value) { // sets entire matrix to value
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
	public static boolean canMultiply(MatrixI mat1, MatrixI mat2) {if(mat1.getN() == mat2.getM()) return true;	return false;}

	// Instance methods, 
	//Modifies this instance when called 
	public void add(MatrixI mat) {
		if(!getSize().isEqual(mat.getSize())) {
			Console.err("MatrixInt -> tried to add matricies of inconsistent size :"); 
			Console.ln(this.getSize().getString());
			Console.ln(mat.getSize().getString());
			return;
		}
		for(int m = 0; m < mat.getM(); m++) {
			for(int n =0; n< mat.getN(); n++) {
				matrix[m][n] += mat.getValue(m, n);
			}
		}
	}
	public void subtract(MatrixI mat) {
		if(!getSize().isEqual(mat.getSize())) {
			Console.err("MatrixInt -> tried to add matricies of inconsistent size :"); 
			Console.ln(this.getSize().getString());
			Console.ln(mat.getSize().getString());
			return;
		}
		for(int m = 0; m < mat.getM(); m++) {
			for(int n =0; n< mat.getN(); n++) {
				matrix[m][n] -= mat.getValue(m, n);
			}
		}
	}
	
	public void multiply(MatrixI mat) {
		if(!canMultiply(this,mat)) {
			Console.err("MatrixInt -> can't multiply matricies because they are incompatible -> Matrix Size : ");
			Console.ln(this.getSize().getString());
			Console.ln(mat.getSize().getString());
			return;
		}
		matrix = MatrixI.multiplicationRaw(this, mat);		
	}
	
	// Static Functions
	// General use Matrix Operations for WarpedFramework 
	// Returns a new matrix leaving old data unchanged
	public static MatrixI addition(MatrixI mat1, MatrixI mat2) { // Matrix 1 + matrix 2
		if(!mat1.getSize().isEqual(mat2.getSize())) {
			Console.err("MatrixInt -> tried to add matricies of inconsistent size :"); 
			Console.ln(mat1.getSize().getString());
			Console.ln(mat2.getSize().getString());
			return null;
		}
		MatrixI result = new MatrixI(mat1.getSize());
		for(int m = 0; m < mat1.getSize().x(); m++) {
			for(int n = 0; n < mat1.getSize().y(); n++) {
				result.setValue(m, n, mat1.getValue(m, n) + mat2.getValue(m, n)) ;
			}
		}
		return result;	 
	}
	public static MatrixI subtraction(MatrixI mat1, MatrixI mat2) { //Matrix 1 - Matrix 2 NOTE Matrix 1 - Matrix 2 != Matrix 2 - Matrix 1
		if(!mat1.getSize().isEqual(mat2.getSize())) {
			Console.err("MatrixInt -> tried to add matricies of inconsistent size :"); 
			Console.ln(mat1.getSize().getString());
			Console.ln(mat2.getSize().getString());
			return null;
		}
		MatrixI result = new MatrixI(mat1.getSize());
		result = MatrixI.copy(mat1);
		result.add(MatrixI.negative(mat2));
		return result;
	}
	
	public static MatrixI multiplication(VectorI vec, MatrixI mat) {
		if(vec.length() != mat.getM()) {
			Console.err("MatrixInt -> can't multiply VectorI with matrix because they are incompatible -> Matrix Size : ");
			Console.ln(mat.getSize().getString()); 
			return null;
		}
		MatrixI result = new MatrixI(vec.length(), mat.getN());
		for(int m = 0; m < result.getM(); m++) {
			for(int n = 0; n < result.getN(); n++) {
				result.setValue(m, n, productVectorIN(vec, mat, n));
			}
		}
		return result;
	}

	
	
	public static MatrixI multiplication(MatrixI mat1, MatrixI mat2) {
		if(!canMultiply(mat1,mat2)) {
			Console.err("MatrixInt -> can't multiply matricies because they are incompatible");
			Console.ln(mat1.getSize().getString());
			Console.ln(mat2.getSize().getString());
			return null;
		}
		MatrixI result = new MatrixI(mat1.getM(), mat2.getN()); // create matrix of resulting matrix size to return
		for(int m = 0; m < result.getM(); m++) { 								
			for(int n = 0; n < result.getN(); n++) {					// go through each value in result matrix and set it		
				result.setValue(m, n, productMN(mat1, m, mat2, n));				
			}
		}
		return result;
	}
	
	public static MatrixI negative(MatrixI mat) {
		MatrixI result = new MatrixI(mat.getM(), mat.getN());
		for(int m = 0; m < mat.getM(); m++) {
			for(int n = 0; n < mat.getN(); n++) {
				result.setValue(m, n, -mat.getValue(m, n));
			}
		}
		return result;
	}
	
	public static MatrixI copy(MatrixI mat) {
		MatrixI result = new MatrixI(mat.getM(), mat.getN());
		for(int m = 0; m < mat.getM(); m++) {
			for(int n = 0; n < mat.getN(); n++) {
				result.setValue(m, n, mat.getValue(m, n));
			}
		}
		return result;
	}

	public static MatrixD convertToDouble(MatrixI mat) {
		MatrixD result = new MatrixD(mat.getM(), mat.getN());
		for(int m = 0; m < mat.getM(); m++) {
			for(int n = 0; n < mat.getN(); n++) {
				result.setValue(m, n, (double)mat.getValue(m, n));
			}
		}
		return result;
	}
	
	//// PRIAVTE METHODS - Returns primitive data type not class -> to be used internally only!
	//returns a new value containing the result doesn't modify input values	
	private static int[][] multiplicationRaw(MatrixI mat1, MatrixI mat2){ // returns actual matrix rather than matrix class object
		if(!canMultiply(mat1,mat2)) {
			Console.err("MatrixInt -> can't multiply matricies because they are incompatible");
			Console.ln(mat1.getSize().getString());
			Console.ln(mat2.getSize().getString());
			return null;
		}
		int[][] result = new int[mat1.getM()][mat2.getN()];
		for(int m = 0; m < mat1.getM(); m++) { 								
			for(int n = 0; n < mat2.getN(); n++) {							
				result[m][n] = productMN(mat1, m, mat2, n);				
			}
		}
		return result;
	}
	
	private static int productMN(MatrixI mat1, int m, MatrixI mat2, int n) {	//CHECK -> canMultiply() before using productMN
		int result = 0;
		for(int p = 0; p < mat1.getN(); p++) {
			result += mat1.getValue(m, p) * mat2.getValue(p, n); // multiply a select row of matrix 1 with a select column of matrix 2
		}
		return result;
	}
	
	private static int productVectorIN(VectorI vec, MatrixI mat, int n) {
		int result = 0;
		for(int p = 0; p < vec.length(); p++) {
			result += vec.get(p) * mat.getValue(p, n);
		}
		return result;
	}

	
	
	
}
