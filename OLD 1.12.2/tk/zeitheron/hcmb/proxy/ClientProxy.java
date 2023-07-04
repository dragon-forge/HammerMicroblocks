package tk.zeitheron.hcmb.proxy;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.client.render.item.ItemRenderingHandler;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tk.zeitheron.hcmb.client.render.RenderItemSaw;
import tk.zeitheron.hcmb.client.render.RenderItemMicroblock;
import tk.zeitheron.hcmb.init.ItemsHM;
import tk.zeitheron.hcmb.items.ItemSaw;

public class ClientProxy
		extends CommonProxy
{
	@Override
	public void init()
	{
		RenderItemSaw renderSaw = new RenderItemSaw();
		GameRegistry.findRegistry(Item.class)
				.getValuesCollection()
				.stream()
				.filter(Predicates.instanceOf(ItemSaw.class))
				.forEach(it -> ItemRenderingHandler.INSTANCE.bindItemRender(it, renderSaw));
		ItemRenderingHandler.INSTANCE.bindItemRender(ItemsHM.MICROBLOCK, new RenderItemMicroblock());
	}
}