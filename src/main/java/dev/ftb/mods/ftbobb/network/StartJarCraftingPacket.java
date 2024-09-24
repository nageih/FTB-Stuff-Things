package dev.ftb.mods.ftbobb.network;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.screens.TemperedJarMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public enum StartJarCraftingPacket implements CustomPacketPayload {
    INSTANCE;

    public static final Type<StartJarCraftingPacket> TYPE = new Type<>(FTBOBB.id("start_jar_crafting"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StartJarCraftingPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void sendToServer() {
        PacketDistributor.sendToServer(INSTANCE);
    }

    public static void handleData(StartJarCraftingPacket packet, IPayloadContext context) {
        if (context.player().containerMenu instanceof TemperedJarMenu menu) {
            menu.getJar().toggleCrafting();
        }
    }
}
