/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.equipment.apparel;

import warped.application.entities.item.equipment.EquipmentType;
import warped.application.entities.item.equipment.ItemEquipment;

public abstract class EquipmentApparel extends ItemEquipment {

	private ApparelType apparelType;
	

	public EquipmentApparel(ApparelType apparelType) {
		super(EquipmentType.APPAREL, apparelType.getString());
		equipmentType = EquipmentType.APPAREL;
	}
	
	public ApparelType getApparelType() {return apparelType;}

}
