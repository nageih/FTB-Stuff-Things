package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.items.FluidCapsuleItem;
import dev.ftb.mods.ftbobb.network.SyncJarContentsPacket;
import dev.ftb.mods.ftbobb.network.SyncJarRecipePacket;
import dev.ftb.mods.ftbobb.recipes.JarRecipe;
import dev.ftb.mods.ftbobb.recipes.NoInventory;
import dev.ftb.mods.ftbobb.recipes.RecipeCaches;
import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbobb.registry.BlocksRegistry;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import dev.ftb.mods.ftbobb.screens.TemperedJarMenu;
import dev.ftb.mods.ftbobb.temperature.TemperatureAndEfficiency;
import dev.ftb.mods.ftbobb.util.DirectionUtil;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class TemperedJarBlockEntity extends BlockEntity implements MenuProvider {
    public static final int TANK_CAPACITY = 8000;  // 3 of these tanks
    private static final ResourceLocation NO_RECIPE = FTBOBB.id("_none_");
    public static final int STOPPED = -1;

    private boolean checkRecipe = true;
    @Nullable private RecipeHolder<JarRecipe> currentRecipe;
    private final Lazy<Boolean> autoProcessing = Lazy.of(this::checkForAutoProcessor);
    private final Lazy<TemperatureAndEfficiency> temperature = Lazy.of(this::checkForTemperature);
    private int remainingTime;  // -1 => don't autocraft, 0 => produce output, >0 => do crafting
    private int processingTime; // 0 when there's no current recipe
    private final JarItemHandler itemHandler = new JarItemHandler();
    private final JarFluidHandler fluidHandler = new JarFluidHandler();
    private final JarContainerData containerData = new JarContainerData();
    private boolean syncNeeded;
    private final Map<Direction, BlockCapabilityCache<IItemHandler, Direction>> itemOutputs = new EnumMap<>(Direction.class);
    private final Map<Direction, BlockCapabilityCache<IFluidHandler, Direction>> fluidOutputs = new EnumMap<>(Direction.class);

    public TemperedJarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesRegistry.TEMPERED_JAR.get(), pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put("Items", itemHandler.serializeNBT(registries));
        tag.put("Tanks", fluidHandler.serializeNBT(registries));
        tag.putInt("remaining", remainingTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        fluidHandler.deserializeNBT(registries, tag.getCompound("Tanks"));
        remainingTime = tag.getInt("remaining");
        currentRecipe = null;
        checkRecipe = true;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        // server-side, chunk loading
        return Util.make(new CompoundTag(), tag -> saveAdditional(tag, registries));
    }

//    @Nullable
//    @Override
//    public Packet<ClientGamePacketListener> getUpdatePacket() {
////        if (getLevel() instanceof ServerLevel serverLevel) {
////            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), SyncJarFluidsPacket.forJar(this));
////        }
//        return null;  // prevents vanilla update packet being sent
//    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ftbobb.tempered_jar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TemperedJarMenu(containerId, playerInventory, getBlockPos());
    }

    public JarContainerData getContainerData() {
        return containerData;
    }

    public void serverTick(ServerLevel serverLevel) {
        if (syncNeeded) {
            // TODO possibly limit sync rate here for performance reasons
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), SyncJarContentsPacket.wholeJar(this));
            syncNeeded = false;
        }

        if (checkRecipe) {
            ResourceLocation prevId = currentRecipe == null ? NO_RECIPE : currentRecipe.id();
            currentRecipe = RecipeCaches.TEMPERED_JAR.getCachedRecipe(this::searchForRecipe, this::genIngredientHash)
                    .orElse(null);
            ResourceLocation newId = currentRecipe == null ? NO_RECIPE : currentRecipe.id();

            processingTime = currentRecipe == null ? 0 : getTemperature().getRecipeTime(currentRecipe.value());

            if (!prevId.equals(newId)) {
                // current recipe has changed! reset any crafting progress
                remainingTime = hasAutoProcessing() ? processingTime : STOPPED;

                // update any players who have the GUI open right now
                SyncJarRecipePacket packet = new SyncJarRecipePacket(getBlockPos(), getCurrentRecipeId());
                level.players().stream()
                        .filter(p -> p.containerMenu instanceof TemperedJarMenu menu && menu.getJar() == this)
                        .forEach(p -> PacketDistributor.sendToPlayer((ServerPlayer) p, packet));
            } else {
                // same recipe, but processing time might have changed if block beneath was replaced
                remainingTime = Math.min(remainingTime, processingTime);
            }

            checkRecipe = false;
        }

        if (currentRecipe != null && remainingTime >= 0) {
            JarRecipe recipe = currentRecipe.value();

            // we have a valid recipe, but still need to ensure temperature and sufficient input resources...
            boolean okToCraft = false;
            int[] itemSlots = new int[0];
            int[] fluidSlots = new int[0];
            if (recipe.getTemperature() == getTemperature().temperature()) {
                itemSlots = locateInputItems(recipe);
                fluidSlots = locateInputFluids(recipe);
                if (allInputsFound(itemSlots, fluidSlots)) {
                    okToCraft = true;
                }
            }

            if (okToCraft) {
                // run a cycle
                runOneCycle(serverLevel, recipe, itemSlots, fluidSlots);
            } else {
                // either wrong temperature or insufficient inputs; halt the process
                remainingTime = STOPPED;
            }
        }
    }

    private void runOneCycle(ServerLevel serverLevel, JarRecipe recipe, int[] itemInputSlots, int[] fluidInputSlots) {
        if (--remainingTime <= 0) {
            // we _should_ have the resources here at this point (we just checked for this)
            for (int i = 0; i < recipe.getInputItems().size(); i++) {
                getInputItemHandler().extractItem(itemInputSlots[i], recipe.getInputItems().get(i).count(), false);
            }
            for (int i = 0; i < recipe.getInputFluids().size(); i++) {
                fluidHandler.tanks.get(fluidInputSlots[i]).drain(recipe.getInputFluids().get(i).amount(), IFluidHandler.FluidAction.EXECUTE);
            }
            syncNeeded = true;

            boolean outputsFull = false;
            // produce output
            List<ItemStack> excessItems = distributeOutputItems(recipe);
            if (!excessItems.isEmpty()) {
                excessItems.forEach(itemStack -> Block.popResource(serverLevel, getBlockPos(), itemStack));
                outputsFull = true;
            }
            List<FluidStack> excessFluids = distributeOutputFluids(recipe);
            if (!excessFluids.isEmpty()) {
                excessFluids.forEach(fluidStack -> Block.popResource(serverLevel, getBlockPos(), FluidCapsuleItem.of(fluidStack)));
                outputsFull = true;
            }

            remainingTime = !outputsFull && hasAutoProcessing() && recipe.canRepeat() ? processingTime : STOPPED;
        }
    }

    private List<ItemStack> distributeOutputItems(JarRecipe recipe) {
        List<ItemStack> items = recipe.getOutputItems().stream().map(ItemStack::copy).toList();
        List<ItemStack> excessList = new ArrayList<>();
        for (Direction dir : DirectionUtil.VALUES) {
            IItemHandler dest = itemOutputs.computeIfAbsent(dir, k ->
                            BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) getLevel(), getBlockPos().relative(dir), dir.getOpposite()))
                    .getCapability();
            if (dest != null) {
                for (ItemStack stack : items) {
                    ItemStack excess = ItemHandlerHelper.insertItem(dest, stack, false);
                    if (!excess.isEmpty()) {
                        excessList.add(excess);
                    }
                }
                if (excessList.isEmpty()) {
                    break;
                } else {
                    items = excessList;
                }
            }
        }
        return excessList;
    }

    private List<FluidStack> distributeOutputFluids(JarRecipe recipe) {
        List<FluidStack> fluids = recipe.getOutputFluids().stream().map(FluidStack::copy).toList();
        List<FluidStack> excessList = new ArrayList<>();
        for (Direction dir : DirectionUtil.VALUES) {
            IFluidHandler dest = fluidOutputs.computeIfAbsent(dir, k ->
                            BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, (ServerLevel) getLevel(), getBlockPos().relative(dir), dir.getOpposite()))
                    .getCapability();
            if (dest != null) {
                for (FluidStack stack : fluids) {
                    int filled = dest.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    if (filled < stack.getAmount()) {
                        excessList.add(stack.copyWithAmount(stack.getAmount() - filled));
                    }
                }
                if (excessList.isEmpty()) {
                    break;
                } else {
                    fluids = excessList;
                }
            }
        }
        return excessList;
    }

    private boolean allInputsFound(int[] itemSlots, int[] fluidSlots) {
        return Arrays.stream(itemSlots).noneMatch(itemSlot -> itemSlot < 0)
                && Arrays.stream(fluidSlots).noneMatch(fluidSlot -> fluidSlot < 0);
    }

    private int[] locateInputItems(JarRecipe value) {
        BitSet slotsChecked = new BitSet(3);
        int[] itemSlots = new int[value.getInputItems().size()];
        Arrays.fill(itemSlots, -1);

        List<SizedIngredient> inputItems = value.getInputItems();
        for (int ingrIdx = 0; ingrIdx < inputItems.size(); ingrIdx++) {
            SizedIngredient ingr = inputItems.get(ingrIdx);
            for (int slotIdx = 0; slotIdx < itemHandler.getSlots(); slotIdx++) {
                if (!slotsChecked.get(slotIdx) && ingr.test(itemHandler.getStackInSlot(slotIdx))) {
                    slotsChecked.set(slotIdx);
                    itemSlots[ingrIdx] = slotIdx;
                }
            }
        }

        return itemSlots;
    }

    private int[] locateInputFluids(JarRecipe recipe) {
        BitSet slotsChecked = new BitSet(3);
        int[] fluidSlots = new int[recipe.getInputFluids().size()];
        Arrays.fill(fluidSlots, -1);

        List<SizedFluidIngredient> inputItems = recipe.getInputFluids();
        for (int ingrIdx = 0; ingrIdx < inputItems.size(); ingrIdx++) {
            SizedFluidIngredient ingr = inputItems.get(ingrIdx);
            for (int slotIdx = 0; slotIdx < fluidHandler.getTanks(); slotIdx++) {
                if (!slotsChecked.get(slotIdx) && ingr.test(fluidHandler.getFluidInTank(slotIdx))) {
                    slotsChecked.set(slotIdx);
                    fluidSlots[ingrIdx] = slotIdx;
                }
            }
        }

        return fluidSlots;
    }

    private Optional<RecipeHolder<JarRecipe>> searchForRecipe() {
        return getLevel().getRecipeManager().getAllRecipesFor(RecipesRegistry.JAR_TYPE.get()).stream()
                .filter(r -> r.value().test(getTemperature().temperature(), itemHandler, fluidHandler))
                .findFirst();
    }

    private int genIngredientHash() {
        IntList itemIds = new IntArrayList();
        itemIds.add(getTemperature().temperature().ordinal());
        for (int i = 0; i < getInputItemHandler().getSlots(); i++) {
            itemIds.add(ItemStack.hashItemAndComponents(getInputItemHandler().getStackInSlot(i)));
        }
        for (int i = 0; i < getFluidHandler().getTanks(); i++) {
            itemIds.add(FluidStack.hashFluidAndComponents(getFluidHandler().getFluidInTank(i)));
        }
        return Objects.hash(itemIds.toArray());
    }

    public TemperatureAndEfficiency getTemperature() {
        return temperature.get();
    }

    public boolean onRightClick(Player player, InteractionHand hand, ItemStack item) {
        boolean res = false;
        if (FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) {
            syncNeeded = true;
            res = true;
        }

        if (!level.isClientSide()) {
            List<Component> msgs = new ArrayList<>();
            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                FluidStack stack = fluidHandler.getFluidInTank(i);
                if (!stack.isEmpty()) {
                    msgs.add(Component.translatable("ftblibrary.mb", stack.getAmount(), stack.getHoverName()));
                }
            }
            if (msgs.isEmpty()) {
                player.displayClientMessage(Component.translatable("ftblibrary.empty"), true);
            } else {
                player.displayClientMessage(msgs.stream().reduce((c1, c2) -> c1.copy().append(" / ").append(c2)).orElse(Component.empty()), true);
            }
        }

        return res;
    }

    public void clearCachedData() {
        temperature.invalidate();
        autoProcessing.invalidate();
        checkRecipe = true;
    }

    public void onNeighbourChange(Direction direction, BlockPos neighborPos) {
//        if (getLevel() instanceof ServerLevel serverLevel) {
//            itemOutputs.put(direction, BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, serverLevel, neighborPos, direction.getOpposite()));
//            fluidOutputs.put(direction, BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, neighborPos, direction.getOpposite()));
//        }
    }

    private boolean hasAutoProcessing() {
        return autoProcessing.get();
    }

    private boolean checkForAutoProcessor() {
        return !level.isOutsideBuildHeight(worldPosition.above())
                && level.getBlockState(worldPosition.above()).is(BlocksRegistry.AUTO_PROCESSING_BLOCK.get());
    }

    private TemperatureAndEfficiency checkForTemperature() {
        return TemperatureAndEfficiency.fromLevel(getLevel(), getBlockPos().below());
    }

    public IItemHandler getInputItemHandler() {
        return itemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public void syncFromServer(List<SyncJarContentsPacket.ResourceSlot> resources) {
        itemHandler.clear();
        fluidHandler.clear();
        resources.forEach(resource -> resource.resource()
                .ifLeft(item -> itemHandler.setStackInSlot(resource.slot(), item))
                .ifRight(fluid -> fluidHandler.tanks.get(resource.slot()).setFluid(fluid))
        );
    }

    public Optional<ResourceLocation> getCurrentRecipeId() {
        return currentRecipe == null ? Optional.empty() : Optional.of(currentRecipe.id());
    }

    public void setCurrentRecipeId(@Nullable ResourceLocation newRecipeId) {
        // only called clientside when a SyncJarRecipePacket is received
        if (level.isClientSide) {
            currentRecipe = level.getRecipeManager().getRecipeFor(RecipesRegistry.JAR_TYPE.get(), NoInventory.INSTANCE, level, newRecipeId)
                    .orElse(null);
        }
    }

    public void toggleCrafting() {
        if (remainingTime < 0 && currentRecipe != null) {
            remainingTime = processingTime;
        } else {
            remainingTime = STOPPED;
        }
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    private class JarItemHandler extends ItemStackHandler {
        private final ItemStack[] prevStack = new ItemStack[3];

        public JarItemHandler() {
            super(3);

            Arrays.fill(prevStack, ItemStack.EMPTY);
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!ItemStack.isSameItemSameComponents(prevStack[slot], getStackInSlot(slot))) {
                checkRecipe = true;
                prevStack[slot] = getStackInSlot(slot).copy();
            }
        }

        public void clear() {
            setSize(getSlots()); // clears slots
        }
    }

    private class JarFluidHandler implements IFluidHandler {
        private final List<JarTank> tanks;

        private JarFluidHandler() {
            tanks = List.of(new JarTank(), new JarTank(), new JarTank());
        }

        @Override
        public int getTanks() {
            return tanks.size();
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return tanks.get(tank).getFluid().copy();
        }

        @Override
        public int getTankCapacity(int tank) {
            return tanks.get(tank).getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return getFluidInTank(tank).isEmpty() || FluidStack.isSameFluidSameComponents(getFluidInTank(tank), stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int firstEmpty = -1;
            int filled = 0;
            for (int i = 0; i < getTanks(); i++) {
                FluidStack current = getFluidInTank(i);
                if (FluidStack.isSameFluidSameComponents(current, resource)) {
                    filled = tanks.get(i).fill(resource, action);
                    break;
                } else if (firstEmpty < 0 && current.isEmpty()) {
                    firstEmpty = i;
                }
            }
            if (firstEmpty >= 0) {
                filled = tanks.get(firstEmpty).fill(resource, action);
            }
            if (filled > 0 && action.execute()) {
                setChanged();
            }
            return filled;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return doDrain(t -> FluidStack.isSameFluidSameComponents(t.getFluid(), resource), resource.getAmount(), action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return doDrain(t -> !t.isEmpty(), maxDrain, action);
        }

        private FluidStack doDrain(Predicate<FluidTank> tankPredicate, int amount, FluidAction action) {
            return tanks.stream()
                    .filter(tankPredicate)
                    .findFirst()
                    .map(tank -> tank.drain(amount, action))
                    .orElse(FluidStack.EMPTY);
        }

        public Tag serializeNBT(HolderLookup.Provider provider) {
            RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
            return Util.make(new CompoundTag(), tag -> {
                for (int i = 0; i < getTanks(); i++) {
                    if (!getFluidInTank(i).isEmpty()) {
                        tag.put("Tank" + i, FluidStack.CODEC.encodeStart(ops, getFluidInTank(i)).result().orElseThrow());
                    }
                }
            });
        }

        private void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
            RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
            for (int i = 0; i < tanks.size(); i++) {
                if (tag.contains("Tank" + i)) {
                    FluidStack stack = FluidStack.CODEC.parse(ops, tag.get("Tank" + i)).getOrThrow();
                    tanks.get(i).setFluid(stack);
                } else {
                    tanks.get(i).setFluid(FluidStack.EMPTY);
                }
            }
        }

        public void clear() {
            tanks.forEach(tank -> tank.setFluid(FluidStack.EMPTY));
        }
    }

    private class JarTank extends FluidTank {
        FluidStack prevFluid = FluidStack.EMPTY;

        public JarTank() {
            super(TANK_CAPACITY);
        }

//        public JarTank(FluidStack contents) {
//            super(TANK_CAPACITY);
//            setFluid(contents);
//        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();

            setChanged();
            if (!FluidStack.isSameFluidSameComponents(prevFluid, getFluid())) {
                prevFluid = getFluid().copy();
                checkRecipe = true;
            }
        }
    }

    public class JarContainerData implements ContainerData {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> processingTime;
                case 1 -> remainingTime;
                default -> throw new IllegalArgumentException("invalid index: " + index);
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> processingTime = value;
                case 1 -> remainingTime = value;
                default -> throw new IllegalArgumentException("invalid index: " + index);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
