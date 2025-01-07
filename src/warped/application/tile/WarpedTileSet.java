/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;

public class WarpedTileSet {
	
	protected boolean valid;
	private String name = "default";
	
	protected WarpedSpriteSheet tileSheet;
	protected TileType primaryType;
	protected RiverType riverType = RiverType.WATER;
	
	protected int setIdentity;
	private static int setCount = 0;
	
	protected ArrayList<TileType> tileTypes; 
	protected ArrayList<TileType> scatterTypes; // tiles that are one off (i.e. oasis) they are not 'tilable' and do not transition to other tiles
	protected ArrayList<TileType> exclusiveTypes = new ArrayList<>(); //exclusive Types will not have the primary transition, only added transitions
	
	protected HashMap<TileType, ArrayList<TileType>> transitionRules;
	protected HashMap<TileType, TileType> scatterRules = new HashMap<>();
		
	protected HashMap<TileType, BufferedImage> tileImages = new HashMap<>();	
	protected HashMap<TileType, HashMap<TileType, HashMap<TileTransitionType, BufferedImage>>> transitionImages;

	/** Tile Set 
	 * **NOTE**
	 * You Must Manually validate a set by calling validate() before it can be used
	 * 
	 * Tile sets in warped framework remove the need to manual generate transition images
	 * When you create a tile set call the setTileTypes() method with a list of tile types and coordinates to their corresponding graphics
	 * Note -> the first entry passed into setTileTypes() will be used as the primary tile for that set, the primary tile will always be able to transition to any other tile in the set
	 * 		   this is so that there is never an instance where a tile can get stuck with no other tile to transition to. Can always return to the primary tile and from there transition to any other tile
	 * Once a set has a list of tile types it can automatically generate transitions between any of those tiles by adding transition rules
	 * Call the addTileTransitionSet() method and pass in the tile that the rules should apply to and a list of transitions that tile can transition to.
	 * After adding all tile transitions to the set you must call the updateTransitionImages() function.
	 * This will generate all the images required to for the transition rules that you have just input
	 * When a transition till is generated it will get its graphic from the set rather then generating a new transition graphic each time a transition tile is created 
	 * 
	 * */
	
	public WarpedTileSet(String name, WarpedSpriteSheet tileSheet) {
		this.tileSheet = tileSheet;
		this.name = name;
		setCount++;
		setIdentity = setCount;
	}
	
	public WarpedTileSet generateSubSet(String name, List<TileType> subSet) {
		if(!valid) {
			Console.err("TileSet : " + name +  " -> generateSubSet() -> set must be valid to generate a subset");
			return null;
		}
		
		if(!containsTileTypes(subSet)) {
			Console.err("TileSet : " + name +  " -> generateSubSet() -> tried to generate a sub set with tiles that are not contained in the set");
			return null;
		}
		
		Console.ln("TileSet : " + this.name +  " -> generateSubSet() -> generating subset : " + name);
		WarpedTileSet result = new WarpedTileSet(name, tileSheet);
		result.tileTypes = new ArrayList<>(subSet);
		result.primaryType = subSet.get(0);
		Console.ln("TileSet : " + this.name +  " -> generateSubSet() -> sub set primary : " + result.primaryType);
		String str = ""; 
		for(int i = 0; i < subSet.size(); i++) str += ", " + subSet.get(i);
		Console.ln("TileSet : " + this.name +  " -> generateSubSet() -> sub set types : " + str);
		
		result.transitionRules = new HashMap<>();
		result.tileImages = new HashMap<>();
		
		result.transitionRules.put(result.primaryType, new ArrayList<>(result.tileTypes.subList(1, result.tileTypes.size())));
		result.tileImages.put(result.primaryType, getTileImage(result.primaryType));
		
		for(int i = 1; i < subSet.size(); i++) {
			TileType type = subSet.get(i);
			if(exclusiveTypes.contains(type)) result.exclusiveTypes.add(type);				
			
			result.transitionRules.put(type, new ArrayList<>(new ArrayList<>(Arrays.asList(result.primaryType))));
			result.tileImages.put(type, getTileImage(type));
			
			
			ArrayList<TileType> transitionRules = getTransitionRules(type);
			for(int j = 0; j < transitionRules.size(); j++) {
				TileType transition = transitionRules.get(j);
				if(subSet.contains(transition)) result.transitionRules.get(type).add(transition);
			}
		}
		
		for(int i = 0; i < result.exclusiveTypes.size(); i++) {
			TileType tileType = result.exclusiveTypes.get(i);
			
			for(int ii = 0; ii < result.transitionRules.get(tileType).size(); ii++) {
				if(result.transitionRules.get(tileType).get(ii) == result.primaryType) {
					result.transitionRules.get(tileType).remove(ii);
				}
			}
			for(int iii = 0; iii < result.transitionRules.get(result.primaryType).size(); iii++) {
				if(result.transitionRules.get(result.primaryType).get(iii) == tileType) {
					result.transitionRules.get(result.primaryType).remove(iii);
				}
			}
		}
		
		result.updateTransitionImages();
		result.validate();
		return result;
	}
	
