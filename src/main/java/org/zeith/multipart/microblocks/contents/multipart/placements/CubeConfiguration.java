package org.zeith.multipart.microblocks.contents.multipart.placements;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import static net.minecraft.core.Direction.AxisDirection.*;

public enum CubeConfiguration
{
	XN_YN_ZN(NEGATIVE, NEGATIVE, NEGATIVE),
	XN_YN_ZP(NEGATIVE, NEGATIVE, POSITIVE),
	XN_YP_ZN(NEGATIVE, POSITIVE, NEGATIVE),
	XN_YP_ZP(NEGATIVE, POSITIVE, POSITIVE),
	XP_YN_ZN(POSITIVE, NEGATIVE, NEGATIVE),
	XP_YN_ZP(POSITIVE, NEGATIVE, POSITIVE),
	XP_YP_ZN(POSITIVE, POSITIVE, NEGATIVE),
	XP_YP_ZP(POSITIVE, POSITIVE, POSITIVE);
	
	private final Direction.AxisDirection x, y, z;
	
	private CubeConfiguration(Direction.AxisDirection x, Direction.AxisDirection y, Direction.AxisDirection z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static final List<CubeConfiguration> CONFIGS = List.of(
			XN_YN_ZN,
			XN_YN_ZP,
			XN_YP_ZN,
			XN_YP_ZP,
			XP_YN_ZN,
			XP_YN_ZP,
			XP_YP_ZN,
			XP_YP_ZP
	);
	
	public CubeConfiguration opposite(Direction.Axis axis)
	{
		return switch(this)
		{
			case XN_YN_ZN -> switch(axis)
			{
				case X -> XP_YN_ZN;
				case Y -> XN_YP_ZN;
				case Z -> XN_YN_ZP;
			};
			case XN_YN_ZP -> switch(axis)
			{
				case X -> XP_YN_ZP;
				case Y -> XN_YP_ZP;
				case Z -> XN_YN_ZN;
			};
			case XN_YP_ZN -> switch(axis)
			{
				case X -> XP_YP_ZN;
				case Y -> XN_YN_ZN;
				case Z -> XN_YP_ZP;
			};
			case XN_YP_ZP -> switch(axis)
			{
				case X -> XP_YP_ZP;
				case Y -> XN_YN_ZP;
				case Z -> XN_YP_ZN;
			};
			case XP_YN_ZN -> switch(axis)
			{
				case X -> XN_YN_ZN;
				case Y -> XP_YP_ZN;
				case Z -> XP_YN_ZP;
			};
			case XP_YN_ZP -> switch(axis)
			{
				case X -> XN_YN_ZP;
				case Y -> XP_YP_ZP;
				case Z -> XP_YN_ZN;
			};
			case XP_YP_ZN -> switch(axis)
			{
				case X -> XN_YP_ZN;
				case Y -> XP_YN_ZN;
				case Z -> XP_YP_ZP;
			};
			case XP_YP_ZP -> switch(axis)
			{
				case X -> XN_YP_ZP;
				case Y -> XP_YN_ZP;
				case Z -> XP_YP_ZN;
			};
		};
	}
	
	public Direction.AxisDirection of(Direction.Axis axis)
	{
		return switch(axis)
		{
			case X -> x;
			case Y -> y;
			case Z -> z;
		};
	}
	
	public boolean positiveX()
	{
		return x == Direction.AxisDirection.POSITIVE;
	}
	
	public boolean positiveY()
	{
		return y == Direction.AxisDirection.POSITIVE;
	}
	
	public boolean positiveZ()
	{
		return z == Direction.AxisDirection.POSITIVE;
	}
	
	
	public double xMin(double thickness)
	{
		return positiveX() ? 0 : 1 - thickness;
	}
	
	public double yMin(double thickness)
	{
		return positiveY() ? 0 : 1 - thickness;
	}
	
	public double zMin(double thickness)
	{
		return positiveZ() ? 0 : 1 - thickness;
	}
	
	public double xMax(double thickness)
	{
		return positiveX() ? thickness : 1;
	}
	
	public double yMax(double thickness)
	{
		return positiveY() ? thickness : 1;
	}
	
	public double zMax(double thickness)
	{
		return positiveZ() ? thickness : 1;
	}
	
	public VoxelShape cube(double thickness)
	{
		return Shapes.box(
				xMin(thickness), yMin(thickness), zMin(thickness),
				xMax(thickness), yMax(thickness), zMax(thickness)
		);
	}
}