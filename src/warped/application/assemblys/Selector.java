/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.util.ArrayList;
import java.util.List;

import warped.application.actionWrappers.ActionOption;
import warped.application.entities.WarpedEntitie;
import warped.application.gui.GUIListVertical;
import warped.application.state.GUIAssembly;
import warped.application.tile.WarpedTile;
import warped.graphics.window.WarpedMouse;
import warped.graphics.window.WarpedWindow;
import warped.utilities.utils.Console;

public class Selector extends GUIAssembly {

	private GUIListVertical optionButtons = new GUIListVertical(150, 420);
	private boolean strictNaming = true;
	
	public Selector() {
		super();		
		
	}

	/**Display the select options for an entitie.
	 * @param ent - the selected entitie.
	 * @apiNote Select options can be set for any warped object.
	 * @author 5som3*/
	public void select(WarpedEntitie ent) {
		if(ent == null) {
			Console.err("WarpedSelector -> select() -> tried to select a null object, somehow?");
			return;
		}
		select(ent.getSelectOptions());
	}
	
	/**Display the select options for an entitie.
	 * @param ent - the selected entitie.
	 * @apiNote Select options can be set for any warped object.
	 * @author 5som3*/
	public void select(WarpedTile tile) {
		if(tile == null) {
			Console.err("WarpedSelector -> select() -> tried to select a null object, somehow?");
			return;
		}
		select(tile.getSelectOptions());
	}
	
	
	/**Display the select options for an entitie as well as a list of extra options.
	 * @param ent - the selected entitie to get options from.
	 * @param extraOptions - a list of extra options to display underneath the options from the select entitie.
	 * @author 5som3*/
	public void select(WarpedEntitie ent, List<ActionOption> extraOptions) {
		ArrayList<ActionOption> combined = new ArrayList<>(ent.getSelectOptions());
		Console.ln("WarpedSelctor -> select() -> combined size : " + combined.size());
		for(int i = 0; i < extraOptions.size(); i++) combined.add(extraOptions.get(i));
		select(combined);
	}
		
	/**Display a list of select
	 * */
	public void select(ArrayList<ActionOption> options) {
		ArrayList<ActionOption> strictOptions;
		if(strictNaming) {
			strictOptions = new ArrayList<>();
			for(ActionOption opt1 : options) {
				boolean isOk = true;
				for(ActionOption opt2 : strictOptions) {
					if(opt1.getName() == opt2.getName()) isOk = false;
				}
				if(isOk) strictOptions.add(opt1);
			}
			options = strictOptions;
		}
		
		optionButtons.setOptions(options);
		if(WarpedMouse.getPoint().y + optionButtons.getWidth() > WarpedWindow.getWindowHeight()) optionButtons.setPosition(WarpedMouse.getPoint().x - 60, WarpedMouse.getPoint().y + 15 - optionButtons.getHeight());
		else optionButtons.setPosition(WarpedMouse.getPoint().x - 60, WarpedMouse.getPoint().y - 15);
		optionButtons.setVisible(true);
		open();
	}	
	
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
	

}
