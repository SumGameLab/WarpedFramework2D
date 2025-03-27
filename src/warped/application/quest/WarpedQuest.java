/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.quest;

import java.util.ArrayList;

public class WarpedQuest {
	
	private String name = "defaultQuest";
	private ArrayList<String> description = new ArrayList<>(); 
	private boolean isComplete;
	
	private ArrayList<QuestStage> stages = new ArrayList<>();
	
	
	public WarpedQuest(String name) {
		this.name = name;	
	}
	
	public WarpedQuest(String name, QuestStage stage) {
		this.name = name;	
		addStage(stage);
	} 
	
	
	public String getName() {return name;}
	public void update() {
		stages.forEach(s -> {s.checkCondition();});
		for(QuestStage stage : stages) {
			if(!stage.isComplete) return;  
		}
		isComplete = true;
	}
	
	public void addStage(QuestStage stage) {
		stages.add(stage);
		for(String string : stage.getDescription()) {
			description.add(string);
		}		
		description.add("");
	}
	
	public boolean isComplete() {return isComplete;}

}
