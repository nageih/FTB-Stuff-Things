package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.*;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class BlocksRegistry {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FTBOBB.MODID);

    public static final DeferredBlock<SluiceBlock> OAK_SLUICE
            = BLOCKS.register("oak_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));
    public static final DeferredBlock<SluiceBlock> IRON_SLUICE
            = BLOCKS.register("iron_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));
    public static final DeferredBlock<SluiceBlock> DIAMOND_SLUICE
            = BLOCKS.register("diamond_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));
    public static final DeferredBlock<SluiceBlock> NETHERITE_SLUICE
            = BLOCKS.register("netherite_sluice", () -> new SluiceBlock(SNBTConfig.create("hello")));
    public static List<DeferredBlock<SluiceBlock>> ALL_SLUICES = List.of(OAK_SLUICE, IRON_SLUICE, DIAMOND_SLUICE, NETHERITE_SLUICE);

    public static final DeferredBlock<PumpBlock> PUMP
            = BLOCKS.register("pump", PumpBlock::new);

    public static final DeferredBlock<TubeBlock> TUBE
            = BLOCKS.register("tube", TubeBlock::new);
    public static final DeferredBlock<JarBlock> JAR
            = BLOCKS.register("jar", JarBlock::new);
    public static final DeferredBlock<TemperedJarBlock> TEMPERED_JAR
            = BLOCKS.register("tempered_jar", TemperedJarBlock::new);
    public static final DeferredBlock<AutoProcessingBlock> AUTO_PROCESSING_BLOCK
            = BLOCKS.register("auto_processing_block", AutoProcessingBlock::new);
    public static final DeferredBlock<Block> BLUE_MAGMA_BLOCK
            = BLOCKS.register("blue_magma_block", () -> new MagmaBlock(
                    Block.Properties.ofFullCopy(Blocks.STONE)
                            .mapColor(MapColor.NETHER)
                            .requiresCorrectToolForDrops()
                            .lightLevel(state -> 3)
                            .randomTicks()
                            .strength(0.5F)
                            .isValidSpawn((state, level, pos, entity) -> entity.fireImmune())
                            .hasPostProcess((state, level, pos) -> true)
                            .emissiveRendering((state, level, pos) -> true)
            ));
    public static final DeferredBlock<Block> CREATIVE_LOW_TEMPERATURE_SOURCE
            = BLOCKS.register("creative_low_temperature_source", CreativeTemperatureSourceBlock::new);
    public static final DeferredBlock<Block> CREATIVE_HIGH_TEMPERATURE_SOURCE
            = BLOCKS.register("creative_high_temperature_source", CreativeTemperatureSourceBlock::new);
    public static final DeferredBlock<Block> CREATIVE_SUBZERO_TEMPERATURE_SOURCE
            = BLOCKS.register("creative_subzero_temperature_source", CreativeTemperatureSourceBlock::new);
    
    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
