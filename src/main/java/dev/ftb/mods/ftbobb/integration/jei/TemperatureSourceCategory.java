package dev.ftb.mods.ftbobb.integration.jei;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.crafting.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;

public class TemperatureSourceCategory extends BaseOBBCategory<TemperatureSourceRecipe> {
    protected TemperatureSourceCategory() {
        super(RecipeTypes.TEMPERATURE_SOURCE,
                Component.translatable("ftbobb.temperature_source"),
                guiHelper().drawableBuilder(FTBOBB.id("textures/gui/temperature_source_jei.png"), 0, 0, 71, 30)
                        .setTextureSize(128, 64).build(),
                guiHelper().createDrawableIngredient(OBBIngredientTypes.TEMPERATURE, Temperature.HOT)
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TemperatureSourceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 48, 7)
                .addIngredient(OBBIngredientTypes.TEMPERATURE, recipe.getTemperature())
                .addRichTooltipCallback((recipeSlotView, tooltip) ->
                        tooltip.add(Component.translatable("ftbobb.efficiency", recipe.getTemperatureAndEfficiency().formatEfficiency())));

        if (!recipe.getDisplayStack().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 3, 7)
                    .addIngredient(VanillaTypes.ITEM_STACK, recipe.getDisplayStack());
        }
    }
}
