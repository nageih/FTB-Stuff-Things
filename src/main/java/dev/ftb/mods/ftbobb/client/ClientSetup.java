package dev.ftb.mods.ftbobb.client;

import dev.ftb.mods.ftbobb.client.model.TubeModel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;

public class ClientSetup {
    public static void onModConstruction(IEventBus modBus) {
        // called from mod ctor
        modBus.addListener(ClientSetup::registerModelLoaders);
    }

    private static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(TubeModel.Loader.ID, TubeModel.Loader.INSTANCE);
    }
}
