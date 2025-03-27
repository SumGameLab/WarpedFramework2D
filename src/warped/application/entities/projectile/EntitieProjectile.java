/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import warped.application.entities.WarpedEntitie;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;

public class EntitieProjectile extends WarpedEntitie {

	private WarpedEntitie parent;
	
	private ProjectileType type;
	private Color color = Color.WHITE; 
	private final static VectorI size = new VectorI(1);

	private boolean hasCollided = false;
	private VectorD velocity = new VectorD();
	
	private List<WarpedGroupIdentity> collisionGroups;
	
	public EntitieProjectile(ProjectileType type, WarpedEntitie parent, VectorD velocity, List<WarpedGroupIdentity> collisionGroups) {
		ateractive();
		this.parent = parent;
		this.type = type;
		this.velocity = velocity;
		setPosition(parent.getCenterX(), parent.getCenterY());
		
		for(int i = 0; i < collisionGroups.size(); i++) {
			if(collisionGroups.get(i).getManagerType() != WarpedManagerType.ENTITIE) {
				System.err.println("ProjectileEntitie -> constructor() -> collision groups must be entitie groups, projectiles are designed to be compatible with entities");
				return;
			}
		}
		this.collisionGroups = collisionGroups;
		
		setSize(size.x(), size.y());
		Graphics g = getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, size.x(), size.y());
		g.dispose();
		pushGraphics();
	}
	
	public ProjectileType getType() {return type;}
	
	@Override
	public void updateObject() {
		move(velocity);

		if(hasCollided) return;
		
		velocity.add(-type.getDrag(), -type.getDrag());
		if(velocity.getMagnitude() < 0.1) {
			System.out.println("ProjectileEntitie -> updateObject() -> projectile killed");
			kill();
			return;
		}
		
		
		for(int i = 0; i < collisionGroups.size(); i++) {
			WarpedGroupIdentity collisionGroup = collisionGroups.get(i);
		
			WarpedState.entitieManager.getGroup(collisionGroup).forEach(e -> {
				if(e.getObjectID().isEqualTo(parent));
				else if(collision(e)) {
					hasCollided = true;
					e.projectileCollision(this);
					kill();
					return;
				}
			});
		}
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
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	
}
