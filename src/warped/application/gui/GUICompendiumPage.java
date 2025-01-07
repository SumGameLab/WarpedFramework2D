/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.utils.Console;

public class GUICompendiumPage extends WarpedGUI {
		
		private GUICompendium parent;
		private String pageName;
		private String pageContents;
		
		public GUICompendiumPage(GUICompendium parent, String pageName, String pageContents) {
			if(parent.containsKey(pageName)) {
				Console.err("GUICompendium -> CompendiumPage -> constructor() -> compendium already contains a page with this name : " + pageName);
			
			}
			this.parent = parent;
			this.pageName = pageName;
			this.pageContents = pageContents;
		}

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
