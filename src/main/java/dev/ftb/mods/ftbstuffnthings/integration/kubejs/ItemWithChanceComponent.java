package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.type.TypeInfo;

public class ItemWithChanceComponent implements RecipeComponent<ItemWithChance> {
    public static final ItemWithChanceComponent INSTANCE = new ItemWithChanceComponent();

    @Override
    public Codec<ItemWithChance> codec() {
        return ItemWithChance.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(ItemWithChance.class);
    }

    @Override
    public String toString() {
        return "item_with_chance";
    }
}
