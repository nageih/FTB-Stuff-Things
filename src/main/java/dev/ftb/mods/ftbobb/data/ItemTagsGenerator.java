package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.FTBOBBTags;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ItemTagsGenerator extends ItemTagsProvider {
    public ItemTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, FTBOBB.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        appendToTag(Tags.Items.INGOTS, FTBOBBTags.Items.INGOTS_CAST_IRON);
        appendToTag(Tags.Items.NUGGETS, FTBOBBTags.Items.NUGGETS_CAST_IRON);
//        appendToTag(FTBOBBTags.Items.GEARS, FTBOBBTags.Items.GEARS_CAST_IRON);

        addItemsToTag(FTBOBBTags.Items.NUGGETS_CAST_IRON, ItemsRegistry.CAST_IRON_NUGGET);
        addItemsToTag(FTBOBBTags.Items.INGOTS_CAST_IRON, ItemsRegistry.CAST_IRON_INGOT);
        addItemsToTag(FTBOBBTags.Items.GEARS, ItemsRegistry.CAST_IRON_GEAR);

        addItemsToTag(FTBOBBTags.Items.MESHES, ItemsRegistry.CLOTH_MESH, ItemsRegistry.IRON_MESH, ItemsRegistry.GOLD_MESH, ItemsRegistry.DIAMOND_MESH);
    }

    @SafeVarargs
    private void addItemsToTag(TagKey<Item> tag, Supplier<? extends ItemLike>... items) {
        tag(tag).add(Arrays.stream(items).map(Supplier::get).map(ItemLike::asItem).toArray(Item[]::new));
    }

    @SafeVarargs
    private void appendToTag(TagKey<Item> tag, TagKey<Item>... toAppend) {
        tag(tag).addTags(toAppend);
    }

    @Override
    public String getName() {
        return FTBOBB.MODID + " Item Tags";
    }
}
