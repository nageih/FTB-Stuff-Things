package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class I18nGenerator extends LanguageProvider {
    public I18nGenerator(PackOutput output) {
        super(output, FTBOBB.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("ftbobb.itemGroup.tab", "FTB Ocean Building Blocks");
        add("ftbobb.any_block", "Any Block");
        add("ftbobb.efficiency", "Efficiency: %s%%");

        add("ftbobb.temperature", "Temperature: %s");
        add("ftbobb.temperature.normal", "Normal");
        add("ftbobb.temperature.hot", "Hot");
        add("ftbobb.temperature.superheated", "Superheated");
        add("ftbobb.temperature.chilled", "Chilled");

        add("ftbobb.processing_time", "Processing time: %d sec");
        add("ftbobb.start_mix", "Mix");
        add("ftbobb.stop_mix", "Stop");

        addBlock(BlocksRegistry.OAK_SLUICE, "Oak Sluice");
        addBlock(BlocksRegistry.IRON_SLUICE, "Iron Sluice");
        addBlock(BlocksRegistry.DIAMOND_SLUICE, "Diamond Sluice");
        addBlock(BlocksRegistry.NETHERITE_SLUICE, "Netherite Sluice");

        addBlock(BlocksRegistry.PUMP, "Pump");

        addBlock(BlocksRegistry.TUBE, "Cast Iron Tube");
        addBlock(BlocksRegistry.JAR, "Glass Jar");
        addBlock(BlocksRegistry.TEMPERED_JAR, "Tempered Glass Jar");
        addBlock(BlocksRegistry.AUTO_PROCESSING_BLOCK, "Jar Auto-Processing Block");
        addBlock(BlocksRegistry.BLUE_MAGMA_BLOCK, "Blue Magma Block");
        addBlock(BlocksRegistry.CREATIVE_LOW_TEMPERATURE_SOURCE, "Creative Hot Temperature Source");
        addBlock(BlocksRegistry.CREATIVE_HIGH_TEMPERATURE_SOURCE, "Creative Superheated Temperature Source");
        addBlock(BlocksRegistry.CREATIVE_SUBZERO_TEMPERATURE_SOURCE, "Creative Chilled Temperature Source");

        addItem(ItemsRegistry.CLOTH_MESH, "Cloth Mesh");
        addItem(ItemsRegistry.IRON_MESH, "Iron Mesh");
        addItem(ItemsRegistry.GOLD_MESH, "Gold Mesh");
        addItem(ItemsRegistry.DIAMOND_MESH, "Diamond Mesh");

        addItem(ItemsRegistry.FLUID_CAPSULE, "Fluid Capsule");
    }
}
