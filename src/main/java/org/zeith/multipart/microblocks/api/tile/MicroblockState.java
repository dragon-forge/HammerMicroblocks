package org.zeith.multipart.microblocks.api.tile;

import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.MicroblockData;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.init.ItemsHM;

import java.util.Objects;

public class MicroblockState
		implements INBTSerializable<CompoundTag>
{
	@Getter
	protected MicroblockType type;
	
	@Getter
	protected MicroblockData data;
	
	protected Item material = Items.AIR;
	
	public MicroblockState()
	{
	}
	
	public boolean isValid()
	{
		return type != null && material != Items.AIR;
	}
	
	public MicroblockState setType(MicroblockType type, MicroblockData data)
	{
		this.type = type;
		this.data = data;
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
		this.data = state.data;
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
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		var tag = new CompoundTag();
		tag.putString("Type", Objects.toString(HammerMicroblocks.microblockTypes().getKey(type)));
		if(this.data != null) tag.put("Data", this.data.serializeNBT(provider));
		tag.putString("Id", Objects.toString(material.builtInRegistryHolder().key().location()));
		return tag;
	}
	
	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag)
	{
		this.type = HammerMicroblocks.microblockTypes().get(ResourceLocation.tryParse(tag.getString("Type")));
		if(this.type != null)
		{
			this.data = this.type.createEmptyData();
			if(this.data != null) this.data.deserializeNBT(provider, tag.getCompound("Data"));
		}
		this.material = provider.lookupOrThrow(Registries.ITEM)
				.get(ResourceKey.create(Registries.ITEM, Resources.location(tag.getString("Id"))))
				.map(Holder.Reference::value)
				.orElseThrow();
	}
}