	public void setTileTypes(List<TileType> tileTypes, List<Vec2i> tileCoords) {
		if(tileTypes.contains(null)) {
			Console.err("TileSet " + name + " -> setTileTypes() -> tileTypes list continas null value");
			return;
		}
		if(tileCoords.contains(null)) {
			Console.err("TileSet : " + name + " -> setTileTypes() -> tileCoords list contains a null value");
			return;
		}
		if(tileTypes.size() > tileSheet.size()) {
			Console.err("TileSheet : " + name + " -> setTileTypes -> more tile types than tiles (tileTypes, tileCount)  :  (" + tileTypes.size() + ", " + tileSheet.size() + ")");
			return;
		}
		if(tileTypes.size() != tileCoords.size()) {
			Console.err("TileSet : " + name + " -> setTileTypes() -> inconsistent sizes, tileTypes size must match tileIndices size (tileTypes, tileIndices)  :  (" + tileTypes.size() + ", " + tileCoords.size() + ")");
			return;
		}
		if(tileTypes.size() <= 0) {
			Console.err("TileSet : " + name + " -> setTileTypes() -> tileTypes size is less than or equal 0 : " + tileTypes.size());
			return;
		}
		
		for(int i = 0; i < tileCoords.size(); i++) {
			int Ax = tileCoords.get(i).x; // check each tile
			int Ay = tileCoords.get(i).y; // check each tile
			if(Ax > tileSheet.getXcount() || Ay > tileSheet.getYcount()) {
				Console.err("TileSet : " + name + " -> setTileTypes() -> tileCoords contains a coordinate that is not in the sheet -> index, (x, y), (xMax, yMax)  :  " + i + ", ( " + Ax + ", " + Ay + "),  (" + tileSheet.getXcount() + ", " + tileSheet.getYcount() + ")" );
				return;
			}
			if(Ax < 0 || Ay < 0) {
				Console.err("TileSet : " + name + " -> setTileTypes() -> tileIndicies contains an index that is less than 0 (index, tileCount)  :  (" + Ax + ", " + tileSheet.size() + ")");
				return;
			}
			for(int j = 0; j < tileCoords.size(); j++) {
				if(i == j) continue; // do not check against itself
				int Bx = tileCoords.get(j).x;
				int By = tileCoords.get(j).y;
				if(Ax == Bx && Ay == By) { // against each other tile in the set
					Console.err("TileSet : " + name + " -> setTileTypes() -> the same tile has been set to two different tile types (tileIndex, i, j)  :  (" + tileTypes.get(Ax) + ", " + i + ", " + j + ")");
					return;
				}
			}
		}
		
		this.tileTypes 		 = new ArrayList<>(tileTypes);
		this.scatterTypes 	 = new ArrayList<>();
		this.transitionRules = new HashMap<>();
		this.transitionImages = new HashMap<>();
		this.tileImages = new HashMap<>();
				
		primaryType = tileTypes.get(0);
		transitionRules.put(primaryType, new ArrayList<>(tileTypes.subList(1, tileTypes.size())));
		tileImages.put(primaryType, tileSheet.getSprite(0));
		
		for(int i = 1; i < tileTypes.size(); i++) {
			transitionRules.put(tileTypes.get(i), new ArrayList<TileType>(Arrays.asList(primaryType)));
			tileImages.put(tileTypes.get(i), tileSheet.getSprite(tileCoords.get(i)));
		}
	}
		

