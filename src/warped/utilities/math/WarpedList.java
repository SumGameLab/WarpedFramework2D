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

	
	/**Add a list of values to the list
	 * @param values - a list of the values to add.
	 * @author 5som3*/
	public void add(List<T> values) {add(this, values);}
	
	/**Add a list of values to the list
	 * @param values - a list of the values to add.
	 * @author 5som3*/
	public static <K> void add(ArrayList<K> list, List<K> values) {for(int i = 0; i < values.size(); i++) list.add(values.get(i));}
	
	/**Add a list of values to the list
	 * @param values - a list of the values to add.
	 * @author 5som3*/
	public void add(@SuppressWarnings("unchecked") T... values) {add(this, values);}
	
	/**Add a list of values to the list
	 * @param values - a list of the values to add.
	 * @author 5som3*/
	public static <K> void add(ArrayList<K> list, @SuppressWarnings("unchecked") K... values) {for(int i = 0; i < values.length; i++) list.add(values[i]);}
	
	/**Get an array list containing multiple components.
	 * @param indices - a list of indices of the components to get.
	 * @return ArrayList<T> - an array list of the specified components. 
	 * @apiNote Will return empty array if indices contains an out of bounds index.
	 * @author 5som3*/
	public ArrayList<T> get(List<Integer> indices){return get(this, indices);}
	
	/**Get an array list containing multiple components.
	 * @param indices - a list of indices of the components to get.
	 * @return ArrayList<T> - an array list of the specified components. 
	 * @apiNote Will return empty array if indices contains an out of bounds index.
	 * @author 5som3*/
	public static <K> ArrayList<K> get(ArrayList<K> list, List<Integer> indices) {
		ArrayList<K> result = new ArrayList<K>();
		
		for(int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			if(index < 0 || index >= list.size()) { 
				Console.err("WarpedList -> get() -> one of the indices passed is out of bounds (position, value) : ( " + i + ", " + index + ")");
				return result;
			}
		}
		
		for(int i = 0; i < indices.size(); i++) result.add(list.get(indices.get(i)));
		return result;
		
	}
	
	/**Get an array list containing multiple components.
	 * @param indices - a list of indices of the components to get.
	 * @return ArrayList<T> - an array list of the specified components. 
	 * @apiNote Will return empty array if indices contains an out of bounds index.
	 * @author 5som3*/
	public ArrayList<T> get(int... indices){return get(this, indices);}
	
	/**Get an array list containing multiple components.
	 * @param indices - a list of indices of the components to get.
	 * @return ArrayList<T> - an array list of the specified components. 
	 * @apiNote Will return empty array if indices contains an out of bounds index.
	 * @author 5som3*/
	public static <K> ArrayList<K> get(ArrayList<K> list, int... indices) {
		ArrayList<K> result = new ArrayList<K>();
		
		for(int i = 0; i < indices.length; i++) {
			int index = indices[i];
			if(index < 0 || index >= list.size()) { 
				Console.err("WarpedList -> get() -> one of the indices passed is out of bounds (position, value) : ( " + i + ", " + index + ")");
				return result;
			}
			
		}
		
		for(int i = 0; i < indices.length; i++) result.add(list.get(indices[i]));
		return result;		
	}
	
	
	/**Count the number of times the value appears in the array
	 * @param value - the value to check for.
	 * @return int - the number of times the value appears in the array
	 * @author 5som3*/
	public int count(T value) {return count(this, value);}
	
	/**Count the number of times the value appears in the array
	 * @param value - the value to check for.
	 * @return int - the number of times the value appears in the array
	 * @author 5som3*/
	public static <K> int count(ArrayList<K> list, K value) {
		int result = 0;
		for(int i = 0; i < list.size(); i++) if(list.get(i) == value) result++;
		return result;
	}
	
	
	
	
}
