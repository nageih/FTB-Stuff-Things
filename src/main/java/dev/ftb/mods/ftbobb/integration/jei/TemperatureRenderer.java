package dev.ftb.mods.ftbobb.integration.jei;

import dev.ftb.mods.ftbobb.temperature.Temperature;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public enum TemperatureRenderer implements IIngredientRenderer<Temperature> {
    INSTANCE;

    @Override
    public void render(GuiGraphics guiGraphics, Temperature ingredient) {
        guiGraphics.blit(ingredient.getTexture(), 0, 0, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public List<Component> getTooltip(Temperature ingredient, TooltipFlag tooltipFlag) {
        return List.of(ingredient.getName());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, Temperature ingredient, TooltipFlag tooltipFlag) {
        tooltip.add(ingredient.getName());
        tooltip.add(Component.translatable("ftbobb.temperature_source"));
    }
}