	//--------
	//------------------- Access ---------------------
	//--------	
	
	public RiverType getRiverType() {return riverType;}
	public void setRiverType(RiverType riverType) {this.riverType = riverType;}

	public int getTileWidth() {
		if(tileSheet == null) {
			Console.err("TileSet : " + name +  " -> width() -> tileSheet is null");
			return -1;
		}
		return tileSheet.spriteWidth;
	}
	
	public String getName() {return name;}
	
	public int getTileHeight() {
		if(tileSheet == null) {
			Console.err("TileSet : " + name +  " -> width() -> tileSheet is null");
			return -1;
		}
		return tileSheet.spriteHeight;
	}
	
	public void removeExclusiveTransition(TileType tileType) {
		if(!exclusiveTypes.contains(tileType)) {
			Console.err("TileSet : " + name +  " ->  removeExclusiveTranstion() -> tile type : " + tileType + " -> never existed or was already removed");
			return;
		}
		
		transitionRules.get(tileType).add(primaryType);
		transitionRules.get(primaryType).add(tileType);
		
		this.updateTransitionImages();
	}
	
	public void setExclusiveTransition(TileType tileType) {
		if(!transitionRules.containsKey(tileType)) {
			Console.err("TileSet : " + name +  " -> removePrimaryTransition() -> transitionRules doesn't contain any rules for : " + tileType);
			return;
		}
		
		if(exclusiveTypes.contains(tileType)) {
			Console.err("TileSet : " + name +  " -> setExclusiveTranstion() -> tile type : " + tileType + " -> is already exclusive type");
			return;
		}
		
		exclusiveTypes.add(tileType);
		
		for(int i = 0; i < transitionRules.get(tileType).size(); i++) {
			if(transitionRules.get(tileType).get(i) == primaryType) {
				transitionRules.get(tileType).remove(i);
			}
		}
		for(int i = 0; i < transitionRules.get(primaryType).size(); i++) {
			if(transitionRules.get(primaryType).get(i) == tileType) {
				transitionRules.get(primaryType).remove(i);
			}
		}
	}
	
	public BufferedImage getTransitionImage(TileType primaryType, TileType secondaryType, TileTransitionType transition) {
		if(!containsTileType(primaryType) || !containsTileType(secondaryType)) {
			Console.err("TileSet : " + name +  " -> getTransitionImage() -> set does not contain primary or secondary type");
			return null;
		}
		if(primaryType == secondaryType) {
			Console.err("TileSet : " + name +  " -> getTransitionImage() -> can't transition between primary and secondary type because they are the same : " + primaryType);
			return null;
		}
		if(!containsTransitionRules(primaryType)) {
			Console.err("TileSet : " + name +  " -> getTransitionImage() -> set contains no transition rules for : " + primaryType);
			return null;
		}
		if(!containsTransitionSetRule(primaryType, secondaryType)) {
			Console.err("TileSet : " + name + " -> getTransitionImage() -> set rules for : " + primaryType + ", contain no transition rule for type : " + secondaryType);
			return null;
		}
		if(!transitionImages.containsKey(primaryType)) {
			Console.err("TileSet : " + name + " -> getTransitionImage() -> transition images does not contain any images for : " + primaryType);
			return null;
		}
		if(!transitionImages.get(primaryType).containsKey(secondaryType)) {
			Console.err("TileSet : " + name + " -> getTransitionImage() -> " + primaryType + " transition images does not contain any images for : " + secondaryType);
			return null;
		}
		if(!transitionImages.get(primaryType).get(secondaryType).containsKey(transition)) {
			Console.err("TileSet : " + name + " -> getTransitionImage() -> " + primaryType + " transition images for : " + secondaryType + " does not contain the transition : " + transition);
			return null;
		}
		return transitionImages.get(primaryType).get(secondaryType).get(transition);
		
	}
	
	public BufferedImage getTileImage(TileType tileType) {
		if(!tileTypes.contains(tileType)) {
			Console.err("TileSet : " + name + " -> getTileImage() -> set does not contain tile type : " + tileType);
			return null;
		}
		if(!tileImages.containsKey(tileType)) {
			Console.err("TileSet : " + name + " -> getTileImage() -> set does not contain tile image for : " + tileType);
			return null;
		}
		if(tileImages.get(tileType) == null) {
			Console.err("TileSet : " + name + " -> geTileImage() -> null tile image at : " + tileType);
			return null;
		}
		return tileImages.get(tileType);
	}
	
