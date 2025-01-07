/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.utils.Console;

public class GUIHierarchyStatic<T extends GUIDrawable> extends WarpedGUI {

	/*
	private int hierarchyWidth = 1000;
	private int hierarchyHeight = 900;
	
	private int nodeWidth  = 80;
	private int nodeHeight = 80;
	
	private Vec2i nodeSpacing  = new Vec2i(200);
	 */
	
	
	private ArrayList<String> tiers;
	private HashMap<String, Integer> tiersIndex;
	private HashMap<String, ArrayList<String>> tierNodes;
	
	protected HashMap<String, HashMap<String, T>> hierarchy;
	
	public GUIHierarchyStatic() {
		
		
	}
	
	public void setTiers(List<String> tiers) {
		tiers = new ArrayList<>();
		for(int i = 0; i < tiers.size(); i++) {
			tiers.add(tiers.get(i));
			tiersIndex.put(tiers.get(i), i);
		}
	}

	public void setTierNodes(String tier, List<String> nodes) {
		if(!tiersIndex.containsKey(tier)) {
			Console.err("GUIHierarchyStatic -> setTierNodes() -> hierarchy has no tier : " + tier);
			return;
		}
		tierNodes.put(tier, new ArrayList<String>(nodes));
	}
	
	public void addMembersToHierarchy(List<T> objects) {addMembersToHierarchy(new ArrayList<T>(objects));}
	public void addMembersToHierarchy(ArrayList<T> objects) {for(int i = 0; i < objects.size(); i++) sortMember(objects.get(i));}
	public void addMemberToHierarchy(T object) {sortMember(object);}
	protected void sortMember(T object) {Console.err("GUIHierarchyStatic -> sortMember() -> this method must be crated as an annonymous inner method");};
	
	/*
	private void updateGraphics() {
		for(int i = 0; i < tiers.size(); i++) {
			ArrayList<String> nodes = tierNodes.get(tiers.get(i));
			for(int j = 0; j < nodes.size(); j++) {
				//GameObject obj = hierarchy.get(tiers.get(i)).get(nodes.get(j));
				
			}
		}
	}
	*/

	@Override
	protected void mouseEntered() {return;}


	@Override
	protected void mouseExited() {return;}


	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {return;}


	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {return;}


	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}


	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {return;}


	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


	@Override
	protected void updateRaster() {return;}


	@Override
	protected void updateObject() {return;}


	@Override
	protected void updatePosition() {return;}
	
	
	
	
}
