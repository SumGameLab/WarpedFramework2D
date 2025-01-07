/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities;

import warped.application.entities.projectile.EntitieProjectile;
import warped.application.object.WarpedObject;
import warped.application.object.WarpedOption;
import warped.application.state.WarpedState;
import warped.user.mouse.WarpedMouseEvent;

public abstract class WarpedEntitie extends WarpedObject {
	
	/** ENTITIE
	 * do not put pointers to other entities on entities
	 * 		doing so will cause issues when the entitie pointed to (may / may not) be removed
	 * 		then you are left with a null pointer
	 * 
	 * */
	
	//protected Sprite sprite;
	//private ArrayList<EntitieEffect> affects = new ArrayList<>(); //apply to another entitie
	//private ArrayList<EntitieEffect> effects = new ArrayList<>(); //applied to this entitie
	
	
	public WarpedEntitie() {
		addSelectOption(new WarpedOption("Inspect", () -> {
			WarpedState.selector.close();
			WarpedState.gameObjectInspector.select(objectID); 
			WarpedState.gameObjectInspector.toggle();
		}));
	}
	
	
	
	public void projectileCollision(EntitieProjectile collider) {
		System.out.println("WarpedEntitie -> projectileCollision() -> projectile hit entitie");
	}
	
	//protected void updateRaster() {sprite.update();}
	//protected void setSprite(Sprite sprite) {this.sprite = sprite; setRaster(sprite.raster());}
	
	
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		//WarpedState.selectionManager.select(objectID);
		WarpedState.selector.select(this);
	};
	
	

	protected void updateMid() {return;};
	
	protected void updateSlow() {return;}; 

	protected void updatePassive() {return;};
}
