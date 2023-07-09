package org.zeith.multipart.microblocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.slf4j.*;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.CommonMessages;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.init.ItemsHM;

import java.util.function.Supplier;

@Mod(HammerMicroblocks.MOD_ID)
public class HammerMicroblocks
{
	public static final Logger LOG = LoggerFactory.getLogger(HammerMicroblocks.class);
	public static final String MOD_ID = "hammermicroblocks";
	
	private static Supplier<IForgeRegistry<MicroblockType>> MICROBLOCK_TYPE_REGISTRY;
	
	@CreativeTab.RegisterTab
	public static final CreativeTab MICROBLOCKS_TAB = new CreativeTab(id("parts"),
			builder -> builder
					.icon(() -> new ItemStack(ItemsHM.DIAMOND_SAW))
					.withTabsBefore(HLConstants.HL_TAB.id())
					.displayItems((parameters, output) ->
					{
					
					})
	);
	
	public HammerMicroblocks()
	{
		CommonMessages.printMessageOnIllegalRedistribution(HammerMicroblocks.class,
				LogManager.getLogger(HammerMicroblocks.class),
				"HammerMicroblocks",
				"https://www.curseforge.com/minecraft/mc-mods/hammer-microblocks"
		);
		
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		LanguageAdapter.registerMod(MOD_ID);
		
		modBus.addListener(this::newRegistries);
	}
	
	private void newRegistries(NewRegistryEvent e)
	{
		MICROBLOCK_TYPE_REGISTRY = e.create(new RegistryBuilder<MicroblockType>()
						.setName(id("mcb_types"))
						.disableSaving()
						.disableSync(),
				reg -> RegistryMapping.report(MicroblockType.class, reg, false)
		);
	}
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
	
	public static IForgeRegistry<MicroblockType> microblockTypes()
	{
		return MICROBLOCK_TYPE_REGISTRY.get();
	}
}