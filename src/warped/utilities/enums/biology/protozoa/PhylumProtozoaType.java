/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.enums.biology.protozoa;

import java.util.HashMap;
import java.util.Map;

import warped.utilities.utils.UtilsMath;

public enum PhylumProtozoaType {

	ANCYROMONADIDA,
	MALAWIMONADA,
	CRUMS,
	AMOEBOZOA,
	BREVIATEA,
	APUSOMONADIDA,
	HOLOMYCOTA,
	HOLLOZOA,
	METAMONADA,
	DISCOBA,
	CRYPTISTA,
	RHODOPHYTA,
	PICOZOA,
	GLAUCOPHYTA,
	VIRIDIPLANTAE,
	HEMIMASTIGOPHORA,
	PROVORA,
	HAPTISTA,
	TELONEMIA,
	RHIZARIA,
	LVEOLATA,
	STRAMENOPILES;
	
	private static Map<Integer, PhylumProtozoaType> map = new HashMap<>();
	static {
		for (PhylumProtozoaType type : PhylumProtozoaType.values()) {
            map.put(type.ordinal(), type);
        }
	}
	
	public static PhylumProtozoaType get(int index) {
	    return (PhylumProtozoaType) map.get(index);
	}
	public static PhylumProtozoaType getRandomType() {return get(UtilsMath.random.nextInt(size()));}
	public static int size() {return map.size();}
	
	
}
