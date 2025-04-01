package warped.application.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import warped.application.actionWrappers.ActionOption;
import warped.functionalInterfaces.WarpedAction;
import warped.user.mouse.WarpedMouse;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsMath;

public class GUIListVertical extends WarpedGUI {
	
	private int hoverIndex = 0;
	
	private VectorI menuMaxSize = new VectorI(120, 30);
	private VectorI buttonSize = new VectorI(120, 30);
	
	private ArrayList<ActionOption> options; 
	private ArrayList<Boolean> optionLocks;
		
	private boolean isAutoHidden = false;
	
	private WarpedAction enterAction = null;
	private WarpedAction exitAction = null;
	
	private Color scrollBarColor 	= Colour.GREY_DARK_DARK.getColor();
	private Color scrollButtonColor = Colour.GREY_LIGHT.getColor();
	
	private int scrollBarThickness = 5;
	private int scrollButtonHeight = menuMaxSize.y();
	private int scrollButtonPosition = 0;
	
	private double scrollProgress = 0.0;
	private int scrollMax = 0;
	private int scrollOffset = 0;
	
	private int buttonPadding = 2;
	private VectorI textOffset = new VectorI(1,1);
	
	private Color paddingColor = Color.BLACK;
	private Color buttonColor = Color.DARK_GRAY;
	private Color buttonHoverColor = new Color(110, 60, 60, 60);
	private Color lockColor = new Color(30, 30, 30, 180);
	
	private Color textColor = Color.YELLOW;
	private Font font 		= UtilsFont.getPreferred();
	
	public GUIListVertical() {
		updateGraphics();
	}
	
	public GUIListVertical(int width, int height) {
		menuMaxSize.set(width, height);
		buttonSize.set(width, buttonSize.y());
	}
	
	
	//--------
	//---------------- Access --------
	//--------
	
	public void autoHide() {isAutoHidden = true;}
	public void setAutoHide(boolean autoHide) {isAutoHidden = autoHide;}
	public void setEnterAction(WarpedAction enterAction) {this.enterAction = enterAction;}
	public void setExitAction(WarpedAction exitAction) {this.exitAction = exitAction;}

