package dev.ftb.mods.ftbstuffnthings.blocks.cobblegen;

import dev.ftb.mods.ftbstuffnthings.Config;
import dev.ftb.mods.ftbstuffnthings.registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class CobblegenBlockEntity extends BlockEntity {

    private final CobbleGenProperties props;
    protected ItemStackHandler inventory = new ItemStackHandler(1);
    private BlockCapabilityCache<IItemHandler, Direction> outputCache;
    private int ticks;

    protected CobblegenBlockEntity(BlockEntityType<?> type, CobbleGenProperties props, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.props = props;
    }

    public void tickServer() {
        ticks++;
        boolean stateActive = getBlockState().getValue(BlockStateProperties.ENABLED);
        if (!stateActive) {
            return;
        }

        if (ticks % Config.DELAY_PER_OPERATION.get() != 0) {
            return;
        }

        IItemHandler inv = getConnectedInventory();
        int amount = props.getCobbleGenAmount();

        if (inv != null) {
            ItemStack excess = ItemHandlerHelper.insertItem(inv, new ItemStack(Items.COBBLESTONE, amount), false);
            if (!excess.isEmpty()) {
                ItemHandlerHelper.insertItem(inventory, excess, false);
            }
        } else {
            ItemHandlerHelper.insertItem(inventory, new ItemStack(Items.COBBLESTONE, amount), false);
        }

    }

    public ItemStackHandler getInternalInventory() {
        return inventory;
    }

    public boolean isActive() {
        if (!getBlockState().getValue(BlockStateProperties.ENABLED)) {
            return false;
        }

        if (inventory.getStackInSlot(0).getCount() >= 64) {

            IItemHandler connectedInventory = getConnectedInventory();
            return connectedInventory != null && isInventoryFree(connectedInventory);
        }
        return true;
    }


    @Nullable
    private IItemHandler getConnectedInventory() {
        if (outputCache == null) {
            for (Direction direction : Direction.values()) {
                outputCache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) getLevel(), getBlockPos().relative(direction), null);
                IItemHandler dest = outputCache.getCapability();
                if (dest == null) {
                    continue;
                }
                if (!isInventoryFree(dest)) {
                    continue;
                }
                return dest;
            }
        } else {
            IItemHandler dest = outputCache.getCapability();
            if (dest != null && !isInventoryFree(dest)) {
                outputCache = null;
            }
            return dest;
        }
        return null;
    }

    private boolean isInventoryFree(IItemHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                return true;
            }
            if (inventory.getStackInSlot(i).is(Items.COBBLESTONE) && stack.getCount() < stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    public static class Stone extends CobblegenBlockEntity {
        public Stone(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.STONE_COBBLEGEN.get(), CobbleGenProperties.STONE, pos, blockState);
        }
    }

    public static class Iron extends CobblegenBlockEntity {
        public Iron(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.IRON_COBBLEGEN.get(), CobbleGenProperties.IRON, pos, blockState);
        }
    }

    public static class Gold extends CobblegenBlockEntity {
        public Gold(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.GOLD_COBBLEGEN.get(), CobbleGenProperties.GOLD, pos, blockState);
        }
    }

    public static class Diamond extends CobblegenBlockEntity {
        public Diamond(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.DIAMOND_COBBLEGEN.get(), CobbleGenProperties.DIAMOND, pos, blockState);
        }
    }

    public static class Netherite extends CobblegenBlockEntity {
        public Netherite(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.NETHERITE_COBBLEGEN.get(), CobbleGenProperties.NETHERITE, pos, blockState);
        }
    }

}
