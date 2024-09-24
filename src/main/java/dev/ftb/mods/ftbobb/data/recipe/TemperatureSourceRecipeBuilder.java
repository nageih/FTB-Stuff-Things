package dev.ftb.mods.ftbobb.data.recipe;

import dev.ftb.mods.ftbobb.recipes.TemperatureSourceRecipe;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TemperatureSourceRecipeBuilder extends BaseRecipeBuilder {
    private final BlockState blockstate;
    private final Temperature temperature;
    private final double efficiency;
    private ItemStack displayStack = ItemStack.EMPTY;
    private boolean hideFromJEI = false;

    public TemperatureSourceRecipeBuilder(BlockState blockstate, Temperature temperature, double efficiency) {
        this.blockstate = blockstate;
        this.temperature = temperature;
        this.efficiency = efficiency;
    }

    public TemperatureSourceRecipeBuilder withDisplayItem(ItemStack stack) {
        this.displayStack = stack;
        return this;
    }

    public TemperatureSourceRecipeBuilder hideFromJEI() {
        this.hideFromJEI = true;
        return this;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, new TemperatureSourceRecipe(blockstate, temperature, efficiency, displayStack, hideFromJEI), null);
    }
}
