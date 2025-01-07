/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.container;

import java.awt.image.BufferedImage;

import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ItemType;
import warped.application.object.WarpedObjectIdentity;

public class ItemContainer extends WarpedItem {

	private ContainerType containerType;
	public ItemContainer(ContainerType containerType) {
		super(ItemType.CONTAINER, containerType.getString(), new BufferedImage(1,1,1));
		this.containerType = containerType;
	}
	
	public ItemContainer(ContainerType containerType, int quantity) {
		super(ItemType.CONTAINER, containerType.getString(), new BufferedImage(1,1,1), quantity);
		this.containerType = containerType;
	}

	public ContainerType getContainerType() {return containerType;}
	
	@Override
	public double massOfOne() {return containerType.getMass();}

	@Override
	public void effect(WarpedObjectIdentity objectID) {
		// TODO 20/5/24 -> Implement effect function	
	}

	@Override
	protected void updateRaster() {return;}

	@Override
	public double valueOfOne() {return containerType.getValue();}

}
