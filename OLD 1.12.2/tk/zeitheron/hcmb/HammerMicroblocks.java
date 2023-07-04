package tk.zeitheron.hcmb;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.internal.SimpleRegistration;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.zeitheron.hcmb.init.ItemsHM;
import tk.zeitheron.hcmb.items.ItemMicroblock;
import tk.zeitheron.hcmb.proxy.CommonProxy;

@Mod(modid = "hcmb", name = "Hammer Microblocks", version = "@VERSION", dependencies = "required-after:hammercore", certificateFingerprint = "9f5e2a811a8332a842b34f6967b7db0ac4f24856", updateJSON = "http://dccg.herokuapp.com/api/fmluc/267263")
public class HammerMicroblocks
{
	public static final Logger LOG = LogManager.getLogger("HammerMicroblocks");

	@SidedProxy(serverSide = "tk.zeitheron.hcmb.proxy.CommonProxy", clientSide = "tk.zeitheron.hcmb.proxy.ClientProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs TAB = new CreativeTabs("hcmb")
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ItemsHM.BEDROCK_SAW);
		}
	};

	public static final CreativeTabs TAB_MICROBLOCKS = new CreativeTabs("hcmb.microblocks")
	{
		@Override
		public ItemStack createIcon()
		{
			return ItemMicroblock.makeStack(Blocks.PLANKS.getStateFromMeta(0), ItemMicroblock.EnumMicroblockType.PANEL, 1);
		}
	};

	@Mod.EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		LOG.warn("*****************************");
		LOG.warn("WARNING: Somebody has been tampering with HammerCore jar!");
		LOG.warn("It is highly recommended that you redownload mod from https://www.curseforge.com/projects/267263 !");
		LOG.warn("*****************************");
		HammerCore.invalidCertificates.put("hcmb", "https://www.curseforge.com/projects/267263");
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		SimpleRegistration.registerFieldItemsFrom(ItemsHM.class, "hcmb", TAB);
		OreDictionary.registerOre("gearStone", ItemsHM.STONE_HANDLE);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init();
	}
}