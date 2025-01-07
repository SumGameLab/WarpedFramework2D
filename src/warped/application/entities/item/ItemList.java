/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item;

import java.util.ArrayList;
import java.util.List;

import warped.application.entities.item.ammunition.AmmunitionType;
import warped.application.entities.item.artifact.ArtifactType;
import warped.application.entities.item.container.ContainerType;
import warped.application.entities.item.drink.DrinkType;
import warped.application.entities.item.element.ElementType;
import warped.application.entities.item.equipment.EquipmentType;
import warped.application.entities.item.food.FoodType;
import warped.application.entities.item.resource.ResourceType;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;

public class ItemList<E extends Enum<E>> {
	
	private ItemType 	  listType;
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
		
		listType = ItemType.getItemType(costTypes.get(0));
		if(listType == null) {
			System.err.println("ItemList -> constructor() -> the list itemTypes is not a valid list of enum types");
			return;
		}
	
		this.itemTypes = costTypes;
		this.itemQuantitys = costValues;		
	}
	
	public ItemList(List<E> costTypes, List<Integer> costValues, ItemType itemType) {
		if(costTypes.size() != costValues.size()) {
			System.err.println("ItemList -> constructor() -> itemTypes and itemValues are not the same size");
			return;
		}
		
		if(costTypes.size() == 0) {
			System.err.println("ItemCost() -> constructor() -> itemTypes and itemValues are empty, item cost must be greater than 0");
			return;
		}
		
		if(itemType == null) {
			System.err.println("ItemList -> constructor() -> the list itemTypes is not a valid list of enum types");
			return;
		}
	
		this.listType = itemType;
		this.itemTypes = costTypes;
		this.itemQuantitys = costValues;		
	}
	
	public ItemList(ItemList<E> copyList) {
		this.listType = copyList.listType;
		
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
	
	public ItemType getListType(){return listType;}
	
	public AmmunitionType getAmmunitionType(int index){
		if(listType != ItemType.AMMUNITION) {
			System.err.println("ItemList -> getAmmunitionType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (AmmunitionType) itemTypes.get(index);
	}
	
	public ArtifactType getArtifactType(int index){
		if(listType != ItemType.ARTIFACT) {
			System.err.println("ItemList -> getArtifactType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (ArtifactType) itemTypes.get(index);
	}
	
	public ContainerType getContainerType(int index){
		if(listType != ItemType.CONTAINER) {
			System.err.println("ItemCost -> getContainerType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (ContainerType) itemTypes.get(index);
	}
	
	public DrinkType getDrinkType(int index){
		if(listType != ItemType.DRINK) {
			System.err.println("ItemList -> getDrinkType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (DrinkType) itemTypes.get(index);
	}
	
	public ElementType getElementType(int index){
		if(listType != ItemType.ELEMENT) {
			System.err.println("ItemCost -> getElementType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (ElementType) itemTypes.get(index);
	}
	
	public EquipmentType getEquipmentType(int index){
		if(listType != ItemType.EQUIPMENT) {
			System.err.println("ItemList -> getEquipmentType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (EquipmentType) itemTypes.get(index);
	}
	
	public FoodType getFoodType(int index){
		if(listType != ItemType.FOOD) {
			System.err.println("ItemList -> getFoodType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (FoodType) itemTypes.get(index);
	}
	
	public ResourceType getResourceType(int index){
		if(listType != ItemType.RESOURCE) {
			System.err.println("ItemList -> getResourceType() -> type cast exception, type is : " + listType);
			return null;
		}
		return (ResourceType) itemTypes.get(index);
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
	public static boolean containsItemList(ItemList<?> itemList, WarpedGroupIdentity supplieInventory) {		
		if(supplieInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> containsItemList() -> supplieInventory is not an contextGroup of items, it is : " + supplieInventory.getManagerType());
			return false;
		}
		
		for(int i = 0; i < itemList.size(); i++) {
			Enum<?> costType  = itemList.getCostType(i);
			int 	costValue = itemList.getItemQuantity(i);
			
			ArrayList<WarpedItem> supplies = WarpedState.itemManager.getGroupMembers(supplieInventory);
			for(int ii = 0; ii < supplies.size(); ii++) {
				WarpedItem item = WarpedState.itemManager.getItem(supplieInventory, costType);
				if(item == null) return false;
				else if(item.getQuantity() < costValue) return false;
				else continue;
			}
		}
		return true;	
	}	
	
	public boolean consumeItemList(WarpedGroupIdentity supplieInventory) {return consumeItemList(this, supplieInventory);}
	public static boolean consumeItemList(ItemList<?> itemList, WarpedGroupIdentity supplieInventory) {
		if(supplieInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> consumeItemList() -> supplieInventory is not an contextGroup of items, it is : " + supplieInventory.getManagerType());
			return false;
		}
		
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem> consumables 	 = new ArrayList<>();
		
		for(int i = 0; i < itemList.size(); i++) {
			Enum<?> costType  = itemList.getCostType(i);
			int 	costValue = itemList.getItemQuantity(i);
			
			WarpedItem item = WarpedState.itemManager.getItem(supplieInventory, costType);
			
			if(item == null) return false;
			else if(item.getQuantity() < costValue) return false;
			else {
				consumableIndices.add(i);
				consumables.add(item);
			}	
		}

		for(int j = 0; j < consumables.size(); j++) {
			consumables.get(j).consumeMultiple(itemList.getItemQuantity(consumableIndices.get(j)));
		}
		return true;
	}
	
	public void produceItemList(WarpedGroupIdentity produceInventory) {produceItemList(this, produceInventory, 1.0);}
	public void produceItemList(WarpedGroupIdentity produceInventory, double scale) {produceItemList(this, produceInventory, scale);}
	public static void produceItemList(ItemList<?> itemList, WarpedGroupIdentity produceInventory) {
		if(produceInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> isAffordable() -> supplieInventory is not an contextGroup of items, it is : " + produceInventory.getManagerType());
			return;
		}
		
		for(int i = 0; i < itemList.size(); i++) {
			Enum<?> itemType = itemList.getCostType(i);
			int itemValue = 0;
			
			itemValue = itemList.getItemQuantity(i);
			
			WarpedItem item = ItemType.generateItem(itemType, itemValue);
			WarpedState.itemManager.addItem(produceInventory, item);
		}
	}
	public static void produceItemList(ItemList<?> itemCost, WarpedGroupIdentity produceInventory, double scale) {
		if(scale <= 0 || scale >= 100) {
			System.err.println("ItemList -> produceItemCost() -> scale is out of bounds : " + scale);
			scale = 1.0;
		}
		
		if(produceInventory.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> isAffordable() -> supplieInventory is not an contextGroup of items, it is : " + produceInventory.getManagerType());
			return;
		}
		
		for(int i = 0; i < itemCost.size(); i++) {
			Enum<?> itemType = itemCost.getCostType(i);
			int itemValue = 0;
			
			if(scale != 1.0) itemValue = (int) (itemCost.getItemQuantity(i) * scale);
			else itemValue = itemCost.getItemQuantity(i);
			
			WarpedItem item = ItemType.generateItem(itemType, itemValue);
			WarpedState.itemManager.addItem(produceInventory, item);
		}
	}
	
	/**Will only transfer the items that are missing from the receiving inventory.
	 * 		i.e. if the ItemList calls to transfer 400 wood and the receiver already has 100 wood then only 300 will be transfered*/
	public boolean transferLimitedItemList(WarpedGroupIdentity giver, WarpedGroupIdentity taker) {return transferLimitedItemList(this, giver, taker);}
	/**Will only transfer the items that are missing from the receiving inventory.
	 * 		 i.e. if the ItemList calls to transfer 400 wood and the receiver already has 100 wood then only 300 will be transfered*/
	public static boolean transferLimitedItemList(ItemList<?> list, WarpedGroupIdentity giver, WarpedGroupIdentity taker) {
		if(giver.getManagerType() != WarpedManagerType.ITEM || taker.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> transferItemList() -> giver or taker is not an contextGroup of items, it is : " + giver.getManagerType() + ", " + taker.getManagerType());
			return false;
		}
		
		ItemList<?> itemList = new ItemList<>(list); // copy list so original list is not modified
		
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem> consumables 	 = new ArrayList<>();
		
		for(int i = 0; i < itemList.size(); i++) {
			Enum<?> costType  = itemList.getCostType(i);
			int 	costValue = itemList.getItemQuantity(i);
									
			WarpedItem existing = WarpedState.itemManager.getItem(taker, costType);
			WarpedItem item = WarpedState.itemManager.getItem(giver, costType);
			
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
			consumables.get(j).consumeMultiple(itemList.getItemQuantity(consumableIndices.get(j)));
		}
		produceItemList(itemList, taker);
		return true;		
	}
	
	
	/**Will only transfer items that are available in the giver.
	 * 		 i.e. if the List calls to transfer 400 wood but the giver only has 300 wood then 300 wood will be transfered instead of the asked 400*/
	public boolean transferItemList(WarpedGroupIdentity giver, WarpedGroupIdentity taker) {return transferItemList(this, giver, taker);}
	/**Will only transfer items that are available in the giver.
	 * 		 i.e. if the List calls to transfer 400 wood but the giver only has 300 wood then 300 wood will be transfered instead of the asked 400*/
	public static boolean transferItemList(ItemList<?> list, WarpedGroupIdentity giver, WarpedGroupIdentity taker) {
		if(giver.getManagerType() != WarpedManagerType.ITEM || taker.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemList -> transferItemList() -> giver or taker is not an contextGroup of items, it is : " + giver.getManagerType() + ", " + taker.getManagerType());
			return false;
		}
		
		ItemList<?> itemList = new ItemList<>(list); // copy list so original list is not modified
		
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem> consumables 	 = new ArrayList<>();
		
		for(int i = 0; i < itemList.size(); i++) {
			Enum<?> costType  = itemList.getCostType(i);
			int 	costValue = itemList.getItemQuantity(i);
			
			WarpedItem item = WarpedState.itemManager.getItem(giver, costType);
			
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
			consumables.get(j).consumeMultiple(itemList.getItemQuantity(consumableIndices.get(j)));
		}
		produceItemList(itemList, taker);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public ItemList<AmmunitionType> castAmmunition(){
		if(listType == ItemType.AMMUNITION) return (ItemList<AmmunitionType>) this;
		else {
			System.err.println("ItemList -> castAmmunition() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public ItemList<ArtifactType> castArtifact(){
		if(listType == ItemType.ARTIFACT) return (ItemList<ArtifactType>) this;
		else {
			System.err.println("ItemList -> castArtifact() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public ItemList<ContainerType> castContainer(){
		if(listType == ItemType.CONTAINER) return (ItemList<ContainerType>) this;
		else {
			System.err.println("ItemList -> castContainer() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public ItemList<DrinkType> castDrink(){
		if(listType == ItemType.DRINK) return (ItemList<DrinkType>) this;
		else {
			System.err.println("ItemList -> castDrink() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public ItemList<ElementType> castElement(){
		if(listType == ItemType.ELEMENT) return (ItemList<ElementType>) this;
		else {
			System.err.println("ItemList -> castElement() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public ItemList<EquipmentType> castEquipment(){
		if(listType == ItemType.EQUIPMENT) return (ItemList<EquipmentType>) this;
		else {
			System.err.println("ItemList -> castEquipment() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public ItemList<FoodType> castFood(){
		if(listType == ItemType.FOOD) return (ItemList<FoodType>) this;
		else {
			System.err.println("ItemList -> castFood() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public ItemList<ResourceType> castResource(){
		if(listType == ItemType.RESOURCE) return (ItemList<ResourceType>) this;
		else {
			System.err.println("ItemList -> castResource() -> class cast exception, item type is : " + listType);
			return null;
		}
	} 
	
}
