package dev.ftb.mods.ftbobb.integration.kubejs;

import dev.ftb.mods.ftbobb.temperature.Temperature;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public interface JarRecipeSchema {
    RecipeKey<List<SizedIngredient>> INPUT_ITEMS = SizedIngredientComponent.FLAT.asList().inputKey("input_items");
    RecipeKey<List<SizedFluidIngredient>> INPUT_FLUIDS = SizedFluidIngredientComponent.FLAT.asList().inputKey("input_fluids");
    RecipeKey<List<ItemStack>> OUTPUT_ITEMS = ItemStackComponent.STRICT_ITEM_STACK.asList().outputKey("output_items");
    RecipeKey<List<FluidStack>> OUTPUT_FLUIDS = FluidStackComponent.FLUID_STACK.asList().outputKey("output_fluids");
    RecipeKey<Temperature> TEMPERATURE = EnumComponent.of("temperature", Temperature.class, StringRepresentable.fromEnum(Temperature::values)).otherKey("temperature");
    RecipeKey<Integer> TIME = NumberComponent.INT.key("time", ComponentRole.OTHER).optional(200);
    RecipeKey<Boolean> CAN_REPEAT = BooleanComponent.BOOLEAN.key("can_repeat", ComponentRole.OTHER).optional(true);
    RecipeKey<String> STAGE = StringComponent.ANY.key("stage", ComponentRole.OTHER).optional("");

    RecipeSchema SCHEMA = new RecipeSchema(INPUT_ITEMS, INPUT_FLUIDS, OUTPUT_ITEMS, OUTPUT_FLUIDS, TEMPERATURE, TIME, CAN_REPEAT, STAGE);
}
