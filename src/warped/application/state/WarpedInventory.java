package warped.application.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import warped.application.entities.item.ItemBindable;
import warped.application.entities.item.ItemList;
import warped.application.entities.item.WarpedItem;
import warped.utilities.utils.Console;

public class WarpedInventory<T extends ItemBindable<? extends Enum<?>>> extends WarpedGroup<WarpedItem<T>>{

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

	protected WarpedInventory(WarpedGroupIdentity groupID) {
		super(groupID);

	}
	
	protected WarpedInventory(WarpedGroupIdentity groupID, String name) {
		super(groupID, name);

	}
	
	/**Get the list of members this group contains
	 * @return ArrayList<T> - the members of this group.
	 * @author 5som3*/
	public ArrayList<WarpedItem<T>> getMembers(){return members;}
	

	/**Get a specific member from this group.
	 * @param objectID - the id of the object to get.
	 * @return T - an object of the type assigned to this groups manager,
	 * @author 5som3*/
	public WarpedItem<T> getMember(WarpedObjectIdentity objectID) {
		if(!objectID.getGroupID().isEqual(groupID)) {
			Console.err("WarpedGroup -> getMember() -> object ID does not belong to this group");
			groupID.printString();
			objectID.printString();
		} return members.get(objectID.getMemberIndex());
	}
	
	/**Get the member at the specified index
	 * @param index - the index of the member to return
	 * @return T - the member at the specified index
	 * @exception will fail if index is out of bounds
	 * @author 5som3*/
	public WarpedItem<T> getMember(int index) {return members.get(index);}
	
