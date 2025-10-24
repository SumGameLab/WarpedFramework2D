/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.graphics.sprite.RotationSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;

public class GUIDialDisplay extends WarpedGUI {

	/*GUIDial provides these functions
	 * 		- display rotation data to the user using a visual dial
	 * 		- can add multiple 'hands' to the dial which can each display a unique rotation
	 * 		- can have static hands and a rotating dial for an alternate effect
	 * 		- Not controlled by the user - if you want the user to input rotation use GUIDialControl
	 */

	private boolean isTextVisible 			= false; // the rotation of the hand at index 0 will be displayed in the dial face
	private boolean isTextBackgroundVisible = false;
	private String text = "0.0";
	private Colour textColour 			= Colour.YELLOW;
	private Colour textBackgroundColour	= Colour.BLACK;
	public Font textFont = UtilsFont.getDefault();
	private VectorI textOffset			= new VectorI(12, 12);
	private VectorI textBackgroundSize = new VectorI(60, 30);
	
	private RotationSprite faceSprite   = FrameworkSprites.dialIcons.generateRotationSprite(4, 0); // the face of the dial behind the hands - can also be rotate for reverse effect, hand stays still and dial rotates
	private ArrayList<RotationSprite> handSprites = new ArrayList<>();	
	
	/**A new display dial with the default parameters.
	 * @author 5som3*/
	public GUIDialDisplay() {
		setSize(100, 100);
		addHand(FrameworkSprites.dialIcons.getSprite(0, 1));
		updateGraphics();
	}
		
	
	/**A new display dial with the specified parameters.
	 * @param width - the width of the display in pixels.
	 * @param height- the height of the display in pixels.
	 * @author 5som3*/
	public GUIDialDisplay(int width, int height) {
		setSize(width, height);
		addHand(FrameworkSprites.dialIcons.getSprite(0, 1));
		updateGraphics();
	}
		
