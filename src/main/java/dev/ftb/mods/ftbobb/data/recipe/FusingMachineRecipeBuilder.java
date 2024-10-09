package dev.ftb.mods.ftbobb.data.recipe;

import dev.ftb.mods.ftbobb.recipes.EnergyComponent;
import dev.ftb.mods.ftbobb.recipes.FusingMachineRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class FusingMachineRecipeBuilder extends BaseRecipeBuilder<FusingMachineRecipe> {
    private final List<Ingredient> inputs;
    private final FluidStack fluidResult;
    private final int fePerTick;
    private final int ticks;

    public FusingMachineRecipeBuilder(List<Ingredient> inputs, FluidStack fluidResult, int fePerTick, int ticks) {
        this.inputs = inputs;
        this.fluidResult = fluidResult;
        this.fePerTick = fePerTick;
        this.ticks = ticks;
    }

    @Override
    protected FusingMachineRecipe buildRecipe() {
        return new FusingMachineRecipe(inputs, fluidResult, new EnergyComponent(fePerTick, ticks));
    }
}
