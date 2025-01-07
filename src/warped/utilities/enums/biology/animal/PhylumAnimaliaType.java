/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.biology.animal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum PhylumAnimaliaType {

	ACANTHOCEPHALA, 	/*Thorny-headed worms*/
	ANNELIDA, 			/*Segmented worms*/
	CHAETOGNATHA, 		/*Arrow worms*/
	GNATHOSTOMULIDA, 	/*Jaw worms*/
	NEMATODA, 			/*Round worms*/
	NEMATOMORPHA, 		/*Horsehair worms*/
	NEMERTEA, 			/*Ribbon Worms*/
	ONYCHOPHORA, 		/*Velvet worms*/
	PLATYHELMINTHES, 	/*Flat worms*/
	
	ARTHROPODA, 		/*Insects, crustaceans, arachnids, centipedes and millipedes*/
	BRACHIOPODA, 		/*Lamp shells*/
	BRYOZOA, 			/*moss animals*/
	
	CHORDATA, 			/*vertebrates, tunicates, lancelets*/
	CNIDARIA, 			/*marine singing animals*/
	ECHINODERMATA, 		/*starfish, sea urchins, sand dollars, sea lilies*/
	GASTROTRICHA, 		/*Hairybacks*/
	KINORHYNCHA, 		/*mud dragons*/
	MOLLUSCA, 			/*mollusks*/
	
	PORIFERA, 			/*sponges*/
	TARDIGRADA; 		/*Water bears*/
	
	private static Map<Integer, PhylumAnimaliaType> map = new HashMap<>();
	static {
		for (PhylumAnimaliaType type : PhylumAnimaliaType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static PhylumAnimaliaType get(int index) {
	    return (PhylumAnimaliaType) map.get(index);
	}
	public static PhylumAnimaliaType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	public List<String> getDescription(){ return getDescription(this);}
	public static List<String> getDescription(PhylumAnimaliaType type){
		switch(type) {
		case ACANTHOCEPHALA:
			return Arrays.asList(
				"Acanthocephala is a group of parasitic worms known as thorny-headed worms.", 
				"They are characterized by the presence of an eversible proboscis, armed with spines, which it uses to pierce and hold the gut wall of its host.", 
				"Acanthocephalans have complex life cycles, involving at least two hosts, which include invertebrates."
			);
		case ANNELIDA: 	
			return Arrays.asList(
				"The annelids also known as the segmented worms, are a large phylum, with over 22,000 extant species including :",
				"ragworms, earthworms, and leeches. The species exist in and have adapted to various ecologies,",
				"some in marine environments as distinct as tidal zones and hydrothermal vents, others in fresh water, and yet others in moist terrestrial environments."
			);
		case CHAETOGNATHA:
			return Arrays.asList(
				"The Chaetognatha are predatory marine worms that are a major component of plankton worldwide. Commonly known as arrow worms, they are mostly nektonic;",
				"however about 20% of the known species are benthic, and can attach to algae and rocks. They are found in all marine waters and polar regions.",
				"Most chaetognaths are transparent and are torpedo shaped, but some deep-sea species are orange. They range in size from 2 to 120 millimetres."               
			);                   
		case GNATHOSTOMULIDA:
			return Arrays.asList(
				"Gnathostomulids, or jaw worms are nearly microscopic marine animals. They inhabit sand and mud beneath shallow coastal waters and can survive",
				"in relatively anoxic environments. Most gnathostomulids measure 0.5 to 1 millimetre in length. They are often slender to thread-like worms,",
				"with a generally transparent body, the neck region is slightly narrower than the rest of the body, giving them a distinct head.",
				"Like flatworms they have a ciliated epidermis. The cilia allow the worms to glide along in the water between sand grains, although they also use muscles"
			);                   
		case NEMATODA: 		
			return Arrays.asList(
				"The nematodes, roundworms or eelworms are a diverse animal phylum inhabiting a broad range of environments.",
				"Most species are free-living, feeding on microorganisms, but there are many that are parasitic.",
				"The parasitic worms are the cause of soil-transmitted helminthiases."               
			);                   
		case NEMATOMORPHA: 	
			return Arrays.asList(
				"Nematomorpha commonly known as horsehair worms or Gordian worms are parasitoid animals superficially similar to nematode worms in morphology.", 
				"Most species range in size from 50 to 100 millimetres, reaching 2 metres in extreme cases, and 1 to 3 millimetres in diameter.",
				"Horsehair worms can be discovered in damp areas, such as watering troughs, swimming pools, streams, puddles, and cisterns.",
				"The adult worms are free-living, but the larvae are parasitic on arthropods."               
			);                   
		case NEMERTEA: 		
			return Arrays.asList(
				"Nemertea also known as ribbon worms are very slim, usually only a few millimeters wide, although a few have relatively short but wide bodies.",
				"Many have patterns of yellow, orange, red and green coloration. The foregut, stomach and intestine run a little below the midline of the body,",
				"the anus is at the tip of the tail, and the mouth is under the front. A little above the gut is a cavity which mostly runs above the midline and",
				"ends a little short of the rear of the body. Nemertea capture prey with venom. A muscle pulls the proboscis in when an attack ends.",
				"A few species with stubby bodies filter feed and have suckers at the front and back ends, with which they attach to a host."               
			);                   
		case ONYCHOPHORA: 	
			return Arrays.asList(
				"Onychophora commonly known as velvet worms due to their velvety texture and somewhat wormlike appearance.",
				"They are soft-bodied, many-legged animals. In appearance they have variously been compared to worms with legs, caterpillars, and slugs.",
				"They prey upon other invertebrates, which they catch by ejecting an adhesive slime.",
				"The two families of velvet worms are Peripatidae and Peripatopsidae. Velvet worms are generally considered close relatives of the Arthropoda."
			);                   
		case PLATYHELMINTHES:
			return Arrays.asList(
				"Platyhelminthes refered to as flatworms are unsegmented, soft-bodied invertebrates having no body cavity",
				"and no specialised circulatory or respiratory organs. They are restricted to having flattened shapes that allow oxygen and nutrients to pass",
				"through their bodies by diffusion. The digestive cavity has only one opening for both ingestion and egestion.",
				"As a result food can not be processed continuously."               
			);                   
	               
			
		case ARTHROPODA:
			return Arrays.asList(
				"Arthropods are invertebrates. They possess an exoskeleton with a cuticle made of chiti, often mineralised with calcium carbonate.",
				"They have a body with differentiated segments, and paired jointed appendages. In order to keep growing, they must go through stages of moulting,",
				"a process by which they shed their exoskeleton to reveal a new one. They are an extremely diverse group, with up to 10 million species."               
			);                   
		case BRACHIOPODA:
			return Arrays.asList(
				"Brachiopods are animals that have hard 'valves' shells on the upper and lower surfaces, unlike the left and right arrangement in bivalve molluscs.",
				"Brachiopod valves are hinged at the rear end, while the front can be opened for feeding or closed for protection.",
				"Two major categories are traditionally recognized, articulate and inarticulate brachiopods."               
			);                   
		case BRYOZOA:
			return Arrays.asList(
				"Bryozoa commonly as moss animals are simple, aquatic invertebrate animals, nearly all living in sedentary colonies. Typically about 0.5 millimetres long.",
				"They have a special feeding structure called a lophophore, a crown of tentacles used for filter feeding.",
				"Most marine bryozoans live in tropical waters, but a few are found in oceanic trenches and polar waters, freshwater bryozoans  are known as Phylactolaemata."               
			);                   
			
		               
		case CHORDATA:
			return Arrays.asList(
				"Chordata possess, five distinctive physical characteristics that distinguish them from other phylum. These are a a hollow dorsal nerve cord,",
				" an endostyle or thyroid, pharyngeal slits, and a post-anal tail. Chordates are bilaterally symmetric, have a coelom,",
				"possess a closed circulatory system, and exhibit metameric segmentation. Well known classes of this phylum are fish, amphibians, reptiles and birds"               
			);                   
		case CNIDARIA: 		
			return Arrays.asList(
				"Cnidaria are aquatic animals found both in fresh water and marine environments. They including jellyfish, hydroids, sea anemones, corals and",
				"some of the smallest marine parasites. Their distinguishing features are a decentralized nervous system distributed throughout a gelatinous body and",
				"the presence of cnidocytes,  specialized cells with ejectable flagella used mainly for envenomation and capturing prey.",
				"Their bodies consist of mesoglea, a non-living, jelly-like substance, sandwiched between two layers of epithelium that are mostly one cell thick.",
				"Cnidarians are also some of the only animals that can reproduce both sexually and asexually."              
			);                                    
		case ECHINODERMATA: 	
			return Arrays.asList(
				"Echinodermata includes starfish, brittle stars, sea urchins, sand dollars and sea cucumbers, as well as the sessile sea lilies",
				"As adults echinoderms are recognisable by their usually five-pointed radial symmetry and are found on the sea bed at every ocean depth",
				"Ecologically, there are few other groupings so abundant in the biotic desert of the deep sea, as well as shallower oceans.",
				"Most echinoderms are able to reproduce asexually and regenerate tissue, organs and limbs; in some cases, they can undergo complete regeneration from a single limb."               
			);                   
		case GASTROTRICHA: 	
			return Arrays.asList(
				"The gastrotrichs commonly referred to as hairybacks, are a group of microscopic, cylindrical, acoelomate animals, and are widely distributed and abundant in",
				"freshwater and marine environments. They are mostly benthic and live within the periphyton, the layer of tiny organisms and detritus that is found",
				"on the seabed and the beds of other water bodies. The majority live on and between particles of sediment or on other submerged surfaces."				              
			);                   
		case KINORHYNCHA: 	
			return Arrays.asList(
				"Kinorhynchs are limbless animals, with a body consisting of a head, neck, and a trunk of eleven segments.",
				"Juveniles have eight or nine segments, depending on genus, with the last two or three being added later during growth.",
				"Like other ecdysozoans they do not have external cilia, but instead have a number of spines along the body, plus up to seven circles of spines around the head.",
				"These spines are used for locomotion, withdrawing the head and pushing forward, then gripping the substrate with the spines while drawing up the body."               
			);                   
		case MOLLUSCA: 		
			return Arrays.asList(
				"Mollusca, known as molluscs, live in marine, freshwater and terrestrial habitats. They are highly diverse, not just in size and anatomical structure,",
				"but also in behaviour and habitat. Cephalopod molluscs, such as squid, cuttlefish, and octopuses, are among the most neurologically advanced",
				"of all invertebrates—and either the giant squid or the colossal squid is the largest known extant invertebrate species.",
				"The gastropods, snails and slugs, are by far the most diverse molluscs and account for 80% of the total classified species."             
			);                   
			         
		case PORIFERA:
			return Arrays.asList(
				"Porifera, commonly known as sponges are multicellular organisms that have bodies full of pores and channels allowing water to circulate through them.",
				"Sponges have unspecialized cells that can transform into other types and that often migrate between the main cell layers in the process.",
				" Sponges do not have complex nervous, digestive or circulatory systems, instead, most rely on maintaining a constant water flow through their bodies",
				"to obtain food and oxygen and to remove wastes."               
			);                   
		case TARDIGRADA:
			return Arrays.asList(
				"Tardigrades known as water bears are eight-legged segmented micro-animals. They have been found in diverse biospheres.",
				"Tardigrades are among the most resilient animals known, with individual species able to survive extreme conditions such as,",
				"exposure to extreme temperatures, extreme pressures, air deprivation, radiation, dehydration, and starvation",
				"that would quickly kill most other known forms of life. Tardigrades have survived exposure to outer space."               
			);                   
	
		default: 
			System.err.println("AnimaliaPhylumType -> getDescription() -> invalid case : " + type);
			return Arrays.asList("Error!");		
		}
	}
	
}
