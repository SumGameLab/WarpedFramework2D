/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

public enum RiverType {

	LAVA, 
	WATER,
	FROZEN;
	
	
	//public BufferedImage getRiverImage(RiverSegmentType riverSegment) {return getRiverImage(this, riverSegment);}
	//public static BufferedImage getRiverImage(RiverType riverType, RiverSegmentType riverSegment) { 
	//	return UtilsImage.generateRotatedSizedCloneDegrees(getRiverSheet(riverType).getRandomSpriteFromRow(riverSegment.getSegementIndex()), riverSegment.getSegmentRotation());
	//}
	
	//public SpriteSheet getRiverSheet() {return getRiverSheet(this);}
	/*
	public static SpriteSheet getRiverSheet(RiverType riverType) {
		switch(riverType) {
		case FROZEN: return WarpedSprites.framework.frozenRivers;
		case LAVA: 	 return WarpedSprites.framework.lavaRivers;
		case WATER:  return WarpedSprites.framework.waterRivers;
		default:
			System.err.println("RiverType -> getRiverSpriteSheet() -> invalid case : " + riverType);
			return null;
		
		}
	}
	*/
	
	
}
