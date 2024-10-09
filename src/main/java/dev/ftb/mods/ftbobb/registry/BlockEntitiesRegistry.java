package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.dripper.DripperBlockEntity;
import dev.ftb.mods.ftbobb.blocks.fusingmachine.FusingMachineBlockEntity;
import dev.ftb.mods.ftbobb.blocks.hammer.AutoHammerBlockEntity;
import dev.ftb.mods.ftbobb.blocks.jar.JarBlockEntity;
import dev.ftb.mods.ftbobb.blocks.jar.TemperedJarBlockEntity;
import dev.ftb.mods.ftbobb.blocks.pump.PumpBlockEntity;
import dev.ftb.mods.ftbobb.blocks.sluice.SluiceBlockEntity;
import dev.ftb.mods.ftbobb.blocks.supercooler.SuperCoolerBlockEntity;
import dev.ftb.mods.ftbobb.blocks.tube.TubeBlockEntity;
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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoHammerBlockEntity.Iron>> IRON_HAMMER
            = BLOCK_ENTITIES.register("iron_hammer", () -> BlockEntityType.Builder.of(AutoHammerBlockEntity.Iron::new, BlocksRegistry.IRON_AUTO_HAMMER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoHammerBlockEntity.Gold>> GOLD_HAMMER
            = BLOCK_ENTITIES.register("gold_hammer", () -> BlockEntityType.Builder.of(AutoHammerBlockEntity.Gold::new, BlocksRegistry.GOLD_AUTO_HAMMER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoHammerBlockEntity.Diamond>> DIAMOND_HAMMER
            = BLOCK_ENTITIES.register("diamond_hammer", () -> BlockEntityType.Builder.of(AutoHammerBlockEntity.Diamond::new, BlocksRegistry.DIAMOND_AUTO_HAMMER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoHammerBlockEntity.Netherite>> NETHERITE_HAMMER
            = BLOCK_ENTITIES.register("netherite_hammer", () -> BlockEntityType.Builder.of(AutoHammerBlockEntity.Netherite::new, BlocksRegistry.NETHERITE_AUTO_HAMMER.get()).build(null));

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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FusingMachineBlockEntity>> FUSING_MACHINE
            = BLOCK_ENTITIES.register("fusing_machine", () -> BlockEntityType.Builder.of(FusingMachineBlockEntity::new, BlocksRegistry.FUSING_MACHINE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SuperCoolerBlockEntity>> SUPER_COOLER
            = BLOCK_ENTITIES.register("super_cooler", () -> BlockEntityType.Builder.of(SuperCoolerBlockEntity::new, BlocksRegistry.SUPER_COOLER.get()).build(null));

    public static void init(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
