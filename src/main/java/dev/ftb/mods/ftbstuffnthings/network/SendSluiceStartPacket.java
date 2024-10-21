package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlockEntity;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SendSluiceStartPacket(BlockPos pos, int processingTime) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendSluiceStartPacket> TYPE = new CustomPacketPayload.Type<>(FTBStuffNThings.id("send_sluice_start"));

    public static final StreamCodec<FriendlyByteBuf, SendSluiceStartPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SendSluiceStartPacket::pos,
            ByteBufCodecs.VAR_INT, SendSluiceStartPacket::processingTime,
            SendSluiceStartPacket::new
    );

    public static SyncDisplayItemPacket forSluice(AbstractMachineBlockEntity machine) {
        return new SyncDisplayItemPacket(machine.getBlockPos(), machine.getItemHandler().getStackInSlot(0).copy());
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(SendSluiceStartPacket packet, IPayloadContext context) {
        ClientUtil.getBlockEntityAt(packet.pos, SluiceBlockEntity.class)
                .ifPresent(holder -> holder.syncProcessingTimeFromServer(packet.processingTime));
    }
}
