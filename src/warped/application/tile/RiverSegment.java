/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;

public class RiverSegment {

	
	private Vec2i coords;
	private DirectionType previousSegmentDir;
	private DirectionType nextSegmentDir;
	private RiverSegmentType segmentType;
	private boolean isInMap = true;
	
	public RiverSegment(Vec2i coords, DirectionType previousSegmentDir) {
		this.coords = coords;
		this.previousSegmentDir = previousSegmentDir;
	}
	
	public boolean isInMap() {return isInMap;}
	public RiverSegmentType getSegmentType() {return segmentType;}
	public Vec2i getCoords() {return coords;}

	public void setSegmentType(RiverSegmentType segmentType) {this.segmentType = segmentType;}
	public void setNextSegment(DirectionType nextSegmentDir) {this.nextSegmentDir = nextSegmentDir;}
	public void setOutsideMap() {isInMap = false;}
	
	public void updateSegmentType() {
		if(nextSegmentDir == null && previousSegmentDir == null) {
			Console.err("RiverSegment -> updateSegmentType() -> tried to updated an invalid case, both previous segment and next segment are null");
			return;
		}
		if(previousSegmentDir == null)  segmentType = RiverSegmentType.getStartingSegment(nextSegmentDir);
		else if(nextSegmentDir == null) segmentType = RiverSegmentType.getEndingSegment(previousSegmentDir);
		else segmentType = RiverSegmentType.getConnectingSegment(previousSegmentDir, nextSegmentDir);
	}
	
	public DirectionType getPreviousSegmentDir() { return previousSegmentDir;}
	public DirectionType getNextSegmentDir() {return nextSegmentDir;}
	
	
}
