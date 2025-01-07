/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.notification;

import java.awt.Color;
import java.awt.Font;

import warped.application.gui.WarpedGUI;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;

public class GUINotificationBox extends WarpedGUI {

	private String[] notifications = new String[100];
	
	private int boxWidth = 250;
	private int boxHeight = 60;
	
	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	
	private Color borderColor = Color.BLACK;
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	
	private Color scrollBarColor = Colour.GREY_LIGHT.getColor();
	private Color scrollButtonColor = Colour.GREY_DARK_DARK.getColor();
	
	private Color normalColor = Color.WHITE;
	private Color urgentColor = Colour.YELLOW_LIGHT.getColor();
	private Color critialColor = Color.YELLOW;
	
	public GUINotificationBox() {
		
	}
	
	//--------
	//---------------- Interaction --------
	//--------
	@Override
	protected void mouseEntered() {
				
	}

	@Override
	protected void mouseExited() {
				
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
			
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
				
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
				
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
				
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
				
	}

	//--------
	//---------------- Update --------
	//--------
	public void updateGraphics() {
		
	}
	@Override
	protected void updateRaster() {
				
	}

	@Override
	protected void updateObject() {
		
	}

	@Override
	protected void updatePosition() {
		
	}

}
