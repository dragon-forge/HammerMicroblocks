package org.zeith.multipart.microblocks.contents.recipes;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
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
import org.zeith.multipart.microblocks.api.recipe.*;
import org.zeith.multipart.microblocks.contents.items.ItemSaw;
import org.zeith.multipart.microblocks.init.*;

import java.util.List;
import java.util.Optional;

public class RecipeCutMicroblock
		implements CraftingRecipe
{
	protected final List<MicroblockConversionRecipe> conversions = GatherMicroblockConversionRecipesEvent.get();
	
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
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider access)
	{
		int sawX = -1, sawY = -1;
		var saw = ItemStack.EMPTY;
		ItemSaw sawItem = null;
		
		var w = inv.width();
		var h = inv.height();
		for(int x = 0; x < w; ++x)
			for(int y = 0; y < h; y++)
			{
				var item = inv.getItem(x, y).copyWithCount(1);
				if(item.isEmpty()) continue;
				
				if(item.getItem() instanceof ItemSaw si)
				{
					if(!saw.isEmpty()) return ItemStack.EMPTY;
					sawItem = si;
					saw = item;
					sawX = x;
					sawY = y;
				}
			}
		
		if(saw.isEmpty() || sawItem == null) return ItemStack.EMPTY;
		
		// Gather all inputs
		List<MicroblockedStack> inputs = Lists.newArrayList();
		for(int x = 0; x < w; ++x)
			for(int y = 0; y < h; y++)
			{
				var item = inv.getItem(x, y).copyWithCount(1);
				if(item.isEmpty()) continue;
				if(item.getItem() instanceof ItemSaw) continue;
				var st = getStackFrom(item, x - sawX, y - sawY);
				
				// ensure we have same block in every spot
				if(st == null || (!inputs.isEmpty() && !inputs.get(0).sameState(st)))
					return ItemStack.EMPTY;
				
				inputs.add(st);
			}
		
		if(inputs.isEmpty()) return ItemStack.EMPTY;
		
		var theState = inputs.get(0).state();
		if(theState.is(sawItem.getTier().getIncorrectBlocksForDrops())) return ItemStack.EMPTY;
		
		for(MicroblockConversionRecipe conversion : conversions)
			if(conversion.matches(inputs))
				return ItemsHM.MICROBLOCK.forItem(conversion.output(), inputs.get(0)
						.stateAsItem(), conversion.count(), false);
		
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
		return RecipeSerializersHM.MICROBLOCK_CUTTING_SERIALIZER;
	}
	
	public static class SimpleSerializer
			implements RecipeSerializer<RecipeCutMicroblock>
	{
		public static final MapCodec<RecipeCutMicroblock> CODEC = MapCodec.unit(RecipeCutMicroblock::new);
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeCutMicroblock> STREAM_CODEC = StreamCodec.of(
				(buf, recipe) ->
				{
				},
				buf -> new RecipeCutMicroblock()
		);
		
		@Override
		public MapCodec<RecipeCutMicroblock> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeCutMicroblock> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}