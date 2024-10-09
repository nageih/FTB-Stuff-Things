package dev.ftb.mods.ftbobb.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class CrookRecipe extends BaseRecipe<CrookRecipe> {
    private final Ingredient ingredient;
    private final List<ItemWithChance> results;
    private final int max;
    private final boolean replaceDrops;

    public CrookRecipe(Ingredient ingredient, List<ItemWithChance> results, int max, boolean replaceDrops) {
        super(RecipesRegistry.CROOK_SERIALIZER, RecipesRegistry.CROOK_TYPE);

        this.ingredient = ingredient;
        this.results = results;
        this.max = max;
        this.replaceDrops = replaceDrops;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<ItemWithChance> getResults() {
        return results;
    }

    public int getMax() {
        return max;
    }

    public boolean replaceDrops() {
        return replaceDrops;
    }

    public interface IFactory<T extends CrookRecipe> {
        T create(Ingredient ingredient, List<ItemWithChance> results, int max, boolean clearDefaultDrops);
    }

    public static class Serializer<T extends CrookRecipe> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(IFactory<T> factory) {
            codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(CrookRecipe::getIngredient),
                    ItemWithChance.CODEC.listOf().fieldOf("results").forGetter(CrookRecipe::getResults),
                    Codec.INT.optionalFieldOf("max", 0).forGetter(CrookRecipe::getMax),
                    Codec.BOOL.optionalFieldOf("replace_drops", true).forGetter(CrookRecipe::replaceDrops)
            ).apply(builder, factory::create));

            streamCodec = StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, CrookRecipe::getIngredient,
                    ItemWithChance.STREAM_CODEC.apply(ByteBufCodecs.list()), CrookRecipe::getResults,
                    ByteBufCodecs.VAR_INT, CrookRecipe::getMax,
                    ByteBufCodecs.BOOL, CrookRecipe::replaceDrops,
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

    public record CrookDrops(List<ItemWithChance> items, int max, boolean replaceDrops) {
    }
}
