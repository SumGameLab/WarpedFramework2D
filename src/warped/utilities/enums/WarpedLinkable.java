/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums;

import java.util.ArrayList;
import java.util.Map;

import warped.utilities.utils.UtilsString;

public interface WarpedLinkable<T extends Enum<?>> {
	
	/**To link you must return a Map of all types in the form :
	 * @return Map<Key, value> = @return Map<Type.ordinal(), Type>
	 * @param Type - an enum value
	 * @param Type.ordinal() - the enum's ordinal
	 * */
	public abstract Map<Integer, T> getMap();
	public default String getString(T value) {return UtilsString.convertEnumToString(value);}
	public default T get(int index) {return getMap().get(index);}
	public default int size() {return getMap().size();}
	/**@returns an ordered ArrayList<T> of all the linkableTypes*/
	public default ArrayList<T> getAll(){
		ArrayList<T> result = new ArrayList<T>();
		for(int i = 0; i < size(); i++) result.add(get(i));
		return result;
	}
	
}
