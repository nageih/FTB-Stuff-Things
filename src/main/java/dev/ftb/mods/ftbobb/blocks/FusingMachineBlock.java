package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.registry.ComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FusingMachineBlock extends AbstractMachineBlock implements SerializableComponentsProvider {
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FusingMachineBlockEntity(pos, state);
    }

    @Override
    public void addSerializableComponents(List<DataComponentType<?>> list) {
        list.add(ComponentsRegistry.STORED_FLUID.get());
        list.add(ComponentsRegistry.STORED_ENERGY.get());
    }
}
