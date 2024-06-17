package org.zeith.multipart.microblocks.contents.multipart;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.multipart.api.*;
import org.zeith.multipart.api.placement.IConfiguredPartPlacer;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.client.IClientPartDefinitionExtensions;
import org.zeith.multipart.client.MultipartEffects;
import org.zeith.multipart.microblocks.api.tile.MicroblockState;
import org.zeith.multipart.microblocks.client.resource.model.ModelGeneratorSystem;
import org.zeith.multipart.microblocks.contents.multipart.entity.MicroblockEntity;
import org.zeith.multipart.microblocks.init.PartDefinitionsHM;

import java.util.function.Consumer;

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
			private MultipartEffects.TintedSprite getSpriteFor(PartEntity part)
			{
				if(!(part instanceof MicroblockEntity mb) || !mb.state.isValid()) return null;
				
				var ctr = part.container();
				var level = Cast.cast(ctr.level(), ClientLevel.class);
				var pos = ctr.pos();
				
				if(level == null) return null;
				
				var mc = Minecraft.getInstance();
				var state = mb.state.asBlockState();
				var model = mc.getBlockRenderer().getBlockModel(state);
				
				int color = 0xFFFFFF;
				
				if(IClientBlockExtensions.of(state).areBreakingParticlesTinted(state, level, pos))
					color = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
				
				return new MultipartEffects.TintedSprite(
						color,
						model.getParticleIcon(ModelData.EMPTY)
				);
			}
			
			@Override
			public boolean addHitEffects(BlockHitResult target, PartEntity part, ParticleEngine manager)
			{
				MultipartEffects.spawnHitFX(part.getShape(), target, () -> getSpriteFor(part));
				return true;
			}
			
			@Override
			public boolean addDestroyEffects(PartEntity part, ParticleEngine manager)
			{
				MultipartEffects.spawnBreakFX(part.container().pos(), part.getShape(), () -> getSpriteFor(part));
				return true;
			}
			
			@Override
			public void addRunningEffects(PartEntity part, VoxelShape shape, Entity living, AABB entityBb, Vec3 particlePos, Vec3 particleMotion)
			{
				MultipartEffects.spawnRunningFX(
						part.container().pos(),
						shape,
						particlePos,
						particleMotion,
						() -> getSpriteFor(part)
				);
			}
			
			@Override
			public void addLandingEffects(PartEntity part, VoxelShape shape, LivingEntity living, int numberOfParticles, AABB entityBb, Vec3 particlePos)
			{
				MultipartEffects.spawnLandingFX(
						part.container().pos(),
						shape,
						particlePos,
						numberOfParticles,
						() -> getSpriteFor(part)
				);
			}
			
			@Override
			public boolean getQuads(PartEntity part, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType, Consumer<BakedQuad> addQuad)
			{
				if(!(part instanceof MicroblockEntity mb)) return true;
				if(side != null) return true;
				
				var ctr = part.container();
				var state = mb.state;
				
				if(!state.isValid()) return true;
				
				var rng = RandomSource.create(ctr.pos().asLong());
				
				var placement = mb.placement();
				var extraData = mb.state.getData();
				
				var strips = state.getType().getModelStrips(placement, extraData);
				ModelGeneratorSystem.generateMesh(state.getType(), placement, extraData, ctr, ctr.level(), ctr.pos(), strips, state.asBlockState(), rng, renderType)
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