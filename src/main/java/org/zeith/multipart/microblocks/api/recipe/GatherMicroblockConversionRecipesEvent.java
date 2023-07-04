package org.zeith.multipart.microblocks.api.recipe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.function.Consumer;

public class GatherMicroblockConversionRecipesEvent
		extends Event
{
	protected final Consumer<MicroblockConversionRecipe> acceptor;
	
	public GatherMicroblockConversionRecipesEvent(Consumer<MicroblockConversionRecipe> acceptor)
	{
		this.acceptor = acceptor;
	}
	
	public static List<MicroblockConversionRecipe> get()
	{
		List<MicroblockConversionRecipe> recipes = Lists.newArrayList();
		MinecraftForge.EVENT_BUS.post(new GatherMicroblockConversionRecipesEvent(recipes::add));
		return recipes;
	}
	
	public void add(MicroblockConversionRecipe recipe)
	{
		acceptor.accept(recipe);
	}
}