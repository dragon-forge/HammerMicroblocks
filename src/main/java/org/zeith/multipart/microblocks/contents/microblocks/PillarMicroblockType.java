package org.zeith.multipart.microblocks.contents.microblocks;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.microblocks.api.*;
import org.zeith.multipart.microblocks.api.data.IAxialMicroblockData;
import org.zeith.multipart.microblocks.api.grids.*;

import java.util.List;

public class PillarMicroblockType
		extends MicroblockType
{
	public final double thickness;
	protected final List<List<AABB>> boxes;
	protected final List<VoxelShape> shapes;
	
	public PillarMicroblockType(float thickness)
	{
		this.thickness = thickness / 32D;
		boxes = createAABBs();
		shapes = createShapes();
	}
	
	protected List<VoxelShape> createShapes()
	{
		return List.of(
				Shapes.box(
						0.0, 0.5 - this.thickness, 0.5 - this.thickness,
						1.0, 0.5 + this.thickness, 0.5 + this.thickness
				),
				Shapes.box(
						0.5 - this.thickness, 0.0, 0.5 - this.thickness,
						0.5 + this.thickness, 1.0, 0.5 + this.thickness
				),
				Shapes.box(
						0.5 - this.thickness, 0.5 - this.thickness, 0.0,
						0.5 + this.thickness, 0.5 + this.thickness, 1.0
				)
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
		return PlanarPillarPlacementGrid.INSTANCE;
	}
	
	@Override
	public List<AABB> getModelStrips(PartPlacement placement, @Nullable MicroblockData data)
	{
		if(data instanceof IAxialMicroblockData ax)
			return boxes.get(ax.getAxis().ordinal());
		return boxes.get(1);
	}
	
	@Override
	public VoxelShape getShape(PartPlacement placement, @Nullable MicroblockData data)
	{
		if(data instanceof IAxialMicroblockData ax)
			return shapes.get(ax.getAxis().ordinal());
		return shapes.get(1);
	}
	
	@Override
	public MicroblockData createDataForPlacement(Player player, BlockHitResult hit, boolean sameBlock)
	{
		return createEmptyData()
				.withAxis(hit.getDirection().getAxis());
	}
	
	@Override
	public @Nullable MicroblockData createItemData()
	{
		return createEmptyData()
				.withAxis(Direction.Axis.Y);
	}
	
	@Override
	public @NotNull PillarMicroblockData createEmptyData()
	{
		return new PillarMicroblockData();
	}
	
	public static class PillarMicroblockData
			extends MicroblockData
			implements IAxialMicroblockData
	{
		@NBTSerializable("Axis")
		public Direction.Axis axis = Direction.Axis.Y;
		
		public PillarMicroblockData withAxis(Direction.Axis axis)
		{
			this.axis = axis;
			return this;
		}
		
		@Override
		public Direction.Axis getAxis()
		{
			return axis;
		}
	}
}