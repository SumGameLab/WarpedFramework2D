/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils.path;

import java.util.ArrayList;
import java.util.List;

import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class WarpedPath {

	private int currentNode = 0;
	private List<WarpedPathNode> nodes;
	private ArrayList<VectorD>  positions  = new ArrayList<>();
	
	private double scale = 0.0;
	private VectorD offset = new VectorD();
	
	private boolean isReverseDirection = false;
	
	private boolean passedX = false;
	private boolean passedY = false;
	//private double moveSpeed = 0.0;
	
	protected WarpedPath(List<WarpedPathNode> nodes) {
		this.nodes = nodes;
		for(int i = 0; i < nodes.size(); i++) positions.add(new VectorD(nodes.get(i).coord));
		currentNode = nodes.size() -1;
	}
	
	/*
	protected WarpedPath(List<WarpedPathNode> nodes, double moveSpeed) {
		this.nodes = nodes;
		this.moveSpeed = moveSpeed;
		for(int i = 0; i < nodes.size(); i++) positions.add(new VectorD(nodes.get(i).coord));	
	}
	*/
	
	public boolean isReverseDirection() {return isReverseDirection;}
	public void setReverseDirection(boolean isReversedDirection) {this.isReverseDirection = isReversedDirection;}
	
	public VectorI getNextCoordinate() {
		if(currentNode < 0 || nodes.get(currentNode) == null) { 
			Console.err("WarpedPath -> getNextCoordinate() -> nodes is empty or node is null");
			return null;
		}
		return nodes.get(currentNode).coord;
	}
	
	public VectorI getCoordinateAfterNext() {
		if(currentNode < 1) {
			Console.err("WarpedPath -> getCoordinateAfterNext() -> path does not have a node after next");
			return null;
		}
		if(nodes.get(currentNode - 1) == null) { 
			Console.err("WarpedPath -> getCoordianteAfterNext() -> node after next is null");
			return null;
		}
		return nodes.get(currentNode - 1).coord;
	}
	
	public VectorD getNextPosition() {
		if(currentNode < 0 || positions.get(currentNode) == null) {
			Console.err("WarpedPath -> getNextPosition() -> coord is empty or null");
			return null;
		}
		return positions.get(currentNode);
	}
	
	public VectorD getPositionAfterNext() {
		if(currentNode < 1) {
			Console.err("WarpedPath -> getPositionAfterNext() -> path does not have a coordinate after next");
			return null;
		}
		if(positions.get(currentNode - 1) == null) {
			Console.err("WarpedPatth -> getPositionAfterNext() -> coordinate after next is null");
			return null;
		}
		return positions.get(currentNode - 1);
	}
	
	public WarpedPathNode getNextNode() {
		if(currentNode < 0 || nodes.get(currentNode) == null) { 
			Console.err("WarpedPath -> getNextNode() -> nodes is empty or node is null");
			return null;
		}
		return nodes.get(currentNode);
	}
	
	public WarpedPathNode getNodeAfterNext() {
		if(currentNode < 1) {
			Console.err("WarpedPath -> getNodeAfterNext() -> path does not have a node after next");
			return null;
		}
		if(nodes.get(currentNode - 1) == null) { 
			Console.err("WarpedPath -> getNodeAfterNext() -> node after next is null");
			return null;
		}
		return nodes.get(currentNode - 1);
	}
	
	public void passedNext() {
		if(isReverseDirection) currentNode++;
		else currentNode--;
		
		if(currentNode >= nodes.size()) {
			Console.ln("WarpedPath -> passedNext() -> path passed start");
			currentNode = nodes.size() - 1;
		}
	}
	
	public int getCurrentNode() {return currentNode;}
	
	
	public List<WarpedPathNode> getNodes(){return nodes;}
	public ArrayList<VectorD> getCoordinates(){return positions;}
	
	public void scalePath(int scale) {scalePath((double)scale);}
	public void scalePath(double scale){
		if(this.scale != 0.0) {
			Console.err("WarpedPath -> scalePath() -> pathScale has already been set");
			return;
		}
		this.scale = scale;
		positions.forEach(c ->{c.scale(scale);});
	}
	public void offsetPath(VectorI offset) {
		this.offset.set(offset);		
		offsetPath(offset.x(), offset.y());
	}
	public void offsetPath(VectorD offset) {
		this.offset.set(offset);
		offsetPath(offset.x(), offset.y());
	}
	public void offsetPath(int v) {
		this.offset.set(v);
		offsetPath(v,v);
	}
	public void offsetPath(int x, int y) {
		this.offset.set(x, y);
		positions.forEach(c -> {c.add(x, y);});
	}
	public void offsetPath(double x, double y) {positions.forEach(c -> {c.add(x, y);});}
	
	public <T extends WarpedPathable> boolean follow(T pathable, double moveValue) {return follow(this, pathable, scale / moveValue);}
	public <T extends WarpedPathable> boolean follow(T pathable) {return follow(this, pathable, scale);}
	public  <T extends WarpedPathable> boolean follow(WarpedPath path, T pathable, double scale) {
		if(path.getCurrentNode() < 0) return true;
		VectorD target = path.getNextPosition();
		if(target == null) return true;
		
		double px = pathable.getPosition().x();
		double py = pathable.getPosition().y();

		
		if(px == target.x()) passedX = true;
		else if(!passedX){			
			if(px < target.x()) {
				pathable.getPosition().add( pathable.getMoveSpeed() * path.getNextNode().smoothness);
				if(pathable.getPosition().x() >= target.x()) passedX = true; 
			}
			if(px > target.x()) {
				pathable.getPosition().add(-pathable.getMoveSpeed() * path.getNextNode().smoothness);
				if(pathable.getPosition().x() <= target.x()) passedX = true; 
			}
		}
		if(py == target.y()) passedY = true;
		else if(!passedY){			
			if(py < target.y()) {
				pathable.getPosition().add(0.0, pathable.getMoveSpeed() * path.getNextNode().smoothness);
				if(pathable.getPosition().y() >= target.y()) passedY = true;
			}
			if(py > target.y()) {
				pathable.getPosition().add(0.0, -pathable.getMoveSpeed() * path.getNextNode().smoothness);
				if(pathable.getPosition().y() <= target.y()) passedY = true;
			}
		}
		
						
		if(target.getDistanceBetweenVectors(pathable.getPosition()) < (scale / 2.0)) {
			if(!pathable.getCurrentCoordinate().isEqual(path.getNextCoordinate())) {				
				pathable.setCurrentCoordinate(path.getNextCoordinate()); //Set coordinate half way between each tile, otherwise crew will appear to be in the next tile while being effected by the previous tile
			}
		}
		
		if(passedY && passedX) {
			passedY = false;
			passedX = false;
			path.passedNext();
		}
		return false;
	}
}
