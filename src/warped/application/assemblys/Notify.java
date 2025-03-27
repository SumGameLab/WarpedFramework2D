/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.assemblys;

import java.util.ArrayList;

import warped.application.gui.GUINotification;
import warped.application.state.WarpedAssembly;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.utilities.utils.Console;

public class Notify extends WarpedAssembly {
		
	public enum StyleType {
		DEFAULT;
	}
	
	private StyleType style = StyleType.DEFAULT; 
	private String lastNote = "";
	
	private ArrayList<Integer> indices = new ArrayList<>();
	
	public Notify(WarpedManagerType type) {
		super(type);
	}

	public void note(String text) {
		if(!WarpedState.guiManager.isGroupOpen(groupID)) open();
		if(lastNote.equals(text)) return;
		else lastNote = text;
		GUINotification note; 
		
		boolean found = false;
		boolean skip = false;
		int index = -1;
		while(!found) {
			skip = false;
			index++;
			for(int i = 0; i < indices.size(); i++) {
				if(indices.get(i).intValue() == index) {
					skip = true;
					break;
				};
			}
			if(skip) continue;
			else {
				indices.add(index);
				found = true;
			}
		}
		
		switch(style) {
		case DEFAULT: 
			note = new GUINotification(text, index);
			note.setKeyFrames(-note.getWidth(), index * note.getHeight(), 0, index * note.getHeight(), () -> {note.setIsStarted(true);}, () -> {freeIndex(note.getIndex()); note.kill();});
			break;
		default: 
			Console.err("Notify -> note() -> invalid style : " + style);
			return;
		}
		
		addMember(note);
		note.start();	
		
		Console.ln("Notify -> note() -> added note : " + text);
	}
	
	public void freeIndex(Integer index) {
		for(int i = 0; i < indices.size(); i++) {
			if(indices.get(i).equals(index)) {
				indices.remove(i);
				if(indices.size() == 0) {
					lastNote = "";
					close();
				} 
				return;
			}
		}
		Console.err("Notify -> freeIndex() -> has no index " + index + " to free");
	}
	
	@Override
	protected void defineAssembly() {return;}

	@Override
	protected void addAssembly() {return;}	
	

}
