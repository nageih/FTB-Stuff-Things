package dev.ftb.mods.ftbobb.integration.jei;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.client.screens.FusingMachineScreen;
import dev.ftb.mods.ftbobb.client.screens.SuperCoolerScreen;
import dev.ftb.mods.ftbobb.client.screens.TemperedJarScreen;
import dev.ftb.mods.ftbobb.crafting.IHideableRecipe;
import dev.ftb.mods.ftbobb.crafting.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@JeiPlugin
public class OBBJeiPlugin implements IModPlugin {
    static IJeiHelpers jeiHelpers;
    static IRecipeManager recipeManager;
    static IRecipesGui recipesGui;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        recipeManager = jeiRuntime.getRecipeManager();
        recipesGui = jeiRuntime.getRecipesGui();

        jeiRuntime.getIngredientManager().addIngredientsAtRuntime(OBBIngredientTypes.TEMPERATURE, Arrays.asList(Temperature.values()));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(OBBIngredientTypes.TEMPERATURE,
                Arrays.asList(Temperature.values()),
                TemperatureHelper.INSTANCE,
                TemperatureRenderer.INSTANCE,
                StringRepresentable.fromEnum(Temperature::values)
        );
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelpers = registration.getJeiHelpers();

        registration.addRecipeCategories(
                new TemperedJarCategory(),
                new TemperatureSourceCategory(),
                new DripperCategory(),
                new HammerCategory(),
                new FusingMachineCategory(),
                new SuperCoolerCategory(),
                new CrookCategory()
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        addRecipeType(registration, RecipesRegistry.TEMPERED_JAR_TYPE.get(), RecipeTypes.TEMPERED_JAR, TemperedJarCategory::sortRecipes);
        addRecipeType(registration, RecipesRegistry.TEMPERATURE_SOURCE_TYPE.get(), RecipeTypes.TEMPERATURE_SOURCE, TemperatureSourceRecipe::sortRecipes);
        addRecipeType(registration, RecipesRegistry.DRIP_TYPE.get(), RecipeTypes.DRIPPER);
        addRecipeType(registration, RecipesRegistry.CROOK_TYPE.get(), RecipeTypes.CROOK);
        addRecipeType(registration, RecipesRegistry.HAMMER_TYPE.get(), RecipeTypes.HAMMER);
        addRecipeType(registration, RecipesRegistry.FUSING_MACHINE_TYPE.get(), RecipeTypes.FUSING_MACHINE);
        addRecipeType(registration, RecipesRegistry.SUPER_COOLER_TYPE.get(), RecipeTypes.SUPER_COOLER);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ItemsRegistry.TEMPERED_JAR.toStack(), RecipeTypes.TEMPERED_JAR);
        registration.addRecipeCatalyst(ItemsRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE.toStack(), RecipeTypes.TEMPERATURE_SOURCE);
        registration.addRecipeCatalyst(ItemsRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE.toStack(), RecipeTypes.TEMPERATURE_SOURCE);
        registration.addRecipeCatalyst(ItemsRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE.toStack(), RecipeTypes.TEMPERATURE_SOURCE);
        registration.addRecipeCatalyst(ItemsRegistry.CROOK.toStack(), RecipeTypes.CROOK);
        registration.addRecipeCatalyst(ItemsRegistry.DRIPPER.toStack(), RecipeTypes.DRIPPER);
        registration.addRecipeCatalyst(ItemsRegistry.FUSING_MACHINE.toStack(), RecipeTypes.FUSING_MACHINE);
        registration.addRecipeCatalyst(ItemsRegistry.SUPER_COOLER.toStack(), RecipeTypes.SUPER_COOLER);

        for (var item : List.of(ItemsRegistry.STONE_HAMMER, ItemsRegistry.IRON_HAMMER, ItemsRegistry.GOLD_HAMMER, ItemsRegistry.DIAMOND_HAMMER, ItemsRegistry.NETHERITE_HAMMER)) {
            registration.addRecipeCatalyst(item.toStack(), RecipeTypes.HAMMER);
        }
        for (var block : List.of(BlocksRegistry.IRON_AUTO_HAMMER, BlocksRegistry.GOLD_AUTO_HAMMER, BlocksRegistry.DIAMOND_AUTO_HAMMER, BlocksRegistry.NETHERITE_AUTO_HAMMER)) {
            registration.addRecipeCatalyst(block.toStack(), RecipeTypes.HAMMER);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(TemperedJarScreen.class, TemperedJarCategory.ContainerHandler.INSTANCE);
        registration.addGuiContainerHandler(FusingMachineScreen.class, FusingMachineCategory.ContainerHandler.INSTANCE);
        registration.addGuiContainerHandler(SuperCoolerScreen.class, SuperCoolerCategory.ContainerHandler.INSTANCE);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return FTBOBB.id("jei_plugin");
    }

    private <I extends RecipeInput, T extends Recipe<I>> void addRecipeType(IRecipeRegistration registration, net.minecraft.world.item.crafting.RecipeType<T> mcRecipeType, RecipeType<T> jeiRecipeType) {
        addRecipeType(registration, mcRecipeType, jeiRecipeType, Function.identity());
    }

    private <I extends RecipeInput, T extends Recipe<I>> void addRecipeType(IRecipeRegistration registration, net.minecraft.world.item.crafting.RecipeType<T> mcRecipeType, RecipeType<T> jeiRecipeType, Function<List<T>, List<T>> postProcessor) {
        List<T> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(mcRecipeType).stream()
                .map(RecipeHolder::value)
                .filter(IHideableRecipe::shouldShow)
                .toList();
        registration.addRecipes(jeiRecipeType, postProcessor.apply(recipes));
    }
}
