package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import dev.ftb.mods.ftbobb.temperature.Temperature;
import dev.ftb.mods.ftbobb.temperature.TemperatureAndEfficiency;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TemperedJarBlock extends JarBlock {
    public static final EnumProperty<Temperature> TEMPERATURE = EnumProperty.create("temperature", Temperature.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public TemperedJarBlock() {
        super();

        registerDefaultState(getStateDefinition().any().setValue(TEMPERATURE, Temperature.NONE).setValue(ACTIVE, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TemperedJarBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(TEMPERATURE, ACTIVE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        TemperatureAndEfficiency tempEff = TemperatureAndEfficiency.fromLevel(context.getLevel(), context.getClickedPos().below());
        return defaultBlockState().setValue(TEMPERATURE, tempEff.temperature());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN) {
            if (level.getBlockEntity(pos) instanceof TemperedJarBlockEntity jar) {
                jar.clearCache();
            }
        }

        return direction == Direction.DOWN && level instanceof Level l ?
                state.setValue(TEMPERATURE, TemperatureAndEfficiency.fromLevel(l, neighborPos).temperature()) :
                state;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack item = player.getItemInHand(hand);

        if (hitResult.getDirection() == Direction.UP && item.getItem() == ItemsRegistry.TUBE.get()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);

            if (entity instanceof TemperedJarBlockEntity jar) {
                player.openMenu(jar, buf -> {
                    buf.writeBlockPos(pos);
                    jar.writeRecipeId(buf);
                });
//                NetworkHooks.openGui((ServerPlayer) player, new MenuProvider() {
//                    @Override
//                    public Component getDisplayName() {
//                        return new TranslatableComponent("block.ftbjarmod.tempered_jar");
//                    }
//
//                    @Override
//                    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player1) {
//                        return new JarMenu(id, playerInv, (TemperedJarBlockEntity) entity, (TemperedJarBlockEntity) entity);
//                    }
//                }, buf -> ((TemperedJarBlockEntity) entity).writeMenu(player, buf));
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
}
