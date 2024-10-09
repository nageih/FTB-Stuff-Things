package dev.ftb.mods.ftbobb.data;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.FTBOBBTags;
import dev.ftb.mods.ftbobb.lootmodifiers.CrookModifier;
import dev.ftb.mods.ftbobb.lootmodifiers.HammerModifier;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class LootModifiersGenerator extends GlobalLootModifierProvider {
    public LootModifiersGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, FTBOBB.MODID);
    }

    @Override
    protected void start() {
        add("crook_loot_modifier", new CrookModifier(new LootItemCondition[] {
                MatchTool.toolMatches(ItemPredicate.Builder.item().of(FTBOBBTags.Items.CROOKS)).build()
        }));

        add("hammer_loot_modifier", new HammerModifier(new LootItemCondition[] {
                MatchTool.toolMatches(ItemPredicate.Builder.item().of(FTBOBBTags.Items.HAMMERS)).build()
        }));
    }
}
