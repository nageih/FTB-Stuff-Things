package dev.ftb.mods.ftbobb.blocks;

import net.minecraft.core.component.DataComponentType;

import java.util.List;

/**
 * Implement this on blocks which have data that needs to be serialized onto the dropped item. This is for the
 * purpose of block loot table data generation; the declared components for each block must also be handled via
 * applyImplicitComponents() and collectImplicitComponents() in the corresponding block entity.
 */
@FunctionalInterface
public interface SerializableComponentsProvider {
    void addSerializableComponents(List<DataComponentType<?>> list);
}
