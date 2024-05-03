package org.zeith.multipart.microblocks.init;

import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.multipart.microblocks.contents.multipart.placements.CubeConfiguration;
import org.zeith.multipart.microblocks.contents.multipart.placements.CubicPartPlacement;

@SimplyRegister
public interface PartPlacementsHM
{
	@RegistryName("cubic/xn_yn_zn")
	CubicPartPlacement XN_YN_ZN = CubicPartPlacement.LOOKUP.get(0);
	
	@RegistryName("cubic/xn_yn_zp")
	CubicPartPlacement XN_YN_ZP = CubicPartPlacement.LOOKUP.get(1);
	
	@RegistryName("cubic/xn_yp_zn")
	CubicPartPlacement XN_YP_ZN = CubicPartPlacement.LOOKUP.get(2);
	
	@RegistryName("cubic/xn_yp_zp")
	CubicPartPlacement XN_YP_ZP = CubicPartPlacement.LOOKUP.get(3);
	
	@RegistryName("cubic/xp_yn_zn")
	CubicPartPlacement XP_YN_ZN = CubicPartPlacement.LOOKUP.get(4);
	
	@RegistryName("cubic/xp_yn_zp")
	CubicPartPlacement XP_YN_ZP = CubicPartPlacement.LOOKUP.get(5);
	
	@RegistryName("cubic/xp_yp_zn")
	CubicPartPlacement XP_YP_ZN = CubicPartPlacement.LOOKUP.get(6);
	
	@RegistryName("cubic/xp_yp_zp")
	CubicPartPlacement XP_YP_ZP = CubicPartPlacement.LOOKUP.get(7);
}