package org.zeith.multipart.microblocks.multipart;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.*;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.multipart.api.*;
import org.zeith.multipart.api.placement.*;
import org.zeith.multipart.client.*;
import org.zeith.multipart.client.model.IBakedMultipartModel;
import org.zeith.multipart.microblocks.api.tile.MicroblockState;
import org.zeith.multipart.microblocks.client.resource.model.ModelGeneratorSystem;
import org.zeith.multipart.microblocks.init.PartDefinitionsHM;
import org.zeith.multipart.microblocks.multipart.entity.MicroblockEntity;
import org.zeith.multipart.mixins.client.TextureSheetParticleAccessor;

import java.util.Random;
import java.util.function.*;

public class MicroblockPartDefinition
		extends PartDefinition
{
	public MicroblockPartDefinition()
	{
	}
	
	@Override
	public boolean canPlaceAt(PartContainer container, @Nullable IConfiguredPartPlacer placer, PartPlacement placement)
	{
		return super.canPlaceAt(container, placer, placement);
	}
	
	@Override
	public SoundType getSoundType(PartEntity entity)
	{
		return entity instanceof MicroblockEntity mb ? mb.getSoundType() : SoundType.STONE;
	}
	
	@Override
	public MicroblockEntity createEntity(PartContainer container, PartPlacement placement)
	{
		return new MicroblockEntity(this, container, placement);
	}
	
	@Override
	public void initializeClient(Consumer<IClientPartDefinitionExtensions> consumer)
	{
		consumer.accept(new IClientPartDefinitionExtensions()
		{
			@Override
			public boolean addHitEffects(BlockHitResult target, PartEntity part, ParticleEngine manager)
			{
				if(!(part instanceof MicroblockEntity mb) || !mb.state.isValid()) return true;
				
				spawnHitFX(part.container().pos(), part.getShape(), target, () ->
				{
					var model = Minecraft.getInstance().getBlockRenderer().getBlockModel(mb.state.asBlockState());
					return new MultipartEffects.TintedSprite(model.getParticleIcon(ModelData.EMPTY));
				});
				
				return true;
			}
			
			@Override
			public boolean addDestroyEffects(PartEntity part, ParticleEngine manager)
			{
				if(!(part instanceof MicroblockEntity mb) || !mb.state.isValid()) return true;
				
				MultipartEffects.spawnBreakFX(part.container().pos(), part.getShape(), () ->
				{
					var model = Minecraft.getInstance().getBlockRenderer().getBlockModel(mb.state.asBlockState());
					return new MultipartEffects.TintedSprite(model.getParticleIcon(ModelData.EMPTY));
				});
				
				return true;
			}
			
			static final Random random = new Random();
			
			public static void spawnHitFX(BlockPos pos, VoxelShape shape, BlockHitResult hit, Supplier<MultipartEffects.TintedSprite> factory)
			{
				var state = WorldPartComponents.BLOCK.defaultBlockState();
				var mc = Minecraft.getInstance();
				
				var model = mc.getBlockRenderer()
						.getBlockModelShaper()
						.getBlockModel(state);
				
				if(!(model instanceof IBakedMultipartModel bmm)) return;
				
				var side = hit.getDirection();
				
				int i = pos.getX();
				int j = pos.getY();
				int k = pos.getZ();
				
				float f = 0.1F;
				
				AABB aabb = shape.bounds();
				
				double d0 = i + random.nextDouble() * (aabb.maxX - aabb.minX - 0.2F) + f + aabb.minX;
				double d1 = j + random.nextDouble() * (aabb.maxY - aabb.minY - 0.2F) + f + aabb.minY;
				double d2 = k + random.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2F) + f + aabb.minZ;
				
				if(side == Direction.DOWN)
					d1 = (double) j + aabb.minY - (double) 0.1F;
				
				if(side == Direction.UP)
					d1 = (double) j + aabb.maxY + (double) 0.1F;
				
				if(side == Direction.NORTH)
					d2 = (double) k + aabb.minZ - (double) 0.1F;
				
				if(side == Direction.SOUTH)
					d2 = (double) k + aabb.maxZ + (double) 0.1F;
				
				if(side == Direction.WEST)
					d0 = (double) i + aabb.minX - (double) 0.1F;
				
				if(side == Direction.EAST)
					d0 = (double) i + aabb.maxX + (double) 0.1F;
				
				var sprites = factory.get();
				
				var fx = new TerrainParticle(mc.level, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, pos)
						.setPower(0.2F)
						.scale(0.6F);
				
				if(sprites != null &&
						fx instanceof TextureSheetParticleAccessor mixin)
				{
					int rgb = sprites.rgb();
					float red = ColorHelper.getRed(rgb) * 0.6F;
					float green = ColorHelper.getGreen(rgb) * 0.6F;
					float blue = ColorHelper.getBlue(rgb) * 0.6F;
					fx.setColor(red, green, blue);
					
					mixin.callSetSprite(sprites.sprite());
					mc.particleEngine.add(fx);
				}
			}
			
			@Override
			public boolean getQuads(PartEntity part, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType, Consumer<BakedQuad> addQuad)
			{
				if(!(part instanceof MicroblockEntity mb)) return true;
				if(side == null) return true;
				
				var ctr = part.container();
				var state = mb.state;
				
				if(!state.isValid()) return true;
				
				var strips = state.getType().getModelStrips(mb.placement());
				ModelGeneratorSystem.generateMesh(state.getType(), mb.placement(), ctr, ctr.level(), ctr.pos(), strips, state.asBlockState(), side, rand, renderType)
						.toBakedBlockQuads()
						.forEach(addQuad);
				
				return true;
			}
		});
	}
	
	public record MicroblockConfiguration(MicroblockState state)
			implements IConfiguredPartPlacer
	{
		@Override
		public PartEntity create(PartContainer container, PartPlacement placement)
		{
			if(!state.isValid())
				return null;
			var e = new MicroblockEntity(PartDefinitionsHM.MICROBLOCK, container, placement);
			e.initState(state);
			return e;
		}
	}
}