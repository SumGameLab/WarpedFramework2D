/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.WarpedProperties;
import warped.graphics.sprite.RotationSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class GUIDial extends WarpedGUI {
	
	private boolean isBackgroundVisible     = false;
	private boolean isFaceBackgroundVisible = true;
	private boolean isFaceForegroundVisible = false;
	private boolean isFaceVisible = true;
	private boolean isValueVisible = true; // the rotation of the hand at index 0 will be displayed in the dial face
	
	private Color backgroundColor		= Color.BLACK;
	private Color faceBackgroundColor	= new Color(0, 255, 80, 50);
	private Color faceForegroundColor	= new Color(160, 50, 50, 100);
	private Color valueColor 			= Color.YELLOW;
	
	private String value = "";
	private Vec2i valueOffset = new Vec2i();
	
	private RotationSprite faceSprite   = FrameworkSprites.dialIcons.generateRotationSprite(0, 0);
	private ArrayList<RotationSprite> handSprites = new ArrayList<>();	
	private ArrayList<Double> handRotations = new ArrayList<>();
	private ArrayList<Double> previousRotations = new ArrayList<>();
	
	private BufferedImage dial;
	
	public GUIDial() {
		addHand(FrameworkSprites.dialIcons.generateRotationSprite(0, 1));
		initializeRaster();
		initializeValueOffset();
		updateGraphics();
	}
	
	public GUIDial(int width, int height) {
		addHand(FrameworkSprites.dialIcons.generateRotationSprite(0, 1));
		dial = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE));
		initializeValueOffset();
		updateGraphics();
	}
	
	public GUIDial(Color faceBackgroundColor) {
		handSprites.add(FrameworkSprites.dialIcons.generateRotationSprite(0, 1));
		this.faceBackgroundColor = faceBackgroundColor;
		isFaceBackgroundVisible = true;
		initializeValueOffset();
		initializeRaster();
		updateGraphics();
	}
	
	public GUIDial(RotationSprite arrowSprite, RotationSprite faceSprite) {
		if(arrowSprite.raster().getWidth() > size.x || arrowSprite.raster().getHeight() > size.y) {
			Console.err("GUIArrow -> constructor() -> the size of the rotationSprite is larger than the background size");
			return;
		}
		addHand(arrowSprite);
		this.faceSprite = faceSprite;
		initializeValueOffset();
		initializeRaster();
		updateGraphics();
	}
	
	private void initializeValueOffset() {
		valueOffset.set((int)((size.x / 2) - (size.x / 6)), (int)(size.y * 0.75));		
	}
	
	public void addHand(RotationSprite sprite) {
		handSprites.add(sprite);
		handRotations.add(0.0);
		previousRotations.add(0.0);
	}
	
	public void setFaceForegroundColor(Color faceForegroundColor) {
		isFaceForegroundVisible = true;
		this.faceForegroundColor = faceForegroundColor;
		updateGraphics();
	}
	
	public void setFaceBackgroundColor(Color faceBackgroundColor) {
		isFaceBackgroundVisible = true;
		this.faceBackgroundColor = faceBackgroundColor;
		updateGraphics();
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		isBackgroundVisible = true;
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	public void setHandRotation(double rotation) {handRotations.forEach(r -> {r = rotation;});}
	public void setHandRotation(int index, double rotation) {handRotations.set(index, rotation);}
	
	public void setFaceRotation(double rotation) {
		faceSprite.setRotation(rotation);
		updateGraphics();
	}
	
	public double getFaceRotation() {return faceSprite.getRotation();}
	public double getHandRotation(int index) {return handSprites.get(index).getRotation();}
	public double[] getHandRotation() {
		double[] result = new double[handSprites.size()];
		for(int i = 0; i < handSprites.size(); i++) {
			result[i] = handSprites.get(i).getRotation();
		}
		return result;
	}
	
	
	private void initializeRaster() {
		dial = new BufferedImage(faceSprite.getWidth(), faceSprite.getHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(new BufferedImage(faceSprite.getWidth(), faceSprite.getHeight(), WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public void updateGraphics() {
		boolean needsUpdate = false;
		for(int i = 0; i < handRotations.size(); i++) {
			if(handRotations.get(i) != previousRotations.get(i)) {
				previousRotations.set(i, handRotations.get(i));
				handSprites.get(i).setRotation(handRotations.get(i));
				needsUpdate = true;
			}
		}
		if(!needsUpdate) return;
		Graphics2D g2d = dial.createGraphics();
		clear(g2d);
		draw(g2d);
		g2d.dispose();
		paintRaster(dial);
	}
	
	private void clear(Graphics2D g2d) {
		g2d.setComposite(UtilsMath.clearComposite);
		g2d.fillRect(0,0, (int)size.x, (int)size.y);
	}
	
	private void draw(Graphics2D g2d) {
		g2d.setComposite(UtilsMath.drawComposite);		
		
		if(isBackgroundVisible && isFaceVisible) {
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, (int)size.x, (int)size.y);
		}
		
		if(isFaceBackgroundVisible && isFaceVisible) {			
			g2d.setColor(faceBackgroundColor);
			g2d.fillOval(0, 0, (int)size.x, (int)size.y);
		}
		
		if(isFaceVisible)g2d.drawImage(faceSprite.raster(), 0, 0, (int)size.x, (int)size.y, null);
		
		for(int i = 0; i < handSprites.size(); i++) {			
			g2d.drawImage(handSprites.get(i).raster(), 0, 0, (int)size.x, (int)size.y, null);
		}
		
		if(isValueVisible) {
			value = "" + UtilsMath.round(Math.toDegrees(handRotations.get(0)), 2);
			g2d.setColor(valueColor);
			g2d.drawString(value, valueOffset.x, valueOffset.y);
		}
		
		if(isFaceForegroundVisible && isFaceVisible) {
			g2d.setColor(faceForegroundColor);
			g2d.fillOval(0, 0, (int)size.x, (int)size.y);
		}
	}
	
	@Override
	protected void updateRaster() {
		updateGraphics();
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
	protected void updateObject() {return;}
	@Override
	protected void updatePosition() {return;}
	
	
	
}
