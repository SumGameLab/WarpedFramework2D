/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.util.HashMap;

import warped.user.mouse.WarpedMouseEvent;

public class GUICompendium extends WarpedGUI {


	/**IMPLEMENT GUICompendium
	 * Should be an interface for any number of compendiumPages,
	 * each page should have any key word for pages hash map act as hyperlink
	 * i.e. if there exist a page in the compendium called 'dogs' any instance of 'dogs' text should be a clickable area that opens the 'dogs' compendium page
	 * */
	
	private HashMap<String, GUICompendiumPage> pages = new HashMap<>();

	
	
	protected boolean containsKey(String key) {if(pages.containsKey(key)) return true; else return false;}


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


	@Override
	protected void updateRaster() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void updateObject() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub
		
	}
	
}