	public ArrayList<TileType> getTransitionRules(TileType tileType){
		if(tileType == null) {
			Console.err("TileSet : " + name + " -> getTileTransitionSet -> passed null parameter");
			return null;
		}
		return transitionRules.get(tileType);
	}
		
	public TileType getRandomType() {
		if(!isValid()) {
			Console.err("TileSet : " + name + " -> getRandomTileType() -> tileSet must be valid");
			return null;
		}
		int rn = UtilsMath.random.nextInt(tileTypes.size());
		return tileTypes.get(rn);
	}
	
	/*
	public TileType getRandomScatterType() {
		if(!isValid()) {
			Console.err("TileSet -> getRandomScatterType() -> tileSet must be valid");
			return null;
		}
		 int rn = Maths.random.nextInt(scatterTypes.size());
		 return scatterTypes.get(rn);
	}	
	
	public TileType getRandomScatterVariantType() {
		if(!isValid()) {
			Console.err("TileSet -> getRandomScatterVariantype() -> tileSet must be valid");
			return null;
		}
		
		TileType type = getRandomScatterType();
		int rn = Maths.random.nextInt(scatterRules.get(type).size());
		return scatterRules.get(type).get(rn);
	}
	
	public TileType getScatterVariantType(TileType type) {
		if(!isValid()) {
			Console.err("TileSet -> getRandomScatterVariantype() -> tileSet must be valid");
			return null;
		}
		if(!scatterTypes.contains(type)) {
			Console.err("TileSet -> getScatterVariantType() -> set does not contain scatter tiles for the type : " + type);
			return null;
		}
		int rn = Maths.random.nextInt(scatterRules.get(type).size());
		return scatterRules.get(type).get(rn);
	
	public boolean containsScatterTypes(TileType type) {
		if(scatterRules.containsKey(type)) return true;
		else return false;
	}
	}*/
	
	public boolean containsTileType(TileType tileType) {
		if(tileTypes.contains(tileType)) return true;
		else return false;
	}
	
	private boolean containsTileTypes(List<TileType> tileType) {
		for(int i = 0; i < tileType.size(); i++) {
			if(!tileTypes.contains(tileType.get(i))) return false;
		}
		return true;
	}
	
	/**Does the tile set contain transition rule set for the tile type other than the primary transition that all tiles have*/
	public boolean containsTransitionRules(TileType type) {
		if(transitionRules.get(type).size() > 0) return true;
		else return false;
	}
	
	/**Does the tile ruleset contain a rule for the type*/
	public boolean containsTransitionSetRule(TileType type, TileType transition) {
		if(!containsTransitionRules(type)) return false;
		else if(transitionRules.get(type).contains(transition)) return true; 
		else return false;		
	}
	
	public int getSetID() {return setIdentity;}
	public boolean isMatchingSet(WarpedTileSet set) {if(set.getSetID() == setIdentity) return true; else return false;}
	
	
	
	
	public WarpedTile generateTile(TileType tileType) {
		if(!containsTileType(tileType)) {
			Console.err("TileSet : " + name + " -> generateTile() -> set does not contain the tile type : " + tileType);
			return null;
		} else return new WarpedTile(this, tileType);
	}
	/*
	public TileCelestial generateCelestialTile(EntitieCelestial parent, TileType tileType) {
		if(!containsTileType(tileType)) {
			Console.err("TileSet : " + name + " -> generateCelestialTile() -> set does not contain the tile type : " + tileType);
			return null;
		} else return new TileCelestial(parent, this, tileType);
	}
	
	public TileCelestial generateCelestialTile(EntitieCelestial parent, TileType primaryType, TileType secondaryType, TileTransitionType transitionType) {
		if(!containsTileType(primaryType) || !containsTileType(secondaryType)) {
			Console.err("TileSet : " + name + " -> generateCelestailTransitionTile() -> set does not contain the tile type : " + primaryType);
			return null;
		} 
		if(primaryType == secondaryType || transitionType == TileTransitionType.NONE) return new TileCelestial(parent, this, primaryType);		
		return new TileCelestial(parent, this, primaryType, secondaryType, transitionType);
	}
	
	public TileCelestial generateCelestialTile(EntitieCelestial parent, TileType primaryType, TileTransitionType transitionType) {
		if(!containsTileType(primaryType)) {
			Console.err("TileSet : " + name + " -> generateCelestailTransitionTile() -> set does not contain the tile type : " + primaryType);
			return null;
		} else return new TileCelestial(parent, this, primaryType, transitionType);
	}
	*/
	
