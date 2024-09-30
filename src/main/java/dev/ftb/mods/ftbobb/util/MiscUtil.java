package dev.ftb.mods.ftbobb.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
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
        return Component.literal("< ")
                .append(Component.translatable("ftblibrary.mb", stack.getAmount(), stack.getFluid().getFluidType().getDescription()))
                .append(" >")
                .withStyle(ChatFormatting.GRAY);
    }
}
