package warped.functionalInterfaces;

import warped.graphics.window.WarpedMouseEvent;

@FunctionalInterface
public interface WarpedButtonAction {
	public void action(WarpedMouseEvent mouseEvent);
}
