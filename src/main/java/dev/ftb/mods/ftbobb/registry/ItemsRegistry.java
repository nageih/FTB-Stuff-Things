package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.TemperedJarBlockEntity;
import dev.ftb.mods.ftbobb.items.FluidCapsuleItem;
import dev.ftb.mods.ftbobb.items.MeshItem;
import dev.ftb.mods.ftbobb.items.MeshType;
import dev.ftb.mods.ftbobb.items.WaterBowlItem;
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
    public static final DeferredItem<WaterBowlItem> WATER_BOWL = ITEMS.register("water_bowl", WaterBowlItem::new);

    public static final DeferredItem<Item> CAST_IRON_INGOT = simpleItem("cast_iron_ingot");
    public static final DeferredItem<Item> CAST_IRON_NUGGET = simpleItem("cast_iron_nugget");
    public static final DeferredItem<Item> CAST_IRON_GEAR = simpleItem("cast_iron_gear");
    public static final DeferredItem<Item> TEMPERED_GLASS = simpleItem("tempered_glass");

    //#region Block Items
    public static final DeferredItem<BlockItem> OAK_SLUICE = blockItem("oak_sluice", BlocksRegistry.OAK_SLUICE);
    public static final DeferredItem<BlockItem> IRON_SLUICE = blockItem("iron_sluice", BlocksRegistry.IRON_SLUICE);
    public static final DeferredItem<BlockItem> DIAMOND_SLUICE = blockItem("diamond_sluice", BlocksRegistry.DIAMOND_SLUICE);
    public static final DeferredItem<BlockItem> NETHERITE_SLUICE = blockItem("netherite_sluice", BlocksRegistry.NETHERITE_SLUICE);

    public static final DeferredItem<BlockItem> PUMP = blockItem("pump", BlocksRegistry.PUMP);

    public static final DeferredItem<BlockItem> DRIPPER = blockItem("dripper", BlocksRegistry.DRIPPER);

    public static final DeferredItem<BlockItem> CAST_IRON_BLOCK = blockItem("cast_iron_block", BlocksRegistry.CAST_IRON_BLOCK);

    public static final DeferredItem<BlockItem> TUBE = blockItem("tube", BlocksRegistry.TUBE);
    public static final DeferredItem<BlockItem> JAR = blockItem("jar", BlocksRegistry.JAR);
    public static final DeferredItem<BlockItem> TEMPERED_JAR = blockItem("tempered_jar", BlocksRegistry.TEMPERED_JAR);
    public static final DeferredItem<BlockItem> AUTO_PROCESSING_BLOCK
            = blockItem("auto_processing_block", BlocksRegistry.AUTO_PROCESSING_BLOCK);
    public static final DeferredItem<BlockItem> BLUE_MAGMA_BLOCK
            = blockItem("blue_magma_block", BlocksRegistry.BLUE_MAGMA_BLOCK);
    public static final DeferredItem<BlockItem> CREATIVE_HOT_TEMPERATURE_SOURCE
            = blockItem("creative_low_temperature_source", BlocksRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE);
    public static final DeferredItem<BlockItem> CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE
            = blockItem("creative_high_temperature_source", BlocksRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE);
    public static final DeferredItem<BlockItem> CREATIVE_CHILLED_TEMPERATURE_SOURCE
            = blockItem("creative_subzero_temperature_source", BlocksRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE);

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
