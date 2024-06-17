package org.zeith.multipart.microblocks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.CommonMessages;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.init.ItemsHM;

@Mod(HammerMicroblocks.MOD_ID)
public class HammerMicroblocks
{
	public static final Logger LOG = LoggerFactory.getLogger(HammerMicroblocks.class);
	public static final String MOD_ID = "hammermicroblocks";
	
	private static Registry<MicroblockType> MICROBLOCK_TYPE_REGISTRY;
	public static final ResourceKey<? extends Registry<MicroblockType>> MICROBLOCK_TYPE = ResourceKey.createRegistryKey(id("mcb_types"));
	
	@CreativeTab.RegisterTab
	public static final CreativeTab MICROBLOCKS_TAB = new CreativeTab(id("parts"),
			builder -> builder
					.icon(() -> new ItemStack(ItemsHM.DIAMOND_SAW))
					.withTabsBefore(HLConstants.HL_TAB.id())
					.displayItems((parameters, output) ->
					{
					
					})
	);
	
	public HammerMicroblocks(IEventBus modBus)
	{
		CommonMessages.printMessageOnIllegalRedistribution(HammerMicroblocks.class,
				LogManager.getLogger(HammerMicroblocks.class),
				"HammerMicroblocks",
				"https://www.curseforge.com/minecraft/mc-mods/hammer-microblocks"
		);
		
		LanguageAdapter.registerMod(MOD_ID);
		
		modBus.addListener(this::newRegistries);
	}
	
	private void newRegistries(NewRegistryEvent e)
	{
		RegistryMapping.report(MicroblockType.class, MICROBLOCK_TYPE_REGISTRY = e.create(new RegistryBuilder<>(MICROBLOCK_TYPE).sync(false)), false);
	}
	
	public static ResourceLocation id(String path)
	{
		return Resources.location(MOD_ID, path);
	}
	
	public static Registry<MicroblockType> microblockTypes()
	{
		return MICROBLOCK_TYPE_REGISTRY;
	}
}