	public TileType getRandomTransition(TileType tileType) {
		if(transitionRules.get(tileType).size() == 0) {
			Console.err("TileSet : " + name + " -> getRandomTransition() -> tile Type : " + tileType + " -> has no transition rules to transition by");
			return TileType.GRASSLAND;
		}
		TileType result = transitionRules.get(tileType).get(UtilsMath.random(transitionRules.get(tileType).size())); 
		//Console.ln("TileSet -> generateRandomTransition() -> generating transition to " + result);
		return result;
	}
	
	public WarpedTile generateRandomTile() {
		if(!isValid()) {
			Console.err("TileSet : " + name + " -> generateRandomTile() -> tileSet must be valid");
			return null;
		}
		TileType type = getRandomType();
		return new WarpedTile(this, type);
	}
	
	/*
	public TileCelestial generatePrimaryCelestialTile(EntitieCelestial parent) {
		if(!isValid()) {
			Console.err("TileSet : " + name + " -> generateRandomTile() -> tileSet must be valid");
			return null;
		}
		return new TileCelestial(parent, this, primaryType);
	}
	
	public TileCelestial generateRandomCelestialTile(EntitieCelestial parent) {
		if(!isValid()) {
			Console.err("TileSet : " + name + " -> generateRandomTile() -> tileSet must be valid");
			return null;
		}
		TileType type = getRandomType();
		return new TileCelestial(parent, this, type);
	}
	*/
	/*
	public Tile generateRandomScatterTile() {
		if(!isValid()) {
			Console.err("TileSet -> generateRandomScatterTile() -> tileSet must be valid");
			return null;
		}
		TileType type = getRandomScatterVariantType();
		return new Tile(type, getTileImage(type));
	}
	
	public Tile generateRandomScatterVariant(TileType type) {
		if(!isValid()) {
			Console.err("TileSet -> generateRandomScatterVariant() -> tileSet must be valid");
			return null;
		}
		TileType scatterType = getScatterVariantType(type);
		return new Tile(scatterType, getTileImage(scatterType));
	}
		

	 */
	
	/**The first tile in the tileTypes list will become the primary tile;
	 * All tiles will be able to transition to the primary tile
	 *This prevents any situation where a tile has no transition to it*/
	/*
	public void setTileTypes(List<TileType> tileTypes) {
		if(tileTypes.contains(null)) {
			Console.err("TileSet -> setTileTypes() -> tileTypes list contains a null value");
			return;
		}
		if(tileTypes.size() > tileSheet.getSpriteCount()) {
			Console.err("TileSheet -> setTileTypes -> more tile types than tiles (tileTypes, tileCount)  :  (" + tileTypes.size() + ", " + tileSheet.getSpriteCount() + ")");
			return;
		}

		transitionRules = new HashMap<>();
		this.tileTypes = new ArrayList<>();
		this.scatterTypes = new ArrayList<>();
		primaryType = tileTypes.get(0);
		
		for(int i = 0; i < tileTypes.size(); i++) {
			ArrayList<TileType> set = new ArrayList<TileType>();
			set.add(primaryType);
			transitionRules.put(tileTypes.get(i), set);
			tileImages.put(tileTypes.get(i), tileSheet.getRawSprite(i));
			tileTypes.add(tileTypes.get(i));
		}
	}
	 */	
	
	
	public void updateTransitionImages() {
		transitionImages = new HashMap<>();
		transitionRules.forEach((t, s) -> {
			HashMap<TileType, HashMap<TileTransitionType, BufferedImage>> transitions = new HashMap<>();
			transitionRules.get(t).forEach(r -> {
				HashMap<TileTransitionType, BufferedImage> images = new HashMap<>();
				for(int i = 0; i < TileTransitionType.size(); i++) {
					TileTransitionType transition = TileTransitionType.get(i);
					if(transition == TileTransitionType.NONE) continue;
					images.put(transition, UtilsImage.generateTileTransitionImage(getTileImage(t), getTileImage(r), transition));					
				}
				transitions.put(r, images);
			});
			transitionImages.put(t, transitions);
		});
	}
	
