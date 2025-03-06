package dev.ftb.mods.ftbstuffnthings.blocks.cobblegen;

import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import dev.ftb.mods.ftbstuffnthings.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public enum BasaltgenProperties implements IResourceGenProps {
    STONE(Config.STONE_BASALTGEN_AMOUNT, BasaltgenBlockEntity.Stone::new),
    IRON(Config.IRON_BASALTGEN_AMOUNT, BasaltgenBlockEntity.Iron::new),
    GOLD(Config.GOLD_BASALTGEN_AMOUNT, BasaltgenBlockEntity.Gold::new),
    DIAMOND(Config.DIAMOND_BASALTGEN_AMOUNT, BasaltgenBlockEntity.Diamond::new),
    NETHERITE(Config.NETHERITE_BASALTGEN_AMOUNT, BasaltgenBlockEntity.Netherite::new);

    private final IntValue itemsPerOp;
    private final BiFunction<BlockPos, BlockState, ? extends BasaltgenBlockEntity> beFactory;

    BasaltgenProperties(IntValue itemsPerOp, BiFunction<BlockPos, BlockState, ? extends BasaltgenBlockEntity> beFactory) {
        this.itemsPerOp = itemsPerOp;
        this.beFactory = beFactory;
    }

    @Override
    public int itemsPerOperation() {
        return itemsPerOp.get();
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState blockState) {
        return beFactory.apply(pos, blockState);
    }
}
