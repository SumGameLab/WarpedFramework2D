/* WarpedFramework 2D - java API - Copyright (C) 2021-2024 Angelo Wilson | released under LGPL 2.1-or-later https://opensource.org/license/lgpl-2-1*/

package warped.application.entities.item.artifact;

import java.awt.image.BufferedImage;

import warped.application.entities.item.WarpedItem;
import warped.application.entities.item.ItemType;
import warped.application.object.WarpedObjectIdentity;
import warped.utilities.enums.generalised.RuneType;

public class ItemArtifact extends WarpedItem{

	private ArtifactType artifactType;
	

	public ItemArtifact(ArtifactType artifactType) {
		super(ItemType.ARTIFACT, artifactType.getString(), new BufferedImage(1,1,1));
		this.artifactType = artifactType;
	}
	
	public ArtifactType getArtifactType() {return artifactType;}

	@Override
	public void effect(WarpedObjectIdentity objectID) {
		// TODO 20/5/24 -> implement artifact effects
		
	}

	@Override
	protected void updateRaster() {return;}
		
	public static ItemArtifact generateRandomRuneTablet() {
		return new RuneTabletArtifact(RuneType.getRandomRuneType());
	}

	@Override
	public double massOfOne() {return artifactType.getMass();}

	@Override
	public double valueOfOne() {return artifactType.getValue();}
	
	
	

}
