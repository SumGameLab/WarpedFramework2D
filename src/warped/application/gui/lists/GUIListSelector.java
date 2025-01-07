/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui.lists;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import warped.WarpedProperties;
import warped.application.gui.WarpedGUI;
import warped.application.state.WarpedState;
import warped.user.actions.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.enums.generalised.Colour;
import warped.utilities.math.vectors.Vec2i;
import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsFont;
import warped.utilities.utils.UtilsImage;
import warped.utilities.utils.UtilsMath;

public class GUIListSelector<T> extends WarpedGUI {

	private Color backgroundColor = Colour.getColor(Colour.GREY_DARK);
	private Color borderColor = Color.BLACK;	

	private boolean isSingleton 			= true; //Allows for only one instance of each available option to be selected. i.e. if the list contains 'grass' option, you can not select grass multiple times
	private boolean isSelectedRemoved  		= true;	//Entrys that are selected from the available list will be removed when selected, and re-added if removed from the selection list
	
	private boolean isAvailableListHovered 	= false;
	private boolean isSelectedListHovered 	= false; 
	private boolean isDragging 	  	  		= false; //If dragging the scroll should be adjusted for the corresponding list
	
	private int selectionCap = 0;
	private WarpedAction selectAction = () -> {};
	
	private int borderThickness 	= 2;
	private int entryThickness 		= 48;
	private int scrollBarThickness 	= 10;
	
	private int listWidth 	= 300;
	private int listDiv     = listWidth / 2;
	private int listHeight 	= 360;
	
	private int hoveredIndex = -1;
	private Color hoverColor = Colour.getDarker(backgroundColor, 30);
	
	private int entryWidth = (listWidth / 2) - (scrollBarThickness);
	private int entryHeight = borderThickness + entryThickness;
 
	private int lineSpacing = 12;
	private Color textColor = Color.WHITE;
	private Font font 		= UtilsFont.getPreferred();
	protected Vec2i textOffset = new Vec2i(5, font.getSize());
	
	private Color scrollBarColor 	= Colour.getColor(Colour.GREY_DARK_DARK);
	private Color scrollButtonColor = Colour.getColor(Colour.GREY_LIGHT);
	private int scrollStep 		  	= 8;
	
	protected ArrayList<T> list;
	
	//-----------Available List ----
	protected ArrayList<List<String>>  availableEntrys  = new ArrayList<>();
	protected ArrayList<BufferedImage> availableIcons   = new ArrayList<>(); 
	private   ArrayList<Integer>       availableIndices = new ArrayList<>();
	
	private int availableScroll 			= 0;
	private double availableScrollProgress 	= 0.0;
	private int availableScrollMax 		  	= 0;
	private int availableButtonSize 		= 400;
	
	//----------Selected List -----
	private ArrayList<List<String>>  selectedEntrys   = new ArrayList<>();
	private ArrayList<BufferedImage> selectedIcons    = new ArrayList<>();
	private ArrayList<Integer> 	     selectedIndices = new ArrayList<>();
	
	private int    selectedScroll 			= 0;
	private double selectedScrollProgress 	= 0.0;
	private int    selectedScrollMax 		= 0;
	private int    selectedButtonSize 		= 400;
	
	
	
