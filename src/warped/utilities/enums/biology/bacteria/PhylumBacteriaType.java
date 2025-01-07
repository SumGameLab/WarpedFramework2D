/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.biology.bacteria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum PhylumBacteriaType {

	ABDITIBACTERIOTA,
	ACIDOBACTERIOTA,
	ADTINOMYCETOTA,
	ATRIBACTEROTA,
	AQUIFICOTA,
	BACTEROIDOTA,
	BACILLOTA,
	CAMPYLOBACTEROTA,
	CHLAMYDIOTA,
	CHLOROBIOTA,
	CHLOROFLEXOTA,
	CYANOBACTERIA,
	DEFERRIBACTEROTA,
	DENIOCOCCOTA,
	DICTYOGLOMOTA,
	FUSOBACTERIOTA,
	GEMMATIOMONDAOTA,
	KRYPTONIA,
	LENTISPHAEROTA,
	MARINIMICROBAIA,
	MELAINABACTERIA,
	MODULIBACTERIA,
	NITROSPINOTA,
	PLANCTOMYCETOTA,
	PORIBACTERIA,
	PSEUDOMONADOTA,
	RHODOTHERMOTA,
	SCCHARIBACTERIA,
	SPIROCHAETOTA,
	SYNERGISTOTA,
	THERMOMICROBIOTA,
	THERMOTOGOTA,
	VERRUCOMICROBIOTA;
	
	private static Map<Integer, PhylumBacteriaType> map = new HashMap<>();
	static {
		for (PhylumBacteriaType type : PhylumBacteriaType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static PhylumBacteriaType get(int index) {
	    return (PhylumBacteriaType) map.get(index);
	}
	public static PhylumBacteriaType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public static List<String> getDescription(PhylumBacteriaType type) {
		switch(type) {
		case ABDITIBACTERIOTA:
			return Arrays.asList(
				"Abditibacteriota bacteria are widespread in extreme environments.", 
				"From polar and desert ecosystems to wastewater and contaminated mining sites.",
				"It has an optimized metabolism for survival in low nutrient habitats.",
				"Extreme resistance against antibiotics and toxic compounds was identified."
			);
		case ACIDOBACTERIOTA:
			return Arrays.asList(
				"Acidobacteriota are physiologically diverse, and can be found in a variety of environment.",
				"These bacteria are particularly abundant in soil habitats representing up to half of the total bacterial community.", 
				"Environmental factors such as pH and nutrients have been seen to drive Acidobacteriota dynamics.", 
				"Many Acidobacteriota are acidophilic"
			);
		case ADTINOMYCETOTA:
			return Arrays.asList(
				"The Actinomycetota are a diverse phylum of bacteria. They can be terrestrial or aquatic.", 
				"In soil they help to decompose the organic matter of dead organisms so the molecules can be taken up anew by plants.", 
				"While this role is also played by fungi, Actinomycetota are much smaller and likely do not occupy the same ecological niche.",
				"In this role the colonies often grow extensive mycelia, like a fungus would.",
				"Some soil actinomycetota live symbiotically with the plants whose roots pervade the soil,",
				"fixing nitrogen for the plants in exchange for access to some of the plant's saccharides." 
			);
		case AQUIFICOTA:
			return Arrays.asList(
				"The Aquificota phylum is a diverse collection of bacteria that live in harsh environmental settings.",
				"These bacteria are able to produce water by oxidizing hydrogen.",
				"They have been found in springs, pools, and oceans. They are autotrophs, and are the primary carbon fixers in their environments.", 
				"These bacteria are non-spore-forming. They are true bacteria as opposed to the other inhabitants of extreme environments"
			);
		case ATRIBACTEROTA:
			return Arrays.asList(
				"Atribacterota bacteria are common in anoxic sediments rich in methane.", 
				"They are broadly distributed and in some cases abundant in anaerobic marine sediments.", 
				"Genetic analyzes suggest a heterotrophic metabolism that gives rise to fermentation products.", 
				"These products in turn can support methanogens within the microbial community", 
				"According to research, Atribacterota shows patterns of gene expressions which consists of fermentative, acetogenic metabolism."
			);
		case BACILLOTA:
			return Arrays.asList(
				"Bacillota bacteria have round thick walled cells, called cocci, or rod-like forms.", 
				"A few Firmicutes, such as Megasphaera and Zymophilus, have a porous pseudo-outer membrane that causes them to stain",
				"Many Bacillota produce endospores, which are resistant to desiccation and can survive extreme conditions.", 
				"They are found in various environments, and the group includes some notable pathogens.",
				"Those in one family produce energy through anoxygenic photosynthesis."
			);
		case BACTEROIDOTA:
			return Arrays.asList(
				"Bacteroidota are rod-shaped bacteria and can be anaerobic or aerobic.", 
				"They are widely distributed in the environment, including in soil, sediments, and sea water.",
				"Bacteroidota can also be found in the guts and on the skin of animals.",
				"Some Bacteroides can be opportunistic pathogens, many Bacteroidota are symbiotic species.",
				"They perform metabolic conversions that are essential for the host, such as degradation of proteins or complex sugars.", 
				"Bacteroides are selectively recognized by the immune system of the host through interaction."
			);
		case CAMPYLOBACTEROTA:
			return Arrays.asList(
				"Many Campylobacterota are motile with flagella, found at deep-sea hydrothermal vents.", 
				"Chemolithotrophy meet their energy needs by oxidizing reduced sulfur, formate, or hydrogen.",
				"Coupled to the reduction of nitrate or oxygen.", 
				"Autotrophic Campylobacterota fix carbon dioxide into biomass.", 
				"The oxygen sensitivity of this pathway is consistent with their microaerophilic or anaerobic niche"
			);
		case CHLAMYDIOTA:
			return Arrays.asList(
				"Chlamydiota bacteria are remarkably diverse, including pathogens of humans and animals.", 
				"Known to be symbionts of ubiquitous protozoa, and marine sediment forms not yet well understood.",
				"All Chlamydiota are obligate intracellular bacteria; many specimins discovered in ocean-floor environments."
			);
		case CHLOROBIOTA:
			return Arrays.asList(
				"Chlorobiota are Green sulfur bacteria that utilize anaerobic photoautotrophic process to metabolize sulfur.",
				"These bacteria live in anaerobic aquatic environments, are nonmotile and capable of anoxygenic photosynthesis.", 
				"In contrast to plants, green sulfur bacteria mainly use sulfide ions as electron donors.", 
				"They are autotrophs that utilize the reverse tricarboxylic acid cycle to perform carbon fixation.", 
				"They are also mixotrophs and reduce nitrogen."
			);
		case CHLOROFLEXOTA:
			return Arrays.asList(
				"Chloroflexota are bacteria containing isolates with a diversity of phenotypes.",
				"Some Chloroflexota are aerobic thermophiles, which use oxygen and grow well in high temperatures.", 
				"Using light for photosynthesis and anaerobic halorespirers, which uses halogenated organics as electron acceptors.",
				"Chloroflexota have one cell membrane with no outer membrane making them a types of monoderms."
			);
		case CYANOBACTERIA:
			return Arrays.asList(
				"Cyanobacteria are probably the most numerous bacteria to have ever existed.", 
				"The first organisms known to have produced oxygen as a byproduct of photosynthesis",
				"Cyanobacteria use photosynthetic pigments, such as various forms of chlorophyll to convert sunlight to chemical energy.",
				"Cyanobacteria are thought to have converted earths early oxygen-poor atmosphere into an oxidizing one.",
				"This caused the rusting of the Earth. Which dramatically changed the composition of life forms on Earth"
			);
		case DEFERRIBACTEROTA:
			return Arrays.asList(
					"Deferribacteraceae are rod-shaped bacteria. Their rod shape may be straight or bent.",
					"They perform anaerobic respiration using iron, manganese, or nitrate.",
					"Known to have the ability to produce energy by fermentation"
				);
		case DENIOCOCCOTA:
			return Arrays.asList(
				"Deinococcota is a phylum of bacteria that are highly resistant to environmental hazards", 
				"Members of Deinococcota are commonly known as extremophiles.",
				"These bacteria have thick cell walls that give their resistant properties",
				"Notibly, they include a second membrane"
			);
		case DICTYOGLOMOTA:
			return Arrays.asList(
				"This organism is extremely thermophilic, meaning it thrives at extremely high temperatures.",
				"Dictyoglomota is chemoorganotrophic, it derives energy by metabolizing organic molecules.",
				"This organism is of interest because it elaborates an enzyme, xylanase, which digests xylan.",
				"By pretreating wood pulp with this enzyme, white paper can be produced with much less chlorine."
			);
		case FUSOBACTERIOTA:
			return Arrays.asList(
				"Fusobacteriota are obligately anaerobic bacteria of the non-sporeforming variety", 
				"Members of this phylum are pathogenic, others commensal and some both",
				"The involvement of Fusobacteriota is found in a wide spectrum of infections.",
				"Causing tissue necrosis and septicaemia, their significant in intra-amniotic infections."
			);
		case GEMMATIOMONDAOTA:
			return Arrays.asList(
				"Gemmatiomondaota bacterium able to grow by both aerobic and anaerobic respiration.",
				"A unique feature of this organism is the presence of bacterial photosynthetic reaction centers.", 
				"It probably acquired genes for anoxygenic photosynthesis via horizontal gene transfer.", 
				"Gemmatiomondaota are facultative photoheterotrophic organisms.",
				"They require the supply of organic substrate for growth, but it may obtain additional energy for its metabolism from light."
			);
		case KRYPTONIA:
			return Arrays.asList(
				"Analysis of the first genomes recovered from this group suggests a capacity for iron respiration.", 
				"Some Members of this group are incapable of producing key metabolic compounds and amino acids on their own.", 
				"Kryptonia may be metabolically dependent on other microbes in their environment."
			);
		case LENTISPHAEROTA:
			return Arrays.asList(
				"Lentisphaerota bacteria can be aerobic or anaerobic and fall under two distinct phenotypes.", 
				"These phenotypes live within bodies of sea water and are particularly hard to isolate in a pure culture.",
				"Some Lentisphaerota consists of terrestrial gut microbiota from mammals and birds.", 
				"Other Lentisphaerota have been found in coral microbiomes and marine sediment."
			);
		case MARINIMICROBAIA:
			return Arrays.asList(
				"Marinimicrobia bacteria have been found mainly at great depths and pressures.",
				"This phylum has a low representation in shallow pelagic samples and high abundance in deep samples.",
				"These bacteria are often associated with low dissolved oxygen environments", 
				"Very little is known about their ecology and metabolic functions."
			);
		case MELAINABACTERIA:
			return Arrays.asList(
				"Melainabacteria are related to Cyanobacteria.", 
				"These organisms have been found in the guts of various creatures and aquatic habitats such as groundwater.", 
				"The bacterial cell is similar to cyanobacteria in being surrounded by two membranes.", 
				"It differs from cyanobacteria in its ability to move by flagella.", 
				"Melainabacteria are not able to perform photosynthesis, but obtain energy by fermentation."				
			);
		case MODULIBACTERIA:
			return Arrays.asList(
				"Modulibacteria produce filamentous structures and are strictly anaerobic fermenters.",
				"Members of this phylum are capable of non-flagellar based gliding motility.",
				"They have an unusually large number of sensory and response regulator genes compared to other bacteria.", 
				"Members of the Modulibacteria phylum have been detected in a variety of environments including,", 
				"Industry, hypersaline mats, wetland sediments, verterbrate mouths, and tubeworms from coldseeps."
			);
		case NITROSPINOTA:
			return Arrays.asList(
				"Nitrospinota have only a few described species however are major nitrite-oxidizing bacteria.",
				"Nitrospinota oxidation of nitrite are important in the process of nitrification in marine environments.",
				"Although genus Nitrospina is aerobic bacterium, it was shown to oxidize nitrite also in oxygen depleted zones."
			);
		case PLANCTOMYCETOTA:
			return Arrays.asList(
				"The Planctomycetota are a phylum of widely distributed bacteria, occurring in both aquatic and terrestrial habitats.", 
				"They play a considerable role in global carbon and nitrogen cycles",
				"Many species capable of anaerobic ammonium oxidation, also known as anammox.", 
				"Many Planctomycetota occur in relatively high abundance as biofilms, often associating with other organisms."
			);
		case PORIBACTERIA:
			return Arrays.asList(
				"Poribacteria are primarily aerobic mixotrophs with the ability for oxidative phosphorylation",
				"and autotrophic carbon fixation via Ljungdahl pathway.",
				"Poribacterial is characterised by an enriched set of glycoside hydrolases, uronic acid degradation,",
				"as well as several specific sulfatases."
			);
		case PSEUDOMONADOTA:
			return Arrays.asList(
				"Pseudomonadota is a major phylum. Currently, they are considered the predominant phylum within the realm of bacteria.",
				"They are naturally found as pathogenic and free-living genera. The Pseudomonadota are widely diverse,",
				"with differences in morphology, metabolic processes and ecological influence."
			);
		case SCCHARIBACTERIA:
			return Arrays.asList(
				"Sccharibacteria is an obligate epibiont of various hosts, thriving on the surfaces of larger creatures.",
				"The full genome sequence revealed a highly reduced genome and a complete lack of amino acid biosynthetic capacity." 
			);
		case SPIROCHAETOTA:
			return Arrays.asList(
				"Spirochaete contains distinctive double-membrane, most of which have long, helically coiled cells.", 
				"Spirochaetes are chemoheterotrophic in nature they are distinguished from other bacteria by the",
				"location of their flagella, called endoflagella.", 
				"Endoflagella are anchored at each end of the bacterium within the space between the inner and outer membranes.", 
				"Where they project backwards to extend the length of the cell.", 
				"These cause a twisting motion which allows the spirochaete to move about."
			);
		case SYNERGISTOTA:
			return Arrays.asList(
				"Synergistota are anaerobic bacteria that have rod cell shape.", 
				"They have a diderm cell envelope and an atypical outer cell envelope.",
				"The Synergistota inhabit a majority of anaerobic environments including animal gastrointestinal tracts, soils, waste",
				"They are also present in sites of diseases such as cysts, abscesses, and areas of periodontal disease.",
				"Due to their presence at illness related sites, the Synergistota are suggested to be opportunistic pathogens.", 
				"However they can also be found in healthy individuals in the microbiome of the umbilicus and in normal flora."
			);
		case THERMOMICROBIOTA:
			return Arrays.asList(
				"Thermomicrobia is a group of thermophilic green non-sulfur bacteria.",
				"These bacteria are non-motile, non-spore-forming rods. Non-sporulating. No diamino acid present.", 
				"No peptidoglycan in significant amount. Atypical proteinaceous cell walls.",
				"Hyper-thermophilic, optimum growth temperature at 70-75 °C. Obligatory aerobic and chemoorganotrophic."
			);
		case THERMOTOGOTA:
			return Arrays.asList(
				"Thermotogota thrive at high temperatures along with their characteristic sheath structure, surrounding the cells",
				"Recently some Thermotogota existing at moderate temperatures have also been identified.",
				"Thermotogota are bounded by a single-unit lipid membrane, hence they are monoderm bacteria.", 
				"Theese bacteria possess the ability to metabolize different complex-carbohydrates", 
				"and release hydrogen gas as a bi-product."
			);
		case VERRUCOMICROBIOTA:
			return Arrays.asList(
				"Verrucomicrobiota have been isolated from fresh water, marine and soil environments.", 
				"They are abundant within the environment, though relatively inactive.", 
				"This phylum can be distinguished from neighbouring phylap by the presence of several conserved signature indels."
			);
		default:
			System.err.println("BacteriaPhylumType -> getPhylumDescription() -> invalid case : " + type);
			return Arrays.asList("Error!");
		}
		
	}
	
}
