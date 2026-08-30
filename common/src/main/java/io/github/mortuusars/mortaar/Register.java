package io.github.mortuusars.mortaar;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.mortuusars.mortaar.network.packet.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class Register {
    /**
     * Gets or creates mod-specific registrar, that allows for easy registering of blocks, items, block-entities, menu-types, etc.,
     * without needing to handle platform specific code,
     * @return First call per modId will create the registrar, subsequent calls will return the same instance.
     */
    @ExpectPlatform
    public static Registrar registrar(String modId) {
        throw new AssertionError();
    }

    // --

    /**
     * Registers serverbound (play to server) packet.
     */
    @ExpectPlatform
    public static void serverboundPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        throw new AssertionError();
    }

    /**
     * Registers clientbound (play to client) packet.
     */
    @ExpectPlatform
    public static void clientboundPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        throw new AssertionError();
    }

    /**
     * Registers bidirectional (play to server and play to client) packet.
     */
    @ExpectPlatform
    public static void bidirectionalPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        throw new AssertionError();
    }
}
