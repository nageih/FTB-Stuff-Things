package dev.ftb.mods.ftbobb.items;

import dev.ftb.mods.ftbobb.registry.ComponentsRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

public class FluidCapsuleItem extends Item {
    public FluidCapsuleItem() {
        super(new Item.Properties().component(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.EMPTY).stacksTo(1));
    }

    public static ItemStack of(FluidStack fluidStack) {
        ItemStack stack = new ItemStack(ItemsRegistry.FLUID_CAPSULE.get());
        stack.set(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.copyOf(fluidStack));
        return stack;
    }
}
