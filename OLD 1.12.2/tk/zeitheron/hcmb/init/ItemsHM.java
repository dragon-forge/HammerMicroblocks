package tk.zeitheron.hcmb.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tk.zeitheron.hcmb.items.ItemMicroblock;
import tk.zeitheron.hcmb.items.ItemSaw;

public class ItemsHM
{
	public static final Item STONE_HANDLE = new Item().setTranslationKey("stone_handle").setMaxStackSize(16);

	public static final ItemSaw IRON_SAW = new ItemSaw("iron", 250, new ItemStack(Items.IRON_INGOT), new ResourceLocation("hcmb", "textures/items/iron_saw.png"));
	public static final ItemSaw GOLD_SAW = new ItemSaw("gold", 30, new ItemStack(Items.GOLD_INGOT), new ResourceLocation("hcmb", "textures/items/gold_saw.png"));
	public static final ItemSaw DIAMOND_SAW = new ItemSaw("diamond", 1536, new ItemStack(Items.DIAMOND), new ResourceLocation("hcmb", "textures/items/diamond_saw.png"));
	public static final ItemSaw EMERALD_SAW = new ItemSaw("emerald", 3072, new ItemStack(Items.EMERALD), new ResourceLocation("hcmb", "textures/items/emerald_saw.png"));
	public static final ItemSaw BEDROCK_SAW = new ItemSaw("bedrock", 0, ItemStack.EMPTY, new ResourceLocation("hcmb", "textures/items/bedrock_saw.png"));

	public static final ItemMicroblock MICROBLOCK = new ItemMicroblock();
}