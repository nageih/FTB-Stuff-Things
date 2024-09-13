package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.PumpBlock;
import dev.ftb.mods.ftbobb.blocks.SluiceBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class BlocksRegistry {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FTBOBB.MODID);

    public static DeferredBlock<SluiceBlock> OAK_SLUICE = BLOCKS.register("oak_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));
    public static DeferredBlock<SluiceBlock> IRON_SLUICE = BLOCKS.register("iron_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));
    public static DeferredBlock<SluiceBlock> DIAMOND_SLUICE = BLOCKS.register("diamond_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));
    public static DeferredBlock<SluiceBlock> NETHERITE_SLUICE = BLOCKS.register("netherite_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));

    public static List<DeferredBlock<SluiceBlock>> ALL_SLUICES = List.of(OAK_SLUICE, IRON_SLUICE, DIAMOND_SLUICE, NETHERITE_SLUICE);

    public static DeferredBlock<PumpBlock> PUMP = BLOCKS.register("pump", PumpBlock::new);

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
