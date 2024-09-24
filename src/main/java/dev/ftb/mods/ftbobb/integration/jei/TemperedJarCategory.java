package dev.ftb.mods.ftbobb.integration.jei;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.recipes.JarRecipe;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TemperedJarCategory extends BaseOBBCategory<JarRecipe> {
    protected TemperedJarCategory() {
        super(RecipeTypes.JAR,
                Component.translatable(BlocksRegistry.TEMPERED_JAR.get().getDescriptionId()),
                guiHelper().drawableBuilder(FTBOBB.id("textures/gui/tempered_jar_recipe.png"), 0, 0, 150, 18)
                        .setTextureSize(256, 32).build(),
                guiHelper().createDrawableItemStack(new ItemStack(ItemsRegistry.TEMPERED_JAR.get()))
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JarRecipe recipe, IFocusGroup focuses) {
        // we assume here that there are no more than 3 inputs & 3 outputs
        // - see JarRecipe.Serializer#validateRecipe

        int nFluidsIn = recipe.getInputFluids().size();
        int nItemsIn = recipe.getInputItems().size();
        int nFluidsOut = recipe.getOutputFluids().size();
        int nItemsOut = recipe.getOutputItems().size();

        for (int i = 0; i < nFluidsIn; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + i * 20, 1)
                    .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.getInputFluids().get(i).getFluids()))
                    .setOverlay(new FluidAmountDrawable(recipe.getInputFluids().get(i).amount()), 0, 0);
        }
        for (int i = 0; i < nItemsIn; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + (i + nFluidsIn) * 20, 1)
                    .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getInputItems().get(i).getItems()));
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 67, 1)
                .addIngredient(OBBIngredientTypes.TEMPERATURE, recipe.getTemperature())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    String time = String.format("%.1f", recipe.getTime() / 20f);
                    tooltip.add(Component.translatable(FTBOBB.MODID + ".processing_time", time));
                });

        for (int i = 0; i < nFluidsOut; i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 93 + i * 20, 1)
                    .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.getOutputFluids().get(i))
                    .setOverlay(new FluidAmountDrawable(recipe.getOutputFluids().get(i).getAmount()), 0, 0);
        }
        for (int i = 0; i < nItemsOut; i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 93 + (i + nFluidsOut) * 20, 1)
                    .addIngredient(VanillaTypes.ITEM_STACK, recipe.getOutputItems().get(i));
        }
    }

    private record FluidAmountDrawable(int amount) implements IDrawable {
        @Override
        public int getWidth() {
            return 16;
        }

        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
            Font font = Minecraft.getInstance().font;
            String txt = amount >= 1000 ? amount / 1000.0 + "B" : amount + "mB";

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(xOffset + 16 - font.width(txt) / 2f, yOffset + 16 - font.lineHeight / 2f, 0f);
            guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
            guiGraphics.drawString(font, txt, 0, 0, 0xFFFFFFFF);
            guiGraphics.pose().popPose();
        }
    }
}
