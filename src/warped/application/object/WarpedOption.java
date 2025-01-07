/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.object;

import warped.user.actions.WarpedAction;
import warped.utilities.utils.Console;

public class WarpedOption {
	
	private Class<?> affectType;
	private String name = "defaultName";
	private WarpedAction action = () -> {Console.ln("ObjectOption -> option -> failed to initialize option action");};
	private String description = "defaultDescription";
	
	public WarpedOption(String name, WarpedAction action) {
		this.name = name;
		this.action = action;
	}
	
	public WarpedOption(String name, Class<?> affectType, WarpedAction action) { 
		this.name = name;
		this.action = action;
		this.affectType = affectType;
	}
	
	public WarpedOption(String name, String description, Class<?> affectType, WarpedAction action) { 
		this.name = name;
		this.description = description;
		this.action = action;
		this.affectType = affectType;
	}
	
	/**This will return the type of class that this option should be applied to*/
	public Class<?> getAffectType(){return affectType;}
	public String getName() {return name;}
	public void action() {action.action();}
	public WarpedAction getAction() {return action;}
	public String getDescription(){return description;}
	public boolean isEqual(WarpedOption option) {if(name.equals(option.name) && action.equals(option.action)) return true; else return false;}
	
	
	
}
