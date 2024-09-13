package dev.ftb.mods.ftbobb.items;

import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public enum MeshType implements StringRepresentable {
    EMPTY("empty", null, () -> null),

    CLOTH("cloth", ItemsRegistry.CLOTH_MESH, () -> Tags.Items.STRINGS),
    IRON("iron", ItemsRegistry.IRON_MESH, () -> Tags.Items.INGOTS_IRON),
    GOLD("gold", ItemsRegistry.GOLD_MESH, () -> Tags.Items.INGOTS_GOLD),
    DIAMOND("diamond", ItemsRegistry.DIAMOND_MESH, () -> Tags.Items.GEMS_DIAMOND);

    public static final MeshType[] VALUES = values();
    public static final List<MeshType> NON_EMPTY_VALUES = Arrays.stream(VALUES).filter(e -> e != EMPTY).toList();
    public static final Map<String, MeshType> MAP = new HashMap<>();

    static {
        for (MeshType type : VALUES) {
            MAP.put(type.name, type);
        }
    }

    @Nullable
    private final DeferredItem<MeshItem> meshItem;
    private final String name;
    private final Supplier<TagKey<Item>> lazyIngredient;
    private TagKey<Item> ingredient;

    MeshType(String name, @Nullable DeferredItem<MeshItem> meshItem, Supplier<TagKey<Item>> meshIngredients) {
        this.name = name;
        this.meshItem = meshItem;
        this.lazyIngredient = meshIngredients;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public ItemStack getItemStack() {
        if (this.meshItem == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(this.meshItem.get());
    }

    public TagKey<Item> getIngredient() {
        if (this.ingredient == null) {
            this.ingredient = this.lazyIngredient.get();
        }

        return this.ingredient;
    }
}
