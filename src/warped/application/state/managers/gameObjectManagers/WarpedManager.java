/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import java.util.ArrayList;

import warped.application.depthFields.WarpedDepthField;
import warped.application.entities.WarpedEntitie;
import warped.application.entities.item.WarpedItem;
import warped.application.gui.WarpedGUI;
import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.prop.WarpedProp;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.tile.WarpedTile;
import warped.utilities.utils.Console;

public class WarpedManager<T extends WarpedObject> {
 
	
	/**	CONTEXT MANAGERS
	 * must have a manager type
	 * are the only class that can create ContextGroupIdentitys
	 * */

	//TODO 18/1/24 ->  Implement a system so that groups and members of groups can be removed
	/*		Currently when a member of a group is removed the following objects in that array will have their GameObjectIdentitys invalidated as their index will change
	 * 		To fix this we need to keep a list of all the GameObjectIdentities for each gameObject and change the index in the identitie when the object is moved in the array
	 * 		this is so that copies of the GameObjectIdentity can all be updated simultaneously when groups or members are removed
	 * 		 
	 * */
	protected boolean isUpdating = false;
	protected WarpedManagerType managerType;
	protected ArrayList<WarpedGroup<T>> groups = new ArrayList<>();	
	protected ArrayList<WarpedGroup<T>> activeGroups = new ArrayList<>();																					
	
	public static WarpedManager<WarpedObject> generateObjectManager(){return new WarpedManager<WarpedObject>();}
	public static ManagerEntitie<WarpedEntitie> generateEntitieManager(){return new ManagerEntitie<WarpedEntitie>();}
	public static ManagerDepthField<WarpedDepthField> generateDepthFieldManager(){return new ManagerDepthField<WarpedDepthField>();}
	public static ManagerGUI<WarpedGUI> generateGUIManager(){return new ManagerGUI<WarpedGUI>();}
	public static ManagerTile<WarpedTile> generateTileManager(){return new ManagerTile<WarpedTile>();}
	public static ManagerVisualEffect<WarpedProp> generateVisualEffectManager(){
		ManagerVisualEffect<WarpedProp> manager = new ManagerVisualEffect<WarpedProp>(); 
		manager.init();
		return manager;
	}
	
	public  ArrayList<WarpedGroup<T>> getActiveGroups(){return activeGroups;}
	public static ManagerItem<WarpedItem> generateItemManager(){return new ManagerItem<WarpedItem>();}
	
	protected WarpedManager() {
		managerType = WarpedManagerType.OBJECT;
	}
	
	public WarpedGroupIdentity addGroup() {
		WarpedGroup<T> g = new WarpedGroup<>();
		return addGroup(g);
	}
	public WarpedGroupIdentity addGroup(String name) {
		WarpedGroup<T> g = new WarpedGroup<>(name);
		return addGroup(g);
	}
	
	public WarpedObjectIdentity addMember(WarpedGroupIdentity groupID, T member) {return addMember(groupID.getGroupIndex(), member);}
	public WarpedObjectIdentity addMember(WarpedObjectIdentity objectID, T member) {return addMember(objectID.getGroupIndex(), member);}
	public void replaceMember(WarpedObjectIdentity objectID, T member) {replaceMember(objectID.getGroupIndex(), objectID.getMemberIndex(), member);}
	
	public void toggleGroup(WarpedGroupIdentity groupID) {
		if(isGroupOpen(groupID)) closeGroup(groupID);
		else openGroup(groupID);
	}
	
	public boolean isGroupOpen(WarpedGroupIdentity groupID) {
		if(!hasGroup(groupID)) {
			Console.err("ContextManager -> isGroupActive() -> group does not exist in manager : " + groupID.getString());
			return false;
		}
		for(int i = 0; i < activeGroups.size(); i++) if(activeGroups.get(i).getGroupID().isEqual(groupID)) return true;
		return false;
	}
	
	public synchronized void openGroup(WarpedGroupIdentity groupID) {addActiveGroup(groupID.getGroupIndex());}
	public synchronized void openGroups(ArrayList<WarpedGroupIdentity> identitys) {
		if(!hasGroups(identitys)) {
			Console.err("ContextManager -> removeActiveGroups() -> doesn't have group");
			return;
		}
		identitys.forEach(id -> {addActiveGroup(id.getGroupIndex());});	
	}
	
	public synchronized void closeGroup(WarpedGroupIdentity groupID) {removeActiveGroup(groupID.getGroupIndex());}
	public synchronized void closeGroups(ArrayList<WarpedGroupIdentity> identitys) {
		if(!hasGroups(identitys)) {
			Console.err("ContextManager -> removeActiveGroups() -> doesn't have group");
			return;
		}	
		identitys.forEach(id -> {removeActiveGroup(id.getGroupIndex());});
	}
	
