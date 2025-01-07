/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.circle;

import java.util.ArrayList;

import warped.utilities.math.vectors.Vec2i;

public class CircleI {

	public Vec2i pos;
	public int radius;
	
	private int x1, x2, y1, y2;
	
	public CircleI(Vec2i pos, int radius) {
		this.pos = pos;
		this.radius = radius;
		
		x1 = pos.x - radius;
		x2 = pos.x + radius;
		y1 = pos.y - radius;
		y2 = pos.y + radius;	
	}
	
	public boolean contains(Vec2i point) {
		boolean result = false;
		if(pos.difference(point) < radius)result = true;
		return result;
	}
	
	public static boolean contains(CircleI circle, Vec2i point) {
		boolean result = false;
		if(Vec2i.difference(circle.pos, point) <= circle.radius) result = true;
		return result;
	}

	public ArrayList<Vec2i> getContainedTiles() {
		ArrayList<Vec2i> result = new ArrayList<Vec2i>();
		for(int y = (int)y1; y <= (int)y2; y++) {
			for(int x = (int)x1; x < (int)x2; x++) {
				if(contains(new Vec2i(x,y))) result.add(new Vec2i(x,y));
			}
		}
		return result;
	}

	
	
}
