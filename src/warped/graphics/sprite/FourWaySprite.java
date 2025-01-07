/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.utils.UtilsMath;

public class FourWaySprite extends WarpedSprite {
	
	private BufferedImage down;
	private BufferedImage right;
	private BufferedImage left;
	private BufferedImage up;
	
	/**Sprites bust be in the order : up, down, right, left*/
	public FourWaySprite(BufferedImage[] sprites) {
		up 	  = sprites[0];
		down  = sprites[1];
		left  = sprites[2];
		right = sprites[3];
	}
	
	/**WarpTech Constructor - very specific format, not for other use*/
	public FourWaySprite(WarpedSpriteSheet heads, WarpedSpriteSheet torso) {
		int sz = 32;
		up 	   = new BufferedImage(sz, sz, WarpedProperties.BUFFERED_IMAGE_TYPE);
		down   = new BufferedImage(sz, sz, WarpedProperties.BUFFERED_IMAGE_TYPE);
		left   = new BufferedImage(sz, sz, WarpedProperties.BUFFERED_IMAGE_TYPE);
		right  = new BufferedImage(sz, sz, WarpedProperties.BUFFERED_IMAGE_TYPE);
		
		int tOffsetX = 4;
		int tOffsetY = 7;
		int tSz 	 = 25;
		
		int hOffsetX = 10;
		int hOffsetY = 0;
		int hSz		 = 13;
		
		Graphics g;
		
		int tIndex = UtilsMath.random(torso.getXcount());
		int hIndex = UtilsMath.random(heads.getXcount());
		
		int dIndex = 0;
		int rIndex = 1;
		int lIndex = 2;
		int uIndex = 3;
		
		//down
		g = down.getGraphics();
		g.drawImage(torso.getSprite(tIndex, dIndex), tOffsetX, tOffsetY, tSz, tSz, null);
		g.drawImage(heads.getSprite(hIndex, dIndex), hOffsetX, hOffsetY, hSz, hSz, null);
		g.dispose();
		//right
		g = right.getGraphics();
		g.drawImage(torso.getSprite(tIndex, rIndex), tOffsetX, tOffsetY, tSz, tSz, null);
		g.drawImage(heads.getSprite(hIndex, rIndex), hOffsetX, hOffsetY, hSz, hSz, null);
		g.dispose();
		//left
		g = left.getGraphics();
		g.drawImage(torso.getSprite(tIndex, lIndex), tOffsetX, tOffsetY, tSz, tSz, null);
		g.drawImage(heads.getSprite(hIndex, lIndex), hOffsetX, hOffsetY, hSz, hSz, null);
		g.dispose();
		//up
		g = up.getGraphics();
		g.drawImage(torso.getSprite(tIndex, uIndex), tOffsetX, tOffsetY, tSz, tSz, null);
		g.drawImage(heads.getSprite(hIndex, uIndex), hOffsetX, hOffsetY, hSz, hSz, null);
		g.dispose();		
	}
	
	@Override
	public void update() {return;}
	
	
	public void up() {raster 	= up;}
	public void down() {raster 	= down;}
	public void left() {raster 	= left;}
	public void right() {raster = right;}
	

	
	
}
