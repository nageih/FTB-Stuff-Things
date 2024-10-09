package dev.ftb.mods.ftbobb.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbobb.crafting.BaseRecipe;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class HammerRecipe extends BaseRecipe<HammerRecipe> {
    private final Ingredient ingredient;
    private final List<ItemStack> results;

    public HammerRecipe(Ingredient ingredient, List<ItemStack> results) {
        super(RecipesRegistry.HAMMER_SERIALIZER, RecipesRegistry.HAMMER_TYPE);

        this.ingredient = ingredient;
        this.results = results;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<ItemStack> getResults() {
        return results;
    }

    public interface IFactory<T extends HammerRecipe> {
        T create(Ingredient ingredient, List<ItemStack> results);
    }

    public static class Serializer<T extends HammerRecipe> implements RecipeSerializer<T> {
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
        private final MapCodec<T> codec;

        public Serializer(IFactory<T> factory) {
            this.codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(HammerRecipe::getIngredient),
                    ItemStack.CODEC.listOf().fieldOf("results").forGetter(HammerRecipe::getResults)
            ).apply(builder, factory::create));

            this.streamCodec = StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, HammerRecipe::getIngredient,
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), HammerRecipe::getResults,
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
