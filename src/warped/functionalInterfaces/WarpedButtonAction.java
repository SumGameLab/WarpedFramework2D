package warped.functionalInterfaces;

import warped.user.mouse.WarpedMouseEvent;

@FunctionalInterface
public interface WarpedButtonAction {
	public void action(WarpedMouseEvent mouseEvent);
}
