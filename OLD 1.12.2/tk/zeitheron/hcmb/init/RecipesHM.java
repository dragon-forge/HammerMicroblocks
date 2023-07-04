package tk.zeitheron.hcmb.init;

import com.zeitheron.hammercore.utils.recipes.helper.RecipeRegistry;
import com.zeitheron.hammercore.utils.recipes.helper.RegisterRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import tk.zeitheron.hcmb.recipes.RecipeAddMicroblocks;
import tk.zeitheron.hcmb.recipes.RecipeCutMicroblock;

@RegisterRecipes(modid = "hcmb")
public class RecipesHM
		extends RecipeRegistry
{
	@Override
	public void crafting()
	{
		recipe(new RecipeCutMicroblock().setRegistryName("cut_microblock"));
		recipe(new RecipeAddMicroblocks().setRegistryName("add_microblock"));
		shaped(ItemsHM.STONE_HANDLE, " s ", "s s", " s ", 's', "stone");
		shaped(ItemsHM.IRON_SAW, "sss", "sih", 's', "stickWood", 'h', "gearStone", 'i', "ingotIron");
		shaped(ItemsHM.GOLD_SAW, "sss", "sih", 's', "stickWood", 'h', "gearStone", 'i', "ingotGold");
		shaped(ItemsHM.DIAMOND_SAW, "sss", "sih", 's', "stickWood", 'h', "gearStone", 'i', "gemDiamond");
		shaped(ItemsHM.EMERALD_SAW, "sss", "sih", 's', "stickWood", 'h', "gearStone", 'i', "gemEmerald");
		shaped(ItemsHM.BEDROCK_SAW, "sss", "sih", 's', "stickWood", 'h', "gearStone", 'i', new ItemStack(Blocks.BEDROCK));
	}
}