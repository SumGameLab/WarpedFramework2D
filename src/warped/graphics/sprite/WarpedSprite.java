/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.graphics.sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.functionalInterfaces.WarpedAction;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsImage;

public class WarpedSprite {

	/**Definitions
	 * BackBuffer - refers to the which ever is the current inactive raster in the rasterBuffer[]
	 * FrontBuffer - refers to the which ever is the current active raster in the rasterBuffer[]
	 * */
	
	protected BufferedImage[] rasterBuffer = new BufferedImage[2];
	private int bufferIndex = 0;
	private BufferedImage raster; //Graphic output - points to the active raster in the rasterBuffer
	private BufferedImage buffer; //Graphic input - points to the inactive raster in the rasterBuffer -> safe to edit 
	
	private VectorI size = new VectorI(1, 1);
	
	protected static RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	static {
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		rh.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);		
		rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
	}
	
	private WarpedAction deltaAction = () -> {return;};
	
	public WarpedSprite(int width, int height) {
		if(width < 1) {
			Console.err("WarpedSprite -> WarpedSprite() -> width is too small : " + width + ", it will be set to 1");
			width = 1;
		}
		if(height < 1) {
			Console.err("WarpedSprite -> WarpedSprite() -> height is too small : " + height + ", it will be set to 1");
			height = 1;
		}
		setSize(width, height);
	}
	
	/**The graphic output for this sprite
	 * @return raster - The sprite image
	 * 				  - This is strictly the output and should not be edited
	 * 				  - To edit the graphics for a warpedSprite you should call getGraphics() or getGraphics2D()
	 * 				  - An alternative to edit the graphics is use on of the various paint() functions
	 * @author SomeKid*/
	public BufferedImage raster() {return raster;}	
	
	/**Set the raster without adjusting for size.
	 * @param raster - the new raster to point to.
	 * @apiNote  - Will trigger the delta action once after the graphics output has changed.
	 * @apiNote  - This method is mainly used for animating a series of frames that are all the same size.
	 * @apiNote  - Use setRasterSized() if the rasters being used are of varying size.
	 * @implNote - This method assumes that all images will be the same size. Graphically it will still work with different sized images but mouse events will not work correctly.
	 * @implNote - Using paint() and pushGraphics() subsequent to setRasterPointer() will reset the raster pointer to the sprites buffer.
	 * @author 5som3*/
	public void setRasterFast(BufferedImage raster) {
		this.raster = raster;
		deltaAction.action();
	}
	
	/**Set the graphic output of the sprite new image. 
	 * @param raster - the new raster to point to. The Sprite size will be adjusted to fit the size of the new raster.
	 * @apiNote  - Will trigger the delta action once after the graphics output has changed.
	 * @apiNote - paint() and pushGraphics() are not compatible with setRasterPointer(). 
	 * @implNote - Will set the size to match the WarpedSprite.
	 * @implNote - Using paint() and pushGraphics() subsequent to setRasterPointer() will reset the raster pointer to the sprites buffer.
	 * @author SomeKid*/
	public void setRasterSized(BufferedImage raster) {
		size.set(raster.getWidth(), raster.getHeight());
		this.raster = raster;
		deltaAction.action();
	}
	
	
	/**Any action set here will trigger (once) every time the raster changes.
	 * @param WarpedAction - any action set here will trigger when ever this objects raster changes. 
	 * @author 5som3*/
	public final void setDeltaAction(WarpedAction deltaAction) {this.deltaAction = deltaAction;}
	
	/**Clears any action that triggers when this raster changes
	 * @author 5som3*/
	public final void clearDeltaAction() {deltaAction = () -> {return;};}
	
	
	/**Edit the Graphics for this sprite with more advance features (slower).
	 * @return Graphics2D - editable graphics2D context for the back buffer in the rasterBuffer.
	 * @apiNote - remember to call dispose() on the graphics object when fished editing.
	 * @apiNote - remember to call pushGraphics() after disposing to make the changes visible.	  
	 * @author SomeKid*/
	public final Graphics2D getGraphics() {
		Graphics2D g2d = buffer.createGraphics();
		g2d.setComposite(UtilsImage.clearComposite);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setComposite(UtilsImage.drawComposite);
		g2d.setRenderingHints(rh);
		return g2d;
		
	}
	
	/**This will make any changes made to the graphics visible.
	 * @apiNote This should only be called after dispose() has been called on the graphics context.
	 * @apiNote This will trigger the delta action once after the graphics output has changed.
	 * @author SomeKid*/
	public final void pushGraphics() {
		if(bufferIndex == 0) {
			bufferIndex = 1;
			buffer = rasterBuffer[1];
			raster = rasterBuffer[0];
		} else {
			bufferIndex = 0;
			buffer = rasterBuffer[0];
			raster = rasterBuffer[1];
		}
		deltaAction.action();
	}
	
	/**The current BackBuffer in the rasterBuffer array.
	 * @apiNote This is very case specific and shouldn't normally need to be used.
	 * @return BufferedImage - BackBuffer.
	 * @author SomeKid*/
	protected final BufferedImage getBackBuffer() {return rasterBuffer[bufferIndex];}
	
	/**Swaps the buffers without pushing any changes.
	 * @apiNote This is very case specific and should'nt normally need to be used.
	 * @author SomeKid*/
	protected final void swapBuffers() {if(bufferIndex == 0) bufferIndex = 1; else bufferIndex = 0;}
	
	/**The size of the BufferedImages in the rasterBuffer
	 * @return VectorI - size
	 * @author SomeKid*/
	public VectorI getSize() {return size;}
	
	/**The width of the BufferedImages in the rasterBuffer
	 * @return int - width
	 * @author SomeKid*/
	public int getWidth() {return size.x();}
	
	/**The height of the BuffferdImages in the rasterBuffer
	 * @return int - height
	 * @author SomeKid*/
	public int getHeight() {return size.y();}
	
	/**Set the size of the sprite
	 * This will clear the rasterBuffer so any graphical data will be lost and need to be painted again
	 * If you want to change the size of a gameObject at runtime you should change the renderScale in the gameObject
	 * @param width  - the new width for the sprite
	 * @param height - the new height for the sprite
	 * @author 5som3*/
	public void setSize(int width, int height) {
		size.set(width, height);
		resizeRasterBuffer();		
	}
	
	/**Set the size of the sprite
	 * This will clear the rasterBuffer so any graphical data will be lost and need to be painted again
	 * If you want to change the size of a gameObject at runtime you should change the renderScale in the gameObject
	 * @param width  - the new width for the sprite
	 * @param height - the new height for the sprite
	 * */
	public void setSize(VectorI size) {
		this.size.set(size);
		resizeRasterBuffer();		
	}
	
	/**Generates new buffered images the size of the sprite to replace the old rasterBuffer images. Any old image data will be deleted.
	 * @author SomeKid*/
	protected void resizeRasterBuffer() {
		rasterBuffer[0] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		rasterBuffer[1] = new BufferedImage(size.x(), size.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		pushGraphics();
	}
	
	/**Makes the BackBuffer completely transparent
	 * @author SomeKid*/
	public void clearBackBuffer() {
		Graphics2D g2d = getGraphics();
		g2d.setComposite(UtilsImage.clearComposite);
		g2d.fillRect(0, 0, size.x(), size.y());
		g2d.dispose();		
	}
	
	/**Makes the BackBuffer and FrontBuffer transparent 
	 * @author SomeKid*/
	public void clearBuffers() {
		clearBackBuffer();
		pushGraphics();
		clearBackBuffer();
	}
	
	/**Paint over the sprite with the input image
	 * @param image - the image to be drawn over the sprite
	 * 				- the input image will be scaled to fit the current size of the sprite
	 * @author SomeKid*/
	public final void paint(BufferedImage image) {
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, (int)size.x(), (int)size.y(), null);
		g.dispose();
		pushGraphics();;
	}
	
	/**Paint over the sprite with the input image
	 * @param image - the image to be drawn over the sprite
	 * @param scale - if true the sprite will be resized to the image param size else the image will be painted at the current sprite size.
	 * @author SomeKid*/
	public final void paint(BufferedImage image, boolean scale) {
		if(scale) setSize(image.getWidth(), image.getHeight());
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, (int)size.x(), (int)size.y(), null);
		g.dispose();
		pushGraphics();
	}
	
	
	/**Paint over the sprite with the input image
	 * @param image - the image to be drawn over the sprite.
	 * @param width - the width for this sprite in pixels (image will be scaled to fit).
	 * @param height - the height for this sprite in pixels (image will be scaled to fit).
	 * @author SomeKid*/
	public final void paint(BufferedImage image, int width, int height) {
		setSize(width, height);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint over the sprite front and back buffer with the input image
	 * @param image - the image to be drawn over the sprite.
	 * @param width - the width for this sprite in pixels (image will be scaled to fit).
	 * @param height - the height for this sprite in pixels (image will be scaled to fit).
	 * @author SomeKid*/
	public final void paintBuffers(BufferedImage image, int width, int height) {
		setSize(width, height);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		pushGraphics();
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, width, height, null);
		g2.dispose();
		pushGraphics();
	}
	
	

	/**Paint over the sprite with the input image
	 * @param image 		- the image to be drawn over the sprite ( will be scaled to fit the specified width and height).
	 * @param x - x offset measured in pixels
	 * @param y - y offset measured in pixels  
	 * @param width- the width for this sprite in pixels.
	 * @param height - the height for this sprite in pixels.
	 * @author SomeKid*/ 
	public final void paint(BufferedImage image, int x, int y, int width, int height) {
		setSize(width, height);
		Graphics g = getGraphics();
		g.drawImage(image, x, y, width, height, null);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint over the sprite with the input color
	 * @param color 		- the color to fill the sprite with 
	 * @author SomeKid*/
	public void paint(Color color) {
		Graphics g = getGraphics();
		g.setColor(color);
		g.fillRect(bufferIndex, bufferIndex, bufferIndex, bufferIndex);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint a rectangle over the sprite with the specified parameters
	 * @param color 		- the color to fill the sprite with
	 * @param x, y  		- the offset for the image 
	 * @param width, height - the size of hte area to fill with the specified color. 
	 * @author SomeKid*/
	public void paint(Color color, int x, int y, int width, int height) {
		Graphics g = getGraphics();
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint text over the sprite
	 * @param text 		- the text to paint over the sprite 
	 * @author SomeKid*/
	public void paint(String text) {
		Graphics g = getGraphics();
		g.drawString(text, 0, 0);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint text over the sprite
	 * @param text 		- the text to paint over the sprite 
	 * @param x, y 		- the text offset relative to the top left corner of the sprite
	 * @author SomeKid*/
	public void paint(String text, int x, int y) {
		Graphics g = getGraphics();
		g.drawString(text, x, y);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint text over the sprite
	 * @param text 		- the text to paint over the sprite 
	 * @param x, y 		- the text offset relative to the top left corner of the sprite
	 * @param colour 	- the text colour
	 * @author SomeKid*/
	public void paint(String text, int x, int y, Colour colour) {
		Graphics g = getGraphics();
		g.setColor(colour.getColor());
		g.drawString(text, x, y);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint text over the sprite
	 * @param text 		- the text to paint over the sprite 
	 * @param x, y 		- the text offset relative to the top left corner of the sprite
	 * @param Font		- the text font
	 * @author SomeKid*/
	public void paint(String text, int x, int y, Font font) {
		Graphics g = getGraphics();
		g.setFont(font);
		g.drawString(text, x, y);
		g.dispose();
		pushGraphics();
	}
	
	/**Paint text over the sprite
	 * @param text 		- the text to paint over the sprite 
	 * @param x, y 		- the text offset relative to the top left corner of the sprite
	 * @param colour 	- the text colour
	 * @param Font		- the text font
	 * @author SomeKid*/
	public void paint(String text, int x, int y, Colour colour, Font font) {
		Graphics g = getGraphics();
		g.setColor(colour.getColor());
		g.setFont(font);
		g.drawString(text, x, y);
		g.dispose();
		pushGraphics();
	}
		
}
