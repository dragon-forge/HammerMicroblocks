package org.zeith.multipart.microblocks.api.grids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.api.PartContainer;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.api.placement.PartPos;
import org.zeith.multipart.init.PartPlacementsHM;
import org.zeith.multipart.microblocks.contents.multipart.entity.MicroblockEntity;

public class PlanarPillarPlacementGrid
		extends PlanarPlacementGrid
{
	public static final PlanarPillarPlacementGrid INSTANCE = new PlanarPillarPlacementGrid();
	
	@Override
	public @Nullable BlockState getAppearance(MicroblockEntity thisEntity, PartPlacement thisPlacement, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos, @Nullable PartContainer queryContainer, @Nullable PartPos queryPartPos)
	{
		if(queryContainer != null && queryPartPos != null
		   && queryContainer.getPartAt(queryPartPos.placement()) instanceof MicroblockEntity mbe
		   && mbe.state.getType() == thisEntity.state.getType())
			return thisEntity.state.asBlockState();
		return null;
	}
	
	@Override
	public @Nullable PartPlacement pickPlacement(Player player, BlockHitResult hit, boolean sameBlock)
	{
		var placement = super.pickPlacement(player, hit, sameBlock);
		if(placement == null) return null;
		var pd = placement.getDirection();
		if(pd != null && pd.getAxis() != hit.getDirection().getAxis()) return null;
		return PartPlacementsHM.CENTER;
	}
}