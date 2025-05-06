/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import java.awt.Rectangle;

import warped.application.gui.GUIButton;
import warped.application.gui.GUIShape;
import warped.application.gui.GUITextBoxLined;
import warped.application.gui.GUITextInputLine;
import warped.application.state.GUIAssembly;
import warped.application.state.WarpedState;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.utilities.enums.generalised.Colour;

public class FileExplorer  extends GUIAssembly {

	private GUIButton title  	= new GUIButton("File Explorer");
	private GUIButton close 	= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIButton back 		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_LEFT));
	private GUIButton forward 	= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_RIGHT));
	private GUIShape background = new GUIShape(800, 600);
	
	private GUITextBoxLined files = new GUITextBoxLined();
	private GUITextInputLine textLine = new GUITextInputLine();
	
	
	public FileExplorer() {
		super();

	}


	@Override
	protected void defineAssembly() {
		close.setOffset(400 - 30, 0);
		forward.setOffset(400 - 60, 0);
		back.setOffset(400 - 90,  0);
		
		background.addRectangle(new Rectangle(400, 600), Colour.BLACK);
		background.addRectangle(new Rectangle(2, 2, 396, 596), Colour.GREY_DARK);
		
		title.setDragable(true);
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				i.offset(title);
			});			
		});
		
		close.setReleasedAction(event -> {close();});
		
			
	}

	@Override
	protected void addAssembly() {
		addMember(background);
		addMember(title);
		addMember(close);
		addMember(forward);
		addMember(back);
	}

	

	
	
}