	/**Get the item of the specified type
	 * @param type - the type of item to get
	 * @return WarpedItem<T> - an item of the specified type, will return null if inventory does not contain the item
	 * @author 5som3*/
	public WarpedItem<T> getMember(T type){
		for(int i = 0; i < members.size(); i++) {
			WarpedItem<T> member = members.get(i);
			if(member.getItemType() == type) return member;
		} return null;
	}
	
	

	
	/**Add a member to the group
	 * @param member - add a member to the inventory
	 * @apiNote If another item of the same type already exist then the items will be consolidated. (their quantities combined)
	 * @author 5som3*/
	public WarpedObjectIdentity addMember(WarpedItem<T> member) {		
		boolean consolidated = false;
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).getItemType() == member.getItemType()) {
				members.get(i).produce(member.getQuantity());
				consolidated = true;
				return members.get(i).getObjectID();
			}
			else continue;
		}
		if(!consolidated) {
			WarpedObjectIdentity ID = new WarpedObjectIdentity(groupID, members.size());
			member.overrideObjectIdentity(ID);
			members.add(member);
			return ID;			
		} else {
			Console.err("ItemManager -> addItem() -> was not handled ");
			return null;
		}
	}
	
	/**Add a member to the group.
	 * @param member - add a member to the inventory.
	 * @param index - the index to add the member at. 
	 * @apiNote If another item of the same type already exist then the items will be consolidated. (their quantities combined).
	 * @implNote Will correct subsequent objectID member indices.
	 * @author 5som3*/
	public WarpedObjectIdentity addMember(WarpedItem<T> member, int index) {		
		boolean consolidated = false;
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).getItemType() == member.getItemType()) {
				members.get(i).produce(member.getQuantity());
				consolidated = true;
				return members.get(i).getObjectID();
			} else continue;
		}
		if(!consolidated) {
			if(index < 0) {
				Console.err("WarpedInventory -> addMember() -> index out of bounds : " + index + ", will be changed to 0");
				index = 0;
			}
			if(index >= members.size()) {
				Console.err("WarpedInventory -> addMember() -> index out of bounds : " + index + ", will be changed to " + members.size());
				index = members.size();
			}
			WarpedObjectIdentity ID = new WarpedObjectIdentity(groupID, index);
			member.overrideObjectIdentity(ID);
			members.add(index, member);
			for(int i = index + 1; i < members.size(); i++) members.get(i).getObjectID().shuffleRight();
			return ID;			
		} else {
			Console.err("ItemManager -> addItem() -> was not handled ");
			return null;
		}
	}
	
	/**Remove a member from the group at the specified index.
	 * @param index - the index of the member to remove.
	 * @implNote The method will not remove a member if the index is out of bounds.
	 * @author 5som3 */
	public void removeMember(int index) {
		if(index < 0 || index >= size()) {
			Console.err("WarpedInventory -> removeMember() -> index is out of bounds : " + index);
			return;
		} else members.remove(index);
	}
	
	/**Swap the position of two items in the inventory.
	 * @param indexA - the index of the object to swap.
	 * @param indexB - the index of the other object to swap.
	 * @apiNote If indexA/B are equal or out of bounds no members will be swapped 
	 * @author 5som3*/
	public void swampMembers(int indexA, int indexB) {
		if(indexA == indexB || indexA < 0 || indexA >= members.size() || indexB < 0 || indexB >= members.size()) {
			Console.err("WarpedInventory -> swapMembers() -> invalid member indices (indexA, indexB) : ( " + indexA  + ", " + indexB + ")");
			return;
		}
		WarpedItem<T> memberA = getMember(indexA);
		WarpedItem<T> memberB = getMember(indexB);
		
		WarpedObjectIdentity identityA = new WarpedObjectIdentity(groupID, memberA.getObjectID(), memberB.getObjectID().getMemberIndex());
		WarpedObjectIdentity identityB = new WarpedObjectIdentity(groupID, memberB.getObjectID(), memberA.getObjectID().getMemberIndex());
		
		memberA.overrideObjectIdentity(identityA);
		memberB.overrideObjectIdentity(identityB);
		
		members.set(indexA, memberB);
		members.set(indexB, memberA);
	}

	
	/**Sort the inventory based on the specified sort type
	 * @param sortType - the type of sorting to use on this inventory
	 * @author 5som3*/
	public void sort(SortType sortType) {
		ArrayList<WarpedItem<T>> newGroup = new ArrayList<>(getMembers());
		List<String> names = new ArrayList<>();
		switch(sortType) {
		case SORT_ALPHABET:
			ArrayList<WarpedItem<T>> sortedAlphabet = new ArrayList<>();
			forEach(i -> {names.add(i.getName());});
			Collections.sort(names);
			for(int i = 0; i < names.size(); i++) {
				String name = names.get(i);
				for(int j = 0; j < size(); j++) {
					if(name.equals(getMember(j).getName())) {
						sortedAlphabet.add(getMember(j));
						break;
					}
				}
			}
			members = sortedAlphabet;
			break;
		case SORT_ALPHABET_REVERSE:
			ArrayList<WarpedItem<T>> reverseAlphabet = new ArrayList<>();
			forEach(i -> {names.add(i.getName());});
			Collections.sort(names);
			Collections.reverse(names);
			for(int i = 0; i < names.size(); i++) {
				String name = names.get(i);
				for(int j = 0; j < size(); j++) {
					if(name.equals(getMember(j).getName())) {
						reverseAlphabet.add(getMember(j));
						break;
					}
				}
			}
			members = reverseAlphabet;
			break;
		case SORT_MASS:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getMassOfOne));
			members = newGroup;
			break;
		case SORT_MASS_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getMassOfOne));
			Collections.reverse(newGroup);
			members = newGroup;
			break;
		case SORT_QUANTITY:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getQuantity));
			members = newGroup;
			break;
		case SORT_QUANTITY_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getQuantity));
			Collections.reverse(newGroup);
			members = newGroup;
			break;
		case SORT_VALUE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getValueOfOne));
			members = newGroup;
			break;
		case SORT_VALUE_REVERSE:
			newGroup.sort(Comparator.comparingDouble(WarpedItem::getValueOfOne));
			Collections.reverse(newGroup);
			members = newGroup;
			break;
		default:
			Console.err("ItemManager -> sortInventory() -> invalid case : " + sortType);
			break;
		}
		resetMemberOrder();
	}
	
	/**Does the inventory contain all items specified on the list 
	 * @param list - an ItemList with the types and quantities to check for.
	 * @return boolean - true if the inventory contains all items in the list else false.
	 * @author 5som3*/
	public boolean contains(ItemList<T> list) {		
		for(int i = 0; i < list.size(); i++) {
			T costType  = list.getType(i);
			int costValue = list.getQuantity(i);
			
			WarpedItem<T> item = getMember(costType);
			if(item == null) return false;
			else if(item.getQuantity() < costValue) return false;
			else continue;
		}
		return true;	
	}

	/**Try to consume the list of items.
	 * @param list - the list of items to consume
	 * @return boolean - true if the inventory has enough items to fulfill the consume request else false
	 * @apiNote If inventory doesn't have enough items, no items will be consumed.
	 * @author 5som3*/
	public boolean consume(ItemList<T> list) {		
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem<T>> consumables = new ArrayList<>();
		
		for(int i = 0; i < list.size(); i++) {
			T costType    = list.getType(i);
			int costValue = list.getQuantity(i);
			
			WarpedItem<T> item = getMember(costType);
			
			if(item == null) return false;
			else if(item.getQuantity() < costValue) return false;
			else {
				consumableIndices.add(i);
				consumables.add(item);
			}	
		}

		for(int j = 0; j < consumables.size(); j++) {
			consumables.get(j).consume(list.getQuantity(consumableIndices.get(j)));
		}
		return true;
	}
	
	/**Create and add the items specified in the list to this inventory
	 * @param items - the list of items to produce
	 * @implNote  if an item in the list already exist in the inventory then their quantities will be combined. 
	 * @author 5som3*/
	public void produce(ItemList<T> items) {
		for(int i = 0; i < items.size(); i++) {
			produce(items.getType(i), items.getQuantity(i));			
		}
	}
	
	/**Create and add the item specified.
	 * @param itemType - the type of item to produce.
	 * @param quantity - the quantity of item to produce.
	 * @author 5som3*/
	public void produce(T itemType, int quantity) {
		WarpedItem<T> item = itemType.generateItem(quantity);
		addMember(item);
	}
	
	
	

}
