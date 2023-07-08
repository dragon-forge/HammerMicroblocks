package org.zeith.multipart.microblocks.contents.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.recipe.MicroblockedStack;
import org.zeith.multipart.microblocks.api.recipe.combination.*;
import org.zeith.multipart.microblocks.contents.items.ItemMicroblock;
import org.zeith.multipart.microblocks.init.*;

import java.util.*;

public class RecipeFuseMicroblock
		implements CraftingRecipe
{
	protected final List<IMicroblockComboRecipe> conversions = GatherMicroblockComboRecipesEvent.get();
	
	protected final ResourceLocation id;
	
	public RecipeFuseMicroblock(ResourceLocation id)
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
		var w = inv.getWidth();
		var h = inv.getHeight();
		
		// Gather all inputs
		MicroblockedStack firstNonNull = null;
		List<MicroblockedStack> inputs = Lists.newArrayList();
		for(int x = 0; x < w; ++x)
			for(int y = 0; y < h; y++)
			{
				var item = inv.getItem(x + y * w).copyWithCount(1);
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
					return inputs.get(0).stateAsItem().copyWithCount(r.count());
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
			implements RecipeSerializer<RecipeFuseMicroblock>
	{
		@Override
		public RecipeFuseMicroblock fromJson(ResourceLocation id, JsonObject p_44104_)
		{
			return new RecipeFuseMicroblock(id);
		}
		
		@Override
		public @Nullable RecipeFuseMicroblock fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
		{
			return new RecipeFuseMicroblock(id);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buf, RecipeFuseMicroblock recipe)
		{
		}
	}
}