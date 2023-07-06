package org.zeith.multipart.microblocks.impl;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.*;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.grids.*;
import org.zeith.multipart.microblocks.multipart.entity.MicroblockEntity;

import java.util.List;

public class PlanarMicroblockType
		extends MicroblockType
{
	protected final List<List<AABB>> boxes;
	protected final List<VoxelShape> shapes;
	public final double thickness;
	
	public boolean compatibleWithSameTypeEdged;
	
	public PlanarMicroblockType(float thickness, boolean compatibleWithSameTypeEdged)
	{
		this.thickness = thickness / 16D;
		this.compatibleWithSameTypeEdged = compatibleWithSameTypeEdged;
		
		boxes = createAABBs();
		shapes = createShapes();
	}
	
	protected List<VoxelShape> createShapes()
	{
		return List.of(
				Shapes.box(0.0, 0.0, 0.0, 1.0, this.thickness, 1.0),
				Shapes.box(0.0, 1.0 - this.thickness, 0.0, 1.0, 1.0, 1.0),
				Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, this.thickness),
				Shapes.box(0.0, 0.0, 1.0 - this.thickness, 1.0, 1.0, 1.0),
				Shapes.box(0.0, 0.0, 0.0, this.thickness, 1.0, 1.0),
				Shapes.box(1.0 - this.thickness, 0.0, 0.0, 1.0, 1.0, 1.0)
		);
	}
	
	protected List<List<AABB>> createAABBs()
	{
		return createShapes()
				.stream()
				.map(VoxelShape::toAabbs)
				.toList();
	}
	
	@Override
	public MicroblockPlacementGrid getPlacementGrid()
	{
		return PlanarPlacementGrid.INSTANCE;
	}
	
	@Override
	public VoxelShape getOccupationShapeFor(PartPlacement ourPlacement, MicroblockType futureType, PartPlacement futureTypePlacement, MicroblockEntity futureEntity)
	{
		if(futureType instanceof PlanarMicroblockType other && other.thickness == thickness && compatibleWithSameTypeEdged)
			return Shapes.empty();
		
		return super.getOccupationShapeFor(ourPlacement, futureType, futureTypePlacement, futureEntity);
	}
	
	@Override
	public List<AABB> getModelStrips(PartPlacement placement)
	{
		var dir = placement.getDirection();
		if(dir == null) return List.of();
		return boxes.get(dir.ordinal());
	}
	
	@Override
	public VoxelShape getShape(PartPlacement placement)
	{
		var dir = placement.getDirection();
		if(dir == null) return Shapes.empty();
		return shapes.get(dir.ordinal());
	}
}