package org.zeith.multipart.microblocks.items;

import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.*;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;
import org.zeith.multipart.api.item.IMultipartPlacerItem;
import org.zeith.multipart.api.placement.PlacedPartConfiguration;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.init.TagsHM;

import java.util.*;
import java.util.stream.Collectors;

public class ItemMicroblock
		extends Item
		implements IMultipartPlacerItem, ITabItem
{
	public ItemMicroblock(Item.Properties props)
	{
		super(props);
		
		HammerLib.EVENT_BUS.addListener(this::applyTags);
	}
	
	private void applyTags(BuildTagsEvent e)
	{
		if(e.reg.getRegistryKey() == ForgeRegistries.Keys.BLOCKS)
		{
			var ae2Facades = new ResourceLocation("ae2", "whitelisted/facades");
			
			e.addAllToTag(TagsHM.Blocks.MICROBLOCK_WHITELIST,
					ForgeRegistries.BLOCKS.getValues()
							.stream()
							.filter(this::allowBlockAsFacade)
							.toList()
			);
			
			var forced = e.tags.computeIfAbsent(TagsHM.Blocks.MICROBLOCK_WHITELIST.location(), l -> new ArrayList<>());
			var vals = e.tags.get(ae2Facades);
			if(vals != null && !vals.isEmpty())
			{
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
	
	public String getSubtypeFromStack(ItemStack stack)
	{
		var mcb = stack.getTagElement("Microblock");
		if(mcb != null)
		{
			var type = ResourceLocation.tryParse(mcb.getString("Type"));
			var itemId = ResourceLocation.tryParse(mcb.getString("Item"));
			return type + ";" + itemId;
		}
		return "null";
	}
	
	@Nullable
	public MicroblockType getMicroblockType(ItemStack stack)
	{
		var mcb = stack.getTagElement("Microblock");
		if(mcb != null)
			return HammerMicroblocks.microblockTypes()
					.getValue(ResourceLocation.tryParse(mcb.getString("Type")));
		return null;
	}
	
	@NotNull
	public ItemStack getFacadeBlockStack(ItemStack stack)
	{
		var nbt = stack.getTagElement("Microblock");
		if(nbt == null) return ItemStack.EMPTY;
		var itemId = ResourceLocation.tryParse(nbt.getString("Item"));
		return new ItemStack(ForgeRegistries.ITEMS.getValue(itemId));
	}
	
	@Nullable
	public BlockState getFacadeBlockState(ItemStack is)
	{
		var baseItemStack = getFacadeBlockStack(is);
		if(baseItemStack.isEmpty()) return null;
		var block = Block.byItem(baseItemStack.getItem());
		if(block == Blocks.AIR) return null;
		return block.defaultBlockState();
	}
	
	@NotNull
	public BlockState getFacadeBlockStateForRendering(ItemStack is)
	{
		var st = getFacadeBlockState(is);
		if(st == null) st = Blocks.STONE.defaultBlockState();
		return st;
	}
	
	public ItemStack forItem(MicroblockType type, ItemStack itemStack, boolean returnItem)
	{
		return forItem(type, itemStack, 1, returnItem);
	}
	
	public ItemStack forItem(MicroblockType type, ItemStack itemStack, int size, boolean returnItem)
	{
		Block block;
		if(itemStack.isEmpty() || itemStack.hasTag() || (block = Block.byItem(itemStack.getItem())) == Blocks.AIR)
			return ItemStack.EMPTY;
		
		// We only support the default state for microblocks. Sorry.
		var blockState = block.defaultBlockState();
		var forcedByTag = blockState.is(TagsHM.Blocks.MICROBLOCK_WHITELIST);
		var isModel = blockState.getRenderShape() == RenderShape.MODEL;
		var isBlockEntity = blockState.hasBlockEntity();
		var isFullCube = blockState.isRedstoneConductor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
		
		if(isModel && (!isBlockEntity || forcedByTag) && (isFullCube || forcedByTag))
		{
			if(returnItem) return itemStack;
			return forItemRaw(type, itemStack, size);
		}
		
		return ItemStack.EMPTY;
	}
	
	public ItemStack forItemRaw(MicroblockType type, ItemStack itemStack, int size)
	{
		var is = new ItemStack(this, size);
		var tag = is.getOrCreateTagElement("Microblock");
		tag.putString("Item", Objects.toString(ForgeRegistries.ITEMS.getKey(itemStack.getItem())));
		tag.putString("Type", Objects.toString(HammerMicroblocks.microblockTypes().getKey(type)));
		return is;
	}
	
	protected boolean allowBlockAsFacade(Block block)
	{
		if(block instanceof AbstractGlassBlock)
			return true;
		
		if(block instanceof LeavesBlock)
			return true;
		
		return false;
	}
	
	@Override
	public Component getName(ItemStack is)
	{
		try
		{
			var in = this.getFacadeBlockStack(is);
			var type = this.getMicroblockType(is);
			
			if(!in.isEmpty() && type != null)
				return Component.translatable(
						getDescriptionId() + "_formatted", type.getDescription(), in.getHoverName());
		} catch(Throwable ignored)
		{
		}
		return super.getName(is);
	}
	
	@Override
	public Optional<PlacedPartConfiguration> getPlacement(Level level, BlockPos pos, Player player, ItemStack stack, BlockHitResult hit)
	{
		return Optional.empty();
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items)
	{
		if(!allowedIn(tab)) return;
		
		for(var b : ForgeRegistries.BLOCKS)
			try
			{
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