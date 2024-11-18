package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.util.lootsummary.LootSummaryCollection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncLootSummaryPacket(LootSummaryCollection summary) implements CustomPacketPayload {
    public static final Type<SyncLootSummaryPacket> TYPE = new Type<>(FTBStuffNThings.id("sync_loot_summary"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLootSummaryPacket> STREAM_CODEC = StreamCodec.composite(
            LootSummaryCollection.STREAM_CODEC, SyncLootSummaryPacket::summary,
            SyncLootSummaryPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(SyncLootSummaryPacket packet, IPayloadContext context) {
        LootSummaryCollection.syncFromServer(packet.summary);
    }
}
