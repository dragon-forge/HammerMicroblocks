package tk.zeitheron.hcmb.multipart;

import com.zeitheron.hammercore.api.multipart.IMultipartBaked;
import com.zeitheron.hammercore.api.multipart.MultipartSignature;
import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;
import tk.zeitheron.hcmb.items.ItemMicroblock;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public class MultipartMicroblock
		extends MultipartSignature
		implements IMultipartBaked
{
	public EnumFacing orientation;
	public ItemMicroblock.EnumMicroblockType type;
	public Block block;
	public int meta;

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Orientation", this.orientation.ordinal());
		nbt.setString("Type", this.type.type);
		nbt.setInteger("Meta", this.meta);
		nbt.setString("Block", this.block.getRegistryName().toString());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.orientation = EnumFacing.VALUES[nbt.getInteger("Orientation") % EnumFacing.VALUES.length];
		this.type = ItemMicroblock.EnumMicroblockType.typeOrNull(nbt.getString("Type"));
		this.meta = nbt.getInteger("Meta");
		this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("Block")));
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		if(this.orientation != null)
		{
			double o1 = this.type.thickness;
			double o2 = 1.0f - this.type.thickness;
			double minX = 0.0;
			double minY = 0.0;
			double minZ = 0.0;
			double maxX = 1.0;
			double maxY = 1.0;
			double maxZ = 1.0;
			if(this.orientation == EnumFacing.DOWN)
			{
				maxY = o1;
			}
			if(this.orientation == EnumFacing.UP)
			{
				minY = o2;
				maxY = 1.0;
			}
			if(this.orientation == EnumFacing.WEST)
			{
				maxX = o1;
			}
			if(this.orientation == EnumFacing.EAST)
			{
				minX = o2;
				maxX = 1.0;
			}
			if(this.orientation == EnumFacing.NORTH)
			{
				maxZ = o1;
			}
			if(this.orientation == EnumFacing.SOUTH)
			{
				minZ = o2;
				maxZ = 1.0;
			}
			return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
		}
		return new AxisAlignedBB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
	}

	@Override
	public void addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		if(this.block != null)
		{
			this.block.addDestroyEffects(world, pos, manager);
		}
	}

	@Override
	public void addHitEffects(World worldObj, RayTraceResult target, ParticleManager manager)
	{
		if(this.block != null)
		{
			this.block.addHitEffects(this.block.getStateFromMeta(this.meta), worldObj, target, manager);
		}
	}

	@Override
	public float getHardness(EntityPlayer player)
	{
		return super.getHardness(player);
	}

	@Override
	protected float getMultipartHardness(EntityPlayer player)
	{
		return this.block != null ? this.block.getStateFromMeta(this.meta).getBlockHardness(this.world, this.pos) : 1.0f;
	}

	@Override
	public ItemStack getPickBlock(EntityPlayer player)
	{
		if(this.block == null || this.type == null)
			return ItemStack.EMPTY;
		return ItemMicroblock.makeStack(block.getStateFromMeta(meta), type, 1);
	}

	@Override
	public SoundType getSoundType(EntityPlayer player)
	{
		return this.block != null ? this.block.getSoundType(this.block.getStateFromMeta(this.meta), this.world, this.pos, (Entity) player) : SoundType.STONE;
	}

	@Override
	public int getLightLevel()
	{
		return this.block != null ? this.block.getLightValue(this.block.getStateFromMeta(this.meta)) : 0;
	}

	@Override
	public boolean canPlaceInto(TileMultipart tmp)
	{
		return super.canPlaceInto(tmp);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void generateBakedQuads(Consumer<BakedQuad> quadConsumer, Function<String, TextureAtlasSprite> spriteFunction, FaceBakery bakery, @Nullable EnumFacing side, long rand, int startTintIndex)
	{
		AxisAlignedBB aabb = getBoundingBox();
		float minX = (float) (aabb.minX * 16);
		float minY = (float) (aabb.minY * 16);
		float minZ = (float) (aabb.minZ * 16);
		float maxX = (float) (aabb.maxX * 16);
		float maxY = (float) (aabb.maxY * 16);
		float maxZ = (float) (aabb.maxZ * 16);
		float[] uv = new float[]{
				0,
				0,
				16,
				16
		};
		if(orientation.getAxis() != side.getAxis())
		{
			boolean p = orientation.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE;
			int a = orientation.getAxis() == EnumFacing.Axis.X || side.getAxis() == EnumFacing.Axis.X ? 0 : 1, b = orientation.getAxis() == EnumFacing.Axis.X || side.getAxis() == EnumFacing.Axis.X ? 2 : 3;
			if(orientation.getAxis() == EnumFacing.Axis.Y)
			{
				a = 1;
				b = 3;
			}
			if(p)
			{
				uv[a] = 0;
				uv[b] = type.thickness * 16F;
			} else
			{
				uv[a] = 16F - type.thickness * 16F;
				uv[b] = 16F;
			}
		}
		IBlockState state = block.getStateFromMeta(meta);
		BakedQuad bq = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getQuads(state, side, rand).stream().findFirst().orElse(null);
		if(bq != null)
			quadConsumer.accept(bakery.makeBakedQuad(new Vector3f(minX, minY, minZ),
					new Vector3f(maxX, maxY, maxZ),
					new BlockPartFace(side,
							startTintIndex,
							"0",
							new BlockFaceUV(uv, 0)),
					bq.getSprite(),
					side,
					ModelRotation.X0_Y0,
					null,
					true,
					bq.shouldApplyDiffuseLighting()));
	}
}

