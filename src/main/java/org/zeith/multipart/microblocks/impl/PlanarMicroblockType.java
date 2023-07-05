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
	public final AABB[] boxes;
	public final VoxelShape[] shapes;
	public final double thickness;
	
	public boolean compatibleWithSameTypeEdged = false;
	
	public PlanarMicroblockType(float thickness, boolean compatibleWithSameTypeEdged)
	{
		this.thickness = thickness / 16D;
		this.compatibleWithSameTypeEdged = compatibleWithSameTypeEdged;
		
		boxes = new AABB[] {
				new AABB(0.0, 0.0, 0.0, 1.0, this.thickness, 1.0),
				new AABB(0.0, 1.0 - this.thickness, 0.0, 1.0, 1.0, 1.0),
				new AABB(0.0, 0.0, 0.0, 1.0, 1.0, this.thickness),
				new AABB(0.0, 0.0, 1.0 - this.thickness, 1.0, 1.0, 1.0),
				new AABB(0.0, 0.0, 0.0, this.thickness, 1.0, 1.0),
				new AABB(1.0 - this.thickness, 0.0, 0.0, 1.0, 1.0, 1.0)
		};
		
		shapes = new VoxelShape[] {
				Shapes.box(0.0, 0.0, 0.0, 1.0, this.thickness, 1.0),
				Shapes.box(0.0, 1.0 - this.thickness, 0.0, 1.0, 1.0, 1.0),
				Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, this.thickness),
				Shapes.box(0.0, 0.0, 1.0 - this.thickness, 1.0, 1.0, 1.0),
				Shapes.box(0.0, 0.0, 0.0, this.thickness, 1.0, 1.0),
				Shapes.box(1.0 - this.thickness, 0.0, 0.0, 1.0, 1.0, 1.0)
		};
	}
	
	@Override
	public MicroblockPlacementGrid getPlacementGrid()
	{
		return PlanarPlacementGrid.INSTANCE;
	}
	
	@Override
	public VoxelShape getOccupationShapeFor(PartPlacement ourPlacement, MicroblockType futureType, PartPlacement futureTypePlacement, MicroblockEntity futureEntity)
	{
		if(futureType == this && compatibleWithSameTypeEdged)
			return Shapes.empty();
		
		return super.getOccupationShapeFor(ourPlacement, futureType, futureTypePlacement, futureEntity);
	}
	
	@Override
	public List<AABB> getModelStrips(PartPlacement placement)
	{
		var dir = placement.getDirection();
		if(dir == null) return List.of();
		return List.of(boxes[dir.ordinal()]);
	}
	
	@Override
	public VoxelShape getShape(PartPlacement placement)
	{
		var dir = placement.getDirection();
		if(dir == null) return Shapes.empty();
		return shapes[dir.ordinal()];
	}
}