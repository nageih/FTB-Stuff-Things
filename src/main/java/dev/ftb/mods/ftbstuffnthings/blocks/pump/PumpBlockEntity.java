package dev.ftb.mods.ftbstuffnthings.blocks.pump;

import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlock;
import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlockEntity;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceBlockEntity;
import dev.ftb.mods.ftbstuffnthings.registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PumpBlockEntity extends AbstractMachineBlockEntity {
    private static final int CHECK_INTERVAL = 50;
    private static final int TICK_RATE = 20;

    public static final int MAX_PUMP_CHARGE = 6000;
    public static final int PUMP_CHARGE_AMOUNT = 14;

    private int timeLeft= 0;
    private int checkTimeout = CHECK_INTERVAL;
    private boolean foundValidSluices = false;
    private int tickCounter = 0;

    public boolean creative = false;
    public Fluid creativeFluid = Fluids.WATER;
    public Item creativeItem = null;

    private final Set<BlockPos> targetBlocks = new HashSet<>();

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.PUMP.get(), pos, state);
    }

    @Override
    public void tickServer(ServerLevel serverLevel) {
        if (timeLeft <= 0 && !creative) {
            return;
        }

        FluidState fluidState = serverLevel.getBlockState(getBlockPos().below()).getFluidState();

        // No valid fluid source
        if (!creative && fluidState.isEmpty() || !fluidState.isSource() || !fluidState.is(FluidTags.WATER)) {
            return;
        }

        if (!foundValidSluices) {
            maybeSearchForSluices();
        } else if (creative) {
            handleCreativeInsertion();
        } else if (++tickCounter >= TICK_RATE) {
            runOneCycle();
        }
    }

    @Override
    protected Optional<ParticleOptions> getActiveParticle() {
        return Optional.of(ParticleTypes.SPLASH);
    }

    private void maybeSearchForSluices() {
        if (checkTimeout++ > CHECK_INTERVAL) {
            for (Direction direction : Direction.values()) {
                BlockEntity blockEntity = level.getBlockEntity(getBlockPos().relative(direction));
                if (blockEntity instanceof SluiceBlockEntity) {
                    targetBlocks.add(getBlockPos().relative(direction));
                    foundValidSluices = true;
                }
            }
            checkTimeout = 0;
        }
    }

    private void handleCreativeInsertion() {
        Set<BlockPos> invalidPos = new HashSet<>();
        for (BlockPos pos : getTargetBlocks()) {
            if (level.getBlockEntity(pos) instanceof SluiceBlockEntity sluice) {
                provideFluidToSluice(sluice);
                if (creativeItem != null) {
                    IItemHandler handler = sluice.getItemHandler();
                    if (handler != null && handler.getStackInSlot(0).isEmpty()) {
                        handler.insertItem(0, new ItemStack(creativeItem), false);
                    }
                }
            } else {
                invalidPos.add(pos);
            }
        }
        invalidPos.forEach(targetBlocks::remove);
    }

    private void runOneCycle() {
        tickCounter = 0;

        boolean didWork = false;
        Set<BlockPos> invalidPos = new HashSet<>();
        for (BlockPos pos : getTargetBlocks()) {
            if (level.getBlockEntity(pos) instanceof SluiceBlockEntity sbe) {
                if (provideFluidToSluice(sbe)) {
                    didWork = true;
                }
            } else {
                invalidPos.add(pos);
            }
        }
        invalidPos.forEach(targetBlocks::remove);

        if (didWork) {
            timeLeft = Math.max(0, timeLeft - 20);
            updatePumpProgress();
            if (timeLeft == 0) {
                level.setBlock(getBlockPos(), getBlockState()
                        .setValue(AbstractMachineBlock.ACTIVE, false)
                        .setValue(PumpBlock.PROGRESS, PumpBlock.Progress.ZERO), AbstractMachineBlock.UPDATE_ALL);
            }
        }
    }

    public void scanForSluices() {
        foundValidSluices = false;
    }

    private boolean provideFluidToSluice(SluiceBlockEntity sluice) {
        boolean didWork = false;

        FluidTank tank = sluice.getFluidTank();
        if (tank.getFluidAmount() < tank.getCapacity()) {
            int filled = tank.fill(new FluidStack(this.creative ? this.creativeFluid : Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
            if (filled > 0) {
                didWork = true;
            }
        }

        return didWork;
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

    @Override
    public @Nullable IItemHandler getItemHandler(@Nullable Direction side) {
        return null;
    }

    @Override
    public @Nullable IFluidHandler getFluidHandler(@Nullable Direction side) {
        return null;
    }

    @Override
    public @Nullable IEnergyStorage getEnergyHandler(@Nullable Direction side) {
        return null;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public boolean windUp() {
        if (timeLeft >= MAX_PUMP_CHARGE) {
            return false;
        }
        timeLeft = Math.min(MAX_PUMP_CHARGE, timeLeft + PUMP_CHARGE_AMOUNT);

        updatePumpProgress();
        setChanged();
//        level.sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_ALL);

        return true;
    }

    private void updatePumpProgress() {
        if (!getBlockState().getValue(AbstractMachineBlock.ACTIVE) && timeLeft > 0) {
            setPumpProgress(PumpBlock.Progress.ZERO);
        } else {
            PumpBlock.Progress value = getBlockState().getValue(PumpBlock.PROGRESS);
            if (timeLeft < 1200 && value != PumpBlock.Progress.TWENTY) {
                setPumpProgress(PumpBlock.Progress.TWENTY);
            } else if (timeLeft >= 1200 && timeLeft < 2400 && value != PumpBlock.Progress.FORTY) {
                setPumpProgress(PumpBlock.Progress.FORTY);
            } else if (timeLeft >= 2400 && timeLeft < 3600 && value != PumpBlock.Progress.SIXTY) {
                setPumpProgress(PumpBlock.Progress.SIXTY);
            } else if (timeLeft >= 3600 && timeLeft < 4800 && value != PumpBlock.Progress.EIGHTY) {
                setPumpProgress(PumpBlock.Progress.EIGHTY);
            } else if (timeLeft >= 4800 && timeLeft < 5500 && value != PumpBlock.Progress.HUNDRED) {
                setPumpProgress(PumpBlock.Progress.HUNDRED);
            }
        }
    }

    private void setPumpProgress(PumpBlock.Progress progress) {
        level.setBlock(getBlockPos(), getBlockState().setValue(AbstractMachineBlock.ACTIVE, true).setValue(PumpBlock.PROGRESS, progress), Block.UPDATE_ALL);
    }
}
