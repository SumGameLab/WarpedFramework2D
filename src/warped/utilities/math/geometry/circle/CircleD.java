/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.circle;

import java.util.ArrayList;

import warped.utilities.math.vectors.VectorD;

public class CircleD {

	public VectorD pos;
	public double radius;
	
	private double x1, x2, y1, y2;
	
	public CircleD(VectorD pos, double radius) {
		this.pos = pos;
		this.radius = radius;
		
		x1 = pos.x() - radius;
		x2 = pos.x() + radius;
		y1 = pos.y() - radius;
		y2 = pos.y() + radius;	
	}
	
	public boolean contains(VectorD point) {
		boolean result = false;
		if(pos.getDistanceBetweenVectors(point) < radius)result = true;
		return result;
	}
	
	public static boolean contains(CircleD circle, VectorD point) {
		boolean result = false;
		if(circle.pos.getDistanceBetweenVectors(point) <= circle.radius) result = true;
		return result;
	}

	public ArrayList<VectorD> getContainedTiles() {
		ArrayList<VectorD> result = new ArrayList<VectorD>();
		for(int y = (int)y1; y <= (int)y2; y++) {
			for(int x = (int)x1; x < (int)x2; x++) {
				if(contains(new VectorD(x,y))) result.add(new VectorD(x,y));
			}
		}
		return result;
	}

	
	
}
