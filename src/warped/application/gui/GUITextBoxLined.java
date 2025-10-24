package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import warped.graphics.window.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;
import warped.utilities.utils.UtilsFont.FontStyleType;

public class GUITextBoxLined extends WarpedGUI {
	
	/*GUITextLines provides these functions
	 * 		- set individual lines of text within a block of text to be displayed to the user
	 * 		- text lines can be skipped to make breaks or paragraphs 
	 * 		- text lines can be set with a specific colour 
	 * 		- text lines are scrollable with a scroll bar on the right side.
	 * 		- scroll bar automatically scales its size and scroll range based on the maximum line
	 * 		- control visibility of line numbers by setting a boolean true or false
	 * 
	 * GUITextLines does not provide any line wrapping	
	 * 		
	 * 		- you will need to make sure the text box is large enough to display the maximum line length
	 * 		- alternatively make all your lines short enough to fit into you required size
	 * 		
	 * 		- If you need line wrapping then you should use GUITextBoxWrapped, it provides a writable area with automatic text line generation (line wrapping)
	 */
	private static final int LINE_NUMBERS_MARGIN = 30; //px
	private static final int SCROLL_BUTTON_MIN_HEIGHT = 60; // px
	private static final int SCROLL_BAR_MIN_WIDTH = 30;
	
	private HashMap<Integer, String>  textLines  = new HashMap<>();	
	private HashMap<Integer, Color>   lineColor = new HashMap<>();
	
	private Font textFont 			= UtilsFont.getDefault();
	private FontStyleType fontStyle = FontStyleType.REGULAR;
	
	private VectorI textOffset      = new VectorI();
			
	private int scrollBarWidth 	   	= SCROLL_BAR_MIN_WIDTH;
	private int scrollButtonHeight 	= 10;	// not set by dev
	
	private int lineThickness 	= 0;	//not set by dev
	private int maxLine   		= 0;	//set automatically when setting text lines
	private int scrollMax 		= 0;	//determined by parameters
	private int scrollButtonPosition = 0; //determined automatically based on scroll 
	
	private double scroll = 0.0; // controlled by users interaction with the scroll button ranges from 0.0 (no scroll) -> 1.0 (max scroll)

	private int borderThickness 	= 2;
	private int linePadding 		= 4;
	private int marginPadding		= 10;
	
	private Color borderColor 			 = Colour.BLACK.getColor();			// will be modified by the set alpha 
	private Color backgroundColor 		 = Colour.GREY_DARK.getColor();		// will be modified by the set alpha

	private boolean isBackgroundVisible = true;
	
	private Color defaultTextColor		 = Colour.WHITE.getColor();				//will have constant alpha
	private Color scrollBarColor 		 = Colour.GREY_DARK_DARK.getColor();	//will have constant alpha
	private Color scrollButtonColor 	 = Colour.GREY.getColor();				//will have constant alpha
	private Color scrollButtonHoverColor = Colour.GREY_LIGHT.getColor();		//will have constant alpha
	private Color scrollButtonDragColor  = Colour.GREY_LIGHT_LIGHT.getColor();	//will have constant alpha
	
	private boolean isLineNumbersVisible = false; //determined by use case 
	private boolean isScrollBarVisible 	 = false; //determined by the number of
		
	private boolean isButtonHovered = false;
	private boolean isButtonDragged = false;
	
	/**A text box with the default parameters.
	 * @author 5som3*/
	public GUITextBoxLined() {
		setTextBoxSize(100, 50);
	}
	
	/**A text box with the specified width and height.
	 * @param width - the width of the text box in pixels.
	 * @param height - the height of the text box in pixels.
	 * @author 5som3*/
	public GUITextBoxLined(int width, int height) {
		setTextBoxSize(width, height);
	}
	
	/**Set the size of the text box, this is the drawable area.
	 * @apiNote TextBoxLined does not have text wrapping so if the text box is smaller than a text line that line will be clipped.
	 * @author 5som3*/
	public void setTextBoxSize(int width, int height) {
		setSize(width, height);
		updateGraphics();		
	}
	
	/** Set the offset of each line text.
	 * @param x - the x offset measured in pixels from the margin
	 * @param y - the y offset measured in pixels from the margin
	 * @author 5som3*/
	public void setTextOffset(int x, int y) {
		this.textOffset.set(x, y);
		updateGraphics();
	}
	
	/**Updates the font based on the language set in UtilsFont.
	 * @apiNote new font will have the style and size set in this object 
	 * @author 5som3*/
	public void updateLanguage() {
		textFont = UtilsFont.getFont(fontStyle, textFont.getSize());
		updateGraphics();
	}
	
