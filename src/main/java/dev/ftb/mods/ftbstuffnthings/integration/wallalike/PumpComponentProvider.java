package dev.ftb.mods.ftbstuffnthings.integration.wallalike;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.pump.PumpBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PumpComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation ID = FTBStuffNThings.id("pump");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag serverData = blockAccessor.getServerData();
        if (serverData.contains("timeLeft")) {
            iTooltip.add(Component.translatable("ftbstuff.jade.time_left", getTimeString(serverData.getInt("timeLeft"))));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof PumpBlockEntity pump) {
            compoundTag.putInt("timeLeft", pump.getTimeLeft());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    private static String getTimeString(int ticks) {
        int seconds = ticks / 20;

        int i = (seconds % 3600) / 60;
        return (i > 0 ? i + "m " : "") + (seconds % 3600) % 60 + "s";
    }
}
