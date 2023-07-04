package org.zeith.multipart.microblocks.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.multipart.microblocks.HammerMicroblocks;

public class TagsHM
{
	@Setup
	public static void init()
	{
		TagsHM.Items.init();
		TagsHM.Blocks.init();
	}
	
	public static class Blocks
	{
		private static void init()
		{
		}
		
		public static final TagKey<Block> MINEABLE_WITH_SAW = create("mineable/saw");
		public static final TagKey<Block> MICROBLOCK_WHITELIST = modTag("microblock_whitelist");
		
		private static TagKey<Block> create(String name)
		{
			return BlockTags.create(new ResourceLocation(name));
		}
		
		private static TagKey<Block> modTag(String name)
		{
			return BlockTags.create(HammerMicroblocks.id(name));
		}
	}
	
	public static class Items
	{
		private static void init()
		{
		}
		
		public static final TagKey<Item> TOOLS_SAW = tag("tools/saw");
		
		private static TagKey<Item> tag(String name)
		{
			return ItemTags.create(new ResourceLocation("forge", name));
		}
		
	}
}