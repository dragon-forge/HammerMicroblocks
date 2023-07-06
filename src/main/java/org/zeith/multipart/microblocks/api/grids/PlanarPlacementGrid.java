package org.zeith.multipart.microblocks.api.grids;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.*;
import org.joml.Vector3f;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.blocks.BlockMultipartContainer;
import org.zeith.multipart.init.PartPlacementsHM;

import java.util.*;

public class PlanarPlacementGrid
		extends MicroblockPlacementGrid
{
	public static final PlanarPlacementGrid INSTANCE = new PlanarPlacementGrid();
	
	protected final Map<Direction, List<Vector3f>> lines = new HashMap<>();
	
	@Override
	public @Nullable PartPlacement pickPlacement(Player player, BlockHitResult hit, boolean sameBlock)
	{
		PartPlacement place = null;
		
		var loc = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()));
		
		float four = 4 / 16F;
		float ifour = 12 / 16F;
		
		if(hit.getDirection().getAxis() == Direction.Axis.X)
		{
			double x = loc.y;
			double z = loc.z;
			
			if(x >= four && z >= four && x <= ifour && z <= ifour)
				place = PartPlacementsHM.SIDED_PLACEMENT.apply(hit.getDirection().getOpposite());
			else if(x > z && x < 1 - z) place = PartPlacementsHM.NORTH;
			else if(z > ifour && 1 - z < x && z > x) place = PartPlacementsHM.SOUTH;
			else if(z > x && z < 1 - x) place = PartPlacementsHM.DOWN;
			else if(x > ifour && 1 - x < z && x > z) place = PartPlacementsHM.UP;
		} else if(hit.getDirection().getAxis() == Direction.Axis.Y)
		{
			double x = loc.x;
			double z = loc.z;
			
			if(x >= four && z >= four && x <= ifour && z <= ifour)
				place = PartPlacementsHM.SIDED_PLACEMENT.apply(hit.getDirection().getOpposite());
			else if(x > z && x < 1 - z) place = PartPlacementsHM.NORTH;
			else if(z > ifour && 1 - z < x && z > x) place = PartPlacementsHM.SOUTH;
			else if(z > x && z < 1 - x) place = PartPlacementsHM.WEST;
			else if(x > ifour && 1 - x < z && x > z) place = PartPlacementsHM.EAST;
		} else if(hit.getDirection().getAxis() == Direction.Axis.Z)
		{
			double x = loc.x;
			double z = loc.y;
			
			if(x >= four && z >= four && x <= ifour && z <= ifour)
				place = PartPlacementsHM.SIDED_PLACEMENT.apply(hit.getDirection().getOpposite());
			else if(x > z && x < 1 - z) place = PartPlacementsHM.DOWN;
			else if(z > ifour && 1 - z < x && z > x) place = PartPlacementsHM.UP;
			else if(z > x && z < 1 - x) place = PartPlacementsHM.WEST;
			else if(x > ifour && 1 - x < z && x > z) place = PartPlacementsHM.EAST;
		}
		
		if(place != null && place.getDirection() != null && sameBlock)
		{
			var pc = BlockMultipartContainer.pc(player.level(), hit.getBlockPos());
			if(pc != null && pc.getPartAt(place) != null)
			{
				var op = PartPlacementsHM.SIDED_PLACEMENT.apply(place.getDirection().getOpposite());
				if(pc.getPartAt(op) == null)
					return op;
			}
		}
		
		return place;
	}
	
	@Override
	public @NotNull List<Vector3f> getLinesForRendering(Player player, VoxelShape blockBounds, BlockHitResult hit)
	{
		if(blockBounds.isEmpty()) return List.of();
		var boundary = blockBounds.bounds();
		
		if(hit.getDirection().getAxis() == Direction.Axis.X)
		{
			var my = (float) (hit.getDirection() == Direction.EAST ? boundary.maxX : boundary.minX);
			
			float four = 4 / 16F;
			float ifour = 12 / 16F;
			
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
					// Extra shenanigans happening here:
					new Vector3f(my, 0, 0),
					new Vector3f(my, four, four),
					new Vector3f(my, four, ifour),
					new Vector3f(my, 0, 1),
					new Vector3f(my, ifour, four),
					new Vector3f(my, 1, 0),
					new Vector3f(my, ifour, ifour),
					new Vector3f(my, 1, 1),
					// The connections between diagonals:
					new Vector3f(my, four, four),
					new Vector3f(my, four, ifour),
					new Vector3f(my, ifour, four),
					new Vector3f(my, ifour, ifour),
					new Vector3f(my, four, four),
					new Vector3f(my, ifour, four),
					new Vector3f(my, four, ifour),
					new Vector3f(my, ifour, ifour)
			);
		} else if(hit.getDirection().getAxis() == Direction.Axis.Y)
		{
			var my = (float) (hit.getDirection() == Direction.UP ? boundary.maxY : boundary.minY);
			
			float four = 4 / 16F;
			float ifour = 12 / 16F;
			
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
					// Extra shenanigans happening here:
					new Vector3f(0, my, 0),
					new Vector3f(four, my, four),
					new Vector3f(four, my, ifour),
					new Vector3f(0, my, 1),
					new Vector3f(ifour, my, four),
					new Vector3f(1, my, 0),
					new Vector3f(ifour, my, ifour),
					new Vector3f(1, my, 1),
					// The connections between diagonals:
					new Vector3f(four, my, four),
					new Vector3f(four, my, ifour),
					new Vector3f(ifour, my, four),
					new Vector3f(ifour, my, ifour),
					new Vector3f(four, my, four),
					new Vector3f(ifour, my, four),
					new Vector3f(four, my, ifour),
					new Vector3f(ifour, my, ifour)
			);
		} else if(hit.getDirection().getAxis() == Direction.Axis.Z)
		{
			var my = (float) (hit.getDirection() == Direction.SOUTH ? boundary.maxZ : boundary.minZ);
			
			float four = 4 / 16F;
			float ifour = 12 / 16F;
			
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
					// Extra shenanigans happening here:
					new Vector3f(0, 0, my),
					new Vector3f(four, four, my),
					new Vector3f(four, ifour, my),
					new Vector3f(0, 1, my),
					new Vector3f(ifour, four, my),
					new Vector3f(1, 0, my),
					new Vector3f(ifour, ifour, my),
					new Vector3f(1, 1, my),
					// The connections between diagonals:
					new Vector3f(four, four, my),
					new Vector3f(four, ifour, my),
					new Vector3f(ifour, four, my),
					new Vector3f(ifour, ifour, my),
					new Vector3f(four, four, my),
					new Vector3f(ifour, four, my),
					new Vector3f(four, ifour, my),
					new Vector3f(ifour, ifour, my)
			);
		}
		
		return lines.getOrDefault(hit.getDirection(), List.of());
	}
}