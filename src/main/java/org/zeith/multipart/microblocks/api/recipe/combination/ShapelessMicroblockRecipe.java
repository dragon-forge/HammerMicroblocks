package org.zeith.multipart.microblocks.api.recipe.combination;

import net.minecraft.core.NonNullList;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.recipe.*;

import java.util.*;

public record ShapelessMicroblockRecipe(
		NonNullList<MicroblockIngredient> inputs,
		MicroblockType output,
		boolean outputIsFullBlock,
		int count
)
		implements IMicroblockComboRecipe
{
	@Override
	public Optional<FusionRecipeResult> matchAndGetResult(List<MicroblockedStack> provided, int width, int height)
	{
		if(matches(provided))
			return Optional.of(getBaseResult());
		return Optional.empty();
	}
	
	@Override
	public FusionRecipeResult getBaseResult()
	{
		return new FusionRecipeResult(output, outputIsFullBlock, count);
	}
	
	public boolean matches(List<MicroblockedStack> provided)
	{
		provided = new ArrayList<>(provided);
		provided.removeIf(Objects::isNull);
		
		if(provided.size() != inputs.size()) return false;
		boolean[] found = new boolean[inputs.size()];
		
		for(MicroblockedStack microblockedStack : provided)
		{
			boolean match = false;
			
			for(int i = 0; i < inputs.size(); i++)
			{
				if(found[i]) continue;
				
				var in = inputs.get(i);
				if(in.test(microblockedStack))
				{
					found[i] = true;
					match = true;
					break;
				}
			}
			
			if(!match)
				return false;
		}
		
		for(boolean b : found) if(!b) return false;
		return true;
	}
}