/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.util.ArrayList;
import java.util.List;

import warped.application.state.managers.gameObjectManagers.WarpedObjectAction;
import warped.application.state.managers.gameObjectManagers.WarpedObjectCheckAction;
import warped.utilities.utils.Console;

public class WarpedManager<T extends WarpedObject> {
 
	protected String name = "Default";
	protected ArrayList<WarpedGroup<T>> groups = new ArrayList<>();	
	protected ArrayList<WarpedGroup<T>> activeGroups = new ArrayList<>();																					

	protected final int UNIQUE_ID; // A unique id for this manager, also doubles as the index of the manager in the state
	private static int managerCount = 0; //Two managersT exist by default, the GUI manager and Object manager
	
	/**A manager for extensions of the WarpedObject class
	 * @param name - the name for the manager (not significant, used for debugging)
	 * @implNote Will automatically add the manager to the warped state.
	 * @author 5som3*/
	public WarpedManager(String name) {
		this.name = name;
		UNIQUE_ID = managerCount;
		managerCount++;
		WarpedState.addManager(this);
	}

	/**Get the active Groups
	 * @return ArrayList<WarpedGroup<T>> - a list of the groups currently being updated
	 * @author 5som3*/
	public  ArrayList<WarpedGroup<T>> getActiveGroups(){return activeGroups;}
	
	
	
	/**Add a group to the manager
	 * @return WarpedGroup<T> - the newly created added group
	 * @author 5som3*/
	public WarpedGroup<T> addGroup() {
		WarpedGroupIdentity groupID = new WarpedGroupIdentity(UNIQUE_ID, groups.size());
		WarpedGroup<T> g = new WarpedGroup<T>(groupID);
		groups.add(g);
		return g;
	}
	
	/**Add a group to the manager
	 * @return WarpedGroup<T> - the newly created added group
	 * @author 5som3*/
	public WarpedGroup<T> addGroup(String name) {
		WarpedGroupIdentity groupID = new WarpedGroupIdentity(UNIQUE_ID, groups.size());
		WarpedGroup<T> g = new WarpedGroup<T>(groupID, name);
		groups.add(g);
		return g;
	}
	
	@SuppressWarnings("unchecked")
	/**Add a group of the specified class type.
	 * @param classType - the class of object that the group will contain.
	 * @apiNote It is up to you to ensure only object of the groups type are added to it.
	 * @apiNote Adding members of another class will cause a class cast exception when accessing them.
	 * @author 5som3*/
	public final <K extends T> WarpedGroup<K> addGroup(Class<K> classType) {
		WarpedGroupIdentity groupID = new WarpedGroupIdentity(UNIQUE_ID, groups.size());
		WarpedGroup<T> g = new WarpedGroup<T>(groupID);
		groups.add(g);
		return (WarpedGroup<K>) g;
	}
	
	/**Add a member to a group in this manager
	 * @param groupID - the ID of the group to added the member too
	 * @param member - the member to add to the group
	 * @author 5som3*/
	public WarpedObjectIdentity addMember(WarpedGroupIdentity groupID, T member) {return getGroup(groupID).addMember(member);}
	
	/**Open the group if it is closed, else closes the group.
	 * @author 5som3*/
	public final void toggleGroup(WarpedGroupIdentity groupID) {
		if(isOpen(groupID)) closeGroup(groupID);
		else openGroup(groupID);
	}
	
	/**Is the group open
	 * @param groupID - the group to check
	 * @return boolean - true if the group is open, else false
	 * @exception return false and print error if the group is not owned by this manager
	 * @implNote Open groups will be updated and rendered by active render methods
	 * @author 5som3*/
	public final boolean isOpen(WarpedGroupIdentity groupID) {
		if(!isOwner(groupID)) {
			Console.err("WarpedManager -> isGroupOpen() -> the group does not belong to this manager : " + name);
			Console.err(groupID.getString());
			return false;
		}
		for(int i = 0; i < activeGroups.size(); i++) if(activeGroups.get(i).getGroupID().isEqual(groupID)) return true;
		return false;
	}
	
	/**Is the manager the owner of this group
	 * @param groupID - the ID to check 
	 * @return boolean - true if the group exists in this manager else false
	 * @author 5som3*/
	public final boolean isOwner(WarpedGroupIdentity groupID) {if(groupID.getManagerID() == UNIQUE_ID) return true; else return false;}
	
	/**Is the manager the owner of the group that contains this object
	 * @param objectID - the ID of the object to check. 
	 * @return boolean - true if the group exists in this manager else false
	 * @author 5som3*/
	public final boolean isOwner(WarpedObjectIdentity objectID) {if(isOwner(objectID.getGroupID())) return true; else return false;}
	
	/**Opens the group if it is not already open
	 * @param group - the group to open
	 * @author 5som3*/
	public final synchronized void openGroup(WarpedGroup<?> group) {openGroup(group.getGroupID());}
	
