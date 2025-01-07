/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.ammunition;

import java.awt.image.BufferedImage;

import warped.application.entities.item.ItemType;
import warped.application.entities.item.WarpedItem;
import warped.application.object.WarpedObjectIdentity;

public class ItemAmmunition extends WarpedItem {

	private AmmunitionType ammunitionType;
	
	public ItemAmmunition(AmmunitionType ammunitionType) {
		super(ItemType.AMMUNITION, ammunitionType.getString(), new BufferedImage(1,1,1));
		this.ammunitionType = ammunitionType;
	}
	
	public ItemAmmunition(AmmunitionType ammunitionType, int quantity) {
		super(ItemType.AMMUNITION, ammunitionType.getString(), new BufferedImage(1,1,1), quantity);
		this.ammunitionType = ammunitionType;
	}
		
	
	

	public AmmunitionType getAmmunitionType() {return ammunitionType;}
	@Override
	public double massOfOne() {return ammunitionType.getValue();}

	@Override
	public void effect(WarpedObjectIdentity objectID) {
		// TODO 20/5/24 -> implement effect 
		
	}

	@Override
	protected void updateRaster() {return;}

	@Override
	public double valueOfOne() {return ammunitionType.getValue();}


	
}
