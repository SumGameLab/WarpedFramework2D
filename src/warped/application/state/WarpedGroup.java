/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.util.ArrayList;

import warped.application.state.managers.gameObjectManagers.WarpedGroupAction;
import warped.application.state.managers.gameObjectManagers.WarpedObjectAction;
import warped.application.state.managers.gameObjectManagers.WarpedObjectCheckAction;
import warped.utilities.utils.Console;

public class WarpedGroup<T extends WarpedObject> {

	protected String name 			   = "default";
	protected ArrayList<T> members 	   = new ArrayList<>();
	protected final WarpedGroupIdentity groupID;
	/*
	//private VectorI pixelSize 		   = new VectorI();
	//private VectorI mapGridSize		   = new VectorI();
	//private VectorI memberSize 		   = new VectorI();
	 */
	//
	//private int memberCapacity		   = 0;
	//protected ArrayList<T> addQueue    = new ArrayList<>();
	//private ArrayList<WarpedObjectIdentity> objectIDs = new ArrayList<>();	
	//protected GroupUpdatePriorityType updatePriority = GroupUpdatePriorityType.ACTIVE;
	
	
	/**A group to contain extensions of WarpedObjects.
	 * @param groupID - the unique ID generated for this group by its manager.
	 * @author 5som3*/
	protected WarpedGroup(WarpedGroupIdentity groupID) {		
		this.groupID = groupID;
		name = "Group " + groupID.getManagerID() + ", " + groupID.getUniqueID();
	}
	
	/**A group to contain extensions of WarpedObjects.
	 * @param groupID - the unique ID generated for this group by its manager.
	 * @param name - the name of this group (non-significant, mainly used for debugging)
	 * @author 5som3*/
	protected WarpedGroup(WarpedGroupIdentity groupID, String name) {	
		this.groupID = groupID;
		this.name = name;
	}
	
	/**Add this group to its managers activeGroups array
	 * @implNote open groups will receive updates from their managers.
	 * @implNote open groups will be drawn by viewports in the render using render active methods. 
	 * @author 5som3*/
	public final void open() {groupID.openGroup();}
	/**Remove this group from its managers activeGroups array
	 * @implNote closed groups will not receive updates from their managers.
	 * @implNote closed groups will not be drawn by viewports unless specifically added to the pipeline 
	 * @author 5som3*/
	public final void close() {groupID.closeGroup();}
	
	/**Change the open/closed state of the group.
	 * @apiNote If open the group will close, if closed the group will open.
	 * @implNote open groups will receive updates from their managers, closed groups do not.
	 * @implNote open groups will be drawn by viewports in the render using render active methods, closed groups are not. 
	 * @author 5som3*/
	public final void toggle() {groupID.toggleGroup();}
	
	/*
	public void setMemberSize(VectorI memberSize) {this.memberSize = memberSize;}
	public void setMemberSize(int width, int height) {memberSize.set(width, height);}
	public void setMapSize(int width, int height) {mapGridSize.set(width, height);}
	public void setMapSize(VectorI mapSize) {this.mapGridSize = mapSize;}
	public void setPixelSize(VectorI size) {this.pixelSize = size;}
	public void setPixelSize(int x, int y) {pixelSize.set(x, y);}
	public VectorI getMapGridSize() {return mapGridSize;}
	/**This was added to be used specifically for tiles and tile maps,
	 * it may be useful if you wanted a context group to be clipped or contained within a specific bounds 
	 * @return The size of the tile map in pixels
	public VectorI getMapPixelSize() {return pixelSize;}
	/**This was added to be used specifically for tiles and tile maps,
	 * it may be useful if you wanted a context group to be clipped or contained within a specific bounds 
	 * @return The size of a single tile in pixels
	public VectorI getMemberSize() {return memberSize;}	
	*/
	
	
	/**The number of members in the group
	 * @return int - the member count.
	 * @author 5som3*/
	public int size() {return members.size();}
	
	/**The name assigned to this group.
	 * @return String - the name of the group.
	 * @implNote name is not significant, mainly used for debugging
	 * @author 5som3*/
	public String getName() {return name;}
	
