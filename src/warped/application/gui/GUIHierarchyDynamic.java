/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.gui;

import java.util.ArrayList;
import java.util.HashMap;

import warped.application.object.WarpedObject;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.Vec2i;

public class GUIHierarchyDynamic<T extends WarpedObject> extends WarpedGUI{

	public class HierarchyNode<T extends WarpedObject> {

		private T object;
		
		private HierarchyNode<T> parent;
		private Vec2i position;
		
		public HierarchyNode<T> getParent(){return parent;}
		
		
		public HierarchyNode(HierarchyNode<T> parent, T object) {
			this.object = object;
			this.parent = parent;
			
		}
		
		public void setPosition(Vec2i position) {this.position = position;}
		
	}

	
	private ArrayList<Integer> tiers = new ArrayList<>();
	private HashMap<Integer, ArrayList<HierarchyNode<T>>> hierarchy = new HashMap<>();
	
	
	public void setHierarchy(ArrayList<T> objects) {}
	
	private GUIHierarchyDynamic() {
		hierarchy.put(0, new ArrayList<HierarchyNode<T>>());
		tiers.add(0);
	}
	
	
	public void addNode(HierarchyNode<T> parent, T object, int tier) {
		if(!hierarchy.containsKey(tier)) {
			hierarchy.put(tier, new ArrayList<HierarchyNode<T>>());
			tiers.add(tier);
		}
		hierarchy.get(tier).add(new HierarchyNode<T>(parent, object));
	}
	
	/*
	private void updateNodePositions() {
		for(int i = tiers.size() - 1; i >= 0; i--) {
			int tier = tiers.get(i);
			
		}
	}
	*/

	@Override
	protected void mouseEntered() {

		
	}

	@Override
	protected void mouseExited() {
		
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		
	}

	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {
		
	}

	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {
		
	}

	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {
		
	}

	@Override
	protected void updateRaster() {
		
	}

	@Override
	protected void updateObject() {
		
	}

	@Override
	protected void updatePosition() {

		
	}
	
}
