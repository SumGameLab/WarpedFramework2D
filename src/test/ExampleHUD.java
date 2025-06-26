/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package test;

import java.awt.Color;
import java.awt.Rectangle;

import warped.application.actionWrappers.ActionOption;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIDialControl;
import warped.application.gui.GUIDialDisplay;
import warped.application.gui.GUIListVertical;
import warped.application.gui.GUIProgressBar;
import warped.application.gui.GUIScrollBar;
import warped.application.gui.GUIShape;
import warped.application.gui.GUITextBoxWrapped;
import warped.application.gui.GUITextInputLine;
import warped.application.state.GUIAssembly;
import warped.application.state.WarpedState;
import warped.audio.FrameworkAudio;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsString;

public class ExampleHUD extends GUIAssembly {

	private int borderThickness = 2;
	private GUIShape background = new GUIShape(410, 620);
	private Rectangle borderRect = new Rectangle(0, 0, 410, 620);
	private Rectangle backgroundRect = new Rectangle(borderThickness, borderThickness, borderRect.width - borderThickness * 2, borderRect.height - borderThickness * 2);
	
	public  GUIButton title = new GUIButton("  Drag me...  ");
	private GUIButton funny = new GUIButton("  Be funny  ");
	private GUIButton inventA = new GUIButton("InventA");
	private GUIButton inventB = new GUIButton("InventA");
	private GUITextInputLine notifyInput = new GUITextInputLine();
	private GUIScrollBar effectVolume = new GUIScrollBar();
	private GUIProgressBar volumeProgress = new GUIProgressBar();
	private GUIDialDisplay rotationDisplay = new GUIDialDisplay();
	private GUIDialControl rotationControl = new GUIDialControl();
	private GUITextBoxWrapped paragraph = new GUITextBoxWrapped(400, 250);
	private GUIListVertical list = new GUIListVertical();
	

	/**The default constructor is fine for most purposes.
	 * No need to do anything here.
	 * */
	public ExampleHUD() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**Define the properties of your assembly.
	 * Here I set the offset that I want for all my HUD pieces relative to the title.
	 * Then the offset is applied.
	 * I added some coloured rectangles to a GUIShape to make the background.
	 * Next I make the title draggable and set a drag action to offset the pieces again when the title moves.
	 * Then I set up a bunch of test elements for the HUD.
	 * */
	@Override
	protected void defineAssembly() { 
		funny.setOffset(50, 60);
		inventA.setOffset(210, 15);
		inventB.setOffset(210, 50);
		notifyInput.setOffset(50, 100);
		effectVolume.setOffset(50, 150);
		volumeProgress.setOffset(50, 190);
		rotationDisplay.setOffset(50, 225);
		rotationControl.setOffset(175, 225);
		paragraph.setOffset(5, 345);
		list.setOffset(300, 150);
		
		funny.offset(title);
		inventA.offset(title);
		inventB.offset(title);
		notifyInput.offset(title);
		effectVolume.offset(title);
		volumeProgress.offset(title);
		rotationDisplay.offset(title);
		rotationControl.offset(title);
		paragraph.offset(title);
		list.offset(title);
		
		background.addRectangle(borderRect, Color.BLACK);
		background.addRectangle(backgroundRect, Colour.GREY_DARK);
		
		title.setDragable(true);
		title.setDraggedAction(() -> {WarpedState.guiManager.getGroup(groupID).forEach(i -> {i.offset(title);});});

		paragraph.setTitle("A Story");
		paragraph.setParagraph("There once was a man named bob. Who had a very big job. One day rained and so he refrained from working out in the bog./n/The quick brown fox jumps over the lazy dog. Last month I had a broken finger.. it wasn't very fun. I wonder how long this line can be. This is a new paragraph. I need to write at least a couple more sentences in order to fill up this area. We want to overflow the field with text in order to test out the fancy new scroll bars.");
		paragraph.setParagraphTextSize(18);
		paragraph.setBorderThickness(0);
		
		funny.setReleasedAction(mouseE -> {WarpedState.notify.note(UtilsString.getFunnyString());});

		inventA.setReleasedAction(mouseE -> {
			ExampleApplication.inventInspectorA.selectInventory(ExampleApplication.exampleInventoryA);
			ExampleApplication.inventInspectorA.toggle();	
		}); 
		
		inventB.setReleasedAction(mouseE -> {
			ExampleApplication.inventInspectorB.selectInventory(ExampleApplication.exampleInventoryB);
			ExampleApplication.inventInspectorB.toggle();		
		}); 
		
		notifyInput.setButtonAction(str -> {WarpedState.notify.note(str);});
		
		effectVolume.setLabel(" Volume ");
		effectVolume.setReleaseAction(() -> {FrameworkAudio.setVolume(effectVolume.getProgress());});
		
		volumeProgress.setProgress(FrameworkAudio.getVolume());
		volumeProgress.setFillDirection(DirectionType.RIGHT);
		volumeProgress.setLabel("Rotation % ");
		volumeProgress.setProgressTextVisible(true);
		
		rotationDisplay.addHand(FrameworkSprites.dialIcons.getSprite(1, 1));
		rotationControl.setRotationBounds(-UtilsMath.TWO_PI * 2, UtilsMath.TWO_PI);
		rotationControl.setRotationAction(() -> {
			volumeProgress.setProgress(rotationControl.getPercentage());
			rotationDisplay.setHandRotation(1, rotationControl.getRotation());
		});
		
		list.setSize(100, 180);
		list.setButtonSize(100, 80);
		list.setOptions(new ActionOption("test1", () -> {Console.ln("TEST1");}), 
						new ActionOption("test2", () -> {Console.ln("TEST2");}),
						new ActionOption("test3", () -> {Console.ln("TEST3");}));
		
	
	}

	/**Everything has to be added to the assembly.
	 * Each member should only be added once.
	 * Once the members are added they will be updated and drawn automatically by the default viewport (this can be set to a custom viewport later if need).
	 * */
	@Override
	protected void addAssembly() {
		addMember(background);
		addMember(title);
		addMember(inventA);
		addMember(inventB);
		addMember(funny);
		addMember(notifyInput);
		addMember(effectVolume);
		addMember(volumeProgress);
		addMember(rotationDisplay);
		addMember(rotationControl);
		addMember(paragraph);
		addMember(list);
	}

	/**Here I override the default update method just to add quick rotation to test the rotation display.
	 * */
	@Override
	public void updateAssembly() {
		rotationDisplay.setHandRotation(0, rotationDisplay.getHandRotation(0) + 0.01);
	}

}
