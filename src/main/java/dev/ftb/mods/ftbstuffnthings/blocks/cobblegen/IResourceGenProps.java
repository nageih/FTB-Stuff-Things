package dev.ftb.mods.ftbstuffnthings.blocks.cobblegen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IResourceGenProps {
    int itemsPerOperation();

    BlockEntity createBlockEntity(BlockPos pos, BlockState blockState);
}
