package dev.ftb.mods.ftbstuffnthings.blocks.cobblegen;

import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlock;
import dev.ftb.mods.ftbstuffnthings.blocks.hammer.AutoHammerBlock;
import dev.ftb.mods.ftbstuffnthings.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CobblegenBlock extends Block implements EntityBlock {

    private final CobbleGenProperties props;


    public CobblegenBlock(CobbleGenProperties props) {
        super(Properties.of().mapColor(MapColor.STONE).strength(1F, 1F));

        this.props = props;

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.ENABLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.ENABLED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.ENABLED, true);
    }

    @Override
    public VoxelShape getVisualShape(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
        return Shapes.empty();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof CobblegenBlockEntity tickable) {
                if (!level1.isClientSide()) {
                    tickable.tickServer();
                }
            }
        };
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        boolean hasSignal = !level.hasNeighborSignal(pos);
        if (hasSignal != state.getValue(BlockStateProperties.ENABLED)) {
            var newState = state.setValue(BlockStateProperties.ENABLED, hasSignal);
            level.setBlock(pos, newState, Block.UPDATE_ALL);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return props.createBlockEntity(blockPos, blockState);
    }

}
