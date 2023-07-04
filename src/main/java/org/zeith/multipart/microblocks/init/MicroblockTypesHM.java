package org.zeith.multipart.microblocks.init;

import org.zeith.hammerlib.annotations.*;
import org.zeith.multipart.microblocks.impl.CoverMicroblockType;

@SimplyRegister
public interface MicroblockTypesHM
{
	@RegistryName("slab")
	CoverMicroblockType SLAB = new CoverMicroblockType(8);
	
	@RegistryName("panel")
	CoverMicroblockType PANEL = new CoverMicroblockType(4);
	
	@RegistryName("cover")
	CoverMicroblockType COVER = new CoverMicroblockType(2);

//	@RegistryName("pillar")
//	MicroblockType PILLAR = new MicroblockType();
}