package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbobb.registry.ComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class JarBlockEntity extends BlockEntity implements ITubeConnectable {
    private final FluidTank tank = new JarFluidTank();

    public JarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntitiesRegistry.JAR.get(), blockPos, blockState);
    }

    @Override
    public boolean isSideTubeConnectable(Direction side) {
        return side == Direction.UP;
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", tank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tank.readFromNBT(registries, tag.getCompound("Tank"));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public IFluidHandler getFluidHandler() {
        return tank;
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getComparatorSignal() {
        return tank.getFluidAmount() * 15 / tank.getCapacity();
    }

    public void onRightClick(Player player, InteractionHand hand, ItemStack item) {
        FluidUtil.interactWithFluidHandler(player, hand, tank);

        if (!level.isClientSide()) {
            if (tank.isEmpty()) {
                player.displayClientMessage(Component.translatable("ftblibrary.empty"), true);
            } else {
                player.displayClientMessage(Component.translatable("ftblibrary.mb", tank.getFluidAmount(), tank.getFluid().getHoverName()), true);
            }
        }
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);

        tank.setFluid(componentInput.getOrDefault(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.EMPTY).copy());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        components.set(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.copyOf(tank.getFluid()));
    }

    private class JarFluidTank extends FluidTank {
        public JarFluidTank() {
            super(8000);
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }
}
