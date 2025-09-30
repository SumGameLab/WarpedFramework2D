package warped.application.state.managers.gameObjectManagers;

import warped.application.state.WarpedGroup;

@FunctionalInterface
public interface WarpedGroupAction<T extends WarpedGroup<?>> {
	public void action(WarpedGroup<?> warpedGroup);

}
	

