package org.zeith.multipart.microblocks.init;

import org.zeith.hammerlib.annotations.*;
import org.zeith.multipart.microblocks.contents.microblocks.*;

@SimplyRegister
public interface MicroblockTypesHM
{
	@RegistryName("anti_cover")
	PlanarMicroblockType ANTI_COVER = new PlanarMicroblockType(14, false);
	
	@RegistryName("hollow_anti_cover")
	PlanarHollowMicroblockType HOLLOW_ANTI_COVER = new PlanarHollowMicroblockType(14, false);
	
	@RegistryName("triple_panel")
	PlanarMicroblockType TRIPLE_PANEL = new PlanarMicroblockType(12, false);
	
	@RegistryName("hollow_triple_panel")
	PlanarHollowMicroblockType HOLLOW_TRIPLE_PANEL = new PlanarHollowMicroblockType(12, false);
	
	@RegistryName("slab")
	PlanarMicroblockType SLAB = new PlanarMicroblockType(8, false);
	
	@RegistryName("hollow_slab")
	PlanarHollowMicroblockType HOLLOW_SLAB = new PlanarHollowMicroblockType(8, false);
	
	@RegistryName("triple_cover")
	PlanarMicroblockType TRIPLE_COVER = new PlanarMicroblockType(6, true);
	
	@RegistryName("hollow_triple_cover")
	PlanarHollowMicroblockType HOLLOW_TRIPLE_COVER = new PlanarHollowMicroblockType(6, true);
	
	@RegistryName("panel")
	PlanarMicroblockType PANEL = new PlanarMicroblockType(4, true);
	
	@RegistryName("hollow_panel")
	PlanarHollowMicroblockType HOLLOW_PANEL = new PlanarHollowMicroblockType(4, true);
	
	@RegistryName("cover")
	PlanarMicroblockType COVER = new PlanarMicroblockType(2, true);
	
	@RegistryName("hollow_cover")
	PlanarHollowMicroblockType HOLLOW_COVER = new PlanarHollowMicroblockType(2, true);
	
	@RegistryName("facade")
	PlanarMicroblockType FACADE = new PlanarMicroblockType(1, true);
	
	@RegistryName("hollow_facade")
	PlanarHollowMicroblockType HOLLOW_FACADE = new PlanarHollowMicroblockType(1, true);
	
	// Pillars
	
	@RegistryName("anticover_pillar")
	PillarMicroblockType ANTICOVER_PILLAR = new PillarMicroblockType(14F);
	
	@RegistryName("triple_panel_pillar")
	PillarMicroblockType TRIPLE_PANEL_PILLAR = new PillarMicroblockType(12F);
	
	@RegistryName("slab_pillar")
	PillarMicroblockType SLAB_PILLAR = new PillarMicroblockType(8F);
	
	@RegistryName("triple_cover_pillar")
	PillarMicroblockType TRIPLE_COVER_PILLAR = new PillarMicroblockType(6F);
	
	@RegistryName("panel_pillar")
	PillarMicroblockType PANEL_PILLAR = new PillarMicroblockType(4F);
	
	@RegistryName("cover_pillar")
	PillarMicroblockType COVER_PILLAR = new PillarMicroblockType(2F);
}