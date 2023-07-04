package org.zeith.multipart.microblocks.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.zeith.multipart.microblocks.api.MicroblockType;

import java.util.Optional;

public record MicroblockedStack(
		int relativeX, int relativeY,
		boolean fullBlock,
		Optional<MicroblockType> type,
		@NotNull BlockState state,
		ItemStack stateAsItem
)
{
	public boolean sameState(MicroblockedStack other)
	{
		return state.equals(other.state);
	}
}