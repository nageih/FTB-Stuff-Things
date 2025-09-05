package dev.ftb.mods.ftbstuffnthings.data.recipe;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.WoodenBasinRecipe;
import net.neoforged.neoforge.fluids.FluidStack;

public class WoodenBasinRecipeBuilder extends BaseRecipeBuilder<WoodenBasinRecipe> {
    private final String inputStateStr;
    private final FluidStack outputFluid;
    private float productionChance = 1f;
    private float blockConsumeChance = 1f;
    private boolean dropItems = false;

    public WoodenBasinRecipeBuilder(String inputStateStr, FluidStack outputFluid) {
        this.inputStateStr = inputStateStr;
        this.outputFluid = outputFluid;
    }

    public WoodenBasinRecipeBuilder withProductionChance(float chance) {
        productionChance = chance;
        return this;
    }

    public WoodenBasinRecipeBuilder withBlockConsumeChance(float chance) {
        blockConsumeChance = chance;
        return this;
    }

    public WoodenBasinRecipeBuilder dropItems() {
        dropItems = true;
        return this;
    }

    @Override
    protected WoodenBasinRecipe buildRecipe() {
        return new WoodenBasinRecipe(inputStateStr, outputFluid, productionChance, blockConsumeChance, dropItems);
    }
}
