package org.zeith.multipart.microblocks.contents.items;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.multipart.api.item.IMultipartPlacerItem;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.api.placement.PlacedPartConfiguration;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.data.MicroblockComponent;
import org.zeith.multipart.microblocks.api.tile.MicroblockState;
import org.zeith.multipart.microblocks.contents.multipart.MicroblockPartDefinition;
import org.zeith.multipart.microblocks.init.PartDefinitionsHM;
import org.zeith.multipart.microblocks.init.TagsHM;

import java.util.*;
import java.util.stream.Collectors;

public class ItemMicroblock
		extends Item
		implements IMultipartPlacerItem, ITabItem
{
	public static final List<Block> LIMITED_TAB_SELECTION = Util.make(new ArrayList<>(), lst ->
	{
		lst.add(Blocks.STONE);
		lst.add(Blocks.DIRT);
		lst.add(Blocks.GRASS_BLOCK);
		
		lst.add(Blocks.OAK_PLANKS);
		lst.add(Blocks.SPRUCE_PLANKS);
		lst.add(Blocks.BIRCH_PLANKS);
		lst.add(Blocks.JUNGLE_PLANKS);
		lst.add(Blocks.ACACIA_PLANKS);
		lst.add(Blocks.DARK_OAK_PLANKS);
		lst.add(Blocks.MANGROVE_PLANKS);
		lst.add(Blocks.CHERRY_PLANKS);
		lst.add(Blocks.BAMBOO_PLANKS);
		lst.add(Blocks.CRIMSON_PLANKS);
		lst.add(Blocks.WARPED_PLANKS);
		
		lst.add(Blocks.IRON_BLOCK);
		lst.add(Blocks.GOLD_BLOCK);
		lst.add(Blocks.DIAMOND_BLOCK);
		lst.add(Blocks.EMERALD_BLOCK);
		lst.add(Blocks.LAPIS_BLOCK);
		
		lst.add(Blocks.WHITE_WOOL);
		lst.add(Blocks.ORANGE_WOOL);
		lst.add(Blocks.MAGENTA_WOOL);
		lst.add(Blocks.LIGHT_BLUE_WOOL);
		lst.add(Blocks.YELLOW_WOOL);
		lst.add(Blocks.LIME_WOOL);
		lst.add(Blocks.PINK_WOOL);
		lst.add(Blocks.GRAY_WOOL);
		lst.add(Blocks.LIGHT_GRAY_WOOL);
		lst.add(Blocks.CYAN_WOOL);
		lst.add(Blocks.PURPLE_WOOL);
		lst.add(Blocks.BLUE_WOOL);
		lst.add(Blocks.BROWN_WOOL);
		lst.add(Blocks.GREEN_WOOL);
		lst.add(Blocks.RED_WOOL);
		lst.add(Blocks.BLACK_WOOL);
		
		lst.add(Blocks.WHITE_CONCRETE);
		lst.add(Blocks.ORANGE_CONCRETE);
		lst.add(Blocks.MAGENTA_CONCRETE);
		lst.add(Blocks.LIGHT_BLUE_CONCRETE);
		lst.add(Blocks.YELLOW_CONCRETE);
		lst.add(Blocks.LIME_CONCRETE);
		lst.add(Blocks.PINK_CONCRETE);
		lst.add(Blocks.GRAY_CONCRETE);
		lst.add(Blocks.LIGHT_GRAY_CONCRETE);
		lst.add(Blocks.CYAN_CONCRETE);
		lst.add(Blocks.PURPLE_CONCRETE);
		lst.add(Blocks.BLUE_CONCRETE);
		lst.add(Blocks.BROWN_CONCRETE);
		lst.add(Blocks.GREEN_CONCRETE);
		lst.add(Blocks.RED_CONCRETE);
		lst.add(Blocks.BLACK_CONCRETE);
		
		lst.add(Blocks.OBSIDIAN);
		
		lst.add(Blocks.NETHERRACK);
		lst.add(Blocks.NETHER_BRICKS);
		lst.add(Blocks.CALCITE);
		lst.add(Blocks.BASALT);
		lst.add(Blocks.AMETHYST_BLOCK);
	});
	
	public ItemMicroblock(Item.Properties props)
	{
		super(props);
		
		HammerLib.EVENT_BUS.addListener(this::applyTags);
	}
	
	@Override
	public Optional<PlacedPartConfiguration> getPlacement(Level level, BlockPos pos, Player player, ItemStack stack, BlockHitResult hit)
	{
		var state = new MicroblockState();
		PartPlacement placement;
		
		var mcb = MicroblockComponent.get(stack);
		if(mcb == null) return Optional.empty();
		
		var mbt = mcb.type();
		var mat = mcb.materialStack();
		
		if(mbt == null || mat.isEmpty()) return Optional.empty();
		
		placement = mbt.getPlacementGrid().pickPlacement(player, hit, pos.equals(hit.getBlockPos()));
		var data = mbt.createDataForPlacement(player, hit, pos.equals(hit.getBlockPos()));
		state.setType(mbt, data)
				.setMaterial(mat);
		
		if(placement != null && state.isValid())
			return Optional.of(new PlacedPartConfiguration(PartDefinitionsHM.MICROBLOCK, new MicroblockPartDefinition.MicroblockConfiguration(state), placement));
		return Optional.empty();
	}
	
	private void applyTags(BuildTagsEvent e)
	{
		if(e.reg.key() == Registries.BLOCK)
		{
			var ae2Facades = Resources.location("ae2", "whitelisted/facades");
			
			var entries = e.tags.getOrDefault(TagsHM.Blocks.MICROBLOCK_BLOCKLIST.location(), List.of())
					.stream()
					.map(TagLoader.EntryWithSource::entry)
					.filter(t -> !t.isTag())
					.map(TagEntry::getId)
					.toList();
			
			var forced = e.tags.computeIfAbsent(TagsHM.Blocks.MICROBLOCK_ALLOWLIST.location(), l -> new ArrayList<>());
			var vals = e.tags.get(ae2Facades);
			if(vals != null && !vals.isEmpty())
			{
				for(TagLoader.EntryWithSource val : vals)
					if(val.entry().isTag() || !entries.contains(val.entry().getId()))
						forced.addAll(vals);
				
				HammerMicroblocks.LOG.info("Added " + e.tags.get(ae2Facades).size() +
										   " AE2 facade whitelisted blocks to our own microblock whitelist.");
			}
			
			HammerMicroblocks.LOG.info(
					"Currently, we have " + forced.size() + " force-white-listed blocks to be slice-able: " +
					forced.stream()
							.map(e0 -> e0.entry().toString())
							.collect(Collectors.joining(", ", "[", "]"))
			);
		}
	}
	
	public ItemStack forItem(MicroblockType type, ItemStack itemStack, boolean returnItem)
	{
		return forItem(type, itemStack, 1, returnItem);
	}
	
	public ItemStack forItem(MicroblockType type, ItemStack itemStack, int size, boolean returnItem)
	{
		Block block;
		if(itemStack.isEmpty() || (block = Block.byItem(itemStack.getItem())) == Blocks.AIR)
			return ItemStack.EMPTY;
		
		// We only support the default state for microblocks. Sorry.
		var blockState = block.defaultBlockState();
		
		if(allowStateAsFacade(blockState, true))
		{
			if(returnItem) return itemStack;
			return forItemRaw(type, itemStack, size);
		}
		
		return ItemStack.EMPTY;
	}
	
	public ItemStack forItemRaw(MicroblockType type, ItemStack material, int size)
	{
		var is = new ItemStack(this, size);
		is.set(MicroblockComponent.TYPE, new MicroblockComponent(type, material.getItem().builtInRegistryHolder()));
		return is;
	}
	
	private boolean allowBlockAsFacade(Block block)
	{
		if(block instanceof TransparentBlock)
			return true;
		
		if(block instanceof LeavesBlock)
			return true;
		
		return false;
	}
	
	public boolean allowStateAsFacade(BlockState blockState, boolean checkBlocklist)
	{
		if(checkBlocklist && blockState.is(TagsHM.Blocks.MICROBLOCK_BLOCKLIST))
			return false;
		
		var forcedByTag = blockState.is(TagsHM.Blocks.MICROBLOCK_ALLOWLIST);
		var isModel = blockState.getRenderShape() == RenderShape.MODEL;
		var isBlockEntity = blockState.hasBlockEntity();
		var isFullCube = blockState.isRedstoneConductor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
		
		if(allowBlockAsFacade(blockState.getBlock()))
			return true;
		
		return isModel && (!isBlockEntity || forcedByTag) && (isFullCube || forcedByTag);
	}
	
	@Override
	public Component getName(ItemStack is)
	{
		try
		{
			var mcb = MicroblockComponent.get(is);
			if(mcb != null)
				return Component.translatable(getDescriptionId() + "_formatted", mcb.type().getDescription(), mcb.materialStack().getHoverName());
		} catch(Throwable ignored)
		{
		}
		return super.getName(is);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab tab, Set<ItemStack> items)
	{
		if(!allowedIn(tab)) return;
		
		for(var b : LIMITED_TAB_SELECTION)
			try
			{
				if(!allowStateAsFacade(b.defaultBlockState(), true))
					continue;
				var item = b.asItem();
				if(item != Items.AIR)
				{
					for(MicroblockType type : HammerMicroblocks.microblockTypes())
					{
						var facade = forItem(type, item.getDefaultInstance(), false);
						if(!facade.isEmpty()) items.add(facade);
					}
				}
			} catch(Throwable ignored)
			{
			}
	}
	
	@Override
	public CreativeModeTab getItemCategory()
	{
		return HammerMicroblocks.MICROBLOCKS_TAB.tab();
	}
}