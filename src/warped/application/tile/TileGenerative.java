package warped.application.tile;

public class TileGenerative<T extends TileableGenerative<? extends Enum<?>>> extends WarpedTile {

	
	protected TileSetGenerative<T> parent; 
	protected T tileType;
	
	/**
	 * */
	public TileGenerative(TileSetGenerative<T> parent, T tileType) {
		this.parent = parent;
		this.tileType = tileType;
		getSprite().paint(parent.getTileImage(tileType));
		initializeSelectOptions();
	}
	
	
	public double getRoughness() {return tileType.getRoughness();}
	
	public T getPrimaryType() {return tileType;}
	
	public TileSetGenerative<T> getParent() {return parent;}	
	public TerrainTileSet<T> getParentTerrain() {return (TerrainTileSet<T>) parent;}
	
	
}
