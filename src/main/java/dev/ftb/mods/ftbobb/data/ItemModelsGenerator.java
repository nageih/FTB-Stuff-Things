package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ItemModelsGenerator extends ItemModelProvider {
    private static final ResourceLocation GENERATED = ResourceLocation.parse("item/generated");

    public ItemModelsGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FTBOBB.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        String path = BlocksRegistry.PUMP.getKey().location().getPath();
        this.getBuilder(path).parent(new ModelFile.UncheckedModelFile(this.modLoc("block/" + path + "_on")));

        withExistingParent("tube", FTBOBB.id("block/tube_inv"));
        withExistingParent("jar", FTBOBB.id("block/jar"));
        withExistingParent("tempered_jar", FTBOBB.id("block/tempered_jar_normal"));
        withExistingParent("auto_processing_block", FTBOBB.id("block/auto_processing_block"));
        withExistingParent("blue_magma_block", FTBOBB.id("block/blue_magma_block"));
        withExistingParent("creative_low_temperature_source", modLoc("block/creative_low_temperature_source"));
        withExistingParent("creative_high_temperature_source", modLoc("block/creative_high_temperature_source"));
        withExistingParent("creative_subzero_temperature_source", modLoc("block/creative_subzero_temperature_source"));
        withExistingParent("cast_iron_block", modLoc("block/cast_iron_block"));

        simpleItem(ItemsRegistry.FLUID_CAPSULE, "item/fluid_container_base", "item/fluid_container_overlay");
        simpleItem(ItemsRegistry.DRIPPER, "item/dripper");
        simpleItem(ItemsRegistry.WATER_BOWL, "item/water_bowl");

        simpleItem(ItemsRegistry.CAST_IRON_GEAR, "item/cast_iron_gear");
        simpleItem(ItemsRegistry.CAST_IRON_INGOT, "item/cast_iron_ingot");
        simpleItem(ItemsRegistry.CAST_IRON_NUGGET, "item/cast_iron_nugget");
        simpleItem(ItemsRegistry.TEMPERED_GLASS, "item/tempered_glass");
    }

    private ItemModelBuilder simpleItem(DeferredItem<? extends Item> item, String... textures) {
        return simpleItem(item.getId(), textures);
    }

    private ItemModelBuilder simpleItem(ResourceLocation itemKey, String... textures) {
        ItemModelBuilder builder = withExistingParent(itemKey.getPath(), GENERATED);
        for (int i = 0; i < textures.length; i++) {
            builder.texture("layer" + i, textures[i]);
        }
        return builder;
    }
}
