/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.state.managers;

import java.util.ArrayList;
import java.util.HashMap;

import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.graphics.camera.WarpedCamera;
import warped.graphics.camera.WarpedCameraType;
import warped.utilities.enums.generalised.DirectionType;
import warped.utilities.math.vectors.VectorD;
import warped.utilities.utils.Console;

public class CameraManager {

	private static HashMap<WarpedCameraType, WarpedCamera> cameras = new HashMap<>();
	private static ArrayList<WarpedCameraType> activeCameras = new ArrayList<>();
	
	public CameraManager() {
		for(int i = 0; i < WarpedCameraType.size(); i++) {
			addCamera(new WarpedCamera(WarpedCameraType.get(i)));
		}		
	}
	//public void update() {cameras.forEach((e, c) -> {c.update();});}
		
	public WarpedCamera getDefaultCamera(WarpedManagerType managerType) {
		switch(managerType) {
		case DEPTH_FIELD: 	return cameras.get(WarpedCameraType.DEFAULT_DEPTH_FIELD);
		case ENTITIE:		return cameras.get(WarpedCameraType.DEFAULT_ENTITIE);
		case GUI:           return cameras.get(WarpedCameraType.DEFAULT_GUI);
		case ITEM:          return cameras.get(WarpedCameraType.DEFAULT_ITEM);
		case OBJECT:        return cameras.get(WarpedCameraType.DEFAULT_OBJECT);
		case TILE:          return cameras.get(WarpedCameraType.DEFAULT_TILE);
		default:
			Console.err("CameraManager -> getDefaultCamera -> invalid case : " + managerType);
			return cameras.get(WarpedCameraType.DEFAULT_ENTITIE); 
		}
	}
	public WarpedCamera getDefaultEntitieCamera() {return cameras.get(WarpedCameraType.DEFAULT_ENTITIE);}
	public WarpedCamera getDefaultDepthFieldfCamera() {return cameras.get(WarpedCameraType.DEFAULT_DEPTH_FIELD);}
	public WarpedCamera getDefaultGUICamera() {return cameras.get(WarpedCameraType.DEFAULT_GUI);}
	
	public static  HashMap<WarpedCameraType, WarpedCamera> getCameras(){return cameras;}
	public static ArrayList<WarpedCameraType> getActiveCameras(){return activeCameras;}
	
	public static WarpedCamera getCamera(WarpedCameraType id) {
		if(cameras.get(id) == null) {
			Console.err("CameraManager -> getCamera() -> null value found at index : " + id);
			return null;
		}		
		return cameras.get(id);
	}
	public void addCamera(WarpedCamera camera) {
		if(camera == null) {
			Console.err("CameraManager -> addCamera() -> passed null value");
			return;
		}
		if(camera.getID() == null){
			Console.err("CameraManager -> addCamera() -> camera has a null ID");
			return;
		}
		
		cameras.put(camera.getID(), camera);
		if(!activeCameras.contains(camera.getID()))	activeCameras.add(camera.getID());
	} 
	public void setCameras(HashMap<WarpedCameraType, WarpedCamera> cameras)  {
		if(cameras == null) {
			Console.err("CameraManager -> setCameras() -> passed null HashMap");
			
		}
		CameraManager.cameras = cameras;
	}
	public void clearCameras() {
		cameras.clear();
		activeCameras.clear();
	}
	
	//Collective camera operations
	public void setAllCameraPositions(VectorD position) {cameras.forEach((e, c) -> {	c.setPosition(position);});}
	public void setAllCameraZooms(double zoom) {cameras.forEach((e, c) -> {c.setZoom(zoom);});}
	public void moveAllCameras(VectorD vec) {cameras.forEach((e, c) -> {c.move(vec);});}
	public void zoomInAllCameras() {cameras.forEach((e, c) -> {c.zoomIn();});}
	public void panAllCameras(DirectionType dir) {
		switch(dir) {
		case UP:         cameras.forEach((e, c) -> {c.panUp();}); 	 return;
		case DOWN:       cameras.forEach((e, c) -> {c.panDown();});  return;
		case LEFT:       cameras.forEach((e, c) -> {c.panLeft();});  return;
		case RIGHT:      cameras.forEach((e, c) -> {c.panRight();}); return;
		case UP_LEFT:    cameras.forEach((e, c) -> {c.panUp();   c.panLeft();});  return;
		case UP_RIGHT:   cameras.forEach((e, c) -> {c.panUp();   c.panRight();}); return;
		case DOWN_LEFT:  cameras.forEach((e, c) -> {c.panDown(); c.panLeft();});  return;
		case DOWN_RIGHT: cameras.forEach((e, c) -> {c.panDown(); c.panRight();}); return;
		default: Console.err("CameraManager -> panAllCameras() -> invalid switch value : " + dir);		
		}
	}
	
	
	
}
