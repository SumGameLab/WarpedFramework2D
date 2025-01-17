/* WarpedFramework 2D - java API - Copyright (C) 2021-2025 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import warped.utilities.utils.UtilsString;

public interface ItemBindable<T extends Enum<?>> {
	
	
	public abstract Map<Integer, T> getMap();
	@SuppressWarnings("unchecked")
	public default String getString() {return UtilsString.convertEnumToString((T) this);}
	public default T get(int index) {return getMap().get(index);}
	public default int size() {return getMap().size();}
	/**@returns an ordered ArrayList<T> of all the linkableTypes*/
	public default ArrayList<T> getAll(){
		ArrayList<T> result = new ArrayList<T>();
		for(int i = 0; i < size(); i++) result.add(get(i));
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public default BufferedImage getRaster() {return getRaster((T)this);}
	public abstract BufferedImage getRaster(T itemType);
	
	@SuppressWarnings("unchecked")
	public default String getDescription() {return getDescription((T)this);};
	public abstract String getDescription(T itemType);
	
	@SuppressWarnings("unchecked")
	/**The Value of one
	 * Implementation of value is up to the developer.
	 * */
	public default int getValue() {return getValue((T)this);};
	/**The Value of one
	 * Implementation of value is up to the developer.
	 * */
	public abstract int getValue(T itemType);
		
	@SuppressWarnings("unchecked")
	/**The Mass of one*/
	public default double getMass() {return getMass((T)this);};
	/**The Mass of one*/
	public abstract double getMass(T itemType);
	
	@SuppressWarnings("unchecked")
	public default <K extends ItemBindable<?>> WarpedItem<K> generateItem(int quantity) {return new WarpedItem<K>((K)this, quantity);}
	
	public static <K extends ItemBindable<?>, L extends ItemBindable<?>> boolean isSameBindable(K a, L b) {if(a.getClass() == b.getClass()) return true; else return false;}
	public default <K extends ItemBindable<?>> boolean isSameBindable(K b) {return isSameBindable(this, b);}
	
	public static  <K extends ItemBindable<?>, L extends ItemBindable<?>> boolean isSameType(K a, L b) {if(isSameBindable(a, b) && a == b) return true; else return false;}
	public default  <K extends ItemBindable<?>>boolean isSameType(K b) {return isSameType(this, b);}
	
}
