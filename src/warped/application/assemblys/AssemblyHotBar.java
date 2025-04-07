/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import warped.application.gui.GUIButton;
import warped.application.gui.GUIHotBar;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.user.keyboard.WarpedKeyboardController;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class AssemblyHotBar extends WarpedAssembly {

	private GUIButton close  = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIButton title  = new GUIButton("HotBar");
	private GUIButton clear  = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.TRASH));
	private GUIHotBar hotBar = new GUIHotBar();
	
	private VectorI closeOffset = new VectorI(150, 0);
	private VectorI clearOffset = new VectorI(120, 0);
	private VectorI hotBarOffset = new VectorI(0, 30);
	
	public AssemblyHotBar(WarpedManagerType type) {
		super(type);

	}

	
	public void selectController(WarpedKeyboardController controller) {
		hotBar.selectController(controller);
	}
	
	public void updateGraphics() {
		Console.ln("HotBar -> updateGraphics()");
		hotBar.updateGraphics();
	}


	@Override
	protected void defineAssembly() {
		close.setOffset(closeOffset);
		clear.setOffset(clearOffset);
		hotBar.setOffset(hotBarOffset);
		
		close.offset(title);
		clear.offset(title);
		hotBar.offset(title);
		
		title.setDragable(true);
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				i.offset(title);
			});			
		});
		
		close.setToolTip("Close");
		close.setReleasedAction(mouseE -> {close();});
		clear.setToolTip("Clear Binds");
		clear.setReleasedAction(mouseE -> {hotBar.clearBinds();});		
	}

	@Override
	protected void addAssembly() {
		addMember(title);
		addMember(clear);
		addMember(close);
		addMember(hotBar);
	}

	

	
	
	
}
