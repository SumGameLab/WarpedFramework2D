/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.artifact;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.enums.generalised.RuneType;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public enum ArtifactType {

	RUNE_TABLET;
	
	private static Map<Integer, ArtifactType> map = new HashMap<>();
	static {
		for (ArtifactType type : ArtifactType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static ArtifactType get(int index) {
	    return (ArtifactType) map.get(index);
	}
	public static ArtifactType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}

	
	public double getMass() {return getMass(this);}
	public static double getMass(ArtifactType type) {
		switch(type) {
		case RUNE_TABLET: return 0.5;
		default:
			System.err.println("ArtifactType -> getArtifactMass() -> invalid case : " + type);
			return 0.0;
		}
	}
	
	public double getValue() {return getValue(this);}
	public static double getValue(ArtifactType type){
		switch(type) {
		case RUNE_TABLET: return 100.0;
		default:
			System.err.println("ArtifactType -> getValue() -> invalid case : " + type);
			return 0.0;
		
		}
	}
	
	public String getString() {return getString(this);}
	public static String getString(ArtifactType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
	public ItemArtifact generateArtifact(int quantity){		
		if(quantity != 1) {
			System.err.println("ArtifactType -> generateArtifact() -> generate multiple quantity artifact not supported, artifacts can not be stacked they are individual items");
			quantity = 1;
		}
		return generateArtifact(this);
	}
	public ItemArtifact generateArtifact(){return generateArtifact(this);}
	public static ItemArtifact generateArtifact(ArtifactType type) {
		switch(type) {
		case RUNE_TABLET: return new RuneTabletArtifact(RuneType.getRandomRuneType());
		default:
			System.err.println("ArtifactType -> generateArtifact() -> invalid type : " + type);
			return new RuneTabletArtifact(RuneType.getRandomRuneType());
		
		}
	}
	
	
	
}
