/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils;

import java.util.ArrayList;

public class UtilsName {

	private final static int MAX_ATTEMPTS = 10000;
	
	private final static String lexicon = "abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";	
	
	private static ArrayList<String> prefix 	 = new ArrayList<>();
	private static ArrayList<String> suffix 	 = new ArrayList<>();
	private static ArrayList<String> rootA  	 = new ArrayList<>();
	private static ArrayList<String> rootB  	 = new ArrayList<>();
	private static ArrayList<String> rootC  	 = new ArrayList<>();
	private static ArrayList<String> rootD  	 = new ArrayList<>();
	private static ArrayList<String> rootE  	 = new ArrayList<>();
	private static ArrayList<String> rootF  	 = new ArrayList<>();
	private static ArrayList<String> rootG  	 = new ArrayList<>();
	private static ArrayList<String> rootH       = new ArrayList<>();
	private static ArrayList<String> rootI  	 = new ArrayList<>();
	private static ArrayList<String> rootJ  	 = new ArrayList<>();
	private static ArrayList<String> rootK  	 = new ArrayList<>();
	private static ArrayList<String> rootL  	 = new ArrayList<>();
	private static ArrayList<String> rootM  	 = new ArrayList<>();
	private static ArrayList<String> rootN  	 = new ArrayList<>();
	private static ArrayList<String> rootO  	 = new ArrayList<>();
	private static ArrayList<String> rootP  	 = new ArrayList<>();
	private static ArrayList<String> rootQ  	 = new ArrayList<>();
	private static ArrayList<String> rootR  	 = new ArrayList<>();
	private static ArrayList<String> rootS  	 = new ArrayList<>();
	private static ArrayList<String> rootT  	 = new ArrayList<>();
	private static ArrayList<String> rootU  	 = new ArrayList<>();
	private static ArrayList<String> rootV  	 = new ArrayList<>();
	private static ArrayList<String> rootW  	 = new ArrayList<>();
	private static ArrayList<String> rootX  	 = new ArrayList<>();
	private static ArrayList<String> rootY  	 = new ArrayList<>();
	private static ArrayList<String> rootZ  	 = new ArrayList<>();
	private static ArrayList<String> maleNames 	 = new ArrayList<>();
	private static ArrayList<String> femaleNames = new ArrayList<>();
	private static ArrayList<String> lastNames 	 = new ArrayList<>();
	private static ArrayList<String> shipNames 	 = new ArrayList<>();
	private static ArrayList<String> shipPrefix  = new ArrayList<>();
	
	private static ArrayList<String> beetleFirstNames 	= new ArrayList<>();
	private static ArrayList<String> beetleLastNames 	= new ArrayList<>();
	private static ArrayList<String> catFirstNames 		= new ArrayList<>();
	private static ArrayList<String> catLastNames 		= new ArrayList<>();
	private static ArrayList<String> cricketFirstNames  = new ArrayList<>();
	private static ArrayList<String> cricketLastNames 	= new ArrayList<>();
	private static ArrayList<String> dryadFirstNames 	= new ArrayList<>();
	private static ArrayList<String> dryadLastNames 	= new ArrayList<>();
	private static ArrayList<String> humanFirstNames 	= new ArrayList<>();
	private static ArrayList<String> lavaFirstNames 	= new ArrayList<>();
	private static ArrayList<String> lavaLastNames 		= new ArrayList<>();
	private static ArrayList<String> lizardFirstNames 	= new ArrayList<>();
	private static ArrayList<String> lizardLastNames 	= new ArrayList<>();
	private static ArrayList<String> bearFirstNames 	= new ArrayList<>();
	private static ArrayList<String> bearLastNames 		= new ArrayList<>();
	private static ArrayList<String> roosterFirstNames 	= new ArrayList<>();
	private static ArrayList<String> roosterLastNames 	= new ArrayList<>();
	private static ArrayList<String> starfishFirstNames	= new ArrayList<>();
	private static ArrayList<String> starfishLastNames 	= new ArrayList<>();
	private static ArrayList<String> vultureFirstNames 	= new ArrayList<>();
	private static ArrayList<String> vultureLastNames 	= new ArrayList<>();
	
	private static ArrayList<String> assignedIdentifiers = new ArrayList<>();
	private static ArrayList<String> assignedLatin  	 = new ArrayList<>();
	private static ArrayList<String> assignedMaleNames 	 = new ArrayList<>();
	private static ArrayList<String> assignedFemaleNames = new ArrayList<>();
	private static ArrayList<String> assignedLastNames 	 = new ArrayList<>();
	private static ArrayList<String> assignedShipNames 	 = new ArrayList<>();
	

	//--------
	//---------------- Access --------
	//--------	
	public static String getUniqueIdentifier() {
		String result = "";
		int tick = 0; 
		int fragments = UtilsMath.random(3) + 7;
		
		while(result.equals("")) {
			tick++;
			if(tick > MAX_ATTEMPTS) {
				Console.err("UtilsName -> generateUniqueIdentifier() -> couldn't generate unique identifier, exceeded MAX_ATTEMPTS : " + MAX_ATTEMPTS);
				result = "Error!";
				continue;
			}

			String identifier = "";
			for(int i = 0; i < fragments; i++) {
				identifier += lexicon.charAt(UtilsMath.random.nextInt(lexicon.length()));
			}
			
			if(!assignedIdentifiers.contains(identifier)) {
				assignedIdentifiers.add(identifier);
				result = identifier;
			} else continue;
		}
		return result;
	}
	
	public static String getUniqueLatin() {
		String result = "";
		int tick = 0; 
		
		while(result.equals("")) {
			tick++;
			if(tick > MAX_ATTEMPTS) {
				Console.err("UtilsName -> generateUniqueLatin() -> couldn't generate unique latin, exceeded MAX_ATTEMPTS : " + MAX_ATTEMPTS);
				result = "Error!";
				break;
			}
		
			String latin = "";
			latin = generateRandomLatin();
			
			if(!assignedLatin.contains(latin)) {
				assignedLatin.add(latin);
				result = latin;
			} else continue;	
		}
		return result;
	}
	
	public static String getUniqueShipName() {
		String result = "";
		int tick = 0;
		
		while(result.equals("")) {
			tick++;
			if(tick > MAX_ATTEMPTS) {
				Console.err("UtilsName -> generateUniqueShipName() -> couldn't generate unique ship name, exceeded MAX_ATTEMPTS : " + MAX_ATTEMPTS);
				result = "Error!";
				continue;
			}
			
			String shipName = "";
			shipName = generateShipName();
			
			if(!assignedShipNames.contains(shipName)) {
				assignedShipNames.add(shipName);
				result = shipName;
			} else continue;	
		}
		return result;
	}
	
	public static String getUniqueMaleName() {
		String result = "";
		int tick = 0;
		
		while(result.equals("")) {
			tick++;
			if(tick > MAX_ATTEMPTS) {
				Console.err("UtilsName -> generateUniqueMaleName() -> couldn't generate unique male name, exceeded MAX_ATTEMPTS : " + MAX_ATTEMPTS);
				result = "Error!";
				continue;
			}
			
			String maleName = "";
			maleName = getRandMaleName();
			
			if(!assignedMaleNames.contains(maleName)) {
				assignedMaleNames.add(maleName);
				result = maleName;
			} else continue;	
		}
		return result;
	}
	
	public static String getUniqueFemaleName() {
		String result = "";
		int tick = 0;
		
		while(result.equals("")) {
			tick++;
			if(tick > MAX_ATTEMPTS) {
				Console.err("UtilsName -> generateUniqueMaleName() -> couldn't generate unique male name, exceeded MAX_ATTEMPTS : " + MAX_ATTEMPTS);
				result = "Error!";
				continue;
			}
			
			String femaleName = "";
			femaleName = getRandFemaleName();
			
			if(!assignedFemaleNames.contains(femaleName)) {
				assignedFemaleNames.add(femaleName);
				result = femaleName;
			} else continue;	
		}
		return result;
	}
	
	public static String getUniqueLastName() {
		String result = "";
		int tick = 0;
		
		while(result.equals("")) {
			tick++;
			if(tick > MAX_ATTEMPTS) {
				Console.err("UtilsName -> generateUniqueMaleName() -> couldn't generate unique male name, exceeded MAX_ATTEMPTS : " + MAX_ATTEMPTS);
				result = "Error!";
				continue;
			}
			
			String lastName = "";
			lastName = getRandLastName();
			
			if(!assignedLastNames.contains(lastName)) {
				assignedLastNames.add(lastName);
				result = lastName;
			} else continue;	
		}
		return result;
	}
	

