package dev.ftb.mods.ftbstuffnthings.crafting;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.*;

public interface RecipeCaches {
    RecipeCache<JarRecipe> TEMPERED_JAR = new RecipeCache<>();
    RecipeCache<DripperRecipe> DRIPPER = new RecipeCache<>();
    RecipeCache<FusingMachineRecipe> FUSING_MACHINE = new RecipeCache<>();
    RecipeCache<SuperCoolerRecipe> SUPER_COOLER = new RecipeCache<>();
    RecipeCache<SluiceRecipe> SLUICE = new RecipeCache<>();

    static void clearAll() {
        TEMPERED_JAR.clear();
        DRIPPER.clear();
        FUSING_MACHINE.clear();
        SUPER_COOLER.clear();
        SLUICE.clear();
    }
}
