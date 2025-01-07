/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.element;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public enum ElementType {	
	HYDROGEN,
	HELIUM,
	LITHIUM,
	BERYLIUM,
	BORON,
	CARBON,
	NITROGEN,
	OXYGEN,
	FLOURINE,
	NEON,
	SODIUM,
	MAGNESIUM,
	ALUMINIUM,
	SILICON,
	PHOSPHORUS,
	SULPHUR,
	CHLORINE,
	ARGON,
	POTASSIUM,
	CALCIUM,
	SCANDIUM,
	TITANIUM,
	VANADIUM,
	CHROMIUM,
	MANGANESE,
	IRON,
	COBALT,
	NICKEL,
	COPPER,
	ZINC,
	GALLIUM,
	GERMANIUM,
	ARSENIC,
	SELENIUM,
	BROMINE,
	KRYPTON,
	RUBIDIUM,
	STRONTIUM,
	YTTRIUM,
	ZIRCONIUM,
	NIOBIUM,
	MOLYBDENUM,
	TECHNETIUM,
	RUTHENIUM,
	RHOBIUM,
	PALLADIUM,
	SILVER,
	CADMIUM,
	INDIUM,
	TIN,
	ANTIMONY,
	TELLURIUM,
	IODINE,
	XENON,
	CESIUM,
	BARIUM,
	LANTHANUM,
	CERIUM,
	PARASEODYMIUM,
	NEODYMIUM,
	PROMETHIUM,
	SAMARIUM,
	EUROPIUM,
	GADOLINIUM,
	TERBIUM,
	DYSPROSIUM,
	HOLMIUM,
	ERBIUM,
	THULIUM,
	YTTERBIUM,
	LUTERTIUM,
	HAFNIUM,
	TANTALUM,
	TUNGSTEN,
	RHENIUM,
	OSMIUM,
	IRIDIUM,
	PLATINUM,
	GOLD,
	MERCURY,
	THALIUM,
	LEAD,
	BISMUTH,
	POLONIUM,
	ASTATINE,
	RADON,
	FRANCIUM,
	RADIUM,
	ACINIUM,
	THORIUM,
	PROTACTINIUM,
	URANIUM,
	NEPTUNIUM,
	PLUTONIUM;
	
	
	private static Map<Integer, ElementType> map = new HashMap<>();
	static {
		for (ElementType type : ElementType.values()) {
			map.put(type.ordinal(), type);
		}
	}
	
	public static ElementType get(int index) {
		return (ElementType) map.get(index);
	}
	public static ElementType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
		
	public static double getElementMass(ElementType type) {return(type.ordinal() * 0.01);}
	
	public String getString() {return getString(this);}
	public static String getString(ElementType type) {
		String result = UtilsString.convertEnumToString(type);
		if(result == null) {
			System.err.println("ResourceType -> getString -> result is null");
			return "error";
		}
		return result;
	}
	
	//public BufferedImage getRaster() {return getRaster(this);}
	//public static BufferedImage getRaster(ElementType type) {return WarpedSprites.warpTech.elementItems.getRawSprite(type.ordinal());}
	
	public ItemElement generateElement() {return generateElement(this, 1);}
	public ItemElement generateElement(int quantity) {return generateElement(this, quantity);}
	public static ItemElement generateElement(ElementType type, int quantity) {
		if(quantity <= 0) {
			System.err.println("AmmunitionType -> generateAmmunition() -> quantity must be greater than 0 : " + quantity + ", it will be set to 1.");
			quantity = 1;
		}
		
		return new ItemElement(type, quantity);
	}
	
}
