package warped.application.assemblys;

import warped.application.gui.GUIButton;
import warped.application.state.WarpedAssembly;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;

public class AssemblyFileInspector  extends WarpedAssembly {

	public GUIButton title  = new GUIButton("File Inspector");
	public GUIButton close = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	
	public AssemblyFileInspector(WarpedManagerType type) {
		super(type);

	}

	@Override
	protected void offsetAssembly() {

		
	}

	@Override
	protected void defineAssembly() {

		
	}

	@Override
	protected void addAssembly() {

		
	}

	@Override
	protected void updateAssembly() {

		
	}

	
	
}
