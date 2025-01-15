package warped.application.tile;

import java.util.ArrayList;
import java.util.Map;

import warped.utilities.utils.UtilsString;

public interface WarpedTileable<T extends Enum<?>> {
	
	
	/**To link you must return a Map of all types in the form :
	 * @return Map<Key, value> = @return Map<Type.ordinal(), Type>
	 * @param Type - an enum value
	 * @param Type.ordinal() - the enum's ordinal
	 * */
	public abstract Map<Integer, T> getMap();
	public static <K extends WarpedTileable<?>> String getString(K tileType) {return UtilsString.convertEnumToString((Enum<?>) tileType);}
	public default String getString() {return getString(this);}
	public default T get(int index) {return getMap().get(index);}
	public default int size() {return getMap().size();}
	/**@returns an ordered ArrayList<T> of all the linkableTypes*/
	public default ArrayList<T> getAll(){
		ArrayList<T> result = new ArrayList<T>();
		for(int i = 0; i < size(); i++) result.add(get(i));
		return result;
	}
	
	public default T getRandom() {return get(size());}
	
	@SuppressWarnings("unchecked")
	public default String getDescription() {return getDescription((T)this);};
	@SuppressWarnings("unchecked")
	public default double getRoughness() {return getRoughness((T)this);};
	
	public abstract String getDescription(T tileType);
	public abstract double getRoughness(T tileType);
	
	
}
