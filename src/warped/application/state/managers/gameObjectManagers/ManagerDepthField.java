package warped.application.state.managers.gameObjectManagers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.depthFields.WarpedDepthField;
import warped.application.state.WarpedGroup;
import warped.application.state.WarpedGroupIdentity;
import warped.application.state.WarpedManager;
import warped.application.state.WarpedObject;
import warped.application.state.WarpedState;
import warped.graphics.window.WarpedWindow;
import warped.utilities.utils.Console;
/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

public class ManagerDepthField <T extends WarpedObject> extends WarpedManager<T> {

	private static boolean isBackgroundBaked;
	
	private static WarpedGroup<WarpedDepthField> bakedGroup;
	private static WarpedGroup<WarpedDepthField> primaryGroup;
	
	
	protected ManagerDepthField() {
		super("Depthfield Manager");
	}
	
	@SuppressWarnings("unchecked")
	public void initialize() {
		if(bakedGroup != null) {
			Console.err("ManagerDepthField -> initialize() -> is already initialized");
			return;
		} else {
			bakedGroup = (WarpedGroup<WarpedDepthField>) addGroup();
			primaryGroup = (WarpedGroup<WarpedDepthField>) addGroup();
		}
	}
	
	public void openBackgroundUnbaked() {
		openGroup(primaryGroup);
		closeGroup(bakedGroup);
	}
	
	public void addBackgroundMember(WarpedDepthField depthField) {getPrimaryGroup().addMember(depthField);}
	
	@SuppressWarnings("unchecked")
	private static WarpedGroup<WarpedDepthField> getPrimaryGroup() {return primaryGroup;}
	
	public void panLeft() {
		if(isBackgroundBaked)return;
		else primaryGroup.forEach(f -> {f.panLeft();});
	}
	public void panRight() {
		if(isBackgroundBaked)return;
		else primaryGroup.forEach(f -> {f.panRight();});	
	}
	public void panUp() {
		if(isBackgroundBaked)return;
		else primaryGroup.forEach(f -> {f.panUp();});
	}
	public void panDown() {
		if(isBackgroundBaked)return;
		else primaryGroup.forEach(f -> {f.panDown();});
	}
	
	public boolean isBackgroundBaked() {return isBackgroundBaked;}
	
	public void bakeBackground() {
		if(isBackgroundBaked) {
			Console.err("ManagerDepthField -> bakeBackground() -> background is already baked");
			return;
		}
		
		isBackgroundBaked = true;
				
		BufferedImage bakedImage = new BufferedImage(WarpedWindow.getWindowWidth(), WarpedWindow.getWindowHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = bakedImage.getGraphics();
				
		for(int i = 0; i < primaryGroup.size(); i++) {
			WarpedDepthField t = primaryGroup.getMember(i);
			g.drawImage(t.raster(), (int)t.getRenderPosition().x(), (int)t.getRenderPosition().y(), (int)t.getRenderSize().x(), (int)t.getRenderSize().y(), null);
		}
		
		bakedGroup.clearMembers();
		bakedGroup.addMember(new WarpedDepthField(bakedImage));
		closeGroup(primaryGroup);
		openGroup(bakedGroup);
		Console.ln("ManagerDepthField -> bakeBackground() -> background was baked");
		
	}
	
	public void unbakeBackground() {
		if(!isBackgroundBaked) {
			Console.err("ManagerDepthField -> unbakeBackground() -> background is not baked");
			return;
		}
		isBackgroundBaked = false;
		WarpedState.depthFieldManager.openGroup(primaryGroup);
		WarpedState.depthFieldManager.closeGroup(bakedGroup);
		Console.ln("ManagerDepthField -> bakeBackground() -> background was unbaked");
	}
	
}

