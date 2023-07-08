package org.zeith.multipart.microblocks.init;

import org.zeith.hammerlib.annotations.*;
import org.zeith.multipart.microblocks.contents.multipart.MicroblockPartDefinition;

@SimplyRegister
public interface PartDefinitionsHM
{
	@RegistryName("microblock")
	MicroblockPartDefinition MICROBLOCK = new MicroblockPartDefinition();
}
