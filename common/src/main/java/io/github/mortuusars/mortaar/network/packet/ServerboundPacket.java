package io.github.mortuusars.mortaar.network.packet;

import io.github.mortuusars.mortaar.Mortaar;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface ServerboundPacket extends Packet {
    boolean handle(PacketFlow flow, ServerPlayer player);

    @Override
    default boolean handle(PacketFlow flow, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            Mortaar.LOGGER.error("Cannot handle '{}' packet. Player is not a ServerPlayer, but {}", type().id(), player.getClass().getName());
            return false;
        }
        return handle(flow, serverPlayer);
    }
}
