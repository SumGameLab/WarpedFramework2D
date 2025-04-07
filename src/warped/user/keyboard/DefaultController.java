/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;

import warped.WarpedFramework2D;
import warped.WarpedProperties;
import warped.application.state.WarpedState;

public class DefaultController extends WarpedKeyboardController {

	public DefaultController() {
		name = "Default Controller";
		
		addKeyBind(new WarpedKeyBind("Toggle File Explorer", KeyEvent.VK_F7, null, () -> {WarpedState.fileExplorer.toggle();}));
		addKeyBind(new WarpedKeyBind("Toggle Console", KeyEvent.VK_BACK_QUOTE, null, () -> {WarpedState.consoleInput.toggle();}));
		addKeyBind(new WarpedKeyBind("Toggle Framework Inspector", KeyEvent.VK_F5, null, () -> {WarpedState.frameworkInspector.toggle();}));
		addKeyBind(new WarpedKeyBind("Toggle Object Inspector", KeyEvent.VK_F6, null, () -> {WarpedState.gameObjectInspector.toggle();}));
		addKeyBind(new WarpedKeyBind("Toggle Cap Render Size", KeyEvent.VK_F2, null, () -> {WarpedProperties.toggleCapMinSize();}));
		addKeyBind(new WarpedKeyBind("Toggle Full screen", KeyEvent.VK_F3, null, () -> {WarpedFramework2D.getWindow().toggleFullscreen();}));
		
		addKeyBind(new WarpedKeyBind("Pan Up", KeyEvent.VK_UP, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panUp();}, null));
		addKeyBind(new WarpedKeyBind("Pan Down", KeyEvent.VK_DOWN, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panDown();}, null));
		addKeyBind(new WarpedKeyBind("Pan Left", KeyEvent.VK_LEFT, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panLeft();}, null));
		addKeyBind(new WarpedKeyBind("Pan Right", KeyEvent.VK_RIGHT, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panRight();}, null));
		addKeyBind(new WarpedKeyBind("Zoom In", KeyEvent.VK_PAGE_UP, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().zoomIn();}, null));
		addKeyBind(new WarpedKeyBind("Zoom Out", KeyEvent.VK_PAGE_DOWN, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().zoomOut();}, null));
	}
	
}
