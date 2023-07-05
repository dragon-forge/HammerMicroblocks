package org.zeith.multipart.microblocks.client.resource.model;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.*;
import org.joml.Vector3f;
import org.zeith.hammerlib.client.model.*;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.init.ItemsHM;

import java.util.*;
import java.util.function.Function;

@LoadUnbakedGeometry(path = "microblock_item")
public class MicroblockItemModel
		implements IUnbakedGeometry<MicroblockItemModel>
{
	public static final ItemTransforms BLOCK_TRANSFORMS = new ItemTransforms(
			new ItemTransform(new Vector3f(75, 45, 0), new Vector3f(
					1 / 16F, -0.75F / 16F, 2 / 16F), new Vector3f(0.375F, 0.375F, 0.375F)), // thirdPersonLeftHand
			new ItemTransform(new Vector3f(75, 45, 0), new Vector3f(
					1 / 16F, -0.75F / 16F, 2 / 16F), new Vector3f(0.375F, 0.375F, 0.375F)), // thirdPersonRightHand
			new ItemTransform(new Vector3f(0, 30, 0), new Vector3f(0,
					2 / 16F, 3 / 16F
			), new Vector3f(0.4F, 0.4F, 0.4F)), // firstPersonLeftHand
			new ItemTransform(new Vector3f(0, 30, 0), new Vector3f(0,
					2 / 16F, 3 / 16F
			), new Vector3f(0.4F, 0.4F, 0.4F)), // firstPersonRightHand
			ItemTransform.NO_TRANSFORM, // head
			new ItemTransform(new Vector3f(30, 225, 0), new Vector3f(
					-4 / 16F, 1.5F / 16F, 0), new Vector3f(0.625F, 0.625F, 0.625F)), // gui
			new ItemTransform(new Vector3f(), new Vector3f(0, 0.1875F, 0), new Vector3f(0.25F, 0.25F, 0.25F)), // ground
			new ItemTransform(new Vector3f(), new Vector3f(), new Vector3f(0.5F, 0.5F, 0.5F)), // fixed
			ImmutableMap.of()
	);
	
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
	{
		return new BakedMicroblockModelRouter();
	}
	
	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
	{
		return List.of();
	}
	
	public static class BakedMicroblockModelRouter
			implements IBakedModel
	{
		@Override
		public ItemTransforms getTransforms()
		{
			return BLOCK_TRANSFORMS;
		}
		
		@Override
		public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)
		{
			return List.of();
		}
		
		@Override
		public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous)
		{
			var state = ItemsHM.MICROBLOCK.getMicroblockMaterialStack(itemStack);
			var model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(state);
			return model.getRenderTypes(itemStack, fabulous);
		}
		
		@Override
		public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous)
		{
			var state = ItemsHM.MICROBLOCK.getMicroblockMaterialStack(itemStack);
			var type = ItemsHM.MICROBLOCK.getMicroblockType(itemStack);
			
			return List.of(new BakedMicroblockModelSpecific(
					BLOCK_TRANSFORMS,
					state,
					type
			));
		}
		
		@Override
		public boolean useAmbientOcclusion()
		{
			return true;
		}
		
		@Override
		public boolean isGui3d()
		{
			return true;
		}
		
		@Override
		public boolean usesBlockLight()
		{
			return true;
		}
		
		@Override
		public boolean isCustomRenderer()
		{
			return false;
		}
		
		@Override
		public TextureAtlasSprite getParticleIcon()
		{
			return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
					.apply(MissingTextureAtlasSprite.getLocation());
		}
	}
	
	public static class BakedMicroblockModelSpecific
			implements IBakedModel
	{
		public final ItemTransforms transforms;
		public final ItemStack blockState;
		public final MicroblockType type;
		
		private List<BakedQuad> quads = null;
		
		public BakedMicroblockModelSpecific(ItemTransforms transforms, ItemStack blockState, MicroblockType type)
		{
			this.transforms = transforms;
			this.blockState = blockState;
			this.type = type;
		}
		
		@Override
		public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)
		{
			if(side != null) return Collections.emptyList();
			if(quads == null)
			{
				var aabbs = type.getModelStrips(type.getPlacementForRendering());
				quads = ModelGeneratorSystem.generateMesh(aabbs, blockState, Direction.NORTH).toBakedBlockQuads();
				quads = Collections.unmodifiableList(quads);
			}
			return quads;
		}
		
		@Override
		public boolean useAmbientOcclusion()
		{
			return true;
		}
		
		@Override
		public boolean isGui3d()
		{
			return true;
		}
		
		@Override
		public boolean usesBlockLight()
		{
			return true;
		}
		
		@Override
		public boolean isCustomRenderer()
		{
			return true;
		}
		
		@Override
		public TextureAtlasSprite getParticleIcon()
		{
			return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(blockState)
					.getParticleIcon(ModelData.EMPTY);
		}
	}
}