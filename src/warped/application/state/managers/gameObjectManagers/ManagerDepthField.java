package warped.application.state.managers.gameObjectManagers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.depthFields.DepthFieldPrimitive;
import warped.application.depthFields.WarpedDepthField;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.graphics.window.WarpedWindow;
import warped.utilities.utils.Console;
/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

public class ManagerDepthField <T extends WarpedObject> extends WarpedManager<T> {

	private static boolean isBackgroundBaked;
	
	private static WarpedGroupIdentity bakedGroupID;
	private static WarpedGroupIdentity primaryGroupID;
	
	
	protected ManagerDepthField() {managerType = WarpedManagerType.DEPTH_FIELD;}
	
	public void initialize() {
		if(bakedGroupID != null) {
			Console.err("ManagerDepthField -> initialize() -> is already initialized");
			return;
		} else {
			bakedGroupID = addGroup();
			primaryGroupID = addGroup();
		}
	}
	
	public void openBackgroundUnbaked() {
		openGroup(primaryGroupID);
		closeGroup(bakedGroupID);
	}
	
	public void addBackgroundMember(WarpedDepthField depthField) {getPrimaryGroup().addMember(depthField);}
	private static WarpedGroup<WarpedDepthField> getPrimaryGroup() {return WarpedState.depthFieldManager.getGroup(primaryGroupID);}
	
	public void panLeft() {
		if(isBackgroundBaked)return;
		else WarpedState.depthFieldManager.getGroup(primaryGroupID).forEach(f -> {f.panLeft();});
	}
	public void panRight() {
		if(isBackgroundBaked)return;
		else WarpedState.depthFieldManager.getGroup(primaryGroupID).forEach(f -> {f.panRight();});	
	}
	public void panUp() {
		if(isBackgroundBaked)return;
		else WarpedState.depthFieldManager.getGroup(primaryGroupID).forEach(f -> {f.panUp();});
	}
	public void panDown() {
		if(isBackgroundBaked)return;
		else WarpedState.depthFieldManager.getGroup(primaryGroupID).forEach(f -> {f.panDown();});
	}
	
	public boolean isBackgroundBaked() {return isBackgroundBaked;}
	
	public void bakeBackground() {
		if(isBackgroundBaked) {
			Console.err("ManagerDepthField -> bakeBackground() -> background is already baked");
			return;
		}
		
		isBackgroundBaked = true;
		
		WarpedGroup<T> group = getGroup(primaryGroupID);
		
		BufferedImage bakedImage = new BufferedImage(WarpedWindow.width, WarpedWindow.height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = bakedImage.getGraphics();
				
		for(int i = 0; i < group.size(); i++) {
			T t = group.getMember(i);
			g.drawImage(t.raster(), (int)t.renderPosition.x, (int)t.renderPosition.y, (int)t.renderSize.x, (int)t.renderSize.y, null);
		}
		
		getGroup(bakedGroupID).clearMembers();
		WarpedState.depthFieldManager.getGroup(bakedGroupID).addMember(new DepthFieldPrimitive(bakedImage));
		WarpedState.depthFieldManager.closeGroup(primaryGroupID);
		WarpedState.depthFieldManager.openGroup(bakedGroupID);
		Console.ln("ManagerDepthField -> bakeBackground() -> background was baked");
		
	}
	
	public void unbakeBackground() {
		if(!isBackgroundBaked) {
			Console.err("ManagerDepthField -> unbakeBackground() -> background is not baked");
			return;
		}
		isBackgroundBaked = false;
		WarpedState.depthFieldManager.openGroup(primaryGroupID);
		WarpedState.depthFieldManager.closeGroup(bakedGroupID);
		Console.ln("ManagerDepthField -> bakeBackground() -> background was unbaked");
	}
	
}

