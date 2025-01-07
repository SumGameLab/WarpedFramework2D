/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.quest;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestStage {

	private QuestStageType type;
	private QuestRewardType rewardType;
	private ArrayList<String> description = new ArrayList<>();
	
	boolean isComplete = false;
	private WarpedCondition condition;
	
	private ArrayList<QuestActionType> actions;
	
	public QuestStage(WarpedCondition condition) {
		this.condition = condition;
	}
	public QuestStage(WarpedCondition condition, List<QuestActionType> actions) {
		
	}
	public QuestStage(QuestStageType type, WarpedCondition condition) {
		this.type = type;
		this.condition = condition;
	}
	
	public void checkCondition() {
		if(isComplete) return;
		else isComplete = condition.isTrue();
	}
	public boolean isComplete() {return isComplete;}
	public QuestRewardType getRewardType() {return rewardType;}
	public QuestStageType getType() {return type;}
	public ArrayList<QuestActionType> getActions(){return actions;}
	public List<String> getDescription() {return description;}

	
}
