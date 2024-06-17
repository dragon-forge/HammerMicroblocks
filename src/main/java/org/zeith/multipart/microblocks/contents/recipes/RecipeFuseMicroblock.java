package org.zeith.multipart.microblocks.contents.recipes;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.data.MicroblockComponent;
import org.zeith.multipart.microblocks.api.recipe.MicroblockedStack;
import org.zeith.multipart.microblocks.api.recipe.combination.GatherMicroblockComboRecipesEvent;
import org.zeith.multipart.microblocks.api.recipe.combination.IMicroblockComboRecipe;
import org.zeith.multipart.microblocks.init.*;

import java.util.*;

public class RecipeFuseMicroblock
		implements CraftingRecipe
{
	protected final List<IMicroblockComboRecipe> conversions = GatherMicroblockComboRecipesEvent.get();
	
	@Override
	public CraftingBookCategory category()
	{
		return CraftingBookCategory.MISC;
	}
	
	@Override
	public boolean isSpecial()
	{
		return true;
	}
	
	@Override
	public boolean matches(CraftingInput inv, Level worldIn)
	{
		return !assemble(inv, worldIn.registryAccess()).isEmpty();
	}
	
	@Override
	public boolean showNotification()
	{
		return false;
	}
	
	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider)
	{
		var w = inv.width();
		var h = inv.height();
		
		// Gather all inputs
		MicroblockedStack firstNonNull = null;
		List<MicroblockedStack> inputs = Lists.newArrayList();
		for(int x = 0; x < w; ++x)
			for(int y = 0; y < h; y++)
			{
				var item = inv.getItem(x, y).copyWithCount(1);
				if(item.isEmpty())
				{
					inputs.add(null);
					continue;
				}
				var st = getStackFrom(item, x, y);
				
				// ensure we have same block in every spot
				if(st == null || (firstNonNull != null && !firstNonNull.sameState(st)))
					return ItemStack.EMPTY;
				
				inputs.add(st);
				if(firstNonNull == null)
					firstNonNull = st;
			}
		
		if(inputs.isEmpty() || firstNonNull == null) return ItemStack.EMPTY;
		
		final var fnn = firstNonNull;
		
		for(var fusion : conversions)
		{
			var res = fusion.matchAndGetResult(inputs, w, h);
			
			var result = res.map(r ->
			{
				if(r.outputIsFullBlock())
					return inputs
							.stream()
							.filter(Objects::nonNull)
							.findFirst()
							.map(MicroblockedStack::stateAsItem)
							.orElse(ItemStack.EMPTY)
							.copyWithCount(r.count());
				else
					return ItemsHM.MICROBLOCK.forItem(r.type(), fnn.stateAsItem(), r.count(), false);
			}).orElse(ItemStack.EMPTY);
			
			if(!result.isEmpty()) return result;
		}
		
		return ItemStack.EMPTY;
	}
	
	@Nullable
	public MicroblockedStack getStackFrom(ItemStack cutStack, int relX, int relY)
	{
		BlockState theState;
		MicroblockType theType;
		if(cutStack.has(MicroblockComponent.TYPE))
		{
			var com = MicroblockComponent.get(cutStack);
			theState = com.materialState();
			theType = com.type();
			if(theState == null || theType == null) return null;
			return new MicroblockedStack(relX, relY, false, Optional.of(theType), theState, com.materialStack());
		} else
		{
			theType = MicroblockTypesHM.SLAB;
			var mcb = ItemsHM.MICROBLOCK.forItem(theType, cutStack, false);
			if(mcb.isEmpty()) return null;
			theState = MicroblockComponent.getOrDefault(mcb, MicroblockComponent::materialState, Blocks.AIR.defaultBlockState());
			if(theState == null) return null;
			return new MicroblockedStack(relX, relY, true, Optional.empty(), theState, cutStack.copyWithCount(1));
		}
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return width * height >= 2;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider access)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipeSerializersHM.MICROBLOCK_FUSION_SERIALIZER;
	}
	
	public static class SimpleSerializer
			implements RecipeSerializer<RecipeFuseMicroblock>
	{
		public static final MapCodec<RecipeFuseMicroblock> CODEC = MapCodec.unit(RecipeFuseMicroblock::new);
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeFuseMicroblock> STREAM_CODEC = StreamCodec.of(
				(buf, recipe) ->
				{
				},
				buf -> new RecipeFuseMicroblock()
		);
		
		@Override
		public MapCodec<RecipeFuseMicroblock> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeFuseMicroblock> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}