package warped.application.assemblys;

import warped.application.gui.GUITextInputLine;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.window.WarpedWindow;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;

public class ConsoleInput extends WarpedAssembly {

	private GUITextInputLine inputLine = new GUITextInputLine();
	
	public ConsoleInput(WarpedManagerType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void defineAssembly() {
		inputLine.setSize(WarpedWindow.getWindowWidth(), 40);
		inputLine.setBorderColor(Colour.BLACK.getColor(120));
		inputLine.setBackgroundColor(Colour.GREY_DARK.getColor(120));
		inputLine.setBlankText("");
		inputLine.setButtonVisible(false);
		inputLine.setPosition(0, WarpedWindow.getWindowHeight() - inputLine.getHeight());
		inputLine.setButtonAction(line -> {
			Console.executeCommand(line);
			inputLine.setInputState(true);
		});
	}

	@Override
	protected void addAssembly() {
		addMember(inputLine);
		
	}
	
	public void open() {
		if(!isOpen) {			
			WarpedState.openGroup(groupID);
			inputLine.setInputState(true);
			isOpen = true;
		}
	}

	public void close() {
		if(isOpen) {			
			WarpedState.closeGroup(groupID);
			inputLine.setInputState(false);
			isOpen = false;
		}
	}


}
