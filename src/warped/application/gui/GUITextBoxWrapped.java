/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsMath;

public class GUITextBoxWrapped extends WarpedGUI {

	/*The GUITextBoxWrapped provides these functionalities : 
	 * 
	 * 		- Set a title to display above a paragraph of text.
	 * 		- Pass a single string for a paragraph and the text will be wrapped into the specified area.
	 * 		- Set margin, linePadding, titlePadding to format the position of title and text.
	 * 		- Set the color for border, background, title and paragraph. 		
	 * 		- Option to make the background and/or border invisible.
	 * 		- Automatic scroll bar if the text is too big to fit inside the textBox.
	 * 
	 * */
	
	private String title;
	private String paragraph = "";
	private ArrayList<String> textLines = new ArrayList<>();
	
	private Font paragraphFont  = new Font("TextBox", Font.PLAIN, 14);
	private Font titleFont		= new Font("Title", Font.BOLD, 18);
	
	private VectorI titleOffset = new VectorI(30, 10);
	
	private int borderThickness = 3;
	private int titlePadding    = 14;
	private int linePadding     = 2;	
	private int marginPadding   = 12;
	
	private Color borderColor 	  	 = Colour.BLACK.getColor();
	private Color backgroundColor 	 = Colour.GREY_DARK.getColor();
	private Color titleBannerColor 	 = Colour.GREY_DARK_DARK.getColor();
	private Color paragraphTextColor = Color.WHITE;
	private Color titleTextColor 	 = Colour.YELLOW_LIGHT.getColor();
	private Color scrollBarColor  	 = Colour.GREY_DARK_DARK.getColor();
	private Color scrollButtonColor  = Colour.GREY.getColor(); 
	
	private boolean isScrollBarVisible = false; 
	private boolean isBackgroundVisible = true;
	
	private double scroll = 0.0;
	private int scrollMax = 0;
	private int scrollBarWidth = 10;
	private int scrollButtonHeight = 0;
	
	private int titleHeight;
	private int lineHeight;
	private int drawableWidth; 
	private int drawableHeight;
	
	/**A text box with the default parameters.
	 * @author 5som3*/
	public GUITextBoxWrapped() {
		setSize(300, 400);
	}
	
	/**A text textBox with the specified parameters.
	 * @param width - the width of the text box in pixels.
	 * @param height - the height of the textBox in pixels.
	 * @author 5som3*/
	public GUITextBoxWrapped(int width, int height) {
		setSize(width, height);
	}
	
	/**A text textBox with the specified parameters.
	 * @param width - the width of the text box in pixels.
	 * @param height - the height of the textBox in pixels.
	 * @param title - the title of the textBox.
	 * @param paragraph - the content of the textBox.
	 * @author 5som3*/
	public GUITextBoxWrapped(int width, int height, String title, String paragraph) {
		this.title = title;
		this.paragraph = paragraph;
		setSize(width, height);
	}
	
	/**Set the title for the textBox
	 * @param title - the title is displayed above the paragraph.
	 * @author 5som3*/
	public void setTitle(String title) {
		this.title = title;
		updateParagraphLines();
		updateGraphics();
	}
	
	/**Set the offset for the title.
	 * @param xOffset - the xOffset measured in pixels.
	 * @param yOffset - the yOffset measured in pixels.
	 * @apiNote offset is measured from the top left corner where the titlePadding and marginPadding intersect. 
	 * @author 5som3
	 * */
	public void setTitleOffset(int xOffset, int yOffset) {
		titleOffset.set(xOffset, yOffset);
		updateGraphics();
	}
	
	/**Clears the title (if any).
	 * @author 5som3
	 * */
	public void clearTitle() {
		title = null;
		updateParagraphLines();
		updateGraphics();
	}
		
	/**Clears any text from the paragraph.
	 * @author 5som3*/
	public void clearParagraph() {
		paragraph = "";
		updateParagraphLines();
		updateGraphics();
	}
	
	/**Set the text to display in the paragraph portion of the text box.
	 * @param text - the text to display.
	 * @apiNote Use the text '/n/' to enter a blank line. [example : setParagraph("This is on line one./n/This is on line three.");]
	 * @author 5som3*/
	public void setParagraph(String text) {
		this.paragraph = text;
		updateParagraphLines();
		updateGraphics();
	}
	
