/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import warped.WarpedFramework2D;
import warped.application.assemblys.FileExplorer;
import warped.application.assemblys.AssemblyHotBar;
import warped.application.assemblys.AssemblyItemInspector;
import warped.application.assemblys.AssemblyPopUpDialogueBox;
import warped.application.assemblys.ConsoleInput;
import warped.application.assemblys.InspectorFramework;
import warped.application.assemblys.InspectorObject;
import warped.application.assemblys.Notify;
import warped.application.assemblys.Selector;
import warped.application.assemblys.ToolTip;
import warped.application.depthFields.WarpedDepthField;
import warped.application.entities.WarpedEntitie;
import warped.application.entities.item.WarpedItem;
import warped.application.gui.WarpedGUI;
import warped.application.object.WarpedObject;
import warped.application.object.WarpedObjectIdentity;
import warped.application.state.groups.WarpedGroup;
import warped.application.state.groups.WarpedGroupIdentity;
import warped.application.state.managers.CameraManager;
import warped.application.state.managers.gameObjectManagers.ManagerDepthField;
import warped.application.state.managers.gameObjectManagers.ManagerEntitie;
import warped.application.state.managers.gameObjectManagers.ManagerGUI;
import warped.application.state.managers.gameObjectManagers.ManagerItem;
import warped.application.state.managers.gameObjectManagers.ManagerTile;
import warped.application.state.managers.gameObjectManagers.WarpedManager;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.application.tile.WarpedTile;
import warped.audio.FrameworkAudio;
import warped.utilities.utils.Console;

public class WarpedState {
	
	public static long activeCycleDuration  = 0;
	public static long midCycleDuration		= 0;
	public static long slowCycleDuration	= 0;
	public static long passiveCycleDuration = 0;
	
	public static short cycleCount = 0;
	
	
	//private final static ScheduledExecutorService executor = Executors.newScheduledThreadPool(4, new WarpedThreadFactory("Warped State"));
	private static Timer activeTimer  	 = new Timer("Timer Thread : State Timer");
	private static TimerTask activeTask  = new TimerTask() {public void run() {updateActive();}};
	private static TimerTask midTask 	 = new TimerTask() {public void run() {updateMid();}};
	private static TimerTask slowTask 	 = new TimerTask() {public void run() {updateSlow();}};
	private static TimerTask passiveTask = new TimerTask() {public void run() {updatePassive();}};
	
	private static boolean pause = true;
	private static boolean isInitialized = false;
	 
	public static CameraManager 			    cameraManager     = new CameraManager();
	
	//Context managers - contains game objects for the declared type
	//These fields will need to be serialized for save / load
	
	private static final WarpedManager<?>[] managers = new WarpedManager[6];
	static {
		managers[WarpedManagerType.OBJECT.ordinal()] 	 = WarpedManager.generateObjectManager();
		managers[WarpedManagerType.ENTITIE.ordinal()] 	 = WarpedManager.generateEntitieManager();
		managers[WarpedManagerType.DEPTH_FIELD.ordinal()]= WarpedManager.generateDepthFieldManager();
		managers[WarpedManagerType.GUI.ordinal()]  		 = WarpedManager.generateGUIManager();
		managers[WarpedManagerType.TILE.ordinal()] 		 = WarpedManager.generateTileManager();
		managers[WarpedManagerType.ITEM.ordinal()] 		 = WarpedManager.generateItemManager();;
	}
	
	
	@SuppressWarnings("unchecked")
	public static final WarpedManager<WarpedObject>      	objectManager     =  (WarpedManager<WarpedObject>) managers[WarpedManagerType.OBJECT.ordinal()];
	@SuppressWarnings("unchecked")
	public static final ManagerEntitie<WarpedEntitie>       entitieManager    =  (ManagerEntitie<WarpedEntitie>) managers[WarpedManagerType.ENTITIE.ordinal()];
	@SuppressWarnings("unchecked")
	public static final ManagerDepthField<WarpedDepthField> depthFieldManager =  (ManagerDepthField<WarpedDepthField>) managers[WarpedManagerType.DEPTH_FIELD.ordinal()];	
	@SuppressWarnings("unchecked")
	public static final ManagerGUI<WarpedGUI>        		guiManager        =  (ManagerGUI<WarpedGUI>) managers[WarpedManagerType.GUI.ordinal()];
	@SuppressWarnings("unchecked")
	public static final ManagerTile<WarpedTile>		    	tileManager 	  =  (ManagerTile<WarpedTile>) managers[WarpedManagerType.TILE.ordinal()];
	@SuppressWarnings("unchecked")
	public static final ManagerItem<WarpedItem<?>>			itemManager	  	  =  (ManagerItem<WarpedItem<?>>) managers[WarpedManagerType.ITEM.ordinal()];
	
