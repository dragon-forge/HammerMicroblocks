package org.zeith.multipart.microblocks.multipart;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.*;
import org.zeith.multipart.api.*;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.client.IClientPartDefinitionExtensions;
import org.zeith.multipart.microblocks.multipart.entity.MicroblockEntity;

import java.util.function.Consumer;

public class MicroblockPartDefinition
		extends PartDefinition
{
	public MicroblockPartDefinition()
	{
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
				return true;
			}
			
			@Override
			public boolean addDestroyEffects(PartEntity part, ParticleEngine manager)
			{
				return true;
			}
			
			@Override
			public boolean getQuads(PartEntity part, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType, Consumer<BakedQuad> addQuad)
			{
				if(!(part instanceof MicroblockEntity mb)) return true;
				
				
				
				return true;
			}
		});
	}
}