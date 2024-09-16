package dev.ftb.mods.ftbobb;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbobb.client.ClientSetup;
import dev.ftb.mods.ftbobb.client.FTBOBBClient;
import dev.ftb.mods.ftbobb.registry.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

@Mod(FTBOBB.MODID)
public class FTBOBB {
    public static final String MODID = "ftbobb";

    private static final Logger LOGGER = LogUtils.getLogger();

    public FTBOBB(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        if (FMLEnvironment.dist.isClient()) {
            ClientSetup.onModConstruction(modEventBus);
        }

        Config.init();

        BlocksRegistry.init(modEventBus);
        ItemsRegistry.init(modEventBus);
        BlockEntitiesRegistry.init(modEventBus);
        RecipesRegistry.init(modEventBus);
        ContentRegistry.init(modEventBus);
        ComponentsRegistry.init(modEventBus);

        modEventBus.addListener(this::clientReady);
        modEventBus.addListener(this::registerCapabilities);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void clientReady(final FMLClientSetupEvent event) {
        FTBOBBClient.INSTANCE.init();
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntitiesRegistry.OAK_SLUICE.get(),
                (blockEntity, side) -> blockEntity.getFluidTank()
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntitiesRegistry.JAR.get(),
                (blockEntity, side) -> side == Direction.UP ? blockEntity.getFluidTank() : null
        );
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
