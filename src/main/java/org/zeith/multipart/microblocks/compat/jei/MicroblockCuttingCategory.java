package org.zeith.multipart.microblocks.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.recipe.*;
import org.zeith.multipart.microblocks.init.ItemsHM;
import org.zeith.multipart.microblocks.items.ItemSaw;

import java.util.*;

public class MicroblockCuttingCategory
		implements IRecipeCategory<MicroblockConversionRecipe>
{
	protected final IGuiHelper helper;
	
	protected final IDrawable background, icon;
	protected final Component title;
	
	public MicroblockCuttingCategory(IGuiHelper helper)
	{
		this.helper = helper;
		
		this.background = helper.createDrawable(new ResourceLocation("textures/gui/container/crafting_table.png"), 29, 16, 116, 54);
		this.icon = helper.createDrawableItemStack(ItemsHM.DIAMOND_SAW.getDefaultInstance());
		this.title = Component.translatable("jei." + HammerMicroblocks.MOD_ID + ".microblock_cutting");
	}
	
	@Override
	public RecipeType<MicroblockConversionRecipe> getRecipeType()
	{
		return JEIHammerMicroblocks.CONVERSION_TYPE;
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
	public void setRecipe(IRecipeLayoutBuilder builder, MicroblockConversionRecipe recipe, IFocusGroup focuses)
	{
		var gridHelper = helper.createCraftingGridHelper();
		
		ItemStack materials = Items.STONE.getDefaultInstance();
		
		// All values here are zero because we always have the (0,0) point taken by the saw.
		int minX = 0, minY = 0;
		int maxX = 0, maxY = 0;
		
		for(MicroblockInput input : recipe.inputs())
		{
			minX = Math.min(minX, input.relativeX());
			minY = Math.min(minY, input.relativeY());
			maxX = Math.max(maxX, input.relativeX());
			maxY = Math.max(maxY, input.relativeY());
		}
		
		int width = maxX - minX + 1;
		int height = maxY - minY + 1;
		
		List<List<ItemStack>> inputs = new ArrayList<>();
		
		int count = width * height;
		for(int i = 0; i < count; i++)
			inputs.add(new ArrayList<>());
		
		for(MicroblockInput input : recipe.inputs())
		{
			int x = input.relativeX() + minX;
			int y = input.relativeY() + minY;
			var itemsHere = inputs.get(x + y * width);
			var obj = input.input();
			if(obj.isFullBlock()) itemsHere.add(materials.copyWithCount(1));
			else HammerMicroblocks.microblockTypes()
					.getValues()
					.stream()
					.filter(obj::matchesType)
					.map(t -> ItemsHM.MICROBLOCK.forItemRaw(t, materials, 1))
					.forEach(itemsHere::add);
		}
		
		inputs.get(minX + minY * width).addAll(ItemSaw.all().map(Item::getDefaultInstance).toList());
		
		gridHelper.createAndSetOutputs(builder, List.of(ItemsHM.MICROBLOCK.forItemRaw(recipe.output(), materials, recipe.count())));
		gridHelper.createAndSetInputs(builder, inputs, width, height);
	}
	
	@Override
	public boolean isHandled(MicroblockConversionRecipe recipe)
	{
		return !recipe.inputs().isEmpty() && recipe.output() != null && recipe.count() > 0;
	}
}
