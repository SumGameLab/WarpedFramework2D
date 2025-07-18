/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.entities.item;

import warped.application.actionWrappers.ActionOption;
import warped.application.entities.WarpedEntitie;
import warped.application.state.WarpedState;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class WarpedItem <T extends ItemBindable<? extends Enum<?>>>extends WarpedEntitie {
		
	protected T itemType;
	protected int quantity = 1;
	private double mass = 0.1;
	private int value = 0;
	
	private static final double FALL_HEIGHT = 80;
	private static final double ITEM_GRAVITY = 0.195;
	private static final double BOUNCE_LOSS = 0.75;
	private VectorD velocity = new VectorD(0.0, 0.0, 0.0);
	private boolean isDropped = false;
	private double height = FALL_HEIGHT;
	
	
	
	/**A new item of of the specified type
	 * @author 5som3*/
	public WarpedItem(T itemType) {
		this.itemType = itemType;
		this.name = itemType.getString();
		setToolTip(name);
		sprite.paint(itemType.getRaster(), itemType.getRaster().getWidth(), itemType.getRaster().getHeight());
		clearSelectOptions();
		addSelectOption(new ActionOption("Inspect", () -> {
			WarpedState.itemInspector.selectItem(this);
			WarpedState.itemInspector.open();
		}));
	}
	
	/**A new item of of the specified type and quantity
	 * @author 5som3*/
	public WarpedItem(T itemType, int quantity) {
		this.itemType = itemType;
		this.name = itemType.getString();
		this.quantity = quantity;
		setToolTip(name);
		sprite.paint(itemType.getRaster(), itemType.getRaster().getWidth(), itemType.getRaster().getHeight());
		clearSelectOptions();
		addSelectOption(new ActionOption("Inspect", () -> {
			WarpedState.itemInspector.selectItem(this);
			WarpedState.itemInspector.open();
		}));
	}
		
	/**Drop the item 
	 * Will randomize the drop velocity and queue the updating of the drop animation  
	 * @author 5som3*/
	public void dropItem() {
		velocity.set(UtilsMath.random(-3.0, 3.0), UtilsMath.random(-3.0, 3.0), 0.0);
		isDropped = true;
	}
	
	/**Set the quantity regardless of what it was prior.
	 * If the quantity of the item should be set to a specific number use this function.
	 * If you want to (for example) add 10 or remove 10 items from the current quantity you should instead use the produce / consume functions.
	 *
	 *@param the item quantity will be set to this value if it is larger than 0 
	 **/
	public final void setQuantity(int quantity) {
		if(quantity < 0) {
			Console.err("ItemEntitie -> setQuantity -> quantity must be great than 0");
			return;
		}
		this.quantity = quantity;
		updateMass();
		updateValue();
	}
	
	/**Increases the quantity
	 * @param the amount to increase by
	 * */
	public final void produce(int quantity) {
		this.quantity += quantity;
		updateMass();
		updateValue();
	}
	/**Increases the quantity by one*/
	public final void produce() {produce(1);}	
	
	
	/**Decrease the quantity by one (if possible)
	 * @return true if the quantity was reduced by 1
	 * @return false if the quantity was already 0 and could not be reduced
	 * */
	public final boolean consume() {return consume(1);}
	
	/**Decrease the quantity
	 * @param the amount to decrease the quantity by.
	 * @return true 
	 * */
	public final boolean consume(int quantity) {
		int val = this.quantity - quantity;
		if(val < 0) return false;
		else {
			this.quantity = val;
			updateMass();
			updateValue();
			return true;
		}
	}
	
	/**Get the item type
	 * @return T - the type of item
	 * @author 5som3*/
	public T getItemType() {return itemType;}
	
	/**Get the item quantity
	 * @return int - the number of items in the stack
	 * @author 5som3*/
	public final int getQuantity() {return quantity;}
	
	/**Get the item quantity as a string
	 * @return String - the number of items
	 * @author 5som3*/
	public final String getQuantityString() {return "" + quantity;}
	
	/**Get the mass of the item stack.
	 * @return double - the total mass of the items 
	 * @author 5som3*/
	public final double getMass() {return mass;}
	
	/**Get the mass of a single item of this type
	 * @return double - the mass of one of this item type
	 * @author 5som3*/
	public final double getMassOfOne() {return itemType.getMass();}
	
	/**Get the value of the item stack
	 * @return int - the value of the stack
	 * @author 5som3*/
	public final int getValue() {return value;}
	
	/**Get the value of a single item of this type
	 * @return int - the value of one item of this type
	 * @author 5som3*/
	public final int getValueOfOne() {return itemType.getValue();}
	
	/**Update the mass of the stack
	 * @author 5som3*/
	private final void updateMass() {mass = quantity * itemType.getMass();}
	
	/**Update the value of the stack
	 * @author 5som3*/
	private final void updateValue() {value = (int)(quantity * itemType.getValue());}

	/**Update the position of the item when it is "dropped" into a group
	 * @author 5som3*/
	private void updateDrop() {
		velocity.add(0.0, 0.0, ITEM_GRAVITY);
		height -= velocity.z();
		if(height < 0) {
			height = 0;
			velocity.setIndex(2, -(velocity.z()));
			velocity.scale(BOUNCE_LOSS);
		}
		if(velocity.x() < 0.3 && velocity.x() > -0.3 && velocity.y() < 0.3 && velocity.y() > -0.3) isDropped = false;
		
		position.add(velocity.x(), (velocity.y() + velocity.z()));
	}
	
	/**Do the items come from the same declaration of itemBindables.
	 * @param a, b   - the items to check.
	 * @return True  - If the itemBindable that defines the itemType for 'a' is the same itemBindable that defines the itemType for 'b'.
	 * @return False - If the itemTypes of a and be are declared by different itemBindables.
	public static boolean isSameBindable(WarpedItem<?> a, WarpedItem<?> b) {if(ItemBindable.isSameBindable(a.getItemType(), b.getItemType())) return true; else return false;}
	 */
	
	/**Do the items come from the same declaration of itemBindables.
	 * @param b      - the items to compare with.
	 * @return True  - If the itemBindable that defines the itemType for 'a' is the same itemBindable that defines the itemType for 'b'.
	 * @return False - If the itemTypes of a and be are declared by different itemBindables.
	public boolean isSameBindable(WarpedItem<?> b) {return isSameBindable(this, b);}
	 * */
	
	
	/**Do the items have the same type.
	 * @param a, b   - the items to check.
	 * @return True  - If 'a' and 'b' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False - If 'a' and 'b' have are declared by different itemBindables or if they are not the same type.
	public static boolean isSameType(WarpedItem<?> a, WarpedItem<?> b) {if(isSameBindable(a, b) && a == b) return true; else return false;}
	 * */
	
	/**Do the items have the same type.
	 * @param a, b   - the items to check.
	 * @return True  - If 'a' and 'b' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False - If 'a' and 'b' have are declared by different itemBindables or if they are not the same type.
	public boolean isSameType(WarpedItem<?> b) {return isSameType(this, b);}
	 * */
	
	/**Does the item type match the input type
	 * @param a        - the item to check.
	 * @param itemType - the type to compare the item against
	 * @return True    - If 'a' and 'itemType' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False   - If 'a' and 'itemType' have are declared by different itemBindables or if they are not the same type.
	public static <K extends ItemBindable<?>> boolean isSameType(WarpedItem<?> a, K itemType) {if(ItemBindable.isSameType(a.getItemType(), itemType)) return true; else return false;}
	 * */
	
	/**Does the item type match the input type.
	 * @param itemType - the type to compare the item against
	 * @return True    - If 'a' and 'itemType' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False   - If 'a' and 'itemType' have are declared by different itemBindables or if they are not the same type.
	public <K extends ItemBindable<?>> boolean isSameType(K itemType) {return isSameType(this, itemType);}
	 * */
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	public void updateObject() {
		if(isDropped) {
			//position.add(0.1);
			updateDrop();
		}
	}

	@Override
	protected void updatePosition(double deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
