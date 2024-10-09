package dev.ftb.mods.ftbobb.integration.jei;


import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.crafting.recipe.DripperRecipe;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class DripperCategory extends BaseOBBCategory<DripperRecipe> {
	public DripperCategory() {
		super(RecipeTypes.DRIPPER,
				Component.translatable(BlocksRegistry.DRIPPER.get().getDescriptionId()),
				guiHelper().drawableBuilder(FTBOBB.id("textures/gui/dripper_jei_background.png"),
						0, 0, 91, 30).setTextureSize(128, 64).build(),
				guiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemsRegistry.DRIPPER.get().getDefaultInstance())
		);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DripperRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 7)
				.addItemStack(recipe.getOutputItem());

		builder.addSlot(RecipeIngredientRole.INPUT, 23, 7)
				.addIngredient(VanillaTypes.ITEM_STACK, recipe.getInputItem());

		builder.addSlot(RecipeIngredientRole.INPUT, 3, 7)
				.addIngredient(NeoForgeTypes.FLUID_STACK, recipe.getFluid())
				.setOverlay(new FluidAmountDrawable(recipe.getFluid().getAmount()), 0, 0)
				.addRichTooltipCallback((recipeSlotView, tooltipBuilder) -> {
					if (recipe.getChance() < 1D) {
						String pct = String.format("%.0f", recipe.getChance() * 100d);
						tooltipBuilder.add(Component.translatable("ftbobb.dripper.chance", pct).withStyle(ChatFormatting.YELLOW));
						if (recipe.consumeFluidOnFail()) {
							tooltipBuilder.add(Component.translatable("ftbobb.dripper.consume_on_fail").withStyle(ChatFormatting.GOLD));
						}
					}
				});
	}
}
