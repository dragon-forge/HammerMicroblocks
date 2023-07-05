package org.zeith.multipart.microblocks.api.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.init.ItemsHM;

import java.util.Objects;

public class MicroblockState
		implements INBTSerializable<CompoundTag>
{
	protected MicroblockType type;
	protected Item material = Items.AIR;
	
	public MicroblockState()
	{
	}
	
	public boolean isValid()
	{
		return type != null && material != Items.AIR;
	}
	
	public MicroblockType getType()
	{
		return type;
	}
	
	public MicroblockState setType(MicroblockType type)
	{
		this.type = type;
		return this;
	}
	
	public MicroblockState setMaterial(ItemStack material)
	{
		this.material = material.getItem();
		return this;
	}
	
	public void copyFrom(MicroblockState state)
	{
		this.type = state.type;
		this.material = state.material;
	}
	
	public ItemStack asStack()
	{
		if(type == null || material == null || material == Items.AIR) return ItemStack.EMPTY;
		return ItemsHM.MICROBLOCK.forItemRaw(type, material.getDefaultInstance(), 1);
	}
	
	public BlockState asBlockState()
	{
		return Block.byItem(material).defaultBlockState();
	}
	
	@Override
	public CompoundTag serializeNBT()
	{
		var tag = new CompoundTag();
		tag.putString("Type", Objects.toString(HammerMicroblocks.microblockTypes().getKey(type)));
		tag.putString("Id", Objects.toString(ForgeRegistries.ITEMS.getKey(material)));
		return tag;
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag)
	{
		this.type = HammerMicroblocks.microblockTypes().getValue(ResourceLocation.tryParse(tag.getString("Type")));
		this.material = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(tag.getString("Id")));
	}
}