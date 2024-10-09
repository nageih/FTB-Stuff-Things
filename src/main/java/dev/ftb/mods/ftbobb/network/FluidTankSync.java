package dev.ftb.mods.ftbobb.network;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.AbstractMachineBlockEntity;
import dev.ftb.mods.ftbobb.client.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FluidTankSync(BlockPos pos, FluidStack fluidStack) implements CustomPacketPayload {
    public static final Type<FluidTankSync> TYPE = new Type<>(FTBOBB.id("fluid_tank_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidTankSync> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, FluidTankSync::pos,
            FluidStack.OPTIONAL_STREAM_CODEC, FluidTankSync::fluidStack,
            FluidTankSync::new
    );

    public static void handleData(FluidTankSync packet, IPayloadContext ctx) {
        ClientUtil.getBlockEntityAt(packet.pos, AbstractMachineBlockEntity.class)
                .ifPresent(holder -> holder.setFluid(packet.fluidStack));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
