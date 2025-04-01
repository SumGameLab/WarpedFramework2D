/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.image.BufferedImage;

import warped.functionalInterfaces.WarpedAction;
import warped.utilities.math.vectors.VectorD;

public interface GUIDrawable {

	boolean isAlive();
	VectorD getMapPosition();
	BufferedImage getMapIcon();
	BufferedImage getMapIconSelected();
	WarpedAction selectAction();
	
	default boolean selectIfHit(int x, int y, int sx, int sy) {
		if(isHit(x, y, sx, sy)) {
			selectAction().action();
			return true;
		} else return false;
	}
	
	default boolean isHit(int x, int y, int sx, int sy) {
		double mx = getMapPosition().x();
		double my = getMapPosition().y();

		if(x > mx && y > my && x < mx + sx && y < my + sy) return true; else return false;
	}
	
}
