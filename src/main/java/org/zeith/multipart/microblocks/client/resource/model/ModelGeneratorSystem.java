package org.zeith.multipart.microblocks.client.resource.model;


import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.client.model.data.ModelData;
import org.zeith.multipart.api.PartContainer;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.init.PartPlacementsHM;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.impl.PlanarMicroblockType;
import org.zeith.multipart.microblocks.multipart.entity.MicroblockEntity;
import org.zeith.multipart.microblocks.shadow.codechicken.lib.model.pipeline.transformers.*;
import org.zeith.multipart.microblocks.shadow.fabric.*;

import java.util.*;

public class ModelGeneratorSystem
{
	private static final Renderer renderer = Renderer.getInstance();
	
	public static Mesh generateMesh(List<AABB> holeStrips, ItemStack textureItem, Direction side)
	{
		MeshBuilder meshBuilder = renderer.meshBuilder();
		QuadEmitter emitter = meshBuilder.getEmitter();
		
		var model = Minecraft.getInstance().getItemRenderer().getModel(textureItem, null,
				null, 0
		);
		
		QuadReInterpolator interpolator = new QuadReInterpolator();
		
		var itemColors = Minecraft.getInstance().getItemColors();
		
		for(int cullFaceIdx = 0; cullFaceIdx <= ModelHelper.NULL_FACE_ID; cullFaceIdx++)
		{
			Direction cullFace = ModelHelper.faceFromIndex(cullFaceIdx);
			List<BakedQuad> quads = model.getQuads(null, cullFace, RandomSource.create());
			
			for(BakedQuad quad : quads)
			{
				QuadTinter quadTinter = null;
				
				// Prebake the color tint into the quad
				if(quad.getTintIndex() != -1)
					quadTinter = new QuadTinter(itemColors.getColor(textureItem, quad.getTintIndex()));
				
				for(AABB box : holeStrips)
				{
					emitter.fromVanilla(quad.getVertices(), 0, false);
					// Keep the cull-face for faces that are flush with the outer block-face on the
					// side the facade is attached to, but clear it for anything that faces inwards
					emitter.cullFace(cullFace == side ? side : null);
					emitter.nominalFace(quad.getDirection());
					interpolator.setInputQuad(emitter);
					
					QuadClamper clamper = new QuadClamper(box);
					if(!clamper.transform(emitter))
					{
						continue;
					}
					
					interpolator.transform(emitter);
					
					// Tints the quad if we need it to. Disabled by default.
					if(quadTinter != null)
					{
						quadTinter.transform(emitter);
					}
					
					emitter.emit();
				}

//				emitter.fromVanilla(quad.getVertices(), 0, false);
//				emitter.cullFace(cullFace);
//				emitter.nominalFace(quad.getDirection());
//				interpolator.setInputQuad(emitter);
//				interpolator.transform(emitter);
//				// Tints the quad if we need it to. Disabled by default.
//				if(quadTinter != null) quadTinter.transform(emitter);
//				emitter.emit();
			}
		}
		
		return meshBuilder.build();
	}
	
