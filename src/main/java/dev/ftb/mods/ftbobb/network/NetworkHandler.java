package dev.ftb.mods.ftbobb.network;


import dev.ftb.mods.ftbobb.FTBOBB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = FTBOBB.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(FTBOBB.MODID).versioned("1.0");

        // clientbound
        registrar.playToClient(SyncJarContentsPacket.TYPE, SyncJarContentsPacket.STREAM_CODEC, SyncJarContentsPacket::handleData);
        registrar.playToClient(SyncJarRecipePacket.TYPE, SyncJarRecipePacket.STREAM_CODEC, SyncJarRecipePacket::handleData);

        // serverbound
        registrar.playToServer(StartJarCraftingPacket.TYPE, StartJarCraftingPacket.STREAM_CODEC, StartJarCraftingPacket::handleData);


        // bidirectional
    }
}