	/**Set the text to display. Each string will be appended with a blank line.
	 * @param lines - the lines to display.
	 * @implNote each line will be appended with /n/ which will add a blank line after each string. 
	 * @author 5som3*/
	public void setParagraph(List<String> lines) {
		String result = "";
		
		for(int i = 0; i < lines.size(); i++) {
			result += lines.get(i);
			if(i != lines.size() - 1) result += "/n/";
		}
		
		setParagraph(result);
	}
	
	/**Set the font for the textBox.
	 * @param font - the font to render the text with.
	 * @author 5som3*/
	public void setParagraphFont(Font paragraphFont) {
		this.paragraphFont = paragraphFont;
		updateParagraphLines();
		updateGraphics();
	}
	
	/**Set the font for the title.
	 * @param titleFont - the font for the title.
	 * @author 5som3*/
	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
		updateGraphics();
	}
	
	/**Set the text size for the title.
	 * @param textSize - the size for the title text.
	 * @author 5som3*/
	public void setTitleTextSize(int textSize) {
		if(textSize < 6) {
			Console.err("GUITextBoxWrapper -> setTitleTextSize() -> text size too small : " + textSize);
			textSize = 6;
		}
		titleFont = new Font(titleFont.getFontName(), titleFont.getStyle(), textSize);
		updateGraphics();
	}

	/**Set the text size for the paragraph.
	 * @param textSize - the size for the paragraph text.
	 * @author 5som3*/
	public void setParagraphTextSize(int textSize) {
		if(textSize < 6) {
			Console.err("GUITextBoxWrapper -> setParagraphTextSize() -> text size too small : " + textSize);
			textSize = 6;
		}
		paragraphFont = new Font(titleFont.getFontName(), titleFont.getStyle(), textSize);
		updateParagraphLines();
		updateGraphics();
	}
	
	/** Set the thickness of the border.
	 * @param borderThickness - the thickness of the border in pixels.
	 * @apiNote setBorderThickness(0) for no border.
	 * @author 5som3*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0) {
			Console.err("GUITextBoxWrapped -> setBorderThickness() -> border thickness must be positive." );
			borderThickness = 0;
		}
		this.borderThickness = borderThickness;
		updateGraphics();
	}
	
	/**Set the padding for the title.
	 * @param titlePadding - the padding (in pixels) is applied above and beneath the title.
	 * @author 5som3*/
	public void setTitlePadding(int titlePadding) {
		if(titlePadding < 0) {
			Console.err("GUITextBoxWrapped -> setTitlePadding() -> titlePadding must be positive.");
			titlePadding = 0;
		}
		this.titlePadding = titlePadding;
		updateGraphics();
	}
	
	/**Set the margin for the text.
	 * @param marginPadding - the padding (in pixels) is applied to the left and right of the text.
	 * @author 5som3*/
	public void setMarginPadding(int marginPadding) {
		if(marginPadding < 0) {
			Console.err("GUITextBoxWrapped -> setMarginPadding() -> margin Padding must be positive.");
			marginPadding = 0;
		}
		this.marginPadding = marginPadding;
		updateGraphics();
	}
	
	/** Set the padding between each line in the paragraph.
	 * @param linePadding - the padding between lines in pixels.
	 * @author 5som3*/	
	public void setLinePadding(int linePadding) {
		if(linePadding < 0) {
			Console.err("GUITextBoxWrapped -> setLinePadding() -> line Padding must be positive.");
			linePadding = 0;
		}
		this.linePadding = linePadding;
		updateGraphics();
	}
	
	/**Set the colour of the border.
	 * @param borderColor - the border color.
	 * @apiNote setBorderThickness(0) for no borderColor
	 * @author 5som3*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	/** Set the colour to render behind the text.
	 * @param backgroundColor - the color of the background.
	 * @author 5som3*/
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	/** Set the background colour behind the title.
	 * @param bannerColor - the color to use for the title banner.
	 * @author 5som3*/
	public void setBannerColor(Color bannerColor) {
		this.titleBannerColor = bannerColor;
		updateGraphics();
	}
	
	/**Set if the background color will be rendered behind the text
	 * @param isBackgroundVisible - if true the background will be rendered.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackgroundVisible) {
		this.isBackgroundVisible = isBackgroundVisible;
		updateGraphics();
	}
	
	/**Set the color of the text in the paragraph.
	 * @param fontColor - the color to render the 
	 * */
	public void setParagraphColor(Color paragraphColor) {
		this.paragraphTextColor = paragraphColor;
		updateGraphics();
	}
	
	/**Set the color of the title text.
	 * @param titleColor - the color of the title text.
	 * @author 5som3*/
	public void setTitleColor(Color titleColor) {
		this.titleTextColor = titleColor;
		updateGraphics();
	}
	
	
	/**Set the size of the text box.
	 * @param width - the width of the text box in pixels.
	 * @param height - the height of the text box in pixels.
	 * @author 5som3*/
	public void setSize(int width, int height) {
		sprite.setSize(width, height);
		updateParagraphLines();
		updateGraphics();
	}
	
	private void updateParagraphLines() {
		textLines.clear();
		scroll = 0.0;
		
		Graphics g = getGraphics();
		g.setFont(titleFont);
		titleHeight = g.getFontMetrics().getHeight();
		g.setFont(paragraphFont);
		FontMetrics metrics = g.getFontMetrics();
		lineHeight = g.getFontMetrics().getHeight() + linePadding;
		
		drawableWidth = getWidth() - borderThickness * 2 - marginPadding * 2 - scrollBarWidth;
		if(title == null) drawableHeight = getHeight() - borderThickness * 2;
		else drawableHeight = getHeight() - borderThickness * 2 - titlePadding - titleHeight;
		
		Console.blueln("GUITextBoxWrapped -> updateParagraphLines() -> drawableWidth : " + drawableWidth);
		
		int characterIndex = -1; //The index of the character we are checking in the string
		int lastWordIndex = 0;
		int lastLineIndex = 0;
		boolean finished = false;
		
		while(!finished){
			
			characterIndex++;
			if(characterIndex >= paragraph.length()) {
				//WarpedConsole.ln("GUITextBoxWrapped -> updateParagraphLines() -> index exceeded paragraph length, " + textLines.size() + " text lines were added");
				finished = true;
				String line = paragraph.substring(lastLineIndex, characterIndex);
				if(metrics.stringWidth(line) >= drawableWidth) { // need to start new line
					textLines.add(paragraph.substring(lastLineIndex, lastWordIndex));
					lastLineIndex = lastWordIndex + 1;
					lastWordIndex++;
					textLines.add(paragraph.substring(lastLineIndex, paragraph.length()));
					
				} else { //up to this word still fits, check if another word will fit
					textLines.add(line);
				}
				break;
			}

			String character = paragraph.substring(characterIndex, characterIndex + 1);
			if(character.equals(" ")) { //check if needs new line
				String line = paragraph.substring(lastLineIndex, characterIndex);
				if(metrics.stringWidth(line) >= drawableWidth) { // need to start new line
					textLines.add(paragraph.substring(lastLineIndex, lastWordIndex));
					lastLineIndex = lastWordIndex + 1;
					lastWordIndex++;
					characterIndex = lastWordIndex;
					
				} else { //up to this word still fits, check if another word will fit
					lastWordIndex = characterIndex;
				}
			}
			
			if(character.equals("/") && characterIndex + 3 < paragraph.length() && paragraph.subSequence(characterIndex, characterIndex + 3).equals("/n/")) { // check if user input the new line key
				String dropLine = paragraph.substring(lastLineIndex, characterIndex);
				if(!dropLine.equals(" ")) {
					if(metrics.stringWidth(dropLine) >= drawableWidth) { // need to start new line
						textLines.add(paragraph.substring(lastLineIndex, lastWordIndex));
						lastLineIndex = lastWordIndex + 1;
						lastWordIndex++;
						textLines.add(paragraph.substring(lastLineIndex, characterIndex));
					} else { //up to this word still fits, check if another word will fit
						textLines.add(dropLine);
					}
					
				}
				textLines.add("");
				lastLineIndex = characterIndex + 3;
				lastWordIndex = lastLineIndex;
				characterIndex = lastWordIndex;
			}
		}
		
		int paragraphHeight = lineHeight * (textLines.size() + 1); 
		if(paragraphHeight > drawableHeight) {
			isScrollBarVisible = true;
			scrollMax = paragraphHeight - drawableHeight;
			scrollButtonHeight = drawableHeight - scrollMax;
			if(scrollButtonHeight < 20) scrollButtonHeight = 20;
		} else isScrollBarVisible = false;
		
	}
	
	protected void updateGraphics() {
		Graphics g = getGraphics();
		
		if(isBackgroundVisible) {			
			g.setColor(backgroundColor);
			g.fillRect(0, 0, getWidth(), getHeight());		
		}
		
		g.setFont(paragraphFont);
		g.setColor(paragraphTextColor);
		int x = borderThickness + marginPadding;
		int y = borderThickness + titlePadding + titleHeight + lineHeight;		
		if(title == null) y = borderThickness +  lineHeight;
		for(int i = 0; i < textLines.size(); i++) {
			int yr =  y + i * lineHeight - (int)(scrollMax * scroll);
			if(yr < - titleHeight - titlePadding) continue;
			g.drawString(textLines.get(i), x, yr);
		}
		
		
		if(title != null) {			
			g.setColor(titleBannerColor);
			g.fillRect(0, 0, getWidth(), titleHeight + titlePadding);
			
			g.setFont(titleFont);
			g.setColor(titleTextColor);
			g.drawString(title, borderThickness + marginPadding + titleOffset.x(), borderThickness + titlePadding + titleOffset.y());			
		}
		
		g.setColor(borderColor);
		g.fillRect(0, 0, getWidth(), borderThickness);
		g.fillRect(0, getHeight() - borderThickness, getWidth(), borderThickness);
		g.fillRect(0, 0, borderThickness, getHeight());
		g.fillRect(getWidth() - borderThickness, 0, borderThickness, getHeight());
		
		
		if(isScrollBarVisible) {
			if(title == null) {
				g.setColor(scrollBarColor);
				g.fillRect(getWidth() - borderThickness * 2 - scrollBarWidth, borderThickness * 2, scrollBarWidth, drawableHeight - borderThickness * 2);
				g.setColor(scrollButtonColor);
				g.fillRect(getWidth() - borderThickness * 2 - scrollBarWidth, borderThickness * 2 + (int)((drawableHeight - scrollButtonHeight) * scroll), scrollBarWidth, scrollButtonHeight);
				g.setColor(borderColor);
				g.fillRect(0, getHeight() - borderThickness, getWidth(), borderThickness);
			} else {
				g.setColor(scrollBarColor);
				g.fillRect(getWidth() - borderThickness * 2 - scrollBarWidth, borderThickness + titlePadding + titleHeight, scrollBarWidth, drawableHeight);
				g.setColor(scrollButtonColor);
				g.fillRect(getWidth() - borderThickness * 2 - scrollBarWidth, borderThickness + titlePadding + titleHeight + (int)((drawableHeight - scrollButtonHeight) * scroll), scrollBarWidth, scrollButtonHeight);
				g.setColor(borderColor);
				g.fillRect(0, getHeight() - borderThickness, getWidth(), borderThickness);
			}
		}
		
		g.dispose();
		pushGraphics();
	}
	
	@Override
	protected void mouseEntered() {return;}
	@Override
	protected void mouseExited() {return;}
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		
	}
	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		scroll = UtilsMath.divide(mouseEvent.getPointRelativeToObject().y, getHeight());
		updateGraphics();
	}
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		if(scrollMax == 0) {
			if(scroll != 0) {
				scroll = 0.0;
				updateGraphics();
			}
			return;
		}
		if(mouseEvent.getWheelRotation() > 0) {
			scroll += 0.04325;
			if(scroll > 1.0) scroll = 1.0;
		} else {
			scroll -= 0.04325;
			if(scroll < 0.0) scroll = 0.0;
		}
		updateGraphics();
		
	}

	
	

}
