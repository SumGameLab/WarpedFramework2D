/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package test;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import test.ExampleItem.ExampleItems;
import warped.application.assemblys.InspectorInventory;
import warped.application.state.ManagerItem;
import warped.application.state.WarpedApplication;
import warped.application.state.WarpedAudioFolder;
import warped.application.state.WarpedCollisionManager;
import warped.application.state.WarpedFramework2D;
import warped.application.state.WarpedGroup;
import warped.application.state.WarpedImageFolder;
import warped.application.state.WarpedInventory;
import warped.application.state.WarpedObject;
import warped.application.state.WarpedSpriteFolder;
import warped.application.state.WarpedState;
import warped.graphics.sprite.CharacterSprite;
import warped.graphics.window.WarpedWindow;
import warped.user.keyboard.WarpedKeyBind;
import warped.user.keyboard.WarpedKeyboard;
import warped.utilities.enums.WarpedLinkable;
import warped.utilities.enums.generalised.AxisType;
import warped.utilities.utils.Console;

public class ExampleApplication extends WarpedApplication {
	
	/*Here I set up some WarpedLinkables so we can refer to the assets i want to load later.	
	 * */
	public enum TestSprites implements WarpedLinkable<TestSprites> {
		SPRITE_8DIR,
		;public static Map<Integer, TestSprites> map = new HashMap<>();
		static {for(TestSprites sprite : TestSprites.values()) map.put(sprite.ordinal(), sprite);}
		@Override
		public Map<Integer, TestSprites> getMap() {return map;}
	}
	
	public enum TestAudio implements WarpedLinkable<TestAudio> {
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
	

	

	/*Here I created some public folders so that I can access the assets across the project
	 * I use the WarpedLinkables as type so each one will be linked with the right names*/
	public static WarpedImageFolder<TestImage> testImage;
	public static WarpedSpriteFolder<TestSprites> testSprites; 
	public static WarpedAudioFolder<TestAudio> testAudio;
	
	public static ManagerItem<ExampleItems> itemManager;
	public static WarpedInventory<ExampleItems> itemDropGroup;
	
	/*Check out example of how to set up a HUD here*/
	public static ExampleHUD hud;
	
	public static InspectorInventory<ExampleItems> inventInspectorA;
	public static InspectorInventory<ExampleItems> inventInspectorB;
	
	public static WarpedInventory<ExampleItems> exampleInventoryA;
	public static WarpedInventory<ExampleItems> exampleInventoryB ;
	
	
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
		//UtilsImage.writeImageToFile(UtilsImage.removeOpacity(UtilsImage.basicLoadBufferedImage("dat/smokes_wf2d_64_64_wf.png")), "dat/smokes_wf2d_64_64_wf");
		/*
		String result = "";
		try {
			result = UtilsString.translate("en", "fr", "Hello World");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Console.ln(Console.ConsoleColour.YELLOW, result);
		*/
		
		
		itemManager = new ManagerItem<ExampleItems>();
		itemDropGroup = itemManager.addGroup();
		
		itemDropGroup.open();

		inventInspectorA = new InspectorInventory<ExampleItems>(4, 5);
		inventInspectorA.assemble();
		
		inventInspectorB = new InspectorInventory<ExampleItems>(4, 5);
		inventInspectorB.assemble();
		
		exampleInventoryA = itemManager.addGroup();
		exampleInventoryA.produce(ExampleItems.FOOD, 10);
		exampleInventoryA.produce(ExampleItems.POTION, 2);
		exampleInventoryA.produce(ExampleItems.ROCK, 5);
		exampleInventoryA.produce(ExampleItems.ROCK, 1);
		
		exampleInventoryB = itemManager.addGroup();
		exampleInventoryB.produce(ExampleItems.FOOD, 10);
		exampleInventoryB.produce(ExampleItems.POTION, 2);
		exampleInventoryB.produce(ExampleItems.ROCK, 5);
		exampleInventoryB.produce(ExampleItems.ROCK, 1);
		
		
		
		WarpedGroup<WarpedObject> testGroup = WarpedState.objectManager.addGroup();
		WarpedState.openGroup(testGroup);

		WarpedGroup<WarpedObject> testGroup2 = WarpedState.objectManager.addGroup();
		WarpedState.openGroup(testGroup2);
		
		for(int i = 0; i < 10000; i++) {			
			CharacterSprite testSprite = new CharacterSprite(testSprites.getSheet(TestSprites.SPRITE_8DIR), AxisType.HORIZONTAL);
			testSprite.setFrameRate(16);
			dude = new ExampleEntitie(testSprite);
			dude.setPosition(900, 500);
			testGroup.addMember(dude);
		}
		testGroup.addMember(new ExampleProjectile(300, 300, 750, 0.0));
		testGroup2.addMember(new ExampleProjectile(900, 300, -500, 0.0));	
		
		//WarpedCollisionManager.setInternalCollisionMode(false);
		WarpedCollisionManager.addCrossGroupCollision(testGroup2, testGroup);
		
		hud = new ExampleHUD();	
		hud.assemble();
		
		WarpedKeyboard.getActiveController().addBinding(new WarpedKeyBind("Toggle Framework Inspector", KeyEvent.VK_F1, null, () -> {hud.toggle();}));
		
		//WarpedMouse.setMouseController(new ExampleMouseController());
		
		WarpedWindow.addViewport(itemManager, 1);
		
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

	@Override
	public void endWarpedCinematic() {
		// TODO Auto-generated method stub
		
	}

}

