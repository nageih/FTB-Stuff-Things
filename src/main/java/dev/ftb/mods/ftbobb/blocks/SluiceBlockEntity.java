package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.capabilities.PublicReadOnlyFluidTank;
import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public class SluiceBlockEntity extends BlockEntity {
    private Lazy<PublicReadOnlyFluidTank> fluidTank = Lazy.of(() -> new PublicReadOnlyFluidTank(this, 10_000));

    public SluiceBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState blockState) {
        super(entity, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        fluidTank.get().writeToNBT(registries, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        fluidTank.get().readFromNBT(registries, tag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        var data = new CompoundTag();
        this.saveAdditional(data, registries);
        return data;
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

    public PublicReadOnlyFluidTank getFluidTank() {
        return fluidTank.get();
    }

    //#region BlockEntity types
    public static class Oak extends SluiceBlockEntity {
        public Oak(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.OAK_SLUICE.get(), pos, blockState);
        }
    }

    public static class Iron extends SluiceBlockEntity {
        public Iron(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.IRON_SLUICE.get(), pos, blockState);
        }
    }

    public static class Diamond extends SluiceBlockEntity {
        public Diamond(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.DIAMOND_SLUICE.get(), pos, blockState);
        }
    }

    public static class Netherite extends SluiceBlockEntity {
        public Netherite(BlockPos pos, BlockState blockState) {
            super(BlockEntitiesRegistry.NETHERITE_SLUICE.get(), pos, blockState);
        }
    }
    //#endregion
}
