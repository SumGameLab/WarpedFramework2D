/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.circle;

import java.util.ArrayList;

import warped.utilities.math.vectors.Vec2d;

public class CircleD {

	public Vec2d pos;
	public double radius;
	
	private double x1, x2, y1, y2;
	
	public CircleD(Vec2d pos, double radius) {
		this.pos = pos;
		this.radius = radius;
		
		x1 = pos.x - radius;
		x2 = pos.x + radius;
		y1 = pos.y - radius;
		y2 = pos.y + radius;	
	}
	
	public boolean contains(Vec2d point) {
		boolean result = false;
		if(pos.difference(point) < radius)result = true;
		return result;
	}
	
	public static boolean contains(CircleD circle, Vec2d point) {
		boolean result = false;
		if(Vec2d.difference(circle.pos, point) <= circle.radius) result = true;
		return result;
	}

	public ArrayList<Vec2d> getContainedTiles() {
		ArrayList<Vec2d> result = new ArrayList<Vec2d>();
		for(int y = (int)y1; y <= (int)y2; y++) {
			for(int x = (int)x1; x < (int)x2; x++) {
				if(contains(new Vec2d(x,y))) result.add(new Vec2d(x,y));
			}
		}
		return result;
	}

	
	
}
