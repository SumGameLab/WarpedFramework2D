/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.keyboard;

import java.awt.event.KeyEvent;

public class DefaultController extends WarpedKeyboardController {

	public DefaultController() {
		type = WarpedControllerType.DEFAULT;
		name = "Default Controller";
		
		addKeyBind(new WarpedKeyBind<>("Toggle Framework Inspector", KeyEvent.VK_F5, null, toggleFrameworkInspector));
		addKeyBind(new WarpedKeyBind<>("Toggle Framework Inspector", KeyEvent.VK_F6, null, toggleObjectInspector));
		addKeyBind(new WarpedKeyBind<>("Toggle Cap Render Size", KeyEvent.VK_F2, null, toggleCapRenderSize));
		addKeyBind(new WarpedKeyBind<>("Toggle Full screen", KeyEvent.VK_F3, null, toggleFullscreen));
		addKeyBind(new WarpedKeyBind<>("Toggle Full screen", KeyEvent.VK_F5, null, toggleFullscreen));
		addKeyBind(new WarpedKeyBind<>("Pan Up", KeyEvent.VK_UP, panDefaultEntitieUp, null));
		addKeyBind(new WarpedKeyBind<>("Pan Down", KeyEvent.VK_DOWN, panDefaultEntitieDown, null));
		addKeyBind(new WarpedKeyBind<>("Pan Left", KeyEvent.VK_LEFT, panDefaultEntitieLeft, null));
		addKeyBind(new WarpedKeyBind<>("Pan Right", KeyEvent.VK_RIGHT, panDefaultEntitieRight, null));
		addKeyBind(new WarpedKeyBind<>("Zoom In", KeyEvent.VK_PAGE_UP, zoomInDefaultEntitie, null));
		addKeyBind(new WarpedKeyBind<>("Zoom Out", KeyEvent.VK_PAGE_DOWN, zoomOutDefaultEntitie, null));
	}
	
}
