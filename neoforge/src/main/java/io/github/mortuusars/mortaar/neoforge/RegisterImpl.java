package io.github.mortuusars.mortaar.neoforge;

import io.github.mortuusars.mortaar.Registrar;
import io.github.mortuusars.mortaar.network.packet.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public class RegisterImpl {
    public static final Map<String, RegistrarNeoForge> REGISTRARS = new HashMap<>();

    public static Registrar registrar(String modId) {
        return REGISTRARS.computeIfAbsent(modId, RegistrarNeoForge::new);
    }

    // --

    @ApiStatus.Internal
    public static final List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends Packet>> serverboundPackets = new ArrayList<>();
    @ApiStatus.Internal
    public static final List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends Packet>> clientboundPackets = new ArrayList<>();
    @ApiStatus.Internal
    public static final List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends Packet>> bidirectionalPackets = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public static void serverboundPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        serverboundPackets.add(new CustomPacketPayload.TypeAndCodec<>((CustomPacketPayload.Type<Packet>) type, (StreamCodec<FriendlyByteBuf, Packet>) codec));
    }

    @SuppressWarnings("unchecked")
    public static void clientboundPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        clientboundPackets.add(new CustomPacketPayload.TypeAndCodec<>((CustomPacketPayload.Type<Packet>) type, (StreamCodec<FriendlyByteBuf, Packet>) codec));
    }

    @SuppressWarnings("unchecked")
    public static void bidirectionalPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        bidirectionalPackets.add(new CustomPacketPayload.TypeAndCodec<>((CustomPacketPayload.Type<Packet>) type, (StreamCodec<FriendlyByteBuf, Packet>) codec));
    }
}
