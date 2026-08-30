package io.github.mortuusars.mortaar.neoforge.event;

import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.command.MortaarCommand;
import io.github.mortuusars.mortaar.neoforge.RegisterImpl;
import io.github.mortuusars.mortaar.neoforge.RegistrarNeoForge;
import io.github.mortuusars.mortaar.network.packet.Packet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Map;

@EventBusSubscriber(modid = Mortaar.ID)
public class NeoForgeCommonEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Querying the stat properly "registers" it or something, idk
            for (Map.Entry<String, RegistrarNeoForge> entry : RegisterImpl.REGISTRARS.entrySet()) {
                for (Map.Entry<ResourceLocation, StatFormatter> statEntry : entry.getValue().STATS.entrySet()) {
                    Stats.CUSTOM.get(statEntry.getKey(), statEntry.getValue());
                }
            }
        });
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        MortaarCommand.register(event.getDispatcher(), event.getBuildContext());
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        for (var definition : RegisterImpl.serverboundPackets) {
            registrar.playToServer((CustomPacketPayload.Type<Packet>) definition.type(),
                  (StreamCodec<? super RegistryFriendlyByteBuf, Packet>) definition.codec(),
                  (packet, context) -> packet.handle(context.flow(), context.player()));
        }

        for (var definition : RegisterImpl.clientboundPackets) {
            registrar.playToClient((CustomPacketPayload.Type<Packet>) definition.type(),
                  (StreamCodec<? super RegistryFriendlyByteBuf, Packet>) definition.codec(),
                  (packet, context) -> packet.handle(context.flow(), context.player()));
        }

        for (var definition : RegisterImpl.bidirectionalPackets) {
            registrar.playBidirectional((CustomPacketPayload.Type<Packet>) definition.type(),
                  (StreamCodec<? super RegistryFriendlyByteBuf, Packet>) definition.codec(),
                  (packet, context) -> packet.handle(context.flow(), context.player()));
        }
    }
}