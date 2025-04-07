/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.groups;

import java.util.ArrayList;

import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.state.managers.gameObjectManagers.GameObjectAction;
import warped.application.state.managers.gameObjectManagers.WarpedObjectAction;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class WarpedGroup<T extends WarpedObject> {

	/**	CONTEXT GROUPS
	 * must have a groupID to be valid
	 * is the ONLY class that can create GameObjectIdentitys
	 * without a groupID it is not possible to create a gameObjectID 
	 * 
	 * MAKE SURE you use the initGroupIdentity() method when adding a group to a ContextManager
	 * */

	/**Size - TileManager
	 * Size was added here specifically for use with the tile manager
	 * using size with other managers is optional but I haven't considered its implications fully*/
	protected String name 			   = "default";
	private VectorI pixelSize 		   = new VectorI();
	private VectorI mapGridSize		   = new VectorI();
	private VectorI memberSize 		   = new VectorI();
	private int memberCapacity		   = 0;
	protected ArrayList<T> members 	   = new ArrayList<>();
	protected ArrayList<T> addQueue    = new ArrayList<>();
	protected WarpedGroupIdentity groupID;
	private boolean hasID = false;
	
	protected GroupUpdatePriorityType updatePriority = GroupUpdatePriorityType.ACTIVE;
	
	private ArrayList<WarpedObjectIdentity> objectIDs = new ArrayList<>();
	
	
	//--------
	//---------------- Constructors / initialization --------
	//--------
	public WarpedGroup() {}
	public WarpedGroup(String name) {this.name = name;}
	public void initGroupIdentity(WarpedGroupIdentity groupID) {
		if(hasID) {
			Console.err("ContextGroup -> initGroupIdentity -> group has already been identified as (manager, index) : ( " + groupID.getManagerType() + ", " + groupID.getGroupIndex() + " )");
			return;
		}
		if(name.equals("default")) name = groupID.getManagerType() + " ContextGroup : " + groupID.getGroupIndex();
		this.groupID = groupID;
		hasID = true;
	}
	
	
	//--------
	//---------------- Access --------
	//--------
	//Functions for the tile manager -> 
	//TODO -> 4/2/24 should make an extension of the contextGroup for tiles and move these functions there as they are specificaly for the tile manager
	public void setMemberSize(VectorI memberSize) {this.memberSize = memberSize;}
	public void setMemberSize(int width, int height) {memberSize.set(width, height);}
	public void setMapSize(int width, int height) {mapGridSize.set(width, height);}
	public void setMapSize(VectorI mapSize) {this.mapGridSize = mapSize;}
	public void setPixelSize(VectorI size) {this.pixelSize = size;}
	public void setPixelSize(int x, int y) {pixelSize.set(x, y);}
	public GroupUpdatePriorityType getUpdatePriority() {return updatePriority;}
	public void setUpdatePriority(GroupUpdatePriorityType updatePriority) {this.updatePriority = updatePriority;}
	/**This was added to be used specifically for tiles and tile maps,
	 * it may be useful if you wanted a context group to be clipped or contained within a specific bounds 
	 * @return The size of the map in tile precision i.e. 10 x 10 tile map*/
	public VectorI getMapGridSize() {return mapGridSize;}
	/**This was added to be used specifically for tiles and tile maps,
	 * it may be useful if you wanted a context group to be clipped or contained within a specific bounds 
	 * @return The size of the tile map in pixels*/
	public VectorI getMapPixelSize() {return pixelSize;}
	/**This was added to be used specifically for tiles and tile maps,
	 * it may be useful if you wanted a context group to be clipped or contained within a specific bounds 
	 * @return The size of a single tile in pixels*/
	public VectorI getMemberSize() {return memberSize;}	
	
	public int size() {return members.size();}
	public String getName() {return name;}
	public T getMember(WarpedObjectIdentity objectID) {return getMember(objectID.getMemberIndex());}
	public WarpedGroupIdentity getGroupID() {return groupID;}
	public int getMemberCount() {return members.size();}
	public ArrayList<T> getMembers(){return members;}	
	public <K> ArrayList<K> getMembers(@SuppressWarnings("rawtypes") Class K){
		ArrayList<K> result = new ArrayList<>();
		for(int i = 0; i < members.size(); i++) {
			if(members.get(i).getClass().equals(K)) {				
				@SuppressWarnings("unchecked")
				K member = (K) members.get(i);
				result.add(member);
			} else Console.err("ContextGroup -> getMembers(Class K) -> members are not of the cast type : " + K);
		}
		return result;
	}
	public ArrayList<WarpedObjectIdentity> getMemberIdentitys(){return objectIDs;}
	
	public void setInteractive(boolean isInteractive) {forEach(obj -> obj.setInteractive(isInteractive));}
	
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
	
	//--------
	//---------------- Member Management --------
	//--------
	
	public WarpedObjectIdentity addMember(T member) {
		if(!hasID) {
			Console.err("ContextGroup -> addMember() -> groupID is null, can not add member without a groupID");
			return null;
		}
		if(memberCapacity > 0 && members.size() > memberCapacity) {
			Console.err("ContextGroup -> addMember() -> group is at capacity : " + name);
			return null;
		}
		
		
		WarpedObjectIdentity ID = new WarpedObjectIdentity(groupID.getManagerType(), groupID.getGroupIndex(), members.size());
		objectIDs.add(ID);
		member.initObjectIdentity(ID);
		members.add(member);
		return ID;
	}
	
	public void addMember(T member, int index) {
		if(index < 0 || index > members.size()) {
			Console.err("ContextGroup -> addMember() -> index is out of bounds : 0 < " +  index + " < " + members.size());
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
	
	private void overrideMember(int index, T member) {
		member.overrideObjectIdentity(new WarpedObjectIdentity(groupID.getManagerType(), groupID.getGroupIndex(), index));
		objectIDs.set(index, member.getObjectID());
		//stack.set(index, member.getObjectID().getMemberIndex());
		members.set(index, member);
	}	
	
	public void replaceMember(int memberIndex, T member) {
		Console.ln("ContextGroup -> replaceMember() -> replacing member at index : " + memberIndex);
		T old = members.get(memberIndex);
		member.initObjectIdentity(old.getObjectID());
		member.setPosition(old.getPosition());
		members.set(memberIndex, member);
	}
	
	public void resetMemberOrder(ArrayList<T> members) {
		if(members.size() != this.members.size()) {
			Console.err("ContextGroup -> resetMemberOrder() -> members sizes do not match (parameter size, this size) :  ( " + members.size() + ", " + this.members.size() + ") " );
			return;
		}
		for(int i = 0; i < members.size(); i++) {
			T member = members.get(i);
			if(member.getObjectID().getManagerType() != groupID.getManagerType()) {
				Console.err("ContextGroup -> resetMemberOrder() -> member is not of this managers type : " + member.getObjectID().getManagerType() + ", " + groupID.getManagerType());
				return;
			}
			overrideMember(i, member);
		}
	}
	
	public void swapMembers(WarpedObjectIdentity memberA, WarpedObjectIdentity memberB) {
		
		
	}
	
	public void removeMember(int index) {removeMember(members.get(index).getObjectID());}
	
	public void removeMember(WarpedObjectIdentity memberID) {
		members.remove(memberID.getMemberIndex());
		objectIDs.remove(memberID.getMemberIndex());
		
		for(int i = memberID.getMemberIndex(); i < objectIDs.size(); i++) objectIDs.get(i).shuffleLeft();
	}
	
	public void clearMembers() {members.clear();}
	
	public void remove() {
		for(int i = 0; i < members.size(); i++){
			if(!members.get(i).isAlive()) members.remove(i);
		}
	}
	
	public T getMember(int index) {
		if(members.size() <= 0) {
			Console.err("ContextGroup -> getMember() -> group " + groupID.getManagerType() + " has no members");
			return null;
		}
		if(index < 0 || index >= members.size()) {
			Console.err("ContextGroup -> getMember() -> tried to get index outside of the arrays domain : ( 0 <= " + index + " <= " + members.size() + " )");
			return null;
		}
		if(members.get(index) == null) {
			Console.err("ContextGroup -> getMember() -> found a null member at (manager, group, index) : ( " + groupID.getManagerType() + ", " + groupID.getGroupIndex() + ", " + index + " )");
			return null;
		}
		return members.get(index);
	}
	
	@SuppressWarnings("unchecked")
	public <K extends WarpedObject> K getMemberAs(int index, Class<K> type) {
		if(members.size() <= 0) {
			Console.err("ContextGroup -> getMember() -> group " + groupID.getManagerType() + " has no members");
			return null;
		}
		if(index < 0 || index >= members.size()) {
			Console.err("ContextGroup -> getMember() -> tried to get index outside of the arrays domain : ( 0 <= " + index + " <= " + members.size() + " )");
			return null;
		}
		if(members.get(index) == null) {
			Console.err("ContextGroup -> getMember() -> found a null member at (manager, group, index) : ( " + groupID.getManagerType() + ", " + groupID.getGroupIndex() + ", " + index + " )");
			return null;
		}
		return (K) members.get(index);
	}
	
	
	//--------
	//---------------- Update --------
	//--------
	public void updateActive() {
		if(updatePriority.ordinal() < GroupUpdatePriorityType.ACTIVE.ordinal()) return;
		for(int i = 0; i < members.size(); i++) {
			members.get(i).updateActively();
		}
	}
	
	public void updateMid() {
		if(updatePriority.ordinal() < GroupUpdatePriorityType.MID.ordinal()) return;
		for(int i = 0; i < members.size(); i++) {
			WarpedObject member = members.get(i);
			member.updateActively();
			member.updateMid();
		}
	}
	
	public void updateSlow() {
		if(updatePriority.ordinal() < GroupUpdatePriorityType.SLOW.ordinal()) return;
		for(int i = 0; i < members.size(); i++) {
			WarpedObject member = members.get(i);
			member.updateActively();
			member.updateMid();
			member.updateSlow();
		}
	}
	
	public void updatePassive() {
		for(int i = 0; i < members.size(); i++) {
			WarpedObject member = members.get(i);
			member.updateActively();
			member.updateMid();
			member.updateSlow();
			member.updatePassive();
		}
	}
	
	
	//--------
	//---------------- Iterators --------
	//--------
	public void forEach(WarpedObjectAction<T> method) {
		//members.forEach(obj -> {method.action(obj);});
		for(int i = 0; i < members.size(); i++) {
			method.action(members.get(i));
		}
	}
	
	public void forEachIndexed(GameObjectAction method) {
		for(int i = 0; i < members.size(); i++) {
			method.action(members.get(i));
		}
	}
	
}