	protected static ArrayList<WarpedAudioFolder<?>> audioFolders = new ArrayList<>();
	protected static ArrayList<WarpedAssembly> assemblys = new ArrayList<>();
	

	public static InspectorObject 				gameObjectInspector;
	public static InspectorFramework 			frameworkInspector;
	public static AssemblyItemInspector 		itemInspector;

	public static Selector 						selector;
	public static ToolTip 						toolTip;
	public static Notify 						notify;
	public static AssemblyHotBar 				hotBar;	
	public static AssemblyPopUpDialogueBox 		dialogue;
	public static FileExplorer 					fileExplorer;
	public static ConsoleInput 					consoleInput;
	
	public static long getActiveCycleDuration()  {return activeCycleDuration;}
	public static long getMidCycleDuration()     {return midCycleDuration;}
	public static long getSlowCycleDuration()    {return slowCycleDuration;}
	public static long getPassiveCycleDuration() {return passiveCycleDuration;}
	public static short getCycleCount() {
		short val = cycleCount;
		cycleCount = 0;
		return val;
	}
	
	/*
	public static void closeAssemblys() {
		selector.close();
		gameObjectInspector.close();
		toolTip.close();
		dialogue.close();
		//itemInspector.close();
		//hotBar.close();
	}
	*/
	
	public WarpedState() {	
		activeTimer.scheduleAtFixedRate(activeTask, 0, 17);
		activeTimer.scheduleAtFixedRate(midTask, 0, 1000);
		activeTimer.scheduleAtFixedRate(slowTask, 0, 60000);
		activeTimer.scheduleAtFixedRate(passiveTask, 0, 3600000);
	}
	
	public void stop() {
		Console.ln("WarpedState -> stop()");
		activeTask.cancel();
		midTask.cancel();
		slowTask.cancel();
		passiveTask.cancel();
		
		activeTimer.cancel();
	
		
		for(int i = 0; i < audioFolders.size(); i++) {
			WarpedAudioFolder<?> folder = audioFolders.get(i);
			folder.stop();
			folder.close();	
		}
	}
	
	
	private static void updateActive() {
		if(pause) return;
		long cycleStartTime = System.nanoTime();
		cycleCount++;
		
		for(int i = 0; i < managers.length; i++) {
			managers[i].updateActive();
			managers[i].remove();
		}
		
		for(WarpedAssembly assembly : assemblys) assembly.updateAssembly();
		for(WarpedAudioFolder<?> folder : audioFolders) folder.update();
		FrameworkAudio.update();
		WarpedFramework2D.getApp().persistentLogic();
		
		activeCycleDuration = System.nanoTime() - cycleStartTime;
	}
	

	private static void updateMid() {
		if(pause) return; 
		long cycleStartTime = System.nanoTime();
		for(int i = 0; i < managers.length; i++) managers[i].updateMid();
		midCycleDuration = System.nanoTime() - cycleStartTime;
	}
	
	private static void updateSlow() {
		System.gc();
		if(pause) return; 
		long cycleStartTime = System.nanoTime();
		for(int i = 0; i < managers.length; i++) managers[i].updateSlow();
		slowCycleDuration = System.nanoTime() - cycleStartTime;
	}

	
	private static void updatePassive() {
		long cycleStartTime = System.nanoTime();
		for(int i = 0; i < managers.length; i++) managers[i].updatePassive();
		passiveCycleDuration = System.nanoTime() - cycleStartTime;
	}
	
	
	public void initialize() {
		if(isInitialized) {
			Console.err("WarpedState -> initialize() -> state is already initialized");
			return;
		}
		isInitialized = true;
		
		toolTip = new ToolTip(WarpedManagerType.GUI);
		toolTip.assemble();
		
		selector = new Selector(WarpedManagerType.GUI);
		selector.assemble();
		
		gameObjectInspector = new InspectorObject(WarpedManagerType.GUI);
		gameObjectInspector.assemble();
		
		frameworkInspector = new InspectorFramework(WarpedManagerType.GUI);
		frameworkInspector.assemble();
		
		dialogue = new AssemblyPopUpDialogueBox(WarpedManagerType.GUI);
		dialogue.assemble();
		
		notify = new Notify(WarpedManagerType.GUI);
		notify.assemble();
		notify.open();

		consoleInput = new ConsoleInput(WarpedManagerType.GUI);
		consoleInput.assemble();
		
		itemInspector = new AssemblyItemInspector(WarpedManagerType.GUI);
		itemInspector.assemble();
		
		hotBar = new AssemblyHotBar(WarpedManagerType.GUI);
		hotBar.assemble();
		
		fileExplorer = new FileExplorer(WarpedManagerType.GUI);
		fileExplorer.assemble();
	}
		