	public void addTileTransitionSet(TileType tileType, ArrayList<TileType> transitionSet) {
		if(transitionRules == null) {
			Console.err("TileSet : " + name + " -> addTileTransitionSet() -> tileSetRules must be set before adding any tile transitions");
			return;
		}
		
		if(tileType == null || transitionSet == null) {
			Console.err("TileSet : " + name + " -> addTileTransitionSet() -> passed null value");
			return;
		}
		
		if(tileType == primaryType) {
			Console.err("TileSet : " + name + " -> the primaryType of tile for a set can not be manually set, by definition the primary type can transition to all other tiles");
			return;
		}

		if(!transitionSet.contains(primaryType)) {
			Console.ln("TileSet : " + name + " -> addTileTransitionSet() -> set does not contain a transition to the primary tile -> it will be added");
			transitionSet.add(primaryType);
		}		
		
		
		if(!tileTypes.contains(tileType)) {
			Console.err("TileSet : " + name + " -> addTileTransition() -> tile set does not contain tile : " + tileType);
			return;
		}
		 		
		for(int i = 0; i < transitionSet.size(); i++) {
			TileType transitionType = transitionSet.get(i);
			if(transitionType == null) {
				Console.err("TileSet : " + name + " -> addTileTransitionSet() -> transitionSet contains null value at index " + i);
				return;
			}
			if(tileType == transitionType) {
				Console.err("TileSet : " + name + " -> addTileTransitionSet() -> can not transition to the same tile : " + tileType);
				return;
			}
			if(!tileTypes.contains(transitionType)) {
				Console.err("TileSet : " + name + " -> addTileTransition() -> tile set does not contain tile : " + transitionType);
				return;
			}
		}
		
		for(int i = 0; i < transitionSet.size(); i++) {
			TileType transitionType = transitionSet.get(i);
			transitionRules.get(tileType).add(transitionType);
			if(!containsTransitionSetRule(transitionType, tileType)) {
				transitionRules.get(transitionType).add(tileType);
			}
			
		}
		
	}
	
	/*
	public void addScatterTile(TileType tileType, TileType scatterTile) {
		if(tileType == null || scatterTile == null) {
			Console.err("TileSet -> addScatterTile() -> passed null variable as parameter");
			return;
		}
		if(tileType == scatterTile) {
			Console.err("TileSet -> addScatterTile() -> tile type must be different to scatter type : " + tileType);
			return;
		}
		if(!tileTypes.contains(tileType)) {
			Console.err("TileSet -> addScatterTile() -> tile type is not contained in the set :  " + tileType);
			return;
		}
		if(!tileTypes.contains(scatterTile)) {
			Console.err("TileSet -> addScatterTile() -> scatter tile type is not contained in the set :  " + scatterTile);
			return;
		}
		if(scatterRules.get(tileType) == null) {
			scatterRules.put(tileType, Arrays.asList(scatterTile));
			return;
		}
		if(scatterRules.get(tileType).contains(scatterTile)) {
			Console.err("TileSet -> addScatterTile() -> tile type already has scatter tile (tileType, scatterTile)  :  (" + tileType + ", " + scatterTile + ")");
			return;
		}			
		
		if(!scatterTypes.contains(scatterTile)) scatterTypes.add(scatterTile);
		scatterRules.get(tileType).add(scatterTile);
	};
	
	public void addScatterTileSet(TileType tileType, List<TileType> scatterTiles) {
		if(tileType == null || scatterTiles == null) {
			Console.err("TileSet -> addScatterTileSet() -> passed null value as variable");
			return;
		}
		
		if(scatterRules.get(tileType) != null) {
			Console.err("TileSet -> addScatterTileSet() -> scatterTileRules already exist for " + tileType + " ");
		}
		
		for(int i = 0; i < scatterTiles.size(); i++) {
			TileType scatterTile = scatterTiles.get(i);
			if(scatterTile == null) {				
				Console.err("TileSet -> addScatterTileSet() -> scatter tile set contains null value");
				return;
			}
			if(tileType == scatterTile) {
				Console.err("TileSet -> addScatterTileSet() -> can not scatter the same tile into itself : " + tileType);
				return;
			}
			if(!tileTypes.contains(scatterTile)) {
				Console.err("TileSet -> addScatterTileSet() -> tile set does not contain the type : " + scatterTile);
				return;
			}
		}
		
		if(!tileTypes.contains(tileType)) {
			Console.err("TileSet -> addScatterTileSet() -> tile set does not contain the type : " + tileType);
			return;
		}
		
		for(int i = 0; i < scatterTiles.size(); i++) {
			if(!scatterTypes.contains(scatterTiles.get(i))) scatterTypes.add(scatterTiles.get(i));
		}
		scatterRules.put(tileType, scatterTiles);
	}
	
	*/
	public boolean isValid() {return valid;}
	
