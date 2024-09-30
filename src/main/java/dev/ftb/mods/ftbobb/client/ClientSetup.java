package dev.ftb.mods.ftbobb.client;

import dev.ftb.mods.ftbobb.client.model.TubeModel;
import dev.ftb.mods.ftbobb.client.renders.JarBlockEntityRenderer;
import dev.ftb.mods.ftbobb.client.renders.PumpBlockEntityRender;
import dev.ftb.mods.ftbobb.client.renders.SluiceBlockEntityRenderer;
import dev.ftb.mods.ftbobb.client.renders.TemperedJarBlockEntityRenderer;
import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbobb.registry.ContentRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import dev.ftb.mods.ftbobb.screens.TemperedJarScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientSetup {
    public static void onModConstruction(IEventBus modBus) {
        // called from mod ctor
        modBus.addListener(ClientSetup::registerModelLoaders);
        modBus.addListener(ClientSetup::registerRenderers);
        modBus.addListener(ClientSetup::registerScreens);
        modBus.addListener(ClientSetup::registerColorHandlers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntitiesRegistry.OAK_SLUICE.get(), SluiceBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntitiesRegistry.IRON_SLUICE.get(), SluiceBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntitiesRegistry.DIAMOND_SLUICE.get(), SluiceBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntitiesRegistry.NETHERITE_SLUICE.get(), SluiceBlockEntityRenderer::new);

        event.registerBlockEntityRenderer(BlockEntitiesRegistry.PUMP.get(), PumpBlockEntityRender::new);

        event.registerBlockEntityRenderer(BlockEntitiesRegistry.JAR.get(), JarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntitiesRegistry.TEMPERED_JAR.get(), TemperedJarBlockEntityRenderer::new);
    }

    private static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(TubeModel.Loader.ID, TubeModel.Loader.INSTANCE);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ContentRegistry.TEMPERED_JAR_MENU.get(), TemperedJarScreen::new);
    }

    private static void registerColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> switch (tintIndex) {
            case 0 -> 0xFFFFFFFF;
            case 1 -> FluidCapsuleColorHandler.getColor(stack);
            default -> 0xFF000000;
        }, ItemsRegistry.FLUID_CAPSULE.get());
    }
}
