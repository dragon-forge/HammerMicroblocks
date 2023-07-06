package org.zeith.multipart.microblocks.api.recipe.combination;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.function.Consumer;

public class GatherMicroblockComboRecipesEvent
		extends Event
{
	protected final Consumer<IMicroblockComboRecipe> acceptor;
	
	public GatherMicroblockComboRecipesEvent(Consumer<IMicroblockComboRecipe> acceptor)
	{
		this.acceptor = acceptor;
	}
	
	public static List<IMicroblockComboRecipe> get()
	{
		List<IMicroblockComboRecipe> recipes = Lists.newArrayList();
		MinecraftForge.EVENT_BUS.post(new GatherMicroblockComboRecipesEvent(recipes::add));
		return recipes;
	}
	
	public void add(IMicroblockComboRecipe recipe)
	{
		acceptor.accept(recipe);
	}
}