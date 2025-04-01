/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.entities.item;

import java.util.ArrayList;
import java.util.List;

import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;

public class ItemList<E extends ItemBindable<?>> {
	
	private List<E> 	  itemTypes;
	private List<Integer> itemQuantitys;
	
	/**If possible, use constructor with ItemType parameter, it's slightly faster*/
	public ItemList(List<E> costTypes, List<Integer> costValues) {
		if(costTypes.size() != costValues.size()) {
			System.err.println("ItemList -> constructor() -> itemTypes and itemValues are not the same size");
			return;
		}
		
		if(costTypes.size() == 0) {
			System.err.println("ItemCost() -> constructor() -> itemTypes and itemValues are empty, item cost must be greater than 0");
			return;
		}
	
		this.itemTypes = costTypes;
		this.itemQuantitys = costValues;		
	}
	
	public ItemList(ItemList<E> copyList) {		
		ArrayList<E> copyTypes = new ArrayList<>();
		ArrayList<Integer> copyQuantities = new ArrayList<>();
		
		for(int i = 0; i < copyList.size(); i++) {
			copyTypes.add(copyList.getCostType(i));
			copyQuantities.add(copyList.getItemQuantity(i));
		}
		
		this.itemTypes = copyTypes;
		this.itemQuantitys = copyQuantities;
	}
	
	public void removeEntry(int index) {
		if(index < 0 || index >= size()) {
			System.err.println("ItemList -> removeEntry() -> index out of bounds : " + index);
			return;
		}
		
		itemTypes.remove(index);
		itemQuantitys.remove(index);
	}
	
	
	public List<E> getItemTypes() {return itemTypes;}
	public List<Integer> getItemQuantitys(){return itemQuantitys;}
	public int size() {return itemTypes.size();}
	public E getCostType(int index) {
		if(index < 0 || index >= itemTypes.size()) {
			System.err.println("ItemList -> getCostType() -> index out of bound : " + index);
			return null;
		}
		return itemTypes.get(index);
	}
	
	public Integer getItemQuantity(int index) {
		if(index < 0 || index >= itemTypes.size()) {
			System.err.println("ItemList -> getCostValue() -> index out of bound : " + index);
			return null;
		}
		return itemQuantitys.get(index);
	}
	
	public void setItemQuantity(int index, int value) {
		if(index < 0 || index >= itemTypes.size()) {
			System.err.println("ItemList -> getCostValue() -> index out of bound : " + index);
			return;
		}
		
		if(value < 0) {
			System.err.println("ItemList -> setCostValue() -> value must be at least 1 : " + value + ", it will be set to 1.");
			value = 1;
		}
		
		itemQuantitys.set(index, value);
	}

	
	public boolean containsItemList(WarpedGroupIdentity supplieInventory) {return containsItemList(this, supplieInventory);}
	public static <K extends ItemBindable<?>> boolean containsItemList(ItemList<K> itemList, WarpedGroupIdentity supplieInventory) {		
		if(supplieInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> containsItemList() -> supplieInventory is not an contextGroup of items, it is : " + supplieInventory.getManagerType());
			return false;
		}
		
		for(int i = 0; i < itemList.size(); i++) {
			K costType  = itemList.getCostType(i);
			int costValue = itemList.getItemQuantity(i);
			
			ArrayList<WarpedItem<?>> supplies = WarpedState.itemManager.getGroup(supplieInventory).getMembers();
			for(int ii = 0; ii < supplies.size(); ii++) {
				WarpedItem<?> item = WarpedState.itemManager.getItem(supplieInventory, costType);
				if(item == null) return false;
				else if(item.getQuantity() < costValue) return false;
				else continue;
			}
		}
		return true;	
	}	
	
	public boolean consumeItemList(WarpedGroupIdentity supplieInventory) {return consumeItemList(this, supplieInventory);}
	public static <K extends ItemBindable<?>> boolean consumeItemList(ItemList<K> itemList, WarpedGroupIdentity supplieInventory) {
		if(supplieInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> consumeItemList() -> supplieInventory is not an contextGroup of items, it is : " + supplieInventory.getManagerType());
			return false;
		}
		
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem<?>> consumables 	 = new ArrayList<>();
		
		for(int i = 0; i < itemList.size(); i++) {
			K costType  = itemList.getCostType(i);
			int 	costValue = itemList.getItemQuantity(i);
			
			WarpedItem<?> item = WarpedState.itemManager.getItem(supplieInventory, costType);
			
			if(item == null) return false;
			else if(item.getQuantity() < costValue) return false;
			else {
				consumableIndices.add(i);
				consumables.add(item);
			}	
		}

		for(int j = 0; j < consumables.size(); j++) {
			consumables.get(j).consume(itemList.getItemQuantity(consumableIndices.get(j)));
		}
		return true;
	}
	
