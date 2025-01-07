/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.equipment.tool;

import warped.application.entities.item.equipment.EquipmentType;
import warped.application.entities.item.equipment.ItemEquipment;

public abstract class EquipmentTool extends ItemEquipment {

	protected ToolType toolType;
	

	public EquipmentTool(ToolType toolType) {
		super(EquipmentType.TOOL, toolType.getString());
		this.toolType = toolType;
	}
	
	public ToolType getToolType() {return toolType;}

}
