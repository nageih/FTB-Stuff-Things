package dev.ftb.mods.ftbstuffnthings.data.recipe;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.DripperRecipe;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.Validate;

public class DripperRecipeBuilder extends BaseRecipeBuilder<DripperRecipe> {
    private final String inputStateStr;
    private final String outputStateStr;
    private final FluidStack fluid;
    private double chance = 1.0;
    private boolean consumeFluidOnFail = false;

    public DripperRecipeBuilder(String inputStateStr, String outputStateStr, FluidStack fluid) {
        this.inputStateStr = inputStateStr;
        this.outputStateStr = outputStateStr;
        this.fluid = fluid;
    }

    public DripperRecipeBuilder withChance(double chance) {
        Validate.isTrue(chance > 0.0 && chance <= 1.0, "chance must be in range (0.0 -> 1.0]");
        this.chance = chance;
        return this;
    }

    public DripperRecipeBuilder consumeFluidOnFail() {
        consumeFluidOnFail = true;
        return this;
    }

    @Override
    protected DripperRecipe buildRecipe() {
        return new DripperRecipe(inputStateStr, outputStateStr, fluid, chance, consumeFluidOnFail);
    }
}
