package org.zeith.multipart.microblocks.api.grids;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.*;
import org.joml.Vector3f;
import org.zeith.multipart.api.placement.PartPlacement;

import java.util.List;

public abstract class MicroblockPlacementGrid
{
	@Nullable
	public abstract PartPlacement pickPlacement(Player player, BlockHitResult hit);
	
	@NotNull
	public abstract List<Vector3f> getLinesForRendering(Player player, VoxelShape blockBounds, BlockHitResult hit);
}