/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.tile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import warped.application.entities.item.resource.ResourceType;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public enum TileType {
	
	VOID,
	
	//-----------TERRAIN TILES-------------------------
	
	//Base Tiles
	CLOUD_WATER,
	CLOUD_HYDROSULFIDE,
	CLOUD_AMMONIA,
	CLOUD_DUST,
	CLOUD_SILICA,

	BARREN,
	DESERT,	
	ETCHPLAIN,		//Plain where the bedrock has been subject to considerable subsurface weathering
	FOREST_TEMPERATE,
	FOREST_ARCTIC,
	FOREST_RAIN,
	GLACIER,
	GRASSLAND,
	HILLS,
	JUNGLE,
	LAVA,
	MOUNTAIN_TEMPERATE,
	MOUNTAIN_ARCTIC,
	MARSH,
	OCEAN,
	OCEAN_ACID,
	OCEAN_BASE,
	OCEAN_SLIME,
	PLAINS,
	REEF,
	SEA,
	SEA_TROPICAL,
	SAVANAH,
	TUNDRA,
	WASTELAND_ASH,
	WASTELAND_LAVA,
	WETLAND,
	
	//Scatter Tiles
	ATOL,		  	//Ring-shaped coral reef
	ISLAND,
	LAKE,
	OASIS,
	SALTLAKE,
	SINKHOLE,
	TIDEPOOL,
	VOLCANO,

	//special case tiles
	SURVEY;

		
	
	
	//--------------SHIP TILES-------------------------
	
	
	private static Map<Integer, TileType> map = new HashMap<>();
	static {
        for (TileType planet : TileType.values()) {
            map.put(planet.ordinal(), planet);
        }
    }
	
	public static TileType get(int index) {
	    return (TileType) map.get(index);
	}
	public static TileType getRandomTile() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public String getString() {return getString(this);}
	
	public static String getString(TileType type) {
		switch(type) {
		case ATOL: 	 	 		return "Atol";
		case BARREN: 	 		return "Barren";
		case CLOUD_WATER: 		return "Water Cloud";
		case CLOUD_HYDROSULFIDE:return "Hydrosufied Cloud";
		case CLOUD_AMMONIA:		return "Ammonia Cloud";
		case CLOUD_DUST:		return "Dust Cloud";
		case CLOUD_SILICA:		return "Silica Cloud";
		case DESERT: 	 		return "Desert";
		case ETCHPLAIN:  		return "Etchplain";
		case FOREST_TEMPERATE: 	return "Temperate Forest";
		case FOREST_ARCTIC:		return "Arctic Forest";
		case FOREST_RAIN:		return "Rain Forest";
		case GLACIER:	 		return "Glacier";
		case GRASSLAND:  		return "Grassland";
		case HILLS:		 		return "Hills";
		case ISLAND:     		return "Island";
		case JUNGLE:	 		return "Jungle";
		case LAKE: 		 		return "Lake";
		case LAVA: 		 		return "Lava";
		case MARSH:		 		return "Marsh";
		case MOUNTAIN_TEMPERATE:return "Mountain";
		case OASIS:		 		return "Oasis";
		case OCEAN:		 		return "Ocean";
		case OCEAN_ACID:		return "Acidic Ocean";
		case OCEAN_BASE:		return "Basic Ocean";
		case OCEAN_SLIME:		return "Slime Ocean";
		case PLAINS: 	 		return "Plains";
		case REEF:				return "Reef";
		case SALTLAKE:	 		return "Salt Lake";
		case SAVANAH:	 		return "Savanah";
		case SEA:		 		return "Sea";
		case SEA_TROPICAL:		return "Tropical Sea";
		case SINKHOLE:	 		return "Sink Hole";
		case TIDEPOOL:	 		return "Tide Pool";
		case TUNDRA:	 		return "Tundra";
		case VOID:		 		return "No Type";
		case VOLCANO:	 		return "Volcano";
		case WASTELAND_ASH:  	return "Ash Wasteland";
		case WASTELAND_LAVA: 	return "Lava Wasteland";
		case WETLAND:    	 	return "Wetland";
		default:			
			Console.err("TileType -> getString() -> invalid case : " + type);
			return "Error!";
		}
	}
	
	public static String getDescription(TileType type) {
		switch(type) {
		case ATOL: return "Surrounded by coral reefs and fertile sand banks, the inner lagoon of an atoll can sometimes contain abundent fresh water.";
		case BARREN: return "Dusty plains and folding outcrops of rock are the only notable features in this bleak landscape.";
		case CLOUD_AMMONIA: return "Thick heavy clouds of ammonia rolling across each other, an extremely harsh environment for any kind of equipment or life form.";
		case CLOUD_DUST: return "A dense cloud of fine particles, hot and fast moving this dust will quickly damage any equipment or respiratory system that becomes exposed to it.";
		case CLOUD_HYDROSULFIDE:  
		case CLOUD_SILICA:
		case CLOUD_WATER:
			
		case DESERT:
		case ETCHPLAIN:
		case FOREST_ARCTIC:
		case FOREST_RAIN:
		case FOREST_TEMPERATE:
		case GLACIER:
		case GRASSLAND:
		case HILLS:
		case ISLAND:
		case JUNGLE:
		case LAKE:
		case LAVA:
		case MARSH:
		case MOUNTAIN_ARCTIC:
		case MOUNTAIN_TEMPERATE:
		case OASIS:
		case OCEAN:
		case OCEAN_ACID:
		case OCEAN_BASE:
		case OCEAN_SLIME:
		case PLAINS:
		case REEF:
		case SALTLAKE:
		case SAVANAH:
		case SEA:
		case SEA_TROPICAL:
		case SINKHOLE:
		case TIDEPOOL:
		case TUNDRA:
		case VOID: return "As you look at the tile the tile also looks at you";
		case VOLCANO:
		case WASTELAND_ASH:
		case WASTELAND_LAVA:
		case WETLAND:
		default:
			Console.err("TileType -> getDescription() -> invalid case : " + type);
			return "error";
		}
	}
		

	public static double getRoughness(WarpedTile tile) {return (((tile.primaryType.getRoughness() * 2) + tile.secondaryType.getRoughness()) / 3);}
	public double getRoughness() {return getRoughness(this);}
	private static double getRoughness(TileType type) {	//Scales crew move speed -> Values closer to 0.0 crew will move slower, values closer to 1.0 crew will move faster 
		switch(type) {
		case LAKE:				 return 0.0025;
		case LAVA:				 return 0.0001;
		case OCEAN:				 return 0.0010;
		case SEA:				 return 0.0011;
		case REEF:				 return 0.0009;
		case ATOL: 				 return 0.0011;
		case SEA_TROPICAL:		 return 0.0012;
		case OCEAN_ACID:		 return 0.0005;
		case OCEAN_BASE:		 return 0.0005;
		case OCEAN_SLIME:		 return 0.0001;
		case SINKHOLE:			 return 0.0001;
		case VOLCANO:			 return 0.0015;
		case BARREN:			 return 0.0070;
		case DESERT:			 return 0.0060;
		case ETCHPLAIN:			 return 0.0065;
		case FOREST_ARCTIC:	     return 0.0068;
		case FOREST_RAIN:		 return 0.0062;
		case FOREST_TEMPERATE:	 return 0.0075;
		case GLACIER:			 return 0.0065;
		case GRASSLAND:			 return 0.0080;
		case HILLS:				 return 0.0075;
		case JUNGLE:			 return 0.0055;
		case MOUNTAIN_ARCTIC:	 return 0.0030;
		case MOUNTAIN_TEMPERATE: return 0.0040;
		case PLAINS:			 return 0.0085;
		case SAVANAH:			 return 0.0080;
		case TUNDRA:			 return 0.0078;
		case CLOUD_AMMONIA:		 return 0.0001;
		case CLOUD_DUST:		 return 0.0001;
		case CLOUD_HYDROSULFIDE: return 0.0001;
		case CLOUD_SILICA:		 return 0.0001;
		case CLOUD_WATER:		 return 0.0001;
		case ISLAND:			 return 0.0040;
		case MARSH:				 return 0.0025;
		case OASIS:				 return 0.0060;
		case SALTLAKE:			 return 0.0030;
		case TIDEPOOL:			 return 0.0050;
		case WETLAND:			 return 0.0035;
		case WASTELAND_ASH:		 return 0.0028;
		case WASTELAND_LAVA:	 return 0.0010;
		default:
			Console.err("TileType -> isCollidable() -> invalid case : " + type);
			return 1.0;
		}
	}
	
	public static double getBaseTemperature(WarpedTile tile) {
		double dayTime = tile.getLocalTime();
		dayTime *= 2.0;
		if(dayTime > 1.0) dayTime = 1.0 - (dayTime % 1);
		double nightTime = 1.0 - dayTime;

		double dayTemp   = getDayTemperature(tile) * dayTime;
		double nightTemp = getNightTemperature(tile) * nightTime;		
		
		return dayTemp + nightTemp + UtilsMath.random(4.0) - 2.0;
	}
	
	/*
	public static double getBaseTemperature(EntitieCelestial parent, WarpedTile tile) {
		double dayTime = tile.getLocalTime();
		dayTime *= 2.0;
		if(dayTime > 1.0) dayTime = 1.0 - (dayTime % 1);
		double nightTime = 1.0 - dayTime;
		
		double dayTemp   = (getDayTemperature(tile)  + parent.getAtmosphericTemperatureVariance().x) * dayTime;
		double nightTemp = (getNightTemperature(tile) - parent.getAtmosphericTemperatureVariance().y) * nightTime;		
		
		return dayTemp + nightTemp + UtilsMath.randomDouble(4.0) - 2.0;
	}
	 */
	
	
	public static double getDayTemperature(WarpedTile tile) { return (((tile.getPrimaryType().getBaseDayTemperature() * 2) + tile.getSecondaryType().getBaseDayTemperature()) / 3);}
	public static double getNightTemperature(WarpedTile tile) { return (((tile.getPrimaryType().getBaseNightTemperature() * 2) + tile.getSecondaryType().getBaseNightTemperature()) / 3);}
	
	public double getBaseNightTemperature() {return getBaseNightTemperature(this);}
	public static double getBaseNightTemperature(TileType type) {
		switch(type) {
		case BARREN:			 return   5.15; 
		case DESERT:			 return   1.25; 
		case ETCHPLAIN:			 return   8.85;
		case FOREST_ARCTIC:	     return -15.95;
		case FOREST_RAIN:		 return  18.55;
		case FOREST_TEMPERATE:	 return  14.15;
		case GLACIER:			 return -25.25;
		case GRASSLAND:			 return  15.75;
		case HILLS:				 return  11.85;
		case JUNGLE:			 return  19.05;
		case MOUNTAIN_ARCTIC:	 return -11.65;
		case MOUNTAIN_TEMPERATE: return   6.15;
		case PLAINS:			 return  12.15;
		case SAVANAH:			 return  13.85;
		case TUNDRA:			 return -12.25;		
		case ATOL: 				 return  10.55;
	
		case LAKE:				 return  14.65;
		case ISLAND:			 return   9.15;
		case MARSH:				 return   7.25;
		case OASIS:				 return  11.75;
		case SEA:				 return  16.55;
		case OCEAN:				 return  15.45;
		case OCEAN_ACID:		 return  15.15;
		case OCEAN_BASE:		 return  15.35;
		case OCEAN_SLIME:		 return  15.55;
		case SEA_TROPICAL:		 return  17.55;
		case REEF:				 return  17.15;
		case SALTLAKE:			 return   9.35;
		case SINKHOLE:			 return  12.15;
		case TIDEPOOL:			 return  16.55;
		case WETLAND:			 return  15.15;
		case WASTELAND_ASH:		 return  42.25;
		case VOLCANO:			 return  49.55;
		case WASTELAND_LAVA: 	 return 110.15;
		case LAVA:				 return 610.55;
		                                             
		case CLOUD_WATER:		 return 18.05; 
		case CLOUD_AMMONIA:		 return 475.05;
		case CLOUD_DUST:		 return 22.05; 
		case CLOUD_HYDROSULFIDE: return 205.05;		
		case CLOUD_SILICA:		 return 845.05;
		
		default:
			Console.err("TileType -> getTemperature() -> invalid case : " + type);
			return 1;
		}
	
	}
	
	public double getBaseDayTemperature() {return getBaseDayTemperature(this);}
	public static double getBaseDayTemperature(TileType type) {
		switch(type) {
		case BARREN:			 return  15.15; 
		case DESERT:			 return  45.25; 
		case ETCHPLAIN:			 return  18.85;
		case FOREST_ARCTIC:	     return   5.95;
		case FOREST_RAIN:		 return  28.55;
		case FOREST_TEMPERATE:	 return  22.15;
		case GLACIER:			 return   2.25;
		case GRASSLAND:			 return  25.75;
		case HILLS:				 return  20.85;
		case JUNGLE:			 return  26.65;
		case MOUNTAIN_ARCTIC:	 return   1.65;
		case MOUNTAIN_TEMPERATE: return  11.15;
		case PLAINS:			 return  26.15;
		case SAVANAH:			 return  29.85;
		case TUNDRA:			 return   4.25;		
		case ATOL: 				 return  22.55;
		case LAKE:				 return  23.65;
		case ISLAND:			 return  26.15;
		case MARSH:				 return  18.25;
		case OASIS:				 return  24.55;
		case SEA:				 return  17.55;
		case OCEAN:				 return  16.45;
		case OCEAN_ACID:		 return  16.15;
		case OCEAN_BASE:		 return  16.35;
		case OCEAN_SLIME:		 return  16.55;
		case SEA_TROPICAL:		 return  18.55;
		case REEF:				 return  18.15;
		case SALTLAKE:			 return  25.35;
		case SINKHOLE:			 return  16.15;
		case TIDEPOOL:			 return  20.55;
		case WETLAND:			 return  21.15;
		case WASTELAND_ASH:		 return  46.25;
		case VOLCANO:			 return  50.55;
		case WASTELAND_LAVA: 	 return 115.15;			
		case LAVA:				 return 635.55;

		case CLOUD_WATER:		 return 20.05;
		case CLOUD_AMMONIA:		 return 485.05;
		case CLOUD_DUST:		 return 32.05;
		case CLOUD_HYDROSULFIDE: return 215.05;
		case CLOUD_SILICA:		 return 915.05;
		
		default:
			Console.err("TileType -> getDayTemperature() -> invalid case : " + type);
			return 1.0;
		}
	}
	
	public boolean isRiverPossible() {return isRiverPossible(this);}
	public static boolean isRiverPossible(TileType type) {
		switch(type) {
		case BARREN:			 return true; 
		case DESERT:			 return true;
		case ETCHPLAIN:			 return true;
		case FOREST_ARCTIC:	     return true;
		case FOREST_RAIN:		 return true;
		case FOREST_TEMPERATE:	 return true;
		case GLACIER:			 return true;
		case GRASSLAND:			 return true;
		case HILLS:				 return true;
		case JUNGLE:			 return true;						
		case MOUNTAIN_ARCTIC:	 return true;
		case MOUNTAIN_TEMPERATE: return true; 
		case PLAINS:			 return true;
		case SAVANAH:			 return true;						
		case TUNDRA:			 return true;					
		
		
		
		case ATOL: 				 return false;
		case CLOUD_AMMONIA:		 return false;			
		case CLOUD_DUST:		 return false;		
		case CLOUD_HYDROSULFIDE: return false;
		case CLOUD_SILICA:		 return false;	
		case CLOUD_WATER:		 return false;
		case LAKE:				 return false;
		case ISLAND:			 return false;
		case LAVA:				 return false;
		case MARSH:				 return false;
		case OASIS:				 return false;
		case OCEAN:				 return false;
		case OCEAN_ACID:		 return false;
		case OCEAN_BASE:		 return false;
		case OCEAN_SLIME:		 return false;
		case SEA_TROPICAL:		 return false;
		case REEF:				 return false;
		case SALTLAKE:			 return false;	
		case SEA:				 return false;
		case SINKHOLE:			 return false;			
		case TIDEPOOL:			 return false;
		case VOLCANO:			 return false;
		case WETLAND:			 return false;
		case WASTELAND_ASH:		 return false;			
		case WASTELAND_LAVA: 	 return false;				
		
		default:
			Console.err("TileType -> isRiverPossible() -> invalid case : " + type);
			return false;
		}
	}
	
	
	/**The rules describe which resources are possible to spawn not resources that will be on every tile*/
	private static HashMap<TileType, List<ResourceType>> resourceSpawnRules = new HashMap<>();
	static {
		resourceSpawnRules.put(TileType.ATOL,   			Arrays.asList(ResourceType.WOOD, 	  		ResourceType.OIL,    	  	  ResourceType.GOLD_NUGGET));
		resourceSpawnRules.put(TileType.BARREN, 			Arrays.asList(ResourceType.COAL, 	  		ResourceType.MAGNETITE,   	  ResourceType.WATER));
		resourceSpawnRules.put(TileType.CLOUD_AMMONIA, 		Arrays.asList(ResourceType.AMMONIA,   		ResourceType.WATER, 	  	  ResourceType.WATER));
		resourceSpawnRules.put(TileType.CLOUD_DUST, 		Arrays.asList(ResourceType.MAGNETITE, 	  	ResourceType.SODIUM_CHLORIDE, ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.CLOUD_HYDROSULFIDE, Arrays.asList(ResourceType.WATER,  	  		ResourceType.WATER, 	  	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.CLOUD_SILICA, 		Arrays.asList(ResourceType.BAUXITE, 		ResourceType.ANGLESITE,	 	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.CLOUD_WATER, 		Arrays.asList(ResourceType.WATER, 	  		ResourceType.ANGLESITE,  	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.DESERT, 			Arrays.asList(ResourceType.OIL,  	  		ResourceType.ANGLESITE,   	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.ETCHPLAIN, 			Arrays.asList(ResourceType.ANGLESITE, 	  	ResourceType.ANGLESITE, 	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.FOREST_ARCTIC, 		Arrays.asList(ResourceType.WOOD, 	  		ResourceType.ANGLESITE,  	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.FOREST_RAIN, 		Arrays.asList(ResourceType.WOOD, 	  		ResourceType.ANGLESITE,  	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.FOREST_TEMPERATE, 	Arrays.asList(ResourceType.WOOD, 	  		ResourceType.COAL,   	  	  ResourceType.OIL));
		resourceSpawnRules.put(TileType.GLACIER,    		Arrays.asList(ResourceType.OIL,  	  		ResourceType.ANGLESITE,       ResourceType.SODIUM_CHLORIDE));
		resourceSpawnRules.put(TileType.GRASSLAND,  		Arrays.asList(ResourceType.COAL, 	  		ResourceType.ANGLESITE, 	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.HILLS,  			Arrays.asList(ResourceType.COAL, 	  		ResourceType.ANGLESITE, 	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.ISLAND,  			Arrays.asList(ResourceType.WATER, 	  		ResourceType.WOOD, 	  		  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.JUNGLE,  			Arrays.asList(ResourceType.WOOD, 	  		ResourceType.ANGLESITE, 	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.LAKE,  				Arrays.asList(ResourceType.WATER, 	  		ResourceType.ANGLESITE,   	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.LAVA,  				Arrays.asList(ResourceType.WATER, 	  		ResourceType.ANGLESITE,  	  ResourceType.DIAMOND));
		resourceSpawnRules.put(TileType.MARSH,  			Arrays.asList(ResourceType.WATER, 	  		ResourceType.ANGLESITE,  	  ResourceType.OIL));
		resourceSpawnRules.put(TileType.MOUNTAIN_ARCTIC,  	Arrays.asList(ResourceType.WATER,  			ResourceType.ANGLESITE,  	  ResourceType.DIAMOND));
		resourceSpawnRules.put(TileType.MOUNTAIN_TEMPERATE, Arrays.asList(ResourceType.WATER,			ResourceType.ANGLESITE,  	  ResourceType.DIAMOND));
		resourceSpawnRules.put(TileType.OASIS,				Arrays.asList(ResourceType.WATER,			ResourceType.ANGLESITE,  	  ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.OCEAN,				Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,  		ResourceType.ANGLESITE));
		resourceSpawnRules.put(TileType.OCEAN_ACID,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.OCEAN_BASE,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.OCEAN_SLIME,		Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.PLAINS,				Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.REEF,				Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.SALTLAKE,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.SAVANAH,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.SEA,				Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.SEA_TROPICAL,		Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.SINKHOLE,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.TIDEPOOL,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.TUNDRA,				Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.VOLCANO,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.WASTELAND_ASH,		Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.WASTELAND_LAVA,		Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
		resourceSpawnRules.put(TileType.WETLAND,			Arrays.asList(ResourceType.WATER,			ResourceType.SODIUM_CHLORIDE,	 		ResourceType.WATER));
	}
	
	public static ResourceType getPrimaryResource(TileType type) {return resourceSpawnRules.get(type).get(0);}
	public static ResourceType getRandomResource(TileType type) {
		int rn = UtilsMath.random.nextInt(resourceSpawnRules.get(type).size());
		return resourceSpawnRules.get(type).get(rn);
	}
	
	//--------
	//---------------- Graphics --------
	//--------
	/*
	public static BufferedImage getTemperatureImage(double temperature) {return SystemModuleType.getTemperatureImage(temperature);}
	
	public static BufferedImage getRoughnessImage(WarpedTile tile) {return getRoughnessImage(getRoughness(tile));}
	public static BufferedImage getRoughnessImage(double roughness) {
			 if(roughness < 0.001)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 9);
		else if(roughness < 0.002)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 8);
		else if(roughness < 0.003)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 7);
		else if(roughness < 0.004)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 6);
		else if(roughness < 0.005)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 5);
		else if(roughness < 0.006)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 4);
		else if(roughness < 0.007)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 3);
		else if(roughness < 0.008)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 2);
		else if(roughness < 0.009)  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 1);
		else 					  return WarpedSprites.warpTech.systemIcons.getRawSprite(22, 0);	
	}
	
	public static BufferedImage getGravitationImage(double gravitationScale) {
		if	   (gravitationScale < 0.92) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 0);
		else if(gravitationScale < 0.94) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 1);
		else if(gravitationScale < 0.96) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 2);
		else if(gravitationScale < 0.98) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 3);
		else if(gravitationScale < 1.00) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 4);
		else if(gravitationScale < 1.02) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 5);
		else if(gravitationScale < 1.04) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 6);
		else if(gravitationScale < 1.06) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 7);
		else if(gravitationScale < 1.08) return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 8);
		else   							 return WarpedSprites.warpTech.systemIcons.getRawSprite(23, 9);
	}
	
	public static BufferedImage getMagnetismImage(double magnetismScale) {
		if	   (magnetismScale < 0.92) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 0);
		else if(magnetismScale < 0.94) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 1);
		else if(magnetismScale < 0.96) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 2);
		else if(magnetismScale < 0.98) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 3);
		else if(magnetismScale < 1.00) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 4);
		else if(magnetismScale < 1.02) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 5);
		else if(magnetismScale < 1.04) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 6);
		else if(magnetismScale < 1.06) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 7);
		else if(magnetismScale < 1.08) return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 8);
		else   						   return WarpedSprites.warpTech.systemIcons.getRawSprite(24, 9);
	}
	*/
}
