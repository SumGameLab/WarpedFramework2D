/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import warped.application.entities.WarpedEntitie;
import warped.application.gui.WarpedGUI;
import warped.application.object.WarpedObject;
import warped.application.prop.WarpedProp;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.utilities.utils.Console;

public abstract class WarpedAssembly {

	protected WarpedGroupIdentity groupID; 
	protected boolean isOpen = false;
	private boolean isAssembled = false;
	
	protected boolean isOpeningQueued = false;
	protected boolean isClosingQueued = false;

	
	public WarpedAssembly(WarpedManagerType type) {
		this.groupID = WarpedState.getManager(type).addGroup();
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
		offsetAssembly();
		defineAssembly();
		addAssembly();
	}
	
	
	@SuppressWarnings("unchecked")
	protected final void addMember(WarpedObject obj) {
		if(obj instanceof WarpedGUI && groupID.getManagerType() != WarpedManagerType.GUI) {
			Console.err("Assembly -> addMember() -> tried to add a member of the wrong type, this assembly only accepts " + groupID.getManagerType().toString() + " type of object");
			return;
		}
		if(obj instanceof WarpedEntitie && groupID.getManagerType() != WarpedManagerType.ENTITIE) {
			Console.err("Assembly -> addMember() -> tried to add a member of the wrong type, this assembly only accepts " + groupID.getManagerType().toString() + " type of object");
			return;
		}
		if(obj instanceof WarpedProp && groupID.getManagerType() != WarpedManagerType.VFX) {
			Console.err("Assembly -> addMember() -> tried to add a member of the wrong type, this assembly only accepts " + groupID.getManagerType().toString() + " type of object");
			return;
		}
		WarpedState.getManager(groupID).getGroup(groupID).addMember(obj);
	}
	
		
	public final boolean isOpen() {return isOpen;}
	public final void update() {if(isOpen) updateAssembly();}
	
	protected abstract void offsetAssembly();
	protected abstract void defineAssembly();
	protected abstract void addAssembly();
	protected abstract void updateAssembly();

	
		
	
	
	
}
