/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import warped.application.object.WarpedObject;
import warped.application.object.WarpedOption;
import warped.application.state.WarpedState;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class WarpedTile extends WarpedObject {

	/**
	 * NOTE : 
	 * For a large game such as WarpTech, there will be several million tiles
	 * Obviously updating 20+ million entities is less than optimal.
	 * The tiles should be kept in an unactive groups and then call updates on them very infrequently or on demand 
	 * */

	protected WarpedTileSet parent; 
	protected BufferedImage tileRaster;
	protected BufferedImage riverRaster;
	
	private double localTime = 1.0; //0.0 = midnight, 0.5 = midday 1.0 = midnight
	protected int index = -1;
	protected Vec2i coordinates = new Vec2i();

	protected boolean isRiver = false;
	protected RiverSegment riverSegment;
	
	protected double temperature = 0.0;
	
	protected TileType primaryType = TileType.VOID;
	protected TileType secondaryType = TileType.VOID;
	protected TileTransitionType transitionType = TileTransitionType.NONE;
	
	
	public WarpedTile(WarpedTileSet parent, TileType primaryType) {
		this.parent = parent;
		this.primaryType = primaryType;
		this.secondaryType = primaryType;
		tileRaster = parent.getTileImage(primaryType);
	    setRaster(tileRaster);
		initializeSelectOptions();
	}
	
	public WarpedTile(WarpedTileSet parent, TileType primaryType, TileTransitionType transitionType) {
		this.parent = parent;
		this.primaryType = primaryType;
		this.secondaryType = parent.getRandomTransition(primaryType);
		this.transitionType = transitionType;
		tileRaster = parent.getTileImage(primaryType);
		setRaster(tileRaster);
		initializeSelectOptions();
	}
	
	public WarpedTile(WarpedTileSet parent, TileType primaryType, TileType secondaryType, TileTransitionType transitionType) {
		this.parent = parent;
		this.primaryType = primaryType;
		this.secondaryType = secondaryType;
		this.transitionType = transitionType;
		tileRaster = parent.getTransitionImage(primaryType, secondaryType, transitionType);
	    setRaster(tileRaster);
		initializeSelectOptions();
	}
	
	//--------
	//------------------- Access ---------------------
	//--------	
	
	public void setRiver(RiverSegment riverSegment) {
		return;
		//TODO fix rivers
		/*
		isRiver = true;
		this.riverSegment = riverSegment;
		riverRaster = parent.getRiverType().getRiverImage(riverSegment.getSegmentType());	
		updateGraphics();
		*/
	}
	
	public void setRiver(DirectionType joiningDirection) {
		return;
		/*
		if(!isRiver) {
			Console.err("Tile -> setRiver() -> tried to join rivers with a tile that is not a river");
			return;
		}
		riverSegment.setSegmentType(RiverSegmentType.getTributarySegment(joiningDirection));
		riverRaster = parent.getRiverType().getRiverImage(riverSegment.getSegmentType());
		updateGraphics();
		*/
		//TODO fix rivers
	}
	
	public double getLocalTime() {return localTime;}
	public double getRoughness() {return TileType.getRoughness(this);}
	public double getTemperature() {return temperature;}
	public double getDayTemperature() {return TileType.getDayTemperature(this);}
	public double getNightTemperature() {return TileType.getNightTemperature(this);}
	public void setLocalTime(double localTime) {this.localTime = localTime;}
	
	public RiverSegment getRiverSegment() {return riverSegment;}
	public RiverSegmentType getRiverSegmentType() {return riverSegment.getSegmentType();}
	public TileType getPrimaryType() {return primaryType;}
	public TileType getSecondaryType() {return secondaryType;}
	public TileTransitionType getTransitionType() {return transitionType;}
	public WarpedTileSet getParent() {return parent;}
	/**the direction of this tile relative to the one in context. i.e. this tile is above the tile being considered -> the dir is UP*/
	public boolean isRiverPossible(DirectionType relativeDir) {
		if(isRiver && riverSegment.getSegmentType().isSegmentPossible(relativeDir)) return true; 
		else if(primaryType.isRiverPossible() && secondaryType.isRiverPossible()) return true; else return false;
	}
	public boolean isRiver() {return isRiver;}
	public boolean isPureTile() {if(primaryType == secondaryType) return true; else return false;}
	public void setTileTransition(TileTransitionType transitionType) {
		tileRaster = parent.getTransitionImage(primaryType, secondaryType, transitionType);
		setRaster(tileRaster);
	}
	
	public void setCoordinates(Vec2i coordinates) {this.coordinates = coordinates;}
	public void setCoordinates(int x, int y, int index) {coordinates.set(x, y); this.index = index;}
	public int getIndex() {return index;}
	public Vec2i getCoords() {return coordinates;}
	
	/**Matching Tiles
	 * Two tiles are matching if they are from the same set, have the same primary and secondary type
	 * isMatching() is not equivalent to isEquals(). If the same tile is passed as tile1 and tile2 parameter isMatching() will return false, the opposite is true for isEquals()*/
	
	
	public boolean isMatching(WarpedTile tile) {return isMatching(this, tile);}
	public static boolean isMatching(WarpedTile tile1, WarpedTile tile2) {
		if(isMatchingSet(tile1, tile2) && isMatchingType(tile1, tile2) && isMatchingShape(tile1, tile2)) return true; else return false;
	}
	
	public boolean isInverseType(WarpedTile tile2) {return isInverseType(this, tile2);}
	public static boolean isInverseType(WarpedTile tile1, WarpedTile tile2) {
		if(tile1.getPrimaryType() == tile2.getSecondaryType() && tile1.getSecondaryType() == tile2.getPrimaryType()) return true; else return false;
	}
	
	public boolean isMatchingType(WarpedTile tile) {return isMatchingType(this, tile);}
	public static boolean isMatchingType(WarpedTile tile1, WarpedTile tile2) {
		if(!isMatchingSet(tile1, tile2)) {
			Console.err("Tile -> isMatchingType() -> tile1 and tile 2 are from different sets, they can't match");
		}
		if(tile1 == null && tile2 == null) {
			Console.err("Tile -> isMathcingType() -> tile1 and tile2 are both null");
			return false;
		}
		if(tile2 == null || tile1 == null) return true;
		if(tile1.getPrimaryType() == tile2.getPrimaryType() && tile1.getSecondaryType() == tile2.getSecondaryType()) return true;
		if(tile1.getPrimaryType() == tile2.getSecondaryType() && tile1.getSecondaryType() == tile2.getPrimaryType()) return true;
		return false;
	}
	
	public boolean isMatchingShape(WarpedTile tile) {return isMatchingShape(this, tile);}
	public static boolean isMatchingShape(WarpedTile tile1, WarpedTile tile2) {
		if(!isMatchingSet(tile1, tile2)) {
			Console.err("Tile -> isMatchingShape() -> tile1 and tile2 are from different sets, they cant' match");
			return false;
		}
		if(tile1.getTransitionType() == tile2.getTransitionType()) return true; else return false;		
	}
	
	public boolean isMatchingSet(WarpedTile tile) {return isMatchingSet(this, tile);}
	public static boolean isMatchingSet(WarpedTile tile1, WarpedTile tile2) {
		if(tile1.isEqual(tile2)) {
			Console.err("Tile -> isMatching() -> tile1 and tile2 are the same tile");
			return false;
		}
		
		if(tile1.parent.isMatchingSet(tile2.parent)) return true; else return false;
	}

	
	//--------
	//------------------- Interaction ---------------------
	//--------
	private void initializeSelectOptions() {
		addSelectOption(new WarpedOption("Inspect", () -> {
			WarpedState.selector.close();
			
			WarpedState.gameObjectInspector.select(objectID);
			WarpedState.gameObjectInspector.open();
		}));
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
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		//WarpedState.selectionManager.select(objectID);
		WarpedState.selector.select(this);
	}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	
	
	//--------
	//------------------- Update ---------------------
	//--------
	protected void updateGraphics() {
		BufferedImage img = UtilsImage.generateClone(tileRaster);
		Graphics g = img.getGraphics();
		if(isRiver)	g.drawImage(riverRaster, 0, 0, (int)size.x, (int)size.y, null);
		g.dispose();
		setRaster(img);
	}
	
	@Override
	protected void updateRaster() {return;}
	@Override
	protected void updateObject() {return;}
	@Override
	protected void updatePosition() {return;}

	protected void updateMid() {return;};
	
	protected void updateSlow() {return;}; 

	protected void updatePassive() {return;};
	
}
