package org.zeith.multipart.microblocks.contents.multipart.placements;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.zeith.multipart.api.placement.PartPlacement;

import java.util.List;

public class CubicPartPlacement
		extends PartPlacement
{
	public static final List<CubicPartPlacement> LOOKUP = CubeConfiguration.CONFIGS
			.stream()
			.map(CubicPartPlacement::new)
			.toList();
	
	protected final CubeConfiguration cfg;
	protected final VoxelShape shape;
	
	public CubicPartPlacement(CubeConfiguration cfg)
	{
		this.cfg = cfg;
		this.shape = Shapes.box(
				cfg.xMin(0.5F), cfg.yMin(0.5F), cfg.zMin(0.5F),
				cfg.xMax(0.5F), cfg.yMax(0.5F), cfg.zMax(0.5F)
		);
	}
	
	public CubeConfiguration getCfg()
	{
		return cfg;
	}
	
	@Override
	public VoxelShape getExampleShape()
	{
		return shape;
	}
	
	public int ordinal()
	{
		return cfg.ordinal();
	}
}