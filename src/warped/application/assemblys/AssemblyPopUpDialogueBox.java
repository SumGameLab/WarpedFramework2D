/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import warped.application.gui.GUIButton;
import warped.application.gui.GUIImage;
import warped.application.gui.GUIShape;
import warped.application.gui.GUITextBoxLined;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.window.WarpedWindow;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.enums.generalised.StyleType;
import warped.utilities.math.geometry.bezier.BezierCurveLinearI;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class AssemblyPopUpDialogueBox extends WarpedAssembly  {

	private DirectionType defPopDirection = DirectionType.UP;
	
	private StyleType defStyle 		   = StyleType.DEFAULT;
	private VectorI defBackgroundSize    = new VectorI(650, 250);
	private VectorI defCharacterSize     = new VectorI(200, 200);
	private Color defBackgroundColor   = new Color(60,60,60);
	private Color defBorderColor       = Color.BLACK;
	private int   defBorderThickness   = 4;
	private int   defPopPositionOffset = (WarpedWindow.getWindowWidth() / 2) - 300;
	private int   defPopDistance 	   = 300;
	private VectorI defCharacterOffset   = new VectorI(420, 25);
	private VectorI defDialogueOffset    = new VectorI(25, 25);
	private VectorI defCloseOffset 	   = new VectorI(230, 200);
	
	private GUIShape background = new GUIShape(defBackgroundSize.x(), defBackgroundSize.y());
	private GUIImage character  = new GUIImage();
	private GUIButton close     = new GUIButton("Read");
	private GUITextBoxLined dialogue = new GUITextBoxLined(300, 300);
	
	private BezierCurveLinearI curveOut;
	private BezierCurveLinearI curveIn;
	
	private DirectionType popDirection = defPopDirection;
	private StyleType style		    = defStyle;
	private int popPositionOffset   = defPopPositionOffset;
	private int popDistance         = defPopDistance;
	private VectorI backgroundSize    = defBackgroundSize;
	private VectorI characterSize     = defCharacterSize;
	private int borderThickness     = defBorderThickness;
	private Color borderColor       = defBorderColor;
	private Color backgroundColor   = defBackgroundColor;
	private VectorI popInPosition     = new VectorI();
	private VectorI position          = new VectorI();
	private VectorI characterOffset   = defCharacterOffset;
	private VectorI dialogueOffset    = defDialogueOffset;
	private VectorI closeOffset       = defCloseOffset;
	
	private boolean isPoppedOut;
	private boolean popOut = false;
	private boolean pullIn = false;
	
	public AssemblyPopUpDialogueBox(WarpedManagerType type) {
		super(type);
		initializeBackground();
		updatePopPosition();
		close.setTextOffset(35, 6);
		close.setSize(100, 30);
		dialogue.setTextSize(16);	
		dialogue.setTextOffset(0, 16);
		dialogue.setTextBoxSize(400, 240);
	}

	
	//--------
	//---------------- Init --------
	//--------
	private void initializeBackground() {
		background.clear();
		background.addRectangle(new Rectangle(0, 0, backgroundSize.x(), backgroundSize.y()), borderColor);
		background.addRectangle(new Rectangle(borderThickness, borderThickness, backgroundSize.x() - borderThickness * 2, backgroundSize.y() - borderThickness * 2), backgroundColor);
		background.addRectangle(new Rectangle(characterOffset.x() - borderThickness * 2, characterOffset.y() - borderThickness * 2, characterSize.x() + borderThickness * 4, characterSize.y() + borderThickness * 4), borderColor);
		//background.updateGraphics();
		
	}


	@Override
	protected void defineAssembly() {
		character.setOffset(characterOffset);
		dialogue.setOffset(dialogueOffset);
		close.setOffset(closeOffset);
		
		character.offset(position);
		dialogue.offset(position);
		close.offset(position);
		
		close.setReleasedAction(buttonE -> {close();});
	}

	@Override
	protected void addAssembly() {
		addMember(background);
		addMember(dialogue);
		addMember(character);
		addMember(close);
	}
	
	
	//--------
	//---------------- Access --------
	//--------
	public final void openDialogue(StyleType style, List<String> dialogue, BufferedImage character) {
		Console.ln("PopUpDialogueBox -> openDialogue()");
		this.dialogue.setTextLine(dialogue, Colour.WHITE);
		this.character.getSprite().paint(character);
		setStyle(style);
		open();
	}
	
	public final void openDialogue(List<String> dialogue, BufferedImage character) {
		Console.ln("PopUpDialogueBox -> openDialogue()");
		this.dialogue.setTextLine(dialogue, Colour.WHITE);
		this.character.getSprite().paint(character);
		setStyle(StyleType.DEFAULT);
		open();
	}
	
	
	public final void setStyle(StyleType style) {
		if(this.style == style) return;
		switch(style) {
		case DEFAULT:
			this.style 		  = style;
			popDirection 	  = defPopDirection;              
			popPositionOffset = defPopPositionOffset; 
			popDistance       = defPopDistance;       
			backgroundSize    = defBackgroundSize;    
			characterSize     = defCharacterSize;     
			borderThickness   = defBorderThickness;   
			borderColor       = defBorderColor;       
			backgroundColor   = defBackgroundColor;                 
			characterOffset   = defCharacterOffset;   
			dialogueOffset    = defDialogueOffset;    
			closeOffset       = defCloseOffset;
			dialogue.setTextSize(16);			
			dialogue.setTextOffset(0, 16);
			dialogue.setTextBoxSize(400, 240);
	
			break;
		case WARP_TECH:
			this.style 		  = style;
			popDirection 	  = DirectionType.RIGHT;              
			popPositionOffset = 0; 
			popDistance       = 420;       
			backgroundSize    = new VectorI(420, WarpedWindow.getWindowHeight());    
			characterSize     = defCharacterSize;     
			borderThickness   = defBorderThickness;   
			borderColor       = defBorderColor;       
			backgroundColor   = defBackgroundColor;                 
			characterOffset   = new VectorI(100, 20);   
			dialogueOffset    = new VectorI(20, 250);    
			closeOffset       = new VectorI(100, WarpedWindow.getWindowHeight() - 100);
			dialogue.setTextSize(16);			
			dialogue.setTextOffset(0, 10);
			dialogue.setTextBoxSize(420, WarpedWindow.getWindowHeight() - 100);
			
			break;
		default:
			Console.err("PopUpDialogueBox -> setStyle() -> invalid style : " + style);
			break;
		
		}
	
		updatePopPosition();
		updateGraphics();
	}
	
	public final void setPop(DirectionType popDirection, int popDistance,int popPositionOffset) {
		this.popDirection = popDirection;
		this.popDistance = popDistance;
		this.popPositionOffset = popPositionOffset;
		updatePopPosition();
	};
	
	public final void setPopDistance(int popDistance) {this.popDistance = popDistance;}
	
	public final void setPopDirection(DirectionType popDirection) {
		this.popDirection = popDirection;
		updatePopPosition();
	}
	public final void setPopPositionOffset(int popPositionOffset) {
		this.popPositionOffset = popPositionOffset;
		updatePopPosition();
	}
	public final void setDialogueText(List<String> dialogue) {this.dialogue.setTextLine(dialogue, Colour.WHITE);}
	
	public final void togglePop() {
		if(isPoppedOut) pullIn = true;
		else popOut = true;
	}	
	
	
	//--------
	//---------------- Update --------
	//--------
	public final void updateGraphics() {
		initializeBackground();
		updatePopPosition();
	}
	
	private void updatePopPosition() {
		VectorI popOutPosition = null;
		switch(popDirection) {
		case UP:   
			popInPosition.set(popPositionOffset, WarpedWindow.getWindowHeight());
			popOutPosition = new VectorI(popInPosition.x(), popInPosition.y() - popDistance);
			break;
		case DOWN: 
			popInPosition.set(popPositionOffset, 0);
			popOutPosition =  new VectorI(popInPosition.x(), popInPosition.y() + popDistance);
			break;
		case RIGHT:	
			popInPosition.set(-popDistance, popPositionOffset);
			popOutPosition = new VectorI(0, popInPosition.y());
			break;
		case LEFT:
			popInPosition.set(WarpedWindow.getWindowWidth(), popPositionOffset);
			popOutPosition = new VectorI(popInPosition.x() - popDistance, popInPosition.y());
		break;
		default: Console.err("PopUpDialogueBox -> updatePop() -> invalid case : " + popDirection); return;
		}
		curveOut = new BezierCurveLinearI(popInPosition, popOutPosition, position);
		curveIn  = new BezierCurveLinearI(popOutPosition, popInPosition, position);
	}


	@Override
	protected void updateAssembly() {
		if(popOut && pullIn) {
			Console.err("PopUpDialogueBox -> updateAssembly -> conflicting arguments");
			popOut = pullIn = false;
			return;
		}
		if(popOut && isPoppedOut) {
			Console.err("PopUpDialogueBox -> updateAssembly -> conflicting arguments");
			popOut = false;
			return;
		}
		if(pullIn && !isPoppedOut) {
			Console.err("PopUpDialogueBox -> updateAssembly -> conflicting arguments");
			pullIn = false;
			return;
		}
		
		if(popOut && curveOut.isComplete()) {
			popOut = false;
			isPoppedOut = true;
			curveOut.reset();
		}
		if(pullIn && curveIn.isComplete()) {
			pullIn = false;
			isPoppedOut = false;
			curveIn.reset();
		}
		
		if(popOut || pullIn) {			
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				i.offset(position);
				
			});		
		}
	}
	
	
	

}