	/**Get a specific member from this group.
	 * @param objectID - the id of the object to get.
	 * @return T - an object of the type assigned to this groups manager,
	 * @author 5som3*/
	public T getMember(WarpedObjectIdentity objectID) {
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
	public T getMember(int index) {return members.get(index);}
	
	
	/**Get the unique ID for this group.
	 * @return WarpedGroupIdentity - the unique ID for this group.
	 * @author 5som3*/
	public WarpedGroupIdentity getGroupID() {return groupID;}
	
	/**Get the list of members this group contains
	 * @return ArrayList<T> - the members of this group.
	 * @author 5som3*/
	public ArrayList<T> getMembers(){return members;}
	
	/**Set the interactivity of every member in the group
	 * @param isInteractive - if true each member will be interactive else they will be non-interactive 
	 * @author 5som3*/
	public void setInteractive(boolean isInteractive) {forEach(obj -> obj.setInteractive(isInteractive));}

	/**Is the group the same as this group
	 * @param group - the group to check
	 * @return boolean - true if the groups are the same else false.
	 * @author 5som3*/
	public boolean isEqual(WarpedGroup<?> group) {return isEqual(group.getGroupID());}
	
	/**Is the group the same as this group
	 * @param groupID - the ID of group to check
	 * @return boolean - true if the groups are the same else false.
	 * @author 5som3*/
	public boolean isEqual(WarpedGroupIdentity groupID) {if(this.groupID.isEqual(groupID)) return true; else return false;}
	
	
	
	/*
	public <K> ArrayList<K> getMembers(@SuppressWarnings("rawtypes") Class K){
		ArrayList<K> result = new ArrayList<>();
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).getClass().equals(K)) {				
				@SuppressWarnings("unchecked")
				K member = (K) members.get(i);
				result.add(member);
			} else WarpedConsole.err("ContextGroup -> getMembers(Class K) -> members are not of the cast type : " + K);
		}
		return result;
	}
	 */
	
	/**
	 * */
	//public ArrayList<WarpedObjectIdentity> getMemberIdentitys(){return objectIDs;}
	
	/**Is the member contained in this group
	 * @param member - the to check
	 * @return boolean - true if the member member is contained in the group else false
	 * @author 5som3*/
	public boolean contains(T member) {
		boolean result = false;
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).isEqual(member)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**Add a member to the group
	 * @param member - a member of the same type as the groups manager
	 * @author 5som3*/
	public WarpedObjectIdentity addMember(T member) {		
		WarpedObjectIdentity ID = new WarpedObjectIdentity(groupID, members.size());
		member.initObjectIdentity(ID);
		members.add(member);
		return ID;
	}
	
	/*
	public void addMember(T member, int index) {
		if(index < 0 || index > members.size()) {
			WarpedConsole.err("ContextGroup -> addMember() -> index is out of bounds : 0 < " +  index + " < " + members.size());
			return;
		}
		
		if(index == members.size()) {
			addMember(member);
			return;
		}
		
		WarpedObjectIdentity ID = new WarpedObjectIdentity(groupID.getManagerType(), groupID.getGroupIndex(), index);
		objectIDs.add(index, ID);
		member.overrideObjectIdentity(ID);
		//stack.add(index);
		members.add(index, member);
		for(int i = index + 1; i < objectIDs.size(); i++) objectIDs.get(i).shuffleRight();
	}
	
	
	public void replaceMember(int memberIndex, T member) {
		WarpedConsole.ln("ContextGroup -> replaceMember() -> replacing member at index : " + memberIndex);
		T old = members.get(memberIndex);
		member.initObjectIdentity(old.getObjectID());
		member.setPosition(old.getPosition());
		members.set(memberIndex, member);
	}
	
	public void swapMembers(WarpedObjectIdentity memberA, WarpedObjectIdentity memberB) {
		
		
	}
	 */




	/**Remove a member from the group
	 * @param memberID - the id of the member to remove
	 * @author 5som3*/
	public void removeMember(WarpedObjectIdentity memberID) {
		members.remove(memberID.getMemberIndex());
		for(int i = memberID.getMemberIndex(); i < members.size(); i++) members.get(i).getObjectID().shuffleLeft();
	}
	
	
	/**Clear all members from the group
	 * @author 5som3 */
	public void clearMembers() {members.clear();}
	
	
	/*
	@SuppressWarnings("unchecked")
	public <K extends WarpedObject> K getMemberAs(int index, Class<K> type) {
		if(members.size() <= 0) {
			WarpedConsole.err("ContextGroup -> getMember() -> group " + groupID.getManagerType() + " has no members");
			return null;
		}
		if(index < 0 || index >= members.size()) {
			WarpedConsole.err("ContextGroup -> getMember() -> tried to get index outside of the arrays domain : ( 0 <= " + index + " <= " + members.size() + " )");
			return null;
		}
		if(members.get(index) == null) {
			WarpedConsole.err("ContextGroup -> getMember() -> found a null member at (manager, group, index) : ( " + groupID.getManagerType() + ", " + groupID.getGroupIndex() + ", " + index + " )");
			return null;
		}
		return (K) members.get(index);
	}
	*/
	
	/*
	/**Execute the specified method for each of the objects in the group 
	 * @param method - the method to execute
	 * @author 5som3
	 * */
	public void forEach(WarpedObjectAction<T> method) {
		for(int i = 0; i < members.size(); i++) {
			method.action(members.get(i));
		}
	}
		
	public boolean forEachReverse(WarpedObjectCheckAction<T> method) {
		for(int i = members.size() - 1; i > 0; i--) {
			if(method.action(members.get(i))) return true;
		} 
		return false;
	}
	
	/**Execute the specified method for each of the objects in the group 
	 * @param method - the method to execute
	 * @author 5som3
	public void forEach(GameObjectAction method) {
		for(int i = 0; i < members.size(); i++) {
			method.action(members.get(i));
		}
	}
	 * */

	/**Update 60 times per second*/
	protected void updatePosition(double deltaTime) {
		for(int i = 0; i < members.size(); i++) {
			members.get(i).updatePosition(deltaTime);
		}
	}
	
	/**Update 60 times per second*/
	protected void updateActive() {
		for(int i = 0; i < members.size(); i++) {
			T member = members.get(i);
			if(!member.isAlive()) members.remove(i);
			else member.updateObject();
		}
	}
	
	/**Update once per second*/
	protected void updateMid() {for(int i = 0; i < members.size(); i++)	members.get(i).updateMid();}
	
	/**Update once per minute*/
	protected void updateSlow() {for(int i = 0; i < members.size(); i++) members.get(i).updateSlow();}

	/*
	/**Called after each update active cycle
	protected void removeDead() {
		for(int i = 0; i < members.size(); i++){
			if(!members.get(i).isAlive()) members.remove(i);
		}
	}
	 * */
	

	/**Will reset the member index for each objects ID in the group.
	 * If this is not called after the group order is resorted then the objectIDs will no long be accurate.
	 * @author 5som3 */
	protected void resetMemberOrder() {
		for(int i = 0; i < members.size(); i++) {
			T member = members.get(i);
			member.getObjectID().overrideMemberIndex(i);			
		}
	}
	
		
}
