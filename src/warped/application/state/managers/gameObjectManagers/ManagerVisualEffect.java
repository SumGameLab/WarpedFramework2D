/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import warped.application.object.WarpedObject;
import warped.application.prop.WarpedProp;
import warped.application.prop.animation.PropAnimation;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.utils.Console;

public class ManagerVisualEffect<T extends WarpedObject> extends WarpedManager<T>{

	
	private static WarpedGroupIdentity vfxGroupID;
	private static WarpedGroup<WarpedProp> vfxGroup;
	
	
	protected ManagerVisualEffect() {
		managerType = WarpedManagerType.VFX;
	}
	
	
	@SuppressWarnings("unchecked")
	public void init() {
		if(vfxGroup != null) {
			Console.err("ManagerVisualEffect -> init() -> alraeady initialized");
			return;
		} else {
			vfxGroupID = addGroup();
			vfxGroup = (WarpedGroup<WarpedProp>) getGroup(vfxGroupID);
			openGroup(vfxGroupID);
		}
	}
	
	
	
	public PropAnimation addPersistantAnimation(WarpedSpriteSheet sheet, double x, double y, boolean isAutoRestarting) {
		PropAnimation result = new PropAnimation(sheet, x, y, isAutoRestarting);
		vfxGroup.addMember(result);
		return result;
	}
	
	public void playAnimation(WarpedSpriteSheet sheet, double x, double y) {
		vfxGroup.addMember(new PropAnimation(sheet, x, y));
	}
	
	public void addProp(WarpedProp prop) {
		vfxGroup.addMember(prop);
	}

	
}
	

