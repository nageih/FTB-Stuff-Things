package dev.ftb.mods.ftbobb.items;

import dev.ftb.mods.ftbobb.registry.ComponentsRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import dev.ftb.mods.ftbobb.util.MiscUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

import java.util.List;

public class FluidCapsuleItem extends Item {
    public FluidCapsuleItem() {
        super(new Item.Properties().component(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.EMPTY).stacksTo(1));
    }

    public static ItemStack of(FluidStack fluidStack) {
        ItemStack stack = new ItemStack(ItemsRegistry.FLUID_CAPSULE.get());
        stack.set(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.copyOf(fluidStack));
        return stack;
    }

    public static FluidStack getFluid(ItemStack stack) {
        return stack.getOrDefault(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.EMPTY).copy();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        SimpleFluidContent content = stack.getOrDefault(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.EMPTY);
        if (!content.isEmpty()) {
            tooltipComponents.add(MiscUtil.makeFluidStackDesc(content.copy()));
        }
    }

    public static class FluidHandler extends FluidHandlerItemStack.Consumable {
        public FluidHandler(ItemStack container) {
            super(ComponentsRegistry.STORED_FLUID, container, FluidType.BUCKET_VOLUME);
        }

        @Override
        public int fill(FluidStack resource, FluidAction doFill) {
            // only allow filling if it's completely empty
            return getFluid().isEmpty() ? super.fill(resource, doFill) : 0;
        }
    }
}
