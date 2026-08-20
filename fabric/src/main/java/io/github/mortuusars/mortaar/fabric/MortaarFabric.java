package io.github.mortuusars.mortaar.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import io.github.mortuusars.mortaar.Config;
import io.github.mortuusars.mortaar.Mortaar;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class MortaarFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Mortaar.init();
        NeoForgeConfigRegistry.INSTANCE.register(Mortaar.ID, ModConfig.Type.COMMON, Config.Common.SPEC);
    }
}
