package io.github.mortuusars.mortaar.neoforge;

import io.github.mortuusars.mortaar.Config;
import io.github.mortuusars.mortaar.Mortaar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Mortaar.ID)
public class MortaarNeoForge {
    public MortaarNeoForge(ModContainer container) {
        Mortaar.init();

        container.registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            MortaarNeoForgeClient.init(container);
        }
    }
}