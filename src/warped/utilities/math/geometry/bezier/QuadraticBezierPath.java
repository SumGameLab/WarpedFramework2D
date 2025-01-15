/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.bezier;

import java.util.ArrayList;

public class QuadraticBezierPath {

	//private int curveIndex = 0;
	private ArrayList<BezierCurveQuadratic> pathCurves = new ArrayList<>();
	
	
	public void addCurve(BezierCurveQuadratic curve) {pathCurves.add(curve);}
	
	
}
