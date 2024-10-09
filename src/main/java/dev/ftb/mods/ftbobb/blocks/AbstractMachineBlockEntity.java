package dev.ftb.mods.ftbobb.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider, FluidEnergyProvider, ProgressProvider {
    public AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // TODO there's a lot more we could move in here, e.g. progress ticking etc.

    public abstract ContainerData getContainerData();

    public abstract void syncFluidTank();

    @Nullable
    public abstract IItemHandler getItemHandler();

    @Nullable
    public abstract IFluidHandler getFluidHandler();

    @Nullable
    public abstract IEnergyStorage getEnergyHandler();

    public void tickClient() {
        if (getBlockState().hasProperty(AbstractMachineBlock.ACTIVE)
                && getBlockState().getValue(AbstractMachineBlock.ACTIVE)
                && level.random.nextInt(5) == 0) {
            Vec3 vec = Vec3.upFromBottomCenterOf(getBlockPos(), 1.05);
            level.addParticle(ParticleTypes.SMOKE, vec.x, vec.y, vec.z, 0, 0, 0);
        }
    }

    public abstract void tickServer();

    public void dropItemContents() {
        IItemHandler handler = getItemHandler();
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {
                Block.popResource(level, getBlockPos(), handler.getStackInSlot(i));
            }
        }
    }

    protected final void setActive(boolean active) {
        boolean curActive = getBlockState().getValue(AbstractMachineBlock.ACTIVE);
        if (active != curActive) {
            level.setBlock(getBlockPos(), getBlockState().setValue(AbstractMachineBlock.ACTIVE, active), Block.UPDATE_CLIENTS);
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event, BlockEntityType<? extends AbstractMachineBlockEntity> machine) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, machine, (blockEntity, side) -> blockEntity.getItemHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, machine, (blockEntity, side) -> blockEntity.getFluidHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, machine, (blockEntity, side) -> blockEntity.getEnergyHandler());
    }
}
