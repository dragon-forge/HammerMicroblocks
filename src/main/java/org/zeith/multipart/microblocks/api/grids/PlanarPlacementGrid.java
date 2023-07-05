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
	public @Nullable PartPlacement pickPlacement(Player player, BlockHitResult hit)
	{
		PartPlacement place = null;
		
		if(hit.getDirection().getAxis() == Direction.Axis.Y)
		{
			var loc = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()));
			
			float four = 4 / 16F;
			float ifour = 12 / 16F;
			
			if(loc.x >= four && loc.z >= four && loc.x <= ifour && loc.z <= ifour)
				place = PartPlacementsHM.SIDED_PLACEMENT.apply(hit.getDirection().getOpposite());
			else if(loc.x > loc.z && loc.x < 1 - loc.z) place = PartPlacementsHM.NORTH;
			else if(loc.z > ifour && 1 - loc.z < loc.x && loc.z > loc.x) place = PartPlacementsHM.SOUTH;
			else if(loc.z > loc.x && loc.z < 1 - loc.x) place = PartPlacementsHM.WEST;
			else if(loc.x > ifour && 1 - loc.x < loc.z && loc.x > loc.z) place = PartPlacementsHM.EAST;
		}
		
		if(place != null && place.getDirection() != null)
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
		
		if(hit.getDirection().getAxis() == Direction.Axis.Y)
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
		}
		
		return lines.getOrDefault(hit.getDirection(), List.of());
	}
}