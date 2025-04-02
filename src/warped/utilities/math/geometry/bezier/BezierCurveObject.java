/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.math.geometry.bezier;

import java.util.Timer;
import java.util.TimerTask;

import warped.functionalInterfaces.WarpedAction;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class BezierCurveObject {
	
	protected VectorD pf;
	protected VectorD p0, p1;
	
	protected double t = 0.0;
	protected double increment = 0.01;
	private int frequency = 40;
	
	private static Timer bezierTimer = new Timer("Timer Thread : Bezier Timer");
	private TimerTask updateTask;
	
	private WarpedAction complete = () -> {return;};
	
	/**A dynamic path between the specified vectors.
	 * @param p0 - the starting point of the path.
	 * @param p1 - the finish point of the path.
	 * @param pf - the vector that will be moved along the path.
	 * @apiNote If you use p0 and p1 that are dynamic then the result path that pf takes will also be dynamic (i.e. it could be curved).
	 * @author 5som3*/
	public BezierCurveObject(VectorD p0, VectorD p1, VectorD pf) {
		this.p0 = p0;
		this.p1 = p1;
		this.pf = pf;
	}
	
	/**A linear path between the specified points.
	 * @param startX - the start point x coordinate.
	 * @param startY - the start point y coordinate.
	 * @param finishX - the end point x coordinate.
	 * @param finishY - the end point y coordinate.
	 * @param pf - the vector that will be moved along the path. 
	 * @author 5som3*/
	public BezierCurveObject(int startX, int startY, int finishX, int finishY, VectorD pf) {
		p0 = new VectorD(startX, startY);
		p1 = new VectorD(finishX, finishY);
		this.pf = pf;
	}
	
	/**A linear path between the specified points.
	 * @param startX - the start point x coordinate.
	 * @param startY - the start point y coordinate.
	 * @param finishX - the end point x coordinate.
	 * @param finishY - the end point y coordinate.
	 * @param pf - the vector that will be moved along the path.
	 * @param complete - the action to execute when the path completes. 
	 * @author 5som3*/
	public BezierCurveObject(int startX, int startY, int finishX, int finishY, VectorD pf, WarpedAction complete) {
		p0 = new VectorD(startX, startY);
		p1 = new VectorD(finishX, finishY);
		this.complete = complete;
		this.pf = pf;
	}
	
	/**Set the frequency of the update task
	 * @param frequency - number of updates per second. Must be at least 1.
	 * @author 5som3*/
	public void setFrequency(int frequency) {
		if(frequency < 1) {
			Console.err("BezierCurveObject -> setFrequency() -> frequency must be positive");
			frequency = 40;
		}
		this.frequency = frequency;
		if(updateTask != null) {			
			stop();
			start();
		}
	}
	
	/**The amount of path to progress in each update.
	 * @param increment - the progress of each update as a percentage. (1.0 = 100%, 0.0 = 0%)
	 * @implNote the progress will increase by <increment> at <frequency> times per second.
	 * @author 5som3*/
	public void setIncrement(double increment) {
		if(increment <= 0.0 || increment >= 1.0) {
			Console.err("BezierCurveObject -> setIncrement() -> increment is out of bounds : " + increment);
			if(increment <= 0.0) increment = 0.01;
			else increment = 0.1;
		}
		this.increment = increment;
	}
	
	/**Start moving along the specified path.
	 * @implNote will schedule the update() at the specified frequency.
	 * @author 5som3*/
	public void start() {
		updateTask = new TimerTask() {public void run() {update();}};
		bezierTimer.scheduleAtFixedRate(updateTask, 0, UtilsMath.convertHzToMillis(frequency));
	}
	
	/**Stop moving along the path.
	 * @author 5som3*/
	public void stop() {
		updateTask.cancel();
		updateTask = null;
	}
	
	/**Reset the path
	 * @author 5som3*/
	public void reset() {
		t = 0.0;
		increment = 0.01;
	}
	
	/**Set an action to occur when the path is complete.
	 * @param complete - the action to execute when the path is finished.
	 * @implNote will execute once each time the path finishes i.e. if it's reset it could trigger again.	 * 
	 * @author 5som3*/
	public void setCompleteAction(WarpedAction complete) {this.complete = complete;}
	
	/**Remove any completeAction that has been set to this curve.
	 * @author 5som3*/
	public void clearCompleteAction() {this.complete = () -> {return;};}
	
	/**update the path*/
	private void update() {		
		if(t < 0.4) increment += 0.0025;
		if(t > 0.6) increment -= 0.0025;
		
		t += increment;
		if(t >= 1.0) {
			t = 1.0;
			stop();
			complete.action();
		}
		
		pf.set(p0.x() + (t * (p1.x() - p0.x())), p0.y() + (t * (p1.y() - p0.y())));
	}

}
