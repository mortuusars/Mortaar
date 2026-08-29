package io.github.mortuusars.mortaar.bugger.network;

import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.bugger.BuggerData;
import io.github.mortuusars.mortaar.network.packet.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record ClientboundBuggerDataPacket(ResourceLocation id, CompoundTag data) implements Packet {
    public static final ResourceLocation ID = Mortaar.resource("bugger_data");
    public static final Type<ClientboundBuggerDataPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBuggerDataPacket> STREAM_CODEC = StreamCodec.composite(
          ResourceLocation.STREAM_CODEC, ClientboundBuggerDataPacket::id,
          ByteBufCodecs.COMPOUND_TAG, ClientboundBuggerDataPacket::data,
          ClientboundBuggerDataPacket::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public boolean handle(PacketFlow direction, Player player) {
        BuggerData.receive(id, data, player.registryAccess());
        return true;
    }
}
