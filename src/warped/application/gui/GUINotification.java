/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.functionalInterfaces.WarpedAction;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.geometry.bezier.BezierCurveObject;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class GUINotification extends WarpedGUI {

	/*GUINotification provides these functions. 
	 * 		
	 * 		- A customizable area to notification display text.
	 * 		- Works in combination with the Notify assembly. 
	 * 		- Keeps track of how long the notification has been active for.
	 * 		- Set border, background and text colour. 
	 * 		- Set the font and content of the notification text.
	 * 		- Typically not necessary to create individual notifications, instead call WarpedState.notify.addNotification("Your notification here");
	 * */
	
	private boolean isExpired = false;
	private boolean isStarted = false;
	
	private int tick	 = 0;
	private int duration = 360;
		
	private boolean isBackgroundVisible = true;
	
	private BufferedImage background;
	private BufferedImage icon;
	
	private int index;
	
	private String text				 = "Default";
	private Color borderColor 		 = Colour.BLACK.getColor();
	private Color backgroundColor    = Colour.GREY_DARK.getColor();
	private Color textColor			 = Colour.YELLOW.getColor();
	
	private int borderThickness 	 = 2;
	
	private VectorI textOffset 		 = new VectorI(12, 14);
	private VectorI notificationSize = new VectorI(200, 40);
	private VectorI iconSize		 = new VectorI(30, 30);
	private VectorI iconOffset 		 = new VectorI(notificationSize.x() - borderThickness * 2 - iconSize.x(), notificationSize.y() - iconSize.y() / 2);
	
	private Font font 				 = new Font("Notification", Font.PLAIN, 12); 
	
	private BezierCurveObject outCurve = new BezierCurveObject(-notificationSize.x(), 0, 0, 0, getPosition(), () -> {isStarted = true;});
	private BezierCurveObject inCurve  = new BezierCurveObject(0, 0, -notificationSize.x(), 0, getPosition(), () -> {kill();});
	
	/**A notification of the specified parameters.
	 * @param text - the text to display in the notification
	 * @apiNote The expire action will trigger once after reaching the set duration.
	 * @author 5som3*/
	public GUINotification(String text, int index) {
		setSize(notificationSize.x(), notificationSize.y());
		this.index = index;
		this.text = text;
	}
	
	/**A notification of the specified parameters.
	 * @param text - the text to display in the notification
	 * @apiNote The expire action will trigger once after reaching the set duration.
	 * @author 5som3*/
	public GUINotification(String text, int startX, int startY, int finishX, int finishY) {
		setSize(notificationSize.x(), notificationSize.y());
		this.text = text;
	}
	
	/**A notification of the specified parameters.
	 * @param text - the text to display in the notification
	 * @param font - the font for the specified text.
	 * @apiNote The expire action will trigger once after reaching the set duration.
	 * @author 5som3*/
	public GUINotification(String text, Font font) {
		setSize(notificationSize.x(), notificationSize.y());
		this.text = text;
		this.font = font;
	}
	
	/**A notification of the specified parameters.
	 * @param text - the text to display in the notification
	 * @param font - the font for the specified text.
	 * @param textXOffset - the x offset of the text in pixels.
	 * @param textYOffset - the y offset of the text in pixels. 
	 * @apiNote the offset for the text is measured from the top left corner of the notification to the bottom left corner of the text.
	 * @apiNote The expire action will trigger once after reaching the set duration.
	 * @author 5som3*/
	public GUINotification(String text, Font font, int textXOffset, int textYOffset) {
		setSize(notificationSize.x(), notificationSize.y());
		this.text = text;
		this.font = font;
		this.textOffset.set(textXOffset, textYOffset);;
	}
	
	/**A notification of the specified parameters.
	 * @param text - the text to display in the notification
	 * @param font - the font for the specified text.
	 * @param textXOffset - the x offset of the text in pixels.
	 * @param textYOffset - the y offset of the text in pixels. 
	 * @apiNote the offset for the text is measured from the top left corner of the notification to the bottom left corner of the text.
	 * @param duration - the number of ticks before the notification expires. 
	 * @apiNote The expire action will trigger once after reaching the set duration.
	 * @author 5som3*/
	public GUINotification(String text, Font font,  int textXOffset, int textYOffset, int duration) {
		setSize(notificationSize.x(), notificationSize.y());
		this.text = text;
		this.font = font;
		this.textOffset.set(textXOffset, textYOffset);;
		this.duration = duration;
	}
	
	/**A notification of the specified parameters.
	 * @param width - the total width of the notification.
	 * @param height -the total height of the notification.
	 * @param duration - the number of ticks before the notification expires. 
	 * @param borderColor - the color for the notification border (if border thickness > 0 && isBackgroundVisible).
	 * @param backgroundColor - the color to render behind the notification text. (if isBackgroundVisible).
	 * @param textColor - the color of the notifications text. 
	 * @param text - the text to display in the notification
	 * @param font - the font for the specified text.
	 * @param textXOffset - the x offset of the text in pixels.
	 * @param textYOffset - the y offset of the text in pixels.
	 * @param backgroundImage - the image to render in front of the background and behind the text. (use null for no background image).
	 * @param iconImage - the image to render as an icon in the notification. (use null for no icon).
	 * @param iconXOffset - the x offset for the icon in pixels.
	 * @param iconYOffset - the y offset for the icon in pixels.
	 * @param iconWidth - the width of the icon in pixels.
	 * @param icon height - the height of the icon in pixels.
	 * @param startX - start x coordinate
	 * @param startY - start y coordinate
	 * @param finishX - end x coordinate. (where the notification will be for the duration)
	 * @param finishY - end x coordinate. (where the notification will be for the duration)
	 * @apiNote the offset for the text is measured from the top left corner of the notification to the bottom left corner of the text.
	 * @apiNote the offset for the icon is measured from the top left corner of the notification to the top left corner of the icon.
	 * @apiNote The expire action will trigger once after reaching the set duration.
	 * @author 5som3*/
	public GUINotification(int width, int height, int duration, Color borderColor, Color backgroundColor,  Color textColor, String text, Font font,  int textXOffset, int textYOffset, BufferedImage backgroundImage, BufferedImage icon, int iconXOffset, int iconYOffset, int iconWidth, int iconHeight, int startX, int startY, int finishX, int finishY) {
		if(width <= 0 || height <= 0) {
			Console.err("GUINotification -> width or height is too small, must be positive");
			if(width <= 0) width = 200;
			else height = 40;
		}
		setSize(width, height);
		this.duration = duration;
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		this.text = text;
		this.font = font;
		this.textOffset.set(textXOffset, textYOffset);
		this.iconSize.set(iconWidth, iconHeight);
		updateGraphics();
		setKeyFrames(startX, startY, finishX, finishY);
	}

	/**Set the start and finish position for the notification.
	 * @param startX - the start point x coordinate.
	 * @param startY - the start point y coordinate.
	 * @param finishX - the finish point x coordinate.
	 * @param finishY - the finish point y coordinate.
	 * @implNote the finish is where the notification will be displayed for the <duration>.
	 * @implNote when the notification is added to notify it will move from the start to the finish point.
	 * @implNote once the duration has expired the notification will move back to the start point and then remove itself. 
	 * @author 5som3*/
	public void setKeyFrames(int startX, int startY, int finishX, int finishY) {
		outCurve = new BezierCurveObject(startX, startY, finishX, finishY, getPosition(), () -> {isStarted = true;});
		inCurve = new BezierCurveObject(finishX, finishY, startX, startY, getPosition(), () -> {kill();});
	}
	
	/**Set the start and finish position for the notification.
	 * @param startX - the start point x coordinate.
	 * @param startY - the start point y coordinate.
	 * @param finishX - the finish point x coordinate.
	 * @param finishY - the finish point y coordinate.
	 * @implNote the finish is where the notification will be displayed for the <duration>.
	 * @implNote when the notification is added to notify it will move from the start to the finish point.
	 * @implNote once the duration has expired the notification will move back to the start point and then remove itself. 
	 * @author 5som3*/
	public void setKeyFrames(int startX, int startY, int finishX, int finishY, WarpedAction outComplete, WarpedAction inComplete) {
		outCurve = new BezierCurveObject(startX, startY, finishX, finishY, getPosition(), outComplete);
		inCurve = new BezierCurveObject(finishX, finishY, startX, startY, getPosition(), inComplete);
	}
	
	/**Get the notification text.
	 * @return String - the text displayed in the notification.
	 * @author 5som3*/
	public String getText() {return text;}
	
	/**Starts the notification animation sequence.
	 * @apiNote this method is called by notify automatically when adding notifications.
	 * @apiNote if you don't use the notify assembly then you will need to start the notification manually by calling this method.*/
	public void start() {
		updateGraphics();
		outCurve.start();
	}
	
	/**Set the isStarted boolean.
	 * @param isStarted - if true the duration will start counting down.
	 * @author 5som3*/
	public void setIsStarted(boolean isStarted) {this.isStarted = isStarted;}
	
	/**Get the index assigned to this notification when it was created.
	 * @return int - the index of the notification.
	 * @author 5som3*/
	public int getIndex() {return index;}
	
	/**Set the notification text.
	 * @param text - the text to display in the notification.
	 * @author 5som3*/
	public void setText(String text) {this.text = text;}
	
	/**Set the size of the notification text.
	 * @param size - the size of the text.
	 * */
	public void setTextSize(int size) {
		if(size < 6) {
			Console.err("GUINotification -> setTextSize() -> the size " + size + " is too small. It will be set to 6");
			size = 6;
		}
		font = new Font(font.getFontName(), font.getStyle(), size);
	}
	
	/**Set the offset of the text within the notification.
	 * @param xOffset - the xOffset in pixels.
	 * @param yOffset - the yOffset in pixels. 
	 * @apiNote Offset measured from the top left corner of the notification to the top left corner of the text.
	 * */
	public void setTextOffset(int xOffset, int yOffset) {this.textOffset.set(xOffset, yOffset + font.getSize());}
	
	/**Set the font for the notification text.
	 * @param font - the new font.
	 * @author 5som3*/
	public void setFont(Font font) {this.font = font;}
	
	/**Set the thickness of the border.
	 * @param borderThickness - the thickness measured in pixels. Set to 0 for no border.
	 * @author 5som3*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0) {
			Console.err("GUINotification -> setBorderThickness() -> Thickness " + borderThickness + " is too small, it will be set to 0");
			borderThickness = 0;
		}
	}
	
	/**Set the colour of the border.
	 * @param borderColor - the color for the border
	 * @apiNote Set borderThickness(0) for no border.
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {this.borderColor = borderColor;}
	
	/**Set the color behind the notification text.
	 * @param backgroundColor - the colour to render behind the text.
	 * @implNote this will also clear any background image that has been set.
	 * @author 5som3
	 * */
	public void setBackgroundColor(Color backgroundColor) {
		background = null;
		this.backgroundColor = backgroundColor;
	}
	
	/**Set the color of the notification text.
	 * @param textColor - the color of the text.
	 * @author 5som3*/
	public void setTextColor(Color textColor) {this.textColor = textColor;}
	
	/**Set if the background will be rendered behind the text.
	 * @param isBackgroundVisible - if true the background will be visible else it will not be rendered.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackgroundVisible) {this.isBackgroundVisible = isBackgroundVisible;}
	
	/**Set the total size of the notification.
	 * @param width - the width of the notification in pixels.
	 * @param height - the height of the notification in pixels.
	 * @author 5som3 */
	public void setNotificationSize(int width, int height) {
		if(width <= 0 || height <= 0) {
			Console.err("GUINotification -> setNotificationSize() -> the width or height is too small, must be positive");
			if(width <= 0) width = 200;
			else height = 40;
		}
		setSize(width, height);
		updateGraphics();
	}
	
	/**Set an image to display behind the notification.
	 * @param background - the background image.
	 * @implNote the notification will be resized to the size of the specified background.
	 * @author 5som3*/
	public void setBackground(BufferedImage background) {
		setSize(background.getWidth(), background.getHeight());
		this.background = background;
		updateGraphics();
	}
	
	/**Set the icon to display in the notification.
	 * @param icon - the icon to display
	 * @author 5som3*/
	public void setIcon(BufferedImage icon) {this.icon = icon;}
	
	/**Remove any icon set for this notification.
	 * @author 5som3*/
	public void clearIcon() {this.icon = null;}
	
	/**Set the offset of the icon (if any).
	 * @param xOffset - the x offset in pixels.
	 * @param yOffset - the y offset in pixels.
	 * @apiNote offset measured from the top left corner of the notification to the top left corner of the icon image.
	 * @author 5som3*/
	public void setIconOffset(int xOffset, int yOffset) {iconOffset.set(xOffset, yOffset);}
	
	/**Set the size of the icon.
	 * @param width - the width in pixels.
	 * @param height - the height in pixels.
	 * @author 5som3*/
	public void setIconSize(int width, int height) {
		if(width <= 0 || height <= 0) {
			Console.err("GUINotification -> setIconSize() -> width or height is too small, must be positive");
			if(width <= 0) width = 30;
			else height = 30;
		}
		iconSize.set(width, height);
	}
	
	/**Set the duration before the notification will expire.
	 * @param duration - the duration in ticks. (usually 58 ticks is approximately 1 second).
	 * @implNote The expireAction will trigger once when the duration is reached.
	 * @author 5som3*/
	public void setDuration(int duration) {this.duration = duration;}
	
	/**Has the notification expired.
	 * @result boolean - true if the notification has expired else false.
	 * @author 5som3*/
	public boolean isExpired() {return isExpired;}
	
	
	private void updateGraphics() {
		Graphics g = getGraphics();
		
		if(isBackgroundVisible) {
			g.setColor(borderColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(backgroundColor);
			g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
			if(background != null) g.drawImage(background, borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2, null);			
		}
		
		if(icon != null) g.drawImage(icon, iconOffset.x(), iconOffset.y(), iconSize.x(), iconSize.y(), null);
		
		g.setColor(textColor);
		g.setFont(font);
		g.drawString(text, textOffset.x(), textOffset.y());		
		
		g.dispose();
		pushGraphics();
	}
	
	@Override
	public void updateObject() {
		if(isExpired) return;
		if(isStarted) {
			tick++;
			if(tick > duration) {
				inCurve.start();
				isExpired = true;
			};	
		}
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
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {tick = duration;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	
	

}
