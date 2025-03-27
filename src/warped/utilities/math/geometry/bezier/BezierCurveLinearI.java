/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.bezier;

import warped.utilities.math.vectors.VectorI;

public class BezierCurveLinearI {

	protected VectorI p0, p1, pf;
	protected double t = 0.0;
	protected double increment = 0.01;
	protected boolean complete = false;
	
	/**You should not be using this constructor to create an instance of a linear curve
	 * This exist only for use by subclasses of BezierCurveLinear*/
	protected BezierCurveLinearI() {};
	
	public BezierCurveLinearI(VectorI p0, VectorI p1) {
		this.p0 = p0;
		this.p1 = p1;
		pf = new VectorI();
	}
	
	public BezierCurveLinearI(VectorI p0, VectorI p1, VectorI pf) {
		this.p0 = p0;
		this.p1 = p1;
		this.pf = pf;
	}
	
	
	public BezierCurveLinearI(VectorI pf, VectorI p1, double increment) {
		this.pf = pf;
		p0 = new VectorI(pf);
		this.p1 = p1;
		this.increment = increment;
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
		
		pf.set((int)(p0.x() + (t * (p1.x() - p0.x()))), (int)(p0.y() + (t * (p1.y() - p0.y()))));
			
		if(complete) return true;
		else return false;
	}
	
}
