/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped;

import java.util.HashMap;
import java.util.Map;

import warped.application.gui.GUIButton;
import warped.application.gui.WarpedGUI;
import warped.application.state.WarpedApplication;
import warped.application.state.WarpedAudioFolder;
import warped.application.state.WarpedImageFolder;
import warped.application.state.WarpedSpriteFolder;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroup;
import warped.graphics.window.WarpedWindow;
import warped.utilities.enums.WarpedLinkable;

public class TestApplication extends WarpedApplication {
	
	public static void main(String[] args) {		
		WarpedFramework2D.startFramework("WarpedFramework2D - SomeKid", 1920, 1080, null);
		WarpedFramework2D.startApplication(new TestApplication());
	}

	private enum TestSprites implements WarpedLinkable<TestSprites>{
		TEST_1,
		TEST_2,
		TEST_3,
		;public static Map<Integer, TestSprites> map = new HashMap<>();
		static {for(TestSprites sprite : TestSprites.values()) map.put(sprite.ordinal(), sprite);}
		@Override
		public Map<Integer, TestSprites> getMap() {return map;}
		
	}
	
	private enum TestAudio implements WarpedLinkable<TestSprites>{
		TEST_1,
		TEST_2,
		TEST_3,
		;public static Map<Integer, TestSprites> map = new HashMap<>();
		static {for(TestSprites sprite : TestSprites.values()) map.put(sprite.ordinal(), sprite);}
		@Override
		public Map<Integer, TestSprites> getMap() {return map;}
		
	}

	private enum TestImage implements WarpedLinkable<TestImage>{
		TEST_1,
		TEST_2,
		TEST_3,
		;public static Map<Integer, TestImage> map = new HashMap<>();
		static {for(TestImage sprite : TestImage.values()) map.put(sprite.ordinal(), sprite);}
		@Override
		public Map<Integer, TestImage> getMap() {return map;}
		
	}
	


	@Override
	public void persistentLogic() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void loadAssets() {
		WarpedImageFolder<TestImage> testImageFolder = WarpedImageFolder.generateFolder(TestImage.TEST_1, "res/framework/testImages");
		WarpedSpriteFolder<TestSprites> testSpriteFolder = WarpedSpriteFolder.generateFolder(TestSprites.TEST_1, "res/framework/testGraphics");
		WarpedAudioFolder<TestAudio> testAudioFolder = WarpedAudioFolder.generateFolder(TestAudio.TEST_1, "res/framework/testAudio");
	    
	}
	
	@Override
	protected void initializeApplication() {
	GUIButton testButton = new GUIButton("exit");
	GUIButton testButton2 = new GUIButton("720p");
	GUIButton testButton3 = new GUIButton("1080p");
	GUIButton testButton4 = new GUIButton("4K");
	testButton.setReleasedAction(() -> {WarpedFramework2D.stop();});
	testButton2.setReleasedAction(() -> {WarpedFramework2D.getWindow().setWindowResolution(1280, 720);});
	testButton3.setReleasedAction(() -> {WarpedFramework2D.getWindow().setWindowResolution(1920, 1080);});
	testButton4.setReleasedAction(() -> {WarpedFramework2D.getWindow().setWindowResolution(2460, 1440);});
	testButton.setPosition(WarpedWindow.width / 2, WarpedWindow.height / 2);
	testButton2.setPosition(testButton.getPosition().x, testButton.getPosition().y + 70);
	testButton3.setPosition(testButton.getPosition().x, testButton2.getPosition().y + 70);
	testButton4.setPosition(testButton.getPosition().x, testButton3.getPosition().y + 70);
    WarpedGroup<WarpedGUI> testGroup = WarpedState.guiManager.getGroup(WarpedState.guiManager.addGroup());
    testGroup.addMember(testButton);
    testGroup.addMember(testButton2);
    testGroup.addMember(testButton3);
    testGroup.addMember(testButton4);
    WarpedState.openGroup(testGroup);
    
    
    //WarpedViewport testViewport = new WarpedViewport("test", WarpedManagerType.GUI, 0, 0, WarpedWindow.width, WarpedWindow.height);
    //WarpedWindow.setViewPorts(Arrays.asList(testViewport));
    //WarpedWindow.createViewport(WarpedManagerType.GUI, 0, 0, WarpedWindow.width, WarpedWindow.height);
    
    
    
		
	}

	@Override
	protected void stopApplication() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startApplication() {
		// TODO Auto-generated method stub
		
	}

}
