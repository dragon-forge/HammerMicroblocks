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
import tk.zeitheron.hcmb.items.ItemSaw;

public class RecipeCutMicroblock
		extends IForgeRegistryEntry.Impl<IRecipe>
		implements IRecipe
{
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		boolean saw = false;
		boolean cuttableMicroblock = false;
		IBlockState cutState = null;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			if(stack.getItem() == ItemsHM.MICROBLOCK)
			{
				if(cuttableMicroblock)
				{
					return false;
				}
				cuttableMicroblock = ItemMicroblock.EnumMicroblockType.cut(ItemMicroblock.getMicroblockType(stack)) != null;
				continue;
			}
			if(stack.getItem() instanceof ItemSaw)
			{
				if(saw)
				{
					return false;
				}
				saw = true;
				continue;
			}
			Block block = Block.getBlockFromItem(stack.getItem());
			if(cuttableMicroblock || cutState != null)
				return false;
			cutState = block.getStateFromMeta(stack.getItemDamage());
			if(block.isFullBlock(cutState)) continue;
			return false;
		}
		return (cuttableMicroblock || cutState != null) && saw;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		boolean saw = false;
		boolean cuttableMicroblock = false;
		IBlockState cutState = null;
		ItemStack microblock = ItemStack.EMPTY;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			if(stack.getItem() == ItemsHM.MICROBLOCK)
			{
				if(cuttableMicroblock)
					return ItemStack.EMPTY;
				microblock = stack.copy();
				cuttableMicroblock = ItemMicroblock.EnumMicroblockType.cut(ItemMicroblock.getMicroblockType(stack)) != null;
				continue;
			}
			if(stack.getItem() instanceof ItemSaw)
			{
				if(saw)
					return ItemStack.EMPTY;
				saw = true;
				continue;
			}
			Block block = Block.getBlockFromItem(stack.getItem());
			if(block == null)
				return ItemStack.EMPTY;
			if(cuttableMicroblock || cutState != null)
				return ItemStack.EMPTY;
			cutState = block.getStateFromMeta(stack.getItemDamage());
			if(block.isFullBlock(cutState)) continue;
			return ItemStack.EMPTY;
		}
		if(cutState != null)
			return ItemMicroblock.makeStack(cutState, ItemMicroblock.EnumMicroblockType.SLAB, 2);
		microblock.setCount(2);
		ItemMicroblock.setMicroblockType(microblock, ItemMicroblock.EnumMicroblockType.cut(ItemMicroblock.getMicroblockType(microblock)));
		return microblock;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width * height > 1;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}

