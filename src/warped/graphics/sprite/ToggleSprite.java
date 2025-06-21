package warped.graphics.sprite;

import java.awt.image.BufferedImage;

import warped.utilities.enums.generalised.Colour;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class ToggleSprite extends WarpedSprite {
	
	private BufferedImage onRaster;   		// The raster when the toggle is in the on state but not hovered.
	private BufferedImage onHoveredRaster;  // The raster when the toggle is in the on state and hovered.
	private BufferedImage offRaster;  		// The raster when the toggle is in the off state but not hovered.
	private BufferedImage offHoveredRaster; // The raster when the toggle is in the off state and hovered.
	private BufferedImage lockedImage;	    // The raster when the toggle is locked and unable to be changed. 
	
	/**
	 * */
	public ToggleSprite(BufferedImage offRaster) {
		super(offRaster.getWidth(), offRaster.getHeight());
		this.offRaster   = offRaster;
		this.onRaster    = UtilsImage.generateTintedClone(offRaster, 80, Colour.RED);
		
		onHoveredRaster  = UtilsImage.generateTintedClone(onRaster, 30, Colour.BLACK);
		offHoveredRaster = UtilsImage.generateTintedClone(offRaster, 30, Colour.BLACK);
		
		lockedImage 	 = UtilsImage.generateTintedClone(offRaster, 120, Colour.BLACK);
		
		toggleOff();
	}
	

	public ToggleSprite(BufferedImage onRaster, BufferedImage offRaster) {
		super(onRaster.getWidth(), onRaster.getHeight());
		if(onRaster.getWidth() != offRaster.getWidth() || onRaster.getHeight() != offRaster.getHeight()) {
			Console.err("ToggleSprite -> ToggleSprite() -> toggle rasters are inconsistent sizes, this could cause clipping of the toggle sprite or other issues" );
			return;
		}
		this.onRaster    = onRaster;
		this.offRaster   = offRaster;
		
		onHoveredRaster  = UtilsImage.generateTintedClone(onRaster, 30, Colour.BLACK);
		offHoveredRaster = UtilsImage.generateTintedClone(offRaster, 30, Colour.BLACK);
		
		lockedImage 	 = UtilsImage.generateTintedClone(offRaster, 120, Colour.BLACK);
		
		toggleOff();
	}
	
	public ToggleSprite(BufferedImage onRaster, BufferedImage offRaster, BufferedImage onHoveredRaster, BufferedImage offHoveredRaster, BufferedImage lockedRaster) {
		super(onRaster.getWidth(), onRaster.getHeight());
		if(onRaster.getWidth() != offRaster.getWidth() || onRaster.getHeight() != offRaster.getHeight() || onRaster.getHeight() != onHoveredRaster.getHeight() || onRaster.getHeight() != offHoveredRaster.getHeight() || onRaster.getHeight() != lockedRaster.getHeight()) { 
			Console.err("ToggleSprite -> ToggleSprite() -> toggle rasters are inconsistent sizes, this could cause clipping of the toggle sprite or other issues" );
			return;
		}
		this.onRaster  		  = onRaster;
		this.offRaster 		  = offRaster;
		this.onHoveredRaster  = onHoveredRaster;
		this.offHoveredRaster = offHoveredRaster;
		this.lockedImage 	  = lockedRaster;
		
		toggleOff();
	}
	
	/**Set the toggle state to on.
	 * @author SomeKid*/
	public void toggleOn() {setRasterFast(onRaster);}

	/**Set the toggle state to off.
	 * @author SomeKid*/
	public void toggleOff() {setRasterFast(offRaster);}
	
	/**Set the toggle state to hovered on.
	 * @author SomeKid*/
	public void hoveredOn() {setRasterFast(onHoveredRaster);}
	
	/**Set the toggle state to hovered off.
	 * @author SomeKid*/
	public void hoveredOff() {setRasterFast(offHoveredRaster);}
	
	/**Set the toggle state to locked
	 * @author SomeKid*/
	public void locked() {setRasterFast(lockedImage);}
	
	/**Get the image set for the toggleOn state
	 * @return BufferedImage - the image set to the toggledOn state
	 * @apiNote If you want to set this image to the sprite just call toggleOn();
	 * @author SomeKid*/
	public BufferedImage getOnRaster() {return onRaster;}
	
	/**Get the image set for the toggleOff state
	 * @return BufferedImage - the image set to the toggleOff state
	 * @apiNote If you want to set this image to the sprite just call toggleOff();
	 * @author SomeKid*/
	public BufferedImage getOffRaster() {return offRaster;}
	
	/**Get the image set for the toggleOff hovered state
	 * @return BufferedImage - the image set to the toggledOff hovered state
	 * @apiNote If you want to set this image to the sprite just call hoveredOff();
	 * @author SomeKid*/
	public BufferedImage getOffHoveredRaster() {return offHoveredRaster;}
	
	/**Get the image set for the toggleOn hovered state
	 * @return BufferedImage - the image set to the toggledOn hovered state
	 * @apiNote If you want to set this image to the sprite just call toggleOn();
	 * @author SomeKid*/
	public BufferedImage getOnHoveredRaster() {return onHoveredRaster;}
	
}
