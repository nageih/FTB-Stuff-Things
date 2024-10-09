package dev.ftb.mods.ftbobb.integration.wallalike;

import dev.ftb.mods.ftbobb.blocks.hammer.AutoHammerBlock;
import dev.ftb.mods.ftbobb.blocks.hammer.AutoHammerBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(AutoHammerComponentProvider.INSTANCE, AutoHammerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(AutoHammerComponentProvider.INSTANCE, AutoHammerBlock.class);
    }
}
