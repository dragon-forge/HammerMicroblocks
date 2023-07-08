package org.zeith.multipart.microblocks.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import org.zeith.hammerlib.annotations.*;
import org.zeith.multipart.microblocks.contents.recipes.*;

@SimplyRegister
public interface RecipeSerializersHM
{
	@RegistryName("microblock_cutting")
	RecipeSerializer<?> MICROBLOCK_CUTTING_SERIALIZER = new RecipeCutMicroblock.SimpleSerializer();
	
	@RegistryName("microblock_fusion")
	RecipeSerializer<?> MICROBLOCK_FUSION_SERIALIZER = new RecipeFuseMicroblock.SimpleSerializer();
}