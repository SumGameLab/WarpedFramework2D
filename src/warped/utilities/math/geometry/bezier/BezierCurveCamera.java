/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.bezier;

import warped.graphics.window.WarpedCamera;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class BezierCurveCamera extends BezierCurveLinearD {

	private boolean zoom = false;
	private double zoomStartT = 0.3;
	private double zoomEndT = 0.95;
	private double zoomCap = 0.33;
	private int zoomMultiplyer = 1;
	
	private VectorD p1;
	
	private double acceleration;
	
	public BezierCurveCamera(VectorD pf, VectorD p1, double increment) { 
		this.pf = pf;
		this.p0 = new VectorD(pf);
		this.p0.scale(-1);
		this.p1 = p1;
		this.increment = increment;
		acceleration = increment * 0.10;
	}
	
	public BezierCurveCamera(VectorD pf, VectorD p1, double increment, boolean zoom) { 
		this.pf = pf;
		this.p0 = new VectorD(pf);
		this.p0.scale(-1);
		this.p1 = p1;
		this.increment = increment;
		this.zoom = zoom;
		acceleration = increment * 0.10;
	}
	
	public BezierCurveCamera(VectorD pf, VectorD p1, double increment, double zoomStartT, double  zoomEndT, double zoomCap) { 
		this.pf = pf;
		this.p0 = new VectorD(pf);
		this.p0.scale(-1);
		this.p1 = p1;
		this.increment = increment;
		acceleration = increment * 0.10;

		this.zoom = true;
		this.zoomStartT = zoomStartT;
		this.zoomEndT = zoomEndT;
		this.zoomCap = zoomCap;
		
		if(zoomStartT > zoomEndT || zoomStartT < 0.0 || zoomStartT >= 1.0) {
			Console.err("BezierCurveCamera -> Constructor() -> zoomStartT is invalid -> it will be set to 0.0");
			zoomStartT = 0.0;
		}
		if(zoomEndT < zoomStartT || zoomEndT < 0.0 || zoomEndT > 1.0) {
			Console.err("BezierCurveCamera -> Constructor() -> zoomEndT is invalid -> it will be set to 1.0");
			zoomEndT = 1.0;
		}
		if(zoomCap < 0.0 || zoomCap > 1.0) {
			Console.err("BezierCurveCamera -> Constructor() -> zoomCap is invalid -> it will be set to 1.0");
			zoomCap = 1.0;
		}
	}
	
	public BezierCurveCamera(VectorD pf, VectorD p1, double increment, double zoomStartT, double  zoomEndT, int zoomMultiplyer) { 
		this.pf = pf;
		this.p0 = new VectorD(pf);
		this.p0.inverse();
		this.p1 = p1;
		this.increment = increment;
		acceleration = increment * 0.10;

		this.zoom = true;
		this.zoomStartT = zoomStartT;
		this.zoomEndT = zoomEndT;
		zoomCap = 1.0;
		this.zoomMultiplyer = zoomMultiplyer;
		
		if(zoomStartT > zoomEndT || zoomStartT < 0.0 || zoomStartT >= 1.0) {
			Console.err("BezierCurveCamera -> Constructor() -> zoomStartT is invalid -> it will be set to 0.0");
			zoomStartT = 0.0;
		}
		if(zoomEndT < zoomStartT || zoomEndT < 0.0 || zoomEndT > 1.0) {
			Console.err("BezierCurveCamera -> Constructor() -> zoomEndT is invalid -> it will be set to 1.0");
			zoomEndT = 1.0;
		}
		if(zoomMultiplyer < 0 || zoomMultiplyer > 10) {
			Console.err("BezierCurveCamera -> Constructor() -> zoomMultiplyer is invalid -> it will be set to 1");
			zoomMultiplyer = 1;
		}
	}
	
	
	/*
	public boolean isComplete(WarpedCamera camera) {
		if(complete) {
			pf = null;
			return true;
		}
		
		if(t < 0.35) increment += acceleration;
		if(t > 0.65) increment -= acceleration;

		if(zoom && t > zoomStartT && t < zoomEndT && camera.getZoom() < zoomCap) {
			for(int i = 0; i < zoomMultiplyer; i++) {				
				camera.zoomIn();
			}
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
	*/
}
