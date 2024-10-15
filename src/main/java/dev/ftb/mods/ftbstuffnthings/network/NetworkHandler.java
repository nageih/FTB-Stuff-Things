package dev.ftb.mods.ftbstuffnthings.network;


import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = FTBStuffNThings.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(FTBStuffNThings.MODID).versioned("1.0");

        // clientbound
        registrar.playToClient(SyncJarContentsPacket.TYPE, SyncJarContentsPacket.STREAM_CODEC, SyncJarContentsPacket::handleData);
        registrar.playToClient(SyncJarRecipePacket.TYPE, SyncJarRecipePacket.STREAM_CODEC, SyncJarRecipePacket::handleData);
        registrar.playToClient(FluidTankSync.TYPE, FluidTankSync.STREAM_CODEC, FluidTankSync::handleData);

        // serverbound
        registrar.playToServer(ToggleJarCraftingPacket.TYPE, ToggleJarCraftingPacket.STREAM_CODEC, ToggleJarCraftingPacket::handleData);

        // bidirectional
    }
}
