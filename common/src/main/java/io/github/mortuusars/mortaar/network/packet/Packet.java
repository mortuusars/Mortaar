package io.github.mortuusars.mortaar.network.packet;

import io.github.mortuusars.mortaar.network.Packets;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface Packet extends CustomPacketPayload {
    boolean handle(PacketFlow flow, Player player);

    default void sendToServer() {
        Packets.sendToServer(this);
    }

    default void sendToClient(ServerPlayer player) {
        Packets.sendToClient(this, player);
    }

    default void sendToAllClients() {
        Packets.sendToAllClients(this);
    }

    default void sendToOtherClients(@NotNull ServerPlayer except) {
        Packets.sendToOtherClients(this, except);
    }

    default void sendToClients(Predicate<ServerPlayer> filter) {
        Packets.sendToClients(this, filter);
    }

    default void sendToPlayersNear(ServerLevel level, @Nullable ServerPlayer excluded,
                                         Entity entity, double radius) {
        Packets.sendToPlayersNear(this, level, excluded, entity, radius);
    }

    default void sendToPlayersNear(@NotNull ServerLevel level, @Nullable ServerPlayer excludedPlayer,
                                         double x, double y, double z, double radius) {
        Packets.sendToPlayersNear(this, level, excludedPlayer, x, y, z, radius);
    }
}
