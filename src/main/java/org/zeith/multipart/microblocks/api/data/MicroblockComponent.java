package org.zeith.multipart.microblocks.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.registrars.Registrar;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.MicroblockType;

import java.util.function.Function;

@SimplyRegister
public record MicroblockComponent(MicroblockType type, Holder<Item> material)
{
	public static final Codec<MicroblockComponent> CODEC = RecordCodecBuilder.create(inst ->
			inst.group(
					Codec.lazyInitialized(() -> HammerMicroblocks.microblockTypes().byNameCodec()).fieldOf("type").forGetter(MicroblockComponent::type),
					RegistryFixedCodec.create(Registries.ITEM).fieldOf("material").forGetter(MicroblockComponent::material)
			).apply(inst, MicroblockComponent::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, MicroblockComponent> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.lazy(() -> ResourceLocation.STREAM_CODEC.map(
					HammerMicroblocks.microblockTypes()::get,
					HammerMicroblocks.microblockTypes()::getKey
			)), MicroblockComponent::type,
			ByteBufCodecs.holderRegistry(Registries.ITEM), MicroblockComponent::material,
			MicroblockComponent::new
	);
	
	@RegistryName("microblock")
	public static final Registrar<DataComponentType<MicroblockComponent>> TYPE = Registrar.dataComponentType(b -> b
			.persistent(CODEC)
			.networkSynchronized(STREAM_CODEC)
			.cacheEncoding()
	);
	
	@NotNull
	public ItemStack materialStack()
	{
		if(material.isBound())
			return new ItemStack(material.value());
		return ItemStack.EMPTY;
	}
	
	@Nullable
	public BlockState materialState()
	{
		var baseItemStack = materialStack();
		if(baseItemStack.isEmpty()) return null;
		var block = Block.byItem(baseItemStack.getItem());
		if(block == Blocks.AIR) return null;
		return block.defaultBlockState();
	}
	
	public static MicroblockComponent get(ItemStack stack)
	{
		return stack.get(TYPE);
	}
	
	public static <T> T getOrDefault(ItemStack stack, Function<MicroblockComponent, T> obtain, T defaultValue)
	{
		var mcb = get(stack);
		if(mcb != null) return obtain.apply(mcb);
		return defaultValue;
	}
}