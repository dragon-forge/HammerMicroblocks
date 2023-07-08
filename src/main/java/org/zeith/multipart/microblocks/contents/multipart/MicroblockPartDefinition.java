package org.zeith.multipart.microblocks.contents.multipart;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.*;
import org.zeith.multipart.api.*;
import org.zeith.multipart.api.placement.*;
import org.zeith.multipart.client.*;
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
				
				var mc = Minecraft.getInstance();
				var model = mc.getBlockRenderer().getBlockModel(mb.state.asBlockState());
				
				return new MultipartEffects.TintedSprite(
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