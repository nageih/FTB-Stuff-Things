package dev.ftb.mods.ftbobb.blocks.hammer;

import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import dev.ftb.mods.ftbobb.Config;
import dev.ftb.mods.ftbobb.items.HammerItem;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.BiFunction;

public enum AutoHammerProperties {
    IRON(ItemsRegistry.IRON_HAMMER, Config.IRON_HAMMER_SPEED, AutoHammerBlockEntity.Iron::new),
    GOLD(ItemsRegistry.GOLD_HAMMER , Config.GOLD_HAMMER_SPEED, AutoHammerBlockEntity.Gold::new),
    DIAMOND(ItemsRegistry.DIAMOND_HAMMER, Config.DIAMOND_HAMMER_SPEED, AutoHammerBlockEntity.Diamond::new),
    NETHERITE(ItemsRegistry.NETHERITE_HAMMER, Config.NETHERITE_HAMMER_SPEED, AutoHammerBlockEntity.Netherite::new);

    private final DeferredItem<HammerItem> hammerItem;
    private final IntValue hammerSpeed;
    private final BiFunction<BlockPos, BlockState, ? extends AutoHammerBlockEntity> beFactory;

    AutoHammerProperties(DeferredItem<HammerItem> hammerItem, IntValue hammerSpeed, BiFunction<BlockPos, BlockState, ? extends AutoHammerBlockEntity> beFactory) {
        this.hammerItem = hammerItem;
        this.hammerSpeed = hammerSpeed;
        this.beFactory = beFactory;
    }

    public Item getHammerItem() {
        return hammerItem.get();
    }

    public int getHammerSpeed() {
        return hammerSpeed.get();
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState blockState) {
        return beFactory.apply(pos, blockState);
    }
}
