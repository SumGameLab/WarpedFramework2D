/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIProgressBar extends WarpedGUI {

	/*GUIProgressBar provides these functions
	 * 		- display a percentage of some value as a graphical bar
	 * 		- set the colour of the bar
	 * 		- display text in front of the bar
	 */
	
	private DirectionType fillDirection = DirectionType.RIGHT;
	
	private String label		 = "";
	private VectorI textOffset = new VectorI(5, 5);
	private Colour textColor  = Colour.YELLOW_LIGHT;
	private Font  textFont 	 = UtilsFont.getPreferred();
	
	private double progress		= 0.0;
	private int borderThickness = 3;
	
	private boolean isProgressTextVisible = false;
	
	private AffineTransform at = new AffineTransform();
	
	private Color emptyColor  = Color.DARK_GRAY;
	private Color fullColor   = Colour.GREEN_DARK.getColor();
	private Color borderColor = Color.BLACK;
	
	public GUIProgressBar() {
		setInteractive(false);
		setSize(180, 30);
		at.rotate(UtilsMath.PI_ON_TWO);
		updateGraphics();
	}
	
	/**A progress bar with the specified parameters.
	 * @param label - the text to display in the progress bar.
	 * @author 5som3*/
	public GUIProgressBar(String label) {
		setInteractive(false);
		setSize(180, 30);
		this.label = label;
		at.rotate(UtilsMath.PI_ON_TWO);
		updateGraphics();
	}
	
	/**A progress bar with the specified parameters.
	 * @param width - the width of the progress bar in pixels.
	 * @param height- the height of the progress bar in pixels.
	 * @author 5som3*/
	public GUIProgressBar(int width, int height) {
		setInteractive(false);
		setSize(width, height);
		at.rotate(UtilsMath.PI_ON_TWO);
		updateGraphics();
	}
	
	/**A progress bar with the specified parameters.
	 * @param width - the width of the progress bar in pixels.
	 * @param height- the height of the progress bar in pixels.
	 * @param fullColor - the color that progress will be shown as.
	 * @author 5som3*/
	public GUIProgressBar(int width, int height, Color fullColor) {
		setInteractive(false);
		setSize(width, height);
		at.rotate(UtilsMath.PI_ON_TWO);
		this.fullColor = fullColor;
		updateGraphics();
	}
	
	/**A progress bar with the specified parameters.
	 * @param width - the width of the progress bar in pixels.
	 * @param height- the height of the progress bar in pixels.
	 * @param fullColor - the color that progress will be shown as.
	 * @param fillDirection - the direction that the fullColor will fill in. (i.e. left is increasing progress will be shown as progress color moving from right to left.
	 * @author 5som3*/
	public GUIProgressBar(int width, int height, Color fullColor, DirectionType fillDirection) {
		setInteractive(false);
		setSize(width, height);
		at.rotate(UtilsMath.PI_ON_TWO);
		this.fullColor = fullColor;
		setFillDirection(fillDirection);
		updateGraphics();
	}
		
	/**Set if the progress should be displayed as text
	 * */
	public void setProgressTextVisible(boolean isProgressTextVisible) {
		if(this.isProgressTextVisible != isProgressTextVisible) {
			this.isProgressTextVisible = isProgressTextVisible;
			updateGraphics();
		}
	}
	
	/**Text is displayed in front of the progress bar.
	 * @author SomeKid*/
	public void setLabel(String text) {
		if(text == "" || text == null) {
			Console.err("GUIProgressBar -> setText() -> tried to set the text with null text");
			return;
		}
		this.label = text;
		updateGraphics();
	}
	
	/**Remove any text that was displayed over the progress bar.
	 * @author SomeKid*/
	public void clearText() {
		label = null;
		updateGraphics();
	}

	/**Offset the progress bar text measured from the top-left corner
	 * @author SomeKid*/
	public void setTextOffset(VectorI textOffset) {
		this.textOffset = textOffset;
		updateGraphics();
	}
	
	/**The size of the text displayed over the progress bar
	 * @param textSize - must be larger than 0;
	 * @author SomeKid*/
	public void setTextSize(int textSize) {
		textSize = UtilsMath.clampMin(textSize, 0);
		textFont = new Font(textFont.getFontName(), textFont.getStyle(), textSize);
		updateGraphics();
	}
	
	/**The style of the text displayed over the progress bar
	 * @param textStyle - 0 Plain
	 * 					- 1 Bold
	 * 					- 2 Italic
	 * @author SomeKid*/
	public void setTextStyle(int textStyle) {
		if(textStyle < 0 || textStyle > 2) {
			Console.err("GUIProgressBar -> setTextStyle() -> invalid style : " + textStyle);
			textStyle = 0;
		}
		textFont = new Font(textFont.getFontName(), textStyle, textFont.getSize());
		updateGraphics();
	}
	
	/**The colour of the text displayed over the progress bar
	 * @param Colour 	- the text color
	 * @author SomeKid */
	public void setTextColor(Colour textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	/**The direction of "flow / fill" for the progress bar 
	 * 		i.e. for fillDirection.RIGHT if the progress bar started at 0.0% and progressed to 100 % 
	 * 			 the user would see the fill colour start to appear on the left side and progress across to the right
	 * @param fillDirection - the direction of fill as progress increases 
	 * 						- INVALID CASES : UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT,
	 * @author SomeKid*/	
	public void setFillDirection(DirectionType fillDirection) {
		if(fillDirection == DirectionType.UP_LEFT   || fillDirection == DirectionType.UP_RIGHT  || fillDirection == DirectionType.DOWN_LEFT || fillDirection == DirectionType.DOWN_RIGHT) {
		   Console.err("GUIProgressBar -> setFillDirection() -> tried to set fill to an invalid direction. ONLY use single directions i.e. up, down, left, right not : " + fillDirection);
		   return;
		} else this.fillDirection = fillDirection;
	}
	
	/**The thickness of the borderColor that surrounds the bar in pixels (px).
	 * @param borderThickness - thickness must be greater than -1px and less than 1/4 the (width or height) which ever is less. 
	 * 						  - set thickness to 0px for no border.
	 * @author SomeKid */
	public void setBorderThickness(int borderThickness) {
		if(sprite.getWidth() < sprite.getHeight()) borderThickness = UtilsMath.clamp(borderThickness, 0, sprite.getWidth() / 4);
		else borderThickness = UtilsMath.clamp(borderThickness, 0, sprite.getHeight() / 4);
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	/**Sets the progress to 0.0
	 *@author SomeKid*/
	public void resetProgress() {
		progress = 0.0;
		updateGraphics();
	}
	
	/**Set the percentage of the bar covered with the fillColour.
	 * @param progress - percentage of bar covered must in the domain 0.0 -> 1.0 (inclusive).
	 * @author SomeKid*/
	public void setProgress(double progress) {
		this.progress = UtilsMath.clamp(progress, 0.0, 1.0);
		updateGraphics();
	}
	
	/**Increase the percentage of the bar covered with the fillColour by 1%.
	 * @author SomeKid*/
	public void increaseProgress() {
		progress += 0.01;
		progress = UtilsMath.clamp(progress, 0.0, 1.0);
		if(progress > 1.0) progress = 1.0;
		updateGraphics();
	}
	
	/**Decrease the percentage of the bar covered with the fillColour by 1%.
	 * @author SomeKid*/
	public void decreaseProgress() {
		progress -= 0.01;
		progress = UtilsMath.clamp(progress, 0.0, 1.0);
		updateGraphics();
	}
	
	
	protected void updateGraphics() {
		Graphics2D g2d = getGraphics();
		
		g2d.setColor(borderColor);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		int x1 = borderThickness;
		int y1 = borderThickness;
		int x2 = (int)(getWidth() - borderThickness * 2);
		int y2 = (int)(getHeight() - borderThickness * 2);
		
		g2d.setColor(emptyColor);
		g2d.fillRect(x1, y1, x2, y2);
		
		if(progress > 0.01) {			
			g2d.setColor(fullColor);
			switch(fillDirection) {
			case DOWN:  g2d.fillRect(x1, y1, x2, (int)(y2 * progress)); break;
			case LEFT:  g2d.fillRect(x2 - (int)(x2 * progress), y1, x2, y2); break;  
			case RIGHT: g2d.fillRect(x1, y1, (int)(x2 * progress), y2); break;
			case UP:    g2d.fillRect(x1, y2 - (int)(y2 * progress), x2, y2); break;
			default:
				Console.err("GUIProgressBar -> updateGraphics() -> invalid case, use only single directions not : " + fillDirection);
				break;
			}
		}
		
		g2d.setColor(textColor.getColor());
		g2d.setFont(textFont);
		if(fillDirection == DirectionType.LEFT || fillDirection == DirectionType.RIGHT) {
			if(isProgressTextVisible) g2d.drawString(label + " " + UtilsMath.getString(progress, 2), textOffset.x(), textFont.getSize()+ textOffset.y());
			else g2d.drawString(label, textOffset.x(), textFont.getSize()+ textOffset.y());
		} else if(fillDirection == DirectionType.UP || fillDirection == DirectionType.DOWN) {
			FontRenderContext frc = g2d.getFontRenderContext();
			Font f = textFont.deriveFont(at);
			TextLayout tString;
			if(isProgressTextVisible) tString = new TextLayout(label + " " + UtilsMath.getString(progress, 2), f, frc);   
			else tString = new TextLayout(label, f, frc);
			tString.draw(g2d, borderThickness * 2, (borderThickness * 2) + textFont.getSize());
		} else { Console.err("GUIProgressBar -> updateGraphics() -> invalid fill dire	ction type "); return;}
		
		g2d.dispose();
		sprite.pushGraphics();
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
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


}