	/**Opens the group if it is not already open
	 * @param groupID - the identity of the group to open
	 * @author 5som3*/
	public final synchronized void openGroup(WarpedGroupIdentity groupID) {if(isOwner(groupID) && !isOpen(groupID)) activeGroups.add(getGroup(groupID));}
	
	/**Open a list of groups
	 * @param identities - a list of the identities to add
	 * @author 5som3*/
	public final synchronized void openGroups(List<WarpedGroupIdentity> identities) {identities.forEach(i -> {openGroup(i);});}
	
	/**Open a list of groups
	 * @param identities - a list of the identities to add
	 * @author 5som3*/
	public final synchronized void openGroups(WarpedGroupIdentity... groupID) {openGroups(groupID);}
	
	/**Open all groups in the manager
	 * @author 5som3*/
	public final synchronized void openGroups() {
		for(int i = 0; i < groups.size(); i++) {
			WarpedGroup<T> group = groups.get(i);
			if(!isOpen(group.getGroupID())) activeGroups.add(group);
		}
	}
	
	/**Close all groups in the manager
	 * @author 5som3*/
	public final synchronized void closeGroups() {activeGroups.clear();}
	
	/**Close the group if it is not already closed
	 * @param group - the group to close
	 * @author 5som3*/
	public final synchronized void closeGroup(WarpedGroup<?> group) {closeGroup(group.getGroupID());}
	
	/**close a group
	 * @param groupID - the ID of the group to close.
	 * @author 5som3*/
	public final synchronized void closeGroup(WarpedGroupIdentity groupID) {
		if(isOwner(groupID)) {
			for(int i = 0; i < activeGroups.size(); i++) {
				if(activeGroups.get(i).isEqual(groupID)) activeGroups.remove(i);
			}
			Console.err("WarpedManager -> closeGroup() -> group is not open : " + groupID.getString());
		}
	}
	
	/**close a list of groups
	 * @param identities - a list of ID's to close
	 * @author 5som3*/
	public synchronized void closeGroups(List<WarpedGroupIdentity> identities) {identities.forEach(i -> {closeGroup(i);});}
	
	/**close a list of groups
	 * @param identities - a list of ID's to close
	 * @author 5som3*/
	public synchronized void closeGroups(WarpedGroupIdentity... identities) {closeGroups(identities);}
	
	/**Get the Unique ID for this manager
	 * @return int - the unique ID for this manager
	 * @implNote the uniqueID is also the index of the manager in the game state   
	 * @author 5som3*/
	public final int getManagerID() {return UNIQUE_ID;}
	
	/**The number of groups contained in this manager.
	 * @return int - the group count
	 * @author 5som3*/
	public final int size() {return groups.size();}
	
	/**The number of groups that are open in this manager.
	 * @return int - the number of groups that are open.
	 * @implNote open groups receive updates and are rendered by active render methods.
	 * @author 5som3*/
	public int getOpenGroupCount() {return activeGroups.size();}
	
	/**The number of objects contained by this manager
	 * @return int - the number of objects contained across all groups in this manager
	 * @author 5som3*/
	public int getObjectCount() {
		int subTotal = 0;
		
		for(int i = 0; i < groups.size(); i++) {
			subTotal += groups.get(i).size();
		}
		
		return subTotal;
	}
	
	/**The number of objects contained by open groups in this manager.
	 * @return int - the number of objects contained across all open groups in this manager
	 * @author 5som3*/
	public int getOpenObjectCount() {
		int subTotal = 0;
		for(int i = 0; i < activeGroups.size(); i++) {
			subTotal += activeGroups.get(i).size();
		}
		return subTotal;
	}
	
	/**Get a group from of objects from this manager.
	 * @param objectID - ID of an object belonging to the group to get.
	 * @return WarpedGroup<T> - The group that contains the specified object. 
	 * @author 5som3*/
	public WarpedGroup<T> getGroup(WarpedObjectIdentity objectID){return getGroup(objectID.getGroupID());}
	
	/**Get a group from this manager
	 * @param groupID - ID of the group to get.
	 * @return WarpedGroup<T> - The group that the specified ID refers to.
	 * @author 5som3*/
	public WarpedGroup<T> getGroup(WarpedGroupIdentity groupID){
		if(isOwner(groupID)) return groups.get(groupID.getGroupIndex());
		else { 
			Console.err("WarpedManager -> getGroup() -> group does not belong to this manager : " + name);
			groupID.printString();
			return null;
		}
	}
	
	//protected ArrayList<WarpedGroup<T>> getGroups() {return groups;}
	
	/**Get a member from a group owned by this manager
	 * @param objectID - the identity of the object to get
	 * @return T - a member of the type assigned to this manager
	 * @author 5som3*/
	public T getMember(WarpedObjectIdentity objectID) {
		if(isOwner(objectID)) return getGroup(objectID.getGroupID()).getMember(objectID); 
		else {
			Console.err("WarpedManager -> getMember() -> member does not belong to this manager : " + name);
			objectID.printString();
			return null;
		}
	}
	
	//public <K extends T> WarpedGroup<K> getGroupOf(WarpedGroupIdentity groupID, Class<K> classType){return getGroupOf(groupID.getGroupIndex(), classType);}
	
