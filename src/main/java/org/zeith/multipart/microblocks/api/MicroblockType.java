package org.zeith.multipart.microblocks.api;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.compress.utils.Lists;
import org.zeith.multipart.api.PartEntity;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.init.PartPlacementsHM;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.grids.MicroblockPlacementGrid;
import org.zeith.multipart.microblocks.multipart.entity.MicroblockEntity;

import java.util.List;

public abstract class MicroblockType
{
	protected String descriptionId;
	protected PartPlacement itemRenderPlacement = PartPlacementsHM.NORTH;
	protected final List<PartPlacement> validPlacementTargets = Lists.newArrayList();
	
	public abstract MicroblockPlacementGrid getPlacementGrid();
	
	public abstract List<AABB> getModelStrips(PartPlacement placement);
	
	public abstract VoxelShape getShape(PartPlacement placement);
	
	public VoxelShape getOccupationShapeFor(PartPlacement ourPlacement, MicroblockType futureType, PartPlacement futureTypePlacement, MicroblockEntity futureEntity)
	{
		return getShape(ourPlacement);
	}
	
	public PartPlacement getPlacementForRendering()
	{
		return itemRenderPlacement;
	}
	
	public Component getDescription()
	{
		return Component.translatable(this.getDescriptionId());
	}
	
	protected String getOrCreateDescriptionId()
	{
		if(this.descriptionId == null)
			this.descriptionId = Util.makeDescriptionId("microblock_type", HammerMicroblocks.microblockTypes()
					.getKey(this));
		return this.descriptionId;
	}
	
	public String getDescriptionId()
	{
		return this.getOrCreateDescriptionId();
	}
}