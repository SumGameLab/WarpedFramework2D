/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import warped.utilities.utils.Console;

public class WarpedObjectIdentity {
	
	private static long GameObjectCount = 1;
    private final WarpedGroupIdentity GROUP_ID;
    private final long UNIQUE_IDENTITY;
    
	private int memberIndex;
	
	/**Generate an identity for a new object
	 * @param groupID - the identity of the group that is adding the object
	 * @param index - the index that the object will be added in the groups member array
	 * @author 5som3*/
	protected WarpedObjectIdentity(WarpedGroupIdentity groupID, int index) {
		GROUP_ID = groupID;
		this.memberIndex = index;
		
		UNIQUE_IDENTITY = GameObjectCount;
		GameObjectCount++;
	}
	
	/**Generate an identity to override the ID of an existing object
	 * @param WarpedGroupID - the ID of the group that will contain the object
	 * @param objectID - the ID that is being replaced
	 * @param index - the index that the object will be added in the groups member array
	 * @author 5som3*/
	protected WarpedObjectIdentity(WarpedGroupIdentity groupID, WarpedObjectIdentity objectID, int index) {
		GROUP_ID = groupID;
		UNIQUE_IDENTITY = objectID.getUniqueID();
		this.memberIndex = index;
	}
	
	/**The Identity of the group that this member belongs to
	 * @return WarpedGroupIdentity - the ID the parent group
	 * @author 5som3*/
	public WarpedGroupIdentity getGroupID() {return GROUP_ID;}
	
	/**The index of the member in the group that contains it
	 * @return int - the index 
	 * @author 5som3*/
	public int getMemberIndex() {return memberIndex;}

	/**print the identity to the console
	 * @author 5som3*/
	public void printString() {Console.ln("(managerID, groupID, memberID) : (" + GROUP_ID.getManagerID() + ", " + GROUP_ID.getUniqueID() + ", " + UNIQUE_IDENTITY + " )");}
	
	/**Get the full identity of this object as a string
	 * @return String - the identity as a string
	 * @author 5som3*/
	public String getString() {return "(managerID, groupID, memberID) : (" + GROUP_ID.getManagerID() + ", " + GROUP_ID.getUniqueID() + ", " + UNIQUE_IDENTITY + " )";}
	
	/**Does the objects ID match this ID
	 * @return boolean - true if the objects ID matches this ID
	 * @author 5som3*/
	public boolean isEqualTo(WarpedObject obj) {return isEqualTo(obj.getObjectID());}
	
	/**Does the ID match this ID
	 * @return boolean - true of the ID's match
	 * @author 5som3 */
	public boolean isEqualTo(WarpedObjectIdentity objectID) {if(UNIQUE_IDENTITY == objectID.getUniqueID()) return true; else return false;}
	
	/**Get the unique id assigned to this object
	 * @return long - the unique ID
	 * @author 5som3*/
	public long getUniqueID() {return UNIQUE_IDENTITY;}
	
	
	/**Call when removing a member left than this one*/
	protected void shuffleLeft() {
		if(memberIndex == 0) Console.err("WarpedObjectIdentity -> shuffleLeft() -> member index already at 0");
		else memberIndex--;
	}
	
	/**Call when adding a member left of this one*/
	protected void shuffleRight() {memberIndex++;}
	
	/**Override the index of the member in the group that contains it*/
	protected void overrideMemberIndex(int memberIndex) {this.memberIndex = memberIndex;}
	
	
}
