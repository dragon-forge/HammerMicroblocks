package org.zeith.multipart.microblocks.api.grids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.zeith.multipart.api.PartContainer;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.api.placement.PartPos;
import org.zeith.multipart.microblocks.contents.multipart.entity.MicroblockEntity;

import java.util.List;

public abstract class MicroblockPlacementGrid
{
	@Nullable
	public abstract BlockState getAppearance(MicroblockEntity thisEntity, PartPlacement thisPlacement, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos, @Nullable PartContainer queryContainer, @Nullable PartPos queryPartPos);
	
	@Nullable
	public abstract PartPlacement pickPlacement(Player player, BlockHitResult hit, boolean sameBlock);
	
	@NotNull
	public abstract List<Vector3f> getLinesForRendering(Player player, VoxelShape blockBounds, BlockHitResult hit);
}