	/**Set the visibility of the background.
	 * @param isBackgroundVisible - if true the background will be visible else it will not be rendered.
	 * @author 5som3*/
	public void setBackgroundVisible(boolean isBackgroundVisible) {
		if(this.isBackgroundVisible != isBackgroundVisible) {
			this.isBackgroundVisible = isBackgroundVisible;
			updateGraphics();
		}
	}
	
	/**Should the line numbers be displayed
	 * @param isLineNumbersVisible - if true then the line numbers will be drawn in their own margin at the left side of the text block
	 * @author SomeKid*/
	public void isLineNumberVisible(boolean isLineNumbersVisible) {
		if(this.isLineNumbersVisible != isLineNumbersVisible) {			
			this.isLineNumbersVisible = isLineNumbersVisible;
			updateGraphics();
		}
	}
	
	/**Set the default color of text lines
	 * @param color - the color will be used if no color is specified when setting a text line
	 * @author SomeKid*/
	public void setDefaultTextColor(Color color) {
		this.defaultTextColor = color;
		updateGraphics();
	}
	
	/**Set the font for the text block
	 * @apiNote the size of the text is also the minimum line spacing, i.e. textSize 12 will have a minimum line spacing of 12 pixels.
	 * @apiNote to increase the line spacing call setLinePadding() with the desired line spacing.
	 * @author SomeKid */
	public void setTextSize(int textSize) {
		if(textSize < 1) {
			Console.err("GUITextLines -> setTextSize() -> invalid size : " + textSize);
			textSize = 12;
		}
		textFont = new Font(textFont.getFontName(), textFont.getStyle(), textSize);
		updateScrollParameters();
		updateGraphics();
	}

	/**Set the style of the text block
	 * @param textStyle   -The style of the text to use
	 * 					  - 0 Plain 
	 * 					  - 1 Bold 
	 * 					  - 2 Italic
	 * @author SomeKid*/
	public void setTextStyle(int textStyle) {
		if(textStyle < 0 || textStyle > 2) {
			Console.err("GUITextLines -> setTextStyle() -> invalid text style : " + textStyle);
			textStyle = 0;
		}
		textFont = new Font(textFont.getFontName(), textStyle, textFont.getSize());
		updateGraphics();
	}
	
	/**Set the font of the text block
	 * @param font - the font for the block
	 * @author SomeKid*/
	public void setTextFont(Font font) {
		this.textFont = font;
		updateScrollParameters();
		updateGraphics();
	}
	
	/**Set the distance between the lines of text
	 * @param linePadding - the padding between the lines, set to 0 for the minimum line spacing
	 * @apiNote The minimum line spacing is the size of the text.
	 * @author SomeKid*/
	public void setLinePadding(int linePadding) {
		this.linePadding = linePadding;
		updateScrollParameters();
		updateGraphics();
	}
	
	/**The distance between the left side of the text block and the the start of each line
	 * @param marginPadding - the padding to use, set to 0 for no margin
	 * @apiNote If the line numbers are visible then their will be a minimum margin - see LINE_NUMBERS_MARGIN. 
	 * @author SomeKid*/
	public void setMarginPadding(int marginPadding) {
		if(marginPadding < 0) {
			Console.err("GUITextLines -> setMarginPadding -> margin padding must be positive : " + marginPadding);
			marginPadding = 0;
		}
		this.marginPadding = marginPadding;
		updateGraphics();		
	}
	
	/**Set the border thickness
	 * @param borderThickness - the thickness of the border colour around the block, set to 0 for no border.
	 * @apiNote The min borderThickness is 0.
	 * @apiNote The max borderThickness is (1/4) * getWidth() - where getWidth() is the width of the sprite
	 * @author SomeKid*/
	public void setBorderThickness(int borderThickness) {
		if(borderThickness < 0 || borderThickness > getWidth() / 4 || borderThickness > getWidth() / 4) {
			Console.err("GUITextLines -> setBorderThickness() -> invalid borderThickness : " + borderThickness);
			borderThickness = 0;
		}
		this.borderThickness = borderThickness;
		updateScrollParameters();
		updateGraphics();
	}
	
