/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import warped.application.state.WarpedGroup;
import warped.application.state.WarpedGroupIdentity;
import warped.application.state.WarpedState;
import warped.graphics.sprite.spriteSheets.WarpedSpriteSheet;
import warped.utilities.utils.UtilsMath;

public class  TileSetGenerative<T extends TileableGenerative<? extends Enum<?>>> {
	
	protected boolean valid;
	
	protected String name = "default";
	protected T setType;
	protected int setIdentity;
	private static int setCount = 0;
	
	protected ArrayList<T> tileTypes; 
	protected WarpedSpriteSheet tileSheet;
	protected HashMap<T, BufferedImage> tileImages = new HashMap<>();	
		
	
	public TileSetGenerative(T setType,  WarpedSpriteSheet tileSheet ) {
		this.tileSheet = tileSheet;
		this.setType = setType;
		this.name = setType.getClass().getSimpleName();
		setCount++;
		setIdentity = setCount;
		

	
	}
	

	//--------
	//------------------- Access ---------------------
	//--------	
	public String getName() {return name;}
	public int getTileWidth() {return tileSheet.getSpriteWidth();}	
	public int getTileHeight() {return tileSheet.getSpriteHeight();}	
	public BufferedImage getTileImage(T tileType) {return tileImages.get(tileType);}		
	public T getRandomType() {return tileTypes.get(UtilsMath.random.nextInt(tileTypes.size()));}
	public int getSetID() {return setIdentity;}
	public boolean isMatchingSet(TileSetGenerative<?> set) {if(set.getSetID() == setIdentity) return true; else return false;}
	public TileGenerative<T> generateTile(T tileType) {return new TileGenerative<T>(this, tileType);}
	public TileGenerative<T> generateRandomTile() {return new TileGenerative<T>(this, getRandomType());}
	
	
	//--------
	//------------------- Generate Tile Maps ---------------------
	//--------	
	
	public WarpedGroupIdentity generateRandomTileMap(int mapWidth, int mapHeight) {
		if(mapWidth <= 0 || mapHeight <= 0) {
			System.err.println("TileManager -> generateRandomTileMap() -> map width/height must be positive (mapWidth, mapHeight)  :  (" + mapWidth + ", " + mapHeight +")");
			return null;
		}
		
		
		WarpedGroupIdentity groupID = WarpedState.tileManager.addGroup();
		
		WarpedGroup<WarpedTile> group = WarpedState.tileManager.getGroup(groupID);
		
		for(int y = 0; y < mapHeight; y++) {
			for(int x = 0; x < mapWidth; x++) {
				WarpedTile tile = generateRandomTile();
				tile.setPosition(x * getTileWidth(), y * getTileHeight());
				tile.setCoordinates(x, y, x + (y * mapWidth));
				group.addMember(tile);
			}
		}
		
		group.setMapSize(mapWidth, mapHeight);
		group.setPixelSize(mapWidth * getTileWidth(), mapHeight * getTileHeight());
		group.setMemberSize(getTileWidth(), getTileHeight());
		return groupID;
	}
	
	public  WarpedGroupIdentity generateContiguousTileMap(T tileType, int mapWidth, int mapHeight) {
		if(mapWidth <= 0 || mapHeight <= 0) {
			System.err.println("TileManager -> generateContiguousTileMap() -> map width/height must be positive (mapWidth, mapHeight)  :  (" + mapWidth + ", " + mapHeight +")");
			return null;
		}
		
		WarpedGroupIdentity groupID = WarpedState.tileManager.addGroup();
		WarpedGroup<WarpedTile> group = WarpedState.tileManager.getGroup(groupID);
		
		for(int y = 0; y < mapHeight; y++) {
			for(int x = 0; x < mapWidth; x++) {
				WarpedTile tile = generateTile(tileType);
				tile.setPosition(x * getTileWidth(), y * getTileHeight());
				tile.setCoordinates(x, y, x + (y * mapWidth));
				group.addMember(tile);
			}
		}
		return groupID;
	}		
	
	
}
