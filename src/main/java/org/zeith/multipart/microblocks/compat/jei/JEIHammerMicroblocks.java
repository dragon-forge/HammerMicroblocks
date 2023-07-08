package org.zeith.multipart.microblocks.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.recipe.*;
import org.zeith.multipart.microblocks.api.recipe.combination.*;
import org.zeith.multipart.microblocks.contents.items.ItemSaw;
import org.zeith.multipart.microblocks.init.ItemsHM;

@JeiPlugin
public class JEIHammerMicroblocks
		implements IModPlugin
{
	public static final ResourceLocation PLUGIN_ID = HammerMicroblocks.id("jei");
	public static final RecipeType<MicroblockConversionRecipe> CONVERSION_TYPE = RecipeType.create(HammerMicroblocks.MOD_ID, "mb_conversions", MicroblockConversionRecipe.class);
	public static final RecipeType<IMicroblockComboRecipe> COMBINATION_TYPE = RecipeType.create(HammerMicroblocks.MOD_ID, "mb_combinations", IMicroblockComboRecipe.class);
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		var guih = registration.getJeiHelpers().getGuiHelper();
		
		registration.addRecipeCategories(
				new MicroblockCuttingCategory(guih),
				new MicroblockCombinationCategory(guih)
		);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
//		registration.addRecipeClickArea(CraftingScreen.class, 88, 32, 28, 23, COMBINATION_TYPE, CONVERSION_TYPE);
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		registration.addItemStackInfo(
				ItemSaw.all()
						.map(Item::getDefaultInstance)
						.toList(),
				Component.translatable("jei." + HammerMicroblocks.MOD_ID + ".saw_info")
		);
		
		registration.addRecipes(CONVERSION_TYPE, GatherMicroblockConversionRecipesEvent.get());
		registration.addRecipes(COMBINATION_TYPE, GatherMicroblockComboRecipesEvent.get());
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		registration.registerSubtypeInterpreter(ItemsHM.MICROBLOCK, (ingredient, context) ->
		{
			var mcb = ingredient.getTagElement("Microblock");
			if(mcb != null)
			{
				var type = ResourceLocation.tryParse(mcb.getString("Type"));
				return type + ";";
			}
			return "null";
		});
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(Items.CRAFTING_TABLE.getDefaultInstance(), CONVERSION_TYPE);
		registration.addRecipeCatalyst(Items.CRAFTING_TABLE.getDefaultInstance(), COMBINATION_TYPE);
		
		ItemSaw.all().map(Item::getDefaultInstance).forEach(st ->
				registration.addRecipeCatalyst(st, CONVERSION_TYPE)
		);
	}
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return PLUGIN_ID;
	}
}