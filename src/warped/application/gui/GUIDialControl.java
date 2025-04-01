/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import warped.application.state.WarpedState;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.sprite.RotationSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class GUIDialControl extends WarpedGUI {

	/*GUIDialControl is designed to supersede GUIRotationPicker. It provides these functionalities : 
	 * 
	 * 		- Allows the user to control a value by rotating a virtual dial.
	 * 		- Set a custom image for dial (the rotating part) and the face (the background).
	 * 		- Option to display value on the dial.
	 * 		- Option to have background behind the value.
	 * 		- Option to have reversed action (the dial in a fixed rotation and move the face).
	 * 		- option to have an action trigger when the dial reaches a specific value.
	 * 		- set the range of motion for the dial with setMax/setMinRotation().
	 * */
	
	private double maxRotation = UtilsMath.TWO_PI;
	private double minRotation = 0.0;

	private boolean isTextVisible = true;
	private boolean isBackgroundVisible = true;
	private boolean isFixedDial = false;
	
	private double actionValue = -1.0;
	private double rotation = 0.0;

	private int borderThickness = 2;
	
	private int labelPadding = 30; 
	
	private int decimals = 2;
	private Color textColor = Colour.YELLOW.getColor();
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color borderColor = Colour.BLACK.getColor();

	private Font font = new Font("DialFont", Font.PLAIN, 16);
	
	private VectorI textOffset = new VectorI(30, 100);
	
	private RotationSprite dial;
	private RotationSprite face;
	
	private WarpedAction valueAction = () -> {return;};
	private WarpedAction rotateAction = () -> {return;};
	
	private Point lastPoint = new Point();
	private Point currentPoint = new Point();
	
	/**A new dial control with the default parameters.
	 * @author 5som3*/
	public GUIDialControl() {
		setSize(100, 100 + labelPadding);
		face = new RotationSprite(FrameworkSprites.dialIcons.getSprite(4, 0));
		dial = new RotationSprite(FrameworkSprites.dialIcons.getSprite(4, 1));
		updateGraphics();
	}
	
	/**A new dial control with the specified parameters.
	 * @param dial - the image to use for the dial.
	 * @param face - the image to use for the face.
	 * @author 5som3*/
	public GUIDialControl(BufferedImage dial, BufferedImage face) {
		setSize(face.getWidth(), face.getHeight() + labelPadding);
		this.face = new RotationSprite(face);
		this.dial = new RotationSprite(dial);
		updateGraphics();
	}
		
	/**Set the font for the value text.
	 * @param font - the font to render the text with.
	 * @author 5som3*/
	public void setFont(Font font) {
		this.font = font;
		updateGraphics();
	}
	
	/**Set the size of the text.
	 * @param size - the size in pixels of the text.
	 * @author 5som3*/
	public void setTextSize(int size) {
		if(size < 6) {
			size = 6;
			Console.err("GUIDialControl -> setTextSize() -> size is too small : " + size);
		}
		font = new Font(font.getFontName(), font.getStyle(), size);
		updateGraphics();
	}
	
	/**Set the the offset of the value text.
	 * @param xOffset - the xOffset in pixels.
	 * @param yOffset - the yOffset in pixels.
	 * @apiNote offset measured from the top left corner of the dial to the bottom left corner of the text.
	 * @author 5som3*/
	public void setTextOffset(int xOffset, int yOffset) {
		textOffset.set(xOffset, yOffset);
		updateGraphics();
	}
	
	/**Set the color of the border around the value text.
	 * @param borderColor - the color of the border.
	 * @apiNote setBorderthickness(0) for no border.
	 * @apiNote Only visible if isTextVisible equals true.
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/**Set the color of the background.
	 * @param backgroundColor - the color rendered behind the value text.
	 * @apiNote Only visible if isTextVisible equals true.
	 * @author 5som3*/
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	/**Set the border thickness.
	 * @param borderThickness - set the thickness of the borderColor in pixels.
	 * @apiNote set to 0 for no border.
	 * @author 5som3*/
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	/**Limit the amount that the dial can be rotated.
	 * @param minRotation - the minimum rotation that the dial can have in radians.
	 * @param maxRotation - the maximum rotation that the dial can have in radians.
	 * @apiNote clockwise rotation is positive, anticlockwise rotation is negative.
	 * @author 5som3 */
	public void setRotationBounds(double minRotation, double maxRotation) {
		if(maxRotation <= minRotation) {
			Console.err("GUIDialControl -> setRotationBounds() -> maxRotation must be larger than minRotation");
			if(minRotation < 0) maxRotation = -minRotation;
			else maxRotation = minRotation * 2;
		}
		this.minRotation = minRotation;
		this.maxRotation = maxRotation;
		if(rotation < minRotation) rotation = minRotation;
		else if(rotation > maxRotation) rotation = maxRotation;
	}
	
	/**Set the rotation in radians.
	 * @param rotation - the rotation for the dial.
	 * @author 5som3*/
	public void setRotation(double rotation) {
		if(rotation < minRotation || rotation > maxRotation) {
			Console.ln("GUIDialControl -> setRotation() -> rotation out of bounds : " + rotation);
			if(rotation < minRotation) {
				rotation = minRotation;
				WarpedState.notify.note("Minimum Reached");
			} else {
				rotation = maxRotation;
				WarpedState.notify.note("Maximum Reached");
			}
		}
		this.rotation = rotation;
		if(rotation == actionValue) valueAction.action();
		if(isFixedDial) face.setRotation(this.rotation);
		else dial.setRotation(this.rotation);
		updateGraphics();
	}
	
	/**Set an action to trigger when the dial reaches the specified value.
	 * @param value - the value that will trigger the action.
	 * @param action - the action to trigger when the value is reached.
	 * @apiNote the action will trigger once when the rotation is set to the value.
	 * @author 5som3*/
	public void setValueAction(double value, WarpedAction action) {
		if(value < minRotation || value > maxRotation) {
			Console.err("GUIDialControl -> setValueAction() -> value out of bounds : " + rotation);
			return;
		}
		this.actionValue = value;
		this.valueAction = action;
	}
	
	
	/**Clear any action set to trigger when the specified value is reached.
	 * @author 5som3*/
	public void clearValueAction() {valueAction = () -> {return;};}
	
	/**Get the rotation in as a percentage of how much it is rotate
	 * @param double - a value between 0.0 -> 1.0 (inclusive).
	 * @apiNote Will return 1.0 when the rotation is at maximum. 
	 * */
	public double getPercentage() {return (Math.abs(minRotation) + rotation)/(maxRotation - minRotation);}
	
	/**Get the rotation of the dial
	 * @param rotation - the rotation of the dial in radians.
	 * @author 5som3*/
	public double getRotation() {return rotation;}
	
	/**Set an action to occur whenever the dial is rotated.
	 * @param rotationAction - that action will trigger every time the dial is rotated. 
	 * @param the action will trigger multiple times during a single instance of dragging.
	 * @author 5som3*/
	public void setRotationAction(WarpedAction rotationAction) {this.rotateAction = rotationAction;}
	
	/**Clear the rotation action (if any).
	 * @author 5som3*/
	public void clearRotationAction() {this.rotateAction = () -> {return;};}
	
	/**Set the amount of padding underneath the dial
	 * @param labelPadding - the padding height in pixels.
	 * @apiNote This space is used to draw the label and rotation text (if visible).
	 * @apiNote Set label padding to 0 for no padding.
	 * */
	public void setLabelPadding(int labelPadding) {
		if(labelPadding < 0) {
			Console.err("GUIDialControl -> setLabelPadding() -> label padding too small : " + labelPadding);
			labelPadding = 0;
		} else if(labelPadding > getHeight() / 2) {
			Console.err("GUIDialControl -> setLabelPadding() -> label padding too large : " + labelPadding);
			labelPadding = getHeight() / 2;
		}
		this.labelPadding = labelPadding;
		updateGraphics();		
	}
	
	private void updateGraphics() {
		Graphics g = getGraphics();
		
		g.drawImage(face.raster(), 0, 0, getWidth(), getHeight() - labelPadding, null);
		g.drawImage(dial.raster(), 0, 0, getWidth(), getHeight() - labelPadding, null);
		
		if(isTextVisible) {
			g.setFont(font);
			String text = UtilsMath.getString(rotation, decimals);
			int width = g.getFontMetrics().stringWidth(text) + borderThickness * 4;
			if(isBackgroundVisible){
				g.setColor(borderColor);
				g.fillRect(textOffset.x(), textOffset.y(), width, font.getSize() + borderThickness * 4);
				g.setColor(backgroundColor);
				g.fillRect(textOffset.x() + borderThickness, textOffset.y() + borderThickness, width - borderThickness * 2,  font.getSize() + borderThickness * 2);
			} 
			
			g.setColor(textColor);
			g.drawString(text, textOffset.x() + borderThickness * 2, textOffset.y() + borderThickness + font.getSize());
		}
		
		g.dispose();
		pushGraphics();
	}
	
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		double rotation = 0.0;
		currentPoint.setLocation(mouseEvent.getPointRelativeToObject());
		
		currentPoint.x -= (getWidth() - labelPadding) / 2;
		currentPoint.y -= (getHeight() - labelPadding) / 2;
		
		rotation = UtilsMath.angleBetweenVectorsSigned(lastPoint, currentPoint);		
		setRotation(this.rotation + rotation);
		
		lastPoint.setLocation(currentPoint);
		rotateAction.action();
		updateGraphics();
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		currentPoint.setLocation(mouseEvent.getPointRelativeToObject());
		lastPoint.setLocation(currentPoint);
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

}
