package dev.ftb.mods.ftbobb.blocks.pump;

import dev.ftb.mods.ftbobb.blocks.sluice.SluiceBlockEntity;
import dev.ftb.mods.ftbobb.capabilities.PublicReadOnlyFluidTank;
import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class PumpBlockEntity extends BlockEntity {
    public int timeLeft = 0;

    final int checkInterval = 50;
    int checkTimeout = 0;
    boolean foundValidBlocks = false;
    int lastTick = 0;

    public boolean creative = false;
    public Fluid creativeFluid = Fluids.WATER;
    public Item creativeItem = null;

    private Set<BlockPos> targetBlocks = new HashSet<>();

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.PUMP.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T givenBlockEntity) {
        if (!(givenBlockEntity instanceof PumpBlockEntity pumpBlockEntity)) {
            return;
        }

        if (pumpBlockEntity.level == null || (pumpBlockEntity.timeLeft <= 0 && !pumpBlockEntity.creative)) {
            return;
        }

        FluidState fluidState = level.getBlockState(pumpBlockEntity.getBlockPos().below()).getFluidState();

        // No valid fluid source
        if (!pumpBlockEntity.creative && fluidState.isEmpty() || !fluidState.isSource() || !fluidState.is(FluidTags.WATER)) {
            return;
        }

        // Try and find a valid block;
        if (!pumpBlockEntity.foundValidBlocks) {
            // Time to check
            if (pumpBlockEntity.checkTimeout > pumpBlockEntity.checkInterval) {
                for (Direction direction : Direction.values()) {
                    BlockEntity blockEntity = level.getBlockEntity(pumpBlockEntity.getBlockPos().relative(direction));
                    if (blockEntity instanceof SluiceBlockEntity) {
                        pumpBlockEntity.targetBlocks.add(pumpBlockEntity.getBlockPos().relative(direction));
                        pumpBlockEntity.foundValidBlocks = true;
                    }
                }

                pumpBlockEntity.checkTimeout = 0;
            }

            pumpBlockEntity.checkTimeout ++;
            return;
        }

        // Just do it
        if (pumpBlockEntity.creative) {
            for (BlockPos pos : pumpBlockEntity.getTargetBlocks()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity == null) {
                    return;
                }

                pumpBlockEntity.provideFluidToSluice(blockEntity);
                if (pumpBlockEntity.creativeItem != null && blockEntity instanceof SluiceBlockEntity) {
//                    ItemsHandler inventory = ((SluiceBlockEntity) blockEntity).inventory;
//                    if (inventory.getStackInSlot(0).isEmpty()) {
//                        inventory.internalInsert(0, new ItemStack(pumpBlockEntity.creativeItem, 1), false);
//                    }
                }
            }

            // Don't do anything else, creative means creative
            return;
        }

        pumpBlockEntity.lastTick ++;
        if (pumpBlockEntity.lastTick < 20) {
            return;
        }

        pumpBlockEntity.lastTick = 0;

        boolean wasUsed = false;
        for (BlockPos pos : pumpBlockEntity.getTargetBlocks()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) {
                return;
            }

            boolean filled = pumpBlockEntity.provideFluidToSluice(blockEntity);
            if (!wasUsed && filled) {
                wasUsed = true;
            }
        }

        if (!wasUsed) {
            return;
        }

        pumpBlockEntity.timeLeft -= 20;
        PumpBlock.computeStateForProgress(pumpBlockEntity.getBlockState(), pumpBlockEntity.getBlockPos(), pumpBlockEntity.level, pumpBlockEntity.timeLeft);
        if (pumpBlockEntity.timeLeft < 0) {
            pumpBlockEntity.timeLeft = 0;

            level.setBlock(pumpBlockEntity.getBlockPos(), pumpBlockEntity.getBlockState().setValue(PumpBlock.ON_OFF, false).setValue(PumpBlock.PROGRESS, PumpBlock.Progress.ZERO), 3);
        }
    }

    private boolean provideFluidToSluice(BlockEntity blockEntity) {
        boolean didFill = false;

        // It's gone! Poof
        if (!(blockEntity instanceof SluiceBlockEntity)) {
            this.foundValidBlocks = false; // flag to update cache
            this.targetBlocks.clear();
            return false;
        }

        // Give it water!
        PublicReadOnlyFluidTank tank = ((SluiceBlockEntity) blockEntity).getFluidTank();
        if (tank.getFluidAmount() < tank.getCapacity()) {
            int i = tank.internalFill(new FluidStack(this.creative ? this.creativeFluid : Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
            if (i > 0) {
                didFill = true;
            }
            blockEntity.setChanged();
        }

        return didFill;
    }

    public Set<BlockPos> getTargetBlocks() {
        return this.targetBlocks;
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        compound.putInt("time_left", this.timeLeft);
        compound.putBoolean("is_creative", this.creative);

        if (this.creativeFluid != Fluids.WATER) {
            ResourceLocation key = BuiltInRegistries.FLUID.getKey(this.creativeFluid);
            compound.putString("creative_fluid", key.toString());
        }

        if (this.creativeItem != null) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(this.creativeItem);
            compound.putString("creative_item", key.toString());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        this.timeLeft = compound.getInt("time_left");
        this.creative = compound.getBoolean("is_creative");

        if (compound.contains("creative_fluid")) {
            ResourceLocation creativeFluidFromReg = ResourceLocation.tryParse(compound.getString("creative_fluid"));
            if (creativeFluidFromReg != null) {
                this.creativeFluid = BuiltInRegistries.FLUID.get(creativeFluidFromReg);
            }
        }

        if (compound.contains("creative_item")) {
            ResourceLocation creativeItemFromReg = ResourceLocation.tryParse(compound.getString("creative_item"));
            if (creativeItemFromReg != null) {
                this.creativeItem = BuiltInRegistries.ITEM.get(creativeItemFromReg);
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        var data = new CompoundTag();
        this.saveAdditional(data, registries);
        return data;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(pkt.getTag(), lookupProvider);
    }
}
