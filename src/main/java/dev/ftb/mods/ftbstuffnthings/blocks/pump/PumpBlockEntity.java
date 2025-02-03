package dev.ftb.mods.ftbstuffnthings.blocks.pump;

import dev.ftb.mods.ftbstuffnthings.Config;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PumpBlockEntity extends AbstractMachineBlockEntity {
    private static final int TICK_RATE = 20;

    // all but down
    private static final List<Direction> OUTPUT_DIRS = List.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    private int timeLeft = 0;
    private int tickCounter = 0;

    public boolean creative = false;
    public Fluid creativeFluid = Fluids.WATER;
    public Item creativeItem = null;

    private final Map<Direction, BlockCapabilityCache<IFluidHandler, Direction>> capabilityCacheMap = new EnumMap<>(Direction.class);

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

        if (++tickCounter >= TICK_RATE) {
            runOneCycle(serverLevel);
        }
    }

    @Override
    protected Optional<ParticleOptions> getActiveParticle() {
        return Optional.of(ParticleTypes.SPLASH);
    }

    private void runOneCycle(ServerLevel serverLevel) {
        tickCounter = 0;

        int totalFilled = 0;
        for (Direction dir : OUTPUT_DIRS) {
            var fluidCache = capabilityCacheMap.computeIfAbsent(dir, k -> BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, getBlockPos().relative(dir), dir.getOpposite()));
            IFluidHandler handler = fluidCache.getCapability();
            if (handler != null) {
                totalFilled += handler.fill(new FluidStack(Fluids.WATER, Config.PUMP_FLUID_TRANSFER.get()), IFluidHandler.FluidAction.EXECUTE);
                handleCreateItemInsertion(handler);
            }
        }

        if (totalFilled > 0) {
            // one unit of pump charge transfers 50mB of water
            if (!creative) {
                timeLeft = Math.max(0, timeLeft - totalFilled / 50);
            }
            updatePumpProgress();
            if (timeLeft == 0) {
                level.setBlock(getBlockPos(), getBlockState()
                        .setValue(AbstractMachineBlock.ACTIVE, false)
                        .setValue(PumpBlock.PROGRESS, PumpBlock.Progress.ZERO), AbstractMachineBlock.UPDATE_ALL);
            }
        }
    }

    private void handleCreateItemInsertion(IFluidHandler handler) {
        if (creative && creativeItem != null && handler instanceof SluiceBlockEntity.SluiceFluidTank sluiceFluidTank) {
            // null side bypasses item IO ability checking
            ItemStackHandler itemHandler = sluiceFluidTank.getOwner().getItemHandler(null);
            if (itemHandler != null) {
                itemHandler.insertItem(0, creativeItem.getDefaultInstance(), false);
            }
        }
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
        int maxCharge = Config.PUMP_MAX_CHARGE.get();

        if (timeLeft >= maxCharge) {
            return false;
        }
        timeLeft = Math.min(maxCharge, timeLeft + Config.PUMP_CHARGEUP_AMOUNT.get());

        updatePumpProgress();
        setChanged();

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
        level.setBlock(getBlockPos(), getBlockState()
                .setValue(AbstractMachineBlock.ACTIVE, true)
                .setValue(PumpBlock.PROGRESS, progress),
                Block.UPDATE_ALL);
    }
}
