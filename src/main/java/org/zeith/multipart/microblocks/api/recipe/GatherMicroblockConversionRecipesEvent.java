package org.zeith.multipart.microblocks.api.recipe;

import com.google.common.collect.Lists;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

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
		NeoForge.EVENT_BUS.post(new GatherMicroblockConversionRecipesEvent(recipes::add));
		return recipes;
	}
	
	public void add(MicroblockConversionRecipe recipe)
	{
		acceptor.accept(recipe);
	}
}