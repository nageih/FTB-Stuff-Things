package dev.ftb.mods.ftbobb.capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class PublicReadOnlyFluidTank extends FluidTank {
    private final BlockEntity entity;

    public PublicReadOnlyFluidTank(BlockEntity entity, int capacity) {
        super(capacity);
        this.entity = entity;
    }

    @Override
    protected void onContentsChanged() {
        this.entity.setChanged();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    public int internalFill(FluidStack resource, FluidAction action) {
        return super.fill(resource, action);
    }
}
