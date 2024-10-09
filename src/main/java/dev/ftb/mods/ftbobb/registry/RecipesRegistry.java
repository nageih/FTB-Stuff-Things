package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.recipes.*;
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

    public static final Supplier<RecipeType<JarRecipe>> TEMPERED_JAR_TYPE
            = registerType("jar", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<JarRecipe>> TEMPERED_JAR_SERIALIZER
            = RECIPE_SERIALIZERS.register("jar", () -> new JarRecipe.Serializer<>(JarRecipe::new));

    public static final Supplier<RecipeType<TemperatureSourceRecipe>> TEMPERATURE_SOURCE_TYPE
            = registerType("temperature_source", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<TemperatureSourceRecipe>> TEMPERATURE_SOURCE_SERIALIZER
            = RECIPE_SERIALIZERS.register("temperature_source", () -> new TemperatureSourceRecipe.Serializer<>(TemperatureSourceRecipe::new));

    public static final Supplier<RecipeType<DripperRecipe>> DRIP_TYPE
            = registerType("dripper", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<DripperRecipe>> DRIP_SERIALIZER
            = RECIPE_SERIALIZERS.register("dripper", () -> new DripperRecipe.Serializer<>(DripperRecipe::new));

    public static final Supplier<RecipeType<CrookRecipe>> CROOK_TYPE
            = registerType("crook", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<CrookRecipe>> CROOK_SERIALIZER
            = RECIPE_SERIALIZERS.register("crook", () -> new CrookRecipe.Serializer<>(CrookRecipe::new));

    public static final Supplier<RecipeType<HammerRecipe>> HAMMER_TYPE
            = registerType("hammer", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<HammerRecipe>> HAMMER_SERIALIZER
            = RECIPE_SERIALIZERS.register("hammer", () -> new HammerRecipe.Serializer<>(HammerRecipe::new));

    public static final Supplier<RecipeType<FusingMachineRecipe>> FUSING_MACHINE_TYPE
            = registerType("fusing_machine", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<FusingMachineRecipe>> FUSING_MACHINE_SERIALIZER
            = RECIPE_SERIALIZERS.register("fusing_machine", () -> new FusingMachineRecipe.Serializer<>(FusingMachineRecipe::new));

    public static final Supplier<RecipeType<SuperCoolerRecipe>> SUPER_COOLER_TYPE
            = registerType("supercooler", FTBOBBRecipeType::new);
    public static final Supplier<RecipeSerializer<SuperCoolerRecipe>> SUPER_COOLER_SERIALIZER
            = RECIPE_SERIALIZERS.register("supercooler", () -> new SuperCoolerRecipe.Serializer<>(SuperCoolerRecipe::new));

    // ---------------------------------------------

    public static void init(IEventBus bus) {
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }

    private static <T extends RecipeType<?>> Supplier<T> registerType(String name, Function<String, T> factory) {
        return RECIPE_TYPES.register(name, () -> factory.apply(name));
    }
}
