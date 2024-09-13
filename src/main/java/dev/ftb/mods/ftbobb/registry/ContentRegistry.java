package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Misc registry for anything that falls outside the scope of the other registry classes
 */
public class ContentRegistry {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FTBOBB.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("obb_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("ftbobb.itemGroup.tab"))
            .icon(() -> new ItemStack(BlocksRegistry.OAK_SLUICE.get()))
            .displayItems((parameters, output) -> {
                for (DeferredHolder<Item, ? extends Item> entry : ItemsRegistry.ITEMS.getEntries()) {
                    output.accept(new ItemStack(entry.get()));
                }
            }).build());

    private static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, FTBOBB.MODID);

    public static final DeferredHolder<DamageType, DamageType> STATIC_ELECTRIC_DAMAGE_TYPE = DAMAGE_TYPES.register("static_electric", () -> new DamageType("static_electric", 0.0F));

    public static void init(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
        DAMAGE_TYPES.register(bus);
    }
}
