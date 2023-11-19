package org.zeith.multipart.microblocks.contents.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.recipe.*;
import org.zeith.multipart.microblocks.contents.items.*;
import org.zeith.multipart.microblocks.init.*;

import java.util.*;

public class RecipeCutMicroblock
		implements CraftingRecipe
{
	protected final List<MicroblockConversionRecipe> conversions = GatherMicroblockConversionRecipesEvent.get();
	
	protected final ResourceLocation id;
	
	public RecipeCutMicroblock(ResourceLocation id)
	{
		this.id = id;
	}
	
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
	public boolean matches(CraftingContainer inv, Level worldIn)
	{
		return !assemble(inv, worldIn.registryAccess()).isEmpty();
	}
	
	@Override
	public boolean showNotification()
	{
		return false;
	}
	
	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess access)
	{
		int sawX = -1, sawY = -1;
		var saw = ItemStack.EMPTY;
		ItemSaw sawItem = null;
		
		var w = inv.getWidth();
		var h = inv.getHeight();
		for(int x = 0; x < w; ++x)
			for(int y = 0; y < h; y++)
			{
				var item = inv.getItem(x + y * w).copyWithCount(1);
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
				var item = inv.getItem(x + y * w).copyWithCount(1);
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
		if(!TierSortingRegistry.isCorrectTierForDrops(sawItem.getTier(), theState))
			return ItemStack.EMPTY;
		
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
		if(cutStack.getItem() instanceof ItemMicroblock imc)
		{
			theState = imc.getMicroblockMaterialState(cutStack);
			theType = imc.getMicroblockType(cutStack);
			if(theState == null || theType == null) return null;
			return new MicroblockedStack(relX, relY, false, Optional.of(theType), theState, imc.getMicroblockMaterialStack(cutStack));
		} else
		{
			theType = MicroblockTypesHM.SLAB;
			var mcb = ItemsHM.MICROBLOCK.forItem(theType, cutStack, false);
			if(mcb.isEmpty()) return null;
			theState = ItemsHM.MICROBLOCK.getMicroblockMaterialState(mcb);
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
	public ItemStack getResultItem(RegistryAccess access)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipeSerializersHM.MICROBLOCK_CUTTING_SERIALIZER;
	}
	
	public static class SimpleSerializer
			implements RecipeSerializer<RecipeCutMicroblock>
	{
		@Override
		public RecipeCutMicroblock fromJson(ResourceLocation id, JsonObject p_44104_)
		{
			return new RecipeCutMicroblock(id);
		}
		
		@Override
		public @Nullable RecipeCutMicroblock fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
		{
			return new RecipeCutMicroblock(id);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buf, RecipeCutMicroblock recipe)
		{
		}
	}
}