	public WarpedManagerType getManagerType() {return managerType;}	
	public int getGroupCount() {return groups.size();}
	public int getActiveGroupCount() {return activeGroups.size();}
	public int getGameObjectCount() {
		int subTotal = 0;
		
		for(int i = 0; i < groups.size(); i++) {
			subTotal += groups.get(i).getMemberCount();
		}
		
		return subTotal;
	}
	public int getActiveGameObjectCount() {
		int subTotal = 0;
		for(int i = 0; i < activeGroups.size(); i++) {
			subTotal += activeGroups.get(i).getMemberCount();
		}
		return subTotal;
	}
	public T getMember(WarpedObjectIdentity objectID) {return groups.get(objectID.getGroupIndex()).getMember(objectID);}
	public ArrayList<WarpedGroup<T>> getGroups() {return groups;}
	public WarpedGroup<T> getGroup(WarpedGroupIdentity groupID){return getGroup(groupID.getGroupIndex());}
	public <K extends T> WarpedGroup<K> getGroupOf(WarpedGroupIdentity groupID, Class<K> classType){return getGroupOf(groupID.getGroupIndex(), classType);}
	public ArrayList<T> getGroupMembers(WarpedGroupIdentity groupID){return getGroup(groupID.getGroupIndex()).getMembers();}
	public WarpedGroup<T> getGroup(WarpedObjectIdentity objectID){return getGroup(objectID.getGroupIndex());}
	
	
	public void updateActive() {
		for(int i = 0; i< activeGroups.size(); i++) {
			activeGroups.get(i).updateActive();	
		}
	}
	public void updateMid() {
		 for(int i = 0; i< activeGroups.size(); i++) {
			 	activeGroups.get(i).updateMid();	
			}
	}
	public void updateSlow() {
		for(int i = 0; i< activeGroups.size(); i++) {	
			activeGroups.get(i).updateSlow();
		}
	}
	public void updatePassive() {
		for(int i = 0; i< activeGroups.size(); i++) {
			activeGroups.get(i).updatePassive();	
		}
	}
	
	public void remove() {
		for(int i = 0; i < activeGroups.size(); i++) {
			activeGroups.get(i).remove();
		}
		//activeGroups.forEach(index -> {groups.get(index).remove();});
	}

	/**Applied to the objects within the group no the groups*/
	public void forEachGroup(WarpedObjectAction<T> method) {groups.forEach((g) ->{g.forEach(method);});}
	public void forEachActiveGroup(WarpedObjectAction<T> method) {
		for(int i = 0; i < activeGroups.size(); i++) activeGroups.get(i).forEach(method);
	}

	
	
	public void clearGroups() {
		activeGroups.clear();
		groups.clear();
	}	


	private boolean hasGroup(WarpedGroupIdentity groupID) {
		if(groupID.getGroupIndex() < 0) {
			Console.err("ContextManger -> hasGroup() -> inavlid group index : " + groupID.getString());
			return false;
		}
		if(groupID.getGroupIndex() > groups.size()) {
			Console.err("ContextManager -> hasGroup() -> group index is larger than number of groups : " + groupID.getString());
			return false;
		}	
		return true;
	}
	
	private boolean hasGroups(ArrayList<WarpedGroupIdentity> identitys) {
		for(int i = 0; i < identitys.size(); i++) {
			if(identitys.get(i) == null) {
				Console.err("ContextManager -> setActiveGroups -> identitys contains a null value at index : " + i);
				return false;
			}
			if(identitys.get(i).getGroupIndex() < 0 || identitys.get(i).getGroupIndex() > groups.size()) {
				Console.err("ContextManager -> setActiveGroups -> invalid identity (index, identity)  :  (" + i + ", " + identitys.get(i).getGroupIndex());
				return false;
			}
		}
		return true;
	}
	
	private WarpedGroupIdentity addGroup(WarpedGroup<T> group) {
		WarpedGroupIdentity groupID = new WarpedGroupIdentity(managerType, groups.size());
		group.initGroupIdentity(groupID);
		groups.add(group);
		return groupID;
	}
	
	private WarpedObjectIdentity addMember(int groupIndex, T member) { 
		if(groups.size() <= 0) {
			Console.err("ContextManager -> addMember() -> no groups exist in " + managerType + " to add a member too");
			return null;
		}
		
		if(groupIndex < 0 || groupIndex > groups.size()){
			Console.err("ContextManager -> addMember() -> index param is outside of function domain : ( 0 <= " + groupIndex + " < " + groups.size() + " )" );
			return null;
		}
		
		if(groups.get(groupIndex) == null) {
			Console.err("ContextManager -> addMember() -> null group found : " + managerType + " -> null group at index : " + groupIndex);
			return null;
		}
		return groups.get(groupIndex).addMember(member);
	}
	

