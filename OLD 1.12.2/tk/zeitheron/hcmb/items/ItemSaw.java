package tk.zeitheron.hcmb.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemSaw
		extends Item
{
	public ItemStack repairStack;
	public ResourceLocation modelTexture;

	public ItemSaw(String name, int durability, ItemStack repairStack, ResourceLocation texture)
	{
		setTranslationKey(name + "_saw");
		setMaxStackSize(1);
		setMaxDamage(durability);
		this.modelTexture = texture;
		this.repairStack = repairStack;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return !repairStack.isEmpty() && repair.isItemEqual(repairStack);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		ItemStack stack = itemStack.copy();
		if(itemStack.getMaxDamage() > 0)
			stack.setItemDamage(stack.getItemDamage() + 1);
		return stack;
	}
}