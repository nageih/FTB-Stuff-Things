package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.SluiceBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TilesRegistry {
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FTBOBB.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Oak>> OAK_SLUICE = TILES.register("oak_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Oak::new, BlocksRegistry.OAK_SLUICE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Iron>> IRON_SLUICE = TILES.register("iron_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Iron::new, BlocksRegistry.IRON_SLUICE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Diamond>> DIAMOND_SLUICE = TILES.register("diamond_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Diamond::new, BlocksRegistry.DIAMOND_SLUICE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Netherite>> NETHERITE_SLUICE = TILES.register("netherite_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Netherite::new, BlocksRegistry.NETHERITE_SLUICE.get()).build(null));

    public static void init(IEventBus bus) {
        TILES.register(bus);
    }
}
