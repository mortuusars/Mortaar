package io.github.mortuusars.mortaar.fabric;

import io.github.mortuusars.mortaar.Registrar;
import io.github.mortuusars.mortaar.network.packet.Packet;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;

public class RegisterImpl {
    public static final Map<String, RegistrarFabric> REGISTRARS = new HashMap<>();

    public static Registrar registrar(String modId) {
        return REGISTRARS.computeIfAbsent(modId, RegistrarFabric::new);
    }

    // --

    @SuppressWarnings("unchecked")
    public static void serverboundPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        PayloadTypeRegistry.playC2S().register(
              (CustomPacketPayload.Type<Packet>) type, (StreamCodec<FriendlyByteBuf, Packet>) codec);
        ServerPlayNetworking.registerGlobalReceiver(
              (CustomPacketPayload.Type<Packet>) type, (payload, context) -> payload.handle(PacketFlow.SERVERBOUND, context.player()));
    }

    @SuppressWarnings("unchecked")
    public static void clientboundPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        PayloadTypeRegistry.playS2C().register(
              (CustomPacketPayload.Type<Packet>) type, (StreamCodec<FriendlyByteBuf, Packet>) codec);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Client.registerPacketReceiver((CustomPacketPayload.Type<Packet>) type);
        }
    }

    @SuppressWarnings("unchecked")
    public static void bidirectionalPacket(CustomPacketPayload.Type<? extends Packet> type, StreamCodec<? extends FriendlyByteBuf, ? extends Packet> codec) {
        PayloadTypeRegistry.playC2S().register(
              (CustomPacketPayload.Type<Packet>) type, (StreamCodec<FriendlyByteBuf, Packet>) codec);
        ServerPlayNetworking.registerGlobalReceiver(
              (CustomPacketPayload.Type<Packet>) type, (payload, context) -> payload.handle(PacketFlow.SERVERBOUND, context.player()));
        PayloadTypeRegistry.playS2C().register(
              (CustomPacketPayload.Type<Packet>) type, (StreamCodec<FriendlyByteBuf, Packet>) codec);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Client.registerPacketReceiver((CustomPacketPayload.Type<Packet>) type);
        }
    }

    /**
     * Putting code in the inner class prevents classloading on the wrong environment.
     */
    private static class Client {
        public static void registerPacketReceiver(CustomPacketPayload.Type<Packet> type) {
            ClientPlayNetworking.registerGlobalReceiver(
                  type, (payload, context) -> payload.handle(PacketFlow.CLIENTBOUND, context.player()));
        }
    }
}