	/**Set the colour of the border that surrounds the block
	 * @param - the color of the border, for no color call setBorderThickness(0)
	 * @author SomeKid*/
	public void setBorderColor(Color borderColor) {
		this.borderColor = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), this.borderColor.getAlpha());
		updateGraphics();
	}
	
	/**The color behind the lines of text
	 * @param backgroundColor - the color to draw behind the text lines, for no background color call setBackgroundAlpha(0).
	 * @author SomeKid*/
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}

	
	/**Set the width of the scroll bar 
	 * @param scrollBarWidth - the width to use, must be larger than 30 and less than 1/8th the width.
	 * @apiNote The scroll bar will only be visible if the maximum line position exceeds the drawable area 
	 * @author SomeKid*/
	public void setScrollBarWidth(int scrollBarWidth) {
		if(scrollBarWidth < SCROLL_BAR_MIN_WIDTH) {
			Console.err("GUITextLines -> setScrollBarWidth() -> width is too small, it will be set to the minimum : " + SCROLL_BAR_MIN_WIDTH);
			scrollBarWidth = SCROLL_BAR_MIN_WIDTH;
		}
		this.scrollBarWidth = scrollBarWidth;
		updateGraphics();
	}
	
	/**Set the color of the scrollBar
	 * @param scrollBarColor - The color of the scroll bar, that is the color behind the scroll button
	 * @author SomeKid*/
	public void setScrollBarColor(Color scrollBarColor) {
		this.scrollBarColor = scrollBarColor;
		updateGraphics();
	}
	
	/**Set the color of the scroll button
	 * @param scrollButtonColor - the color of the scroll button when it is not being interacted with by the users mouse
	 * @author SomeKid*/
	public void setScrollButtonColor(Color scrollButtonColor) {
		this.scrollButtonColor = scrollButtonColor;
		updateGraphics();
	}
	
	/**Set the color of the scroll button when dragged
	 * @param dragColor - the color that the scroll button will appear when the user is dragging it up or down with the mouse
	 * @author SomeKid*/
	public void setScrollButtonDragColor(Color dragColor) {
		this.scrollButtonDragColor = dragColor;
		updateGraphics();
	}
	
	/**Set the color of the scroll bar when hovered
	 * @param hoverColor - the color that the scroll button will appear when the user hovers over it with their mouse.
	 * @author SomeKid*/
	public void setScrollButtonHoverColor(Color hoverColor) {
		this.scrollButtonHoverColor = hoverColor;
		updateGraphics();
	}	
	
	/**Set a line of text at the end of the block
	 * @Param lineText - the text to add, will be added 1 line after the current max line not at the next vacant line.
	 * @apiNote You will need to manually call updateGraphis() after setting text lines individually. 
	 * @author SomeKid*/
	public void setTextLine(String lineText) {setTextLine(UtilsMath.findMax((Integer[])this.textLines.keySet().toArray()), lineText);};
	
	/**Set text to a specific line in the block
	 * @param line - the line where the text should be written
	 * @param lineText - the text to insert at the specified line
	 * @apiNote If a line already exist at the specified line then that line will be overwritten
	 * @apiNote You will need to manually call updateGraphis() after setting text lines individually.
	 * @author SomeKid*/
	public void setTextLine(int line, String lineText) {setTextLine(line, lineText, defaultTextColor);}
	
	/**Set text to a specific line with the specified colour.
	 * @param line - the line where the text should be written
	 * @param lineText - the text to insert at the specified line
	 * @param lineColour - the colour to draw the text line, (the color of the text - not the background color of the line)
	 * @apiNote If a line already exist at the specified line then that line will be overwritten
	 * @apiNote You will need to manually call updateGraphis() after setting text lines individually.
	 * @author SomeKid*/
	public void setTextLine(int line, String lineText, Color lineColour) {
		if(line < 0) {
			Console.err("GUITextLines -> addTextLine() -> invalid line : " + line + ", lines must be positive");
			return;
		}
		this.textLines.put(line, lineText);
		this.lineColor.put(line, lineColour);
		updateScrollParameters();
	}
	
	/**Set multiple lines of text at once.
	 * @param textLines - a list of the text lines to add, these will be added consecutively starting with line 0;
	 * @apiNote It is faster to set multiple lines at once than to call setTextLine() multiple times with individual lines
	 * @author SomeKid*/
	public void setTextLine(List<String> textLines) {
		clearTextLines();
		for(int i = 0; i < textLines.size(); i++) {			
			this.textLines.put(i, textLines.get(i));
			this.lineColor.put(i, Colour.WHITE.getColor());
		}
		updateGraphics();
	}
	
	/**Set multiple lines of text at once.
	 * @param textLines - a list of the text lines to add, these will be added consecutively starting with line 0;
	 * @param colour - the colour for each line of text.
	 * @apiNote It is faster to set multiple lines at once than to call setTextLine() multiple times with individual lines
	 * @author SomeKid*/
	public void setTextLine(List<String> textLines, Colour colour) {
		clearTextLines();
		for(int i = 0; i < textLines.size(); i++) {			
			this.textLines.put(i, textLines.get(i));
			this.lineColor.put(i, colour.getColor());
		}
		updateGraphics();
	}
	
	/**Set multiple lines of text at once.
	 * @param lines - the line numbers that each text line should be written at.
	 * @param text - the strings of text to be written on their corresponding lines.
	 * @param colors - the colors that their corresponding strings should be drawn in.
	 * @apiNote It is faster to set multiple lines at once than to call setTextLine() multiple times with individual lines
	 * @author SomeKid*/
	public void setTextLine(List<Integer> lines, List<String> text, List<Color> colors) {
		if(lines.size() != text.size() || lines.size() != colors.size() || text.size() != colors.size()) {
			Console.err("GUITextLines -> setTextLine() -> the size of lines, text or colors do not match, must pass an equal number of parametrs");
			return;
		}
		for(int i = 0; i < lines.size(); i++) {
			if(lines.get(i) < 0) {
				Console.err("GUITextLines -> addTextLine() -> invalid line : " + lines.get(i) + ", lines must be positive");
				return;
			}
			this.textLines.put(lines.get(i) , text.get(i));
			this.lineColor.put(lines.get(i) , colors.get(i));
		}
		updateScrollParameters();
		updateGraphics();
	}
	
	/**Clears all text lines and line colours, resets the text box.  
	 * @author 5som3*/
	public void clearTextLines() {
		textLines.clear();
		lineColor.clear();
	}
	
	
	private void updateScrollParameters() {
		if(textLines.size() == 0) return;
		lineThickness  = textFont.getSize() + linePadding;
		maxLine = UtilsMath.findMax(new ArrayList<>(textLines.keySet()));
		
		double documentHeight = lineThickness * maxLine;
		double writableHeight = getHeight() - borderThickness * 2;
		
		if(documentHeight > writableHeight) { //Show scroll bar
			isScrollBarVisible = true;
			double percentOfDocumentVisible = writableHeight / documentHeight;
			
			scrollButtonHeight = (int)(percentOfDocumentVisible * writableHeight);
			if(scrollButtonHeight < SCROLL_BUTTON_MIN_HEIGHT) scrollButtonHeight = SCROLL_BUTTON_MIN_HEIGHT;
			
			scrollButtonPosition = (int) (((writableHeight * scroll) - 0.5 * scrollButtonHeight));
			if(scrollButtonPosition < borderThickness + 1) scrollButtonPosition = borderThickness + 1;
			if(scrollButtonPosition > writableHeight - scrollButtonHeight - borderThickness - 1) scrollButtonPosition = (int)(writableHeight - scrollButtonHeight - borderThickness - 1);
			
			scrollMax = (int)(documentHeight - writableHeight);			
		} else isScrollBarVisible = false; //Hide scroll bar, it is not needed
	}

	/**Updates the graphics based on the current parameters.
	 * This should only be called after setting text lines individually.
	 * @author 5som3*/
	public void updateGraphics() {
		Graphics g = getGraphics();
		
		if(isBackgroundVisible) {			
			g.setColor(borderColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(backgroundColor);	// draw background
			g.fillRect(borderThickness, borderThickness, getWidth() - borderThickness * 2, getHeight() - borderThickness * 2);
		}
		
		if(isScrollBarVisible) { //draw scroll bar
			g.setColor(scrollBarColor);	
			g.fillRect(getWidth() - borderThickness - scrollBarWidth, borderThickness, scrollBarWidth, getHeight() - borderThickness * 2);
			
			if(isButtonDragged) g.setColor(scrollButtonDragColor); // draw scroll button
			else if(isButtonHovered) g.setColor(scrollButtonHoverColor);
			else g.setColor(scrollButtonColor);
			g.fillRect(getWidth() - borderThickness - scrollBarWidth + 1, scrollButtonPosition, scrollBarWidth - 2, scrollButtonHeight);			
		}

		if(isLineNumbersVisible) {	// draw line numbers
			g.setColor(defaultTextColor);
			g.setFont(textFont);
			int x = borderThickness + 2;
			for(int i = 0; i < maxLine; i++) {
				int y = (int)(textFont.getSize() + 2 + (lineThickness * i) - (scroll * scrollMax));
				if(y < 0 - textFont.getSize() || y > getWidth() - borderThickness * 2) continue;
				g.drawString("" + (i + 1), x, y);
			}
		}

		final int x;
		if(isLineNumbersVisible) x = borderThickness + 2 + marginPadding + textOffset.x() + LINE_NUMBERS_MARGIN;
		else x = borderThickness + 2 + marginPadding + textOffset.x();
		g.setFont(textFont);
		
		textLines.forEach((k, v) -> {
			int y = (int)(textFont.getSize() + 2 + (lineThickness * k) - (scroll * scrollMax) + textOffset.y());
			if(y < 0 - textFont.getSize() || y > getWidth() - borderThickness * 2);
			else {				
				g.setColor(lineColor.get(k));
				g.drawString(textLines.get(k), x, y);
			}
		});
		
		
		g.dispose();
		pushGraphics();
		
	}
	
	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		//TODO implement scroll drag
	}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		//TODO implement mouse wheel
	}
	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		//TODO implement scroll button hover
	}

	
	@Override
	protected void mouseEntered() {return;}
	@Override
	protected void mouseExited() {return;}
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

}
