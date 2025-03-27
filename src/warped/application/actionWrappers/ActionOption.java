/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.actionWrappers;

import warped.functionalInterfaces.WarpedAction;
import warped.utilities.utils.Console;

public class ActionOption {
	
	private String name = "defaultName";
	private WarpedAction action = () -> {Console.ln("ObjectOption -> option -> failed to initialize option action");};
	private String description = "defaultDescription";
	
	public ActionOption(String name, WarpedAction action) {
		this.name = name;
		this.action = action;
	}
	

	public ActionOption(String name, String description, WarpedAction action) { 
		this.name = name;
		this.description = description;
		this.action = action;
		
	}
	
	/**This will return the type of class that this option should be applied to*/
	public String getName() {return name;}
	public void action() {action.action();}
	public WarpedAction getAction() {return action;}
	public String getDescription(){return description;}
	public boolean isEqual(ActionOption option) {if(name.equals(option.name) && action.equals(option.action)) return true; else return false;}
	
	
	
}