	public static String getBeetleName()   {return beetleFirstNames.get(UtilsMath.random(beetleFirstNames.size())) + " " + beetleLastNames.get(UtilsMath.random(beetleLastNames.size()));}
	public static String getCatName() 	   {return catFirstNames.get(UtilsMath.random(catFirstNames.size())) + " " + catLastNames.get(UtilsMath.random(catLastNames.size()));}
	public static String getCricketName()  {return cricketFirstNames.get(UtilsMath.random(cricketFirstNames.size())) + " " + cricketLastNames.get(UtilsMath.random(cricketLastNames.size()));}
	public static String getDryadName()    {return dryadFirstNames.get(UtilsMath.random(dryadFirstNames.size())) + " " + dryadLastNames.get(UtilsMath.random(dryadLastNames.size()));}
	public static String getHumanName()    {return humanFirstNames.get(UtilsMath.random(humanFirstNames.size())) + " " + getRandLastName();}
	public static String getLarvaName()    {return lavaFirstNames.get(UtilsMath.random(lavaFirstNames.size())) + " " + lavaLastNames.get(UtilsMath.random(lavaLastNames.size()));}
	public static String getLizardName()   {return lizardFirstNames.get(UtilsMath.random(lizardFirstNames.size())) + " " + lizardLastNames.get(UtilsMath.random(lizardLastNames.size()));}
	public static String getBearName() 	   {return bearFirstNames.get(UtilsMath.random(bearFirstNames.size())) + " " + bearLastNames.get(UtilsMath.random(bearLastNames.size()));}
	public static String getRoosterName()  {return roosterFirstNames.get(UtilsMath.random(roosterFirstNames.size())) + " " + roosterLastNames.get(UtilsMath.random(roosterLastNames.size()));}
	public static String getStarfishName() {return starfishFirstNames.get(UtilsMath.random(starfishFirstNames.size())) + " " + starfishLastNames.get(UtilsMath.random(starfishLastNames.size()));}
	public static String getVultureName()  {return vultureFirstNames.get(UtilsMath.random(vultureFirstNames.size())) + " " + vultureLastNames.get(UtilsMath.random(vultureLastNames.size()));}
	
	
	//--------
	//---------------- Name Generation --------
	//--------
	private static String generateShipName() {
		String result = "";
		if(UtilsMath.random.nextBoolean()) result = getRandShipPrefix();
		result += getRandShipSuffix();
		return result;
	}
	
	private static String generateRandomLatin() {
		String result = "";
		for(int i = 0; i < 2 + UtilsMath.random.nextInt(1); i++) {
			if(i == 0) {				
				String root = generateRandomRoot();
				result += root.substring(0, 1).toUpperCase();
				result += root.substring(1, root.length());
			} else result += generateRandomRoot();
		}
		if(UtilsMath.coinFlip()) {			
			result += " ";
			for(int i = 0; i < 1 + UtilsMath.random.nextInt(1); i++) {
				if(i == 0) {				
					String root = generateRandomRoot();
					result += root.substring(0, 1).toUpperCase();
					result += root.substring(1, root.length());
				} else result += generateRandomRoot();
			}
		}
		return result;
	}
	
	private static String generateRandomRoot() {
		String result = "";
		switch(UtilsMath.random.nextInt(25)) { 
			case 0:  result = getRandRootA(); break;
			case 1:  result = getRandRootB(); break;
			case 2:  result = getRandRootC(); break;
			case 3:  result = getRandRootD(); break;
			case 4:  result = getRandRootE(); break;
			case 5:  result = getRandRootF(); break;
			case 6:  result = getRandRootG(); break;
			case 7:  result = getRandRootH(); break;
			case 8:  result = getRandRootI(); break;
			case 9:  result = getRandRootJ(); break;
			case 10: result = getRandRootK(); break;
			case 11: result = getRandRootL(); break;
			case 12: result = getRandRootM(); break;
			case 13: result = getRandRootN(); break;
			case 14: result = getRandRootO(); break;
			case 15: result = getRandRootP(); break;
			case 16: result = getRandRootQ(); break;
			case 17: result = getRandRootR(); break;
			case 18: result = getRandRootS(); break;
			case 19: result = getRandRootT(); break;
			case 20: result = getRandRootU(); break;
			case 21: result = getRandRootV(); break;
			case 22: result = getRandRootW(); break;
			case 23: result = getRandRootX(); break;
			case 24: result = getRandRootY(); break;
			case 25: result = getRandRootZ(); break;	
		}
		return result;
	}
	
	private static String getRandRootA() {return rootA.get(UtilsMath.random(rootA.size()));}
	private static String getRandRootB() {return rootB.get(UtilsMath.random(rootB.size()));}
	private static String getRandRootC() {return rootC.get(UtilsMath.random(rootC.size()));}
	private static String getRandRootD() {return rootD.get(UtilsMath.random(rootD.size()));}
	private static String getRandRootE() {return rootE.get(UtilsMath.random(rootE.size()));}
	private static String getRandRootF() {return rootF.get(UtilsMath.random(rootF.size()));}
	private static String getRandRootG() {return rootG.get(UtilsMath.random(rootG.size()));}
	private static String getRandRootH() {return rootH.get(UtilsMath.random(rootH.size()));}
	private static String getRandRootI() {return rootI.get(UtilsMath.random(rootI.size()));}
	private static String getRandRootJ() {return rootJ.get(UtilsMath.random(rootJ.size()));}
	private static String getRandRootK() {return rootK.get(UtilsMath.random(rootK.size()));}
	private static String getRandRootL() {return rootL.get(UtilsMath.random(rootL.size()));}
	private static String getRandRootM() {return rootM.get(UtilsMath.random(rootM.size()));}
	private static String getRandRootN() {return rootN.get(UtilsMath.random(rootN.size()));}
	private static String getRandRootO() {return rootO.get(UtilsMath.random(rootO.size()));}
	private static String getRandRootP() {return rootP.get(UtilsMath.random(rootP.size()));}
	private static String getRandRootQ() {return rootQ.get(UtilsMath.random(rootQ.size()));}
	private static String getRandRootR() {return rootR.get(UtilsMath.random(rootR.size()));}
	private static String getRandRootS() {return rootS.get(UtilsMath.random(rootS.size()));}
	private static String getRandRootT() {return rootT.get(UtilsMath.random(rootT.size()));}
	private static String getRandRootU() {return rootU.get(UtilsMath.random(rootU.size()));}
	private static String getRandRootV() {return rootV.get(UtilsMath.random(rootV.size()));}
	private static String getRandRootW() {return rootW.get(UtilsMath.random(rootW.size()));}
	private static String getRandRootX() {return rootX.get(UtilsMath.random(rootX.size()));}
	private static String getRandRootY() {return rootY.get(UtilsMath.random(rootY.size()));}
	private static String getRandRootZ() {return rootZ.get(UtilsMath.random(rootZ.size()));}
	
	private static String getRandShipPrefix() {return shipPrefix.get( UtilsMath.random.nextInt(shipPrefix.size()));}
	private static String getRandShipSuffix() {return shipNames.get(  UtilsMath.random.nextInt(shipNames.size()));}
	private static String getRandMaleName()   {return maleNames.get(  UtilsMath.random.nextInt(maleNames.size()));}
	private static String getRandFemaleName() {return femaleNames.get(UtilsMath.random.nextInt(femaleNames.size()));}
	private static String getRandLastName()   {return lastNames.get(  UtilsMath.random.nextInt(lastNames.size()));}
	
	
	//--------
	//---------------- Initialization --------
	//--------		
	public static void initialize() {
		initPrefix();
		initSuffix();
		initRoot();
		initMaleNames();
		initFemaleNames();
		initLastNames();
		initShipNames();
		initShipPrefix();
		
		initCelestialNames();
	}
	
	//--------
	//---------------- Alien Names -------
	//-------- 
	
	private static void initCelestialNames() {
		initBeetleNames();
		initCatNames();
		initCricketNames();
		initDryadNames();
		initHumanNames();
		initLavaNames();
		initLizzardNames();
		initRoosterNames();
		initStarfishNames();
		initBearNames();
		initVultureNames();
	}
	
	private static void initBeetleNames() {
		beetleFirstNames.add("Bar");
		beetleFirstNames.add("Lar");
		beetleFirstNames.add("Rar");
		beetleFirstNames.add("Char");
		beetleFirstNames.add("Star");
		beetleFirstNames.add("Nar");
		beetleFirstNames.add("Tar");
		beetleFirstNames.add("Far");
		beetleFirstNames.add("Dar");
		beetleFirstNames.add("Var");
		
		beetleLastNames.add("Tack");
		beetleLastNames.add("Stek");
		beetleLastNames.add("Atek");
		beetleLastNames.add("Bawtek");
		beetleLastNames.add("Cortek");
		beetleLastNames.add("Sortak");
		beetleLastNames.add("Lawtek");
		beetleLastNames.add("Nurtack");
		beetleLastNames.add("Surtack");
		beetleLastNames.add("Curtack");		
	}
	
