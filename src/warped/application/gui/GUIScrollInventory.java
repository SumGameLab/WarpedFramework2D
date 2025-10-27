/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.application.state.WarpedObjectIdentity;
import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.utils.FontMatrix.FontStyleType;

public class GUIScrollInventory extends WarpedGUI {
	
	/*
	private Color backgroundColor 		 = new Color(60,60,60);
	private Color scrollBarColor 		 = new Color(120, 120, 120);
	private Color scrollButtonPlainColor = new Color(90, 90, 90);
	private Color scrollButtonHoverColor = new Color(150, 90, 90);
	private Color scrollButtonPressColor = new Color(210, 90, 90);
	private Color titleTextColor 		 = Color.YELLOW;
	private Color titleBackgroundColor	 = Color.DARK_GRAY;
	private Color iconBackgroundColor	 = Color.DARK_GRAY;
	
	private boolean isIconBackgroundVisible = true;
	private boolean isTitleBackgroundVisible = true;
	
	private String titleText = "default Title";
	private int titleBackgroundSize = 30;
	private Vec2i titleTextOffset = new Vec2i(titleTextSize, titleTextSize);
	
	private int iconSize = 100;
	private int titleTextSize 	 	= 16;
	 */
	private Font titleFont = fontMatrix.getDynamic();
	private FontStyleType fontStyle = FontStyleType.REGULAR;
	
	private BufferedImage inventory;

	private ArrayList<WarpedObjectIdentity> inventoryObjects = new ArrayList<>(); 
	
	public void openInventory(ArrayList<WarpedObjectIdentity> inventoryObjects) {
		this.inventoryObjects = inventoryObjects;
		
	}

	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		titleFont = fontMatrix.getDynamic(fontStyle, titleFont.getSize());
	}
	
	public void setTitleTextSize(int titleTextSize) {
		titleFont = fontMatrix.getDynamic(titleTextSize);		
	}
	
	
	
	public void updateGraphics() {
		
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
	public void updateObject() {return;	}

	
	
	
}
