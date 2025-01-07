/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.projectile;

public enum ProjectileType {

	BULLET,
	RAIL;

	
	public double getDrag() {return getDrag(this);}
	public static double getDrag(ProjectileType type) {
		switch(type) {
		case BULLET: return 0.001;
		case RAIL: return 0.001;
		default:
			System.err.println("ProjectileType -> getDrag() -> invalid case : " + type);
			return 0.001;
		
		}
	}
	
}