	private static void initCricketNames() {
		cricketFirstNames.add("Crick");
		cricketFirstNames.add("Rick");
		cricketFirstNames.add("Hick");
		cricketFirstNames.add("Flik");
		cricketFirstNames.add("Slick");
		cricketFirstNames.add("Pop");
		cricketFirstNames.add("Jiminy");
		cricketFirstNames.add("Cri-Kee");
		cricketFirstNames.add("Chirp");
		cricketFirstNames.add("Crumpet");
		
		cricketLastNames.add("Cricket");
		cricketLastNames.add("Hopper");
		cricketLastNames.add("Wings");
		cricketLastNames.add("Hopper");
		cricketLastNames.add("Tettigon");
		cricketLastNames.add("Gryllidae");
		cricketLastNames.add("Nemobiinae");
		cricketLastNames.add("Trigonidiinae");
		cricketLastNames.add("Mogoplistidae");
		cricketLastNames.add("Pteroplistinae");	
	}
	
	private static void initCatNames() {
		catFirstNames.add("Spot");
		catFirstNames.add("Spek");
		catFirstNames.add("Noot");
		catFirstNames.add("Bloop");
		catFirstNames.add("Toot");
		catFirstNames.add("Coop");
		catFirstNames.add("Dewp");
		catFirstNames.add("Mewt");
		catFirstNames.add("Groot");
		catFirstNames.add("Zoot");
		
		catLastNames.add("Rew");
		catLastNames.add("Raw");
		catLastNames.add("Mew");
		catLastNames.add("Paw");
		catLastNames.add("Maw");
		catLastNames.add("Claw");
		catLastNames.add("Sew");
		catLastNames.add("Clew");
		catLastNames.add("Chew");
		catLastNames.add("New");
	}
	
	private static void initDryadNames() {
		dryadFirstNames.add("Grow");
		dryadFirstNames.add("Stow");
		dryadFirstNames.add("Vow");
		dryadFirstNames.add("Krow");
		dryadFirstNames.add("Yevo");
		dryadFirstNames.add("Ikow");
		dryadFirstNames.add("Uknow");
		dryadFirstNames.add("Berow");
		dryadFirstNames.add("Zenow");
		dryadFirstNames.add("Marow");
		
		dryadLastNames.add("Flur");
		dryadLastNames.add("Flor");
		dryadLastNames.add("Alor");
		dryadLastNames.add("Chlor");
		dryadLastNames.add("Valor");
		dryadLastNames.add("Zawr");
		dryadLastNames.add("Quar");
		dryadLastNames.add("Kar");
		dryadLastNames.add("Lar");
		dryadLastNames.add("Tar");
	}
	
	private static void initHumanNames() {
		humanFirstNames.add("Alex");
		humanFirstNames.add("Riley");
		humanFirstNames.add("Jordan");
		humanFirstNames.add("Dylan");
		humanFirstNames.add("Chris");
		humanFirstNames.add("Jamie");
		humanFirstNames.add("Kirby");
		humanFirstNames.add("Rene");
		humanFirstNames.add("Jackie");
		humanFirstNames.add("Blair");
		humanFirstNames.add("Harley");
		humanFirstNames.add("Kit");
		humanFirstNames.add("Peyton");
		humanFirstNames.add("Sidney");
		humanFirstNames.add("Presley");
		humanFirstNames.add("Harley");
		humanFirstNames.add("Darcy");
		humanFirstNames.add("Bailey");		
	}
	
	private static void initLavaNames() {
		lavaFirstNames.add("Basar");
		lavaFirstNames.add("Corsite");
		lavaFirstNames.add("Talket");
		lavaFirstNames.add("Nitar");
		lavaFirstNames.add("Itor");
		lavaFirstNames.add("Basar");
		lavaFirstNames.add("Pluton");
		lavaFirstNames.add("Ditor");
		lavaFirstNames.add("Halite");
		lavaFirstNames.add("Jaspar");
		
		lavaLastNames.add("Palladium");
		lavaLastNames.add("Stalite");
		lavaLastNames.add("Rhodium");
		lavaLastNames.add("Turquoise");
		lavaLastNames.add("Amicite");
		lavaLastNames.add("Bararite");
		lavaLastNames.add("Cerium");
		lavaLastNames.add("Datolite");
		lavaLastNames.add("Perite");
		lavaLastNames.add("Natrolite");
	}
	
	private static void initLizzardNames() {
		lizardFirstNames.add("Komodo");
		lizardFirstNames.add("Tokay");
		lizardFirstNames.add("Rango");
		lizardFirstNames.add("Spike");
		lizardFirstNames.add("Tokay");
		lizardFirstNames.add("Haku");
		lizardFirstNames.add("Igor");
		lizardFirstNames.add("Tokay");
		lizardFirstNames.add("Scylia");
		lizardFirstNames.add("Quil");
		
		lizardLastNames.add("Dragon");
		lizardFirstNames.add("Gecko");
		lizardFirstNames.add("Chameleon");
		lizardFirstNames.add("Iguana");
		lizardFirstNames.add("Basilisk");
		lizardFirstNames.add("Skink");
		lizardFirstNames.add("Armadillo");
		lizardFirstNames.add("Monitor");
		lizardFirstNames.add("Agama");
		lizardFirstNames.add("Tegu");		
	}
	
	private static void initBearNames() {
		bearFirstNames.add("Dasha");
		bearFirstNames.add("Sveta");
		bearFirstNames.add("Vera");
		bearFirstNames.add("Polya");
		bearFirstNames.add("Arisha");
		bearFirstNames.add("Galya");
		bearFirstNames.add("Lyuba");
		bearFirstNames.add("Kolya");
		bearFirstNames.add("Tolya");
		bearFirstNames.add("Gyena");
		
		bearLastNames.add("Grizzly");
		bearLastNames.add("Sloth");
		bearLastNames.add("Kodiak");
		bearLastNames.add("Malayan");
		bearLastNames.add("Marsican");
		bearLastNames.add("Polar");
		bearLastNames.add("Panda");
		bearLastNames.add("Black");
		bearLastNames.add("Brown");
		bearLastNames.add("Blue");
	}
	
	private static void initRoosterNames() {
		roosterFirstNames.add("Fleury");
		roosterFirstNames.add("Candide");
		roosterFirstNames.add("Elie");
		roosterFirstNames.add("Gurvan");
		roosterFirstNames.add("Jules");
		roosterFirstNames.add("Lyonel");
		roosterFirstNames.add("Marley");
		roosterFirstNames.add("Thais");
		roosterFirstNames.add("Nikola");
		roosterFirstNames.add("Hilaire");
		
		roosterLastNames.add("Ardeale");
		roosterLastNames.add("Bourbourg");
		roosterLastNames.add("Contres");
		roosterLastNames.add("Favoris");
		roosterLastNames.add("Gasconne");
		roosterLastNames.add("Houdan");
		roosterLastNames.add("Mantes");
		roosterLastNames.add("Landaise");
		roosterLastNames.add("Pavilly");
		roosterLastNames.add("Nicobari");
	}
	
	private static void initStarfishNames() {
		starfishFirstNames.add("Avila");
		starfishFirstNames.add("Castel");
		starfishFirstNames.add("Elia");
		starfishFirstNames.add("Fran");
		starfishFirstNames.add("Ginez");
		starfishFirstNames.add("Jules");
		starfishFirstNames.add("Madrid");
		starfishFirstNames.add("Sage");
		starfishFirstNames.add("Valentine");
		starfishFirstNames.add("Naolin");
		
		starfishLastNames.add("Annulatus");
		starfishLastNames.add("Gorgonalus");
		starfishLastNames.add("Miniate");
		starfishLastNames.add("Duebeni");
		starfishLastNames.add("Levigata");
		starfishLastNames.add("Brisingida");
		starfishLastNames.add("Ophiuroida");
		starfishLastNames.add("Capensis");
		starfishLastNames.add("Catalai");
		starfishLastNames.add("Patiriella");
	}
	
	private static void initVultureNames() {
		vultureFirstNames.add("Amondi");
		vultureFirstNames.add("Badu");
		vultureFirstNames.add("Delu");
		vultureFirstNames.add("Kemi");
		vultureFirstNames.add("Eshe");
		vultureFirstNames.add("Lewa");
		vultureFirstNames.add("Meaza");
		vultureFirstNames.add("Nacala");
		vultureFirstNames.add("Orma");
		vultureFirstNames.add("Sondo");
		
		vultureLastNames.add("Monachus");
		vultureLastNames.add("Fulvus");
		vultureLastNames.add("Bengalis");
		vultureLastNames.add("Rupelli");
		vultureLastNames.add("Indicus");
		vultureLastNames.add("Calvus");
		vultureLastNames.add("Teracheli");
		vultureLastNames.add("Barbatus");
		vultureLastNames.add("Angolensis");
		vultureLastNames.add("Tenuiro");
	}
	
	//--------
	//---------------- Ship Names -------
	//-------- 
	
