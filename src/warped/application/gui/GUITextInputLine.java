/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.state.WarpedState;
import warped.user.actions.WarpedAction;
import warped.user.keyboard.WarpedKeyboard;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;

public class GUITextInputLine extends WarpedGUI {

	private Color borderColor = Color.BLACK;
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	
	private int borderThickness = 2;
	
	private boolean isInput = false;
	
	private boolean isButtonHovered = false;
	private Color hoverColor = new Color(120, 120, 120, 120);
	private Color inputColor = new Color(100, 0, 0, 120);
	
	private static final int MAX_CHARACTERS = 25;
	
	private Vec2i textLineSize = new Vec2i(250, 46);
	private Vec2i textCompositeSize = new Vec2i();
	private Vec2i buttonSize = new Vec2i();
	private Vec2i symbolProportion = new Vec2i();
	
	private BufferedImage buttonComposite = new BufferedImage(1, 1, WarpedProperties.BUFFERED_IMAGE_TYPE);
	private BufferedImage textComposite = new BufferedImage(1, 1, WarpedProperties.BUFFERED_IMAGE_TYPE);
	
	
	private String inputString = "Click here..";
	
	private Color fontColor = Color.YELLOW;
	private int fontSize = 24;
	private int fontStyle = Font.PLAIN;
	
	private Vec2i textOffset = new Vec2i(fontSize, fontSize + fontSize / 2);
	
	private Font font = new Font(Font.SANS_SERIF, fontStyle, fontSize);
	

	private WarpedAction action = () -> {Console.err("GUITextInputLine -> default action");}; 
	
	public GUITextInputLine() { 
		setTextLineSize(textLineSize.x, textLineSize.y);
	}
	
	
	public void setTextLineSize(int x, int y) {
		textLineSize.set(x, y);
		buttonSize.set(y, y);
		textCompositeSize.set(textLineSize.x - buttonSize.x, textLineSize.y);
		symbolProportion.set((int)buttonSize.x * 0.15, buttonSize.y - (int)buttonSize.x * 0.15);

		updateGraphics();
	}
	
	public void setFont(Font font) {
		this.font = font;
		updateGraphics();
	}
	
	public void setButtonAction(WarpedAction action) {
		this.action = action;
	}
	
	public String getLine() {return inputString;}
	
	
	//--------
	//---------------- Graphics --------
	//--------
	public void setText(String inputString) {
		this.inputString = inputString;
		updateGraphics();
	}
	
	public void updateGraphics() {
		updateComposites();
		drawComposites();
	}
	
	private void updateComposites() {
		//Draw textLine composite
		BufferedImage textComp = new BufferedImage(textCompositeSize.x, textCompositeSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics gt = textComp.getGraphics();
		
		gt.setColor(fontColor);
		gt.setFont(font);
		gt.drawString(inputString, textOffset.x, textOffset.y);
		 
		gt.dispose();
		textComposite = textComp;
		
		
		//Draw button image composite
		BufferedImage buttonComp = new BufferedImage(buttonSize.x, buttonSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics gb = buttonComp.getGraphics();
		gb.setColor(backgroundColor);
		
		gb.setColor(borderColor);
		gb.fillRect(0, 0, buttonSize.x, buttonSize.y);
		
		gb.setColor(backgroundColor);
		gb.fillRect(borderThickness,borderThickness, buttonSize.x - borderThickness * 2,buttonSize.y - borderThickness * 2);
		
		gb.setColor(fontColor);
		gb.fillRect((buttonSize.x / 2) - (symbolProportion.x / 2), (buttonSize.y - symbolProportion.y) / 2, symbolProportion.x, symbolProportion.y);
		gb.fillRect((buttonSize.x - symbolProportion.y) / 2, (buttonSize.y / 2) - (symbolProportion.x / 2), symbolProportion.y, symbolProportion.x);
		
		gb.dispose();
		buttonComposite = buttonComp;
	}
	
	private void drawComposites() {
		BufferedImage img = new BufferedImage(textLineSize.x, textLineSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		g.setColor(borderColor);
		g.fillRect(0, 0, textLineSize.x, textLineSize.y);
		 
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, textLineSize.x - borderThickness * 2, textLineSize.y - borderThickness * 2);
		 
		g.drawImage(textComposite, 0, 0, textCompositeSize.x, textCompositeSize.y, null);
		g.drawImage(buttonComposite, textLineSize.x - buttonSize.x, 0, buttonSize.x, buttonSize.y, null);
		
		if(isButtonHovered) {
			g.setColor(hoverColor);
			g.fillRect(textLineSize.x - buttonSize.x, 0, buttonSize.x, buttonSize.y);
		}
		
		if(isInput) {
			g.setColor(inputColor);
			g.fillRect(0, 0, textLineSize.x - buttonSize.x, textLineSize.y);
		}
	
		g.dispose();
		setRaster(img);
		
	}
	
	//--------
	//---------------- Interaction --------
	//--------	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
		isButtonHovered = false;
		isInput = false;
		WarpedKeyboard.stopKeyLogging();
		WarpedKeyboard.clearKeyLog();
		drawComposites();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		if(mouseEvent.getPointRelativeToObject().x > textLineSize.x - buttonSize.x) {
			if(isButtonHovered) return;
			else {
				isButtonHovered = true;
				drawComposites();
			}
		} else {
			if(!isButtonHovered) return;
			else {
				isButtonHovered = false;
				drawComposites();
			}
		}
			
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) { 
		if(isButtonHovered) {action.action();}
		else {
			isInput = true;
			WarpedKeyboard.startKeyLogging();
			drawComposites();
		}
	}
	
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	
	//--------
	//---------------- Update --------
	//--------
	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {
		if(isInput) {
			if(!WarpedKeyboard.isKeyLogging()) {
				isInput = false;
				inputString = WarpedKeyboard.getKeyLog();
				if(inputString.length() > MAX_CHARACTERS) {		
					WarpedState.notify.addNotification("Label is too large");
					inputString = inputString.substring(0, MAX_CHARACTERS - 1);
				}
				updateGraphics();
				return;
			}
						
			if(!WarpedKeyboard.getKeyLog().equals(inputString)) {
				inputString = WarpedKeyboard.getKeyLog();
				if(inputString.length() > MAX_CHARACTERS) {		
					WarpedState.notify.addNotification("Label is too large");
					inputString = inputString.substring(0, MAX_CHARACTERS - 1);
				}
				updateGraphics();
			}
			
		}
	}

	@Override
	protected void updatePosition() {return;}

}
