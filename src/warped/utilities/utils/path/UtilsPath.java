/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class UtilsPath {	
	
	private static Comparator<WarpedPathNode> nodeSorter = new Comparator<>() {
		public int compare(WarpedPathNode n1, WarpedPathNode n2) {
			if(n2.fCost > n1.fCost) return -1;
			if(n2.fCost < n1.fCost) return +1;
			return 0;
		}
	};
	
	public static boolean isPathPossible(boolean[] map, VectorI mapDim, VectorI start, VectorI goal) {if(findPath(map, mapDim, start, goal) == null) return false; else return true;}
	public static boolean isPathPossibleNoDiagonal(boolean[] map, VectorI mapDim, VectorI start, VectorI goal) {if(findPathNoDiagonal(map, mapDim, start, goal) == null) return false; else return true;}
	
	
	public static WarpedPath findPath(double[] map, VectorI mapDim, VectorI start, VectorI goal) {
		List<WarpedPathNode> openList = new ArrayList<>();
		List<WarpedPathNode> closeList = new ArrayList<>();
		
		WarpedPathNode current = new WarpedPathNode(start, null, 0, getDistance(start, goal), map[start.x() + start.y() * mapDim.x()]);
		openList.add(current);

		
		if(map[goal.x() + goal.y() * mapDim.x()] < 0.0) {
			Console.err("UtilsPath -> findPath() -> goal coord is invalid : " + goal.getString());
			return null;
		}
		
		while(openList.size() > 0) {
			
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			
			if(current.coord.isEqual(goal)) {
				List<WarpedPathNode> path = new ArrayList<>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closeList.clear();
				return new WarpedPath(path);
			}
			
			openList.remove(current);
			closeList.add(current);
			
			for(int i = 0; i < 9; i++) {
				if(i == 4) continue;

				int x = current.coord.x();
				int y = current.coord.y();
				
				int xi = (i % 3) -1;
				int yi = (i / 3) -1;
				
				VectorI checkCoord = new VectorI(x + xi, y + yi);
				if(checkCoord.x() >= mapDim.x() || checkCoord.y() >= mapDim.y()) continue; 
				else if(map[checkCoord.x() + checkCoord.y() * mapDim.x()] == 0.0) continue;				
				
				double smoothness = map[checkCoord.x() + checkCoord.y() * mapDim.x()];
				double gCost = current.gCost + getDistance(current.coord, checkCoord, smoothness);
				double hCost = getDistance(checkCoord, goal, smoothness);
				
				WarpedPathNode node = new WarpedPathNode(checkCoord, current, gCost, hCost, smoothness);
				if(isNodeInList(closeList, checkCoord) && gCost >= current.gCost) continue;
				if(!isNodeInList(openList, checkCoord) || gCost < current.gCost) openList.add(node);
				
				openList.add(node);
			}
		}
		
		closeList.clear();
		return null;	
	}
	
	
	public static WarpedPath findPath(boolean[] map, VectorI mapDim, VectorI start, VectorI goal){
		List<WarpedPathNode> openList = new ArrayList<>();
		List<WarpedPathNode> closeList = new ArrayList<>();
		
		WarpedPathNode current = new WarpedPathNode(start, null, 0, getDistance(start, goal));
		openList.add(current);

		if(map[goal.x() + goal.y() * mapDim.x()] == false) {
			Console.err("UtilsPath -> findPath() -> goal coord is invalid : " + goal.getString());
			return null;
		}
		
		while(openList.size() > 0) {
			
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			
			if(current.coord.isEqual(goal)) {
				List<WarpedPathNode> path = new ArrayList<>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closeList.clear();
				return new WarpedPath(path);
			}
			
			openList.remove(current);
			closeList.add(current);
			
			for(int i = 0; i < 9; i++) {
				if(i == 4) continue;

				int x = current.coord.x();
				int y = current.coord.y();
				
				int xi = (i % 3) -1;
				int yi = (i / 3) -1;
				
				VectorI checkCoord = new VectorI(x + xi, y + yi);
				if(map[checkCoord.x() + checkCoord.y() * mapDim.x()] == false) continue;				
				
				double gCost = current.gCost + getDistance(current.coord, checkCoord);
				double hCost = getDistance(checkCoord, goal);
				
				WarpedPathNode node = new WarpedPathNode(checkCoord, current, gCost, hCost);
				if(isNodeInList(closeList, checkCoord) && gCost >= current.gCost) continue;
				if(!isNodeInList(openList, checkCoord) || gCost < current.gCost) openList.add(node);
			}
		}
		
		closeList.clear();
		return null;
	}
	
	public static WarpedPath findPathNoDiagonal(boolean[] map, VectorI mapDim, VectorI start, VectorI goal){
		List<WarpedPathNode> openList = new ArrayList<>();
		List<WarpedPathNode> closeList = new ArrayList<>();
		
		//WarpedConsole.ln("UtilsPath -> findPathNoDiagonal -> looking for path from " + start.getString() + ", to " + goal.getString());
		
		if(start.x() < -1 || start.y() < -1 || start.x() >= mapDim.x() || start.y() >= mapDim.y()) {
			Console.err("UtilsPath -> findPath() -> start coord is invalid : " + start.getString());
			return null;
		}
		
		if(goal.x() < -1 || goal.y() < -1 || goal.x() >= mapDim.x() || goal.y() >= mapDim.y()) {
			Console.err("UtilsPath -> findPath() -> goal coord is invalid : " + goal.getString());
			return null;
		} else if(map[goal.x() + goal.y() * mapDim.x()] == false) {
			Console.err("UtilsPath -> findPath() -> goal coord is invalid : " + goal.getString());
			return null;
		}
		
		WarpedPathNode current = new WarpedPathNode(start, null, 0, getDistance(start, goal));
		openList.add(current);
		
		while(openList.size() > 0) {
			
		//	WarpedConsole.ln("Utils -> findPathNoDiagonal() -> looking for path");			
			
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			
			if(current.coord.isEqual(goal)) {
				List<WarpedPathNode> path = new ArrayList<>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closeList.clear();
				return new WarpedPath(path);
			}
			
			openList.remove(current);
			closeList.add(current);
			
			int x = current.coord.x();
			int y = current.coord.y();

			for(int i = 0; i < 4; i++) {
				
				int xi = 0, yi = 0;
				switch(i) {
				case 0:
					xi = -1;
					yi = 0;
					break;
				case 1:
					xi = 0;
					yi = 1;
					break;
				case 2:
					xi = 1; 
					yi = 0;
					break;
				case 3:
					xi = 0;
					yi = -1;
					break;
				}
				
				VectorI checkCoord = new VectorI(x + xi, y + yi);
				if(checkCoord.x() < 0 || checkCoord.y() < 0 || checkCoord.x() >= mapDim.x() || checkCoord.y() >= mapDim.y()) continue; 
				else if(map[checkCoord.x() + checkCoord.y() * mapDim.x()] == false) continue;				
				
				double gCost = current.gCost + (getDistance(current.coord, checkCoord) == 1 ? 1 : 0.95);
				double hCost = getDistance(checkCoord, goal);
				
				
				WarpedPathNode node = new WarpedPathNode(checkCoord, current, gCost, hCost);
				if(isNodeInList(closeList, checkCoord) && gCost >= current.gCost) continue;
				if(!isNodeInList(openList, checkCoord) || gCost < current.gCost) openList.add(node);
			}
		}
		
		closeList.clear();
		return null;
	}
	
	public static WarpedPath findPathNoDiagonal(double[] map, VectorI mapDim, VectorI start, VectorI goal){
		List<WarpedPathNode> openList = new ArrayList<>();
		List<WarpedPathNode> closeList = new ArrayList<>();
		
		//WarpedConsole.ln("UtilsPath -> findPathNoDiagonal -> looking for path from " + start.getString() + ", to " + goal.getString());
		
		if(start.x() < -1 || start.y() < -1 || start.x() >= mapDim.x() || start.y() >= mapDim.y()) {
			Console.err("UtilsPath -> findPath() -> start coord is invalid : " + start.getString());
			return null;
		}
		
		if(goal.x() < -1 || goal.y() < -1 || goal.x() >= mapDim.x() || goal.y() >= mapDim.y()) {
			Console.err("UtilsPath -> findPath() -> goal coord is invalid : " + goal.getString());
			return null;
		} else if(map[goal.x() + goal.y() * mapDim.x()] <= 0.0) {
			Console.err("UtilsPath -> findPath() -> goal coord is invalid : " + goal.getString());
			return null;
		}
		
		WarpedPathNode current = new WarpedPathNode(start, null, 0, getDistance(start, goal));
		openList.add(current);
		
		while(openList.size() > 0) {
			
		//	WarpedConsole.ln("Utils -> findPathNoDiagonal() -> looking for path");			
			
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			
			if(current.coord.isEqual(goal)) {
				List<WarpedPathNode> path = new ArrayList<>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closeList.clear();
				return new WarpedPath(path);
			}
			
			openList.remove(current);
			closeList.add(current);
			
			int x = current.coord.x();
			int y = current.coord.y();

			for(int i = 0; i < 4; i++) {
				
				int xi = 0, yi = 0;
				switch(i) {
				case 0:
					xi = -1;
					yi = 0;
					break;
				case 1:
					xi = 0;
					yi = 1;
					break;
				case 2:
					xi = 1; 
					yi = 0;
					break;
				case 3:
					xi = 0;
					yi = -1;
					break;
				}
				
				VectorI checkCoord = new VectorI(x + xi, y + yi);
				if(checkCoord.x() < 0 || checkCoord.y() < 0 || checkCoord.x() >= mapDim.x() || checkCoord.y() >= mapDim.y()) continue; 
				double roughness = map[checkCoord.x() + checkCoord.y() * mapDim.x()];
				if(roughness <= 0.0) continue;				
				
				double gCost = current.gCost + (getDistance(current.coord, checkCoord)) * roughness;
				double hCost = getDistance(checkCoord, goal) * roughness;
				
				
				WarpedPathNode node = new WarpedPathNode(checkCoord, current, gCost, hCost, roughness);
				if(isNodeInList(closeList, checkCoord) && gCost >= current.gCost) continue;
				if(!isNodeInList(openList, checkCoord) || gCost < current.gCost) openList.add(node);
			}
		}
		
		closeList.clear();
		return null;
	}
	
	
	private static boolean isNodeInList(List<WarpedPathNode> list, VectorI vec) {
		for(WarpedPathNode node : list) if(node.coord.isEqual(vec)) return true;
		return false;
	}
	
	private static double getDistance(VectorI v1, VectorI v2) {
 		double dx = v1.x() - v2.x();
		double dy = v1.y() - v2.y();
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	private static double getDistance(VectorI v1, VectorI v2, double scale) {
 		double dx = v1.x() - v2.x();
		double dy = v1.y() - v2.y();
		return (Math.sqrt(dx * dx + dy * dy) / scale);
	}
}
