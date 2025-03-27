/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import java.util.ArrayList;

import warped.application.actionWrappers.ActionOption;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedState;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorI;

public class WarpedTile extends WarpedObject {

	/*
	 * NOTE : 
	 * For a large game such as WarpTech, there will be several million tiles
	 * Obviously updating 20+ million entities is less than optimal.
	 * The tiles should be kept in an unactive groups and then call updates on them very infrequently or on demand 
	 * */

	protected int index = -1;
	protected VectorI coordinates = new VectorI();
	
	private ArrayList<ActionOption> selectOptions = new ArrayList<>();

	
	//--------
	//------------------- Access ---------------------
	//--------	
	
	/**Set the coordinates for this tile.
	 * @param coordinates - the location of the tile in its tileMap.
	 * @author 5som3 */
	public void setCoordinates(VectorI coordinates) {this.coordinates = coordinates;}
	
	public void setCoordinates(int x, int y, int index) {coordinates.set(x, y); this.index = index;}
	public int getIndex() {return index;}
	public VectorI getCoords() {return coordinates;}
	
	public final ActionOption getSelectOption(int index) {return selectOptions.get(index);}
	public final ArrayList<ActionOption> getSelectOptions() {return selectOptions;}
	
	protected final void addSelectOption(ActionOption option) {selectOptions.add(option);}
	protected final void clearSelectOptions() {selectOptions.clear();}
	
	public double getRoughness() {return 1.0;}
	
	//--------
	//------------------- Interaction ---------------------
	//--------
	protected void initializeSelectOptions() {
		addSelectOption(new ActionOption("Inspect", () -> {
			WarpedState.selector.close();
			
			WarpedState.gameObjectInspector.select(this);
			WarpedState.gameObjectInspector.open();
		}));
	}
	
	
	@Override
	protected void mouseEntered() {return;}
	@Override
	protected void mouseExited() {return;}
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {WarpedState.selector.select(this);}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	
	//--------
	//------------------- Update ---------------------
	//--------

	
	@Override
	public void updateObject() {return;}

	@Override
	public void updateMid() {return;};
	
	@Override
	public void updateSlow() {return;}; 

	@Override
	public void updatePassive() {return;};
	
}
