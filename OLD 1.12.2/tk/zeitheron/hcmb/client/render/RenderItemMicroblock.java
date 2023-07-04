package tk.zeitheron.hcmb.client.render;

import com.zeitheron.hammercore.client.render.item.IItemRender;
import com.zeitheron.hammercore.client.utils.RenderBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tk.zeitheron.hcmb.items.ItemMicroblock;

import java.util.List;

public class RenderItemMicroblock
		implements IItemRender
{
	@Override
	public void renderItem(ItemStack stack)
	{
		IBlockState state = null;
		ItemMicroblock.EnumMicroblockType type = ItemMicroblock.getMicroblockType(stack);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Block") && type != null)
		{
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTagCompound().getString("Block")));
			int meta = stack.getTagCompound().getInteger("Meta");
			state = b.getStateFromMeta(meta);
		}
		if(state != null)
		{
			IBakedModel mod = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
			RenderBlocks rb = RenderBlocks.forMod((String) "hammermicroblocks");
			GlStateManager.enableBlend();
			GlStateManager.disableLighting();
			Tessellator tess = Tessellator.getInstance();
			int bright = 15728640;
			try
			{
				bright = rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
			} catch(Throwable throwable)
			{
				// empty catch block
			}
			int j = bright % 65536;
			int k = bright / 65536;
			OpenGlHelper.setLightmapTextureCoords((int) OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			tess.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			rb.renderFromInside = false;
			rb.setRenderBounds(0.0, 0.0, (double) (1.0f - type.thickness), 1.0, 1.0, 1.0);
			for(EnumFacing facing : EnumFacing.VALUES)
			{
				List quads = mod.getQuads(state, facing, 1L);
				if(quads.size() != 1) continue;
				TextureAtlasSprite sprite = ((BakedQuad) quads.get(0)).getSprite();
				rb.renderFace(facing, 0.0, 0.0, 0.0, sprite, 1.0f, 1.0f, 1.0f, bright);
			}
			tess.draw();
			GlStateManager.disableBlend();
		}
	}
}