	public static int getGameObjectCount() {
		int gameObjectCount = 0;
		
		gameObjectCount += entitieManager.getGameObjectCount();
		gameObjectCount += depthFieldManager.getGameObjectCount();
		gameObjectCount += guiManager.getGameObjectCount();
		gameObjectCount += tileManager.getGameObjectCount();
		
		return gameObjectCount;
	}
	public static int getActiveGameObjectCount() {
		int activeGameObjectCount = 0;
		
		activeGameObjectCount += entitieManager.getActiveGameObjectCount();
		activeGameObjectCount += depthFieldManager.getActiveGameObjectCount();
		activeGameObjectCount += guiManager.getActiveGameObjectCount();
		activeGameObjectCount += tileManager.getActiveGameObjectCount();
		
		return activeGameObjectCount;
	}
	/**Useful when you only have the ID for an object that you need to modify
	 * Note : if you possible use a more specific access method (i.e. call the manager directly) to avoid unnecessary switching
	 * @param  id of the game object to return
	 * @return the game object that the id refers to*/
	public static WarpedObject getGameObject(WarpedObjectIdentity objectID) {return getManager(objectID.getManagerType()).getMember(objectID);}
	@SuppressWarnings("rawtypes")
	public static WarpedManager getManager(WarpedObjectIdentity objectID) {return getManager(objectID.getManagerType());}
	@SuppressWarnings("rawtypes")
	public static WarpedManager getManager(WarpedGroupIdentity groupID) {return getManager(groupID.getManagerType());}
	@SuppressWarnings("rawtypes")
	public static WarpedManager getManager(WarpedManagerType managerID) {
		switch(managerID) {
		case DEPTH_FIELD: 	return depthFieldManager;
		case ENTITIE: 		return entitieManager;
		case GUI: 			return guiManager;
		case TILE: 			return tileManager;
		case ITEM:			return itemManager;
		case OBJECT:		return objectManager;
		default:
			Console.err("GameContext -> getManager(ContextManagers) -> invalid case : " + managerID);
			return null;
		}
	}
	
	public static WarpedGroup<?> getGroup(WarpedGroupIdentity groupID){return getManager(groupID.getManagerType()).getGroup(groupID);}
	public static void toggleGroup(WarpedGroupIdentity groupID) {getManager(groupID.getManagerType()).toggleGroup(groupID);}
	public static void openGroup(WarpedGroup<?> group) {getManager(group.getGroupID().getManagerType()).openGroup(group.getGroupID());}
	public static void openGroup(WarpedGroupIdentity groupID) {getManager(groupID.getManagerType()).openGroup(groupID);}
	public static void closeGroup(WarpedGroupIdentity groupID) {getManager(groupID.getManagerType()).closeGroup(groupID);}
	public static boolean isGroupOpen(WarpedGroupIdentity groupID) {return getManager(groupID.getManagerType()).isGroupOpen(groupID);}
	public static void pause() {
		Console.ln("WarpedState -> pause()");
		pause = true;
	}
	public static void play() {
		Console.ln("WarpedState -> play()");
		pause =  false;
	}
	public static void togglePause() { 
		if(pause) pause = false;
		else pause = true;
	}
	public static boolean isPaused() {return pause;}
	
	@SuppressWarnings("unchecked")
	public static void addGameObject(WarpedGroupIdentity groupID, WarpedObject object) {
		getManager(groupID).addMember(groupID, object);
	}
	
}
