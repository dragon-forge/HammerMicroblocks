package tk.zeitheron.hcmb.recipes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tk.zeitheron.hcmb.init.ItemsHM;
import tk.zeitheron.hcmb.items.ItemMicroblock;

public class RecipeAddMicroblocks
		extends IForgeRegistryEntry.Impl<IRecipe>
		implements IRecipe
{
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		Block block = null;
		int meta = 0;
		float thickness = 0.0f;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			if(stack.getItem() == ItemsHM.MICROBLOCK)
			{
				IBlockState state = ItemMicroblock.getState(stack);
				if(state == null)
					return false;
				if(block == null)
				{
					block = state.getBlock();
					meta = block.getMetaFromState(state);
				}
				if(block != state.getBlock() || meta != state.getBlock().getMetaFromState(state))
					return false;
				ItemMicroblock.EnumMicroblockType type = ItemMicroblock.getMicroblockType(stack);
				if(type == null)
					return false;
				thickness += type.thickness;
				continue;
			}
			return false;
		}
		return ItemMicroblock.EnumMicroblockType.byThickness(thickness) != null || thickness == 1.0f;
	}

	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		Block block = null;
		int meta = 0;
		float thickness = 0.0f;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			if(stack.getItem() == ItemsHM.MICROBLOCK)
			{
				IBlockState state = ItemMicroblock.getState(stack);
				if(state == null)
					return ItemStack.EMPTY;
				if(block == null)
				{
					block = state.getBlock();
					meta = block.getMetaFromState(state);
				}
				if(block != state.getBlock() || meta != state.getBlock().getMetaFromState(state))
					return ItemStack.EMPTY;
				ItemMicroblock.EnumMicroblockType type = ItemMicroblock.getMicroblockType(stack);
				if(type == null)
					return ItemStack.EMPTY;
				thickness += type.thickness;
				continue;
			}
			return ItemStack.EMPTY;
		}
		if(thickness == 1.0f)
			return new ItemStack(block, 1, meta);
		ItemMicroblock.EnumMicroblockType type = ItemMicroblock.EnumMicroblockType.byThickness(thickness);
		return ItemMicroblock.makeStack(block.getStateFromMeta(meta), type, 1);
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width * height > 1;
	}

	public int getRecipeSize()
	{
		return 10;
	}

	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return ForgeHooks.defaultRecipeGetRemainingItems((InventoryCrafting) inv);
	}
}

