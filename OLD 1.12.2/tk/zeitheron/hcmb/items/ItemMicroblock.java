package tk.zeitheron.hcmb.items;

import com.zeitheron.hammercore.api.multipart.IMultipartProvider;
import com.zeitheron.hammercore.api.multipart.ItemBlockMultipartProvider;
import com.zeitheron.hammercore.api.multipart.MultipartSignature;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tk.zeitheron.hcmb.HammerMicroblocks;
import tk.zeitheron.hcmb.init.ItemsHM;
import tk.zeitheron.hcmb.multipart.MultipartMicroblock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ItemMicroblock
		extends ItemBlockMultipartProvider
		implements IMultipartProvider
{
	public ItemMicroblock()
	{
		setTranslationKey("microblock");
	}

	@Override
	public Item setCreativeTab(CreativeTabs tab)
	{
		return super.setCreativeTab(HammerMicroblocks.TAB_MICROBLOCKS);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(tab != HammerMicroblocks.TAB_MICROBLOCKS) return;
		ArrayList<ItemStack> stacksMicro = new ArrayList<>();
		ForgeRegistries.ITEMS.getValuesCollection().forEach(item ->
		{
			Block b = Block.getBlockFromItem(item);
			if(b == null || b.hasTileEntity()) return;
			b.getBlockState().getValidStates().forEach(state ->
			{
				if(!state.isFullBlock() || !state.isFullCube()) return;
				for(ItemMicroblock.EnumMicroblockType type : ItemMicroblock.EnumMicroblockType.values())
					stacksMicro.add(makeStack(state, type, 1));
			});
		});
		stacksMicro.sort(Comparator.comparingInt(stack -> Block.getIdFromBlock(getState(stack).getBlock())));
		items.addAll(stacksMicro);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		EnumMicroblockType type = ItemMicroblock.getMicroblockType(stack);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Block") && type != null)
		{
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTagCompound().getString("Block")));
			if(b == null)
				return "Unnamed";
			int meta = stack.getTagCompound().getInteger("Meta");
			Item i = Item.getItemFromBlock((Block) b);
			if(i != null)
				return super.getItemStackDisplayName(stack).replaceAll("&material", i.getItemStackDisplayName(new ItemStack(b, 1, meta))).replaceAll("&type", I18n.translateToLocal("hcmb:type." + type.type));
		}
		return "Unnamed";
	}

	@Override
	public MultipartSignature createSignature(int signatureIndex, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		IBlockState state = null;
		EnumMicroblockType type = ItemMicroblock.getMicroblockType(stack);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Block") && type != null)
		{
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTagCompound().getString("Block")));
			int meta = stack.getTagCompound().getInteger("Meta");
			state = b.getStateFromMeta(meta);
		}
		if(state != null && type != null)
		{
			MultipartMicroblock m = new MultipartMicroblock();
			m.block = state.getBlock();
			m.meta = state.getBlock().getMetaFromState(state);
			m.orientation = side.getOpposite();
			m.type = type;
			return m;
		}
		return null;
	}

	public static EnumMicroblockType getMicroblockType(ItemStack stack)
	{
		return stack.hasTagCompound() ? EnumMicroblockType.typeOrNull(stack.getTagCompound().getString("Type")) : null;
	}

	public static ItemStack makeStack(IBlockState state, EnumMicroblockType type, int count)
	{
		ItemStack stack = new ItemStack(ItemsHM.MICROBLOCK, count);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("Block", state.getBlock().getRegistryName().toString());
		stack.getTagCompound().setInteger("Meta", state.getBlock().getMetaFromState(state));
		stack.getTagCompound().setString("Type", type.type);
		return stack;
	}

	public static ItemStack setMicroblockType(ItemStack stack, EnumMicroblockType type)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setString("Type", type.type);
		return stack;
	}

	public static IBlockState getState(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null && nbt.hasKey("Block", 8) && nbt.hasKey("Meta", 3))
			return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("Block"))).getStateFromMeta(nbt.getInteger("Meta"));
		return Blocks.AIR.getDefaultState();
	}

	public enum EnumMicroblockType
	{
		COVER(0.125f),
		ANTICOVER(0.25f),
		PANEL(0.375f),
		SLAB(0.5f),
		ANTIPANEL(0.625f),
		ANTISLAB(0.75f);

		public final String type = this.name().toLowerCase();
		public final float thickness;
		private static final Map<String, EnumMicroblockType> types;

		private EnumMicroblockType(float thickness)
		{
			this.thickness = thickness;
		}

		public static EnumMicroblockType cut(EnumMicroblockType type)
		{
			return EnumMicroblockType.byThickness(type.thickness / 2.0f);
		}

		public static EnumMicroblockType byThickness(float thick)
		{
			for(EnumMicroblockType type : EnumMicroblockType.values())
			{
				if(type.thickness != thick) continue;
				return type;
			}
			return null;
		}

		public static EnumMicroblockType typeOrNull(String type)
		{
			return types.get(type);
		}

		static
		{
			types = new HashMap<String, EnumMicroblockType>();
			for(EnumMicroblockType type : EnumMicroblockType.values())
			{
				types.put(type.type, type);
			}
		}
	}
}