/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.UtilsMath;

public abstract class WarpedSprite {

	private Graphics2D g2d;
	private Graphics g;

	private Vec2i size = new Vec2i();

	protected BufferedImage raster;
	
	public BufferedImage raster() {return raster;}
	public abstract void update();  		
	
	public void setRaster(BufferedImage raster) {
		this.raster = raster;
		size.x = raster.getWidth();
		size.y = raster.getHeight();
	}
	
	public Graphics getRasterGraphics() {return raster.getGraphics();}
	public Graphics2D getRasterGraphics2D() {return raster.createGraphics();}
	
	public Vec2i getSize() {return size;}
	public int getWidth() {return size.x;}
	public int getHeight() {return size.y;}
	
	public void clearRaster() {
		g2d = raster.createGraphics();
		g2d.setComposite(UtilsMath.clearComposite);
		g2d.fillRect(0, 0, size.x, size.y);
		g2d.dispose();
	}
	
	public void fillRaster() {
		g = raster.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, size.x, size.y);
		g.dispose();
	}
	
	public void fillRaster(Color color) {
		g = raster.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, size.x, size.y);
		g.dispose();
	}
	
	public void fillRect(Color color, int x, int y, int width, int height) {
		g = raster.getGraphics();
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.dispose();
	}
	
	public void drawRaster(BufferedImage image, boolean scale) {
		g = raster.getGraphics();
		if(scale) g.drawImage(image, 0, 0, size.x, size.y, null);
		else g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		g.dispose();	
	}
	
	public void drawRaster(WarpedSprite sprite, boolean scale) {
		g = raster.getGraphics();
		if(scale) g.drawImage(sprite.raster, 0, 0, size.x, size.y, null);
		else g.drawImage(sprite.raster, 0, 0, sprite.raster.getWidth(), sprite.raster.getHeight(), null);
		g.dispose();	
	}
	
}
