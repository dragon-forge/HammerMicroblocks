package org.zeith.multipart.microblocks.init;

import net.minecraft.world.item.*;
import org.zeith.hammerlib.annotations.*;
import org.zeith.multipart.microblocks.items.*;

@SimplyRegister
public interface ItemsHM
{
	@RegistryName("iron_saw")
	ItemSaw IRON_SAW = new ItemSaw(Tiers.IRON, 2, 0F, new Item.Properties());
	
	@RegistryName("diamond_saw")
	ItemSaw DIAMOND_SAW = new ItemSaw(Tiers.DIAMOND, 3, 0F, new Item.Properties());
	
	@RegistryName("netherite_saw")
	ItemSaw NETHERITE_SAW = new ItemSaw(Tiers.DIAMOND, 4, 0F, new Item.Properties());
	
	@RegistryName("microblock")
	ItemMicroblock MICROBLOCK = new ItemMicroblock(new Item.Properties());
}