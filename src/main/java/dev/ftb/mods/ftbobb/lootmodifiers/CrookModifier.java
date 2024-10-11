package dev.ftb.mods.ftbobb.lootmodifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbobb.FTBOBBTags;
import dev.ftb.mods.ftbobb.crafting.ToolsRecipeCache;
import dev.ftb.mods.ftbobb.crafting.recipe.CrookRecipe;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CrookModifier extends LootModifier {
    public static final Supplier<MapCodec<CrookModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(
            builder -> codecStart(builder).apply(builder, CrookModifier::new))
    );

    public CrookModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
        ItemStack crook = context.getParamOrNull(LootContextParams.TOOL);
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);

        if (!(entity instanceof Player) || crook == null || blockState == null || !crook.is(FTBOBBTags.Items.CROOKS) || !ToolsRecipeCache.crookable(blockState)) {
            return list;
        }

        CrookRecipe.CrookDrops crookDrops = ToolsRecipeCache.getCrookDrops(entity.level(), new ItemStack(blockState.getBlock()));
        int maxDrops = crookDrops.max() <= 0 ? Integer.MAX_VALUE : crookDrops.max();
        if (!crookDrops.items().isEmpty()) {
            RandomSource random = context.getRandom();

            List<ItemStack> collect = crookDrops
                    .items()
                    .stream()
                    .filter(itemWithChance -> random.nextFloat() < itemWithChance.chance())
                    .map(itemWithChance -> itemWithChance.item().copy())
                    .collect(Collectors.toList());

            Collections.shuffle(collect);
            if (crookDrops.replaceDrops()) {
                list.clear();
            }
            collect.stream().limit(maxDrops).forEach(list::add);
        }

        return list;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
