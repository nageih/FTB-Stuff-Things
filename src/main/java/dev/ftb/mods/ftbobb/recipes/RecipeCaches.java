package dev.ftb.mods.ftbobb.recipes;

public interface RecipeCaches {
    RecipeCache<JarRecipe> TEMPERED_JAR = new RecipeCache<>();
    RecipeCache<DripperRecipe> DRIPPER = new RecipeCache<>();

    static void clearAll() {
        TEMPERED_JAR.clear();
        DRIPPER.clear();
    }
}
