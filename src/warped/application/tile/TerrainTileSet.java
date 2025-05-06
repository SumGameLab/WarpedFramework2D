package warped.application.tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import warped.application.state.WarpedGroup;
import warped.application.state.WarpedGroupIdentity;
import warped.application.state.WarpedState;
import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;

public class TerrainTileSet<T extends TileableGenerative<? extends Enum<?>>> extends TileSetGenerative<T> {


	/* Generative Tile Set 
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
	
	protected T primaryType;
	
	protected ArrayList<T> scatterTypes; // tiles that are one off (i.e. oasis) they are not 'tilable' and do not transition to other tiles
	protected ArrayList<T> exclusiveTypes = new ArrayList<>(); //exclusive Types will not have the primary transition, only added transitions
	
	protected HashMap<T, ArrayList<T>> transitionRules;
	protected HashMap<T, T> scatterRules = new HashMap<>();
	
	//protected RiverType riverType = RiverType.WATER;

	protected HashMap<T, HashMap<T, HashMap<TerrainTileTransitionType, BufferedImage>>> transitionImages;
	
	public TerrainTileSet(T setType, WarpedSpriteSheet tileSheet) {
		super(setType, tileSheet);


		this.tileTypes 		 = (ArrayList<T>) setType.getAll();
		this.scatterTypes 	 = new ArrayList<>();
		this.transitionRules = new HashMap<>();
		this.transitionImages = new HashMap<>();
		this.tileImages = new HashMap<>();
				
		primaryType = tileTypes.get(0);
		transitionRules.put(primaryType, new ArrayList<>(tileTypes.subList(1, tileTypes.size())));
		tileImages.put(primaryType, tileSheet.getSprite(0));
		
		if(tileSheet.getSpriteCount() < tileTypes.size()) {
			Console.err("WarpedTileSet -> WarpedTileSet() -> number of images on tile sheet is less than the number of tiles specified -> can't proceede");
			return;
		}
		
		for(int i = 1; i < tileTypes.size(); i++) {
			transitionRules.put(tileTypes.get(i), new ArrayList<T>(Arrays.asList(primaryType)));
			tileImages.put(tileTypes.get(i), tileSheet.getSprite(i));
		}
	}
	
	

	public TerrainTileSet<T> generateSubSet(String name, List<T> subSet) {
		if(!valid) {
			Console.err("TileSet : " + name +  " -> generateSubSet() -> set must be valid to generate a subset");
			return null;
		}
		
		if(!containsTileTypes(subSet)) {
			Console.err("TileSet : " + name +  " -> generateSubSet() -> tried to generate a sub set with tiles that are not contained in the set");
			return null;
		}
		
		Console.ln("TileSet : " + this.name +  " -> generateSubSet() -> generating subset : " + name);
		TerrainTileSet<T> result = new TerrainTileSet<>(setType, tileSheet);
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
			T type = subSet.get(i);
			if(exclusiveTypes.contains(type)) result.exclusiveTypes.add(type);				
			
			result.transitionRules.put(type, new ArrayList<>(new ArrayList<>(Arrays.asList(result.primaryType))));
			result.tileImages.put(type, getTileImage(type));
			
			
			ArrayList<T> transitionRules = getTransitionRules(type);
			for(int j = 0; j < transitionRules.size(); j++) {
				T transition = transitionRules.get(j);
				if(subSet.contains(transition)) result.transitionRules.get(type).add(transition);
			}
		}
		
		for(int i = 0; i < result.exclusiveTypes.size(); i++) {
			T tileType = result.exclusiveTypes.get(i);
			
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
	
	/*
	public RiverType getRiverType() {return riverType;}
	public void setRiverType(RiverType riverType) {this.riverType = riverType;}
	*/

	
	/**Use this if you need to link your tile types in a specific order. Otherwise you should just list your tileTypes as they appear in the sprite sheet and they will be linked automatically*/
	public void setTileTypes(List<T> tileTypes, List<VectorI> tileCoords) {
		if(tileTypes.contains(null)) {
			Console.err("TileSet " + name + " -> setTileTypes() -> tileTypes list continas null value");
			return;
		}
		if(tileCoords.contains(null)) {
			Console.err("TileSet : " + name + " -> setTileTypes() -> tileCoords list contains a null value");
			return;
		}
		if(tileTypes.size() > tileSheet.getSpriteCount()) {
			Console.err("TileSheet : " + name + " -> setTileTypes -> more tile types than tiles (tileTypes, tileCount)  :  (" + tileTypes.size() + ", " + tileSheet.getSpriteCount() + ")");
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
			int Ax = tileCoords.get(i).x(); // check each tile
			int Ay = tileCoords.get(i).y(); // check each tile
			if(Ax > tileSheet.getSpriteCountX() || Ay > tileSheet.getSpriteCountY()) {
				Console.err("TileSet : " + name + " -> setTileTypes() -> tileCoords contains a coordinate that is not in the sheet -> index, (x, y), (xMax, yMax)  :  " + i + ", ( " + Ax + ", " + Ay + "),  (" + tileSheet.getSpriteCountX() + ", " + tileSheet.getSpriteCountY() + ")" );
				return;
			}
			if(Ax < 0 || Ay < 0) {
				Console.err("TileSet : " + name + " -> setTileTypes() -> tileIndicies contains an index that is less than 0 (index, tileCount)  :  (" + Ax + ", " + tileSheet.getSpriteCount() + ")");
				return;
			}
			for(int j = 0; j < tileCoords.size(); j++) {
				if(i == j) continue; // do not check against itself
				int Bx = tileCoords.get(j).x();
				int By = tileCoords.get(j).y();
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
			transitionRules.put(tileTypes.get(i), new ArrayList<T>(Arrays.asList(primaryType)));
			tileImages.put(tileTypes.get(i), tileSheet.getSprite(tileCoords.get(i)));
		}
	}
	
	
	public void removeExclusiveTransition(T tileType) {
		if(!exclusiveTypes.contains(tileType)) {
			Console.err("TileSet : " + name +  " ->  removeExclusiveTranstion() -> tile type : " + tileType + " -> never existed or was already removed");
			return;
		}
		
		transitionRules.get(tileType).add(primaryType);
		transitionRules.get(primaryType).add(tileType);
		
		this.updateTransitionImages();
	}
	
	public void setExclusiveTransition(T tileType) {
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
	
	public BufferedImage getTransitionImage(T primaryType, T secondaryType, TerrainTileTransitionType transition) {
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
	
	
        /**Does the tile set contain transition rule set for the tile type other than the primary transition that all tiles have*/
        public boolean containsTransitionRules(T type) {
        	if(transitionRules.get(type).size() > 0) return true;
        	else return false;
        }
        
        /**Does the tile ruleset contain a rule for the type*/
        public boolean containsTransitionSetRule(T type, T transition) {
        	if(!containsTransitionRules(type)) return false;
        	else if(transitionRules.get(type).contains(transition)) return true; 
        	else return false;		
        }
        
        public ArrayList<T> getTransitionRules(T tileType){
    		if(tileType == null) {
    			Console.err("TileSet : " + name + " -> getTileTransitionSet -> passed null parameter");
    			return null;
    		}
    		return transitionRules.get(tileType);
    	}
        
        public T getPrimaryType() {return primaryType;}
    	
    	
    	public T getRandomTransition(T tileType) {
    		if(transitionRules.get(tileType).size() == 0) {
    			Console.err("TileSet : " + name + " -> getRandomTransition() -> tile Type : " + tileType + " -> has no transition rules to transition by");
    			return tileType;
    		}
    		T result = transitionRules.get(tileType).get(UtilsMath.random(transitionRules.get(tileType).size())); 
    		//Console.ln("TileSet -> generateRandomTransition() -> generating transition to " + result);
    		return result;
    	}
    	
    	public WarpedTile generateTile(T primaryType, T secondaryType, TerrainTileTransitionType transitionType) {
    		if(!containsTileType(primaryType) || !containsTileType(secondaryType)) {
    			Console.err("TileSet : " + name + " -> generateCelestailTransitionTile() -> set does not contain the tile type : " + primaryType);
    			return null;
    		} 
    		if(primaryType == secondaryType || transitionType == TerrainTileTransitionType.NONE) return new TerrainTile<T>(this, primaryType);		
    		return new  TerrainTile<T>(this, primaryType, secondaryType, transitionType);
    	}
    	
    	public TerrainTile<T> generateTerrainTile(T primaryType, T secondaryType, TerrainTileTransitionType transitionType) {
    		if(!containsTileType(primaryType) || !containsTileType(secondaryType)) {
    			Console.err("TileSet : " + name + " -> generateCelestailTransitionTile() -> set does not contain the tile type : " + primaryType);
    			return null;
    		} 
    		if(primaryType == secondaryType || transitionType == TerrainTileTransitionType.NONE) return new TerrainTile<T>(this, primaryType);		
    		return new  TerrainTile<T>(this, primaryType, secondaryType, transitionType);
    	}

    	public void updateTransitionImages() {
    		transitionImages = new HashMap<>();
    		transitionRules.forEach((t, s) -> {
    			HashMap<T, HashMap<TerrainTileTransitionType, BufferedImage>> transitions = new HashMap<>();
    			transitionRules.get(t).forEach(r -> {
    				HashMap<TerrainTileTransitionType, BufferedImage> images = new HashMap<>();
    				for(int i = 0; i < TerrainTileTransitionType.size(); i++) {
    					TerrainTileTransitionType transition = TerrainTileTransitionType.get(i);
    					if(transition == TerrainTileTransitionType.NONE) continue;
    					images.put(transition, UtilsImage.generateTileTransitionImage(getTileImage(t), getTileImage(r), transition));					
    				}
    				transitions.put(r, images);
    			});
    			transitionImages.put(t, transitions);
    		});
    	}
    	
    	public void addTileTransitionSet(T tileType, ArrayList<T> transitionSet) {
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
    			T transitionType = transitionSet.get(i);
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
    			T transitionType = transitionSet.get(i);
    			transitionRules.get(tileType).add(transitionType);
    			if(!containsTransitionSetRule(transitionType, tileType)) {
    				transitionRules.get(transitionType).add(tileType);
    			}
    			
    		}
    		
    	}
    	
    
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
    		if(tileTypes.size() > tileSheet.getSpriteCount()) {
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
    			T type = tileTypes.get(i);
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

	/*
	 public TileCelestial generateCelestialTile(EntitieCelestial parent, TileType tileType) {
		if(!containsTileType(tileType)) {
			Console.err("TileSet : " + name + " -> generateCelestialTile() -> set does not contain the tile type : " + tileType);
			return null;
		} else return new TileCelestial(parent, this, tileType);
	}
	
	public TileCelestial generateCelestialTile(EntitieCelestial parent, TileType primaryType, TileTransitionType transitionType) {
		if(!containsTileType(primaryType)) {
			Console.err("TileSet : " + name + " -> generateCelestailTransitionTile() -> set does not contain the tile type : " + primaryType);
			return null;
		} else return new TileCelestial(parent, this, primaryType, transitionType);
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
    	public TerrainTile<T> generatePrimaryTile() {
    		if(!isValid()) {
    			Console.err("TileSet : " + name + " -> generateRandomTile() -> tileSet must be valid");
    			return null;
    		}
    		return new TerrainTile<T>(this, primaryType);
    	}
	
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
    	
    	
    	public boolean containsTileType(T tileType) {
    		if(tileTypes.contains(tileType)) return true;
    		else return false;
    	}
    	
    	public boolean containsTileTypes(List<T> tileType) {
    		for(int i = 0; i < tileType.size(); i++) {
    			if(!tileTypes.contains(tileType.get(i))) return false;
    		}
    		return true;
    	}
	
    	
    	
	
    	@SuppressWarnings("unchecked")
		public WarpedGroupIdentity generateTerrainTileMap(int mapWidth, int mapHeight, String name) {
    		if(mapWidth <= 0 || mapHeight <= 0) {
    			System.err.println("TileManager -> generateCollapseWaveTileMap() -> map width/height must be positive (mapWidth, mapHeight)  :  (" + mapWidth + ", " + mapHeight +")");
    			return null;
    		}
    		
    		
    		WarpedGroupIdentity groupID = WarpedState.tileManager.addGroup(name);
    		WarpedGroup<WarpedTile> group = WarpedState.tileManager.getGroup(groupID);
    		
    		group.setMapSize(mapWidth, mapHeight);
    		group.setPixelSize(mapWidth * getTileWidth(), mapHeight * getTileHeight());
    		group.setMemberSize(getTileWidth(), getTileHeight());
    	
    		for(int y = 0; y < mapHeight; y++) {
    			for(int x = 0; x < mapWidth; x++) {
    				
    				if(x == 0 && y == 0) {
    					TerrainTile<T> tile = generatePrimaryTile();
    					tile.setPosition(x * getTileWidth(), y * getTileHeight());
    					tile.setCoordinates(x, y, x + y * mapWidth);
    					group.addMember(tile);
    					continue;
    				}
    									
    				TerrainTile<T> left = null;
    				TerrainTile<T> above = null;
    				TerrainTile<T> aboveRight = null;
    				if(x != 0) left  = (TerrainTile<T>) WarpedState.tileManager.getTile(groupID, x - 1, y);
    				if(y != 0) above = (TerrainTile<T>) WarpedState.tileManager.getTile(groupID, x, y - 1);
    				if(y != 0 && x != mapWidth - 1) aboveRight = (TerrainTile<T>) WarpedState.tileManager.getTile(groupID, x + 1, y - 1);
    								
    				TerrainTile<T> tile = TerrainTileTransitionType.getConnectingTile(left, above, aboveRight);
    				tile.setPosition(x * getTileWidth(), y * getTileHeight());
    				tile.setCoordinates(x, y, x + y * mapWidth);
    				group.addMember(tile);

    			}
    		}
    			
    		//generateRivers(groupID);
    		return groupID;		
    	}
    	
    	/*
    	public void generateRivers(WarpedGroupIdentity groupID) {
    		if(groupID.getManagerType() != WarpedManagerType.TILE) { 
    			System.err.println("TileManager -> generateRivers() -> the input group is not a group of tiles, it's a group of : " + groupID.getManagerType());
    			return;
    		}
    		
    		WarpedGroup<WarpedTile> group = WarpedState.tileManager.getGroup(groupID);
    		
    		int mapWidth = group.getMapGridSize().x;
    		int mapHeight = group.getMapGridSize().y;
    		
    		for(int y = 0; y < mapHeight; y++) {
    			for(int x = 0; x < mapWidth; x++) {
    				
    				WarpedTile possibleTile = group.getMember(x + y * mapWidth);
    				if(possibleTile.isRiver()) continue;
    				
    				if(possibleTile.getPrimaryType().isRiverPossible() && possibleTile.getSecondaryType().isRiverPossible() && UtilsMath.d16()) {
    					ArrayList<RiverSegment> path = generateRiverSegments(group, possibleTile.getCoords(), mapWidth, mapHeight);
    					if(path.size() <= 1) continue;
    					
    					for(int i = 0; i < path.size(); i++) {
    						 VectorI coord = path.get(i).getCoords();
    						 if(coord.x < 0 || coord.y < 0 || coord.x >= mapWidth || coord.y >= mapHeight) {
    							 if(i == 0) System.err.println("TileManager -> generateRivers() -> why is this outside map?");
    							 continue;
    						 }
    						 WarpedTile pathTile = group.getMember(coord.x + coord.y * mapWidth);
    						
    						 if(pathTile.isRiver()) pathTile.setRiver(path.get(i - 1).getNextSegmentDir());
    						 else pathTile.setRiver(path.get(i));
    					 }
    				}
    				
    			}
    		}
    				
    	}
    	
    	private ArrayList<RiverSegment> generateRiverSegments(WarpedGroup<WarpedTile> group, VectorI startCoords, int mapWidth, int mapHeight){
    		int MAX_PASSES = 10000;
    		int passes = 0;

    		ArrayList<RiverSegment> path = new ArrayList<>();
    		
    		path.add(new RiverSegment(startCoords, null));
    		
    		boolean isComplete = false;
    		while(!isComplete) {
    			passes++;
    			if(passes > MAX_PASSES) {
    				System.err.println("TileManager -> generateRiverPath() -> number of pass attempts has exceeded " + MAX_PASSES + ", unless intended path is this large there is probably an error, path should be calculated prior to this many attempts");
    				isComplete = true;
    				break;
    			}
    			
    			RiverSegment previousSegment = path.get(path.size() - 1);
    			VectorI previousCoord = previousSegment.getCoords();
    						
    			ArrayList<DirectionType> possibleDirections = new ArrayList<>();
    			
    			int lx = previousCoord.x - 1;
    			int ly = previousCoord.y;
    			
    			int ux = previousCoord.x;
    			int uy = previousCoord.y - 1;
    			
    			int rx = previousCoord.x + 1;
    			int ry = previousCoord.y;
    			
    			int dx = previousCoord.x;
    			int dy = previousCoord.y + 1;
    			
    			
    			
    			WarpedTile lTile = null;
    			if(!UtilsMath.containsCoordinate(path, lx, ly)) {				
    				if(lx < 0 || ly < 0 || lx >= mapWidth || ly >= mapHeight) possibleDirections.add(DirectionType.LEFT);
    				else {
    					lTile = group.getMember(lx + ly * mapWidth);					
    					if(lTile.isRiverPossible(DirectionType.LEFT)) possibleDirections.add(DirectionType.LEFT);
    				}
    			}

    			WarpedTile uTile = null;
    			if(!UtilsMath.containsCoordinate(path, ux, uy)) {				
    				if(ux < 0 || uy < 0 || ux >= mapWidth || uy >= mapHeight) possibleDirections.add(DirectionType.UP); 
    				else {
    					uTile = group.getMember(ux + uy * mapWidth);					
    					if(uTile.isRiverPossible(DirectionType.UP)) possibleDirections.add(DirectionType.UP);
    				}
    			}
    			
    			WarpedTile rTile = null;
    			if(!UtilsMath.containsCoordinate(path, rx, ry)) {				
    				if(rx < 0 || ry < 0 || rx >= mapWidth || ry >= mapHeight) possibleDirections.add(DirectionType.RIGHT);
    				else {
    					rTile = group.getMember(rx + ry * mapWidth);					
    					if(rTile.isRiverPossible(DirectionType.RIGHT)) possibleDirections.add(DirectionType.RIGHT);
    				}
    			}
    			
    			WarpedTile dTile = null;
    			if(!UtilsMath.containsCoordinate(path, dx, dy)) {				
    				if(dx < 0 || dy < 0 || dx >= mapWidth || dy >= mapHeight)  possibleDirections.add(DirectionType.DOWN); 
    				else {
    					dTile = group.getMember(dx + dy * mapWidth);					
    					if(dTile.isRiverPossible(DirectionType.DOWN)) possibleDirections.add(DirectionType.DOWN);
    				}			
    			}
    			
    			
    			if(possibleDirections.size() <= 0 || UtilsMath.d20()) {
    				if(path.size() > 1)	previousSegment.updateSegmentType();
    				isComplete = true;
    			} else {					
    				DirectionType pathDirection = possibleDirections.get(UtilsMath.randomInt(possibleDirections.size()));
    				switch(pathDirection) {
    				case DOWN:  
    					previousSegment.setNextSegment(DirectionType.DOWN);
    					previousSegment.updateSegmentType();
    					RiverSegment dSeg = new RiverSegment(new VectorI(dx, dy), DirectionType.UP);
    					path.add(dSeg);
    					if(dTile == null || dTile.isRiver()) {
    						isComplete = true;
    						dSeg.updateSegmentType();
    					}
    					break;			
    				case LEFT:
    					previousSegment.setNextSegment(DirectionType.LEFT);
    					previousSegment.updateSegmentType();
    					RiverSegment lSeg = new RiverSegment(new VectorI(lx, ly), DirectionType.RIGHT);
    					path.add(lSeg);
    					if(lTile == null || lTile.isRiver()) {
    						isComplete = true;
    						lSeg.updateSegmentType();
    					}
    					break;
    				case RIGHT: 
    					previousSegment.setNextSegment(DirectionType.RIGHT);
    					previousSegment.updateSegmentType();
    					RiverSegment rSeg = new RiverSegment(new VectorI(rx, ry), DirectionType.LEFT);
    					path.add(rSeg);
    					if(rTile == null || rTile.isRiver()) {
    						isComplete = true;
    						rSeg.updateSegmentType();
    					}
    					break;
    				case UP: 	
    					previousSegment.setNextSegment(DirectionType.UP);
    					previousSegment.updateSegmentType();
    					RiverSegment uSeg = new RiverSegment(new VectorI(ux, uy), DirectionType.DOWN);
    					path.add(uSeg);
    					if(uTile == null || uTile.isRiver()) {
    						isComplete = true;
    						uSeg.updateSegmentType();
    					}
    					break;
    				default:
    					System.err.println("TileManager -> generateRiverPath -> invalid case : " + pathDirection);
    				}
    			}
    		}
    		
    		//if(path.size() <= 1) System.out.println("TileManager -> generateRiverPath() -> river path is less than 2, river paths must be more thant 1 tile");
    		//System.out.println("TileManager() -> generateRiverSegments() -> generated river with " + path.size() + " segments");
    		return path;
    	}
    	*/
    	
}