	public int optionCount() {return options.size();}
	public void setButtonSize(VectorI vec) {setButtonSize(vec.x(),vec.y());}
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
	}
	public void setTextSize(int size) {
		font = new Font(font.getFontName(), font.getStyle(), size);
	}
	
	public void setButtonSize(int x, int y) {
		buttonSize.set(x,y);
		setScrollMax();
	}
	public void setMenuSize(VectorI vec) {setMenuSize(vec.x(), vec.y());}
	public void setMenuSize(int x, int y) {
		menuMaxSize.set(x, y);
		buttonSize.set(x, buttonSize.y());
	}
	public void setOptions(ArrayList<ActionOption> options) {
		this.options = options;
		updateGraphics();
		setScrollMax();
	}
	
	public void clear() {
		if(options == null) options = new ArrayList<ActionOption>();
		else options.clear();
		updateGraphics();
		setScrollMax();
	}
	
	public void setOptions(ArrayList<ActionOption> options, ArrayList<Boolean> optionLocks) {
		if(options.size() != optionLocks.size()) {
			Console.err("GUIListBUtton -> setOptions() -> options and optionLocks are not equal size");
			return;
		}
		this.options = options;
		this.optionLocks = optionLocks;
		updateGraphics();
		setScrollMax();
	}
	
		
	
	public void setScrollMax() {
		if(options == null) {
			scrollMax = 0;
			return;
		}
		scrollOffset = 0;
		scrollMax = options.size() * (buttonSize.y() + buttonPadding) - menuMaxSize.y();
		if(scrollMax < 0) scrollMax = 0;
	}
	
	public VectorI getMenuSize() {return menuMaxSize;}
	public VectorI getButtonSize() {return buttonSize;}
	
	//--------
	//---------------- Update --------
	//--------
	
	@Override
	public void updateObject() {return;}
	
	//--------
	//---------------- Graphics --------
	//--------
	private void updateGraphics() {		
		if(options == null) return;
		
		int totalButtonHeight = optionCount() * (buttonSize.y() + buttonPadding);
		if(totalButtonHeight <= 0) {
			Console.err("GUIListButton -> updateGraphics() -> height is less than 0");
			return;
		}
		
		int menuHeight;
		if(totalButtonHeight > menuMaxSize.y()) menuHeight = menuMaxSize.y();
		else menuHeight = totalButtonHeight;
		
		setSize(menuMaxSize.x(), menuHeight);
		Graphics g = getGraphics();		
		
		g.setColor(paddingColor);
		g.fillRect(0, 0, menuMaxSize.x(), menuMaxSize.y()); // fill background 

		g.setColor(scrollBarColor);
		g.fillRect(menuMaxSize.x() - scrollBarThickness, 0, scrollBarThickness, menuMaxSize.y()); // draw scroll bar
		
		for(int i = 0; i < optionCount(); i++) {	// draw each button
			ActionOption option = options.get(i);
			
			int drawY = (i * (buttonPadding + buttonSize.y())) - scrollOffset;
			int drawWidth = buttonSize.x() - scrollBarThickness;
			
			if(drawY + buttonSize.y() < 0 || drawY > menuMaxSize.y()) continue;
			
			g.setColor(buttonColor);
			g.fillRect(0, drawY, drawWidth, buttonSize.y());
			
			g.setFont(font);
			g.setColor(textColor);
			g.drawString(option.getName(), textOffset.x(), drawY + textOffset.y() + font.getSize());
			
			if(i == hoverIndex) {
				if(optionLocks != null && optionLocks.get(i));					
				else {
					g.setColor(buttonHoverColor);
					g.fillRect(0, drawY, drawWidth, buttonSize.y());
				}
			}			
			
			if(optionLocks != null && optionLocks.get(i)) {
				g.setColor(lockColor);
				g.fillRect(0, drawY, drawWidth, buttonSize.y());
			}
		}
		
		g.setColor(scrollButtonColor);	
		g.fillRect(menuMaxSize.x() - scrollBarThickness, scrollButtonPosition, scrollBarThickness, scrollButtonHeight);
		
		g.dispose();
		pushGraphics();
	}


	
	//--------
	//---------------- Interaction --------
	//--------
	@Override
	protected void mouseEntered() {if(enterAction!= null) enterAction.action();}
	@Override
	protected void mouseExited() {
		hoverIndex = -1;
		if(exitAction != null) exitAction.action();
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int index = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), buttonSize.y() + buttonPadding);

		if(index != hoverIndex) {
			hoverIndex = index;
			updateGraphics();
		}		
	}


	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int index = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), buttonSize.y() + buttonPadding);
		Console.ln("GUIScrollButtonMenu -> mouseReleaseed -> select index : " + index);
		if(index < 0 || index >= options.size()) return;
		else if(optionLocks != null && optionLocks.get(index)) return;
		options.get(index).action();
		if(isAutoHidden) invisible();
	}

	public void updateScrollButtonPosition() {
		scrollButtonPosition = (int)(menuMaxSize.y() * scrollProgress) - scrollButtonHeight / 2;
		if(scrollButtonPosition < 0) scrollButtonPosition = 0;
		if(scrollButtonPosition > menuMaxSize.y() - scrollButtonHeight) scrollButtonPosition = menuMaxSize.y() - scrollButtonHeight;
	}
	
	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(WarpedMouse.isFocused()) return;
		if(scrollMax <= 0) return;
		Point p = mouseEvent.getPointRelativeToObject();
		scrollProgress = p.y / (double)menuMaxSize.y();
		scrollProgress = UtilsMath.clamp(scrollProgress, 0.0, 1.0);
		scrollOffset = (int)(scrollMax * scrollProgress);
		
		int index = Math.floorDiv((mouseEvent.getPointRelativeToObject().y + scrollOffset), buttonSize.y());
		if(index >= 0 && index < options.size()) hoverIndex = index;
			
		updateScrollButtonPosition();
		updateGraphics();	
		
	}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}

}
