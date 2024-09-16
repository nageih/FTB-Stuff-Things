package dev.ftb.mods.ftbobb.recipes;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class FTBOBBRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    private final String name;

    public FTBOBBRecipeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
