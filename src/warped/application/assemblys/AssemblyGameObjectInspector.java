/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.awt.Color;

import warped.application.entities.WarpedEntitie;
import warped.application.gui.GUIButton;
import warped.application.gui.GUILabel;
import warped.application.gui.GUIObjectIcon;
import warped.application.gui.GUIValue;
import warped.application.gui.WarpedGUI;
import warped.application.gui.textBox.GUITextBoxLined;
import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.application.tile.WarpedTile;
import warped.audio.FrameworkAudio;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.math.vectors.Vec2d;
import warped.utilities.utils.Console;

public class AssemblyGameObjectInspector extends WarpedAssembly {

	private enum ObjectInspectorType {
		OBJECT,
		PLANET,
		TILE,
		ENTITIE,
		GALAXY,
		STELLAR,
	}
	
	ObjectInspectorType type = ObjectInspectorType.OBJECT;
	
	private WarpedObject object;
	
	private Vec2d closeButtonOffset   = new Vec2d(370, 0);
	private Vec2d iconOffset          = new Vec2d(305, 35);
	private Vec2d titleLabelOffset    = new Vec2d( 10, 8);
	private Vec2d positionValueOffset = new Vec2d( 10, 38);
	private Vec2d objectTextOffset    = new Vec2d(  0, 60);
	
	private GUIButton titleButton   = new GUIButton("GameObject Inspector");
	private GUIButton closeButton   = new GUIButton(FrameworkSprites.standardIcons.getSprite(2, 4));
	
	private GUIObjectIcon   icon   	    = new GUIObjectIcon( new Vec2d(iconOffset.x, iconOffset.y), 90);
	private GUIValue  positionValue	= new GUIValue(new Vec2d(positionValueOffset.x, positionValueOffset.y), new Vec2d(300,30));
	private GUILabel  titleLabel    = new GUILabel("Default");
	private GUITextBoxLined objectText 	= new GUITextBoxLined((int)titleButton.getSize().x, 250, new Color(84,84,84,220));
		
	
	public AssemblyGameObjectInspector(WarpedManagerType type) {
		super(type);
	}
		
	public boolean select(WarpedObjectIdentity objectID) {
		if(this.object == WarpedState.getGameObject(objectID)) {
			this.object = null;
			return true;
		}
		else {
			this.object = WarpedState.getGameObject(objectID);
			type = ObjectInspectorType.OBJECT;
			if(object instanceof WarpedEntitie)  type = ObjectInspectorType.ENTITIE;
			if(object instanceof WarpedTile) 		  	 type = ObjectInspectorType.TILE;
			//if(object instanceof CelestialPlanet)  type = ObjectInspectorType.PLANET; 
			//if(object instanceof CelestialStellar) type = ObjectInspectorType.STELLAR;
		//	if(object instanceof CelestailGalaxy)  type = ObjectInspectorType.GALAXY;
			
			Console.err("ObjectInspector -> select() -> object Type : " + type);
			objectText.clearText();
			icon.setTarget(object);
			
			switch(type) {
			case OBJECT:
				titleLabel.setText("Object :  " + object.getClass().getSimpleName());
				objectText.putTextLine(0, "Name :  " + object.getName());
				objectText.putTextLine(1, "Size :  " + object.getSize().getString());
				objectText.putTextLine(2, "Position :  " + object.getPosition().getString());
				objectText.updateTextBox();
				
				
				break;
				
			case ENTITIE:
				
				titleLabel.setText("Object :  " + object.getClass().getSimpleName());
				objectText.putTextLine(0, "Size :  " + object.getSize().getString());
				objectText.updateTextBox();
	
				break;
				
			case TILE:
				WarpedTile<?> tile = (WarpedTile<?>) object;
				
				titleLabel.setText("Tile  :  " + tile.getPrimaryType().getString());
				objectText.putTextLine(0, "Size : " + tile.getSize().getString());
				objectText.putTextLine(1, "Tile Type    :  " + tile.getPrimaryType());
				objectText.updateTextBox();
				

				break;
		/*		
			case PLANET:
				CelestialPlanet planet = (CelestialPlanet) object;
				
				titleLabel.setText("Planet  :  " +  UtilsString.convertEnumToString(planet.getPlanetType()) + "  :  " + planet.getName());
				objectText.putTextLine(0, "Size :  " + planet.getSize().getString());
				objectText.putTextLine(1, "Atmospheric Composition", Color.YELLOW);
				objectText.putTextLine(2, "Primary    :  " + planet.getPrimaryAtmosphericComponenet().getString() + "  " + UtilsMath.round(planet.getPrimaryAtmosphericConcentration() * 100,2) + "%");
				objectText.putTextLine(3, "Secondary  :  " + planet.getSecondaryAtmosphericComponenet().getString() + "  " + UtilsMath.round(planet.getSecondaryAtmosphericConcentration() * 100,2) + "%");
				objectText.putTextLine(4, "Tertiary   :  " + planet.getTertiaryAtmosphericComponenet().getString() + "  " + UtilsMath.round(planet.getTertiaryAtmosphericConcentration() * 100,2) + "%");
				objectText.putTextLine(5, "Trace elements :  "  + UtilsMath.round(planet.getTraceAtmosphericConcentration() * 100,2) + "%");
				objectText.updateTextBox();
			
				break;
				
			case GALAXY:
				CelestailGalaxy g = (CelestailGalaxy) object;
				titleLabel.setText("Gallactic Core");
				objectText.putTextLine(0, "Stellar Mass    :  " + g.getStellarMass() + " M☉");
				objectText.putTextLine(1, "Surface Gravity :  " + g.getAverageSurfaceGravity() + " m/s²");
				objectText.updateTextBox();
				break;
				
			case STELLAR:
				CelestialStellar s = (CelestialStellar) object;
				titleLabel.setText(s.getStellarType().getString());
				objectText.putTextLine(0, "Name :  " + object.getName());
				objectText.putTextLine(1, "Stellar Mass    :  " + s.getStellarMass() + " M☉");
				objectText.putTextLine(2, "Surface Gravity :  " + s.getAverageSurfaceGravity() + " m/s²");
				objectText.updateTextBox();
				break;
			default:
				break;
				
		 */
			default:
				break;
			}
			positionValue.setValue(object.getPosition());
			
			
			
			return false;
		}
	}
	
	
	protected void offsetAssembly() {
		closeButton.setPositionOffset(closeButtonOffset);
		
		titleLabel.setPositionOffset(titleLabelOffset);
		positionValue.setPositionOffset(positionValueOffset);
		objectText.setPositionOffset(objectTextOffset);
		icon.setPositionOffset(iconOffset);
		
		objectText.offsetPosition();
		titleLabel.offsetPosition();
		
	}

	
	protected void defineAssembly() {
		titleButton.draggable();
		titleButton.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {
				WarpedGUI element = (WarpedGUI)i;
				element.setPosition(titleButton.getPosition().x, titleButton.getPosition().y);
				element.offsetPosition();
			});			
		});
		
		closeButton.setReleasedAction(() -> {close();});
		closeButton.setReleasedSFX(FrameworkAudio.defaultClose);
		
	
		titleLabel.ateractive();
		
		positionValue.setPreText("Position : ");
		positionValue.ateractive();
		
		objectText.setTextOffset(9, 18);
		objectText.setLineSpacing(3);
		objectText.ateractive();
	}
	

	protected void addAssembly() {
		addMember(titleButton);
		addMember(icon);
		addMember(closeButton);
		addMember(titleLabel);
		addMember(positionValue);
		addMember(objectText);	
		
	}

	@Override
	protected void updateAssembly() {return;}
	
}
