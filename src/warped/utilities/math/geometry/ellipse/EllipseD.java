/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.ellipse;

import warped.utilities.math.vectors.VectorD;

public class EllipseD {

	public VectorD position = new VectorD();
	public VectorD scale = new VectorD();
	
	public VectorD focus1, focus2;
	
	public EllipseD() {};
	
	public EllipseD(VectorD pos, VectorD scale) {
		this.position = pos;
		this.scale = scale;
		if(scale.x() > scale.y()) {
			double c = Math.sqrt((scale.x() * scale.x()) - (scale.y() * scale.y()));
			focus1 = new VectorD( c, 0);
			focus2 = new VectorD(-c, 0);
		}
	}
	
	public VectorD getScale() {return scale;}
	public VectorD getPosition() {return position;}
	public VectorD getPointOnEllipse(Double theta) {return new VectorD(position.x() + (scale.x() * Math.cos(theta)), position.y() + (scale.y() * Math.sin(theta)));}
	
	public boolean contains(double x, double y) {
		boolean result = false;
		if(   (Math.pow((x - position.x()), 2)/(scale.x() * scale.x())) 
			+ (Math.pow((y - position.y()), 2)/(scale.y() * scale.y())) <= 1)  result = true;
		return result;
	}
	public boolean contains(VectorD point) {
		boolean result = false;
		if(   (Math.pow((point.x() - position.x()), 2)/(scale.x() * scale.x())) 
			+ (Math.pow((point.y() - position.y()), 2)/(scale.y() * scale.y())) <= 1)  result = true;
		return result;
	}
	public static boolean contains(EllipseD ellipse, VectorD point) {
		boolean result = false;
		if(   (Math.pow((point.x() - ellipse.position.x()), 2)/(ellipse.scale.x() * ellipse.scale.x())) 
			+ (Math.pow((point.y() - ellipse.position.y()), 2)/(ellipse.scale.y() * ellipse.scale.y())) <= 1)  result = true;
		return result;
	}
	
}
