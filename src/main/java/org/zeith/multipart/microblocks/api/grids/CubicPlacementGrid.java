package org.zeith.multipart.microblocks.api.grids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.zeith.multipart.api.PartContainer;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.api.placement.PartPos;
import org.zeith.multipart.microblocks.contents.microblocks.CubicMicroblockType;
import org.zeith.multipart.microblocks.contents.multipart.entity.MicroblockEntity;
import org.zeith.multipart.microblocks.contents.multipart.placements.CubicPartPlacement;

import java.util.List;

public class CubicPlacementGrid
		extends MicroblockPlacementGrid
{
	public static final CubicPlacementGrid INSTANCE = new CubicPlacementGrid();
	
	@Override
	public @Nullable BlockState getAppearance(MicroblockEntity thisEntity, PartPlacement thisPlacement, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos, @Nullable PartContainer queryContainer, @Nullable PartPos queryPartPos)
	{
		if(queryPos == null) return null;
		var thisPos = thisEntity.pos().pos();
		if(!(thisPlacement instanceof CubicPartPlacement cpp)) return null;
		if(!(thisEntity.state.getType() instanceof CubicMicroblockType cmt) || cmt.thickness != 0.5) return null;
		var rel = queryPos.subtract(thisPos);
		
		var theState = thisEntity.state.asBlockState();
		
		var cfg = cpp.getCfg();
		var a = side.getAxis();
		var ad = side.getAxisDirection();
		var part = cfg.of(a).opposite();
		
		differentPlane:
		if(ad != part)
		{
			if(queryPartPos != null && queryPartPos.placement() instanceof CubicPartPlacement cp2 && cp2.getCfg().of(a) ==  cfg.of(a))
			{
				break differentPlane;
			}
			
			return null;
		}
		
		Direction.Axis a1;
		Direction.Axis a2;
		
		if(a == Direction.Axis.Y)
		{
			a1 = Direction.Axis.X;
			a2 = Direction.Axis.Z;
		} else if(a == Direction.Axis.X)
		{
			a1 = Direction.Axis.Y;
			a2 = Direction.Axis.Z;
		} else
		{
			a1 = Direction.Axis.X;
			a2 = Direction.Axis.Y;
		}
		
		int x = rel.get(a1);
		int z = rel.get(a2);
		
		int x1 = -cfg.of(a1).getStep();
		int z1 = -cfg.of(a2).getStep();
		
		if(x == x1)
		{
			var rev = CubicPartPlacement.LOOKUP.get(cfg.opposite(a2).ordinal());
			if(thisEntity.container().getPartAt(rev) instanceof MicroblockEntity mbe && mbe.state.asBlockState() == theState)
				return theState;
		}
		
		if(z == z1)
		{
			var rev = CubicPartPlacement.LOOKUP.get(cfg.opposite(a1).ordinal());
			if(thisEntity.container().getPartAt(rev) instanceof MicroblockEntity mbe && mbe.state.asBlockState() == theState)
				return theState;
		}
		
		return null;
	}
	
	@Override
	public @Nullable PartPlacement pickPlacement(Player player, BlockHitResult hit, boolean sameBlock)
	{
		var loc = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()));
		
		int x = 0;
		int y = 0;
		int z = 0;
		
		var hitSide = hit.getDirection();
		var a = hitSide.getAxis();
		
		if(hitSide.getAxisDirection() != (sameBlock ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE))
		{
			if(a == Direction.Axis.X) ++x;
			if(a == Direction.Axis.Y) ++y;
			if(a == Direction.Axis.Z) ++z;
		}
		
		if(a == Direction.Axis.X)
		{
			if(loc.y > 0.5) ++y;
			if(loc.z > 0.5) ++z;
		}
		
		if(a == Direction.Axis.Y)
		{
			if(loc.x > 0.5) ++x;
			if(loc.z > 0.5) ++z;
		}
		
		if(a == Direction.Axis.Z)
		{
			if(loc.x > 0.5) ++x;
			if(loc.y > 0.5) ++y;
		}
		
		int ordinal = z + y * 2 + x * 4;
		return CubicPartPlacement.LOOKUP.get(7 - ordinal);
	}
	
	@Override
	public @NotNull List<Vector3f> getLinesForRendering(Player player, VoxelShape blockBounds, BlockHitResult hit)
	{
		if(blockBounds.isEmpty()) return List.of();
		var boundary = blockBounds.bounds();
		
		if(hit.getDirection().getAxis() == Direction.Axis.X)
		{
			var my = (float) (hit.getDirection() == Direction.EAST ? boundary.maxX : boundary.minX);
			
			return List.of(
					// Normal outline:
					new Vector3f(my, 0, 0),
					new Vector3f(my, 1, 0),
					new Vector3f(my, 1, 1),
					new Vector3f(my, 0, 1),
					new Vector3f(my, 0, 0),
					new Vector3f(my, 0, 1),
					new Vector3f(my, 1, 0),
					new Vector3f(my, 1, 1),
					// Cross:
					new Vector3f(my, 0, 0.5F),
					new Vector3f(my, 1, 0.5F),
					new Vector3f(my, 0.5F, 0),
					new Vector3f(my, 0.5F, 1)
			);
		} else if(hit.getDirection().getAxis() == Direction.Axis.Y)
		{
			var my = (float) (hit.getDirection() == Direction.UP ? boundary.maxY : boundary.minY);
			
			return List.of(
					// Normal outline:
					new Vector3f(0, my, 0),
					new Vector3f(1, my, 0),
					new Vector3f(1, my, 1),
					new Vector3f(0, my, 1),
					new Vector3f(0, my, 0),
					new Vector3f(0, my, 1),
					new Vector3f(1, my, 0),
					new Vector3f(1, my, 1),
					// Cross:
					new Vector3f(0.5F, my, 0),
					new Vector3f(0.5F, my, 1),
					new Vector3f(0, my, 0.5F),
					new Vector3f(1, my, 0.5F)
			);
		} else if(hit.getDirection().getAxis() == Direction.Axis.Z)
		{
			var my = (float) (hit.getDirection() == Direction.SOUTH ? boundary.maxZ : boundary.minZ);
			
			return List.of(
					// Normal outline:
					new Vector3f(0, 0, my),
					new Vector3f(1, 0, my),
					new Vector3f(1, 1, my),
					new Vector3f(0, 1, my),
					new Vector3f(0, 0, my),
					new Vector3f(0, 1, my),
					new Vector3f(1, 0, my),
					new Vector3f(1, 1, my),
					// Cross:
					new Vector3f(0.5F, 0, my),
					new Vector3f(0.5F, 1, my),
					new Vector3f(0, 0.5F, my),
					new Vector3f(1, 0.5F, my)
			);
		}
		
		return List.of();
	}
}