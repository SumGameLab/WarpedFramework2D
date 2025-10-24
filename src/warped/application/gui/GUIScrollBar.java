/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import warped.audio.FrameworkAudio;
import warped.functionalInterfaces.WarpedAction;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsFont.FontStyleType;

public class GUIScrollBar extends WarpedGUI {
	
	private AxisType scrollAxis = AxisType.HORIZONTAL;

	private double progress 		= 0.5;
	private int borderThickness 	= 2;
	
	private Color borderColor 		= Color.BLACK;
	private Color backgroundColor	= Colour.GREY_DARK_DARK.getColor();
	private Color progressColor 	= Colour.GREEN_DARK.getColor();
	private Color buttonColor 		= Colour.GREY.getColor();
	private Color buttonHoverColor 	= Colour.GREY_LIGHT.getColor();
	private Color buttonDragColor 	= Colour.RED_DARK.getColor();
	private Color progressTextColor = Color.WHITE;
	private Color labelColor 		= Colour.YELLOW_LIGHT.getColor();
		
	private double buttonScale 		  = 0.25;
	private VectorI buttonSize 		  = new VectorI(10, 10);
	private VectorI buttonPosition    = new VectorI(10, 10);

	private boolean isLabelVisible 	  = false;
	private String label 			  = "default";
	private Font labelFont			  = UtilsFont.getDefault();
	
	private AffineTransform at 		  = new AffineTransform();
	
	private boolean showProgressText  = true;
	private String progressText 	  = "0.50";
	private VectorI textOffset		  = new VectorI(4, 4);
	private Font progressFont		  = UtilsFont.getDefault();
	private FontStyleType fontStyle = FontStyleType.REGULAR;
	
	private boolean isButtonHovered   = false;
	private boolean isProgressVisible = true; 
	private boolean isDragging		  = false;

	
	private WarpedAction releaseAction = () -> {return;};
	private WarpedAction scrollAction = () -> {return;};
	
	/**A scroll bar with the default parameters.
	 * @author 5som3*/
	public GUIScrollBar() {
		at.rotate(UtilsMath.PI_ON_TWO);
		initRaster(200, 36);		
		initButton();
		updateGraphics();
	}
	
	/**A scroll bar with the specified parameters.
	 * @param label - the label to display in the scroll bar
	 * @author 5som3*/
	public GUIScrollBar(String label) {
		at.rotate(UtilsMath.PI_ON_TWO);
		initRaster(200, 36);			
		initButton();
		this.label = label;
		isLabelVisible = true;
		updateGraphics();
	}
	
	/**A scroll bar with the specified parameters.
	 * @param label - the label to display in the scroll bar.
	 * @param length - the length of the scroll bar in pixels.
	 * @param thickness - the thickness of the scroll bar in pixels.
	 * @author 5som3*/
	public GUIScrollBar(String label, int length, int thickness) {
		at.rotate(UtilsMath.PI_ON_TWO);
		initRaster(length, thickness);		
		initButton();
		this.label = label;
		isLabelVisible = true;
		updateGraphics();
	}
	
	/**A scroll bar with the specified parameters.
	 * @param label - the label to display in the scroll bar.
	 * @param length - the length of the scroll bar in pixels.
	 * @param thickness - the thickness of the scroll bar in pixels.
	 * @param progressColor - the color to show progress of the scroll bar.
	 * @param backgroundColor - the color of the background.
	 * @author 5som3*/
	public GUIScrollBar(String label, int length, int thickness, Color progressColor, Color backgroundColor) {
		at.rotate(UtilsMath.PI_ON_TWO);
		initRaster(length, thickness);		
		initButton();
		this.label = label;
		isLabelVisible = true;
		this.progressColor = progressColor;
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		progressFont = UtilsFont.getFont(fontStyle, progressFont.getSize());
		updateGraphics();
	}
	

