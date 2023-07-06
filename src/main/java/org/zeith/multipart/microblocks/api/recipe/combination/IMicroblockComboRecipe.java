package org.zeith.multipart.microblocks.api.recipe.combination;

import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.recipe.MicroblockedStack;

import java.util.*;

public interface IMicroblockComboRecipe
{
	Optional<FusionRecipeResult> matchAndGetResult(List<MicroblockedStack> provided, int width, int height);
	
	FusionRecipeResult getBaseResult();
	
	record FusionRecipeResult(MicroblockType type, boolean outputIsFullBlock, int count) {}
}