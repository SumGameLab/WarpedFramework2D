/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package test;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import warped.application.assemblys.InspectorInventory;
import warped.application.entities.item.ItemBindable;
import warped.application.state.ManagerItem;
import warped.application.state.WarpedApplication;
import warped.application.state.WarpedAudioFolder;
import warped.application.state.WarpedFramework2D;
import warped.application.state.WarpedGroup;
import warped.application.state.WarpedImageFolder;
import warped.application.state.WarpedInventory;
import warped.application.state.WarpedObject;
import warped.application.state.WarpedSpriteFolder;
import warped.application.state.WarpedState;
import warped.application.tile.TileableGenerative;
import warped.graphics.sprite.CharacterSprite;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.user.keyboard.WarpedKeyBind;
import warped.user.keyboard.WarpedKeyboard;
import warped.utilities.enums.WarpedLinkable;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.utils.Console;

public class ExampleApplication extends WarpedApplication {
	
	/*Here I set up some WarpedLinkables so we can refer to the assets i want to load later.	
	 * */
	public enum TestSprites implements WarpedLinkable<TestSprites>{
		SPRITE_8DIR,
		;public static Map<Integer, TestSprites> map = new HashMap<>();
		static {for(TestSprites sprite : TestSprites.values()) map.put(sprite.ordinal(), sprite);}
		@Override
		public Map<Integer, TestSprites> getMap() {return map;}
	}
	
	public enum TestAudio implements WarpedLinkable<TestAudio>{
		TEST_1,
		TEST_2,
		TEST_3,
		;public static Map<Integer, TestAudio> map = new HashMap<>();
		static {for(TestAudio audio : TestAudio.values()) map.put(audio.ordinal(), audio);}
		@Override
		public Map<Integer, TestAudio> getMap() {return map;}
	}
	
	public enum TestImage implements WarpedLinkable<TestImage>{
		FOOD,
		POTION,
		ROCK,
		TEST_1,
		TEST_2,
		TEST_3,
		;public static Map<Integer, TestImage> map = new HashMap<>();
		static {for(TestImage image : TestImage.values()) map.put(image.ordinal(), image);}
		@Override
		public Map<Integer, TestImage> getMap() {return map;}
	}
	
	public enum TestTiles implements TileableGenerative<TestTiles>{
		WATER,
		SAND,
		GRASS,
		DIRT,
		STONE,
		;public static Map<Integer, TestTiles> map = new HashMap<>();
		static {for(TestTiles tile : TestTiles.values()) map.put(tile.ordinal(), tile);}
		
		@Override
		public Map<Integer, TestTiles> getMap() {return map;}

		@Override
		public double getRoughness(TestTiles tileType) {
			switch(tileType) {
			case DIRT: return 0.75;
			case GRASS: return 0.9;
			case SAND: return 0.6;
			case STONE: return 0.95;
			case WATER: return 0.5;
			default:
				Console.err("ExampleApplication -> TestTiles -> getRoughness() -> invalid case : " + tileType);
				return 0.25;			
			}
		}		
	}
	
	public enum TestItems implements ItemBindable<TestItems>{
		POTION,
		FOOD,
		ROCK,
		;

		public static Map<Integer, TestItems> map = new HashMap<>();
		static {for(TestItems tile : TestItems.values()) map.put(tile.ordinal(), tile);}

		@Override
		public Map<Integer, TestItems> getMap() {return map;}

		@Override
		public BufferedImage getRaster(TestItems itemType) {
			switch(itemType) {
			case FOOD:   return testImage.getImage(TestImage.FOOD);	
			case POTION: return testImage.getImage(TestImage.POTION);		
			case ROCK:	 return testImage.getImage(TestImage.ROCK);	
			default:
				Console.err("ExampleApplication -> getRaster() -> invalid case : " + itemType);
				return FrameworkSprites.error;			
			}
		}

		@Override
		public String getDescription(TestItems itemType) {
			switch(itemType) {
			case FOOD: 	 return "Yummy food"; 
			case POTION: return "Health potion";
			case ROCK:	 return "Heavy rock";
			default:
				Console.err("ExampleApplication -> getRaster() -> invalid case : " + itemType);
				return "Error!";			
			}
		}

		@Override
		public int getValue(TestItems itemType) {
			switch(itemType) {
			case FOOD: 	 return 4;
			case POTION: return 6;
			case ROCK:	 return 3;
			default:
				Console.err("ExampleApplication -> getRaster() -> invalid case : " + itemType);
				return -1;		
			}
		}

