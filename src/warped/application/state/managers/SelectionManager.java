/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers;

import java.util.ArrayList;

import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.state.WarpedState;
import warped.utilities.utils.Console;

public class SelectionManager {
	
	
	private ArrayList<WarpedObjectIdentity> selection = new ArrayList<>();
	
	public SelectionManager() {
		
	}
	
	public WarpedObject getSelected() {
		if(selection.size() <= 0) {
			Console.err("SelectionManager -> getSelection() -> no object is selected");
			return null;
		}
		return WarpedState.getGameObject(selection.get(0));
	}
	public WarpedObjectIdentity getSelectedIdentity() {return selection.get(0);}
	
	public ArrayList<WarpedObject> getSelection(){
		if(selection.size() <= 0) {
			Console.err("SelectionManager -> getAllSelected() -> no objects are selected");
			return null;
		}
		ArrayList<WarpedObject> result = new ArrayList<>();
		for(int i = 0; i < selection.size(); i++) {
			result.add(WarpedState.getGameObject(selection.get(i)));
		}
		return result;
	}
	public ArrayList<WarpedObjectIdentity> getSelectionIdentity(){return selection;}
	
	public void select(WarpedObjectIdentity selected) {
		selection.clear();
		selection.add(selected);
	}
	
	
	public void select(ArrayList<WarpedObjectIdentity> selection) {this.selection = selection;}
	
	
	
	public void update() {
		
	}

}
