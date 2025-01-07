/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import warped.application.state.WarpedState;
import warped.application.tile.WarpedTile;
import warped.graphics.sprite.ButtonSprite;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.ButtonStateType;

public class GUITile extends WarpedGUI {

	ButtonSprite tileSprite;
	WarpedTile tile;
	
	public GUITile(WarpedTile tile, int xOffset, int yOffset) {
		this.tile = tile;
		this.tileSprite = new ButtonSprite(tile.raster());
		position.x = tile.getPosition().x + xOffset;
		position.y = tile.getPosition().y + yOffset;
		tileSprite.plain();
		setRaster(tileSprite.raster());
	}
	
	public void refresh() {
		tile = WarpedState.tileManager.getMember(tile.getObjectID());
		tileSprite = new ButtonSprite(tile.raster());
		setRaster(tileSprite.raster());
	}
	
	@Override
	protected void mouseEntered() {
		tileSprite.hover();
		setRaster(tileSprite.raster());
	}

	@Override
	protected void mouseExited() {
		tileSprite.plain();
		setRaster(tileSprite.raster());
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(tileSprite.getButtonState() != ButtonStateType.PRESSED) {
			tileSprite.press();
			setRaster(tileSprite.raster());
		}
		
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		tileSprite.hover();
		setRaster(tileSprite.raster());
		tile.mouseEvent(mouseEvent);
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

}
