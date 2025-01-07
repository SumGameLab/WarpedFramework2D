/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.notification;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.StyleType;
import warped.utilities.utils.Console;

public class GUINotification extends WarpedGUI {

	
	private BufferedImage icon;
	
	private int index = 0;
	private StyleType style = StyleType.DEFAULT;
	private boolean isExpired = false;
	private boolean isStarted = false;
	
	private String text;
	private int tick = 0;
	private int duration = 360;
	
	private BufferedImage notification;
	
	public GUINotification(String text) {
		this.text = text;
		style = StyleType.DEFAULT;
		updateGraphics();
	}
	
	public GUINotification(String text, BufferedImage icon) {
		this.text = text;
		this.icon = icon;
		style = StyleType.DEFAULT;
		updateGraphics();
	}
	
	public GUINotification(String text, int index) {
		this.text = text;
		this.index = index;
		style = StyleType.DEFAULT;
		updateGraphics();
	}
	
	public GUINotification(String text, int index, int duration) {
		this.text = text;
		this.duration = duration;
		style = StyleType.DEFAULT;
		updateGraphics();
	}
	
	public void setIndex(int index) {this.index = index;}
	public int getIndex() {return this.index;}
	public void start() {isStarted = true;}
	public boolean isStarted() {return isStarted;}
	public String getText() {return text;}
	public StyleType getStyle() {return style;}
	
	private void updateGraphics() {
		Graphics g;
		switch(style) {
		case DEFAULT:
			int defWidth = 400; int defHeight = 50;
			int border = 4;
			Font font = new Font("default", Font.PLAIN, 16);
			notification = new BufferedImage(defWidth, defHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
			g = notification.getGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, defWidth, defHeight);
			g.setColor(Colour.getColor(Colour.GREY_DARK));
			g.fillRect(border, border, defWidth - border * 2, defHeight - border * 2);		
			int textX = 20;
			int textY = 20;
			if(icon != null) {
				g.drawImage(icon, 2, 2, defHeight - 4, defHeight - 4, null);
				textX += 50;
			}
			g.setColor(Color.YELLOW);
			g.setFont(font);
			g.drawString(text, textX, textY);
			g.dispose();
			break;
		default:
			Console.err("GUINotification -> udpateGraphics() -> unsupported style : " + style);
			break;
		}
		
		setRaster(notification);
	}
	
	public boolean isExpired() {return isExpired;}
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {tick = duration;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {
		if(isStarted) {			
			tick++;
			if(tick > duration) {
				isExpired = true;
			}
		}
		
	}

	@Override
	protected void updatePosition() {return;}
	
	

}
