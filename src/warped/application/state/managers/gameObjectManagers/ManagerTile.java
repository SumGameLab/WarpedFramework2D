/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers.gameObjectManagers;

import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.tile.WarpedTile;
import warped.application.tile.TileableGenerative;
import warped.utilities.utils.Console;

public class ManagerTile<T extends WarpedTile> extends WarpedManager<T> {


	protected ManagerTile() {
		managerType = WarpedManagerType.TILE;		
	}
	
	 
	//--------
	//------------------- Access ---------------------
	//--------
	@SuppressWarnings("unchecked")
	public <K extends TileableGenerative<? extends Enum<?>>> WarpedTile getTile(WarpedGroupIdentity groupID, int x, int y) {
		WarpedGroup<WarpedTile> group = WarpedState.tileManager.getGroup(groupID);		
		if(x < 0 || y < 0 || x >= group.getMapGridSize().x() || y >= group.getMapGridSize().y()) {
			Console.err("TileManager -> getTile() -> tile coordinates are outside of bounds : " + group.getMapGridSize().getString());
			return null;
		}
		
		WarpedTile result = group.getMember(x + (y * group.getMapGridSize().x()));
		if(result == null) {
			Console.err("TileManager -> getTile() -> getMember() returned a null tile from the group");
			return null;
		} else return (WarpedTile) result;
	} 
	
	
	public double[] getRoughnessGrid(WarpedGroupIdentity groupID) {
		if(groupID.getManagerType() != WarpedManagerType.TILE) {
			Console.err("TileManager -> getCollisionGrid() -> group is not a tile groupd");
			return null;
		}
		
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);
		int width  = group.getMapGridSize().x();
		int height = group.getMapGridSize().y();
		double [] result = new double[width * height];
		
		
		for(int i = 0; i < group.size(); i++) result[i] = group.getMember(i).getRoughness();		
		return result;
	}
	
	public WarpedTile[] getTiles(WarpedGroupIdentity groupID) {
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);		
		int width  = group.getMapGridSize().x();
		int height = group.getMapGridSize().y();
		WarpedTile [] result = new WarpedTile[width * height];
		
		for(int i = 0; i < group.getMemberCount(); i++) {
			result[i] = group.getMember(i);
		}
		return result;
	}
	
	public WarpedTile[][] getTiles2D(WarpedGroupIdentity groupID) {
		@SuppressWarnings("unchecked")
		WarpedGroup<WarpedTile> group = (WarpedGroup<WarpedTile>) getGroup(groupID);		
		int width  = group.getMapGridSize().x();
		int height = group.getMapGridSize().y();
		WarpedTile [][] result = new WarpedTile[width][height];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				result[x][y] = getTile(groupID, x, y);
			}
		}
		return result;
	}
		

	//--------
	//------------------- Find ---------------------
	//--------
	
	/*
	public <K extends WarpedTileable<? extends Enum<?>>> Vec2i findNearestPrimary(WarpedGroupIdentity groupID, K type, int xCord, int yCord) {
		return findNearestInstance(groupID,type,xCord,yCord, true);
	}
	public <K extends WarpedTileable<? extends Enum<?>>> Vec2i findNearestSecondary(WarpedGroupIdentity groupID, K type, int xCord, int yCord) {
		return findNearestInstance(groupID,type,xCord,yCord, false);
	}
	
	public <K extends WarpedTileable<? extends Enum<?>>> WarpedTile<?> findNearestTileBySecondary(WarpedGroupIdentity groupID, K type, Vec2i vec) {
		Vec2i pos = findNearestSecondary(groupID, type, vec.x, vec.y);
		return getTile(groupID, pos.x, pos.y);
	}
	
	public <K extends WarpedTileable<? extends Enum<?>>> WarpedTile<?> findNearestTileBySecondary(WarpedGroupIdentity groupID, K type, int xCord, int yCord) {
		Vec2i pos = findNearestSecondary(groupID, type, xCord, yCord);
		return getTile(groupID, pos.x, pos.y);
	}
	
	public <K extends WarpedTileable<? extends Enum<?>>> WarpedTile<?> findNearestTileByPrimary(WarpedGroupIdentity groupID, K type, int xCord, int yCord) {
		Vec2i pos = findNearestPrimary(groupID, type, xCord, yCord);
		return getTile(groupID, pos.x, pos.y);
	}
	private <K extends WarpedTileable<? extends Enum<?>>> Vec2i findNearestInstance(WarpedGroupIdentity groupID, K type, int xCord, int yCord, boolean primary) {
		WarpedGroup<WarpedTile<K>> group = (WarpedGroup<WarpedTile<K>>) getGroup(groupID);		
		WarpedTile<K> tile = (WarpedTile<K>) getTile(groupID, xCord, yCord);
		
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
				
				WarpedTile<K> checkTile = group.getMember(x + (y * width));
				K checkType;
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
	*/
	
	
	
	
}
