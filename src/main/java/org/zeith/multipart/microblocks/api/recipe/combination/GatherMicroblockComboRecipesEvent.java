package org.zeith.multipart.microblocks.api.recipe.combination;

import com.google.common.collect.Lists;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

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
		NeoForge.EVENT_BUS.post(new GatherMicroblockComboRecipesEvent(recipes::add));
		return recipes;
	}
	
	public void add(IMicroblockComboRecipe recipe)
	{
		acceptor.accept(recipe);
	}
}