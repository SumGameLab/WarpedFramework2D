/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.groups;

import java.util.ArrayList;

import warped.application.object.WarpedObjectIdentity;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.utilities.utils.Console;

public class WarpedGroupIdentity {

	/** ContextGroups
	 * will not be moved or removed from their array once created
	 * this is to stop invalidation of the groupIndex and managerID
	 * */
	
	private static int ContextGroupCount = 1;

	private final WarpedManagerType managerType;
	private final int groupIndex;
	private final int uniqueIdentity;
	
	
	public WarpedGroupIdentity(WarpedManagerType managerType, int groupIndex) {
		this.managerType = managerType;
		this.groupIndex = groupIndex;
		
		uniqueIdentity = ContextGroupCount;
		ContextGroupCount++;
	}
	
	public WarpedManagerType getManagerType() {return managerType;}
	public int getGroupIndex() {return groupIndex;}
	
	public int getUniqueIdentity() {return uniqueIdentity;}
	public boolean isEqual(WarpedGroupIdentity groupID) {if(groupID.getUniqueIdentity() == uniqueIdentity) return true; else return false;}
	
	public void println() {Console.ln("(manager, groupIndex) : (" + managerType.toString() + ", " + groupIndex + ") ");}
	public String getString() {
		String result = "(manager, groupIndex) : (" + managerType.toString() + ", " + groupIndex + ") ";
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<WarpedObjectIdentity> getMemberIDs(){return WarpedState.getManager(this).getGroup(this).getMemberIdentitys();}
	
	public void closeGroup() {
		WarpedState.closeGroup(this);
	}
	public void openGroup() {
		WarpedState.openGroup(this);
	}
	
	//public WarpedGroup<?> getGroup(){return WarpedState.getManager(managerType).getGroup(groupIndex);}
}
