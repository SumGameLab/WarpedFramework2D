package warped.application.actionWrappers;

import warped.functionalInterfaces.WarpedAction;
import warped.utilities.utils.Console;

public class ActionToggle {

	private String name = "defaultName";
	private boolean toggle = false;
	private WarpedAction toggleOnAction = () -> {Console.ln("ObjectOption -> option -> failed to initialize toggle on action");};
	private WarpedAction toggleOffAction = () -> {Console.ln("ObjectOption -> option -> failed to initialize toggle off action");};
	
	/**A new toggle action with the specified properties. 
	 * @param name - The name associated with the toggle.
	 * @param initialState - The state that the toggle will start with.
	 * @param toggleOnAction - The action to execute when the state changes to on.
	 * @param toggleOffAction - The action to execute when the state changes to off.
	 * @implNote ToggleOnAction or ToggleOffAction will trigger once each time the state changes to the respective action. 
	 * @author 5som3*/
	public ActionToggle(String name, boolean initialState, WarpedAction toggleOnAction, WarpedAction toggleOffAction) {
		this.name = name;
		this.toggle = initialState;
		this.toggleOnAction = toggleOnAction;
		this.toggleOffAction = toggleOffAction;
	}
	

	/**The name associated with this toggle
	 * @return String - A string containing the name.
	 * @author 5som3*/
	public String getName() {return name;}
	
	/**The state of this toggle.
	 * @return boolean - the current state of the toggle.
	 * @author 5som3*/
	public boolean isToggled() {return toggle;}
	
	/**Alternate the state of this toggle.
	 * @implNote Will trigger the action for the new state, i.e. if going from off to on then the toggleOnAction will trigger.
	 * @author 5som3*/
	public void toggle() {if(toggle) toggleOff(); else toggleOn();}
	
	/**Set the state of the toggle to true.
	 * @implNote will trigger the toggleOnAction if the state changed to on.
	 * @author 5som3*/
	public void toggleOn() {
		if(!toggle) {			
			toggle = true;
			toggleOnAction.action();
		}
	}
	
	/**Set the state of the toggle.
	 * @implNote will not trigger either toggleOn or toggleOff action.
	 * @author 5som3*/
	public void setState(boolean toggleState) {this.toggle = toggleState;}
	
	/**Set the state of the toggle to false.
	 * @implNote will trigger the toggleOffAction if the state changed to off
	 * @author 5som3*/
	public void toggleOff() {
		if(toggle) {			
			toggle = false;
			toggleOffAction.action();
		}
	}	
	
}
