package io.github.mortuusars.mortaar.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.MortaarClient;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class MortaarFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MortaarClient.init();
        ConfigScreenFactoryRegistry.INSTANCE.register(Mortaar.ID, ConfigurationScreen::new);
    }
}
