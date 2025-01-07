/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item;

import java.awt.image.BufferedImage;

import warped.application.entities.item.ammunition.ItemAmmunition;
import warped.application.entities.item.artifact.ItemArtifact;
import warped.application.entities.item.container.ItemContainer;
import warped.application.entities.item.drink.ItemDrink;
import warped.application.entities.item.element.ItemElement;
import warped.application.entities.item.equipment.ItemEquipment;
import warped.application.entities.item.equipment.apparel.EquipmentApparel;
import warped.application.entities.item.equipment.tool.EquipmentTool;
import warped.application.entities.item.food.ItemFood;
import warped.application.entities.item.resource.ItemResource;
import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.object.WarpedOption;
import warped.application.state.WarpedState;
import warped.user.mouse.WarpedMouseEvent;

public abstract class WarpedItem extends WarpedObject {
		
	protected ItemType itemType;
	protected int quantity = 1;
	private double mass = 0.1;
	
	
	public WarpedItem(ItemType itemType, String name, BufferedImage raster) {
		this.itemType = itemType;
		this.name = name;
		setToolTip(name);
		setRaster(raster);
		clearSelectOptions();
		addSelectOption(new WarpedOption("Inspect", () -> {
			WarpedState.itemInspector.selectItem(this);
			WarpedState.itemInspector.open();
		}));
	}
	
	public WarpedItem(ItemType itemType, String name, BufferedImage raster, int quantity) {
		this.itemType = itemType;
		this.name = name;
		this.quantity = quantity;
		setToolTip(name);
		setRaster(raster);
		clearSelectOptions();
		addSelectOption(new WarpedOption("Inspect", () -> {
			WarpedState.itemInspector.selectItem(this);
			WarpedState.itemInspector.open();
		}));
	}
	
	public final void addOne() {
		quantity++;
		updateMass();
	}
	
	public final void zeroQuantity() {
		quantity = 0;
		updateMass();
	}
	
	public abstract double massOfOne();
	public abstract double valueOfOne();
	
	public final void addMultiple(int quantity) {
		this.quantity += quantity;
		updateMass();
	}
	
	/**Note, this should not be used for things like trade / build cost, instead use consumeOne*/
	public final void setQuantity(int quantity) {
		if(quantity < 0) {
			System.err.println("ItemEntitie -> setQuantity -> quantity must be great than 0");
			return;
		}
		this.quantity = quantity;
		updateMass();
	}
	
	public final boolean consumeOne() {
		quantity--;
		if(quantity < 0) {
			quantity = 0;
			updateMass();
			return false;
		} else {
			updateMass();
			return true;
		}
	}
	
	public final boolean consumeMultiple(int quantity) {
		int val = this.quantity - quantity;
		if(val < 0) {
			System.out.println("ItemEntitie -> subtractMultiple() -> item does not have enough quantity to subtract " + quantity);
			return false;
		} else {
			this.quantity -= quantity;
			updateMass();
			return true;
		}
	}
	
	
	public ItemType getItemType() {return itemType;}
	public final int getQuantity() {return quantity;}
	public final String getQuantityString() {return "" + quantity;}

	public final double getMass() {return mass;}
	public final double getValue() {return quantity * valueOfOne();}
	
	public abstract void effect(WarpedObjectIdentity objectID);

	protected final void updateMass() {mass = quantity * massOfOne();}
	
	
	public final boolean equals(WarpedItem item2) {return equals(this, item2);}
	
