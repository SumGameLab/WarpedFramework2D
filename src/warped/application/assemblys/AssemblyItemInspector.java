/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.Rectangle;

import warped.application.entities.item.WarpedItem;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIImage;
import warped.application.gui.GUIShape;
import warped.application.gui.GUITextBoxLined;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.audio.FrameworkAudio;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.UtilsMath;

public class AssemblyItemInspector extends WarpedAssembly {

	private Rectangle backgroundRect = new Rectangle(0, 0, 200, 400);
	private GUIShape background = new GUIShape(200, 300);
	private GUITextBoxLined details = new GUITextBoxLined(200, 300);
	private GUIImage item = new GUIImage();
	private GUIButton title = new GUIButton(200, 30);
	private GUIButton close = new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	
	private VectorD moduleOffset = new VectorD(35, 35);
	private VectorD detailsOffset = new VectorD(5, 100);
	private VectorD closeOffset = new VectorD(170, 0);
	
	public AssemblyItemInspector(WarpedManagerType type) {
		super(type);
		
	}


	
	public void selectItem(WarpedItem<?> item) {
		this.item.getSprite().paint(item.raster());;
		title.setText("Inspecting : " + item.getName());
		
		details.clearTextLines();
		details.setTextLine(0, "Item Type " + item.getItemType());
		
		details.setTextLine(3, "Quantity : " + item.getQuantity());
		details.setTextLine(4, "Single Mass : " +  UtilsMath.round(item.getMassOfOne(), 9) );
		details.setTextLine(5, "Combined Mass : " + UtilsMath.round(item.getMass(), 3));
		details.setTextLine(6, "Single Value : " + item.getValueOfOne());
		details.setTextLine(7, "Combined Value : " + item.getValue());
	}


	@Override
	protected void defineAssembly() {
		item.setOffset(moduleOffset);
		details.setOffset(detailsOffset);
		close.setOffset(closeOffset);
		
		item.offset(title);
		details.offset(title);		
		close.offset(title);
		
		background.addRectangle(backgroundRect, Colour.GREY_DARK.getColor());
		
		title.draggable();
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {		
				i.offset(title);
			});			
		});

		close.setReleasedAction(mouseE -> {close();});
		close.setReleasedSFX(FrameworkAudio.defaultClose);	
		
		details.setTextBoxSize(300, 300);
		details.setTextSize(14);
		
		
	}

	@Override
	protected void addAssembly() {
		addMember(background);
		addMember(details);
		addMember(item);
		addMember(title);
		addMember(close);
		
	}


}
