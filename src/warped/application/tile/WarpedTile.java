/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import warped.application.object.WarpedObject;
import warped.application.object.WarpedOption;
import warped.application.state.WarpedState;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2i;

public class WarpedTile<T extends WarpedTileable<? extends Enum<?>>> extends WarpedObject {

	/**
	 * NOTE : 
	 * For a large game such as WarpTech, there will be several million tiles
	 * Obviously updating 20+ million entities is less than optimal.
	 * The tiles should be kept in an unactive groups and then call updates on them very infrequently or on demand 
	 * */

	protected WarpedTileSet<T> parent; 
	protected T tileType;
	
	protected int index = -1;
	protected Vec2i coordinates = new Vec2i();
	

	public WarpedTile(WarpedTileSet<T> parent, T tileType) {
		this.parent = parent;
		this.tileType = tileType;
	    setRaster(parent.getTileImage(tileType));
		initializeSelectOptions();
	}
	
	
	//--------
	//------------------- Access ---------------------
	//--------	
	
	public double getRoughness() {return tileType.getRoughness();}
	
	public T getPrimaryType() {return tileType;}
	
	public WarpedTileSet<T> getParent() {return parent;}	
	public TerrainTileSet<T> getParentTerrain() {return (TerrainTileSet<T>) parent;}
	
	/**the direction of this tile relative to the one in context. i.e. this tile is above the tile being considered -> the dir is UP*/

	public void setCoordinates(Vec2i coordinates) {this.coordinates = coordinates;}
	public void setCoordinates(int x, int y, int index) {coordinates.set(x, y); this.index = index;}
	public int getIndex() {return index;}
	public Vec2i getCoords() {return coordinates;}
	

	//--------
	//------------------- Interaction ---------------------
	//--------
	protected void initializeSelectOptions() {
		addSelectOption(new WarpedOption("Inspect", () -> {
			WarpedState.selector.close();
			
			WarpedState.gameObjectInspector.select(objectID);
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
	protected void updateRaster() {return;}
	@Override
	protected void updateObject() {return;}
	@Override
	protected void updatePosition() {return;}

	protected void updateMid() {return;};
	
	protected void updateSlow() {return;}; 

	protected void updatePassive() {return;};
	
}