	private static void initShipPrefix() {
		shipPrefix.add("SS ");
		shipPrefix.add("The ");
		shipPrefix.add("USS ");
		shipPrefix.add("UNS ");
		shipPrefix.add("All ");
		shipPrefix.add("Pure ");
		shipPrefix.add("Big ");
		shipPrefix.add("The ");
	}
	private static void initShipNames() {
		shipNames.add("Adventure");
		shipNames.add("Abstract");
		shipNames.add("Affinity");
		shipNames.add("Adaptive");
		shipNames.add("Accurate");
		shipNames.add("Acceptance");
		shipNames.add("Affluent");
		shipNames.add("Aperture");
		shipNames.add("Analytic");
		shipNames.add("Amenable");
		shipNames.add("Blessing");
		shipNames.add("Buttress");
		shipNames.add("Becoming");
		shipNames.add("Boundary");
		shipNames.add("Bluebird");
		shipNames.add("Botanist");
		shipNames.add("Brighten");
		shipNames.add("Blossom");
		shipNames.add("Boyant");
		shipNames.add("Cohkler");
		shipNames.add("Creation");
		shipNames.add("Currency");
		shipNames.add("Civilian");
		shipNames.add("Colonial");
		shipNames.add("Catalys");
		shipNames.add("Campaign");
		shipNames.add("Concept");
		shipNames.add("Charity");
		shipNames.add("Chapman");
		shipNames.add("Crystal");
		shipNames.add("Challenge");
		shipNames.add("Curiosity");
		shipNames.add("Deadline");
		shipNames.add("Devotion");
		shipNames.add("Destiny");
		shipNames.add("Dilligent");
		shipNames.add("Disciplined");
		shipNames.add("Distinctive");
		shipNames.add("Destructive");
		shipNames.add("Descriptive");
		shipNames.add("Determined");
		shipNames.add("Envelope");
		shipNames.add("Emerging");
		shipNames.add("Endeavor");
		shipNames.add("Eloquent");
		shipNames.add("Emergent");
		shipNames.add("Esthetic");
		shipNames.add("Eventful");
		shipNames.add("Elective");
		shipNames.add("Efficient");
		shipNames.add("Encounter");
		shipNames.add("Fundemental");
		shipNames.add("Foundation");
		shipNames.add("Functional");
		shipNames.add("Friendship");
		shipNames.add("Forbidding");
		shipNames.add("Favorable");
		shipNames.add("Fortitude");
		shipNames.add("Forgiving");
		shipNames.add("Footprint");
		shipNames.add("Forgetful");
		shipNames.add("Greatful");
		shipNames.add("Goodwill");
		shipNames.add("Grandeur");
		shipNames.add("Gauntlet");
		shipNames.add("Guardian");
		shipNames.add("Gemstone");
		shipNames.add("Gargoyle");
		shipNames.add("Granite");
		shipNames.add("Getaway");
		shipNames.add("Genuine");
		shipNames.add("Horizon");
		shipNames.add("Holding");
		shipNames.add("Helpful");
		shipNames.add("Honesty");
		shipNames.add("Heroic");
		shipNames.add("Hospitality");
		shipNames.add("Highspeed");
		shipNames.add("Highland");
		shipNames.add("Honorable");
		shipNames.add("Iluminate");
		shipNames.add("Idealistic");
		shipNames.add("Invincible");
		shipNames.add("Identical");
		shipNames.add("Injector");
		shipNames.add("Influencer");
		shipNames.add("Instant");
		shipNames.add("Inventive");
		shipNames.add("Intellect");
		shipNames.add("Imagination");
		shipNames.add("Ingenious");
		shipNames.add("Justice");
		shipNames.add("Justification");
		shipNames.add("Journeyman");
		shipNames.add("Judgement");
		shipNames.add("Judiciary");
		shipNames.add("Joker");
		shipNames.add("Jolly");
		shipNames.add("Knowledge");
		shipNames.add("Kickstart");
		shipNames.add("Kindness");
		shipNames.add("Kingdom");
		shipNames.add("Kestrel");
		shipNames.add("Kerogen");
		shipNames.add("Kinesis");
		shipNames.add("Kinglet");
		shipNames.add("Leadership");
		shipNames.add("Liberty");
		shipNames.add("Limitless");
		shipNames.add("Legendary");
		shipNames.add("Leisurely");
		shipNames.add("Luminous");
		shipNames.add("Loophole");
		shipNames.add("Locomotive");
		shipNames.add("Loyalist");
		shipNames.add("Lifeline");
		shipNames.add("Loyalty");
		shipNames.add("Lineman");
		shipNames.add("Logical");
		shipNames.add("Lottery");
		shipNames.add("Leisurely");
		shipNames.add("Motivation");
		shipNames.add("Monolithic");
		shipNames.add("Missionary");
		shipNames.add("Mercenary");
		shipNames.add("Manifest");
		shipNames.add("Monument");
		shipNames.add("Merchant");
		shipNames.add("Mobility");
		shipNames.add("Majestic");
		shipNames.add("Magician");
		shipNames.add("Neutrality");
		shipNames.add("Navigator");
		shipNames.add("Nobleman");
		shipNames.add("Novelty");
		shipNames.add("Neutral");
		shipNames.add("Narrow");
		shipNames.add("Obscurity");
		shipNames.add("Opportunity");
		shipNames.add("Original");
		shipNames.add("Optimism");
		shipNames.add("Ordinary");
		shipNames.add("Ordinate");
		shipNames.add("Optical");
		shipNames.add("Obscure");
		shipNames.add("Outpost");
		shipNames.add("Obvious");
		shipNames.add("Outlast");
		shipNames.add("Outcast");
		shipNames.add("Overlie");
		shipNames.add("Perseverance");
		shipNames.add("Profitable");
		shipNames.add("Prosperity");
		shipNames.add("Philosophy");
		shipNames.add("Principal");
		shipNames.add("Privilege");
		shipNames.add("Plausible");
		shipNames.add("Priceless");
		shipNames.add("Plentiful");
		shipNames.add("Pursuant");
		shipNames.add("Qualified");
		shipNames.add("Question");
		shipNames.add("Quality");
		shipNames.add("Quality");
		shipNames.add("Quantum");
		shipNames.add("Quartz");
		shipNames.add("Relativity");
		shipNames.add("Respective");
		shipNames.add("Reputable");
		shipNames.add("Resilient");
		shipNames.add("Reverence");
		shipNames.add("Recurrent");
		shipNames.add("Requisite");
		shipNames.add("Replicate");
		shipNames.add("Reghteous");
		shipNames.add("Repurpose");
		shipNames.add("Resurrect");
		shipNames.add("Recovery");
		shipNames.add("Restless");
		shipNames.add("Residual");
		shipNames.add("Rational");
		shipNames.add("Simplicity");
		shipNames.add("Sufficient");
		shipNames.add("Successful");
		shipNames.add("Signature");
		shipNames.add("Suitable");
		shipNames.add("Seamless");
		shipNames.add("Symphony");
		shipNames.add("Symbolic");
		shipNames.add("Scrutiny");
		shipNames.add("Treasury");
		shipNames.add("Truthful");
		shipNames.add("Tenacity");
		shipNames.add("Tranquil");
		shipNames.add("Tireless");
		shipNames.add("Timeless");
		shipNames.add("Truthful");
		shipNames.add("Trivial");
		shipNames.add("Timely");
		shipNames.add("Trophy");
		shipNames.add("Unaffected");
		shipNames.add("Universal");
		shipNames.add("Utility");
		shipNames.add("Undying");
		shipNames.add("Utopian");
		shipNames.add("Utilize");
		shipNames.add("Uniform");
		shipNames.add("Uplift");
		shipNames.add("Upheld");
		shipNames.add("Unique");
		shipNames.add("Unseen");
		shipNames.add("Visualizer");
		shipNames.add("Visibility");
		shipNames.add("Victory");
		shipNames.add("Vitality");
		shipNames.add("Vigilance");
		shipNames.add("Visionary");
		shipNames.add("Valuable");
		shipNames.add("Velocity");
		shipNames.add("Vitality");
		shipNames.add("Worship");
		shipNames.add("Worthy");
		shipNames.add("Welbeing");
		shipNames.add("Willpower");
		shipNames.add("Wholesome");
		shipNames.add("Wildcard");
		shipNames.add("Watchful");
		shipNames.add("Welfare");
		shipNames.add("Willing");
		shipNames.add("Wishful");
		shipNames.add("Xenolith");
		shipNames.add("Xerox");
		shipNames.add("Xerosis");
		shipNames.add("Xenograft");
		shipNames.add("Xenogenic");
		shipNames.add("Xenon");
		shipNames.add("Yearned");
		shipNames.add("Yielding");
		shipNames.add("Youthful");
		shipNames.add("Yearning");
		shipNames.add("Zeppelin");
		shipNames.add("zillion");
		shipNames.add("zipper");
		shipNames.add("zealot");
		
		//joke names
		shipNames.add("Boondoggle");
		shipNames.add("Catastrophe");
		shipNames.add("Tragedy");
		shipNames.add("Wobbly");
		shipNames.add("Shabby");
		shipNames.add("DogeCoin");
		shipNames.add("YOLO");
		shipNames.add("Heartless");
		shipNames.add("GetToTheChoppa");
		shipNames.add("Titanic");
		shipNames.add("Failure");
		shipNames.add("BadIdea");
		shipNames.add("Joke");
		shipNames.add("I'mPickleShip!");
		shipNames.add("We'reGoingDown!");
		shipNames.add("BadTimes");
		shipNames.add("InstantRegret");
		
		//star trek names
		shipNames.add("Archer");
		shipNames.add("Voyager");
		shipNames.add("Enterprise");
		shipNames.add("Narada");
		shipNames.add("Vengeance");
		shipNames.add("Scimitar");
		shipNames.add("Discovery");
		shipNames.add("Defiant");
		shipNames.add("Prometheus");
		shipNames.add("Raven");
	}
	
