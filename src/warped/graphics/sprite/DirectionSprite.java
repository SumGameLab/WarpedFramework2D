/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.image.BufferedImage;

import warped.utilities.utils.Console;

public class DirectionSprite extends WarpedSprite {

	BufferedImage[] directionalImage;
	
	public DirectionSprite(BufferedImage[] directionalImage) {
		if(directionalImage.length > 8 || directionalImage.length < 8) {
			Console.err("ERROR! -> Directional Sprite -> tried to load Buffered Image of size : " + directionalImage.length);
			Console.err("ERROR! -> Directional Sprite -> use propper length array of 8 in format up, down, left, right, up_left, up_right, down_left, down_right");
			return;
		} else this.directionalImage = directionalImage;
	}
	
	//public BufferedImage getImage(MoveDirection dir) {return directionalImage[dir.ordinal()];}

	public void update() {

		
	}
	
}
