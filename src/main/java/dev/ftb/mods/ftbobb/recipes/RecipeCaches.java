package dev.ftb.mods.ftbobb.recipes;

public interface RecipeCaches {
    RecipeCache<JarRecipe> TEMPERED_JAR = new RecipeCache<>();

    static void clearAll() {
        TEMPERED_JAR.clear();
    }
}
