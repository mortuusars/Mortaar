package io.github.mortuusars.mortaar.neoforge.event;

import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = Mortaar.ID, value = Dist.CLIENT)
public class NeoForgeClientEvents {
    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterStandalone event) {
        //TODO: seems standalone still being relevant
//        Platform.Client.CUSTOM_MODELS.forEach(event::register);
    }
}