	//--------
	//---------------- Human Names -------
	//-------- 
	
	private static void initMaleNames() {
		maleNames.add("Ash");
		maleNames.add("Aron");
		maleNames.add("Ace");
		maleNames.add("Adam");
		maleNames.add("Adrian");
		maleNames.add("Allan");
		maleNames.add("Alex");
		maleNames.add("Alfie");
		maleNames.add("Angus");
		maleNames.add("Axel");
		maleNames.add("Basil");
		maleNames.add("Bilbo");
		maleNames.add("Brice");
		maleNames.add("Brian");
		maleNames.add("Bruno");
		maleNames.add("Buster");
		maleNames.add("Barry");
		maleNames.add("Blair");
		maleNames.add("Brant");
		maleNames.add("Boyd");
		maleNames.add("Cleb");
		maleNames.add("Carl");
		maleNames.add("Cash");
		maleNames.add("Clark");
		maleNames.add("Cody");
		maleNames.add("Curtis");
		maleNames.add("Chad");
		maleNames.add("Chet");
		maleNames.add("Clif");
		maleNames.add("Chase");
		maleNames.add("Dale");
		maleNames.add("Dan");
		maleNames.add("Donald");
		maleNames.add("Dwight");
		maleNames.add("Dwayne");
		maleNames.add("Dilbert");
		maleNames.add("Dill");
		maleNames.add("David");
		maleNames.add("Dexter");
		maleNames.add("Darius");
		maleNames.add("Earl");
		maleNames.add("Edgar");
		maleNames.add("Edward");
		maleNames.add("Elmer");
		maleNames.add("Evan");
		maleNames.add("Everett");
		maleNames.add("Eric");
		maleNames.add("Ernest");
		maleNames.add("Elliott");
		maleNames.add("Ethan");
		maleNames.add("Fabio");
		maleNames.add("Felix");
		maleNames.add("Finn");
		maleNames.add("Flynn");
		maleNames.add("Forbes");
		maleNames.add("Frank");
		maleNames.add("Fraser");
		maleNames.add("Freeman");
		maleNames.add("Forrest");
		maleNames.add("Francis");
		maleNames.add("Gabriel");
		maleNames.add("Gage");
		maleNames.add("Galen");
		maleNames.add("Gavin");
		maleNames.add("George");
		maleNames.add("Gerald");
		maleNames.add("Glen");
		maleNames.add("Grant");
		maleNames.add("Gray");
		maleNames.add("Greg");
		maleNames.add("Hall");
		maleNames.add("Hamish");
		maleNames.add("Henry");
		maleNames.add("Homer");
		maleNames.add("Hugo");
		maleNames.add("Hunter");
		maleNames.add("Howie");
		maleNames.add("Harold");
		maleNames.add("Hamish");
		maleNames.add("Herbert");
		maleNames.add("Ian");
		maleNames.add("Ichabod");
		maleNames.add("Ignacio");
		maleNames.add("Igor");
		maleNames.add("Indigo");
		maleNames.add("Irwin");
		maleNames.add("Isaac");
		maleNames.add("Irwin");
		maleNames.add("Ingvar");
		maleNames.add("Isaiah");
		maleNames.add("Jack");
		maleNames.add("Jacob");
		maleNames.add("Jarod");
		maleNames.add("Jenson");
		maleNames.add("Jesper");
		maleNames.add("Jett");
		maleNames.add("John");
		maleNames.add("Jonas");
		maleNames.add("Judd");
		maleNames.add("Justin");
		maleNames.add("Kato");
		maleNames.add("Keanu");
		maleNames.add("Keith");
		maleNames.add("Kermit");
		maleNames.add("Kevin");
		maleNames.add("Klaus");
		maleNames.add("Knox");
		maleNames.add("Kyle");
		maleNames.add("Kwame");
		maleNames.add("Ken");
		maleNames.add("Lachy");
		maleNames.add("Lane");
		maleNames.add("Lars");
		maleNames.add("Leo");
		maleNames.add("Liam");
		maleNames.add("Loagn");
		maleNames.add("Louis");
		maleNames.add("Luke");
		maleNames.add("Leroy");
		maleNames.add("Leif");
		maleNames.add("Mark");
		maleNames.add("Mike");
		maleNames.add("Matt");
		maleNames.add("Miles");
		maleNames.add("Mario");
		maleNames.add("Martin");
		maleNames.add("Malcolm");
		maleNames.add("Magnus");
		maleNames.add("Marshall");
		maleNames.add("Ming");
		maleNames.add("Nathan");
		maleNames.add("Nemo");
		maleNames.add("Nestor");
		maleNames.add("Neville");
		maleNames.add("Naom");
		maleNames.add("Norris");
		maleNames.add("Norman");
		maleNames.add("Noor");
		maleNames.add("Nolam");
		maleNames.add("Neelam");
		maleNames.add("Ocavio");
		maleNames.add("Olam");
		maleNames.add("Oliver");
		maleNames.add("Omar");
		maleNames.add("Oreste");
		maleNames.add("Own");
		maleNames.add("Oscar");
		maleNames.add("Ozzy");
		maleNames.add("Olaf");
		maleNames.add("Orange");
		maleNames.add("Pablo");
		maleNames.add("Palmer");
		maleNames.add("Paul");
		maleNames.add("Pavel");
		maleNames.add("Paris");
		maleNames.add("Porter");
		maleNames.add("Peter");
		maleNames.add("Philip");
		maleNames.add("Pierce");
		maleNames.add("Plato");
		maleNames.add("Quincy");
		maleNames.add("Quinn");
		maleNames.add("Quinton");
		maleNames.add("Quinlan");
		maleNames.add("Queen");
		maleNames.add("Qing");
		maleNames.add("Quincy");
		maleNames.add("Quang");
		maleNames.add("Qadir");
		maleNames.add("Qasim");
		maleNames.add("Ragnar");
		maleNames.add("Ralph");
		maleNames.add("Remy");
		maleNames.add("Rhes");
		maleNames.add("Ross");
		maleNames.add("Ryan");
		maleNames.add("Ryder");
		maleNames.add("Rhett");
		maleNames.add("Regis");
		maleNames.add("Raoul");
		maleNames.add("Salman");
		maleNames.add("Samir");
		maleNames.add("Sawyer");
		maleNames.add("Sherwin");
		maleNames.add("Silas");
		maleNames.add("Simon");
		maleNames.add("Steven");
		maleNames.add("Stanley");
		maleNames.add("Stone");
		maleNames.add("Sall");
		maleNames.add("Thor");
		maleNames.add("Trey");
		maleNames.add("Tyrone");
		maleNames.add("Tristan");
		maleNames.add("Thomas");
		maleNames.add("Tom");
		maleNames.add("Tyler");
		maleNames.add("Tim");
		maleNames.add("Trevor");
		maleNames.add("Tone");
		maleNames.add("Ulric");
		maleNames.add("Ulysses");
		maleNames.add("Urban");
		maleNames.add("Umberto");
		maleNames.add("Ultan");
		maleNames.add("Umar");
		maleNames.add("Upton");
		maleNames.add("Uriah");
		maleNames.add("Ulrich");
		maleNames.add("Urving");
		maleNames.add("Vance");
		maleNames.add("Vasek");
		maleNames.add("Vern");
		maleNames.add("Viggo");
		maleNames.add("Victor");
		maleNames.add("Virgil");
		maleNames.add("Vitus");
		maleNames.add("Vishnu");
		maleNames.add("Vaughn");
		maleNames.add("Vasiliy");
		maleNames.add("Waldo");
		maleNames.add("Walter");
		maleNames.add("Warick");
		maleNames.add("Webster");
		maleNames.add("Wiley");
		maleNames.add("William");
		maleNames.add("Willis");
		maleNames.add("Winslow");
		maleNames.add("Wyman");
		maleNames.add("Willard");
		maleNames.add("Xander");
		maleNames.add("Xavier");
		maleNames.add("Xenon");
		maleNames.add("Xerxes");
		maleNames.add("Ximun");
		maleNames.add("Xandros");
		maleNames.add("Xoan");
		maleNames.add("Xeno");
		maleNames.add("Yale");
		maleNames.add("Yuri");
		maleNames.add("Yasir");
		maleNames.add("Yosef");
		maleNames.add("Yorick");
		maleNames.add("Yves");
		maleNames.add("Zach");
		maleNames.add("Zane");
		maleNames.add("Zion");
		maleNames.add("Zebby");
		maleNames.add("Zebulon");
		maleNames.add("Zachary");
		maleNames.add("Ziah");		
	}
	
