/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.ellipsoid;

import warped.utilities.math.vectors.VectorI;

public class EllipsoidI {

	public VectorI position;
	public VectorI scale;
	
	@SuppressWarnings("unused")
	private int x1, x2, y1, y2, z1, z2;
	
	public EllipsoidI() {
		position = new VectorI();
		scale = new VectorI();
		x1 = x2 = y1 = y2 = z1 = z2 = 0;
	}
	
	public EllipsoidI(VectorI pos, VectorI scale) {
		position = pos;
		this.scale = scale;
		
		x1 = pos.x() - scale.x();
		x2 = pos.x() + scale.x() + 1;
		y1 = pos.y() - scale.y();
		y2 = pos.y() + scale.y();
		z1 = pos.z() - scale.z();
		z2 = pos.z() + scale.z();
	}
	
	
	public boolean contains(VectorI point) {
		boolean result = false;
		if(   (Math.pow((point.x() - position.x()), 2)/(scale.x()*scale.x())) 
			+ (Math.pow((point.y() - position.y()), 2)/(scale.y()*scale.y())) 
			+ (Math.pow((point.z() - position.z()), 2)/(scale.z()*scale.z())) <= 1)  result = true;
		return result;
	}
	
	
	public static boolean contains(EllipsoidI ellipsoid, VectorI point) {
		boolean result = false;
		if(   (Math.pow((point.x() - ellipsoid.position.x()), 2)/(ellipsoid.scale.x()*ellipsoid.scale.x())) 
			+ (Math.pow((point.y() - ellipsoid.position.y()), 2)/(ellipsoid.scale.y()*ellipsoid.scale.y())) 
			+ (Math.pow((point.z() - ellipsoid.position.z()), 2)/(ellipsoid.scale.z()*ellipsoid.scale.z())) <= 1)  result = true;
		return result;
	}
	

	
	
}
