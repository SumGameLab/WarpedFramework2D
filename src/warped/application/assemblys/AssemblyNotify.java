/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.application.gui.notification.GUINotification;
import warped.application.state.WarpedAssembly;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.window.WarpedWindow;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.geometry.bezier.BezierCurveObject;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;

public class AssemblyNotify extends WarpedAssembly {
	
	private static final int MAX_SIMULTANIOUS_NOTIFICATIONS = 25;
	
	private ArrayList<BezierCurveObject> curveIn 	 = new ArrayList<>();
	private ArrayList<BezierCurveObject> curveOut 	 = new ArrayList<>();
	private ArrayList<GUINotification> notifications = new ArrayList<>();
	
	private boolean hasAlive = false;
	
	private DirectionType popDirection = DirectionType.RIGHT;
	
	public AssemblyNotify(WarpedManagerType type) {
		super(type);

	}

	private final boolean notificationExists(GUINotification notification) {
		for(int i = 0; i < notifications.size(); i++){
			GUINotification test = notifications.get(i);
			if(test.isExpired()) continue;
			if(notification.getText() == test.getText() && notification.getStyle() == test.getStyle()) {
				Console.ln("Notify -> notificationExists() notification already exists");
				return true;
			}
		}		
		return false;
	}
	
	
	public final synchronized void addNotification(String text){addNotification(new GUINotification(text));}
	public final synchronized void addNotification(String text, BufferedImage img){addNotification(new GUINotification(text, img));}
	
	public final synchronized void addNotification(GUINotification notification) {
		if(notifications.size() > MAX_SIMULTANIOUS_NOTIFICATIONS) {
			Console.err("Notify -> addNotification() -> reached maximum simultanious notifications");
			return;
		}
		if(notificationExists(notification)) return;
		if(!isOpen()) open();
		Vec2i p1 = new Vec2i();
		Vec2i p2 = new Vec2i();
		int index = notifications.size();
		double offset = index * notification.getSize().y;
		if(popDirection == DirectionType.RIGHT) {
			p1.set(-notification.getSize().x, offset );
			p2.set(0, offset);
		} else {
			p1.set(WarpedWindow.width, offset);
			p2.set(WarpedWindow.width - notification.getSize().x, offset);
		}
		notification.setPosition(p1);
		addMember(notification);
		curveOut.add(new BezierCurveObject(p1, p2, notification.getPosition()));
		curveIn.add(new BezierCurveObject(p2, p1, notification.getPosition()));
		notifications.add(notification);	
	}
	
	public final void setPopDirection(DirectionType popDirection) {
		if(popDirection != DirectionType.LEFT && popDirection != DirectionType.RIGHT) {
			Console.err("Notify -> setPopDirection() -> only supports pop left or pop right");
			return;
		} else this.popDirection = popDirection;
	}
	
	@Override
	protected void offsetAssembly() {return;}

	@Override
	protected void defineAssembly() {return;}

	@Override
	protected void addAssembly() {return;}

	@Override
	protected void updateAssembly() {
		hasAlive = false;
		for(int i = 0; i < notifications.size(); i++) {
			BezierCurveObject cIn  = curveIn.get(i);
			BezierCurveObject cOut = curveOut.get(i);
			GUINotification note   = notifications.get(i); 
			if(note.isAlive()) hasAlive = true;
			if(!note.isStarted() && cOut.isComplete()) note.start();
			if(note.isExpired()  && cIn.isComplete()) note.kill();
		};
		if(!hasAlive) {
			curveIn.clear();
			curveOut.clear();
			notifications.clear();
			close();
		}
	}
	


	

}
