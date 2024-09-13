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

        addBlock(BlocksRegistry.OAK_SLUICE, "Oak Sluice");
        addBlock(BlocksRegistry.IRON_SLUICE, "Iron Sluice");
        addBlock(BlocksRegistry.DIAMOND_SLUICE, "Diamond Sluice");
        addBlock(BlocksRegistry.NETHERITE_SLUICE, "Netherite Sluice");

        addBlock(BlocksRegistry.PUMP, "Pump");

        addItem(ItemsRegistry.CLOTH_MESH, "Cloth Mesh");
        addItem(ItemsRegistry.IRON_MESH, "Iron Mesh");
        addItem(ItemsRegistry.GOLD_MESH, "Gold Mesh");
        addItem(ItemsRegistry.DIAMOND_MESH, "Diamond Mesh");
    }
}
