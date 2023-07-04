package org.zeith.multipart.microblocks.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.init.ItemsHM;

@JeiPlugin
public class JEIHammerMicroblocks
		implements IModPlugin
{
	public static final ResourceLocation PLUGIN_ID = HammerMicroblocks.id("jei");
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		registration.registerSubtypeInterpreter(ItemsHM.MICROBLOCK, (ingredient, context) ->
				ItemsHM.MICROBLOCK.getSubtypeFromStack(ingredient)
		);
	}
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return PLUGIN_ID;
	}
}