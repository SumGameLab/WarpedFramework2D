package warped.utilities.math.vectors;

import java.util.List;

import warped.functionalInterfaces.VecIAction;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class VectorI {

	/*This class is designed to supersede the old Vec2d/3d/2i/3i class that is getting to be unmanageable.
	 * This vector also has the ability to set an event to occur when the vector changes. 
	 * You should not make this event complicated as that would make adding and subtracting slower. Ideally just set a boolean to queue a task or create an event task
	 * */
	private final int[]  vec;
	
	private VecIAction deltaAction = vec -> {return;};
	 
	/**A vector with x, y components.
	 * @implNote x = 0.0
	 * @implNote y = 0.0
	 * @author 5som3*/
	public VectorI() {
		vec = new int[2];
		vec[0] = 0;
		vec[1] = 0;
	}
	
	/**A vector with the specified number of components.
	 * @param length - the number of components this vector will have.
	 * @apiNote The minimum length for a VectorD is 2.
	 * @author 5som3*/
	public VectorI(int length) {
		if(length < 2) {
			Console.err("VectorI -> VectorI(length) -> vectors must be at least length 2. Values will be added to make the vector size 2.");
			vec = new int[2];			
 		} else vec = new int[length];
		
		for(int i = 0; i < length(); i++) vec[i] = 0;
	}
	
	/**A vector with the specified number of components.
	 * @param val - list of the component values for this vector. 
	 * 				e.g. (1, -5, 3).
	 * 
	 * @apiNote The minimum length for a VectorI is 2.
	 * @author 5som3*/
	public VectorI(int... val) {
		if(val.length < 2) {
			Console.err("VectorI -> VectorI(values...) -> vectors must be at least length 2. Values will be added to make the vector size 2.");
			
			vec = new int[2];
			for(int i = 0; i < 2; i++) {
				if(i >= val.length) vec[i] = 0;
				else vec[i] = val[i];
			}
			return;
		}
		vec = new int[val.length];
		for(int i = 0; i < val.length; i++) vec[i] = val[i];
	}
	
	/**A vector copy of another vector.
	 * @param vec - the vector to copy its components from.
	 * @author 5som3*/
	public VectorI(VectorI vec) {
		this.vec = new int[vec.length()];
		for(int i = 0; i < vec.length(); i++) this.vec[i] = vec.get(i);		
	}
	
	/**A vector copy of another vector.
	 * @param vec - the vector to copy its components from.
	 * @author 5som3*/
	public VectorI(VectorD vec) {
		this.vec = new int[vec.length()];
		for(int i = 0; i < vec.length(); i++) this.vec[i] = (int)vec.get(i);		
	}
	
	/**Any action set here will be triggered when ever the value of this vector changes.
	 * @param deltaAction - the action to carry out when the vector changes.
	 * @apiNote You should only use this action for simple operations i.e. set a boolean true/false, creating an event, etc..
	 * @apiNote Setting a complex deltaAction like redrawing graphics will impact performance instead queue graphics updates with deltaAction.
	 * @author 5som3*/
	public void setDeltaAction(VecIAction deltaAction) {this.deltaAction = deltaAction;}
	
	/**Deletes the deltaAction (if any were set).
	 * Any action that was previously set to trigger will no longer occur.
	 * @author 5som3*/
	public void clearDeltaAction() {this.deltaAction = vec -> {return;};}
	
	/**Get this vector as a line of text.
	 * @return String - A string in vector form containing each of the components as text.
	 * @apiNote Useful for debugging and printing out vectors has no real mathematical application. 
	 * @param */
	public final String getString() {
		String result = "( ";
		for(int i = 0; i < length(); i++) result += (vec[i] + ", ");
		return result += ")";
	}
	
	/**The number of components the vector contains.
	 * @return int - the vector dimension
	 * @author 5som3*/
	public final int length() {return vec.length;}

	/**Does the vector have a z component.
	 * @return True if the vector has 3 components or greater else returns false;;
	 * */
	public final boolean hasZComponent() {if(length() < 3) return false; else return true;}
	
	/**Get the x component.
	 * @return int - the value at index 0.
	 * @author 5som3*/
	public final int x() {return vec[0];};
	
	/**Get the y component.
	 * @return int - the value at index 1.
	 * @author 5som3*/
	public final int y() {return vec[1];}
	
	/**Get the z component.
	 * @return int - the value at index 2.
	 * 				  - will return -1.0 if the vector has no z component.
	 * @author 5som3*/
	public final int z() {
		if(!hasZComponent()) {
			Console.err("VectorI -> z() -> this vector is length 2 and has no z componenet.");
			return -1;
		} else return vec[3];
	};
	
	/**Get a specific value in the vector.
	 * @param index - the index of the value to get.
	 * @return int - the vectors value at the specified index.
	 * @author 5som3*/
	public final int get(int index) {
		if(index < 0 || index >= length()) {
			Console.err("VectorI -> get() -> invalid index : " + index);
			return -1;
		}
		return vec[index];
	}
	
	/**Generate a new VectorI with the same components as this vector.
	 * @return VectorI - a copy of this vector.
	 * @author 5som3*/
	public final VectorI generateClone() {return new VectorI(this);}
	
	/**Check if the vector has the same components.
	 * @param vec - the vector to compare components with.
	 * @return True each component of the vectors are the same else false.
	 * @author 5som3*/
	public final boolean isEqual(VectorI vec) {
		if(length() != vec.length()) return false; //check if same length
		for(int i = 0; i < length(); i++) if(this.vec[i] != vec.get(i)) return false; // check if each component is equal
		return true;
	}
	
	/**Check if the vector has the same components.
	 * @param vec - the vector to compare components with.
	 * @return True each component of the vectors are the same else false.
	 * @author 5som3*/
	public final boolean isEqual(int... values) {
		if(length() != values.length) {
			Console.err("VectorD -> isEqual() -> number of values " + values.length + " does not match number of components" + length());
			return false; //check if same length
		}
		for(int i = 0; i < length(); i++) if(this.vec[i] != values[i]) return false; // check if each component is equal
		return true;
	}
	
	/**Calculate the length of the vector
	 * @return int - the length of the vector also known as the magnitude.
	 * @author 5som3*/
	public final double getMagnitude() {
		int val = 0;
		for(int i = 0; i < length(); i++) val += UtilsMath.square(vec[i]);
		return Math.sqrt(val);
	}
	
	/**Calculate the dot product of this with the input vec.
	 * @return int - the dot product of these two vectors.
	 * @apiNote Consider this vector as A and input vector as B then the result is A * B.
	 * @author 5som3*/
	public final double getDotProduct(VectorI vec) {
		if(vec.length() != length()) {
			Console.err("VectorI -> getDotProduct() -> number of components in each vector must match, (this.length(), vec.length()) : ( " + this.length() + ", " + vec.length() + ")");
			return -1.0;
		}
		int val = 0;
		for(int i = 0; i < length(); i++) val += this.vec[i] * vec.get(i);
		return val;
	}
	
	/**Calculate the distance between this vector and another vector.
	 * @param vec - the vector to measure the distance against.
	 * @return double - the distance between the vectors.
	 * @author 5som3*/
	public final double getDistanceBetweenVectors(VectorI vec) {
		if(length() != vec.length()) {
			Console.err("VectorD -> getDistanceBetweenVectors() -> vectors must be the same dimension to calculate distances ");
			return -1.0;
		}
		double sum = 0.0;
		for(int i = 0; i < length(); i++) sum += UtilsMath.square(vec.get(i) - this.vec[i]);
		return Math.sqrt(sum);
	}
	
	/**Calculate the angle between this vector and another vec.
	 * @return int - the angle between vectors in radians. 
	 * @apiNote Consider this vector as A and input vector as B then the result theta = acos(A * B / |A||B|).
	 * @author 5som3*/
	public final double getAngleBetweenVectors(VectorI vec) {
		if(vec.length() != length()) {
			Console.err("VectorI -> getAngleBetweenVectors() -> number of components in each vector must match, (this.length(), vec.length()) : ( " + this.length() + ", " + vec.length() + ")");
			return -1.0;
		}
		
		return Math.acos(getDotProduct(vec) / (getMagnitude() * vec.getMagnitude()));
	}
	
	
	/**Calculate the angle between the xAxis and this vector.
	 * @apiNote As the +X axis is considered 0 in java this result is effectively the angle to rotate any object to match the vector direction. 
	 * @author 5som3*/
	public final double getRotationAngle() {
		VectorI xAxis = new VectorI(length());
		xAxis.set(1);
		return xAxis.getAngleBetweenVectors(this);
	}
	
	
	//--------
	//---------------- Methods beyond this line are all some form of setter, they MUST call deltaAction after their operation completes ---------------
	//--------
	//--------
	//---------------- Setters --------
	//--------
	
	
	/**Zeros all components of the vector
	 * @implNote Will trigger the deltaAction once after zeroing the vector components.
	 * @author 5som3*/
	public final void zero() {
		for(int i = 0; i < length(); i++) vec[i] = 0;
		deltaAction.action(this);
	}
	
	/**Swaps the signs ( + / - ) of each vector component.
	 * 		e.g. vector A    = [ 4,-1, 7]
	 * 			 A.inverse() = [-4, 1,-7]
	 * The inverse is such that the sum of A + A.inverse() = A.zero();
	 * @implNote Will trigger the deltaAction once after inverting the vector components.
	 *@author 5som3 */
	public final void inverse() {scale(-1);}
	
	/**Set the values of this vector
	 * @param val - a list of ints that will be indexed in the vector as the are listed.
	 * @param     - example : (a, b, c) -> vec[0] = a 
	 * 								       vec[1] = b
	 * 								       vec[3] = c
	 * @implNote Will trigger the deltaAction once after setting the vector components.
	 * @author 5som3 */
	public final void set(int... val) {
		for(int i = 0; i < val.length; i++) {
			if(i >= length()) {
				Console.err("VectorI -> set() -> number of values exceedes vector size, skipping value : " + val[i]);
				continue;
			} else this.vec[i] = val[i];
		}
		deltaAction.action(this);
	}
	
	/**Set the components of this vector based on another vector.
	 * @param vec - a vector to copy the components from.
	 * @apiNote If the vec is longer than this vector those values will be skipped.
	 * @implNote Will trigger the deltaAction once after setting the vector components.
	 * @author 5som3*/
	public final void set(VectorI vec) {
		for(int i = 0; i < vec.length(); i++) {
			if(i >= length()) {
				Console.err("VectorD -> set(VectorD) -> the vec param is longer than this vector, the out of bounds components will be ignored");
				return;
			}
			this.vec[i] = vec.get(i);
		}
		deltaAction.action(this);
	}
	
	/**Set the components of this vector based on another vector.
	 * @param vec - a vector to copy the components from.
	 * @apiNote If the vec is longer than this vector those values will be skipped.
	 * @implNote Will trigger the deltaAction once after setting the vector components.
	 * @author 5som3*/
	public final void set(VectorD vec) {
		for(int i = 0; i < vec.length(); i++) {
			if(i >= length()) {
				Console.err("VectorD -> set(VectorD) -> the vec param is longer than this vector, the out of bounds components will be ignored");
				return;
			}
			this.vec[i] = (int)vec.get(i);
		}
		deltaAction.action(this);
	}
	
	
	/**Set the value at a specific index in the vector
	 * @param index - the index of the vector to set.
	 * @param value - the value to set in the vector.
	 * @apiNote The method will fail if index < 0 or index >= vector length().
	 * @implNote Will trigger the deltaAction once after setting the vector components.
	 * @author 5som3*/
	public final void setIndex(int index, int value) {
		if(index < 0 || index >= length()) {
			Console.err("VectorI -> set() -> the index " + index + " is invalid for a vector of length " + length());
			return;
		} 
		vec[(int)index] = value;
		deltaAction.action(this);
	}
	
	
	/**Set a list of specific values in the vector
	 * @param indices - a list of the indices where the values should be set.
	 * @param values  - a list of the values to set at the corresponding index.  
	 * 				  - e.g. vector[indices.get(0)] = values.get(0);
	 * 						 vector[indices.get(1)] = values.get(1);
	 * @apiNote The method will skip any index that is < 0 or index >= vector length().
	 * @apiNote The method will fail if the size of indices and values do not match.
	 * @author 5som3*/
	public final void set(List<Integer> indices, List<Integer> values) {
		if(indices.size() != values.size()) {
			Console.err("VectorI -> set(list, list) -> each index must have a corresponding value, sizes don't match");
			return;
		}
		for(int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			if(index < 0 || index >= length()) {
				Console.err("VectorI -> set(list, list) -> element " + i + " of the index list contains an out of bounds index : " + index + ". It will be skipped");
				continue;
			} else vec[index] = values.get(i);
		}
		deltaAction.action(this);
	}
	

	//--------
	//---------------- Addition --------
	//--------
	
	/**Add another VectorI to this vector.
	 * @param vec - the vector to add.
	 * @apiNote If the vector added (vec) is larger then the extra values of vec will be ignored.
	 * @apiNote If the vec is smaller then the extra values of this vector will be unaffected.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @implNote Adding negative values is effectively the same as Subtracting positive values i.e. add(-x) = subtract(x)
	 * @author 5som3*/
	public final void add(VectorI vec) {
		for(int i = 0; i < vec.length(); i++) {
			if(i >= length()) {
				Console.err("VectorI -> add(vec) -> vector has no component " + i +", extra values will be skipped.");
				break;
			}
			this.vec[i] += vec.get(i);
		}
		deltaAction.action(this);
	}
	
	/**Add a VectorD to this vector.
	 * @param vec - the vector to add.
	 * @apiNote If the vector added (vec) is larger then the extra values of vec will be ignored.
	 * @apiNote If the vec is smaller then the extra values of this vector will be unaffected.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @implNote Adding negative values is effectively the same as Subtracting positive values i.e. add(-x) = subtract(x)
	 * @author 5som3*/
	public final void add(VectorD vec) {
		for(int i = 0; i < vec.length(); i++) {
			if(i >= length()) {
				Console.err("VectorD -> add(vec) -> vector has no component " + i +", extra values will be skipped.");
				break;
			}
			this.vec[i] += (int)vec.get(i);
		}
		deltaAction.action(this);
	}
	
	/**Add to the vector components.
	 * @param values - a list of the values to add in the order to added them
	 * @author 5som3*/
	public final void add(int... values) {
		for(int i = 0; i < values.length; i++) {
			if(i >= length()) {
				Console.err("VectorD -> add(values...) -> vector has no component " + i + ", extra value will be skipped.");
				break;
			} else vec[i] += values[i];
		}
		deltaAction.action(this);		
	}

	/**Add a value to the component at the specified index.
	 * @param index - the index of the vector component that will be altered.
	 * @param value - the value to add to the vector component at the specified index.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @implNote Adding negative values is effectively the same as Subtracting positive values i.e. add(-x) = subtract(x)
	 * @author 5som3.*/
	public final void addIndex(int index, int value) {
		if(index < 0 || index >= length()) {
			Console.err("VectorI -> add(index, value) -> index is out of bounds : " + index);
			return;
		} 
		vec[(int)index] += value;
		deltaAction.action(this);
	}
	
	/**Add a list of values to the specified vector components.
	 * @param indices - a list of the vector components to add values to.
	 * @param values - a list of values to add to the corresponding vector components.
	 * @apiNote The method will skip any index that is < 0 or index >= vector length().
	 * @apiNote The method will fail if the size of indices and values do not match.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @implNote Adding negative values is effectively the same as Subtracting positive values i.e. add(-x) = subtract(x)
	 * @author 5som3 */
	public final void add(List<Integer> indices, List<Integer> values) {
		if(indices.size() != values.size()) {
			Console.err("VectorI -> add(indices, values) -> each index must have a corresponding value, indices size and values size must be equal.");
			return;
		}
		
		for(int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			if(index < 0 || index >= length()) {
				Console.err("VectorI -> add(indices, values) -> element " + i + " of the indices list is out of bounds " + index + ", it will be skipped");
				continue;
			} else vec[index] += values.get(i);
		}
		deltaAction.action(this);
	}
	
	
	//--------
	//---------------- Subtraction --------
	//--------
	
	/**Subtract a list of values from the components.
	 * @param values - will be subtracted from the component at the corresponding index i.e. vec[0] -= values[0].
	 * @apiNote If the vector subtracted (vec) is larger then the extra values of vec will be ignored.
	 * @apiNote If the vec is smaller then the extra values of this vector will be unaffected.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @author 5som3*/
	public final void subtract(int... values) {
		for(int i = 0; i < values.length; i++) {
			if(i >= length()) {
				Console.err("VectorD -> subtract(values...) -> values contains more components than this vector has, extras will be skipped");
				break;
			} 
			this.vec[i] -= values[i];
		}
		deltaAction.action(this);
	}
	
	/**Subtract another VectorD to this vector.
	 * @param vec - the vector to add.
	 * @apiNote If the vector subtracted (vec) is larger then the extra values of vec will be ignored.
	 * @apiNote If the vec is smaller then the extra values of this vector will be unaffected.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @author 5som3*/
	public final void subtract(VectorI vec) {
		for(int i = 0; i < vec.length(); i++) {
			if(i >= length()) {
				Console.ln("VectorD -> subtract(vec) -> vec contains more components than thsi vector, extras will be skipped");
				break;
			}
			this.vec[i] -= vec.get(i);
		}
		deltaAction.action(this);
	}
	
	/**Subtract another VectorD to this vector.
	 * @param vec - the vector to add.
	 * @apiNote If the vector subtracted (vec) is larger then the extra values of vec will be ignored.
	 * @apiNote If the vec is smaller then the extra values of this vector will be unaffected.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @author 5som3*/
	public final void subtract(VectorD vec) {
		for(int i = 0; i < vec.length(); i++) {
			if(i >= length()) {
				Console.ln("VectorD -> subtract(vec) -> vec contains more components than thsi vector, extras will be skipped");
				break;
			}
			this.vec[i] -= vec.get(i);
		}
		deltaAction.action(this);
	}
	
	/**Subtract a list of values to the specified vector components.
	 * @param indices - a list of the vector components to subtract values to.
	 * @param values - a list of values to add to the corresponding vector components.
	 * @apiNote The method will skip any index that is < 0 or index >= vector length().
	 * @apiNote The method will fail if the size of indices and values do not match.
	 * @apiNote Will trigger the deltaAction once after adding the vector components.
	 * @author 5som3 */
	public final void subtract(List<Integer> indices, List<Integer> values) {
		if(indices.size() != values.size()) {
			Console.err("VectorD -> subtract(indices, values) -> each index must have a corresponding value, indices size and values size must be equal.");
			return;
		}
		
		for(int i = 0; i < indices.size(); i++) {
			int index = indices.get(i);
			if(index < 0 || index >= length()) {
				Console.err("VectorD -> add(indices, values) -> element " + i + " of the indices list is out of bounds " + index + ", it will be skipped");
				continue;
			} else vec[index] -= values.get(i);
		}
		deltaAction.action(this);
	}
	
	/**Subtract a value to the component at the specified index.
	 * @param index - the index of the vector component that will be altered.
	 * @param value - the value to subtract to the vector component at the specified index.
	 * @apiNote Will trigger the deltaAction once after subtracting the vector components.
	 * @implNote Adding negative values is effectively the same as Subtracting positive values i.e. add(-x) = subtract(x)
	 * @author 5som3.*/
	public final void subtractIndex(int index, int value) {
		if(index < 0 || index >= length()) {
			Console.err("VectorD -> add(index, value) -> index is out of bounds : " + index);
			return;
		} 
		vec[index] -= value;
		deltaAction.action(this);
	}
	//--------
	//---------------- Scale --------
	//--------
	

	/**Scale each value of the vector by the specified amount
	 * @param scalar - Each component of the vector will be multiplied by this amount
	 * @implNote Will trigger the deltaAction once after scaling the vector components.
	 * @author 5som3*/
	public final void scale(int scalar) {
		for(int i = 0; i < length(); i++) vec[i] *= scalar;
		deltaAction.action(this);
	}
	
	/**Scale a specific component of the vector
	 * @param index - the index of the component to scale.
	 * @param scalar - the amount to scale the specified component by.
	 * @apiNote Will fail if the index is out of bounds. index < 0 || index > length()
	 * @implNote Will trigger the deltaAction once after scaling the vector components.
	 * @author 5som3*/
	public final void scaleIndex(int index, double scalar) {
		if(index < 0 || index > length()) {
			Console.err("VectorI -> scale(index, scalar) -> the index is out of bounds : " + index);
			return;
		}
		vec[index] *= scalar;
		deltaAction.action(this);
	}
	
	/**scale the vector components by the specified amounts.
	 * @param val - a list of the amounts to scale each component by.
	 * @implNote Will trigger the deltaAction once after inverting the vector components.
	 * @author 5som3*/
	public final void scale(int... val) {
		for(int i = 0; i < val.length; i++) {
			if(i >= length()) {
				Console.ln("VectorD -> scale() -> list contains more values than vector has components, extra values will be skipped");
				break;
			} 
			vec[i] *= val[i];
			
		}
		deltaAction.action(this);
	}
	
	//--------
	//---------------- Clamp --------
	//--------
	
	/**Keep each vector component within the specified range.
	 * @param max - the maximum value that any vector component can have (inclusive).
	 * @author 5som3 */
	public final void clampMax(int max) {
		for(int i = 0; i < length(); i++) if(vec[i] > max) vec[i] = max;
		deltaAction.action(this);
	}
	
	/**Keep each vector component within the specified range.
	 * @param min - the minimum value that any vector component can have (inclusive).
	 * @author 5som3*/
	public final void clampMin(int min) {
		for(int i = 0; i < length(); i++) if(vec[i] < min) vec[i] = min;
		deltaAction.action(this);		
	}
	
	/**Keep each vector component within the specified range.
	 * @param min - the minimum value that any vector component can have (inclusive).
	 * @param max - the maximum value that any vector component can have (inclusive).
	 * @author 5som3 */
	public final void clamp(int min, int max) {
		for(int i = 0; i < length(); i++) {
			if(vec[i] < min) vec[i] = min;
			else if(vec[i] > max) vec[i] = max;
		}
		deltaAction.action(this);		
	}
	
}
