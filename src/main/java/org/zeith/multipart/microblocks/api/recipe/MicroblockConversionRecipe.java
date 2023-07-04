package org.zeith.multipart.microblocks.api.recipe;

import org.zeith.multipart.microblocks.api.MicroblockType;

import java.util.List;

public record MicroblockConversionRecipe(
		List<MicroblockInput> inputs,
		MicroblockType output,
		int count
)
{
	public boolean matches(List<MicroblockedStack> provided)
	{
		return MicroblockInput.matches(inputs, provided);
	}
}