package org.zeith.multipart.microblocks.init;

import org.zeith.hammerlib.annotations.*;
import org.zeith.multipart.microblocks.impl.PlanarMicroblockType;

@SimplyRegister
public interface MicroblockTypesHM
{
	@RegistryName("anti_cover")
	PlanarMicroblockType ANTI_COVER = new PlanarMicroblockType(14, false);
	
	@RegistryName("triple_panel")
	PlanarMicroblockType TRIPLE_PANEL = new PlanarMicroblockType(12, false);
	
	@RegistryName("slab")
	PlanarMicroblockType SLAB = new PlanarMicroblockType(8, false);
	
	@RegistryName("triple_cover")
	PlanarMicroblockType TRIPLE_COVER = new PlanarMicroblockType(6, false);
	
	@RegistryName("panel")
	PlanarMicroblockType PANEL = new PlanarMicroblockType(4, false);
	
	@RegistryName("cover")
	PlanarMicroblockType COVER = new PlanarMicroblockType(2, true);

//	@RegistryName("pillar")
//	MicroblockType PILLAR = new MicroblockType();
}