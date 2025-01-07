/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsImage;

public class GUILabel  extends WarpedGUI {
	
	private static final double WIDTH_HEIGHT_RATIO = 0.75;
	
	public static final int CLEAR_LABEL  	= 0;
	public static final int COLOUR_LABEL 	= 1;
	public static final int IMAGE_LABEL		= 2;
	
	private int labelType = CLEAR_LABEL;
	private Color labelColor = Color.DARK_GRAY;
	private BufferedImage label;
	
	private String text = "default text";

	private boolean isBorderVisible = false;
	private Color borderColor = Color.BLACK;
	private int borderThickness = 2;
	
	private Color textColor = Color.WHITE;
	private Font textFont = UtilsFont.getPreferred();

	private boolean fixedSize = true;
	
	private Vec2i textOffset = new Vec2i(5, textFont.getSize() - textFont.getSize() / 8);
	
	public GUILabel() {
		label = new BufferedImage(100, 50, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(label);
	}
	
	public GUILabel(int width, int height) {
		label = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(label);
	}
	
	public GUILabel(String text) {
		label = new BufferedImage(textFont.getSize() * text.length(), textFont.getSize(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(label);
		this.text = text;
		updateGraphics();
	}
	
	public void pointToFormat(GUILabel label) {
		this.labelType       = label.labelType;
		this.labelColor      = label.labelColor;
		this.isBorderVisible = label.isBorderVisible;
		this.borderColor     = label.borderColor;
		this.borderThickness = label.borderThickness;
		this.textColor 		 = label.textColor;
		this.textFont 		 = label.textFont;
		this.textOffset 	 = label.textOffset;
		if(label.fixedSize) {
			this.setLabelSize((int)label.getSize().x, (int)label.getSize().x);
		}
	}
	
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		isBorderVisible = true;
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		isBorderVisible = true;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	
	public void setTextStyle(int textStyle) {
		textFont = UtilsFont.getPreferred(textStyle, UtilsFont.getPreferredSize());
	}
	
	public void setTextSize(int textSize) {
		textFont = UtilsFont.getPreferred(textSize);
	}
	
	//private void updateFont() {this.textFont = new Font("labelFont", textStyle, textSize);}
	
	public void setTextOffset(int x, int y) {
		textOffset.x = x;
		textOffset.y = y;
	}
	
	public void setLabelType(int labelType) {
		this.labelType = labelType;
	}
	
	public void setLabelColor(Color labelColor) {
		this.labelColor = labelColor;
		labelType = COLOUR_LABEL;
	}
	
	public void fixedSize() {fixedSize = true;}
	public void dynamicSyze() {fixedSize = false;}
	
	public void setLabelImage(BufferedImage textBoxImage) {
		this.label = textBoxImage;
		labelType = IMAGE_LABEL;
	}
	
	public void setLabelAlpha(int alpha) {
		switch(labelType) {
		case CLEAR_LABEL :
			Console.err("GUIText -> setTextBoxAlpha() -> can't set alpha because of invalid textBoxType : " + labelType);
			return;
		case COLOUR_LABEL :
			labelColor = new Color(labelColor.getRed(), labelColor.getGreen(), labelColor.getBlue(), alpha);
			break;
		case IMAGE_LABEL :
			UtilsImage.setAlpha(label, (byte)alpha);
			break;
			default : 
		}
	}
	
	public void setLabelSize(int width, int height) {
		fixedSize();
		label = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(label);	
	}
	
	private void updateLabelSize() {
		switch(labelType) {
		case CLEAR_LABEL :
		case COLOUR_LABEL :
			setRaster(new BufferedImage((text.length() * (int)(textFont.getSize() * WIDTH_HEIGHT_RATIO)) + textOffset.x, textFont.getSize() + textOffset.y, WarpedProperties.BUFFERED_IMAGE_TYPE));
			break;
		case IMAGE_LABEL:
			break;
		default:
			Console.err("GUILabel -> setTextBoxSize() -> invalid case : " + labelType);
		}
	}
	
	public void updateGraphics() {
		if(!fixedSize) updateLabelSize();
		BufferedImage img = new BufferedImage((int) size.x, (int) size.y, WarpedProperties.BUFFERED_IMAGE_TYPE);;
		Graphics2D g2d = img.createGraphics();
		switch(labelType) {
		case CLEAR_LABEL :
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g2d.fillRect(0,0, (int)size.x, (int)size.y);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			break;
		case COLOUR_LABEL : 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g2d.fillRect(0,0, (int)size.x, (int)size.y);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			if(isBorderVisible) {
				g2d.setColor(borderColor);
				g2d.fillRect(0, 0, (int)size.x, (int)size.y);
				g2d.setColor(labelColor);
				g2d.fillRect(borderThickness, borderThickness, (int)size.x - (borderThickness * 2), (int)size.y - (borderThickness * 2));
			} else {				
				g2d.setColor(labelColor);
				g2d.fillRect(0, 0, (int)size.x, (int)size.y); 
			}
			break;
		case IMAGE_LABEL : 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g2d.fillRect(0,0, (int)size.x, (int)size.y);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g2d.drawImage(label, 0, 0, (int)size.x, (int)size.y, null);
			break;
		default :
			Console.err("GUIText -> drawBackground() -> invalid case : " + labelType);
			return;
		}
		
		g2d.setColor(textColor);
		g2d.setFont(textFont);
		g2d.drawString(text, textOffset.x, textOffset.y);

		g2d.dispose();
		setRaster(img);
	}
	
	protected void mouseEntered() {return;}
	protected void mouseExited() {return;}
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	protected void updateRaster() {return;}
	protected void updateObject() {return;}
	protected void updatePosition() {return;}
	
	
}
