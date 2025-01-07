/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.textBox;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsString;

public class GUITextBoxLined extends WarpedGUI{
	

	private static final double WIDTH_HEIGHT_RATIO = 0.75;

	public static final int CLEAR_TEXTBOX  = 0;
	public static final int COLOUR_TEXTBOX = 1;
	public static final int IMAGE_TEXTBOX  = 2;
	
	private boolean fixedSize = true;
	
	private int textBoxType    = CLEAR_TEXTBOX;
	private Color textBoxColor = new Color(50, 50, 50, 170);
	private BufferedImage textBoxImage;
	
	private HashMap<Integer, Color>  lineColorsModable = new HashMap<>();
	private HashMap<Integer, String> textLinesModable  = new HashMap<>();
	
	private HashMap<Integer, Color>  lineColorsFixed = new HashMap<>();
	private HashMap<Integer, String> textLinesFixed  = new HashMap<>();
	
	private int lineSpacing = 2;
	
	private Color defaultTextColor = Color.WHITE;
	private Font font = UtilsFont.getPreferred();
	
	private Vec2i textOffset = new Vec2i(5, font.getSize() - font.getSize() / 8);
	
	private WarpedAction selectAction = () -> {};
	
	private BufferedImage textBox;
	
	public GUITextBoxLined() {
		textBox = new BufferedImage(100, 50, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(new BufferedImage(100, 50, WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public GUITextBoxLined(int width, int height) {
		textBox = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public GUITextBoxLined(int width, int height, Color color) {
		textBox = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		setRaster(new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE));
		setBackgroundColor(color);
	}
	
	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
		updateTextBox();
	}
	
	public void setTextLines(List<String> textLines) {
		HashMap<Integer, String> result = new HashMap<>();
		for(int i = 0; i < textLines.size(); i++) {
			result.put(i, textLines.get(i));
		}
		setTextLines(result);
	}
	
	public void setSelectAction(WarpedAction selectAction) {this.selectAction = selectAction;}
	public void clearSelectAction() {this.selectAction = () -> {};}
	
	public void setTextLines(HashMap<Integer, String> textLines) {
		this.textLinesModable = textLines;
		updateTextBox();
	}
		
	public void putTextLine(int line, String text) {
		textLinesModable.put(line, text);
	}
	public void putTextLine(int line, String text, Color color) {
		textLinesModable.put(line, text);
		lineColorsModable.put(line, color);
	}
	
	public void removeTextLine(int line) {
		if(textLinesModable.containsKey(line)) textLinesModable.remove(line);
		else Console.err("GUITextBox -> removeTextLine -> text box has no line : " + line);
	}

	public void setTextSize(int textSize) {
		font = UtilsFont.getPreferred(textSize);
		updateTextBox();
	}

	public void setDefaultTextColor(Color defaultTextColor) {
		this.defaultTextColor = defaultTextColor;
		updateGraphics();
	}
	
	public void setTextStyle(int textStyle) { 
	font = UtilsFont.getPreferred(textStyle, UtilsFont.getPreferredSize());
	updateGraphics();
	}
	
	public void setTextOffset(int x, int y) {
		textOffset.x = x;
		textOffset.y = y;
		updateGraphics();
	}

	public void setTextBoxSize(int width, int height) {
		fixedSize = true;
		textBox = new BufferedImage(width, height, WarpedProperties.BUFFERED_IMAGE_TYPE);
		//setRaster(new BufferedImage(width, height, FrameworkProperties.BUFFERED_IMAGE_TYPE));
		updateGraphics();
	}
	
	public void setBackgroundType(int textBoxType) {
		this.textBoxType = textBoxType;
		updateGraphics();
	}
	
	public void setBackgroundColor(Color textBoxColor) {
		this.textBoxColor = textBoxColor;
		textBoxType = COLOUR_TEXTBOX;
		updateGraphics();
	}
	
	public void setBackgroundImage(BufferedImage textBoxImage) {
		this.textBoxImage = textBoxImage;
		textBoxType = IMAGE_TEXTBOX;
		updateGraphics();
	}
	
	public void setTextBoxAlpha(int alpha) {
		switch(textBoxType) {
		case CLEAR_TEXTBOX :
			Console.err("GUIText -> setTextBoxAlpha() -> can't set alpha because of invalid textBoxType : " + textBoxType);
			return;
		case COLOUR_TEXTBOX :
			textBoxColor = new Color(textBoxColor.getRed(), textBoxColor.getGreen(), textBoxColor.getBlue(), alpha);
			break;
		case IMAGE_TEXTBOX :
			UtilsImage.setAlpha(textBoxImage, (byte)alpha);
			break;
			default : 
		}
	}
	
	public void clearText() {
		textLinesModable.clear();
		lineColorsModable.clear();
	}
	public void fixedSize() {fixedSize = true;}
	public void resizeable() {fixedSize = false;}
	
	public void updateTextBox() {
		updateTextBoxSize();
		updateGraphics();
	}
		
	private void updateGraphics() {
		lineColorsFixed = new HashMap<>(lineColorsModable);
		textLinesFixed  = new HashMap<>(textLinesModable);
		
		Graphics2D g2d = textBox.createGraphics();
		drawBackground(g2d);
		drawText(g2d);
		g2d.dispose();
		paintRaster(textBox);
	}
	
	private void updateTextBoxSize() {
		if(fixedSize) return;  
		else {		
			switch(textBoxType) {
			case CLEAR_TEXTBOX : case COLOUR_TEXTBOX :
				textBox = new BufferedImage((UtilsString.getLongestLineLength(textLinesModable) + 1) * (int)((font.getSize() + lineSpacing) * WIDTH_HEIGHT_RATIO), (UtilsString.getMaxLine(textLinesModable) + 1) * (font.getSize() + lineSpacing), WarpedProperties.BUFFERED_IMAGE_TYPE);
				break;
			case IMAGE_TEXTBOX : default :
				Console.err("GUITextBox -> updateTextBoxSize() -> invalid case : " + textBoxType);
				return;
			}
		}
	}
	
	private void drawBackground(Graphics2D g2d) {
		switch(textBoxType) {
		case CLEAR_TEXTBOX :
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g2d.fillRect(0,0, (int)size.x, (int)size.y);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			break;
		case COLOUR_TEXTBOX : 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g2d.fillRect(0,0, (int)size.x, (int)size.y);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g2d.setColor(textBoxColor);
			g2d.fillRect(0, 0, (int)size.x, (int)size.y);
			break;
		case IMAGE_TEXTBOX : 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g2d.fillRect(0,0, (int)size.x, (int)size.y);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g2d.drawImage(textBoxImage, 0, 0, (int)size.x, (int)size.y, null);
			break;
		default :
			Console.err("GUIText -> drawBackground() -> invalid case : " + textBoxType);
			return;
		}
	}
	
	private void drawText(Graphics2D g2d) {
		g2d.setFont(font);
	
		textLinesFixed.forEach((line, text) -> {
			if(lineColorsModable.containsKey(line)) g2d.setColor(lineColorsFixed.get(line));
			else g2d.setColor(defaultTextColor);
			
			int rx = textOffset.x;
			int ry = textOffset.y + (font.getSize() + lineSpacing) * line;
			
			g2d.drawString(text, rx, ry);
		});
	}
	
	
	
	protected void mouseEntered() {return;}
	protected void mouseExited() {return;}
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {selectAction.action();}
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}
	protected void updateRaster() {return;}
	protected void updateObject() {return;}
	protected void updatePosition() {return;}

}
