/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.artifact;

import warped.application.object.WarpedObjectIdentity;
import warped.utilities.enums.generalised.RuneType;

public class RuneTabletArtifact extends ItemArtifact {

	private RuneType runeType;
	
	public RuneTabletArtifact(RuneType runeType) {
		super(ArtifactType.RUNE_TABLET);
	}
	
	public static RuneTabletArtifact generateRandomRuneTablet() {return new RuneTabletArtifact(RuneType.getRandomRuneType());} 
	
	@Override
	public void effect(WarpedObjectIdentity objectID) {
		//IMPLEMENT-> 20/5/24 -> effect method / system. I may have already made one? Investigate further
	}

	@Override
	protected void updateRaster() {return;}

	public RuneType getRuneType() {return runeType;}
	
	
}
