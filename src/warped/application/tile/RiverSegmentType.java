/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.utils.Console;

public enum RiverSegmentType {
	
	NONE,
	STRAIGHT_1,
	STRAIGHT_2,
	
	CORNER_1,
	CORNER_2,
	CORNER_3,
	CORNER_4,
	
	TRIBUTARY_1,
	TRIBUTARY_2,
	TRIBUTARY_3,
	TRIBUTARY_4,
	
	DELTA_1,
	DELTA_2,
	DELTA_3,
	DELTA_4,
	
	START_1,
	START_2,
	START_3,
	START_4;
	

	
	public int getSegmentRotation() {return getSegmentRotation(this);}
	public static int getSegmentRotation(RiverSegmentType riverSegment) {
		switch(riverSegment) {
		case START_1:
		case STRAIGHT_1:
		case CORNER_1:
		case TRIBUTARY_1:
		case DELTA_1:
			return 0;
			
		case START_2:
		case STRAIGHT_2:
		case CORNER_2:
		case TRIBUTARY_2:
		case DELTA_2:
			return 90;
			
		case CORNER_3:
		case START_3:
		case TRIBUTARY_3:
		case DELTA_3:
			return 180;
			
		case START_4:	
		case CORNER_4:			
		case TRIBUTARY_4:
		case DELTA_4:
			return 270;
		default:
			Console.err("RiverSegmentType -> getSegmentRotation() -> invalidCase " + riverSegment);
			return 0;
		
		}
	}
	
	public int getSegementIndex() {return getSegmentIndex(this);}
	public static int getSegmentIndex(RiverSegmentType segmentType) {
		switch(segmentType) {
		case START_1: case START_2: case START_3: case START_4:					return 0;
		case STRAIGHT_1: case STRAIGHT_2: 										return 1; 
		case CORNER_1: case CORNER_2: case CORNER_3: case CORNER_4: 		    return 2;
		case TRIBUTARY_1: case TRIBUTARY_2: case TRIBUTARY_3: case TRIBUTARY_4: return 3;
		case DELTA_1: case DELTA_2: case DELTA_3: case DELTA_4: 				return 4;
		default:
			Console.err("RiverSegmentType -> getSegmentIndex() -> invalid type : " + segmentType);
			return -1;		
		}
	}
	
	
	public static RiverSegmentType getTributarySegment(DirectionType joiningDirection) {
		switch(joiningDirection) {
		case LEFT: 	return TRIBUTARY_3;
		case RIGHT:	return TRIBUTARY_1;
		case UP:	return TRIBUTARY_4;
		case DOWN: 	return TRIBUTARY_2;
		default:
			Console.err("RiverSegmentType -> getTributarySegment() -> invalid case : " + joiningDirection);
			return RiverSegmentType.NONE;
		}
	}
	
	public static RiverSegmentType getEndingSegment(DirectionType previousSegLocation) {
		switch(previousSegLocation) {
		case LEFT: 	return DELTA_1;
		case UP:   	return DELTA_2;
		case RIGHT: return DELTA_3;
		case DOWN: 	return DELTA_4;
		default:
			Console.err("RiverSegmentType -> getEndingSegment() -> invalid case : " + previousSegLocation);
			return RiverSegmentType.NONE;
		}
	}
	
	public static RiverSegmentType getStartingSegment(DirectionType nextSegLocation) {
		switch(nextSegLocation) {
		case LEFT:	return START_1;
		case UP:	return START_2;
		case RIGHT:	return START_3;
		case DOWN:	return START_4;
		default:
			Console.err("RiverSegmentType -> getEndingSegment() -> invalid case : " + nextSegLocation);
			return RiverSegmentType.NONE;
		}
	}
	
	public static RiverSegmentType getConnectingSegment(DirectionType previousSegLocation, DirectionType nextSegLocation) {
		switch(previousSegLocation) {
		case LEFT:
			switch(nextSegLocation) {
			case UP: 	return CORNER_1;
			case RIGHT: return STRAIGHT_1;
			case DOWN:	return CORNER_4;
			default: break;
			}
		case UP:
			switch(nextSegLocation) {
			case RIGHT:	return CORNER_2;
			case DOWN:	return STRAIGHT_2;
			case LEFT:	return CORNER_1;
			default: break;
			}
		case RIGHT:
			switch(nextSegLocation) {
			case DOWN:	return CORNER_3; 
			case LEFT:	return STRAIGHT_1;
			case UP:	return CORNER_2;
			default: break;
			}
		case DOWN:
			switch(nextSegLocation) {
			case LEFT:	return CORNER_4;
			case UP:	return STRAIGHT_2;
			case RIGHT:	return CORNER_3;
			default: break;
			}
		default:
			Console.err("RiverSegmentType -> getConnectingSegment() -> previousSegLocation : " + previousSegLocation + " -> nextSegLocation : " + nextSegLocation);
			return RiverSegmentType.STRAIGHT_1;
		}
	}
	
	public boolean isSegmentPossible(DirectionType segmentLocation) {return isSegmentPossible(segmentLocation, this);}
	public static boolean isSegmentPossible(DirectionType segmentLocation, RiverSegmentType segment) {
		switch(segmentLocation) {
		case DOWN: case UP:
			switch(segment) {
			case NONE: case STRAIGHT_1:	 return true;
			default: return false;
			}
			
		case LEFT: case RIGHT:
			switch(segment) {
			case NONE :case STRAIGHT_2: return true;
			default: return false;
			}
			
		default:
			Console.err("RiverSegmentType -> connects() -> invalid case, segmentLocation : " + segmentLocation + " -> segmentType : " + segment);
			return false;
		}
	}
 
	
}



