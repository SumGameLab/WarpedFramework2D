/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.biology.fungi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum PhylumFungiType {

	ASCOMYCOTA,
	BASIDIOMYCOTA,
	CHYTRIDIOMYCOTA,
	MICROSPORIDIAN,
	ROZELLOMYCOTA,
	MICROSPORIDIOMYCOTA,
	APHELIDIOMYCOTA,
	NEOCALLIMASTIGOMYCOTA,
	CHYTRIDIOMYCTOA,
	BLASTOCLADIOMYCOTA,
	AMASTIOGOMYCOTA,
	
	PUCCINIOMYCOTINA,
	ORTHOMYCOTINA,
	TAPHRINOMYCOTINA,
	SACCHAROMYCOTINA,
	PEZIZOMYCOTINA;
	
	private static Map<Integer, PhylumFungiType> map = new HashMap<>();
	static {
		for (PhylumFungiType type : PhylumFungiType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static PhylumFungiType get(int index) {
	    return (PhylumFungiType) map.get(index);
	}
	public static PhylumFungiType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	
	public static List<String> getDescription(PhylumFungiType type){
		switch(type) {
		case ASCOMYCOTA:
			return Arrays.asList(
				"Ascomycota are characterized by a saclike structure, the ascus, which contains four to eight",
				"ascospores in the sexual stage.",
				"Many ascomycetes are plant pathogens, some are animal pathogens, a few are edible mushrooms,",
				"and many live on dead organic matter."
			);
		case BASIDIOMYCOTA:
			return Arrays.asList(
				"Basidiomycota includes jelly and shelf fungi, mushrooms, puffballs, and stinkhorns;",
				"certain yeasts; and the rusts and smuts. Basidiomycota are typically filamentous fungi composed of hyphae.",
				"Most species reproduce sexually with a club-shaped spore-bearing organ the basidium.", 
				"That usually produces four sexual spores which are borne on fruiting bodies." 
			);
		case CHYTRIDIOMYCOTA: 
			return Arrays.asList(
				"Chytridiomycota is distinguished by having zoospores with a single, posterior, whiplash structure called a flagellum.",
				"Species are microscopic in size, and most are found in freshwater or wet soils.",
				"Most are parasites of algae and animals or live on organic debris.",
				"A few species in the order Chytridiales cause plant disease and some have been shown to cause disease in amphibians."
			);	
		case MICROSPORIDIAN:
			return Arrays.asList(
				"Microsporidian are parasitic fungusfound mainly in cells of the gut epithelium of insects and the skin and muscles of vertebrates.", 
				"They also occur in annelids and some other invertebrates. Infection is characterized by enlargement of the affected tissue."
			);
		case AMASTIOGOMYCOTA:
			return Arrays.asList(
				"Amastigomycota are fungi without flagella or centrioles, and with unstacked Golgi apparatus cisternae. Members of this clade are Dikarya and the traditional paraphyletic assemblage \"Zygomycota\",[1][2][3] now divided into several monophyletic phyla.[4]",
				""
			);
		case APHELIDIOMYCOTA:
			return Arrays.asList("");
		case BLASTOCLADIOMYCOTA:
			return Arrays.asList("");
		case CHYTRIDIOMYCTOA:
			return Arrays.asList("");
		case MICROSPORIDIOMYCOTA:
			return Arrays.asList("");
		case NEOCALLIMASTIGOMYCOTA:
			return Arrays.asList("");
		case ORTHOMYCOTINA:
			return Arrays.asList("");
		case PEZIZOMYCOTINA:
			return Arrays.asList("");
		case PUCCINIOMYCOTINA:
			return Arrays.asList("");
		case ROZELLOMYCOTA:
			return Arrays.asList("");
		case SACCHAROMYCOTINA:
			return Arrays.asList("");
		case TAPHRINOMYCOTINA:
			return Arrays.asList("");
		default:
			System.err.println("FungiPHylumType -> getDescription() -> invalid case : " + type);
			return Arrays.asList("Error!");		
		}
	}
}
