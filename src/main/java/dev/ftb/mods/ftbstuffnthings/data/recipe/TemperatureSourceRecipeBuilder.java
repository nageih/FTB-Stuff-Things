package dev.ftb.mods.ftbstuffnthings.data.recipe;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TemperatureSourceRecipeBuilder extends BaseRecipeBuilder<TemperatureSourceRecipe> {
    private final String blockstateStr;
    private final Temperature temperature;
    private final double efficiency;
    private ItemStack displayStack = ItemStack.EMPTY;
    private boolean hideFromJEI = false;

    public TemperatureSourceRecipeBuilder(String blockstateStr, Temperature temperature, double efficiency) {
        this.blockstateStr = blockstateStr;
        this.temperature = temperature;
        this.efficiency = efficiency;
    }

    public TemperatureSourceRecipeBuilder(BlockState blockstate, Temperature temperature, double efficiency) {
        this(BlockStateParser.serialize(blockstate), temperature, efficiency);
    }

    public TemperatureSourceRecipeBuilder(Block block, Temperature temperature, double efficiency) {
        this(BlockStateParser.serialize(block.defaultBlockState()), temperature, efficiency);
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
    protected TemperatureSourceRecipe buildRecipe() {
        return new TemperatureSourceRecipe(blockstateStr, temperature, efficiency, displayStack, hideFromJEI);
    }
}
