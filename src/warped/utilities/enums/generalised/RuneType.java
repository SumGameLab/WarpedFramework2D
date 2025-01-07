/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.generalised;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum RuneType {

	/**Cattle -> Wealth*/
	FEHU,
	/**Cattle Other -> Poverty*/
	FEHU_ANNARR,
	/**Aurochs -> Will*/
	URUZ,
	/**Aurochs Other -> Conscience*/
	URUZ_ANNARR,
	/**Giant -> Danger*/
	THURISAZ,
	/**Giant Other -> Hospitality*/
	THURISAZ_ANNARR,
	/**An Aesir God -> Vitality*/
	ANSUZ,
	/**An Aesir God Other -> Fatality*/
	ANSUZ_ANNARR,
	/**Journey on Horseback -> Movement*/
	RAIDHO,
	/**Journey on Horseback Other -> Stationary*/
	RAIDHO_ANNARR,
	/**Ulcer -> Pain*/
	KAUNAN,
	/**Ulcer Other-> Wellbeing*/
	KAUNAN_ANNARR,
	/**Gift ->	Generosity*/
	GEBO,
	/**Gift Other->	Greed*/
	GEBO_ANNARR,
	/**Joy -> happiness*/
	WUNJO,
	/**Joy Other-> misery*/
	WUNJO_ANNARR,
	/** Hail -> Chaos*/
	HAGALAZ,
	/** Hail Other -> Order*/
	HAGALAZ_ANNARR,
	/**Need ->	Unfulfilled */
	NAUDHIZ,
	/**Need Other -> Satisfaction*/
	NAUDHIZ_ANNARR,
	/**Ice -> Beginning*/
	ISAZ,
	/**Ice Other-> End*/
	ISAZ_ANNARR,
	/**Year ->  Reward*/
	JERA,
	/**Year Other ->  Sacrifice*/
	JERA_ANNARR,
	/**Yew -> strength*/
	EIHWAZ,
	/**Yew Other -> weakness*/
	EIHWAZ_ANNARR,
	/**Protection -> Defend*/
	ALGIZ,
	/**Protection Other -> Attack*/
	ALGIZ_ANNARR,
	/**Sun -> success*/
	SOWILO,
	/**Sun Other -> failure*/
	SOWILO_ANNARR,
	/**God Tiwaz -> victory*/
	TIWAZ,
	/**God Tiwaz Other-> defeat*/
	TIWAZ_ANNAR,
	/**Birch -> growth*/
	BERKANAN,
	/**Birch Other -> decay*/
	BERKANAN_ANNARR,
	/**Horse ->  Companionship*/
	EHWAZ,
	/**Horse Other ->  Loneliness*/
	EHWAZ_ANNARR,
	/**Man -> Build*/
	MANNAZ,
	/**Man Other -> Destroy*/
	MANNAZ_ANNARR,
	/**Formlessness -> potentiality*/
	LAGUZ,
	/**Formlessness Other-> impossibility*/
	LAGUZ_ANNARR,
	/**God Ingwaz -> fertile*/
	INGWAZ,
	/**God Ingwaz Other -> polluted*/
	INGWAZ_ANNARR,
	/**Heritage-> Inherit*/
	OTHALAN,
	/**Inheritance Other-> Achieve*/
	OTHALAN_ANNARR,
	/**Day -> hope*/
	DAGAZ,
	/**Day Other -> despair*/
	DAGAZ_ANNARR,
	/**Sense -> Awareness*/
	INGUZ,
	/**Sense Other-> Ignorance*/
	INGUZ_ANNAR,
	
	NONE;
	
	private static Map<Integer, RuneType> map = new HashMap<>();
	static {
		for (RuneType type : RuneType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static RuneType get(int index) {
	    return (RuneType) map.get(index);
	}
	public static RuneType getRandomRuneType() {return get(UtilsMath.random.nextInt(size() - 1));}
	public static int size() {return map.size();}

	
	/*
	public static BufferedImage getRuneRaster(RuneType type) {
		switch(type) {
		case ALGIZ:				return WarpedSprites.framework.runes.getRawSprite(2, 3);
		case ALGIZ_ANNARR:		return WarpedSprites.framework.runes.getRawSprite(7, 3);
		case ANSUZ:				return WarpedSprites.framework.runes.getRawSprite(1, 1);
		case ANSUZ_ANNARR:		return WarpedSprites.framework.runes.getRawSprite(6, 1);
		case BERKANAN:          return WarpedSprites.framework.runes.getRawSprite(4, 2);
		case BERKANAN_ANNARR:   return WarpedSprites.framework.runes.getRawSprite(9, 2);
		case DAGAZ:             return WarpedSprites.framework.runes.getRawSprite(3, 4);
		case DAGAZ_ANNARR:      return WarpedSprites.framework.runes.getRawSprite(8, 4);
		case EHWAZ:             return WarpedSprites.framework.runes.getRawSprite(4, 2);
		case EHWAZ_ANNARR:      return WarpedSprites.framework.runes.getRawSprite(9, 2);
		case EIHWAZ:            return WarpedSprites.framework.runes.getRawSprite(0, 3);
		case EIHWAZ_ANNARR:     return WarpedSprites.framework.runes.getRawSprite(5, 3);
		case FEHU:              return WarpedSprites.framework.runes.getRawSprite(0, 0);
		case FEHU_ANNARR:       return WarpedSprites.framework.runes.getRawSprite(5, 0);
		case GEBO:              return WarpedSprites.framework.runes.getRawSprite(2, 1);
		case GEBO_ANNARR:       return WarpedSprites.framework.runes.getRawSprite(7, 1);
		case HAGALAZ:           return WarpedSprites.framework.runes.getRawSprite(0, 2);
		case HAGALAZ_ANNARR:    return WarpedSprites.framework.runes.getRawSprite(5, 2);
		case INGUZ:             return WarpedSprites.framework.runes.getRawSprite(4, 4);
		case INGUZ_ANNAR:       return WarpedSprites.framework.runes.getRawSprite(9, 4);
		case INGWAZ:            return WarpedSprites.framework.runes.getRawSprite(1, 4);
		case INGWAZ_ANNARR:     return WarpedSprites.framework.runes.getRawSprite(6, 4);
		case ISAZ:              return WarpedSprites.framework.runes.getRawSprite(2, 2);
		case ISAZ_ANNARR:       return WarpedSprites.framework.runes.getRawSprite(7, 2);
		case JERA:              return WarpedSprites.framework.runes.getRawSprite(3, 2);
		case JERA_ANNARR:       return WarpedSprites.framework.runes.getRawSprite(8, 2);
		case KAUNAN:            return WarpedSprites.framework.runes.getRawSprite(3, 0);
		case KAUNAN_ANNARR:     return WarpedSprites.framework.runes.getRawSprite(8, 0);
		case LAGUZ:				return WarpedSprites.framework.runes.getRawSprite(0, 4);
		case LAGUZ_ANNARR:      return WarpedSprites.framework.runes.getRawSprite(5, 4);
		case MANNAZ:            return WarpedSprites.framework.runes.getRawSprite(4, 3);
		case MANNAZ_ANNARR:     return WarpedSprites.framework.runes.getRawSprite(9, 3);
		case NAUDHIZ:           return WarpedSprites.framework.runes.getRawSprite(1, 2);
		case NAUDHIZ_ANNARR:    return WarpedSprites.framework.runes.getRawSprite(6, 2);
		case OTHALAN:           return WarpedSprites.framework.runes.getRawSprite(2, 4);
		case OTHALAN_ANNARR:    return WarpedSprites.framework.runes.getRawSprite(7, 4);
		case RAIDHO:            return WarpedSprites.framework.runes.getRawSprite(2, 0);
		case RAIDHO_ANNARR:     return WarpedSprites.framework.runes.getRawSprite(7, 0);
		case SOWILO:            return WarpedSprites.framework.runes.getRawSprite(3, 3);
		case SOWILO_ANNARR:     return WarpedSprites.framework.runes.getRawSprite(8, 3);
		case THURISAZ:          return WarpedSprites.framework.runes.getRawSprite(0, 1);
		case THURISAZ_ANNARR:   return WarpedSprites.framework.runes.getRawSprite(5, 1);
		case TIWAZ:             return WarpedSprites.framework.runes.getRawSprite(5, 0);
		case TIWAZ_ANNAR:       return WarpedSprites.framework.runes.getRawSprite(9, 0);
		case URUZ:              return WarpedSprites.framework.runes.getRawSprite(1, 0);
		case URUZ_ANNARR:       return WarpedSprites.framework.runes.getRawSprite(6, 0);
		case WUNJO:             return WarpedSprites.framework.runes.getRawSprite(3, 1);
		case WUNJO_ANNARR:      return WarpedSprites.framework.runes.getRawSprite(8, 1);
		default:
			System.err.println("RuneType -> getRuneRaster() -> invalid case : " + type);
			return null;
		}
	}
	*/
	
	
	
	
	
	
}
