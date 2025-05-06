/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/
package warped.application.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import warped.WarpedProperties;
import warped.application.state.WarpedGroup;
import warped.application.state.WarpedGroupIdentity;
import warped.application.state.WarpedState;
import warped.application.state.managers.gameObjectManagers.WarpedManagerType;
import warped.application.tile.WarpedTile;
import warped.functionalInterfaces.WarpedAction;
import warped.user.mouse.WarpedMouseEvent;
import warped.utilities.math.vectors.VectorI;
import warped.utilities.utils.Console;

public class GUITileMap extends WarpedGUI {

	public static final double ZOOM_OUT_SCALE  = 0.5;
	public static final double ZOOM_DEF_SCALE  = 1.0;
	public static final double ZOOM_IN_SCALE   = 2.0;
	protected VectorI tileSize = new VectorI(); //The size of the unmodified tile 
	
	protected double drawScale = ZOOM_OUT_SCALE; 
	protected VectorI gridOffset = new VectorI();
	protected VectorI mapGridSize = new VectorI();
	
	protected VectorI tileMapSize = new VectorI();
	
	protected WarpedGroup<WarpedTile> group;
	protected WarpedGroupIdentity groupID;
	
	
	protected BufferedImage mapComposite;
	
	protected int hx, hy = hx = -1;
	protected Color hoverColor = new Color(60, 60, 60, 60); 
	
	protected WarpedTile selectTile;
	protected WarpedAction altSelectAction = () -> {Console.ln("GUITileMap -> default altSelectAction");};
	
	
	//--------
	//---------------- Constructors --------
	//--------
	public GUITileMap(int width, int height) {
		tileMapSize.set(width, height);
		setSize(width, height);
	}
	
	
	//--------
	//---------------- Access --------
	//--------
	public WarpedTile getSelectTile() {return selectTile;}
	public void setAltSelectAction(WarpedAction altSelectAction) {this.altSelectAction = altSelectAction;}
	public VectorI getPixelOffset() {return new VectorI(gridOffset.x() * tileSize.x(), gridOffset.y() * tileSize.y());}
	public VectorI getGridOffset() {return gridOffset;}
	public VectorI getTileSize() {return tileSize;}
	public double getDrawScale() {return drawScale;}
	public void zoomIn() {
		if(drawScale != ZOOM_IN_SCALE) {
			drawScale = ZOOM_IN_SCALE;
		}
	}
	public void zoomOut() {
		if(drawScale != ZOOM_OUT_SCALE) {
			drawScale = ZOOM_OUT_SCALE;
		}
	}
	public void zoomReset() {
		if(drawScale != ZOOM_DEF_SCALE) {
			drawScale = ZOOM_DEF_SCALE;
		}
	}
	public void resetPan() {gridOffset.zero(); combineComposites();}
	public boolean panUp() {
		if(gridOffset.y() == 0) return false;
		gridOffset.add(0, 1);
		return true;
	}
	public boolean panDown() {
		int sy = (int)(tileSize.y() * drawScale);
		if(gridOffset.y() <= - (mapGridSize.y() - (tileMapSize.y() / sy) )) return false;
		gridOffset.add(0, -1); 
		return true;
	}
	public boolean panLeft() {
		if(gridOffset.x() == 0) return false;
		gridOffset.add(1, 0);
		return true;
	}
	public boolean panRight() {
		int sx = (int)(tileSize.x() * drawScale);
		if(gridOffset.x() <= - (mapGridSize.x() - (tileMapSize.x() / sx) )) return false;
		gridOffset.add(-1, 0);
		return true;
	}	
	
	public WarpedTile getTile(VectorI vec) {return getTile(vec.x(), vec.y());}
	@SuppressWarnings("unchecked")
	public WarpedTile getTile(int x, int y) {
		if(x < 0 || y < 0 || x > group.getMapGridSize().x() || y > group.getMapGridSize().y()) {
			Console.err("GUITileMap -> getTile() -> out of bounds : ( " + x + ", " + y + ") ");
			return null;
		}
		
		return group.getMember(x + y * group.getMapGridSize().x());
	}
	
	//--------
	//---------------- Selection --------
	//--------
	public WarpedGroupIdentity getGroupID() {return groupID;}
	
	public void selectTileMap(WarpedGroupIdentity groupID){
		if(groupID.getManagerType() != WarpedManagerType.TILE) {
			Console.err("GUITileMap -> selectTileMap() -> tried to select a group that is not a tile group : " + groupID.getManagerType());
			return;
		}
		
		this.groupID = groupID;
		group 		 = WarpedState.tileManager.getGroup(groupID);
		tileSize 	 = WarpedState.tileManager.getGroup(groupID).getMemberSize();
		mapGridSize = WarpedState.tileManager.getGroup(groupID).getMapGridSize();
		gridOffset.set(-(int)((mapGridSize.x() * 0.5) - ((tileMapSize.x() * 0.5) / tileSize.x())), -(int)((mapGridSize.y() * 0.5) - ((tileMapSize.y() * 0.5) / tileSize.y())));
				
		updateGraphics();
	}
	
		
	//--------
	//---------------- Graphics --------
	//--------
	public void updateGraphics() {
		updateMapComposite();
		combineComposites();
	}
	
