/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIProgressBar extends WarpedGUI {

	private DirectionType fillDirection = DirectionType.RIGHT;
	
	private String text		 = "default";
	private Vec2i textOffset = new Vec2i(5, 5);
	private Color textColor  = Color.WHITE;
	private Font  textFont 	 = UtilsFont.getPreferred();
	
	private double progress		= 0.0;
	private int borderThickness = 3;
	
	AffineTransform at 			= new AffineTransform();
	
	private Color emptyColor  = Color.DARK_GRAY;
	private Color fullColor   = Color.GREEN;
	private Color borderColor = Color.BLACK;
	
	private BufferedImage progressBarRaster;
	private Vec2i defaultBarSize = new Vec2i(150, 30);
	
	public GUIProgressBar() {
		at.rotate(UtilsMath.PI_ON_TWO);
		progressBarRaster = new BufferedImage(defaultBarSize.x, defaultBarSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);		
		setRaster(progressBarRaster);
		updateGraphics();
	}
	
	public GUIProgressBar(String label) {
		this.text = label;
		at.rotate(UtilsMath.PI_ON_TWO);
		progressBarRaster = new BufferedImage(defaultBarSize.x, defaultBarSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);		
		setRaster(progressBarRaster);
		updateGraphics();
	}
	
	public GUIProgressBar(Vec2i size) {
		at.rotate(UtilsMath.PI_ON_TWO);
		progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		updateGraphics();
	}
	
	public GUIProgressBar(Vec2i size, Color fullColor) {
		at.rotate(UtilsMath.PI_ON_TWO);
		progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		this.fullColor = fullColor;
		updateGraphics();
	}
	
	public GUIProgressBar(Vec2i size, Color fullColor, DirectionType fillDirection) {
		at.rotate(UtilsMath.PI_ON_TWO);
		progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		this.fullColor = fullColor;
		setFillDirection(fillDirection);
		updateGraphics();
	}
	
	public GUIProgressBar(int width, int height, Color fullColor, DirectionType fillDirection) {
		at.rotate(UtilsMath.PI_ON_TWO);
		progressBarRaster = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		this.fullColor = fullColor;
		setFillDirection(fillDirection);
		updateGraphics();
	}
	
	public GUIProgressBar(Vec2i size, Color borderColor, Color emptyColor, Color fullColor) {
		at.rotate(UtilsMath.PI_ON_TWO);
		this.borderColor = borderColor;
		this.emptyColor  = emptyColor;
		this.fullColor   = fullColor;
		progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		updateGraphics();
	}
	
	public GUIProgressBar(Vec2i size, Color borderColor, Color emptyColor, Color fullColor, DirectionType fillDirection) {
		at.rotate(UtilsMath.PI_ON_TWO);
		this.borderColor = borderColor;
		this.emptyColor  = emptyColor;
		this.fullColor   = fullColor;
		setFillDirection(fillDirection);
		progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		updateGraphics();
	}
	
	public GUIProgressBar(Vec2i size, Color borderColor, Color emptyColor, Color fullColor, String text) {
		at.rotate(UtilsMath.PI_ON_TWO);
		this.borderColor = borderColor;
		this.emptyColor  = emptyColor;
		this.fullColor   = fullColor;
		this.text = text;
		progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		updateGraphics();
	}
	
	public GUIProgressBar(Vec2i size, Color borderColor, Color emptyColor, Color fullColor, int borderThickness) {
		at.rotate(UtilsMath.PI_ON_TWO);
		this.borderColor = borderColor;
		this.emptyColor  = emptyColor;
		this.fullColor   = fullColor;
		this.borderThickness = borderThickness;
		progressBarRaster = new BufferedImage(size.x, size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(progressBarRaster);
		updateGraphics();
	}
	
	public void setText(String text) {
		if(text == "" || text == null) {
			Console.err("GUIProgressBar -> setText() -> tried to set the text with null text");
			return;
		}
		this.text = text;
		updateGraphics();
	}
	
	public void clearText() {
		text = "";
		updateGraphics();
	}

	public void setTextOffset(Vec2i textOffset) {
		this.textOffset = textOffset;
		updateGraphics();
	}
	
	
	public void setTextSize(int textSize) {
		textFont = UtilsFont.getPreferred();
		updateGraphics();
	}
	
	public void setTextStyle(int textStyle) {
		textFont = UtilsFont.getPreferred(textStyle, UtilsFont.getPreferredSize());
		updateGraphics();
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	public void setFillDirection(DirectionType fillDirection) {
		if(fillDirection == DirectionType.UP_LEFT   ||
		   fillDirection == DirectionType.UP_RIGHT  ||		
		   fillDirection == DirectionType.DOWN_LEFT ||
		   fillDirection == DirectionType.DOWN_RIGHT) {
			Console.err("GUIProgressBar -> setFillDirection() -> tried to set fill to an invalid direction. ONLY use single directions i.e. up, down, left, right not : " + fillDirection);
			return;
		}		
		this.fillDirection = fillDirection;
	}
	
	
	
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
		updateGraphics();
	}
	
	public void increaseProgress() {
		progress += 0.01;
		if(progress > 1.0) progress = 1.0;
		updateGraphics();
	}
	public void decreaseProgress() {
		progress -= 0.01;
		if(progress < 0.0) progress = 0.0;
		updateGraphics();
	}
	
	public void updateGraphics() {
		Graphics2D g2d = progressBarRaster.createGraphics();
		
		g2d.setColor(borderColor);
		g2d.fillRect(0, 0, defaultBarSize.x, defaultBarSize.y);

		int x1 = borderThickness;
		int y1 = borderThickness;
		int x2 = (int)(size.x - borderThickness * 2);
		int y2 = (int)(size.y - borderThickness * 2);
		
		g2d.setColor(emptyColor);
		g2d.fillRect(x1, y1, x2, y2);
		
		if(progress > 0.01) {			
			g2d.setColor(fullColor);
			switch(fillDirection) {
			case DOWN:  g2d.fillRect(x1, y1, x2, (int)(y2 * progress)); break;
			case LEFT:  g2d.fillRect(x2 - (int)(x2 * progress), y1, x2, y2); break;  
			case RIGHT: g2d.fillRect(x1, y1, (int)(x2 * progress), y2); break;
			case UP:    g2d.fillRect(x1, y2 - (int)(y2 * progress), x2, y2); break;
			default:
				Console.err("GUIProgressBar -> updateGraphics() -> invalid case, use only single directions not : " + fillDirection);
				break;
			}
		}
		
		
		g2d.setColor(textColor);
		g2d.setFont(textFont);
		if(fillDirection == DirectionType.LEFT || fillDirection == DirectionType.RIGHT) {
			if(text != "" && text != null) g2d.drawString(text, textOffset.x, textFont.getSize()+ textOffset.y);
		} else if(fillDirection == DirectionType.UP || fillDirection == DirectionType.DOWN) {
			if(text != "" && text != null) {
				FontRenderContext frc = g2d.getFontRenderContext();
				Font f = textFont.deriveFont(at);
				TextLayout tString = new TextLayout(text, f, frc);
				tString.draw(g2d, borderThickness * 2, (borderThickness * 2) + textFont.getSize());
			}
		} else { Console.err("GUIProgressBar -> updateGraphics() -> invalid fill dire	ction type "); return;}
		
		g2d.dispose();
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
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

}
