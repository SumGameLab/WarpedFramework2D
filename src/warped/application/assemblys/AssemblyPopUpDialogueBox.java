/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import warped.application.gui.GUIButton;
import warped.application.gui.GUIImage;
import warped.application.gui.GUIShape;
import warped.application.gui.WarpedGUI;
import warped.application.gui.textBox.GUITextBoxLined;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.window.WarpedWindow;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.enums.generalised.StyleType;
import warped.utilities.math.geometry.curves.bezier.BezierCurveLinearI;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;

public class AssemblyPopUpDialogueBox extends WarpedAssembly  {

	private DirectionType defPopDirection = DirectionType.UP;
	
	private StyleType defStyle 		   = StyleType.DEFAULT;
	private Vec2i defBackgroundSize    = new Vec2i(650, 250);
	private Vec2i defCharacterSize     = new Vec2i(200, 200);
	private Color defBackgroundColor   = new Color(60,60,60);
	private Color defBorderColor       = Color.BLACK;
	private int   defBorderThickness   = 4;
	private int   defPopPositionOffset = (WarpedWindow.width / 2) - 300;
	private int   defPopDistance 	   = 300;
	private Vec2i defCharacterOffset   = new Vec2i(420, 25);
	private Vec2i defDialogueOffset    = new Vec2i(25, 25);
	private Vec2i defCloseOffset 	   = new Vec2i(230, 200);
	
	private GUIShape background = new GUIShape();
	private GUIImage character  = new GUIImage();
	private GUIButton close     = new GUIButton("Read");
	private GUITextBoxLined dialogue = new GUITextBoxLined();
	
	private BezierCurveLinearI curveOut;
	private BezierCurveLinearI curveIn;
	
	private DirectionType popDirection = defPopDirection;
	private StyleType style		    = defStyle;
	private int popPositionOffset   = defPopPositionOffset;
	private int popDistance         = defPopDistance;
	private Vec2i backgroundSize    = defBackgroundSize;
	private Vec2i characterSize     = defCharacterSize;
	private int borderThickness     = defBorderThickness;
	private Color borderColor       = defBorderColor;
	private Color backgroundColor   = defBackgroundColor;
	private Vec2i popInPosition     = new Vec2i();
	private Vec2i position          = new Vec2i();
	private Vec2i characterOffset   = defCharacterOffset;
	private Vec2i dialogueOffset    = defDialogueOffset;
	private Vec2i closeOffset       = defCloseOffset;
	
	private boolean isPoppedOut;
	private boolean popOut = false;
	private boolean pullIn = false;
	
	public AssemblyPopUpDialogueBox(WarpedManagerType type) {
		super(type);
		initializeBackground();
		updatePopPosition();
		close.setTextOffset(35, 6);
		close.setGameObjectSize(100, 30);
		close.updateGraphics();
		dialogue.setTextSize(16);	
		dialogue.setTextOffset(0, 16);
		dialogue.setTextBoxSize(400, 240);
	}

	
	//--------
	//---------------- Init --------
	//--------
	private void initializeBackground() {
		background.clear();
		background.addRectangle(new Rectangle(0, 0, backgroundSize.x, backgroundSize.y), borderColor);
		background.addRectangle(new Rectangle(borderThickness, borderThickness, backgroundSize.x - borderThickness * 2, backgroundSize.y - borderThickness * 2), backgroundColor);
		background.addRectangle(new Rectangle(characterOffset.x - borderThickness * 2, characterOffset.y - borderThickness * 2, characterSize.x + borderThickness * 4, characterSize.y + borderThickness * 4), borderColor);
		background.updateGraphics();
		
	}

	@Override
	protected void offsetAssembly() {
		character.setPositionOffset(characterOffset);
		dialogue.setPositionOffset(dialogueOffset);
		close.setPositionOffset(closeOffset);
		
		character.offsetPosition();
		dialogue.offsetPosition();
		close.offsetPosition();
	}

	@Override
	protected void defineAssembly() {
		close.setReleasedAction(() -> {close();});
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
		this.dialogue.setTextLines(dialogue);
		this.character.setRaster(character);
		setStyle(style);
		open();
	}
	
	public final void openDialogue(List<String> dialogue, BufferedImage character) {
		Console.ln("PopUpDialogueBox -> openDialogue()");
		this.dialogue.setTextLines(dialogue);
		this.character.setRaster(character);
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
			backgroundSize    = new Vec2i(420, WarpedWindow.height);    
			characterSize     = defCharacterSize;     
			borderThickness   = defBorderThickness;   
			borderColor       = defBorderColor;       
			backgroundColor   = defBackgroundColor;                 
			characterOffset   = new Vec2i(100, 20);   
			dialogueOffset    = new Vec2i(20, 250);    
			closeOffset       = new Vec2i(100, WarpedWindow.height - 100);
			dialogue.setTextSize(16);			
			dialogue.setTextOffset(0, 10);
			dialogue.setTextBoxSize(420, WarpedWindow.height - 100);
			
			break;
		default:
			Console.err("PopUpDialogueBox -> setStyle() -> invalid style : " + style);
			break;
		
		}
		offsetAssembly();
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
	public final void setDialogueText(List<String> dialogue) {this.dialogue.setTextLines(dialogue);}
	
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
		Vec2i popOutPosition = null;
		switch(popDirection) {
		case UP:   
			popInPosition.set(popPositionOffset, WarpedWindow.height);
			popOutPosition = new Vec2i(popInPosition.x, popInPosition.y - popDistance);
			break;
		case DOWN: 
			popInPosition.set(popPositionOffset, 0);
			popOutPosition =  new Vec2i(popInPosition.x, popInPosition.y + popDistance);
			break;
		case RIGHT:	
			popInPosition.set(-popDistance, popPositionOffset);
			popOutPosition = new Vec2i(0, popInPosition.y);
			break;
		case LEFT:
			popInPosition.set(WarpedWindow.width, popPositionOffset);
			popOutPosition = new Vec2i(popInPosition.x - popDistance, popInPosition.y);
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
				WarpedGUI element = (WarpedGUI)i;
				element.setPosition(position.x, position.y);
				element.offsetPosition();
				
			});		
		}
	}
	
	
	

}
