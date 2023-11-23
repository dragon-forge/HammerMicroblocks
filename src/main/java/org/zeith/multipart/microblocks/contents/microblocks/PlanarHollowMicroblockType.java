package org.zeith.multipart.microblocks.contents.microblocks;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.*;

import java.util.*;

import static net.minecraft.world.level.block.Block.box;

public class PlanarHollowMicroblockType
		extends PlanarMicroblockType
{
	public PlanarHollowMicroblockType(float thickness, boolean compatibleWithSameTypeEdged)
	{
		super(thickness, compatibleWithSameTypeEdged);
	}
	
	protected VoxelShape getCutoutShape(Direction dir)
	{
		return switch(dir.getAxis())
		{
			case Y -> box(5, 0, 5, 11, 16, 11);
			case Z -> box(5, 5, 0, 11, 11, 16);
			case X -> box(0, 5, 5, 16, 11, 11);
		};
	}
	
	protected List<VoxelShape> createFullShapes()
	{
		return super.createShapes();
	}
	
	@Override
	protected List<VoxelShape> createShapes()
	{
		var dirs = Direction.values();
		var vs = new ArrayList<>(createFullShapes());
		for(int i = 0; i < 6; i++)
		{
			var pipeShapes = getCutoutShape(dirs[i]);
			vs.set(i, Shapes.join(vs.get(i), pipeShapes, BooleanOp.ONLY_FIRST));
		}
		return vs;
	}
}