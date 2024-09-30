package dev.ftb.mods.ftbobb.recipes;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemperatureSourceRecipe implements Recipe<NoInventory>, IHideableRecipe {
    private final String blockStateStr;
    private final BlockState blockState;
    private final TemperatureAndEfficiency temperatureAndEfficiency;
    private final ItemStack stack;
    private final boolean hideFromJEI;
    private final Map<String,String> predicates;

    public TemperatureSourceRecipe(String blockStateStr, Temperature temperature, double efficiency, ItemStack stack, boolean hideFromJEI) {
        this.temperatureAndEfficiency = new TemperatureAndEfficiency(temperature, efficiency);
        this.stack = stack;
        this.hideFromJEI = hideFromJEI;
        this.blockStateStr = blockStateStr;
        try {
            this.blockState = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), blockStateStr, false).blockState();
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("invalid blockstate: " + blockStateStr);
        }
        this.predicates = extractPredicates(blockStateStr);
    }

    private static Map<String,String> extractPredicates(String blockStateStr) {
        Map<String,String> res = new HashMap<>();
        int l = blockStateStr.indexOf('[');
        if (l > 0) {
            String[] preds = blockStateStr.substring(l + 1, blockStateStr.length() - 1).split(",");
            for (String pred : preds) {
                String[] parts = pred.split("=");
                res.put(parts[0], parts[1]);
            }
        }
        return res;
    }

    public static @NotNull List<TemperatureSourceRecipe> sortRecipes(List<TemperatureSourceRecipe> l) {
        return l.stream().sorted(Comparator.comparing(TemperatureSourceRecipe::getTemperature)
                .thenComparing(TemperatureSourceRecipe::getEfficiency)
                .thenComparing(r -> r.getDisplayStack().getHoverName().getString())
        ).toList();
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

    public ItemStack getDisplayStack() {
        return stack.isEmpty() ? new ItemStack(blockState.getBlock()) : stack;
    }

    public boolean hideFromJEI() {
        return hideFromJEI;
    }

    public TemperatureAndEfficiency getTemperatureAndEfficiency() {
        return temperatureAndEfficiency;
    }

    @Override
    public boolean shouldShowRecipe() {
        return !hideFromJEI;
    }

    private String getBlockStateStr() {
        return blockStateStr;
    }

    public boolean test(BlockState state) {
        if (blockState.getBlock() == Blocks.AIR || blockState.getBlock() != state.getBlock()) {
            return false;
        }

        for (Map.Entry<String, String> entry : predicates.entrySet()) {
            Property<?> prop = state.getBlock().getStateDefinition().getProperty(entry.getKey());
            if (prop == null) {
                return false;
            }
            Object val = prop.getValue(entry.getValue()).orElse(null);
            if (val == null || state.getValue(prop) != val) {
                return false;
            }
        }

        return true;
    }

    public interface IFactory<T extends TemperatureSourceRecipe> {
        T create(String blockState, Temperature temperature, double efficiency, ItemStack stack, boolean hideFromJEI);
    }

    public static class Serializer<T extends TemperatureSourceRecipe> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf,T> streamCodec;

        public Serializer(IFactory<T> factory) {
            this.codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Codec.STRING.fieldOf("blockstate")
                            .forGetter(TemperatureSourceRecipe::getBlockStateStr),
                    StringRepresentable.fromEnum(Temperature::values).optionalFieldOf("temperature", Temperature.NORMAL)
                            .forGetter(TemperatureSourceRecipe::getTemperature),
                    Codec.DOUBLE.optionalFieldOf("efficiency", 1.0)
                            .forGetter(TemperatureSourceRecipe::getEfficiency),
                    ItemStack.OPTIONAL_CODEC.optionalFieldOf("display_item", ItemStack.EMPTY)
                            .forGetter(TemperatureSourceRecipe::getDisplayStack),
                    Codec.BOOL.optionalFieldOf("hide_from_jei", false)
                            .forGetter(TemperatureSourceRecipe::hideFromJEI)
            ).apply(builder, factory::create));

            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, TemperatureSourceRecipe::getBlockStateStr,
                    NeoForgeStreamCodecs.enumCodec(Temperature.class), TemperatureSourceRecipe::getTemperature,
                    ByteBufCodecs.DOUBLE, TemperatureSourceRecipe::getEfficiency,
                    ItemStack.OPTIONAL_STREAM_CODEC, TemperatureSourceRecipe::getDisplayStack,
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
