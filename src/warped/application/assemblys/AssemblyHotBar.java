/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import warped.application.gui.GUIButton;
import warped.application.gui.GUIHotBar;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.keyboard.WarpedKeyboardController;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;

public class AssemblyHotBar extends WarpedAssembly {

	private GUIButton close  = new GUIButton(FrameworkSprites.standardIcons.getSprite(2, 4));
	private GUIButton title  = new GUIButton("HotBar");
	private GUIButton clear  = new GUIButton(FrameworkSprites.standardIcons.getSprite(9, 4));
	private GUIHotBar hotBar = new GUIHotBar();
	
	private Vec2i closeOffset = new Vec2i(150, 0);
	private Vec2i clearOffset = new Vec2i(120, 0);
	private Vec2i hotBarOffset = new Vec2i(0, 30);
	
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
	protected void offsetAssembly() {
		close.setPositionOffset(closeOffset);
		clear.setPositionOffset(clearOffset);
		hotBar.setPositionOffset(hotBarOffset);
		
		close.offsetPosition();
		clear.offsetPosition();
		hotBar.offsetPosition();
	}

	@Override
	protected void defineAssembly() {
		title.draggable();
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				i.setPosition(title.getPosition().x, title.getPosition().y);
				i.offsetPosition();
			});			
		});
		
		close.setToolTip("Close");
		close.setReleasedAction(() -> {close();});
		clear.setToolTip("Clear Binds");
		clear.setReleasedAction(() -> {hotBar.clearBinds();});		
	}

	@Override
	protected void addAssembly() {
		addMember(title);
		addMember(clear);
		addMember(close);
		addMember(hotBar);
	}

	@Override
	protected void updateAssembly() {return;}

	
	
	
}
