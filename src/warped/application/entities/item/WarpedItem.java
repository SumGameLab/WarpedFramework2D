/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.entities.item;

import java.awt.image.BufferedImage;

import warped.application.actionWrappers.ActionOption;
import warped.application.entities.WarpedEntitie;
import warped.application.state.WarpedState;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.utils.Console;

public class WarpedItem <T extends ItemBindable<? extends Enum<?>>>extends WarpedEntitie {
		
	protected T itemType;
	protected int quantity = 1;
	private double mass = 0.1;
	
	
	public WarpedItem(T itemType) {
		this.itemType = itemType;
		this.name = itemType.getString();
		setToolTip(name);
		sprite.paint(itemType.getRaster());
		clearSelectOptions();
		addSelectOption(new ActionOption("Inspect", () -> {
			WarpedState.itemInspector.selectItem(this);
			WarpedState.itemInspector.open();
		}));
	}
	
	public WarpedItem(T itemType, int quantity) {
		this.itemType = itemType;
		this.name = itemType.getString();
		this.quantity = quantity;
		setToolTip(name);
		sprite.paint(itemType.getRaster());
		clearSelectOptions();
		addSelectOption(new ActionOption("Inspect", () -> {
			WarpedState.itemInspector.selectItem(this);
			WarpedState.itemInspector.open();
		}));
	}
	
	
	public WarpedItem(T itemType, String name, BufferedImage raster, int quantity) {
		this.itemType = itemType;
		this.name = name;
		this.quantity = quantity;
		setToolTip(name);
		sprite.paint(itemType.getRaster());
		clearSelectOptions();
		addSelectOption(new ActionOption("Inspect", () -> {
			WarpedState.itemInspector.selectItem(this);
			WarpedState.itemInspector.open();
		}));
	}
	
	@SuppressWarnings("unchecked")
	/**Cast the input item to the specified type
	 * @return null if the cast is not valid else returns the cast item 
	 * */
	public static <K extends ItemBindable<?>, L extends ItemBindable<?>> WarpedItem<K> castItem(WarpedItem<L> a, K castType){if(isSameType(a, castType)) return (WarpedItem<K>) a; else return null;}
	
	/**Cast the input item to the specified type
	 * @return null if the cast is not valid else returns the cast item 
	 * */
	public <K extends ItemBindable<?>, L extends ItemBindable<?>> WarpedItem<K> castItem(K castType){return castItem(this, castType);}
	
	
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
	}
	
	/**Increases the quantity
	 * @param the amount to increase by
	 * */
	public final void produce(int quantity) {
		this.quantity += quantity;
		updateMass();
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
			return true;
		}
	}
	
	

	public T getItemType() {return itemType;}
	public final int getQuantity() {return quantity;}
	public final String getQuantityString() {return "" + quantity;}
	public final double getMass() {return mass;}
	public final double getMassOfOne() {return itemType.getMass();}
	public final int getValue() {return quantity * itemType.getValue();}
	public final int getValueOfOne() {return itemType.getValue();}
	

	protected final void updateMass() {mass = quantity * itemType.getMass();}

	
	/**Do the items come from the same declaration of itemBindables.
	 * @param a, b   - the items to check.
	 * @return True  - If the itemBindable that defines the itemType for 'a' is the same itemBindable that defines the itemType for 'b'.
	 * @return False - If the itemTypes of a and be are declared by different itemBindables.
	 * */
	public static boolean isSameBindable(WarpedItem<?> a, WarpedItem<?> b) {if(ItemBindable.isSameBindable(a.getItemType(), b.getItemType())) return true; else return false;}
	
	/**Do the items come from the same declaration of itemBindables.
	 * @param b      - the items to compare with.
	 * @return True  - If the itemBindable that defines the itemType for 'a' is the same itemBindable that defines the itemType for 'b'.
	 * @return False - If the itemTypes of a and be are declared by different itemBindables.
	 * */
	public boolean isSameBindable(WarpedItem<?> b) {return isSameBindable(this, b);}
	
	
	/**Do the items have the same type.
	 * @param a, b   - the items to check.
	 * @return True  - If 'a' and 'b' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False - If 'a' and 'b' have are declared by different itemBindables or if they are not the same type.
	 * */
	public static boolean isSameType(WarpedItem<?> a, WarpedItem<?> b) {if(isSameBindable(a, b) && a == b) return true; else return false;}
	
	/**Do the items have the same type.
	 * @param a, b   - the items to check.
	 * @return True  - If 'a' and 'b' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False - If 'a' and 'b' have are declared by different itemBindables or if they are not the same type.
	 * */
	public boolean isSameType(WarpedItem<?> b) {return isSameType(this, b);}
	
	/**Does the item type match the input type
	 * @param a        - the item to check.
	 * @param itemType - the type to compare the item against
	 * @return True    - If 'a' and 'itemType' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False   - If 'a' and 'itemType' have are declared by different itemBindables or if they are not the same type.
	 * */
	public static <K extends ItemBindable<?>> boolean isSameType(WarpedItem<?> a, K itemType) {if(ItemBindable.isSameType(a.getItemType(), itemType)) return true; else return false;}
	
	/**Does the item type match the input type.
	 * @param itemType - the type to compare the item against
	 * @return True    - If 'a' and 'itemType' have are declared by the same itemBindable and they are of the same type in that declaration.
	 * @return False   - If 'a' and 'itemType' have are declared by different itemBindables or if they are not the same type.
	 * */
	public <K extends ItemBindable<?>> boolean isSameType(K itemType) {return isSameType(this, itemType);}
	
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
	public void updateObject() {return;}
	
	
	
}
