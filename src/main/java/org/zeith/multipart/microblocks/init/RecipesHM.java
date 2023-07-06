package org.zeith.multipart.microblocks.init;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.RecipeShape;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.recipe.*;
import org.zeith.multipart.microblocks.api.recipe.combination.*;
import org.zeith.multipart.microblocks.recipes.*;

import java.util.List;

@ProvideRecipes
public class RecipesHM
		implements IRecipeProvider
{
	static
	{
		MinecraftForge.EVENT_BUS.addListener(RecipesHM::addConversions);
		MinecraftForge.EVENT_BUS.addListener(RecipesHM::addFusions);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, RecipesHM::addFusionsLast);
	}
	
	@Override
	public void provideRecipes(RegisterRecipesEvent e)
	{
		e.shaped()
				.shape("sss", "sih")
				.map('s', Tags.Items.RODS_WOODEN)
				.map('h', TagsHL.Items.GEARS_STONE)
				.map('i', Items.POLISHED_GRANITE)
				.result(ItemsHM.GRANITE_SAW)
				.register();
		
		e.shaped()
				.shape("sss", "sih")
				.map('s', Tags.Items.RODS_WOODEN)
				.map('h', TagsHL.Items.GEARS_STONE)
				.map('i', Tags.Items.INGOTS_GOLD)
				.result(ItemsHM.GOLD_SAW)
				.register();
		
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
		e.add(new RecipeFuseMicroblock(HammerMicroblocks.id("microblock_fusion")));
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
		
		e.add(new MicroblockConversionRecipe(
				List.of(new MicroblockInput(0, 1, MicroblockIngredient.of(MicroblockTypesHM.HOLLOW_PANEL))),
				MicroblockTypesHM.HOLLOW_COVER,
				2
		));
		
		e.add(new MicroblockConversionRecipe(
				List.of(new MicroblockInput(0, 1, MicroblockIngredient.of(MicroblockTypesHM.ANTI_COVER))),
				MicroblockTypesHM.COVER,
				7
		));
		
		e.add(new MicroblockConversionRecipe(
				List.of(new MicroblockInput(0, 1, MicroblockIngredient.of(MicroblockTypesHM.TRIPLE_COVER))),
				MicroblockTypesHM.COVER,
				3
		));
		
		e.add(new MicroblockConversionRecipe(
				List.of(new MicroblockInput(0, 1, MicroblockIngredient.of(MicroblockTypesHM.TRIPLE_PANEL))),
				MicroblockTypesHM.PANEL,
				3
		));
	}
	
	private static void addFusions(GatherMicroblockComboRecipesEvent e)
	{
		e.add(ShapedMicroblockRecipe.newRecipe(
				MicroblockTypesHM.HOLLOW_COVER, false, 8,
				new RecipeShape("ccc", "c c", "ccc"),
				Tuples.immutable('c', MicroblockIngredient.of(MicroblockTypesHM.COVER))
		));
		e.add(ShapedMicroblockRecipe.newRecipe(
				MicroblockTypesHM.COVER, false, 8,
				new RecipeShape("ccc", "c c", "ccc"),
				Tuples.immutable('c', MicroblockIngredient.of(MicroblockTypesHM.HOLLOW_COVER))
		));
		
		e.add(ShapedMicroblockRecipe.newRecipe(
				MicroblockTypesHM.HOLLOW_PANEL, false, 8,
				new RecipeShape("ccc", "c c", "ccc"),
				Tuples.immutable('c', MicroblockIngredient.of(MicroblockTypesHM.PANEL))
		));
		e.add(ShapedMicroblockRecipe.newRecipe(
				MicroblockTypesHM.PANEL, false, 8,
				new RecipeShape("ccc", "c c", "ccc"),
				Tuples.immutable('c', MicroblockIngredient.of(MicroblockTypesHM.HOLLOW_PANEL))
		));
	}
	
	private static void addFusionsLast(GatherMicroblockComboRecipesEvent e)
	{
		// Slabs
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(2, MicroblockIngredient.of(MicroblockTypesHM.SLAB)),
				null, true, 1
		));
		
		// Hollow Covers
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(2, MicroblockIngredient.of(MicroblockTypesHM.HOLLOW_COVER)),
				MicroblockTypesHM.HOLLOW_PANEL, false, 1
		));
		
		// Panels
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(2, MicroblockIngredient.of(MicroblockTypesHM.PANEL)),
				MicroblockTypesHM.SLAB, false, 1
		));
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(3, MicroblockIngredient.of(MicroblockTypesHM.PANEL)),
				MicroblockTypesHM.TRIPLE_PANEL, false, 1
		));
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(4, MicroblockIngredient.of(MicroblockTypesHM.PANEL)),
				null, true, 1
		));
		
		// Covers
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(2, MicroblockIngredient.of(MicroblockTypesHM.COVER)),
				MicroblockTypesHM.PANEL, false, 1
		));
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(3, MicroblockIngredient.of(MicroblockTypesHM.COVER)),
				MicroblockTypesHM.TRIPLE_COVER, false, 1
		));
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(4, MicroblockIngredient.of(MicroblockTypesHM.COVER)),
				MicroblockTypesHM.SLAB, false, 1
		));
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(6, MicroblockIngredient.of(MicroblockTypesHM.COVER)),
				MicroblockTypesHM.TRIPLE_PANEL, false, 1
		));
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(7, MicroblockIngredient.of(MicroblockTypesHM.COVER)),
				MicroblockTypesHM.ANTI_COVER, false, 1
		));
		
		e.add(new ShapelessMicroblockRecipe(
				NonNullList.withSize(8, MicroblockIngredient.of(MicroblockTypesHM.COVER)),
				null, true, 1
		));
	}
}