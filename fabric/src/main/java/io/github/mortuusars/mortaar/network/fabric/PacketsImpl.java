package io.github.mortuusars.mortaar.network.fabric;

import io.github.mortuusars.mortaar.network.packet.Packet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class PacketsImpl {
    public static void sendToServer(Packet packet) {
        ClientPlayNetworking.send(packet);
    }

    public static void sendToClient(Packet packet, ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }
}
