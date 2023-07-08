package org.zeith.multipart.microblocks.api;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.init.PartPlacementsHM;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.grids.MicroblockPlacementGrid;
import org.zeith.multipart.microblocks.contents.multipart.entity.MicroblockEntity;

import java.util.List;

public abstract class MicroblockType
{
	protected String descriptionId;
	protected PartPlacement itemRenderPlacement = PartPlacementsHM.NORTH;
	
	public abstract MicroblockPlacementGrid getPlacementGrid();
	
	public abstract List<AABB> getModelStrips(PartPlacement placement, @Nullable MicroblockData data);
	
	public abstract VoxelShape getShape(PartPlacement placement, @Nullable MicroblockData data);
	
	public VoxelShape getOccupationShapeFor(PartPlacement ourPlacement, MicroblockType futureType, PartPlacement futureTypePlacement, MicroblockEntity futureEntity, @Nullable MicroblockData data)
	{
		return getShape(ourPlacement, data);
	}
	
	@Nullable
	public MicroblockData createDataForPlacement(Player player, BlockHitResult hit, boolean sameBlock)
	{
		return createEmptyData();
	}
	
	@Nullable
	public MicroblockData createItemData()
	{
		return createEmptyData();
	}
	
	@Nullable
	public MicroblockData createEmptyData()
	{
		return null;
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