package dev.ftb.mods.ftbstuffnthings.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class FTBStuffRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    private final String name;

    public FTBStuffRecipeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
