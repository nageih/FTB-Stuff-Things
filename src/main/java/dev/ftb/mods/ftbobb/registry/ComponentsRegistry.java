package dev.ftb.mods.ftbobb.registry;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbobb.FTBOBB;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ComponentsRegistry {
    private static final DeferredRegister.DataComponents COMPONENTS
            = DeferredRegister.createDataComponents(FTBOBB.MODID);

    public static final Supplier<DataComponentType<SimpleFluidContent>> STORED_FLUID
            = register("stored_fluid", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC);

    public static void init(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }

    private static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return COMPONENTS.registerComponentType(name, builder -> builder
                .persistent(codec)
                .networkSynchronized(streamCodec)
        );
    }
}
