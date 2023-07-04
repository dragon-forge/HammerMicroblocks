package tk.zeitheron.hcmb.client.render;

import com.zeitheron.hammercore.client.render.item.IItemRender;
import com.zeitheron.hammercore.client.utils.UtilsFX;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import tk.zeitheron.hcmb.client.models.ModelSaw;
import tk.zeitheron.hcmb.items.ItemSaw;

public class RenderItemSaw
		implements IItemRender
{
	public ModelSaw model = new ModelSaw();

	@Override
	public void renderItem(ItemStack item)
	{
		GlStateManager.pushMatrix();
		ItemSaw saw = Cast.cast(item.getItem(), ItemSaw.class);
		if(saw != null)
		{
			UtilsFX.bindTexture(saw.modelTexture);
			GlStateManager.translate(0.25F, 1F, 0.5F);
			GlStateManager.rotate(135, 0, 1, 0);
			GlStateManager.rotate(200, 0, 0, 1);
			GlStateManager.scale(1.5F, 1.5F, 1.5F);
			this.model.render(null, 0, 0, -0.1F, 0, 0, 1 / 16F);
		}
		GlStateManager.popMatrix();
	}
}