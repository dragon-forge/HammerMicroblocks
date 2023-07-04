package org.zeith.multipart.microblocks.impl;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.*;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.init.PartPlacementsHM;
import org.zeith.multipart.microblocks.api.MicroblockType;

import java.util.List;

public class CoverMicroblockType
		extends MicroblockType
{
	public final AABB[] THIN_FACADE_BOXES;
	public final VoxelShape[] THIN_FACADE_SHAPES;
	public final double THIN_THICKNESS;
	
	public CoverMicroblockType(float thickness)
	{
		super(PartPlacementsHM.NORTH);
		
		this.THIN_THICKNESS = thickness / 16D;
		
		THIN_FACADE_BOXES = new AABB[] {
				new AABB(0.0, 0.0, 0.0, 1.0, THIN_THICKNESS, 1.0),
				new AABB(0.0, 1.0 - THIN_THICKNESS, 0.0, 1.0, 1.0, 1.0),
				new AABB(0.0, 0.0, 0.0, 1.0, 1.0, THIN_THICKNESS),
				new AABB(0.0, 0.0, 1.0 - THIN_THICKNESS, 1.0, 1.0, 1.0),
				new AABB(0.0, 0.0, 0.0, THIN_THICKNESS, 1.0, 1.0),
				new AABB(1.0 - THIN_THICKNESS, 0.0, 0.0, 1.0, 1.0, 1.0)
		};
		
		THIN_FACADE_SHAPES = new VoxelShape[] {
				Shapes.box(0.0, 0.0, 0.0, 1.0, THIN_THICKNESS, 1.0),
				Shapes.box(0.0, 1.0 - THIN_THICKNESS, 0.0, 1.0, 1.0, 1.0),
				Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, THIN_THICKNESS),
				Shapes.box(0.0, 0.0, 1.0 - THIN_THICKNESS, 1.0, 1.0, 1.0),
				Shapes.box(0.0, 0.0, 0.0, THIN_THICKNESS, 1.0, 1.0),
				Shapes.box(1.0 - THIN_THICKNESS, 0.0, 0.0, 1.0, 1.0, 1.0)
		};
	}
	
	@Override
	public List<AABB> getModelStrips(PartPlacement placement)
	{
		var dir = placement.getDirection();
		if(dir == null) return List.of();
		return List.of(THIN_FACADE_BOXES[dir.ordinal()]);
	}
}