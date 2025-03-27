package warped.utilities.math;

import warped.functionalInterfaces.WoubleAction;
import warped.utilities.utils.UtilsMath;

public class Wouble {

	private double val = 0.0;
	private WoubleAction deltaAction = val -> {return;};
	
	public Wouble(double val) {this.val = val;}
	
	/**Get the value.
	 * @return double - the primitive int that contains the actual value
	 * @author 5som3*/
	public final double get() {return val;}
	
	/**Get the value as a string
	 * @return string - the value as a line of text.
	 * @author 5som3*/
	public final String getString() {return "" + val;}
	
	/**Get the value as a string
	 * @return string - the value as a line of text.
	 * @author 5som3*/
	public final String getString(int decimals) {return "" + UtilsMath.round(val, decimals);}
	
	/**Set an action to occur when the value changes
	 * @param deltaAction - this action will trigger once subsequent to the value being set. 
	 * @author 5som3 */
	public final void setDeltaAction(WoubleAction deltaAction) {this.deltaAction = deltaAction;}
	
	/**Remove the delta action (if one exists).
	 * Any delta action set to this wouble will not trigger again.
	 * @author 5som3*/
	public final void clearDeltaAction() {this.deltaAction = val -> {return;};}
	

	//--------
	//---------------- Methods beyond this line are all some form of setter, they MUST call deltaAction after their operation completes ---------------
	//--------
	
	/**Set the value
	 * @param double - the new value.
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void set(double val) {
		this.val = val;
		deltaAction.action(this);
	}
	
	/**Add to the value
	 * @param double - the amount to add
	 * @apiNote Adding a negative is the same as subtracting a positive i.e. add(-x) = sub(x);
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void add(double val) {
		this.val += val;
		deltaAction.action(this);
	}
	
	/**Subtract from the value.
	 * @param double - the amount to subtract
	 * @apiNote Adding a negative is the same as subtracting a positive i.e. add(-x) = sub(x);
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void sub(double val) {
		this.val -= val;
		deltaAction.action(this);
	}
	
	/**Scale the value.
	 * @param scalar - the amount to scale the value by.
	 * @apiNote Adding a negative is the same as subtracting a positive i.e. add(-x) = sub(x);
	 * @apiNote Will trigger the deltaAction once after setting the value (if any has been set).
	 * @author 5som3*/
	public final void scale(double scalar) {
		this.val *= scalar;
		deltaAction.action(this);
	}
	
	
	
}

