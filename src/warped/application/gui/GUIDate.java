/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.DayType;
import warped.utilities.enums.generalised.MonthType;
import warped.utilities.enums.generalised.StyleType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.timers.WarpedDateTimer;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUIDate extends WarpedGUI {

	private StyleType style = StyleType.ORNATE;
	private WarpedDateTimer dateTimer = new WarpedDateTimer();
	private int tick = 0;
	private int delay = 60;
	private BufferedImage date;
	
	private Font font = UtilsFont.getPreferred(18);
	
	private int borderThickness = 3;
	private Vec2i textOffset = new Vec2i(14, 25);
	
	private Color borderColor = Color.BLACK;
	private Color backgroundColor = Colour.getColor(Colour.GREY_DARK);
	private Color textColor = Color.WHITE;
	
	private int width = 200;
	private int height = 40;
	
	private boolean isNumerical = true;
	
	public GUIDate() {
		date = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		updateGraphics();
	}
	
	public WarpedDateTimer getDateTimer() {return dateTimer;}
	
	
	private void updateGraphics() {
		Graphics g = date.getGraphics();
		g.setColor(borderColor);
		g.fillRect(0, 0, width, height);
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, width - borderThickness * 2, height - borderThickness * 2);
		g.setColor(textColor);
		g.setFont(font);
		String text = "";
		if(isNumerical)	{
			switch(style) {
			case ORNATE:
				text += dateTimer.getYear() + "" + (dateTimer.getDay().ordinal() + 1) + "." + dateTimer.getWeek() + "" + (dateTimer.getMonth().ordinal() + 1); 
				break;
			case PLAIN:
				text += (dateTimer.getDay().ordinal() + 1) + " : " + dateTimer.getWeek() + " : " + (dateTimer.getMonth().ordinal() + 1) + " : " + dateTimer.getYear();
				break;
			default:
				Console.err("GUIDate -> updateGraphics() -> isNumerical = true -> invalid case : " + style);
				break;
			}
		}
		else {
			switch(style) {
			case PLAIN:
				text += DayType.getString(dateTimer.getDay()) + " : Week " + dateTimer.getWeek() + " : " + MonthType.getString(dateTimer.getMonth()) + " : " + dateTimer.getYear();
				break;
			default:
				Console.err("GUIDate -> updateGraphics() -> isNumerical = false -> invalid case : " + style);
				break;
			}
		}
		if(text.endsWith("0")) text = text.substring(0, text.length() - 1) + "1";
		g.drawString(text, textOffset.x, textOffset.y);
		g.dispose();
		setRaster(date);
	}
	
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
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {
		tick++;
		if(tick > delay) {
			tick = 0;
			dateTimer.increment();
			updateGraphics();
		}
	}

	@Override
	protected void updatePosition() {return;}

}
