package org.zeith.multipart.microblocks.api.recipe.combination;

import net.minecraft.core.NonNullList;
import org.zeith.hammerlib.core.adapter.recipe.RecipeShape;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.recipe.*;

import java.util.*;
import java.util.stream.Collectors;

public record ShapedMicroblockRecipe(
		int width,
		int height,
		NonNullList<MicroblockIngredient> inputs,
		MicroblockType output,
		boolean outputIsFullBlock,
		int count
)
		implements IMicroblockComboRecipe
{
	public static ShapedMicroblockRecipe newRecipe(MicroblockType output, boolean outputIsFullBlock, int count, RecipeShape shape, Tuple2<Character, MicroblockIngredient>... mappings)
	{
		return new ShapedMicroblockRecipe(
				shape.width, shape.height,
				createPattern(shape, mappings),
				output, outputIsFullBlock, count
		);
	}
	
	public static NonNullList<MicroblockIngredient> createPattern(RecipeShape shape, Tuple2<Character, MicroblockIngredient>... mappings)
	{
		var dictionary = Arrays.stream(mappings).collect(Collectors.toMap(Tuple2::a, Tuple2::b));
		
		StringBuilder s = new StringBuilder();
		for(String s2 : shape.shape) s.append(s2);
		
		NonNullList<MicroblockIngredient> grid = NonNullList.withSize(
				shape.width * shape.height,
				MicroblockIngredient.EMPTY
		);
		
		for(int l = 0; l < shape.width * shape.height; ++l)
		{
			char c0 = s.charAt(l);
			if(dictionary.containsKey(c0)) grid.set(l, dictionary.get(c0));
		}
		
		return grid;
	}
	
	@Override
	public Optional<FusionRecipeResult> matchAndGetResult(List<MicroblockedStack> provided, int width, int height)
	{
		if(matches(provided, width, height))
			return Optional.of(getBaseResult());
		return Optional.empty();
	}
	
	@Override
	public FusionRecipeResult getBaseResult()
	{
		return new FusionRecipeResult(output, outputIsFullBlock, count);
	}
	
	public boolean matches(List<MicroblockedStack> provided, int providedWidth, int providedHeight)
	{
		if(provided.size() < width * height) return false;
		for(int x = 0; x <= providedWidth - this.width; ++x)
		{
			for(int y = 0; y <= providedHeight - this.height; ++y)
			{
				if(this.matches(provided, providedWidth, providedHeight, x, y, true))
				{
					return true;
				}
				
				if(this.matches(provided, providedWidth, providedHeight, x, y, false))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean matches(List<MicroblockedStack> provided, int providedWidth, int providedHeight, int xOff, int yOff, boolean mirror)
	{
		for(int i = 0; i < providedWidth; ++i)
		{
			for(int j = 0; j < providedHeight; ++j)
			{
				int k = i - xOff;
				int l = j - yOff;
				
				MicroblockIngredient ingredient = null;
				
				if(k >= 0 && l >= 0 && k < this.width && l < this.height)
				{
					if(mirror)
					{
						ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
					} else
					{
						ingredient = this.inputs.get(k + l * this.width);
					}
				}
				
				if(ingredient == MicroblockIngredient.EMPTY)
					ingredient = null;
				
				if(ingredient != null && !ingredient.test(provided.get(i + j * providedWidth)))
				{
					return false;
				}
			}
		}
		
		return true;
	}
}