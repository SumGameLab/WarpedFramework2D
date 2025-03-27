/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.Rectangle;
import java.util.ArrayList;

import warped.application.actionWrappers.ActionToggle;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIIcon;
import warped.application.gui.GUILabel;
import warped.application.gui.GUIPopOutToggle;
import warped.application.gui.GUIShape;
import warped.application.gui.GUITextBoxLined;
import warped.application.gui.GUIToggle;
import warped.application.gui.GUIValue;
import warped.application.object.WarpedObject;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.audio.FrameworkAudio;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;

public class InspectorObject extends WarpedAssembly {
	
	private WarpedObject object;
		
	private GUIShape  background    = new GUIShape(400, 600);
	private GUIButton title   		= new GUIButton("Object Inspector");
	private GUIToggle refresh       = new GUIToggle(FrameworkSprites.getStandardIcon(StandardIcons.REFRESH));
	private GUIButton close   		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	
	private GUIPopOutToggle params   = new GUIPopOutToggle("Param");
	private GUITextBoxLined details	 = new GUITextBoxLined(380, 430);
	private GUIIcon   icon   	     = new GUIIcon(100, 100);
	
	private GUILabel positionLabel 		 = new GUILabel("Position : ");
	private GUILabel renderPositionLabel = new GUILabel("Render Position : ");
	private GUILabel renderSizeLabel 	 = new GUILabel("Render Size : ");
		
	private GUIValue  position		 = new GUIValue(120, 30);
	private GUIValue  renderPosition = new GUIValue(120, 30);
	private GUIValue  renderSize	 = new GUIValue(120, 30);

	
	public InspectorObject(WarpedManagerType type) {
		super(type);
	}
	
	protected void defineAssembly() {
		background.addRectangle(new Rectangle(0, 0, 400, 600), Colour.GREY_DARK);
		
		close.setOffset(370, 0);
		refresh.setOffset(340, 0);
		params.setOffset(250, 0);
		details.setOffset(10, 160);
		icon.setOffset(10, 40);
		
		positionLabel.setOffset(110,  40);
		renderPositionLabel.setOffset(110,  80);
		renderSizeLabel.setOffset(110, 120);
		position.setOffset(240,  40);
		renderPosition.setOffset(240,  80);
		renderSize.setOffset(240, 120);
		
		close.offset(title);
		refresh.offset(title);
		params.offset(title);
		details.offset(title);
		icon.offset(title);
		
		positionLabel.offset(title);
		renderPositionLabel.offset(title);
		renderSizeLabel.offset(title);
		
		position.offset(title);
		renderPosition.offset(title);
		renderSize.offset(title);
		
		positionLabel.setLabelSize(120, 30);
		renderPositionLabel.setLabelSize(120, 30);
		renderSizeLabel.setLabelSize(120, 30);
		
		title.draggable();
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				i.offset(title);
			});			
		});
		
		ArrayList<ActionToggle> actions = new ArrayList<>();
		actions.add(new ActionToggle("Visible", true,
				() -> {if(object != null) object.setVisible(true); updateDetails();},
				() -> {if(object != null) object.setVisible(false); updateDetails();}));
		actions.add(new ActionToggle("Interactive", true,
				() -> {if(object != null) object.setInteractive(true); updateDetails();},
				() -> {if(object != null) object.setInteractive(false); updateDetails();}));
		actions.add(new ActionToggle("Express", false,
				() -> {if(object != null) object.setExpress(true); updateDetails();},
				() -> {if(object != null) object.setExpress(false);; updateDetails();}));
		actions.add(new ActionToggle("Alive", true,
				() -> {select(null);},
				() -> {if(object != null) object.kill(); updateDetails();}));
		params.setToggles(actions);
		
		icon.setIconSize(100, 100);
		icon.setBackgroundColor(Colour.GREY_LIGHT.getColor());
		icon.setPreserveAspectRatio(true);
		
		position.setDecimals(2);
		renderPosition.setDecimals(2);
		
		close.setReleasedAction(mouseE -> {close();});
		close.setReleasedSFX(FrameworkAudio.defaultClose);
		
		refresh.setRepeatingAction(() -> {updateDetails();});
	}
	
	
	protected void addAssembly() {
		addMember(background);
		addMember(title);
		addMember(refresh);
		addMember(icon);
		addMember(close);
		addMember(details);
		addMember(position);
		addMember(renderPosition);
		addMember(renderSize);
		addMember(positionLabel);
		addMember(renderPositionLabel);
		addMember(renderSizeLabel);
		addMember(params);
	}
	
	/**Select an object to inspect.
	 * @param obj - the object to view.
	 * @implNote use null as parameter to clear the selection.
	 * @author 5som3*/
	public void select(WarpedObject obj) {
		if(obj == null) clearSelection(); 
		else {			
			this.object = obj;
			icon.setTarget(obj);
			position.setTarget(obj.getPosition());
			renderPosition.setTarget(obj.getRenderPosition());
			renderSize.setTarget(obj.getRenderSize());
			params.setToggleStates(obj.isVisible(), obj.isInteractive(), obj.isExpress(), obj.isAlive());
		}
		updateDetails();
	}
	
	/**Clear the object that is being inspected.
	 * @apiNote also updates the graphics to show that nothing is selected.
	 * @author 5som3*/
	private void clearSelection() {
		this.object = null;
		icon.setTarget(null);
		position.clearTarget();
		renderPosition.clearTarget();
		renderSize.clearTarget();;		
	}

	private void updateDetails() {
		if(object == null) {
			Console.ln("InspectorObject -> updateDetails() -> inspector is not targeting an object -> clearing details");
			details.clearTextLines();
		} else {			
			details.setTextLine(0, "Class : " + object.getClass().getSimpleName());
			details.setTextLine(1, "Express : " + object.isExpress());
			details.setTextLine(2, "Alive : " + object.isAlive());
			details.setTextLine(3, "Visible : " + object.isVisible());
			details.setTextLine(4, "Interactive : " + object.isInteractive());
			details.setTextLine(5, "ToolTip : " + object.getToolTip());
			details.updateGraphics();
		}
	}
	
	
}
