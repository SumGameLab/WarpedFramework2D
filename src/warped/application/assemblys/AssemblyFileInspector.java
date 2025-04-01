/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import java.awt.Rectangle;

import warped.application.gui.GUIButton;
import warped.application.gui.GUIShape;
import warped.application.state.WarpedAssembly;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.utilities.enums.generalised.Colour;

public class AssemblyFileInspector  extends WarpedAssembly {

	private GUIButton title  	= new GUIButton("File Inspector");
	private GUIButton close 	= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIButton back 		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_LEFT));
	private GUIButton forward 	= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.POINT_RIGHT));
	private GUIShape background = new GUIShape(800, 600);
	
	private int width = 800;
	private int height = 600;
	
	public AssemblyFileInspector(WarpedManagerType type) {
		super(type);

	}


	@Override
	protected void defineAssembly() {
		close.setOffset(width - 30, 0);
		forward.setOffset(width - 60, 0);
		back.setOffset(width - 90,  0);
		
		background.addRectangle(new Rectangle(width, height), Colour.GREY_LIGHT);
		//title.setButtonSize(width - 30 * 3, 30);
		
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
