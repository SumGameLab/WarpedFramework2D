/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.sphere;

import warped.utilities.math.vectors.VectorD;

public class SphereD {

	
	public VectorD position;
	public double radius;
	@SuppressWarnings("unused")
	private double x1, x2, y1, y2, z1, z2;
	
	public SphereD() {
		position = new VectorD();
		radius = 0;
		x1 = x2 = y1 = y2 = z1 = z2 = 0;
	}
	public SphereD(VectorD pos, double radius) {
		position = pos;
		this.radius = radius;
		x1 = pos.x() - radius;
		x2 = pos.x() + radius + 1;
		y1 = pos.y() - radius;
		y2 = pos.y() + radius;
		z1 = pos.z() - radius;
		z2 = pos.z() + radius;
	}
	
	
	public boolean contains(VectorD point) {
		boolean result = false;
		//FIXME 18/1/23 -> fix contains method
		//if(VectorD.doubleDifference(position, point) <= radius) result = true;
		return result;
	}
	
	public static boolean contains(SphereI sphere, VectorD point) {
		boolean result = false;
		//FIXME 18/1/24 -> fix contains method, apparently this still doesn't work?
		//if(Vec3i.doubleDifference(sphere.position, point) <= sphere.radius) result = true;
		return result;
	}
	

}
