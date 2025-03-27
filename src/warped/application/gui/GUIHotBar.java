/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.WarpedProperties;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.KeyboardIcons;
import warped.user.keyboard.WarpedKeyBind;
import warped.user.keyboard.WarpedKeyboardController;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class GUIHotBar extends WarpedGUI {

	private int hoverX, hoverY = hoverX = -1;
	
	private Color backgroundColor = Colour.GREY_DARK.getColor();
	private Color borderColor = Color.BLACK;
	private Color hoverColor = new Color(60,60,60,60);
	private Color bindColor = new Color(120, 0, 0, 60);
	private int borderThickness = 2;
	
	private VectorI buttonSize = new VectorI(50, 50);
	private VectorI keySize = new VectorI(30, 30);
	private int buttonPaddingX = 4;
	private int innerPadding = 8;
	private int buttonPaddingY = 4;
	
	private int columns = 4;
	
	WarpedKeyboardController controller;
	
	public void selectController(WarpedKeyboardController controller) {
		this.controller = controller;
		updateGraphics();
	}
	
	public void clearBinds() {
		controller.clearHotBinds();
		updateGraphics();
	}
	
	public void updateGraphics() {
		ArrayList<WarpedKeyBind<?>> hotBinds = controller.getHotBinds();

		int rows = (int) Math.ceil(((double)hotBinds.size() / 10.0));
		if(rows == 0) rows = 1;
		Console.ln("GUIHotBar -> updateGraphics() -> hotBar size (row, column) : (" + rows + ", " + columns);
		
		int sx = buttonPaddingX + (hotBinds.size() + 1) * (buttonSize.x() + keySize.x() + innerPadding + buttonPaddingX);
		int sy = buttonPaddingY + rows * (buttonSize.y() + buttonPaddingY);
		Console.ln("GUIHotBar -> updateGraphics() -> hotBar size (pixel width, pixel height) : (" + sx + ", " + sy);
		
		BufferedImage img = new BufferedImage(sx, sy, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		g.setColor(borderColor);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(backgroundColor);
		g.fillRect(buttonPaddingX, buttonPaddingY, img.getWidth() - buttonPaddingX * 2, img.getHeight() - borderThickness * 2);
		
		
		
		for(int i = 0; i <= hotBinds.size(); i++) {
			int y = rows - 1;
			int x = i - y;
			
			int rx =  buttonPaddingX + x * (buttonSize.x() + keySize.x() + innerPadding + buttonPaddingX);
			int ry =  buttonPaddingY + y * (buttonSize.y() + buttonPaddingY);
			Console.ln("GUIHotBar -> updateGraphics() -> drawing binds rx, ry : " + rx + ", " + ry);
			
			int bx = rx + keySize.x() + innerPadding;
			
			
			if(i == hotBinds.size()) {
				g.drawImage(WarpedKeyBind.getKeyImage(-1), rx, ry, rx + keySize.x(), ry + keySize.y(), null);
				g.drawImage(FrameworkSprites.getKeyboardIcon(KeyboardIcons.ARROW_UP), rx, ry, buttonSize.x(), buttonSize.y(), null); 
			} else {
				WarpedKeyBind<?> bind = hotBinds.get(i);
				g.drawImage(WarpedKeyBind.getKeyImage(bind.getKey()), rx, ry, keySize.x(), keySize.y(), null);
				if(bind.isListening()) {
					g.setColor(bindColor);
					g.fillRect(rx, ry, keySize.x(), keySize.y());
				}
				g.drawImage(bind.raster(), bx, ry, buttonSize.x(), buttonSize.y(), null);
			}
			
			if(x == hoverX && y == hoverY) {
				g.setColor(hoverColor);
				g.fillRect(bx, ry,  bx + buttonSize.x(), ry + buttonSize.y());
			}	
		}
		
		setRaster(img);
		g.dispose();
	}

	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
		hoverX = hoverY = -1;
		updateGraphics();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int hx = Math.floorDiv(mouseEvent.getPointRelativeToObject().x, buttonSize.x() + buttonPaddingX);
		int hy = Math.floorDiv(mouseEvent.getPointRelativeToObject().y, buttonSize.y() + buttonPaddingY);
		
		if(hx < 0 || hy < 0 || hx >= columns) return;
		
		int index = hx + hy * columns;
		if(index >=  controller.getHotBindCount()) return;
				
		if(hoverX != hx || hoverY != hy) {
			hoverX = hx;
			hoverY = hy;
			updateGraphics();
		}
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		/*
		if(WarpTech.mouseController.isDraggingModule()) {
			WarpedKeyBind<SpaceShipSystemModule> bind = new WarpedKeyBind<>("Trigger", WarpTech.mouseController.getSystemModule());
			bind.setSprite(WarpTech.mouseController.getSystemModule().getModuleRaster());
			bind.setActions(null, () -> {bind.getBound().trigger();});
			controller.addHotBind(bind);
			WarpedKeyboard.speak();
			updateGraphics();
		}
		*/
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


	@Override
	public void updateObject() {return;}

		
	
	
	
	
}
