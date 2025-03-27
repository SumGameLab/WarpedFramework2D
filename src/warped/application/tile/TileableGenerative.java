package warped.application.tile;

import java.util.ArrayList;
import java.util.Map;

import warped.utilities.utils.Console;
import warped.utilities.utils.UtilsString;

public interface TileableGenerative<T extends Enum<?>> {
	
	
	/**To link you must return a Map of all types in the form :
	 * @return Map<Key, value> - a hashMap containing the values 
	 * @author 5som3 */
	public abstract Map<Integer, T> getMap();
	
	/**Get the specified type as a string.
	 * @param tileType - the tile type to get the string for.
	 * @return String - the tile type as a string.
	 * @author 5som3*/
	public static <K extends TileableGenerative<?>> String getString(K tileType) {return UtilsString.convertEnumToString((Enum<?>) tileType);}
	
	/**Get the this type as a string.
	 * @return String - this type as a string.
	 * @author 5som3*/
	public default String getString() {return getString(this);}
	
	/**Get the type at the specified index.
	 * @param index - the index to get.
	 * @return T - the type at the specified index.
	 * @author 5som3*/
	public default T get(int index) {
		if(index < 0 || index >= size()) {
			Console.err("WarpedTilable -> " + this.getClass() + " -> get() -> index out of bounds : " + index);
			index = 0;
		} return getMap().get(index);
	}
	
	/**The number of types for the WarpedTilable extension.
	 * @return int - the number of types.
	 * @author 5som3*/
	public default int size() {return getMap().size();}
	
	/**Get an array list containing one instance of each WarpedTileable.
	 * @returns an ordered ArrayList<T> of all the linkableTypes
	 * @author 5som3*/
	public default ArrayList<T> getAll(){
		ArrayList<T> result = new ArrayList<T>();
		for(int i = 0; i < size(); i++) result.add(get(i));
		return result;
	}
	
	/**Get a random tile
	 * @return T - a random instance of the WarpedTileable extension.
	 * @author 5som3*/
	public default T getRandom() {return get(size());}
	
	/**Get the tile roughness for this tile. This is a multiplier for the move speed of WarpedPathables. i.e. 0.5 = half move speed.
	 * @author 5som3*/
	@SuppressWarnings("unchecked")
	public default double getRoughness() {return getRoughness((T)this);};
	
	/**Get the tile roughness. This is a multiplier for the move speed of WarpedPathables. i.e. 0.5 = half move speed.
	 * @param tileType - the tile type to get the roughness for.
	 * @author 5som3*/
	public abstract double getRoughness(T tileType);
	
	
}
