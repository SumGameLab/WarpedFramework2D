package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsString;

public class GUIClock extends WarpedGUI {

	//A graphic readout for the time
	
	private Color borderColor = Color.BLACK;
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color textColor = Color.WHITE;
	
	private boolean isBackgroundVisible = true;
	
	private Font font = fontMatrix.getDynamic();
	private VectorI textOffset = new VectorI();
	private int borderThickness = 2;
	
	public enum ClockModeType {
		HOUR_12_WITH_SECONDS,
		HOUR_12_WITHOUT_SECONDS,
		HOUR_24,
	}
	
	private ClockModeType mode = ClockModeType.HOUR_12_WITHOUT_SECONDS;
	
	public GUIClock() {
		setSize(70, 30);
	}
	
	/**Set the font of the time
	 * @param font - the font to draw the time. 
	 * @author 5som3*/
	public void setFont(Font font) {
		this.font = font;
		updateGraphics();
	}
	
	/**Set the offset of the time text.
	 * @param int x - the x offset in pixels measured from the top left corner.
	 * @param int y - the y offset in pixels measured from the top left corner.
	 * @author 5som3*/
	public void setTextOffset(int x, int y){
		textOffset.set(x, y);
		updateGraphics();
	}
	
	/**Set the colour of the time text.
	 * @param textColour - set the color of the text. 
	 * @author 5som3*/
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	/**Set the size of the text.
	 * @param textSize - the size of the text.
	 * @author 5som3*/
	public void setTextSize(int textSize) {
		if(textSize < 4) {
			Console.err("GUIClock -> setTextSize() -> text size is too small : " + textSize);
			return;
		}
		font = font.deriveFont(Font.PLAIN, textSize);
		updateGraphics();
	}
	
	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		return;
	}
	
	/**Set if the background and border should be rendered behind the time text.
	 * @param isBackgroundVisible - if true the background and border will be visible else they will not be rendered.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackgroundVisible) {
		this.isBackgroundVisible = isBackgroundVisible;
		updateGraphics();
	}
	
	/**Set the display mode for the time.
	 * @param mode - the mode of time display.
	 * @author 5som3*/
	public void setClockMode(ClockModeType mode) {
		this.mode = mode;
		updateGraphics();
	}
	
	/**Set the colour of the border.
	 * @param borderColor - the colour to render the border.
	 * @apiNote set border thickness to 0 for no border.
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/**Set the colour of the background.
	 * @param backgroundColor - the colour to render the background behind the time text.
	 * @author 5som3*/
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	/**Set the thickness of the border.
	 * @param borderThickness - the thickness of the border in pixels.
	 * @apiNote borderThickness must be 0 or greater.
	 * @author 5som3*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0) {
			Console.err("GUIClock -> setBorderThickness() -> the border thickness must be positive or 0 not : " + borderThickness);
			borderThickness = 0;
		}
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	
	
	@Override
	public void updateMid() {updateGraphics();};
	
	
	@Override
	public void updateGraphics() {
		Graphics2D g = getGraphics();
		
		if(isBackgroundVisible) {			
			g.setColor(borderColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(backgroundColor);
			g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
		}
		
		g.setColor(textColor);
		g.setFont(font);
		switch(mode) {
		case HOUR_12_WITHOUT_SECONDS: g.drawString(UtilsString.getLocalTime(), textOffset.x(), textOffset.y() + g.getFontMetrics().getMaxAscent());		break;
		case HOUR_12_WITH_SECONDS: g.drawString(UtilsString.getLocalTimePrecise(), textOffset.x(), textOffset.y() + g.getFontMetrics().getMaxAscent());	break;
		case HOUR_24: g.drawString(UtilsString.getLocalTime24Hour(), textOffset.x(), textOffset.y() + g.getFontMetrics().getMaxAscent()); 				break;
		default:
			Console.err("GUIClock -> updateGraphisc() -> invalid case : " + mode);
			g.drawString("ERROR!", textOffset.x(), textOffset.y() + g.getFontMetrics().getMaxAscent());
			break;
		}
		
		g.dispose();
		pushGraphics();
	}

	@Override
	protected void mouseEntered() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseExited() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

}
