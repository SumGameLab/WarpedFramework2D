package warped.application.tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.application.entities.item.ResourceType;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class TerrainTile<T extends WarpedTileable<? extends Enum<?>>> extends WarpedTile<T> {

	protected T secondaryType;
	protected TerrainTileTransitionType transitionType = TerrainTileTransitionType.NONE;
	
	protected BufferedImage tileRaster;
	protected BufferedImage riverRaster;
	protected boolean isRiver = false;
	protected RiverSegment riverSegment;
	
	
	private static BufferedImage surveyRaster;
	
	private boolean isSurveyed	 = false; //once a tile has been surveyed it will be revealed 
	private boolean isProspected = false; // once a till has been prospected no further resources can be discovered on it 
	
	boolean isConstructed;
	boolean isDestructing;
	boolean isConstructing;
	
	double constructionProgress = 0.0;
	double destructionProgress = 0.0;
//	private TerrainDevelopmentType developmentType = TerrainDevelopmentType.NONE;
	
	private WarpedGroupIdentity developmentSuppliesID;
	
	private double surfaceGravity;
	private double surfaceGravityScalar;
	
	private double surfaceMagnetism;
	private double surfaceMagnetismScalar;

	protected ArrayList<ResourceType> resources = new ArrayList<>();
	private double surveyProgress = 0.0;

	
	//private double localTime = 1.0; //0.0 = midnight, 0.5 = midday 1.0 = midnight
	//protected double temperature = 0.0;
	
	
	public TerrainTile(TerrainTileSet<T> parent, T primaryType) {
		super(parent, primaryType);
		this.parent = parent;
		this.tileType = primaryType;
		this.secondaryType = primaryType;
		this.transitionType = TerrainTileTransitionType.NONE;
		tileRaster = parent.getTileImage(primaryType);
		setRaster(tileRaster);
		initializeSelectOptions();
	}

	
	public TerrainTile(TerrainTileSet<T> parent, T primaryType, TerrainTileTransitionType transitionType) {
		super(parent, primaryType);
		this.parent = parent;
		this.tileType = primaryType;
		this.secondaryType = parent.getRandomTransition(primaryType);
		this.transitionType = transitionType;
		tileRaster = parent.getTileImage(primaryType);
		setRaster(tileRaster);
		initializeSelectOptions();
	}
	
	public TerrainTile(TerrainTileSet<T> parent, T primaryType, T secondaryType, TerrainTileTransitionType transitionType) {
			super(parent, primaryType);
		this.parent = parent;
		this.tileType = primaryType;
		this.secondaryType = secondaryType;
		this.transitionType = transitionType;
		tileRaster = parent.getTransitionImage(primaryType, secondaryType, transitionType);
	    setRaster(tileRaster);
		initializeSelectOptions();
	}


	public T getSecondaryType() {return secondaryType;}
	public TerrainTileTransitionType getTransitionType() {return transitionType;}
	
	/*
	public void setRiver(RiverSegment riverSegment) {
		return;
		//TODO fix rivers
		
		isRiver = true;
		this.riverSegment = riverSegment;
		riverRaster = parent.getRiverType().getRiverImage(riverSegment.getSegmentType());	
		updateGraphics();
		
	}
	
	public void setRiver(DirectionType joiningDirection) {
		return;
		
		if(!isRiver) {
			Console.err("Tile -> setRiver() -> tried to join rivers with a tile that is not a river");
			return;
		}
		riverSegment.setSegmentType(RiverSegmentType.getTributarySegment(joiningDirection));
		riverRaster = parent.getRiverType().getRiverImage(riverSegment.getSegmentType());
		updateGraphics();
		
		//TODO fix rivers
	}

	public RiverSegment getRiverSegment() {return riverSegment;}
	public RiverSegmentType getRiverSegmentType() {return riverSegment.getSegmentType();}
	public boolean isRiverPossible(DirectionType relativeDir) {
		if(isRiver && riverSegment.getSegmentType().isSegmentPossible(relativeDir)) return true; 
		else if(primaryType.isRiverPossible() && secondaryType.isRiverPossible()) return true; else return false;
	}
	public boolean isRiver() {return isRiver;}
	public void setTileTransition(TileTransitionType transitionType) {
		tileRaster = parent.getTransitionImage(primaryType, secondaryType, transitionType);
		setRaster(tileRaster);
	}
	public double getLocalTime() {return localTime;}
	public double getTemperature() {return temperature;}
	public double getDayTemperature() {return T.getDayTemperature(this);}
	public double getNightTemperature() {return TileType.getNightTemperature(this);}
	public void setLocalTime(double localTime) {this.localTime = localTime;}	
	*/
	
	
	/**Matching Tiles
	 * Two tiles are matching if they are from the same set, have the same primary and secondary type
	 * isMatching() is not equivalent to isEquals(). If the same tile is passed as tile1 and tile2 parameter isMatching() will return false, the opposite is true for isEquals()*/
	
	public boolean isMatching(TerrainTile<?> tile) {return isMatching(this, tile);}
	public static boolean isMatching(TerrainTile<?> tile1, TerrainTile<?> tile2) {
		if(isMatchingSet(tile1, tile2) && isMatchingType(tile1, tile2) && isMatchingShape(tile1, tile2)) return true; else return false;
	}
	
	public boolean isInverseType(TerrainTile<?> tile2) {return isInverseType(this, tile2);}
	public static boolean isInverseType(TerrainTile<?> tile1, TerrainTile<?> tile2) {
		if(tile1.getPrimaryType() == tile2.getSecondaryType() && tile1.getSecondaryType() == tile2.getPrimaryType()) return true; else return false;
	}
	
	public boolean isMatchingType(TerrainTile<?> tile) {return isMatchingType(this, tile);}
	public static boolean isMatchingType(TerrainTile<?> tile1, TerrainTile<?> tile2) {
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
	
	public boolean isMatchingShape(TerrainTile<?> tile) {return isMatchingShape(this, tile);}
	public static boolean isMatchingShape(TerrainTile<?> tile1, TerrainTile<?> tile2) {
		if(!isMatchingSet(tile1, tile2)) {
			Console.err("Tile -> isMatchingShape() -> tile1 and tile2 are from different sets, they cant' match");
			return false;
		}
		if(tile1.getTransitionType() == tile2.getTransitionType()) return true; else return false;		
	}
	
	public boolean isMatchingSet(WarpedTile<?> tile) {return isMatchingSet(this, tile);}
	public static boolean isMatchingSet(WarpedTile<?> tile1, WarpedTile<?> tile2) {
		if(tile1.isEqual(tile2)) {
			Console.err("Tile -> isMatching() -> tile1 and tile2 are the same tile");
			return false;
		}
		
		if(tile1.parent.isMatchingSet(tile2.parent)) return true; else return false;
	}

	
	public boolean isPureTile() {if(tileType == secondaryType) return true; else return false;}
	
	protected void updateGraphics() {
		BufferedImage img = UtilsImage.generateClone(tileRaster);
		Graphics g = img.getGraphics();
	//	if(isRiver)	g.drawImage(riverRaster, 0, 0, (int)size.x, (int)size.y, null);
		g.dispose();
		setRaster(img);
	}
	

	
}
