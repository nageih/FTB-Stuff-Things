package dev.ftb.mods.ftbobb.blocks.fusingmachine;

import dev.ftb.mods.ftbobb.blocks.AbstractMachineMenu;
import dev.ftb.mods.ftbobb.capabilities.EmittingStackHandler;
import dev.ftb.mods.ftbobb.registry.ContentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.Objects;

public class FusingMachineMenu extends AbstractMachineMenu<FusingMachineBlockEntity> {
    public FusingMachineMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(windowId, playerInventory, getTilePos(buffer));
    }

    public FusingMachineMenu(int windowId, Inventory playerInventory, BlockPos pos) {
        super(ContentRegistry.FUSING_MACHINE_MENU.get(), windowId, playerInventory, pos);

        EmittingStackHandler itemHandler = Objects.requireNonNull(blockEntity.getItemHandler());
        addSlot(new SlotItemHandler(itemHandler, 0, 43, 27));
        addSlot(new SlotItemHandler(itemHandler, 1, 43 + 18, 27));

        addPlayerSlots(playerInventory, 8, 84);

        addDataSlots(containerData);
    }
}
