/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.element;

import warped.application.entities.item.ItemType;
import warped.application.entities.item.WarpedItem;
import warped.application.object.WarpedObjectIdentity;

public class ItemElement extends WarpedItem{

	ElementType elementType;
	
	public ItemElement(ElementType elementType) {
		super(ItemType.ELEMENT, elementType.getString(), null);
		this.elementType = elementType;
		//setRaster(WarpedSprites.warpTech.elementItems.getRawSprite(elementType.ordinal()));
		updateMass();
	}
	
	public ItemElement(ElementType elementType, int quantity) {
		super(ItemType.ELEMENT, elementType.getString(),null, quantity);
		this.elementType = elementType;
		//setRaster(WarpedSprites.warpTech.elementItems.getRawSprite(elementType.ordinal()));
		updateMass();
	}
	
	public ElementType getElementType() {return elementType;}

	@Override
	public void effect(WarpedObjectIdentity objectID) {
		
	}

	@Override
	protected void updateRaster() {return;}

	@Override
	public double massOfOne() {return ElementType.getElementMass(elementType);}

	@Override
	public double valueOfOne() {
		// TODO 20/5/24 -> Implement get value function in elementType enumeration
		return 0;
	}
	

	

}
