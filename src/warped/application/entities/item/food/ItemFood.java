/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.food;

import java.awt.image.BufferedImage;

import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ItemType;
import warped.application.object.WarpedObjectIdentity;

public class ItemFood extends WarpedItem {

	protected FoodType foodType;
	
	public ItemFood(FoodType foodType) {
		super(ItemType.FOOD, foodType.getString(), new BufferedImage(1,1,1));
	}
	
	public ItemFood(FoodType foodType, int quantity) {
		super(ItemType.FOOD, foodType.getString(), new BufferedImage(1,1,1), quantity);
	}
	
	public FoodType getFoodType() {return foodType;}

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
