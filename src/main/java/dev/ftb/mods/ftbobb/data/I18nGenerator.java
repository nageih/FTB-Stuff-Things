package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class I18nGenerator extends LanguageProvider {
    public I18nGenerator(PackOutput output) {
        super(output, FTBOBB.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("ftbobb.itemGroup.tab", "FTB Ocean Building Blocks");
        add("ftbobb.any_block", "Any Block");
        add("ftbobb.efficiency", "Efficiency: %s");
        add("ftbobb.temperature", "Temperature: %s");
        add("ftbobb.temperature.normal", "Normal");
        add("ftbobb.temperature.hot", "Hot");
        add("ftbobb.temperature.superheated", "Superheated");
        add("ftbobb.temperature.chilled", "Chilled");
        add("ftbobb.processing_time", "Processing time: %d sec");
        add("ftbobb.start_mix", "Mix");
        add("ftbobb.stop_mix", "Stop");
        add("ftbobb.temperature_source", "Temperature Source");
        add("ftbobb.temperature_source.click", "Click to show Temperature Sources");
        add("ftbobb.jar_status.ready", "Ready To Craft");
        add("ftbobb.jar_status.no_recipe", "No Recipe");
        add("ftbobb.jar_status.not_enough_resources", "Insufficient Resources");
        add("ftbobb.jar_status.crafting", "Crafting!");
        add("ftbobb.making", "Making:");

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
        addBlock(BlocksRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE, "Creative Hot Temperature Source");
        addBlock(BlocksRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE, "Creative Superheated Temperature Source");
        addBlock(BlocksRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE, "Creative Chilled Temperature Source");

        addBlock(BlocksRegistry.CAST_IRON_BLOCK, "Cast Iron Block");

        addItem(ItemsRegistry.CLOTH_MESH, "Cloth Mesh");
        addItem(ItemsRegistry.IRON_MESH, "Iron Mesh");
        addItem(ItemsRegistry.GOLD_MESH, "Gold Mesh");
        addItem(ItemsRegistry.DIAMOND_MESH, "Diamond Mesh");

        addItem(ItemsRegistry.FLUID_CAPSULE, "Fluid Capsule");

        addItem(ItemsRegistry.CAST_IRON_GEAR, "Cast Iron Gear");
        addItem(ItemsRegistry.CAST_IRON_INGOT, "Cast Iron Ingot");
        addItem(ItemsRegistry.CAST_IRON_NUGGET, "Cast Iron Nugget");
        addItem(ItemsRegistry.TEMPERED_GLASS, "Tempered Glass");
    }
}
