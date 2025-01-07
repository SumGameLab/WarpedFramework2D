/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.util.ArrayList;
import java.util.List;

import warped.application.gui.lists.GUIListButton;
import warped.application.object.WarpedObject;
import warped.application.object.WarpedOption;
import warped.application.state.WarpedAssembly;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.window.WarpedWindow;
import warped.user.mouse.WarpedMouse;
import warped.utilities.utils.Console;

public class AssemblyWarpedSelector extends WarpedAssembly {

	public static WarpedObject selectObject;
	private GUIListButton optionButtons = new GUIListButton(150, 420);
	private boolean strictNaming = true;
	
	public AssemblyWarpedSelector(WarpedManagerType type) {
		super(type);		
		
	}

	public void select(WarpedObject obj) {
		if(obj == null) {
			Console.err("WarpedSelector -> select() -> tried to select a null object, somehow?");
			return;
		}
		selectObject = obj;
		select( obj.getSelectOptions());
	}
	public void select(WarpedObject obj, List<WarpedOption> extraOptions) {
		ArrayList<WarpedOption> combined = new ArrayList<>(obj.getSelectOptions());
		Console.ln("WarpedSelctor -> select() -> combined size : " + combined.size());
		for(int i = 0; i < extraOptions.size(); i++) combined.add(extraOptions.get(i));
		select(combined);
	}
		
	public void select(ArrayList<WarpedOption> options) {
		ArrayList<WarpedOption> strictOptions;
		if(strictNaming) {
			strictOptions = new ArrayList<>();
			for(WarpedOption opt1 : options) {
				boolean isOk = true;
				for(WarpedOption opt2 : strictOptions) {
					if(opt1.getName() == opt2.getName()) isOk = false;
				}
				if(isOk) strictOptions.add(opt1);
			}
			options = strictOptions;
		}
		
		optionButtons.setOptions(options);
		if(WarpedMouse.getPoint().y + optionButtons.getMenuSize().y > WarpedWindow.height) optionButtons.setPosition(WarpedMouse.getPoint().x - 60, WarpedMouse.getPoint().y + 15 - optionButtons.getSize().y);
		else optionButtons.setPosition(WarpedMouse.getPoint().x - 60, WarpedMouse.getPoint().y - 15);
		optionButtons.visible();
		open();
	}	
	
	@Override
	protected void offsetAssembly() {return;}

	@Override
	protected void defineAssembly() {
		optionButtons.setExitAction(() -> {
			close();
		});
		optionButtons.autoHide();
	}

	@Override
	protected void addAssembly() {
		addMember(optionButtons);
	}

	@Override
	public void updateAssembly() {return;}
	

}
