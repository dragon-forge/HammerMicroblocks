package org.zeith.multipart.microblocks.api.recipe;

import org.zeith.multipart.microblocks.api.MicroblockType;

import java.util.List;
import java.util.function.Predicate;

public final class MicroblockIngredient
{
	public static final MicroblockIngredient FULL_BLOCK = new MicroblockIngredient(f -> false, true);
	
	private final boolean isFullBlock;
	private final Predicate<MicroblockType> typeFilter;
	
	private MicroblockIngredient(Predicate<MicroblockType> typeFilter, boolean isFullBlock)
	{
		this.typeFilter = typeFilter;
		this.isFullBlock = isFullBlock;
	}
	
	public static MicroblockIngredient fullBlock()
	{
		return FULL_BLOCK;
	}
	
	public static MicroblockIngredient of(MicroblockType... types)
	{
		var tlst = List.of(types);
		return new MicroblockIngredient(tlst::contains, false);
	}
	
	public boolean matchesType(MicroblockType type)
	{
		return typeFilter.test(type);
	}
	
	public boolean isFullBlock()
	{
		return isFullBlock;
	}
	
	public boolean test(MicroblockedStack stack)
	{
		return (isFullBlock && stack.fullBlock()) ||
				(
						!isFullBlock
								&& !stack.fullBlock()
								&& stack.type().map(this::matchesType).orElse(false)
				);
	}
}