package org.zeith.multipart.microblocks.api.recipe;

import java.util.List;

public record MicroblockInput(
		int relativeX, int relativeY,
		MicroblockIngredient input
)
{
	public boolean test(MicroblockedStack stack)
	{
		return stack != null
				&& stack.relativeX() == relativeX()
				&& stack.relativeY() == relativeY()
				&& input.test(stack);
	}
	
	public static boolean matches(List<MicroblockInput> inputs, List<MicroblockedStack> provided)
	{
		if(inputs.size() != provided.size()) return false;
		for(var request : inputs)
			if(provided.stream().noneMatch(request::test))
				return false;
		return true;
	}
}