/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import warped.application.entities.item.ItemBindable;
import warped.application.entities.item.WarpedItem;
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
		WarpedGroup<WarpedItem<?>> group = WarpedState.itemManager.getGroup(groupID);
		group.setMemberSize(iconWidth, iconHeight);
		return groupID;		
	}
	
	
	
	public void sortInventory(WarpedGroupIdentity inventID, SortType sortType) {
		WarpedGroup<WarpedItem<?>> invent = WarpedState.itemManager.getGroup(inventID);
		ArrayList<WarpedItem<?>> sorted = new ArrayList<>();
		ArrayList<WarpedItem<?>> newGroup = new ArrayList<>(WarpedState.itemManager.getGroup(inventID).getMembers());
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
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getMassOfOne));
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		case SORT_MASS_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getMassOfOne));
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
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getValueOfOne));
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		case SORT_VALUE_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getValueOfOne));
			Collections.reverse(newGroup);
			WarpedState.itemManager.getGroup(inventID).resetMemberOrder(newGroup);
			break;
		default:
			Console.err("ItemManager -> sortInventory() -> invalid case : " + sortType);
			break;
		}
	}
	
	public boolean addItem(WarpedGroupIdentity groupID, WarpedItem<?> item) {
		ArrayList<WarpedItem<?>> members = WarpedState.itemManager.getGroup(groupID).getMembers();
		boolean consolidated = false;
		for(int i = 0; i < members.size(); i++) {
			if(WarpedItem.isSameType(members.get(i), item)) {
				members.get(i).produce(item.getQuantity());
				consolidated = true;
				return true;
			} else continue;
		}
		if(!consolidated) {
			WarpedState.itemManager.getGroup(groupID).addMember(item);
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
	/*
	public boolean containsItem(WarpedGroupIdentity groupID, T member) {
		WarpedItem item =  (WarpedItem) member;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<WarpedItem> members =  (ArrayList<WarpedItem>) getGroup(groupID).getMembers();
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).) return true;
		} return false;
	}
	*/
	
	public <K extends ItemBindable<? extends Enum<?>>> boolean containsItem(WarpedGroupIdentity groupID, K type) {if(getItem(groupID, type) == null) return false; else return true;}
	
	@SuppressWarnings("unchecked")
	public <K extends ItemBindable<? extends Enum<?>>> WarpedItem<K> getItem(WarpedGroupIdentity groupID, K type) {
		if(groupID.getManagerType() != WarpedManagerType.ITEM) {
			Console.err("ItemManager -> containsItem() -> groupID is not an item group, it is a : " + groupID.getManagerType());
			return null;
		}
			
		@SuppressWarnings("rawtypes")
		ArrayList<WarpedItem> members =  (ArrayList<WarpedItem>) getGroup(groupID).getMembers();
		
		
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).getItemType().isSameType(type)) return members.get(i).castItem(type);
		}
		Console.err("ItemManager-> getItem() -> invent doesn't contain any item of the type : " + type);
		return null;
	}
		
}
