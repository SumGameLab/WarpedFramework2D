/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import warped.functionalInterfaces.WarpedGraphicAction;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.Winteger;
import warped.utilities.math.Woolean;
import warped.utilities.math.Wouble;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUIValue extends WarpedGUI {

	/*GUIValue provides these functions
	 * 
	 * 		- display a value as text on the screen.
	 * 		- set and forget, automatically queues updating of the graphics when ever the set value changes
	 * 		- set a specific size for the value to be displayed.
	 * 		- set a color for the border, background and value
	 * 		- set the font of the value displayed
	 * 		- set visibility of background
	 * */
	
	public enum ValueType{
		INTEGER,
		DOUBLE,
		BOOLEAN,
		VECTOR_D,
		VECTOR_I,
	}
	
	private ValueType valueType = ValueType.INTEGER;
	private int borderThickness = 2;
	
	private Color borderColor = Color.BLACK;
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color fontColor = Color.WHITE;
	
	private int decimals = 0;
		
	private Font font = UtilsFont.getPreferred();
	private VectorI textOffset = new VectorI(borderThickness, borderThickness + font.getSize()); 
	
	private Winteger  winteger;
	private Wouble    wouble;
	private Woolean   woolean;
	private VectorD   vectorD;
	private VectorI   vectorI;
	
	private boolean isBackgroundVisible = true;
	private boolean isGraphicsQueued 	= false;

	private WarpedGraphicAction updateGraphicsAction = g -> {return;};
	
	/**A new value with the specified size.
	 * @param width - the width in pixels.
	 * @param height -the height in pixels.
	 * @author 5som3*/
	public GUIValue(int width, int height) {
		setSize(width, height);
		updateGraphics();
	}
	
	/**queues graphics to be updated at the next updateObject cycle*/
	public final void queueUpdateGraphics() {isGraphicsQueued = true;}
		
	@Override
	public void updateObject() {
		if(isGraphicsQueued) {
			updateGraphics();
			isGraphicsQueued = false;
		}
	}
	
	/**Set the font for the text
	 * @param font - the new font to render the text with
	 * @author 5som3*/
	public void setFont(Font font) {
		this.font = font;
		updateGraphics();
	}
	
	/**Get the type of value
	 * @return type - possible types - Integer, Double, Boolean, Vector_D, Vector_I
	 * @author 5som3*/
	public ValueType getType() {return valueType;}
	
	/**Set the size of the font ( the text size)
	 * @param size - the new text size;
	 * @author 5som3*/
	public void setFontSize(int size) {
		this.font = new Font(font.getFontName(), font.getStyle(), size);
		updateGraphics();
	}
	
	/**Set the number of decimal places when displaying a vectorD or wouble.
	 * @param decimals - the number of decimal places to show.
	 * @apiNote setDecimals(0) will display the full number of decimal places.
	 * @apiNote Only visibly different when displaying a wouble or VectorD.
	 * @author 5som3*/
	public void setDecimals(int decimals) {
		if(decimals < 0 || decimals > 9) {
			Console.ln("GUIValue -> setDecimals() -> decimals is out of bounds 0 <= decimals < 6. decimals value : " + decimals);
			if(decimals < 0) decimals = 1;
			else decimals = 9;
		}
		this.decimals = decimals;
	}

	/**Set the thickness of the border color.
	 * @param borderThickness - the new thickness for the borderColor
	 * @apiNote Set border thickness to 0 for no border.
	 * @apiNote The border will not be visible if setBackgroundVisible(false);
	 * @author 5som3 */
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	/**Set the colour of the border that surrounds the GUIValue
	 * @param borderColor - the color of the border to draw
	 * @apiNote If the setBackgroundVisible(false) the border will also be invisible
	 * @apiNote To have a colour background with no border setBorderThickness(0)
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/**Set if the background behind the text will be visible
	 * @param isBackground - if true the background will be rendered, if false the background will be 100% transparent.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackground) {
		this.isBackgroundVisible = isBackground;
		updateGraphics();
	}
	
	/**Set the color of the background
	 * @param backgroundColor - the colour of the background behind the text
	 * @author 5som3
	 * */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	/**Set the color to draw the text
	 * @param Color - the colour of the text value
	 * @author 5som3*/
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
		updateGraphics();
	}
	
	/**Set the offset to draw the text value at.
	 * @param xOffset - the xOffset measured in pixels from the top left corner of the raster
	 * @param yOffset - the yOffset measured in pixels from the top left corner of the raster.
	 * @author 5som3*/
	public void setTextOffset(int xOffset, int yOffset) {
		textOffset.set(xOffset, yOffset);
		updateGraphics();
	}
		
	/**Clears all data and updates the graphics the specified parameters.
	 * 
	 * */
	public void clearTarget() {
		woolean  = null;
		winteger = null;
		wouble 	 = null;
		vectorI  = null;
		vectorD  = null;
		clearGraphics();
	}
	
	/**Set a Integer value to display
	 * @param val - An (object) Integer that this GUIValue should display 
	 * @author 5som3*/
	public void setTarget(Winteger winteger) {		
		valueType = ValueType.INTEGER;
		updateGraphicsAction = updateInteger;
		if(this.winteger != null) this.winteger.clearDeltaAction();
		this.winteger = winteger;
		winteger.setDeltaAction(val -> {queueUpdateGraphics();});
		updateGraphics();
	}
	
	/**Set a Double value to display
	 * @param wouble - An (object) Integer that this GUIValue should display 
	 * @author 5som3*/
	public void setTarget(Wouble wouble) {
		valueType = ValueType.DOUBLE;
		updateGraphicsAction = updateDouble;
		if(this.wouble != null) this.wouble.clearDeltaAction();
		this.wouble = wouble;
		wouble.setDeltaAction(val -> {queueUpdateGraphics();});
		updateGraphics();		
	}
	
	/**Set a Boolean value to display
	 * @param woolean - An (object) Boolean that this GUIValue should display 
	 * @author 5som3*/
	public void setTarget(Woolean woolean) {
		valueType = ValueType.BOOLEAN;
		updateGraphicsAction = updateBoolean;
		if(this.woolean != null) this.woolean.clearDeltaAction();
		this.woolean = woolean;
		woolean.setDeltaAction(val -> {queueUpdateGraphics();});
		updateGraphics();		
	}
	
	/**Set a VectorI value to display
	 * @param vectorI - An (object) VectorI that this GUIValue should display 
	 * @author 5som3*/
	public void setTarget(VectorI vectorI) {
		valueType = ValueType.VECTOR_I;
		updateGraphicsAction = updateVectorI;
		if(this.vectorI != null) this.vectorI.clearDeltaAction();		
		this.vectorI = vectorI;
		vectorI.setDeltaAction(val -> {queueUpdateGraphics();});
		updateGraphics();		
	}
	
	/**Set a Vec2d value to display
	 * @param vectorD - An (object) Vec2d that this GUIValue should display 
	 * @author 5som3*/
	public void setTarget(VectorD vectorD) {		
		valueType = ValueType.VECTOR_D;
		updateGraphicsAction = updateVectorD;
		if(this.vectorD != null) this.vectorD.clearDeltaAction();
		this.vectorD = vectorD;
		vectorD.setDeltaAction(val -> {queueUpdateGraphics();});
		updateGraphics();		
	}
	
	/**Clear the graphics from this GUI.
	 * @author 5som3*/
	private void clearGraphics() {
		Graphics2D g2d = getGraphics();
		if(isBackgroundVisible) {
			g2d.setColor(borderColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			
			g2d.setColor(backgroundColor);
			g2d.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);			
		}
		
		g2d.dispose();
		pushGraphics();
	}
	
	/**Update the graphics by running the corresponding updateGraphicsAction.
	 * @implNote The updateGraphicsAction is set automatically to be the corresponding action when the setValue() function is called
	 * @author 5som3*/
	private void updateGraphics() {
		Graphics2D g2d = getGraphics();
		
		if(isBackgroundVisible) {
			g2d.setColor(borderColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			
			g2d.setColor(backgroundColor);
			g2d.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);			
		}
		
		g2d.setColor(fontColor);
		g2d.setFont(font);
		updateGraphicsAction.action(g2d);
		
		g2d.dispose();
		pushGraphics();
	}

	private final WarpedGraphicAction updateInteger = g2d -> {g2d.drawString(winteger.getString(),textOffset.x(), textOffset.y());};
	private final WarpedGraphicAction updateDouble  = g2d -> {
		if(decimals == 0) g2d.drawString(wouble.getString(),  textOffset.x(), textOffset.y());
		else g2d.drawString(wouble.getString(decimals),  textOffset.x(), textOffset.y());
	};
	private final WarpedGraphicAction updateBoolean = g2d -> {g2d.drawString(woolean.getString(), textOffset.x(), textOffset.y());};
	private final WarpedGraphicAction updateVectorI = g2d -> {g2d.drawString(vectorI.getString(), textOffset.x(), textOffset.y());};
	private final WarpedGraphicAction updateVectorD = g2d -> {
		if(decimals == 0) g2d.drawString(vectorD.getString(), textOffset.x(), textOffset.y());
		else g2d.drawString(vectorD.getString(decimals), textOffset.x(), textOffset.y());
	};
	
	
	@Override
	protected void mouseEntered() {}

	@Override
	protected void mouseExited() {}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {}
	
		
	



}
