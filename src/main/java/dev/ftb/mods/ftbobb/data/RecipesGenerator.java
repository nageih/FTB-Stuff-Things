package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.data.recipe.TemperatureSourceRecipeBuilder;
import dev.ftb.mods.ftbobb.data.recipe.TemperedJarRecipeBuilder;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipesGenerator extends RecipeProvider {
    public RecipesGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        shaped(ItemsRegistry.TEMPERED_JAR.get(), Items.GLASS,
                " I /G G/GGG",
                'I', Items.IRON_INGOT,
                'G', Items.GLASS
        ).save(output);

        temperedJarRecipes(output);
        temperatureSourceRecipes(output);
    }

    private void temperedJarRecipes(RecipeOutput output) {
        // TODO temporary recipe for testing purposes

        temperedJar(List.of(SizedIngredient.of(Tags.Items.COBBLESTONES, 4)), List.of(),
                List.of(), List.of(new FluidStack(Fluids.LAVA, 10)),
                Temperature.SUPERHEATED
        ).save(output, FTBOBB.id("jar/cobble_to_lava"));

        temperedJar(List.of(SizedIngredient.of(Tags.Items.DUSTS_REDSTONE, 1)), List.of(SizedFluidIngredient.of(Fluids.WATER, 1000)),
                List.of(), List.of(new FluidStack(Fluids.LAVA, 1000)),
                Temperature.NORMAL
        ).save(output, FTBOBB.id("jar/redstone_to_lava"));

        temperedJar(List.of(), List.of(SizedFluidIngredient.of(Fluids.WATER, 1000), SizedFluidIngredient.of(Fluids.LAVA, 1000)),
                List.of(new ItemStack(Items.OBSIDIAN)), List.of(),
                Temperature.NORMAL, 60
        ).save(output, FTBOBB.id("jar/obsidian"));
    }

    private void temperatureSourceRecipes(RecipeOutput output) {
        new TemperatureSourceRecipeBuilder(Blocks.STONE.defaultBlockState(), Temperature.NORMAL, 1.0)
                .withDisplayItem(Util.make(Blocks.STONE.asItem().getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Component.translatable("ftbobb.any_block"))))
                .save(output, FTBOBB.id("temperature_source/any_block"));
        new TemperatureSourceRecipeBuilder(Blocks.BEACON.defaultBlockState(), Temperature.SUPERHEATED, 4.0)
                .save(output, FTBOBB.id("temperature_source/beacon"));
        new TemperatureSourceRecipeBuilder(Blocks.BLUE_ICE.defaultBlockState(), Temperature.CHILLED, 4.0)
                .save(output, FTBOBB.id("temperature_source/blue_ice"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.BLUE_MAGMA_BLOCK.get().defaultBlockState(), Temperature.SUPERHEATED, 4.0)
                .save(output, FTBOBB.id("temperature_source/blue_magma_block"));
        new TemperatureSourceRecipeBuilder(Blocks.CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, true), Temperature.HOT, 0.5)
                .save(output, FTBOBB.id("temperature_source/campfire"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_HIGH_TEMPERATURE_SOURCE.get().defaultBlockState(), Temperature.SUPERHEATED, 100.0)
                .save(output, FTBOBB.id("temperature_source/creative_high"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_LOW_TEMPERATURE_SOURCE.get().defaultBlockState(), Temperature.HOT, 100.0)
                .save(output, FTBOBB.id("temperature_source/creative_low"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_SUBZERO_TEMPERATURE_SOURCE.get().defaultBlockState(), Temperature.CHILLED, 100.0)
                .save(output, FTBOBB.id("temperature_source/creative_subzero"));
        new TemperatureSourceRecipeBuilder(Blocks.CRYING_OBSIDIAN.defaultBlockState(), Temperature.NORMAL, 3.0)
                .save(output, FTBOBB.id("temperature_source/crying_obsidian"));
        new TemperatureSourceRecipeBuilder(Blocks.FIRE.defaultBlockState(), Temperature.HOT, 0.75)
                .withDisplayItem(Util.make(Items.FLINT_AND_STEEL.getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Blocks.FIRE.getName())))
                .save(output, FTBOBB.id("temperature_source/fire"));
        new TemperatureSourceRecipeBuilder(Blocks.GLOWSTONE.defaultBlockState(), Temperature.NORMAL, 1.25)
                .save(output, FTBOBB.id("temperature_source/glowstone"));
        new TemperatureSourceRecipeBuilder(Blocks.ICE.defaultBlockState(), Temperature.CHILLED, 0.25)
                .save(output, FTBOBB.id("temperature_source/ice"));
        new TemperatureSourceRecipeBuilder(Blocks.LAVA.defaultBlockState(), Temperature.HOT, 4.0)
                .withDisplayItem(Items.LAVA_BUCKET.getDefaultInstance())
                .save(output, FTBOBB.id("temperature_source/lava"));
        new TemperatureSourceRecipeBuilder(Blocks.MAGMA_BLOCK.defaultBlockState(), Temperature.HOT, 1.0)
                .save(output, FTBOBB.id("temperature_source/magma_block"));
        new TemperatureSourceRecipeBuilder(Blocks.PACKED_ICE.defaultBlockState(), Temperature.CHILLED, 1.0)
                .save(output, FTBOBB.id("temperature_source/packed_ice"));
        new TemperatureSourceRecipeBuilder(Blocks.RESPAWN_ANCHOR.defaultBlockState(), Temperature.NORMAL, 8.0)
                .save(output, FTBOBB.id("temperature_source/respawn_anchor"));
        new TemperatureSourceRecipeBuilder(Blocks.SOUL_CAMPFIRE.defaultBlockState().setValue(BlockStateProperties.LIT, true), Temperature.SUPERHEATED, 0.5)
                .save(output, FTBOBB.id("temperature_source/soul_campfire"));
        new TemperatureSourceRecipeBuilder(Blocks.SOUL_FIRE.defaultBlockState(), Temperature.SUPERHEATED, 0.75)
                .withDisplayItem(Util.make(Items.FLINT_AND_STEEL.getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Blocks.SOUL_FIRE.getName())))
                .save(output, FTBOBB.id("temperature_source/soul_fire"));
        new TemperatureSourceRecipeBuilder(Blocks.TORCH.defaultBlockState(), Temperature.HOT, 0.25)
                .save(output, FTBOBB.id("temperature_source/torch"));
        new TemperatureSourceRecipeBuilder(Blocks.WALL_TORCH.defaultBlockState(), Temperature.HOT, 0.25)
                .save(output, FTBOBB.id("temperature_source/wall_torch"));
    }

    private RecipeBuilder temperedJar(List<SizedIngredient> itemsIn, List<SizedFluidIngredient> fluidsIn, List<ItemStack> itemsOut, List<FluidStack> fluidsOut, Temperature requiredTemp) {
        return new TemperedJarRecipeBuilder(itemsIn, fluidsIn, itemsOut, fluidsOut, requiredTemp);
//                .unlockedBy(getHasName(BlocksRegistry.TEMPERED_JAR.get()), has(BlocksRegistry.TEMPERED_JAR.get()));;
    }

    private RecipeBuilder temperedJar(List<SizedIngredient> itemsIn, List<SizedFluidIngredient> fluidsIn, List<ItemStack> itemsOut, List<FluidStack> fluidsOut, Temperature requiredTemp, int time) {
        return new TemperedJarRecipeBuilder(itemsIn, fluidsIn, itemsOut, fluidsOut, requiredTemp).withTime(time);
//                .unlockedBy(getHasName(BlocksRegistry.TEMPERED_JAR.get()), has(BlocksRegistry.TEMPERED_JAR.get()));;
    }

    private <T extends ItemLike> ShapedRecipeBuilder shaped(T result, T required, String pattern, Object... keys) {
        return shaped(result, 1, required, pattern, keys);
    }

    private <T extends ItemLike> ShapedRecipeBuilder shaped(T result, int count, T required, String pattern, Object... keys) {
        ShapedRecipeBuilder b = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, count);
        Arrays.stream(pattern.split("/")).forEach(b::pattern);
        for (int i = 0; i < keys.length; i += 2) {
            Object v = keys[i + 1];
            switch (v) {
                case TagKey<?> tagKey ->
                    //noinspection unchecked
                        b.define((Character) keys[i], (TagKey<Item>) v);
                case ItemLike itemLike -> b.define((Character) keys[i], itemLike);
                case Ingredient ingredient -> b.define((Character) keys[i], ingredient);
                case null, default -> throw new IllegalArgumentException("bad type for recipe ingredient " + v);
            }
        }
        b.unlockedBy("has_" + safeName(required), has(required));
        return b;
    }

    private <T extends ItemLike> ShapelessRecipeBuilder shapeless(T result, T required, Object... ingredients) {
        return shapeless(result, 1, required, ingredients);
    }

    private <T extends ItemLike> ShapelessRecipeBuilder shapeless(T result, int count, T required, Object... ingredients) {
        ShapelessRecipeBuilder b = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count);
        for (Object v : ingredients) {
            switch (v) {
                case TagKey<?> tagKey ->
                    //noinspection unchecked
                        b.requires((TagKey<Item>) v);
                case ItemLike itemLike -> b.requires(itemLike);
                case Ingredient ingredient -> b.requires(ingredient);
                case null, default -> throw new IllegalArgumentException("bad type for recipe ingredient " + v);
            }
        }
        b.unlockedBy("has_" + safeName(required), has(required));
        return b;
    }

    private <T extends ItemLike> String safeName(T itemLike) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(itemLike.asItem());
        return key.getPath().replace('/', '_');
    }
}
