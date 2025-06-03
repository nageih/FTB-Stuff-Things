package dev.ftb.mods.ftbstuffnthings.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * An ItemStackHandler which supports cached comparator signal level calculation.
 * Only recalculates the signal when the contents have changed.
 */
public class ComparatorItemStackHandler extends ItemStackHandler {
    private int signalLevel = -1;  // -1 indicates recalc needed

    public ComparatorItemStackHandler(int invSize) {
        super(invSize);
    }

    @Override
    protected void onContentsChanged(int slot) {
        invalidateComparatorValue();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        super.deserializeNBT(provider, nbt);
        invalidateComparatorValue();
    }

    public int getComparatorLevel() {
        if (signalLevel < 0) {
            signalLevel = ItemHandlerHelper.calcRedstoneFromInventory(this);
        }
        return signalLevel;
    }

    public void invalidateComparatorValue() {
        signalLevel = -1;
    }
}
