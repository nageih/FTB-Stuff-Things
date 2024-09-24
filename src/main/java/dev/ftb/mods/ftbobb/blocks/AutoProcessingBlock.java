package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.util.DirectionUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public class AutoProcessingBlock extends Block implements ITubeConnectable {
    public static final VoxelShape SHAPE = box(3, 0, 3, 13, 13, 13);

    public static final EnumMap<Direction,BooleanProperty> CONN_PROPS = Util.make(new EnumMap<>(Direction.class), map -> {
        for (Direction direction : DirectionUtil.VALUES) {
            // note: no connection property for DOWN
            if (direction != Direction.DOWN) {
                map.put(direction, BooleanProperty.create(direction.getName().substring(0, 1)));
            }
        }
    });

    public AutoProcessingBlock() {
        super(Properties.of().mapColor(MapColor.METAL).strength(5F, 6F).sound(SoundType.METAL));

        BlockState state = stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false);
        CONN_PROPS.keySet().forEach(direction -> state.setValue(CONN_PROPS.get(direction), false));
        registerDefaultState(state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(BlockStateProperties.WATERLOGGED);
        CONN_PROPS.keySet().forEach(direction -> builder.add(CONN_PROPS.get(direction)));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return direction != Direction.DOWN && level instanceof Level l ?
                state.setValue(CONN_PROPS.get(direction), ITubeConnectable.canConnect(l, neighborPos, direction.getOpposite())) :
                state;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = defaultBlockState();

        for (Direction direction : CONN_PROPS.keySet()) {
            state = state.setValue(CONN_PROPS.get(direction), ITubeConnectable.canConnect(level, pos.relative(direction), direction.getOpposite()));
        }

        return state.setValue(BlockStateProperties.WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return !state.getValue(BlockStateProperties.WATERLOGGED);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockPos pos1 = pos.below();

        if (level.getBlockState(pos1).is(BlocksRegistry.TEMPERED_JAR.get())) {
            return level.getBlockState(pos1).useWithoutItem(level, player, hitResult.withPosition(pos1));
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isSideTubeConnectable(Direction side) {
        return side != Direction.DOWN;
    }
}
