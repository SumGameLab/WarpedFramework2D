/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import warped.utilities.utils.Console;

public abstract class WarpedAssembly {

	protected WarpedGroupIdentity groupID; 
	protected boolean isOpen = false;
	private boolean isAssembled = false;
	
	protected boolean isOpeningQueued = false;
	protected boolean isClosingQueued = false;

	
	public WarpedAssembly(WarpedManager<?> manager) {
		this.groupID = manager.addGroup().getGroupID();
	}
	
	public void open() {
		if(!isOpen) {			
			WarpedState.openGroup(groupID);
			isOpen = true;
		}
	}

	public void close() {
		if(isOpen) {			
			WarpedState.closeGroup(groupID);
			isOpen = false;
		}
	}

	public final void toggle() {
		Console.ln("Assembly -> toggle() -> state : " + WarpedState.isGroupOpen(groupID));
		if(WarpedState.isGroupOpen(groupID)) close();
		else open();
	}
	
	public final void assemble() {
		if(isAssembled) {
			Console.err("WarpedAssembly -> assemble() -> assembly has already been assembled");
			return;
		} 
		isAssembled = true;
		WarpedState.assemblys.add(this);
		defineAssembly();
		addAssembly();
	}
	
		
		
	public final boolean isOpen() {return isOpen;}
	
	protected void updateAssembly() {return;}
	
	protected abstract void defineAssembly();
	protected abstract void addAssembly();
	

	
		
	
	
	
}
