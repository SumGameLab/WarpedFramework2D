/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.actions;

import warped.user.mouse.WarpedMouseEvent;

@FunctionalInterface

public interface WarpedMouseAction {
	public void action(WarpedMouseEvent mouseEvent);
}
