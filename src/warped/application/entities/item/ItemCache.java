/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item;

import java.util.ArrayList;
import java.util.List;

import warped.application.entities.item.artifact.ArtifactType;
import warped.application.entities.item.artifact.ItemArtifact;
import warped.application.entities.item.drink.DrinkType;
import warped.application.entities.item.element.ElementType;
import warped.application.entities.item.element.ItemElement;
import warped.application.entities.item.equipment.EquipmentType;
import warped.application.entities.item.food.FoodType;
import warped.application.entities.item.resource.ItemResource;
import warped.application.entities.item.resource.ResourceType;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.utilities.utils.UtilsMath;

public class ItemCache {	
	
	private ArrayList<ArtifactType>  artifacts          = new ArrayList<>();
	private ArrayList<Integer> 		 artifactQuantitys  = new ArrayList<>();
	private ArrayList<FoodType> 	 foods              = new ArrayList<>();
	private ArrayList<Integer> 		 foodsQuantitys     = new ArrayList<>();
	private ArrayList<DrinkType> 	 drinks             = new ArrayList<>();
	private ArrayList<Integer> 		 drinksQuantitys    = new ArrayList<>();
	private ArrayList<ElementType> 	 elements	        = new ArrayList<>();
	private ArrayList<Integer> 		 elementsQuantitys  = new ArrayList<>();
	private ArrayList<EquipmentType> equipment          = new ArrayList<>();
	private ArrayList<Integer> 		 equipmentQuantitys = new ArrayList<>();
	private ArrayList<ResourceType>  resources          = new ArrayList<>();
	private ArrayList<Integer> 		 resourcesQuantitys = new ArrayList<>();
	
	ArrayList<WarpedItem> items;
	
	public ItemCache() {

	}
	
	
	//--------
	//---------------- Artifacts -------
	//--------
	public void addRandomArtifacts(int number, int max) {
		if(number < 1) {
			System.err.println("ItemCache -> addRandomArtifactsToCache() -> number must be at least 1 : " + number + " -> it will be set to 1 ");
			number = 1;
		}
		
		ArrayList<ArtifactType> artifacts = new ArrayList<>();
		for(int i = 0; i < number; i++) {
			ArtifactType type = ArtifactType.getRandomType();
			if(!artifacts.contains(type)) artifacts.add(type);
		}
		addArtifacts(artifacts, max);
	}
	
	public void addArtifacts(List<ArtifactType> artifacts, int max) {
		if(max < 2) {
			System.err.println("ItemCache -> addArtifactsToCache() -> max must be greater than 0 : " + max + " -> it will be set to 2");
			max = 2;
		}
		for(int i = 0; i < artifacts.size(); i++) {
			this.artifacts.add(artifacts.get(i));
			this.artifactQuantitys.add(UtilsMath.random(1, max));
		}
	}
	
	public void addArtifacts(List<ArtifactType> artifacts, List<Integer> quantitys) {
		if(artifacts.size() != quantitys.size()) {
			System.err.println("ItemCache -> addArtifcatsTocache() -> artifacts size doesn't match quantitys size : " + artifacts.size() + ", " + quantitys.size());
			return;
		}
		for(int i = 0; i < artifacts.size(); i++) {
			this.artifacts.add(artifacts.get(i));
			this.artifactQuantitys.add(quantitys.get(i));
		}
	}
	public void addArtifact(ArtifactType artifact, Integer quantity) {
		artifacts.add(artifact);
		artifactQuantitys.add(quantity);
	}
	
	//--------
	//---------------- Food -------
	//--------
	public void addFoods(List<FoodType> foods, List<Integer> quantitys) {
		if(foods.size() != quantitys.size()) {
			System.err.println("ItemCache -> addFoodsTocache() -> artifacts size doesn't match quantitys size : " + artifacts.size() + ", " + quantitys.size());
			return;
		}
		for(int i = 0; i < foods.size(); i++) {
			this.foods.add(foods.get(i));
			this.foodsQuantitys.add(quantitys.get(i));
		}
	}
	
	//--------
	//---------------- Drinks -------
	//--------
	public void addDrinks(List<DrinkType> drinks, List<Integer> quantitys) {
		if(drinks.size() != quantitys.size()) {
			System.err.println("ItemCache -> addDrinksTocache() -> artifacts size doesn't match quantitys size : " + artifacts.size() + ", " + quantitys.size());
			return;
		}
		for(int i = 0; i < artifacts.size(); i++) {
			this.drinks.add(drinks.get(i));
			this.drinksQuantitys.add(quantitys.get(i));
		}
	}
	
	//--------
	//---------------- Elements -------
	//--------
	public void addRandomElements(int number, int max) {
		if(number < 1) {
			System.err.println("ItemCache -> addRandomElementsToCache() -> number is less than 1 -> it will be set to 1");
			number = 1;
		}

		ArrayList<ElementType> elements = new ArrayList<>();
		for(int i = 0; i < number; i++) {
			ElementType type = ElementType.getRandomType(); 
			if(!elements.contains(type)) elements.add(type);
		}
		addElements(elements, max);
	}
	
	public void addElements(List<ElementType> elements, int max) {
		if(max < 2) {
			System.err.println("ItemCache -> addElementsToCache() -> max is less than 1 -> it will be set to 1");
			max = 2;
		}
		
		for(int i = 0; i < elements.size(); i++) {
			this.elements.add(elements.get(i));
			elementsQuantitys.add(UtilsMath.random(1, max));
		}
	}
	
