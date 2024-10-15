package dev.ftb.mods.ftbstuffnthings.registry;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FTBStuffNThings.MODID);

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

    public static final DeferredItem<HammerItem> STONE_HAMMER = registerHammer("stone_hammer", Tiers.STONE);
    public static final DeferredItem<HammerItem> IRON_HAMMER = registerHammer("iron_hammer", Tiers.IRON);
    public static final DeferredItem<HammerItem> GOLD_HAMMER = registerHammer("gold_hammer", Tiers.GOLD);
    public static final DeferredItem<HammerItem> DIAMOND_HAMMER = registerHammer("diamond_hammer", Tiers.DIAMOND);
    public static final DeferredItem<HammerItem> NETHERITE_HAMMER = registerHammer("netherite_hammer", Tiers.NETHERITE);

    public static final DeferredItem<CrookItem> CROOK = ITEMS.register("stone_crook",
            () -> new CrookItem(Tiers.STONE, new Item.Properties().attributes(
                    DiggerItem.createAttributes(Tiers.STONE, 2, -2.8F)
            ))
    );
    public static final DeferredItem<Item> STONE_ROD = simpleItem("stone_rod");

    //#region Block Items
    public static final DeferredItem<BlockItem> OAK_SLUICE = blockItem("oak_sluice", BlocksRegistry.OAK_SLUICE);
    public static final DeferredItem<BlockItem> IRON_SLUICE = blockItem("iron_sluice", BlocksRegistry.IRON_SLUICE);
    public static final DeferredItem<BlockItem> DIAMOND_SLUICE = blockItem("diamond_sluice", BlocksRegistry.DIAMOND_SLUICE);
    public static final DeferredItem<BlockItem> NETHERITE_SLUICE = blockItem("netherite_sluice", BlocksRegistry.NETHERITE_SLUICE);

    public static final DeferredItem<BlockItem> IRON_AUTO_HAMMER = blockItem("iron_auto_hammer", BlocksRegistry.IRON_AUTO_HAMMER);
    public static final DeferredItem<BlockItem> GOLD_AUTO_HAMMER = blockItem("gold_auto_hammer", BlocksRegistry.GOLD_AUTO_HAMMER);
    public static final DeferredItem<BlockItem> DIAMOND_AUTO_HAMMER = blockItem("diamond_auto_hammer", BlocksRegistry.DIAMOND_AUTO_HAMMER);
    public static final DeferredItem<BlockItem> NETHERITE_AUTO_HAMMER = blockItem("netherite_auto_hammer", BlocksRegistry.NETHERITE_AUTO_HAMMER);

    public static final DeferredItem<BlockItem> PUMP = blockItem("pump", BlocksRegistry.PUMP);

    public static final DeferredItem<BlockItem> DRIPPER = blockItem("dripper", BlocksRegistry.DRIPPER);

    public static final DeferredItem<BlockItem> FUSING_MACHINE = blockItem("fusing_machine", BlocksRegistry.FUSING_MACHINE);
    public static final DeferredItem<BlockItem> SUPER_COOLER = blockItem("super_cooler", BlocksRegistry.SUPER_COOLER);

    public static final DeferredItem<BlockItem> CAST_IRON_BLOCK = blockItem("cast_iron_block", BlocksRegistry.CAST_IRON_BLOCK);

    public static final DeferredItem<BlockItem> TUBE = blockItem("tube", BlocksRegistry.TUBE);
    public static final DeferredItem<BlockItem> JAR = blockItem("jar", BlocksRegistry.JAR);
    public static final DeferredItem<BlockItem> TEMPERED_JAR = blockItem("tempered_jar", BlocksRegistry.TEMPERED_JAR);
    public static final DeferredItem<BlockItem> AUTO_PROCESSING_BLOCK
            = blockItem("auto_processing_block", BlocksRegistry.JAR_AUTOMATER);
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

    private static DeferredItem<HammerItem> registerHammer(String name, Tiers tier) {
        return ITEMS.register(name, () -> new HammerItem(tier,
                new Item.Properties().attributes(DiggerItem.createAttributes(tier, 1.0F, -2.8F))
        ));
    }
}
