package io.github.mortuusars.mortaar.neoforge.event;

import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.command.MortaarCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Mortaar.ID)
public class NeoForgeCommonEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        MortaarCommand.register(event.getDispatcher(), event.getBuildContext());
    }
}