		@Override
		public double getMass(TestItems itemType) {
			switch(itemType) {
			case FOOD: 	 return 1.0;
			case POTION: return 0.5;
			case ROCK:	 return 3.0;
			default:
				Console.err("ExampleApplication -> getRaster() -> invalid case : " + itemType);
				return 4.0;			
			}
		}
		
	}
	
	/*Here I created some public folders so that I can access the assets across the project
	 * I use the WarpedLinkables as type so each one will be linked with the right names*/
	public static WarpedImageFolder<TestImage> testImage;
	public static WarpedSpriteFolder<TestSprites> testSprites; 
	public static WarpedAudioFolder<TestAudio> testAudio;
	
	public static ManagerItem<TestItems> itemManager;
	
	/*Check out example of how to set up a HUD here*/
	public static ExampleHUD hud;
	
	public static InspectorInventory<TestItems> inventInspectorA;
	public static InspectorInventory<TestItems> inventInspectorB;
	
	public static WarpedInventory<TestItems> exampleInventoryA;
	public static WarpedInventory<TestItems> exampleInventoryB ;
	
	
	/*Check out example entitie here*/
	private ExampleEntitie dude;
	
	public static void main(String[] args) {		
		WarpedFramework2D.startFramework("WarpedFramework2D - SomeKid", 1920, 1080, null);
		WarpedFramework2D.startApplication(new ExampleApplication());
	}

	/*This application doesn't have any overall conditions but you might put checks here like 
	 * [example : if(score > 100) win();]
	 * [example : if(hasWon) openMenu();]
	 * This is mainly for smaller games / applications.
	 * For larger projects it will be necessary to set up most logic as events and have the main logic here (if any).
	 * */
	@Override
	public void persistentLogic() {
		//Put your main game logic here (if need).
	}
	
	/*This will load all the assets from the resource folders and bind them to the WarpedLinkables I specified.*/
	@Override
	public void loadAssets() {
		testImage 	= WarpedImageFolder.generateFolder(TestImage.FOOD, "res/framework/testImages");
		testSprites = WarpedSpriteFolder.generateFolder(TestSprites.SPRITE_8DIR, "res/framework/testSprites");
		testAudio 	= WarpedAudioFolder.generateFolder(TestAudio.TEST_1, "res/framework/testAudio");
	}
	
	/*This is where you set up your application.
	 * I added a second viewport as well as the default GUI viewport. 
	 * Set up the example entitie and HUD.
	 * Added a new key bind to open the HUD with F1.
	 * */
	@Override
	protected void initializeApplication() {
		itemManager = new ManagerItem<TestItems>();

		inventInspectorA = new InspectorInventory<TestItems>(4, 5);
		inventInspectorA.assemble();
		
		inventInspectorB = new InspectorInventory<TestItems>(4, 5);
		inventInspectorB.assemble();
		
		exampleInventoryA = itemManager.addGroup();
		exampleInventoryA.produce(TestItems.FOOD, 10);
		exampleInventoryA.produce(TestItems.POTION, 2);
		exampleInventoryA.produce(TestItems.ROCK, 5);
		exampleInventoryA.produce(TestItems.ROCK, 1);
		
		exampleInventoryB = itemManager.addGroup();
		exampleInventoryB.produce(TestItems.FOOD, 10);
		exampleInventoryB.produce(TestItems.POTION, 2);
		exampleInventoryB.produce(TestItems.ROCK, 5);
		exampleInventoryB.produce(TestItems.ROCK, 1);
		
		CharacterSprite testSprite = new CharacterSprite(testSprites.getSheet(TestSprites.SPRITE_8DIR), AxisType.HORIZONTAL);
		testSprite.setFrameRate(16);
		
		dude = new ExampleEntitie(testSprite);
		dude.setPosition(900, 500);
		
		WarpedGroup<WarpedObject> testGroup = WarpedState.objectManager.addGroup();
		testGroup.addMember(dude);
		WarpedState.openGroup(testGroup);
		
		hud = new ExampleHUD();	
		hud.assemble();
		
		WarpedKeyboard.getActiveController().addBinding(new WarpedKeyBind("Toggle Framework Inspector", KeyEvent.VK_F1, null, () -> {hud.toggle();}));
		
		Console.addCommand("/addHealth", () -> {WarpedState.notify.note("Cheat health");});
		Console.addCommand("/addStamina", () -> {WarpedState.notify.note("Cheat stamina");});
	}

	/*This function is run once when the framework has finished initializing and has prepared the application.
	 * You could have some logic here that begins a routine or process if need
	 * */
	@Override
	public void startApplication() {
		//example : startCinimatic();
	}
	
	/*This function will run once right before the application closes so you could have some termination logic here.
	 * Might want to add some save logic here.
	 * */
	@Override
	protected void stopApplication() {
		//example : saveState();
	}

}
