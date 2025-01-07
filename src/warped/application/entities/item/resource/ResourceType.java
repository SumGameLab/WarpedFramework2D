/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public enum ResourceType {
	
	/**NOTE:
	 * When adding new entrys to the list, consider 
	 * 		a) if the new entry starts a new block i.e. salt/organic , make a random getter for the block
	 * 		b) if the new entry is part of an existing block i.e. its Liquid, add it somewhere in the middle, not the start or end of the block
	 * 				if you do make it the start or end of the block, adjust the randomizing getters in this class to match the new block parameters  
	 * Compounds can be broken down into elements,
	 * elements can not be combined into compounds without specific tech or knowledge*/
	
	
	//--------
	//---------------- Raw Resources -------
	//--------
	//Inorganics
	SAND,
	GRAVEL,
	
	//Organics
	WOOD,
	COAL,

	//Liquids
	WATER,
	OIL,
	ETHANOL,
	BENZENE,
	
	//Gas
	HYDROGEN_SULFIED,
	AMMONIA,
	METHANE,
	BUTANE,
	PROPANE,

	//Acids
	HYDROCHLORIC_ACID,
	SULFURIC_ACID,
	PHOSPHORIC_ACID,
	NITRIC_ACID,
	
	//Gems
	DIAMOND,
	EMERALD,
	AMETHYST,
	TOPAZ,
	JADE,
	OPAL,
	SAPPHIRE,
	RUBY,
	ZIRCON,
	GARNET,
	
	//Ores
	//Aluminum ores
	BAUXITE,
	FELDSPAR,
	//Iron ores
	HEMATITE,
	MAGNETITE,
	//Copper ores
	CUPRITE,
	CHALCOCITE,
	//Zinc ores
	SPHALERITE,
	ZINCITE,
	//Sodium ores
	SODIUM_CHLORIDE,
	SODIUM_HYDROXIDE,
	SODIUM_CARBONATE,
	AMMONIUM_NITRATE,
	//Potassium ores
	CARNALLITE,
	SALTPETRE,
	//Lead ores
	GALENA,
	ANGLESITE,
	//Tin ores
	PYRITE,
	CASSITERITE,
	//Silver ores
	ARGENTITE,
	CHLORARGYRITE,
	//Gold ores
	CALAVERITE,
	SYLVANITE,
	//Mercury ores
	CINNABAR,
	CALOMEL,
	//Magnesium ores
	DOLOMITE,
	MAGNESITE,
	//Calcium ores
	LIMESTONE,
	GYPSUM,
	//Phosphorous ores
	PHOSPHORITE,
	FLOURAPATITE,
	//Manganese ores
	MANGANITE,
	BRAUNITE,
	//Titanium ores
	PEROVSKITE,
	TITANITE,
	//Lanthanoid ores
	GADOLINITE,
	MONAZITE,
	//Nickel ores
	PENTLANDITE,
	GARNIERITE,
	//Chromium ores
	CHROMITE,
	CHROMITITE,
	//Uranium ore
	AUTUNITE,
	TORBERNITE,
	//Bismuth ore
	BISMUTHINITE,
	BISMITE,
	//Tungsten ore
	WOLFRAMITE,
	SCHEELITE,
	//Osmium ores
	OSMIRIDIUM,
	IRIDOSMINE,
	
	GOLD_NUGGET,
			
	
	//--------
	//---------------- Manufactured Resources -------
	//--------
	STEEL_REBAR,
	STEEL_CABLE,
	STEEL_WIRE,
	STEEL_ROPE,
	STEEL_PLATE,
	STEEL_PIPE,
	
	CEMENT,
	BRICKS,
	LUMBER,
	HARDWARE,
	
	ELECTRIC_ISOLATOR,
	ELECTRIC_LIGHT,
	ELECTRIC_WIRE,
	ELECTRIC_FUSE,
	ELECTRIC_OUTLET,
	
	MICRO_PROCESSORS,
	LED_DISPLAY,
	
	UNKNOWN;
	
	
	private static Map<Integer, ResourceType> map = new HashMap<>();
	static {
        for (ResourceType type : ResourceType.values()) {
            map.put(type.ordinal(), type);
        }
    }
	public static ResourceType get(int index) {
	    return (ResourceType) map.get(index);
	}
	
	private static ResourceType gasStart 	= HYDROGEN_SULFIED;
	private static ResourceType gasEnd 	 	= PROPANE;
	private static ResourceType saltStart 	= SODIUM_CHLORIDE;
	private static ResourceType saltEnd 	= AMMONIUM_NITRATE;
	private static ResourceType metalStart 	= BAUXITE;
	private static ResourceType metalEnd 	= GOLD_NUGGET;
	private static ResourceType gemStart 	= DIAMOND;
	private static ResourceType gemEnd 	 	= GARNET;
	private static ResourceType acidStart 	= HYDROCHLORIC_ACID;
	private static ResourceType acidEnd 	= NITRIC_ACID;
	private static ResourceType liquidStart = WATER;
	private static ResourceType liquidEnd 	= BENZENE;

	public static int getNumberOfEntrysFrom(ResourceType a, ResourceType b) {
		if(a == b) return 1;
		if(a.ordinal() > b.ordinal()) {
			System.err.println("ResrouceType -> getNumberOfEntrysFrom() -> a ordinal is larger than b ordinal, parameters will be swapped");
			ResourceType a1 = a;
			a = b;
			b = a1;
		}
		return (b.ordinal() - a.ordinal()) + 1;
	}
	public static int getNumberOfGasses() {return getNumberOfEntrysFrom(gasStart, gasEnd);} 
	public static int getNumberOfSalts() {return getNumberOfEntrysFrom(saltStart, saltEnd);}
	public static int getNumberOfMetals() {return getNumberOfEntrysFrom(metalStart, metalEnd);}
	public static int getNumberOfGems() {return getNumberOfEntrysFrom(gemStart, gemEnd);}
	public static int getNumberOfAcids() {return getNumberOfEntrysFrom(acidStart, acidEnd);}
	public static int getNumberOfLiquids() {return getNumberOfEntrysFrom(liquidStart, liquidEnd);}
	
	/**a & b inclusive*/
	public static ResourceType getRandomResourceFrom(ResourceType a, ResourceType b) {
		int rn = UtilsMath.random.nextInt(a.ordinal(), b.ordinal() + 1);
		return get(rn); 
	}
	public static ResourceType getRandomResource() {return get(UtilsMath.random.nextInt(getSize() - 1));}
	public static ResourceType getRandomGas() {return getRandomResourceFrom(gasStart, gasEnd);}
	public static ResourceType getRandomSalt() {return getRandomResourceFrom(saltStart, saltEnd);}
	public static ResourceType getRandomMetallicResource() {return getRandomResourceFrom(metalStart, metalEnd);}
	public static ResourceType getRandomGem() {return getRandomResourceFrom(gemStart, gemEnd);}
	public static ResourceType getRandomAcid() {return getRandomResourceFrom(acidStart, acidEnd);}
	public static ResourceType getRandomLiquid() {return getRandomResourceFrom(liquidStart, liquidEnd);}
	
	public static ArrayList<ResourceType> getRandomGasses(int number){
		if(number < 1) { 
			System.err.println("ResourceType -> getRandomGasses() -> number is less than 1 -> it will be set to 1");
			number = 1;
		}
		
		int max = getNumberOfGasses();
		if(number > max) {
			System.err.println("ResourceType -> getRandomGasses() -> number is larger than number of gasses -> it will be set to number of gasses");
			number = max;
		}
		
		ArrayList<ResourceType> result = new ArrayList<>();
		ArrayList<Integer> order = UtilsMath.getRandomUniqueOrderFrom(gasStart.ordinal(), gasEnd.ordinal());
		if(number < order.size()) order = (ArrayList<Integer>) order.subList(0, number);
		for(int i = 0; i < order.size(); i++) {result.add(get(order.get(i)));}
		return result;
	}
	
	public static ArrayList<ResourceType> getRandomMetals(int number){
		if(number < 1) { 
			System.err.println("ResourceType -> getRandomMetals() -> number is less than 1 -> it will be set to 1");
			number = 1;
		}
		
		int max = getNumberOfMetals();
		if(number > max) {
			System.err.println("ResourceType -> getRandomMetals() -> number is larger than number of gasses -> it will be set to number of metals");
			number = max;
		}
		
		ArrayList<ResourceType> result = new ArrayList<>();
		List<Integer> order = UtilsMath.getRandomUniqueOrderFrom(metalStart.ordinal(), metalEnd.ordinal());
		if(number < order.size()) order = order.subList(0, number);
		for(int i = 0; i < order.size(); i++) {result.add(get(order.get(i)));}
		return result;
	}
	
	public static int getSize() {return map.size();}
	public String getString() {return getString(this);}
	public static String getString(ResourceType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
	public double getResourceMass() {return getResourceMass(this);}
	public static double getResourceMass(ResourceType type) {
		 switch(type) {
			case WOOD:					return 0.010;							
			case COAL:					return 0.005;
					
			case WATER:					return 0.015;
			case OIL:                   return 0.025;
			case ETHANOL:               return 0.005;
			case BENZENE:               return 0.010;

			case HYDROGEN_SULFIED:		return 0.015;
			case AMMONIA:               return 0.020;
			case METHANE:               return 0.020;
			case BUTANE:                return 0.015;
			case PROPANE:               return 0.010;
			
			case HYDROCHLORIC_ACID:		return 0.035;
			case SULFURIC_ACID:         return 0.030;
			case PHOSPHORIC_ACID:       return 0.040;
			case NITRIC_ACID:           return 0.025;

			case DIAMOND:				return 0.005;
			case EMERALD:               return 0.010;
			case AMETHYST:              return 0.015;
			case TOPAZ:                 return 0.008;
			case JADE:                  return 0.009;
			case OPAL:                  return 0.012;
			case SAPPHIRE:              return 0.004;
			case RUBY:                  return 0.003;
			case ZIRCON:                return 0.010;
			case GARNET:                return 0.015;

			case BAUXITE:				return 0.100;
			case FELDSPAR:              return 0.210;

			case HEMATITE:				return 0.150;
			case MAGNETITE:             return 0.180;
			
			case CUPRITE:				return 0.120;
			case CHALCOCITE:            return 0.160;
			
			case SPHALERITE:			return 0.210;
			case ZINCITE:               return 0.220;
			
			case SODIUM_CARBONATE:		return 0.190;
			case SODIUM_CHLORIDE:       return 0.180;
			case SODIUM_HYDROXIDE:      return 0.170;
			case AMMONIUM_NITRATE:      return 0.150;
			
			case CARNALLITE:			return 0.025;
			case SALTPETRE:             return 0.022;
				
			case GALENA:				return 0.018;
			case ANGLESITE:				return 0.016;

			case PYRITE:				return 0.024;
			case CASSITERITE:           return 0.028;
				
			case ARGENTITE:				return 0.019;
			case CHLORARGYRITE:         return 0.017;

			case CALAVERITE:			return 0.023;
			case SYLVANITE:             return 0.027;

			case CINNABAR:				return 0.031;
			case CALOMEL:               return 0.013;

			case DOLOMITE:				return 0.024;
			case MAGNESITE:             return 0.021;

			case LIMESTONE:				return 0.023;
			case GYPSUM:                return 0.033;

			case PHOSPHORITE:			return 0.015;
			case FLOURAPATITE:          return 0.035;

			case MANGANITE:				return 0.034;
			case BRAUNITE:              return 0.016;

			case PEROVSKITE:			return 0.024;
			case TITANITE:              return 0.029;

			case GADOLINITE:			return 0.036;
			case MONAZITE:              return 0.034;

			case PENTLANDITE:			return 0.025;
			case GARNIERITE:            return 0.035;

			case CHROMITE:				return 0.015;
			case CHROMITITE:            return 0.028;

			case AUTUNITE:				return 0.017;
			case TORBERNITE:            return 0.015;

			case BISMUTHINITE:			return 0.021;
			case BISMITE:               return 0.024;

			case WOLFRAMITE:			return 0.023;
			case SCHEELITE:             return 0.026;

			case OSMIRIDIUM:			return 0.022;
			case IRIDOSMINE:            return 0.027;

			case GOLD_NUGGET:			return 0.031;

			case UNKNOWN:
			default:
				System.err.println("ResourceType -> getResourceMass() -> invalid case " + type);
				return 0.0;
			}
	}

	/*
	
	public BufferedImage getRaster() {return getRaster(this);}
	public static BufferedImage getRaster(ResourceType type) {
		switch(type) {
		case WOOD:				return WarpedSprites.warpTech.resourceItems.getRawSprite(2, 0);	
		case COAL:				return WarpedSprites.warpTech.resourceItems.getRawSprite(3, 0);
		case WATER:				return WarpedSprites.warpTech.resourceItems.getRawSprite(2, 2);
		case OIL:				return WarpedSprites.warpTech.resourceItems.getRawSprite(3, 2);
		case ETHANOL:           return WarpedSprites.warpTech.resourceItems.getRawSprite(2, 3);
		case BENZENE:           return WarpedSprites.warpTech.resourceItems.getRawSprite(3, 3);

		case HYDROGEN_SULFIED: 	return WarpedSprites.warpTech.resourceItems.getRawSprite(0, 2);
		case AMMONIA:           return WarpedSprites.warpTech.resourceItems.getRawSprite(1, 2);
		case METHANE:           return WarpedSprites.warpTech.resourceItems.getRawSprite(1, 3);
		case BUTANE:            return WarpedSprites.warpTech.resourceItems.getRawSprite(0, 4);
		case PROPANE:           return WarpedSprites.warpTech.resourceItems.getRawSprite(0, 3);
		
		case HYDROCHLORIC_ACID:	return WarpedSprites.warpTech.resourceItems.getRawSprite(2, 4);
		case SULFURIC_ACID:     return WarpedSprites.warpTech.resourceItems.getRawSprite(3, 4);
		case PHOSPHORIC_ACID:   return WarpedSprites.warpTech.resourceItems.getRawSprite(2, 5);
		case NITRIC_ACID:       return WarpedSprites.warpTech.resourceItems.getRawSprite(3, 5);

		case DIAMOND:			return WarpedSprites.warpTech.resourceItems.getRawSprite(4, 0);
		case EMERALD:           return WarpedSprites.warpTech.resourceItems.getRawSprite(5, 0);
		case TOPAZ:             return WarpedSprites.warpTech.resourceItems.getRawSprite(4, 1);
		case JADE:              return WarpedSprites.warpTech.resourceItems.getRawSprite(5, 1);
		case OPAL:              return WarpedSprites.warpTech.resourceItems.getRawSprite(4, 2);
		case SAPPHIRE:          return WarpedSprites.warpTech.resourceItems.getRawSprite(5, 2);
		case RUBY:              return WarpedSprites.warpTech.resourceItems.getRawSprite(4, 3);
		case GARNET:            return WarpedSprites.warpTech.resourceItems.getRawSprite(5, 3);
		case AMETHYST:          return WarpedSprites.warpTech.resourceItems.getRawSprite(4, 4);
		case ZIRCON:            return WarpedSprites.warpTech.resourceItems.getRawSprite(5, 4);

		case BAUXITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 0);
		case FELDSPAR:          return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 0);

		case HEMATITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 1);
		case MAGNETITE:         return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 1);
		
		case CUPRITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 2);
		case CHALCOCITE:        return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 2);
		
		case SPHALERITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 3);
		case ZINCITE:           return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 3);
		
		case SODIUM_CARBONATE:	return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 4);
		case SODIUM_CHLORIDE:	return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 4);
		case SODIUM_HYDROXIDE:  return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 5);
		case AMMONIUM_NITRATE:  return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 5);
		
		case CARNALLITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 6);
		case SALTPETRE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 6);
			
		case GALENA:			return WarpedSprites.warpTech.resourceItems.getRawSprite(6, 7);
		case ANGLESITE:         return WarpedSprites.warpTech.resourceItems.getRawSprite(7, 7);

		case PYRITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 0);
		case CASSITERITE:       return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 0);
			
		case ARGENTITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 1);
		case CHLORARGYRITE:     return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 1);

		case CALAVERITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 2);
		case SYLVANITE:         return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 2);

		case CINNABAR:			return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 3);
		case CALOMEL:           return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 3);

		case DOLOMITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 4);
		case MAGNESITE:         return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 4);

		case LIMESTONE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 5);
		case GYPSUM:            return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 5);

		case PHOSPHORITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 6);
		case FLOURAPATITE:	    return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 6);

		case MANGANITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(8, 7);
		case BRAUNITE:          return WarpedSprites.warpTech.resourceItems.getRawSprite(9, 7);

		case PEROVSKITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 0);
		case TITANITE:          return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 0);
		
		case GADOLINITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 1);
		case MONAZITE:          return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 1);

		case PENTLANDITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 2);
		case GARNIERITE:        return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 2);

		case CHROMITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 3);		
		case CHROMITITE:        return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 3);

		case AUTUNITE:			return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 4);
		case TORBERNITE:        return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 4);

		case BISMUTHINITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 5);		
		case BISMITE:           return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 5);

		case WOLFRAMITE:		return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 6);
		case SCHEELITE:         return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 6);

		case OSMIRIDIUM:		return WarpedSprites.warpTech.resourceItems.getRawSprite(10, 7);
		case IRIDOSMINE:        return WarpedSprites.warpTech.resourceItems.getRawSprite(11, 7);

		case GOLD_NUGGET:		return WarpedSprites.warpTech.resourceItems.getRawSprite(4, 5);

		case UNKNOWN:			return WarpedSprites.warpTech.resourceItems.getRawSprite(0, 0);
		default:
			System.err.println("ResourceType -> getResourceImage() -> invalid case " + type);
			return null;
		}
	}
	
	*/

	public ItemResource generateResource() {return generateResource(this, 1);}
	public ItemResource generateResource(int quantity) {return generateResource(this, quantity);}
	public static ItemResource generateResource(ResourceType type, int quantity) {
		if(quantity <= 0) {
			System.err.println("AmmunitionType -> generateAmmunition() -> quantity must be greater than 0 : " + quantity + ", it will be set to 1.");
			quantity = 1;
		}
		
		return new ItemResource(type, quantity);
	}
	
	
}
