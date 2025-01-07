/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.drink;

import java.awt.image.BufferedImage;

import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ItemType;
import warped.application.object.WarpedObjectIdentity;

public class ItemDrink extends WarpedItem {

	private DrinkType drinkType;
	

	public ItemDrink(DrinkType drinkType) {
		super(ItemType.DRINK, drinkType.getString(), new BufferedImage(1,1,1));
		this.drinkType = drinkType;
	}
	
	public ItemDrink(DrinkType drinkType, int quantity) {
		super(ItemType.DRINK, drinkType.getString(), new BufferedImage(1,1,1), quantity);
		this.drinkType = drinkType;
	}
	
	public DrinkType getDrinkType() {return drinkType;}

	@Override
	public double massOfOne() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double valueOfOne() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void effect(WarpedObjectIdentity objectID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateRaster() {
		// TODO Auto-generated method stub
		
	}
	

}
