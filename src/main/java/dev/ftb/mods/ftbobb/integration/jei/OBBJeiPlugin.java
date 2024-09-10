package dev.ftb.mods.ftbobb.integration.jei;

import dev.ftb.mods.ftbobb.FTBOBB;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class OBBJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return FTBOBB.id("jei_plugin");
    }
}
