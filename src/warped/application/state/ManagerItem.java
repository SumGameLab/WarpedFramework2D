/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.util.ArrayList;

import warped.application.entities.item.ItemBindable;
import warped.application.entities.item.ItemList;
import warped.application.entities.item.WarpedItem;
import warped.utilities.utils.Console;

public class ManagerItem<T extends ItemBindable<? extends Enum<?>>> extends WarpedManager<WarpedItem<T>> {

	
	
	/**A manager for items, provides item sorting by various ways*/
	public ManagerItem() {
		super("Item Manager");
	}
	
	
	/**Add a group to the manager
	 * @return WarpedGroup<T> - the newly created added group
	 * @author 5som3*/
	public WarpedInventory<T> addGroup() {
		WarpedGroupIdentity groupID = new WarpedGroupIdentity(UNIQUE_ID, groups.size());
		WarpedInventory<T> g = new WarpedInventory<T>(groupID);
		groups.add(g);
		return g;
	}
	
	/**Add a group to the manager
	 * @return WarpedGroup<T> - the newly created added group
	 * @author 5som3*/
	public WarpedInventory<T> addGroup(String name) {
		WarpedGroupIdentity groupID = new WarpedGroupIdentity(UNIQUE_ID, groups.size());
		WarpedInventory<T> g = new WarpedInventory<T>(groupID, name);
		groups.add(g);
		return g;
	}
	
	/**Get a member from a group owned by this manager
	 * @param objectID - the identity of the object to get
	 * @return T - a member of the type assigned to this manager
	 * @author 5som3*/
	public WarpedItem<T> getMember(WarpedObjectIdentity objectID) {
		if(isOwner(objectID)) return getGroup(objectID.getGroupID()).getMember(objectID); 
		else {
			Console.err("WarpedManager -> getMember() -> member does not belong to this manager : " + name);
			objectID.printString();
			return null;
		}
	}
	
	/**Get a member from a group owned by this manager
	 * @param groupID - the ID of the group to get the item from.
	 * @param itemType - the type of item to get from the inventory
	 * @return WarpedItem<T> -  an item of the specified type.
	 * @apiNote Will return null if the item type is not in the specified inventory. 
	 * @author 5som3*/
	public WarpedItem<T> getMember(WarpedGroupIdentity groupID, T itemType) {return getGroup(groupID).getMember(itemType);}
	
	/**Get a group from of objects from this manager.
	 * @param objectID - ID of an object belonging to the group to get.
	 * @return WarpedGroup<T> - The group that contains the specified object. 
	 * @author 5som3*/
	public WarpedInventory<T> getGroup(WarpedObjectIdentity objectID){return getGroup(objectID.getGroupID());}
	
	/**Get a group from this manager
	 * @param groupID - ID of the group to get.
	 * @return WarpedGroup<T> - The group that the specified ID refers to.
	 * @author 5som3*/
	public WarpedInventory<T> getGroup(WarpedGroupIdentity groupID){
		if(isOwner(groupID)) return (WarpedInventory<T>) groups.get(groupID.getGroupIndex());
		else { 
			Console.err("WarpedManager -> getGroup() -> group does not belong to this manager : " + name);
			groupID.printString();
			return null;
		}
	}
	
	/**Add a member to a group in this manager
	 * @param groupID - the ID of the group to added the member too
	 * @param member - the member to add to the group
	 * @author 5som3*/
	public WarpedObjectIdentity addMember(WarpedGroupIdentity groupID, WarpedItem<T> member) {return getGroup(groupID).addMember(member);}

	
	/**Transfer the contents of one inventory into another
	 * @param giver - the inventory to empty all the items from.
	 * @param receiver - the inventory to add the items to.
	 * @implNote - Transferring items doesn't actually transfer, Items will be removed from the giver and new items are created in receiver
	 * @author 5som3*/
	public void transfer(WarpedGroupIdentity giver, WarpedGroupIdentity receiver) {
		ItemList<T> list = new ItemList<T>(getGroup(giver));
		getGroup(giver).clearMembers();
		getGroup(receiver).produce(list);
	}
	
	/**Transfer the items specified only if the giver has enough items to fulfill the request
	 * @param itemList - a list of item types and quantities to transfer
	 * @param giver - the ID of the inventory to take the items from.
	 * @param reciever - the ID of the inventory to receive the items from the giver. 
	 * @result boolean - true if the transfer was complete else false.
	 * @implNote - Transferring items doesn't actually transfer, Items will be removed from the giver and new items are created in receiver
	 * @implNote The transfer will only complete if the giver has every item type with enough quantity.
	 * @implNote If the transfer is unable to be completed no inventory items or quantities will be changed.
	 * @author 5som3*/
	public boolean transferIfAble(ItemList<T> itemList, WarpedGroupIdentity giver, WarpedGroupIdentity reciever) {
		if(!isOwner(giver) || !isOwner(reciever)){
			Console.err("ManagerItem -> transferIfAble() -> giver or reciever of transfer is not a group belonging to this manager");
			return false;
		}
		if(getGroup(giver).consume(itemList)) {
			getGroup(reciever).produce(itemList);
			return true;
		} else {
			WarpedState.notify.note("Not enough to complete transfer");
			Console.err("ManagerItem -> transferIfAble() -> was not able to transfer because of insufficient giver inventory");
			return false;
		}
	}
	
