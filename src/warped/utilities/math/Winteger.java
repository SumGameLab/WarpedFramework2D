package warped.utilities.math;

import warped.functionalInterfaces.WintegerAction;

public class Winteger {

	private int val = 0;
	private WintegerAction deltaAction = val -> {return;}; 
	
	private Winteger(int val) {this.val = val;}
	
	/**Set an action to occur when the value changes
	 * @param deltaAction - this action will trigger once subsequent to the value being set. 
	 * @author 5som3 */
	public void setDeltaAction(WintegerAction deltaAction) {this.deltaAction = deltaAction;}
	
	/**Remove the delta action (if one exists).
	 * Any delta action set to this winteger will not trigger again.
	 * @author 5som3*/
	public final void clearDeltaAction() {this.deltaAction = val -> {return;};}
	
	/**Get the value.
	 * @return int - the primitive int that contains the actual value
	 * @author 5som3*/
	public final int get() {return val;}
	
	/**Get the value as a string
	 * @return string - the value as a line of text.
	 * @author 5som3*/
	public final String getString() {return "" + val;}
	
	//--------
	//---------------- Methods beyond this line are all some form of setter, they MUST call deltaAction after their operation completes ---------------
	//--------
	
	/**Set the value
	 * @param val - the new value.
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void set(int val) {
		this.val = val;
		deltaAction.action(this);
	}
	
	/**Add to the value
	 * @param val - the amount to add
	 * @apiNote Adding a negative is the same as subtracting a positive i.e. add(-x) = sub(x);
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void add(int val) {
		this.val += val;
		deltaAction.action(this);
	}
	
	/**Subtract from the value.
	 * @param val - the amount to subtract
	 * @apiNote Adding a negative is the same as subtracting a positive i.e. add(-x) = sub(x);
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void sub(int val) {
		this.val -= val;
		deltaAction.action(this);
	}
	
	/**Scale the value.
	 * @param scalar - the amount to scale the value by.
	 * @apiNote Adding a negative is the same as subtracting a positive i.e. add(-x) = sub(x);
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void scale(int scalar) {
		this.val *= scalar;
		deltaAction.action(this);
	}
	
	
	
}
