package dev.ftb.mods.ftbobb.crafting;

import dev.ftb.mods.ftbobb.crafting.recipe.CrookRecipe;
import dev.ftb.mods.ftbobb.crafting.recipe.HammerRecipe;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.*;

@EventBusSubscriber
public class ToolsRecipeCache {
    // TODO see if this can be integrated into RecipeCache

    private static final Set<Ingredient> crookableCache = new HashSet<>();
    private static final Set<Ingredient> hammerableCache = new HashSet<>();

    private static final Map<Item, CrookRecipe.CrookDrops> crookCache = new HashMap<>();
    private static final Map<Item, List<ItemStack>> hammerCache = new HashMap<>();

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener((ResourceManagerReloadListener) arg -> ToolsRecipeCache.refreshCaches(event.getServerResources().getRecipeManager()));
    }

    @SubscribeEvent
    public static void recipesSetup(RecipesUpdatedEvent event) {
        RecipeManager recipeManager = event.getRecipeManager();
        ToolsRecipeCache.refreshCaches(recipeManager);
    }

    public static void refreshCaches(RecipeManager manager) {
        clearCache();
        crookableCache.addAll(manager.getAllRecipesFor(RecipesRegistry.CROOK_TYPE.get()).stream()
                .map(e -> e.value().getIngredient()).toList());
        hammerableCache.addAll(manager.getAllRecipesFor(RecipesRegistry.HAMMER_TYPE.get()).stream()
                .map(e -> e.value().getIngredient()).toList());
    }

    public static void clearCache() {
        crookCache.clear();
        crookableCache.clear();
        hammerCache.clear();
        hammerableCache.clear();
    }

    public static List<ItemStack> getHammerDrops(Level level, ItemStack input) {
        return hammerCache.computeIfAbsent(input.getItem(), key -> {
            List<ItemStack> drops = new ArrayList<>();
            for (RecipeHolder<HammerRecipe> holder : level.getRecipeManager().getRecipesFor(RecipesRegistry.HAMMER_TYPE.get(), NoInventory.INSTANCE, level)) {
                HammerRecipe value = holder.value();
                if (value.getIngredient().test(input)) {
                    value.getResults().forEach(e -> drops.add(e.copy()));
                }
            }

            return drops;
        });
    }

    public static boolean hammerable(BlockState state) {
        return hammerable(new ItemStack(state.getBlock()));
    }

    public static boolean hammerable(ItemStack stack) {
        return hammerableCache.stream().anyMatch(e -> e.test(stack));
    }

    public static CrookRecipe.CrookDrops getCrookDrops(Level level, ItemStack input) {
        return crookCache.computeIfAbsent(input.getItem(), key -> {
            List<ItemWithChance> drops = new ArrayList<>();
            int max = -1;
            boolean replaceDrops = false;
            for (RecipeHolder<CrookRecipe> holder : level.getRecipeManager().getRecipesFor(RecipesRegistry.CROOK_TYPE.get(), NoInventory.INSTANCE, level)) {
                CrookRecipe recipe = holder.value();
                if (recipe.getIngredient().test(input)) {
                    if (recipe.getMax() > 0) {
                        max = recipe.getMax();
                    }
                    if (recipe.replaceDrops()) {
                        replaceDrops = true;
                    }
                    recipe.getResults().forEach(res -> drops.add(res.copy()));
                }
            }

            return new CrookRecipe.CrookDrops(drops, max, replaceDrops);
        });
    }

    public static boolean crookable(BlockState state) {
        return crookable(new ItemStack(state.getBlock()));
    }

    public static boolean crookable(ItemStack stack) {
        return crookableCache.stream().anyMatch(e -> e.test(stack));
    }
}