	/**Transfer (all or as much as possible) of the items specified in the list.
	 * @param itemList - a list of the items to transfer.
	 * @param giver - the ID of the inventory to take the items from.
	 * @param reciever - the ID of the inventory to receive the items from the giver.
	 * @implNote - Transferring items doesn't actually transfer, Items will be removed from the giver and new items are created in receiver 
	 * @apiNote Will only transfer items that are available in the giver.
	 * @apiNote i.e. if the List calls to transfer 400 wood but the giver only has 300 wood then 300 wood will be transfered instead of the asked 400*/
	public void transferAvailable(ItemList<T> list, WarpedGroupIdentity giver, WarpedGroupIdentity reciever) {				
		ItemList<T> itemList = new ItemList<T>(list);
		ArrayList<Integer> consumableIndices = new ArrayList<>();
		ArrayList<WarpedItem<T>> consumables = new ArrayList<>();
		
		WarpedInventory<T> giverInvent = getGroup(giver);
		
		for(int i = 0; i < itemList.size(); i++) {// check list
			T type       = itemList.getType(i);
			int	quantity = itemList.getQuantity(i);
			
			WarpedItem<T> item = giverInvent.getMember(type);
			
			if(item == null) {	//Item not available, remove request
				itemList.removeEntry(i);
				i--;
				continue;
			}
			else {
				if(item.getQuantity() < quantity) itemList.setQuantity(i, item.getQuantity()); // Item quantity insufficient, reduce request quantity 
				consumableIndices.add(i);
				consumables.add(item);
			}	
		}
		
		getGroup(giver).consume(itemList);
		getGroup(reciever).produce(itemList); // produce adjusted request
	}
	
	/**Transfer as much as needed (or as much as is available) of the specified list.
	 * @param list - the list of items to transfer 
	 * @param giver - the ID of the inventory to take the items from.
	 * @param reciever - the ID of the inventory to receive the items from the giver. 
	 * @implNote Transferring items doesn't actually transfer, Items will be removed from the giver and new items are created in receiver
	 * @implNote Will only transfer the items that are missing from the receiving inventory.
	 * @implNote i.e. if the ItemList calls to transfer 400 wood and the receiver already has 100 wood then only 300 will be transfered*/
	public void transferNeeded(ItemList<T> list, WarpedGroupIdentity giver, WarpedGroupIdentity receiver) {
		WarpedInventory<T> giverInvent   = getGroup(giver);
		WarpedInventory<T> receiverInvent = getGroup(receiver);
		
		ItemList<T> itemList = new ItemList<T>(list);
		
		for(int i = 0; i < itemList.size(); i++) {
			T costType    = itemList.getType(i);
			int costValue = itemList.getQuantity(i);
									
			WarpedItem<T> existing = getMember(receiver, costType);
			WarpedItem<T> item = getMember(giver, costType);
			
			if(existing != null) {
				costValue -= existing.getQuantity();
				if(costValue <= 0) item = null;
			}
				
			if(item == null) {
				itemList.removeEntry(i);
				i--;
				continue;
				
			} else if(item.getQuantity() < costValue) itemList.setQuantity(i, costValue);
			
		}

		giverInvent.consume(itemList);
		receiverInvent.produce(itemList);	
	}

	/*

	public boolean addItem(WarpedGroupIdentity groupID, WarpedItem<T> item) {
	}
	
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
	
	
	/*
	public boolean containsItem(WarpedGroupIdentity groupID, T type) {if(getItem(groupID, type) == null) return false; else return true;}
	public WarpedItem<T> getItem(WarpedGroupIdentity groupID, T type) {
		if(groupID.getManagerID() != UNIQUE_ID) {
			Console.err("ItemManager -> containsItem() -> groupID is not an item group, it is a : " + groupID.getManagerID());
			return null;
		}			
		
		ArrayList<WarpedItem<T>> members =  getGroup(groupID).getMembers();
		
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).getItemType().isSameType(type)) return members.get(i);
		}
		Console.err("ItemManager-> getItem() -> invent doesn't contain any item of the type : " + type);
		return null;
	}
		
	*/
}
