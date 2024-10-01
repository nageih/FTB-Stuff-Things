package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntitiesRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FTBOBB.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Oak>> OAK_SLUICE
            = BLOCK_ENTITIES.register("oak_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Oak::new, BlocksRegistry.OAK_SLUICE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Iron>> IRON_SLUICE
            = BLOCK_ENTITIES.register("iron_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Iron::new, BlocksRegistry.IRON_SLUICE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Diamond>> DIAMOND_SLUICE
            = BLOCK_ENTITIES.register("diamond_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Diamond::new, BlocksRegistry.DIAMOND_SLUICE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SluiceBlockEntity.Netherite>> NETHERITE_SLUICE
            = BLOCK_ENTITIES.register("netherite_sluice", () -> BlockEntityType.Builder.of(SluiceBlockEntity.Netherite::new, BlocksRegistry.NETHERITE_SLUICE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PumpBlockEntity>> PUMP
            = BLOCK_ENTITIES.register("pump", () -> BlockEntityType.Builder.of(PumpBlockEntity::new, BlocksRegistry.PUMP.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TubeBlockEntity>> TUBE
            = BLOCK_ENTITIES.register("tube", () -> BlockEntityType.Builder.of(TubeBlockEntity::new, BlocksRegistry.TUBE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<JarBlockEntity>> JAR
            = BLOCK_ENTITIES.register("jar", () -> BlockEntityType.Builder.of(JarBlockEntity::new, BlocksRegistry.JAR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TemperedJarBlockEntity>> TEMPERED_JAR
            = BLOCK_ENTITIES.register("tempered_jar", () -> BlockEntityType.Builder.of(TemperedJarBlockEntity::new, BlocksRegistry.TEMPERED_JAR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DripperBlockEntity>> DRIPPER
            = BLOCK_ENTITIES.register("dripper", () -> BlockEntityType.Builder.of(DripperBlockEntity::new, BlocksRegistry.DRIPPER.get()).build(null));

    public static void init(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
