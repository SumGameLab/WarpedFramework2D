/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils.path;

import warped.utilities.math.vectors.VectorD;
import warped.utilities.math.vectors.VectorI;

public interface WarpedPathable {

	VectorD getPosition();
	
	void setCurrentCoordinate(VectorI coord);
	VectorI getCurrentCoordinate();
	double getMoveSpeed();
	

}

