package dev.ftb.mods.ftbobb.recipes;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import dev.ftb.mods.ftbobb.temperature.TemperatureAndEfficiency;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public class TemperatureSourceRecipe implements Recipe<NoInventory> {
    private final BlockState blockState;
    private final TemperatureAndEfficiency temperatureAndEfficiency;
    private final ItemStack stack;
    private final boolean hideFromJEI;

    public TemperatureSourceRecipe(BlockState blockState, Temperature temperature, double efficiency, ItemStack stack, boolean hideFromJEI) {
        this.blockState = blockState;
        this.temperatureAndEfficiency = new TemperatureAndEfficiency(temperature, efficiency);
        this.stack = stack;
        this.hideFromJEI = hideFromJEI;
    }

    @Override
    public boolean matches(NoInventory input, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(NoInventory input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.TEMPERATURE_SOURCE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipesRegistry.TEMPERATURE_SOURCE_TYPE.get();
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public Temperature getTemperature() {
        return temperatureAndEfficiency.temperature();
    }

    public double getEfficiency() {
        return temperatureAndEfficiency.efficiency();
    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean hideFromJEI() {
        return hideFromJEI;
    }

    public TemperatureAndEfficiency getTemperatureAndEfficiency() {
        return temperatureAndEfficiency;
    }

    public interface IFactory<T extends TemperatureSourceRecipe> {
        T create(BlockState blockState, Temperature temperature, double efficiency, ItemStack stack, boolean hideFromJEI);
    }

    public boolean test(BlockState state) {
        if (blockState.getBlock() == Blocks.AIR || blockState.getBlock() != state.getBlock()) {
            return false;
        }

        return blockState.getValues().entrySet().stream()
                .allMatch(entry -> state.getValue(entry.getKey()).equals(entry.getValue()));
    }

    public static class Serializer<T extends TemperatureSourceRecipe> implements RecipeSerializer<T> {
        private static final Codec<BlockState> BLOCKSTATE_STRING_CODEC = Codec.STRING.comapFlatMap(
                string -> {
                    try {
                        return DataResult.success(BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), string, false).blockState());
                    } catch (CommandSyntaxException e) {
                        return DataResult.error(() -> "invalid blockstate definition: " + string);
                    }
                },
                BlockStateParser::serialize
        );

        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf,T> streamCodec;

        public Serializer(IFactory<T> factory) {
            this.codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    BLOCKSTATE_STRING_CODEC.fieldOf("blockstate")
                            .forGetter(TemperatureSourceRecipe::getBlockState),
                    StringRepresentable.fromEnum(Temperature::values).optionalFieldOf("temperature", Temperature.NONE)
                            .forGetter(TemperatureSourceRecipe::getTemperature),
                    Codec.DOUBLE.optionalFieldOf("efficiency", 1.0)
                            .forGetter(TemperatureSourceRecipe::getEfficiency),
                    ItemStack.OPTIONAL_CODEC.fieldOf("item")
                            .forGetter(TemperatureSourceRecipe::getStack),
                    Codec.BOOL.optionalFieldOf("hide_from_jei", false)
                            .forGetter(TemperatureSourceRecipe::hideFromJEI)
            ).apply(builder, factory::create));

            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.fromCodec(BLOCKSTATE_STRING_CODEC), TemperatureSourceRecipe::getBlockState,
                    NeoForgeStreamCodecs.enumCodec(Temperature.class), TemperatureSourceRecipe::getTemperature,
                    ByteBufCodecs.DOUBLE, TemperatureSourceRecipe::getEfficiency,
                    ItemStack.OPTIONAL_STREAM_CODEC, TemperatureSourceRecipe::getStack,
                    ByteBufCodecs.BOOL, TemperatureSourceRecipe::hideFromJEI,
                    factory::create
            );
        }

        @Override
        public MapCodec<T> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return streamCodec;
        }
    }
}
