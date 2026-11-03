package io.github.mortuusars.mortaar.network.neoforge;

import io.github.mortuusars.mortaar.network.packet.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("unused")
public class PacketsImpl {
    public static void sendToServer(Packet packet) {
        Client.sendToServer(packet);
    }

    public static void sendToClient(Packet packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    private static class Client {
        public static void sendToServer(Packet packet) {
            ClientPacketDistributor.sendToServer(packet);
        }
    }
}