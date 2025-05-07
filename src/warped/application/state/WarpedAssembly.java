/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import warped.utilities.utils.Console;

public abstract class WarpedAssembly {

	protected WarpedGroupIdentity groupID; 
	protected boolean isOpen = false;
	private boolean isAssembled = false;
	
	protected boolean isOpeningQueued = false;
	protected boolean isClosingQueued = false;

	/**An assembly manages a group of objects.
	 * @param manager - the manager that the assembly will create it's group in.
	 * @author 5som3*/
	public WarpedAssembly(WarpedManager<?> manager) {
		this.groupID = manager.addGroup().getGroupID();
	}
	
	/**Open the assemblys group of objects
	 * @implNote Open groups will receive updates from their manager and be rendered by ActiveRender methods  
	 * @author 5som3*/
	public void open() {
		if(!isOpen) {			
			WarpedState.openGroup(groupID);
			isOpen = true;
		}
	}

	/**Close the assemblys group of objects
	 * @implNote Closed groups won't receive updates from their manager or be rendered by ActiveRender methods  
	 * @author 5som3*/
	public void close() {
		if(isOpen) {			
			WarpedState.closeGroup(groupID);
			isOpen = false;
		}
	}

	/**Toggle the open/closed state of the this groups assembly.
	 * @author 5som3*/
	public final void toggle() {
		Console.ln("Assembly -> toggle() -> state : " + WarpedState.isGroupOpen(groupID));
		if(WarpedState.isGroupOpen(groupID)) close();
		else open();
	}
	
	/**Assemble the group using the defineAssembly() method.
	 * @implNote Assemblys can only be assembled once. 
	 * @implNote Once an assembly is assembled it should be 'ready to use' for the rest of the run time. 
	 * @author 5som3*/
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
			
	/**Is the group for this assembly currently open.
	 * @return boolean - true if the group is currently open else false.
	 * @author 5som3*/
	public final boolean isOpen() {return isOpen;}
	
	
	protected void updateAssembly() {return;}
	
	protected abstract void defineAssembly();
	protected abstract void addAssembly();
	

	
		
	
	
	
}