	public static Mesh generateMesh(MicroblockType type, PartPlacement placement, PartContainer pc, BlockAndTintGetter parentWorld, BlockPos pos, List<AABB> holeStrips, BlockState blockState, Direction side, RandomSource random, RenderType renderType)
	{
		BlockColors blockColors = Minecraft.getInstance().getBlockColors();
		
		MeshBuilder meshBuilder = renderer.meshBuilder();
		QuadEmitter emitter = meshBuilder.getEmitter();
		
		// calculate the side mask.
		float planarThickness = 0;
		int facadeMask = 0;
		Direction planarAttachment = null;
		if(type instanceof PlanarMicroblockType pl && placement != null && pc != null)
		{
			planarThickness = (float) pl.thickness;
			planarAttachment = placement.getDirection();
			
			for(Direction dir : Direction.values())
			{
				if(dir.getAxis() != side.getAxis())
				{
					var part = pc.getPartAt(PartPlacementsHM.SIDED_PLACEMENT.apply(dir));
					if(part instanceof MicroblockEntity mb && mb.state.getType() instanceof PlanarMicroblockType)
						facadeMask |= 1 << dir.ordinal();
				}
			}
		}
		
		var facadeAccess = new FacadeBlockAccess(parentWorld, pos,
				planarAttachment != null ? planarAttachment : side, blockState
		);
		
		var dispatcher = Minecraft.getInstance().getBlockRenderer();
		var model = dispatcher.getBlockModel(blockState);
		
		var fullBounds = type.getShape(placement).bounds();
		
		QuadFaceStripper faceStripper = new QuadFaceStripper(fullBounds, facadeMask);
		
		// Setup the kicker.
		QuadCornerKicker kicker = new QuadCornerKicker();
		if(planarAttachment != null)
		{
			kicker.setSide(planarAttachment.ordinal());
			kicker.setFacadeMask(facadeMask);
			kicker.setBox(fullBounds);
			kicker.setThickness(planarThickness);
		}
		
		QuadReInterpolator interpolator = new QuadReInterpolator();
		
		for(int cullFaceIdx = 0; cullFaceIdx <= ModelHelper.NULL_FACE_ID; cullFaceIdx++)
		{
			Direction cullFace = ModelHelper.faceFromIndex(cullFaceIdx);
			List<BakedQuad> quads = renderType == null || model.getRenderTypes(blockState, random, ModelData.EMPTY)
					.contains(renderType) ?
									model.getQuads(blockState, cullFace, random, ModelData.EMPTY, renderType)
										  : List.of();
			
			for(BakedQuad quad : quads)
			{
				QuadTinter quadTinter = null;
				
				// Prebake the color tint into the quad
				if(quad.getTintIndex() != -1)
				{
					quadTinter = new QuadTinter(
							blockColors.getColor(blockState, facadeAccess, pos, quad.getTintIndex()));
				}
				
				for(AABB box : holeStrips)
				{
					emitter.fromVanilla(quad.getVertices(), 0, false);
					// Keep the cull-face for faces that are flush with the outer block-face on the
					// side the facade is attached to, but clear it for anything that faces inwards
					emitter.cullFace(cullFace == planarAttachment ? planarAttachment : null);
					emitter.nominalFace(quad.getDirection());
					interpolator.setInputQuad(emitter);
					
					QuadClamper clamper = new QuadClamper(box);
					if(!clamper.transform(emitter))
						continue;
					
					// Strips faces if they match a mask.
					if(!faceStripper.transform(emitter))
						continue;
					
					// Kicks the edge inner corners in, solves Z fighting
					if(planarAttachment != null && !kicker.transform(emitter))
						continue;
					
					interpolator.transform(emitter);
					
					// Tints the quad if we need it to. Disabled by default.
					if(quadTinter != null)
						quadTinter.transform(emitter);
					
					emitter.emit();
				}
			}
		}
		
		return meshBuilder.build();
	}
	
	@javax.annotation.Nullable
	private static AEAxisAlignedBB getCutOutBox(AABB facadeBox, List<AABB> partBoxes)
	{
		AEAxisAlignedBB b = null;
		for(AABB bb : partBoxes)
		{
			if(bb.intersects(facadeBox))
			{
				if(b == null)
				{
					b = AEAxisAlignedBB.fromBounds(bb);
				} else
				{
					b.maxX = Math.max(b.maxX, bb.maxX);
					b.maxY = Math.max(b.maxY, bb.maxY);
					b.maxZ = Math.max(b.maxZ, bb.maxZ);
					b.minX = Math.min(b.minX, bb.minX);
					b.minY = Math.min(b.minY, bb.minY);
					b.minZ = Math.min(b.minZ, bb.minZ);
				}
			}
		}
		return b;
	}
	
