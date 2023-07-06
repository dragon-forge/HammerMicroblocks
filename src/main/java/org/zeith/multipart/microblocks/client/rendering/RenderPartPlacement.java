package org.zeith.multipart.microblocks.client.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.multipart.api.*;
import org.zeith.multipart.blocks.BlockMultipartContainer;
import org.zeith.multipart.microblocks.api.tile.MicroblockState;
import org.zeith.multipart.microblocks.client.resource.model.ModelGeneratorSystem;
import org.zeith.multipart.microblocks.init.*;
import org.zeith.multipart.microblocks.multipart.MicroblockPartDefinition;

import java.util.stream.Collectors;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderPartPlacement
{
	public static float r = 0, g = 0, b = 0, a = 0.4F;
	
	@SubscribeEvent
	public static void renderOutline(RenderHighlightEvent.Block e)
	{
		var mc = Minecraft.getInstance();
		var pl = mc.player;
		if(pl == null) return;
		var microblockStack = pl.getItemInHand(InteractionHand.MAIN_HAND);
		if(!microblockStack.is(ItemsHM.MICROBLOCK)) microblockStack = pl.getItemInHand(InteractionHand.OFF_HAND);
		if(!microblockStack.is(ItemsHM.MICROBLOCK)) return;
		var type = ItemsHM.MICROBLOCK.getMicroblockType(microblockStack);
		var microstate = ItemsHM.MICROBLOCK.getMicroblockMaterialState(microblockStack);
		if(type == null || microstate == null) return;
		var hit = e.getTarget();
		var pos = hit.getBlockPos();
		var state = mc.level.getBlockState(pos);
		var bounds = state.getVisualShape(mc.level, pos, CollisionContext.of(pl));
		if(bounds.isEmpty()) bounds = state.getShape(mc.level, pos, CollisionContext.of(pl));
		var grid = type.getPlacementGrid();
		var lines = grid.getLinesForRendering(pl, bounds, hit);
		if(lines.isEmpty()) return;
		
		VertexConsumer vb = e.getMultiBufferSource().getBuffer(RenderType.lines());
		
		var pose = e.getPoseStack();
		pose.pushPose();
		
		var last = pose.last();
		var p = last.pose();
		var n = last.normal();
		
		Vec3 vec3 = e.getCamera().getPosition();
		double x = pos.getX() - vec3.x();
		double y = pos.getY() - vec3.y();
		double z = pos.getZ() - vec3.z();
		
		if(lines.size() % 2 == 0)
		{
			for(int i = 0; i < lines.size(); i += 2)
			{
				var v1 = lines.get(i);
				var v2 = lines.get(i + 1);
				
				float x1 = v1.x, y1 = v1.y, z1 = v1.z;
				float x2 = v2.x, y2 = v2.y, z2 = v2.z;
				
				float dX = x2 - x1;
				float dY = y2 - y1;
				float dZ = z2 - z1;
				float len = Mth.sqrt(dX * dX + dY * dY + dZ * dZ);
				dX /= len;
				dY /= len;
				dZ /= len;
				
				vb.vertex(p, (float) (x1 + x), (float) (y1 + y), (float) (z1 + z))
						.color(r, g, b, a)
						.normal(n, dX, dY, dZ)
						.endVertex();
				
				vb.vertex(p, (float) (x2 + x), (float) (y2 + y), (float) (z2 + z))
						.color(r, g, b, a)
						.normal(n, dX, dY, dZ)
						.endVertex();
			}
		}
		
		var placement = grid.pickPlacement(pl, hit, pos.equals(hit.getBlockPos()));
		
		if(placement != null)
		{
			var hitDir = hit.getDirection();
			
			var pc = BlockMultipartContainer.pc(pl.level(), pos);
			var shift = pc == null;
			c:
			if(pc != null)
			{
				var loc = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()));
				
				double axial = switch(hit.getDirection().getAxis())
				{
					case Y -> loc.y;
					case X -> loc.x;
					case Z -> loc.z;
					default -> -1;
				};
				
				double desired = hit.getDirection().getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 0 : 1;
				
				if(Math.abs(axial - desired) < 0.001)
				{
					shift = true;
					break c;
				}
				
				if(pc.getPartAt(placement) != null)
				{
					shift = true;
					break c;
				}
				
				MicroblockPartDefinition.MicroblockConfiguration config = new MicroblockPartDefinition.MicroblockConfiguration(
						new MicroblockState()
								.setType(type)
								.setMaterial(microblockStack)
				);
				
				if(!placement.canBePlacedAlongside(pc.parts().stream().map(PartEntity::placement)
						.collect(Collectors.toSet())))
				{
					shift = true;
					break c;
				}
				if(!PartDefinitionsHM.MICROBLOCK.canPlaceAt(pc, config, placement))
				{
					shift = true;
					break c;
				}
				
				for(var part : pc.parts())
				{
					if(!part.placement().isCompatibleWith(placement))
					{
						shift = true;
						break c;
					}
					if(part.blocksPlacementFor(PartDefinitionsHM.MICROBLOCK, placement))
					{
						shift = true;
						break c;
					}
				}
				
				PartEntity placeEntity = config.create(pc, placement);
				if(placeEntity == null)
				{
					shift = true;
					break c;
				}
				
				var shapeOfEntity = placeEntity.getPartOccupiedShape();
				for(var entry : pc.parts())
					if(IndexedVoxelShape.shapesIntersect(shapeOfEntity, entry
							.getPartOccupiedShapeWith(placeEntity, shapeOfEntity)))
					{
						shift = true;
						break c;
					}
			}
			
			if(shift)
			{
				x += hitDir.getStepX();
				y += hitDir.getStepY();
				z += hitDir.getStepZ();
			}
			
			pose.translate(x, y, z);
			last = pose.last();
			
			VertexConsumer buf = e.getMultiBufferSource().getBuffer(RenderType.translucent());
			
			var finalPos = shift ? pos.relative(hitDir) : pos;
			
			var pl0 = grid.pickPlacement(pl, hit, finalPos.equals(hit.getBlockPos()));
			if(pl0 != null)
				placement = pl0;
			
			var strips = type.getModelStrips(placement);
			
			pc = BlockMultipartContainer.pc(mc.level, finalPos);
			
			var rng = RandomSource.create(pos.asLong());
			for(RenderType layer : RenderType.chunkBufferLayers())
				for(BakedQuad quad : ModelGeneratorSystem.generateMesh(type, placement, pc, mc.level, finalPos, strips, microstate, rng, layer)
						.toBakedBlockQuads())
					buf.putBulkData(last, quad, 1, 1, 1, 0.6F, 255, 0, true);
		}
		
		pose.popPose();
		e.setCanceled(true);
	}
}