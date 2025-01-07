/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public class GUIScrollBar extends WarpedGUI {
	
	private AxisType scrollAxis = AxisType.HORIZONTAL;

	private double progress 		= 0.0;
	private int borderThickness 	= 3;
	
	private Color borderColor 		= Color.BLACK;
	private Color barColor    		= Color.DARK_GRAY;
	private Color progressColor 	= new Color(0, 255, 0, 100);
	private Color buttonColor 		= Color.LIGHT_GRAY;
	private Color buttonHoverColor 	= new Color(110,60,60);
	private Color buttonDragColor 	= new Color(190,60,60);
	private Color progressTextColor = Color.WHITE;
	private Color labelColor 		= Color.YELLOW;
	
	private BufferedImage progressBarRaster;
	private Vec2i defaultBarSize	= new Vec2i(150, 35);
	
	private int buttonScale 		= 4;
	private Vec2i buttonSize;
	private Vec2i buttonPosition;

	private boolean showLabel 		= false;
	private String label 			= "default";
	AffineTransform at 				= new AffineTransform();
	
	private boolean showProgressText = true;
	private String progressText 	 = "0.50";
	private int textOffset			 = 2;
	private Font progressFont		 = UtilsFont.getPreferred();
	
	private boolean isHovered		  = false;
	private boolean isProgressVisible = true; 
	private boolean isDragging		  = false;
	private boolean enteredDragging	  = false;
	private Vec2d dragOffset		  = new Vec2d();
	
	private WarpedAction scrollAction;
	
	public GUIScrollBar() {
		at.rotate(UtilsMath.PI_ON_TWO);
		initRaster(defaultBarSize);		
		setRaster(UtilsImage.generateClone(progressBarRaster));
		initButton();
		updateGraphics();
	}
	
	public GUIScrollBar(String label) {
		at.rotate(UtilsMath.PI_ON_TWO);
		initRaster(defaultBarSize);		
		setRaster(UtilsImage.generateClone(progressBarRaster));
		this.label = label;
		showLabel = true;
		initButton();
		updateGraphics();
	}
	
	public GUIScrollBar(Vec2i size) {
		at.rotate(UtilsMath.PI_ON_TWO);
		initRaster(size);
		setRaster(UtilsImage.generateClone(progressBarRaster));
		initButton();
		updateGraphics();
	}
	
	public GUIScrollBar(Vec2i size, Color borderColor, Color barColor) {
		at.rotate(UtilsMath.PI_ON_TWO);
		this.borderColor = borderColor;
		this.barColor = barColor;
		initRaster(size);
		setRaster(UtilsImage.generateClone(progressBarRaster));
		initButton();
		updateGraphics();
	}
	
	public GUIScrollBar(Vec2i size, Color borderColor, Color barColor, Color buttonColor) {
		at.rotate(UtilsMath.PI_ON_TWO);
		this.borderColor = borderColor;
		this.barColor = barColor;
		this.buttonColor = buttonColor;
		initRaster(size);
		setRaster(UtilsImage.generateClone(progressBarRaster));
		initButton();
		updateGraphics();
	}

	
	//--------
	//-------------- ScrollBar init -----------------
	//--------
	private void initRaster(Vec2i size) {		
		switch(scrollAxis) {
		case HORIZONTAL: progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE); break;
		case VERTICAL:   progressBarRaster = new BufferedImage(size.y, size.x, WarpedProperties.BUFFERED_IMAGE_TYPE); break;
		default: Console.err("GUIScrollBar -> initRaster () -> invalid Case : " + scrollAxis); break;
		}
	}
	
	private void initButton() {
		switch(scrollAxis) {
		case HORIZONTAL: buttonSize = new Vec2i((int)(size.x / buttonScale) - borderThickness * 2, (int)(size.y) - borderThickness * 2); break;
		case VERTICAL:   buttonSize = new Vec2i((int)(size.x) - borderThickness * 2, (int)(size.y / buttonScale) - borderThickness * 2); break;
		default:
			Console.err("GUIScrollBar -> initButton() -> invalid case : " + scrollAxis);
			break;
		}
		buttonPosition = new Vec2i((int)((size.x / 2) - (buttonSize.x / 2)), (int)((size.y / 2) - (buttonSize.y / 2)));
	}	

	
	//--------
	//-------------- Access -----------------
	//--------
	public void setScrollAxis(AxisType scrollAxis) {
		this.scrollAxis = scrollAxis;		
		initRaster(new Vec2i(size.x, size.y));
		setRaster(UtilsImage.generateClone(progressBarRaster));
		initButton();
		updateGraphics();
	}
	public void setScrollAction(WarpedAction scrollAction) {this.scrollAction = scrollAction;}
	public double getProgress() {return progress;}	
	
	public void setFullColor(Color progressColor) {this.progressColor = progressColor; updateGraphics();}
	public void setEmptyColor(Color barColor) {this.barColor = barColor; updateGraphics();}
	
	
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	public void resetProgress() {
		progress = 0.0;
		updateGraphics();
	}
	
	public void setProgress(double progress) {
		if(progress < 0 || progress > 1.0) {
			Console.err("GUIProgressBar -> setProgress() -> the domain of progress is 0.0 -> 1.0 inclusive, the progress value : " + progress + " is outside the domain");
			return;
		}
		this.progress = progress;
		progressText = "" + progress;
		updateButtonPosition();
		updateGraphics();
	}
	
	public void increaseProgress() {
		progress += 0.01;
		if(progress > 1.0) progress = 1.0;
		progressText = "" + UtilsMath.round(progress, 2);
		updateButtonPosition();
		updateGraphics();
	}
	
	public void decreaseProgress() {
		progress -= 0.01;
		if(progress < 0.0) progress = 0.0;
		progressText = "" + UtilsMath.round(progress, 2);
		updateButtonPosition();
		updateGraphics();
	}
	
	
	//--------
	//-------------- Update -----------------
	//--------
	private void updateButtonPosition() {
		switch(scrollAxis) {
		case HORIZONTAL: buttonPosition.x = (int)((size.x - buttonSize.x - borderThickness) * progress); break;
		case VERTICAL:   buttonPosition.y = (int)((size.y - buttonSize.y - borderThickness) * progress); break;
		default: Console.err("GUIScrollBar -> updateButtonPosition() -> invalid switch case : " + scrollAxis); return;
		}
	}

	private void updateProgress() {
		switch(scrollAxis) {
		case HORIZONTAL: progress = (buttonPosition.x - borderThickness) / (size.x - buttonSize.x - borderThickness * 2); break;
		case VERTICAL: 	 progress = 1.0 - ((buttonPosition.y - borderThickness) / (size.y - buttonSize.y - borderThickness * 2)); break;
		default: Console.err("GUIScrollBar -> updateProgress() -> invalid switch case : " + scrollAxis);	return;		
		}
		progressText = UtilsString.convertDoubleToString(progress, 2);
	}
	
	private void updateGraphics() {
		Graphics2D g = progressBarRaster.createGraphics();
		g.setColor(borderColor);
		g.fillRect(0, 0, (int)size.x, (int)size.y);
			
		int x2 = (int)(size.x - borderThickness * 2);
		int y2 = (int)(size.y - borderThickness * 2);
		
		g.setColor(barColor);
		g.fillRect(borderThickness, borderThickness, x2, y2);
		
		if(isProgressVisible) {
			g.setColor(progressColor);
			if(scrollAxis == AxisType.VERTICAL) g.fillRect(borderThickness, (buttonPosition.y + buttonSize.y), x2, (int)(size.y - (buttonPosition.y + buttonSize.y) - (borderThickness * 2)));				
			else g.fillRect(borderThickness, borderThickness, (int)(buttonPosition.x), y2);			
		}
		
		if(showLabel) {
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
		
		if(isDragging) g.setColor(buttonDragColor);
		else if(isHovered)g.setColor(buttonHoverColor); 
		else g.setColor(buttonColor);
		g.fillRect(buttonPosition.x, buttonPosition.y, buttonSize.x, buttonSize.y);
		
		if(showProgressText) {
			g.setColor(progressTextColor);
			g.setFont(progressFont);
			g.drawString(progressText, buttonPosition.x + textOffset, buttonPosition.y + textOffset + progressFont.getSize());			
		}
		
		g.dispose();
		paintRaster(progressBarRaster);
	}
	
	@Override
	protected void updateRaster() {return;}
	@Override
	protected void updateObject() {
		if(!WarpedMouse.isDragging() && !WarpedMouse.isPressed()) {				
			if(isDragging) {
				isDragging = false;
				WarpedMouse.unfocus();
				enteredDragging = false;
				updateGraphics();
			}
		}
		if(isDragging) {
			switch(scrollAxis) {
			case HORIZONTAL:
				buttonPosition.x = (int)(WarpedMouse.getPoint().x - position.x - dragOffset.x);
				if(buttonPosition.x < borderThickness) buttonPosition.x = borderThickness;
				if(buttonPosition.x + buttonSize.x > size.x - borderThickness) buttonPosition.x = (int)(size.x - buttonSize.x - borderThickness); 
				break;
			case VERTICAL:
				buttonPosition.y = (int)(WarpedMouse.getPoint().y - position.y - dragOffset.y);
				if(buttonPosition.y < borderThickness) buttonPosition.y = borderThickness;
				if(buttonPosition.y + buttonSize.y > size.y - borderThickness) buttonPosition.y = (int)(size.y - buttonSize.y - borderThickness);
				break;
			default:
				Console.err("GUIScrollBar -> updatePosition() -> invalid case : " + scrollAxis);
				return;			
			}
			updateProgress();
			if(scrollAction != null) scrollAction.action();
			updateGraphics();	
		}
	}

	@Override
	protected void updatePosition() {return;}

	
	
	//--------
	//------------------- Interaction ---------------------
	//--------
	protected boolean isHit(WarpedMouseEvent mouseEvent) {
		Point p = mouseEvent.getPointRelativeToObject();
		if(p == null) {
			Console.err("GUIScrollBar -> isHit() -> warpedmouseevent.getPointRelativeToObject() returned null");
			return false;
		}
		//Console.ln("GUIScrollBar -> isHit() -> mouseEventPoint : " + buttonP.toString());
		if(p.x > buttonPosition.x && p.y > buttonPosition.y && p.x < buttonPosition.x + buttonSize.x && p.y < buttonPosition.y + buttonSize.y) {
			dragOffset.set(p.x - buttonPosition.x, p.y - buttonPosition.y);
			return true;
		} else return false;
	}
	
	@Override
	protected void mouseEntered() {if(WarpedMouse.isDragging()) enteredDragging = true;}

	@Override
	protected void mouseExited() {
		enteredDragging = false;
		if(isHovered) {
			isHovered = false;
			updateGraphics();
		}
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		boolean result = isHit(mouseEvent);
		//Console.ln("GUIScrollBar -> mouseMoved -> isHit() result : " + result);
		if(result) {
			if(!isHovered) {
				isHovered = true;				
				updateGraphics();
			}
		} else if(isHovered) {
				isHovered = false;
				updateGraphics();
		}
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(isHit(mouseEvent)) {			
			if(!isDragging && !enteredDragging && !WarpedMouse.isFocused()) {
				WarpedMouse.focus();
				isDragging = true;
			} 
		}
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		if(isHit(mouseEvent)) {			
			if(!isDragging && !enteredDragging && !WarpedMouse.isFocused()) {
				WarpedMouse.focus();
				isDragging = true;
			} 
		}
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		enteredDragging = false;
		isDragging = false;
		updateGraphics();
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		if(0 + mouseEvent.getWheelEvent().getWheelRotation() > 0) increaseProgress();
		else decreaseProgress();
		if(scrollAction != null) scrollAction.action();
	}
	
}
