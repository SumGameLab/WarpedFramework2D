/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.utilities.utils.path;

import warped.utilities.math.vectors.Vec2d;
import warped.utilities.math.vectors.Vec2i;

public interface WarpedPathable {

	Vec2d getPosition();
	
	void setCurrentCoordinate(Vec2i coord);
	Vec2i getCurrentCoordinate();
	double getMoveSpeed();
	

}

