package test;

import test.ExampleItem.ExampleItems;
import warped.application.entities.item.WarpedItem;
import warped.application.gui.GUIInventory;
import warped.graphics.window.WarpedMouse;
import warped.user.mouse.WarpedMouseController;

public class ExampleMouseController extends WarpedMouseController {

	protected WarpedItem<ExampleItems> dragItem;
	
	public void update() {
		if(isDraggingItem && !WarpedMouse.isDragging()) {
			dropTick++;
			if(dropTick > DROP_DELAY) {
				dropTick = 0;
				isDraggingItem = false;
				resetCursor();
				dragItem.setPosition(mousePoint.x, mousePoint.y);
				ExampleApplication.itemDropGroup.dropMember(dragItem);
				//dragGUI.restoreItem(dragItem);
			}
		}
		
	}
	
	
	/**Drag an item with the mouse
	 * @param dragGUI - the GUI that the item was dragged from.
	 * @param dragItem - the item that is being dragged.War
	 * @implNote Will set the cursor to match the dragged item.
	 * @author 5som3 
	 * */
	public void dragItem(GUIInventory<?> dragGUI, WarpedItem<?> dragItem) {
		this.dragGUI = dragGUI;
		this.dragItem = (WarpedItem<ExampleItems>) dragItem;
		isDraggingItem = true;
	}
	
	
	
}
