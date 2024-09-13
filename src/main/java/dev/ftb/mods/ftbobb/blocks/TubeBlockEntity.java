package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbobb.util.DirectionUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TubeBlockEntity extends BlockEntity {
    public static final ModelProperty<Integer> CONNECTION_PROPERTY = new ModelProperty<>();

    private int sidesClosed = 0;    // toggleable by player clicking with empty hand
    private int sidesConnected = 0; // updated when tube placed or neighbour updates

    public TubeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntitiesRegistry.TUBE.get(), blockPos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        sidesClosed = tag.getInt("sides_closed");
        sidesConnected = tag.getInt("sides_connected");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (sidesClosed != 0) tag.putInt("sides_closed", sidesClosed);
        if (sidesConnected != 0) tag.putInt("sides_connected", sidesConnected);
    }

    public int getShapeCacheKey() {
        return Objects.hash(sidesClosed, sidesConnected);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        // server side, chunk sending
        return Util.make(new CompoundTag(), tag -> saveAdditional(tag, registries));
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        // client side, chunk sending
        super.handleUpdateTag(tag, lookupProvider);

        requestModelDataUpdate();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // server side, block update
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        // client side, block update
        super.onDataPacket(net, pkt, lookupProvider);

        requestModelDataUpdate();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(CONNECTION_PROPERTY, sidesConnected)
                .build();
    }

    public boolean isSideClosed(Direction dir) {
        return DirectionUtil.getDirectionBit(sidesClosed, dir);
    }

    public void setSideClosed(Direction dir, boolean closed) {
        if (!level.isClientSide) {
            int prevSidesClosed = sidesClosed;
            sidesClosed = DirectionUtil.setDirectionBit(sidesClosed, dir, closed);
            if (sidesClosed != prevSidesClosed) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    public boolean isSideConnected(Direction dir) {
        return DirectionUtil.getDirectionBit(sidesConnected, dir);
    }

    public boolean updateSide(Direction dir, boolean updateNow) {
        int prevSidesConnected = sidesConnected;
        BlockPos pos1 = getBlockPos().relative(dir);
        Direction dir1 = dir.getOpposite();
        boolean connectable = !isSideClosed(dir) &&
                (level.getCapability(Capabilities.ItemHandler.BLOCK, pos1, dir1) != null
                        || level.getCapability(Capabilities.FluidHandler.BLOCK, pos1, dir1) != null
                        || level.getBlockEntity(pos1) instanceof TubeBlockEntity tube && !tube.isSideClosed(dir1));
        sidesConnected = DirectionUtil.setDirectionBit(sidesConnected, dir, connectable);

        if (sidesConnected != prevSidesConnected) {
            if (updateNow) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
            setChanged();
            return true;
        }
        return false;
    }

    public void updateConnectedSides() {
        boolean changed = false;
        for (Direction dir : DirectionUtil.VALUES) {
            if (updateSide(dir, false)) {
                changed = true;
            }
        }

        if (changed) {
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