	public void addElement(ElementType element, Integer quantity) {
		elements.add(element);
		elementsQuantitys.add(quantity);
	}
	
	public void addElements(List<ElementType> elements, List<Integer> quantitys) {
		if(elements.size() != quantitys.size()) {
			System.err.println("ItemCache -> addElementsTocache() -> artifacts size doesn't match quantitys size : " + artifacts.size() + ", " + quantitys.size());
			return;
		}
		
		for(int i = 0; i < elements.size(); i++) {
			this.elements.add(elements.get(i));
			this.elementsQuantitys.add(quantitys.get(i));
		}
	}
	
	//--------
	//---------------- Equipment -------
	//--------
	public void addEquipment(List<EquipmentType> equipment, List<Integer> quantitys) {
		if(equipment.size() != quantitys.size()) {
			System.err.println("ItemCache -> addEquipmentTocache() -> artifacts size doesn't match quantitys size : " + artifacts.size() + ", " + quantitys.size());
			return;
		}
		for(int i = 0; i < equipment.size(); i++) {
			this.equipment.add(equipment.get(i));
			this.equipmentQuantitys.add(quantitys.get(i));
		}
	}

	//--------
	//---------------- Resources -------
	//--------
	public void addRandomMetals(int number, int max) {addResources(ResourceType.getRandomMetals(number), max);}
	
	public void addResources(List<ResourceType> resources, int max) {
		if(max < 2) {
			System.err.println("ItemCache -> addResources() -> max is less than 2 -> it will be set to 2");
			max = 2;
		}
		
		for(int i = 0; i < elements.size(); i++) {
			this.elements.add(elements.get(i));
			elementsQuantitys.add(UtilsMath.random(1, max));
		}
	}
	public void addResources(List<ResourceType> resources, List<Integer> quantitys) {
		if(resources.size() != quantitys.size()) {
			System.err.println("ItemCache -> addResrouces() -> resources size doesn't match quantitys size : " + artifacts.size() + ", " + quantitys.size());
			return;
		}
		for(int i = 0; i < resources.size(); i++) {
			resources.add(resources.get(i));
			resourcesQuantitys.add(quantitys.get(i));
		}
	}
	public void addResource(ResourceType resource, Integer quantity) {
		resources.add(resource);
		resourcesQuantitys.add(quantity);
	}
	
	
	
	
	//--------
	
	public void lootCache(WarpedGroupIdentity inventID) {
		if(inventID.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemCache -> lootCache() -> group is not an item group : " + inventID.getManagerType());
			return;
		}
		
		if(items == null) generateCachedItems();
		for(int i = 0; i < items.size(); i++) {
			WarpedState.itemManager.addItem(inventID, items.get(i));
		}
	}
	
	public void lootExcludingType(WarpedGroupIdentity inventID, ItemType itemType) {
		if(inventID.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemCache -> lootResrouces() -> group is not an item group : " + inventID.getManagerType());
			return;
		}
		
		if(items == null) generateCachedItems();
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getItemType() == itemType) continue;
			WarpedState.itemManager.addItem(inventID, items.get(i));
		}
	}
	
	public void lootResources(WarpedGroupIdentity inventID) {
		if(inventID.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemCache -> lootResrouces() -> group is not an item group : " + inventID.getManagerType());
			return;
		}

		if(items == null) generateCachedItems();
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getItemType() != ItemType.RESOURCE) continue;
			WarpedState.itemManager.addItem(inventID, items.get(i));
		}
	}
	
	public void lootElements(WarpedGroupIdentity inventID) {
		if(inventID.getManagerType() != WarpedManagerType.ITEM) {
			System.err.println("ItemCache -> lootResrouces() -> group is not an item group : " + inventID.getManagerType());
			return;
		}
		
		if(items == null) generateCachedItems();
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getItemType() != ItemType.ELEMENT) continue;
			WarpedState.itemManager.addItem(inventID, items.get(i));
		}
	}
	
	
	public int getItemCount() {return artifacts.size() + foods.size() + drinks.size() + elements.size() + equipment.size() + resources.size();}
	
	
	public ArrayList<WarpedItem> getCachedItems(){
		if(items == null) generateCachedItems();
		return items;
	}
	public void generateCachedItems(){
		if(items != null) {
			System.err.println("ItemCache -> generateCachedItems() -> items are alread generated");
			return;
		}
		ArrayList<WarpedItem> result = new ArrayList<>();
		
		for(int i = 0; i < artifacts.size(); i++) {
			ArtifactType type = artifacts.get(i);
			switch(type){ 
			case RUNE_TABLET: 
				result.add(ItemArtifact.generateRandomRuneTablet());
				break;
			default:
				System.err.println("ItemCache -> generateCacheItems() -> generateArtifacts -> invalid case : " + type);
				break;
			}	
		}
		
		for(int i = 0; i < foods.size(); i++) {
			
		}
		for(int i = 0; i < drinks.size(); i++) {
			
		}
		for(int i = 0; i < elements.size(); i++) {result.add(new ItemElement(elements.get(i), elementsQuantitys.get(i)));}
		
		for(int i = 0; i < equipment.size(); i++) {
			
		}
		for(int i = 0; i < resources.size(); i++) {result.add(new ItemResource(resources.get(i), resourcesQuantitys.get(i)));}
	
		items = result;
		System.out.println("ItemCache -> generateCachedItems() -> generated " + items.size() + " items in cache");
	}
	
}
