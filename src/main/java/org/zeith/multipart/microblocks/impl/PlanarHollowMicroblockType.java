package org.zeith.multipart.microblocks.impl;

import net.minecraft.world.phys.shapes.*;

import java.util.List;

import static net.minecraft.world.level.block.Block.box;

public class PlanarHollowMicroblockType
		extends PlanarMicroblockType
{
	public PlanarHollowMicroblockType(float thickness, boolean compatibleWithSameTypeEdged)
	{
		super(thickness, compatibleWithSameTypeEdged);
	}
	
	protected VoxelShape getCutoutShape()
	{
		return Shapes.or(
				box(5, 11, 5, 11, 16, 11),
				box(5, 0, 5, 11, 5, 11),
				box(5, 5, 0, 11, 11, 5),
				box(11, 5, 5, 16, 11, 11),
				box(5, 5, 11, 11, 11, 16),
				box(0, 5, 5, 5, 11, 11)
		);
	}
	
	protected List<VoxelShape> createFullShapes()
	{
		return super.createShapes();
	}
	
	@Override
	protected List<VoxelShape> createShapes()
	{
		var pipeShapes = getCutoutShape();
		return createFullShapes()
				.stream()
				.map(fullShape -> Shapes.join(fullShape, pipeShapes, BooleanOp.ONLY_FIRST))
				.toList();
	}
}