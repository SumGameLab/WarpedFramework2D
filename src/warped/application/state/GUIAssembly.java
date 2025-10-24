package warped.application.state;

import warped.application.gui.WarpedGUI;

public abstract class GUIAssembly extends WarpedAssembly {

	public GUIAssembly() {
		super(WarpedState.guiManager);
		
	}

	protected void addMember(WarpedGUI obj) {WarpedState.guiManager.getGroup(groupID).addMember(obj);}
	
	/**Update the graphics for all members of this assembly.
	 * @author 5som3*/
	public void updateGraphics() {
		WarpedGroup<WarpedGUI> group = WarpedState.guiManager.getGroup(groupID);
		for(int i = 0; i < group.size(); i++) {
			group.getMember(i).updateGraphics();
		}
	}
	
	public void updateLanguage() {
		WarpedGroup<WarpedGUI> group = WarpedState.guiManager.getGroup(groupID);
		for(int i = 0; i < group.size(); i++) {
			group.getMember(i).updateLanguage();
		}
	}
	
	
}
