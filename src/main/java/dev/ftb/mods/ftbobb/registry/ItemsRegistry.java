package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.items.MeshItem;
import dev.ftb.mods.ftbobb.items.MeshType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FTBOBB.MODID);

    public static final DeferredItem<MeshItem> CLOTH_MESH = ITEMS.register("cloth_mesh", () -> new MeshItem(MeshType.CLOTH));
    public static final DeferredItem<MeshItem> IRON_MESH = ITEMS.register("iron_mesh", () -> new MeshItem(MeshType.IRON));
    public static final DeferredItem<MeshItem> GOLD_MESH = ITEMS.register("gold_mesh", () -> new MeshItem(MeshType.GOLD));
    public static final DeferredItem<MeshItem> DIAMOND_MESH = ITEMS.register("diamond_mesh", () -> new MeshItem(MeshType.DIAMOND));

    //#region Block Items
    public static final DeferredItem<BlockItem> OAK_SLUICE = ITEMS.register("oak_sluice", () -> new BlockItem(BlocksRegistry.OAK_SLUICE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> IRON_SLUICE = ITEMS.register("iron_sluice", () -> new BlockItem(BlocksRegistry.IRON_SLUICE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DIAMOND_SLUICE = ITEMS.register("diamond_sluice", () -> new BlockItem(BlocksRegistry.DIAMOND_SLUICE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> NETHERITE_SLUICE = ITEMS.register("netherite_sluice", () -> new BlockItem(BlocksRegistry.NETHERITE_SLUICE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> PUMP = ITEMS.register("pump", () -> new BlockItem(BlocksRegistry.PUMP.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> TUBE = ITEMS.register("tube", () -> new BlockItem(BlocksRegistry.TUBE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> JAR = ITEMS.register("jar", () -> new BlockItem(BlocksRegistry.JAR.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> TEMPERED_JAR = ITEMS.register("tempered_jar", () -> new BlockItem(BlocksRegistry.TEMPERED_JAR.get(), new Item.Properties()));
    //#endregion

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }
}
