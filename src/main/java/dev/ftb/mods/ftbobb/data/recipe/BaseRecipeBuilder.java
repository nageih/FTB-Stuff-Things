package dev.ftb.mods.ftbobb.data.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public abstract class BaseRecipeBuilder<T extends Recipe<?>> implements RecipeBuilder {
    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    abstract protected T buildRecipe();

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, buildRecipe(), null);
    }
}