	/**Get the members of a group owned by this manager
	 * @param groupID - the ID of the group to get the members from.
	 * @return ArrayList<T> - any array containing the members of the specified group
	 * @author 5som3 */
	public ArrayList<T> getGroupMembers(WarpedGroupIdentity groupID){
		if(isOwner(groupID)) return getGroup(groupID).getMembers(); 
		else{
			Console.err("WarpedManager -> getGroupMembers() -> group does not belong to this manager : " + name);
			groupID.printString();
			return null;
		}
	}
	
	/**Applied to the objects within the group no the groups*/
	public void forEachGroup(WarpedObjectAction<T> method) {groups.forEach((g) ->{g.forEach(method);});}
	
	/** Executes the supplied ObjectAction for each WarpedObject in each active group.
	 * @param WarpedObjectAction - Will be applied to each object in each active group.
	 * @implNote The order of execution will be from 0 -> n - 1 where n is the length of activeGroups array.
	 * @implNote Each group will execute in the order 0 -> m - 1 where m is the length of members array for the corresponding group.
	 * @author SomeKid*/
	public void forEachActiveGroup(WarpedObjectAction<T> method) {for(int i = 0; i < activeGroups.size(); i++) activeGroups.get(i).forEach(method);}
	
	public boolean forEachActiveGroupReverse(WarpedObjectCheckAction<T> method) {
		for(int i = activeGroups.size() - 1; i > 0; i--) {
			if(activeGroups.get(i).forEachReverse(method)) return true;
		}
		return false;
	}
	

	/**Clears all data from the manager
	 * @apiNote Use open and close to control group updating. Do not clear groups to stop them from updating.
	 * @author 5som3*/
	public void clearGroups() {
		activeGroups.clear();
		groups.clear();
	}	
	
	/**Update 60 times per second*/
	protected final void updateActive() {for(int i = 0; i< activeGroups.size(); i++) activeGroups.get(i).updateActive();}
	
	/**Update once per second*/
	protected final void updateMid() {for(int i = 0; i< activeGroups.size(); i++) activeGroups.get(i).updateMid();}
	
	/**Update once per minute*/
	protected final void updateSlow() {for(int i = 0; i< activeGroups.size(); i++) activeGroups.get(i).updateSlow();}
	
	/**Update once per hour*/
	protected final void updatePassive() {for(int i = 0; i< activeGroups.size(); i++) activeGroups.get(i).updatePassive();}
	

	@SuppressWarnings("unchecked")
	/**Get the specified group cast as the declared classType.
	 * @param groupID - the id of the group to get.
	 * @param classType - the type to cast this group to.
	 * @apiNote It is up to you to ensure the group is cast to the type it contains.
	 * @apiNote If the group contains members that are not the same classType then class exception will occur when accessing those members.
	 * @author 5som3*/
	public final <K extends T> WarpedGroup<K> getGroupOf(WarpedGroupIdentity groupID, Class<K> classType) {
		int index = groupID.getGroupIndex();
		if(groupID.getManagerID() != UNIQUE_ID) {
			Console.err("WarpedManager -> getGroupOf() -> the group doesn't exist in " + name +", it exist in manager : " + groupID.getManagerID());
			return null;
		}
		if(groups.size() <= 0) {
			Console.err("ContextManager -> getGroupOf() -> There are no groups in : " + name);
			return null;
		}
		
		if(index < 0 || index >= groups.size()) {
			Console.err("ContextManager -> getGroupOf -> index is out side function domain : ( 0 < " + index + " < " + groups.size() + " )");
			return null;
		}
		
		if(groups.get(index) == null) {
			Console.err("ContextManager -> getGroupOf -> null group found in " + name + " at index " + index);
			return null;
		}
		
		if(groups.get(index).size() > 0 && groups.get(index).getMember(0).getClass() != classType) {
			Console.err("ContextManager -> getGroupOf -> group is not a gropu of " + classType);
			return null;
		}
			
		return (WarpedGroup<K>) groups.get(index);
	}
	

	
	

	 
	/*
	private void putActiveGroup(int index, Integer groupIndex) {
		WarpedConsole.err("ContextManager -> putActiveGroup() -> index, groupIndex : " + index + ", " + groupIndex);
		 
		if(FrameworkProperties.DEBUG) {			
			if(groupIndex < 0 || groupIndex >= groups.size()) {
				WarpedConsole.err("ContextManager -> putActiveGroup() ->  group does not exist in manager : " + groupIndex);
				return;
			} 
			
			if(groups.get(groupIndex) == null) {
				WarpedConsole.err("ContextManager -> putActiveGroup -> tried to add a null group to the manager");
				return;
			}
			
			if(index < 0 || index >= activeGroups.size()) { 
				WarpedConsole.err("ContextManager -> putActiveGroup() -> invalid index : " + index);
				return;
			}
		}
		if(activeGroups.contains(groupIndex)) activeGroups.remove(groupIndex);		
		activeGroups.add(index, groupIndex);		
	}
	


	


	 */
	
}
