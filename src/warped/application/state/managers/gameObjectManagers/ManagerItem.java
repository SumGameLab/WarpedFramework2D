/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import warped.application.entities.item.ItemType;
import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ammunition.ItemAmmunition;
import warped.application.entities.item.artifact.ItemArtifact;
import warped.application.entities.item.container.ItemContainer;
import warped.application.entities.item.drink.ItemDrink;
import warped.application.entities.item.element.ItemElement;
import warped.application.entities.item.equipment.ItemEquipment;
import warped.application.entities.item.food.ItemFood;
import warped.application.entities.item.resource.ItemResource;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.utilities.utils.Console;

public class ManagerItem<T extends WarpedObject> extends WarpedManager<T> {

	public static enum SortType {
		SORT_ALPHABET, 		 
		SORT_ALPHABET_REVERSE,
		SORT_VALUE, 			
		SORT_VALUE_REVERSE, 	
		SORT_QUANTITY, 		
		SORT_QUANTITY_REVERSE,
		SORT_MASS,			
		SORT_MASS_REVERSE, 	
	}
	
	protected ManagerItem() {
		managerType = WarpedManagerType.ITEM;
	}
	
	
	
	public static WarpedGroupIdentity generateInventory(int iconWidth, int iconHeight) {
		WarpedGroupIdentity groupID = WarpedState.itemManager.addGroup();
		WarpedGroup<WarpedItem> group = WarpedState.itemManager.getGroup(groupID);
		group.setMemberSize(iconWidth, iconHeight);
		return groupID;		
	}
	
	
	public void sortInventory(WarpedGroupIdentity inventID, SortType sortType) {
		WarpedGroup<WarpedItem> invent = WarpedState.itemManager.getGroup(inventID);
		ArrayList<WarpedItem> sorted = new ArrayList<>();
		ArrayList<WarpedItem> newGroup = new ArrayList<>(WarpedState.itemManager.getGroup(inventID).getMembers());
		List<String> names = new ArrayList<>();
		switch(sortType) {
		case SORT_ALPHABET:
			invent.forEach(i -> {names.add(i.getName());});
			Collections.sort(names);
			for(int i = 0; i < names.size(); i++) {
				String name = names.get(i);
				for(int j = 0; j < invent.getMemberCount(); j++) {
					if(name.equals(invent.getMember(j).getName())) {
						sorted.add(invent.getMember(j));
						break;
					}
				}
			}
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(sorted);
			break;
		case SORT_ALPHABET_REVERSE:
			invent.forEach(i -> {names.add(i.getName());});
			Collections.sort(names);
			Collections.reverse(names);
			for(int i = 0; i < names.size(); i++) {
				String name = names.get(i);
				for(int j = 0; j < invent.getMemberCount(); j++) {
					if(name.equals(invent.getMember(j).getName())) {
						sorted.add(invent.getMember(j));
						break;
					}
				}
			}
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(sorted);
			break;
		case SORT_MASS:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::massOfOne));
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		case SORT_MASS_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::massOfOne));
			Collections.reverse(newGroup);
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		case SORT_QUANTITY:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getQuantity));
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		case SORT_QUANTITY_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getQuantity));
			Collections.reverse(newGroup);
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		case SORT_VALUE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::valueOfOne));
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		case SORT_VALUE_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::valueOfOne));
			Collections.reverse(newGroup);
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		default:
			Console.err("ItemManager -> sortInventory() -> invalid case : " + sortType);
			break;
		}
	}
	
	public boolean addItem(WarpedGroupIdentity groupID, T member) {
		WarpedItem item =  (WarpedItem) member;
		@SuppressWarnings("unchecked")
		ArrayList<WarpedItem> members =  (ArrayList<WarpedItem>) getGroup(groupID).getMembers();
		boolean consolidated = false;
		for(int i = 0; i < members.size(); i++) {
			if(WarpedItem.equals(members.get(i), item)) {
				members.get(i).addMultiple(item.getQuantity());
				consolidated = true;
				return true;
			} else continue;
		}
		if(!consolidated) {
			getGroup(groupID).addMember(member);
			return true;
		} else {
			Console.err("ItemManager -> addItem() -> was not handled ");
			return false;
		}
	}
	
	/*
	public boolean removeItem(ContextGroupIdentity groupID, T member) {
		ItemEntitie item =  (ItemEntitie) member;
		@SuppressWarnings("unchecked")
		ArrayList<ItemEntitie> members =  (ArrayList<ItemEntitie>) getGroup(groupID).getMembers();
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).equals(item)) {
				members.get(i).zeroQuantity();
				return true;
			}
		} return false;		
	}
	*/
	
	public boolean containsItem(WarpedGroupIdentity groupID, T member) {
		WarpedItem item =  (WarpedItem) member;
		@SuppressWarnings("unchecked")
		ArrayList<WarpedItem> members =  (ArrayList<WarpedItem>) getGroup(groupID).getMembers();
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).equals(item)) return true;
		} return false;
	}
	
	public boolean containsItem(WarpedGroupIdentity groupID, Enum<?> type) {if(getItem(groupID, type) == null) return false; else return true;}
	
	public WarpedItem getItem(WarpedGroupIdentity groupID, Enum<?> type) {
		if(groupID.getManagerType() != WarpedManagerType.ITEM) {
			Console.err("ItemManager -> containsItem() -> groupID is not an item group, it is a : " + groupID.getManagerType());
			return null;
		}
		
		ItemType itemType = ItemType.getItemType(type);
		if(itemType == null) {
			Console.err("ItemManager -> containsItem() -> item type is not a valid item type");
			return null;
		}
				
		@SuppressWarnings("unchecked")
		ArrayList<WarpedItem> members =  (ArrayList<WarpedItem>) getGroup(groupID).getMembers();
		
		for(int i = 0; i < members.size(); i++) {
			WarpedItem member = members.get(i);
			
			if(member.getItemType() == itemType) {
				switch(itemType) {
				case AMMUNITION:
					ItemAmmunition ammunition = member.castAmmunition();
					if(ammunition.getAmmunitionType() == type) return member; else break;
				case ARTIFACT:
					ItemArtifact artifact = member.castArtifact();
					if(artifact.getArtifactType() == type) return member; else break;
				case CONTAINER:
					ItemContainer container = member.castContainer();
					if(container.getContainerType() == type) return member; else break;
				case DRINK:
					ItemDrink drink = member.castDrink();
					if(drink.getDrinkType() == type) return member; else break;
				case ELEMENT:
					ItemElement element = member.castElement();
					if(element.getElementType() == type) return member; else break;
				case EQUIPMENT:
					ItemEquipment equipment = member.castEquipment();
					if(equipment.getEquipmentType() == type) return member; else break;
				case FOOD:
					ItemFood food = member.castFood();
					if(food.getFoodType() == type) return member; else break;
				case RESOURCE:
					ItemResource resource = member.castResource();
					if(resource.getResourceType() == type) return member; else break;
				default:
					break;
				
				}
			}
		}
		Console.err("ItemManager-> getItem() -> invent doesn't contain any item of the type : " + type);
		return null;
	}
		
}
