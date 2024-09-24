package dev.ftb.mods.ftbobb.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;

public interface ITubeConnectable {
    static boolean canConnect(Level level, BlockPos pos, Direction face) {
        return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, face) != null
                || level.getCapability(Capabilities.FluidHandler.BLOCK, pos, face) != null
                || level.getBlockState(pos).getBlock() instanceof ITubeConnectable c && c.isSideTubeConnectable(face)
                || level.getBlockEntity(pos) instanceof ITubeConnectable c1 && c1.isSideTubeConnectable(face);
    }

    boolean isSideTubeConnectable(Direction side);
}
