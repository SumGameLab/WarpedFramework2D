/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import java.util.ArrayList;

import warped.application.object.WarpedObject;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.tile.RiverSegment;
import warped.application.tile.TileType;
import warped.application.tile.WarpedTile;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class ManagerTile<T extends WarpedObject> extends WarpedManager<T> {


	protected ManagerTile() {
		managerType = WarpedManagerType.TILE;		
	}
	
	 
	//--------
	//------------------- Access ---------------------
	//--------
	public WarpedTile getTile(WarpedGroupIdentity groupID, int x, int y) {
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);		
		if(x < 0 || y < 0 || x >= group.getMapGridSize().x || y >= group.getMapGridSize().y) {
			Console.err("TileManager -> getTile() -> tile coordinates are outside of bounds : " + group.getMapGridSize().getString());
			return null;
		}
		
		WarpedTile result = group.getMember(x + (y * group.getMapGridSize().x));
		if(result == null) {
			Console.err("TileManager -> getTile() -> getMember() returned a null tile from the group");
			return null;
		} else return result;
	} 
	
	public double[] getRoughnessGrid(WarpedGroupIdentity groupID) {
		if(groupID.getManagerType() != WarpedManagerType.TILE) {
			Console.err("TileManager -> getCollisionGrid() -> group is not a tile groupd");
			return null;
		}
		
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);
		int width  = group.getMapGridSize().x;
		int height = group.getMapGridSize().y;
		double [] result = new double[width * height];
		
		
		for(int i = 0; i < group.size(); i++) result[i] = group.getMember(i).getRoughness();		
		return result;
	}
	
	public WarpedTile[] getTiles(WarpedGroupIdentity groupID) {
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);		
		int width  = group.getMapGridSize().x;
		int height = group.getMapGridSize().y;
		WarpedTile [] result = new WarpedTile[width * height];
		
		for(int i = 0; i < group.getMemberCount(); i++) {
			result[i] = group.getMember(i);
		}
		return result;
	}
	
	public WarpedTile[][] getTiles2D(WarpedGroupIdentity groupID) {
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);		
		int width  = group.getMapGridSize().x;
		int height = group.getMapGridSize().y;
		WarpedTile [][] result = new WarpedTile[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				result[x][y] = getTile(groupID, x, y);
			}
		}
		return result;
	}
	
	//TODO make getter for input class type
	/*
	public ArrayList<TileCelestial> getCelestialTiles(WarpedGroupIdentity groupID){
		ArrayList<TileCelestial> result = new ArrayList<>();
		ArrayList<T> tiles = getGroup(groupID).getMembers();
		for(int i = 0; i < tiles.size(); i++){
			if(tiles.get(i) instanceof TileCelestial) {
				TileCelestial ct = (TileCelestial) tiles.get(i);
				result.add(ct);
			} else Console.err("TileManager -> getCelestialTiles() -> tile is not a CelestialTile -> group : " + groupID.getString());
		}
		return result;
	}
	*/
	
	
	//--------
	//------------------- Find ---------------------
	//--------
	public Vec2i findNearestPrimary(WarpedGroupIdentity groupID, TileType type, int xCord, int yCord) {
		return findNearestInstance(groupID,type,xCord,yCord, true);
	}
	public Vec2i findNearestSecondary(WarpedGroupIdentity groupID, TileType type, int xCord, int yCord) {
		return findNearestInstance(groupID,type,xCord,yCord, false);
	}
	
	public WarpedTile findNearestTileBySecondary(WarpedGroupIdentity groupID, TileType type, Vec2i vec) {
		Vec2i pos = findNearestSecondary(groupID, type, vec.x, vec.y);
		return getTile(groupID, pos.x, pos.y);
	}
	
	public WarpedTile findNearestTileBySecondary(WarpedGroupIdentity groupID, TileType type, int xCord, int yCord) {
		Vec2i pos = findNearestSecondary(groupID, type, xCord, yCord);
		return getTile(groupID, pos.x, pos.y);
	}
	
	public WarpedTile findNearestTileByPrimary(WarpedGroupIdentity groupID, TileType type, int xCord, int yCord) {
		Vec2i pos = findNearestPrimary(groupID, type, xCord, yCord);
		return getTile(groupID, pos.x, pos.y);
	}
	
	private Vec2i findNearestInstance(WarpedGroupIdentity groupID, TileType type, int xCord, int yCord, boolean primary) {
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);		
		WarpedTile tile = getTile(groupID, xCord, yCord);
		
		if(group == null || tile == null) {
			Console.err("TileManager -> findNearestInstance() -> group or tile is null");
			return null;
		}
		
		int width = group.getMapGridSize().x;
		int height = group.getMapGridSize().y;
		
		Vec2i nearest = null;
		double nearestDif = 0.0;
		for(int y = 0; y < width; y++) {
			for(int x = 0; x < height; x++) {
				
				WarpedTile checkTile = group.getMember(x + (y * width));
				TileType checkType;
				if(primary)checkType = checkTile.getPrimaryType(); else checkType = checkTile.getSecondaryType();
				if(checkType == type) {
					
					if(nearest == null) {
						nearest = new Vec2i(x, y);
						nearestDif = UtilsMath.vectorDifference(xCord, yCord, x, y);
						continue;
					} else {
						double checkDif = UtilsMath.vectorDifference(xCord, yCord, x, y);
						if(checkDif < nearestDif) {
							nearest.set(x, y);
							nearestDif = checkDif;
						}
					}
				
				} else continue;
			
			}
		}
		
		if(nearest == null) {
			Console.err("TileManager -> findNearestInstance() -> the set contains no instances of the tile type : " + type);
			return null;
		}
		
		return nearest;
	}
	
	
	//--------
	//------------------- Generate Tile Maps ---------------------
	//--------
	/*
	public static ContextGroupIdentity generateRandomTileMap(TileSet set, int mapWidth, int mapHeight) {
		if(!set.isValid()) {
			Console.err("TileManager -> generateRandomTileMap() -> set is not valid ");
			return null;
		}
		if(mapWidth <= 0 || mapHeight <= 0) {
			Console.err("TileManager -> generateRandomTileMap() -> map width/height must be positive (mapWidth, mapHeight)  :  (" + mapWidth + ", " + mapHeight +")");
			return null;
		}

				
		ContextGroupIdentity groupID = GameState.tileManager.addGroup();
		ContextGroup<TileObject> group = GameState.tileManager.getGroup(groupID);
		
		for(int y = 0; y < mapHeight; y++) {
			for(int x = 0; x < mapWidth; x++) {
				TileObject tile = set.generateRandomTile();
				tile.setPosition(x * set.getTileWidth(), y * set.getTileHeight());
				tile.setCoordinates(x, y, x + (y * mapWidth));
				group.addMember(tile);
			}
		}
		
		group.setMapSize(mapWidth, mapHeight);
		group.setPixelSize(mapWidth * set.getTileWidth(), mapHeight * set.getTileHeight());
		group.setMemberSize(set.getTileWidth(), set.getTileHeight());
		return groupID;
	}
	
	public static ContextGroupIdentity generateRandomTileMap(TileSet set, TileClassType classType, int mapWidth, int mapHeight, String name) {
		if(!set.isValid()) {
			Console.err("TileManager -> generateRandomTileMap() -> set is not valid ");
			return null;
		}
		if(mapWidth <= 0 || mapHeight <= 0) {
			Console.err("TileManager -> generateRandomTileMap() -> map width/height must be positive (mapWidth, mapHeight)  :  (" + mapWidth + ", " + mapHeight +")");
			return null;
		}
				
		ContextGroupIdentity groupID = GameState.tileManager.addGroup(name);
		ContextGroup<TileObject> group = GameState.tileManager.getGroup(groupID);
		
		for(int y = 0; y < mapHeight; y++) {
			for(int x = 0; x < mapWidth; x++) {
				switch(classType) {
				case CELESTIAL_TILE:
					CelestialTile cTile = set.generateRandomCelestialTile();
					cTile.setPosition(x * set.getTileWidth(), y * set.getTileHeight());
					cTile.setCoordinates(x, y, x + (y * mapWidth));
					group.addMember(cTile);
					break;
				case TILE:
					TileObject tile = set.generateRandomTile();
					tile.setPosition(x * set.getTileWidth(), y * set.getTileHeight());
					tile.setCoordinates(x, y, x + (y * mapWidth));
					group.addMember(tile);
					break;
				default:
					break;
				
				}
			}
		}
		
		group.setMapSize(mapWidth, mapHeight);
		group.setPixelSize(mapWidth * set.getTileWidth(), mapHeight * set.getTileHeight());
		group.setMemberSize(set.getTileWidth(), set.getTileHeight());
		return groupID;
	}	
	
	public static ContextGroupIdentity generateContiguousTileMap(TileSet set, TileType tileType, TileClassType classType, int mapWidth, int mapHeight) {
		if(!set.isValid()) {
			Console.err("TileManager -> generateContiguousTileMap() -> set is not valid ");
			return null;
		}
		if(mapWidth <= 0 || mapHeight <= 0) {
			Console.err("TileManager -> generateContiguousTileMap() -> map width/height must be positive (mapWidth, mapHeight)  :  (" + mapWidth + ", " + mapHeight +")");
			return null;
		}
		if(!set.containsTileType(tileType)) {
			Console.err("TileManager -> generateContiguousTileMap() -> set does not contain the tile type : " + tileType);
			return null;
		}
		
		ContextGroupIdentity groupID = GameState.tileManager.addGroup();
		ContextGroup<TileObject> group = GameState.tileManager.getGroup(groupID);
		
		for(int y = 0; y < mapHeight; y++) {
			for(int x = 0; x < mapWidth; x++) {
				switch(classType) {
				case TILE:
					TileObject tile = set.generateTile(tileType);
					tile.setPosition(x * set.getTileWidth(), y * set.getTileHeight());
					tile.setCoordinates(x, y, x + (y * mapWidth));
					group.addMember(tile);
					break;
					
				case CELESTIAL_TILE:
					CelestialTile cTile = set.generateCelestialTile(tileType);
					cTile.setPosition(x * set.getTileWidth(), y * set.getTileHeight());
					cTile.setCoordinates(x, y, x + (y * mapWidth));
					group.addMember(cTile);
					break;
				default:
					break;
				
				}
			}
		}
		return groupID;
	}	
	*/
	
	/*
	public WarpedGroupIdentity generateCelestialTileMap(EntitieCelestial parent, WarpedTileSet set, int mapWidth, int mapHeight, String name) {
		if(!set.isValid()) {
			Console.err("TileManager -> generateCollapseWaveTileMap() -> set is not valid ");
			return null;
		}
		if(mapWidth <= 0 || mapHeight <= 0) {
			Console.err("TileManager -> generateCollapseWaveTileMap() -> map width/height must be positive (mapWidth, mapHeight)  :  (" + mapWidth + ", " + mapHeight +")");
			return null;
		}
		WarpedGroupIdentity groupID = WarpedState.tileManager.addGroup(name);
		WarpedGroup<WarpedTile> group = WarpedState.tileManager.getGroup(groupID);
		
		group.setMapSize(mapWidth, mapHeight);
		group.setPixelSize(mapWidth * set.getTileWidth(), mapHeight * set.getTileHeight());
		group.setMemberSize(set.getTileWidth(), set.getTileHeight());
	
		for(int y = 0; y < mapHeight; y++) {
			for(int x = 0; x < mapWidth; x++) {
				
				if(x == 0 && y == 0) {
					TileCelestial tile = set.generatePrimaryCelestialTile(parent);
					tile.setPosition(x * set.getTileWidth(), y * set.getTileHeight());
					tile.setCoordinates(x, y, x + y * mapWidth);
					group.addMember(tile);
					continue;
				}
									
				WarpedTile left = null;
				WarpedTile above = null;
				WarpedTile aboveRight = null;
				if(x != 0) left  = getTile(groupID, x - 1, y);
				if(y != 0) above = getTile(groupID, x, y - 1);
				if(y != 0 && x != mapWidth - 1) aboveRight = getTile(groupID, x + 1, y - 1);
								
				TileCelestial tile = TileTransitionType.getConnectingTile(parent, left, above, aboveRight);
				tile.setPosition(x * set.getTileWidth(), y * set.getTileHeight());
				tile.setCoordinates(x, y, x + y * mapWidth);
				group.addMember(tile);

			}
		}
			
		generateRivers(groupID);
		return groupID;		
	}
	*/
	
	
	public void generateRivers(WarpedGroupIdentity groupID) {
		if(groupID.getManagerType() != WarpedManagerType.TILE) { 
			Console.err("TileManager -> generateRivers() -> the input group is not a group of tiles, it's a group of : " + groupID.getManagerType());
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
						 Vec2i coord = path.get(i).getCoords();
						 if(coord.x < 0 || coord.y < 0 || coord.x >= mapWidth || coord.y >= mapHeight) {
							 if(i == 0) Console.err("TileManager -> generateRivers() -> why is this outside map?");
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
	
	private ArrayList<RiverSegment> generateRiverSegments(WarpedGroup<WarpedTile> group, Vec2i startCoords, int mapWidth, int mapHeight){
		int MAX_PASSES = 10000;
		int passes = 0;

		ArrayList<RiverSegment> path = new ArrayList<>();
		
		path.add(new RiverSegment(startCoords, null));
		
		boolean isComplete = false;
		while(!isComplete) {
			passes++;
			if(passes > MAX_PASSES) {
				Console.err("TileManager -> generateRiverPath() -> number of pass attempts has exceeded " + MAX_PASSES + ", unless intended path is this large there is probably an error, path should be calculated prior to this many attempts");
				isComplete = true;
				break;
			}
			
			RiverSegment previousSegment = path.get(path.size() - 1);
			Vec2i previousCoord = previousSegment.getCoords();
						
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
				DirectionType pathDirection = possibleDirections.get(UtilsMath.random(possibleDirections.size()));
				switch(pathDirection) {
				case DOWN:  
					previousSegment.setNextSegment(DirectionType.DOWN);
					previousSegment.updateSegmentType();
					RiverSegment dSeg = new RiverSegment(new Vec2i(dx, dy), DirectionType.UP);
					path.add(dSeg);
					if(dTile == null || dTile.isRiver()) {
						isComplete = true;
						dSeg.updateSegmentType();
					}
					break;			
				case LEFT:
					previousSegment.setNextSegment(DirectionType.LEFT);
					previousSegment.updateSegmentType();
					RiverSegment lSeg = new RiverSegment(new Vec2i(lx, ly), DirectionType.RIGHT);
					path.add(lSeg);
					if(lTile == null || lTile.isRiver()) {
						isComplete = true;
						lSeg.updateSegmentType();
					}
					break;
				case RIGHT: 
					previousSegment.setNextSegment(DirectionType.RIGHT);
					previousSegment.updateSegmentType();
					RiverSegment rSeg = new RiverSegment(new Vec2i(rx, ry), DirectionType.LEFT);
					path.add(rSeg);
					if(rTile == null || rTile.isRiver()) {
						isComplete = true;
						rSeg.updateSegmentType();
					}
					break;
				case UP: 	
					previousSegment.setNextSegment(DirectionType.UP);
					previousSegment.updateSegmentType();
					RiverSegment uSeg = new RiverSegment(new Vec2i(ux, uy), DirectionType.DOWN);
					path.add(uSeg);
					if(uTile == null || uTile.isRiver()) {
						isComplete = true;
						uSeg.updateSegmentType();
					}
					break;
				default:
					Console.err("TileManager -> generateRiverPath -> invalid case : " + pathDirection);
				}
			}
		}
		
		//if(path.size() <= 1) Console.ln("TileManager -> generateRiverPath() -> river path is less than 2, river paths must be more thant 1 tile");
		//Console.ln("TileManager() -> generateRiverSegments() -> generated river with " + path.size() + " segments");
		return path;
	}
	
	
	

	
	
}
