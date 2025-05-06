/* WarpedFramework 2D - java API - Copyright (C) 2021-2025 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import warped.application.assemblys.AssemblyHotBar;
import warped.application.assemblys.AssemblyPopUpDialogueBox;
import warped.application.assemblys.ConsoleInput;
import warped.application.assemblys.FileExplorer;
import warped.application.assemblys.InspectorFramework;
import warped.application.assemblys.InspectorItem;
import warped.application.assemblys.InspectorObject;
import warped.application.assemblys.Notify;
import warped.application.assemblys.Selector;
import warped.application.assemblys.ToolTip;
import warped.application.gui.WarpedGUI;
import warped.audio.FrameworkAudio;
import warped.utilities.utils.Console;

public class WarpedState {
	
	public static long activeCycleDuration  = 0;
	public static long midCycleDuration		= 0;
	public static long slowCycleDuration	= 0;
	public static long passiveCycleDuration = 0;
	
	public static short cycleCount = 0;
	
	private static Timer activeTimer  	 = new Timer("Timer Thread : State Timer");
	private static TimerTask activeTask  = new TimerTask() {public void run() {updateActive();}};
	private static TimerTask midTask 	 = new TimerTask() {public void run() {updateMid();}};
	private static TimerTask slowTask 	 = new TimerTask() {public void run() {updateSlow();}};
	private static TimerTask passiveTask = new TimerTask() {public void run() {updatePassive();}};
	
	private static boolean pause = true;
	private static boolean isInitialized = false;
	
	public static final WarpedManager<WarpedObject> objectManager = new WarpedManager<WarpedObject>("Object Manager");	
	public static final WarpedManager<WarpedGUI> guiManager = new WarpedManager<WarpedGUI>("GUI Manager");
	private static WarpedManager<?>[] managers = new WarpedManager[] {objectManager, guiManager};	
	
	protected static ArrayList<WarpedAudioFolder<?>> audioFolders = new ArrayList<>();
	protected static ArrayList<WarpedAssembly> assemblys = new ArrayList<>();

	public static InspectorObject 				gameObjectInspector;
	public static InspectorFramework 			frameworkInspector;
	public static InspectorItem					itemInspector;

	public static Selector 						selector;
	public static ToolTip 						toolTip;
	public static Notify 						notify;
	public static AssemblyHotBar 				hotBar;	
	public static AssemblyPopUpDialogueBox 		dialogue;
	public static FileExplorer 					fileExplorer;
	public static ConsoleInput 					consoleInput;
	
	
	/**The application state, only one instance should ever exist*/
	protected WarpedState() {	
		activeTimer.scheduleAtFixedRate(activeTask, 0, 17);
		activeTimer.scheduleAtFixedRate(midTask, 0, 1000);
		activeTimer.scheduleAtFixedRate(slowTask, 0, 60000);
		activeTimer.scheduleAtFixedRate(passiveTask, 0, 3600000);
	}
	
	/**The duration of the last active update cycle
	 * @return long - the duration in nano-seconds.
	 * @implNote active update cycle is scheduled to run every 17ms (approximate 60hz) 
	 * @author 5som3*/
	public static long getActiveCycleDuration()  {return activeCycleDuration;}
	
	/**The duration of the last mid update cycle
	 * @return long - the duration in nano-seconds.
	 * @implNote mid update cycle is scheduled to run once a second.
	 * @author 5som3*/
	public static long getMidCycleDuration()     {return midCycleDuration;}
	
	/**The duration of the last slow update cycle
	 * @return long - the duration in nano-seconds.
	 * @implNote slow update cycle is scheduled to run ever minute.
	 * @author 5som3*/
	public static long getSlowCycleDuration()    {return slowCycleDuration;}
	
	/**The duration of the last passive update cycle
	 * @return long - the duration in nano-seconds.
	 * @implNote the passive update cycle is scheduled to run once an hour
	 * @author 5som3*/
	public static long getPassiveCycleDuration() {return passiveCycleDuration;}
	
	/**The number of cycles completed by the active update loop since the last time this method was called.
	 * @return short - the active update cycle count
	 * @author 5som3*/
	public static short getCycleCount() {
		short val = cycleCount;
		cycleCount = 0;
		return val;
	}
	
	/**Closes all assemblys in the state
	 * @author 5som3*/
	public static void closeAssemblys() {assemblys.forEach(a -> {a.close();});}
	
	/**Cancels all update tasks
	 * @author 5som3*/
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
	
	
	/**Total number of objects
	 * @return int - the object count.
	 * @author 5som3*/
	public static int getObjectCount() {
		int gameObjectCount = 0;
		for(int i = 0; i < managers.length; i++){
			gameObjectCount += managers[i].getObjectCount();
		}
		return gameObjectCount;
	}
	
	/**The total number of objects that are active
	 * @return int - the active object count
	 * @author 5som3*/
	public static int getActiveObjectCount() {
		int activeGameObjectCount = 0;
		for(int i = 0; i < managers.length; i++) {
			activeGameObjectCount += managers[i].getOpenObjectCount();
		}
		return activeGameObjectCount;
	}
	
	/**Add a manager to the state
	 * @author 5som3*/
	public static void addManager(WarpedManager<?> manager) {
		for(int i = 0; i < managers.length; i++) {
			if(managers[i].UNIQUE_ID == manager.UNIQUE_ID) {
				Console.err("WarpedState -> addManager() -> manager already exist in state");
				return;
			}
		}
		WarpedManager<?>[] managers = new WarpedManager<?>[WarpedState.managers.length + 1];
		for(int i = 0; i < WarpedState.managers.length; i++) {
			managers[i] = WarpedState.managers[i];
			Console.ln("WarpedState -> addManager() -> adding manager : " + managers[i].name);
		}
		managers[managers.length - 1] = manager;
		WarpedState.managers = managers;
	}
	
	/**Get an object using an objectID
	 * @param objectID - the ID of the object to get.
	 * @return WarpedObject - the object to get
	 * @author 5som3*/
	public static WarpedObject getMember(WarpedObjectIdentity objectID) {return getManager(objectID).getMember(objectID);}
	
	/**Remove a member from group that contains it.
	 * @param objectID - the id of the object to remove.
	 * @author 5som3*/
	public static void removeMember(WarpedObjectIdentity objectID) {getManager(objectID).getGroup(objectID).removeMember(objectID);}
	
	/**Get the manager that contains the specified object ID.
	 * @param objectID - ID of the object to check
	 * @return WarpedManager<?> - the manager that contains the object
	 * @author 5som3*/
	public static WarpedManager<?> getManager(WarpedObjectIdentity objectID) {return getManager(objectID.getGroupID());}
	
	/**Get the manager that contains the specified group ID.
	 * @param groupID - the id of the group to check.
	 * @return WarpedManager<?> - the manager that contains the group
	 * @author 5som3*/
	public static WarpedManager<?> getManager(WarpedGroupIdentity groupID) {return managers[groupID.getManagerID()];}
	
	/**Get the group specified by the groupID
	 * @param groupID - ID of the group to get.
	 * @return WarpedGroup<?> - the group for the specified the groupID
	 * @author 5som3*/
	public static WarpedGroup<?> getGroup(WarpedGroupIdentity groupID){return getManager(groupID).getGroup(groupID);}
	
	/**Change the Open/Closed state of the specified group.
	 * @param groupID - the ID of the group to toggle.
	 * @author 5som3*/
	public static void toggleGroup(WarpedGroupIdentity groupID) {getManager(groupID).toggleGroup(groupID);}
	
	/**Open the specified group.
	 * @param group - the group to open.
	 * @author 5som3*/
	public static void openGroup(WarpedGroup<?> group) {getManager(group.getGroupID()).openGroup(group.getGroupID());}
	
	/**Open the specified group
	 * @param groupID - the ID of the group to open.
	 * @author 5som3*/
	public static void openGroup(WarpedGroupIdentity groupID) {getManager(groupID).openGroup(groupID);}
	
	/**Close the specified group
	 * @param groupID -  the ID of the group to close
	 * @author 5som3*/
	public static void closeGroup(WarpedGroupIdentity groupID) {getManager(groupID).closeGroup(groupID);}
	
	/**Is the specified group open
	 * @param groupID - the group to check.
	 * @return boolean - true if the group is open, else false.
	 * @author 5som3*/
	public static boolean isGroupOpen(WarpedGroupIdentity groupID) {return getManager(groupID).isOpen(groupID);}
	
	/**Pause updating the managers of the game state 
	 * @author 5som3*/
	public static void pause() {
		Console.ln("WarpedState -> pause()");
		pause = true;
	}
	
	/**Resume updating the managers of the game state
	 * @author 5som3*/
	public static void play() {
		Console.ln("WarpedState -> play()");
		pause =  false;
	}
	
	/**Change the Play/Pause condition of the state
	 * @author 5som3*/
	public static void togglePause() { 
		if(pause) pause = false;
		else pause = true;
	}
	
	/**Is the state updating
	 * @return boolean - if true the state will update its managers else false.
	 * @author 5som3*/
	public static boolean isPaused() {return pause;}
	
	/**Update 60 times per second*/
	private static void updateActive() {
		if(pause) return;
		long cycleStartTime = System.nanoTime();
		cycleCount++;
		
		for(int i = 0; i < managers.length; i++) {
			managers[i].updateActive();
			managers[i].removeDead();
		}
		
		for(WarpedAssembly assembly : assemblys) assembly.updateAssembly();
		for(WarpedAudioFolder<?> folder : audioFolders) folder.update();
		FrameworkAudio.update();
		WarpedFramework2D.getApp().persistentLogic();
		
		activeCycleDuration = System.nanoTime() - cycleStartTime;
	}
	
	/**Update once per second*/
	private static void updateMid() {
		if(pause) return; 
		long cycleStartTime = System.nanoTime();
		for(int i = 0; i < managers.length; i++) managers[i].updateMid();
		midCycleDuration = System.nanoTime() - cycleStartTime;
	}
	
	/**Update once per minute*/
	private static void updateSlow() {
		System.gc();
		if(pause) return; 
		long cycleStartTime = System.nanoTime();
		for(int i = 0; i < managers.length; i++) managers[i].updateSlow();
		slowCycleDuration = System.nanoTime() - cycleStartTime;
	}

	/**Update once per hour*/
	private static void updatePassive() {
		long cycleStartTime = System.nanoTime();
		for(int i = 0; i < managers.length; i++) managers[i].updatePassive();
		passiveCycleDuration = System.nanoTime() - cycleStartTime;
	}
	
	/**Initialize the default assemblys*/
	protected void initializeAssemblys() {
		if(isInitialized) {
			Console.err("WarpedState -> initialize() -> state is already initialized");
			return;
		}
		isInitialized = true;
		
		toolTip = new ToolTip();
		toolTip.assemble();
		
		selector = new Selector();
		selector.assemble();
		
		gameObjectInspector = new InspectorObject();
		gameObjectInspector.assemble();
		
		frameworkInspector = new InspectorFramework();
		frameworkInspector.assemble();
		
		dialogue = new AssemblyPopUpDialogueBox();
		dialogue.assemble();
		
		notify = new Notify();
		notify.assemble();
		notify.open();

		consoleInput = new ConsoleInput();
		consoleInput.assemble();
				
		hotBar = new AssemblyHotBar();
		hotBar.assemble();
		
		fileExplorer = new FileExplorer();
		fileExplorer.assemble();
	}
	
}
