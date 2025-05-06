/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;

import warped.WarpedProperties;
import warped.application.state.WarpedFramework2D;
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
		
	}
	
}
