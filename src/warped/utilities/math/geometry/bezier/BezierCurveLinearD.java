/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.utilities.math.geometry.bezier;

import java.util.Timer;
import java.util.TimerTask;

import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class BezierCurveLinearD {
	
	private static Timer consoleTimer = new Timer("Timer Thread : BezierCurveLinearD");
	private TimerTask updateTask;
	private long lastTime = 0;
	
	protected VectorD p0, p1, pf;
	protected double progress = 0.0;
	protected double increment = 0.01;
	protected boolean isComplete = false;
	protected double xOffset = 0;
	protected double yOffset = 0;
	
	/**You should not be using this constructor to create an instance of a linear curve
	 * This exist only for use by subclasses of BezierCurveLinear*/
	public BezierCurveLinearD() {};
	
	
	/**A curve with the specified parameters.
	 * @param p0 - the starting point of the curve
	 * @param p1 - the ending point of the curve
	 * @param pf - the vector to move along the curve
	 * @apiNote the components of pf will be updated after start() has been called. 
	 * @author 5som3*/
	public BezierCurveLinearD(VectorD p0, VectorD p1, VectorD pf) {
		this.p0 = p0;
		this.p1 = p1;
		this.pf = pf;
	}
	
	/**A curve with the specified parameters.
	 * @param p0 - the starting point of the curve
	 * @param p1 - the ending point of the curve
	 * @param pf - the vector to move along the curve
	 * @param increment - the amount to progress along the curve per second. i.e. 0.25 will progress 25% of the distance per second. 
	 * @apiNote the components of pf will be updated after start() has been called. 
	 * @author 5som3*/
	public BezierCurveLinearD(VectorD p0, VectorD p1, VectorD pf, double increment) {
		this.pf = pf;
		this.p0 = p0;
		this.p1 = p1;
		this.increment = increment;
	}
	
	/**A curve with the specified parameters.
	 * @param p0 - the starting point of the curve
	 * @param p1 - the ending point of the curve
	 * @param pf - the vector to move along the curve
	 * @param increment - the amount to progress along the curve per second. i.e. 0.25 will progress 25% of the distance per second.
	 * @param xOffset - the offset x component to apply to pf as it progresses.
	 * @param yOffset - the offset y component to apply to pf as it progresses. 
	 * @apiNote the components of pf will be updated after start() has been called. 
	 * @author 5som3*/
	public BezierCurveLinearD(VectorD p0, VectorD p1, VectorD pf, double increment, double xOffset, double yOffset) {
		this.pf = pf;
		this.p0 = p0;
		this.p1 = p1;
		this.increment = increment;
	}
	
	/**Get the current progress of the curve.
	 * @return double - the progress between 0.0 (no progress) to 1.0 (complete).
	 * @author 5som3 */
	public double getProgress() {return progress;}

	/**Set the amount to progress along the curve per second.
	 * @param increment - the amount to progress as percentage. 
	 * @author 5som3*/
	public void setIncrement(double increment) {this.increment = increment;}
	
	/**Set the parameters of the curve and resets the curve.
	 * @param p0 - the starting point of the curve.
	 * @param p1 - the ending point of the curve. 
	 * @author 5som3*/
	public void setCurve(VectorD p0, VectorD p1) {
		progress = 0.0;
		isComplete = false;
		this.p0 = p0;
		this.p1 = p1;
	}
	
	/**Set the parameters of the curve and resets the curve.
	 * @param p0 - the starting point of the curve.
	 * @param p1 - the ending point of the curve.
	 * @param pf - the vector to move along the curve 
	 * @author 5som3*/
	public void setCurve(VectorD p0, VectorD p1, VectorD pf) {
		progress = 0.0;
		isComplete = false;
		this.p0 = p0;
		this.p1 = p1;
		this.pf = pf;
	}
	
	/**Prepare the curve to be started again.
	 * @apiNote if the curve has been started it must be reset before it can start again.
	 * @author 5som3*/
	public void reset() {
		isComplete = false;
		progress = 0.0;
	}
	
	/**Start progressing the curve.
	 * @apiNote can only be started once or if it has been reset. 
	 * @author 5som3*/
	public void start() {
		if(updateTask != null) {
			Console.err("BezierCurveLinearD -> start() -> curve already started");
			return;
		}
		lastTime = System.nanoTime();
		updateTask = new TimerTask(){public void run() {update();}};
		consoleTimer.scheduleAtFixedRate(updateTask, 0, 4);
		
	}
	
	/**Stop progressing the curve.
	 * @author 5som3*/
	public void stop() {
		updateTask.cancel();
		updateTask = null;
	}
	
	/**progress the curve.
	 * @author 5som3*/
	private void update() {
		progress += (System.nanoTime() - lastTime) / 1000_000_000.0 * increment;
		if(progress >= 1.0) {
			stop(); 
			progress = 1.0;
			isComplete = true;
		}
		pf.set(p0.x() + xOffset + (progress * (p1.x() - p0.x())), p0.y() + yOffset + (progress * (p1.y() - p0.y())));
		lastTime = System.nanoTime();
	}
	
	/*
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
	*/

	/**Check if the path is complete.
	 * @apiNote If not complete will update the path by one increment. 
	 * @author 5som3*/
	public boolean isComplete() {
		return isComplete;
		/*
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
		*/
	}

	
	
	
}
