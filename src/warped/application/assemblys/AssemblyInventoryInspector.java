/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.assemblys;

import warped.application.gui.GUIButton;
import warped.application.gui.GUIInventoryItems;
import warped.application.object.WarpedObjectIdentity;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.sprite.spriteSheets.FrameworkSprites;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.KeyboardIcons;
import warped.graphics.sprite.spriteSheets.FrameworkSprites.StandardIcons;

public class AssemblyInventoryInspector extends WarpedAssembly {

	//NOTE - If your looking for the WarpTech Inventory the calss is CargobayInspector, not inventory inspector
	
	private static final int SORT_ASSENDING 	= 0;
	private static final int SORT_DESSENDING 	= 1;
	
	private int aAlphabetState 	= 0;
	private int aMassState 		= 0;
	private int aQuantityState 	= 0;
	private int aValueState 	= 0;

	private int bAlphabetState 	= 0;
	private int bMassState 		= 0;
	private int bQuantityState 	= 0;
	private int bValueState  	= 0;
	
	//private boolean isAVisible = true;
	//private boolean isBVisible = true;
	
	private GUIButton 	 aTitle			= new GUIButton(100, 30);
	private GUIButton 	 aClose			= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIButton 	 aSortAlphabet	= new GUIButton(FrameworkSprites.getKeyboardIcon(KeyboardIcons.A));
	private GUIButton 	 aSortMass		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.MASS));
	private GUIButton 	 aSortQuantity	= new GUIButton(FrameworkSprites.getKeyboardIcon(KeyboardIcons.ONE));
	private GUIButton 	 aSortValue		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.DOLLAR));
	private GUIInventoryItems aInvent	= new GUIInventoryItems(64, 64);
	
	private GUIButton 	 bTitle			= new GUIButton(100, 30);
	private GUIButton 	 bClose			= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.CLOSE));
	private GUIButton 	 bSortAlphabet	= new GUIButton(FrameworkSprites.getKeyboardIcon(KeyboardIcons.A));
	private GUIButton 	 bSortMass		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.MASS));
	private GUIButton 	 bSortQuantity	= new GUIButton(FrameworkSprites.getKeyboardIcon(KeyboardIcons.ONE));
	private GUIButton 	 bSortValue		= new GUIButton(FrameworkSprites.getStandardIcon(StandardIcons.DOLLAR));	
	private GUIInventoryItems bInvent	= new GUIInventoryItems(64, 64);	
	
	public AssemblyInventoryInspector(WarpedManagerType type) {
		super(type);
	}


	
	private void aInvisible() {
		//isAVisible = false;
		aTitle.invisible();         
		aClose.invisible();         
		aSortAlphabet.invisible();  
		aSortMass.invisible();      
		aSortQuantity.invisible();  
		aSortValue.invisible();     
		aInvent.invisible();        
	}
	private void aVisible() {
		//isAVisible = true;
		aTitle.visible();         
		aClose.visible();         
		aSortAlphabet.visible();  
		aSortMass.visible();      
		aSortQuantity.visible();  
		aSortValue.visible();     
		aInvent.visible();        
	}
	private void bInvisible() {
		//isBVisible = false;
		bTitle.invisible();         
		bClose.invisible();         
		bSortAlphabet.invisible();
		bSortMass.invisible();      
		bSortQuantity.invisible();  
		bSortValue.invisible();     
		bInvent.invisible();        
	}
	private void bVisible() {
		//isBVisible = true;
		bTitle.visible();         
		bClose.visible();         
		bSortAlphabet.visible();  
		bSortMass.visible();      
		bSortQuantity.visible();  
		bSortValue.visible();     
		bInvent.visible();        
	}
	
	public void selectInventoryA(WarpedGroupIdentity inventID) {
		aVisible();
		bInvisible();
		aInvent.selectInventory(inventID);
	}
	public void selectInventoryB(WarpedGroupIdentity inventID) {
		bVisible();
		aInvisible();
		bInvent.selectInventory(inventID);
	}
	public void selectInventorys(WarpedGroupIdentity aInventID, WarpedGroupIdentity bInventID) {
		aVisible();
		bVisible();
		aInvent.selectInventory(aInventID); 
		bInvent.selectInventory(bInventID);
	}
	
	@Override
	protected void defineAssembly() {
		aTitle.setPosition(100, 100);
		aClose.setPosition(300, 100);       
		aSortAlphabet.setPosition(100, 130);
		aSortMass.setPosition(130, 130);    
		aSortQuantity.setPosition(160, 130);
		aSortValue.setPosition(190, 130);   
		aInvent.setPosition(100, 160);      
		
		bTitle.setPosition(600, 100);       
		bClose.setPosition(900, 100);      
		bSortAlphabet.setPosition(200, 100);
		bSortMass.setPosition(230, 100);    
		bSortQuantity.setPosition(260, 100);
		bSortValue.setPosition(290, 100);   
		bInvent.setPosition(100, 130);      
		
		//--------
		//--------------- Invent A ------------------
		//--------
		aTitle.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {					
				WarpedObjectIdentity id = i.getObjectID();
				if(id.isEqualTo(bTitle) || id.isEqualTo(bSortAlphabet) || id.isEqualTo(bSortMass) || id.isEqualTo(bSortQuantity) || id.isEqualTo(bSortValue) || id.isEqualTo(bClose));
				else {					
					i.offset(aTitle);
				}
			});			
		});
		
		aClose.setReleasedAction(mouseE -> {aInvisible();});
		
		aSortAlphabet.setReleasedAction(mouseE -> {
			if(aAlphabetState == SORT_ASSENDING) {
				aAlphabetState = SORT_DESSENDING;
			} else {
				aAlphabetState = SORT_ASSENDING;
			}
		});
		
		aSortMass.setReleasedAction(mouseE -> {
			if(aMassState == SORT_ASSENDING) {
				aMassState = SORT_DESSENDING;
			} else {
				aMassState = SORT_ASSENDING;
			}
		});
		
		aSortQuantity.setReleasedAction(mouseE -> {
			if(aQuantityState == SORT_ASSENDING) {
				aQuantityState = SORT_DESSENDING;
			} else {
				aQuantityState = SORT_ASSENDING;
			}
		});	
		
		aSortValue.setReleasedAction(mouseE -> {
			if(aValueState == SORT_ASSENDING) {
				aValueState = SORT_DESSENDING;
			} else {
				aValueState = SORT_ASSENDING;
			}
		});	
		
		
		//--------
		//--------------- Invent B ------------------
		//--------
		bTitle.setDraggedAction(() -> {
			WarpedState.guiManager.getGroup(groupID).forEach(i -> {					
				WarpedObjectIdentity id = i.getObjectID();
				if(id.isEqualTo(aTitle) || id.isEqualTo(aSortAlphabet) || id.isEqualTo(aSortMass) || id.isEqualTo(aSortQuantity) || id.isEqualTo(aSortValue) || id.isEqualTo(aClose));
				else {					
					i.offset(bTitle);
				}
			});			
		});
		
		bClose.setReleasedAction(mouseE -> {bInvisible();});
		
		bSortAlphabet.setReleasedAction(mouseE -> {
			if(bAlphabetState == SORT_ASSENDING) {
				bAlphabetState = SORT_DESSENDING;
			} else {
				bAlphabetState = SORT_ASSENDING;
			}
		});
		
		bSortMass.setReleasedAction(mouseE -> {
			if(bMassState == SORT_ASSENDING) {
				bMassState = SORT_DESSENDING;
			} else {
				bMassState = SORT_ASSENDING;
			}
		});
		
		bSortQuantity.setReleasedAction(mouseE -> {
			if(bQuantityState == SORT_ASSENDING) {
				bQuantityState = SORT_DESSENDING;
			} else {
				bQuantityState = SORT_ASSENDING;
			}
		});	
		
		bSortValue.setReleasedAction(mouseE -> {
			if(bValueState == SORT_ASSENDING) {
				bValueState = SORT_DESSENDING;
			} else {
				bValueState = SORT_ASSENDING;
			}
		});	
	}

	
	@Override
	protected void addAssembly() {
		addMember(aTitle);         
		addMember(aClose);         
		addMember(aSortAlphabet);  
		addMember(aSortMass);      
		addMember(aSortQuantity);  
		addMember(aSortValue);     
		addMember(aInvent);        
		                
		addMember(bTitle);         
		addMember(bClose);         
		addMember(bSortAlphabet);  
		addMember(bSortMass);      
		addMember(bSortQuantity);  
		addMember(bSortValue);     
		addMember(bInvent);       
	}

	

}
