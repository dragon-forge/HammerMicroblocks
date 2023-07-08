package org.zeith.multipart.microblocks.init;

import net.minecraft.world.item.*;
import org.zeith.hammerlib.annotations.*;
import org.zeith.multipart.microblocks.contents.items.*;

@SimplyRegister
public interface ItemsHM
{
	@RegistryName("gold_saw")
	ItemSaw GOLD_SAW = new ItemSaw(0, Tiers.GOLD, 1, 0F, new Item.Properties().durability(512));
	
	@RegistryName("granite_saw")
	ItemSaw GRANITE_SAW = new ItemSaw(1, Tiers.STONE, 1, 0F, new Item.Properties());
	
	@RegistryName("iron_saw")
	ItemSaw IRON_SAW = new ItemSaw(2, Tiers.IRON, 2, 0F, new Item.Properties());
	
	@RegistryName("diamond_saw")
	ItemSaw DIAMOND_SAW = new ItemSaw(3, Tiers.DIAMOND, 3, 0F, new Item.Properties());
	
	@RegistryName("netherite_saw")
	ItemSaw NETHERITE_SAW = new ItemSaw(4, Tiers.NETHERITE, 4, 0F, new Item.Properties());
	
	@RegistryName("microblock")
	ItemMicroblock MICROBLOCK = new ItemMicroblock(new Item.Properties());
}