package dev.ftb.mods.ftbobb.integration.jei;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.recipes.JarRecipe;
import dev.ftb.mods.ftbobb.recipes.TemperatureSourceRecipe;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeTypes {
    public static final RecipeType<JarRecipe> TEMPERED_JAR = register("jar", JarRecipe.class);
    public static final RecipeType<TemperatureSourceRecipe> TEMPERATURE_SOURCE = register("temperature_source", TemperatureSourceRecipe.class);

    private static <T extends Recipe<?>> RecipeType<T> register(String name, Class<T> recipeClass) {
        return RecipeType.create(FTBOBB.MODID, name, recipeClass);
    }
}
