/* WarpedFramework 2D - java API - Copyright (C) 2021-2025 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.utils.Console;

public enum ElementType implements ItemBindable<ElementType>{	
	
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
	PLUTONIUM
	
	
	;public static Map<Integer, ElementType> map = new HashMap<>();
	static {for(ElementType sprite : ElementType.values()) map.put(sprite.ordinal(), sprite);}
	@Override
	public Map<Integer, ElementType> getMap() {return map;}
	@Override
	
	
	public String getDescription(ElementType elementType) {
		switch(elementType) {
		case HYDROGEN:
		case HELIUM:
		case LITHIUM:
		case BERYLIUM:
		case BORON:
		case CARBON:
		case NITROGEN:
		case OXYGEN:
		case FLOURINE:
		case NEON:
		case SODIUM:
		case MAGNESIUM:
		case ALUMINIUM:
		case SILICON:
		case PHOSPHORUS:
		case SULPHUR:
		case CHLORINE:
		case ARGON:
		case POTASSIUM:
		case CALCIUM:
		case SCANDIUM:
		case TITANIUM:
		case VANADIUM:
		case CHROMIUM:
		case MANGANESE:
		case IRON:
		case COBALT:
		case NICKEL:
		case COPPER:
		case ZINC:
		case GALLIUM:
		case GERMANIUM:
		case ARSENIC:
		case SELENIUM:
		case BROMINE:
		case KRYPTON:
		case RUBIDIUM:
		case STRONTIUM:
		case YTTRIUM:
		case ZIRCONIUM:
		case NIOBIUM:
		case MOLYBDENUM:
		case TECHNETIUM:
		case RUTHENIUM:
		case RHOBIUM:
		case PALLADIUM:
		case SILVER:
		case CADMIUM:
		case INDIUM:
		case TIN:
		case ANTIMONY:
		case TELLURIUM:
		case IODINE:
		case XENON:
		case CESIUM:
		case BARIUM:
		case LANTHANUM:
		case CERIUM:
		case PARASEODYMIUM:
		case NEODYMIUM:
		case PROMETHIUM:
		case SAMARIUM:
		case EUROPIUM:
		case GADOLINIUM:
		case TERBIUM:
		case DYSPROSIUM:
		case HOLMIUM:
		case ERBIUM:
		case THULIUM:
		case YTTERBIUM:
		case LUTERTIUM:
		case HAFNIUM:
		case TANTALUM:
		case TUNGSTEN:
		case RHENIUM:
		case OSMIUM:
		case IRIDIUM:
		case PLATINUM:
		case GOLD:
		case MERCURY:
		case THALIUM:
		case LEAD:
		case BISMUTH:
		case POLONIUM:
		case ASTATINE:
		case RADON:
		case FRANCIUM:
		case RADIUM:
		case ACINIUM:
		case THORIUM:
		case PROTACTINIUM:
		case URANIUM:
		case NEPTUNIUM:
		case PLUTONIUM:
		default :
			Console.err("ElementType -> getDescription() -> invalid case : " + elementType);
			return "error";
		}
		
	}
	@Override
	public double getMass(ElementType elementType) {
		switch(elementType) {
		case HYDROGEN:
		case HELIUM:
		case LITHIUM:
		case BERYLIUM:
		case BORON:
		case CARBON:
		case NITROGEN:
		case OXYGEN:
		case FLOURINE:
		case NEON:
		case SODIUM:
		case MAGNESIUM:
		case ALUMINIUM:
		case SILICON:
		case PHOSPHORUS:
		case SULPHUR:
		case CHLORINE:
		case ARGON:
		case POTASSIUM:
		case CALCIUM:
		case SCANDIUM:
		case TITANIUM:
		case VANADIUM:
		case CHROMIUM:
		case MANGANESE:
		case IRON:
		case COBALT:
		case NICKEL:
		case COPPER:
		case ZINC:
		case GALLIUM:
		case GERMANIUM:
		case ARSENIC:
		case SELENIUM:
		case BROMINE:
		case KRYPTON:
		case RUBIDIUM:
		case STRONTIUM:
		case YTTRIUM:
		case ZIRCONIUM:
		case NIOBIUM:
		case MOLYBDENUM:
		case TECHNETIUM:
		case RUTHENIUM:
		case RHOBIUM:
		case PALLADIUM:
		case SILVER:
		case CADMIUM:
		case INDIUM:
		case TIN:
		case ANTIMONY:
		case TELLURIUM:
		case IODINE:
		case XENON:
		case CESIUM:
		case BARIUM:
		case LANTHANUM:
		case CERIUM:
		case PARASEODYMIUM:
		case NEODYMIUM:
		case PROMETHIUM:
		case SAMARIUM:
		case EUROPIUM:
		case GADOLINIUM:
		case TERBIUM:
		case DYSPROSIUM:
		case HOLMIUM:
		case ERBIUM:
		case THULIUM:
		case YTTERBIUM:
		case LUTERTIUM:
		case HAFNIUM:
		case TANTALUM:
		case TUNGSTEN:
		case RHENIUM:
		case OSMIUM:
		case IRIDIUM:
		case PLATINUM:
		case GOLD:
		case MERCURY:
		case THALIUM:
		case LEAD:
		case BISMUTH:
		case POLONIUM:
		case ASTATINE:
		case RADON:
		case FRANCIUM:
		case RADIUM:
		case ACINIUM:
		case THORIUM:
		case PROTACTINIUM:
		case URANIUM:
		case NEPTUNIUM:
		case PLUTONIUM:
		default :
			Console.err("ElementType -> getMass() -> invalid case : " + elementType);
			return 1.0;
		}
	}
	@Override
	public int getValue(ElementType elementType) {
		switch(elementType) {
		case HYDROGEN:
		case HELIUM:
		case LITHIUM:
		case BERYLIUM:
		case BORON:
		case CARBON:
		case NITROGEN:
		case OXYGEN:
		case FLOURINE:
		case NEON:
		case SODIUM:
		case MAGNESIUM:
		case ALUMINIUM:
		case SILICON:
		case PHOSPHORUS:
		case SULPHUR:
		case CHLORINE:
		case ARGON:
		case POTASSIUM:
		case CALCIUM:
		case SCANDIUM:
		case TITANIUM:
		case VANADIUM:
		case CHROMIUM:
		case MANGANESE:
		case IRON:
		case COBALT:
		case NICKEL:
		case COPPER:
		case ZINC:
		case GALLIUM:
		case GERMANIUM:
		case ARSENIC:
		case SELENIUM:
		case BROMINE:
		case KRYPTON:
		case RUBIDIUM:
		case STRONTIUM:
		case YTTRIUM:
		case ZIRCONIUM:
		case NIOBIUM:
		case MOLYBDENUM:
		case TECHNETIUM:
		case RUTHENIUM:
		case RHOBIUM:
		case PALLADIUM:
		case SILVER:
		case CADMIUM:
		case INDIUM:
		case TIN:
		case ANTIMONY:
		case TELLURIUM:
		case IODINE:
		case XENON:
		case CESIUM:
		case BARIUM:
		case LANTHANUM:
		case CERIUM:
		case PARASEODYMIUM:
		case NEODYMIUM:
		case PROMETHIUM:
		case SAMARIUM:
		case EUROPIUM:
		case GADOLINIUM:
		case TERBIUM:
		case DYSPROSIUM:
		case HOLMIUM:
		case ERBIUM:
		case THULIUM:
		case YTTERBIUM:
		case LUTERTIUM:
		case HAFNIUM:
		case TANTALUM:
		case TUNGSTEN:
		case RHENIUM:
		case OSMIUM:
		case IRIDIUM:
		case PLATINUM:
		case GOLD:
		case MERCURY:
		case THALIUM:
		case LEAD:
		case BISMUTH:
		case POLONIUM:
		case ASTATINE:
		case RADON:
		case FRANCIUM:
		case RADIUM:
		case ACINIUM:
		case THORIUM:
		case PROTACTINIUM:
		case URANIUM:
		case NEPTUNIUM:
		case PLUTONIUM:
		default :
			Console.err("ElementType -> getValue() -> invalid case : " + elementType);
			return 1;
		}
	}
	@Override
	public BufferedImage getRaster(ElementType elementType) {
		switch(elementType) {
		case HYDROGEN:
		case HELIUM:
		case LITHIUM:
		case BERYLIUM:
		case BORON:
		case CARBON:
		case NITROGEN:
		case OXYGEN:
		case FLOURINE:
		case NEON:
		case SODIUM:
		case MAGNESIUM:
		case ALUMINIUM:
		case SILICON:
		case PHOSPHORUS:
		case SULPHUR:
		case CHLORINE:
		case ARGON:
		case POTASSIUM:
		case CALCIUM:
		case SCANDIUM:
		case TITANIUM:
		case VANADIUM:
		case CHROMIUM:
		case MANGANESE:
		case IRON:
		case COBALT:
		case NICKEL:
		case COPPER:
		case ZINC:
		case GALLIUM:
		case GERMANIUM:
		case ARSENIC:
		case SELENIUM:
		case BROMINE:
		case KRYPTON:
		case RUBIDIUM:
		case STRONTIUM:
		case YTTRIUM:
		case ZIRCONIUM:
		case NIOBIUM:
		case MOLYBDENUM:
		case TECHNETIUM:
		case RUTHENIUM:
		case RHOBIUM:
		case PALLADIUM:
		case SILVER:
		case CADMIUM:
		case INDIUM:
		case TIN:
		case ANTIMONY:
		case TELLURIUM:
		case IODINE:
		case XENON:
		case CESIUM:
		case BARIUM:
		case LANTHANUM:
		case CERIUM:
		case PARASEODYMIUM:
		case NEODYMIUM:
		case PROMETHIUM:
		case SAMARIUM:
		case EUROPIUM:
		case GADOLINIUM:
		case TERBIUM:
		case DYSPROSIUM:
		case HOLMIUM:
		case ERBIUM:
		case THULIUM:
		case YTTERBIUM:
		case LUTERTIUM:
		case HAFNIUM:
		case TANTALUM:
		case TUNGSTEN:
		case RHENIUM:
		case OSMIUM:
		case IRIDIUM:
		case PLATINUM:
		case GOLD:
		case MERCURY:
		case THALIUM:
		case LEAD:
		case BISMUTH:
		case POLONIUM:
		case ASTATINE:
		case RADON:
		case FRANCIUM:
		case RADIUM:
		case ACINIUM:
		case THORIUM:
		case PROTACTINIUM:
		case URANIUM:
		case NEPTUNIUM:
		case PLUTONIUM:
		default :
			Console.err("ElementType -> getRaster() -> invalid case : " + elementType);
			return FrameworkSprites.error;
		}
	}
	
}
