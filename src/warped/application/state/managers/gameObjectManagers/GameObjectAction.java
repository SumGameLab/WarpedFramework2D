/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import warped.application.object.WarpedObject;

@FunctionalInterface
public interface GameObjectAction {
	public void action(WarpedObject object);
}
