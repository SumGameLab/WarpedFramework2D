/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils.path;

import warped.utilities.math.vectors.VectorI;

public class WarpedPathNode {

	public VectorI coord;
	public WarpedPathNode parent;
	public double fCost, gCost, hCost;
	public double smoothness = 1.0;  //Domain 0.0(exclusive) -> 1.0(inclusive) : values closer to 1.0 will have faster movement, values closer to 0.0 will have slower movement 
	
	public WarpedPathNode(VectorI coord, WarpedPathNode parent, double gCost, double hCost, double smoothness) {
		this.coord = coord;
		this.parent = parent; //node prior to this one in the path
		this.smoothness = smoothness;//moment speed
		this.gCost = gCost; //the cost to get from the start of the path to this node
		this.hCost = hCost; //approximate cost from this node to the end
		this.fCost = gCost + hCost; //sum cost
	}
	
	public WarpedPathNode(VectorI coord, WarpedPathNode parent, double gCost, double hCost) {
		this.coord = coord;
		this.parent = parent; 
		this.smoothness = 1.0;
		this.gCost = gCost; 
		this.hCost = hCost; 
		this.fCost = gCost + hCost; 
	}
}
	

