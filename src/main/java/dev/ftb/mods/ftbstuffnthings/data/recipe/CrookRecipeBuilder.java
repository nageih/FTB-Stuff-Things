package dev.ftb.mods.ftbstuffnthings.data.recipe;

import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.CrookRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class CrookRecipeBuilder extends BaseRecipeBuilder<CrookRecipe> {
    private final Ingredient ingredient;
    private final List<ItemWithChance> results;
    private final int max;
    private boolean replaceDrops;

    public CrookRecipeBuilder(Ingredient ingredient, List<ItemWithChance> results, int max) {
        this.ingredient = ingredient;
        this.results = results;
        this.max = max;
        this.replaceDrops = true;
    }

    public CrookRecipeBuilder(Ingredient ingredient, List<ItemWithChance> results) {
        this(ingredient, results, 1);
    }

    public CrookRecipeBuilder keepExistingDrops() {
        replaceDrops = false;
        return this;
    }

    @Override
    protected CrookRecipe buildRecipe() {
        return new CrookRecipe(ingredient, results, max, replaceDrops);
    }
}
