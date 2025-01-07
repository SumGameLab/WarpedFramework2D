/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item;

import java.util.HashMap;
import java.util.Map;

import warped.application.entities.item.ammunition.AmmunitionType;
import warped.application.entities.item.artifact.ArtifactType;
import warped.application.entities.item.container.ContainerType;
import warped.application.entities.item.drink.DrinkType;
import warped.application.entities.item.element.ElementType;
import warped.application.entities.item.equipment.EquipmentType;
import warped.application.entities.item.food.FoodType;
import warped.application.entities.item.resource.ResourceType;
import warped.utilities.utils.UtilsMath;

public enum ItemType {

	AMMUNITION,
	ARTIFACT,
	CONTAINER,
	DRINK,
	EQUIPMENT,
	FOOD,
	RESOURCE,
	ELEMENT;

	private static Map<Integer, ItemType> map = new HashMap<>();
	static {
		for (ItemType type : ItemType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static ItemType get(int index) {
	    return (ItemType) map.get(index);
	}
	public static ItemType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}

	
	public static ItemType getItemType(Enum<?> type) {
		Class<?> c = type.getClass();
		for(int i = 0; i < size(); i++) {
			switch(get(i)) {
			case AMMUNITION: if(c == AmmunitionType.class) 	return ItemType.AMMUNITION; else break;
			case ARTIFACT: 	 if(c == ArtifactType.class) 	return ItemType.ARTIFACT; else break;
			case CONTAINER:  if(c == ContainerType.class) 	return ItemType.CONTAINER; else break;
			case DRINK: 	 if(c == DrinkType.class) 		return ItemType.DRINK; else break;					
			case ELEMENT: 	 if(c == ElementType.class) 	return ItemType.ELEMENT; else break;
			case EQUIPMENT:  if(c == EquipmentType.class) 	return ItemType.EQUIPMENT; else break;
			case FOOD: 	 	 if(c == FoodType.class) 	 	return ItemType.FOOD; else break;
			case RESOURCE: 	 if(c == ResourceType.class) 	return ItemType.RESOURCE; else break;
			default:
				System.err.println("ItemType -> getItemType() -> Enum is not an item type : " + c.toString());
				return null;			
			}
		}
		return null;
	}
	
	public static WarpedItem generateItem(Enum<?> itemType) {return generateItem(itemType, 1);}
	
	public static WarpedItem generateItem(Enum<?> itemType, int quantity) {
		ItemType type = getItemType(itemType);
		if(type == null) {
			System.err.println("ItemType -> generateItem() -> enum is not an item type : " + itemType);
			return null;
		}
		
		switch(type) {
		case AMMUNITION: 
			AmmunitionType ammunitionType = (AmmunitionType) itemType;
			return ammunitionType.generateAmmunition(quantity);
		case ARTIFACT:
			ArtifactType artifactType = (ArtifactType) itemType;
			return artifactType.generateArtifact(quantity);
		case CONTAINER:
			ContainerType containerType = (ContainerType) itemType;
			return containerType.generateContainer(quantity);
		case DRINK:
			DrinkType drinkType = (DrinkType) itemType;
			return drinkType.generateDrink(quantity);
		case ELEMENT:
			ElementType elementType = (ElementType) itemType;
			return elementType.generateElement(quantity);
		case EQUIPMENT:
			EquipmentType equipmentType = (EquipmentType) itemType;
			return equipmentType.generateEquipment(quantity);
		case FOOD:
			FoodType foodType = (FoodType) itemType;
			return foodType.generateFood(quantity);
		case RESOURCE:
			ResourceType resourceType = (ResourceType) itemType;
			return resourceType.generateResource(quantity);
		default:
			System.err.println("ItemType -> generateItem() -> invalid case : " + itemType);
			return AmmunitionType.getRandomType().generateAmmunition(quantity);		
		}
	}
	
	
}
