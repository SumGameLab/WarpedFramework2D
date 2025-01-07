/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.resource;

import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ItemType;
import warped.application.object.WarpedObjectIdentity;

public class ItemResource extends WarpedItem {

	protected ResourceType resourceType;
	
	public ItemResource(ResourceType resourceType) {
		super(ItemType.RESOURCE, resourceType.getString(), null);
		this.resourceType = resourceType;
		updateMass();
	}
	
	public ItemResource(ResourceType resourceType, int quantity) {
		super(ItemType.RESOURCE, resourceType.getString(), null, quantity);
		this.resourceType = resourceType;	
		updateMass();
	}
	
	public ResourceType getResourceType() {return resourceType;}

	@Override
	public void effect(WarpedObjectIdentity objectID) {
		// TODO 20/5/24 -> Implement resourceItem effect method / system?
		
	}

	@Override
	protected void updateRaster() {return;}

	@Override
	public double massOfOne() {return resourceType.getResourceMass();}

	@Override
	public double valueOfOne() {
		// TODO 20/5/24 -> Implement get value function in resourceType enumeration
		return 0;
	}


}
