package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.items.FluidCapsuleItem;
import dev.ftb.mods.ftbobb.items.MeshItem;
import dev.ftb.mods.ftbobb.items.MeshType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FTBOBB.MODID);

    public static final DeferredItem<MeshItem> CLOTH_MESH = ITEMS.register("cloth_mesh", () -> new MeshItem(MeshType.CLOTH));
    public static final DeferredItem<MeshItem> IRON_MESH = ITEMS.register("iron_mesh", () -> new MeshItem(MeshType.IRON));
    public static final DeferredItem<MeshItem> GOLD_MESH = ITEMS.register("gold_mesh", () -> new MeshItem(MeshType.GOLD));
    public static final DeferredItem<MeshItem> DIAMOND_MESH = ITEMS.register("diamond_mesh", () -> new MeshItem(MeshType.DIAMOND));

    public static final DeferredItem<FluidCapsuleItem> FLUID_CAPSULE = ITEMS.register("fluid_capsule", FluidCapsuleItem::new);

    //#region Block Items
    public static final DeferredItem<BlockItem> OAK_SLUICE = blockItem("oak_sluice", BlocksRegistry.OAK_SLUICE);
    public static final DeferredItem<BlockItem> IRON_SLUICE = blockItem("iron_sluice", BlocksRegistry.IRON_SLUICE);
    public static final DeferredItem<BlockItem> DIAMOND_SLUICE = blockItem("diamond_sluice", BlocksRegistry.DIAMOND_SLUICE);
    public static final DeferredItem<BlockItem> NETHERITE_SLUICE = blockItem("netherite_sluice", BlocksRegistry.NETHERITE_SLUICE);

    public static final DeferredItem<BlockItem> PUMP = blockItem("pump", BlocksRegistry.PUMP);

    public static final DeferredItem<BlockItem> TUBE = blockItem("tube", BlocksRegistry.TUBE);
    public static final DeferredItem<BlockItem> JAR = blockItem("jar", BlocksRegistry.JAR);
    public static final DeferredItem<BlockItem> TEMPERED_JAR = blockItem("tempered_jar", BlocksRegistry.TEMPERED_JAR);
    public static final DeferredItem<BlockItem> AUTO_PROCESSING_BLOCK
            = blockItem("auto_processing_block", BlocksRegistry.AUTO_PROCESSING_BLOCK);
    public static final DeferredItem<BlockItem> BLUE_MAGMA_BLOCK
            = blockItem("blue_magma_block", BlocksRegistry.BLUE_MAGMA_BLOCK);
    public static final DeferredItem<BlockItem> CREATIVE_LOW_TEMPERATURE_SOURCE
            = blockItem("creative_low_temperature_source", BlocksRegistry.CREATIVE_LOW_TEMPERATURE_SOURCE);
    public static final DeferredItem<BlockItem> CREATIVE_HIGH_TEMPERATURE_SOURCE
            = blockItem("creative_high_temperature_source", BlocksRegistry.CREATIVE_HIGH_TEMPERATURE_SOURCE);
    public static final DeferredItem<BlockItem> CREATIVE_SUBZERO_TEMPERATURE_SOURCE
            = blockItem("creative_subzero_temperature_source", BlocksRegistry.CREATIVE_SUBZERO_TEMPERATURE_SOURCE);
    //#endregion

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static DeferredItem<Item> simpleItem(String id) {
        return ITEMS.register(id, () -> new Item(new Item.Properties()));
    }

    public static DeferredItem<BlockItem> blockItem(String id, Supplier<? extends Block> sup) {
        return ITEMS.register(id, () -> new BlockItem(sup.get(), new Item.Properties()));
    }
}
