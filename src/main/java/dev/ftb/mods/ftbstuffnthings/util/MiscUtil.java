package dev.ftb.mods.ftbstuffnthings.util;

import com.mojang.serialization.DataResult;
import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class MiscUtil {
    public static NonNullList<ItemStack> getItemsInHandler(IItemHandler handler) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
        return NonNullList.copyOf(items);
    }

    public static Component makeFluidStackDesc(FluidStack stack) {
        return Component.translatable("ftbstuff.tooltip.fluid", stack.getAmount(), stack.getHoverName()).withStyle(ChatFormatting.AQUA);
    }

    public static DataResult<Double> validateChanceRange(double d) {
        return d > 0.0 && d <= 1.0 ? DataResult.success(d) : DataResult.error(() -> "must be in range (0.0 -> 1.0]");
    }

}
