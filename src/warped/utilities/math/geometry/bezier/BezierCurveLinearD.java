/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.bezier;

import warped.utilities.math.vectors.VectorD;

public class BezierCurveLinearD {
	
	protected VectorD p0, p1, pf;
	protected double t = 0.0;
	protected double increment = 0.01;
	protected boolean complete = false;
	
	/**You should not be using this constructor to create an instance of a linear curve
	 * This exist only for use by subclasses of BezierCurveLinear*/
	public BezierCurveLinearD() {};
	
	public double getTime() {return t;}

	public boolean isValid() {if(p0 != null && p1 != null) return true; else return false;}
	public void setIncrement(double increment) {this.increment = increment;}
	public void setCurve(VectorD p0, VectorD p1) {
		t = 0.0;
		complete = false;
		this.p0 = p0;
		this.p1 = p1;
		pf = new VectorD();
	}
	
	public BezierCurveLinearD(VectorD p0, VectorD p1) {
		this.p0 = p0;
		this.p1 = p1;
		pf = new VectorD();
	}
	
	public BezierCurveLinearD(VectorD p0, VectorD p1, VectorD pf) {
		this.p0 = p0;
		this.p1 = p1;
		this.pf = pf;
	}
	
	
	public BezierCurveLinearD(VectorD pf, VectorD p1, double increment) {
		this.pf = pf;
		p0 = new VectorD(pf);
		this.p1 = p1;
		this.increment = increment;
	}
	
	public void reset() {
		complete = false;
		t = 0.0;
	}
	
	/**Check if the path is complete.
	 * @apiNote If not complete will update the path by one increment. 
	 * @author 5som3*/
	public boolean isComplete(VectorD vec) {
		if(complete) {
			pf = null;
			return true;
		}
		
		t += increment;
		if(t >= 1.0) {
			t = 1.0;
			complete = true;
		}
		
		vec.set(p0.x() + (t * (p1.x() - p0.x())), p0.y() + (t * (p1.y() - p0.y())));
		
		if(complete) return true;
		else return false;
	}

	/**Check if the path is complete.
	 * @apiNote If not complete will update the path by one increment. 
	 * @author 5som3*/
	public boolean isComplete() {
		if(complete) {
			pf = null;
			return true;
		}
		
		t += increment;
		if(t >= 1.0) {
			t = 1.0;
			complete = true;
		}
		
		pf.set(-(p0.x() + (t * (p1.x() - p0.x()))), -(p0.y() + (t * (p1.y() - p0.y()))));
		
		if(complete) return true;
		else return false;
	}

	
	
	
}
