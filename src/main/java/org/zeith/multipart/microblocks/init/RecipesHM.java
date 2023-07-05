package org.zeith.multipart.microblocks.init;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.recipe.*;
import org.zeith.multipart.microblocks.recipes.RecipeCutMicroblock;

import java.util.List;

@ProvideRecipes
public class RecipesHM
		implements IRecipeProvider
{
	static
	{
		MinecraftForge.EVENT_BUS.addListener(RecipesHM::addConversions);
	}
	
	@Override
	public void provideRecipes(RegisterRecipesEvent e)
	{
		e.shaped()
				.shape("sss", "sih")
				.map('s', Tags.Items.RODS_WOODEN)
				.map('h', TagsHL.Items.GEARS_STONE)
				.map('i', Tags.Items.INGOTS_IRON)
				.result(ItemsHM.IRON_SAW)
				.register();
		
		e.shaped()
				.shape("sss", "sih")
				.map('s', Tags.Items.RODS_WOODEN)
				.map('h', TagsHL.Items.GEARS_STONE)
				.map('i', Tags.Items.GEMS_DIAMOND)
				.result(ItemsHM.DIAMOND_SAW)
				.register();
		
		var id = ForgeRegistries.ITEMS.getKey(ItemsHM.NETHERITE_SAW);
		e.register(id, new SmithingTransformRecipe(id,
				Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
				Ingredient.of(ItemsHM.DIAMOND_SAW),
				RecipeHelper.fromTag(Tags.Items.INGOTS_NETHERITE),
				new ItemStack(ItemsHM.NETHERITE_SAW)
		));
		
		e.add(new RecipeCutMicroblock(HammerMicroblocks.id("microblock_cutting")));
	}
	
	private static void addConversions(GatherMicroblockConversionRecipesEvent e)
	{
		e.add(new MicroblockConversionRecipe(
				List.of(new MicroblockInput(0, 1, MicroblockIngredient.fullBlock())),
				MicroblockTypesHM.SLAB,
				2
		));
		
		e.add(new MicroblockConversionRecipe(
				List.of(new MicroblockInput(0, 1, MicroblockIngredient.of(MicroblockTypesHM.SLAB))),
				MicroblockTypesHM.PANEL,
				2
		));
		
		e.add(new MicroblockConversionRecipe(
				List.of(new MicroblockInput(0, 1, MicroblockIngredient.of(MicroblockTypesHM.PANEL))),
				MicroblockTypesHM.COVER,
				2
		));
	}
}