package io.github.mortuusars.mortaar.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.MortaarClient;
import io.github.mortuusars.mortaar.Platform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class MortaarFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MortaarClient.init();
        ConfigScreenFactoryRegistry.INSTANCE.register(Mortaar.ID, ConfigurationScreen::new);

        ModelLoadingPlugin.register(context -> context.addModels(
              Platform.Client.CUSTOM_MODELS.stream().map(ModelResourceLocation::id).toArray(ResourceLocation[]::new)
        ));
    }
}