	public static final boolean equals(WarpedItem item1, WarpedItem item2) {
		if(item1.itemType == item2.itemType) {
			switch(item1.itemType) {
			case ARTIFACT: 
				ItemArtifact artifact1 = (ItemArtifact)item1;
				ItemArtifact artifact2 = (ItemArtifact)item2;
				if(artifact1.getArtifactType() == artifact2.getArtifactType()) return true;
				else return false;
			case DRINK:	
				ItemDrink drink1 = (ItemDrink)item1;
				ItemDrink drink2 = (ItemDrink)item2;
				if(drink1.getDrinkType() == drink2.getDrinkType()) return true;
				else return false;
			case ELEMENT :
				ItemElement element1 = (ItemElement) item1;
				ItemElement element2 = (ItemElement) item2;
				if(element1.getElementType() == element2.getElementType()) return true;
				else return false;				
			case EQUIPMENT:
				ItemEquipment equipment1 = (ItemEquipment)item1;
				ItemEquipment equipment2 = (ItemEquipment)item2;
				switch(equipment1.getEquipmentType()) {
				case APPAREL:
					EquipmentApparel apparell = (EquipmentApparel)equipment1;
					EquipmentApparel apparel2 = (EquipmentApparel)equipment2;
					if(apparell.getApparelType() == apparel2.getApparelType()) return true;
					else return false;
				case TOOL:
					EquipmentTool tool1 = (EquipmentTool)equipment1;
					EquipmentTool tool2 = (EquipmentTool)equipment2;
					if(tool1.getToolType() == tool2.getToolType()) return true;
					else return false;
				default:
					System.err.println("ItemEntitie -> equals() -> equipment case : -> invalid case : " + equipment1.getEquipmentType());
					return false;
				}
			case FOOD:		
				ItemFood food1 = (ItemFood) item1;
				ItemFood food2 = (ItemFood) item2;
				if(food1.getFoodType() == food2.getFoodType()) return true;
				else return false;
			case RESOURCE:
				ItemResource resource1 = (ItemResource) item1;
				ItemResource resource2 = (ItemResource) item2;
				if(resource1.getResourceType() == resource2.getResourceType())return true;
				else return false;
			default: System.err.println("ItemEntitie -> equals() -> invalid item Type : " + item1.itemType); return false;
			}
		} else return false;
	}

	public ItemAmmunition castAmmunition() {return castAmmunition(this);}
	public static ItemAmmunition castAmmunition(WarpedItem item) {
		if(item.getItemType() != ItemType.AMMUNITION) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not ammunition");
			return null;
		} else return (ItemAmmunition) item;
	}
	
	public ItemArtifact castArtifact() {return castArtifact(this);}
	public static ItemArtifact castArtifact(WarpedItem item) {
		if(item.getItemType() != ItemType.ARTIFACT) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not artifact");
			return null;
		} else return (ItemArtifact) item;
	}
	
	public ItemContainer castContainer() {return castContainer(this);}
	public static ItemContainer castContainer(WarpedItem item) {
		if(item.getItemType() != ItemType.CONTAINER) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not container");
			return null;
		} else return (ItemContainer) item;
	}
	
	public ItemDrink castDrink() {return castDrink(this);}
	public static ItemDrink castDrink(WarpedItem item) {
		if(item.getItemType() != ItemType.DRINK) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not drink");
			return null;
		} else return (ItemDrink) item;
	}
	
	public ItemEquipment castEquipment() {return castEquipment(this);}
	public static ItemEquipment castEquipment(WarpedItem item) {
		if(item.getItemType() != ItemType.EQUIPMENT) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not equipment");
			return null;
		} else return (ItemEquipment) item;
	}
	
	public ItemFood castFood() {return castFood(this);}
	public static ItemFood castFood(WarpedItem item) {
		if(item.getItemType() != ItemType.FOOD) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not food");
			return null;
		} else return (ItemFood) item;
	}
	
	public ItemResource castResource() {return castResource(this);}
	public static ItemResource castResource(WarpedItem item) {
		if(item.getItemType() != ItemType.RESOURCE) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not resource");
			return null;
		} else return (ItemResource) item;
	}
	
	public ItemElement castElement() {return castElement(this);}
	public static ItemElement castElement(WarpedItem item) {
		if(item.getItemType() != ItemType.ELEMENT) {
			System.err.println("ItemEntitie -> cast() -> cast exception, item is not element");
			return null;
		} else return (ItemElement) item;
	}
	
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}
	
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateMid() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateSlow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updatePassive() {
		// TODO Auto-generated method stub
		
	}
}
