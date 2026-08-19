package io.github.mortuusars.seal.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import io.github.mortuusars.seal.Seal;
import io.github.mortuusars.seal.SealClient;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class SealFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SealClient.init();
        ConfigScreenFactoryRegistry.INSTANCE.register(Seal.ID, ConfigurationScreen::new);
    }
}
