package org.zeith.multipart.microblocks.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.recipe.MicroblockIngredient;
import org.zeith.multipart.microblocks.api.recipe.combination.*;
import org.zeith.multipart.microblocks.init.*;

import java.util.*;

public class MicroblockCombinationCategory
		implements IRecipeCategory<IMicroblockComboRecipe>
{
	protected final IGuiHelper helper;
	
	protected final IDrawable background, icon;
	protected final Component title;
	
	public MicroblockCombinationCategory(IGuiHelper helper)
	{
		this.helper = helper;
		
		this.background = helper.createDrawable(new ResourceLocation("textures/gui/container/crafting_table.png"), 29, 16, 116, 54);
		this.icon = helper.createDrawableItemStack(ItemsHM.MICROBLOCK.forItemRaw(MicroblockTypesHM.HOLLOW_COVER, Items.STONE.getDefaultInstance(), 1));
		this.title = Component.translatable("jei." + HammerMicroblocks.MOD_ID + ".microblock_combination");
	}
	
	@Override
	public RecipeType<IMicroblockComboRecipe> getRecipeType()
	{
		return JEIHammerMicroblocks.COMBINATION_TYPE;
	}
	
	@Override
	public Component getTitle()
	{
		return title;
	}
	
	@Override
	public IDrawable getBackground()
	{
		return background;
	}
	
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IMicroblockComboRecipe recipe, IFocusGroup focuses)
	{
		var gridHelper = helper.createCraftingGridHelper();
		
		ItemStack materials = Items.STONE.getDefaultInstance();
		
		int width = 0;
		int height = 0;
		List<List<ItemStack>> inputs = new ArrayList<>();
		NonNullList<MicroblockIngredient> inputsRaw;
		
		if(recipe instanceof ShapedMicroblockRecipe r)
		{
			width = r.width();
			height = r.height();
			inputsRaw = r.inputs();
		} else if(recipe instanceof ShapelessMicroblockRecipe r)
		{
			inputsRaw = r.inputs();
		} else return;
		
		for(MicroblockIngredient obj : inputsRaw)
		{
			List<ItemStack> itemsHere = new ArrayList<>();
			if(obj.isFullBlock()) itemsHere.add(materials.copyWithCount(1));
			else HammerMicroblocks.microblockTypes()
					.getValues()
					.stream()
					.filter(obj::matchesType)
					.map(t -> ItemsHM.MICROBLOCK.forItemRaw(t, materials, 1))
					.forEach(itemsHere::add);
			inputs.add(itemsHere);
		}
		
		var result = recipe.getBaseResult();
		var resItem = result.outputIsFullBlock()
					  ? materials
					  : ItemsHM.MICROBLOCK.forItemRaw(result.type(), materials, result.count());
		
		gridHelper.createAndSetOutputs(builder, List.of(resItem));
		gridHelper.createAndSetInputs(builder, inputs, width, height);
	}
	
	@Override
	public boolean isHandled(IMicroblockComboRecipe recipe)
	{
		return recipe instanceof ShapedMicroblockRecipe
				|| recipe instanceof ShapelessMicroblockRecipe;
	}
}