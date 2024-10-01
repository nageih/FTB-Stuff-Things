package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.FTBOBBTags;
import dev.ftb.mods.ftbobb.data.recipe.DripperRecipeBuilder;
import dev.ftb.mods.ftbobb.data.recipe.TemperatureSourceRecipeBuilder;
import dev.ftb.mods.ftbobb.data.recipe.TemperedJarRecipeBuilder;
import dev.ftb.mods.ftbobb.recipes.DevEnvironmentCondition;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import net.minecraft.Util;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
        // cast iron nuggets/ingots/blocks/etc.
        shaped(ItemsRegistry.CAST_IRON_INGOT.get(), Items.IRON_INGOT,
                "NNN/NNN/NNN",
                'N', FTBOBBTags.Items.NUGGETS_CAST_IRON
        ).save(output, FTBOBB.id("cast_iron_ingot_from_nugget"));
        shaped(BlocksRegistry.CAST_IRON_BLOCK.get(), ItemsRegistry.CAST_IRON_INGOT.get(),
                "III/III/III",
                'I', FTBOBBTags.Items.INGOTS_CAST_IRON
        ).save(output);
        shaped(ItemsRegistry.CAST_IRON_GEAR.get(), ItemsRegistry.CAST_IRON_INGOT.get(),
                " I /I I/ I ",
                'I', FTBOBBTags.Items.INGOTS_CAST_IRON
        ).save(output);
        shapeless(ItemsRegistry.CAST_IRON_INGOT.get(), 9, BlocksRegistry.CAST_IRON_BLOCK.get())
                .save(output, FTBOBB.id("cast_iron_ingot_from_block"));
        shapeless(ItemsRegistry.CAST_IRON_NUGGET.get(), 9, ItemsRegistry.CAST_IRON_INGOT.get())
                .save(output);
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC,
                ItemsRegistry.CAST_IRON_INGOT.get(), 0.1f, 600
        ).unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON)).save(output, FTBOBB.id("cast_iron_ingot_from_campfire"));

        // tempered glass
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Tags.Items.GLASS_PANES), RecipeCategory.MISC,
                ItemsRegistry.TEMPERED_GLASS.get(), 0.1f, 600
        ).unlockedBy("has_glass", has(Tags.Items.GLASS_PANES)).save(output, FTBOBB.id("tempered_glass_from_campfire"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Tags.Items.GLASS_PANES), RecipeCategory.MISC,
                ItemsRegistry.TEMPERED_GLASS.get(), 0.1f, 200
        ).unlockedBy("has_glass", has(Tags.Items.GLASS_PANES)).save(output, FTBOBB.id("tempered_glass_from_furnace"));

        // jars
        shaped(ItemsRegistry.TEMPERED_JAR.get(), ItemsRegistry.TEMPERED_GLASS,
                "GIG/G G/GGG",
                'I', ItemsRegistry.CAST_IRON_INGOT,
                'G', ItemsRegistry.TEMPERED_GLASS
        ).save(output);
        shaped(ItemsRegistry.JAR.get(), Items.GLASS,
                "GBG/G G/GGG",
                'B', ItemTags.WOODEN_BUTTONS,
                'G', Tags.Items.GLASS_PANES
        ).save(output);

        shaped(BlocksRegistry.AUTO_PROCESSING_BLOCK.get(), ItemsRegistry.TEMPERED_JAR.get(),
                "CDC/CPC/CHC",
                'C', ItemsRegistry.CAST_IRON_INGOT.get(),
                'D', Blocks.DROPPER,
                'P', Blocks.PISTON,
                'H', Blocks.HOPPER
        ).save(output);
        shaped(BlocksRegistry.BLUE_MAGMA_BLOCK.get(), Blocks.MAGMA_BLOCK,
                "SMS/MLM/SMS",
                'S', Blocks.SOUL_SAND,
                'L', Items.LAVA_BUCKET,
                'M', Blocks.MAGMA_BLOCK
        ).save(output);
        shaped(BlocksRegistry.DRIPPER.get(), Items.STICK,
                "SPS/SBS",
                'S', Tags.Items.RODS_WOODEN,
                'P', ItemTags.WOODEN_SLABS,
                'B', ItemTags.WOODEN_BUTTONS
        ).save(output);

        temperedJarRecipes(output);
        temperatureSourceRecipes(output);
        dripperRecipes(output);
    }

    private void temperedJarRecipes(RecipeOutput output) {
        // testing recipes; note the use of DevEnvironmentCondition

        temperedJar(List.of(SizedIngredient.of(Tags.Items.COBBLESTONES, 4)), List.of(),
                List.of(), List.of(new FluidStack(Fluids.LAVA, 10)),
                Temperature.SUPERHEATED
        ).save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("jar/cobble_to_lava"));

        temperedJar(
                List.of(SizedIngredient.of(Tags.Items.DUSTS_REDSTONE, 1)),
                List.of(SizedFluidIngredient.of(Fluids.WATER, 1000)),
                List.of(),
                List.of(new FluidStack(Fluids.LAVA, 1000)),
                Temperature.HOT
        ).save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("jar/redstone_to_lava"));
        temperedJar(
                List.of(SizedIngredient.of(Tags.Items.DUSTS_REDSTONE, 1), SizedIngredient.of(Tags.Items.DUSTS_GLOWSTONE, 1)),
                List.of(SizedFluidIngredient.of(Fluids.WATER, 1000)),
                List.of(),
                List.of(new FluidStack(Fluids.LAVA, 2000)),
                Temperature.HOT
        ).save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("jar/redstone_glowstone_to_lava"));

        temperedJar(List.of(), List.of(SizedFluidIngredient.of(Fluids.WATER, 1000), SizedFluidIngredient.of(Fluids.LAVA, 1000)),
                List.of(new ItemStack(Items.OBSIDIAN)), List.of(),
                Temperature.NORMAL, 60
        ).save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("jar/obsidian"));

        temperedJar(List.of(SizedIngredient.of(Items.SUGAR, 1)), List.of(SizedFluidIngredient.of(Fluids.WATER, 1000)),
                List.of(new ItemStack(Blocks.GLASS, 4)), List.of(),
                Temperature.CHILLED
        ).save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("jar/sugar_glass"));
    }

    private void temperatureSourceRecipes(RecipeOutput output) {
        new TemperatureSourceRecipeBuilder(Blocks.STONE, Temperature.NORMAL, 1.0)
                .withDisplayItem(Util.make(Blocks.STONE.asItem().getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Component.translatable("ftbobb.any_block"))))
                .save(output, FTBOBB.id("temperature_source/any_block"));
        new TemperatureSourceRecipeBuilder(Blocks.BEACON, Temperature.SUPERHEATED, 4.0)
                .save(output, FTBOBB.id("temperature_source/beacon"));
        new TemperatureSourceRecipeBuilder(Blocks.BLUE_ICE, Temperature.CHILLED, 4.0)
                .save(output, FTBOBB.id("temperature_source/blue_ice"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.BLUE_MAGMA_BLOCK.get(), Temperature.SUPERHEATED, 4.0)
                .save(output, FTBOBB.id("temperature_source/blue_magma_block"));
        new TemperatureSourceRecipeBuilder("minecraft:campfire[lit=true]", Temperature.HOT, 0.5)
                .save(output, FTBOBB.id("temperature_source/campfire"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE.get(), Temperature.SUPERHEATED, 100.0)
                .hideFromJEI()
                .save(output, FTBOBB.id("temperature_source/creative_high"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE.get(), Temperature.HOT, 100.0)
                .hideFromJEI()
                .save(output, FTBOBB.id("temperature_source/creative_low"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE.get(), Temperature.CHILLED, 100.0)
                .hideFromJEI()
                .save(output, FTBOBB.id("temperature_source/creative_subzero"));
        new TemperatureSourceRecipeBuilder(Blocks.CRYING_OBSIDIAN, Temperature.NORMAL, 3.0)
                .save(output, FTBOBB.id("temperature_source/crying_obsidian"));
        new TemperatureSourceRecipeBuilder(Blocks.FIRE, Temperature.HOT, 0.75)
                .withDisplayItem(Util.make(Items.FLINT_AND_STEEL.getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Blocks.FIRE.getName())))
                .save(output, FTBOBB.id("temperature_source/fire"));
        new TemperatureSourceRecipeBuilder(Blocks.GLOWSTONE, Temperature.NORMAL, 1.25)
                .save(output, FTBOBB.id("temperature_source/glowstone"));
        new TemperatureSourceRecipeBuilder(Blocks.ICE, Temperature.CHILLED, 0.25)
                .save(output, FTBOBB.id("temperature_source/ice"));
        new TemperatureSourceRecipeBuilder(Blocks.LAVA, Temperature.HOT, 4.0)
                .withDisplayItem(Items.LAVA_BUCKET.getDefaultInstance())
                .save(output, FTBOBB.id("temperature_source/lava"));
        new TemperatureSourceRecipeBuilder(Blocks.MAGMA_BLOCK, Temperature.HOT, 1.0)
                .save(output, FTBOBB.id("temperature_source/magma_block"));
        new TemperatureSourceRecipeBuilder(Blocks.PACKED_ICE, Temperature.CHILLED, 1.0)
                .save(output, FTBOBB.id("temperature_source/packed_ice"));
        new TemperatureSourceRecipeBuilder(Blocks.RESPAWN_ANCHOR, Temperature.NORMAL, 8.0)
                .save(output, FTBOBB.id("temperature_source/respawn_anchor"));
        new TemperatureSourceRecipeBuilder("minecraft:soul_campfire[lit=true]", Temperature.SUPERHEATED, 0.5)
                .save(output, FTBOBB.id("temperature_source/soul_campfire"));
        new TemperatureSourceRecipeBuilder(Blocks.SOUL_FIRE, Temperature.SUPERHEATED, 0.75)
                .withDisplayItem(Util.make(Items.FLINT_AND_STEEL.getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Blocks.SOUL_FIRE.getName())))
                .save(output, FTBOBB.id("temperature_source/soul_fire"));
        new TemperatureSourceRecipeBuilder(Blocks.TORCH, Temperature.HOT, 0.25)
                .save(output, FTBOBB.id("temperature_source/torch"));
        new TemperatureSourceRecipeBuilder(Blocks.WALL_TORCH, Temperature.HOT, 0.25)
                .hideFromJEI() // looks identical to torch
                .save(output, FTBOBB.id("temperature_source/wall_torch"));
    }

    private void dripperRecipes(RecipeOutput output) {
        new DripperRecipeBuilder(stateStr(Blocks.DIRT), stateStr(Blocks.MUD), new FluidStack(Fluids.WATER, 50))
                .withChance(0.2)
                .save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("dripper/dirt_to_mud"));
        new DripperRecipeBuilder(stateStr(Blocks.IRON_BLOCK), stateStr(Blocks.GOLD_BLOCK), new FluidStack(Fluids.LAVA, 250))
                .withChance(0.01)
                .save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("dripper/iron_to_gold"));
        new DripperRecipeBuilder(stateStr(Blocks.SAND), stateStr(Blocks.CLAY), new FluidStack(Fluids.WATER, 5))
                .withChance(0.1)
                .consumeFluidOnFail()
                .save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("dripper/sand_to_clay"));
        new DripperRecipeBuilder("minecraft:campfire[lit=false]", "minecraft:campfire[lit=true]", new FluidStack(Fluids.LAVA, 250))
                .withChance(0.5)
                .save(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBOBB.id("dripper/campfire_lighting"));
    }

    private static RecipeBuilder temperedJar(List<SizedIngredient> itemsIn, List<SizedFluidIngredient> fluidsIn, List<ItemStack> itemsOut, List<FluidStack> fluidsOut, Temperature requiredTemp) {
        return new TemperedJarRecipeBuilder(itemsIn, fluidsIn, itemsOut, fluidsOut, requiredTemp);
    }

    private static RecipeBuilder temperedJar(List<SizedIngredient> itemsIn, List<SizedFluidIngredient> fluidsIn, List<ItemStack> itemsOut, List<FluidStack> fluidsOut, Temperature requiredTemp, int time) {
        return new TemperedJarRecipeBuilder(itemsIn, fluidsIn, itemsOut, fluidsOut, requiredTemp).withTime(time);
    }

    private static <T extends ItemLike> ShapedRecipeBuilder shaped(T result, T required, String pattern, Object... keys) {
        return shaped(result, 1, required, pattern, keys);
    }

    private static <T extends ItemLike> ShapedRecipeBuilder shaped(T result, int count, T required, String pattern, Object... keys) {
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

    private static <T extends ItemLike> ShapelessRecipeBuilder shapeless(T result, T required, Object... ingredients) {
        return shapeless(result, 1, required, ingredients);
    }

    private static <T extends ItemLike> ShapelessRecipeBuilder shapeless(T result, int count, T required, Object... ingredients) {
        return _shapeless(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count), required, ingredients);
    }

    private <T extends ItemLike> ShapelessRecipeBuilder shapelessStack(ItemStack result, T required, Object... ingredients) {
        return _shapeless(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result), required, ingredients);
    }

    private static <T extends ItemLike> ShapelessRecipeBuilder _shapeless(ShapelessRecipeBuilder b, T required, Object... ingredients) {
        if (ingredients.length == 0) {
            ingredients = new Object[] { required };
        }
        for (Object v : ingredients) {
            switch (v) {
                case TagKey<?> ignored ->
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

    private static <T extends ItemLike> String safeName(T itemLike) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(itemLike.asItem());
        return key.getPath().replace('/', '_');
    }

    private static String stateStr(BlockState state) {
        return BlockStateParser.serialize(state);
    }

    private static String stateStr(Block block) {
        return BlockStateParser.serialize(block.defaultBlockState());
    }
}
