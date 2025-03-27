/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.ellipse;

import warped.utilities.math.vectors.VectorI;


public class EllipseI {

	public VectorI pos = new VectorI();
	public VectorI scale = new VectorI();
	
	protected int x1, x2, y1, y2;
	
	public EllipseI() {};
	
	public EllipseI(VectorI pos, VectorI scale) {
		this.pos = pos;
		this.scale = scale;
		x1 = pos.x() - scale.x();
		x2 = pos.x() + scale.x();
		y1 = pos.y() - scale.y();
		y2 = pos.y() + scale.y();
	}
	
	public boolean contains(int x, int y) {
		boolean result = false;
		if(   (Math.pow((x - pos.x()), 2)/(scale.x()*scale.x())) 
			+ (Math.pow((y - pos.y()), 2)/(scale.y()*scale.y())) <= 1)  result = true;
		return result;
	}
	public boolean contains(VectorI point) {
		boolean result = false;
		if(   (Math.pow((point.x() - pos.x()), 2)/(scale.x()*scale.x())) 
			+ (Math.pow((point.y() - pos.y()), 2)/(scale.y()*scale.y())) <= 1)  result = true;
		return result;
	}
	public static boolean contains(EllipseD ellipse, VectorI point) {
		boolean result = false;
		if(   (Math.pow((point.x() - ellipse.position.x()), 2)/(ellipse.scale.x()*ellipse.scale.x())) 
			+ (Math.pow((point.y() - ellipse.position.y()), 2)/(ellipse.scale.y()*ellipse.scale.y())) <= 1)  result = true;
		return result;
	}
	
}
