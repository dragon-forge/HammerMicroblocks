package org.zeith.multipart.microblocks.items;

import net.minecraft.world.item.*;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.multipart.microblocks.init.TagsHM;

public class ItemSaw
		extends DiggerItem
{
	public ItemSaw(Tier tier, float baseDamage, float speedModifier, Item.Properties properties)
	{
		super(baseDamage, speedModifier, tier, TagsHM.Blocks.MINEABLE_WITH_SAW, properties);
		HLConstants.HL_TAB.add(this);
		TagAdapter.bind(TagsHM.Items.TOOLS_SAW, this);
	}
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack)
	{
		ItemStack stack = itemStack.copy();
		if(itemStack.getMaxDamage() > 0)
			stack.setDamageValue(stack.getDamageValue() + 1);
		return stack;
	}
	
	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack)
	{
		return true;
	}
}