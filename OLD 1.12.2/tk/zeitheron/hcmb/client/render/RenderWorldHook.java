package tk.zeitheron.hcmb.client.render;

import com.zeitheron.hammercore.client.utils.RenderBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import tk.zeitheron.hcmb.init.ItemsHM;
import tk.zeitheron.hcmb.items.ItemMicroblock;

import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class RenderWorldHook
{
	@SubscribeEvent
	public static void renderWorldLast(RenderWorldLastEvent evt)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		float partialTicks = evt.getPartialTicks();
		RayTraceResult rtl = Minecraft.getMinecraft().objectMouseOver;
		IBlockState state = null;
		ItemMicroblock.EnumMicroblockType type = null;
		if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemsHM.MICROBLOCK)
		{
			ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
			type = ItemMicroblock.getMicroblockType(stack);
			if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Block") && type != null)
			{
				Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stack.getTagCompound().getString("Block")));
				int meta = stack.getTagCompound().getInteger("Meta");
				state = b.getStateFromMeta(meta);
			}
		}
		if(rtl != null && rtl.typeOfHit == RayTraceResult.Type.BLOCK && state != null)
		{
			BlockPos pos = rtl.getBlockPos().offset(rtl.sideHit);
			EnumFacing facing = rtl.sideHit.getOpposite();
			double o1 = type.thickness;
			double o2 = 1.0f - type.thickness;
			double minX = 0.0;
			double minY = 0.0;
			double minZ = 0.0;
			double maxX = 1.0;
			double maxY = 1.0;
			double maxZ = 1.0;
			if(facing == EnumFacing.DOWN)
			{
				maxY = o1;
			}
			if(facing == EnumFacing.UP)
			{
				minY = o2;
				maxY = 1.0;
			}
			if(facing == EnumFacing.WEST)
			{
				maxX = o1;
			}
			if(facing == EnumFacing.EAST)
			{
				minX = o2;
				maxX = 1.0;
			}
			if(facing == EnumFacing.NORTH)
			{
				maxZ = o1;
			}
			if(facing == EnumFacing.SOUTH)
			{
				minZ = o2;
				maxZ = 1.0;
			}
			double d0 = (double) pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
			double d1 = (double) pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
			double d2 = (double) pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;
			GL11.glPushMatrix();
			GlStateManager.enableBlend();
			GL11.glTranslated(d0, d1, d2);
			IBakedModel mod = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
			RenderBlocks rb = RenderBlocks.forMod("hcmb");
			GlStateManager.disableLighting();
			GlStateManager.enableAlpha();
			Tessellator tess = Tessellator.getInstance();
			int bright = rb.setLighting(player.world, player.getPosition());
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			rb.renderFromInside = false;
			float alpha = RenderBlocks.alpha;
			RenderBlocks.alpha = 0.5f;
			rb.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
			for(EnumFacing f : EnumFacing.VALUES)
			{
				List<BakedQuad> quads = mod.getQuads(state, f, 1L);
				if(quads.size() != 1) continue;
				TextureAtlasSprite sprite = ((BakedQuad) quads.get(0)).getSprite();
				rb.renderFace(f, 0.0, 0.0, 0.0, sprite, 1.0f, 1.0f, 1.0f, bright);
			}
			tess.draw();
			GlStateManager.disableBlend();
			RenderBlocks.alpha = alpha;
			GL11.glPopMatrix();
		}
	}
}