	public void combineComposites() {
		BufferedImage img = new BufferedImage(tileMapSize.x(), tileMapSize.y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		Graphics g = img.getGraphics();
		
		int drawSizeX =	(int)(tileSize.x() * mapGridSize.x() * drawScale); 
		int drawSizeY =	(int)(tileSize.y() * mapGridSize.y() * drawScale);
		
		int drawX = (int)(tileSize.x() * drawScale * gridOffset.x());
		int drawY = (int)(tileSize.y() * drawScale * gridOffset.y());
		
		g.drawImage(mapComposite, drawX, drawY, drawSizeX, drawSizeY, null); //Draw the tiles image
		drawMouseHover(g);
		
		g.dispose();
		setRaster(img);
	}

	public void updateMapComposite() {
		BufferedImage img = new BufferedImage(group.getMapPixelSize().x(), group.getMapPixelSize().y(), WarpedProperties.BUFFERED_IMAGE_TYPE);
		
		Graphics g = img.getGraphics();

		for(int y = 0; y < mapGridSize.y(); y++) {
			for(int x = 0; x < mapGridSize.x(); x++) {
							
				int drawX = x * tileSize.x();
				int drawY = y * tileSize.y();
				
				WarpedTile tile = group.getMember(x + y * mapGridSize.x());
				g.drawImage(tile.raster(), drawX, drawY, tileSize.x(), tileSize.y(), null);				
			}
		}
		g.dispose();
		mapComposite = img;		
	}
	
	protected void drawMouseHover(Graphics g) {
		g.setColor(hoverColor);
		int sx = (int)(tileSize.x() * drawScale);
		int sy = (int)(tileSize.y() * drawScale);
		g.fillRect((int)((hx + gridOffset.x()) * sx), (int)((hy + gridOffset.y()) * sy), sx, sy);
	}

	//--------
	//---------------- Update --------
	//--------
	
	@Override
	public void updateObject() {
		combineComposites();
	}
	
	
	
	
	//--------
	//---------------- Interaction --------
	//--------
	@Override
	protected void mouseEntered() {return;}

	@Override
	protected void mouseExited() {
		if(hx != -1 && hy != -1);
		hx = hy = -1;
	}

	@Override
	protected void mouseMoved(WarpedMouseEvent mouseEvent) {
		int sx = (int)(tileSize.x() * drawScale);
		int sy = (int)(tileSize.y() * drawScale);
		
		int hoverX = (int) Math.floor(mouseEvent.getPointRelativeToObject().x / sx);
		int hoverY = (int) Math.floor(mouseEvent.getPointRelativeToObject().y / sy);
		
		hoverX -= gridOffset.x();
		hoverY -= gridOffset.y();
		
		if(hoverX < 0 || hoverY < 0 || hoverX >= mapGridSize.x() || hoverY >= mapGridSize.y()) return;
		if(hoverX != hx || hoverY != hy) {
			hx = hoverX;
			hy = hoverY;
		}
	}

	@Override
	protected void mouseDragged(WarpedMouseEvent mouseEvent) {
		int sx = (int)(tileSize.x() * drawScale);
		int sy = (int)(tileSize.y() * drawScale);
		
		int hoverX = (int) Math.floor(mouseEvent.getPointRelativeToObject().x / sx);
		int hoverY = (int) Math.floor(mouseEvent.getPointRelativeToObject().y / sy);
		
		hoverX -= gridOffset.x();
		hoverY -= gridOffset.y();
		
		
		if(hoverX < 0 || hoverY < 0 || hoverX >= group.getMapGridSize().x() || hoverY >= group.getMapGridSize().y()) return;
		if(hoverX != hx || hoverY != hy) {
			hx = hoverX;
			hy = hoverY;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void mouseReleased(WarpedMouseEvent mouseEvent) {		
		int sx = (int)(tileSize.x() * drawScale);
		int sy = (int)(tileSize.y() * drawScale);
		
		int rx = (int) Math.floor(mouseEvent.getPointRelativeToObject().x / sx);
		int ry = (int) Math.floor(mouseEvent.getPointRelativeToObject().y / sy);
		
		rx -= gridOffset.x();
		ry -= gridOffset.y();
		
		if(rx < 0 || ry < 0 || rx >= group.getMapGridSize().x() || ry >= group.getMapGridSize().y()) return;
		

		MouseEvent me = mouseEvent.getMouseEvent();
		if(me.getButton() == MouseEvent.BUTTON3) {
			selectTile = group.getMember(rx + ry * group.getMapGridSize().x());
			altSelectAction.action();
		}
		else {			
			selectTile = group.getMember(rx + ry * group.getMapGridSize().x());
			selectTile.mouseEvent(mouseEvent);
		}
		
	}

	
	@Override
	protected void mousePressed(WarpedMouseEvent mouseEvent) {return;}
	@Override
	protected void mouseRotation(WarpedMouseEvent mouseEvent) {return;}


}