	/**Set the axis of movement for the scroll button
	 * @param scrollAxis - the axis to scroll along
	 * @author SomeKid */
	public void setScrollAxis(AxisType scrollAxis) {
		this.scrollAxis = scrollAxis;		
		initRaster(getWidth(), getHeight());
		initButton();
		updateGraphics();
	}
	
	/**Set the action to trigger when the mouse is released on the scrollButton.
	 * @param releaseAction - will trigger once when the mouse is released on the scroll button.
	 * @param				- typically used to apply the new scrolled value to something.
	 * @author SomeKid */
	public void setReleaseAction(WarpedAction releaseAction) {this.releaseAction = releaseAction;}
	
	/**Set the action to trigger when ever the scroll bar moves.
	 * @param scrollAction - will trigger whenever the button is scrolled, will trigger multiple times when dragging the scrollButton.
	 * @author 5som3*/
	public void setScrollAction(WarpedAction scrollAction) {this.scrollAction = scrollAction;}
	
	/**Will remove the release action (if any).
	 * @author 5som3*/
	public void clearReleaseAction() {releaseAction = () -> {return;};}
	
	/**Will remove the scroll action  (if any).
	 * @author 5som3*/
	public void clearScrollAction() {scrollAction = () -> {return;};}
	
	
	/**Get the current progress of the scroll bar.
	 * @return progress - the current progress, range 0.0 -> 1.0
	 * @author SomeKid*/
	public double getProgress() {return progress;}	
	
	/**Should the current progress value be displayed on the scroll button
	 * @param isProgressVisible - if true the current progress will be drawn on the button
	 * @author SomeKid*/
	public void isProgressVisible(Boolean isProgressVisible) {
		this.isProgressVisible = isProgressVisible;
		updateGraphics();
	}
	
	/**Set the font the of the progress text displayed on the scroll button
	 * @param font - the font to set 
	 * @author SomeKid*/
	public void setProgressFont(Font font) {
		this.progressFont = font;
		updateGraphics();
	}
	
	/**Set the size of the progress text.
	 * @param size - the size in pixels
	 * @author 5som3*/
	public void setProgressTextSize(int size) {
		if(size < 6) {
			Console.err("GUIScrollBar -> setProgressTextSize() -> size too small " + size);
			size = 6;
		}
		this.progressFont = new Font(progressFont.getFontName(), progressFont.getStyle(), size);
		updateGraphics();
	}
	
	/**set the color of the progress text.
	 * @param progressColor - the color to render the text.
	 * @author 5som3 */
	public void setProgressTextColor(Color progressColor) {
		this.progressColor = progressColor;
		updateGraphics();
	}
	
	/**Set the text to display in the scrollBar.
	 * @param label - the text to display.
	 * @apiNote will also make the label visible.
	 * @author 5som3*/
	public void setLabel(String label) {
		this.label = label;
		isLabelVisible = true;
		updateGraphics();
	}
	
	/**Set the font of the label.
	 * @param font - the font of the label text.
	 * @author 5som3*/
	public void setLabelFont(Font font) {
		this.labelFont = font;
		updateGraphics();		
	}
	
