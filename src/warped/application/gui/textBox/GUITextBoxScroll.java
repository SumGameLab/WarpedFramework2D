/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.textBox;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUITextBoxScroll extends WarpedGUI {

	private Color backgroundColor = Colour.getColor(Colour.GREY_DARK);
	
	private int width = 300;
	private int height = 400;
	
	private HashMap<Integer, Color>  lineColors = new HashMap<>();
	private HashMap<Integer, String> textLines  = new HashMap<>();
	
	private Color defaultLineColor = Color.WHITE;
	private Font font = UtilsFont.getPreferred();
	
	private int lineSpacing   = 2;
	private int lineThickness = 14;
	private Vec2i textOffset  = new Vec2i(5, font.getSize() - (font.getSize() / 8));
	
	
	private Color scrollBarColor = Color.DARK_GRAY;
	private Color scrollButtonColor = Color.LIGHT_GRAY;
	private int scrollBarThickness = 10;
    private int scrollButtonThickness = 400;
    private int scrollButtonPosition = 0;
		
    private int maxLine = 0;
	private int scroll 	 = 0;
	private double scrollProgress = 0.0;
	private int scrollMax  = 0;
	private int scrollMin  = 0;
	private int scrollStep = 8;
	
	public GUITextBoxScroll() {
		
	}

	//--------
	//---------------- Access --------
	//--------
	public void setTextSize(int textSize) {
		font = UtilsFont.getPreferred();
		updateGraphics();
	}
	
	public void setDefaultLineColor(Color defaultLineColor) {this.defaultLineColor = defaultLineColor;}
	public void setTextLines(List<String> textLines, Color color) {
		setDefaultLineColor(color);
		setTextLines(textLines);	
	}
	/**Note : It IS NOT necessary to update the textBox Graphics if you use setTextLines()*/
	public void setTextLines(List<String> textLines) {
		clearText();
		for(int i = 0; i < textLines.size(); i++) {putTextLine(i, textLines.get(i));}
		updateGraphics();
	}
	/**Note : It IS necessary to update the textBox Graphics if you use putTextLine(), use updateGraphics() after putting all text lines not just one*/
	public void putTextLine(int line, String text) {putTextLine(line, text, defaultLineColor);}
	public void putTextLine(int line, String text, Color lineColor) {
		if(textLines.containsKey(line)) Console.ln("GUITextBoxScroll -> putTextLine() -> text line " + line + " will be overwritten");
		
		textLines.put(line, text);
		lineColors.put(line, lineColor);
		
		if(line > maxLine) maxLine = line;
		updateScrollMax();
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	public void clearText() {
		textLines.clear();
		lineColors.clear();
	}
	
	public void setLineColor(int line, Color lineColor) {
		if(!textLines.containsKey(line)) {
			Console.err("GUITextBoxScroll -> setLineColor() -> textBox has no line " + line);
			return;
		}
		lineColors.put(line, lineColor);
		updateGraphics();
	}
	
	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = UtilsMath.clamp(lineSpacing, 0, 10);
		updateScrollMax();
		updateGraphics();
	}
	
	public void setLineThickness(int lineThickness) {
		this.lineThickness = UtilsMath.clamp(lineThickness, 10, 40);
		updateScrollMax();
		updateGraphics();
	}
	
	public void setTextBoxSize(int width, int height) {
		this.width = width;
		this.height = height;
		updateScrollMax();
		updateGraphics();
	}
	
	public void scrollUp() {
		if(scrollMax <= 0) return;
		Console.ln("GUIListScroll -> scrollUp() -> scrollProgress : " + scrollProgress);
		scroll += scrollStep;
		if(scroll > scrollMax) scroll = scrollMax;
		scrollProgress = UtilsMath.dividePrecise(scroll, scrollMax);	
		updateGraphics();	
	}
	
	public void scrollDown() {
		if(scrollMax <= 0) return;
		Console.ln("GUIListScroll -> scrollDown() -> scrollProgress : " + scrollProgress);
		scroll -= scrollStep;
		if(scroll < scrollMin) scroll = scrollMin;
		scrollProgress = UtilsMath.dividePrecise(scroll, scrollMax);
		updateGraphics();		
	}
	
	public void setScroll(int scroll) {
		if(scrollMax <= 0) return;
		if(scroll > scrollMin && scroll < scrollMax) {
			this.scroll = scroll;
			scrollProgress = scroll / scrollMax;
			scrollProgress = UtilsMath.dividePrecise(scroll, scrollMax);	
			updateGraphics();
		} else Console.err("GUIScrollList -> setScroll() -> scroll is outside of domain : (min, scroll, max) : (" + scrollMin + ", " + scroll + ", " + scrollMax + ")");
	}
	
	//--------
	//---------------- Update --------
	//--------
	private void updateScrollMax() {		
		int textMax = (maxLine + 2) * (lineSpacing + lineThickness);
		
		if(textMax <= height) scrollMax = 0;
		else scrollMax = textMax - height;
		
		updateScrollButtonThickness();
		updateScrollButtonPosition();
	}
	
	private void updateScrollButtonThickness() {
		if(scrollMax <= 0) scrollButtonThickness = height;
		else {
			scrollButtonThickness = height - scrollMax;
			if(scrollButtonThickness < 30) scrollButtonThickness = 30;
		}
	}
	
	public void updateScrollButtonPosition() {
		scrollButtonPosition = (int)(height * scrollProgress) - scrollButtonThickness / 2;
		if(scrollButtonPosition < 0) scrollButtonPosition = 0;
		if(scrollButtonPosition > height - scrollButtonThickness) scrollButtonPosition = height - scrollButtonThickness;
	}
	
	
	public void updateGraphics() {
		BufferedImage img = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
		
		g.setFont(font);
		textLines.forEach((i, l) -> {
			int x = textOffset.x;
			int y = i * (lineThickness + lineSpacing) - scroll;
			
			int y1 = (y - lineThickness - lineSpacing);
			if(y > 0 && y1 < height) {
				g.setColor(lineColors.get(i));
				g.drawString(l, x, y);
			}
		});
		
		g.setColor(scrollBarColor);
		g.fillRect(width - scrollBarThickness, 0, scrollBarThickness, height);
		
		g.setColor(scrollButtonColor);
		g.fillRect(width - scrollBarThickness, scrollButtonPosition, scrollBarThickness, scrollButtonThickness);
		
		g.dispose();
		
		setRaster(img);
	}
	
	
	@Override
	protected void updateRaster() {return;}
	@Override
	protected void updateObject() {return;}
	@Override
	protected void updatePosition() {return;	}
		
	//--------
	//---------------- Interaction --------
	//--------
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {return;}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(WarpedMouse.isFocused()) return;
		if(scrollMax <= 0) return;
		Point p = mouseEvent.getPointRelativeToObject();
		scrollProgress = p.y / (double)height;
		scrollProgress = UtilsMath.clamp(scrollProgress, 0.0, 1.0);
		scroll = (int)(scrollMax * scrollProgress);
		updateScrollButtonPosition();
		updateGraphics();	
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		if(scrollMax <= 0) return;
		Point p = mouseEvent.getPointRelativeToObject();
		if(p.x > width - scrollBarThickness) {
			scrollProgress = p.y / (double)height;
			scrollProgress = UtilsMath.clamp(scrollProgress, 0.0, 1.0);
			scroll = (int)(scrollMax * scrollProgress);
			updateScrollButtonPosition();
			updateGraphics();
		}
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		if(0 + mouseEvent.getWheelEvent().getWheelRotation() > 0) scrollUp();
		else scrollDown();
		}

	

	
	
}
