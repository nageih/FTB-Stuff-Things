package dev.ftb.mods.ftbobb.tubes;

import dev.ftb.mods.ftbobb.recipes.JarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public record ConnectedHandlers(List<BlockCapabilityCache<IItemHandler, Direction>> itemHandlers,
                                List<BlockCapabilityCache<IFluidHandler, Direction>> fluidHandlers) {
    public static ConnectedHandlers create() {
        return new ConnectedHandlers(new ArrayList<>(), new ArrayList<>());
    }

    public void checkAndAddHandlers(ServerLevel level, BlockPos pos, Direction dir) {
        if (level.getCapability(Capabilities.ItemHandler.BLOCK, pos, dir) != null) {
            itemHandlers.add(BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, level, pos, dir));
        }
        if (level.getCapability(Capabilities.FluidHandler.BLOCK, pos, dir) != null) {
            fluidHandlers.add(BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, level, pos, dir));
        }
    }

    public ExtractionContext findIngredients(JarRecipe recipe) {
        ExtractionContext context = new ExtractionContext();

        for (var input : recipe.allInputs()) {
            input.ifLeft(fluid -> findFluid(fluid, context))
                    .ifRight(item -> findItem(item, context));
            if (context.isInsufficient()) {
                break;
            }
        }

        return context;
    }

    public boolean distributeOutputs(BlockEntity jar, JarRecipe recipe) {
        List<ItemStack> excessItems = new ArrayList<>();

        for (ItemStack stack : recipe.getOutputItems()) {
            int remaining = stack.getCount();
            for (var handler : itemHandlers) {
                if (handler.getCapability() != null) {
                    ItemStack excess = ItemHandlerHelper.insertItem(handler.getCapability(), stack.copy(), false);
                    remaining -= stack.getCount() - excess.getCount();
                    if (remaining <= 0) {
                        break;
                    }
                }
            }
            if (remaining > 0) {
                excessItems.add(stack.copy());
            }
        }

        return false;
    }

    private void findFluid(SizedFluidIngredient ingredient, ExtractionContext context) {
        int remaining = ingredient.amount();

        for (var fluidCaches : fluidHandlers) {
            IFluidHandler handler = fluidCaches.getCapability();
            if (handler != null) {
                FluidStack stack = handler.drain(remaining, IFluidHandler.FluidAction.SIMULATE);
                if (ingredient.ingredient().test(stack)) {
                    remaining -= stack.getAmount();
                    context.addFluidSource(handler, stack);
                    if (remaining <= 0) {
                        break;
                    }
                }
            }
        }
        if (remaining > 0) {
            context.markInsufficient();
        }
    }

    private void findItem(SizedIngredient ingredient, ExtractionContext context) {
        int remaining = ingredient.count();

        for (var itemCaches : itemHandlers) {
            IItemHandler handler = itemCaches.getCapability();
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.extractItem(i, remaining, true);
                    if (ingredient.ingredient().test(stack)) {
                        remaining -= stack.getCount();
                        context.addItemSource(handler, stack, i);
                        if (remaining <= 0) {
                            break;
                        }
                    }
                }
            }
        }

        if (remaining > 0) {
            context.markInsufficient();
        }
    }

    public static class ExtractionContext {
        private boolean insufficient;
        private final List<FluidSource> fluidSources = new ArrayList<>();
        private final List<ItemSource> itemSources = new ArrayList<>();

        public ExtractionContext() {
            this.insufficient = false;
        }

        public boolean isInsufficient() {
            return insufficient;
        }

        public void markInsufficient() {
            insufficient = true;
        }

        public void addFluidSource(IFluidHandler handler, FluidStack fluidStack) {
            fluidSources.add(new FluidSource(handler, fluidStack));
        }

        public void addItemSource(IItemHandler handler, ItemStack itemStack, int slot) {
            itemSources.add(new ItemSource(handler, itemStack, slot));
        }

        public boolean apply() {
            // actually extract the resource from the handlers - should always succeed!
            // - as long as this context is used on the same tick that it was created

            return true;
        }
    }

    private record FluidSource(IFluidHandler handler, FluidStack fluidStack) {
    }

    private record ItemSource(IItemHandler handler, ItemStack itemStack, int slot) {
    }
}
