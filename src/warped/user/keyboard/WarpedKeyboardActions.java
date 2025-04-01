/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import warped.WarpedFramework2D;
import warped.WarpedProperties;
import warped.application.state.WarpedState;
import warped.functionalInterfaces.WarpedAction;
import warped.utilities.utils.Console;

public interface WarpedKeyboardActions {

	WarpedAction toggleConsoleInput = () -> {WarpedState.consoleInput.toggle();};
	
	WarpedAction toggleFrameworkInspector = () -> {WarpedState.frameworkInspector.toggle();};
	WarpedAction toggleObjectInspector    = () -> {WarpedState.gameObjectInspector.toggle();};
	WarpedAction toggleFullscreen 		  = () -> {WarpedFramework2D.getWindow().toggleFullscreen();};
	WarpedAction toggleCapRenderSize 	  = () -> {WarpedProperties.toggleCapMinSize();};

	WarpedAction panDefaultEntitieUp 	= () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panUp();};
	WarpedAction panDefaultEntitieDown  = () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panDown();};
	WarpedAction panDefaultEntitieLeft  = () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panLeft();};
	WarpedAction panDefaultEntitieRight = () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panRight();};
	WarpedAction zoomInDefaultEntitie   = () -> {WarpedState.cameraManager.getDefaultEntitieCamera().zoomIn();};
	WarpedAction zoomOutDefaultEntitie  = () -> {WarpedState.cameraManager.getDefaultEntitieCamera().zoomOut();};
	
	
	
}