	public void validate() {
		//Check Types
		if(tileTypes == null) {
			Console.err("TileSet : " + name + " -> validate() -> tileTypes is null");
			valid = false;
			return;
		}
		if(tileTypes.contains(null)) {
			Console.err("TileSet : " + name + " -> validate() -> tileTypes contains null value");
			valid = false;
			return;
		}
		
		
		//Check Set Rules
		if(transitionRules == null) {
			Console.err("TileSet : " + name + " -> validate() -> tileSetRules is null");
			valid = false;
			return;
		}
		if(transitionRules.containsKey(null)) {
			Console.err("TileSet : " + name + " -> validate() -> tileSetRules contians a null key");
			valid = false;
			return;
		}
		if(transitionRules.containsValue(null)) {
			Console.err("TileSet : " + name + " -> validate() -> tileSetRules contains a null value");
			valid = false;
			return;
		}
		for(int i = 0; i < tileTypes.size(); i++) {
			if(transitionRules.get(tileTypes.get(i)).contains(null)){
				Console.err("TileSet : " + name + " -> validate() -> tileSetRules for tile " + tileTypes.get(i) + ", contains a null transition rule");
				valid = false;
				return;
			}
		}
		
		/*
		for(int i = 0; i < tileTypes.size(); i++){
			TileType type = tileTypes.get(i);
			if(type != primaryType && !transitionRules.containsKey(type)) {
				Console.ln("TileSet -> validate() -> warning, no transition rules were set for the type : " + type + ", primary transition rule will be created");
				transitionRules.put(type, Arrays.asList(primaryType));			
			}
		}
		*/
		
		//Check Base Tiles
		if(tileImages.containsKey(null)) {
			Console.err("TileSet : " + name + " -> validate() -> baseTiles contains a null key");
			valid = false;
			return;
		}
		if(tileImages.containsValue(null)) {
			Console.err("TileSet : " + name + " -> validate() -> baseTiles contains a null value");
			valid = false;
			return;
		}
		
		
		
		//Consistency checks
		if(tileTypes.size() != transitionRules.size()) {
			Console.err("TileSet : " + name + " -> isValid() -> number of tile Types does not match the number of tile Rules");
			valid = false;
			return;
		}
		if(tileTypes.size() > tileSheet.size()) {
			Console.err("TileSet : " + name + " -> isValid() -> number of tile types exceeds the number of tiles");
			valid = false;
			return;
		}
		if(tileTypes.size() != tileImages.size()) {
			Console.err("TileSet : " + name + " -> isValid() -> number of tile types does not match number of tile images : " + tileTypes.size() + ", " + tileImages.size());
			valid = false;
			return;
		}
		
		for(int i = 0; i < tileTypes.size(); i++) {
			TileType type = tileTypes.get(i);
			if(!transitionRules.containsKey(type)) {
				Console.err("TileSet : " + name + " -> isValid() -> transition rules is missing an entry for the type  : " + tileTypes);
				valid = false;
				return;
			}
			
			if(!transitionImages.containsKey(type)){
				Console.err("TileSet : " + name + " -> isValid() -> transition Images is missing an entry for the type  : " + tileTypes);
				valid = false;
				return;
			}
		}
		
		Console.ln("TileSet : " + name + " -> isValid() -> the tile set was succesfully validated");
		valid = true;
	}
	
}
