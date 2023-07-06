package org.zeith.multipart.microblocks.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.core.adapter.TagAdapter;
import org.zeith.multipart.microblocks.HammerMicroblocks;

public class TagsHM
{
	@Setup
	public static void init()
	{
		TagsHM.Items.init();
		TagsHM.Blocks.init();
		
		TagAdapter.bind(Blocks.MICROBLOCK_BLOCKLIST, net.minecraft.world.level.block.Blocks.REDSTONE_LAMP);
	}
	
	public static class Blocks
	{
		private static void init()
		{
		}
		
		public static final TagKey<Block> MINEABLE_WITH_SAW = create("mineable/saw");
		public static final TagKey<Block> MICROBLOCK_ALLOWLIST = modTag("microblock_allowlist");
		public static final TagKey<Block> MICROBLOCK_BLOCKLIST = modTag("microblock_blocklist");
		
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