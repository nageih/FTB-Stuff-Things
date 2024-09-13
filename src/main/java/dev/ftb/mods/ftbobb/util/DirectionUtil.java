package dev.ftb.mods.ftbobb.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.core.Direction.*;
import static net.minecraft.core.Direction.EAST;

public class DirectionUtil {
    // since this is private in Direction...
    public static final Direction[] VALUES = new Direction[] {
            DOWN, UP, NORTH, SOUTH, WEST, EAST
    };

    public static boolean getDirectionBit(int val, Direction dir) {
        return (val & (1 << dir.get3DDataValue())) != 0;
    }

    public static byte setDirectionBit(int val, Direction dir, boolean set) {
        return set ?
                (byte) (val | (1 << dir.get3DDataValue())) :
                (byte) (val & ~(1 << dir.get3DDataValue()));
    }

    @Nullable
    public static Direction getRelativeDirection(BlockPos from, BlockPos to) {
        BlockPos offset = to.subtract(from);
        if (offset.getX() == 0 && offset.getY() == 0) {
            return offset.getZ() == -1 ? NORTH : offset.getZ() == 1 ? SOUTH : null;
        }
        if (offset.getX() == 0 && offset.getZ() == 0) {
            return offset.getY() == -1 ? DOWN : offset.getY() == 1 ? UP : null;
        }
        if (offset.getY() == 0 && offset.getZ() == 0) {
            return offset.getX() == -1 ? WEST : offset.getX() == 1 ? EAST : null;
        }
        return null;
    }
}
