package warped.application.state;

import warped.application.gui.WarpedGUI;

public abstract class GUIAssembly extends WarpedAssembly {

	public GUIAssembly() {
		super(WarpedState.guiManager);
		
	}

	protected void addMember(WarpedGUI obj) {WarpedState.guiManager.getGroup(groupID).addMember(obj);}
	
	
}
