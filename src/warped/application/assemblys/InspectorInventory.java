/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import warped.application.entities.item.ItemBindable;
import warped.application.gui.GUIButton;
import warped.application.gui.GUIInventory;
import warped.application.state.GUIAssembly;
import warped.application.state.WarpedInventory;
import warped.application.state.WarpedState;
import warped.application.state.WarpedInventory.SortType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.KeyboardIcons;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;

public class InspectorInventory<T extends ItemBindable<?>> extends GUIAssembly {

	
	private static final int SORT_ASSENDING 	= 0;
	private static final int SORT_DESSENDING 	= 1;
	
	private int alphabetState 	= 0;
	private int massState 		= 0;
	private int quantityState 	= 0;
	private int valueState 		= 0;
		
	private GUIButton 	 title			= new GUIButton(100, 30);
	private GUIButton 	 close			= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIButton 	 sortAlphabet	= new GUIButton(FrameworkSprites.getKeyboardIcon(KeyboardIcons.A));
	private GUIButton 	 sortMass		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.MASS));
	private GUIButton 	 sortQuantity	= new GUIButton(FrameworkSprites.getKeyboardIcon(KeyboardIcons.ONE));
	private GUIButton 	 sortValue		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.DOLLAR));
	private GUIInventory<T> invent;
	
	
	public InspectorInventory(int rows, int columns) {
		super();
		invent = new GUIInventory<>(rows, columns);
	}
	
	/**Select an inventory to display the items it contains
	 * @param invent - the inventory to display. 
	 * @author 5som3*/
	public void selectInventory(WarpedInventory<T> invent) {
		this.invent.selectInventory(invent);
	}

	@Override
	protected void defineAssembly() {
		title.setButtonSize(invent.getWidth() - 150, 30);
		title.setPosition(100, 100);
		
		close.setOffset(invent.getWidth() - 30, 0);       
		sortAlphabet.setOffset(invent.getWidth() - 60, 0);
		sortMass.setOffset(invent.getWidth() - 90, 0);    
		sortQuantity.setOffset(invent.getWidth() - 120, 0);
		sortValue.setOffset(invent.getWidth() - 150, 0);   
		invent.setOffset(0, 30);      
		
		close.offset(title);
		sortAlphabet.offset(title);
		sortMass.offset(title);
		sortQuantity.offset(title);
		sortValue.offset(title);
		invent.offset(title);
		

		title.setDragable(true);
		title.setDraggedAction(() -> {WarpedState.guiManager.getGroup(groupID).forEach(i -> {i.offset(title);});});
		
		close.setReleasedAction(mouseE -> {close();});
		
		sortAlphabet.setReleasedAction(mouseE -> {
			if(alphabetState == SORT_ASSENDING) {
				alphabetState = SORT_DESSENDING;
				invent.sort(SortType.SORT_ALPHABET_REVERSE);
			} else {
				alphabetState = SORT_ASSENDING;
				invent.sort(SortType.SORT_ALPHABET);
			}
			
		});
		
		sortMass.setReleasedAction(mouseE -> {
			if(massState == SORT_ASSENDING) {
				massState = SORT_DESSENDING;
				invent.sort(SortType.SORT_MASS_REVERSE);
			} else {
				massState = SORT_ASSENDING;
				invent.sort(SortType.SORT_MASS);
			}
		});
		
		sortQuantity.setReleasedAction(mouseE -> {
			if(quantityState == SORT_ASSENDING) {
				quantityState = SORT_DESSENDING;
				invent.sort(SortType.SORT_QUANTITY_REVERSE);
			} else {
				quantityState = SORT_ASSENDING;
				invent.sort(SortType.SORT_QUANTITY);
			}
		});	
		
		sortValue.setReleasedAction(mouseE -> {
			if(valueState == SORT_ASSENDING) {
				valueState = SORT_DESSENDING;
				invent.sort(SortType.SORT_VALUE_REVERSE);
			} else {
				valueState = SORT_ASSENDING;
				invent.sort(SortType.SORT_VALUE);
			}
		});	
	
	}

	
	@Override
	protected void addAssembly() {
		addMember(title);         
		addMember(close);         
		addMember(sortAlphabet);  
		addMember(sortMass);      
		addMember(sortQuantity);  
		addMember(sortValue);     
		addMember(invent);        
	}

	

}
