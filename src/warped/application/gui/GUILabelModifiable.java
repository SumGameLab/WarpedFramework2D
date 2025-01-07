/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.state.WarpedState;
import warped.user.keyboard.WarpedKeyboard;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUILabelModifiable extends WarpedGUI {

	private static final int CLEAR_LABEL  	= 0;
	private static final int COLOUR_LABEL 	= 1;
	
	private static final int MAX_CHARACTERS = 25;
	
	private int labelType = COLOUR_LABEL;
	
	private String text = "default text";
	
	private int width  = 300;
	private int height = 80;
	
	private boolean isInput = false;
	private boolean isHovered = false;
	
	private Color backgroundColor = Color.DARK_GRAY;
	private Color borderColor   = Color.BLACK;
	private int borderThickness = 2;
	
	private Color textColor  = Color.WHITE;
	private Font font 		 = UtilsFont.getPreferred();
	private Vec2i textOffset = new Vec2i(5, font.getSize() - font.getSize() / 8);
	
	private Color inputColor = Colour.RED_DARK.getColor();
	private Color hoverColor = Color.LIGHT_GRAY;
	
	public GUILabelModifiable() {}
	public GUILabelModifiable(String text) {
		this.text = text;
		updateGraphics();
	}
	
	
	//--------
	//---------------- Access ---------------
	//--------
	public void setText(String text) {
		this.text = text;
		updateGraphics();
	}
	public String getText() {return text;}
	public void setLabelSize(Vec2i vec) {setLabelSize(vec.x, vec.y);}
	public void setLabelSize(int width, int height) {
		this.width = width;
		this.height = height;
		updateGraphics();
	}	
	
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 1 || borderThickness > width / 4 || borderThickness > height / 4) {
			System.err.print("GUILabelModifiable() -> setBorderThickness -> " + borderThickness + " is invalid, borderthickness will be set to 2");
			borderThickness = 2;
		}
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	public void setTextSize(int textSize) {
		font = UtilsFont.getPreferred(textSize);
		updateGraphics();
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	public void setLabelClear() {
		labelType = CLEAR_LABEL;
		updateGraphics();
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor; 
		labelType = COLOUR_LABEL;
		updateGraphics();
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	public void setFont(Font textFont) {
		this.font = textFont;
		updateGraphics();
	}
	
	public void setTextOffset(Vec2i textOffset) {
		if(textOffset.x < 0 || textOffset.y < 0 || textOffset.x > width || textOffset.y > height) {
			Console.err("GUILabelModifiable -> setTextOffset() -> invalid offset, it will be set to 0");
			this.textOffset.set(0);
		} else this.textOffset.set(textOffset);
		updateGraphics();
	}
	
	public void setTextOffset(int x, int y) {
		if(x < 0 || y < 0 || x > width || y > height) {
			Console.err("GUILabelModifiable -> setTextOffset() ->  (" + x + ", " + y + ") is an invalid offset, it will be set to 0");
			x = y = 0;
		}
		if(textOffset.x != x || textOffset.y != y) {		
			textOffset.set(x, y);
			updateGraphics();
		}
	}
	
	//--------
	//---------------- Update ---------------
	//--------
	public void updateGraphics() {
		BufferedImage img = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		if(labelType == CLEAR_LABEL) {
			g.setFont(font);
			g.setColor(textColor);
			g.drawString(text, textOffset.x, textOffset.y);
			
		} else {
			g.setColor(backgroundColor);
			g.fillRect(0, 0, width, height);
			if(isInput) g.setColor(inputColor);
			else if(isHovered) g.setColor(hoverColor);
			else g.setColor(borderColor);
			g.fillRect(borderThickness, borderThickness, width - borderThickness * 2, height - borderThickness * 2);
			g.setColor(textColor);
			g.setFont(font);
			g.drawString(text, textOffset.x, textOffset.y);	
		}
		g.dispose();
		setRaster(img);
	}
	
	@Override
	protected void updateRaster() {return;}
	
	@Override
	protected void updateObject() {
		if(isInput) {
			if(!WarpedKeyboard.isKeyLogging()) {
				isInput = false;
				text = WarpedKeyboard.getKeyLog();
				if(text.length() > MAX_CHARACTERS) {		
					WarpedState.notify.addNotification("Label is too large");
					text = text.substring(0, MAX_CHARACTERS - 1);
				}
				updateGraphics();
				return;
			}
						
			if(!WarpedKeyboard.getKeyLog().equals(text)) {
				text = WarpedKeyboard.getKeyLog();
				if(text.length() > MAX_CHARACTERS) {		
					WarpedState.notify.addNotification("Label is too large");
					text = text.substring(0, MAX_CHARACTERS - 1);
				}
				updateGraphics();
			}
			
		}
	}
	
	@Override
	protected void updatePosition() {return;}
	
	
	//--------
	//---------------- Interaction ---------------
	//--------
	@Override
	protected void mouseEntered() { 
		if(!isHovered) {
			isHovered = true;
			updateGraphics();
		}
		
	}

	@Override
	protected void mouseExited() {
		WarpedKeyboard.stopKeyLogging();
		if(isHovered || isInput) {
			isInput = false;
			isHovered = false;
			updateGraphics();
			return;
		} 
		isInput = false;
		isHovered = false;
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(!isInput) {
			isInput = true;
			updateGraphics();
			WarpedKeyboard.startKeyLogging();
		}
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


}
