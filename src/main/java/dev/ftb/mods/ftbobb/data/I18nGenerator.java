package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class I18nGenerator extends LanguageProvider {
    public I18nGenerator(PackOutput output) {
        super(output, FTBOBB.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("ftbobb.itemGroup.tab", "FTB Ocean Building Blocks");
    }
}
