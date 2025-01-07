/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.object;

import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.utilities.utils.Console;

public class WarpedObjectIdentity {
	
	private static int GameObjectCount = 1;
	
	private final WarpedManagerType managerType;
	private final int groupIndex;
	private int memberIndex;
	private final int uniqueIdentity;
	
	
	public WarpedObjectIdentity(WarpedManagerType managerID, int groupIndex, int index) {
		this.managerType = managerID;
		this.groupIndex = groupIndex;
		this.memberIndex = index;
		
		uniqueIdentity = GameObjectCount;
		GameObjectCount++;
	}
	
	public WarpedManagerType getManagerType() {return managerType;}
	public int getGroupIndex() {return groupIndex;}
	public int getMemberIndex() {return memberIndex;}
	public void shuffleLeft() {
		if(memberIndex == 0) Console.err("WarpedObjectIdentity -> shuffleLeft() -> member index already at 0");
		else memberIndex--;
	}
	public void shuffleRight() {memberIndex++;}
	
	public long getUniqueIdentity() {return uniqueIdentity;}
	
	public void println() {Console.ln("(manager, groupIndex, memberIndex) : (" + managerType.toString() + ", " + groupIndex + ", " + memberIndex + ") ");}
	public String getPrintln() {
		String result = "(manager, groupIndex, memberIndex) : (" + managerType.toString() + ", " + groupIndex + ", " + memberIndex + ") ";
		return result;
	}
	
	public boolean isEqualTo(WarpedObject obj) {return isEqualTo(obj.getObjectID());}
	public boolean isEqualTo(WarpedObjectIdentity objectID) {if(uniqueIdentity == objectID.getUniqueIdentity()) return true; else return false;}
	
	public void overrideMemberIndex(int memberIndex) {this.memberIndex = memberIndex;}
	
}
