package dev.ftb.mods.ftbstuffnthings.blocks.sluice;

import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftbstuffnthings.FTBStuffTags;
import dev.ftb.mods.ftbstuffnthings.blocks.SerializableComponentsProvider;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.SluiceRecipe;
import dev.ftb.mods.ftbstuffnthings.items.MeshItem;
import dev.ftb.mods.ftbstuffnthings.items.MeshType;
import dev.ftb.mods.ftbstuffnthings.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SluiceBlock extends Block implements EntityBlock, SerializableComponentsProvider {
    public static final EnumProperty<MeshType> MESH = EnumProperty.create("mesh", MeshType.class);
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    private static final VoxelShape NORTH_BODY_SHAPE = Stream.of(Block.box(12.5, 0, 0, 14.5, 1, 1), Block.box(1.5, 0, 13.5, 3.5, 1, 15.5), Block.box(12.5, 0, 13.5, 14.5, 1, 15.5), Block.box(1.5, 0, 0, 3.5, 1, 1), Block.box(1, 1, 0, 15, 2, 16), Block.box(14, 2, 0, 15, 8, 16), Block.box(1, 2, 0, 2, 8, 16), Block.box(2, 5, 0, 14, 8, 1), Block.box(2, 2, 15, 14, 8, 16), Block.box(2, 2, 0, 14, 2.5, 1), Block.box(2, 7, 1, 14, 12, 2), Block.box(2, 7, 14, 14, 12, 15), Block.box(13, 7, 2, 14, 12, 14), Block.box(2, 7, 2, 3, 12, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape EAST_BODY_SHAPE = Stream.of(Block.box(15, 0, 12.5, 16, 1, 14.5), Block.box(0.5, 0, 1.5, 2.5, 1, 3.5), Block.box(0.5, 0, 12.5, 2.5, 1, 14.5), Block.box(15, 0, 1.5, 16, 1, 3.5), Block.box(0, 1, 1, 16, 2, 15), Block.box(0, 2, 14, 16, 8, 15), Block.box(0, 2, 1, 16, 8, 2), Block.box(15, 5, 2, 16, 8, 14), Block.box(0, 2, 2, 1, 8, 14), Block.box(15, 2, 2, 16, 2.5, 14), Block.box(14, 7, 2, 15, 12, 14), Block.box(1, 7, 2, 2, 12, 14), Block.box(2, 7, 13, 14, 12, 14), Block.box(2, 7, 2, 14, 12, 3)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape SOUTH_BODY_SHAPE = Stream.of(Block.box(1.5, 0, 15, 3.5, 1, 16), Block.box(12.5, 0, 0.5, 14.5, 1, 2.5), Block.box(1.5, 0, 0.5, 3.5, 1, 2.5), Block.box(12.5, 0, 15, 14.5, 1, 16), Block.box(1, 1, 0, 15, 2, 16), Block.box(1, 2, 0, 2, 8, 16), Block.box(14, 2, 0, 15, 8, 16), Block.box(2, 5, 15, 14, 8, 16), Block.box(2, 2, 0, 14, 8, 1), Block.box(2, 2, 15, 14, 2.5, 16), Block.box(2, 7, 14, 14, 12, 15), Block.box(2, 7, 1, 14, 12, 2), Block.box(2, 7, 2, 3, 12, 14), Block.box(13, 7, 2, 14, 12, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape WEST_BODY_SHAPE = Stream.of(Block.box(0, 0, 1.5, 1, 1, 3.5), Block.box(13.5, 0, 12.5, 15.5, 1, 14.5), Block.box(13.5, 0, 1.5, 15.5, 1, 3.5), Block.box(0, 0, 12.5, 1, 1, 14.5), Block.box(0, 1, 1, 16, 2, 15), Block.box(0, 2, 1, 16, 8, 2), Block.box(0, 2, 14, 16, 8, 15), Block.box(0, 5, 2, 1, 8, 14), Block.box(15, 2, 2, 16, 8, 14), Block.box(0, 2, 2, 1, 2.5, 14), Block.box(1, 7, 2, 2, 12, 14), Block.box(14, 7, 2, 15, 12, 14), Block.box(2, 7, 2, 14, 12, 3), Block.box(2, 7, 13, 14, 12, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape NORTH_FRONT_SHAPE = Stream.of(Block.box(2, 1.5, 12, 14, 2.5, 13), Block.box(1, 2, 0, 2, 4, 16), Block.box(2, 1.5, 8, 14, 2.5, 9), Block.box(2, 1.5, 4, 14, 2.5, 5), Block.box(1, 1, 0, 15, 2, 16), Block.box(14, 2, 0, 15, 4, 16), Block.box(12.5, 0, 0.5, 14.5, 1, 2.5), Block.box(1.5, 0, 0.5, 3.5, 1, 2.5), Block.box(1.5, 0, 15, 3.5, 1, 16), Block.box(12.5, 0, 15, 14.5, 1, 16)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape EAST_FRONT_SHAPE = Stream.of(Block.box(3, 1.5, 2, 4, 2.5, 14), Block.box(0, 2, 1, 16, 4, 2), Block.box(7, 1.5, 2, 8, 2.5, 14), Block.box(11, 1.5, 2, 12, 2.5, 14), Block.box(0, 1, 1, 16, 2, 15), Block.box(0, 2, 14, 16, 4, 15), Block.box(13.5, 0, 12.5, 15.5, 1, 14.5), Block.box(13.5, 0, 1.5, 15.5, 1, 3.5), Block.box(0, 0, 1.5, 1, 1, 3.5), Block.box(0, 0, 12.5, 1, 1, 14.5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape SOUTH_FRONT_SHAPE = Stream.of(Block.box(2, 1.5, 3, 14, 2.5, 4), Block.box(14, 2, 0, 15, 4, 16), Block.box(2, 1.5, 7, 14, 2.5, 8), Block.box(2, 1.5, 11, 14, 2.5, 12), Block.box(1, 1, 0, 15, 2, 16), Block.box(1, 2, 0, 2, 4, 16), Block.box(1.5, 0, 13.5, 3.5, 1, 15.5), Block.box(12.5, 0, 13.5, 14.5, 1, 15.5), Block.box(12.5, 0, 0, 14.5, 1, 1), Block.box(1.5, 0, 0, 3.5, 1, 1)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private static final VoxelShape WEST_FRONT_SHAPE = Stream.of(Block.box(12, 1.5, 2, 13, 2.5, 14), Block.box(0, 2, 14, 16, 4, 15), Block.box(8, 1.5, 2, 9, 2.5, 14), Block.box(4, 1.5, 2, 5, 2.5, 14), Block.box(0, 1, 1, 16, 2, 15), Block.box(0, 2, 1, 16, 4, 2), Block.box(0.5, 0, 1.5, 2.5, 1, 3.5), Block.box(0.5, 0, 12.5, 2.5, 1, 14.5), Block.box(15, 0, 12.5, 16, 1, 14.5), Block.box(15, 0, 1.5, 16, 1, 3.5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final Map<Direction, Pair<VoxelShape, VoxelShape>> SHAPES = Map.of(
            Direction.NORTH, Pair.of(NORTH_BODY_SHAPE, NORTH_FRONT_SHAPE),
            Direction.EAST, Pair.of(EAST_BODY_SHAPE, EAST_FRONT_SHAPE),
            Direction.SOUTH, Pair.of(SOUTH_BODY_SHAPE, SOUTH_FRONT_SHAPE),
            Direction.WEST, Pair.of(WEST_BODY_SHAPE, WEST_FRONT_SHAPE)
    );

    private final SNBTConfig config;

    public SluiceBlock(SNBTConfig config) {
        super(Properties.of().sound(SoundType.METAL).strength(0.9F));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(MESH, MeshType.EMPTY)
                .setValue(PART, Part.MAIN)
                .setValue(HORIZONTAL_FACING, Direction.NORTH));

        this.config = config;
    }

    public SNBTConfig getProps() {
        return config;
    }


    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return state.getValue(PART) == Part.FUNNEL ? List.of() : super.getDrops(state, params);
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(HORIZONTAL_FACING);
        if (!SHAPES.containsKey(direction)) {
            // HOW?!
            return Shapes.empty();
        }

        Pair<VoxelShape, VoxelShape> bodyFrontShapes = SHAPES.get(direction);
        return state.getValue(PART) == Part.MAIN
                ? bodyFrontShapes.getKey()
                : bodyFrontShapes.getValue();
    }

    @Override
    @Deprecated
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            if (state.getValue(MESH) != MeshType.EMPTY) {
                ItemStack current = state.getValue(MESH).getItemStack();
                level.setBlock(pos, state.setValue(MESH, MeshType.EMPTY), 3);

                if (!level.isClientSide()) {
                    ItemHandlerHelper.giveItemToPlayer(player, current);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(PART) == Part.FUNNEL) {
            return ItemInteractionResult.FAIL;
        }

        // Get the sluice tile entity
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (!(tileEntity instanceof SluiceBlockEntity sluice)) {
            return ItemInteractionResult.FAIL;
        }

        if (stack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.is(FTBStuffTags.Items.MESHES)) {
            MeshType type = ((MeshItem) stack.getItem()).mesh;

            ItemStack current = state.getValue(MESH).getItemStack();
            level.setBlock(pos, state.setValue(MESH, type), 3);
            if (!player.isCreative()) {
                stack.shrink(1);

                if (!level.isClientSide()) {
                    ItemHandlerHelper.giveItemToPlayer(player, current);
                }

            }
//            sluice.clearCache();
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (stack.getItem() instanceof BucketItem || stack.getCapability(Capabilities.FluidHandler.ITEM) != null) {
            if (!level.isClientSide()) {
                FluidUtil.interactWithFluidHandler(player, hand, sluice.getFluidTank());
            }

            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }


        // Right, the player is trying to insert an item into the sluice
        Optional<RecipeHolder<SluiceRecipe>> recipeFor = sluice.getRecipeFor(stack);
        boolean insertResult = recipeFor.map(recipe -> {
            ItemStackHandler itemStackHandler = sluice.getInputInventory().get();
            if (!itemStackHandler.getStackInSlot(0).isEmpty()) {
                return false;
            }

            itemStackHandler.insertItem(0, stack.copyWithCount(1), false);
            sluice.setChanged();

            stack.shrink(1);

            return true;
        }).orElse(false);

        if (insertResult) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MESH, PART, HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos offsetPos = context.getClickedPos().relative(context.getHorizontalDirection().getOpposite());
        return context.getLevel().getBlockState(offsetPos).canBeReplaced(context)
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(PART, Part.MAIN)
                : null;
    }

    @Override
    @Deprecated
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
        return super.updateShape(state, facing, facingState, world, pos, facingPos);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        Direction direction = state.getValue(HORIZONTAL_FACING);

        // If you break the funnel, reject and break the main block for the player
        if (state.getValue(PART) == Part.FUNNEL) {
            BlockPos endPos = pos.relative(direction.getOpposite());
            level.destroyBlock(endPos, !level.isClientSide);
            return false;
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    @Deprecated
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Direction direction = state.getValue(HORIZONTAL_FACING);
            BlockPos endPos = pos.relative(state.getValue(PART) == Part.FUNNEL
                    ? direction.getOpposite()
                    : direction);
            BlockState endState = world.getBlockState(endPos);

            // Don't act on the funnel
            if (state.getValue(PART) != Part.FUNNEL) {
                BlockEntity tileEntity = world.getBlockEntity(pos);

//                if (tileEntity instanceof SluiceBlockEntity) {
//                    SluiceBlockEntity sluice = (SluiceBlockEntity) tileEntity;
//                    popResource(world, pos, sluice.inventory.getStackInSlot(0));
//                    world.updateNeighbourForOutputSignal(pos, this);
//
//                    // Drop the upgrade inventory
//                    for (int i = 0; i < sluice.upgradeInventory.getSlots(); i++) {
//                        popResource(world, pos, sluice.upgradeInventory.getStackInSlot(i));
//                    }
//                }

                world.removeBlock(endPos, false);
                popResource(world, pos, state.getValue(MESH).getItemStack());

                super.onRemove(state, world, pos, newState, isMoving);
            }
        } else {
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        boolean isShift = Screen.hasShiftDown();

        // TOOD: Translate
        tooltip.add(Component.empty()
                .append(Component.literal(" (Shift)").withStyle(isShift ? ChatFormatting.DARK_GRAY : ChatFormatting.GRAY))
                .withStyle(ChatFormatting.BLUE));

        if (isShift) {
//            tooltip.add(Component.translatable("ftbsluice.properties.processing_time",
//                    new TextComponent(props.timeMod.get() + "").withStyle(TextUtil.COLOUR_HIGHLIGHT)).withStyle(ChatFormatting.GRAY));
//            tooltip.add(Component.translatable("ftbsluice.properties.fluid_usage",
//                    new TextComponent(props.fluidMod.get() + "").withStyle(TextUtil.COLOUR_HIGHLIGHT)).withStyle(ChatFormatting.GRAY));
//            tooltip.add(Component.translatable("ftbsluice.properties.tank",
//                    new TextComponent(props.tankCap.get() + "").withStyle(TextUtil.COLOUR_HIGHLIGHT)).withStyle(ChatFormatting.GRAY));

//            tooltip.add(Component.translatable("ftbsluice.properties.auto",
//                    Component.translatable("ftbsluice.properties.auto.item").withStyle(props.allowsIO.get() ? ChatFormatting.BLUE : ChatFormatting.BLUE),
//                    Component.translatable("ftbsluice.properties.auto.fluid").withStyle(props.allowsTank.get() ? ChatFormatting.BLUE : ChatFormatting.BLUE)
//            ).withStyle(ChatFormatting.GRAY));

//            if (props.upgradeable.get()) {
//                tooltip.add(Component.translatable("ftbsluice.properties.upgradeable").withStyle(ChatFormatting.BLUE));
//            }
        } else {
//            tooltip.add(Component.translatable("ftbsluice.tooltip." + this.getName()).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
        super.setPlacedBy(level, pos, state, entity, item);
        if (!level.isClientSide) {
            BlockPos lv = pos.relative(state.getValue(HORIZONTAL_FACING));
            level.setBlock(lv, state.setValue(PART, Part.FUNNEL), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (!blockState.hasProperty(PART) || blockState.getValue(PART) == Part.FUNNEL) {
            return null;
        }

        Block block = blockState.getBlock();

        if (block == BlocksRegistry.OAK_SLUICE.get()) {
            return BlockEntitiesRegistry.OAK_SLUICE.get().create(blockPos, blockState);
        } else if (block == BlocksRegistry.IRON_SLUICE.get()) {
            return BlockEntitiesRegistry.IRON_SLUICE.get().create(blockPos, blockState);
        } else if (block == BlocksRegistry.DIAMOND_SLUICE.get()) {
            return BlockEntitiesRegistry.DIAMOND_SLUICE.get().create(blockPos, blockState);
        }

        return BlockEntitiesRegistry.NETHERITE_SLUICE.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return SluiceBlockEntity::tick;
    }

    @Override
    public void addSerializableComponents(List<DataComponentType<?>> list) {

    }

    public static class SluiceBlockItem extends BlockItem {
        public SluiceBlockItem(Block block) {
            super(block, new Item.Properties());
        }
//
//        @Override
//        public CompoundTag getShareTag(ItemStack stack) {
//            if (getBlock() == BlocksRegistry.NETHERITE_SLUICE.get() || getBlock() == BlocksRegistry.EMPOWERED_SLUICE.get()) {
//                if (!stack.getOrCreateTag().contains("BlockEntityTag")) {
//                    CompoundTag tag = new CompoundTag();
//                    CompoundTag energy = new CompoundTag();
//                    energy.putInt("energy", 0);
//                    tag.put("Energy", energy);
//
//                    stack.getOrCreateTag().put("BlockEntityTag", tag);
//                }
//            }
//            return super.getShareTag(stack);
//        }
    }

    public enum Part implements StringRepresentable {
        MAIN("main"),
        FUNNEL("funnel");

        String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
