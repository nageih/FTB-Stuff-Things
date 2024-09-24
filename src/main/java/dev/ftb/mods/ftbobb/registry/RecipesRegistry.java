package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.recipes.FTBOBBRecipeType;
import dev.ftb.mods.ftbobb.recipes.JarRecipe;
import dev.ftb.mods.ftbobb.recipes.TemperatureSourceRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class RecipesRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS
            = DeferredRegister.create(Registries.RECIPE_SERIALIZER, FTBOBB.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES
            = DeferredRegister.create(Registries.RECIPE_TYPE, FTBOBB.MODID);

    // ---------------------------------------------

    public static final Supplier<RecipeType<JarRecipe>> JAR_TYPE
            = registerType("jar", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<JarRecipe>> JAR_SERIALIZER
            = RECIPE_SERIALIZERS.register("jar", () -> new JarRecipe.Serializer<>(JarRecipe::new));

    public static final Supplier<RecipeType<TemperatureSourceRecipe>> TEMPERATURE_SOURCE_TYPE
            = registerType("temperature_source", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<TemperatureSourceRecipe>> TEMPERATURE_SOURCE_SERIALIZER
            = RECIPE_SERIALIZERS.register("temperature_source", () -> new TemperatureSourceRecipe.Serializer<>(TemperatureSourceRecipe::new));


    // ---------------------------------------------

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }

    private static <T extends RecipeType<?>> Supplier<T> registerType(String name, Function<String, T> factory) {
        return RECIPE_TYPES.register(name, () -> factory.apply(name));
    }
}
