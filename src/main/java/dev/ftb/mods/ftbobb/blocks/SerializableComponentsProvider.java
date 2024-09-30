package dev.ftb.mods.ftbobb.blocks;

import net.minecraft.core.component.DataComponentType;

import java.util.List;

/**
 * Implements on block entities which have data that needs to be serialized onto the dropped item.
 */
@FunctionalInterface
public interface SerializableComponentsProvider {
    void addSerializableComponents(List<DataComponentType<?>> list);
}
