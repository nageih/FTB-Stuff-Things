package dev.ftb.mods.ftbobb.client;

import dev.ftb.mods.ftbobb.client.renders.PumpBlockEntityRender;
import dev.ftb.mods.ftbobb.client.renders.SluiceBlockEntityRenderer;
import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public enum FTBOBBClient {
    INSTANCE;

    public void init() {
        BlockEntityRenderers.register(BlockEntitiesRegistry.OAK_SLUICE.get(), SluiceBlockEntityRenderer::new);
        BlockEntityRenderers.register(BlockEntitiesRegistry.IRON_SLUICE.get(), SluiceBlockEntityRenderer::new);
        BlockEntityRenderers.register(BlockEntitiesRegistry.DIAMOND_SLUICE.get(), SluiceBlockEntityRenderer::new);
        BlockEntityRenderers.register(BlockEntitiesRegistry.NETHERITE_SLUICE.get(), SluiceBlockEntityRenderer::new);

        BlockEntityRenderers.register(BlockEntitiesRegistry.PUMP.get(), PumpBlockEntityRender::new);
    }
}
