/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.biology.plant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum PhylumPlantaeType {

	ANTHOCEROTOPHYTA, /**Hornwarts : Horn-shaped sporophytes, no vascular system*/
	BRYOPHYTA, /**Mosses : Persistent unbranched sporophytes, no vascular system*/
	CHLOROPHYTA, /**Green algae : Mainly autotrophs with exceptions*/
	CYCADOPHYTA, /**Cycads : Seeds, crown of compound leaves*/
	GINKGOPHYTA, /**Ginkgo : Seeds not protected by fruit*/
	GLAUCOPHYTA, /**Glaucophytes : */
	GNETOPHYTA,	/**Gnetophytes : Seeds, woody vascular system with vessels*/
	LYCOPODIOPHYTA,	/**Clubmosses : Microphyll leaves, vascular system*/
	MAGNOLIOPHYTA,/**Magnolia : Flowers and fruit, vascular system with vessels*/
	MARCHANTIOPHYTA, /**Liverworts : Ephemeral unbranched sporophyts, no vascular system*/
	PINOPHYTA, /**Conifers : Cones containing seeds and wood composed of tracheids*/
	POLYPODIOPHYTA;	/**Ferns : Prothallus gametophyts, vascular system*/
	
	private static Map<Integer, PhylumPlantaeType> map = new HashMap<>();
	static {
		for (PhylumPlantaeType type : PhylumPlantaeType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static PhylumPlantaeType get(int index) {
	    return (PhylumPlantaeType) map.get(index);
	}
	public static PhylumPlantaeType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public static List<String> getDescription(PhylumPlantaeType type){
		switch(type) {
		case ANTHOCEROTOPHYTA:
			return Arrays.asList(
				"Commonly known as Hornworts, Anthocerotophyta are a group of non-vascular plants that have an elongated horn-like structure, which is the sporophyte.",
				"Hornworts have a gametophyte-dominant life cycle, in which cells of the plant carry only a single set of genetic information;",
				"the flattened, green plant body of a hornwort is the gametophyte stage of the plant. Hornwor tend to grow only in places that are damp or humid.",
				"Some species grow in large numbers as tiny weeds in the soil. Large tropical and sub-tropical species may be found growing on the bark of trees."
			);
		case BRYOPHYTA:
			return Arrays.asList(
				"Bryophytes are non-vascular plants commonly known as moss. Bryophytes are limited in size and prefer moist habitats although,",
				"they can survive in drier environments. The bryophytes produce enclosed reproductive structures gametangia and sporangia,",
				"but they do not produce flowers or seeds. They reproduce sexually by spores and asexually by fragmentation or the production of gemmae."
			);
		case CHLOROPHYTA:
			return Arrays.asList(
				"Charophyta are green algae. They contain chlorophyll a and chlorophyll b and store food as starch in their plastids.",
				"They reproduce asexually by the development of a septum and sexually by fusion of the entire cell-contents of the two conjugating cells.",
				"In some charophyte groups, flagella are absent and sexual reproduction does not involve free-swimming flagellate sperm. ",
				"Unicellular or filamentous members of the Charophyta, are dominant in, acid waters, lochans, tarns and bogs."
			);
		case CYCADOPHYTA:
			return Arrays.asList(
				"Commonly known as Cycads, Cycadophyta are seed plants that typically have a stout and woody trunk. The species are dioecious, that is,",
				"individual plants of a species are either male or female. Cycads vary in size from having trunks only a few centimeters to several meters tall.",
				"They typically grow very slowly and live very long. Because of their superficial resemblance, they are sometimes mistaken for palms or ferns,",
				"but they are not closely related to either group. Cycads are gymnosperms, their unfertilized seeds are open to the air",
				"to be directly fertilized by pollination."
			);
		case GINKGOPHYTA:
			return Arrays.asList(
				"Ginkgophyta commonly known as Ginkgo are hardy deciduous trees. They resemble an angiosperm in that the woody stem is frequently and",
				"irregularly branched and bears broad leaves, which are fan-shaped with dichotomously branched veins.",
				"Ginkgo is dioecious and bears microsporangia and megasporangia on separate trees. Microstrobilus is borne on a dwarf shoot among the fan-shaped leaves.",
				"The microstrobilar axis bears stalked appendages at the ends of each of which are two microsporangia.",
				"Megastrobili are borne on elongated slender stalks, each with a pair of terminal ovules. Usually only one ovule matures into a seed."
			);
		case GLAUCOPHYTA:
			return Arrays.asList(
				"The Glaucophytes are unicellular algae found in freshwater and moist environments. They are similar to the original algal type",
				"that led to the red algae and green plants. Unlike red and green algae, glaucophytes only have asexual reproduction.",
				"The plastids of glaucophytes are known as cyanoplasts. Unlike the plastids in other organisms, they have a peptidoglycan layer,",
				"believed to be a relic of the endosymbiotic origin of plastids from cyanobacteria."
			);
		case GNETOPHYTA:
			return Arrays.asList(
				"The Gnetophytes specialization to their respective environments is so complete that they hardly resemble each other at all.",
				"The characteristics common in most Gnetophyta are the presence of enveloping bracts around the ovules",
				"Gnetum species are mostly woody vines in tropical forests. One species is ground-hugging two large leaves that grow from the base.",
				"Another species, have long slender branches which bear tiny scale-like leaves at their nodes."
			);
		case LYCOPODIOPHYTA:
			return Arrays.asList(
				"The lycophytes are a group of vascular plants that include the clubmosses. They are one of the oldest lineages of vascular plants;",
				"the group contains extinct plants that have been dated from the Silurian, 425 million years ago.",
				"Lycophytes were some of the dominating plant species of the Carboniferous period, and included the tree-like Lepidodendrales,",
				"which grew over 40 metres in height .Lycophytes reproduce by spores and have alternation of generations in which sporophyte generation is dominant."
			);
		case MAGNOLIOPHYTA:
			return Arrays.asList(
				"Magnoliophyta are flowering plants that bear fruits, commonly called angiosperms, they include all flowering plants without a woody stem,",
				"grasses and grass-like plants, a vast majority of broad-leaved trees, shrubs and vines, and most aquatic plants.",
				"They are by far the most diverse group of plants. Angiosperms are distinguished from the other seed-producing plants",
				"by having flowers and fruits that completely envelop the seeds"
			);
		case MARCHANTIOPHYTA:
			return Arrays.asList(
				"The Marchantiophyta are non-vascular plants commonly referred to as liverworts. Like mosses and hornworts,", 
				"they have a gametophyte-dominant life cycle, in which cells of the plant carry only a single set of genetic information.",
				"Some of the more familiar species grow as a flattened leafless thallus, but most species are leafy with a form very much like a flattened moss.",
				"Leafy species can be distinguished from the apparently similar mosses on the basis of a number of features, including their single-celled rhizoids.",
				"Leafy liverworts also differ from most mosses in that their leaves never have a costa present in many mosses."
			);
		case PINOPHYTA:
			return Arrays.asList(
				"Commonly known as Conifers, Pinophyta are a group of cone-bearing seed plants. Conifers are perennial woody plants with secondary growth.",
				"The great majority are trees, though a few are shrubs. Examples include cedars, cypresses, firs, junipers, pines, redwoods, spruces, and yews.",				
				"Conifers are ecologically important. They dominant large areas of land, forming extensive woods and forests.",
				"Boreal conifers have many wintertime adaptations. The narrow conical shape and their downward-drooping limbs, help them shed snow.",
				"Many of them seasonally alter their biochemistry to make them more resistant to freezing. While tropical rainforests have more biodiversity and turnover."
			);
		case POLYPODIOPHYTA:
			return Arrays.asList(
				"Polypodiophyta are commonly known as ferns, they are a group of vascular plants that reproduce via spores and have neither seeds nor flowers.",
				"Ferns consist of stems, roots and complex leaves called megaphylls, that are more complex than the microphylls of clubmosses.",
				"They produce coiled fiddleheads that uncoil and expand into fronds. Rerns are herbaceous perennials and most lack woody growth.",
				"When woody growth is present, it is found in the stem. Their foliage may be deciduous or evergreen and some are semi-evergreen depending on the climate."
			);
		default:
			System.err.println("PlantaePhylumType -> getDescription() -> invalid case : " + type);
			return Arrays.asList("Error!");		
		}
	}
	
}