	private static void initFemaleNames() {
		femaleNames.add("Abby");
		femaleNames.add("Ada");
		femaleNames.add("Adaline");
		femaleNames.add("Adele");
		femaleNames.add("Alana");
		femaleNames.add("Alice");
		femaleNames.add("Alexis");
		femaleNames.add("Allison");
		femaleNames.add("Alora");
		femaleNames.add("Amelia");
		femaleNames.add("Bridget");
		femaleNames.add("Bria");
		femaleNames.add("Briella");
		femaleNames.add("Brooke");
		femaleNames.add("Brylee");
		femaleNames.add("Bristol");
		femaleNames.add("Brit");
		femaleNames.add("Brittany");
		femaleNames.add("Bonnie");
		femaleNames.add("Blair");
		femaleNames.add("Cara");
		femaleNames.add("Carmen");
		femaleNames.add("Casey");
		femaleNames.add("Catherine");
		femaleNames.add("Catalina");
		femaleNames.add("Camila");
		femaleNames.add("Callie");
		femaleNames.add("Cameron");
		femaleNames.add("Chanel");
		femaleNames.add("Chloe");
		femaleNames.add("Danna");
		femaleNames.add("Deborah");
		femaleNames.add("Delilah");
		femaleNames.add("Demi");
		femaleNames.add("Diana");
		femaleNames.add("Dorothy");
		femaleNames.add("Dylan");
		femaleNames.add("Dream");
		femaleNames.add("Dixy");
		femaleNames.add("Doris");
		femaleNames.add("Edith");
		femaleNames.add("Elaine");
		femaleNames.add("Eliza");
		femaleNames.add("Ellen");
		femaleNames.add("Eloise");
		femaleNames.add("Elora");
		femaleNames.add("Emelia");
		femaleNames.add("Emilly");
		femaleNames.add("Emma");
		femaleNames.add("Eva");
		femaleNames.add("Faith");
		femaleNames.add("Faye");
		femaleNames.add("Felicity");
		femaleNames.add("Fiona");
		femaleNames.add("Florence");
		femaleNames.add("Frances");
		femaleNames.add("Floral");
		femaleNames.add("Frida");
		femaleNames.add("Foxley");
		femaleNames.add("Frankie");
		femaleNames.add("Gabriela");
		femaleNames.add("Gina");
		femaleNames.add("Giselle");
		femaleNames.add("Gracie");
		femaleNames.add("Greta");
		femaleNames.add("Gwen");
		femaleNames.add("Grace");
		femaleNames.add("Gloria");
		femaleNames.add("Glory");
		femaleNames.add("Goldie");	
		femaleNames.add("Hadlee");
		femaleNames.add("Hailee");
		femaleNames.add("Harley");
		femaleNames.add("Halle");
		femaleNames.add("Hannah");
		femaleNames.add("Harlow");
		femaleNames.add("Harper");
		femaleNames.add("Haylee");
		femaleNames.add("Harmoni");
		femaleNames.add("Hazel");
		femaleNames.add("India");
		femaleNames.add("Irene");
		femaleNames.add("Iris");
		femaleNames.add("Isla");
		femaleNames.add("Ivanna");
		femaleNames.add("Isabel");
		femaleNames.add("Isabela");
		femaleNames.add("Isabelle");
		femaleNames.add("Itzel");
		femaleNames.add("Ingrid");
		femaleNames.add("Jade");
		femaleNames.add("Jada");
		femaleNames.add("Jamie");
		femaleNames.add("Jane");
		femaleNames.add("Janelle");
		femaleNames.add("Jaycee");
		femaleNames.add("Jayda");
		femaleNames.add("Jaylee");
		femaleNames.add("Joyce");
		femaleNames.add("Joanne");
		femaleNames.add("Kai");
		femaleNames.add("Kailey");
		femaleNames.add("Kaiya");
		femaleNames.add("Kali");
		femaleNames.add("Kate");
		femaleNames.add("Karen");
		femaleNames.add("Kara");
		femaleNames.add("Karla");
		femaleNames.add("Kassidy");
		femaleNames.add("kaylee");
		femaleNames.add("Lacey");
		femaleNames.add("Leah");
		femaleNames.add("Lana");
		femaleNames.add("Lora");
		femaleNames.add("Leia");
		femaleNames.add("Leyla");
		femaleNames.add("Lola");
		femaleNames.add("Lila");
		femaleNames.add("Laura");
		femaleNames.add("Lauren");
		femaleNames.add("Mabel");
		femaleNames.add("Macy");
		femaleNames.add("Maddison");
		femaleNames.add("Maisie");
		femaleNames.add("Malia");
		femaleNames.add("Margo");
		femaleNames.add("Marie");
		femaleNames.add("Mary");
		femaleNames.add("Mavis");
		femaleNames.add("Maxine");
		femaleNames.add("Nala");
		femaleNames.add("Nancy");
		femaleNames.add("Natalie");
		femaleNames.add("Nola");
		femaleNames.add("Nova");
		femaleNames.add("Nicole");
		femaleNames.add("Nina");
		femaleNames.add("Norah");
		femaleNames.add("Novah");
		femaleNames.add("Naya");
		femaleNames.add("Oaklee");
		femaleNames.add("Oakley");
		femaleNames.add("Oaklynn");
		femaleNames.add("Octavia");
		femaleNames.add("Olive");
		femaleNames.add("Olivia");
		femaleNames.add("Opal");
		femaleNames.add("Ophelia");
		femaleNames.add("Oregon");
		femaleNames.add("Opal");
		femaleNames.add("Paige");
		femaleNames.add("Palmer");
		femaleNames.add("Penny");
		femaleNames.add("Piper");
		femaleNames.add("Pearl");
		femaleNames.add("Poppy");
		femaleNames.add("Paris");
		femaleNames.add("Payton");
		femaleNames.add("Promise");
		femaleNames.add("Paula");
		femaleNames.add("Queen");
		femaleNames.add("Quinn");
		femaleNames.add("Quincey");
		femaleNames.add("Rachel");
		femaleNames.add("Rikky");
		femaleNames.add("Rain");
		femaleNames.add("Rayna");
		femaleNames.add("Raven");
		femaleNames.add("Ragen");
		femaleNames.add("Robin");
		femaleNames.add("Sabrina");
		femaleNames.add("Sadie");
		femaleNames.add("Saige");
		femaleNames.add("Salma");
		femaleNames.add("Sammy");
		femaleNames.add("Sara");
		femaleNames.add("Sarah");
		femaleNames.add("Sasha");
		femaleNames.add("Stella");
		femaleNames.add("Selena");
		femaleNames.add("Sky");
		femaleNames.add("Talia");
		femaleNames.add("Tatum");
		femaleNames.add("Taylor");
		femaleNames.add("Teagan");
		femaleNames.add("Tenley");
		femaleNames.add("Tessa");
		femaleNames.add("Tiana");
		femaleNames.add("Trinity");
		femaleNames.add("Tiffany");
		femaleNames.add("Undine");
		femaleNames.add("Ursula");
		femaleNames.add("Uma");
		femaleNames.add("Valery");
		femaleNames.add("Veronica");
		femaleNames.add("Vivian");
		femaleNames.add("Violet");
		femaleNames.add("Vera");
		femaleNames.add("Vienna");
		femaleNames.add("Victoria");
		femaleNames.add("Wendy");
		femaleNames.add("Waverly");
		femaleNames.add("Willa");
		femaleNames.add("Willow");
		femaleNames.add("Winter");
		femaleNames.add("Wren");
		femaleNames.add("Ximena");
		femaleNames.add("Xina");
		femaleNames.add("Xola");
		femaleNames.add("Yara");
		femaleNames.add("Yevone");
		femaleNames.add("Zahra");
		femaleNames.add("Zelda");
		femaleNames.add("Zaina");
	}
	
