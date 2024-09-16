package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TemperedJarBlockEntity extends BlockEntity implements MenuProvider, ITubeConnectable {
    private ResourceLocation recipeId;

    public TemperedJarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesRegistry.TEMPERED_JAR.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ftbobb.tempered_jar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        // TODO
        return null;
    }

    public void clearCache() {

    }

    public void writeRecipeId(RegistryFriendlyByteBuf buf) {
        buf.writeOptional(Optional.ofNullable(recipeId), FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public boolean isSideTubeConnectable(Direction side) {
        return side == Direction.UP;
    }
}
