package org.zeith.multipart.microblocks.multipart.entity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SoundType;
import org.zeith.multipart.api.*;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.microblocks.api.tile.MicroblockState;

public class MicroblockEntity
		extends PartEntity
{
	public final MicroblockState state = new MicroblockState();
	
	public MicroblockEntity(PartDefinition definition, PartContainer container, PartPlacement placement)
	{
		super(definition, container, placement);
	}
	
	public MicroblockEntity initState(MicroblockState state)
	{
		this.state.copyFrom(state);
		syncDirty = true;
		container.causeBlockUpdate = true;
		return this;
	}
	
	@Override
	public boolean canHarvestPart(Player player)
	{
		return super.canHarvestPart(player);
	}
	
	@Override
	public float getDestroySpeed(Player player)
	{
		return super.getDestroySpeed(player);
	}
	
	public SoundType getSoundType()
	{
		return SoundType.STONE;
	}
}