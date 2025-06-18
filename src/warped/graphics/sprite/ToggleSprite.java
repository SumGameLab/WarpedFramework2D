package warped.graphics.sprite;

import java.awt.image.BufferedImage;

import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class ToggleSprite extends WarpedSprite {
	
	private BufferedImage toggleOnRaster;
	private BufferedImage toggleOffRaster;
	private BufferedImage hoveredOnRaster;
	private BufferedImage hoveredOffRaster;
	private BufferedImage lockedImage;
	
	/**
	 * */
	public ToggleSprite(BufferedImage toggleOffRaster) {
		super(toggleOffRaster.getWidth(), toggleOffRaster.getHeight());
		this.toggleOffRaster = toggleOffRaster;
		this.toggleOnRaster = UtilsImage.generateTintedClone(toggleOffRaster, 80, Colour.RED);
		
		hoveredOnRaster = UtilsImage.generateTintedClone(toggleOnRaster, 30, Colour.BLACK);
		hoveredOffRaster = UtilsImage.generateTintedClone(toggleOffRaster, 30, Colour.BLACK);
		
		lockedImage = UtilsImage.generateTintedClone(toggleOffRaster, 120, Colour.BLACK);
		
		toggleOff();
	}
	

	public ToggleSprite(BufferedImage toggleOnRaster, BufferedImage toggleOffRaster) {
		super(toggleOnRaster.getWidth(), toggleOnRaster.getHeight());
		if(toggleOnRaster.getWidth() != toggleOffRaster.getWidth() || toggleOnRaster.getHeight() != toggleOffRaster.getHeight()) {
			Console.err("ToggleSprite -> ToggleSprite() -> toggle rasters are inconsistent sizes, this could cause clipping of the toggle sprite or other issues" );
			return;
		}
		this.toggleOnRaster = toggleOnRaster;
		this.toggleOffRaster = toggleOffRaster;
		
		hoveredOnRaster = UtilsImage.generateTintedClone(toggleOnRaster, 30, Colour.BLACK);
		hoveredOffRaster = UtilsImage.generateTintedClone(toggleOffRaster, 30, Colour.BLACK);
		
		lockedImage = UtilsImage.generateTintedClone(toggleOffRaster, 120, Colour.BLACK);
		
		toggleOff();
	}
	
	public ToggleSprite(BufferedImage toggleOnRaster, BufferedImage toggleOffRaster, BufferedImage hoveredOnRaster, BufferedImage hoveredOffRaster, BufferedImage lockedRaster) {
		super(toggleOnRaster.getWidth(), toggleOnRaster.getHeight());
		if(toggleOnRaster.getWidth() != toggleOffRaster.getWidth() || toggleOnRaster.getHeight() != toggleOffRaster.getHeight() || toggleOnRaster.getHeight() != hoveredOnRaster.getHeight() || toggleOnRaster.getHeight() != hoveredOffRaster.getHeight() || toggleOnRaster.getHeight() != lockedRaster.getHeight()) { 
			Console.err("ToggleSprite -> ToggleSprite() -> toggle rasters are inconsistent sizes, this could cause clipping of the toggle sprite or other issues" );
			return;
		}
		this.toggleOnRaster = toggleOnRaster;
		this.toggleOffRaster = toggleOffRaster;
		this.hoveredOnRaster  = hoveredOnRaster;
		this.hoveredOffRaster = hoveredOffRaster;
		this.lockedImage 	 = lockedRaster;
		
		toggleOff();
	}
	
	/**Set the toggle state to on.
	 * @author SomeKid*/
	public void toggleOn() {setRasterFast(toggleOnRaster);}

	/**Set the toggle state to off.
	 * @author SomeKid*/
	public void toggleOff() {setRasterFast(toggleOffRaster);}
	
	/**Set the toggle state to hovered on.
	 * @author SomeKid*/
	public void hoveredOn() {setRasterFast(hoveredOnRaster);}
	
	/**Set the toggle state to hovered off.
	 * @author SomeKid*/
	public void hoveredOff() {setRasterFast(hoveredOffRaster);}
	
	/**Set the toggle state to locked
	 * @author SomeKid*/
	public void locked() {setRasterFast(lockedImage);}
	
	/**Get the image set for the toggleOn state
	 * @return BufferedImage - the image set to the toggledOn state
	 * @apiNote If you want to set this image to the sprite just call toggleOn();
	 * @author SomeKid*/
	public BufferedImage getOnRaster() {return toggleOnRaster;}
	
	/**Get the image set for the toggleOff state
	 * @return BufferedImage - the image set to the toggleOff state
	 * @apiNote If you want to set this image to the sprite just call toggleOff();
	 * @author SomeKid*/
	public BufferedImage getOffRaster() {return toggleOffRaster;}
	
	/**Get the image set for the toggleOff hovered state
	 * @return BufferedImage - the image set to the toggledOff hovered state
	 * @apiNote If you want to set this image to the sprite just call hoveredOff();
	 * @author SomeKid*/
	public BufferedImage getOffHoveredRaster() {return hoveredOffRaster;}
	
	/**Get the image set for the toggleOn hovered state
	 * @return BufferedImage - the image set to the toggledOn hovered state
	 * @apiNote If you want to set this image to the sprite just call toggleOn();
	 * @author SomeKid*/
	public BufferedImage getOnHoveredRaster() {return hoveredOnRaster;}
	
}
