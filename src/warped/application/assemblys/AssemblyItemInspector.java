/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.Rectangle;
import java.util.Arrays;

import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ammunition.ItemAmmunition;
import warped.application.entities.item.artifact.ItemArtifact;
import warped.application.entities.item.container.ItemContainer;
import warped.application.entities.item.drink.ItemDrink;
import warped.application.entities.item.element.ItemElement;
import warped.application.entities.item.equipment.ItemEquipment;
import warped.application.entities.item.food.ItemFood;
import warped.application.entities.item.resource.ItemResource;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIImage;
import warped.application.gui.GUIShape;
import warped.application.gui.textBox.GUITextBoxLined;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.audio.FrameworkAudio;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.utils.UtilsMath;

public class AssemblyItemInspector extends WarpedAssembly {

	private Rectangle backgroundRect = new Rectangle(0, 0, 200, 400);
	private GUIShape background = new GUIShape();
	private GUITextBoxLined details = new GUITextBoxLined();
	private GUIImage item = new GUIImage();
	private GUIButton title = new GUIButton(200, 30);
	private GUIButton close = new GUIButton(FrameworkSprites.standardIcons.getSprite(2, 4));
	
	private Vec2d moduleOffset = new Vec2d(35, 35);
	private Vec2d detailsOffset = new Vec2d(5, 100);
	private Vec2d closeOffset = new Vec2d(170, 0);
	
	public AssemblyItemInspector(WarpedManagerType type) {
		super(type);
		
	}

	@Override
	protected void offsetAssembly() {
		item.setPositionOffset(moduleOffset);
		details.setPositionOffset(detailsOffset);
		close.setPositionOffset(closeOffset);
		
		item.offsetPosition();
		details.offsetPosition();		
		close.offsetPosition();
		
	}
	
	public void selectItem(WarpedItem item) {
		this.item.setRaster(item.raster());
		title.setText(Arrays.asList("Inspecting : " + item.getName()));
		
		details.clearText();
		details.putTextLine(0, "Item Type " + item.getItemType());
		
		switch(item.getItemType()) {
		case AMMUNITION:
			ItemAmmunition ammo = (ItemAmmunition)item;
			details.putTextLine(1, "Ammo Type : " + ammo.getAmmunitionType());
			break;
		case ARTIFACT:
			ItemArtifact artifact = (ItemArtifact)item;
			details.putTextLine(1, "Artifact Type : " + artifact.getArtifactType());
			break;
		case CONTAINER:
			ItemContainer container = (ItemContainer)item;
			details.putTextLine(1, "Container Type : " + container.getContainerType());
			break;
		case DRINK:
			ItemDrink drink = (ItemDrink)item;
			details.putTextLine(1, "Drink Type : " + drink.getDrinkType());
			break;
		case ELEMENT:
			ItemElement element = (ItemElement)item;
			details.putTextLine(1, "Element Type : " + element.getElementType());
			break;
		case EQUIPMENT:
			ItemEquipment equipment = (ItemEquipment)item;
			details.putTextLine(1, "Equipment Type : " + equipment.getEquipmentType());
			break;
		case FOOD:
			ItemFood food = (ItemFood)item;
			details.putTextLine(1, "Food Type : " + food.getFoodType());
			break;
		case RESOURCE:
			ItemResource resource = (ItemResource)item;
			details.putTextLine(1, "Resrouce Type : " + resource.getResourceType());
			break;
		default:
			break;
		}
		
		details.putTextLine(3, "Quantity : " + item.getQuantity());
		details.putTextLine(4, "Single Mass : " +  UtilsMath.round(item.massOfOne(), 9) );
		details.putTextLine(5, "Combined Mass : " + UtilsMath.round(item.getMass(), 3));
		details.putTextLine(6, "Single Value : " + item.valueOfOne());
		details.putTextLine(7, "Combined Value : " + item.getValue());
		details.updateTextBox();
	}


	@Override
	protected void defineAssembly() {
		background.addRectangle(backgroundRect, Colour.GREY_DARK.getColor());
		background.updateGraphics();
		
		title.draggable();
		title.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {		
				i.setPosition(title.getPosition().x, title.getPosition().y);
				i.offsetPosition();
			});			
		});

		close.setReleasedAction(() -> {close();});
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

	@Override
	protected void updateAssembly() {return;}

}
