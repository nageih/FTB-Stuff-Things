package dev.ftb.mods.ftbobb;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class FTBOBBTags {
    public static class Blocks {
        static TagKey<Block> tag(String modid, String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Block> modTag(String name) {
            return tag(FTBOBB.MODID, name);
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

        public static final TagKey<Item> MESHES = modTag("meshes");

        static TagKey<Item> tag(String modid, String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
        }

        static TagKey<Item> modTag(String name) {
            return tag(FTBOBB.MODID, name);
        }

        static TagKey<Item> commonTag(String name) {
            return tag("c", name);
        }
    }
}
