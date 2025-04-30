/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;

import warped.WarpedFramework2D;
import warped.WarpedProperties;
import warped.application.state.WarpedState;

public class DefaultController extends WarpedKeyboardController {

	public DefaultController() {
		name = "Default Controller";
		
		addBinding(new WarpedKeyBind("Toggle File Explorer", KeyEvent.VK_F7, null, () -> {WarpedState.fileExplorer.toggle();}));
		addBinding(new WarpedKeyBind("Toggle Console", KeyEvent.VK_BACK_QUOTE, null, () -> {WarpedState.consoleInput.toggle();}));
		addBinding(new WarpedKeyBind("Toggle Framework Inspector", KeyEvent.VK_F5, null, () -> {WarpedState.frameworkInspector.toggle();}));
		addBinding(new WarpedKeyBind("Toggle Object Inspector", KeyEvent.VK_F6, null, () -> {WarpedState.gameObjectInspector.toggle();}));
		addBinding(new WarpedKeyBind("Toggle Cap Render Size", KeyEvent.VK_F2, null, () -> {WarpedProperties.toggleCapMinSize();}));
		addBinding(new WarpedKeyBind("Toggle Full screen", KeyEvent.VK_F3, null, () -> {WarpedFramework2D.getWindow().toggleFullscreen();}));
		
		addBinding(new WarpedKeyBind("Pan Up", KeyEvent.VK_UP, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panUp();}, null));
		addBinding(new WarpedKeyBind("Pan Down", KeyEvent.VK_DOWN, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panDown();}, null));
		addBinding(new WarpedKeyBind("Pan Left", KeyEvent.VK_LEFT, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panLeft();}, null));
		addBinding(new WarpedKeyBind("Pan Right", KeyEvent.VK_RIGHT, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().panRight();}, null));
		addBinding(new WarpedKeyBind("Zoom In", KeyEvent.VK_PAGE_UP, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().zoomIn();}, null));
		addBinding(new WarpedKeyBind("Zoom Out", KeyEvent.VK_PAGE_DOWN, () -> {WarpedState.cameraManager.getDefaultEntitieCamera().zoomOut();}, null));
	}
	
}
