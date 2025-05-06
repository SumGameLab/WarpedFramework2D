/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import warped.utilities.utils.Console;

public class WarpedGroupIdentity {
	
	
	/*WarpedGroupIdentity
	 * The purpose of warpedGroupIdentity is serve as a uniqueID for any group.
	 * The ID can be used to easily open or close a group.
	 * */
	
	
	private static int groupCount = 0;

	private final int UNIQUE_ID; //A unique number to identify each group
	private final int MANAGER_ID; //The unique ID for the manager that this group belongs to
	private final int GROUP_INDEX; //The index of this group in the manager
	
	/**A Group Identity is generated automatically by a WarpedManager every time a group is added
	 * @param managerID - the uniqueID of the manager that the added group belongs to
	 * @author 5som3*/
	protected WarpedGroupIdentity(int managerID, int groupIndex) {
		this.MANAGER_ID = managerID;
		this.GROUP_INDEX = groupIndex;
		groupCount++;
		UNIQUE_ID = groupCount;
	}
	
	/**The ID of the manager that this group belongs to
	 * @return int - the ID of the manager
	 * @implNote the uniqueID is also the index of the manager in the game state   
	 * @author 5som3*/
	public int getManagerID() {return MANAGER_ID;}
	
	/**The UNIQUE_ID of the group
	 * @return int - the unique ID assigned to the group when it was added to a manager
	 * @author 5som3*/
	public int getUniqueID() {return UNIQUE_ID;}
	
	/**The index of this group in the managers groups array.
	 * @return int - the index of this group
	 * @author 5som3*/
	public int getGroupIndex() {return GROUP_INDEX;}
	
	/**Is the group the same as this group
	 * @param WarpedGroupIdentity - the identity of the group to check
	 * @return boolean - True if the identitys match, else false. 
	 * @author 5som3*/
	public boolean isEqual(WarpedGroupIdentity groupID) {if(groupID.getUniqueID() == UNIQUE_ID) return true; else return false;}
	
	/**Print the ID to the console
	 * @author 5som3*/
	public void printString() {Console.err("(manager, groupIndex) : (" + MANAGER_ID + ", " + UNIQUE_ID + ") ");}
	
	/**Get the ID as a string
	 * @author 5som3*/
	public String getString() {
		String result = "(manager, groupIndex) : (" + MANAGER_ID + ", " + UNIQUE_ID + ") ";
		return result;
	}
	
	
	/**Remove this group from its managers activeGroups array
	 * @implNote closed groups will not receive updates from their managers.
	 * @implNote closed groups will not be drawn by viewports unless specifically added to the pipeline 
	 * @author 5som3*/
	public void closeGroup() {WarpedState.closeGroup(this);}
	
	/**Add this group to its managers activeGroups array
	 * @implNote open groups will receive updates from their managers.
	 * @implNote open groups will be drawn by viewports in the render using render active methods. 
	 * @author 5som3*/
	public void openGroup() {WarpedState.openGroup(this);}
	
}
