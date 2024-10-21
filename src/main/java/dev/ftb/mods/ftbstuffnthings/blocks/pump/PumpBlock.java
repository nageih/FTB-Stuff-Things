package dev.ftb.mods.ftbstuffnthings.blocks.pump;

import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlock;
import dev.ftb.mods.ftbstuffnthings.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ModDamageSources;
import dev.ftb.mods.ftbstuffnthings.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class PumpBlock extends AbstractMachineBlock implements EntityBlock {
    public enum Progress implements StringRepresentable {
        ZERO(0),
        TWENTY(20),
        FORTY(40),
        SIXTY(60),
        EIGHTY(80),
        HUNDRED(100);

        private final int percentage;

        Progress(int percentage) {
            this.percentage = percentage;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }

    private static final VoxelShape NORTH = VoxelShapeUtils.or(
            box(0, 0, 0, 16, 10, 16),
            box(3, 10, 6, 13, 13, 9),
            box(6, 10, 0, 10, 12, 2),
            box(0, 15, 8, 16, 16, 16),
            box(1, 10, 9, 15, 15, 15),
            box(0, 10, 15, 1, 15, 16),
            box(0, 10, 8, 1, 15, 9),
            box(15, 10, 8, 16, 15, 9),
            box(15, 10, 15, 16, 15, 16),
            box(2.5, 4, 3, 13.5, 15, 5),
            box(7, 9, 2, 9, 11, 6)
    );
    private static final VoxelShape EAST = VoxelShapeUtils.rotateY(NORTH, 90);
    private static final VoxelShape SOUTH = VoxelShapeUtils.rotateY(EAST, 90);
    private static final VoxelShape WEST = VoxelShapeUtils.rotateY(SOUTH, 90);
    private static final Map<Direction,VoxelShape> SHAPES = new EnumMap<>(Map.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST
    ));

    public static final EnumProperty<Progress> PROGRESS = EnumProperty.create("progress", Progress.class);

    public PumpBlock() {
        super(Properties.of().sound(SoundType.STONE).strength(1f, 1f));

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(PROGRESS, Progress.ZERO)
                .setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(PROGRESS);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player instanceof FakePlayer) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof PumpBlockEntity pump)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            if (pump.timeLeft < 6000) {
                pump.timeLeft += 14;
                if (pump.timeLeft > 6000) {
                    pump.timeLeft = 6000;
                }

                computeStateForProgress(state, pos, level, pump.timeLeft);
                sendTileUpdate(level, pos, state, pump);
            } else {
                player.hurt(level.damageSources().source(ModDamageSources.STATIC_ELECTRIC, player), 2);
                if (player.getHealth() - 2 < 0) {
                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                    if (lightning != null) {
                        lightning.moveTo(Vec3.atBottomCenterOf(player.blockPosition()));
                        lightning.setVisualOnly(true);
                        level.addFreshEntity(lightning);
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof PumpBlockEntity pump)) {
            return ItemInteractionResult.FAIL;
        }

        if (pump.creative) {
            ItemStack itemInHand = player.getItemInHand(hand);

            ItemInteractionResult result = ItemInteractionResult.FAIL;
            // Try a normal bucket
            if (!itemInHand.isEmpty()) {
                if (itemInHand.getItem() instanceof BucketItem bucketItem) {
                    pump.creativeFluid = bucketItem.content;
                    sendTileUpdate(level, pos, state, pump);
                    result = ItemInteractionResult.SUCCESS;
                } else {
                    IFluidHandlerItem capability = itemInHand.getCapability(Capabilities.FluidHandler.ITEM);
                    if (capability != null) {
                        // Take the first one
                        pump.creativeFluid = capability.getFluidInTank(0).getFluid();
                        sendTileUpdate(level, pos, state, pump);
                        result = ItemInteractionResult.SUCCESS;
                    }
                }
            }

            if (result.consumesAction()) {
                return result;
            }

            pump.creativeItem = itemInHand.getItem();
            if (!level.isClientSide) {
                sendTileUpdate(level, pos, state, pump);
            }

            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private void sendTileUpdate(Level level, BlockPos pos, BlockState state, PumpBlockEntity tile) {
        tile.setChanged();
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PumpBlockEntity(blockPos, blockState);
    }

    public static void computeStateForProgress(BlockState state, BlockPos pos, Level level, int timeLeft) {
        if (!state.getValue(ACTIVE) && timeLeft > 0) {
            level.setBlock(pos, state.setValue(ACTIVE, true).setValue(PumpBlock.PROGRESS, Progress.ZERO), 3);
        } else {
            Progress value = state.getValue(PROGRESS);
            if (timeLeft < 1200 && value != Progress.TWENTY)
                level.setBlock(pos, state.setValue(ACTIVE, true).setValue(PumpBlock.PROGRESS, Progress.TWENTY), 3);
            else if (timeLeft >= 1200 && timeLeft < 2400 && value != Progress.FORTY)
                level.setBlock(pos, state.setValue(ACTIVE, true).setValue(PumpBlock.PROGRESS, Progress.FORTY), 3);
            else if (timeLeft >= 2400 && timeLeft < 3600 && value != Progress.SIXTY)
                level.setBlock(pos, state.setValue(ACTIVE, true).setValue(PumpBlock.PROGRESS, Progress.SIXTY), 3);
            else if (timeLeft >= 3600 && timeLeft < 4800 && value != Progress.EIGHTY)
                level.setBlock(pos, state.setValue(ACTIVE, true).setValue(PumpBlock.PROGRESS, Progress.EIGHTY), 3);
            else if (timeLeft >= 4800 && timeLeft < 5500 && value != Progress.HUNDRED)
                level.setBlock(pos, state.setValue(ACTIVE, true).setValue(PumpBlock.PROGRESS, Progress.HUNDRED), 3);
        }
    }

    public VoxelShape getVisualShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return SHAPES.getOrDefault(dir, NORTH);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        level.getBlockEntity(pos, BlockEntitiesRegistry.PUMP.get()).ifPresent(pump -> {
            pump.scanForSluices();
        });
    }
}