	public void produceItemList(WarpedGroupIdentity produceInventory) {produceItemList(this, produceInventory, 1.0);}
	public void produceItemList(WarpedGroupIdentity produceInventory, double scale) {produceItemList(this, produceInventory, scale);}
	public static <K extends ItemBindable<?>> void produceItemList(ItemList<K> itemList, WarpedGroupIdentity produceInventory) {
		if(produceInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> isAffordable() -> supplieInventory is not an contextGroup of items, it is : " + produceInventory.getManagerType());
			return;
		}
		
		for(int i = 0; i < itemList.size(); i++) {
			K itemType = itemList.getCostType(i);
			int itemValue = 0;
			
			itemValue = itemList.getItemQuantity(i);
			
			WarpedItem<K> item = itemType.generateItem(itemValue);
			WarpedState.itemManager.addItem(produceInventory, item);
		}
	}
	public static <K extends ItemBindable<?>> void produceItemList(ItemList<K> itemCost, WarpedGroupIdentity produceInventory, double scale) {
		if(scale <= 0 || scale >= 100) {
			System.err.println("ItemList -> produceItemCost() -> scale is out of bounds : " + scale);
			scale = 1.0;
		}
		
		if(produceInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> isAffordable() -> supplieInventory is not an contextGroup of items, it is : " + produceInventory.getManagerType());
			return;
		}
		
		for(int i = 0; i < itemCost.size(); i++) {
			K itemType = itemCost.getCostType(i);
			int itemValue = 0;
			
			if(scale != 1.0) itemValue = (int) (itemCost.getItemQuantity(i) * scale);
			else itemValue = itemCost.getItemQuantity(i);
			
			WarpedItem<K> item = itemType.generateItem(itemValue);
			WarpedState.itemManager.addItem(produceInventory, item);
		}
	}
	
	/**Will only transfer the items that are missing from the receiving inventory.
	 * 		i.e. if the ItemList calls to transfer 400 wood and the receiver already has 100 wood then only 300 will be transfered*/
	public boolean transferLimitedItemList(WarpedGroupIdentity giver, WarpedGroupIdentity taker) {return transferLimitedItemList(this, giver, taker);}
	/**Will only transfer the items that are missing from the receiving inventory.
	 * 		 i.e. if the ItemList calls to transfer 400 wood and the receiver already has 100 wood then only 300 will be transfered*/
	public static <K extends ItemBindable<?>> boolean transferLimitedItemList(ItemList<K> list, WarpedGroupIdentity giver, WarpedGroupIdentity taker) {
		if(giver.getManagerType() != WarpedManagerType.ITEM || taker.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> transferItemList() -> giver or taker is not an contextGroup of items, it is : " + giver.getManagerType() + ", " + taker.getManagerType());
			return false;
		}
		
		ItemList<K> itemList = new ItemList<>(list); // copy list so original list is not modified
		
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem<?>> consumables 	 = new ArrayList<>();
		
		for(int i = 0; i < itemList.size(); i++) {
			K costType  = itemList.getCostType(i);
			int costValue = itemList.getItemQuantity(i);
									
			WarpedItem<?> existing = WarpedState.itemManager.getItem(taker, costType);
			WarpedItem<?> item = WarpedState.itemManager.getItem(giver, costType);
			
			if(existing != null) {
				costValue -= existing.getQuantity();
				if(costValue <= 0) item = null;
			}
				
			if(item == null) {
				itemList.removeEntry(i);
				i--;
				continue;
				
			} else {
				if(item.getQuantity() < costValue) itemList.setItemQuantity(i, item.getQuantity());
				
				consumableIndices.add(i);
				consumables.add(item);
			}	
		}

		for(int j = 0; j < consumables.size(); j++) {
			consumables.get(j).consume(itemList.getItemQuantity(consumableIndices.get(j)));
		}
		produceItemList(itemList, taker);
		return true;		
	}
	
	
	/**Will only transfer items that are available in the giver.
	 * 		 i.e. if the List calls to transfer 400 wood but the giver only has 300 wood then 300 wood will be transfered instead of the asked 400*/
	public boolean transferItemList(WarpedGroupIdentity giver, WarpedGroupIdentity taker) {return transferItemList(this, giver, taker);}
	/**Will only transfer items that are available in the giver.
	 * 		 i.e. if the List calls to transfer 400 wood but the giver only has 300 wood then 300 wood will be transfered instead of the asked 400*/
	public static <K extends ItemBindable<?>> boolean transferItemList(ItemList<K> list, WarpedGroupIdentity giver, WarpedGroupIdentity taker) {
		if(giver.getManagerType() != WarpedManagerType.ITEM || taker.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> transferItemList() -> giver or taker is not an contextGroup of items, it is : " + giver.getManagerType() + ", " + taker.getManagerType());
			return false;
		}
		
		ItemList<K> itemList = new ItemList<>(list); // copy list so original list is not modified
		
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem<?>> consumables 	 = new ArrayList<>();
		
		for(int i = 0; i < itemList.size(); i++) {
			K costType  = itemList.getCostType(i);
			int 	costValue = itemList.getItemQuantity(i);
			
			WarpedItem<?> item = WarpedState.itemManager.getItem(giver, costType);
			
			if(item == null) {
				itemList.removeEntry(i);
				i--;
				continue;
			}
			else {
				if(item.getQuantity() < costValue) itemList.setItemQuantity(i, item.getQuantity());
				
				consumableIndices.add(i);
				consumables.add(item);
			}	
		}

		for(int j = 0; j < consumables.size(); j++) {
			consumables.get(j).consume(itemList.getItemQuantity(consumableIndices.get(j)));
		}
		produceItemList(itemList, taker);
		return true;
	}

	
}
