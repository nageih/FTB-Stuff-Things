package dev.ftb.mods.ftbstuffnthings.data.recipe;


import net.neoforged.neoforge.fluids.FluidStack;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.SluiceRecipe;
import dev.ftb.mods.ftbstuffnthings.items.MeshType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;

import java.util.HashSet;
import java.util.List;

public class SluiceRecipeBuilder extends BaseRecipeBuilder<SluiceRecipe> {
    private final Ingredient ingredient;
    private final List<ItemWithChance> results;
    private int maxResults;
    private FluidStack fluid;
    private float processingTimeMultiplier;
    private final HashSet<MeshType> meshTypes;

    public SluiceRecipeBuilder(Ingredient ingredient, List<ItemWithChance> results, List<MeshType> meshTypes) {
        this.ingredient = ingredient;
        this.results = results;
        this.maxResults = 4;
        this.fluid = new FluidStack(Fluids.WATER, 10);
        this.processingTimeMultiplier = 1F;
        this.meshTypes = new HashSet<>(meshTypes);
    }

    public SluiceRecipeBuilder maxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public SluiceRecipeBuilder fluid(FluidStack fluid) {
        this.fluid = fluid;
        return this;
    }

    public SluiceRecipeBuilder processingTimeMultiplier(float processingTimeMultiplier) {
        this.processingTimeMultiplier = processingTimeMultiplier;
        return this;
    }

    @Override
    protected SluiceRecipe buildRecipe() {
        return new SluiceRecipe(ingredient, results, maxResults, fluid, processingTimeMultiplier, List.copyOf(meshTypes));
    }
}
