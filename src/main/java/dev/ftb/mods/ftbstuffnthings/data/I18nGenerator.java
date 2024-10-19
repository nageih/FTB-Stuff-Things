package dev.ftb.mods.ftbstuffnthings.data;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class I18nGenerator extends LanguageProvider {
    public I18nGenerator(PackOutput output) {
        super(output, FTBStuffNThings.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("ftbstuff.itemGroup.tab", "FTB Ocean Building Blocks");
        add("ftbstuff.any_block", "Any Block");
        add("ftbstuff.efficiency", "Efficiency: %s");
        add("ftbstuff.temperature", "Temperature: %s");
        add("ftbstuff.temperature.normal", "Normal");
        add("ftbstuff.temperature.hot", "Hot");
        add("ftbstuff.temperature.superheated", "Superheated");
        add("ftbstuff.temperature.chilled", "Chilled");
        add("ftbstuff.processing_time", "Processing time: %d sec");
        add("ftbstuff.start_mix", "Mix");
        add("ftbstuff.stop_mix", "Stop");
        add("ftbstuff.temperature_source", "Temperature Source");
        add("ftbstuff.temperature_source.click", "Click to show Temperature Sources");
        add("ftbstuff.jar_status.ready", "Ready To Craft");
        add("ftbstuff.jar_status.no_recipe", "No Recipe");
        add("ftbstuff.jar_status.not_enough_resources", "Insufficient Resources");
        add("ftbstuff.jar_status.crafting", "Crafting!");
        add("ftbstuff.making", "Making:");
        add("ftbstuff.dripper.chance", "Chance: %s%% / sec");
        add("ftbstuff.dripper.consume_on_fail", "Fluid is consumed even on failed attempt");
        add("ftbstuff.tooltip.hammers", "Crushes materials down to their core components");
        add("ftbstuff.tooltip.energy", "Energy: %s FE");
        add("ftbstuff.tooltip.fluid", "Fluid: %smB %s");
        add("ftbstuff.crook.limit", "Max drops per block broken: %s");
        add("ftbstuff.jade.processing", "Processing");
        add("ftbstuff.jade.buffer", "Buffer");
        add("config.jade.plugin_ftbstuff.autohammer", "Autohammer");
        add("block.ftbstuff.fusing_machine.tooltip", "Used to fuse items together to produce new results");
        add("block.ftbstuff.super_cooler.tooltip", "Used to \"super\"-cool items to produce new results");

        add("death.attack.static_electric", "%1$s was killed by static electricity");

        addBlock(BlocksRegistry.OAK_SLUICE, "Oak Sluice");
        addBlock(BlocksRegistry.IRON_SLUICE, "Iron Sluice");
        addBlock(BlocksRegistry.DIAMOND_SLUICE, "Diamond Sluice");
        addBlock(BlocksRegistry.NETHERITE_SLUICE, "Netherite Sluice");

        addBlock(BlocksRegistry.IRON_AUTO_HAMMER, "Iron Auto-Hammer");
        addBlock(BlocksRegistry.GOLD_AUTO_HAMMER, "Gold Auto-Hammer");
        addBlock(BlocksRegistry.DIAMOND_AUTO_HAMMER, "Diamond Auto-Hammer");
        addBlock(BlocksRegistry.NETHERITE_AUTO_HAMMER, "Netherite Auto-Hammer");

        addBlock(BlocksRegistry.FUSING_MACHINE, "SlowMelter 9000");
        addBlock(BlocksRegistry.SUPER_COOLER, "\"Super\" Cooler");

        addBlock(BlocksRegistry.PUMP, "Pump");

        addBlock(BlocksRegistry.DRIPPER, "Dripper");

        addBlock(BlocksRegistry.TUBE, "Cast Iron Tube");
        addBlock(BlocksRegistry.JAR, "Glass Jar");
        addBlock(BlocksRegistry.TEMPERED_JAR, "Tempered Glass Jar");
        addBlock(BlocksRegistry.JAR_AUTOMATER, "Jar Automater");
        addBlock(BlocksRegistry.BLUE_MAGMA_BLOCK, "Blue Magma Block");
        addBlock(BlocksRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE, "Creative Hot Temperature Source");
        addBlock(BlocksRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE, "Creative Superheated Temperature Source");
        addBlock(BlocksRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE, "Creative Chilled Temperature Source");

        addBlock(BlocksRegistry.CAST_IRON_BLOCK, "Cast Iron Block");

        addBlock(BlocksRegistry.DUST_BLOCK, "Dust");
        addBlock(BlocksRegistry.CRUSHED_BASALT, "Crushed Basalt");
        addBlock(BlocksRegistry.CRUSHED_ENDSTONE, "Crushed Endstone");
        addBlock(BlocksRegistry.CRUSHED_NETHERRACK, "Crushed Netherrack");

        addItem(ItemsRegistry.CLOTH_MESH, "Cloth Mesh");
        addItem(ItemsRegistry.IRON_MESH, "Iron Mesh");
        addItem(ItemsRegistry.GOLD_MESH, "Gold Mesh");
        addItem(ItemsRegistry.DIAMOND_MESH, "Diamond Mesh");

        addItem(ItemsRegistry.FLUID_CAPSULE, "Fluid Capsule");
        addItem(ItemsRegistry.WATER_BOWL, "Water Bowl");

        addItem(ItemsRegistry.CAST_IRON_GEAR, "Cast Iron Gear");
        addItem(ItemsRegistry.CAST_IRON_INGOT, "Cast Iron Ingot");
        addItem(ItemsRegistry.CAST_IRON_NUGGET, "Cast Iron Nugget");
        addItem(ItemsRegistry.TEMPERED_GLASS, "Tempered Glass");

        addItem(ItemsRegistry.STONE_HAMMER, "Stone Hammer");
        addItem(ItemsRegistry.IRON_HAMMER, "Iron Hammer");
        addItem(ItemsRegistry.GOLD_HAMMER, "Gold Hammer");
        addItem(ItemsRegistry.DIAMOND_HAMMER, "Diamond Hammer");
        addItem(ItemsRegistry.NETHERITE_HAMMER, "Netherite Hammer");

        addItem(ItemsRegistry.CROOK, "Stone Crook");
        addItem(ItemsRegistry.STONE_ROD, "Stone Rod");

        BlocksRegistry.BARRELS.forEach((barrel) -> {
            String name = barrel.getId().getPath().split("_")[0];
            // Upper case first letter
            name = name.substring(0, 1).toUpperCase() + name.substring(1);

            addBlock(barrel, name + " Barrel");
        });

        addBlock(BlocksRegistry.CRATE, "Crate");
        addBlock(BlocksRegistry.SMALL_CRATE, "Small Crate");
        addBlock(BlocksRegistry.PULSATING_CRATE, "Pulsating Crate");
    }
}