	/**Set the size of the label text.
	 * @param size - the size in pixels;
	 * @author 5som3*/
	public void setLabelTextSize(int size) {
		if(size < 6) {
			Console.err("GUIScrollBar -> setLabelTextSize() -> size too small " + size);
			size = 6;
		}
		this.labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(), size);
		updateGraphics();
	}
	
	/**set the color of the label text.
	 * @param labelColor - the color of the label text.
	 * @author 5som3*/
	public void setLabelTextColor(Color labelColor) {
		this.labelColor = labelColor;
		updateGraphics();
	}
	
	/**Set the color of the border.
	 * @param borderColor - the color of the border, call setBorderThickness(0) for no border color.
	 * @author SomeKid*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	/**Set the colour that will be displayed on the side of the bar that shows progress.
	 * @param progressColor - the color to apply.
	 * 						- if the progress is 100% this is the color the bar will appear.
	 * @author SomeKid*/
	public void setProgressColor(Color progressColor) {
		this.progressColor = progressColor;
		updateGraphics();
	}
	
	/**Set the colour that will be displayed on the side of the bar that shows regression. 
	 * @param barColor - the color to apply.
	 * 				   - if the progress is 0% this is the color the bar will appear.
	 * @author SomeKid*/
	public void setBarColor(Color barColor) {
		this.backgroundColor = barColor; 
		updateGraphics();
	}
	
	/**Set the thickness of the border color.
	 * @param borderThickness - the thickness to use, set 0 thickness for no border color.
	 * @author SomeKid*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0 || borderThickness > getWidth() / 4 || borderThickness > getHeight() / 4) {
			borderThickness = 2;
			Console.err("GUIScrollBar -> setBorderThickness() -> invalid thickness " + borderThickness);
		}
		this.borderThickness = borderThickness;
		updateGraphics();
	}

	/**Set the progress of the scroll bar.
	 * @param progress - the progress of the bar, value must be in the domain 0.0 -> 1.0.
	 * @author SomeKid*/
	public void setProgress(double progress) {
		if(progress < 0 || progress > 1.0) {
			Console.err("GUIProgressBar -> setProgress() -> the domain of progress is 0.0 -> 1.0 inclusive, the progress value : " + progress + " is outside the domain");
			return;
		}
		this.progress = progress;
		progressText = UtilsMath.getString(progress, 2);
		updateButtonPosition();
		updateGraphics();
	}
	
	/**Increase the progress of the scroll bar by 1%
	 * @author SomeKid */
	public void increaseProgress() {
		progress += 0.01;
		if(progress > 1.0) progress = 1.0;
		progressText = "" + UtilsMath.round(progress, 2);
		updateButtonPosition();
		updateGraphics();
	}
	
	/**Decrease the progress of the scroll bar by 1%
	 * @author SomeKid */
	public void decreaseProgress() {
		progress -= 0.01;
		if(progress < 0.0) progress = 0.0;
		progressText = "" + UtilsMath.round(progress, 2);
		updateButtonPosition();
		updateGraphics();
	}
	
	
	public void setSize(int width, int height) {
		initRaster(width, height);
		updateButtonPosition();
		updateGraphics();
	}
	
	private void initRaster(int length, int thickness) {		
		switch(scrollAxis) {
		case HORIZONTAL: sprite.setSize(length, thickness); break;
		case VERTICAL:   sprite.setSize(thickness, length); break;
		default: Console.err("GUIScrollBar -> initRaster () -> invalid Case : " + scrollAxis); break;
		}
	}
	
	private void initButton() {
		switch(scrollAxis) {
		case HORIZONTAL: 
			buttonSize.set((int)(getWidth() * buttonScale) - borderThickness * 2, (int)(getHeight()) - borderThickness * 2);
			buttonPosition.set((int)((getWidth() - buttonSize.x() - borderThickness) * progress), borderThickness);
			break;
		case VERTICAL:
			buttonSize.set((int)(getWidth()) - borderThickness * 2, (int)(getHeight() * buttonScale) - borderThickness * 2);
			buttonPosition.set(borderThickness, (int)((getHeight() - buttonSize.y() - borderThickness) * progress));
			break;
		default:
			Console.err("GUIScrollBar -> initButton() -> invalid case : " + scrollAxis);
			break;
		}
		
	}	
	
	//--------
	//-------------- Update -----------------
	//--------
	private void updateButtonPosition() {
		switch(scrollAxis) {
		case HORIZONTAL: buttonPosition.set((int)((getWidth() - buttonSize.x() - borderThickness) * progress), borderThickness); break;
		case VERTICAL:   buttonPosition.set(borderThickness, (int)((getHeight() - buttonSize.y() - borderThickness) * progress)); break;
		default: Console.err("GUIScrollBar -> updateButtonPosition() -> invalid switch case : " + scrollAxis); return;
		}
	}
	
	public void updateGraphics() {
		Graphics2D g = getGraphics();
		
		g.setColor(borderColor);
		g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
			
		int x2 = (int)(getWidth() - borderThickness * 2);
		int y2 = (int)(getHeight() - borderThickness * 2);
		
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, x2, y2);
		
		if(isProgressVisible) {
			g.setColor(progressColor);
			if(scrollAxis == AxisType.VERTICAL) g.fillRect(borderThickness, borderThickness, x2, buttonPosition.y());				
			else g.fillRect(borderThickness, borderThickness, (int)(buttonPosition.x()), y2);			
		}
		
		if(isLabelVisible) {
			g.setColor(labelColor);
			if(scrollAxis == AxisType.VERTICAL) {
				FontRenderContext frc = g.getFontRenderContext();
				Font f = progressFont.deriveFont(at);
				TextLayout tString = new TextLayout(label, f, frc);
				tString.draw(g, (borderThickness * 2) + (progressFont.getSize() / 2), (borderThickness * 4) + (progressFont.getSize() * 2));
			} else {				
				g.setFont(progressFont);
				g.drawString(label, borderThickness * 2, progressFont.getSize() + borderThickness * 2);
			}
		}
		
		g.setColor(borderColor);
		g.fillRect(buttonPosition.x(), buttonPosition.y(), buttonSize.x(), buttonSize.y());
		if(isDragging) g.setColor(buttonDragColor);
		else if(isButtonHovered)g.setColor(buttonHoverColor); 
		else g.setColor(buttonColor);
		g.fillRect(buttonPosition.x() + borderThickness, buttonPosition.y() + borderThickness, buttonSize.x() - borderThickness * 2, buttonSize.y() - borderThickness * 2);
		
		if(showProgressText) {
			g.setColor(progressTextColor);
			g.setFont(progressFont);
			g.drawString(progressText, buttonPosition.x() + textOffset.x(), buttonPosition.y() + textOffset.y() + progressFont.getSize());			
		}
		
		g.dispose();
		pushGraphics();
	}
		
	protected boolean isButtonHit(WarpedMouseEvent mouseEvent) {
		Point p = mouseEvent.getPointRelativeToObject();
		if(p.x > buttonPosition.x() && p.y > buttonPosition.y() && p.x < buttonPosition.x() + buttonSize.x() && p.y < buttonPosition.y() + buttonSize.y()) return true;
		else return false;
	}
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
		isButtonHovered = false;
		if(isDragging) {
			isDragging = false;
			releaseAction.action();
		}
		updateGraphics();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		if(isDragging) {
			isDragging = false;
			releaseAction.action();
			
		}
		if(isButtonHit(mouseEvent)) {
			if(!isButtonHovered) {
				isButtonHovered = true;
				updateGraphics();
				FrameworkAudio.defaultHover.play();
			}
		} else {
			if(isButtonHovered) {
				isButtonHovered = false;
				updateGraphics();
				FrameworkAudio.defaultUnhover.play();
			}
		}
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(isDragging) {
			if(scrollAxis == AxisType.HORIZONTAL) setProgress(UtilsMath.divide(mouseEvent.getPointRelativeToObject().x, getWidth()));
			else setProgress(UtilsMath.divide(mouseEvent.getPointRelativeToObject().y, getHeight()));
			updateButtonPosition();
			updateGraphics();
			scrollAction.action();
		} else if(isButtonHit(mouseEvent)) isDragging = true;
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(isButtonHit(mouseEvent)) {			
			if(!isDragging) {
				isDragging = true;
				updateGraphics();
			}
		}
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		isDragging = false;
		updateGraphics();
		releaseAction.action();			
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		if(0 + mouseEvent.getWheelRotation() > 0) increaseProgress();
		else decreaseProgress();
		if(scrollAction != null) scrollAction.action();
	}
	
}