	private static void initLastNames() {
		lastNames.add("Acre");
		lastNames.add("Addams");
		lastNames.add("Atkins");
		lastNames.add("Anderson");
		lastNames.add("Allen");
		lastNames.add("Adler");
		lastNames.add("Amber");
		lastNames.add("Baker");
		lastNames.add("Bell");
		lastNames.add("Brown");
		lastNames.add("Bennett");
		lastNames.add("Burns");
		lastNames.add("Campbell");
		lastNames.add("Carter");
		lastNames.add("Clark");
		lastNames.add("Collins");
		lastNames.add("Cook");
		lastNames.add("Cooper");
		lastNames.add("Davidson");
		lastNames.add("Dawson");
		lastNames.add("Evans");
		lastNames.add("Easmon");
		lastNames.add("Edmend");
		lastNames.add("Ellis");
		lastNames.add("Edwards");
		lastNames.add("Eady");
		lastNames.add("Fleming");
		lastNames.add("Forester");
		lastNames.add("Foster");
		lastNames.add("Fox");
		lastNames.add("Gardner");
		lastNames.add("Gibbs");
		lastNames.add("Gray");
		lastNames.add("Green");
		lastNames.add("Goyle");
		lastNames.add("Hall");
		lastNames.add("Hamilton");
		lastNames.add("Haris");
		lastNames.add("Henderson");
		lastNames.add("Homes");
		lastNames.add("Hughes");
		lastNames.add("Jackson");
		lastNames.add("Johnson");
		lastNames.add("Jones");
		lastNames.add("Jameson");
		lastNames.add("Jenkins");
		lastNames.add("Jailer");
		lastNames.add("Kelly");
		lastNames.add("King");
		lastNames.add("Kramer");
		lastNames.add("Krall");
		lastNames.add("Kong");
		lastNames.add("Kerry");
		lastNames.add("Knife");
		lastNames.add("Kettle");
		lastNames.add("Karington");
		lastNames.add("Kolly");
		lastNames.add("Lee");
		lastNames.add("Lewis");
		lastNames.add("Luster");
		lastNames.add("Lane");
		lastNames.add("Lynch");
		lastNames.add("Lawson");
		lastNames.add("Levy");
		lastNames.add("Lender");
		lastNames.add("Lonsly");
		lastNames.add("Lock");
		lastNames.add("Miller");
		lastNames.add("Marshal");
		lastNames.add("McDonald");
		lastNames.add("Moore");
		lastNames.add("Murray");
		lastNames.add("Murphy");
		lastNames.add("Mathias");
		lastNames.add("Nicholson");
		lastNames.add("Newman");
		lastNames.add("Neal");
		lastNames.add("Norton");
		lastNames.add("Nash");
		lastNames.add("Noble");
		lastNames.add("Norman");
		lastNames.add("Osborne");
		lastNames.add("Olson");
		lastNames.add("O'Brien");
		lastNames.add("Owens");
		lastNames.add("Oritz");
		lastNames.add("Parker");
		lastNames.add("Peterson");
		lastNames.add("Phillips");
		lastNames.add("Porter");
		lastNames.add("Potter");
		lastNames.add("Powell");
		lastNames.add("Reed");
		lastNames.add("Reid");
		lastNames.add("Richards");
		lastNames.add("Richardson");
		lastNames.add("Robers");
		lastNames.add("Russel");
		lastNames.add("Sanders");
		lastNames.add("Scott");
		lastNames.add("Shaw");
		lastNames.add("Simpson");
		lastNames.add("Smith");
		lastNames.add("Summers");
		lastNames.add("Snyder");
		lastNames.add("Turner");
		lastNames.add("Teller");
		lastNames.add("Tiler");
		lastNames.add("Taylor");
		lastNames.add("Teal");
		lastNames.add("Ubel");
		lastNames.add("Uber");
		lastNames.add("Ubben");
		lastNames.add("Ubl");
		lastNames.add("Ubel");
		lastNames.add("Uhle");
		lastNames.add("Underle");
		lastNames.add("Uster");
		lastNames.add("Ubel");
		lastNames.add("Vacca");
		lastNames.add("Vacek");
		lastNames.add("Vaden");
		lastNames.add("Vacura");
		lastNames.add("Vagel");
		lastNames.add("Vahl");
		lastNames.add("Vaile");
		lastNames.add("White");
		lastNames.add("Wilson");
		lastNames.add("Wood");
		lastNames.add("Wright");
		lastNames.add("Whaler");
		lastNames.add("Young");
		lastNames.add("York");
		lastNames.add("Yates");
		lastNames.add("Yoink");
		lastNames.add("Zafar");
		lastNames.add("Zaldi");
		lastNames.add("Zakar");
		lastNames.add("Zalt");
		lastNames.add("Zale");
		lastNames.add("Zerg");
	}
	
	//--------
	//---------------- Latin -------
	//--------
	
	private static void initRoot() {
		initRootA();
		initRootB();
		initRootC();
		initRootD();
		initRootE();
		initRootF();
		initRootG();
		initRootH();
		initRootI();
		initRootJ();
		initRootK();
		initRootL();
		initRootM();
		initRootN();
		initRootO();
		initRootP();
		initRootQ();
		initRootR();
		initRootS();
		initRootT();
		initRootU();
		initRootV();
		initRootW();
		initRootX();
		initRootY();
		initRootZ();
	}	
	
	private static void initRootA() {
		rootA.add("acro");
		rootA.add("act");
		rootA.add("agr");
		rootA.add("aer");
		rootA.add("alg");
		rootA.add("ambi");
		rootA.add("ami");
		rootA.add("ana");
		rootA.add("andr");
		rootA.add("anim");
		rootA.add("ann");
		rootA.add("ante");
		rootA.add("anth");
		rootA.add("anti");
		rootA.add("apo");
		rootA.add("aqu");
		rootA.add("arbo");
		rootA.add("arch");
		rootA.add("arth");
		rootA.add("art");
		rootA.add("astr");
		rootA.add("auto");
		rootA.add("avi");
	}
	
	private static void initRootB() {
		rootB.add("bar");
		rootB.add("bell");
		rootB.add("bene");
		rootB.add("bi");
		rootB.add("bio");
		rootB.add("blas");
		rootB.add("burs");
		rootB.add("bibl");	
		rootB.add("blo");
		rootB.add("ber");
		rootB.add("bos");
		rootB.add("bas");
		rootB.add("bal");
		rootB.add("bok");
		rootB.add("bon");	
	}
	
	private static void initRootC() {
		rootC.add("calc");
		rootC.add("cand");
		rootC.add("cept");
		rootC.add("card");
		rootC.add("carn");
		rootC.add("cata");
		rootC.add("caut");
		rootC.add("cess");
		rootC.add("cele");
		rootC.add("ceph");
		rootC.add("cere");
		rootC.add("cert");
		rootC.add("chrom");
		rootC.add("chros");
		rootC.add("chrys");
		rootC.add("cide");
		rootC.add("carn");
		rootC.add("clam");
		rootC.add("clar");
		rootC.add("clud");
		rootC.add("clus");
		rootC.add("co");
		rootC.add("com");
		rootC.add("con");
		rootC.add("corp");
		rootC.add("cosm");
		rootC.add("cou");
		rootC.add("cran");
		rootC.add("cred");
		rootC.add("cruc");
		rootC.add("cryp");
		rootC.add("cumu");
		rootC.add("curr");
		rootC.add("cycl");
	}
	
	private static void initRootD() {
		rootD.add("de");
		rootD.add("deci");
		rootD.add("dem");
		rootD.add("demi");
		rootD.add("dend");
		rootD.add("dent");
		rootD.add("don");
		rootD.add("derm");
		rootD.add("di");
		rootD.add("dia");
		rootD.add("dict");
		rootD.add("domi");
		rootD.add("duc");
		rootD.add("du");
		rootD.add("dur");
		rootD.add("dyn");
		rootD.add("dys");
	}
	
	private static void initRootE() {
		rootE.add("eg");
		rootE.add("ego");
		rootE.add("endo");
		rootE.add("enn");
		rootE.add("equ");
		rootE.add("erg");
		rootE.add("esth");
		rootE.add("ethn");
		rootE.add("eu");
		rootE.add("ex");
		rootE.add("extr");
		rootE.add("ef");
		rootE.add("erl");
		rootE.add("ent");
		rootE.add("est");
		rootE.add("ers");
	}
	
	private static void initRootF() {
		rootF.add("fac");
		rootF.add("fer");
		rootF.add("fid");
		rootF.add("flec");
		rootF.add("flor");
		rootF.add("fleu");
		rootF.add("fore");
		rootF.add("form");
		rootF.add("frac");
		rootF.add("fug");
		rootF.add("fus");
	}
	
	private static void initRootG() {
		rootG.add("gas");
		rootG.add("gen");
		rootG.add("geo");
		rootG.add("ger");
		rootG.add("giga");
		rootG.add("gon");
		rootG.add("gram");
		rootG.add("grap");
		rootG.add("grat");
		rootG.add("gyn");
		rootG.add("gres");
	}
	
