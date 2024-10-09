package dev.ftb.mods.ftbobb.crafting;

import dev.ftb.mods.ftbobb.crafting.recipe.DripperRecipe;
import dev.ftb.mods.ftbobb.crafting.recipe.FusingMachineRecipe;
import dev.ftb.mods.ftbobb.crafting.recipe.JarRecipe;
import dev.ftb.mods.ftbobb.crafting.recipe.SuperCoolerRecipe;

public interface RecipeCaches {
    RecipeCache<JarRecipe> TEMPERED_JAR = new RecipeCache<>();
    RecipeCache<DripperRecipe> DRIPPER = new RecipeCache<>();
    RecipeCache<FusingMachineRecipe> FUSING_MACHINE = new RecipeCache<>();
    RecipeCache<SuperCoolerRecipe> SUPER_COOLER = new RecipeCache<>();

    static void clearAll() {
        TEMPERED_JAR.clear();
        DRIPPER.clear();
        FUSING_MACHINE.clear();
        SUPER_COOLER.clear();
    }
}
