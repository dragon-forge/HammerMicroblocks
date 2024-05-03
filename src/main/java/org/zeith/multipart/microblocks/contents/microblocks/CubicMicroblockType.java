package org.zeith.multipart.microblocks.contents.microblocks;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.init.PartPlacementsHM;
import org.zeith.multipart.microblocks.api.MicroblockData;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.grids.CubicPlacementGrid;
import org.zeith.multipart.microblocks.api.grids.MicroblockPlacementGrid;
import org.zeith.multipart.microblocks.contents.multipart.placements.CubeConfiguration;
import org.zeith.multipart.microblocks.contents.multipart.placements.CubicPartPlacement;

import java.util.List;

public class CubicMicroblockType
		extends MicroblockType
{
	protected final List<List<AABB>> boxes;
	protected final List<VoxelShape> shapes;
	
	protected final List<AABB> itemBoxes;
	
	public final double thickness;
	
	public CubicMicroblockType(float thickness)
	{
		this.thickness = thickness / 16.0;
		this.itemRenderPlacement = PartPlacementsHM.CENTER;
		this.itemBoxes = List.of(new AABB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5).inflate(this.thickness / 2));
		this.boxes = createAABBs();
		this.shapes = createShapes();
	}
	
	protected List<VoxelShape> createShapes()
	{
		return CubeConfiguration.CONFIGS.stream()
				.map(c -> c.cube(thickness))
				.toList();
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
		return CubicPlacementGrid.INSTANCE;
	}
	
	@Override
	public List<AABB> getModelStrips(PartPlacement placement, @Nullable MicroblockData data)
	{
		if(placement == PartPlacementsHM.CENTER) return itemBoxes;
		
		var dir = placement instanceof CubicPartPlacement cpp ? cpp.ordinal() : -1;
		if(dir < 0) return List.of();
		return boxes.get(dir);
	}
	
	@Override
	public VoxelShape getShape(PartPlacement placement, @Nullable MicroblockData data)
	{
		var dir = placement instanceof CubicPartPlacement cpp ? cpp.ordinal() : -1;
		if(dir < 0) return Shapes.empty();
		return shapes.get(dir);
	}
}