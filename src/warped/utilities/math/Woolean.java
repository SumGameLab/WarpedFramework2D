package warped.utilities.math;

import warped.functionalInterfaces.WooleanAction;

public class Woolean {

	private boolean val = true;
	private WooleanAction deltaAction = val -> {return;};
	
	public Woolean(boolean val) {this.val = val;}
	
	/**Get the value.
	 * @return boolean - the primitive int that contains the actual value
	 * @author 5som3*/
	public boolean get() {return val;}
	
	/**Get the value as a string
	 * @return string - the value as a line of text.
	 * @author 5som3*/
	public final String getString() {return "" + val;}
	
	/**Set an action to occur when the value changes
	 * @param deltaAction - this action will trigger once subsequent to the value being set. 
	 * @author 5som3 */
	public void setDeltaAction(WooleanAction deltaAction) {this.deltaAction = deltaAction;}
	
	/**Remove the delta action (if one exists).
	 * Any delta action set to this woolean will not trigger again.
	 * @author 5som3*/
	public final void clearDeltaAction() {this.deltaAction = val -> {return;};}

	//--------
	//---------------- Methods beyond this line are all some form of setter, they MUST call deltaAction after their operation completes ---------------
	//--------
	
	/**Set the value
	 * @param boolean - the new value.
	 * @apiNote Setting the value will trigger deltaAction (if any has been set).
	 * @author 5som3*/
	public void set(boolean val) {
		this.val = val;
		deltaAction.action(this);
	}
	
}
