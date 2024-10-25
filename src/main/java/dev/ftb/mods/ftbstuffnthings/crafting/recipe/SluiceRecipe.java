package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbstuffnthings.crafting.BaseRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.items.MeshType;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SluiceRecipe extends BaseRecipe<SluiceRecipe> {
    private final Ingredient ingredient;
    private final List<ItemWithChance> results;
    private final int maxResults;
    private final Optional<SizedFluidIngredient> fluid;
    private final float processingTimeMultiplier;
    private final HashSet<MeshType> meshTypes;

    public SluiceRecipe(Ingredient ingredient, List<ItemWithChance> results, int maxResults, Optional<SizedFluidIngredient> fluid, float processingTimeMultiplier, List<MeshType> meshTypes) {
        super(RecipesRegistry.SLUICE_SERIALIZER, RecipesRegistry.SLUICE_TYPE);

        this.ingredient = ingredient;
        this.results = results;
        this.maxResults = maxResults;
        this.fluid = fluid;
        this.processingTimeMultiplier = processingTimeMultiplier;
        this.meshTypes = new HashSet<>(meshTypes);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<ItemWithChance> getResults() {
        return results;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public Optional<SizedFluidIngredient> getFluid() {
        return fluid;
    }

    public boolean testFluid(FluidStack toCheck, boolean checkAmount) {
        return fluid.map(ingr ->
                checkAmount ? ingr.test(toCheck) : ingr.ingredient().test(toCheck)
        ).orElse(true);
    }

    public float getProcessingTimeMultiplier() {
        return processingTimeMultiplier;
    }

    public HashSet<MeshType> getMeshTypes() {
        return meshTypes;
    }

    public List<MeshType> getMeshTypesAsList() {
        return List.copyOf(meshTypes);
    }

    public interface IFactory<T extends SluiceRecipe> {
        T create(Ingredient ingredient, List<ItemWithChance> results, int maxResults, Optional<SizedFluidIngredient> fluid, float processingTimeMultiplier, List<MeshType> meshTypes);
    }

    public static class Serializer<T extends SluiceRecipe> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(IFactory<T> factory) {
            codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(SluiceRecipe::getIngredient),
                    ItemWithChance.CODEC.listOf().fieldOf("results").forGetter(SluiceRecipe::getResults),
                    Codec.INT.optionalFieldOf("max_results", 4).forGetter(SluiceRecipe::getMaxResults),
                    SizedFluidIngredient.FLAT_CODEC.optionalFieldOf("fluid").forGetter(SluiceRecipe::getFluid),
                    Codec.FLOAT.optionalFieldOf("processing_time_multiplier", 1F).forGetter(SluiceRecipe::getProcessingTimeMultiplier),
                    MeshType.CODEC.listOf().fieldOf("mesh_types").forGetter(SluiceRecipe::getMeshTypesAsList)
            ).apply(builder, factory::create));

            streamCodec = StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, SluiceRecipe::getIngredient,
                    ItemWithChance.STREAM_CODEC.apply(ByteBufCodecs.list()), SluiceRecipe::getResults,
                    ByteBufCodecs.VAR_INT, SluiceRecipe::getMaxResults,
                    ByteBufCodecs.optional(SizedFluidIngredient.STREAM_CODEC), SluiceRecipe::getFluid,
                    ByteBufCodecs.FLOAT, SluiceRecipe::getProcessingTimeMultiplier,
                    MeshType.STREAM_CODEC.apply(ByteBufCodecs.list()), SluiceRecipe::getMeshTypesAsList,
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
