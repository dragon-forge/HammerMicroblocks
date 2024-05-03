package org.zeith.multipart.microblocks.contents.items;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.*;
import net.minecraftforge.common.TierSortingRegistry;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.init.TagsHM;

import java.util.*;
import java.util.stream.Stream;

public class ItemSaw
		extends DiggerItem
		implements IRegisterListener
{
	private static final Set<ItemSaw> SAW_ITEMS = Collections.synchronizedSet(new LinkedHashSet<>());
	
	protected final float tierIndex;
	
	public ItemSaw(float tierIndex, Tier tier, float baseDamage, float speedModifier, Item.Properties properties)
	{
		super(baseDamage, speedModifier, tier, TagsHM.Blocks.MINEABLE_WITH_SAW, properties);
		this.tierIndex = tierIndex;
		HLConstants.HL_TAB.add(this);
		HammerMicroblocks.MICROBLOCKS_TAB.add(this);
		TagAdapter.bind(TagsHM.Items.TOOLS_SAW, this);
	}
	
	@Override
	public final void onPostRegistered()
	{
		SAW_ITEMS.add(this);
		onPostRegisteredHook();
	}
	
	protected void onPostRegisteredHook()
	{
	}
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack)
	{
		ItemStack stack = itemStack.copy();
		int md = itemStack.getMaxDamage();
		if(md > 0)
		{
			int dv = stack.getDamageValue();
			if(dv < md)
			{
				int i = itemStack.getEnchantmentLevel(Enchantments.UNBREAKING);
				int j = 0;
				int pAmount = 1;
				for(int k = 0; i > 0 && k < pAmount; ++k)
					if(DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(itemStack, i, RandomSource.create()))
						++j;
				pAmount -= j;
				if(pAmount <= 0) return stack;
				
				stack.setDamageValue(dv + pAmount);
			} else return ItemStack.EMPTY;
		}
		return stack;
	}
	
	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack)
	{
		return true;
	}
	
	public static Ingredient getSawsMatchingTier(Tier tier)
	{
		var sorted = TierSortingRegistry.getSortedTiers();
		int idx = sorted.indexOf(tier);
		if(idx == -1) return Ingredient.of(SAW_ITEMS.toArray(ItemSaw[]::new));
		return Ingredient.of(SAW_ITEMS.stream()
				.filter(saw -> sorted.indexOf(saw.getTier()) >= idx)
				.toArray(ItemSaw[]::new));
	}
	
	public static Ingredient getSawsMatchingTier(float tier)
	{
		return Ingredient.of(SAW_ITEMS.stream()
				.filter(saw -> saw.tierIndex >= tier)
				.toArray(ItemSaw[]::new));
	}
	
	public static Stream<ItemSaw> all()
	{
		return SAW_ITEMS.stream();
	}
}