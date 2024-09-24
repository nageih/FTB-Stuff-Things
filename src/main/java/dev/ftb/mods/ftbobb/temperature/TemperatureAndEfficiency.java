package dev.ftb.mods.ftbobb.temperature;

import dev.ftb.mods.ftbobb.recipes.JarRecipe;
import dev.ftb.mods.ftbobb.recipes.NoInventory;
import dev.ftb.mods.ftbobb.recipes.TemperatureSourceRecipe;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record TemperatureAndEfficiency(Temperature temperature, double efficiency) {
	public static final TemperatureAndEfficiency DEFAULT = new TemperatureAndEfficiency(Temperature.NORMAL, 1D);

	public static TemperatureAndEfficiency fromLevel(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		for (RecipeHolder<TemperatureSourceRecipe> recipe : level.getRecipeManager().getRecipesFor(RecipesRegistry.TEMPERATURE_SOURCE_TYPE.get(), NoInventory.INSTANCE, level)) {
			if (recipe.value().test(state)) {
				return recipe.value().getTemperatureAndEfficiency();
			}
		}

		return DEFAULT;
	}

	public int getRecipeTime(JarRecipe recipe) {
		return Mth.clamp((int) (recipe.getTime() / efficiency), 1, Short.MAX_VALUE);
	}
}