	private void replaceMember(int groupIndex, int memberIndex, T member) { 
		if(groups.size() <= 0) {
			Console.err("ContextManager -> addMember() -> no groups exist in " + managerType + " to add a member too");
			
		}
		
		if(groupIndex < 0 || groupIndex > groups.size()){
			Console.err("ContextManager -> addMember() -> index param is outside of function domain : ( 0 <= " + groupIndex + " < " + groups.size() + " )" );
			
		}
		
		if(groups.get(groupIndex) == null) {
			Console.err("ContextManager -> addMember() -> null group found : " + managerType + " -> null group at index : " + groupIndex);
			
		}
		groups.get(groupIndex).replaceMember(memberIndex, member);
	}
	 
	/*
	private void putActiveGroup(int index, Integer groupIndex) {
		Console.err("ContextManager -> putActiveGroup() -> index, groupIndex : " + index + ", " + groupIndex);
		 
		if(FrameworkProperties.DEBUG) {			
			if(groupIndex < 0 || groupIndex >= groups.size()) {
				Console.err("ContextManager -> putActiveGroup() ->  group does not exist in manager : " + groupIndex);
				return;
			} 
			
			if(groups.get(groupIndex) == null) {
				Console.err("ContextManager -> putActiveGroup -> tried to add a null group to the manager");
				return;
			}
			
			if(index < 0 || index >= activeGroups.size()) { 
				Console.err("ContextManager -> putActiveGroup() -> invalid index : " + index);
				return;
			}
		}
		if(activeGroups.contains(groupIndex)) activeGroups.remove(groupIndex);		
		activeGroups.add(index, groupIndex);		
	}
	*/
	
	private void addActiveGroup(int groupIndex) {
		if(groupIndex < 0 || groupIndex >= groups.size()) {
			Console.err("ContextManager -> addActiveGroup() ->  group does not exist in manager : " + groupIndex);
			return;
		} 
		
		WarpedGroup<T> group = groups.get(groupIndex);
		
		if(group == null) {
			Console.err("ContextManager -> addActiveGroup -> tried to add a null group to the manager");
			return;
		}
		
		if(activeGroups.contains(group)) {
			Console.err("ContextManager -> addActiveGroup() -> group already active : " + groupIndex);
			return;
		}
		activeGroups.add(group);			
	}
	
	public WarpedGroup<T> getGroup(int index) {
		if(groups.size() <= 0) {
			Console.err("ContextManager -> getGroup() -> There are no groups in : " + managerType);
			return null;
		}
		
		if(index < 0 || index >= groups.size()) {
			Console.err("ContextManager -> getGroup -> index is out side function domain : ( 0 < " + index + " < " + groups.size() + " )");
			return null;
		}
		
		if(groups.get(index) == null) {
			Console.err("ContextManager -> getGroup -> null group found in " + managerType + " at index " + index);
			return null;
		}
		return groups.get(index);
	}
	

	@SuppressWarnings("unchecked")
	protected <K extends T> WarpedGroup<K> getGroupOf(int index, Class<K> classType) {
		if(groups.size() <= 0) {
			Console.err("ContextManager -> getGroupOf() -> There are no groups in : " + managerType);
			return null;
		}
		
		if(index < 0 || index >= groups.size()) {
			Console.err("ContextManager -> getGroupOf -> index is out side function domain : ( 0 < " + index + " < " + groups.size() + " )");
			return null;
		}
		
		if(groups.get(index) == null) {
			Console.err("ContextManager -> getGroupOf -> null group found in " + managerType + " at index " + index);
			return null;
		}
		
		if(groups.get(index).size() > 0 && groups.get(index).getMember(0).getClass() != classType) {
			Console.err("ContextManager -> getGroupOf -> group is not a gropu of " + classType);
			return null;
		}
			
		return (WarpedGroup<K>) groups.get(index);
	}
	
	protected void removeActiveGroup(int groupIndex) {
		if(groupIndex < 0) {
			Console.err("ContextManager -> removeActiveGroup() -> tried to remove a negative index : " + groupIndex);
			return;
		}
		
		if(!activeGroups.contains(groups.get(groupIndex))) {
			Console.err("ContextManager -> removeActiveGroup() -> There is no active group (" + groupIndex + ") to be removed");
			return;
		}
		for(int i = 0; i < activeGroups.size(); i++) {
			if(activeGroups.get(i).getGroupID().getGroupIndex() == groupIndex) activeGroups.remove(i);
		}
	}
	
}
