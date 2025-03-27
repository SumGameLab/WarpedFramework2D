/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.user.mouse;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import warped.graphics.window.WarpedViewport;
import warped.graphics.window.WarpedWindow;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class WarpedMouseEvent {

	private MouseEvent e;
	private MouseEventType ID;
	private Point canvasP;   //mouse event location relative to the canvas
	private Point viewPortP; //mouse event location relative to the view port
	private Point viewPortLayerP; //mouse event location relative to the view port layer
	private Point objectP; //mouse event location relative to the object 
	private Point traceP; // mouse event trace, updated at each stage of mouse handleing -> screen -> view port -> view port layer -> object
	
	//private boolean isChecked = false;
	
	protected WarpedMouseHandler handler;
	
	public WarpedMouseEvent(MouseEvent e, MouseEventType ID) {
		handler = new WarpedMouseHandler();
		this.e = e;
		this.ID = ID;
		updateTrace(e);
	}
	
	private WarpedMouseEvent(WarpedMouseEvent e) {
		this.e = e.e;
		this.ID = e.ID;
		this.handler = e.handler;
		
		updateTrace(e.e);
				
		if(e.viewPortP == null) viewPortP = new Point();
		else viewPortP = new Point(e.viewPortP);
		
		if(e.viewPortLayerP == null) viewPortLayerP = new Point();
		else viewPortLayerP = new Point(e.viewPortLayerP);
		
		if(e.objectP == null) objectP = new Point();
		else objectP = new Point(e.objectP);
		
	}
	
	public MouseEvent getMouseEvent() {return e;}
	public MouseWheelEvent getWheelEvent() {
		if(ID == MouseEventType.WHEEL_ROTATION)	return (MouseWheelEvent)e;
		else {
			Console.err("WarpedMouseEvent -> getWheelEvent() -> mouse event is not a wheel event");
			return null;
		}
	}
	public MouseEventType getMouseEventType() {return ID;}
	
	public Point getPointRelativeToCanvas() {return canvasP;}
	public Point getPointRelativeToViewPort() {return viewPortP;}
	public Point getPointRelativeToObject() {return objectP;}
	
	public Point getPointTrace() {return traceP;}
	//public void checked() {isChecked = true;}
	//public boolean isChecked() {return isChecked;}
	
	public boolean isHandled() {return handler.isHandled();}
	public void handle() {handler.handle();}
	
	public void updateTrace(MouseEvent e) {
		traceP  = new Point(e.getPoint().x, e.getPoint().y);
		traceP.x /= WarpedWindow.getWindowScale().x();
		traceP.y /= WarpedWindow.getWindowScale().y();
		canvasP = (Point)traceP.clone();
	}
	
	public void updateTrace(WarpedViewport viewPort) {
		traceP.setLocation(traceP.x - viewPort.getX(), traceP.y - viewPort.getY());
		viewPortP = (Point)traceP.clone();		
	}
	
	
	public void updateTrace(VectorD object) {
		traceP.setLocation(traceP.x - object.x(), traceP.y - object.y());
		objectP = (Point)traceP.clone();
	}
	


	public static WarpedMouseEvent generateClone(WarpedMouseEvent e) {return new WarpedMouseEvent(e);}
	
	
	
}
