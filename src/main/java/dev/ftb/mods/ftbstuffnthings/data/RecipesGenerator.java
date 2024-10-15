package dev.ftb.mods.ftbstuffnthings.data;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.FTBStuffTags;
import dev.ftb.mods.ftbstuffnthings.crafting.DevEnvironmentCondition;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.data.recipe.*;
import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
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
import net.neoforged.neoforge.fluids.FluidType;
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
                'N', FTBStuffTags.Items.NUGGETS_CAST_IRON
        ).save(output, FTBStuffNThings.id("cast_iron_ingot_from_nugget"));
        shaped(BlocksRegistry.CAST_IRON_BLOCK.get(), ItemsRegistry.CAST_IRON_INGOT.get(),
                "III/III/III",
                'I', FTBStuffTags.Items.INGOTS_CAST_IRON
        ).save(output);
        shaped(ItemsRegistry.CAST_IRON_GEAR.get(), ItemsRegistry.CAST_IRON_INGOT.get(),
                " I /I I/ I ",
                'I', FTBStuffTags.Items.INGOTS_CAST_IRON
        ).save(output);
        shapeless(ItemsRegistry.CAST_IRON_INGOT.get(), 9, BlocksRegistry.CAST_IRON_BLOCK.get())
                .save(output, FTBStuffNThings.id("cast_iron_ingot_from_block"));
        shapeless(ItemsRegistry.CAST_IRON_NUGGET.get(), 9, ItemsRegistry.CAST_IRON_INGOT.get())
                .save(output);
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Tags.Items.INGOTS_IRON), RecipeCategory.MISC,
                ItemsRegistry.CAST_IRON_INGOT.get(), 0.1f, 600
        ).unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON)).save(output, FTBStuffNThings.id("cast_iron_ingot_from_campfire"));

        // tempered glass
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Tags.Items.GLASS_PANES), RecipeCategory.MISC,
                ItemsRegistry.TEMPERED_GLASS.get(), 0.1f, 600
        ).unlockedBy("has_glass", has(Tags.Items.GLASS_PANES)).save(output, FTBStuffNThings.id("tempered_glass_from_campfire"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Tags.Items.GLASS_PANES), RecipeCategory.MISC,
                ItemsRegistry.TEMPERED_GLASS.get(), 0.1f, 200
        ).unlockedBy("has_glass", has(Tags.Items.GLASS_PANES)).save(output, FTBStuffNThings.id("tempered_glass_from_furnace"));

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

        shaped(BlocksRegistry.JAR_AUTOMATER.get(), ItemsRegistry.TEMPERED_JAR.get(),
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
        shaped(ItemsRegistry.STONE_ROD.get(), Items.COBBLESTONE,
                "S/S",
                'S', Items.COBBLESTONE
        ).save(output);
        shaped(ItemsRegistry.CROOK.get(), ItemsRegistry.STONE_ROD,
                "SS/ S/ S",
                'S', ItemsRegistry.STONE_ROD
        ).save(output);

        temperedJarRecipes(output);
        temperatureSourceRecipes(output);
        dripperRecipes(output);
        crookRecipes(output);
        hammerRecipes(output);
        fusingMachineRecipes(output);
        superCoolerRecipes(output);
    }

    private void temperedJarRecipes(RecipeOutput output) {
        // testing recipes; note the use of DevEnvironmentCondition

        temperedJar(List.of(SizedIngredient.of(Tags.Items.COBBLESTONES, 4)), List.of(),
                List.of(), List.of(new FluidStack(Fluids.LAVA, 10)),
                Temperature.SUPERHEATED
        ).saveTest(output, FTBStuffNThings.id("cobble_to_lava"));

        temperedJar(
                List.of(SizedIngredient.of(Tags.Items.DUSTS_REDSTONE, 1)),
                List.of(SizedFluidIngredient.of(Fluids.WATER, 1000)),
                List.of(),
                List.of(new FluidStack(Fluids.LAVA, 1000)),
                Temperature.HOT
        ).saveTest(output, FTBStuffNThings.id("redstone_to_lava"));
        temperedJar(
                List.of(SizedIngredient.of(Tags.Items.DUSTS_REDSTONE, 1), SizedIngredient.of(Tags.Items.DUSTS_GLOWSTONE, 1)),
                List.of(SizedFluidIngredient.of(Fluids.WATER, 1000)),
                List.of(),
                List.of(new FluidStack(Fluids.LAVA, 2000)),
                Temperature.HOT
        ).saveTest(output, FTBStuffNThings.id("redstone_glowstone_to_lava"));

        temperedJar(List.of(), List.of(SizedFluidIngredient.of(Fluids.WATER, 1000), SizedFluidIngredient.of(Fluids.LAVA, 1000)),
                List.of(new ItemStack(Items.OBSIDIAN)), List.of(),
                Temperature.NORMAL, 60
        ).saveTest(output, FTBStuffNThings.id("obsidian"));

        temperedJar(List.of(SizedIngredient.of(Items.SUGAR, 1)), List.of(SizedFluidIngredient.of(Fluids.WATER, 1000)),
                List.of(new ItemStack(Blocks.GLASS, 4)), List.of(),
                Temperature.CHILLED
        ).saveTest(output, FTBStuffNThings.id("sugar_glass"));
    }

    private void temperatureSourceRecipes(RecipeOutput output) {
        new TemperatureSourceRecipeBuilder(Blocks.STONE, Temperature.NORMAL, 1.0)
                .withDisplayItem(Util.make(Blocks.STONE.asItem().getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Component.translatable("ftbstuff.any_block"))))
                .save(output, FTBStuffNThings.id("any_block"));
        new TemperatureSourceRecipeBuilder(Blocks.BEACON, Temperature.SUPERHEATED, 4.0)
                .save(output, FTBStuffNThings.id("beacon"));
        new TemperatureSourceRecipeBuilder(Blocks.BLUE_ICE, Temperature.CHILLED, 4.0)
                .save(output, FTBStuffNThings.id("blue_ice"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.BLUE_MAGMA_BLOCK.get(), Temperature.SUPERHEATED, 4.0)
                .save(output, FTBStuffNThings.id("blue_magma_block"));
        new TemperatureSourceRecipeBuilder("minecraft:campfire[lit=true]", Temperature.HOT, 0.5)
                .save(output, FTBStuffNThings.id("campfire"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE.get(), Temperature.SUPERHEATED, 100.0)
                .hideFromJEI()
                .save(output, FTBStuffNThings.id("creative_high"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE.get(), Temperature.HOT, 100.0)
                .hideFromJEI()
                .save(output, FTBStuffNThings.id("creative_low"));
        new TemperatureSourceRecipeBuilder(BlocksRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE.get(), Temperature.CHILLED, 100.0)
                .hideFromJEI()
                .save(output, FTBStuffNThings.id("creative_subzero"));
        new TemperatureSourceRecipeBuilder(Blocks.CRYING_OBSIDIAN, Temperature.NORMAL, 3.0)
                .save(output, FTBStuffNThings.id("crying_obsidian"));
        new TemperatureSourceRecipeBuilder(Blocks.FIRE, Temperature.HOT, 0.75)
                .withDisplayItem(Util.make(Items.FLINT_AND_STEEL.getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Blocks.FIRE.getName())))
                .save(output, FTBStuffNThings.id("fire"));
        new TemperatureSourceRecipeBuilder(Blocks.GLOWSTONE, Temperature.NORMAL, 1.25)
                .save(output, FTBStuffNThings.id("glowstone"));
        new TemperatureSourceRecipeBuilder(Blocks.ICE, Temperature.CHILLED, 0.25)
                .save(output, FTBStuffNThings.id("ice"));
        new TemperatureSourceRecipeBuilder(Blocks.LAVA, Temperature.HOT, 4.0)
                .withDisplayItem(Items.LAVA_BUCKET.getDefaultInstance())
                .save(output, FTBStuffNThings.id("lava"));
        new TemperatureSourceRecipeBuilder(Blocks.MAGMA_BLOCK, Temperature.HOT, 1.0)
                .save(output, FTBStuffNThings.id("magma_block"));
        new TemperatureSourceRecipeBuilder(Blocks.PACKED_ICE, Temperature.CHILLED, 1.0)
                .save(output, FTBStuffNThings.id("packed_ice"));
        new TemperatureSourceRecipeBuilder(Blocks.RESPAWN_ANCHOR, Temperature.NORMAL, 8.0)
                .save(output, FTBStuffNThings.id("respawn_anchor"));
        new TemperatureSourceRecipeBuilder("minecraft:soul_campfire[lit=true]", Temperature.SUPERHEATED, 0.5)
                .save(output, FTBStuffNThings.id("soul_campfire"));
        new TemperatureSourceRecipeBuilder(Blocks.SOUL_FIRE, Temperature.SUPERHEATED, 0.75)
                .withDisplayItem(Util.make(Items.FLINT_AND_STEEL.getDefaultInstance(),
                        stack -> stack.set(DataComponents.CUSTOM_NAME, Blocks.SOUL_FIRE.getName())))
                .save(output, FTBStuffNThings.id("soul_fire"));
        new TemperatureSourceRecipeBuilder(Blocks.TORCH, Temperature.HOT, 0.25)
                .save(output, FTBStuffNThings.id("torch"));
        new TemperatureSourceRecipeBuilder(Blocks.WALL_TORCH, Temperature.HOT, 0.25)
                .hideFromJEI() // looks identical to torch
                .save(output, FTBStuffNThings.id("wall_torch"));
    }

    private void dripperRecipes(RecipeOutput output) {
        new DripperRecipeBuilder(stateStr(Blocks.DIRT), stateStr(Blocks.MUD), new FluidStack(Fluids.WATER, 50))
                .withChance(0.2)
                .saveTest(output.withConditions(DevEnvironmentCondition.INSTANCE), FTBStuffNThings.id("dirt_to_mud"));
        new DripperRecipeBuilder(stateStr(Blocks.IRON_BLOCK), stateStr(Blocks.GOLD_BLOCK), new FluidStack(Fluids.LAVA, 250))
                .withChance(0.01)
                .saveTest(output, FTBStuffNThings.id("iron_to_gold"));
        new DripperRecipeBuilder(stateStr(Blocks.SAND), stateStr(Blocks.CLAY), new FluidStack(Fluids.WATER, 5))
                .withChance(0.1)
                .consumeFluidOnFail()
                .saveTest(output, FTBStuffNThings.id("sand_to_clay"));
        new DripperRecipeBuilder("minecraft:campfire[lit=false]", "minecraft:campfire[lit=true]", new FluidStack(Fluids.LAVA, 250))
                .withChance(0.5)
                .saveTest(output, FTBStuffNThings.id("campfire_lighting"));
    }

    private void crookRecipes(RecipeOutput output) {
        new CrookRecipeBuilder(Ingredient.of(ItemTags.LEAVES), List.of(
                new ItemWithChance(new ItemStack(Items.GOLD_NUGGET), 0.5),
                new ItemWithChance(new ItemStack(Items.IRON_NUGGET), 0.5)
        )).saveTest(output, FTBStuffNThings.id("nuggets_from_leaves"));

        new CrookRecipeBuilder(Ingredient.of(Blocks.SHORT_GRASS), List.of(
                new ItemWithChance(new ItemStack(Items.STRING), 0.5)
        )).keepExistingDrops().saveTest(output, FTBStuffNThings.id("string_from_grass"));
    }

    private void hammerRecipes(RecipeOutput output) {
        new HammerRecipeBuilder(Ingredient.of(Items.COBBLESTONE), List.of(
                new ItemStack(Blocks.GRAVEL)
        )).saveTest(output, FTBStuffNThings.id("gravel_from_cobblestone"));
        new HammerRecipeBuilder(Ingredient.of(Items.GRAVEL), List.of(
                new ItemStack(Blocks.SAND)
        )).saveTest(output, FTBStuffNThings.id("sand_from_gravel"));
        new HammerRecipeBuilder(Ingredient.of(Items.COBBLED_DEEPSLATE), List.of(
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64),
                new ItemStack(Blocks.GRAVEL, 64)
        )).saveTest(output, FTBStuffNThings.id("gravel_from_deepslate"));
    }

    private void fusingMachineRecipes(RecipeOutput output) {
        new FusingMachineRecipeBuilder(
                List.of(Ingredient.of(Items.COBBLESTONE), Ingredient.of(Items.GRAVEL)),
                new FluidStack(Fluids.LAVA, 1000),
                100, 60
        ).saveTest(output, FTBStuffNThings.id("lava_from_cobble_gravel"));
        new FusingMachineRecipeBuilder(
                List.of(Ingredient.of(Items.COBBLESTONE)),
                new FluidStack(Fluids.LAVA, 250),
                50, 40
        ).saveTest(output, FTBStuffNThings.id("lava_from_cobble"));
        new FusingMachineRecipeBuilder(
                List.of(Ingredient.of(Items.ICE)),
                new FluidStack(Fluids.WATER, 1000),
                5, 20
        ).saveTest(output, FTBStuffNThings.id("water_from_ice"));
    }

    private void superCoolerRecipes(RecipeOutput output) {
        new SuperCoolerRecipeBuilder(
                List.of(Ingredient.of(ItemTags.SAND), Ingredient.of(Tags.Items.GRAVELS), Ingredient.of(Tags.Items.DYES_WHITE)),
                SizedFluidIngredient.of(Fluids.WATER, FluidType.BUCKET_VOLUME),
                50, 20,
                new ItemStack(Items.WHITE_CONCRETE, 2)
        ).saveTest(output, FTBStuffNThings.id("white_concrete"));
        new SuperCoolerRecipeBuilder(
                List.of(Ingredient.of(Items.DIRT)),
                SizedFluidIngredient.of(Fluids.WATER, 100),
                25, 40,
                new ItemStack(Items.MUD)
        ).saveTest(output, FTBStuffNThings.id("mud"));
    }

    private static TemperedJarRecipeBuilder temperedJar(List<SizedIngredient> itemsIn, List<SizedFluidIngredient> fluidsIn, List<ItemStack> itemsOut, List<FluidStack> fluidsOut, Temperature requiredTemp) {
        return new TemperedJarRecipeBuilder(itemsIn, fluidsIn, itemsOut, fluidsOut, requiredTemp);
    }

    private static TemperedJarRecipeBuilder temperedJar(List<SizedIngredient> itemsIn, List<SizedFluidIngredient> fluidsIn, List<ItemStack> itemsOut, List<FluidStack> fluidsOut, Temperature requiredTemp, int time) {
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
