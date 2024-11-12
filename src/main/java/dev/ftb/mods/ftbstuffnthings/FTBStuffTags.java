package dev.ftb.mods.ftbstuffnthings;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class FTBStuffTags {
    public static class Blocks {
        public static final TagKey<Block> MINEABLE_WITH_HAMMER = commonTag("mineable/hammer");

        public static final TagKey<Block> CRATE = modTag("crate");
        public static final TagKey<Block> BARREL = modTag("barrel");
        public static final TagKey<Block> WATER_STRAINER = modTag("water_strainer");

        static TagKey<Block> tag(String modid, String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Block> modTag(String name) {
            return tag(FTBStuffNThings.MODID, name);
        }

        static TagKey<Block> commonTag(String name) {
            return tag("c", name);
        }
    }

    public static class Items {
        public static final TagKey<Item> INGOTS_CAST_IRON = commonTag("ingots/cast_iron");
        public static final TagKey<Item> NUGGETS_CAST_IRON = commonTag("nuggets/cast_iron");
        public static final TagKey<Item> GEARS = commonTag("gears");
        public static final TagKey<Item> GEARS_CAST_IRON = commonTag("gears/cast_iron");
        public static final TagKey<Item> HAMMERS = modTag("hammers");
        public static final TagKey<Item> CROOKS = modTag("crooks");

        public static final TagKey<Item> MESHES = modTag("meshes");

        static TagKey<Item> tag(String modid, String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Item> modTag(String name) {
            return tag(FTBStuffNThings.MODID, name);
        }

        static TagKey<Item> commonTag(String name) {
            return tag("c", name);
        }
    }
}
