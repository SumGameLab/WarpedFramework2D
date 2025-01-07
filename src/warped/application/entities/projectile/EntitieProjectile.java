/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import warped.WarpedProperties;
import warped.application.entities.WarpedEntitie;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;

public class EntitieProjectile extends WarpedEntitie {

	private WarpedEntitie parent;
	
	private ProjectileType type;
	private Color color = Color.WHITE; 
	private final static Vec2i size = new Vec2i(1);

	private boolean hasCollided = false;
	private Vec2d velocity = new Vec2d();
	
	private List<WarpedGroupIdentity> collisionGroups;
	
	public EntitieProjectile(ProjectileType type, WarpedEntitie parent, Vec2d velocity, List<WarpedGroupIdentity> collisionGroups) {
		ateractive();
		this.parent = parent;
		this.type = type;
		this.velocity = velocity;
		this.position.set(parent.getCenter());
		
		for(int i = 0; i < collisionGroups.size(); i++) {
			if(collisionGroups.get(i).getManagerType() != WarpedManagerType.ENTITIE) {
				System.err.println("ProjectileEntitie -> constructor() -> collision groups must be entitie groups, projectiles are designed to be compatible with entities");
				return;
			}
		}
		this.collisionGroups = collisionGroups;
		
		BufferedImage img = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, size.x, size.y);
		g.dispose();
		setRaster(img);
	}
	
	public ProjectileType getType() {return type;}
	
	@Override
	protected void updatePosition() {position.add(velocity);}
	@Override
	protected void updateObject() {
		if(hasCollided) return;
		
		
		if(velocity.reduced(type.getDrag(), 0.1)) {
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
	protected void updateRaster() {return;}
	
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
