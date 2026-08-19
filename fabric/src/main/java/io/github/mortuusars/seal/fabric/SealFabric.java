package io.github.mortuusars.seal.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import io.github.mortuusars.seal.Config;
import io.github.mortuusars.seal.Seal;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class SealFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Seal.init();
        NeoForgeConfigRegistry.INSTANCE.register(Seal.ID, ModConfig.Type.COMMON, Config.Common.SPEC);
    }
}
