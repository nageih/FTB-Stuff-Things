package dev.ftb.mods.ftbobb.data.recipe;

import dev.ftb.mods.ftbobb.crafting.recipe.HammerRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class HammerRecipeBuilder extends BaseRecipeBuilder<HammerRecipe> {
    private final Ingredient ingredient;
    private final List<ItemStack> results;

    public HammerRecipeBuilder(Ingredient ingredient, List<ItemStack> results) {
        this.ingredient = ingredient;
        this.results = results;
    }

    @Override
    protected HammerRecipe buildRecipe() {
        return new HammerRecipe(ingredient, results);
    }
}
