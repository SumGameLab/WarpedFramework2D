/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.equipment;

import java.awt.image.BufferedImage;

import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ItemType;
import warped.application.object.WarpedObjectIdentity;
import warped.utilities.utils.UtilsName;

public class ItemEquipment extends WarpedItem {

	protected EquipmentType equipmentType;
	
	protected ItemEquipment(EquipmentType equipmentType, String name) {
		super(ItemType.EQUIPMENT, name, new BufferedImage(1,1,1));
		this.equipmentType = equipmentType;
	}
	
	protected ItemEquipment(EquipmentType equipmentType, int quantity) {
		super(ItemType.EQUIPMENT, UtilsName.getUniqueLatin(), new BufferedImage(1,1,1), quantity);
		this.equipmentType = equipmentType;
	}
	
	public EquipmentType getEquipmentType() {return equipmentType;}

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
