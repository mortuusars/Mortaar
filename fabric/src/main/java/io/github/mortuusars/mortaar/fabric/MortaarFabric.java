package io.github.mortuusars.mortaar.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import io.github.mortuusars.mortaar.Config;
import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.command.MortaarCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.neoforged.fml.config.ModConfig;

public class MortaarFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Mortaar.init();
        NeoForgeConfigRegistry.INSTANCE.register(Mortaar.ID, ModConfig.Type.SERVER, Config.Server.SPEC);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            MortaarCommand.register(dispatcher, registryAccess);
        });
    }
}
