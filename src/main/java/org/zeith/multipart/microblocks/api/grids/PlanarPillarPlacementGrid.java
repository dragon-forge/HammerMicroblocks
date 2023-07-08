package org.zeith.multipart.microblocks.api.grids;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.init.PartPlacementsHM;

public class PlanarPillarPlacementGrid
		extends PlanarPlacementGrid
{
	public static final PlanarPillarPlacementGrid INSTANCE = new PlanarPillarPlacementGrid();
	
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