/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state;

import warped.graphics.window.WarpedMouse;
import warped.utilities.utils.Console;

public abstract class WarpedApplication  {
		
	
	//FIXME  18/1/24 -> setup synchronized access to game state. Data could/will become corrupted otherwise -> LoL this has been broken since i made the program, when I try to implement a 'fix' it always tanks performance rip   
	//Warped game should control the monitor during update cycle. 
	//When cycle completes other threads (i.e. screenComposers) should be notified that they can access the context to get game data for rendering 
	
	/////////////////////////////////////////////////////////////
			
	public final void load() {
		Console.ln("WarpedGame -> Constructing Application : " + this.getClass().getSimpleName());
		WarpedMouse.setCursorState(WarpedMouse.LOAD);
		loadAssets();
		initializeApplication();
		WarpedMouse.setCursorState(WarpedMouse.PLAIN);
		
	}
	
	
	/**Do Not, call this manually, it is called automatically.
	 * This method will run once when the Application object is created
	 *Loading of all game assets -> audio and graphics should be loaded in this method*/
	protected abstract void loadAssets();
	
	/**Do Not, call this manually, it is called automatically.
	 * This method will run once when the loading screen is closed
	 * This method may be used to queue animations or events that should start immediately after loading*/
	public abstract void startApplication();
	
	/**This method is called when the warped2D cinematic finishes playing.
	 * */
	public abstract void endWarpedCinematic();
	
	/**Do Not, call this manually, it is called automatically.
	 * This method will be called at framerate -> typically 60 times a second
	 * This method should be used to check persistent game logic i.e. has a set of win conditions been met*/
	protected abstract void persistentLogic(); 
	
	/**Do Not, call this manually, it is called automatically.
	 * This method will be called once after the loadAssets() function has run
	 * This should be used to set up everything for your application.
	 * You will want to generated all the WarpedAssemblys that your application will need and assemble them. 
	 * You will need to create a WarpedKeyboardController and a WarpedMouseController
	 * You will need to generate your Viewports in the WarpedWindow class*/
	protected abstract void initializeApplication();
	
	/**Do Not, call this manually, it is called automatically.
	 * It is not typical to need to write any code here.
	 * This method will be called once when the application closes
	 * It is not necessary to unload any of the audio / graphic files if you used WarpedFolders,
	 * all warped folders will clean up their own assets when the application ends.
	 * */
	protected abstract void stopApplication();
	
	

}