	/**A new display dial with the specified parameters.
	 * @param dialImage - the image for the face of the display.
	 * @param handImage - the image for the rotation indicator hand.
	 * @apiNote the width and height of the dial will based on the dialImage size.
	 * @author 5som3*/
	public GUIDialDisplay(BufferedImage dialImage, BufferedImage handImage) {
		setSize(dialImage.getWidth(), dialImage.getHeight());
		faceSprite = new RotationSprite(dialImage);
		addHand(handImage);
		updateGraphics();
	}
	
	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		return;
	}
	
	/**Add a rotatable hand to the dial
	 * @param handImage - the Image to use for the rotation sprite
	 * @apiNote The dial can be used to display a rotation angle.
	 * @apiNote If isUserControllable is set true the dial can be used for user input of rotation.
	 * @apiNote 	-Only the default hand for the dial is user controllable, any hand added with addHands() will not be controllable.
	 * @apiNote     -To have multiple user controlled hands you will need to add multiple GUIDials.
	 * @apiNote It would be possible to display and receive input on the same dial but I would recommend making a dial for each.
	 * @author SomeKid*/
	public void addHand(BufferedImage handImage) {
		handSprites.add(new RotationSprite(handImage));
	}

	
	/**Set the rotation of all the hands on the dial. 
	 * @param rotation - the angle to rotate.
	 * @apiNote Angle is measured in radians as clockwise rotation starting from the positive horizontal axis
	 * @apiNote i.e. a hand with 0 rotation will point to the right, PI/2 rotation will point down
	 * @author SomeKid*/
	public void setHandRotation(double rotation) {
		text = "" + rotation;
		handSprites.forEach(s -> {s.setRotation(rotation);});
		updateGraphics();
	}
	
	/**Set the rotation of a specific hand on the dial.
	 * @param index    - the index of the hand to rotate 
	 * @param rotation - the angle to rotate.
	 * @apiNote Angle is measured in radians as clockwise rotation starting from the positive horizontal axis
	 * @apiNote i.e. a hand with 0 rotation will point to the right, PI/2 rotation will point down
	 * @author SomeKid*/
	public void setHandRotation(int index, double rotation) {
		if(index == 0) text = "" + rotation;
		if(index < 0 || index >= handSprites.size()) {
			Console.err("GUIDial -> setHandRotation() -> tried to set the rotation for a non-existant hand at index : " + index);
			return;
		}
		handSprites.get(index).setRotation(rotation);
		updateGraphics();
	}
	
	/**Set the rotation of the dial's face. 
	 * (The image behind the hands).
	 * Can be used as an alternative way to display a value, have the face rotate to the arrow instead of arrow rotate on a face.
	 * @param rotation - measured as clockwise rotation from the positive horizontal axis.
	 * @author SomeKid*/
	public void setFaceRotation(double rotation) {
		text = "" + rotation;
		faceSprite.setRotation(rotation);
		updateGraphics();
	}
	
	/**@return double - the current rotation of the Dials faceSprite.
	 * @return 		 - measured as clockwise rotation from the positive horizontal axis.
	 * @author SomeKid*/
	public double getFaceRotation() {return faceSprite.getRotation();}
	
	/**Get the current rotation of a specific hand.
	 * @param index  - the index of the hand to get.
	 * @return double - rotation measured as clockwise rotation from the positive horizontal axis.
	 * @author SomeKid */
	public double getHandRotation(int index) {
		if(index < 0 || index >= handSprites.size()) {
			Console.err("GUIDial -> getHandRotation() -> tried to get the rotation for a hand that doesn't exist at index : " + index);
			return 0.0;
		} else return handSprites.get(index).getRotation();
	}
	
	/**Get the current rotation of all the hands on the dial
	 * @return double[] - array containing each rotation
	 * @author SomeKid*/
	public double[] getHandRotations() {
		double[] result = new double[handSprites.size()];
		for(int i = 0; i < handSprites.size(); i++) {
			result[i] = handSprites.get(i).getRotation();
		}
		return result;
	}
	
	/**Makes the rotation value of the primary hand hand visible as text on the dial
	 * @author SomeKid*/
	public void visibleText() {isTextVisible = true;}
	
	/**Makes the rotation value of the primary hand hand invisible as text on the dial
	 * @author SomeKid*/
	public void invisibleText() {isTextVisible = false;}
	
	/**The text colour for the value displaying rotation
	 * @param Colour - the colour for the text
	 * @apiNote The text will only be drawn if the visibleText() function is called*/
	public void setTextColour(Colour textColour) {
		this.textColour = textColour;
		updateGraphics();
	}
	
	/**The text background colour is displayed behind the rotation text
	 * @apiNote text background will only be visible if the visibleText() has been called and a background colour has been specified with setTextBackgroundColour();
	 * @author SomeKid*/
	public void setTextBackgroundColor(Colour colour) {
		isTextBackgroundVisible = true;
		this.textBackgroundColour = colour;
	}
	
	/**The offset for the rotation text.
	 * @param x, y - the offset measured in pixels from the top left corner.
	 * @apiNote The text will only be drawn if the visibleText() function is called.
	 * @author SomeKid*/
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
	}
	
	/**The text size for the value displaying rotation.
	 * @param textSize - the size for the text.
	 * @apiNote The text will only be drawn if the visibleText() function is called*/	
	public void setTextSize(int textSize) {
		textSize = UtilsMath.clampMin(textSize, 0);
		this.textFont = new Font(textFont.getFontName(), textFont.getStyle(), textSize);
	}
	
	/**Set the textStyle of the rotation text.
	 * @param textStyle - 0 : plain
	 * 					- 1 : Bold
	 * 					- 2 : Italic
	 * @author SomeKid */
	public void setTextStyle(int textStyle) {
		if(textStyle < 0 || textStyle > 2) {
			Console.err("GUIDial -> setTextStyle() -> invalid text style : " + textStyle + ", see documentation for valid styles");
			textStyle = 0;
		} 
		textFont = new Font(textFont.getFontName(), textStyle, textFont.getSize());
		updateGraphics();
	}
	
	/**Set the size of the background behind the rotation text
	 * @param width, height - the size of the box in pixels
	 * @apiNote text background will only be visible if the visibleText() has been called and a background colour has been specified with setTextBackgroundColour();
	 * @author SomeKid*/
	public void setTextBackgroundSize(int width, int height) {
		if(width < 0 || height < 0 || width > getWidth() || height > getHeight()) {
			Console.err("GUIDial -> setTextBackgroundSize() -> text background is invalid size (width, height) : ( " + width + ", " + height + ")");
			return;
		} else textBackgroundSize.set(width, height);
	}
	
	
	public void updateGraphics() {
		Graphics2D g = getGraphics();
		
		g.setComposite(UtilsImage.drawComposite);
		g.drawImage(faceSprite.raster(), 0, 0, getWidth(), getHeight(), null);
		
		for(int i = 0; i < handSprites.size(); i++) {			
			g.drawImage(handSprites.get(i).raster(), 0, 0, getWidth(), getHeight(), null);
		}
		
		if(isTextVisible) {
			if(isTextBackgroundVisible) {
				g.setColor(textBackgroundColour.getColor());
				g.fillRect(textOffset.x() - 4, textOffset.y() - 4, textBackgroundSize.x(), textBackgroundSize.y());			
			}
			
			g.setColor(textColour.getColor());
			g.drawString(text, textOffset.x(), textOffset.y());
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
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
}
