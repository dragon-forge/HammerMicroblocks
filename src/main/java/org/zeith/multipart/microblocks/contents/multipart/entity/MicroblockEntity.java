package org.zeith.multipart.microblocks.contents.multipart.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.*;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.multipart.api.*;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.tile.MicroblockState;
import org.zeith.multipart.microblocks.init.MicroblockTypesHM;

import java.util.List;

public class MicroblockEntity
		extends PartEntity
{
	@NBTSerializable("State")
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
	public ItemStack getCloneItemStack(BlockHitResult target, Player player, IndexedVoxelShape selection)
	{
		return state.asStack();
	}
	
	@Override
	public List<ItemStack> getDrops(@Nullable ServerPlayer harvester, LootParams.Builder context)
	{
		return List.of(state.asStack());
	}
	
	@Override
	protected VoxelShape updateShape()
	{
		if(!state.isValid() && state.getType() == null)
			state.setType(MicroblockTypesHM.FACADE, state.getData());
		return state.getType().getShape(placement, state.getData());
	}
	
	@Override
	public VoxelShape getPartOccupiedShapeWith(PartEntity toBePlaced, VoxelShape shapeOfEntity)
	{
		if(toBePlaced instanceof MicroblockEntity mbe)
			return state.getType().getOccupationShapeFor(placement, mbe.state.getType(), mbe.placement(), mbe, state.getData());
		return getShape();
	}
	
	@Nullable
	@Override
	public BlockState getHardnessState()
	{
		return state.asBlockState();
	}
	
	@Override
	public boolean isCorrectToolForDrops(@NotNull Player player)
	{
		return canHarvestPart(player);
	}
	
	@Override
	public boolean canHarvestPart(Player player)
	{
		return ForgeHooks.isCorrectToolForDrops(state.asBlockState(), player);
	}
	
	@Override
	public float getDestroySpeed(Player player)
	{
		return state.asBlockState().getDestroySpeed(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
	}
	
	@Override
	public boolean isViewBlocking()
	{
		return state.asBlockState().isViewBlocking(container.level(), container.pos());
	}
	
	public SoundType getSoundType()
	{
		return state.asBlockState().getSoundType();
	}
}