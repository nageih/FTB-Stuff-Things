package dev.ftb.mods.ftbstuffnthings.integration.jei;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.*;
import dev.ftb.mods.ftbstuffnthings.util.lootsummary.WrappedLootSummary;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeTypes {
    public static final RecipeType<JarRecipe> TEMPERED_JAR = register("jar", JarRecipe.class);
    public static final RecipeType<TemperatureSourceRecipe> TEMPERATURE_SOURCE = register("temperature_source", TemperatureSourceRecipe.class);
    public static final RecipeType<DripperRecipe> DRIPPER = register("dripper", DripperRecipe.class);
    public static final RecipeType<CrookRecipe> CROOK = register("crook", CrookRecipe.class);
    public static final RecipeType<HammerRecipe> HAMMER = register("hammer", HammerRecipe.class);
    public static final RecipeType<FusingMachineRecipe> FUSING_MACHINE = register("fusing_machine", FusingMachineRecipe.class);
    public static final RecipeType<SuperCoolerRecipe> SUPER_COOLER = register("super_cooler_jei", SuperCoolerRecipe.class);
    public static final RecipeType<SluiceRecipe> SLUICE = register("sluice", SluiceRecipe.class);

    // special pseudo-recipe with its own recipe manager plugin
    public static final RecipeType<WrappedLootSummary> LOOT_SUMMARY = RecipeType.create(FTBStuffNThings.MODID, "loot_summary", WrappedLootSummary.class);

    private static <T extends Recipe<?>> RecipeType<T> register(String name, Class<T> recipeClass) {
        return RecipeType.create(FTBStuffNThings.MODID, name, recipeClass);
    }
}
