/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import warped.WarpedProperties;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIRadioButtons extends WarpedGUI {

	public enum StyleType {
		CLASSIC,
		CHECK_BOX,
	}
	private StyleType style = StyleType.CLASSIC;
	private AxisType axis = AxisType.VERTICAL;

	private ArrayList<Boolean> buttons = new ArrayList<>();
	private ArrayList<String> buttonNames = new ArrayList<>();
	private ArrayList<WarpedAction> buttonActions = new ArrayList<>();
	
	private Vec2i buttonSize = new Vec2i(30, 30);
	private Vec2i radioSize = new Vec2i(200, 200);
	private Vec2i textOffset = new Vec2i(5);
	
	private int buttonPadding = 5; 
	
	private static BufferedImage selectedImage;
	private static BufferedImage unselectedImage;
	//--------
	//---------------- Constructor --------
	//--------
	public GUIRadioButtons(List<WarpedAction> buttonActions) {		
		Console.ln("GUIRadioButtons -> Constructor() -> construction with " + buttonActions.size() + " buttons");
		for(int i = 0 ; i < buttonActions.size(); i++) {
			if(i == 0) buttons.add(true);
			else buttons.add(false);
			this.buttonNames.add("");
			this.buttonActions.add(buttonActions.get(i));
		}
		
		setStyleType(StyleType.CLASSIC);
		updateRadioSize();
		updateGraphics();
	}
	
	public GUIRadioButtons(List<String> buttonNames, List<WarpedAction> buttonActions) {
		if(buttonNames.size() != buttonActions.size()) {
			Console.err("GUIRadioButtons -> constructor() -> number of names doesn't match number of actions");
			return;
		}
		
		for(int i = 0 ; i < buttonNames.size(); i++) {
			if(i == 0) buttons.add(true);
			else buttons.add(false);
			this.buttonNames.add(buttonNames.get(i));
			this.buttonActions.add(buttonActions.get(i));
		}
		setStyleType(StyleType.CLASSIC);
		updateRadioSize();
		updateGraphics();
	}
	
	public GUIRadioButtons(StyleType style, List<String> buttonNames, List<WarpedAction> buttonActions) {
		if(buttonNames.size() != buttonActions.size()) {
			Console.err("GUIRadioButtons -> constructor() -> number of names doesn't match number of actions");
			return;
		}
		
		this.style = style;
		for(int i = 0 ; i < buttonNames.size(); i++) {
			if(i == 0) buttons.add(true);
			else buttons.add(false);
			this.buttonNames.add(buttonNames.get(i));
			this.buttonActions.add(buttonActions.get(i));
		}
		
		setStyleType(style);
		updateRadioSize();
		updateGraphics();
	}
	
	
	//--------
	//---------------- Access --------
	//--------
	public void setRadioSize(int width, int height) {radioSize.set(width, height);}
	public void setRadioSize(Vec2i radioSize) {this.radioSize.set(radioSize);}
	public void setAxis(AxisType axis) {this.axis = axis;}
	public void setButtonPadding(int buttonPadding) {this.buttonPadding = buttonPadding;}
	public void setSelectButton(int index) {
		if(buttons.size() == 0) {
			Console.err("GUIRadioButtons -> setSelectButton() -> radio buttons have not been initialised");
			return;
		}
		if(index < 0 || index >= buttons.size()) {
			Console.err("GUIRadioButtons -> setSelectButton() -> index is out of bounds : " + index);
			return;
		}
		for(int i = 0; i < buttons.size(); i++) buttons.set(i, false);
		buttons.set(index, true);
	}
	
	public int getSelectedButtonIndex() {
		for(int i = 0; i < buttons.size(); i++) if(buttons.get(i) == true) return i;
		Console.err("GUIRadioButton -> getSelectedButtonIndex() -> No button is selected");
		return -1;
	}
	
	public void setStyleType(StyleType style) {
		this.style = style;
		switch(style) {
		case CHECK_BOX:
			selectedImage   = FrameworkSprites.standardIcons.getSprite(3, 26);
			unselectedImage = FrameworkSprites.standardIcons.getSprite(3, 27);
			break;
		case CLASSIC:
			selectedImage   = FrameworkSprites.standardIcons.getSprite(4, 26);
			unselectedImage = FrameworkSprites.standardIcons.getSprite(4, 27);
			break;
		default:
			Console.err("GUIRadioButtons -> setStyleType() -> invalid case : " + style);
			break;
		}
	}

	//--------
	//---------------- Update --------
	//--------
	private void updateRadioSize() {
		if(axis == AxisType.HORIZONTAL) size.x = (buttonSize.x + buttonPadding) * buttons.size();  
		else size.y = (buttonSize.y + buttonPadding) * buttons.size();		
	}
	
	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

	
	//--------
	//---------------- Graphics --------
	//--------
	public void updateGraphics() {
		BufferedImage img = new BufferedImage(radioSize.x, radioSize.y, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		for(int i = 0; i < buttons.size(); i++) {
			
			int buttonX = 0;
			int buttonY = 0;
			
			if(axis == AxisType.HORIZONTAL) buttonX = i * (buttonSize.x + buttonPadding);    				
			else buttonY = i * (buttonSize.y + buttonPadding);
			
			if(buttons.get(i)) g.drawImage(selectedImage, buttonX, buttonY, buttonSize.x, buttonSize.y, null);
			else g.drawImage(unselectedImage, buttonX, buttonY, buttonSize.x, buttonSize.y, null); 
			
			g.setColor(UtilsFont.getPreferredColor());
			g.setFont(UtilsFont.getPreferred());
			g.drawString(buttonNames.get(i), buttonX + buttonSize.x + textOffset.x, buttonY + UtilsFont.getPreferredSize() + textOffset.y);	
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
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		
		int index = -1;
		
		if(axis == AxisType.HORIZONTAL && mouseEvent.getPointRelativeToObject().getY() < buttonSize.y) index = UtilsMath.floor(mouseEvent.getPointRelativeToObject().getX() / (buttonSize.x + buttonPadding));
		else index = UtilsMath.floor(mouseEvent.getPointRelativeToObject().getY() / (buttonSize.y + buttonPadding));
		
		if(index < 0 || index >= buttons.size()) return;
		for(int i = 0; i < buttons.size(); i++) buttons.set(i, false);
		buttons.set(index, true);
		buttonActions.get(index).action();
		updateGraphics();
		Console.ln("GUIRadioButtons -> mouseReleased() -> radio buttons set : " + index);
		
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	
	
	
}
