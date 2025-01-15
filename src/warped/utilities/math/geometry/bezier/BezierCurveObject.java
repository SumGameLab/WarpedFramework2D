/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.bezier;

import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;

public class BezierCurveObject {
	
	protected Vec2d pf;
	protected Vec2i p0, p1;
	protected double t = 0.0;
	protected double increment = 0.01;
	protected boolean complete = false;
	
	
	public BezierCurveObject(Vec2i p0, Vec2i p1, Vec2d pf) {
		this.p0 = p0;
		this.p1 = p1;
		this.pf = pf;
	}
	
	
	public void reset() {
		complete = false;
		t = 0.0;
		increment = 0.01;
	}
	
	public boolean isComplete() {
		if(complete) return true;
		
		if(t < 0.4) increment += 0.0025;
		if(t > 0.6) increment -= 0.0025;
		
		t += increment;
		if(t >= 1.0) {
			t = 1.0;
			complete = true;
		}
		
		pf.x = p0.x + (t * (p1.x - p0.x));
		pf.y = p0.y + (t * (p1.y - p0.y));
		
		if(complete) return true;
		else return false;
	}

}