	public GUIListSelector() {
		setRaster(new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public GUIListSelector(int listWidth, int listHeight) {
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		
		setEntryDimensions();
		setRaster(new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public GUIListSelector(int listWidth, int listHeight, int selectionCap) {
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		
		setSelectionCap(selectionCap);
		setEntryDimensions();
		setRaster(new BufferedImage(listWidth, listHeight, WarpedProperties.BUFFERED_IMAGE_TYPE));
	}
	
	public void setList(ArrayList<T> list){Console.err("GUIListSelector -> setList(ArrayList) -> this method needs to be created as an annonymous inner method when this object is created");}
	
	public void initialize() {
		if(list == null || list.size() == 0) {
			Console.err("GUIListSelector -> initialize() -> must set list before initializing");
			return;
		}
		
		if(availableEntrys == null || availableEntrys.size() == 0) {
			Console.err("GUIListSelector -> initialize() -> must set available entrys before initializing");
			return;
		}
		if(list.size() != availableEntrys.size()) {Console.err("GUIListSelector -> initialize() -> size of list does not mach number of list entrys or number of list indices");}
		
		for(int i = 0; i < list.size(); i++) {availableIndices.add(i);}
		
		updateAvailableScrollParameters();
		if(isSingleton) {
			for(int i = 0; i < list.size(); i++) {
				T obj1 = list.get(i);
				for(int j = 0; j < list.size(); j++) {
					if(i == j) continue;
					T obj2 = list.get(j);
					if(obj1 == obj2) {
						list.remove(j);
						Console.err("GUIListSelector -> initialize() -> this list isSingleton, allows for only instance of each entry -> copys of entrys will be removed");
						availableEntrys.remove(j);
						availableIcons.remove(j);
						availableIndices.remove(j);
					}
				}
			}
		}
	}
	
	private void setEntryDimensions() {
		listDiv = listWidth / 2;
		entryWidth = listDiv - scrollBarThickness;
		entryHeight = borderThickness + entryThickness; 
		updateAvailableScrollParameters();
		updateSelectedScrollParameters();
	}
	//--------
	//------------- Access ------------
	//--------
	public void setTextOffset(int x, int y) {
		textOffset.set(x, y);
		updateGraphics();
	}
	
	public void setTextOffset(Vec2i textOffset) {
		this.textOffset.set(textOffset);
		updateGraphics();
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		updateGraphics();
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateGraphics();
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		updateGraphics();
	}
	
	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
		updateGraphics();
	}
	
	public void setTextFont(Font font) {
		this.font = font;
		updateGraphics();
	}
	
	public void setTextSize(int textSize) {font = UtilsFont.getPreferred(textSize);}
	
	public void setSelectAction(WarpedAction selectAction) {
		this.selectAction = selectAction;
	}
	public void setListSize(int listWidth, int listHeight) {
		if(listWidth < 50) {
			Console.err("GUIListSelector -> setListSize() -> listWidth is too small : " + listWidth);
			listWidth = 50;
		}
		if(listHeight < 50) {
			Console.err("GUIListSelector -> setListSize() -> ListHeight is too small : " + entryThickness);
			listHeight = 50;
		}
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		
		setEntryDimensions();
		updateGraphics();
	}
	
	public void setListSize(int listWidth, int listHeight, int entryThickness) {
		if(listWidth < 50) {
			Console.err("GUIListSelector -> setListSize() -> listWidth is too small : " + listWidth);
			listWidth = 50;
		}
		if(listHeight < 50) {
			Console.err("GUIListSelector -> setListSize() -> ListHeight is too small : " + entryThickness);
			listHeight = 50;
		}
		this.listWidth = listWidth;
		this.listHeight = listHeight;
		if(entryThickness < 5) {
			Console.err("GUIListSelector -> setListSize() -> entryThickness is too small : " + entryThickness);
			entryThickness = 5;
		}
		if(entryThickness > listHeight) {
			Console.err("GUIListSelector -> setListSize() -> entryThickness is too large : " + entryThickness);
			entryThickness = listHeight / 6;
		}
		this.entryThickness = entryThickness;
		
		setEntryDimensions();
		updateGraphics();
	}
	
	public void setSelectionCap(int selectionCap) {
		if(selectionCap <= 0) Console.err("GUIListSelector -> selectionCap must be positive to be applied, negative or 0 cap will be considered as no cap");
		this.selectionCap = selectionCap;
	}
	public int getSelectionCap() {return selectionCap;}
	public int getAvailableSelections() {return selectionCap - selectedEntrys.size();}
	public ArrayList<T> getSelected() {
		ArrayList<T> result = new ArrayList<>();
		for(int i = 0; i < selectedIndices.size(); i++) {
			result.add(list.get(selectedIndices.get(i)));
		}
		return result;
	}
	protected void clearList() {
		selectedEntrys.clear();
		selectedIcons.clear();
		availableEntrys.clear();
		availableIcons.clear();
		updateGraphics();
	}



	public void scrollAvailableUp() {
		if(availableScrollMax <= 0) return;
		Console.ln("GUIListSSelector -> scrollUp() -> scrollProgress : " + availableScrollProgress);
		availableScroll += scrollStep;
		if(availableScroll > availableScrollMax) availableScroll = availableScrollMax;
		availableScrollProgress = UtilsMath.dividePrecise(availableScroll, availableScrollMax);	
		updateAvailableGraphics();	
	}
	
	public void scrollAvailableDown() {
		if(availableScrollMax <= 0) return;
		Console.ln("GUIListSelector -> scrollDown() -> scrollProgress : " + availableScrollProgress);
		availableScroll -= scrollStep;
		if(availableScroll < 0) availableScroll = 0;
		availableScrollProgress = UtilsMath.dividePrecise(availableScroll, availableScrollMax);
		updateAvailableGraphics();		
	}
	
	public void scrollSelectedUp() {
		if(selectedScrollMax <= 0) return;
		Console.ln("GUIListScroll -> scrollUp() -> scrollProgress : " + selectedScrollProgress);
		selectedScroll += scrollStep;
		if(selectedScroll > selectedScrollMax) selectedScroll = selectedScrollMax;
		selectedScrollProgress = UtilsMath.dividePrecise(selectedScroll, selectedScrollMax);	
		updateSelectedGraphics();	
	}
	
	public void scrollSelectedDown() {
		if(selectedScrollMax <= 0) return;
		Console.ln("GUIListScroll -> scrollDown() -> scrollProgress : " + selectedScrollProgress);
		selectedScroll -= scrollStep;
		if(selectedScroll < 0) selectedScroll = 0;
		selectedScrollProgress = UtilsMath.dividePrecise(selectedScroll, selectedScrollMax);
		updateSelectedGraphics();		
	}
	//--------
	//------------- Update ------------
	//--------
	

	protected void updateGraphics() {
		updateAvailableGraphics();
		updateSelectedGraphics();
	}
	
	@Override
	protected void updateRaster() {return;}

	@Override
	protected void updateObject() {return;}

	@Override
	protected void updatePosition() {return;}

	private void updateAvailableScrollParameters() {
		availableScrollMax = ((availableEntrys.size() * entryHeight) - listHeight);
		if(availableScrollMax < 0) availableScrollMax = 0;
		availableButtonSize = listHeight - availableScrollMax;
		if(availableButtonSize < 20) availableButtonSize = 20;
		if(availableScroll > availableScrollMax) {
			availableScroll = availableScrollMax;
			updateGraphics();
		}
	}
	
	private void updateSelectedScrollParameters() {
		selectedScrollMax = ((selectedEntrys.size() * entryHeight) - listHeight);
		if(selectedScrollMax < 0) selectedScrollMax = 0;
		selectedButtonSize = listHeight - selectedScrollMax;
		if(selectedButtonSize < 20) selectedButtonSize = 20;
		if(selectedScroll > selectedScrollMax) {
			selectedScroll = selectedScrollMax;
			updateGraphics();
		}
	}
	
	//--------
	//------------- Graphics ------------
	//--------
	
	protected void updateAvailableGraphics() {
		BufferedImage img = UtilsImage.generateClone(raster);
		Graphics g = img.getGraphics();
		
		g.setColor(borderColor);
		g.fillRect(0, 0, listDiv, listHeight);
		
		g.setColor(backgroundColor);
		g.fillRect(borderThickness, borderThickness, listDiv - borderThickness * 2, listHeight - borderThickness * 2);
		
		for(int i = 0; i < availableEntrys.size(); i++) {			
			int ex = borderThickness;
			int ey = i * entryHeight - availableScroll;
			if(ex + entryWidth < 0 || ey + entryHeight < 0 || ex > listDiv || ey > listHeight) continue;
			
			g.setColor(borderColor);
			g.fillRect(ex, ey, listDiv, borderThickness);
			
			if(isAvailableListHovered && hoveredIndex == i) {
				g.setColor(hoverColor);
				g.fillRect(scrollBarThickness, ey + borderThickness, entryWidth, entryThickness);
			}

			g.setColor(textColor);
			g.setFont(font);
			List<String> entry = availableEntrys.get(i);
		
			
			for(int j = 0; j < entry.size(); j++) {
				int lx = ex + textOffset.x;
				int ly = ey + textOffset.y + j * lineSpacing;
				if(lx > ex + entryWidth || ly > ey + entryHeight) continue;
				g.drawString(entry.get(j), lx, ly);
			}
			
			if(availableIcons.get(i) == null) continue;
			g.drawImage(availableIcons.get(i), ex + listDiv - borderThickness - scrollBarThickness, ey, entryHeight, entryHeight, null);
		}

		g.setColor(scrollBarColor);
		g.fillRect(0, 0, scrollBarThickness, listHeight - borderThickness * 2 );
		
		int buttonY = (int)((listHeight * availableScrollProgress) - (availableButtonSize / 2));
		if(buttonY < 0) buttonY = 0;
		if(buttonY + availableButtonSize > listHeight) buttonY = listHeight - availableButtonSize;
		g.setColor(scrollButtonColor);
		g.fillRect(0, buttonY, scrollBarThickness, availableButtonSize);
		
		g.dispose();
		setRaster(img);
	}
	
	
	
	protected void updateSelectedGraphics() {
		BufferedImage img = UtilsImage.generateClone(raster);
		Graphics g = img.getGraphics();
		
		g.setColor(borderColor);
		g.fillRect(listDiv, 0, listDiv, listHeight);
		
		g.setColor(backgroundColor);
		g.fillRect(listDiv + borderThickness, borderThickness, listDiv - scrollBarThickness - borderThickness * 2, listHeight - borderThickness * 2);
		
		for(int i = 0; i < selectedEntrys.size(); i++) {			
			int ex = listDiv + borderThickness;
			int ey = i * entryHeight - selectedScroll;
			if(ex + entryWidth < listDiv || ey + entryHeight < 0 || ex > listWidth || ey > listHeight) continue;			
			g.setColor(borderColor);
			g.fillRect(ex, ey, listDiv - scrollBarThickness - borderThickness * 2, borderThickness);
			
			if(isSelectedListHovered && hoveredIndex == i) {
				g.setColor(hoverColor);
				g.fillRect(listDiv, ey + borderThickness, entryWidth, entryThickness);
			}
			
			g.setColor(textColor);
			g.setFont(font);
			List<String> entry = selectedEntrys.get(i);
			
			for(int j = 0; j < entry.size(); j++) {
				int lx = ex + textOffset.x;
				int ly = ey + textOffset.y + j * lineSpacing;
				if(lx > ex + entryWidth || ly > ey + entryHeight) continue;
				g.drawString(entry.get(j), lx, ly);
			}
			
			if(selectedIcons.get(i) == null) continue;
			g.drawImage(selectedIcons.get(i), ex + listDiv - borderThickness - scrollBarThickness, ey, entryHeight, entryHeight, null);
		}

		g.setColor(scrollBarColor);
		g.fillRect(listWidth - borderThickness - scrollBarThickness, borderThickness, scrollBarThickness, listHeight - borderThickness * 2 );
		
		int buttonY = (int)((listHeight * selectedScrollProgress) - (selectedButtonSize / 2));
		if(buttonY < 0) buttonY = 0;
		if(buttonY + selectedButtonSize > listHeight) buttonY = listHeight - selectedButtonSize;
		g.setColor(scrollButtonColor);
		g.fillRect(listWidth - borderThickness - scrollBarThickness, buttonY, scrollBarThickness, selectedButtonSize);
		
		g.dispose();
		setRaster(img);
	}
	
	
	//--------
	//------------- Interaction ------------
	//--------
	
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
		isAvailableListHovered = false;
		isSelectedListHovered = false;
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		isDragging = false;
		boolean swapedLists = false;
		if(mouseEvent.getPointRelativeToObject().x < listDiv) {
			if(isSelectedListHovered) {
				isSelectedListHovered  = false;
				swapedLists = true;
				updateSelectedGraphics();
			}
			isAvailableListHovered = true;				
		} else { 
			if(isAvailableListHovered) {
				isAvailableListHovered = false;
				swapedLists = true;
				updateAvailableGraphics();
			}
			isSelectedListHovered  = true;
		}
		
		int hovered = (int) (Math.floor((mouseEvent.getPointRelativeToObject().y + availableScroll)/entryHeight));
		if(hoveredIndex != hovered || swapedLists) {
			hoveredIndex = hovered;
			if(isAvailableListHovered) updateAvailableGraphics();
			else updateSelectedGraphics();
		}
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		if(mouseEvent.getPointRelativeToObject().x < listDiv) {
			if(availableScrollMax <= 0) return;
			double selectY = mouseEvent.getPointRelativeToObject().y;		
			isDragging = true;
			setAvailableScroll((int)((selectY / listHeight) * availableScrollMax));		
		} else {
			if(selectedScrollMax <= 0) return;
			double selectY = mouseEvent.getPointRelativeToObject().y;		
			isDragging = true;
			setSelectedScroll((int)((selectY / listHeight) * selectedScrollMax));
		}
	}
	
	public void setAvailableScroll(int scroll) {
		if(availableScrollMax <= 0) return;
		if(scroll > 0 && scroll < availableScrollMax) {
			availableScroll = scroll;
			availableScrollProgress = UtilsMath.dividePrecise(scroll, availableScrollMax);	
			updateAvailableGraphics();
		} else Console.err("GUIScrollList -> setScroll() -> scroll is outside of domain : (min, scroll, max) : (" + 0 + ", " + scroll + ", " + availableScrollMax + ")");
	}
	
	public void setSelectedScroll(int scroll) {
		if(selectedScrollMax <= 0) return;
		if(scroll > 0 && scroll < selectedScrollMax) {
			selectedScroll = scroll;
			selectedScrollProgress = UtilsMath.dividePrecise(scroll, selectedScrollMax);	
			updateSelectedGraphics();
		} else Console.err("GUIScrollList -> setScroll() -> scroll is outside of domain : (min, scroll, max) : (" + 0 + ", " + scroll + ", " + availableScrollMax + ")");
	}
	

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
	
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		int selectX = mouseEvent.getPointRelativeToObject().x;		
		if(selectX < listDiv) {
			if(availableEntrys.size() == 0) return;
			int selectY = mouseEvent.getPointRelativeToObject().y + availableScroll;
			
			if(availableScrollMax > 0  && isDragging) return;
			int entryIndex = (int) (Math.floor(selectY/entryHeight));
			if(entryIndex >= availableEntrys.size()) entryIndex = availableEntrys.size() - 1;
			if(entryIndex < 0) entryIndex = 0;
			hoveredIndex = entryIndex;
				
			if(selectionCap <= 0 ) selectAvailable(entryIndex);
			else if(selectionCap - selectedEntrys.size() > 0) selectAvailable(entryIndex);
			else WarpedState.notify.addNotification("Can't chose more");
				
		} else {
			if(selectedEntrys.size() == 0) return;
			int selectY = mouseEvent.getPointRelativeToObject().y + selectedScroll;
			
			if(selectedScrollMax > 0  && isDragging) return;
			int entryIndex = (int) (Math.floor(selectY/entryHeight));
			if(entryIndex >= selectedEntrys.size()) entryIndex = selectedEntrys.size() - 1;
			if(entryIndex < 0) entryIndex = 0;
			hoveredIndex = entryIndex;
				
			removeSelected(entryIndex);
		}
	}
	
	private void selectAvailable(int entryIndex) {
		selectedEntrys.add(availableEntrys.get(entryIndex));
		selectedIcons.add(availableIcons.get(entryIndex));
		selectedIndices.add(availableIndices.get(entryIndex));
		
		if(isSelectedRemoved) {
			availableEntrys.remove(entryIndex);
			availableIcons.remove(entryIndex);
			availableIndices.remove(entryIndex);
			updateAvailableScrollParameters();			
		}
		updateSelectedScrollParameters();
		updateGraphics();
		selectAction.action();
	}
	
	private void removeSelected(int selectIndex) {
		if(isSelectedRemoved) {
			availableEntrys.add(selectedEntrys.get(selectIndex));
			availableIcons.add(selectedIcons.get(selectIndex));
			availableIndices.add(selectedIndices.get(selectIndex));
			updateAvailableScrollParameters();	
		}
		
		selectedEntrys.remove(selectIndex);
		selectedIcons.remove(selectIndex);
		selectedIndices.remove(selectIndex);
		updateSelectedScrollParameters();
		updateGraphics();
		selectAction.action();
	}
	

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		if(mouseEvent.getPointRelativeToObject().x < listDiv) {			
			if(0 + mouseEvent.getWheelEvent().getWheelRotation() > 0) scrollAvailableUp();
			else scrollAvailableDown();
		} else {
			if(0 + mouseEvent.getWheelEvent().getWheelRotation() > 0) scrollSelectedUp();
			else scrollSelectedDown();
		}
	}
	
}
