package dev.ftb.mods.ftbstuffnthings.blocks.cobblegen;

import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import dev.ftb.mods.ftbstuffnthings.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public enum CobblegenProperties implements IResourceGenProps {
    STONE(Config.STONE_COBBLEGEN_AMOUNT, CobblegenBlockEntity.Stone::new),
    IRON(Config.IRON_COBBLEGEN_AMOUNT, CobblegenBlockEntity.Iron::new),
    GOLD(Config.GOLD_COBBLEGEN_AMOUNT, CobblegenBlockEntity.Gold::new),
    DIAMOND(Config.DIAMOND_COBBLEGEN_AMOUNT, CobblegenBlockEntity.Diamond::new),
    NETHERITE(Config.NETHERITE_COBBLEGEN_AMOUNT, CobblegenBlockEntity.Netherite::new);

    private final IntValue cobblegenSpeed;
    private final BiFunction<BlockPos, BlockState, ? extends CobblegenBlockEntity> beFactory;

    CobblegenProperties(IntValue cobblegenSpeed, BiFunction<BlockPos, BlockState, ? extends CobblegenBlockEntity> beFactory) {
        this.cobblegenSpeed = cobblegenSpeed;
        this.beFactory = beFactory;
    }

    @Override
    public int itemsPerOperation() {
        return cobblegenSpeed.get();
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState blockState) {
        return beFactory.apply(pos, blockState);
    }
}
