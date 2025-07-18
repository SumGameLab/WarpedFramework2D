/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities;

import java.util.ArrayList;

import warped.application.actionWrappers.ActionOption;
import warped.application.state.WarpedObject;
import warped.application.state.WarpedState;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.utils.Console;

public abstract class WarpedEntitie extends WarpedObject {
		
	protected String name = "default Name";
	private ArrayList<ActionOption> selectOptions = new ArrayList<>();
	
	
	public WarpedEntitie() {
		addSelectOption(new ActionOption("Inspect", () -> {
			WarpedState.selector.close();
			WarpedState.gameObjectInspector.select(this); 
			WarpedState.gameObjectInspector.open();
		}));
	}
	
	public final String getName() {return name;}
	
	public final ActionOption getSelectOption(int index) {return selectOptions.get(index);}
	public final ArrayList<ActionOption> getSelectOptions() {return selectOptions;}
	
	protected final void addSelectOption(ActionOption option) {selectOptions.add(option);}
	protected final void clearSelectOptions() {selectOptions.clear();}
	
	
	
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		Console.ln("WarpedEntitie -> mouseReleased()");
		WarpedState.selector.select(this);
	};
	
	
	@Override
	public void updateMid() {return;};
	
	@Override
	public void updateSlow() {return;}; 

}