	private static void initRootH() {
		rootH.add("hect");
		rootH.add("heli");
		rootH.add("hel");
		rootH.add("hemi");
		rootH.add("hem");
		rootH.add("hepa");
		rootH.add("hept");
		rootH.add("herb");
		rootH.add("hete");
		rootH.add("hex");
		rootH.add("hist");
		rootH.add("homo");
		rootH.add("hydr");
		rootH.add("hype");
	}
	
	private static void initRootI() {
		rootI.add("iatr");
		rootI.add("icon");
		rootI.add("idio");
		rootI.add("il");
		rootI.add("in");
		rootI.add("im");
		rootI.add("ir");
		rootI.add("imag");
		rootI.add("infr");
		rootI.add("inte");
		rootI.add("intr");
		rootI.add("ir");
		rootI.add("iso");
	}
	
	private static void initRootJ() {
		rootJ.add("ject");
		rootJ.add("jud");
		rootJ.add("junc");
		rootJ.add("juv");	
		rootJ.add("jus");
		rootJ.add("jos");
		rootJ.add("jen");
		rootJ.add("jaf");
		rootJ.add("jep");
		rootJ.add("jil");
		rootJ.add("jas");
		rootJ.add("jen");	
	}
	
	private static void initRootK() {
		rootK.add("kil");
		rootK.add("kin");
		rootK.add("ken");
		rootK.add("kor");
		rootK.add("kel");
		rootK.add("kas");
		rootK.add("kon");
		rootK.add("kan");
		rootK.add("kam");
		rootK.add("kal");
	}
	
	private static void initRootL() {
		rootL.add("lab");
		rootL.add("lact");
		rootL.add("lat");
		rootL.add("lex");
		rootL.add("leuc");
		rootL.add("lip");
		rootL.add("log");
		rootL.add("luc");
		rootL.add("lud");
		rootL.add("lumi");
		rootL.add("libe");
		rootL.add("lun");		
	}
	
	private static void initRootM() {
		rootM.add("macr");
		rootM.add("magn");
		rootM.add("mal");
		rootM.add("man");
		rootM.add("mand");
		rootM.add("mani");
		rootM.add("mate");
		rootM.add("max");
		rootM.add("medi");
		rootM.add("mega");
		rootM.add("mela");
		rootM.add("memo");
		rootM.add("merg");
		rootM.add("mers");
		rootM.add("meso");
		rootM.add("meta");
		rootM.add("mete");
		rootM.add("micr");
		rootM.add("mid");
		rootM.add("migr");
		rootM.add("mill");
		rootM.add("min");
		rootM.add("mis");
		rootM.add("mob");
		rootM.add("mon");
		rootM.add("mot");
		rootM.add("morp");
		rootM.add("mort");
		rootM.add("mult");
		rootM.add("mut");
	}
	
	private static void initRootN() {
		rootN.add("narr");
		rootN.add("nat");
		rootN.add("nav");
		rootN.add("necr");
		rootN.add("neg");
		rootN.add("neo");
		rootN.add("neph");
		rootN.add("neur");
		rootN.add("nom");
		rootN.add("non");
		rootN.add("not");
		rootN.add("noun");
		rootN.add("nov");
		rootN.add("nume");
	}
	
	private static void initRootO() {
		rootO.add("ob");
		rootO.add("oct");
		rootO.add("ocu");
		rootO.add("od");
		rootO.add("odor");
		rootO.add("omni");
		rootO.add("op");
		rootO.add("opt");
		rootO.add("orth");
		rootO.add("oste");
		rootO.add("out");
		rootO.add("over");
		rootO.add("oxi");
	}
	
	private static void initRootP() {
		rootP.add("pale");
		rootP.add("pan");
		rootP.add("para");
		rootP.add("path");
		rootP.add("ped");
		rootP.add("pel");
		rootP.add("pent");
		rootP.add("pept");
		rootP.add("per");
		rootP.add("peri");
		rootP.add("phag");
		rootP.add("phil");
		rootP.add("phon");
		rootP.add("phot");
		rootP.add("phyl");
		rootP.add("phys");
		rootP.add("phyt");
		rootP.add("plas");
		rootP.add("plod");
		rootP.add("pneu");
		rootP.add("pod");
		rootP.add("poli");
		rootP.add("poly");
		rootP.add("pon");
		rootP.add("pop");
		rootP.add("port");
		rootP.add("pos");
		rootP.add("pre");
		rootP.add("pro");
		rootP.add("prot");
		rootP.add("psyc");
		rootP.add("pung");
		rootP.add("purg");
		rootP.add("put");
	}
	
	private static void initRootQ() {
		rootQ.add("quad");
		rootQ.add("quar");
		rootQ.add("quin");	
		rootQ.add("quer");	
		rootQ.add("quil");
		rootQ.add("quaz");
		rootQ.add("quel");	
	}
	
	private static void initRootR() {
		rootR.add("radi");
		rootR.add("ram");
		rootR.add("re");
		rootR.add("reg");
		rootR.add("retr");
		rootR.add("rhin");
		rootR.add("rhod");
		rootR.add("rid");
		rootR.add("rub");
		rootR.add("rupt");
	}
	
	private static void initRootS() {
		rootS.add("san");
		rootS.add("scen");
		rootS.add("sci");
		rootS.add("scle");
		rootS.add("scop");
		rootS.add("scri");
		rootS.add("se");
		rootS.add("sect");
		rootS.add("sed");
		rootS.add("sid");
		rootS.add("sess");
		rootS.add("self");
		rootS.add("semi");
		rootS.add("sept");
		rootS.add("serv");
		rootS.add("sex");
		rootS.add("sol");
		rootS.add("somn");
		rootS.add("soph");
		rootS.add("spec");
		rootS.add("spir");
		rootS.add("sta");
		rootS.add("stel");
		rootS.add("stru");
		rootS.add("sub");
		rootS.add("sum");
		rootS.add("sup");
	}
	
	private static void initRootT() {
		rootT.add("tact");
		rootT.add("tang");
		rootT.add("tax");
		rootT.add("tech");
		rootT.add("tel");
		rootT.add("temp");
		rootT.add("ten");
		rootT.add("tin");
		rootT.add("ter");
		rootT.add("term");
		rootT.add("terr");
		rootT.add("tetr");
		rootT.add("the");
		rootT.add("ther");
		rootT.add("tort");
		rootT.add("tox");
		rootT.add("trac");
		rootT.add("tri");
		rootT.add("tran");
	}
	
	private static void initRootU() {
		rootU.add("un");
		rootU.add("uni");
		rootU.add("urb");
		rootU.add("ur");
		rootU.add("ul");
	}
	
	private static void initRootV() {
		rootV.add("vac");
		rootV.add("ven");
		rootV.add("ver");
		rootV.add("verb");
		rootV.add("vers");
		rootV.add("vice");
		rootV.add("vid");
		rootV.add("vic");
		rootV.add("vid");
		rootV.add("vis");
		rootV.add("vol");
		rootV.add("voc");
		rootV.add("vour");
	}
	
	private static void initRootW() {
		rootW.add("wer");
		rootW.add("wel");
		rootW.add("was");
		rootW.add("wen");
		rootW.add("wan");
		rootW.add("war");
		rootW.add("wor");
		rootW.add("wal");
		rootW.add("wir");
		rootW.add("wed");
		rootW.add("wen");
		rootW.add("wil");
		rootW.add("wis");
		rootW.add("win");
	}
	private static void initRootX() {
		rootX.add("xen");
		rootX.add("xer");
		rootX.add("xor");
		rootX.add("xan");
	}
	
	private static void initRootY() {
		rootY.add("ye");
		rootX.add("yen");
		rootX.add("yam");
		rootX.add("yo");
		rootX.add("ya");
		rootX.add("yan");
		rootX.add("yon");
		rootX.add("yer");
	}
	
	private static void initRootZ() {
		rootZ.add("zo");
		rootZ.add("zon");
		rootZ.add("zol");
		rootZ.add("za");
		rootZ.add("zan");
		rootZ.add("zal");
		rootZ.add("ze");
		rootZ.add("zen");
		rootZ.add("zel");
		rootZ.add("zep");
		rootZ.add("zect");
		rootZ.add("zil");
		rootZ.add("zi");
	}
	
	//--------
	//---------------- Prefix / Suffix -------
	//--------
	
	private static void initSuffix() {
		suffix.add("able");
		suffix.add("al");
		suffix.add("er");
		suffix.add("est");
		suffix.add("ful");
		suffix.add("ible");
		suffix.add("ily");
		suffix.add("ing");
		suffix.add("less");
		suffix.add("ly");
		suffix.add("ness");
		suffix.add("y");		
	}
	
	private static void initPrefix() {
		prefix.add("de");
		prefix.add("dis");
		prefix.add("ex");
		prefix.add("il");
		prefix.add("im");
		prefix.add("in");
		prefix.add("mis");
		prefix.add("non");
		prefix.add("pre");
		prefix.add("pro");
		prefix.add("re");
		prefix.add("un");		
	}
	
}
