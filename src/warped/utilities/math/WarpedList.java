/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math;

import java.util.ArrayList;
import java.util.List;

import warped.utilities.utils.Console;

public class WarpedList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public void add(List<T> values) {for(int i = 0; i < values.size(); i++) add(values.get(i));}
	
	public ArrayList<T> get(List<Integer> indices){
		ArrayList<T> result = new ArrayList<T>();
		
		for(int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			if(index < 0 || index >= this.size()) { 
				Console.err("WarpedList -> get() -> one of the indices passed is out of bounds (position, value) : ( " + i + ", " + index + ")");
				return result;
			}
		}
		
		for(int i = 0; i < indices.size(); i++) result.add(get(indices.get(i)));
		return result;
	}
	
	public ArrayList<T> get(int... indices){
		ArrayList<T> result = new ArrayList<T>();
		
		for(int i = 0; i < indices.length; i++) {
			int index = indices[i];
			if(index < 0 || index >= this.size()) { 
				Console.err("WarpedList -> get() -> one of the indices passed is out of bounds (position, value) : ( " + i + ", " + index + ")");
				return result;
			}
			
		}
		
		for(int i = 0; i < indices.length; i++) result.add(get(indices[i]));
		return result;
	}
	
	public boolean containsOnly(T value) {
		for(int i = 0; i < size(); i++) {if(get(i) != value) return false;}
		return true;
	}
	
}
