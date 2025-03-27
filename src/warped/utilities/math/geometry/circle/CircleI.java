/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.circle;

import java.util.ArrayList;

import warped.utilities.math.vectors.VectorI;

public class CircleI {

	public VectorI pos;
	public int radius;
	
	private int x1, x2, y1, y2;
	
	public CircleI(VectorI pos, int radius) {
		this.pos = pos;
		this.radius = radius;
		
		x1 = pos.x() - radius;
		x2 = pos.x() + radius;
		y1 = pos.y() - radius;
		y2 = pos.y() + radius;	
	}
	
	public boolean contains(VectorI point) {
		boolean result = false;
		if(pos.getDistanceBetweenVectors(point) < radius)result = true;
		return result;
	}
	
	public static boolean contains(CircleI circle, VectorI point) {
		boolean result = false;
		if(circle.pos.getDistanceBetweenVectors(point) <= circle.radius) result = true;
		return result;
	}

	public ArrayList<VectorI> getContainedTiles() {
		ArrayList<VectorI> result = new ArrayList<VectorI>();
		for(int y = (int)y1; y <= (int)y2; y++) {
			for(int x = (int)x1; x < (int)x2; x++) {
				if(contains(new VectorI(x,y))) result.add(new VectorI(x,y));
			}
		}
		return result;
	}

	
	
}
