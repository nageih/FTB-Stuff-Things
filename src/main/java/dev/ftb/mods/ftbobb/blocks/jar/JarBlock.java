package dev.ftb.mods.ftbobb.blocks.jar;

import dev.ftb.mods.ftbobb.blocks.SerializableComponentsProvider;
import dev.ftb.mods.ftbobb.registry.ComponentsRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import dev.ftb.mods.ftbobb.util.MiscUtil;
import dev.ftb.mods.ftbobb.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JarBlock extends Block implements EntityBlock, SerializableComponentsProvider {
    public static final VoxelShape SHAPE = VoxelShapeUtils.or(
            box(3, 0, 3, 13, 13, 13),
            box(6, 13, 6, 10, 14, 10),
            box(5, 14, 5, 11, 16, 11)
    );

    public JarBlock() {
        super(Properties.of().sound(SoundType.BONE_BLOCK).strength(0.6F).noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new JarBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof JarBlockEntity jar ? jar.getComparatorSignal() : 0;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            level.updateNeighbourForOutputSignal(pos, this);
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack item = player.getItemInHand(hand);

        if (hitResult.getDirection() == Direction.UP && item.getItem() == ItemsRegistry.TUBE.get()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);

            if (tileEntity instanceof JarBlockEntity jar) {
                jar.onRightClick(player, hand, item);
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void addSerializableComponents(List<DataComponentType<?>> list) {
        list.add(ComponentsRegistry.STORED_FLUID.get());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        FluidStack content = stack.getOrDefault(ComponentsRegistry.STORED_FLUID, SimpleFluidContent.EMPTY).copy();
        if (!content.isEmpty()) {
            tooltipComponents.add(MiscUtil.makeFluidStackDesc(content));
        }
    }
}