	/**
	 * Generates the box segments around the specified hole. If the specified hole is null, a Singleton of the Facade
	 * box is returned.
	 *
	 * @param fb
	 * 		The Facade's box.
	 * @param hole
	 * 		The hole to 'cut'.
	 * @param axis
	 * 		The axis the facade is on.
	 *
	 * @return The box segments.
	 */
	private static List<AABB> getBoxes(AABB fb, AEAxisAlignedBB hole, Direction.Axis axis)
	{
		if(hole == null)
		{
			return Collections.singletonList(fb);
		}
		List<AABB> boxes = new ArrayList<>();
		switch(axis)
		{
			case Y:
				boxes.add(new AABB(fb.minX, fb.minY, fb.minZ, hole.minX, fb.maxY, fb.maxZ));
				boxes.add(new AABB(hole.maxX, fb.minY, fb.minZ, fb.maxX, fb.maxY, fb.maxZ));
				
				boxes.add(new AABB(hole.minX, fb.minY, fb.minZ, hole.maxX, fb.maxY, hole.minZ));
				boxes.add(new AABB(hole.minX, fb.minY, hole.maxZ, hole.maxX, fb.maxY, fb.maxZ));
				
				break;
			case Z:
				boxes.add(new AABB(fb.minX, fb.minY, fb.minZ, fb.maxX, hole.minY, fb.maxZ));
				boxes.add(new AABB(fb.minX, hole.maxY, fb.minZ, fb.maxX, fb.maxY, fb.maxZ));
				
				boxes.add(new AABB(fb.minX, hole.minY, fb.minZ, hole.minX, hole.maxY, fb.maxZ));
				boxes.add(new AABB(hole.maxX, hole.minY, fb.minZ, fb.maxX, hole.maxY, fb.maxZ));
				
				break;
			case X:
				boxes.add(new AABB(fb.minX, fb.minY, fb.minZ, fb.maxX, hole.minY, fb.maxZ));
				boxes.add(new AABB(fb.minX, hole.maxY, fb.minZ, fb.maxX, fb.maxY, fb.maxZ));
				
				boxes.add(new AABB(fb.minX, hole.minY, fb.minZ, fb.maxX, hole.maxY, hole.minZ));
				boxes.add(new AABB(fb.minX, hole.minY, hole.maxZ, fb.maxX, hole.maxY, fb.maxZ));
				break;
			default:
				// should never happen.
				throw new RuntimeException("switch falloff. " + String.valueOf(axis));
		}
		
		return boxes;
	}
	
	public static class FacadeBlockAccess
			implements BlockAndTintGetter
	{
		private final BlockAndTintGetter level;
		private final BlockPos pos;
		private final Direction side;
		private final BlockState state;
		
		public FacadeBlockAccess(BlockAndTintGetter level, BlockPos pos, Direction side, BlockState state)
		{
			this.level = level;
			this.pos = pos;
			this.side = side;
			this.state = state;
		}
		
		@javax.annotation.Nullable
		@Override
		public BlockEntity getBlockEntity(BlockPos pos)
		{
			return this.level.getBlockEntity(pos);
		}
		
		@Override
		public BlockState getBlockState(BlockPos pos)
		{
			if(this.pos == pos)
			{
				return this.state;
			}
			return this.level.getBlockState(pos);
		}
		
		@Override
		public FluidState getFluidState(BlockPos pos)
		{
			return level.getFluidState(pos);
		}
		
		// This is for diffuse lighting
		@Override
		public float getShade(Direction p_230487_1_, boolean p_230487_2_)
		{
			return level.getShade(p_230487_1_, p_230487_2_);
		}
		
		@Override
		public LevelLightEngine getLightEngine()
		{
			return level.getLightEngine();
		}
		
		@Override
		public int getBlockTint(BlockPos blockPosIn, ColorResolver colorResolverIn)
		{
			return level.getBlockTint(blockPosIn, colorResolverIn);
		}
		
		@Override
		public int getHeight()
		{
			return level.getHeight();
		}
		
		@Override
		public int getMinBuildHeight()
		{
			return level.getMinBuildHeight();
		}
	}
	
	private static class AEAxisAlignedBB
	{
		public double minX;
		public double minY;
		public double minZ;
		public double maxX;
		public double maxY;
		public double maxZ;
		
		public AABB getBoundingBox()
		{
			return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
		}
		
		public AEAxisAlignedBB(double a, double b, double c, double d, double e,
							   double f)
		{
			this.minX = a;
			this.minY = b;
			this.minZ = c;
			this.maxX = d;
			this.maxY = e;
			this.maxZ = f;
		}
		
		public static AEAxisAlignedBB fromBounds(double a, double b, double c, double d,
												 double e, double f)
		{
			return new AEAxisAlignedBB(a, b, c, d, e, f);
		}
		
		public static AEAxisAlignedBB fromBounds(AABB bb)
		{
			return new AEAxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
		}
	}
}