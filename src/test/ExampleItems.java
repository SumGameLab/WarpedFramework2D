package test;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import test.ExampleApplication.TestImage;
import warped.application.entities.item.ItemBindable;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.utilities.utils.Console;

public enum ExampleItems implements ItemBindable<ExampleItems>{
	POTION,
	FOOD,
	ROCK,
	;

	public static Map<Integer, ExampleItems> map = new HashMap<>();
	static {for(ExampleItems tile : ExampleItems.values()) map.put(tile.ordinal(), tile);}

	@Override
	public Map<Integer, ExampleItems> getMap() {return map;}

	@Override
	public BufferedImage getRaster(ExampleItems itemType) {
		switch(itemType) {
		case FOOD:   return ExampleApplication.testImage.getImage(TestImage.FOOD);	
		case POTION: return ExampleApplication.testImage.getImage(TestImage.POTION);		
		case ROCK:	 return ExampleApplication.testImage.getImage(TestImage.ROCK);	
		default:
			Console.err("ExampleApplication -> getRaster() -> invalid case : " + itemType);
			return FrameworkSprites.error;			
		}
	}

	@Override
	public String getDescription(ExampleItems itemType) {
		switch(itemType) {
		case FOOD: 	 return "Yummy food"; 
		case POTION: return "Health potion";
		case ROCK:	 return "Heavy rock";
		default:
			Console.err("ExampleApplication -> getRaster() -> invalid case : " + itemType);
			return "Error!";			
		}
	}

	@Override
	public int getValue(ExampleItems itemType) {
		switch(itemType) {
		case FOOD: 	 return 4;
		case POTION: return 6;
		case ROCK:	 return 3;
		default:
			Console.err("ExampleApplication -> getValue() -> invalid case : " + itemType);
			return -1;		
		}
	}

	@Override
	public double getMass(ExampleItems itemType) {
		switch(itemType) {
		case FOOD: 	 return 1.0;
		case POTION: return 0.5;
		case ROCK:	 return 3.0;
		default:
			Console.err("ExampleApplication -> getRaster() -> invalid case : " + itemType);
			return 4.0;			
		}
	}

	@Override
	public boolean isDropable(ExampleItems itemType) {
		switch(itemType) {
		case FOOD: 	 return true;
		case POTION: return false;
		case ROCK:	 return true;
		default:
			Console.err("ExampleApplication -> isDropable() -> invalid case : " + itemType);
			return false;			
		}
		
	}
}
