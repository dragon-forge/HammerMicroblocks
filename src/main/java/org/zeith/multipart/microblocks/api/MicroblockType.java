package org.zeith.multipart.microblocks.api;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.microblocks.HammerMicroblocks;

import java.util.List;

public abstract class MicroblockType
{
	protected String descriptionId;
	protected final PartPlacement itemRenderPlacement;
	protected final List<PartPlacement> validPlacementTargets = Lists.newArrayList();
	
	public MicroblockType(PartPlacement itemRenderPlacement)
	{
		this.itemRenderPlacement = itemRenderPlacement;
	}
	
	public abstract List<AABB> getModelStrips(PartPlacement placement);
	
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