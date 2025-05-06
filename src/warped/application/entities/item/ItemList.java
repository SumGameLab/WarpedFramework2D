/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.entities.item;

import java.util.ArrayList;
import java.util.List;

import warped.application.state.WarpedInventory;
import warped.utilities.utils.Console;

public class ItemList<T extends ItemBindable<?>> {
	
	private List<T> 	  itemTypes;
	private List<Integer> itemQuantities;
	
	/**A list of item types and their quantities
	 * @param types - a list of the item types
	 * @param quantities - the quantity for each item type
	 * @apiNote each type specified must specify a corresponding quantity. (the size of types and quantities must be equal)
	 * @author 5som3 */
	public ItemList(List<T> types, List<Integer> quantities) {
		for(int i = 0; i < quantities.size(); i++) {
			if(quantities.get(i) <= 0) {
				Console.err("ItemList -> all quantities must be 1 or greater");
				quantities.set(i, 1);
			}
		}
		
		if(types.size() != quantities.size()) {
			Console.err("ItemList -> constructor() -> itemTypes and itemValues are not the same size");
			return;
		}
		
		if(types.size() == 0) {
			Console.err("ItemCost() -> constructor() -> itemTypes and itemValues are empty, item cost must be greater than 0");
			return;
		}
		
		this.itemTypes = types;
		this.itemQuantities = quantities;		
	}
	
	/**A list of item types and their quantities
	 * @param types - a list of the item types
	 * @param quantities - the quantity for each item type
	 * @param If 
	 * @author 5som3 */
	public ItemList(List<T> types, int... quantities) {	
		this.itemTypes = types;
		this.itemQuantities = new ArrayList<>();
		if(quantities.length > types.size()) Console.err("ItemList -> ItemList() -> more quantities specified than types, extra quantities will be ignored");
		for(int i = 0; i < types.size(); i++) {
			if(i >= quantities.length) itemQuantities.add(1);
			else if(quantities[i] <= 0) {
				Console.err("ItemList -> ItemList() -> all quantities must be 1 or greater");
				itemQuantities.add(1);
			} else itemQuantities.add(quantities[i]);
		}		
	}
	
	/**List all the items in the specified inventory
	 * @author 5som3*/
	public ItemList(WarpedInventory<T> invent) {
		for(int i = 0; i < invent.getMemberCount(); i++) {
			WarpedItem<T> item = invent.getMember(i);
			if(addType(item.getItemType())) itemQuantities.add(item.getQuantity());
			else addQuantity(item.getItemType(), item.getQuantity());
		}
	}
	
	/**A new copy another ItemList 
	 * @author 5som3*/
	public ItemList(ItemList<T> copyList) {		
		ArrayList<T> copyTypes = new ArrayList<>();
		ArrayList<Integer> copyQuantities = new ArrayList<>();
		
		for(int i = 0; i < copyList.size(); i++) {
			copyTypes.add(copyList.getType(i));
			copyQuantities.add(copyList.getQuantity(i));
		}
		
		this.itemTypes = copyTypes;
		this.itemQuantities = copyQuantities;
	}
	
	/**Remove an entry from the list
	 * @param index - the index of the entry to remove.
	 * @author 5som3*/
	public void removeEntry(int index) {
		if(index < 0 || index >= size()) {
			Console.err("ItemList -> removeEntry() -> index out of bounds : " + index);
			return;
		}
		
		itemTypes.remove(index);
		itemQuantities.remove(index);
	}
	
	/**Get the types of items in the list
	 * @return List<E> - a list of the types
	 * @author 5som3*/
	public List<T> getTypes() {return itemTypes;}
	
	/**Add a type that doesn't already exist in the list
	 * @param type - the type of item to add to the list
	 * @return boolean - true if the type was added. false if the type already exist in the list
	 * @author 5som3*/
	public boolean addType(T type) {
		if(itemTypes.contains(type)) return false;
		else {
			itemTypes.add(type);
			return true;
		}
	}
	
	/**Get the quantities of the items in the list.
	 * @return List<Integer> - a list of the quantities for each type
	 * @author 5som3*/
	public List<Integer> getQuantitys(){return itemQuantities;}
	
	/**The number of item types in the list.
	 * @return int - the number of types in the list 
	 * @author 5som3*/
	public int size() {return itemTypes.size();}
	
	/**Get the type at the specified index.
	 * @param index - the index of the type to get.
	 * @return T - the type of item at the index.
	 * @author 5som3*/
	public T getType(int index) {
		if(index < 0 || index >= itemTypes.size()) {
			Console.err("ItemList -> getCostType() -> index out of bound : " + index);
			return null;
		}
		return itemTypes.get(index);
	}
	
	/**Get the quantity at for the type at the specified index
	 * @param index - the index of the type to check
	 * @return integer - the quantity 
	 * @author 5som3*/
	public Integer getQuantity(int index) {
		if(index < 0 || index >= itemTypes.size()) {
			Console.err("ItemList -> getCostValue() -> index out of bound : " + index);
			return null;
		}
		return itemQuantities.get(index);
	}
	
	/**Add to the quantity of the specified item type.
	 * @param type - the type of item to add the quantity too.
	 * @param quantity - the quantity to add to the specified type.
	 * @return boolean - true if the quantity was added else false
	 * @apiNote If the type does not already exist in the list no quantity will be added
	 * @author 5som3*/
	public boolean addQuantity(T type, int quantity) {
		for(int i = 0; i < itemTypes.size(); i++) {
			if(itemTypes.get(i) == type) {
				itemQuantities.set(i, itemQuantities.get(i) + quantity);
				return true;
			}
		}
		Console.err("ItemList -> addQuantity() -> list does not contain type : " + type);
		return false;
	}
	
	/**Set the quantity of the specified item type in this list
	 * @param type - the type of item to set the quantity of.
	 * @param quantity - the quantity to set the specified type to
	 * @apiNote If the list does not already contain the item then no change will occur.
	 * @author 5som3*/
	public boolean setQuantity(T type, int quantity) {	
		for(int i = 0; i < itemTypes.size(); i++) {
			if(itemTypes.get(i) == type) {
				itemQuantities.set(i, quantity);
				return true;
			}
		}
		Console.err("ItemList -> setQuantity() -> list does not contain type : " + type);
		return false;
	}
	
	/**Set the quantity at the specified index
	 * @param index - the index of the quantity to set.
	 * @param value - the value to set at the specified index.
	 * @author 5som3*/
	public void setQuantity(int index, int value) {
		if(index < 0 || index >= itemTypes.size()) {
			Console.err("ItemList -> getCostValue() -> index out of bound : " + index);
			return;
		}
		
		if(value < 0) {
			Console.err("ItemList -> setCostValue() -> value must be at least 1 : " + value + ", it will be set to 1.");
			value = 1;
		}
		
		itemQuantities.set(index, value);
	}


	
}
