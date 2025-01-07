/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.textBox;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;

public class GUITextBoxColumn extends WarpedGUI {

	
	private Color backgroundColor = Colour.getColor(Colour.GREY_DARK);
	private Color borderColor = Color.BLACK;
	private int borderThickness = 2;
	
	private ArrayList<String>  texts 	= new ArrayList<>();
	private ArrayList<Integer> lines 	= new ArrayList<>();
	private ArrayList<Color> lineColors	= new ArrayList<>();
	
	private int columnWidth  = 250;
	private int columnHeight = 400;
	
	private static final Color DEFAULT_LINE_COLOR = Color.WHITE;
	
	private Font font = UtilsFont.getPreferred();
	
	private int columns 	   = 1; //Number of columns
	private int lineHeight = font.getSize();//Distance between each line
	private int linesPerColumn = 0;	

	private Vec2i textOffset 	= new Vec2i();	
    
	//--------
	//---------------- Constructor --------
	//--------
	public GUITextBoxColumn(int columnWidth, int columnHeight) {
		this.columnWidth = columnWidth;
		this.columnWidth = columnWidth;
		 
		
		linesPerColumn = columnHeight / lineHeight;
		updateGraphics();
	}
	
	//--------
	//---------------- Access --------
	//--------
	public void setTextSize(int textSize) {
		font = UtilsFont.getPreferred(textSize);
		updateGraphics();
	}
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	
	
	//--------
	//---------------- Text Lines --------
	//--------
	public void addTextLine(int line, String text) {
		if(lines.contains(line)) {
			Console.err("GUITextBoxColumns -> addTextline() -> line already exits : " + line);
			return;
		}
		
		texts.add(text);
		lines.add(line);
		lineColors.add(DEFAULT_LINE_COLOR);
	}
	
	public void clearTextLines() {
		texts.clear();
		lines.clear();
		lineColors.clear();
	}
		
	
	//--------
	//---------------- Update --------
	//--------	
	@Override
	protected void updateRaster() {return;}
	
	@Override
	protected void updateObject() {return;}
	
	@Override
	protected void updatePosition() {return;}
	
	public void updateGraphics(){
		int width = columnWidth * columns;
		BufferedImage img = new BufferedImage(width, columnHeight, WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		g.setColor(borderColor);
		g.fillRect(0, 0, width, columnHeight);
		
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, width - borderThickness * 2, columnHeight - borderThickness * 2);
		
		g.setFont(font);
		
		for(int i = 0; i < texts.size(); i++) {
			g.setColor(lineColors.get(i));
			
			int line = lines.get(i);
			int xPos = columnWidth * (line / linesPerColumn);
			int yPos = line % linesPerColumn;
			
			g.drawString(texts.get(i), xPos + textOffset.x, yPos + textOffset.y);
		}
		
		g.dispose();
		setRaster(img);
	};
	
	
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
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


	
	
	
}
