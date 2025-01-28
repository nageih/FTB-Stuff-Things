package dev.ftb.mods.ftbstuffnthings.data;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.advancements.CustomTrigger;
import dev.ftb.mods.ftbstuffnthings.registry.CriterionTriggerRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AdvancementsGenerator extends AdvancementProvider {
    private static final ResourceLocation BACKGROUND_TEXTURE
            = ResourceLocation.withDefaultNamespace("textures/block/blue_concrete_powder.png");

    public AdvancementsGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, existingFileHelper, List.of(new FTBStuffAdvancements()));
    }

    private static class FTBStuffAdvancements implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = customAdvancement(CriterionTriggerRegistry.FTBSTUFF_ROOT, AdvancementType.TASK, ItemsRegistry.CRATE, true)
                    .save(saver, id("root"));

            customAdvancement(CriterionTriggerRegistry.SUPERCHARGED, AdvancementType.TASK, ItemsRegistry.PUMP.asItem(), false)
                    .parent(root)
                    .save(saver, id("supercharged"));
        }

        private static String id(String s) {
            return FTBStuffNThings.MODID + ":" + s;
        }

        private Advancement.Builder customAdvancement(Supplier<CustomTrigger> triggerSupplier, AdvancementType type, ItemLike itemDisp, boolean stealth) {
            CustomTrigger trigger = triggerSupplier.get();
            String namespace = trigger.getInstance().id().getNamespace();
            String path = trigger.getInstance().id().getPath();
            return Advancement.Builder.advancement()
                    .display(itemDisp,
                            Component.translatable(namespace + ".advancement." + path),
                            Component.translatable(namespace + ".advancement." + path + ".desc"),
                            BACKGROUND_TEXTURE, type, !stealth, !stealth, false)
                    .addCriterion("0", new Criterion<>(trigger, trigger.getInstance()));
        }
    }
}
