/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.generalised;

public enum DataType {

	INTEGER,
	DOUBLE,
	FLOAT,
	LONG;
	
	
	public static <T extends Number> DataType getDataType(T data){
		if(data instanceof Integer) return INTEGER;
		if(data instanceof Double) 	return DOUBLE;
		if(data instanceof Float) 	return FLOAT;
		if(data instanceof Long) 	return LONG;
		
		System.err.println("DataType -> getDataType() -> data type not supported, add support for data type : " + data.getClass().toString());
		return null;
	}
	
}
