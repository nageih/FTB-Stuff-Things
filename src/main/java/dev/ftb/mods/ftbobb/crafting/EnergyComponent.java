package dev.ftb.mods.ftbobb.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record EnergyComponent(int fePerTick, int ticksToProcess) {
    public static final Codec<EnergyComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("fe_per_tick").forGetter(EnergyComponent::fePerTick),
            ExtraCodecs.POSITIVE_INT.fieldOf("ticks_to_process").forGetter(EnergyComponent::ticksToProcess)
    ).apply(builder, EnergyComponent::new));

    public static final StreamCodec<FriendlyByteBuf, EnergyComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, EnergyComponent::fePerTick,
            ByteBufCodecs.VAR_INT, EnergyComponent::ticksToProcess,
            EnergyComponent::new
    );
}
